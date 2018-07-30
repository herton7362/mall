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
            recommend: [],
            carouselImgs: [],
            bannerImgs: []
        },
        filters: {
            coverPath: function (val) {
                if(val) {
                    return utils.patchUrl('/attachment/download/' + val.id);
                } else {
                    return null;
                }
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
                    }
                });
            },
            loadNewest: function() {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/product'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc',
                        currentPage: 1,
                        pageSize: 3,
                        newest: true
                    },
                    success: function(data) {
                        self.newest = data.content;
                    }
                });
            },
            loadRecommend: function() {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/product'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc',
                        currentPage: 1,
                        pageSize: 3,
                        recommend: true
                    },
                    success: function(data) {
                        self.recommend = data.content;
                    }
                });
            },
            productDetail: function (row) {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/product/detail?id=' + row.id);
            },
            more: function (id) {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/product/all'+ (id? '?categoryId=' + id: ''));
            },
            clickAdvertisement: function(img) {
                if(img.productId) {
                    this.productDetail({id: img.productId});
                } else if(img.url) {
                    window.location.href = img.url;
                }
            },
            addCart: function (product) {
                productsheet.open(product);
            },
            loadCarouselImg: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/carouselImg'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc',
                        currentPage: 1,
                        pageSize: 5
                    },
                    success: function (data) {
                        self.carouselImgs = data.content;
                    }
                });
            },
            loadBannerImg: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/bannerImg'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc',
                        currentPage: 1,
                        pageSize: 5
                    },
                    success: function (data) {
                        self.bannerImgs = data.content;
                    }
                });
            },
            initSearchButton: function () {

                $(function(){
                    var $searchBar = $('#searchBar'),
                        $searchText = $('#searchText'),
                        $searchInput = $('#searchInput'),
                        $searchClear = $('#searchClear'),
                        $searchCancel = $('#searchCancel');

                    function cancelSearch(){
                        $searchBar.removeClass('weui-search-bar_focusing');
                        $searchText.show();
                    }

                    $searchText.on('click', function(){
                        $searchBar.addClass('weui-search-bar_focusing');
                        $searchInput.focus();
                    });
                    $searchInput
                        .on('blur', function () {
                            if(!this.value.length) cancelSearch();
                        })
                        .on('keydown', function (event) {
                            if(event.keyCode === 13) {
                                window.location.href = utils.patchUrlPrefixUrl('/wechat/product/all?keyword=' + $(this).val());
                            }
                        })
                    ;
                    $searchClear.on('click', function(){
                        $searchInput.focus();
                    });
                    $searchCancel.on('click', function(){
                        cancelSearch();
                        $searchInput.blur();
                    });
                });
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
            this.loadNewest();
            this.loadRecommend();
            this.loadCarouselImg();
            this.loadBannerImg();
            this.initSearchButton()
        }
    });
});
