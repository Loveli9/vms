$(function () {
    $(document).on("click", ".topNav li", function() {
        $(this.parentNode.children).removeClass("active");
        $(this.parentNode.children).each(function () {
            var tab = $(this).attr("tabname");
            $("#"+tab).css('display', 'none');
        });
        $(this).addClass("active");
        var id = $(this).attr("tabname");
        $("#" + id).css('display', 'block');
    });

    var kpi_type = getSelectValueByType("kpi_type");
    var light_up_rule = getSelectValueByType("light_up_rule");
    var period = getSelectValueByType("period");
    //报表配置列表初始化
    $('#reportTable').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + "/report/reportConfig/query_sytem_report",
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        clickToSelect: true,//是否启用点击选中行
        onDblClickRow: function () {
            editReportHandler();
        },
        responseHandler: function (res) {
            return {
                "rows": res.data,
                "total": res.totalCount
            }
        },
        toolbar: '#reportToolbar',
        toolbarAlign: 'right',
        pageSize: 10, //单页记录数
        pageList: [5, 10, 20, 40], //分页步进值
        queryParams: function (params) {
            var param = {
                'size': params.limit,
                'current': params.pageNumber,
                'queryStr': $('#queryName').val()
            }
            return param;
        },
        columns: [
            {
                radio: true  //第一列显示单选框
            },
            {
                filed: 'id',
                visible: false
            },
            {
                title: '名称',
                field: 'name',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '类型',
                field: 'type',
                align: "center",
                width: 100,
                valign: 'middle',
                formatter: function (value, row, index) {
                    return formatColumnVal(value, kpi_type);
                }
            },
            {
                title: '周期',
                field: 'period',
                align: "center",
                width: 100,
                valign: 'middle',
                formatter: function (value, row, index) {
                    return formatColumnVal(value, period);
                }
            },
            {
                title: '列自适应',
                field: 'forceFit',
                align: "center",
                width: 100,
                valign: 'middle',
                formatter: function (value, row, index) {
                    return (value == true || value == "true") ? "是" : "否";
                }
            },
            {
                title: '描述',
                field: 'description',
                align: "center",
                width: 100,
                valign: 'middle'
            }
        ],
        locale: 'zh-CN'//中文支持
    });
    //报表配置模糊查询
    $('#report_search_btn').click(function () {
        $('#reportTable').bootstrapTable('refresh');
    });
    //报表配置模糊查询重置
    $('#report_clear_btn').click(function () {
        $('#queryName').val('');
        $('#reportTable').bootstrapTable('refresh');
    });

    /*********************************  报表配置编辑   *******************************/
    function initReportEditForm() {
        $("#kpi").click();
        setOption(kpi_type, "#type", "分类");
        setOption(period, "#period", "周期");
    }

    function initReportEditPage(data) {
        if (data.code == '200') {
            if (data.data) {
                $("#id").val(data.data.id);
                $("#name").val(data.data.name);
                $("#type").val(data.data.type);
                $("#period").val(data.data.period);
                $("#forceFit").find("option[value=" + data.data.forceFit + "]").prop("selected", true);
                $("#description").val(data.data.description);
            }
        } else {
            toastr.error('服务请求失败，请稍后再试！');
        }
    }
    //打开报表配置新增页面
    $('#report_add_btn').click(function () {
        document.getElementById("reportEditForm").reset();
        initReportEditForm();
        initReportKpiTable([], light_up_rule);
        initReportChartTable([]);
        $("#reportEditPage").modal('show');
    });

    reportFormValidator();
    $('#reportEditPage').on('hidden.bs.modal', function () {
        $("#reportEditForm").data('bootstrapValidator').destroy();
        $('#reportEditForm').data('bootstrapValidator', null);
        reportFormValidator();
    });
    //隐藏报表配置编辑页面
    $('#report_back_btn').click(function () {
        $("#reportEditPage").modal('hide');
    });
    //打开报表配置修改页面
    $('#report_edit_btn').click(function () {
        editReportHandler();
    });
    var editReportHandler = function () {
        var selectRow = $('#reportTable').bootstrapTable('getSelections');
        if (selectRow.length != 1) {
            toastr.warning('请先选择一条数据');
            return;
        }
        initReportEditForm();
        $.ajax({
            contentType: "application/x-www-form-urlencoded", //设置请求头信息
            url: getRootPath() + '/report/reportConfig/get_by_id',
            type: 'post',
            data: {
                'id': selectRow[0].id
            },
            success: function (resp) {
                initReportEditPage(resp);
                initReportKpiTable(resp.data.kpiConfigRefs, light_up_rule);
                initReportChartTable(resp.data.charts);
                $("#reportEditPage").modal('show');
            }
        });
    }


    //报表配置移除
    $('#report_delete_btn').click(function () {
        var data = $('#reportTable').bootstrapTable('getSelections');
        if (data.length == 0) {
            toastr.warning('请先选择需要移除的数据');
            return;
        }
        var reportConfigIds = [];
        for (j = 0, len = data.length; j < len; j++) {
            reportConfigIds[j] = data[j].id;
        }
        $.ajax({
            url: getRootPath() + '/report/reportConfig/delReportConfigs',
            type: 'post',
            dataType: "json",
            contentType: 'application/x-www-form-urlencoded', //设置请求头信息
            data: {'reportConfigIds': reportConfigIds.join(',')},
            success: function (data) {
                if (data.code == '200') {
                    toastr.success(data.message);
                    $('#reportTable').bootstrapTable('refresh');
                } else {
                    toastr.error(data.message);
                }

            }
        });
    });

    //报表配置保存
    $('#report_save_btn').click(function () {
        $('#reportEditForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#reportEditForm").data('bootstrapValidator').isValid()) {
            var kpiConfigRefs = $('#reportKpiTable').bootstrapTable('getData');
            if (kpiConfigRefs.length == 0) {
                toastr.warning('请先添加指标配置数据');
                return;
            }
            for (j = 0, len = kpiConfigRefs.length; j < len; j++) {
                kpiConfigRefs[j].idx = j + 1;
            }
            var charts = $('#reportChartTable').bootstrapTable('getData');
            var formJSON = $('#reportEditForm').serializeJSON();
            formJSON['kpiConfigRefs'] = kpiConfigRefs;
            formJSON['charts'] = charts;
            $.ajax({
                url: getRootPath() + '/report/reportConfig/saveReportConfig',
                type: 'post',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify(formJSON),
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == '200') {
                        toastr.success('报表配置保存成功！');
                        $('#reportTable').bootstrapTable('refresh');
                        $("#reportEditPage").modal('hide');
                    } else {
                        toastr.error('报表配置保存失败!');
                    }
                }
            });
        }
    });
    $("#type").change(function () {
        $('#reportKpiTable').bootstrapTable('removeAll');
    });

    /*********************************  指标配置项选择   *******************************/
    //打开指标配置选择页面
    $('#report_kpi_add').click(function () {
        var type = $("#type").val();
        if (type == '' || type.length == 0) {
            toastr.warning('请先选择报表配置的分类');
            return;
        }
        var data = $('#reportKpiTable').bootstrapTable('getData');
        var ids = [];
        for (j = 0, len = data.length; j < len; j++) {
            ids[j] = data[j].reportKpiConfigId;
        }
        initKpiTable(ids.join(), light_up_rule);
        $("#reportKpiAddPage").modal('show');
    });
    //移除指标配置
    $('#report_kpi_delete').click(function () {
        var data = $('#reportKpiTable').bootstrapTable('getSelections');
        if (data.length == 0) {
            toastr.warning('请先选择需要移除的数据');
            return;
        }
        var ids = [];
        for (j = 0, len = data.length; j < len; j++) {
            ids[j] = data[j].reportKpiConfigId;
        }
        $('#reportKpiTable').bootstrapTable('remove', {field: 'reportKpiConfigId', values: ids});
    });
    //隐藏指标配置选择页面
    $('#kpi_back_btn').click(function () {
        $("#reportKpiAddPage").modal('hide');
    });
    //新增指标配置保存
    $('#kpi_save_btn').click(function () {
        var data = $('#kpiTable').bootstrapTable('getSelections');
        if (data.length == 0) {
            toastr.warning('请先选择需要添加的数据');
            return;
        }
        var count = $('#reportKpiTable').bootstrapTable('getData').length;
        for (j = 0, len = data.length; j < len; j++) {
            var row = {
                'kpiName': data[j].kpiName,
                'maxValue': data[j].maxValue,
                'minValue': data[j].minValue,
                'targetValue': data[j].targetValue,
                'lightUpRule': data[j].lightUpRule,
                'reportKpiConfigId': data[j].id,
                'reportKpiConfig': data[j],
                'reportConfigId': $("#id").val(),
                'readOnly': data[j].expression ? true : false,
                'hidden': false,
                'idx': count + 1
            }
            $('#reportKpiTable').bootstrapTable('insertRow', {index: count, row: row});
            count++;
        }
        //$('#reportKpiTable').bootstrapTable('append', selectRow);
        $("#reportKpiAddPage").modal('hide');
        $("#reportEditPage").css("overflow", "auto");
    });

    //指标配置选择模糊查询
    $('#kpi_search_btn').click(function () {
        $('#kpiTable').bootstrapTable('refresh');
    });
    //指标配置选择模糊查询重置
    $('#kpi_clear_btn').click(function () {
        $('#queryKpiName').val('');
        $('#kpiTable').bootstrapTable('refresh');
    });

    /*********************************  图配置编辑   *******************************/
    //新增图配置编辑页面
    $('#report_chart_add').click(function () {
        document.getElementById("chartEditForm").reset();
        initChartAxisTable([]);
        initChartSeriesTable([]);
        $("#theme").find("option[value='shine']").prop("selected", true);
        $("#chart_axis").click();
        $("#reportChartEditPage").modal('show');
    });
    //修改图配置编辑页面
    $('#report_chart_edit').click(function () {
        editChartHandler();
    });

    chartFormValidator();
    $('#reportChartEditPage').on('hidden.bs.modal', function () {
        $("#chartEditForm").data('bootstrapValidator').destroy();
        $('#chartEditForm').data('bootstrapValidator', null);
        chartFormValidator();
    });

    //移除图配置
    $('#report_chart_delete').click(function () {
        var data = $('#reportChartTable').bootstrapTable('getSelections');
        if (data.length == 0) {
            toastr.warning('请先选择需要移除的数据');
            return;
        }
        var ids = [];
        var idxs = [];
        for (j = 0, len = data.length; j < len; j++) {
            if(null!=data[j].id){
                ids[j] = data[j].id;
            }else{
                idxs[j] = data[j].idx;
            }
        }
        $('#reportChartTable').bootstrapTable('remove', {field: 'idx', values: idxs});
        $('#reportChartTable').bootstrapTable('remove', {field: 'id', values: ids});
    });
    //隐藏图配置页面
    $('#chart_back_btn').click(function () {
        $("#reportChartEditPage").modal('hide');
    });
    //保存图配置
    $('#chart_save_btn').click(function () {
        $('#chartEditForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#chartEditForm").data('bootstrapValidator').isValid()) {
            var axes = $('#chartAxisTable').bootstrapTable('getData');
            if (axes.length == 0) {
                toastr.warning('请先配置轴配置数据');
                return;
            }
            var positions = [];
            for (j = 0, len = axes.length; j < len; j++) {
                if(''==axes[j].title){
                    toastr.warning('轴配置标题数据不允许为空！！！');
                    return;
                }
                axes[j].fields= axes[j].fields+'';
                positions[j]=axes[j].position;
            }
            if (isRepeat(positions)) {
                toastr.warning('轴配置位置数据不允许重复！！！');
                return;
            }

            var series = $('#chartSeriesTable').bootstrapTable('getData');
            if (series.length == 0) {
                toastr.warning('请先配置SERIES配置数据');
                return;
            }
            for (j = 0, len = series.length; j < len; j++) {
                if(''==series[j].name){
                    toastr.warning('SERIES配置名称数据不允许为空！！！');
                    return;
                }
                series[j].yFields= series[j].yFields+'';
            }

            var chartJSON = $('#chartEditForm').serializeJSON();
            var count = $('#reportChartTable').bootstrapTable('getData').length;
            var row = {
                'title': chartJSON.title,
                'description': chartJSON.description,
                'width': chartJSON.width,
                'height': chartJSON.height,
                'theme': chartJSON.theme,
                'axes' : axes,
                'series' : series,
                'idx': count + 1
            };
            if($('#pie').prop('checked')){
                row.pie = true;
            }else{
                row.pie = false;
            }
            if(chartJSON.idx!= ''){
                if (chartJSON.id != ''){
                    var id = Number(chartJSON.id);
                    $('#reportChartTable').bootstrapTable('remove', {field: 'id', values: [id]});
                    row.id = id;
                }else{
                    $('#reportChartTable').bootstrapTable('remove', {field: 'idx', values: [ Number(chartJSON.idx)]});
                }
            }
            $('#reportChartTable').bootstrapTable('insertRow',{index:count,row:row});
            $("#reportChartEditPage").modal('hide');
            $("#reportEditPage").css("overflow", "auto");
        }
    });
    //饼图切换
    $("#pie").change(function() {
        initChartSeriesTable([]);
    });
    //新增轴配置
    $('#chart_axis_add').click(function () {
        var count = $('#chartAxisTable').bootstrapTable('getData').length;
        if(count<3){
            $('#chartAxisTable').bootstrapTable('insertRow',{index:count,row:{'title':'','percent':false,'idx': count + 1}});
        }else{
            toastr.warning('轴配置数据最多为三条且位置不能重复！！！');
            return;
        }
    });
    //移除轴配置
    $('#chart_axis_delete').click(function () {
        var axes = $('#chartAxisTable').bootstrapTable('getSelections');
        if (axes.length == 0) {
            toastr.warning('请先选择要移除的轴配置数据');
            return;
        }
        var ids = [];
        var idxs = [];
        for (j = 0, len = axes.length; j < len; j++) {
            if(null!=axes[j].id){
                ids[j] = axes[j].id;
            }else{
                idxs[j] = axes[j].idx;
            }
        }

        $('#chartAxisTable').bootstrapTable('remove', {field: 'id', values: ids});
        $('#chartAxisTable').bootstrapTable('remove', {field: 'idx', values: idxs});
    });
    //新增SERIES配置
    $('#chart_series_add').click(function () {
        var count = $('#chartSeriesTable').bootstrapTable('getData').length;
        var row = {'name':'','percent':false,'stacked':false,'fill':false,'idx': count + 1};
        if($('#pie').prop('checked')){
            if(count<2){
                row.type = 'pie';
                $('#chartSeriesTable').bootstrapTable('insertRow',{index:count,row:row});
                //$("#chartSeriesTable tr[data-index='"+count+"'] a[data-name='type']").editable('toggleDisabled',false);
                $("#chartSeriesTable tr a[data-name='type']").editable('toggleDisabled',false);
            }else{
                toastr.warning('SERIES配置最多为两条条！！！');
                return;
            }

        }else{
            $('#chartSeriesTable').bootstrapTable('insertRow',{index:count,row:row});
        }

    });
    //移除SERIES配置
    $('#chart_series_delete').click(function () {
        var series = $('#chartSeriesTable').bootstrapTable('getSelections');
        if (series.length == 0) {
            toastr.warning('请先选择要移除的SERIES配置数据');
            return;
        }

        var ids = [];
        var idxs = [];
        for (j = 0, len = series.length; j < len; j++) {
            if(null!=series[j].id){
                ids[j] = series[j].id;
            }else{
                idxs[j] = series[j].idx;
            }
        }

        $('#chartSeriesTable').bootstrapTable('remove', {field: 'id', values: ids});
        $('#chartSeriesTable').bootstrapTable('remove', {field: 'idx', values: idxs});
    });
})

