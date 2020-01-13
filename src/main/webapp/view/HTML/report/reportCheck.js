var checkId = getParameter("id");
var reportConfigId = getParameter("reportConfigId");
var projectNo = getParameter("reportConfigId");
var hwAccount = getCookie("username");
$(function () {
    initPage(reportConfigId);
});

function checkPassHandler() {
    let params = {
        id: checkId
    };
    $.ajax({
        url: getRootPath() + '/report/report_check/pass',
        type: 'post',
        data: params,
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                toastr.success(data.message);
                loadDatas();
            } else {
                toastr.error(data.message)
            }
            $("#success_dialog").modal("show");
        }
    });
};

function checkFailedHandler() {
    $("#audit_not_passed_reason").modal("show");
}

var closeHandler = function () {
    window.location.href = "reportChecks.html";
}

//保存不通过
function submitFailed() {
    let params = {
        id: checkId,
        description: $("#failedDescription").val()
    }
    if ($.trim(params.description).length == 0) {
        toastr.error("请输入审核不通过的原因！");
        return;
    }
    $.ajax({
        url: getRootPath() + '/report/report_check/failed',
        type: 'post',
        data: params,
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                toastr.success(data.message);
                loadDatas();
            } else {
                toastr.error(data.message)
            }
            $("#audit_not_passed_reason").modal("hide");
            $("#success_dialog").modal("show");
        }
    });
};
var cancelSubmitFailed = function () {
    $("#audit_not_passed_reason").modal("hide");
}
var createCoumns = function (kpiConfigRefs, data) {
    let columns = [];
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
    $('#dataItemManagement').bootstrapTable({
        editable: false,//可行内编辑
        sortable: true, //是否启用排序
        sortOrder: "asc",
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        striped: false, //是否显示行间隔色
        dataField: "rows",
        //showRefresh:true,//刷新按钮
        responseHandler: function (res) {
            return {
                "rows": res.data.rows,
                "total": res.data.rows.length
            }
        },
        // showColumns: true,
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        toolbar: '#toolbar',//指定工作栏
        queryParams: function (params) {
            let param = {
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
    $('#dataItemManagement').bootstrapTable("removeAll")
    $.ajax({
        method: 'post',
        url: getRootPath() + '/report/report_check/get_full_by_id',
        data: {"id": checkId},
        success: function (resp) {
            if (resp && resp.data && resp.data.report && resp.data.report.rows) {

                var rows = resp.data.report.rows;
                $('#dataItemManagement').bootstrapTable("load", rows)
            }
        }
    });
}

function initPage(report_config_id) {
    $('#dataItemManagement').bootstrapTable("destroy");
    $.ajax({
        url: getRootPath() + '/report/reportConfig/get_excluded_by_id_and_project_no',
        type: 'POST',
        dataType: "json",
        data: {id: report_config_id, projectNo: 10021},
        success: function (resp) {
            if (resp.code == 200) {
                let kpiConfigRefs = resp.data.kpiConfigRefs;
                let columns = createCoumns(kpiConfigRefs, resp);
                initTable(columns)
            }
        }
    });
};
