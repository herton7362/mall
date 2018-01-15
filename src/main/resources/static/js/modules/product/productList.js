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
                    {field:'sortNumber', title:'排序'},
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
                productProductStandards: [],
                sortNumber: null
            },
            selectedProductStandards: {
                data: []
            }
        },
        methods: {
            modalOpen: function() {
                var $form = this.crudgrid.$instance.getForm();
                if($form.id) {
                    this.selectedProductStandards.data = this.getSelectedProductStandards($form.productCategory.id);
                    $.each(this.selectedProductStandards.data, function (i) {
                        if(!$form.productProductStandards[i]) {
                            $form.productProductStandards.push({productStandardItems: []});
                        }
                    });
                    return;
                }
                var selectedId = this.sidebar.$instance.getSelectedId();
                if(selectedId) {
                    $form.productCategory = {
                        id: selectedId,
                        productStandards: this.getSelectedProductStandards(selectedId)
                    };
                    this.comboboxChange(selectedId);
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
                        productStandardItems:  [].concat(this.items)
                    })
                });
                this.makeSkus();
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
                var $form = this.crudgrid.$instance.getForm();
                if($form.productProductStandards.length <= 0) {
                    return 0;
                }
                var count = $form.productProductStandards.length;
                var total = $form.productProductStandards[0].productStandardItems.length;
                for(var i = 1, l = count; i < l; i++) {
                    total = total * $form.productProductStandards[i].productStandardItems.length;
                }
                return total;
            },
            increaseProductStandardsIndex: function (hex, index) {
                var $form = this.crudgrid.$instance.getForm();
                if(hex[index] + 1
                    >= $form.productProductStandards[index].productStandardItems.length) {
                    if(index > 0) {
                        this.increaseProductStandardsIndex(hex, index - 1);
                    }
                    hex[index] = 0;
                } else {
                    hex[index] += 1;
                }
            },
            matchSku: function (skus, productStandardItems) {
                function compare(s1, s2) {
                    if (s1.length !== s2.length) {
                        return false;
                    }
                    for (var i = 0, l = s1.length; i < l; i++) {
                        if (s1[i].id !== s2[i].id) {
                            return false;
                        }
                    }
                    return true;
                }
                for(var i = 0, l = skus.length; i < l; i++) {
                    if(compare(skus[i].productStandardItems, productStandardItems)) {
                        return skus[i];
                    }
                }
                return null;
            },
            makeSkus: function () {
                var $form = this.crudgrid.$instance.getForm();
                var skus = [];
                var productStandardItems;
                var hex = [];
                var oldSku;
                $.each($form.productProductStandards, function (i) {
                    hex[i] = 0;
                });
                if(!$form.skus) {
                    Vue.set($form, 'skus', []);
                }
                for(var i = 0, l = this.getSkuTotalLength(); i < l; i++) {
                    productStandardItems = [];
                    for(var j = 0, jl = $form.productProductStandards.length; j < jl; j++) {
                        productStandardItems.push($form.productProductStandards[j].productStandardItems[hex[j]]);
                    }
                    this.increaseProductStandardsIndex(hex, jl - 1);
                    oldSku = this.matchSku($form.skus, productStandardItems);
                    if(oldSku) {
                        skus[i] = oldSku;
                    } else {
                        skus[i] = {
                            productStandardItems: productStandardItems,
                            price: null,
                            stockCount: 0,
                            isDefault: false,
                            coverImage: null
                        }
                    }
                }

                $form.skus.splice(0);
                $.each(skus, function () {
                    $form.skus.push(this)
                })
            }
        },
        mounted: function() {
            var self = this;
            this.loadSidebar(function() {
                this.sidebarClick(this.sidebar.root);
            });
            this.crudgrid.$instance.$watch('form.productProductStandards', function () {
                self.makeSkus();
            }, {
                deep: true
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