var report_config_id;
var hwAccount = getCookie("username");
var projNo = getCookie("projNo_comm");
$(function () {
    $.ajax({
        url: getRootPath() + '/report/reportConfig/list_excluded_by_project_no',
        type: 'post',
        data: {projectNo: projNo},
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                var htmlFragment = '';
                var appendHtml = '';
                if (data.data.length > 0) {
                    for (let i = 0; i < data.data.length; i++) {
                        if (i == 0) {
                            htmlFragment = `<li role="presentation" id=${data.data[i].id} class="active" metricsTableConfigId=${data.data[i].projectId}><a href="#">${data.data[i].name}</a></li>`;
                            appendHtml += htmlFragment;
                        } else {
                            htmlFragment = `<li role="presentation" id=${data.data[i].id} metricsTableConfigId=${data.data[i].projectId}><a href="#">${data.data[i].name}</a></li>`;
                            appendHtml += htmlFragment;
                        }
                    }
                    $(".rdm-tabs").append(appendHtml)
                    report_config_id = data.data[0].id;
                    initPage(report_config_id);
                }
            }
        }
    });

    $(".rdm-tabs").on("click", "li", function () {
        $(this).addClass("active").siblings('li').removeClass("active");
        report_config_id = $(this).attr("id");
        initPage(report_config_id);
    });
    //删除方法
    $("#delete_table_item").click(function () {
        var selections = $('#dataItemManagement').bootstrapTable('getSelections');
        if (selections.length <= 0) {
            toastr.error('请选择需要删除的数据！');
            return;
        }
        var ids = [];
        for (var i = 0; i < selections.length; i++) {
            var item = selections[i];
            if (item.checkStatus == 2) {
                toastr.error('“' + item.periodName + '”数据已经审核通过，禁止删除！');
                return;
            } else if (item.checkStatus == 1) {
                toastr.error('“' + item.periodName + '”数据已经提交审核，禁止删除！');
                return;
            }
            ids.push(item.id);
        }
        var params = {ids: ids}
        $.ajax({
            url: getRootPath() + '/report/report_row/delete',
            type: 'post',
            method: "post",
            data: JSON.stringify(params),
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (data) {
                if (data.code == 200) {
                    loadDatas();
                    toastr.success('删除成功！');
                } else {
                    toastr.error('删除失败!');
                }
            }
        });
    });

    //提交审核方法
    $("#btn_submit_review").click(function () {
        var selections = $('#dataItemManagement').bootstrapTable('getSelections');
        if (selections.length <= 0) {
            toastr.error('请选择需要审核的数据！');
            return;
        }
        var ids = [];
        for (var i = 0; i < selections.length; i++) {
            var item = selections[i];
            if (item.checkStatus == 2) {
                toastr.error('“' + item.periodName + '”数据已经审核通过，不需要再审核！');
                return;
            } else if (item.checkStatus == 1) {
                toastr.error('“' + item.periodName + '”数据已经提交审核，不能重复提交！');
                return;
            }
            ids.push(item.id);
        }
        var params = {
            projectId: projNo,
            reportId: selections[0].reportId,
            reportRowIds: ids.join(","),
            submitor: hwAccount,
        };
        $.ajax({
            url: getRootPath() + '/report/report_check/submit',
            type: 'post',
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            data: JSON.stringify(params),
            success: function (data) {
                if (data.code == 200) {
                    loadDatas();
                    toastr.success(data.message);
                } else {
                    toastr.error(data.message);
                }
            }
        });
    });

    var initDialog = function (selection) {
        $("#cancelCheckDialog").modal("show");
        $('#reportChecks').bootstrapTable({
            striped: true, //是否显示行间隔色
            minimumCountColumns: 2, //最少允许的列数
            clickToSelect: true,//是否启用点击选中行
            locale: 'zh-CN',
            columns: [{
                title: "审核状态",
                field: 'checkStatus',
                halign: 'center',
                align: 'center',
                sortable: false,
                formatter: function (value, row) {
                    var checkStatu;
                    switch (row.checkStatus) {
                        case 0:
                            checkStatu = "待提审";
                            break;
                        case 1:
                            checkStatu = "待审核";
                            break;
                        case 2:
                            checkStatu = "审核通过";
                            break;
                        case 3:
                            checkStatu = "审核不过";
                            break;
                    }
                    return checkStatu
                }
            }]
        });
        $.ajax({
            url: getRootPath() + '/report/report_check/get_full_by_id',
            type: 'post',
            data: {id: selection.reportCheckId},
            success: function (resp) {
                $('#reportChecks').bootstrapTable("load", resp.data.report.rows);
            }
        });
    }
    //撤回审核方法
    $("#btn_submit_cancel").click(function () {
        var selections = $('#dataItemManagement').bootstrapTable('getSelections');
        if (selections.length <= 0) {
            toastr.error('请选择需要撤回审核的数据！');
            return;
        }
        for (var i = 0; i < selections.length; i++) {
            var item = selections[i];
            if (item.checkStatus != 1) {
                toastr.error('“' + item.periodName + '”数据不是待审核状态，不能执行撤回！');
                return;
            }
        }
        initDialog(selections[0]);
    });

    //取消方法
    $("#edit_backBtn_cancel").click(function () {
        $("#edit_add_page_id").modal("hide");
    });
    $("#btn_data_recalu").click(function () {
        if ($('#dataItemManagement').bootstrapTable('getSelections').length > 0) {
            var id = $('#dataItemManagement').bootstrapTable('getSelections')[0].id;
            $.ajax({
                url: getRootPath() + '/report/report_row/recalculate',
                data: {id: id},
                success: function (resp) {
                    if (resp.success) {
                        toastr.success(resp.result);
                    } else {
                        toastr.error(resp.errorsList);
                    }
                    loadDatas();
                }
            })
        } else {
            toastr.error('请选择数据');
        }
    });

    $("#btn_data_repar").click(function () {
        $.ajax({
            url: getRootPath() + '/report/table/repair',
            data: {reportConfigId: report_config_id, projectId: projNo},
            type: 'post',
            success: function (resp) {
                if (resp.success) {
                    toastr.success(resp.result);
                } else {
                    toastr.error(resp.result);
                }
                loadDatas();
            }
        })
    });
    $("#btn_add").click(function () {
        doEdit(null)
    });
    $("#btn_edit").click(function () {
        var selections = $('#dataItemManagement').bootstrapTable('getSelections');
        if (selections.length <= 0) {
            toastr.error('请选择需要编辑的数据！');
            return;
        }
        for (var i = 0; i < selections.length; i++) {
            var item = selections[i];
            if (item.checkStatus == 2) {
                toastr.error('“' + item.periodName + '”数据已经审核通过，禁止修改！');
                return;
            } else if (item.checkStatus == 1) {
                toastr.error('“' + item.periodName + '”数据已经提交审核，禁止修改！');
                return;
            }
        }
        doEdit(selections[0]);
    });
});

