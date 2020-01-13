/**
 * 访问页面时传入报表ID（report.html?id=xxx）
 */
var projNo = getCookie("projNo_comm");
;$(function () {
    var columns = createCoumns()
    initTable(columns)
    loadDatas();


    $("#btn_handle").click(function () {
        var selections = $('#dataItemManagement').bootstrapTable("getSelections");
        if (selections.length == 0) {
            toastr.error("请选选择需要处理的数据！");
        } else {
            var data = selections[0];
            handleCheck(data);
        }
    })
});

var handleCheck = function (data) {
    window.location.href = "reportCheck.html?id=" + data.id +
        "&reportConfigId=" + data.report.reportConfigId +
        "&projectNo=" + data.report.projectId
}
var createCoumns = function (kpiConfigRefs, data) {
    let columns = [
        {
            title: '全选',
            field: 'select',
            radio: true,
            align: 'center',
            valign: 'middle',
            width: '30'
        }, {
            title: "项目",
            field: 'project',
            halign: 'center',
            align: 'center',
            sortable: false,
            width: 70, formatter: function (val, row, index) {
                if (val) {
                    return val.name;
                } else {
                    return "-";
                }
            }
        }, {
            title: "报表",
            field: 'report',
            halign: 'center',
            align: 'center',
            sortable: false,
            width: 70,
            formatter: function (val, row, index) {
                if (val) {
                    return val.reportName;
                } else {
                    return "-";
                }
            }
        }, {
            title: "提交时间",
            field: 'submitDate',
            halign: 'center',
            align: 'center',
            sortable: false,
            width: 70,
            formatter: function (val, row, index) {
                if (val) {
                    return dateFormat("YYYY/mm/dd HH:MM:SS", new Date(val));
                } else {
                    return "-";
                }
            }
        }, {
            title: "提交人",
            field: 'submitor',
            halign: 'center',
            align: 'center',
            sortable: false,
            width: 70
        }];
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
        locale: 'zh-CN',//中文支持
        onDblClickRow: function (row) {
            handleCheck(row);
        }
    });
}

function loadDatas() {
    $.ajax({
        method: 'post',
        url: getRootPath() + '/report/report_check/page_by_qa',
        data: {"qa": "admin"},
        success: function (resp) {
            var data = resp.data;
            $('#dataItemManagement').bootstrapTable("load", data);
        }
    });
};
