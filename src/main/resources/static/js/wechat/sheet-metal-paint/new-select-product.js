require(['jquery', 'vue', 'utils', 'weui', 'messager', _appConf.ctx + '/static/js/wechat/vehicle/vehicle-selector.js'],
    function ($, Vue, utils, weui, messager, vehicleSelector) {
    new Vue({
        el: '#content',
        data: {
            member: {},
            selectedPaintSurfaces: [],
            selectedPaint: null,
            userVehicle: {
                vehicleCategory: {}
            }
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
                if(!this.userVehicle.id) {
                    messager.bubble('请选择车辆');
                    return;
                }
                localStorage.userVehicle = JSON.stringify(this.userVehicle);
                localStorage.selectedPaint = JSON.stringify(this.selectedPaint);
                window.location.href = utils.patchUrlPrefixUrl('/wechat/sheet-metal-paint/confirm');
            },
            getFinalTotal: function () {
                var self = this;
                var price = 0;
                $.each(this.selectedPaintSurfaces, function () {
                    $.each(this.skus, function () {
                        if(self.selectedPaint && this.productStandardItems[0].id === self.selectedPaint.id) {
                            price += this.price;
                        }
                    })
                });
                return price;
            },
            loadSelectedPaintSurfaces: function () {
                this.selectedPaintSurfaces = eval(localStorage.selectedPaintSurfaces);
            },
            selectPaint: function (paint) {
                this.selectedPaint = paint;
            },
            myVehicleOpen: function () {
                vehicleSelector.open();
            },
            getPaintSurfacePrice: function (row) {
                var self = this;
                var price = 0;
                $.each(row.skus, function () {
                    if(self.selectedPaint && this.productStandardItems[0].id === self.selectedPaint.id) {
                        price = this.price;
                    }
                });
                return price;
            }
        },
        mounted: function () {
            var self = this;
            utils.getLoginMember(function (member) {
                self.member = member;
            });
            this.loadSelectedPaintSurfaces();
            vehicleSelector.$watch('userVehicle', function (val) {
                self.userVehicle = val;
            });
            vehicleSelector.$watch('selectedPaint', function (val) {
                self.selectedPaint = val;
            });
            vehicleSelector.load();
        }
    });
});