var metrics_table_id;
var metrics_table_config_id;
var showFlag = true;
var hwAccount = getCookie("username");
var projNo = getCookie("projNo_comm");
;$(function () {
    $.ajax({
        url: getRootPath() + '/report/metrics_table_config/query',
        type: 'post',
        data: {projectNo: projNo},
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                var htmlFragment = '';
                var appendHtml = '';
                for (let i = 0; i < data.data.length; i++) {
                    if (i == 0) {
                        htmlFragment = `<li role="presentation" id=${data.data[i].id} class="active" metricsTableConfigId=${data.data[i].id}><a href="#">${data.data[i].alias}</a></li>`;
                        appendHtml += htmlFragment;
                        metrics_table_config_id = data.data[i].id;
                        initPage();
                    } else {
                        htmlFragment = `<li role="presentation" id=${data.data[i].id} metricsTableConfigId=${data.data[i].id}><a href="#">${data.data[i].alias}</a></li>`;
                        appendHtml += htmlFragment;
                    }
                }
                $(".mtm-tabs").append(appendHtml)
            }
        }
    });

    $(".mtm-tabs").on("click", "li", function () {
        if (showFlag) {
            $(this).addClass("active").siblings('li').removeClass("active");
            metrics_table_config_id = $(this).attr("metricsTableConfigId");
            initPage();
        }
    });

    //删除方法
    $("#delete_table_item").click(function () {
        var id = $('#dataItemManagement').bootstrapTable('getSelections')[0];
        if (id) {
            $.ajax({
                url: getRootPath() + '/report/metrics_row/delete?id=' + id.id,
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        $('#dataItemManagement').bootstrapTable('refresh');
                        toastr.success('删除成功！');
                    } else {
                        toastr.error('删除失败!');
                    }
                }
            });
        } else {
            toastr.error('请选择要删除的数据');
        }
    });

    //取消方法
    $("#edit_backBtn_cancel").click(function () {
        $("#edit_add_page_id").modal("hide");
    });

    //数据收集
    $("#btn_data_collect").click(function () {
        console.log("数据收集")
    });

    //数据补齐
    $("#btn_data_makeUup").click(function () {
        $.ajax({
            url: getRootPath() + '/report/metrics_table/repair',
            data: {
                metricsTableConfigId: metrics_table_config_id,
                projectNo: projNo
            },
            success: function (resp) {
                if (resp.succeed) {
                    toastr.success(resp.message);
                } else {
                    toastr.success(resp.message);
                }
            }
        })
    });

});
var dateFormat = function (fmt, date) {
    let ret;
    let opt = {
        "Y+": date.getFullYear().toString(),        // 年
        "m+": (date.getMonth() + 1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "H+": date.getHours().toString(),           // 时
        "M+": date.getMinutes().toString(),         // 分
        "S+": date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
    };
    for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        }
    }
    return fmt;
}

