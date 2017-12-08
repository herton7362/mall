require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            datagrid: {
                $instance: {},
                queryParams: {
                    maxStockCount: '100'
                },
                columns: [
                    {field:'coverImage', title:'封面', formatter: function(value) {
                        if(value) {
                            return '<img width="50" src="'+utils.patchUrl('/attachment/download/' + value.id)+'">';
                        } else {
                            return '';
                        }
                    }},
                    {field:'productCategory.name', title:'分类'},
                    {field:'name', title:'名称'},
                    {field:'stockCount', title:'库存数量'}
                ],
                url: utils.patchUrl('/api/product/stock')
            }
        },
        methods: {
            clearSearch: function () {
                this.datagrid.queryParams.maxStockCount = '';
                this.datagrid.$instance.reload();
            },
            onLoaded: function (content) {
                var self = this;
                $.each(content, function (i) {
                    if(this.skus && this.skus.length > 0) {
                        $.each(this.skus, function (j) {
                            var skuName = '';
                            $.each(this.productStandardItems, function () {
                                skuName += this.name;
                            });
                            if(this.stockCount > self.datagrid.queryParams.maxStockCount) {
                                return;
                            }
                            content.splice(i + j + 1, 0, {
                                coverImage: this.coverImage,
                                name: '<span style="color: rgb(153, 153, 153);">--'+skuName+'</span>',
                                productCategory: {
                                    name: ''
                                },
                                stockCount: this.stockCount
                            })
                        })
                    }
                })
            }
        },
        mounted: function() {
            this.datagrid.$instance.load();
        }
    });
});