function reportFormValidator() {
    $('#reportEditForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '名称不能为空'
                    }
                }
            },
            type: {
                validators: {
                    notEmpty: {
                        message: '类型不能为空'
                    }
                }
            },
            period: {
                validators: {
                    notEmpty: {
                        message: '管理维度不能为空'
                    }
                }
            }
        }
    });
}

function chartFormValidator() {
    $('#chartEditForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            title: {
                validators: {
                    notEmpty: {
                        message: '名称不能为空'
                    }
                }
            },
            width: {
                validators: {
                    notEmpty: {
                        message: '宽度（栅格数）不能为空'
                    }
                }
            },
            height: {
                validators: {
                    notEmpty: {
                        message: '高度（px）不能为空'
                    }
                }
            }
        }
    });
}

function initReportKpiTable(data, light_up_rule) {
    var whether = [{value: true, text: "是"}, {value: false, text: "否"}];

    $('#reportKpiTable').bootstrapTable('destroy');
    $('#reportKpiTable').bootstrapTable({
        data: data,
        striped: true, //是否显示行间隔色
        pagination: false, //是否分页
        editable: true,//开启编辑模式
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        sortName: "idx",                     //排序字段
        toolbar: '#reportKpiToolbar',
        toolbarAlign: 'right',
        columns: [
            {
                radio: true  //第一列显示单选框
            },
            {
                filed: 'id',
                visible: false
            },
            {
                filed: 'reportKpiConfigId',
                visible: false
            },
            {
                title: '名称',
                field: 'kpiName',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '上限',
                field: 'maxValue',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: "text",
                    title: '请选择上限',
                    emptytext: '&#12288',
                }
            },
            {
                title: '下限',
                field: 'minValue',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: "text",
                    title: '请选择下限',
                    emptytext: '&#12288',
                }
            },
            {
                title: '目标',
                field: 'targetValue',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: "text",
                    title: '请选择上限',
                    emptytext: '&#12288',
                }
            },
            {
                title: '亮灯规则',
                field: 'lightUpRule',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '请选择亮灯规则',
                    emptytext: '&#12288',
                    source: light_up_rule,
                    valueField: 'value',
                    textField: 'key'
                }
            },
            {
                title: '数据只读',
                field: 'readOnly',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '请选择数据只读',
                    emptytext: '&#12288',
                    source: whether
                }
            },
            {
                title: '隐藏',
                field: 'hidden',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '请选择隐藏',
                    emptytext: '&#12288',
                    source: whether
                }
            }
        ],
        locale: 'zh-CN'//中文支持
    });
}

