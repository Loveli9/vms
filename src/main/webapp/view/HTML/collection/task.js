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
            "entity.parameters": JSON.stringify(parameters),
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
                        toastr.success('操作成功!');
                    }
                }
            });
        } else {
            toastr.error('接口为必填字段！');
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
                        toastr.success('删除成功!');
                    }
                }
            });
        } else {
            toastr.error('请选择要删除的数据!');
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
            table_data = JSON.parse(select_ID.parameters)
        } else {
            toastr.error('请选择要编辑的数据!');
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
                }
            },

        ],
        locale: 'zh-CN',//中文支持,
    });
    //change事件
    $("#interface_type").on("change", function () {
        table_data = JSON.parse($("#interface_type").val());
        // table_data.forEach(e => {
        //     e.value = "";
        // })
        $('#interface_parameter_table').bootstrapTable('load', table_data)
    })
}