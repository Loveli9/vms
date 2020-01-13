$(function () {
    function echars(data) {
        var assessList = [];
        var AARList = [];
        var auditList = [];
        var qualityList = [];
        var assessList1 = [];
        var AARList1 = [];
        var auditList1 = [];
        var qualityList1 = [];
        var date = new Date;
        var month = date.getMonth() + 1;
        var queryParams = {
            'area': $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
//	            'projectState':projectState,
            'number': data,
            'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
        };
        $.ajax({
            url: getRootPath() + '/projectReport/projectAssessList',
            type: "post",
            data: queryParams,
            dataType: "json",
            cache: false,
            success: function (data) {
                if (data.assess == null) {
                    assessList = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
                } else {
                    for (var i in data.assess) {
                        assessList.push(data.assess[i]);
                    }
                }
                if (data.AAR == null) {
                    AARList = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
                } else {
                    for (var i in data.AAR) {
                        AARList.push(data.AAR[i]);
                    }
                }
                if (data.audit == null) {
                    auditList = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
                } else {
                    for (var i in data.audit) {
                        auditList.push(data.audit[i]);
                    }
                }
                if (data.quality == null) {
                    qualityList = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
                } else {
                    for (var i in data.quality) {
                        qualityList.push(data.quality[i]);
                    }
                }
                for (var i = 0; i < 12; i++) {
                    if (month + i < 12) {
                        assessList1.splice(i, 0, assessList[i] == "null" ? "0" : assessList[month + i]);
                    } else {
                        assessList1.splice(i, 0, assessList[i] == "null" ? "0" : assessList[i + month - 12]);
                    }
                }
                for (var i = 0; i < 12; i++) {
                    if (month + i < 12) {
                        AARList1.splice(i, 0, assessList[i] == "null" ? "0" : AARList[month + i]);
                    } else {
                        AARList1.splice(i, 0, assessList[i] == "null" ? "0" : AARList[i + month - 12]);
                    }
                }
                for (var i = 0; i < 12; i++) {
                    if (month + i < 12) {
                        auditList1.splice(i, 0, auditList[i] == "null" ? "0" : auditList[month + i]);
                    } else {
                        auditList1.splice(i, 0, auditList[i] == "null" ? "0" : auditList[i + month - 12]);
                    }
                }
                for (var i = 0; i < 12; i++) {
                    if (month + i < 12) {
                        qualityList1.splice(i, 0, qualityList[i] == "null" ? "0" : qualityList[month + i]);
                    } else {
                        qualityList1.splice(i, 0, qualityList[i] == "null" ? "0" : qualityList[i + month - 12]);
                    }
                }
                var myChart = echarts.init(document.getElementById("main"));
                var option = {
                    tooltip: {
                        trigger: 'axis',
                    },
                    color: ['#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed',
                        '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0',
                        '#1e90ff', '#ff6347', '#7b68ee', '#00fa9a', '#ffd700',
                        '#6699FF', '#ff6666', '#3cb371', '#b8860b', '#30e0e0'],
                    legend: {
                        data: ['361评估问题', 'AAR', '开工会审计', '质量回溯']
                    },

                    grid: {
                        left: '3%',   //图表距边框的距离
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        show: true,
                        right: 100,
                        feature: {
                            dataZoom: {
                                yAxisIndex: 'none'
                            },
                            dataView: {readOnly: false},
                            magicType: {type: ['line', 'bar']},
                            restore: {},
                            saveAsImage: {}
                        }
                    },
                    //x轴信息样式
                    xAxis: {
                        type: 'category',
                        boundaryGap: true,
                        data: data.months,
                        //坐标轴颜色
                        axisLine: {
                            lineStyle: {
                                color: 'black'
                            }
                        },
                        //x轴文字旋转
                        axisLabel: {
                            rotate: 0,
                            interval: 0
                        },
                    },
                    yAxis: [
                        {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value} 个'
                            }
                        }
                    ],
                    series: [
                        {
                            type: 'line',
                            symbolSize: 4,   //拐点圆的大小
                            name: '361评估问题',
                            data: assessList1,

                            smooth: false,   //关键点，为true是不支持虚线的，实线就用true
                            itemStyle: {
                                normal: {
                                    lineStyle: {
                                        width: 2,
                                        type: 'solid'  //'dotted'虚线 'solid'实线
                                    }
                                }
                            }
                        },
                        //实线
                        {
                            type: 'line',
                            name: 'AAR',
                            data: AARList1,
                            symbol: 'circle',
                            symbolSize: 4,
                            itemStyle: {
                                normal: {}
                            },
                        },
                        {
                            type: 'line',
                            name: '开工会审计',
                            data: auditList1,
                            symbolSize: 4,
                            itemStyle: {
                                normal: {
                                    lineStyle: {
                                        width: 2,
                                        type: 'solid'  //'dotted'虚线 'solid'实线
                                    }
                                }
                            },
                        },
                        {
                            type: 'line',
                            name: '质量回溯',
                            data: qualityList1,
                            symbol: 'circle',
                            symbolSize: 4,
                            itemStyle: {
                                normal: {}
                            }
                        }
                    ]
                };
                myChart.setOption(option);
            }
        });
    }

    var bsTable;
