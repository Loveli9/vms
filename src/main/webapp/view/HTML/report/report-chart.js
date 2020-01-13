;(function ($) {
    //有横向及纵向轴的图
    $.fn.XYChart = function (options) {
        var that = this;
        var config = options.chartConfig;
        var data = options.data;
        var kpiConfigs = options.kpiConfigs;
        var chart = initChartBox(that, config)
        var options = createBasicOptions(config);

        var legend = {data: [], y: 'bottom', x: 'center'};
        var axis = findAxis(config.axes);
        var xAxis = createAxes(axis, data, kpiConfigs);
        var yAxis = createYAxes(config.axes)
        var series = createXYSeries(config.title, axis, config.series, yAxis, data, kpiConfigs, legend);
        var toolbox = createToolbox(axis);
        var tooltip = createTooltip(config);
        options.tooltip = tooltip;
        options.toolbox = toolbox;
        options.xAxis = xAxis;
        options.yAxis = yAxis;
        options.series = series;
        options.legend = legend;
        chart.setOption(options);
    }
    //饼图
    $.fn.PieChart = function (options) {
        var that = this;
        var chartConfig = options.chartConfig;
        var data = options.data;
        var kpiConfigs = options.kpiConfigs;
        var chart = initChartBox(that, chartConfig)
        var options = createBasicOptions(chartConfig);

        var legend = {data: [], y: 'bottom', x: 'center'};
        var axis = findAxis(chartConfig.axes);
        var series = createPieSeries(chartConfig.title, axis, chartConfig.series, data, kpiConfigs, legend);
        var tooltip = createTooltip(chartConfig);
        options.tooltip = tooltip;
        options.toolbox = {show: true, right: 40, top: 20, feature: {saveAsImage: {}}};
        options.series = series;
        options.legend = legend;
        chart.setOption(options);
    }
    var createPieSeries = function (chartTitle, axis, seriesConfigs, data, kpiConfigs, legend) {
        var serieses = [];
        for (var i = 1; i <= seriesConfigs.length; i++) {
            var seriesConfig = seriesConfigs[i - 1];
            var series = {
                name: seriesConfig.name,
                type: 'pie',
                radius: [20, (100 / seriesConfigs.length) + "%"],
                center: [(100 / seriesConfigs.length) / 2 + (i % 2 == 0 ? 100 / seriesConfigs.length : 0) + "%", "50%"],
                label: {
                    position: 'inside', show: true,
                    emphasis: {show: true, textStyle: {fontSize: '16', fontWeight: 'bold'}}
                },
                labelLine: {normal: {show: true}},
                data: createPieDatas(axis, seriesConfig, data, kpiConfigs, legend)
            }
            if (seriesConfigs.length == 1) {
                series.radius = [20, "70%"];
            }
            serieses.push(series);
        }
        return serieses;
    }
    var createPieDatas = function (axis, seriesConfig, data, kpiConfigs, legend) {
        var seriesDatas = [];
        var axisKpiConfigId = findKpiConfigIdByKpiName(kpiConfigs, axis.fields);
        var seriesKpiConfigId = findKpiConfigIdByKpiName(kpiConfigs, seriesConfig.yFields);
        for (var i = 0; i < data.rows.length; i++) {
            var seriesData = {name: "", value: 0};
            var row = data.rows[i];
            var findCount = 0;
            for (var j = 0; j < row.kpis.length; j++) {
                var kpi = row.kpis[j];
                if (kpi.reportKpiConfigId == seriesKpiConfigId) {
                    var value = kpi.value;
                    if (!$.isEmptyObject(value) && $.isNumeric(value) && seriesConfig.percent == true) {
                        value = kpi.value = kpi.value * 100;
                    }
                    seriesData.value = value;
                    findCount += 1;
                } else if (kpi.reportKpiConfigId == axisKpiConfigId) {
                    seriesData.name = kpi.value;
                    legend.data.push(kpi.value)
                    findCount += 1;
                }
                if (findCount == 2) break;
            }
            seriesDatas.push(seriesData)
        }
        return seriesDatas;
    }
    //创建鼠标提示内容
    var createTooltip = function (config) {
        var series = config.series;
        var tooltip = {
            //当前鼠标位置使用虚线划出来
            trigger: config.pie == true ? "item" : 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: 'red'
                }
            }
        };
        return tooltip;
    }
    var createYAxes = function (axes) {
        var yAxis = [];
        $.each(axes, function (idx, item) {
            if (item.position != 'bottom') {
                yAxis.push({
                    nameLocation: 'middle',
                    nameGap: 45,
                    fields: item.fields.replace(/\s/g, '').split(','),
                    type: item.type == 'numeric' ? 'value' : item.type,
                    position: item.position,
                    name: item.title || item.fields,
                    axisLine: {
                        lineStyle: {
                            color: 'gray'
                        }
                    },
                    axisLabel: {
                        formatter: function (value, index) {
                            if (item.percent == true) {
                                return value + '%'
                            }
                            return value;
                        }
                    }
                });
            }
        })
        return yAxis;
    }
    var createAxes = function (axis, data, kpiConfigs) {
        var datas = []
        var reportKpiConfig = findKpiConfigByKpiName(kpiConfigs, axis.fields);
        $.each(data.rows, function (idx, row) {
            var finded = false;
            for (var i = 0; i < row.kpis.length; i++) {
                var kpi = row.kpis[i];
                if (kpi.reportKpiConfigId == reportKpiConfig.id) {
                    var value = kpi.value;
                    if (axis.type == 'numeric') {
                        if (reportKpiConfig.dataType == 'int') {
                            value = parseInt(value);
                        } else if (reportKpiConfig.dataType == 'float') {
                            value = parseFloat(value);
                            value = fomatFloat(value, 2);
                        }
                    }
                    datas.push(value);
                    finded = true;
                }
            }
            if (finded == false) {
                datas.push();
            }
        })

        var xAxis = [{
            type: axis.type == 'numeric' ? 'value' : axis.type,
            name: axis.title,
            nameGap: 20,
            data: datas,
            axisLabel: {
                formatter: function (value, index) {
                    return value;
                }
            },
            axisLine: {
                lineStyle: {
                    color: 'gray'
                }
            },
            //x轴交点为一组数据
            axisPointer: {
                type: 'shadow'
            }
        }];
        return xAxis;
    }
    var createXYSeries = function (chartTitle, axis, seriesConfigs, yAxis, data, kpiConfigs, legend) {
        var serieses = [];
        //循环处理series配置
        for (var j = 0; j < seriesConfigs.length; j++) {
            var item = seriesConfigs[j];
            var yFields = item.yFields.split(",");
            //如果存在多个y轴字段时生成多生series
            for (var i = 0; i < yFields.length; i++) {
                var yField = yFields[i];
                if (yAxis.length > 1) {
                    var yAxisIndex = findYAxisIndex(yAxis, yField);
                    if (yAxisIndex < 0) {
                        toastr.error("图“" + chartTitle + "”中的Series引用的轴“" + item.axis + "”中不存在“" + yField + "”数据字段！");
                        break;
                    }
                }
                var series = {
                    barMaxWidth: 60,
                    data: createXYDatas(axis, item, data, yField, kpiConfigs),
                    type: item.type,
                    stack: item.stacked,
                    name: item.name,
                    label: {show: true},
                    yAxisIndex: yAxisIndex
                };

                legend.data.push(item.name);
                serieses.push(series);
                if (series.type == 'area') {
                    series.type = "line";
                    series.areaStyle = {};
                } else if (series.type == 'scatter') {
                    delete series.label;
                    series.symbolSize = 20;
                    //'linear', 'exponential', 'logarithmic', 'polynomial'。
                    var myRegression = ecStat.regression('exponential', series.data);
                    myRegression.points.sort(function (a, b) {
                        return a[0] - b[0];
                    });
                    serieses.push({
                        name: item.name + "-趋势",
                        type: 'line',
                        showSymbol: false,
                        smooth: true,
                        data: myRegression.points
                    });

                    legend.data.push(item.name + "-趋势");
                } else if (series.type == "smoothline") {
                    series.type = "line";
                    series.smooth = true;
                }
                if (series.type == "line") {
                    series.symbolSize = 10;
                    series.showSymbol = true;
                }
            }
        }
        return serieses;
    }
    var findAxis = function (axes) {
        var axis = null;
        for (var i = 0; i < axes.length; i++) {
            var item = axes[i];
            if (($.trim(item.position) == 'Bottom', $.trim(item.position) == 'bottom')) {
                axis = item;
                break;
            }
        }
        return axis;
    }
    var createXYDatas = function (axis, series, data, yField, kpiConfigs) {
        var datas = [];
        var rows = data.rows;
        $.each(rows, function (i, row) {
            var axisKpiConfigId = findKpiConfigIdByKpiName(kpiConfigs, axis.fields);
            var seriesKpiConfigId = findKpiConfigIdByKpiName(kpiConfigs, series.yFields);
            var findCount = 0;
            var values = {};
            for (var i = 0; i < row.kpis.length; i++) {
                var kpi = row.kpis[i];
                if (kpi.reportKpiConfigId == seriesKpiConfigId) {
                    var value = kpi.value;
                    if (!$.isEmptyObject(value) && $.isNumeric(value)) {
                        if (series.percent == true) {
                            value = kpi.value = value * 100;
                            value = fomatFloat(value, 2);
                        } else if (value % 0 !== 0) {
                            value = fomatFloat(value, 2);
                        }
                    }
                    if (series.type == 'scatter') {
                        values.b = value;
                    } else {
                        datas.push(value);
                    }
                    findCount++;
                } else if (series.type == 'scatter' && kpi.reportKpiConfigId == axisKpiConfigId) {
                    var value = kpi.value;
                    if (!$.isEmptyObject(value) && $.isNumeric(value)) {
                        if (series.percent == true) {
                            value = kpi.value = value * 100;
                            value = fomatFloat(value, 2);
                        } else if (value % 0 !== 0) {
                            value = fomatFloat(value, 2);
                        }
                    }
                    values.a = value;
                    findCount++;
                }
            }
            if (series.type != "scatter") {
                if (findCount == 1) {
                    datas.push();
                }
            } else {
                datas.push([values.a, values.b])
            }
        })

        return datas;
    }
    var findKpiConfigIdByKpiName = function (kpiConfigs, yField) {
        var kcid = null;
        $.each(kpiConfigs, function (i, kc) {
            if (kc.reportKpiConfig.kpiName == yField) {
                kcid = kc.reportKpiConfig.id;
                return true;
            }
        });
        return kcid;
    }
    var findKpiConfigByKpiName = function (kpiConfigs, yField) {
        for (var i = 0; i < kpiConfigs.length; i++) {
            var kc = kpiConfigs[i];
            if (kc.reportKpiConfig.kpiName == yField) {
                return kc.reportKpiConfig;
            }
        }
        return null;
    }
    var createBasicOptions = function (config) {
        var options = {
            calculable: true,
            grid: {
                top: 90,
                left: 30,
                right: 100,
                bottom: 40,
                containLabel: true
            },
            title: {
                text: config.title || '',
                subtext: config.description || ""
            },
            axisLabel: {
                formatter: function (value, index) {
                    return value
                }
            },
            //x轴交点为一组数据
            axisPointer: {
                type: 'shadow'
            }
        }
        return options;
    }
    var initChartBox = function (that, config) {
        var chartBox = $('<div class="chart-box" style="height:400px;padding-top:40px;"></div>')
        if (config.width) {
            chartBox.addClass("col-lg-" + config.width);
        } else {
            chartBox.addClass("col-lg-12");
        }
        if (config.height) {
            chartBox.css({height: config.height + "px"});
        } else {
            chartBox.css({height: "400px"});
        }
        that.append(chartBox);
        var chart = echarts.init(chartBox[0], config.theme);
        return chart;
    }
    var findYAxisIndex = function (yAxis, field) {
        for (var i = 0; i < yAxis.length; i++) {
            var fields = yAxis[i].fields;
            if (fields.indexOf(field) >= 0) {
                return i;
            }
        }
        return -1;
    }

    var createToolbox = function (axis) {
        return {
            show: true,
            right: 40,
            top: 20,
            feature: {
                dataView: {
                    title: '数据',
                    show: true,
                    readOnly: false,
                    backgroundColor: '#CCC',
                    textareaBorderColor: "#000",
                    optionToContent: function (opt) {
                        var datas = [];
                        var rows = 0;
                        var html = "<style>";
                        html += "",
                            html += ".echart-dataview{border-left:1px solid gray;border-top:1px solid gray;margin:auto;}";
                        html += ".echart-dataview td{border-right:1px solid gray;border-bottom:1px solid gray;height:25px;}";
                        html += ".echart-dataview th{border-right:1px solid gray;border-bottom:1px solid gray;height:28px;background:#999;}";
                        html += "</style>";
                        html += '<table class="echart-dataview" style="width:95%;text-align:center;" cellspacing="0" cellpadding="0"><thead><tr>';
                        var axes = opt.xAxis;
                        //创建表头
                        $.each(axes, function (idx, axis) {
                            html += '<th>' + axis.name + '</th>';
                            datas.push(axis.data);
                            if (axis.data.length > rows) {
                                rows = axis.data.length;
                            }
                        })

                        var series = opt.series;
                        $.each(series, function (idx, item) {
                            html += '<th>' + item.name + '</th>';
                            datas.push(item.data);
                        })
                        html += '</tr></thead>';


                        html += '</tbody>';

                        var lines = axes.length + series.length;
                        for (var i = 0; i < rows; i++) {
                            html += '<tr>';
                            for (var j = 0; j < lines; j++) {
                                var value = datas[j][i];
                                console.log(value + '=' + (typeof value));
                                if ((value == null && value == undefined) || (typeof value == 'number' && isNaN(value))) {
                                    value = "";
                                }
                                html += '<td>' + value + '</td>';
                            }
                            html += '</tr>';
                        }
                        html += '</tbody></table>';
                        return html;
                    }
                },
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        };
    };
})
(jQuery);