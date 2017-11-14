require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            orderForm: {
                deliverToAddress: {},
                items:[],
                coupon: {amount: 0}
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
                    total += this.product.price * this.count;
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
                    point = this.account.balance;
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
                    }
                })
            },
            pay: function () {
                var self = this;
                var items = [];
                $.each(self.orderForm.items, function () {
                    items.push({
                        count: this.count,
                        id: this.product.id
                    })
                });
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