var proNo = getQueryString("projNo");

//整体评估
function actualMemberTable() {
    var str = '开发工程师,PM,产品经理,SE,MDE,BA,IA,运维工程师,测试工程师,TC,TSE';
    $('#actualMemberTable').bootstrapTable('destroy');
    $("#actualMemberTable").bootstrapTable({
        method: 'GET',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/personnelTrack/overallAssessment',
        striped: false, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: false, //是否分页
        // queryParamsType: 'limit',
        sidePagination: 'server',
        // pageSize: 20, //单页记录数
        // pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
        queryParams: function (params) {
            var param = {
                projectId: proNo,
                duty: str,
            }
            return param;
        },
        columns: [
            [
                {title: '姓名', field: 'name', align: 'center', valign: 'middle', width: '10%', rowspan: 2, colspan: 1},
                {title: '工号', field: 'account', align: 'center', valign: 'middle', width: '10%', rowspan: 2, colspan: 1},
                {title: '岗位', field: 'role', align: "center", valign: 'middle', width: '10%', rowspan: 2, colspan: 1},
                {title: '需求', align: 'center', valign: 'middle', width: '10%', rowspan: 1, colspan: 1},
                {title: '产出', align: 'center', valign: 'middle', width: '30%', rowspan: 1, colspan: 3},
                {title: '风险', align: 'center', valign: 'middle', width: '33%', rowspan: 1, colspan: 3}
            ],
            [
                {
                    title: '需求完成率', field: 'demandCompletion', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return getNumber(value);
                    }
                },
                {
                    title: '代码量', field: 'codeQuantity', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return getNumber(value);
                    }
                },
                {
                    title: '测试用例', field: 'testCase', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return getNumber(value);
                    }
                },
                {
                    title: '文档', field: 'file', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return getNumber(value);
                    }
                },
                {
                    title: '个人缺陷数', field: 'personalDefects', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return getNumber(value);
                    }
                },
                {
                    title: '风险人员', field: 'riskPersonnel', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return getNumber(value);
                    }
                },
                {
                    title: '风险原因', field: 'riskReason', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return getNumber(value);
                    }
                }
            ]
        ],
        locale: 'zh-CN'//中文支持
    });
}

//代码--测试用例--文档
var twelves = [];

