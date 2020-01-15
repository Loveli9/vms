; $(function () {
    $('#task_table').bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: "http://192.168.0.11:8080/datacollection/collectionschedule/query",
        editable: false,//可行内编辑
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        sortable: false, //是否启用排序
        sortOrder: "asc",
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 40], //分页步进值
        striped: false, //是否显示行间隔色
        dataField: "rows",
        queryParamsType: 'limit',
        sidePagination: 'server',
        queryParams: function (params) {
            var param = {
                "pageable.size": params.limit, //页面大小
                "pageable.page": params.pageNumber - 1,
                "pageable.start": params.limit * (params.pageNumber - 1),
                "pageable.sort": '[{ "property": "id", "direction": "DESC" }]'
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
                title: '计划名称',
                halign: 'center',
                align: 'center',
                field: 'name',
                sortable: false,
                width: 70,
            },
            {
                title: '任务名称',
                halign: 'center',
                align: 'center',
                sortable: false,
                width: 70,
                formatter: function (value, row, index) {
                    return row.task.name
                }
            },
            {
                title: '开始时间',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'startDatetime',
                width: 70,
            },
            {
                title: 'CRON表达式',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'cron',
                width: 70,
            },
            {
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
    $("#collection_plan_back_btn").on("click", function () {
        $("#edit_interface").modal("hide");
    });

    //保存
    $("#collection_plan_save_btn").on("click", function () {
        let select_ID = $('#task_table').bootstrapTable('getSelections')[0];
        let params = {
            "entity.id": select_ID ? select_ID.id : "",
            "entity.startDatetime": $("#collection_plan_date").val() + " " + $("#collection_plan_time").val()+":00",
            "entity.name": $("#collection_plan_name").val(),
            "entity.disabled": $("#collection_plan_stop").val(),
            "entity.task.id": $("#collection_plan_type option:selected").attr("taskId"),
            "entity.startDate": $("#collection_plan_date").val(),
            "entity.startTime": $("#collection_plan_time").val()+":00",
            "entity.description": $("#collection_plan_des").val(),
            "entity.cron": $("#collection_plan_cron").val()
        };
        $.ajax({
            contentType: "application/x-www-form-urlencoded", //设置请求头信息
            url: "http://192.168.0.11:8080/datacollection/collectionschedule/save",
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
    });

    //删除
    $("#delete_table_item").on("click", function () {
        let select_ID = $('#task_table').bootstrapTable('getSelections')[0];
        if (select_ID) {
            $.ajax({
                contentType: "application/x-www-form-urlencoded", //设置请求头信息
                url: "http://192.168.0.11:8080/datacollection/collectionschedule/delete",
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
        url: "http://192.168.0.11:8080/datacollection/collectiontask/all",
        type: 'post',
        method: 'post',
        async: false,
        success: function (res) {
            if (res.success) {
                $("#collection_plan_type").empty();
                var htmlFragment = "";
                var appendHtml = "";
                for (let i = 0; i < res.data.length; i++) {
                    htmlFragment = `<option value=${res.data[i].name} taskId=${res.data[i].id}>${res.data[i].name}</option>`;
                    appendHtml += htmlFragment;
                    htmlFragment = "";
                };
                $("#collection_plan_type").append(appendHtml)
            }
        }
    });
};
//新增，编辑
function add_editBtn(message) {
    collectioninterface();
    if (message == "editMsg") {
        let select_ID = $('#task_table').bootstrapTable('getSelections')[0];
        if (select_ID) {
            $("#edit_interface").modal("show");
            $("#collection_plan_name").val(select_ID.name);
            $("#collection_plan_stop").val(select_ID.disabled ? "true" : "false");
            $("#collection_plan_type").val(select_ID.task.name);
            $("#collection_plan_date").val(select_ID.startDate);
            $("#collection_plan_time").val(select_ID.startTime);
            $("#collection_plan_des").val(select_ID.description);
            $("#collection_plan_cron").val(select_ID.cron);
        } else {
            window.top.toastr.error('请选择要编辑的数据!');
        }
    } else if (message == "addMsg") {
        $('#task_table').bootstrapTable('uncheckAll');
        $("#edit_interface").modal("show");
        $("#collection_plan_name").val("");
        $("#collection_plan_stop").val("");
        $("#collection_plan_type").val("");
        $("#collection_plan_time").val("");
        $("#collection_plan_date").val("");
        $("#collection_plan_des").val("");
        $("#collection_plan_cron").val("");
    }
};
