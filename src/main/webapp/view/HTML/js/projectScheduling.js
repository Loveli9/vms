var proNo;
var scheduleType;
var scheduleTypeVersions;
var scheduleTypeIteration;
var scheduleIcon;
var bsTables = [];
$(function () {
    proNo = window.parent.projNo;
    scheduleType = getSelectValueByType('schedule_type');
    scheduleTypeVersions = getSelectValueByType('schedule_type_versions');
    scheduleTypeIteration = getSelectValueByType('schedule_type_iteration');
    scheduleIcon = getSelectValueByType('schedule_icon');
});

function lcbInitData() {
    $.ajax({
        type: "post",
        url: getRootPath() + '/projectPlan/queryProjectSchedulePlan',
        async: false,
        data: {
            no: proNo,
        },
        success: function (data) {
            if (data.code === '0') {
                _.each(data.data, function (lists, index) {
                    // if(lists.planType==1){
                    //     $("#projectSchedulePlan").fishBone(lists.id,lists.list);
                    //     $("#addMilestone").attr("projectScheduleId",lists.id);
                    // }else{
                    var tab = '<div class="container" style="width: 97%;padding: 10px;margin-bottom: 20px;">';
                    if (lists.planType === 2) {
                        tab = tab + '<div style="float: left;font-size: 16px;font-weight: 700;">迭代-' + lists.planName + '</div>' +
                            '<a id="delProjectSchedulePlan' + lists.id + '" onclick="delProjectSchedulePlan(' + lists.id + ')" style="z-index: 100;font-size: 14px;margin-left: 10px;color: #f00;">删除计划</a>';
                    } else if (lists.planType === 3) {
                        tab = tab + '<div style="float: left;font-size: 16px;font-weight: 700;">版本-' + lists.planName + '</div>' +
                            '<a id="delProjectSchedulePlan' + lists.id + '" onclick="delProjectSchedulePlan(' + lists.id + ')" style="z-index: 100;font-size: 14px;margin-left: 10px;color: #f00;">删除计划</a>';
                    }

                    tab = tab + '<div style="float: right">' +
                        '<button id="addMilestone' + lists.id + '" onclick="addMilestoneFun(' + lists.id + ')" type="button" class="btn search-btn">新建</button>' +
                        '<span id="milestoneImageShow' + lists.id + '" onclick="milestoneImageShowFun(' + lists.id + ')" class="glyphicon glyphicon-th-large" style="color: #491bff;font-size: 20px;margin-right: 5px;" aria-hidden="true"></span>' +
                        '<span id="milestoneTableShow' + lists.id + '" onclick="milestoneTableShowFun(' + lists.id + ')" class="glyphicon glyphicon-th-list" style="font-size: 20px;margin-right: 5px;" aria-hidden="true"></span>' +
                        '</div>' +
                        '<div id="milestoneImage' + lists.id + '" style="margin-top: 70px">' +
                        '<div id="projectSchedulePlan' + lists.id + '"></div>' +
                        '</div>' +
                        // '<div id="milestoneTable'+lists.id+'" class="myTab">' +
                        '<div id="milestoneTable' + lists.id + '" class="myTab" style="margin-top: 70px;display: none">' +
                        '<div>' +
                        '<table id="milestoneScheduleTable' + lists.id + '" class="table text-nowrap"></table>' +
                        '</div>' +
                        '</div>' +
                        '</div>' /*+
                        '<div style="border-bottom: 5px #ebf3ff solid;"></div>'*/;
                    $("#containers").append(tab);
                    $("#projectSchedulePlan" + lists.id).fishBone(lists.id, lists.list);
                    $("#addMilestone" + lists.id).attr("planType", lists.planType);
                    $("#addMilestone" + lists.id).attr("projectScheduleId", lists.id);
                    $("#milestoneImageShow" + lists.id).attr("projectScheduleId", lists.id);
                    $("#milestoneTableShow" + lists.id).attr("projectScheduleId", lists.id);
                    // }
                    initTableSave(lists.planType, lists.id);
                })
            }
        }
    });
}

