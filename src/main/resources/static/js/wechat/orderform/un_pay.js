require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            orderForm: {
                deliverToAddress: {},
                items:[],
                coupon: {amount: 0},
                shop: {}
            },
            account: {
                cash: 0,
                balance: 0,
                point: 0
            }
        },
        filters: {
            coverPath: function (val) {
                return utils.patchUrl('/attachment/download/' + val);
            },
            productPrice: function (val) {
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
            },
            price: function (val) {
                return utils.formatMoney(val);
            },
            addressName: function (val) {
                if(!val) {
                    return '';
                }
                var name = val.name;
                if(val.parent) {
                    name = val.parent.name + name;
                    if(val.parent.parent) {
                        name = val.parent.parent.name + name;
                    }
                }
                return name;
            },
            paymentType: function (val) {
                if(val === 'IN_SHOP') {
                    return '到店支付';
                } else if(val === 'ONLINE') {
                    return '在线支付';
                } else {
                    return '未知';
                }
            }
        },
        methods: {
            openAddressSelector: function () {
                var self = this;
                weui.picker(utils.treeDataConverter(this.address), {
                    onConfirm: function (result) {
                        self.memberAddressForm.address = self.addressMap[result[result.length - 1]];
                    }
                });
            },
            getTotal: function () {
                var total = 0;
                $.each(this.orderForm.items, function () {
                    if(this.sku) {
                        total += this.sku.price * this.count;
                    } else {
                        total += this.product.price * this.count;
                    }
                });
                return total;
            },
            getFinalTotal: function () {
                var total = this.getTotal();
                var point = 0;
                if(this.account.point) {
                    point = this.account.point / 100;
                }
                var balance = 0;
                if(this.account.balance) {
                    balance = this.account.balance;
                }
                return total - ((this.orderForm.coupon && this.orderForm.coupon.amount)||0) - point - balance;
            },
            loadOrderForm: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/orderForm/' + utils.getQueryString('id')),
                    success: function(data) {
                        self.orderForm = data;
                        self.account.point = data.point;
                        self.account.balance = data.balance;
                        self.orderForm.deliverToAddress = self.orderForm.deliverToAddress || {
                            name: self.orderForm.shop.name,
                            detailAddress: self.orderForm.shop.address
                        };
                    }
                })
            },
            pay: function () {
                var self = this;
                var items = [];
                /*function onBridgeReady(){
                    $.ajax({
                        url: utils.patchUrl('/api/orderForm/sign'),
                        contentType: 'application/json',
                        type: 'post',
                        data: JSON.stringify($.extend(self.orderForm, {
                            cash: self.account.cash,
                            balance: self.account.balance,
                            point: self.account.point
                        })),
                        success: function (data) {
                            WeixinJSBridge.invoke(
                                'getBrandWCPayRequest', {
                                    "appId":"wxf446893ee7d612ae",     //公众号名称，由商户传入
                                    "timeStamp":data.timeStamp,         //时间戳，自1970年以来的秒数
                                    "nonceStr":data.nonceStr, //随机串
                                    "package":data.package,
                                    "signType":data.signType,         //微信签名方式：
                                    "paySign":data.sign //微信签名
                                },
                                function(res){
                                    if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                                        pay();
                                    }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                                }
                            );
                        }
                    });
                }
                if (typeof WeixinJSBridge == "undefined"){
                    if( document.addEventListener ){
                        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                    }else if (document.attachEvent){
                        document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                    }
                }else{
                    onBridgeReady();
                }

                function pay() {
                    $.each(self.orderForm.items, function () {
                        items.push({
                            count: self.count,
                            id: self.product.id
                        })
                    });
                    if(self.orderForm.deliverToAddress.id == null) {
                        self.orderForm.deliverToAddress = null;
                    }
                    self.account.cash = self.getFinalTotal();
                    $.ajax({
                        url: utils.patchUrl('/api/orderForm/pay'),
                        contentType: 'application/json',
                        data: JSON.stringify($.extend(self.orderForm, {
                            cash: self.account.cash,
                            balance: self.account.balance,
                            point: self.account.point
                        })),
                        type: 'POST',
                        success: function() {
                            messager.bubble("支付成功", 'success');
                            setTimeout(function () {
                                window.location.href = utils.patchUrlPrefixUrl('/wechat/orderform/list?page=all');
                            }, 1000);
                        }
                    })
                }*/
                $.each(self.orderForm.items, function () {
                    items.push({
                        count: this.count,
                        id: this.product.id
                    })
                });
                if(self.orderForm.deliverToAddress.id == null) {
                    self.orderForm.deliverToAddress = null;
                }
                this.account.cash = this.getFinalTotal();
                $.ajax({
                    url: utils.patchUrl('/api/orderForm/pay'),
                    contentType: 'application/json',
                    data: JSON.stringify($.extend(this.orderForm, {
                        cash: this.account.cash,
                        balance: this.account.balance,
                        point: this.account.point
                    })),
                    type: 'POST',
                    success: function() {
                        messager.bubble("支付成功", 'success');
                        setTimeout(function () {
                            window.location.href = utils.patchUrlPrefixUrl('/wechat/orderform/list?page=all');
                        }, 1000);
                    }
                })
            }
        },
        mounted: function () {
            this.loadOrderForm();
        }
    });
});