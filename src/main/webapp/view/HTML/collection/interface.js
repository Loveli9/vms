; $(function () {
    $('#interface_table').bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: "http://192.168.0.11:8080/datacollection/collectioninterface/all",
        editable: false,//可行内编辑
        sortable: false, //是否启用排序
        sortOrder: "asc",
        striped: false, //是否显示行间隔色
        dataField: "rows",
        queryParamsType: 'limit',
        sidePagination: 'server',
        queryParams: function (params) {
            var param = {
                size: params.limit, //页面大小
                current: params.pageNumber, //页码
                sort: params.sort,//排序列名
                sortOrder: params.order //排位命令（desc，asc）
            }
            return param;
        },
        responseHandler: function (res) {
            return {
                "rows": res.data,
                "total": res.totalCount
            }
        },
        // showColumns: true,
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        toolbar: '#toolbar',//指定工作栏
        buttonsAlign: 'right',//按钮对齐方式
        columns: [
            {
                title: '全选',
                field: 'select',
                radio: true,
                width: 30,
                align: 'center',
                valign: 'middle',
            },
            {
                title: '接口类型',
                halign: 'center',
                align: 'center',
                field: 'type',
                sortable: false,
                width: 70,
            },
            {
                title: '接口名称',
                halign: 'center',
                align: 'center',
                field: 'name',
                sortable: false,
                width: 70,
            },
            {
                title: '接口地址',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'baseUrl',
                width: 70,
            },
            {
                title: '参数列表',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'parameters',
                width: 70,
            }, {
                title: '描述',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'description',
                width: 70,
            },

        ],
        locale: 'zh-CN',//中文支持,
    });

    //返回
    $("#interface_back_btn").on("click", function () {
        $("#edit_interface").modal("hide");
    });

    // 保存
    $("#interface_save_btn").on("click", function () {
        var editMsg = $("#add_edit_table").bootstrapTable("getData");
        let select_ID = $('#interface_table').bootstrapTable('getSelections')[0];
        let parameters = [];
        let item = {};
        editMsg.forEach(e => {
            item.name = e.name;
            item.value = e.value;
            item.inputType = e.inputType;
            item.required = e.required;
            parameters.push(item);
            item = {};
        })
        let params = {
            "entity.baseUrl": $("#interface_URL").val(),
            "entity.description": $("#interface_des").val(),
            "entity.id": select_ID ? select_ID.id : null,
            "entity.name": $("#interface_name").val(),
            "entity.parameters": JSON.stringify(parameters),
            "entity.type": $("#interface_type").val(),
        }
        $.ajax({
            contentType: "application/x-www-form-urlencoded", //设置请求头信息
            url: "http://192.168.0.11:8080/datacollection/collectioninterface/save",
            type: 'post',
            method: 'post',
            data: params,
            success: function (res) {
                if (res.success) {
                    $('#interface_table').bootstrapTable('refresh')
                    $("#edit_interface").modal("hide");
                    toastr.success('操作成功!');
                }
            }
        });
    });

    //删除
    $("#delete_table_item").on("click", function () {
        let select_ID = $('#interface_table').bootstrapTable('getSelections')[0];
        if (select_ID) {
            $.ajax({
                contentType: "application/x-www-form-urlencoded", //设置请求头信息
                url: "http://192.168.0.11:8080/datacollection/collectioninterface/delete",
                type: 'post',
                method: 'post',
                data: { id: select_ID.id },
                success: function (res) {
                    if (res.success) {
                        $('#interface_table').bootstrapTable('refresh')
                        toastr.success('删除成功!');
                    }
                }
            });
        } else {
            toastr.error('请选择要删除的数据!');
        }
    })
});

