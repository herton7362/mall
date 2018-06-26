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
            productCategories: [],// 四个一组
            categoryMore: false,
            newest: [],
            recommend: []
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
            loadProductCategory: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/productCategory'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc'
                    },
                    success: function(data) {
                        var result = [];
                        for(var i=0,len=data.content.length;i<len;i+=4){
                            result.push(data.content.slice(i,i+4));
                        }
                        self.productCategories = result;
                    }
                })
            },
            loadProducts: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/product'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc',
                        currentPage: 1,
                        pageSize: 10
                    },
                    success: function(data) {
                        self.products = data.content;
                        for (var i = 0, l = data.content.length; i < l; i++) {
                            if(self.newest.length > 3) {
                                break;
                            }
                            if(data.content[i].newest) {
                                self.newest.push(data.content[i]);
                            }
                        }
                        for (var i = 0, l = data.content.length; i < l; i++) {
                            if(self.recommend.length > 5) {
                                break;
                            }
                            if(data.content[i].recommend) {
                                self.recommend.push(data.content[i]);
                            }
                        }
                    }
                });
            },
            productDetail: function (row) {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/product/detail?id=' + row.id);
            },
            more: function (id) {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/product/all'+ (id? '?categoryId=' + id: ''));
            },
            addCart: function (product) {
                productsheet.open(product);
            }
        },
        mounted: function () {
            var username, token;
            token = utils.getQueryString("token");
            username = utils.getQueryString("username");
            if(token && username) {
                $.ajax({
                    url: utils.patchUrlPrefixUrl('/token/login'),
                    data: {
                        appId: 'tonr',
                        appSecret: 'secret',
                        username: username,
                        token: token
                    },
                    type: 'POST',
                    success: function(data) {
                        window.localStorage.accessToken = data['access_token'];
                        window.localStorage.refreshToken = data['refresh_token'];
                        window.localStorage.expiration = new Date().getTime() + ((data['expires_in'] / 2) * 1000);
                    }
                })
            }
            this.loadProductCategory();
            this.loadProducts();
        }
    });
})