function initKpiTable(kpiConfigIds, light_up_rule) {
    var manage_group = getSelectValueByType("manage_group");
    var data_type = getSelectValueByType("data_type");
    $('#kpiTable').bootstrapTable('destroy');
    $('#kpiTable').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + "/report/reportConfig/queryExcluedKpiConfigsByIds",
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        editable: false,//开启编辑模式
        toolbar: '#kpiToolbar',
        toolbarAlign: 'right',
        clickToSelect: true,//是否启用点击选中行
        responseHandler: function (res) {
            return {
                "rows": res.data,
                "total": res.totalCount
            }
        },
        pageSize: 10, //单页记录数
        pageList: [5, 10, 20, 40], //分页步进值
        queryParams: function (params) {
            var types = $('#type').val();
            if ($('#period').val() == 1) {
                types += ",10";
            } else {
                types += ",9";
            }
            if ($('#type').val() == 3) {
                types += ",8";
            } else if ($('#type').val() == 2) {
                types += ",7";
            }
            var param = {
                'size': params.limit,
                'current': params.pageNumber,
                'kpiName': $('#queryKpiName').val(),
                'kpiTypes': types,
                'kpiConfigIds': kpiConfigIds
            }
            return param;
        },
        columns: [
            {
                radio: true  //第一列显示单选框
            },
            {
                filed: 'id',
                visible: false
            },
            {
                title: '指标名称',
                field: 'kpiName',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '管理维度',
                field: 'manageGroup',
                align: "center",
                width: 100,
                valign: 'middle',
                formatter: function (value, row, index) {
                    return formatColumnVal(value, manage_group);
                }
            },
            {
                title: '数据类型',
                field: 'dataType',
                align: "center",
                width: 100,
                valign: 'middle',
                formatter: function (value, row, index) {
                    return formatColumnVal(value, data_type);
                }
            },
            {
                title: '上限',
                field: 'maxValue',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '下限',
                field: 'minValue',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '目标',
                field: 'targetValue',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '亮灯规则',
                field: 'lightUpRule',
                align: "center",
                width: 100,
                valign: 'middle',
                formatter: function (value, row, index) {
                    return formatColumnVal(value, light_up_rule);
                }
            },
            {
                title: '描述',
                field: 'description',
                align: "center",
                width: 200,
                valign: 'middle'
            }
        ],
        locale: 'zh-CN'//中文支持
    });
}

