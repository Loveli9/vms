$(function () {
    $(document).ready(function () {
        initDateSection();

        var Menus = getCookie('Menulist');

        if (Menus.indexOf("-6-") != -1) {
            var table = '<table id="groupAcceptanceTable" class="table text-nowrap"></table>';
            $("#groupAcceptanceTableDiv").append(table);

            loadGroupAcceptance();
        }
    })
});

function selectOnchange() {
    loadGroupAcceptance();
}

/*业务群验收表*/
function loadGroupAcceptance() {
    $('#groupAcceptanceTable').bootstrapTable('destroy');
    $("#groupAcceptanceTable").bootstrapTable({
        method: 'GET',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/groupBoard/groupAcceptance',
        striped: false, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: false, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
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
            };
            return param;
        },
        columns: [
            [
                {title: '子产品线', field: 'hwzpdu', align: 'center', valign: 'middle', width: '15%',
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {title: 'PDU/SPDT', field: 'pduSpdt', align: "center", valign: 'middle', width: '15%',
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {title: '结项项目数', field: 'knotProject', align: "center", valign: 'middle', width: '10%'},
                {title: '验收KPI', field: 'acceptanceKPI', align: "center", valign: 'middle', width: '20%'},
                {title: '结项星级分布', field: 'knotStarDistribution', align: "center", valign: 'middle', width: '20%'},
                {title: '扣款', field: 'deduction', align: "center", valign: 'middle', width: '20%'}
            ]
        ],
        locale: 'zh-CN'//中文支持
    });
}