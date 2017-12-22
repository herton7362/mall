require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            member: {},
            userVehicle: {
                vehicleCategory: {},
                logo: {}
            },
            surfaceCount: 0,
            selectedPaint: null
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
            },
            getFinalTotal: function () {
                if(this.selectedPaint) {
                    return this.selectedPaint.price * this.surfaceCount;
                } else {
                    return 0;
                }
            },
            loadSelectedPaintSurfaces: function () {
                var self = this;
                this.selectedPaintSurfaces = eval(localStorage.selectedPaintSurfaces);
                this.userVehicle = eval(localStorage.userVehicle);
                this.selectedPaint = eval(localStorage.selectedPaint);
                $.each(this.selectedPaintSurfaces, function () {
                    self.surfaceCount += this.standardsPercent;
                })
            }
        },
        mounted: function () {
            var self = this;
            utils.getLoginMember(function (member) {
                self.member = member;
            });
            this.loadSelectedPaintSurfaces();
        }
    });
});