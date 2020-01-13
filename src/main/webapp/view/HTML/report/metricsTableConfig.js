$(function () {
    var hwAccount = getCookie("username");
    $('#dataSheetTable').bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/report/metrics_table_config/query',
        editable: false,//可行内编辑
        sortable: true, //是否启用排序
        sortOrder: "asc",
        striped: false, //是否显示行间隔色
        dataField: "rows",
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true,//是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 10,//单页记录数
        pageList: [5, 10, 20, 30],//分页步进值
        onDblClickRow: function () {
            edit_tableMsg();
        },
        //showRefresh:true,//刷新按钮
        responseHandler: function (res) {
            return {
                "rows": res.data,
                "total": res.totalCount
            }
        },
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        toolbar: '#toolbar',//指定工作栏
        queryParams: function (params) {
            var param = {
                queryStr: $("#topic").val(),
                size: params.limit, //页面大小
                current: params.pageNumber, //页码
                hwAccount: hwAccount,
                sort: params.sort,//排序列名
                sortOrder: params.order //排位命令（desc，asc）
            }
            return param;
        },
        columns: [
            {
                title: '全选',
                field: 'select',
                radio: true,
                width: 30,
                align: 'center',
                valign: 'middle',
            },
            {
                title: '表名',
                halign: 'center',
                align: 'center',
                field: 'tableName',
                sortable: false,
                width: 70,
            },
            {
                title: '别名',
                halign: 'center',
                align: 'center',
                field: 'alias',
                sortable: false,
                width: 70,
            },
            {
                title: '类型',
                halign: 'center',
                align: 'center',
                field: 'type',
                sortable: false,
                width: 70,
            },
            {
                title: '周期',
                halign: 'center',
                align: 'center',
                field: 'period',
                sortable: false,
                width: 70,
            },
            {
                title: '虚拟表',
                halign: 'center',
                align: 'center',
                field: 'virtualTable',
                sortable: false,
                width: 70,
            },
            {
                title: '描述',
                halign: 'center',
                align: 'center',
                field: 'description',
                sortable: false,
                width: 70,
            },
        ],
        locale: 'zh-CN',//中文支持,
    });


});

// 搜索
function searchTableMsg() {
    $('#dataSheetTable').bootstrapTable('refresh', {url: getRootPath() + '/report/metrics_table_config/query'});
};

//删除方法
function deleteTableMsg() {
    var id = $('#dataSheetTable').bootstrapTable('getSelections')[0];
    if (id) {
        $.ajax({
            url: getRootPath() + '/report/metrics_table_config/delete?id=' + id.id,
            type: 'get',
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息 
            success: function (data) {
                if (data.code == 200) {
                    toastr.success('删除成功！');
                    $('#dataSheetTable').bootstrapTable('refresh');
                } else {
                    toastr.error('删除失败!');
                }
            }
        });
    } else {
        toastr.error('请选择要编辑的数据');
    }
};
var tableData = [];

//编辑方法
function edit_tableMsg() {
    var editMsg = $('#dataSheetTable').bootstrapTable('getSelections')[0];
    if (editMsg != undefined) {
        $.ajax({
            url: getRootPath() + '/report/metrics_table_config/get_full_by_id?id=' + editMsg.id,
            type: 'post',
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息 
            success: function (data) {
                if (data.code == 200) {
                    tableData = data.data.metricsItemConfigs;
                    for (let i = 0; i < tableData.length; i++) {
                        tableData[i].frontId = Math.random();
                    }
                    add_tableMsg("editMessage");
                    $("#AddtableName").val(`${data.data.tableName}`);
                    $("#AddAlias").val(`${data.data.alias}`);
                    $("#virtualTable").val(`${data.data.virtualTable}`);
                    $("#addType").val(`${data.data.type}`);
                    $("#Addperiod").val(`${data.data.period}`);
                    $("#AddDescription").val(`${data.data.description}`);
                }
            }
        });
    } else {
        toastr.error('请选择要编辑的数据');
    }
}

