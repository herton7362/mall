require(['jquery', 'vue', 'utils'], function($, Vue, utils) {
    new Vue({
        el: '#content',
        data: {
            counts: {
                todaySale: 0,
                monthSale: 0,
                product: 0,
                member: 0
            }
        },
        methods: {
            loadTodaySaleCount: function () {

            },
            loadMonthSaleCount: function () {

            },
            loadProductCount: function () {

            },
            loadMemberCount: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/member/count'),
                    cache: false,
                    success: function (data) {
                        self.counts.member = data;
                    }
                })
            }
        },
        mounted: function () {
            this.loadTodaySaleCount();
            this.loadMonthSaleCount();
            this.loadProductCount();
            this.loadMemberCount();
        }
    });
});