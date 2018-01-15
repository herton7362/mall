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
            products: [],
            currentPage: 1,
            pageSize: 15,
            totalPage: 0,
            searchKey: null
        },
        filters: {
            coverPath: function (val) {
                return utils.patchUrl('/attachment/download/' + val);
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
                    return utils.formatMoney(min) + '-' +  utils.formatMoney(max);;
                }
                return utils.formatMoney(val.price);
            }
        },
        methods: {
            tabClick: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/product'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc',
                        name: this.searchKey,
                        currentPage: this.currentPage,
                        pageSize: this.pageSize
                    },
                    success: function(data) {
                        self.products = data.content;
                        self.totalPage = parseInt(data.totalElements / self.pageSize) + 1;
                    }
                });
            },
            prev: function () {
                this.currentPage = this.currentPage - 1;
                this.tabClick();
            },
            next: function () {
                this.currentPage = this.currentPage + 1;
                this.tabClick();
            },
            productDetail: function (row) {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/product/detail?id=' + row.id);
            },
            addCart: function (product) {
                productsheet.open(product);
            },
            search: function () {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/index/search-list?key=' + this.searchKey)
            }
        },
        mounted: function () {
            this.searchKey = utils.getQueryString('key');
            this.tabClick();
        }
    });
})