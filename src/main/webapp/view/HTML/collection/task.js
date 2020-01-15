; $(function () {
    $('#task_table').bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: "http://192.168.0.11:8080/datacollection/collectiontask/all",
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
                "total": res.total
            }
        },
        onDblClickRow: function () {
            add_editBtn("editMsg")
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
                title: '接口名称',
                halign: 'center',
                align: 'center',
                field: 'name',
                sortable: false,
                width: 70,
            },
            {
                title: '项目',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'projectId',
                width: 70,
            },
            {
                title: '接口',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'parameters',
                width: 70,
                formatter: function (value, row, index) {
                    return row.collectionInterface.name
                }
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

    // 返回
    $("#interface_back_btn").on("click", function () {
        $("#edit_interface").modal("hide");
    });

    //保存
    $("#interface_save_btn").on("click", function () {
        var editMsg = $("#interface_parameter_table").bootstrapTable("getData");
        let select_ID = $('#task_table').bootstrapTable('getSelections')[0];
        let parameters = [];
        let item = {};
        editMsg.forEach(e => {
            item.name = e.name;
            item.value = e.value;
            parameters.push(item);
            item = {};
        })
        let params = {
            "entity.projectId": getCookie("projNo_comm"),
            "entity.parameters": JSON.stringify(editMsg),
            "entity.id": select_ID ? select_ID.id : null,
            "entity.projectId": select_ID ? select_ID.projectId : null,
            "entity.name": $("#interface_name").val(),
            "entity.collectionInterface.id": $("#interface_type option:selected").attr("collectionInterfaceId"),
            "entity.description": $("#interface_des").val(),
        };
        if ($("#interface_type option:selected").attr("collectionInterfaceId")) {
            $.ajax({
                contentType: "application/x-www-form-urlencoded", //设置请求头信息
                url: "http://192.168.0.11:8080/datacollection/collectiontask/save",
                type: 'post',
                method: 'post',
                data: params,
                success: function (res) {
                    if (res.success) {
                        $('#task_table').bootstrapTable('refresh')
                        $("#edit_interface").modal("hide");
                        window.top.toastr.success('操作成功!');
                    }
                }
            });
        } else {
            window.top.toastr.error('接口为必填字段！');
        }
    });

    // 立即执行
    $("#btn_immediately").on("click", function () {
        let select_ID = $('#task_table').bootstrapTable('getSelections')[0];
        if (select_ID) {
            $.ajax({
                contentType: "application/x-www-form-urlencoded", //设置请求头信息
                url: "http://192.168.0.11:8080/datacollection/collectionschedule/execute_once",
                type: 'post',
                method: 'post',
                data: { executor: getCookie("username"), taskId: select_ID.id },
                success: function (res) {
                    if (res.success) {
                        window.top.toastr.success('执行成功!');
                    } else {
                        window.top.toastr.error(res.message);
                    }
                }
            });
        } else {
            window.top.toastr.error('请选择要执行的任务!');
        }
    });

    //删除
    $("#delete_table_item").on("click", function () {
        let select_ID = $('#task_table').bootstrapTable('getSelections')[0];
        if (select_ID) {
            $.ajax({
                contentType: "application/x-www-form-urlencoded", //设置请求头信息
                url: "http://192.168.0.11:8080/datacollection/collectiontask/delete",
                type: 'post',
                method: 'post',
                data: { id: select_ID.id },
                success: function (res) {
                    if (res.success) {
                        $('#task_table').bootstrapTable('refresh')
                        window.top.toastr.success('删除成功!');
                    }
                }
            });
        } else {
            window.top.toastr.error('请选择要删除的数据!');
        }
    })
})
// 获取采集接口
function collectioninterface() {
    $.ajax({
        contentType: "application/x-www-form-urlencoded", //设置请求头信息
        url: "http://192.168.0.11:8080/datacollection/collectioninterface/all",
        type: 'post',
        method: 'post',
        async: false,
        success: function (res) {
            if (res.success) {
                $("#interface_type").empty();
                var htmlFragment = "";
                var appendHtml = "";
                for (let i = 0; i < res.data.length; i++) {
                    htmlFragment = `<option value=${res.data[i].parameters} collectionInterfaceId=${res.data[i].id}>${res.data[i].name}</option>`;
                    appendHtml += htmlFragment;
                    htmlFragment = "";
                };
                $("#interface_type").append(appendHtml)
            }
        }
    });
};
//新增，编辑
function add_editBtn(message) {
    collectioninterface();
    var table_data = []
    if (message == "editMsg") {
        $('#interface_parameter_table').bootstrapTable('destroy');
        let select_ID = $('#task_table').bootstrapTable('getSelections')[0];
        if (select_ID) {
            $("#edit_interface").modal("show");
            $("#interface_name").val(select_ID.name);
            $("#interface_type").val(select_ID.collectionInterface.parameters);
            $("#interface_des").val(select_ID.description);
            var select_table_data = JSON.parse(select_ID.parameters);
            var select_parameters = JSON.parse(select_ID.collectionInterface.parameters);
            for (let i = 0; i < select_parameters.length; i++) {
                var flag = true;
                var item = {};
                for (let j = 0; j < select_table_data.length; j++) {
                    if (select_parameters[i].name == select_table_data[j].name) {
                        flag = false;
                        item = select_table_data[j];
                        break
                    }
                };
                if (flag) {
                    item = {
                        name: select_parameters[i].name,
                        value: "",
                        inputType: "",
                        required: ""
                    };

                }
                table_data.push(item);
            }
        } else {
            window.top.toastr.error('请选择要编辑的数据!');
        }
    } else if (message == "addMsg") {
        $('#task_table').bootstrapTable('uncheckAll');
        $('#interface_parameter_table').bootstrapTable('destroy');
        $("#edit_interface").modal("show");
        $("#interface_name").val("");
        $("#interface_type").val("");
        $("#interface_des").val("");
        table_data = [];
    }
    $('#interface_parameter_table').bootstrapTable({
        sortable: false, //是否启用排序
        sortOrder: "asc",
        editable: true,//可行内编辑
        striped: false, //是否显示行间隔色
        dataField: "rows",
        data: table_data,
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        toolbar: '#interface_toolbar',//指定工作栏
        buttonsAlign: 'right',//按钮对齐方式
        columns: [
            {
                title: '全选',
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
            },
            {
                title: '值',
                halign: 'center',
                align: 'center',
                field: 'value',
                sortable: false,
                width: 70,
                editable: {
                    type: "text",
                    title: '值',
                    emptytext: '&#12288',
                },
            },

        ],
        locale: 'zh-CN',//中文支持,
    });
    for (let i = 0; i < table_data.length; i++) {
        if (table_data[i].required == "1") {
            $(`#interface_parameter_table tr[data-index=${i}] a[data-name=value].editable`).editable("toggleDisabled", true);
        } else {
            $(`#interface_parameter_table tr[data-index=${i}] a[data-name=value].editable`).editable("toggleDisabled", false);
        }
    };
    //change事件
    $("#interface_type").on("change", function () {
        let change_table_data = [];
        let select_Msg = $('#task_table').bootstrapTable('getSelections')[0];
        if (select_Msg && select_Msg.collectionInterface.id == $("#interface_type option:selected").attr("collectionInterfaceId")) {
            var select_table_data = JSON.parse(select_Msg.parameters);
            var select_parameters = JSON.parse(select_Msg.collectionInterface.parameters);
            for (let i = 0; i < select_parameters.length; i++) {
                var flag = true;
                var item = {};
                for (let j = 0; j < select_table_data.length; j++) {
                    if (select_parameters[i].name == select_table_data[j].name) {
                        flag = false;
                        item = select_table_data[j];
                        break
                    }
                };
                if (flag) {
                    item = {
                        name: select_parameters[i].name,
                        value: "",
                        inputType: "",
                        required: ""
                    };

                }
                change_table_data.push(item);
            }
        } else {
            change_table_data = JSON.parse($("#interface_type").val());
        }
        $('#interface_parameter_table').bootstrapTable('load', change_table_data)
        for (let i = 0; i < change_table_data.length; i++) {
            if (change_table_data[i].required == "1") {
                $(`#interface_parameter_table tr[data-index=${i}] a[data-name=value].editable`).editable("toggleDisabled", true);
            } else {
                $(`#interface_parameter_table tr[data-index=${i}] a[data-name=value].editable`).editable("toggleDisabled", false);
            }
        };
    });
};