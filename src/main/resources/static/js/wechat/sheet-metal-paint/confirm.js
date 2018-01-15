require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            member: {},
            userVehicle: {
                vehicleCategory: {},
                logo: {}
            },
            shopSelector: {
                $instance: {}
            },
            serverSelector: {
                $instance: {}
            },
            selectedPaintSurfaces: [],
            shops: [],
            selectedPaint: null,
            selectedShop: null,
            account: {
                cash: 0,
                balance: null,
                point: null
            },
            paymentType: 'ONLINE'
        },
        filters: {
            price: function (val) {
                return utils.formatMoney(val);
            },
            coverPath: function (val) {
                return utils.patchUrl('/attachment/download/' + val);
            }
        },
        methods: {
            submit: function () {
                var self = this;
                var items = [];
                if(!this.validate()) {
                    return;
                }
                self.account.cash = self.getFinalTotal();
                $.each(this.selectedPaintSurfaces, function () {
                    var product = this;
                    $.each(this.skus, function () {
                        if (self.selectedPaint && this.productStandardItems[0].id === self.selectedPaint.id) {
                            items.push({
                                count: 1,
                                product: product,
                                sku: this
                            })
                        }
                    })
                });
                $.ajax({
                    url: utils.patchUrl('/api/orderForm/makeOrder'),
                    contentType: 'application/json',
                    data: JSON.stringify({
                        vehicle: self.userVehicle,
                        shop: self.selectedShop,
                        status: 'UN_PAY', // 下单未支付
                        member: self.member,
                        cash: self.account.cash,
                        balance: self.account.balance || 0,
                        point: self.account.point || 0,
                        paymentType: self.paymentType,
                        items: items
                    }),
                    type: 'POST',
                    success: function(orderForm) {
                        messager.bubble("操作成功");
                        setTimeout(function () {
                            window.location.href = utils.patchUrlPrefixUrl('/wechat/orderform/un_pay?id=' + orderForm.id);
                        }, 1000);
                    }
                })
            },
            validate: function () {
                if(!this.selectedShop || !this.selectedShop.id) {
                    messager.bubble('请选择门店信息', 'warning');
                    return false;
                }
                return true;
            },
            getFinalTotal: function () {
                var self = this;
                var price = 0;
                $.each(this.selectedPaintSurfaces, function () {
                    $.each(this.skus, function () {
                        if(self.selectedPaint && this.productStandardItems[0].id === self.selectedPaint.id) {
                            price += this.price;
                        }
                    })
                });
                return price;
            },
            loadSelectedPaintSurfaces: function () {
                var self = this;
                this.selectedPaintSurfaces = eval(localStorage.selectedPaintSurfaces);
                this.userVehicle = eval('(' + localStorage.userVehicle + ')');
                this.selectedPaint = eval('(' + localStorage.selectedPaint + ')');
                $.each(this.selectedPaintSurfaces, function () {
                    self.surfaceCount += this.standardsPercent;
                })
            },
            loadShops: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/shop'),
                    success: function (data) {
                        self.shops = data;
                    }
                })
            },
            selectShop: function (shop) {
                this.selectedShop = shop;
                this.shopSelector.$instance.close();
            },
            getPaintSurfacePrice: function (row) {
                var self = this;
                var price = 0;
                $.each(row.skus, function () {
                    if(self.selectedPaint && this.productStandardItems[0].id === self.selectedPaint.id) {
                        price = this.price;
                    }
                });
                return price;
            }
        },
        mounted: function () {
            var self = this;
            utils.getLoginMember(function (member) {
                self.member = member;
            });
            this.loadSelectedPaintSurfaces();
            this.loadShops();
        }
    });
});