//		var projectState = "在行";
    var mode = "popup"//编辑框的模式：支持popup和inline两种模式，默认是popup
    function initTableSave(data) {
        var columns = initColumn();
        var queryParams = {
            'area': $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
            'number': data,
            'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
            'limit': 5,
            'pageNumber': 1,
            'offset': 0,

        };
        var url = getRootPath() + '/projectReport/getProjectAssessPageQ';
        bsTable = new BSTable('mytable', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(queryParams);
        bsTable.setPageSize(5);
        // bsTable.setEditable(true);//开启行内编辑
        bsTable.setEditable(false);
        bsTable.setOnEditableSave(initOnEditableSave());
        bsTable.init();
    }

    function initOnEditableSave() {
        return function (field, row, oldValue, $el) {
            $.ajax({
                type: "post",
                url: getRootPath() + '/project/editCommentsList',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify(row),
                success: function (data, status) {
                    if (status == "success") {
                        toastr.success('修改成功！');
                    } else {
                        toastr.success('修改失败！');
                    }
                }
            });
        }
    }

    function initColumn() {
        return [
            {
                title: '序号',
                align: "center",
                width: 100,
                formatter: function (value, row, index) {
                    var pageSize = $('#mytable').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#mytable').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {
                title: '项目名称', field: 'name', width: 550,
                formatter: function (value, row) {
                    var path1 = getRootPath() + '/view/HTML/page.html?url=' + getRootPath() + '/bu/projView?projNo=' + row.no;
                    var pduSpdt = row.pduSpdt == null ? '/' : ('/' + row.pduSpdt);
                    var tab = '<a target="_blank" style="color: #000;;" title="' + value + '" href="' + path1 + '">' +
                        '<div style="width: 100%;text-align: left;">' +
                        '<label style="font-size: 16px;font-weight: unset;">' +
                        '' + value + '-' + row.area +
                        '</label>' +
                        '<div style="font-size: 14px;color: #a3a3a3;">' +
                        '<div style="width: 90%;">' +
                        '<sqan>PM: ' + row.pm + '</sqan>' +
                        '<sqan style="float: right;">结项时间:' + new Date(row.endDate).format('yyyy-MM-dd') + '</sqan>' +
                        '</div>' +
                        '<div style="width: 90%;">' +
                        '<sqan>' + row.hwzpdu + pduSpdt + '</sqan>' +
                        '<sqan style="float: right;">' + row.area + '</sqan>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</a>';
                    return tab
                },
            },
            {
                title: '361评估问题', field: '',
                formatter: function (value, row) {
                    var assessment = row.assessment;
                    return assessment;
                }

            },
            {
                title: 'AAR', field: '',
                formatter: function (value, row) {
                    var aar = row.aar;
                    return aar;
                }
            },
            {
                title: '开工会审计', field: '',
                formatter: function (value, row) {
                    var audit = row.audit;
                    return audit;
                }

            },
            {
                title: '质量回溯', field: '',
                formatter: function (value, row, index) {
                    var quality = row.quality;
                    return quality;
                }

            },
        ]
    };
    $.fn.loadzbTop = function () {
        var queryParams = {
            'area': $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
            'number': data,
            'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
        };
        bsTable.setQueryParams(queryParams);
        bsTable.refresh();
    };
    var projectState = "在行";

    function exportProblem() {
        $("#exportProblem").click(function () {
            var $eleForm = $("<form method='POST'></form>");
            var areaVal = $("#usertype1").selectpicker("val") == null ? "" : $("#usertype1").selectpicker("val").join();
//			var projectStateVal=$("#usertype2").selectpicker("val")==null?"":$("#usertype2").selectpicker("val").join();
            var hwpduVal = $("#usertype3").selectpicker("val") == null ? "" : $("#usertype3").selectpicker("val").join();
            var hwzpduVal = $("#usertype4").selectpicker("val") == null ? "" : $("#usertype4").selectpicker("val").join();
            var pduSpdtVal = $("#usertype5").selectpicker("val") == null ? "" : $("#usertype5").selectpicker("val").join();
            var buVal = $("#usertype6").selectpicker("val") == null ? "" : $("#usertype6").selectpicker("val").join();
            var pduVal = $("#usertype7").selectpicker("val") == null ? "" : $("#usertype7").selectpicker("val").join();
            var duVal = $("#usertype8").selectpicker("val") == null ? "" : $("#usertype8").selectpicker("val").join();
            var clientType = $("#selectBig").selectpicker("val") == "2" ? "0" : "1";
            $eleForm.append($('<input type="hidden" name="area" value="' + areaVal + '">'));
//			$eleForm.append($('<input type="hidden" name="projectState" value="'+ encodeURI(projectState) +'">'));
            $eleForm.append($('<input type="hidden" name="clientType" value="' + clientType + '">'));
            $eleForm.append($('<input type="hidden" name="hwpdu" value="' + hwpduVal + '">'));
            $eleForm.append($('<input type="hidden" name="hwzpdu" value="' + hwzpduVal + '">'));
            $eleForm.append($('<input type="hidden" name="pduSpdt" value="' + pduSpdtVal + '">'));
            $eleForm.append($('<input type="hidden" name="bu" value="' + buVal + '">'));
            $eleForm.append($('<input type="hidden" name="pdu" value="' + pduVal + '">'));
            $eleForm.append($('<input type="hidden" name="du" value="' + duVal + '">'));
            $eleForm.attr("action", getRootPath() + "/projectReport/exportProblem");
            $(document.body).append($eleForm);
            //提交表单，实现下载
            $eleForm.submit();
        })
    }

    $(document).ready(function () {
        $("#index_botton").hide();
        initTableSaveAll(0);
        exportProblem();
        var ing = document.getElementById('ing');
        var add = document.getElementById('add');
        var all = document.getElementById('all');
        var close = document.getElementById('close');
        var delay = document.getElementById('delay');
        $(".weekSelection").click(function () {
            $("#index_botton").show();
            $("#index_top").hide();
            var index = $(this).index();
            if (index != 0) {
                $(this).addClass("tab_active").siblings().removeClass("tab_active");
                initTableSave(index);
                echars(index);
                bsTable.refresh();
            }
            if (index == 0) {
                $(this).addClass("tab_active").siblings().removeClass("tab_active");
                $("#index_botton").hide();
                $("#index_top").show();
                initTableSaveAll(index);
                bsTable.refresh();
            }
        })
    })

//    function selectOnchange() {
//        $().loadzbTop();
//    }


    function initTableSaveAll(data) {
        var columns = initColumnAll();
        var queryParams = {
            'area': $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
            'number': data,
            'projectState': projectState,
            'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
        };
        var url = getRootPath() + '/projectReport/getProjectAssessPage';
        bsTable = new BSTable('mytableAll', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(queryParams);
        bsTable.setPageSize(5);
        bsTable.setEditable(false);
        bsTable.setOnEditableSave(initOnEditableSaveAll());
        bsTable.init();
    }

    function initOnEditableSaveAll() {
        return function (field, row, oldValue, $el) {
            $.ajax({
                type: "post",
                url: getRootPath() + '/project/editCommentsList',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify(row),
                success: function (data, status) {
                    if (status == "success") {
                        toastr.success('修改成功！');
                    } else {
                        toastr.success('修改失败！');
                    }
                }
            });
        }
    }

    function initColumnAll() {
        return [
            {title: '问题Id ', field: 'id', width: 80, visible: false},
            {title: '项目编号 ', field: 'no', width: 80, visible: false},
            {
                title: '序号',
                align: "center",
                width: 60,
                formatter: function (value, row, index) {
                    var pageSize = $('#mytableAll').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#mytableAll').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {
                title: '项目名称', field: 'name', width: 300,
                formatter: function (value, row) {
                    var path1 = getRootPath() + '/view/HTML/page.html?url=' + getRootPath() + '/bu/projView?projNo=' + row.no;
                    var pduSpdt = row.pduSpdt == null ? '/' : ('/' + row.pduSpdt);
                    var tab = '<a target="_blank" style="color: #000;;" title="' + value + '" href="' + path1 + '">' +
                        '<div style="width: 100%;text-align: left;">' +
                        '<label style="font-size: 16px;font-weight: unset;">' +
                        '' + value + '-' + row.area +
                        '</label>' +
                        '<div style="font-size: 14px;color: #a3a3a3;">' +
                        '<div style="width: 90%;">' +
                        '<sqan>PM: ' + row.pm + '</sqan>' +
                        '<sqan style="float: right;">结项时间:' + new Date(row.endDate).format('yyyy-MM-dd') + '</sqan>' +
                        '</div>' +
                        '<div style="width: 90%;">' +
                        '<sqan>' + row.hwzpdu + pduSpdt + '</sqan>' +
                        '<sqan style="float: right;">' + row.area + '</sqan>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</a>';
                    return tab
                },
            },
            {
                title: '361评估问题', field: '', width: 200,
                formatter: function (value, row) {
                    var val1 = (row.degree361).toFixed(2);
                    var val2 = row.open361;
                    val3 = row.closed361;
                    var val4 = row.delay361;
                    var val5 = row.added361;
                    var str = getDegree(val1, val2, val3, val4, val5);
                    return str
                },
            },
            {
                title: 'AAR', field: '', width: 200,
                formatter: function (value, row) {
                    var val1 = (row.degreeAAR).toFixed(2);
                    var val2 = row.openAAR;
                    val3 = row.closedAAR;
                    var val4 = row.delayAAR;
                    var val5 = row.addedAAR;
                    var str = getDegree(val1, val2, val3, val4, val5);
                    return str
                },
            },
            {
                title: '开工会审计', field: '', width: 200,
                formatter: function (value, row) {
                    var val1 = (row.degreeAudit).toFixed(2);
                    var val2 = row.openAudit;
                    val3 = row.closedAudit;
                    var val4 = row.delayAudit;
                    var val5 = row.addedAudit;
                    var str = getDegree(val1, val2, val3, val4, val5);
                    return str
                },
            },
            {
                title: '质量回溯', field: '', width: 200,
                formatter: function (value, row) {
                    var val1 = (row.degreeQuality).toFixed(2);
                    var val2 = row.openQuality;
                    val3 = row.closedQuality;
                    var val4 = row.delayQuality;
                    var val5 = row.addedQuality;
                    var str = getDegree(val1, val2, val3, val4, val5);
                    return str
                },
            }
        ]
    };

    function getDegree(val1, val2, val3, val4, val5) {
        var degree;
        var val = val1 + "%";
        if (val1 == 0.00){
            degree = '<div style="float:left;width: 100px;height: 100px;border:5px solid #f57454;border-radius:50%; ">' +
                '<div style="margin:0 auto;width:80%;height:35px;;border: 0px solid #1adc21;border-bottom: 1px solid #a3a3a3; "><div style="margin-top: 24px;text-align:center;font-weight:bold;font-size:20px;">' + '-' + '</div></div>' +
                '<div style="margin:0 auto;width:80%;height:30%; "><div style="margin-top: 5px;text-align:center;font-size:10px;color: #a3a3a3;">问题闭环率</div></div>' +
                '</div>';
        } else if (val1 <= 40 && val1 > 0) {
            degree = '<div style="float:left;width: 100px;height: 100px;border: 5px solid #f57454;border-radius:50%; ">' +
                '<div style="margin:0 auto;width:80%;height:35px;;border: 0px solid #1adc21;border-bottom: 1px solid #a3a3a3; "><div style="margin-top: 24px;text-align:center;font-weight:bold;font-size:20px;">' + val + '</div></div>' +
                '<div style="margin:0 auto;width:80%;height:30%; "><div style="margin-top: 5px;text-align:center;font-size:10px;color: #a3a3a3;">问题闭环率</div></div>' +
                '</div>';
        } else if (val1 >= 70) {
            degree = '<div style="float:left;width: 100px;height: 100px;border: 5px solid #1adc21;border-radius:50%; ">' +
                '<div style="margin:0 auto;width:80%;height:35px;;border: 0px solid #1adc21;border-bottom: 1px solid #a3a3a3; "><div style="margin-top: 24px;text-align:center;font-weight:bold;font-size:20px;">' + val + '</div></div>' +
                '<div style="margin:0 auto;width:80%;height:30%; "><div style="margin-top: 5px;text-align:center;font-size:10px;color: #a3a3a3;">问题闭环率</div></div>' +
                '</div>';
        } else {
            degree = '<div style="float:left;width: 100px;height: 100px;border: 5px solid #f7b547;border-radius:50%; ">' +
                '<div style="margin:0 auto;width:80%;height:35px;;border: 0px solid #1adc21;border-bottom: 1px solid #a3a3a3; "><div style="margin-top: 24px;text-align:center;font-weight:bold;font-size:20px;">' + val + '</div></div>' +
                '<div style="margin:0 auto;width:80%;height:30%; "><div style="margin-top: 5px;text-align:center;font-size:10px;color: #a3a3a3;">问题闭环率</div></div>' +
                '</div>';
        }
        degree = '<div style="margin:0 auto;width: 200px;height: 105px;">' + degree + '' +
            '<div style="float:left;width: 85px;height: 100px;">' +
            '<div style="margin-top: 3px;margin-left: 12px;font-size:14px;"><div >处理中：   ' + val2 + '</div>' +
            '<div style="margin-top: 7px;font-size:14px;"><div >新增：   ' + val5 + '</div>' +
            '<div style="margin-top: 7px;font-size:14px;"><div >关闭：   ' + val3 + '</div>' +
            '<div style="margin-top: 7px;font-size:14px;"><div >延迟：   ' + val4 + '</div></div>' +
            '</div>';
        return degree;
    }

    $.fn.loadzbTop = function (data) {
        var queryParams = {
            'area': $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),//转换为字符串
            'number': data,
            'clientType': $("#selectBig").selectpicker("val") == "2" ? "0" : "1",
        };
        bsTable.setQueryParams(queryParams);
        bsTable.refresh();
    };
});

function selectOnchange() {
	var index = $(".weekSelection.tab_active").index();
    $().loadzbTop(index);
}


	