// 新增方法
function add_tableMsg(message) {
    var data_type = getSelectValueByType("data_type");
    setOption(data_type, "#dataTypeSelect", "数据类型");
    $('#tableField').bootstrapTable('destroy');
    if (message == "addMessage") {
        $('#dataSheetTable').bootstrapTable('uncheckAll');
        tableData = [];
        $("#AddtableName").val('');
        $("#AddAlias").val('');
        $("#virtualTable").val('');
        $("#addType").val('');
        $("#Addperiod").val('');
        $("#AddDescription").val('');
    }
    $("#iteAddPage").modal('show');
    $('#tableField').bootstrapTable({
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        sortName: "idx",
        striped: false, //是否显示行间隔色
        cache: false,
        onDblClickRow: function () {
            edit_tableField();
        },
        data: tableData,
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        toolbar: '#toolbarBtn',//指定工作栏
        columns: [
            {
                title: '全选',
                radio: true,
                width: 30,
                align: 'center',
                valign: 'middle',
            },
            {
                field: 'idx',
                visible: false,
                sorttable: true
            },
            {
                title: '别名',
                halign: 'center',
                align: 'center',
                field: 'fieldAlias',
                width: 70,
            },
            {
                title: '字段名',
                halign: 'center',
                align: 'center',
                field: 'fieldName',
                width: 70,
            },
            {
                title: '数据类型',
                halign: 'center',
                align: 'center',
                field: 'dataType',
                width: 70,
                formatter: function (value, row, index) {
                    return formatColumnVal(value, data_type);
                }
            },
            {
                title: '只读',
                halign: 'center',
                align: 'center',
                field: 'readOnly',
                width: 70,
                formatter: function (value, row, index) {
                    return (value == true || value == "true") ? "是" : "否";
                }
            },
        ],
        locale: 'zh-CN',//中文支持,
    });
};

//查看指标
function viewIndex() {
    $('#IndicatorConfig').bootstrapTable("destroy");
    var selectLine = $('#tableField').bootstrapTable('getSelections')[0];
    if (selectLine != undefined) {
        $("#IndicatorConfigModal").modal('show');
        $('#IndicatorConfig').bootstrapTable({
            method: 'post',
            contentType: "application/x-www-form-urlencoded",
            url: getRootPath() + '/report/reportKpiConfig/getKpiConfListByMictricsItemConfigId',
            editable: false,//可行内编辑
            sortable: true, //是否启用排序
            sortOrder: "asc",
            striped: false, //是否显示行间隔色
            dataField: "rows",
            pageNumber: 1, //初始化加载第一页，默认第一页
            pagination: false,//是否分页
            queryParamsType: 'limit',
            sidePagination: 'server',
            pageSize: 10,//单页记录数
            pageList: [5, 10, 20, 30],//分页步进值
            //showRefresh:true,//刷新按钮
            responseHandler: function (res) {
                return {
                    "rows": res.data,
                }
            },
            // showColumns: true,
            minimumCountColumns: 2, //最少允许的列数
            clickToSelect: true,//是否启用点击选中行
            toolbarAlign: 'right',
            buttonsAlign: 'right',//按钮对齐方式
            queryParams: function (params) {
                return {mictricsItemConfigId: selectLine.id};
            },
            columns: [
                {
                    title: 'kpi名称',
                    halign: 'center',
                    align: 'center',
                    field: 'kpiName',
                    sortable: false,
                    width: 70,
                },
                {
                    title: '分组',
                    halign: 'center',
                    align: 'center',
                    field: 'manageGroup',
                    sortable: false,
                    width: 70,
                },
                {
                    title: '描述',
                    halign: 'center',
                    align: 'center',
                    field: 'description',
                    sortable: false,
                    width: 70,
                },
            ],
            locale: 'zh-CN',//中文支持,
        });
    } else {
        toastr.error('请选择要查看的数据');
    }
};

//删除新增方法
function delete_tableField() {
    var deleteObj = $('#tableField').bootstrapTable('getSelections')[0];
    if (deleteObj != undefined) {
        for (let i = 0; i < tableData.length; i++) {
            if (tableData[i].frontId == deleteObj.frontId) {
                tableData.splice(i, 1)
            }
        }
        ;
        $('#tableField').bootstrapTable('load', tableData);
    } else {
        toastr.error('请选择要删除的数据');
    }
};