//初始化方法
function initPage() {
    if (showFlag) {
        showFlag = false;
        $('#dataItemManagement').bootstrapTable("destroy")
        let tableHead = [{
            title: '全选',
            field: 'select',
            radio: true,
            width: 30,
            align: 'center',
            valign: 'middle',
        }];
        $.ajax({
            url: getRootPath() + '/report/metrics_table_config/get_full_by_id',
            type: 'post',
            data: {id: metrics_table_config_id},
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    if (data.data.period == '迭代内') {
                        tableHead.push({
                            title: "迭代",
                            halign: 'center',
                            align: 'center',
                            sortable: false,
                            width: 70,
                            formatter: function (value, row, index) {
                                var itemValue;
                                for (var j = 0; j < row.metricsItems.length; j++) {
                                    if (row.metricsItems[j].metricsItemConfigId == "v_iteration_name") {
                                        itemValue = row.metricsItems[j].value;
                                    }
                                }
                                return itemValue
                            }
                        });
                        tableHead.push({
                            title: "时间段",
                            halign: 'center',
                            align: 'center',
                            sortable: false,
                            width: 70,
                            formatter: function (value, row, index) {
                                var itemValue;
                                for (let j = 0; j < row.metricsItems.length; j++) {
                                    if (row.metricsItems[j].metricsItemConfigId == "v_iteration_times") {
                                        itemValue = row.metricsItems[j].value;
                                    }
                                }
                                return itemValue
                            }
                        });
                    }
                    if (data.data.type == "人员") {
                        tableHead.push({
                            title: "工号",
                            halign: 'center',
                            align: 'center',
                            sortable: false,
                            width: 70,
                            formatter: function (value, row, index) {
                                var itemValue;
                                for (var j = 0; j < row.metricsItems.length; j++) {
                                    if (row.metricsItems[j].metricsItemConfigId == "v_code") {
                                        itemValue = row.metricsItems[j].value;
                                    }
                                }
                                return itemValue
                            }
                        });
                        tableHead.push({
                            title: "姓名",
                            halign: 'center',
                            align: 'center',
                            sortable: false,
                            width: 70,
                            formatter: function (value, row, index) {
                                var itemValue;
                                for (var j = 0; j < row.metricsItems.length; j++) {
                                    if (row.metricsItems[j].metricsItemConfigId == "v_name") {
                                        itemValue = row.metricsItems[j].value;
                                    }
                                }
                                return itemValue
                            }
                        });
                    }
                    var useData = data.data.metricsItemConfigs;
                    for (let i = 0; i < useData.length; i++) {
                        var item = {
                            title: useData[i].fieldAlias,
                            halign: 'center',
                            align: 'center',
                            sortable: false,
                            width: 70,
                            formatter: function (value, row, index) {
                                var itemValue;
                                for (let j = 0; j < row.metricsItems.length; j++) {
                                    if (row.metricsItems[j].metricsItemConfigId == useData[i].id) {
                                        itemValue = row.metricsItems[j].value;
                                    }
                                }
                                return itemValue
                            }
                        };
                        tableHead.push(item)
                    }
                    ;
                    $('#dataItemManagement').bootstrapTable({
                        method: 'post',
                        contentType: "application/x-www-form-urlencoded",
                        url: getRootPath() + '/report/metrics_table/get_by_project_id_and_metrics_table_config_id?metricsTableConfigId=' + metrics_table_config_id + "&projectId=" + projNo,
                        editable: false,//可行内编辑
                        sortable: true, //是否启用排序
                        sortOrder: "asc",
                        striped: false, //是否显示行间隔色
                        dataField: "rows",
                        pageNumber: 1, //初始化加载第一页，默认第一页
                        pagination: false,//是否分页
                        queryParamsType: 'limit',
                        sidePagination: 'server',
                        onDblClickRow: function () {
                            add_editBtn('editMsg')
                        },
                        onLoadSuccess: function () {
                            showFlag = true;
                        },
                        pageSize: 10,//单页记录数
                        pageList: [5, 10, 20, 30],//分页步进值
                        //showRefresh:true,//刷新按钮
                        responseHandler: function (res) {
                            metrics_table_id = res.data.id;
                            if (res.data.metricsRows.length > 0 && res.data.metricsRows[0].period == '迭代内') {
                                for (var i = 0; i < res.data.metricsRows.length; i++) {
                                    var row = res.data.metricsRows[i];
                                    var it = dateFormat('YYYY/mm/dd', new Date(row.periodStartDate)) + '~' + dateFormat('YYYY/mm/dd', new Date(row.periodEndDate));
                                    row.metricsItems.splice(0, 0, {
                                        metricsItemConfigId: 'v_iteration_name',
                                        value: row.periodName
                                    });
                                    row.metricsItems.splice(1, 0, {
                                        metricsItemConfigId: 'v_iteration_times',
                                        value: it
                                    });
                                    row.metricsItems.splice(1, 0, {
                                        metricsItemConfigId: 'v_code',
                                        value: row.code
                                    });
                                    row.metricsItems.splice(1, 0, {
                                        metricsItemConfigId: 'v_name',
                                        value: row.name
                                    });
                                }
                            }
                            return {
                                "rows": res.data.metricsRows,
                                "total": res.data.metricsRows.length
                            }
                        },
                        // showColumns: true,
                        minimumCountColumns: 2, //最少允许的列数
                        clickToSelect: true,//是否启用点击选中行
                        toolbarAlign: 'right',
                        buttonsAlign: 'right',//按钮对齐方式
                        toolbar: '#toolbar',//指定工作栏
                        queryParams: function (params) {
                            var param = {
                                metricsTableConfigId: metrics_table_config_id,
                                hwAccount: hwAccount,
                                projectId: projNo
                            }
                            return JSON.stringify(param);
                        },
                        columns: tableHead,
                        locale: 'zh-CN',//中文支持,
                    });
                }
            }
        });
    }
};

