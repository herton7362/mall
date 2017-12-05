require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                columns: [
                    {field:'name', title:'名称'},
                    {field:'remark', title:'备注'}
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
                remark: null,
                productStandards: []
            }
        },
        methods: {
            refresh: function () {
                this.loadCombobox();
            },
            loadCombobox: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/productCategory'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc'
                    },
                    success: function(data) {
                        self.parent.data = data.content;
                    }
                })
            },
            addStandard: function () {
                var $form = this.crudgrid.$instance.getForm();
                $form.productStandards.push({
                    name: null,
                    items:[]
                })
            },
            addItem: function (row) {
                row.items.push({
                    name: null
                })
            },
            deleteStandard: function (standards, row, event) {
                messager.alert('确认删除' + row.name + '吗？', event, function () {
                    standards.splice($.inArray(row, standards), 1);
                })
            },
            deleteItem: function(row, item, event) {
                messager.alert('确认删除' + row.name + '：' + item.name + '吗？', event, function () {
                    row.items.splice($.inArray(item, row.items), 1);
                })
            }
        },
        mounted: function() {
            this.loadCombobox();
        }
    });
});