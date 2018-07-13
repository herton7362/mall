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
                    {field:'image', title:'图片', formatter: function(value) {
                            return '<img width="50" src="'+utils.patchUrl('/attachment/download/' + value.id)+'">';
                        }},
                    {field:'product.name', title:'商品'},
                    {field:'url', title:'跳转url地址'},
                    {field:'remark', title:'备注'}
                ]
            },
            formData: {
                id: null,
                image: null,
                productId: null,
                url: null,
                remark: null
            },
            products: []
        },
        methods: {
            tableTransformResponse: function (data) {
                var self = this;
                $.each(data, function (k, row) {
                    row.product = {name: '无'};
                    $.each(self.products, function () {
                        if(row.productId === this.id)
                            row.product = this;
                    })
                });
                return data;
            }
        },
        mounted: function() {
            var self = this;
            $.ajax({
                url: utils.patchUrl('/api/product'),
                data: {
                    sort: 'sortNumber,updatedDate',
                    order: 'asc,desc'
                },
                success: function(data) {
                    self.products = data.content;
                    self.crudgrid.$instance.load();
                }
            });
        }
    });
});