//新增方法
function add_editBtn(parameters) {
    $.ajax({
        url: getRootPath() + '/report/metrics_table_config/get_full_by_id?id=' + metrics_table_config_id,
        type: 'post',
        data: {id: metrics_table_config_id},
        dataType: "json",
        contentType: 'application/json;charset=utf-8', //设置请求头信息 
        success: function (data) {
            if (data.code == 200) {
                if (parameters == 'addMsg') {
                    $('#dataItemManagement').bootstrapTable('uncheckAll');
                    $("#edit_add_page_id").modal("show");
                    for (let i = 0; i < data.data.metricsItemConfigs.length; i++) {
                        data.data.metricsItemConfigs[i].collectedValue = 0;
                        data.data.metricsItemConfigs[i].inputValue = 0;
                    }
                    $('#add_edit_table').bootstrapTable("destroy")
                    $('#add_edit_table').bootstrapTable({
                        editable: true,//可行内编辑
                        sortable: true, //是否启用排序
                        sortOrder: "asc",
                        striped: false, //是否显示行间隔色
                        dataField: "rows",
                        queryParamsType: 'limit',
                        sidePagination: 'server',
                        data: data.data.metricsItemConfigs,
                        // showColumns: true,
                        minimumCountColumns: 2, //最少允许的列数
                        clickToSelect: true,//是否启用点击选中行
                        toolbarAlign: 'right',
                        buttonsAlign: 'right',//按钮对齐方式
                        columns: [
                            {
                                title: '度量项',
                                halign: 'center',
                                align: 'center',
                                field: 'fieldAlias',
                                sortable: false,
                                width: 70,
                            },
                            {
                                title: '采集值',
                                halign: 'center',
                                align: 'center',
                                field: 'collectedValue',
                                sortable: false,
                                width: 70,
                            },
                            {
                                title: '录入值',
                                halign: 'center',
                                align: 'center',
                                field: 'inputValue',
                                sortable: false,
                                width: 70,
                                formatter: function (value, row, index) {
                                    rowData = row;
                                    let displayValue = '';
                                    switch (row.dataType) {
                                        case "int":
                                            displayValue = 0;
                                            break;
                                        case "float":
                                            displayValue = 0;
                                            break;
                                        case "String":
                                            displayValue = "";
                                            break;
                                        case "date":
                                            let nowTime = new Date();
                                            let year = nowTime.getFullYear();
                                            let month = nowTime.getMonth() + 1;
                                            let day = nowTime.getDate();
                                            displayValue = year + "-" + month + "-" + day;
                                            break;
                                    }
                                    return "<span class='inputValue'>" + displayValue + "</span>"
                                }
                            },
                        ],
                        locale: 'zh-CN',//中文支持,
                    });
                } else if (parameters == 'editMsg') {
                    let selectMsg = $('#dataItemManagement').bootstrapTable('getSelections')[0];
                    let dataVal = geT_edit_msg();
                    if (selectMsg != undefined) {
                        $("#edit_add_page_id").modal("show");
                        $('#add_edit_table').bootstrapTable("destroy")
                        $('#add_edit_table').bootstrapTable({
                            editable: true,//可行内编辑
                            sortable: true, //是否启用排序
                            sortOrder: "asc",
                            striped: false, //是否显示行间隔色
                            dataField: "rows",
                            queryParamsType: 'limit',
                            sidePagination: 'server',
                            data: data.data.metricsItemConfigs,
                            // showColumns: true,
                            minimumCountColumns: 2, //最少允许的列数
                            clickToSelect: true,//是否启用点击选中行
                            toolbarAlign: 'right',
                            buttonsAlign: 'right',//按钮对齐方式
                            columns: [
                                {
                                    title: '度量项',
                                    halign: 'center',
                                    align: 'center',
                                    field: 'fieldAlias',
                                    sortable: false,
                                    width: 70,
                                },
                                {
                                    title: '采集值',
                                    halign: 'center',
                                    align: 'center',
                                    field: 'collectedValue',
                                    sortable: false,
                                    width: 70,
                                    formatter: function (value, row, index) {
                                        var collectedValue = "";
                                        for (let i = 0; i < dataVal.data.metricsItems.length; i++) {
                                            if (dataVal.data.metricsItems[i].metricsItemConfigId == row.id) {
                                                collectedValue = dataVal.data.metricsItems[i].collectedValue;
                                            }
                                        }
                                    }
                                },
                                {
                                    title: '录入值',
                                    halign: 'center',
                                    align: 'center',
                                    sortable: false,
                                    field: 'inputValue',
                                    width: 70,
                                    formatter: function (value, row, index) {
                                        var inputValue = "";
                                        for (let i = 0; i < dataVal.data.metricsItems.length; i++) {
                                            if (dataVal.data.metricsItems[i].metricsItemConfigId == row.id) {
                                                inputValue = dataVal.data.metricsItems[i].inputValue;
                                            }
                                        }
                                        (inputValue == null || inputValue == undefined) ? inputValue = "" : inputValue;
                                        return "<span class='inputValue'>" + inputValue + "</span>"
                                    }
                                },

                            ],
                            locale: 'zh-CN',//中文支持,
                        });
                    } else {
                        toastr.error('请选择要编辑的数据');
                    }
                }
                for (let i = 0; i < data.data.metricsItemConfigs.length; i++) {
                    var target = $("#add_edit_table tr[data-index='" + i + "'] .inputValue");
                    let metricsItemConfig = data.data.metricsItemConfigs[i];
                    if (metricsItemConfig.readOnly != true) {
                        target.editable({
                            type: 'text',
                            title: '输入数据',
                            validate: function (value) {
                                let flag = false;
                                if (metricsItemConfig.dataType == "int" || metricsItemConfig.dataType == "float") {
                                    if (!isNaN(value)) {
                                        flag = true;
                                    }
                                } else {
                                    flag = true;
                                }
                                if (!flag) {
                                    flag = !flag
                                    return "请输入纯数字"
                                }
                            }
                        });
                    }
                    if (metricsItemConfig.dataType == "date") {
                        target.editable({
                            type: 'datetime',
                            title: '录入数据',
                        });
                    }
                }
            }
        }
    });
};


