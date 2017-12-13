define([
    'jquery',
    'vue',
    'messager',
    'utils',
    'text!'+_appConf.ctx+'/static/js/wechat/product/actionsheet/productsheet.html'
], function($, Vue, messager, utils, html) {
    $(html).appendTo('body');
    return new Vue({
        el: '#product-sheet',
        data: {
            actionsheet: {
                $instance: {}
            },
            count: 1,
            product: {
                coverImage: {}
            },
            selectedProductProductStandardItems: [],
            selectedSku: null,
            button: {
                text: '确定',
                callback: function () {
                    this.close();
                }
            }
        },
        filters: {
            coverPath: function (val) {
                if(!val) {
                    return '';
                }
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
            open: function (product) {
                if(product) {
                    this.product = product;
                    var self = this;
                    if(this.product.productProductStandards) {
                        self.selectedProductProductStandardItems = [];
                        $.each(this.product.productProductStandards, function () {
                            self.selectedProductProductStandardItems.push({id: null});
                        })
                    }
                    this.selectedSku = null;
                }
                this.actionsheet.$instance.open();
            },
            close: function () {
                this.actionsheet.$instance.close();
            },
            increase: function () {
                this.count ++;
            },
            reduce: function () {
                this.count --;
            },
            buttonClick: function () {
                this.button.callback.call(this);
            },
            addCart: function () {
                var self = this;
                if(!this.validate()) {
                    return;
                }
                utils.getLoginMember(function(member) {
                    $.ajax({
                        url: utils.patchUrl('/api/cart/addProduct'),
                        contentType: 'application/json',
                        type: 'POST',
                        data: JSON.stringify({
                            member: member,
                            items: [{
                                product: self.product,
                                count: self.count,
                                sku: self.selectedSku
                            }]
                        }),
                        success: function() {
                            messager.bubble('成功加入购物车', 'success');
                            self.close();
                            loadCartCount();
                        }
                    });
                });
            },
            justBuy: function () {
                if(!this.validate()) {
                    return;
                }
                window.location.href = utils.patchUrlPrefixUrl('/wechat/orderform/new?productId=' +
                    this.product.id +
                    '&count=' +
                    this.count +
                    '&skuId=' +
                    (this.selectedSku? this.selectedSku.id: ''))
            },
            validate: function () {
                if(!this.product) {
                    messager.bubble('商品参数为空');
                    return false;
                }
                if(!this.count) {
                    messager.bubble('数量为空');
                    return false;
                }

                return this.validateProductProductStandardItems();
            },
            validateProductProductStandardItems: function (silence) {
                if(this.selectedProductProductStandardItems) {
                    var itemsSelected = true;
                    var unSelectStandard = -1;
                    for(var i = 0, l = this.selectedProductProductStandardItems.length; i < l; i++) {
                        if(!this.selectedProductProductStandardItems[i].id) {
                            itemsSelected = false;
                            unSelectStandard = i;
                            break;
                        }
                    }
                    if(!itemsSelected) {
                        if(!silence) {
                            messager.bubble('请选择' + this.product.productProductStandards[unSelectStandard].productStandard.name);
                        }
                        return false;
                    }
                }
                return true;
            },
            selectCategory: function (index, item) {
                $.extend(this.selectedProductProductStandardItems[index], item);
                if(this.validateProductProductStandardItems(true)) {
                    var skuMap = {};
                    var itemIds = [];
                    $.each(this.product.skus, function () {
                        itemIds = [];
                        $.each(this.productStandardItems, function () {
                            itemIds.push(this.id)
                        });
                        skuMap[itemIds.join(',')] = this;
                    });
                    itemIds = [];
                    $.each(this.selectedProductProductStandardItems, function () {
                        itemIds.push(this.id)
                    });
                    this.selectedSku = skuMap[itemIds.join(',')];
                }
            }
        }
    });
});