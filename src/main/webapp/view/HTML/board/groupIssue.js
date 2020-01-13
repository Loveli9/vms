$(function () {
    $(document).ready(function () {
        initDateSection();

        var Menus = getCookie('Menulist');

        if (Menus.indexOf("-6-") != -1) {
            var table = '<table id="groupIssueTable" class="table text-nowrap"></table>';
            $("#groupIssueTableDiv").append(table);

            loadGroupIssue();
        }
    })
});

function selectOnchange() {
    loadGroupIssue();
}

function loadGroupIssue() {
    $('#groupIssueTable').bootstrapTable('destroy');
    $("#groupIssueTable").bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/measureComment/kanbanProblemAnalysis',
        editable: false,// 可行内编辑
        striped: true, // 是否显示行间隔色
        dataField: "rows",
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pagination: false,// 是否分页
        queryParamsType: 'limit',
        sidePagination: 'client',
        showColumns: false,
        toolbar: '#toolbar',// 指定工作栏
        toolbarAlign: 'right',
        dataType: "json",
        queryParams: function (params) {
            var param = {
                'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
                'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
                'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
                'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
                'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
                'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
                'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
                'currentDate': $("#dateSection").val()
            }
            return param;
        },
        columns: [
            [{
                title: '序号', align: "center", valign: 'middle', width: "5%", rowspan: 2, colspan: 1,
                formatter: function (value, row, index) {
                    var pageSize = $('#groupIssueTable').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#groupIssueTable').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
                {
                    title: '部门',
                    field: 'department',
                    align: 'center',
                    valign: 'middle',
                    width: "12%",
                    rowspan: 2,
                    colspan: 1,
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {
                    title: '及时闭环</br>率(汇总)',
                    field: 'closed',
                    align: 'center',
                    valign: 'middle',
                    width: "8%",
                    rowspan: 2,
                    colspan: 1,
                    formatter: function (val, row) {
                        var v = val == null ? "0%" : val;
                        return v;
                    }
                },
                {
                    title: '延期</br>(汇总)',
                    field: 'delay',
                    align: 'center',
                    valign: 'middle',
                    width: "8%",
                    rowspan: 2,
                    colspan: 1
                },
                {title: '361', align: 'center', valign: 'middle', width: "16%", rowspan: 1, colspan: 2},
                {title: 'AAR', align: 'center', valign: 'middle', width: "16%", rowspan: 1, colspan: 2},
                {title: '质量回溯', align: 'center', valign: 'middle', width: "18%", rowspan: 1, colspan: 2},
                {title: '开工会审计', align: 'center', valign: 'middle', width: "18%", rowspan: 1, colspan: 2}
            ],
            [
                {
                    title: '闭环率', field: 'problemClosed', align: 'center', valign: 'middle', width: 75,
                    formatter: function (val, row) {
                        var v = val == null ? "0" : val;
                        return v + "%";
                    }
                },
                {title: '延期', field: 'problemDelay', align: 'center', valign: 'middle', width: 75},
                {
                    title: '闭环率', field: 'aarClosed', align: 'center', valign: 'middle', width: 75,
                    formatter: function (val, row) {
                        var v = val == null ? "0" : val;
                        return v + "%";
                    }
                },
                {title: '延期', field: 'aarDelay', align: 'center', valign: 'middle', width: 75},
                {
                    title: '闭环率', field: 'backClosed', align: 'center', valign: 'middle', width: 75,
                    formatter: function (val, row) {
                        var v = val == null ? "0" : val;
                        return v + "%";
                    }
                },
                {title: '延期', field: 'backDelay', align: 'center', valign: 'middle', width: 75},
                {
                    title: '闭环率', field: 'auditClosed', align: 'center', valign: 'middle', width: 75,
                    formatter: function (val, row) {
                        var v = val == null ? "0" : val;
                        return v + "%";
                    }
                },
                {title: '延期', field: 'auditDelay', align: 'center', valign: 'middle', width: 75},
            ]
        ],
        locale: 'zh-CN'//中文支持
    });
}