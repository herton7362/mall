require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            orderForm: {
                deliverToAddress: {},
                items:[]
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
            loadOrderForm: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/orderForm/' + utils.getQueryString('id')),
                    success: function(data) {
                        self.orderForm = data;
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
                this.account.cash = this.getTotal();
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
                        messager.bubble("操作成功");
                        window.location.href = utils.patchUrlPrefixUrl('/wechat/orderform/list?page=all');
                    }
                })
            }
        },
        mounted: function () {
            this.loadOrderForm();
        }
    });
});