function refreshData(id) {
    $.ajax({
        type: "post",
        url: getRootPath() + '/projectPlan/queryProjectSchedulePlan',
        async: false,
        data: {
            no: proNo,
        },
        success: function (data) {
            if (data.code === '0') {
                _.each(data.data, function (lists, index) {
                    if (id == lists.id) {
                        $("#projectSchedulePlan" + id).fishBone(lists.id, lists.list);
                        $("#addMilestone" + id).attr("projectScheduleId", lists.id);
                        $("#milestoneImageShow" + lists.id).attr("projectScheduleId", lists.id);
                        $("#milestoneTableShow" + lists.id).attr("projectScheduleId", lists.id);
                    }
                })
            }
        }
    });
}

function initTableSave(planType, id) {
    var columns = lcbInitColumn(planType, id);
    bsTables.push({
        key: id,
        value: $('#milestoneScheduleTable' + id).bootstrapTable({
            method: 'post',
            contentType: "application/x-www-form-urlencoded",
            url: getRootPath() + '/projectPlan/querySchedulePlan',
            editable: false,// 可行内编辑
            striped: false, // 是否显示行间隔色
            pagination: false,// 是否分页
            dataField: 'data',
            queryParams: function (params) {
                var param = {
                    projectScheduleId: $("#addMilestone" + id).attr("projectScheduleId"),
                    proNo : proNo
                }
                return param;
            },
            columns: columns,
            locale: 'zh-CN'//中文支持
        })
    });
}

function lcbInitColumn(planType, id) {
    return [
        {title: 'id ', field: 'id', visible: false},
        {title: 'projectScheduleId', field: 'projectScheduleId', visible: false},
        {title: '名称', field: 'name', width: 25},
        {title: '描述', field: 'description', width: 25},
        {
            title: '类型', field: 'scheduleType', width: 25,
            formatter: function (value, row, index) {
                var scheduleTypes;
                if (planType === 1) {
                    scheduleTypes = scheduleType;
                } else if (planType === 2) {
                    scheduleTypes = scheduleTypeIteration;
                } else if (planType === 3) {
                    scheduleTypes = scheduleTypeVersions;
                }
                for (var i = 0; i < scheduleTypes.length; i++) {
                    if (scheduleTypes[i].value === value) {
                        return scheduleTypes[i].key;
                    }
                }
                return "-";
            },
        },
        {
            title: '图标', field: 'scheduleIcon', width: 25,
            formatter: function (value, row, index) {
                for (var i = 0; i < scheduleIcon.length; i++) {
                    if (scheduleIcon[i].value === value) {
                        return scheduleIcon[i].key;
                    }
                }
                return "-";
            }
        },
        {
            title: '计划完成时间', field: 'plannedFinishDate', width: 25,
            formatter: function (value, row, index) {
                if (value == null) {
                    return "-";
                }
                return new Date(value).format('yyyy-MM-dd');
            }
        },
        {
            title: '实际完成时间', field: 'actualFinishDate', width: 25,
            formatter: function (value, row, index) {
                if (value == null) {
                    return "-";
                }
                return new Date(value).format('yyyy-MM-dd');
            }
        },
        {
            title: '操作', field: 'operation', width: 25,
            formatter: function (value, row, index) {
                return '<a href="#" style="color:blue;margin-right: 5px;" onclick="projectSchedulingEditData(' + index + ',' + id + ',' + planType + ')">修改</a>' +
                    '<a href="#" style="color:blue" onclick="projectSchedulingDelData(' + index + ',' + id + ')">删除</a>';
            }
        },
    ];
}


