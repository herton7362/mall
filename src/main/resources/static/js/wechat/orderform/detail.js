require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    var vue = new Vue({
        el: '#content',
        data: {
            orderItems: [],
            deliverToAddress: {},
            orderForm: {
                deliverToAddress: {},
                shop: {}
            },
            orderStatus: []
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
            date: function (val) {
                return new Date(val).format("yyyy-MM-dd HH:mm:ss");
            },
            status: function (val) {
                var result = '';
                if(!val) {
                    return null;
                }
                $.each(vue.orderStatus, function () {
                    if(this.id.toUpperCase() === val) {
                        result = this.text;
                    }
                });
                return result;
            },
            paymentType: function (val) {
                if(val === 'IN_SHOP') {
                    return '到店支付（请前往店铺支付）';
                } else if(val === 'ONLINE') {
                    return '在线支付';
                } else {
                    return '未知';
                }
            }
        },
        methods: {
            getTotal: function () {
                var total = 0;
                $.each(this.orderItems, function () {
                    if(this.sku) {
                        total += this.sku.price * this.count;
                    } else {
                        total += this.product.price * this.count;
                    }
                });
                return total;
            },
            loadOrderForm: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/orderForm/' + utils.getQueryString('id')),
                    success: function(data) {
                        self.orderItems = data.items;

                        self.orderForm = data;
                        data.deliverToAddress = data.deliverToAddress || {
                            name: self.orderForm.shop.name,
                            detailAddress: self.orderForm.shop.address
                        };
                    }
                })
            }
        },
        mounted: function () {
            var self = this;
            this.loadOrderForm();
            $.ajax({
                url: utils.patchUrl('/api/orderForm/status'),
                success: function (data) {
                    self.orderStatus = data;
                }
            })
        }
    });
});