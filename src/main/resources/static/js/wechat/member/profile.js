require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            member: {},
            formCount: {},
            couponCount: 0
        },
        filters: {
            coverPath: function (val) {
                return utils.patchUrl('/attachment/download/' + val);
            },
            price: function (val) {
                return utils.formatMoney(val);
            }
        },
        methods: {
            getHeadPhoto: function () {
                if(this.member.headPhoto) {
                    return utils.patchUrl('/attachment/download/' + this.member.headPhoto.id);
                } else {
                    return window._appConf.ctx + '/static/image/default_user.jpg';
                }
            },
            orderForm: function (page) {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/orderform/list?page=' + page);
            },
            loadFormCount: function (memberId) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/orderForm/count/' + memberId),
                    cache: false,
                    success: function (data) {
                        self.formCount = data;
                    }
                })
            },
            coupon: function () {
                window.location.href = utils.patchUrlPrefixUrl('/wechat/coupon/list');
            },
            loadCouponCount: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/coupon/count/' + this.member.id),
                    cache: false,
                    success: function (count) {
                        self.couponCount = count;
                    }
                })
            }
        },
        mounted: function () {
            var self = this;
            utils.getLoginMember(function (member) {
                self.member = member;
                self.loadFormCount(member.id);
            }, true);

        }
    });
});