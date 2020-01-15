$(function () {
    $(document).ready(function () {
        initDateSection();

        var Menus = getCookie('Menulist');

        if (Menus.indexOf("-6-") != -1) {
            var pieChart = '<div id="loadGroupOverviewPieChart" style="height: 300px;width:100%;"></div>';
            $("#pieChart").append(pieChart);

            loadGroupOverviewPieChart();

            var histogram = '<div id="loadGroupOverviewHistogram" style="height: 280px;width:100%;"></div>';
            $("#histogram").append(histogram);

            loadGroupOverviewHistogram("风险", "#b50b2a");

            var table = '<table id="groupOverviewTable" class="table text-nowrap"></table>';
            $("#groupOverviewTableDiv").append(table);

            loadGroupOverview();

            var overviewEcharts = document.getElementById("loadGroupOverviewPieChart");
            echarts.init(overviewEcharts).on('click', function (params) {
                var name = params.data.name;
                loadGroupOverviewHistogram(name, getColor(name));
            });
        }
    })
});

function selectOnchange() {
    loadGroupOverviewPieChart();
    loadGroupOverviewHistogram("风险", "#b50b2a");
    loadGroupOverview();
}

/*业务群总览饼状图*/
function loadGroupOverviewPieChart() {
    $.ajax({
        url: getRootPath() + '/groupBoard/groupOverviewPieChart',
        type: "get",
        dataType: "json",
        cache: false,
        data: {
            'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
            'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
            'month': $("#dateSection").val()
        },
        success: function (data) {
            var myChart = echarts.init(document.getElementById("loadGroupOverviewPieChart"));

            var option = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{b} : {c} ({d}%)"
                },
                legend: {
                    bottom: 3,
                    left: 'center',
                    data: data.sources
                },
                color: ['#b50b2a', '#fcc746', '#22b573', '#B5A267'],
                series: [
                    {
                        type: 'pie',
                        radius: ['46%', '68%'],
                        center: ['48%', '47%'],
                        selectedMode: 'single',
                        data: data.values,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter: '{b} : {c}'
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ],
            };
            myChart.setOption(option);
        }
    });
}

/*业务群总览柱状图*/
function loadGroupOverviewHistogram(name, color) {
    $.ajax({
        url: getRootPath() + '/groupBoard/groupOverviewHistogram',
        type: "post",
        dataType: "json",
        cache: false,
        data: {
            'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
            'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
            'month': $("#dateSection").val(),
            'riskCategory': name
        },
        success: function (data) {
            var myChart = echarts.init(document.getElementById("loadGroupOverviewHistogram"));

            var option = {
                color: [color],
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: {
                    type: 'category',
                    data: data.names,
                    axisLabel: {
                        formatter: function (value) {
                            var newParamsName = "";// 最终拼接成的字符串
                            var paramsNameNumber = value.length;// 实际标签的个数
                            if (paramsNameNumber > 9) {
                                value = value.slice(0, 9) + "...";
                            }
                            var provideNumber = 3;// 每行能显示的字的个数
                            var rowNumber = Math.ceil(paramsNameNumber / provideNumber);// 换行的话，需要显示几行，向上取整

                            // 条件等同于rowNumber>1
                            if (paramsNameNumber > provideNumber) {
                                /** 循环每一行,p表示行 */
                                for (var p = 0; p < rowNumber; p++) {
                                    var tempStr = "";// 表示每一次截取的字符串
                                    var start = p * provideNumber;// 开始截取的位置
                                    var end = start + provideNumber;// 结束截取的位置

                                    // 此处特殊处理最后一行的索引值
                                    if (p == rowNumber - 1) {
                                        // 最后一次不换行
                                        tempStr = value.substring(start, paramsNameNumber);
                                    } else {
                                        // 每一次拼接字符串并换行
                                        tempStr = value.substring(start, end) + "\n";
                                    }
                                    newParamsName += tempStr;// 最终拼成的字符串
                                }

                            } else {
                                // 将旧标签的值赋给新标签
                                newParamsName = value;
                            }
                            return newParamsName;
                        }
                    },
                },
                yAxis: {
                    type: 'value',
                    minInterval: 1,
                },
                series: [{
                    data: data.values,
                    type: 'bar',
                    barWidth: '26%',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: 'top',
                            }
                        }
                    }
                }]
            };
            myChart.setOption(option);
        }
    });
}

function getColor(name) {
    var color = "";
    if ("风险" == name) {
        color = "#b50b2a";
    } else if ("预警" == name) {
        color = "#fcc746";
    } else if ("正常" == name) {
        color = "#22b573";
    } else {
        color = "#B5A267";
    }
    return color;
}

function toPercent(value) {
    var str = value;

    if (null != value) {
        value = Number(value) > 1 ? 1 : value;
        str = Number(value * 100).toFixed(1);
        str += "%";
    }
    return str;
}

/*业务群总览表*/
function loadGroupOverview() {
    $('#groupOverviewTable').bootstrapTable('destroy');
    $("#groupOverviewTable").bootstrapTable({
        method: 'GET',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/groupBoard/groupOverview',
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
                {title: '子产品线', field: 'hwzpdu', align: 'center', valign: 'middle', width: '14%', rowspan: 2, colspan: 1,
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {title: 'PDU/SPDT', field: 'pduSpdt', align: "center", valign: 'middle', width: '15%', rowspan: 2, colspan: 1,
                    formatter: function (value, row, index) {
                        return completeInformation(value, "center");
                    }
                },
                {title: '验收', align: 'center', valign: 'middle', width: '18%', rowspan: 1, colspan: 2},
                {title: '过程', align: 'center', valign: 'middle', width: '27%', rowspan: 1, colspan: 3},
                {title: '人员', align: 'center', valign: 'middle', width: '18%', rowspan: 1, colspan: 2},
                {title: '可信项目', field: 'credibleProject', align: "center", valign: 'middle', width: '8%', rowspan: 2, colspan: 1}
            ],
            [
                {
                    title: '验收项目', field: 'checkedProject', align: 'center', valign: 'middle'
                },
                {
                    title: '客户评分', field: 'customerScoring', align: 'center', valign: 'middle'
                },
                {
                    title: '执行项目', field: 'executedProject', align: 'center', valign: 'middle'
                },
                {
                    title: '风险项目', field: 'riskProject', align: 'center', valign: 'middle'
                },
                {
                    title: '问题闭环率', field: 'issueCloseLoop', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return toPercent(value);
                    }
                },
                {
                    title: '人员到位率', field: 'personnelArrival', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return toPercent(value);
                    }
                },
                {
                    title: '人员稳定度', field: 'personnelStable', align: 'center', valign: 'middle',
                    formatter: function (value, row, index) {
                        return toPercent(value);
                    }
                }
            ]
        ],
        locale: 'zh-CN'//中文支持
    });
}