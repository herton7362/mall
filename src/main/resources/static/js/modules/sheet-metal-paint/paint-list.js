require(['jquery', 'vue', 'utils'], function($, Vue, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                queryParams: {
                    name: ''
                },
                columns: [
                    {field:'name', title:'名称'},
                    {field:'price', title:'价格'},
                    {field:'remark', title:'备注'}
                ]
            },
            client: {
                data: []
            },
            formData: {
                id: null,
                name: null,
                price: null,
                remark: null
            }
        },
        methods: {
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});