//编辑
projectSchedulingEditData = function (index, id, planType) {
    var row = $('#milestoneScheduleTable' + id).bootstrapTable('getData')[index];
    $("#editName").val(row.name);
    $("#editDescription").val(row.description);
    if (planType === 1) {
        $("#editSelScheduleType").empty();
        setOption(scheduleType, "#editSelScheduleType", "类型");
    } else if (planType === 2) {
        $("#editSelScheduleType").empty();
        setOption(scheduleTypeIteration, "#editSelScheduleType", "类型");
    } else if (planType === 3) {
        $("#editSelScheduleType").empty();
        setOption(scheduleTypeVersions, "#editSelScheduleType", "类型");
    }
    $("#editSelScheduleType").find("option").attr("selected", false);
    $("#editSelScheduleType").find("option[value='" + row.scheduleType + "']").attr("selected", true);

    $("#editSelScheduleIcon").empty();
    setOption(scheduleIcon, "#editSelScheduleIcon", "类型");
    $("#editSelScheduleIcon").find("option").attr("selected", false);
    $("#editSelScheduleIcon").find("option[value='" + row.scheduleIcon + "']").attr("selected", true);

    $("#editPlannedFinishDate").val(changeDateFormat(row.plannedFinishDate));
    $("#editActualFinishDate").val(changeDateFormat(row.actualFinishDate));
    $('#editProjectScheduleId').val(row.projectScheduleId);
    $('#editId').val(row.id);
    $("#editMilestonePage").modal('show');
};
//删除
projectSchedulingDelData = function (index, id) {
    var id1 = $('#milestoneScheduleTable' + id).bootstrapTable('getData')[index].id;
    Ewin.confirm({title: "节点删除", message: "是否要删除节点？"}).on(function (e) {
        if (!e) {
            return;
        } else {
            $.ajax({
                url: getRootPath() + '/projectPlan/delSchedulePlan',
                type: 'post',
                async: false,
                data: {
                    id: id1,
                },
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == '0') {
                        toastr.success('删除成功！');
                        $('#milestoneScheduleTable' + id).bootstrapTable('refresh', {projectScheduleId: id});
                    }
                    else {
                        toastr.error('删除失败!');
                    }
                }
            });
        }
    });

};
delProjectSchedulePlan = function (id) {
    Ewin.confirm({title: "里程碑计划删除", message: "是否要删除里程碑计划？"}).on(function (e) {
        if (!e) {
            return;
        } else {
            $.ajax({
                url: getRootPath() + '/projectPlan/delProjectSchedulePlan',
                type: 'post',
                async: false,
                data: {
                    id: id,
                },
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == '0') {
                        toastr.success('删除成功！');
                        window.location.reload();
                    }
                    else {
                        toastr.error('删除失败!');
                    }
                }
            });
        }
    });
}
addMilestoneFun = function (id) {
    $('#addProjectScheduleId').val(id);
    var planType = $("#addMilestone" + id).attr("planType");
    if (planType === '1') {
        $("#addSelScheduleType").empty();
        setOption(scheduleType, "#addSelScheduleType", "类型");
    } else if (planType === '2') {
        $("#addSelScheduleType").empty();
        setOption(scheduleTypeIteration, "#addSelScheduleType", "类型");
    } else if (planType === '3') {
        $("#addSelScheduleType").empty();
        setOption(scheduleTypeVersions, "#addSelScheduleType", "类型");
    }

    $("#addMilestonePage").modal('show');
};

$(document).on("click", "#projectScheduling_add_saveBtn", function () {
    $('#projectSchedulingAddForm').bootstrapValidator('validate');
    if ($("#projectSchedulingAddForm").data('bootstrapValidator').isValid()) {
        $("#addScheduleType").val($("#addSelScheduleType option:selected").val());
        $("#addScheduleIcon").val($("#addSelScheduleIcon option:selected").val());
        var id = $('#addProjectScheduleId').val();
        $.ajax({
            url: getRootPath() + '/projectPlan/addSchedulePlan',
            type: 'post',
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            data: JSON.stringify($('#projectSchedulingAddForm').serializeJSON()),
            success: function (data) {
                //后台返回添加成功
                if (data.code == '0') {
                    $("#addMilestonePage").modal('hide');
                    if ($('#milestoneImage' + id).css("display") !== "none") {
                        refreshData(id);
                    }
                    $('#milestoneScheduleTable' + id).bootstrapTable('refresh', {projectScheduleId: id});
                    toastr.success('编辑成功！');
                }
                else {
                    toastr.error('编辑失败!');
                }
                cleanDate();
            }
        });
    }

});
$(document).on("click", "#projectScheduling_add_backBtn", function () {
    $("#addMilestonePage").modal('hide');
    cleanDate();
});