function initReportChartTable(data) {
    $('#reportChartTable').bootstrapTable('destroy');
    $('#reportChartTable').bootstrapTable({
        data: data,
        striped: true, //是否显示行间隔色
        pagination: false, //是否分页
        clickToSelect: true,//是否启用点击选中行
        onDblClickRow: function () {
            editChartHandler();
        },
        toolbar: '#reportChartToolbar',
        toolbarAlign: 'right',
        columns: [
            {
                radio: true  //第一列显示单选框
            },
            {
                filed: 'id',
                visible: false
            },
            {
                title: '标题',
                field: 'title',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '主题',
                field: 'theme',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '描述',
                field: 'description',
                align: "center",
                width: 150,
                valign: 'middle'
            },
            {
                title: '宽度（栅格数）',
                field: 'width',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '高度（px）',
                field: 'height',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '饼图',
                field: 'pie',
                align: "center",
                width: 100,
                valign: 'middle',
                formatter: function (value, row, index) {
                    return (value == true || value == "true") ? "是" : "否";
                }
            }
        ],
        locale: 'zh-CN'//中文支持
    });
}

function editChartHandler() {
    var data = $('#reportChartTable').bootstrapTable('getSelections');
    if (data.length != 1) {
        toastr.warning('请先选择一条数据');
        return;
    }
    $("#chart_axis").click();
    $("#chartId").val(data[0].id);
    $("#idx").val(data[0].idx);
    $("#title").val(data[0].title);
    $("#width").val(data[0].width);
    $("#theme").find("option[value=" + data[0].theme + "]").prop("selected", true);
    $("#height").val(data[0].height);

    $("input:checkbox").removeAttr("checked");
    $("input[name='pie'][value='"+data[0].pie+"']").prop("checked",true);

    $("#chartDescription").val(data[0].description);

    initChartAxisTable(data[0].axes);
    initChartSeriesTable(data[0].series);
    $("#reportChartEditPage").modal('show');
}