var doCancelCheck = function () {
    var selections = $('#dataItemManagement').bootstrapTable('getSelections');

    $.ajax({
        url: getRootPath() + '/report/report_check/cancel',
        type: 'post',
        data: {id: selections[0].reportCheckId},
        success: function (resp) {
            if (resp.succeed) {
                toastr.success(resp.message);
                loadDatas();
            } else {
                toastr.error(resp.message);
            }
            $("#cancelCheckDialog").modal("hide");
        }
    });
}
var createCoumns = function (kpiConfigRefs, data) {
    let columns = [{
        title: '全选',
        field: 'select',
        checkbox: true,
        align: 'center',
        valign: 'middle',
        width: '30'
    }];
    if (data.data.period == 2) {
        columns.push();
    }
    for (let i = 0; i < kpiConfigRefs.length; i++) {
        let reportKpiConfig = kpiConfigRefs[i].reportKpiConfig;
        if (reportKpiConfig) {
            let item = {
                    title: reportKpiConfig.kpiName,
                    halign: 'center',
                    align: 'center',
                    field: 'reportKpiConfig',
                    sortable: false,
                    formatter: function (value, row, index) {
                        let itemValue;
                        let kpiConfigRef = null;
                        var kpi = null;
                        for (let j = 0; j < row.kpis.length; j++) {
                            kpiConfigRef = kpiConfigRefs[i];
                            let id1 = row.kpis[j].reportKpiConfigId;
                            let id2 = kpiConfigRef.reportKpiConfigId;
                            if (id1 == id2) {
                                kpi = row.kpis[j];
                                itemValue = kpi.value;
                            }
                        }
                        if (!$.isEmptyObject(kpi)) {
                            if (kpi.status != true) {
                                let title = "，";
                                if (kpiConfigRef.maxValue && kpiConfigRef.reportKpiConfig.maxValue) {
                                    title += ",最大值：" + kpiConfigRef.maxValue || kpiConfigRef.reportKpiConfig.maxValue;
                                }
                                if (kpiConfigRef.minValue && kpiConfigRef.reportKpiConfig.minValue) {
                                    title += ",最小值：" + kpiConfigRef.minValue || kpiConfigRef.reportKpiConfig.minValue;
                                }
                                if (kpiConfigRef.targetValue) {
                                    title += ",目标值：" + kpiConfigRef.targetValue || kpiConfigRef.reportKpiConfig.targetValue;
                                }
                                if (kpiConfigRef.lightUpRule) {
                                    var map = {max: "最大值优先", min: "最小值优先", target: "目标值优先"};
                                    title += ",点灯规则：" + map[kpiConfigRef.lightUpRule || kpiConfigRef.reportKpiConfig.lightUpRule];
                                }
                                return '<span style="color:red;" title="' + title.substr(2, title.length) + '">' + itemValue + '</span>'
                            } else {
                                return itemValue
                            }
                        }
                    }
                }
            ;
            if (reportKpiConfig.width != undefined && reportKpiConfig.width != null) {
                item.width = reportKpiConfig.width;
            }
            columns.push(item)
        }
    }
    columns.push({
        title: "状态",
        halign: 'center',
        align: 'center',
        sortable: false,
        width: 70,
        formatter: function (value, row, index) {
            let checkStatu;
            switch (row.checkStatus) {
                case 0:
                    checkStatu = "待提审";
                    break;
                case 1:
                    checkStatu = "待审核";
                    break;
                case 2:
                    checkStatu = "审核通过";
                    break;
                case 3:
                    checkStatu = "审核不过";
                    break;
            }
            return checkStatu
        }
    });
    return columns;
}
var initTable = function (columns) {
    var target = $('#dataItemManagement[cid="' + report_config_id + '"]');
    if (target.length <= 0) {
        return;
    }
    target.bootstrapTable({
        editable: false,//可行内编辑
        sortable: true, //是否启用排序
        sortOrder: "asc",
        singleSelect: false,
        striped: false, //是否显示行间隔色
        dataField: "rows",
        //showRefresh:true,//刷新按钮
        responseHandler: function (res) {
            return {
                "rows": res.data.rows,
                "total": res.data.rows.length
            }
        },
        onDblClickRow: function (row, el, columnIndex) {
            if (row.checkStatus == 2) {
                toastr.error('“' + row.periodName + '”数据已经审核通过，禁止修改！');
                return;
            } else if (row.checkStatus == 1) {
                toastr.error('“' + row.periodName + '”数据已经提交审核，禁止修改！');
                return;
            }
            target.bootstrapTable("uncheckAll");
            target.bootstrapTable("checkBy", {field: "id", values: [row.id]})
            doEdit(row)
        },
        // showColumns: true,
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        toolbar: '#toolbar',//指定工作栏
        queryParams: function (params) {
            let param = {
                id: report_config_id,
                hwAccount: hwAccount,
            }
            return JSON.stringify(param);
        },
        columns: columns,
        locale: 'zh-CN',//中文支持,
    });
    loadDatas();
}
var loadDatas = function () {
    $.ajax({
        method: 'post',
        url: getRootPath() + '/report/table/get_full_by_project_id_and_report_config_id',
        data: {reportConfigId: report_config_id, projectId: projNo},
        success: function (resp) {
            var target = $('#dataItemManagement[cid="' + report_config_id + '"]');
            if (target.length <= 0) {
                return;
            }
            target.bootstrapTable("load", resp.data.rows)
        }
    });
}

