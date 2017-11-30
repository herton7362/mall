require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                queryParams: {
                    name: ''
                },
                columns: [
                    {field:'coverImage', title:'封面', formatter: function(value) {
                        return '<img width="50" src="'+utils.patchUrl('/attachment/download/' + value.id)+'">';
                     }},
                    {field:'productCategory.name', title:'分类'},
                    {field:'name', title:'名称'},
                    {field:'remark', title:'备注'},
                    {field:'points', title:'积分'},
                    {field:'price', title:'价格', formatter: function(value) {
                        return utils.formatMoney(value);
                    }}
                ]
            },
            sidebar: {
                $instance: {},
                root: {
                    id: null,
                    name: '所有类别',
                    open: true,
                    alwaysExpended: true
                },
                selectedId: null,
                data: []
            },
            productCategory: {
                data: []
            },
            formData: {
                id: '',
                name: null,
                remark: null,
                points: null,
                price: null,
                productCategory: {
                    productStandards: []
                },
                coverImage: null,
                styleImages: [],
                detailImages: []
            },
            selectedProductStandards: {
                data: [],
                items: [],
                index: []
            }

        },
        watch: {
            'selectedProductStandards.data': function (val) {
                var self = this;
                this.selectedProductStandards.items.splice(0);
                $.each(val, function (i) {
                    self.selectedProductStandards.items[i] = [];
                })

            },
            deep: true
        },
        methods: {
            modalOpen: function() {
                var $form = this.crudgrid.$instance.getForm();
                if($form.id) {
                    this.selectedProductStandards.data = this.getSelectedProductStandards($form.productCategory.id);
                    return;
                }
                var selectedId = this.sidebar.$instance.getSelectedId();
                if(selectedId) {
                    $form.productCategory = {
                        id: selectedId,
                        productStandards: this.getSelectedProductStandards(selectedId)
                    };
                    this.selectedProductStandards.data = this.getSelectedProductStandards(selectedId);
                } else {
                    $form.productCategory = {};
                }
            },
            loadSidebar: function(callback) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/productCategory'),
                    data: {
                        sort:'sortNumber,updatedDate',
                        order: 'asc,desc'
                    },
                    success: function(data) {
                        self.sidebar.data = data.content;

                        if(callback instanceof Function) {
                            callback.call(self, data.content);
                        }
                    }
                });
            },
            sidebarClick: function(row) {
                var param = {};
                if(row.id !== null) {
                    param = {'productCategory.id': row.id};
                }
                this.crudgrid.$instance.load(param);
            },
            comboboxChange: function (selectedId) {
                this.selectedProductStandards.data = this.getSelectedProductStandards(selectedId);
            },
            hasStandard: function () {
                var productStandards = this.selectedProductStandards.data;
                return productStandards && productStandards.length > 0;
            },
            getSelectedProductStandards: function (productCategoryId) {
                var productCategory = {};
                $.each(this.productCategory.data, function () {
                    if(this.id === productCategoryId) {
                        productCategory = this;
                    }
                });
                return productCategory.productStandards;
            },
            getSkuTotalLength: function (selectedProductStandards) {
                if(selectedProductStandards.items.length <= 0) {
                    return 0;
                }
                var count = selectedProductStandards.items.length;
                var total = selectedProductStandards.items[0].length;
                for(var i = 1, l = count; i < l; i++) {
                    total = total * selectedProductStandards.items[i].length;
                }
                return total;
            }
        },
        mounted: function() {
            var self = this;
            this.loadSidebar(function() {
                this.sidebarClick(this.sidebar.root);
            });
            $.ajax({
                url: utils.patchUrl('/api/productCategory'),
                data: {
                    sort: 'sortNumber,updatedDate',
                    order: 'asc,desc'
                },
                success: function(data) {
                    self.productCategory.data = data.content;
                }
            })
        }
    });
});