//编辑新增方法
function edit_tableField() {
    var editObj = $('#tableField').bootstrapTable('getSelections')[0];
    if (editObj != undefined) {
        $("#addPploadModalLabel").html("编辑表字段")
        $("#add_saveBtn").css('display', 'none');
        $("#edit_saveBtn").css('display', 'block');
        $("#addTableField").modal('show');
        $("#addAliasValue").val(`${editObj.fieldAlias}`);
        $("#addFieldNameValue").val(`${editObj.fieldName}`);
        $("#dataTypeSelect").val(`${editObj.dataType}`);
        $("#isReadOnly").val(`${editObj.readOnly}`);
    } else {
        toastr.error('请选择要编辑的数据');
    }
};

//新增新增方法
function add_tableField() {
    $("#add_saveBtn").css('display', 'block');
    $("#edit_saveBtn").css('display', 'none');
    $("#addAliasValue").val('');
    $("#addFieldNameValue").val('');
    $("#addTableField").modal('show');
};

//关闭按钮
function shutDownBtn() {
    $("#IndicatorConfigModal").modal('hide');
};

//取消按钮
function cancelBtn() {
    $("#addFieldNameValue").val("");
    $("#addAliasValue").val("");
    $("#addTableField").modal('hide');
}

//保存编辑按钮
function saveEditBtn() {
    var editObj = $('#tableField').bootstrapTable('getSelections')[0].frontId;
    var addObj = $('#tableField').bootstrapTable('getSelections')[0];
    addObj.fieldAlias = $("#addAliasValue").val();
    addObj.fieldName = $("#addFieldNameValue").val();
    addObj.dataType = $("#dataTypeSelect").val();
    addObj.readOnly = $("#isReadOnly").val();
    addObj.select = undefined;
    addObj.frontId = editObj;
    for (let i = 0; i < tableData.length; i++) {
        if (tableData[i].frontId == editObj) {
            tableData.splice(i, 1, addObj)
        }
    }
    ;
    $("#addTableField").modal('hide');
    $('#tableField').bootstrapTable('load', tableData);
}

//保存按钮
function saveBtn() {
    var addObj = {};
    addObj.fieldAlias = $("#addAliasValue").val();
    addObj.fieldName = $("#addFieldNameValue").val();
    addObj.dataType = $("#dataTypeSelect").val();
    addObj.readOnly = $("#isReadOnly").val();
    addObj.frontId = Math.random();
    tableData.push(addObj);
    $('#tableField').bootstrapTable('load', tableData);
    $("#addFieldNameValue").val("");
    $("#addAliasValue").val("");
    $("#addTableField").modal('hide');
}

//取消按钮
function cancelAddBtn() {
    $("#iteAddPage").modal('hide');
}

//保存按钮
function saveAddMsgBtn() {
    let addId = $('#dataSheetTable').bootstrapTable('getSelections')[0];
    var metricsItemConfigs = $("#tableField").bootstrapTable("getData");
    for (j = 0, len = metricsItemConfigs.length; j < len; j++) {
        metricsItemConfigs[j].idx = j + 1;
    }
    let params = {
        "tableName": $("#AddtableName").val(),
        "alias": $("#AddAlias").val(),
        "type": $("#addType").val(),
        "period": $("#Addperiod").val(),
        "description": $("#AddDescription").val(),
        "virtualTable": $("#virtualTable").val(),
        "metricsItemConfigs": metricsItemConfigs
    }
    if (params.metricsItemConfigs != null && params.metricsItemConfigs != undefined) {
        for (var i = 0; i < params.metricsItemConfigs.length; i++) {
            var mic = metricsItemConfigs[i];
            mic.idx = i + 1;
        }
    }
    if (addId) params.id = addId.id;
    params.virtualTable = params.virtualTable == "false" ? false : true;
    $.ajax({
        url: getRootPath() + '/report/metrics_table_config/save',
        type: 'post',
        data: JSON.stringify(params),
        dataType: "json",
        contentType: 'application/json;charset=utf-8', //设置请求头信息 
        success: function (data) {
            if (data.code == 200) {
                toastr.success("操作成功");
                $("#iteAddPage").modal('hide');
                $('#dataSheetTable').bootstrapTable('refresh');
            } else {
                toastr.error(`${data.message}`);
            }
        }
    });
}