function codeAndCaseTable(tab) {
    var str = "";
    if ("codeTable" == tab) {
        str = '开发工程师,SE,MDE';
        var arr = str.split(',');
        $("#userRole").selectpicker('val', arr);
        //折线图
        myChart1new();
        personalCodeCount();
    } else if ("testCaseTable" == tab) {
        str = '测试工程师,TC,TSE';
        var arr1 = str.split(',');
        $("#userRoles").selectpicker('val', arr1);
        personalCodeCount1();
    } else {
        str = 'PM,产品经理,SE';
        var arr1 = str.split(',');
        $("#documentUserRole").selectpicker('val', arr1);
        personalCodeCount2();
    }

    var columnsArray = [];
    columnsArray.push({
        "title": '姓名',
        "field": 'name',
        halign: 'center',
        align: 'center',
        width: 42
    });
    columnsArray.push({
        "title": '工号',
        "field": 'account',
        halign: 'center',
        align: 'center',
        width: 42
    });
    columnsArray.push({
        "title": '岗位',
        "field": 'role',
        halign: 'center',
        align: 'center',
        width: 42
    });

    $.ajax({
        async: false,
        type: "GET",
        url: getRootPath() + '/workload/abscissa',
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        json: 'callback',
        success: function (json) {
            twelves = json.data;
            for (var i = 0; i < twelves.length; i++) {
                var title = twelves[i];
                columnsArray.push({
                    title: title,
                    field: title,
                    halign: 'center',
                    align: 'center',
                    width: 42,
                    formatter: function (value, row, index, field) {
                        var count = $('#' + tab).bootstrapTable('getData').length - 1;
                        if (count == index) {
                            return value;
                        }

                        if (row && row['workloads'] && row['workloads'][field]) {
                            var load = row['workloads'][field];
                            if (load && load['times'] > 0) {
                                value = load['amount'] == 0 ? "--" : load['amount'];
                                var tips = '提交次数: ' + load['times'] + '</br>' + '最后提交时间: ' + load['lastCommitTime'] + '</br>';
                                tips = load['amount'] > 0 ? (load['type'] + ': ' + load['amount'] + '</br>' + tips) : tips;

                                var show = '<a class="tooltip-test" data-toggle="tooltip"' +
                                    ' data-placement="bottom" data-html="true" title="' + tips + '" >'
                                    + value + '</a>';

                                return show;
                            }
                        }
                    },
                });
            }
        }
    });

    columnsArray.push({
        "title": '合计',
        "field": 'quantity',
        halign: 'center',
        align: 'center',
        width: 42,
        formatter: function (value, row, index) {
            var count = $('#' + tab).bootstrapTable('getData').length - 1;
            if (count == index) {
                return value;
            }

            var total = 0;
            if (row && row['workloads']) {
                var loads = row['workloads'];
                for (var key in loads) {
                    total += loads[key]['amount'];
                }
            }
            return total > 0 ? total : "-";
        },
    });

    $('#' + tab).bootstrapTable({
        method: 'get',
        url: getRootPath() + '/workload/metric',
        toolbar: '#toolbar',
        striped: false, //是否显示行间隔色
        dataField: "data",
        queryParamsType: 'limit',
        sidePagination: 'server',
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        queryParams: function (params) {
            var roles = "";
            if ("codeTable" == tab) {
                if ($("#userRole").val()) {
                    roles = $("#userRole").val().join(',');
                }
            } else if ("testCaseTable" == tab) {
                if ($("#userRoles").val()) {
                    roles = $("#userRoles").val().join(',');
                }
            } else {
                if ($("#documentUserRole").val()) {
                    roles = $("#documentUserRole").val().join(',');
                }
            }
            var param = {
                projectId: proNo,
                codeType: $('#personal-code-type').val(),
                duty: encodeURI(roles)
            };
            return param;
        },
        columns: columnsArray,
        locale: 'zh-CN',//中文支持,
        onLoadSuccess: function () {
            addRow(tab);
            tip();
        }
    });
}

function addRow(tab1) {
    var summation = $('#' + tab1).bootstrapTable('getData');


    if (summation && summation.length > 0) {
        var count = summation.length;

        var total = {};


        for (var j = 0, len = count; j < len; j++) {
            var loads = summation[j]['workloads'];
            if (loads) {
                for (var key in loads) {
                    var amount = loads[key]['amount'];
                    if (amount > 0) {
                        var monthAmount = total[key];
                        total[key] = monthAmount ? monthAmount + amount : amount;
                    }
                }
            }
        }

        for (var key in total) {
            var amount = total[key];
            if (amount > 0) {
                var totalAmount = total['quantity'];
                total['quantity'] = totalAmount ? totalAmount + amount : amount;
            }
        }
        total['name'] = "合计";

        $('#' + tab1).bootstrapTable('insertRow', {
            index: count,
            row: total
        });

        $('#' + tab1).bootstrapTable('mergeCells', {
            index: count,
            field: 'name',
            colspan: 3,
            rowspan: 1
        });
    }
}

function tip() {
    $("[data-toggle='tooltip']").tooltip();
}

var myChart1;
var option1;

