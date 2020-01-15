$(function () {
    $(document).ready(function () {
        initDateSection();

        var Menus = getCookie('Menulist');

        if (Menus.indexOf("-6-") != -1) {
            var table = '<table id="projectIssueTable" class="table text-nowrap"></table>';
            $("#projectIssueTableDiv").append(table);

            loadProjectIssue();
        }
    })
});

function selectOnchange() {
    loadProjectIssue();
}

function loadProjectIssue() {
    $('#projectIssueTable').bootstrapTable('destroy');
    $("#projectIssueTable").bootstrapTable({
        method: 'GET',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/projectOverview/queryProblemAnalysis',
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true,// 是否分页
        paginationPreText: '<上一页',//指定分页条中上一页按钮的图标或文字
        paginationNextText: '下一页>',//指定分页条中下一页按钮的图标或文字
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
        queryParams: function (params) {
            var param = {
                'limit': params.limit,
                'offset': params.offset,
                'date': $("#dateSection").val(),
                'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
                'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
                'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
                'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
                'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
                'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
                'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
                'pm': '1' == getCookie('zrOrhwselect') ? zeroFill(getCookie('username'), 10) : getZRAccount()
            }
            return param;
        },
        columns: [
            [{
                title: '序号', align: "center", valign: 'middle', width: '5%', rowspan: 2, colspan: 1,
                formatter: function (value, row, index) {
                    var pageSize = $('#projectIssueTable').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#projectIssueTable').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
                {
                    title: '项目名称',
                    field: 'name',
                    align: 'center',
                    valign: 'middle',
                    width: "15%",
                    rowspan: 2,
                    colspan: 1,
                    formatter: function (value, row, index) {
                        var path = getRootPath() + '/view/HTML/page.html?projNo=' + row.no;
                        return '<a target="_parent" style="color: #721b77;" title="' + value + '" href="' + path + '">' + value + '</a>';
                    }
                },
                {
                    title: 'PM', field: 'pm', align: "center", valign: 'middle', width: "6%", rowspan: 2, colspan: 1,
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {
                    title: 'QA', field: 'qa', align: "center", valign: 'middle', width: "6%", rowspan: 2, colspan: 1,
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {
                    title: '部门',
                    field: 'department',
                    align: 'center',
                    valign: 'middle',
                    width: "10%",
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
                    width: "10%",
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
                    width: "10%",
                    rowspan: 2,
                    colspan: 1
                },
                {title: '361', align: 'center', valign: 'middle', width: "10%", rowspan: 1, colspan: 2},
                {title: 'AAR', align: 'center', valign: 'middle', width: "10%", rowspan: 1, colspan: 2},
                {title: '质量回溯', align: 'center', valign: 'middle', width: "10%", rowspan: 1, colspan: 2},
                {title: '开工会审计', align: 'center', valign: 'middle', width: "10%", rowspan: 1, colspan: 2}
            ],
            [
                {
                    title: '闭环率', field: 'problemClosed', align: 'center', valign: 'middle', width: 60,
                    formatter: function (val, row) {
                        var v = val == null ? "0" : val;
                        return v + "%";
                    }
                },
                {title: '延期', field: 'problemDelay', align: 'center', valign: 'middle', width: 60},
                {
                    title: '闭环率', field: 'aarClosed', align: 'center', valign: 'middle', width: 60,
                    formatter: function (val, row) {
                        var v = val == null ? "0" : val;
                        return v + "%";
                    }
                },
                {title: '延期', field: 'aarDelay', align: 'center', valign: 'middle', width: 60},
                {
                    title: '闭环率', field: 'backClosed', align: 'center', valign: 'middle', width: 60,
                    formatter: function (val, row) {
                        var v = val == null ? "0" : val;
                        return v + "%";
                    }
                },
                {title: '延期', field: 'backDelay', align: 'center', valign: 'middle', width: 60},
                {
                    title: '闭环率', field: 'auditClosed', align: 'center', valign: 'middle', width: 60,
                    formatter: function (val, row) {
                        var v = val == null ? "0" : val;
                        return v + "%";
                    }
                },
                {title: '延期', field: 'auditDelay', align: 'center', valign: 'middle', width: 60},
            ]
        ],
        locale: 'zh-CN'//中文支持
    });
}