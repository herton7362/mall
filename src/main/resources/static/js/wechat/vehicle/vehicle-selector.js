define([
    'jquery',
    'vue',
    'messager',
    'utils',
    'text!'+_appConf.ctx+'/static/js/wechat/vehicle/vehicle-selector.html'
], function($, Vue, messager, utils, html) {
    $(html).appendTo('body');
    return new Vue({
        el: '#vehicle-selector',
        data: {
            member:{},
            vehicleSelector: {
                $instance: {}
            },
            vehicleSelector2: {
                $instance: {}
            },
            myVehicle: {
                $instance: {}
            },
            userVehicles: [],
            vehicleCategories: [],
            selectedPaint: null,
            selectedVehicle: {
                vehicleCategory: {},
                logo: {}
            },
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
            loadUserVehicle: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/vehicle'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc',
                        member: {
                            id: this.member.id
                        }
                    },
                    success: function(data) {
                        self.userVehicles = data.content;
                        if(!self.userVehicles || self.userVehicles.length <= 0) {
                            self.vehicleSelector.$instance.open();
                        } else {
                            $.each(self.userVehicles, function () {
                                if(this.isDefault) {
                                    self.userVehicle = this;
                                }
                            })
                        }
                        if(self.userVehicle && self.userVehicle.vehicleCategory && self.userVehicle.vehicleCategory.paints) {
                            self.selectedPaint = self.userVehicle.vehicleCategory.paints[0];
                        }
                    }
                })
            },
            loadVehicleCategories: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/vehicleCategory'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc'
                    },
                    success: function(data) {
                        self.vehicleCategories = utils.treeDataConverter(data.content);
                    }
                })
            },
            selectVehicleCategory: function (category) {
                this.selectedVehicle = category;
                this.vehicleSelector2.$instance.open();
            },
            createVehicle: function (category) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/vehicle'),
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        member: self.member,
                        vehicleCategory: category,
                        isDefault: true
                    }),
                    success: function (vehicle) {
                        self.userVehicle = vehicle;
                        self.vehicleSelector.$instance.close();
                        self.vehicleSelector2.$instance.close();
                        self.loadUserVehicle();
                    }
                })
            },
            selectVehicle: function (vehicle) {
                var self = this;
                this.userVehicle = vehicle;
                $.ajax({
                    url: utils.patchUrl('/api/vehicle'),
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify($.extend(vehicle, {
                        member: self.member,
                        isDefault: true
                    })),
                    success: function () {
                        self.myVehicle.$instance.close();
                        self.loadUserVehicle();
                    }
                })
            },
            removeVehicle: function (vehicle) {
                var self = this;
                messager.alert('确认删除['+vehicle.vehicleCategory.name+']吗？', function () {
                    $.ajax({
                        url: utils.patchUrl('/api/vehicle/' + vehicle.id),
                        type: 'DELETE',
                        success: function () {
                            for(var i = 0, l = self.userVehicles.length; i < l; i++) {
                                if(self.userVehicles[i].id !== vehicle.id) {
                                    self.userVehicle = self.userVehicles[i];
                                    break;
                                }
                            }
                            self.loadUserVehicle();
                        }
                    })
                })
            },
            open: function () {
                this.load();
                this.myVehicle.$instance.open();
            },
            load: function () {
                var self = this;
                utils.getLoginMember(function (member) {
                    self.member = member;
                });
                this.loadUserVehicle();
                this.loadVehicleCategories();
            }
        },
        mounted: function() {

        }
    });
});