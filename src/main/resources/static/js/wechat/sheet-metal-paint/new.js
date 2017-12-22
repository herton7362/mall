require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            member: {},
            paintSurfaces: [],
            selectedPaintSurfaces: [],
            checkAll: false
        },
        filters: {
            coverPath: function (val) {
                return utils.patchUrl('/attachment/download/' + val);
            }
        },
        methods: {
            submit: function () {
                localStorage.selectedPaintSurfaces = JSON.stringify(this.selectedPaintSurfaces);
                window.location.href = utils.patchUrl('/wechat/sheet-metal-paint/new-select-product');
            },
            loadPaintSurface: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/paintSurface'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc'
                    },
                    success: function(data) {
                        self.paintSurfaces = data.content;
                    }
                })
            },
            isSelected: function (item) {
                return $.inArray(item, this.selectedPaintSurfaces) >= 0;
            },
            selectPaintSurfaces: function (item) {
                var index = $.inArray(item, this.selectedPaintSurfaces);
                if(index >= 0) {
                    this.selectedPaintSurfaces.splice(index, 1);
                } else {
                    this.selectedPaintSurfaces.push(item);
                }
            },
            selectAllPaintSurfaces: function () {
                var self = this;
                this.checkAll = !this.checkAll;
                if(this.checkAll) {
                    $.each(this.paintSurfaces, function () {
                        if(!self.isSelected(this)) {
                            self.selectPaintSurfaces(this);
                        }
                    })
                } else {
                    $.each(this.paintSurfaces, function () {
                        if(self.isSelected(this)) {
                            self.selectPaintSurfaces(this);
                        }
                    })
                }
            }
        },
        mounted: function () {
            var self = this;
            utils.getLoginMember(function (member) {
                self.member = member;
            });
            this.loadPaintSurface();
        }
    });
});