function myChart1new() {
    // 基于准备好的dom，初始化echarts实例
    myChart1 = echarts.init(document.getElementById('Div1'));
    // 指定图表的配置项和数据
    option1 = {
        title: {
            trigger: 'axis',
        },
        tooltip: {},
        xAxis: {
            type: 'category',
            splitLine: {show: false},//去除网格线
            //name: "2018 年",
            nameLocation: "middle",
            nameTextStyle: {
                color: '#333',
                padding: 15
            },
            axisLabel: {
                textStyle: {
                    color: '#333',
                }
            },
            axisLine: {
                color: '#333',
                lineStyle: {
                    color: '#2e8afc',
                    width: 2,
                }
            },
            data: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
        },
        yAxis: [{
            type: 'value',
            splitLine: {show: false},//去除网格线
            name: "代码产出 (LOC)",
            nameTextStyle: {
                padding: 10
            }
        }, {
            type: 'value',
            name: '提交次数 (次)',
            splitLine: {show: false},
            position: 'right',
            nameTextStyle: {
                padding: 10
            }
        }],
        series: [{
            type: 'bar',
            data: [],
            barWidth: 30,
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#2e8afc'
                    }]),
                }
            }
        }, {
            type: 'line',
            label: {
                normal: {
                    show: true,
                    position: 'top',
                }
            },
            yAxisIndex: 1,
            lineStyle: {
                normal: {
                    color: "#e41f2b",
                    width: 3,
                    shadowColor: 'rgba(0,0,0,0.4)',
                    shadowBlur: 10,
                    shadowOffsetY: 10
                }
            },
        }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart1.setOption(option1);
    //初始化图标标签
}

function search(op1, op2) {
    var roles = ""
    if ($("#userRole").val() != null) {
        roles = $("#userRole").val().join(',');
    }
    $.ajax({
        url: getRootPath() + '/workload/summary',
        type: 'get',
        async: false,
        data: {
            projectId: proNo,
            type: op1,
            codeType: op2,
            role: encodeURI(roles)
        },
        success: function (data) {
            option1.xAxis = {
                data: data.months
            };
            option1.series = [{
                data: data.abilities
            }, {
                data: data.commits
            }];
            //console.log(data);
            myChart1.setOption(option1);
            $("[data-toggle='tooltip']").tooltip();
        }
    });
};

//根据条件统计员工个人代码量、并根据代码类别筛选
function personalCodeCount() {
    var param1 = "0";//0:commit 1:message
    var param2 = "all";//所有代码量

    $("#userRole").change(function () {
        param2 = $('#personal-code-type').val();
        search(param1, param2);
        $('#codeTable').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
    });

    $("#personal-code-type").change(function () {
        param2 = $('#personal-code-type').val();
        search(option1, param2);
        $('#codeTable').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
    });

    search(param1, param2);
}

//根据条件统计--测试用例
function personalCodeCount1() {
    $("#userRoles").change(function () {
        $('#testCaseTable').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
    });
}

//根据条件统计--文档
function personalCodeCount2() {
    $("#documentUserRole").change(function () {
        $('#documentTable').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
    });
}

//质量问题
function qualityProblemTable() {
    $('#qualityProblemTable').bootstrapTable('destroy');
    $("#qualityProblemTable").bootstrapTable({
        method: 'GET',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/personnelTrack/queryQualityProblem',
        striped: false, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
        queryParams: function (params) {
            var param = {
                'limit': params.limit,
                'offset': params.offset,
            }
            return param;
        },
        columns: [
            [
                {title: '问题编号', field: 'problemNumber', align: 'center', valign: 'middle', width: 125},
                {title: '问题描述', field: 'description', align: 'center', valign: 'middle', width: 145},
                {title: '类型', field: 'type', align: "center", valign: 'middle', width: 125},
                {title: '严重程度', field: 'severity', align: 'center', valign: 'middle', width: 125},
                {title: '责任人', field: 'personLiable', align: 'center', valign: 'middle', width: 125},
                {title: '生成时间', field: 'createTime', align: 'center', valign: 'middle', width: 125},
                {title: '状态', field: 'state', align: "center", valign: 'middle', width: 135},
                {title: '解决时间', field: 'solveTime', align: 'center', valign: 'middle', width: 135}
            ]
        ],
        locale: 'zh-CN'//中文支持
    });
}

function getNumber(val) {
    var demand = val;
    if (val == 0 || val == -1) {
        demand = "-";
    }
    return demand;
}