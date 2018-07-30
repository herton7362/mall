require([
    'jquery',
    'vue',
    'utils',
    'messager',
    _appConf.ctx + '/static/js/wechat/product/actionsheet/productsheet.js'
], function ($, Vue, utils, messager, productsheet) {
    new Vue({
        el: '#content',
        data: {
            productCategories: [],
            products: {},
            activeId: null,
            currentPage: 1,
            pageSize: 30,
            totalPage: 0,
            flatProducts: []
        },
        filters: {
            coverPath: function (val) {
                if(val)
                    return utils.patchUrl('/attachment/download/' + val.id);
            },
            price: function (val) {
                if(val.skus && val.skus.length > 0) {
                    var min = 999999999999;
                    var max = 0;
                    $.each(val.skus, function () {
                        if(min > this.price) {
                            min = this.price;
                        }
                        if(max < this.price) {
                            max = this.price;
                        }
                    });
                    if(min === max) {
                        return utils.formatMoney(min);
                    }
                    return utils.formatMoney(min) + '-' +  utils.formatMoney(max);
                }
                return utils.formatMoney(val.price);
            }
        },
        methods: {
            loadProductCategory: function (callback) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/productCategory'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc'
                    },
                    success: function(data) {
                        self.productCategories = data.content;
                        if(callback instanceof Function) {
                            callback.call(self, data.content);
                        }
                    }
                })
            },
            tabClick: function (id) {
                var self = this;
                this.activeId = id;
                $.ajax({
                    url: utils.patchUrl('/product'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc',
                        'productCategory.id': id,
                        currentPage: this.currentPage,
                        pageSize: this.pageSize
                    },
                    success: function(data) {
                        Vue.set(self.products, id, data.content);
                        self.totalPage = parseInt(data.totalElements / self.pageSize) + 1;
                    }
                });
            },
            loadProduct: function (data) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/product'),
                    data: $.extend({
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc',
                    }, data),
                    success: function(data) {
                        self.flatProducts = data.content;
                    }
                });
            },
            prev: function () {
                this.currentPage = this.currentPage - 1;
                this.tabClick(this.activeId);
            },
            next: function () {
                this.currentPage = this.currentPage + 1;
                this.tabClick(this.activeId);
            },
            productDetail: function (row) {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/product/detail?id=' + row.id);
            },
            addCart: function (product) {
                productsheet.open(product);
            }
        },
        mounted: function () {
            var id = utils.getQueryString("categoryId");
            var keyword = utils.getQueryString("keyword");
            if(id === 'newest') {
                this.loadProduct({
                    newest: true
                });
            } else if(id === 'recommend') {
                this.loadProduct({
                    recommend: true
                });
            } else if(keyword) {
                this.loadProduct({
                    name: keyword
                });
            } else {
                this.loadProductCategory(function(data) {
                    this.tabClick(id? id: data[0].id);
                });
            }

        }
    });
});