function initChartAxisTable(data) {
    $('#chartAxisTable').bootstrapTable('destroy');
    $('#chartAxisTable').bootstrapTable({
        data: data,
        striped: true, //是否显示行间隔色
        pagination: false, //是否分页
        editable: true,//可行内编辑
        toolbar: '#chartAxisToolbar',
        toolbarAlign: 'right',
        onEditableSave: function (field, row, oldValue, $el) {
            if(field=='type'){
                var index =$el.parent().parent().data('index');
                if(row[field]!='numeric'){
                    $("#chartAxisTable tr[data-index='"+index+"'] a[data-name='percent']").editable('toggleDisabled',false);
                }else{
                    $("#chartAxisTable tr[data-index='"+index+"'] a[data-name='percent']").editable('toggleDisabled',true);
                }
            }
        },
        columns: [
            {
                radio: true  //第一列显示单选框
            },
            {
                filed: 'id',
                visible: false
            },
            {
                title: '标题',
                field: 'title',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'text',
                    title: '请输入标题',
                    emptytext: '&#12288',
                    validate: function (v) {
                        if ($.trim(v) == '') {
                            return '标题不能为空';
                        }
                    }
                }
            },
            {
                title: '类型',
                field: 'type',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: "select",
                    title: '请选择类型',
                    emptytext: '&#12288',
                    source: [{value: "category", text: "category"}, {value: "time", text: "time"}, {value: "numeric", text: "numeric"}]
                }
            },
            {
                title: '位置',
                field: 'position',
                align: "center",
                width: 150,
                valign: 'middle',
                editable: {
                    type: "select",              //编辑框的类型。支持text|textarea|select|date|checklist等
                    title: '请选择位置',
                    emptytext: '&#12288',
                    source: [{value: "bottom", text: "底部"},
                             {value: "left", text: "左侧"},
                             {value: "right", text: "右侧"}]
                }
            },
            {
                title: '数据字段',
                field: 'fields',
                align: "center",
                width: 150,
                valign: 'middle',
                editable: {
                    type: 'checklist',              //编辑框的类型。支持text|textarea|select|date|checklist等
                    title: '请选择数据字段',
                    emptytext: '&#12288',
                    source: function(){
                        var result = [];
                        //获取类型
                        var type=$(this).parents("tr").children("td").eq(2).text();
                        var data = $('#reportKpiTable').bootstrapTable('getData');

                        for (j = 0, len = data.length; j < len; j++) {
                            var dataType = data[j].reportKpiConfig.dataType;
                            if(type == 'category' && dataType == 'string'){
                                result.push({ value: data[j].kpiName, text: data[j].kpiName });
                            }else if(type == 'time' && dataType == 'date'){
                                result.push({ value: data[j].kpiName, text: data[j].kpiName });
                            }else if(type == 'numeric' && (dataType == 'int' || dataType == 'float')){
                                result.push({ value: data[j].kpiName, text: data[j].kpiName });
                            }
                        }
                        return result;
                    }
                }
            },
            {
                title: '百分比',
                field: 'percent',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '百分比',
                    emptytext: '&#12288',
                    source: [{value: true, text: "是"}, {value: false, text: "否"}],
                    toggleDisabled:function(isEnable){
                        if (isEnable){
                            this.enable();// 可编辑
                        } else {
                            this.disabled();// 不可编辑
                        }
                    }
                }
            }
        ],
        locale: 'zh-CN'//中文支持
    });
    var data = $('#chartAxisTable').bootstrapTable('getData');
    for (j = 0, len = data.length; j < len; j++) {
        if(data[j].type!='numeric'){
            $("#chartAxisTable tr[data-index='"+j+"'] a[data-name='percent']").editable('toggleDisabled',false);
        }
    }
}