function add_editBtn(message) {
    var readonly = [{ value: "0", text: "是" }, { value: "1", text: "否" }];
    var dataSource = [
        { value: "enter_manually", text: "手动录入" },
        { value: "Iteration_start_time", text: "迭代开始时间" },
        { value: "Iteration_end_time", text: "迭代结束时间" },
        { value: "project_name", text: "项目名称" },
        { value: "project_start_time", text: "项目开始时间" },
        { value: "project_end_time", text: "项目结束时间" },
        { value: "project_member", text: "项目人员" },
    ];
    var table_data = [];
    if (message == "editMsg") {
        $('#add_edit_table').bootstrapTable('destroy');
        let select_ID = $('#interface_table').bootstrapTable('getSelections')[0];
        if (select_ID) {
            $("#edit_interface").modal("show");
            $("#interface_type").val(`${select_ID.type}`);
            $("#interface_name").val(`${select_ID.name}`);
            $("#interface_URL").val(`${select_ID.baseUrl}`);
            $("#interface_des").val(`${select_ID.description ? select_ID.description : ""}`);
            table_data = JSON.parse(select_ID.parameters)
            if (table_data) {
                table_data.forEach(e => {
                    e.deleteId = Math.random();
                });
            }
        } else {
            toastr.error('请选择要编辑的数据!');
        }
    } else if (message == "addMsg") {
        $('#interface_table').bootstrapTable('uncheckAll')
        $('#add_edit_table').bootstrapTable('destroy');
        $("#edit_interface").modal("show");
        $("#interface_type").val("");
        $("#interface_name").val("");
        $("#interface_URL").val("");
        $("#interface_des").val("");
        table_data = [{
            deleteId: Math.random(),
            name: "",
            value: "",
            inputType: "",
            required: "",
            select: undefined
        }];
    };
    $('#add_edit_table').bootstrapTable({
        sortable: false, //是否启用排序
        sortOrder: "asc",
        editable: true,//可行内编辑
        striped: false, //是否显示行间隔色
        dataField: "rows",
        queryParamsType: 'limit',
        sidePagination: 'server',
        data: table_data,
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        toolbar: '#interface_toolbar',//指定工作栏
        buttonsAlign: 'right',//按钮对齐方式
        columns: [
            {
                title: '全选',
                field: 'select',
                radio: true,
                width: 30,
                align: 'center',
                valign: 'middle',
            },
            {
                title: '参数名称',
                halign: 'center',
                align: 'center',
                field: 'name',
                sortable: false,
                width: 70,
                editable: {
                    type: "text",
                    title: '参数名称',
                    emptytext: '&#12288',
                }
            },
            {
                title: '默认值',
                halign: 'center',
                align: 'center',
                field: 'value',
                sortable: false,
                width: 70,
                editable: {
                    type: "text",
                    title: '默认值',
                    emptytext: '&#12288',
                }
            },
            {
                title: '数据来源',
                halign: 'center',
                align: 'center',
                field: 'inputType',
                sortable: false,
                width: 70,
                editable: {
                    type: 'select',
                    title: '数据来源',
                    source: dataSource
                }
            },
            {
                title: '是否只读',
                halign: 'center',
                align: 'center',
                field: 'required',
                sortable: false,
                width: 70,
                editable: {
                    type: 'select',
                    title: '是否只读',
                    source: readonly
                }
            },
        ],
        locale: 'zh-CN',//中文支持,
    });
    $("#interface_add").on("click", function () {
        var table_data_item = {
            deleteId: Math.random(),
            name: "",
            value: "",
            inputType: "",
            required: "",
            select: undefined
        }
        table_data.push(table_data_item);
        $('#add_edit_table').bootstrapTable('load', table_data);
    });
    $("#interface_delete").on("click", function () {
        var select_delete = $('#add_edit_table').bootstrapTable('getSelections')[0];
        if (select_delete) {
            for (let i = 0; i < table_data.length; i++) {
                if (table_data[i].deleteId == select_delete.deleteId) {
                    table_data.splice(i, 1)
                };
            }
            $('#add_edit_table').bootstrapTable('load', table_data);
        } else {
            toastr.error('请选择要移除的数据!');
        }
    });
};