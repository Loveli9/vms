/**
 * 访问页面时传入报表ID（report.html?id=xxx）
 */
$(function () {
//获取url中的参数
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]);
        return null; //返回参数值
    }

    var id = getUrlParam("id");
    loadDatas(id)
});
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
                                if (kpiConfigRef.maxValue) {
                                    title += ",最大值：" + kpiConfigRef.maxValue;
                                }
                                if (kpiConfigRef.minValue) {
                                    title += ",最小值：" + kpiConfigRef.minValue;
                                }
                                if (kpiConfigRef.targetValue) {
                                    title += ",目标值：" + kpiConfigRef.targetValue;
                                }
                                if (kpiConfigRef.lightUpRule) {
                                    var map = {max: "最大值优先", min: "最小值优先", target: "目标值优先"};
                                    title += ",点灯规则：" + map[kpiConfigRef.lightUpRule];
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
    return columns;
}
var initTable = function (columns) {
    var target = $('#dataItemManagement');
    if (target.length <= 0) {
        return;
    }
    target.bootstrapTable({
        editable: false,//可行内编辑
        sortable: true, //是否启用排序
        sortOrder: "asc",
        striped: false, //是否显示行间隔色
        dataField: "rows",
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        toolbar: '#toolbar',//指定工作栏
        columns: columns,
        locale: 'zh-CN',//中文支持,
    });
}
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

function loadDatas(id) {
    // $('#dataItemManagement').bootstrapTable("destroy")
    $('#dataItemManagement').bootstrapTable("destroy");
    $.ajax({
        method: 'post',
        url: getRootPath() + '/report/table/get_full_and_passed_by_id',
        data: {"id": id},
        success: function (resp) {
            var data = resp.data;
            $.ajax({
                url: getRootPath() + '/report/reportConfig/get_excluded_by_id_and_project_no',
                type: 'post',
                data: {id: data.reportConfigId, projectNo: data.projectId},
                dataType: "json",
                success: function (resp) {
                    var config = resp.data;
                    $("#table_name").html(config.name);
                    var kpiConfigRefs = config.kpiConfigRefs;
                    var columns = createCoumns(kpiConfigRefs, resp);
                    initTable(columns)
                    $('#dataItemManagement').bootstrapTable("load", data.rows);
                    $.each(resp.data.charts, function (i, chartConfig) {
                        if (chartConfig.pie == true) {
                            $("#charts-box").PieChart({
                                data: data,
                                chartConfig: chartConfig,
                                kpiConfigs: kpiConfigRefs
                            });
                        } else {
                            $("#charts-box").XYChart({
                                data: data,
                                chartConfig: chartConfig,
                                kpiConfigs: kpiConfigRefs
                            });
                        }
                    })
                }
            });
        }
    });
};