function geT_edit_msg() {
    var idCode = $('#dataItemManagement').bootstrapTable('getSelections')[0];
    let returnVal;
    if (idCode != undefined) {
        $.ajax({
            url: getRootPath() + '/report/metrics_row/get_full_by_id?id=' + idCode.id,
            type: 'post',
            dataType: "json",
            async: false,
            data: {id: idCode.id},
            contentType: 'application/json;charset=utf-8', //设置请求头信息 
            success: function (data) {
                returnVal = data;
            }
        });
    }
    return returnVal
};

//保存按钮
function submit_line_message() {
    let selectID = $('#dataItemManagement').bootstrapTable('getSelections')[0];
    let submitVal = $('#add_edit_table').bootstrapTable('getData');
    let arr = [];
    $("#add_edit_table td").each(function () {
        arr.push($(this).text());
    });
    let params = {
        id: selectID ? selectID.id : null,
        metricsTableId: metrics_table_id,
        metricsItems: []
    };
    let paramsItem = {};
    for (let i = 0; i < submitVal.length; i++) {
        paramsItem = {};
        paramsItem.collectedValue = arr[i * 3 + 1];
        paramsItem.inputValue = arr[i * 3 + 2] == "Empty" ? "-" : arr[i * 3 + 2];
        paramsItem.metricsTableId = metrics_table_id;
        paramsItem.metricsItemConfigId = submitVal[i].id;
        params.metricsItems.push(paramsItem);
    }
    ;
    $.ajax({
        url: getRootPath() + '/report/metrics_row/save',
        type: 'post',
        data: JSON.stringify(params),
        dataType: "json",
        contentType: 'application/json;charset=utf-8', //设置请求头信息 
        success: function (data) {
            if (data.code == 200) {
                toastr.success('操作成功！');
                $("#edit_add_page_id").modal("hide");
                $('#dataItemManagement').bootstrapTable('refresh');
            }
        }
    });
}