function initPage(report_config_id) {
    // $('#dataItemManagement').bootstrapTable("destroy")
    $('#dataItemManagement').bootstrapTable("destroy");
    $('#dataItemManagement').attr("cid", report_config_id);
    $.ajax({
        url: getRootPath() + '/report/reportConfig/get_excluded_by_id_and_project_no',
        type: 'POST',
        dataType: "json",
        data: {id: report_config_id, projectNo: projNo},
        success: function (resp) {
            if (resp.code == 200) {
                let kpiConfigRefs = resp.data.kpiConfigRefs;
                let columns = createCoumns(kpiConfigRefs, resp);
                initTable(columns)
            }
        }
    });
};

//新增方法
function doEdit(data) {
    $.ajax({
        url: getRootPath() + '/report/reportConfig/get_by_id',
        type: 'post',
        data: {id: report_config_id},
        dataType: "json",
        success: function (resp) {
            if (resp.code == 200) {
                if (!data) {
                    $('#dataItemManagement').bootstrapTable('uncheckAll');
                    $("#edit_add_page_id").modal("show");
                    for (let i = 0; i < resp.data.kpiConfigRefs.length; i++) {
                        resp.data.kpiConfigRefs[i].value = 0;
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
                        data: resp.data.kpiConfigRefs,
                        // showColumns: true,
                        minimumCountColumns: 2, //最少允许的列数
                        clickToSelect: true,//是否启用点击选中行
                        toolbarAlign: 'right',
                        buttonsAlign: 'right',//按钮对齐方式
                        columns: [
                            {
                                title: '指标',
                                halign: 'center',
                                align: 'center',
                                field: 'reportKpiConfig',
                                sortable: false,
                                width: 70,
                                formatter: function (value, row, index) {
                                    return value.kpiName;
                                }
                            },
                            {
                                title: '值',
                                halign: 'center',
                                align: 'center',
                                field: 'value',
                                sortable: false,
                                width: 70,
                                formatter: function (value, row, index) {
                                    var inputVal = "";
                                    return '<span class="inputValue">' + inputVal + '</span>'
                                }
                            },

                        ],
                        locale: 'zh-CN',//中文支持,
                    });
                } else if (data) {
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
                        data: resp.data.kpiConfigRefs,
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
                                field: 'reportKpiConfig',
                                sortable: false,
                                width: 70,
                                formatter: function (value, row, index) {
                                    return value.kpiName;
                                }
                            },
                            {
                                title: '值',
                                halign: 'center',
                                align: 'center',
                                sortable: false,
                                field: 'value',
                                width: 70,
                                formatter: function (value, row, index) {
                                    var inputVal = "";
                                    for (let i = 0; i < data.kpis.length; i++) {
                                        if (data.kpis[i].reportKpiConfigId == row.reportKpiConfigId) {
                                            inputVal = data.kpis[i].value;
                                        }
                                    }
                                    return '<span class="inputValue">' + inputVal + '</span>'
                                }
                            },

                        ],
                        locale: 'zh-CN',//中文支持,
                    });
                }

                for (let i = 0; i < resp.data.kpiConfigRefs.length; i++) {
                    var target = $("#add_edit_table tr[data-index='" + i + "'] .inputValue");
                    let ref = resp.data.kpiConfigRefs[i].reportKpiConfig;
                    if (ref.expression != undefined && ref.expression != null && $.trim(ref.expression) != "" && ref.readOnly != true) {
                        target.editable({
                            type: 'text',
                            title: '输入数据',
                            validate: function (value) {
                                let flag = false;
                                if (ref.dataType == "int" || ref.dataType == "float") {
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
                    if (ref.dataType == "date") {
                        target.editable({
                            type: 'datetime',
                            title: '输入数据',
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
    if (idCode) {
        $.ajax({
            url: getRootPath() + '/report/report_row/get_full_by_id?id=' + idCode.id,
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
    let arr = [];
    let selectID = $('#dataItemManagement').bootstrapTable('getSelections')[0];
    var submitVals = $('#add_edit_table').bootstrapTable('getData');
    var params = {
        id: selectID ? selectID.id : null,
        reportId: selectID.reportId,
        kpis: []
    };
    $("#add_edit_table td").each(function () {
        arr.push($(this).text());
    });
    var paramsItem = {};
    for (var i = 0; i < submitVals.length; i++) {
        var submitRow = submitVals[i];
        paramsItem = {};
        paramsItem.value = arr[i * 2 + 1] == "Empty" ? "-" : arr[i * 2 + 1];
        paramsItem.reportId = selectID.reportId;
        paramsItem.reportKpiConfigId = submitRow.reportKpiConfigId;
        params.kpis.push(paramsItem);
    }
    $.ajax({
        url: getRootPath() + '/report/report_row/save',
        type: 'post',
        data: JSON.stringify(params),
        dataType: "json",
        contentType: 'application/json;charset=utf-8', //设置请求头信息
        success: function (data) {
            if (data.code == 200) {
                toastr.success('操作成功！');
                $("#edit_add_page_id").modal("hide");
                loadDatas();
            }
        }
    });
}