$(document).on("click", "#projectScheduling_edit_saveBtn", function () {
    $('#projectSchedulingEditForm').bootstrapValidator('validate');
    if ($("#projectSchedulingEditForm").data('bootstrapValidator').isValid()) {
        $("#editScheduleType").val($("#editSelScheduleType option:selected").val());
        $("#editScheduleIcon").val($("#editSelScheduleIcon option:selected").val());
        var id = $('#editProjectScheduleId').val();
        $.ajax({
            url: getRootPath() + '/projectPlan/editSchedulePlan',
            type: 'post',
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            data: JSON.stringify($('#projectSchedulingEditForm').serializeJSON()),
            success: function (data) {
                //后台返回添加成功
                if (data.code == '0') {
                    $("#editMilestonePage").modal('hide');
                    $('#milestoneScheduleTable' + id).bootstrapTable('refresh', {projectScheduleId: id});
                    toastr.success('编辑成功！');
                }
                else {
                    toastr.error('编辑失败!');
                }
                cleanDate();
            }
        });
    }
});
$(document).on("click", "#projectScheduling_edit_backBtn", function () {
    $("#editMilestonePage").modal('hide');
});

$(document).on("click", "#newVersionsOrIteration", function () {
    $('#addNo').val(proNo);
    $("#addMilestonePlan").modal('show');
});
$(document).on("click", "#add_PlanSaveBtn", function () {
    $('#addPlanForm').bootstrapValidator('validate');
    if ($("#addPlanForm").data('bootstrapValidator').isValid()) {
        $("#addPlanType").val($("#addSelPlanType option:selected").val());
        $.ajax({
            url: getRootPath() + '/projectPlan/addProjectSchedulePlan',
            type: 'post',
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            data: JSON.stringify($('#addPlanForm').serializeJSON()),
            success: function (data) {
                //后台返回添加成功
                if (data.code == '0') {
                    $("#addMilestonePlan").modal('hide');
                    window.location.reload();
                }
                else {
                    toastr.error('编辑失败!');
                }
                cleanPlanDate();
            }
        });
    }

});

$(document).on("click", "#add_PlanBackBtn", function () {
    $("#addMilestonePlan").modal('hide');
    cleanPlanDate();
});

function cleanPlanDate() {
    var planType = $("#addSelPlanType").find("option");
    planType.attr("selected", false);
    planType.first().attr("selected", true);
    $('#projectSchedulingAddForm').data('bootstrapValidator').resetForm(true);
}

function cleanDate() {
    $("#addDescription").val("").focus();
    var scheduleType = $("#addSelScheduleType").find("option");
    scheduleType.attr("selected", false);
    scheduleType.first().attr("selected", true);
    var scheduleIcon = $("#addSelScheduleIcon").find("option");
    scheduleIcon.attr("selected", false);
    scheduleIcon.first().attr("selected", true);
    $("#addPlannedFinishDate").val();
    $("#addName").val("").focus();
    $('#projectSchedulingAddForm').data('bootstrapValidator').resetForm(true);
}

//获取下拉列表的值
function setOption(s, id, name) {
    $(id).empty();
    // $(id).append('<option label="'+"请选择"+name+'" value="" ></option>');
    for (j = 0; j < s.length; j++) {
        if (j == 0) {
            $(id).append('<option value="' + s[j].value + '" selected>' + s[j].key + '</option>');
        } else {
            $(id).append('<option value="' + s[j].value + '" >' + s[j].key + '</option>');
        }

    }
}

