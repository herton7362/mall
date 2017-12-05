require([
    'jquery',
    'vue',
    'utils',
    _appConf.ctx + '/static/js/wechat/product/actionsheet/productsheet.js',
    'messager'
], function ($, Vue, utils, productsheet, messager) {
    new Vue({
        el: '#content',
        data: {
            product: {
                coverImage: {}
            }
        },
        filters: {
            coverPath: function (val) {
                return utils.patchUrlPrefixUrl('/attachment/download/' + val);
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
            addCart: function () {
                productsheet.open(this.product);
            },
            justBuy: function () {
                productsheet.open(this.product);
            }
        },
        mounted: function () {
            var self =this;
            $.ajax({
                url: utils.patchUrl('/product/' + utils.getQueryString('id')),
                success: function(data) {
                    self.product = data;
                }
            })
        }
    });
})