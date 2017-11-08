require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            member: {},
            formCount: {}
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