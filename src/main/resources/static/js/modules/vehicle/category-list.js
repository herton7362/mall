require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                columns: [
                    {field:'name', title:'名称'}
                ]
            },
            sidebar: {
                root: {
                    id: 'isNull',
                    name: '所有类别',
                    open: true,
                    alwaysExpended: true
                }
            },
            parent: {
                data: []
            },
            formData: {
                id: null,
                parent: {},
                name: null,
                logo: null
            },
            paints: []
        },
        methods: {
            refresh: function () {
                this.loadCombobox();
            },
            loadCombobox: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/vehicleCategory'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc'
                    },
                    success: function(data) {
                        self.parent.data = data.content;
                    }
                })
            },
            loadPaints: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/productCategory/402881d160bef6460160bfa8c67d0002'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc'
                    },
                    success: function(data) {
                        self.paints = data.productStandards[0].items;
                    }
                })
            }
        },
        mounted: function() {
            this.loadCombobox();
            this.loadPaints();
        }
    });
});