//获取下拉列表的值
function getSelectValueByType(code) {
    var s = "";
    $.ajax({
        url: getRootPath() + '/dict/items?entryCode=' + code,
        type: 'get',
        async: false,
        dataType: "json",
        contentType: 'application/x-www-form-urlencoded', //设置请求头信息
        //data: {
        //    "type": type
        //},
        success: function (data) {
            if (data.code == '200') {
                s = data.data;
            } else {
                toastr.error('查询失败!');
            }
        }
    });
    return s;
}


milestoneImageShowFun = function (id) {
    $('#milestoneImageShow' + id).css("color", "#491bff");
    $('#milestoneTableShow' + id).css("color", "#000000");
    $('#milestoneImage' + id).css("display", "block");
    $('#milestoneTable' + id).css("display", "none");
    refreshData(id);
};

milestoneTableShowFun = function (id) {
    $('#milestoneImageShow' + id).css("color", "#000000");
    $('#milestoneTableShow' + id).css("color", "#491bff");
    $('#milestoneImage' + id).css("display", "none");
    $('#milestoneTable' + id).css("display", "block");
};

//日期转换函数
function changeDateFormat(cellval) {
    if (cellval == null) {
        return "";
    }
    return new Date(cellval).format('yyyy-MM-dd');
}

function lcbInit() {
    $('#addPlanForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            planName: {
                validators: {
                    notEmpty: {
                        message: '名称不能为空'
                    }
                }
            }
        }
    });
    $('#projectSchedulingAddForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '节点名称不能为空'
                    }
                }
            },
            plannedFinishDate: {
                validators: {
                    notEmpty: {
                        message: '计划完成时间不能为空'
                    }
                }
            }
        }
    });
    $('#projectSchedulingEditForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '节点名称不能为空'
                    }
                }
            },
            plannedFinishDate: {
                validators: {
                    notEmpty: {
                        message: '计划完成时间不能为空'
                    }
                }
            }
        }
    });

    $("#addPlannedFinishDate").datetimepicker({
        minView: "month", //选择日期后，不会再跳转去选择时分秒
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        todayBtn: 1,
        todayHighlight: true
    }).on('hide', function (e) {
        $('#projectSchedulingAddForm')
            .bootstrapValidator('updateStatus', $('#addPlannedFinishDate'), 'NOT_VALIDATED')
            .bootstrapValidator('validateField', $('#addPlannedFinishDate'));
    });
    $("#editPlannedFinishDate").datetimepicker({
        minView: "month", //选择日期后，不会再跳转去选择时分秒
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        todayBtn: 1,
        todayHighlight: true
    }).on('hide', function (e) {
        $('#projectSchedulingEditForm')
            .bootstrapValidator('updateStatus', $('#editPlannedFinishDate'), 'NOT_VALIDATED')
            .bootstrapValidator('validateField', $('#editPlannedFinishDate'));
    });
    $("#editActualFinishDate").datetimepicker({
        minView: "month", //选择日期后，不会再跳转去选择时分秒
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        todayBtn: 1,
        todayHighlight: true
    }).on('hide', function (e) {
        $('#projectSchedulingEditForm')
            .bootstrapValidator('updateStatus', $('#editActualFinishDate'), 'NOT_VALIDATED')
            .bootstrapValidator('validateField', $('#editActualFinishDate'));
    });
}

// $(document).ready(function(){
//     setOption(scheduleType,"#addSelScheduleType","类型");
//     setOption(scheduleIcon,"#addSelScheduleIcon","图标");
//     setOption(scheduleType,"#editSelScheduleType","类型");
//     setOption(scheduleIcon,"#editSelScheduleIcon","图标");
//     init();
//     initData();
// })
