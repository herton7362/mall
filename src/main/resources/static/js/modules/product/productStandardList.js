require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                queryParams: {
                    name: ''
                },
                columns: [
                    {field:'name', title:'名称'},
                    {field:'remark', title:'备注'}
                ]
            },
            formData: {
                id: '',
                name: null,
                remark: null,
                items: []
            }
        },
        methods: {
            addItem: function () {
                this.crudgrid.$instance.getForm().items.push({});
            }
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});