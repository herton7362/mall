require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            member: {},
            orderForm: {
                shop: {},
                items: []
            },
            account: {
                cash: 0,
                balance: null,
                point: null
            },
            serverSelector: {
                $instance: {}
            }
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
                this.account.cash = this.getFinalTotal();
                $.ajax({
                    url: utils.patchUrl('/api/paintOrderForm/pay'),
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
            },
            getFinalTotal: function () {
                var price = 0;
                $.each(this.orderForm.items, function () {
                    price += this.paint.price * this.count;
                });
                return price;
            },
            loadOrderForm: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/paintOrderForm/' + utils.getQueryString('id')),
                    success: function(data) {
                        self.orderForm = data;
                        self.account.point = data.point;
                        self.account.balance = data.balance;
                    }
                })
            },
            getSurfaceCount: function () {
                var self = this;
                var count = 0;
                if(this.orderForm) {
                    $.each(this.orderForm.items, function () {
                        count += this.count;
                    })
                }
                return count;
            }
        },
        mounted: function () {
            var self = this;
            utils.getLoginMember(function (member) {
                self.member = member;
            });
            this.loadOrderForm();
        }
    });
});