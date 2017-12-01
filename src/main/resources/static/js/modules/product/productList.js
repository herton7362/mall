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
                detailImages: [],
                productProductStandards: []
            },
            selectedProductStandards: {
                data: [],
                items: [],
                index: []
            },
            skus: []
        },
        watch: {
            'selectedProductStandards.data': function (val) {
                var self = this;
                this.selectedProductStandards.items.splice(0);
                $.each(val, function (i) {
                    self.selectedProductStandards.index[i] = 0;
                });
            },
            'selectedProductStandards.items': function (val) {

                this.makeSkus();
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
                var productStandards = this.getSelectedProductStandards(selectedId);
                var $form = this.crudgrid.$instance.getForm();
                this.selectedProductStandards.data = productStandards;
                $form.productProductStandards = [];
                $.each(productStandards, function () {
                    $form.productProductStandards.push({
                        productStandard: this,
                        productStandardItems: this.items
                    })
                });
            },
            hasStandard: function () {
                if(!this.crudgrid.$instance.getForm) {
                    return false;
                }
                var $form = this.crudgrid.$instance.getForm();
                var productStandards = $form.productProductStandards;
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
            getSkuTotalLength: function () {
                if(this.selectedProductStandards.items.length <= 0) {
                    return 0;
                }
                var count = this.selectedProductStandards.items.length;
                var total = this.selectedProductStandards.items[0].length;
                for(var i = 1, l = count; i < l; i++) {
                    total = total * this.selectedProductStandards.items[i].length;
                }
                return total;
            },
            increaseProductStandardsIndex: function (index) {
                if(this.selectedProductStandards.index[index] + 1
                    >= this.selectedProductStandards.items[index].length) {
                    if(index > 0) {
                        this.increaseProductStandardsIndex(index - 1);
                    }
                    this.selectedProductStandards.index[index] = 0;
                } else {
                    this.selectedProductStandards.index[index] += 1;
                }
            },
            makeSkus: function () {
                this.skus = [];
                var productStandardItems;
                for(var i = 0, l = this.getSkuTotalLength(); i < l; i++) {
                    productStandardItems = [];
                    for(var j = 0, jl = this.selectedProductStandards.items.length; j < jl; j++) {
                        productStandardItems.push(this.selectedProductStandards.items[j][this.selectedProductStandards.index[j]]);
                    }
                    this.increaseProductStandardsIndex(jl - 1);
                    this.skus[i] = {
                        productStandardItems: productStandardItems,
                        price: null,
                        stockCount: 0,
                        isDefault: i === 0,
                        coverImage: {}
                    }
                }
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