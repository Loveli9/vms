$(function () {
    $(document).ready(function () {
        initDateSection();

        var Menus = getCookie('Menulist');

        if (Menus.indexOf("-6-") != -1) {
            var table = '<table id="projectAcceptanceTable" class="table text-nowrap"></table>';
            $("#projectAcceptanceTableDiv").append(table);

            loadProjectAcceptance();
        }
    })
});

function selectOnchange() {
    loadProjectAcceptance();
}

/*业务群验收表*/
function loadProjectAcceptance() {
    $('#projectAcceptanceTable').bootstrapTable('destroy');
    $("#projectAcceptanceTable").bootstrapTable({
        method: 'GET',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/groupBoard/projectAcceptance',
        striped: false, //是否显示行间隔色
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
                'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
                'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
                'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
                'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
                'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
                'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
                'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
                'month': $("#dateSection").val(),
                'pm': '1' == getCookie('zrOrhwselect') ? zeroFill(getCookie('username'), 10) : getZRAccount()
            };
            return param;
        },
        columns: [
            [
                {title: '序号', align: "center", valign: 'middle', width: '5%',
                    formatter: function (value, row, index) {
                        var pageSize = $('#projectAcceptanceTable').bootstrapTable('getOptions').pageSize;
                        var pageNumber = $('#projectAcceptanceTable').bootstrapTable('getOptions').pageNumber;
                        return pageSize * (pageNumber - 1) + index + 1;
                    }
                },
                {title: '项目名称', field: 'name', align: "center", valign: 'middle', width: '18%',
                    formatter: function (value, row, index) {
                        var path = getRootPath() + '/view/HTML/page.html?projNo=' + row.projectNo;
                        return '<a target="_parent" style="color: #721b77;" title="' + value + '" href="' + path + '">' + value + '</a>';
                    }
                },
                {title: 'PM', field: 'pm', align: "center", valign: 'middle', width: '6%',
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {title: 'QA', field: 'qa', align: "center", valign: 'middle', width: '6%',
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {title: '子产品线', field: 'hwzpdu', align: 'center', valign: 'middle', width: '10%',
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {title: 'PDU/SPDT', field: 'pduSpdt', align: "center", valign: 'middle', width: '13%',
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {title: '结项项目', field: 'knotProject', align: "center", valign: 'middle', width: '10%',
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                },
                {title: '验收KPI', field: 'acceptanceKPI', align: "center", valign: 'middle', width: '10%'},
                {title: '结项星级分布', field: 'knotStarDistribution', align: "center", valign: 'middle', width: '10%'},
                {title: '扣款', field: 'deduction', align: "center", valign: 'middle', width: '10%'}
            ]
        ],
        locale: 'zh-CN'//中文支持
    });
}