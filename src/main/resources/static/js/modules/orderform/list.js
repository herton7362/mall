require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    var vue = new Vue({
        el: '#content',
        data: {
            data: [],
            currentPage: 1,
            pagerSize: 7,
            pageSize: 15,
            count: 0,
            checkAll: false,
            selectedRows: [],
            orderStatus: []
        },
        filters: {
            coverPath: function (val) {
                return utils.patchUrl('/attachment/download/' + val);
            },
            price: function (val) {
                return utils.formatMoney(val);
            },
            date: function (val) {
                return new Date(val).format("yyyy-MM-dd HH:mm:ss");
            },
            status: function (val) {
                var result = '';
                $.each(vue.orderStatus, function () {
                    if(this.id.toUpperCase() === val) {
                        result = this.text;
                    }
                });
                return result;
            }
        },
        methods: {
            load: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/orderForm'),
                    data: {
                        sort: 'updatedDate',
                        order: 'desc',
                        currentPage: this.currentPage,
                        pageSize: this.pageSize
                    },
                    success: function(data) {
                        self.data = data.content;
                        self.count = data.totalElements;
                        self.clearSelected();
                    }
                })
            },
            goToPage: function (page) {
                this.currentPage = page;
                this.load();
            },
            expend: function (row) {
                Vue.set(row, 'expended', !row.expended);
            },
            getProductTotal: function (row) {
                var total = 0;
                $.each(row.items, function () {
                    total += this.count;
                });
                return total;
            },
            clearSelected: function () {
                this.selectedRows.splice(0);
            },
            selectAll: function(event) {
                var self = this;
                if($(event.target).is(':checked')) {
                    $.each(this.data, function () {
                        if($.inArray(this, this.selectedRows) < 0) {
                            self.selectedRows.push(this);
                        }
                    });
                } else {
                    this.clearSelected();
                }
            }
        },
        mounted: function() {
            var self = this;
            this.load();
            $.ajax({
                url: utils.patchUrl('/api/orderForm/status'),
                success: function (data) {
                    self.orderStatus = data;
                }
            })
        }
    });
});