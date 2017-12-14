require(['jquery', 'vue', 'utils'], function($, Vue, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                queryParams: {
                    name: ''
                },
                columns: [
                    {field:'img', title:'封面', formatter: function(value) {
                            return '<img width="50" src="'+utils.patchUrl('/attachment/download/' + value.id)+'">';
                        }},
                    {field:'name', title:'名称'},
                    {field:'standardsPercent', title:'标准面百分比'},
                    {field:'sortNumber', title:'排序号'}
                ]
            },
            client: {
                data: []
            },
            formData: {
                id: null,
                name: null,
                standardsPercent: null,
                img: null,
                sortNumber: null
            }
        },
        methods: {
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});