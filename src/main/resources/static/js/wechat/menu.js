require(['jquery', 'vue', 'utils'], function ($, Vue, utils) {
    function selectMenu(vue) {
        var url = location.pathname;
        $.each(vue.menus, function() {
            if(this.url.split('?')[0] === url) {
                Vue.set(this, 'active', true);
            }
        });
    }
    if($('#tabbar').length <= 0) {
        return;
    }
    new Vue({
        el: '#tabbar',
        data: {
            menus: [
                {
                    name: '首页',
                    url: utils.patchUrlPrefixUrl('/'),
                    icon: 'fa fa-home',
                    active: false
                }, {
                    name: '全部商品',
                    url: utils.patchUrlPrefixUrl('/wechat/product/all'),
                    icon: 'fa fa-list-ul',
                    active: false
                }, {
                    name: '购物车',
                    url: utils.patchUrlPrefixUrl('/wechat/cart/list'),
                    icon: 'fa fa-shopping-cart',
                    active: false
                }, {
                    name: '个人中心',
                    url: utils.patchUrlPrefixUrl('/wechat/member/profile'),
                    icon: 'iconfont icon-gerenzhongxin',
                    active: false
                }
            ]
        },
        mounted: function () {
            selectMenu(this);
        }
    });
})