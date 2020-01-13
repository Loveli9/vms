$(function () {
    var manage_group = getSelectValueByType("manage_group");
    var kpi_type = getSelectValueByType("kpi_type");
    var data_type = getSelectValueByType("data_type");
    var light_up_rule = getSelectValueByType("light_up_rule");
    //指标管理列表配置
    $('#kpiTable').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + "/report/reportKpiConfig/query",
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        editable: false,//开启编辑模式
        toolbar: '#kpiToolbar',
        toolbarAlign: 'right',
        clickToSelect: true,//是否启用点击选中行
        onDblClickRow: function () {
            editHandler()
        },
        pageSize: 10, //单页记录数
        pageList: [5, 10, 20, 40], //分页步进值
        queryParams: function (params) {
            var param = {
                'pageSize': params.limit,
                'pageNumber': params.pageNumber,
                'kpiName': $('#queryName').val()
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
                title: '分类',
                field: 'kpiType',
                align: "center",
                width: 100,
                valign: 'middle',
                formatter: function (value, row, index) {
                    return formatColumnVal(value, kpi_type);
                }
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
                title: '计算公式',
                field: 'expression',
                align: "center",
                width: 150,
                valign: 'middle'
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
                title: '数据格式',
                field: 'formatPattern',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '格式函数',
                field: 'formatter',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '显示宽度',
                field: 'width',
                align: "center",
                width: 100,
                valign: 'middle'
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

    function initKpiEditForm() {
        var kts = [];
        for (var i = 0; i < kpi_type.length; i++) {
            if (kpi_type[i].key !== "迭代基础指标" && kpi_type[i].key !== "项目基础指标") {
                kts.push(kpi_type[i]);
            }
        }
        setOption(kts, "#kpiType", "分类");
        setOption(manage_group, "#manageGroup", "管理维度");
        setOption(data_type, "#dataType", "数据类型");
        setOption(light_up_rule, "#lightUpRule", "点灯规则");
    }

    function initKpiEditPage(data) {
        if (data.code == '200') {
            if (data.data) {
                $("#id").val(data.data.id);
                $("#kpiName").val(data.data.kpiName);
                $("#dataType").val(data.data.dataType);
                $("#formatPattern").val(data.data.formatPattern);
                $("#width").val(data.data.width);
                $("#formatter").val(data.data.formatter);
                $("#maxValue").val(data.data.maxValue);
                $("#minValue").val(data.data.minValue);
                $("#targetValue").val(data.data.targetValue);
                $("#lightUpRule").val(data.data.lightUpRule);
                $("#manageGroup").val(data.data.manageGroup);
                $("#kpiType").val(data.data.kpiType);
                $("#readOnly").find("option[value=" + data.data.readOnly + "]").attr("selected", true);
                $("#description").val(data.data.description);
                $("#expression").val(data.data.expression);
            }
        } else {
            toastr.error('服务请求失败，请稍后再试！');
        }
    }

    formValidator();

    /*********************************  指标编辑   *******************************/
    //打开指标新增页面
    $('#kpi_add_btn').click(function () {
        document.getElementById("kpiEditForm").reset();
        initKpiEditForm();
        initKpiMetricTable([], data_type);
        $("#kpiEditPage").modal('show');
    });
    //隐藏指标编辑页面
    $('#kpi_back_btn').click(function () {
        $("#kpiEditPage").modal('hide');
    });

    $('#kpiEditPage').on('hidden.bs.modal', function () {
        $("#kpiEditForm").data('bootstrapValidator').destroy();
        $('#kpiEditForm').data('bootstrapValidator', null);
        formValidator();
    });

    $('#formatPattern').on('input', function () {
        if ($(this).val().length > 0) {
            $('#formatter').val('');
            $('#formatter').attr('disabled', 'disabled');
        } else {
            $('#formatter').removeAttr('disabled');
        }
    });

    $('#formatter').on('input', function () {
        if ($(this).val().length > 0) {
            $('#formatPattern').val('');
            $('#formatPattern').attr('disabled', 'disabled');
        } else {
            $('#formatPattern').removeAttr('disabled');
        }
    });

    //打开指标修改页面
    $('#kpi_edit_btn').click(function () {
        editHandler();
    });
    var editHandler = function () {
        var selectRow = $('#kpiTable').bootstrapTable('getSelections');
        if (selectRow.length == 0) {
            toastr.error('请先选择需要修改的数据!');
            return;
        }
        if (selectRow[0].id <= 0) {
            toastr.error('基础指标禁止修改!');
            return;
        }
        initKpiEditForm();
        $.ajax({
            contentType: "application/x-www-form-urlencoded", //设置请求头信息
            url: getRootPath() + '/report/reportKpiConfig/get_full_by_id',
            type: 'post',
            data: {
                id: selectRow[0].id
            },
            success: function (resp) {
                $("#kpiEditPage").modal('show');
                initKpiEditPage(resp);
                initKpiMetricTable(resp.data.metricsItemConfigs, data_type);
            }
        });
    };
    //指标移除
    $('#kpi_delete_btn').click(function () {
        var data = $('#kpiTable').bootstrapTable('getSelections');
        if (data.length == 0) {
            toastr.warning('请先选择需要移除的数据');
            return;
        }
        var kpiConfigIds = [];
        for (j = 0; j < data.length; j++) {
            kpiConfigIds[j] = data[j].id;
        }

        $.ajax({
            url: getRootPath() + '/report/reportKpiConfig/delete',
            type: 'post',
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            data: JSON.stringify(kpiConfigIds),
            success: function (data) {
                if (data.code == '200') {
                    toastr.success(data.message);
                    $('#kpiTable').bootstrapTable('refresh');
                } else {
                    toastr.error(data.message);
                }
            }
        });
    });
    //指标保存
    $('#kpi_save_btn').click(function () {
        var data = $('#kpiMetricTable').bootstrapTable('getData');
        /*if (data.length == 0) {
            toastr.warning('请先添加度量项数据');
            return;
        }*/
        $('#kpiEditForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#kpiEditForm").data('bootstrapValidator').isValid()) {
            var formJSON = $('#kpiEditForm').serializeJSON();
            formJSON['metricsItemConfigs'] = data;
            saveKpiConfig(formJSON);
        }
    });
    //指标模糊查询
    $('#kpi_search_btn').click(function () {
        $('#kpiTable').bootstrapTable('refresh');
    });
    //指标模糊查询重置
    $('#kpi_clear_btn').click(function () {
        $('#queryName').val('');
        $('#kpiTable').bootstrapTable('refresh');
    });
    /*********************************  度量项选择   *******************************/
    //打开度量项选择页面
    $('#kpi_metric_add').click(function () {
        var data = $('#kpiMetricTable').bootstrapTable('getData');
        var ids = [];
        for (j = 0; j < data.length; j++) {
            ids[j] = data[j].id;
        }
        initMetricItemTable(ids.join(","), data_type);
        $("#kpiMetricAddPage").modal('show');
    });
    //移除度量项
    $('#kpi_metric_delete').click(function () {
        var data = $('#kpiMetricTable').bootstrapTable('getSelections');
        if (data.length == 0) {
            toastr.warning('请先选择需要移除的数据');
            return;
        }
        var ids = [];
        for (j = 0; j < data.length; j++) {
            ids[j] = data[j].id;
        }
        $('#kpiMetricTable').bootstrapTable('remove', {field: 'id', values: ids});
    });
    //隐藏度量项选择页面
    $('#metric_back_btn').click(function () {
        $("#kpiMetricAddPage").modal('hide');
    });
    //新增度量项保存
    $('#metric_save_btn').click(function () {
        var selectRow = $('#metricItemTable').bootstrapTable('getSelections');
        if (selectRow.length == 0) {
            toastr.warning('请先选择需要添加的数据');
            return;
        }
        /*var count = $('#kpiMetricTable').bootstrapTable('getData').length;
        for(j = 0; j < selectRow.length; j++) {
            $('#kpiMetricTable').bootstrapTable('insertRow',{index:count,row:selectRow[j]});
            count++;
        }*/
        $('#kpiMetricTable').bootstrapTable('append', selectRow);
        $("#kpiMetricAddPage").modal('hide');
        $("#kpiEditPage").css("overflow", "auto");
    });

})

function formValidator() {
    $('#kpiEditForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            kpiName: {
                validators: {
                    notEmpty: {
                        message: '指标名称不能为空'
                    }
                }
            },
            dataType: {
                validators: {
                    notEmpty: {
                        message: '数据类型不能为空'
                    }
                }
            },
            maxValue: {
                validators: {
                    regexp: { //只需加此键值对，包含正则表达式，和提示
                        regexp: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
                        message: '请输入正整数或小数'
                    },
                }
            },
            minValue: {
                validators: {
                    regexp: { //只需加此键值对，包含正则表达式，和提示
                        regexp: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
                        message: '请输入正整数或小数'
                    },
                }
            },
            targetValue: {
                validators: {
                    regexp: { //只需加此键值对，包含正则表达式，和提示
                        regexp: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
                        message: '请输入正整数或小数'
                    },
                }
            },
            kpiType: {
                validators: {
                    notEmpty: {
                        message: '分类不能为空'
                    }
                }
            }/*,
            expression: {
                validators: {
                    notEmpty: {
                        message: '计算公式不能为空'
                    }
                }
            }
*/
        }
    });
}

