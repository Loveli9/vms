$(function () {
    $(document).ready(function () {
        initDateSection();

        var Menus = getCookie('Menulist');

        if (Menus.indexOf("-6-") != -1) {
            var table = '<table id="projectExecuteTable" class="table text-nowrap"></table>';
            $("#projectExecuteTableDiv").append(table);

            loadProjectExecute();
        }
    })
});

function selectOnchange(){
    loadProjectExecute();
}

function loadProjectExecute() {
    $("#projectExecuteTable").bootstrapTable('destroy');
    $('#projectExecuteTable').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/groupBoard/projectExecute',
        editable: false,// 可行内编辑
        striped: true, // 是否显示行间隔色
        dataField: "rows",
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pagination: true,// 是否分页
        paginationPreText: '<上一页',//指定分页条中上一页按钮的图标或文字
        paginationNextText: '下一页>',//指定分页条中下一页按钮的图标或文字
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        showColumns: false,
        toolbar: '#toolbar',// 指定工作栏
        toolbarAlign: 'right',
        dataType: "json",
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
            }
            return param;
        },
        columns: [
            [
                {
                    title: '序号',
                    width: '5%',
                    rowspan: 2,
                    colspan: 1,
                    align: "center",
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        var pageSize = $('#projectExecuteTable').bootstrapTable('getOptions').pageSize;
                        var pageNumber = $('#projectExecuteTable').bootstrapTable('getOptions').pageNumber;
                        return pageSize * (pageNumber - 1) + index + 1;
                    }
                },
                {
                    title: '项目名称',
                    field: 'name',
                    width: '10%',
                    rowspan: 2,
                    colspan: 1,
                    align: "center",
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        var path = getRootPath() + '/view/HTML/page.html?projNo=' + row.projectNo;
                        return '<a target="_parent" style="color: #721b77;" title="' + value + '" href="' + path + '">' + value + '</a>';
                    }
                },
                {
                    title: 'PM',
                    field: 'pm',
                    width: '6%',
                    rowspan: 2,
                    colspan: 1,
                    align: "center",
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {
                    title: 'QA',
                    field: 'qa',
                    width: '6%',
                    rowspan: 2,
                    colspan: 1,
                    align: "center",
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {
                    title: '阶段',
                    width: '18%',
                    rowspan: 1,
                    colspan: 2,
                    align: "center",
                    valign: 'middle',
                }, {
                    title: '本次',
                    colspan: 3,
                    align: 'center',
                    width: '28%',
                    rowspan: 1,
                }, {
                    title: '上次',
                    colspan: 3,
                    align: 'center',
                    width: '28%',
                    rowspan: 1,
                }
            ],
            [
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
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }

                },
                {
                    title: '预警',
                    field: 'yellowState',
                    align: "center",
                    valign: 'middle',
                    width: 100,
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                }, {
                    title: '正常',
                    field: 'greenState',
                    align: "center",
                    valign: 'middle',
                    width: 100,
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                }, /*{
                    title: '连续两周</br>风险项目',
                    field: 'red',
                    align: "center",
                    valign: 'middle',
                    width: 100,
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                }, {
                    title: '连续两周</br>预警项目',
                    field: 'yellow',
                    align: "center",
                    valign: 'middle',
                    width: 100,
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                }*/ {
                    title: '风险',
                    field: 'redState1',
                    align: "center",
                    valign: 'middle',
                    width: 100,
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                }, {
                    title: '预警',
                    field: 'yellowState1',
                    align: "center",
                    valign: 'middle',
                    width: 100,
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                }, {
                    title: '正常',
                    field: 'greenState1',
                    align: "center",
                    valign: 'middle',
                    width: 100,
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                }/*, {
                    title: '连续两周</br>风险项目',
                    field: 'red1',
                    align: "center",
                    valign: 'middle',
                    width: 100,
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                }, {
                    title: '连续两周</br>预警项目',
                    field: 'yellow1',
                    align: "center",
                    valign: 'middle',
                    width: 100,
                    formatter: function (value, row, index) {
                        return projectBoardDisplayTransform(value);
                    }
                }*/
            ]
        ],
        locale: 'zh-CN',// 中文支持,
    });
}