var projectInfo = {
    'hwpdu': 32,
    'clientType': 0,
};

$(function () {
    $(document).ready(function () {
        initDateSection();

        var Menus = getCookie('Menulist');

        if (Menus.indexOf("-6-") != -1) {
            var table = '<table id="groupExecuteTable" class="table text-nowrap"></table>';
            $("#groupExecuteTableDiv").append(table);

            loadGroupExecute();
        }
    })
});

function selectOnchange() {
    loadGroupExecute();
}

function loadGroupExecute() {
    $("#groupExecuteTable").bootstrapTable('destroy');
    $('#groupExecuteTable').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/projects/projectBoard',
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
                'month': $("#dateSection").val()
            }
            return param;
        },
        columns: [[{
            title: '阶段',
            field: 'pdu',
            width: '22%',
            rowspan: 1,
            colspan: 2,
            align: "center",
            valign: 'middle',
        }, {
            title: '本次',
            colspan: 3,
            align: 'center',
            width: '41%',
            rowspan: 1,
        }, {
            title: '上次',
            colspan: 3,
            align: 'center',
            width: '41%',
            rowspan: 1,
        }], [
            {
                title: '子产品线',
                field: 'pdu',
                align: "center",
                valign: 'middle',
                width: 100,
                formatter: function (value, row, index) {
                    return completeInformation(value, "center");
                }
            },
            {
                title: 'PDU/SPDT',
                field: 'pduspt',
                align: "center",
                valign: 'middle',
                width: 100,
                formatter: function (value, row, index) {
                    return completeInformation(value, "center");
                }
            },
            {
                title: '风险',
                field: 'redState',
                align: "center",
                valign: 'middle',
                width: 100,
            }, {
                title: '预警',
                field: 'yellowState',
                align: "center",
                valign: 'middle',
                width: 100,
            }, {
                title: '正常',
                field: 'greenState',
                align: "center",
                valign: 'middle',
                width: 100,
            }, /*{
                title: '连续两周</br>风险项目',
                field: 'red',
                align: "center",
                valign: 'middle',
                width: 100,
            }, {
                title: '连续两周</br>预警项目',
                field: 'yellow',
                align: "center",
                valign: 'middle',
                width: 100,
            },*/ {
                title: '风险',
                field: 'redState1',
                align: "center",
                valign: 'middle',
                width: 100,
            }, {
                title: '预警',
                field: 'yellowState1',
                align: "center",
                valign: 'middle',
                width: 100,
            }, {
                title: '正常',
                field: 'greenState1',
                align: "center",
                valign: 'middle',
                width: 100,
            }/*, {
                title: '连续两周</br>风险项目',
                field: 'red1',
                align: "center",
                valign: 'middle',
                width: 100,
            }, {
                title: '连续两周</br>预警项目',
                field: 'yellow1',
                align: "center",
                valign: 'middle',
                width: 100,
            }*/]],
        locale: 'zh-CN',// 中文支持,
    });
}