function initChartSeriesTable(data) {
    var editType = 'checklist';
    if($('#pie').prop('checked')){
        editType = 'select';
    }
    var whether = [{value: true, text: "是"}, {value: false, text: "否"}]
    $('#chartSeriesTable').bootstrapTable('destroy');
    $('#chartSeriesTable').bootstrapTable({
        data: data,
        striped: true, //是否显示行间隔色
        pagination: false, //是否分页
        editable: true,//可行内编辑
        toolbar: '#chartSeriesToolbar',
        toolbarAlign: 'right',
        onEditableSave: function (field, row, oldValue, $el) {
            if(field=='axis'){
                var data = $('#chartAxisTable').bootstrapTable('getData');
                for (j = 0, len = data.length; j < len; j++) {
                    if(row[field]==data[j].title){
                        var index =$el.parent().parent().data('index');
                        if(data[j].type!='numeric'){
                            row.yFields=row.yFields+'';
                            row.percent=false;
                            $('#chartSeriesTable').bootstrapTable('updateRow', {index: index, row: row});
                            $("#chartSeriesTable tr[data-index='"+index+"'] a[data-name='percent']").editable('toggleDisabled',false);
                        }else{
                            $("#chartSeriesTable tr[data-index='"+index+"'] a[data-name='percent']").editable('toggleDisabled',true);
                        }
                    }
                }
            }
        },
        columns: [
            {
                radio: true  //第一列显示单选框
            },
            {
                filed: 'id',
                visible: false
            },
            {
                title: '名称',
                field: 'name',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'text',
                    title: '请输入名称',
                    emptytext: '&#12288',
                    validate: function (v) {
                        if ($.trim(v) == '') {
                            return '名称不能为空';
                        }
                    }

                }
            },
            {
                title: '类型',
                field: 'type',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '请选择类型',
                    emptytext: '&#12288',
                    //柱状=bar 折线=line  散点=scatter 饼图=pie  区域=area
                    source: [{value: "bar", text: "柱状"},
                             {value: "line", text: "折线"},
                             {value: "scatter", text: "散点"},
                             {value: "pie", text: "饼图"},
                             {value: "area", text: "区域"}],
                    toggleDisabled:function(isEnable){
                        if (isEnable){
                            this.enable();// 可编辑
                        } else {
                            this.disabled();// 不可编辑
                        }
                    }
                }
            },
            {
                title: 'X轴数据',
                field: 'xField',
                align: "center",
                width: 150,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '请选择X轴数据',
                    emptytext: '&#12288',
                    source: function(){
                        var result = [];

                        var data = $('#chartAxisTable').bootstrapTable('getData');
                        for (j = 0, len = data.length; j < len; j++) {
                            if(data[j].position == 'bottom'){
                                var fields = data[j].fields + '';
                                var arr = fields.split(',');
                                for ( i= 0, len = arr.length; i < len; i++) {
                                    result.push({value: arr[i], text: arr[i]});
                                }
                            }
                        }
                        return result;
                    }
                }
            },
            {
                title: 'Y轴',
                field: 'axis',
                align: "center",
                width: 150,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '请选择Y轴',
                    emptytext: '&#12288',
                    source: function(){
                        var result = [];
                        var data = $('#chartAxisTable').bootstrapTable('getData');
                        for (j = 0, len = data.length; j < len; j++) {
                            if(data[j].position == 'left' || data[j].position == 'right'){
                                if(data[j].position == 'left' || data[j].position == 'right'){
                                    result.push({value: data[j].title, text: data[j].title});
                                }
                            }
                        }
                        return result;
                    }
                }
            },
            {
                title: 'Y轴数据',
                field: 'yFields',
                align: "center",
                width: 150,
                valign: 'middle',
                editable: {
                    type: editType,              //编辑框的类型。支持text|textarea|select|date|checklist等
                    title: '请选择Y轴数据',
                    separator:',',
                    emptytext: '&#12288',
                    source: function(){
                        var result = [];
                        //获取Y轴
                        var axis=$(this).parents("tr").children("td").eq(4).text();
                        var data = $('#chartAxisTable').bootstrapTable('getData');
                        for (j = 0, len = data.length; j < len; j++) {
                            if(data[j].title == axis){
                                var fields = data[j].fields + '';
                                var arr = fields.split(',');
                                for ( i= 0, l = arr.length; i < l; i++) {
                                    result.push({value: arr[i], text: arr[i]});
                                }
                            }
                        }
                        return result;
                    }
                }

            },
            {
                title: '百分比',
                field: 'percent',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '百分比',
                    source: whether,
                    toggleDisabled:function(isEnable){
                        if (isEnable){
                            this.enable();// 可编辑
                        } else {
                            this.disabled();// 不可编辑
                        }
                    }
                }
            },
            {
                title: '堆叠',
                field: 'stacked',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '堆叠',
                    source: whether
                }
            },
            {
                title: '填充',
                field: 'fill',
                align: "center",
                width: 100,
                valign: 'middle',
                editable: {
                    type: 'select',
                    title: '填充',
                    source: whether
                }
            }
        ],
        locale: 'zh-CN'//中文支持
    });

    var series = $('#chartSeriesTable').bootstrapTable('getData');
    for (j = 0, len = series.length; j < len; j++) {
        var axes = $('#chartAxisTable').bootstrapTable('getData');
        for (i = 0, le = axes.length; i < le; i++) {
            if(series[j].axis==axes[i].title && axes[i].type!='numeric'){
                $("#chartSeriesTable tr[data-index='"+j+"'] a[data-name='percent']").editable('toggleDisabled',false);
            }
        }
    }
}

function isRepeat(arr){
    var  hash = {};
    for(var i in arr) {
        if(hash[arr[i]]) {
            return true;
        }
        hash[arr[i]] = true;
    }
    return false;
}