function saveKpiConfig(kpiConfigData) {
    $.ajax({
        url: getRootPath() + '/report/reportKpiConfig/save',
        type: 'post',
        dataType: "json",
        contentType: 'application/json;charset=utf-8', //设置请求头信息
        data: JSON.stringify(kpiConfigData),
        success: function (data) {
            //后台返回添加成功
            if (data.code == '200') {
                toastr.success(data.message);
                $('#kpiTable').bootstrapTable('refresh');
                $("#kpiEditPage").modal('hide');
            } else {
                toastr.error(data.message);
            }
        }
    });
}

function initKpiMetricTable(metricsItemConfigs, data_type) {
    $('#kpiMetricTable').bootstrapTable('destroy');
    $('#kpiMetricTable').bootstrapTable({
        data: metricsItemConfigs,
        striped: true, //是否显示行间隔色
        pagination: false, //是否分页
        queryParamsType: 'limit',
        clickToSelect: true,
        sidePagination: 'server',
        dataField: 'data',
        toolbar: '#kpiMetricToolbar',
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
                title: '字段名称',
                field: 'fieldAlias',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '字段',
                field: 'fieldName',
                align: "center",
                width: 100,
                valign: 'middle'
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
            }
        ],
        locale: 'zh-CN'//中文支持
    });
}

function initMetricItemTable(mictricsItemConfigIds, data_type) {
    $('#metricItemTable').bootstrapTable('destroy');
    $('#metricItemTable').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + "/report/reportKpiConfig/queryExcluedMictricsItemsByIds",
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        clickToSelect: true,
        sidePagination: 'server',
        toolbar: '#metricItemToolbar',
        toolbarAlign: 'right',
        pageSize: 10, //单页记录数
        pageList: [5, 10, 20, 40], //分页步进值
        queryParams: function (params) {
            var param = {
                'pageSize': params.limit,
                'pageNumber': params.pageNumber,
                'mictricsItemConfigIds': mictricsItemConfigIds
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
                title: '字段名称',
                field: 'fieldAlias',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '字段',
                field: 'fieldName',
                align: "center",
                width: 100,
                valign: 'middle'
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
            }
        ],
        locale: 'zh-CN'//中文支持
    });
}
