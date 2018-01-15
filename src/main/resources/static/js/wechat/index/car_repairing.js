require([
    'jquery',
    'vue',
    'utils',
    'messager',
    _appConf.ctx + '/static/js/wechat/vehicle/vehicle-selector.js'
], function ($, Vue, utils, messager, vehicleSelector) {
    new Vue({
        el: '#content',
        data: {
            tyres: [],
            engineOils: [],
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
            loadTyres: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/product'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc',
                        productCategory: {
                            id: '402881d160b455650160b45a1dcf0000'
                        },
                        currentPage: 1,
                        pageSize: 10
                    },
                    success: function(data) {
                        self.tyres = data.content
                    }
                });
            },
            loadEngineOils: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/product'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc',
                        productCategory: {
                            id: '402881d160b455650160b45a45b50001'
                        },
                        currentPage: 1,
                        pageSize: 10
                    },
                    success: function(data) {
                        self.engineOils = data.content
                    }
                });
            },
            productDetail: function (row) {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/product/detail?id=' + row.id);
            },
            more: function () {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/product/all');
            },
            vehicleSelectorOpen: function () {
                vehicleSelector.open();
            },
            search: function () {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/index/search-list?key=' + this.searchKey)
            }
        },
        mounted: function () {
            this.loadTyres();
            this.loadEngineOils();
        }
    });
})