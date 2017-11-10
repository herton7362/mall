require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            coupons: [],
            member: {
                coupons: []
            }
        },
        filters: {
            price: function (val) {
                return utils.formatMoney(val);
            },
            date: function (val) {
                return new Date(val).format('yyyy-MM-dd');
            }
        },
        methods: {
            loadCoupons: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/coupon'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc'
                    },
                    success: function (data) {
                        $.each(data.content, function () {
                            this.expired = self.ifExpired(this);
                        });
                        self.coupons = data.content;
                    }
                })
            },
            loadMemberCoupons: function (id) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/coupon/member/' + id),
                    success: function (data) {
                        $.each(data, function () {
                            this.expired = self.ifExpired(this);
                        });
                        self.member.coupons = data;
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
            }
        },
        mounted: function() {
            var self = this;
            utils.getLoginMember(function (member) {
                self.member = member;
                self.loadMemberCoupons(member.id);
            }, true);
            this.loadCoupons();
        }
    });
});