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
                deliverToAddress: {},
                remark: null,
                member: null
            },
            memberAddresses: [],
            memberAddressForm: {
                id: null,
                name: null,
                tel: null,
                address: {},
                postalCode: null
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
        watch: {
            address: function (val) {
                $.each(val, function () {
                    this.label = this.name;
                    this.value = this.id;
                });
                return val;
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
                    $.ajax({
                        url: utils.patchUrl('/product/' + productId),
                        success: function(data) {
                            self.orderForm.items.push({
                                product: data,
                                count: count
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
                function makeOrder() {
                    $.each(self.orderForm.items,function () {
                        this.id = null;
                    });
                    $.ajax({
                        url: utils.patchUrl('/api/orderForm/makeOrder'),
                        contentType: 'application/json',
                        data: JSON.stringify($.extend(self.orderForm,{
                            status: 'UN_PAY', // 下单未支付
                            member: self.member
                        })),
                        type: 'POST',
                        success: function(orderForm) {
                            messager.bubble("操作成功");
                            window.location.href = utils.patchUrlPrefixUrl('/wechat/orderform/un_pay?id=' + orderForm.id);
                        }
                    })
                }
                if(utils.getQueryString("id")) {
                    $.ajax({
                        url: utils.patchUrl('/api/cart/' + utils.getQueryString("id")),
                        type: 'DELETE',
                        success: makeOrder
                    })
                } else {
                    makeOrder();
                }
            }
        },
        mounted: function () {
            var self = this;
            this.loadAddress();
            this.loadOrderForm();
            utils.getLoginMember(function (member) {
                self.member = member;
                self.loadMemberAddress();
            });
        }
    });
});