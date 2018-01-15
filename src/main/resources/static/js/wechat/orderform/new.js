require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            newAddressActionsheet: {
                $instance: {}
            },
            selectAddressActionsheet: {
                $instance: {}
            },
            address: [],
            addressMap: {},
            orderForm: {
                items: [],
                deliverToAddress: null,
                remark: null,
                member: null,
                coupon: {amount: 0}
            },
            member: {},
            memberAddresses: [],
            memberAddressForm: {
                id: null,
                name: null,
                tel: null,
                address: {},
                postalCode: null
            },
            coupons: [],
            couponSelector: {
                $instance: {},
                open: false
            },
            pointSelector: {
                $instance: {},
                open: false,
                other: false
            },
            balanceSelector: {
                $instance: {},
                open: false,
                other: false
            },
            account: {
                cash: 0,
                balance: null,
                point: null
            },
            paymentType: 'ONLINE',
            selectedShop: null,
            shops: [],
            shopSelector: {
                $instance: {}
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
            }
        },
        watch: {
            address: function (val) {
                $.each(val, function () {
                    this.label = this.name;
                    this.value = this.id;
                });
                return val;
            },
            'account.balance': function (val, oldVal) {
                if(val > this.member.balance) {
                    messager.bubble('余额不足');
                    this.account.balance = oldVal;
                }
            },
            'account.point': function (val, oldVal) {
                if(val > this.member.salePoint) {
                    messager.bubble('积分不足');
                    this.account.point = oldVal;
                }
                if(this.getFinalTotal() < 0) {
                    messager.bubble('积分超出商品价格','danger');
                    this.account.point = oldVal;
                }
            },
            'orderForm.coupon': function (val, oldVal) {
                if(this.getFinalTotal() < 0) {
                    messager.bubble('优惠券优惠超出商品价格','danger');
                    this.orderForm.coupon = oldVal;
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
                return total - this.orderForm.coupon.amount - point - balance;
            },
            saveMemberAddress: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/memberAddress'),
                    contentType: 'application/json',
                    type: 'POST',
                    dataType: 'JSON',
                    data: JSON.stringify($.extend(this.memberAddressForm, {member: this.member})),
                    success: function(data) {
                        messager.bubble('保存成功！');
                        self.orderForm.deliverToAddress = data;
                        self.memberAddresses.push(data);
                        self.newAddressActionsheet.$instance.close();
                    }
                });
            },
            changeDefaultAddress: function (row) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/memberAddress/defaultAddress/' + row.id),
                    type: 'POST',
                    success: function() {
                        self.selectAddressActionsheet.$instance.close();
                        self.loadMemberAddress();
                    }
                });
            },
            loadAddress: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/address'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc'
                    },
                    success: function(data) {
                        self.address = data.content;
                        $.each(data.content, function () {
                            self.addressMap[this.id] = this;
                        })
                    }
                })
            },
            loadOrderForm: function () {
                var isCart = false; // 是否从购物车结算过来
                var self = this;
                if(utils.getQueryString('id')) {
                    isCart = true;
                }

                if(isCart) {
                    this.loadCarts(utils.getQueryString('id'));
                } else {
                    var productId = utils.getQueryString('productId');
                    var count = utils.getQueryString('count');
                    var skuId = utils.getQueryString('skuId');
                    var sku = null;
                    $.ajax({
                        url: utils.patchUrl('/product/' + productId),
                        success: function(data) {
                            if(skuId) {
                                $.each(data.skus, function () {
                                    if(this.id === skuId) {
                                        sku = this;
                                    }
                                });
                            }
                            self.orderForm.items.push({
                                product: data,
                                count: count,
                                sku: sku
                            });
                        }
                    })

                }
            },
            loadCarts: function (id) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/cart/' + id),
                    success: function(data) {
                        self.orderForm.items = data.items;
                    }
                })
            },
            loadMemberAddress: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/memberAddress'),
                    data: {
                        sort: 'createdDate',
                        order: 'desc',
                        'member.id': this.member.id
                    },
                    success: function(data) {
                        self.memberAddresses = data.content;
                        $.each(self.memberAddresses, function () {
                            if(this.defaultAddress) {
                                self.orderForm.deliverToAddress = this;
                            }
                        })
                    }
                })
            },
            submit: function () {
                var self = this;
                if(!this.selectedShop || !this.selectedShop.id) {
                    messager.bubble('请选择门店信息', 'warning');
                    return;
                }
                function makeOrder() {
                    $.each(self.orderForm.items,function () {
                        this.id = null;
                    });
                    self.account.cash = self.getFinalTotal();
                    $.ajax({
                        url: utils.patchUrl('/api/orderForm/makeOrder'),
                        contentType: 'application/json',
                        data: JSON.stringify($.extend(self.orderForm,{
                            status: 'UN_PAY', // 下单未支付
                            member: self.member,
                            shop: self.selectedShop,
                            paymentType: self.paymentType,
                            cash: self.account.cash,
                            balance: self.account.balance || 0,
                            point: self.account.point || 0
                        })),
                        type: 'POST',
                        success: function(orderForm) {
                            messager.bubble("操作成功");
                            setTimeout(function () {
                                if(utils.getQueryString("id")) {
                                    $.ajax({
                                        url: utils.patchUrl('/api/cart/' + utils.getQueryString("id")),
                                        type: 'DELETE',
                                        success: function () {
                                            window.location.href = utils.patchUrlPrefixUrl('/wechat/orderform/un_pay?id=' + orderForm.id);
                                        }
                                    })
                                } else {
                                    window.location.href = utils.patchUrlPrefixUrl('/wechat/orderform/un_pay?id=' + orderForm.id);
                                }
                            }, 1000);
                        }
                    })
                }
                makeOrder();
            },
            loadCouponCount: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/coupon/member/' + self.member.id),
                    success: function (data) {
                        if(data) {
                            $.each(data, function () {
                                if(!self.ifExpired(this.coupon) && !this.used && this.coupon.minAmount <= self.getTotal()) {
                                    self.coupons.push(this.coupon);
                                }
                            });
                        }
                    }
                })
            },
            ifExpired: function (row) {
                var now = new Date().getTime();
                return !(row.startDate <= now && now <= row.endDate);
            },
            priceFormatter: function (price) {
                var result = utils.formatMoney(price).split('.');
                return '<small>￥</small>' + result[0] + '<small>.'+result[1]+'</small>';
            },
            selectCoupon: function () {
                if(this.coupons.length > 0) {
                    this.couponSelector.$instance.open();
                }
            },
            useCoupon: function (coupon) {
                this.orderForm.coupon = coupon;
                window.history.go(-1);
            },
            selectPoint: function () {
                this.pointSelector.$instance.open();
            },
            usePoint:function (point) {
                this.account.point = point;
                window.history.go(-1);
            },
            otherPoint: function () {
                var self = this;
                this.pointSelector.other = true;
                setTimeout(function () {
                    $(self.$refs['pointSelectorInput']).focus();
                }, 200);
            },
            confirmPoint: function () {
                if(this.account.point < 100) {
                    messager.bubble('积分最小不能小于100');
                    this.account.point = null;
                } else {
                    window.history.go(-1);
                }
            },
            selectBalance: function () {
                this.balanceSelector.$instance.open();
            },
            useBalance:function (balance) {
                var total = this.getFinalTotal() + this.account.balance;
                if(total > 0) {
                    if(balance > total) {
                        balance = total;
                    }
                    this.account.balance = balance;
                }
                window.history.go(-1);
            },
            otherBalance: function () {
                var self = this;
                this.balanceSelector.other = true;
                setTimeout(function () {
                    $(self.$refs['balanceSelectorInput']).focus();
                }, 200);
            },
            confirmBalance: function () {
                if(this.account.balance > this.member.balance) {
                    messager.bubble('余额不足');
                    this.account.balance = null;
                    return;
                }
                if(this.account.balance > this.getTotal()) {
                    this.account.balance = this.getTotal();
                }
                window.history.go(-1);
            },
            onActionsheetOpen: function () {
                $('body').css('position', 'fixed');
            },
            onActionsheetClose: function () {
                $('body').css('position', 'static');
            },
            editAddress: function (row) {
                this.newAddressActionsheet.$instance.open();
                this.memberAddressForm = row;
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
            }
        },
        mounted: function () {
            var self = this;
            this.loadAddress();
            this.loadOrderForm();
            utils.getLoginMember(function (member) {
                self.member = member;
                self.loadMemberAddress();
                self.loadCouponCount();
            });
            this.loadShops();
        }
    });
});