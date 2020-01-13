$(function () {
    var iteName = $("#iteName").val();
    //生成用户数据
    $("#mytab").bootstrapTable('destroy'); // 销毁数据表格
    $('#mytab').bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/iteration/queryList',
        // height:tableHeight(),//高度调整
        toolbar: '#toolbar', //指定工作栏
        editable: true,//可行内编辑
        sortable: true,                     //是否启用排序
        sortOrder: "asc",
        striped: false, //是否显示行间隔色
        dataField: "rows",
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true,//是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 10,//单页记录数
        pageList: [5, 10, 20, 30],//分页步进值
        paginationPreText: '<上一页',//指定分页条中上一页按钮的图标或文字
        paginationNextText: '下一页>',//指定分页条中下一页按钮的图标或文字
        showColumns: true,
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        queryParams: function (params) {
            var param = {
                username:getCookie("username"),
                proNo:getCookie("projNo_comm"),
                iteName: $("#iteName").val(),
                limit: params.limit, // 页面大小
                offset: params.offset, // 页码
                sort: params.sort,      //排序列名
                sortOrder: params.order, //排位命令（desc，asc）
            }
            return param;
        },
        onEditableSave: function (field, row, oldValue, $el) {
            $.ajax({
                type: "post",
                url: getRootPath() + '/iteration/edit',
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
        },
        columns: [
            {
                title: '全选',
                field: 'select',
                radio: true,
                width: 0,
                align: 'center',
                valign: 'middle',
                visible: false,
            },
            {
                title: '序号',
                align: "center",
                width: 60,
                formatter: function (value, row, index) {
                    var pageSize = $('#mytab').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#mytab').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {
                title: '项目名称',
                halign: 'center',
                align: 'center',
                field: 'projectName',
            },
            {
                title: '迭代名称',
                halign: 'center',
                align: 'center',
                field: 'iteName',
                sortable: true,
                width: 150,
                formatter: function (value, row, index) {
                    if (value) {
                        return '<a href="#" style="color:blue;font-size: 14px;" onclick="editData(\'' + row.id + '\')">' + value + '</a>';
                    }
                    return '';
                }
            },
            {
                title: '计划开始时间',
                halign: 'center',
                align: 'center',
                field: 'planStartDate',
                sortable: true,
                width: 120,
                formatter: function (value, row, index) {

                    return changeDateFormat(value);
                },
                editable: {
                    type: 'date',
                    placement: 'bottom',
                    format: 'yyyy-mm-dd',
                    viewformat: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    datepicker: {
                        weekStart: 1,
                    },
                    validate: function (v) {
                        if (!v) {
                            return '计划开始时间不能为空！';
                        } else {

                        }
                    }
                }
            },
            {
                title: '计划结束时间',
                halign: 'center',
                align: 'center',
                field: 'planEndDate',
                sortable: true,
                width: 120,
                formatter: function (value, row, index) {
                    return changeDateFormat(value);
                },
                editable: {
                    type: 'date',
                    placement: 'bottom',
                    format: 'yyyy-mm-dd',
                    viewformat: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    datepicker: {
                        weekStart: 1,
                    },
                    validate: function (v) {
                        if (!v) {
                            return '计划结束时间不能为空！';
                        } else {

                        }
                    }
                }
            },
            {
                title: '实际开始时间',
                halign: 'center',
                align: 'center',
                field: 'startDate',
                sortable: true,
                width: 120,
                formatter: function (value, row, index) {
                    return changeDateFormat(value);
                },
                editable: {
                    type: 'date',
                    placement: 'bottom',
                    format: 'yyyy-mm-dd',
                    viewformat: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    datepicker: {
                        weekStart: 1,
                    },
                    /*validate: function (v) {
                        if(!v){
                            return '实际开始时间不能为空！';
                        }else{

                        }
                    }*/
                    emptytext: '&#12288'
                }
            },
            {
                title: '实际结束时间',
                halign: 'center',
                align: 'center',
                field: 'endDate',
                sortable: true,
                width: 120,
                formatter: function (value, row, index) {
                    return changeDateFormat(value);
                },
                editable: {
                    type: 'date',
                    placement: 'bottom',
                    format: 'yyyy-mm-dd',
                    viewformat: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    datepicker: {
                        weekStart: 1,
                    },
                    /*validate: function (v) {
                        if(!v){
                            return '实际结束时间不能为空!';
                        }

                    }*/
                    emptytext: '&#12288'
                }
            },
            {
                title: '迭代描述',
                align: 'left',
                halign: 'center',
                field: 'describeInfo',
                width: 300
            },
            {
                field: 'operator',
                title: '操作',
                align: 'center',
                valign: 'middle',
                width: '10%',
                formatter: function (value, row, index) {

                    return '<a href="#editProject" data-toggle="modal" title="修改" style="color: blue;">修改'+
                        '</a> '+
                        ' <a href="#editProject" data-toggle="modal" title="删除" style="color: blue;">删除'+
                        '</a>';
                },
                events: {
                    'click a[title=删除]': function (e, value, row, index) {
                        deletedata(row.id);
                    },
                    'click a[title=修改]': function (e, value, row, index) {
                        $("#iteEditPage").modal('show');
                        $.ajax({
                            url: getRootPath() + '/iteration/editPage',
                            type: 'post',
                            data: {
                                // id: selectRow[0].id
                                id: row.id
                            },
                            success: function (data) {
                                if (data.code == 'success') {
                                    if (data.rows) {
                                        $("#editId").val(data.rows.id);
                                        $("#editproNo").val(data.rows.proNo);
                                        $("#editisDeleted").val(data.rows.isDeleted);
                                        $("#editcode").val(data.rows.code);
                                        $("#editIteName").val(data.rows.iteName);
                                        $("#editplanStartDate").val(changeDateFormat(data.rows.planStartDate));
                                        $("#editplanEndDate").val(changeDateFormat(data.rows.planEndDate));
                                        $("#editstartDate").val(changeDateFormat(data.rows.startDate));
                                        $("#editendDate").val(changeDateFormat(data.rows.endDate));
                                        $("#editdescribeInfo").val(data.rows.describeInfo);
                                    }
                                } else {
                                    toastr.error('服务请求失败，请稍后再试！');
                                }
                            }
                        });
                    }
                }
            }
        ],
        locale: 'zh-CN',//中文支持,
    });

    $('#addForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            proNo: {
                validators: {
                    notEmpty: {
                        message: '项目名称不能为空'
                    }
                }
            },
            iteName: {
                validators: {
                    notEmpty: {
                        message: '迭代名称不能为空'
                    }
                }
            },
            planStartDate: {
                validators: {
                    notEmpty: {
                        message: '计划开始时间不能为空'
                    }
                }
            },
            planEndDate: {
                validators: {
                    notEmpty: {
                        message: '计划结束时间不能为空'
                    }
                }
            },
            startDate: {
                validators: {
                    /*notEmpty: {
                        message: '迭代开始时间不能为空'
                    }*/
                }
            },
            endDate: {
                validators: {
                    /*notEmpty: {
                        message: '迭代结束时间不能为空'
                    }*/
                }
            },
            describe: {
                validators: {
                    notEmpty: {
                        message: '迭代描述不能为空'
                    }
                }
            }
        }
    });
    $('#editForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            iteName: {
                validators: {
                    notEmpty: {
                        message: '迭代名称不能为空'
                    }
                }
            },
            planStartDate: {
                validators: {
                    notEmpty: {
                        message: '计划开始时间不能为空'
                    }
                }
            },
            planEndDate: {
                validators: {
                    notEmpty: {
                        message: '计划结束时间不能为空'
                    }
                }
            },
            startDate: {
                validators: {
                    /*notEmpty: {
                        message: '迭代开始时间不能为空'
                    }*/
                }
            },
            endDate: {
                validators: {
                    /*notEmpty: {
                        message: '迭代结束时间不能为空'
                    }*/
                }
            }
        }
    });


    function operateFormatter(value, row, index) {
        if (value == 2) {
            return '<i class="fa fa-lock" style="color:red"></i>'
        } else if (value == 1) {
            return '<i class="fa fa-unlock" style="color:green"></i>'
        } else {
            return '数据错误'
        }
    };

    //查询按钮事件
    $('#search_btn').click(function () {
        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/iteration/queryList'});
    });

    //清空按钮事件
    $('#clear_btn').click(function () {
        $("#code").val('');
        $("#iteName").val('');
        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/iteration/queryList'});
    });

    /******************************** 刷新 *********************************/
    $('#btn_refresh').click(function () {
        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/iteration/queryList'});
    });

    /*********************************  新增   *******************************/
    //打开新增页面
    $('#btn_add').click(function () {
        // $.ajax({
        //     url: getRootPath() + '/iteration/checkStartTime',
        //     type: 'post',
        //     dataType: "json",
        //     async: false,
        //     data: {
        //         proNo: proNo
        //     },
        //     success: function (data) {
        //         if (data) {
        //             $("#planStartDate").datetimepicker("setStartDate", addDate(data.endDate));
        //             $("#planStartDate").val(addDate(data.endDate));
        //         }
        //     }
        // });
        $("#describeInfo").val('');
        $("#iteAddPage").modal('show');
    });
    //隐藏新增页面
    $('#add_backBtn').click(function () {
        $("#iteAddPage").modal('hide');
    });
    //新增保存
    $('#add_saveBtn').click(function () {
        //点击保存时触发表单验证
        $('#addForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#addForm").data('bootstrapValidator').isValid()) {
            //迭代名称唯一校验
            $("#proNo").val(getCookie("projNo_comm"));
            $.ajax({
                url: getRootPath() + '/iteration/add',
                type: 'post',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify($('#addForm').serializeJSON()),
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == 'success') {
                        $("#iteAddPage").modal('hide');
                        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/iteration/queryList'});
                        $('#addForm').data('bootstrapValidator').resetForm(true);
                        /*//隐藏修改与删除按钮
                        $('#btn_delete').css('display','none');
                        $('#btn_edit').css('display','none');*/
                        toastr.success('保存成功！');
                    } else if (data.code == 'repeat') {
                        toastr.warning('当前迭代名称已经存在，请勿重复插入');
                    }
                }
            });
        }
    });


    /************************************* 编辑 *********************************/
    //点击迭代名称启用编辑
    editData = function (selectId) {
        if (selectId == null || selectId == '') {
            toastr.warning('请选择一条数据进行编辑');
            return false;
        } else {
            $("#iteEditPage").modal('show');
        }
        $.ajax({
            url: getRootPath() + '/iteration/editPage',
            type: 'post',
            data: {
                id: selectId
            },
            success: function (data) {
                if (data.code == 'success') {
                    if (data.rows) {
                        $("#editId").val(data.rows.id);
                        $("#editproNo").val(data.rows.proNo);
                        $("#editisDeleted").val(data.rows.isDeleted);
                        $("#editcode").val(data.rows.code);
                        $("#editIteName").val(data.rows.iteName);
                        $("#editplanStartDate").val(changeDateFormat(data.rows.planStartDate));
                        $("#editplanEndDate").val(changeDateFormat(data.rows.planEndDate));
                        $("#editstartDate").val(changeDateFormat(data.rows.startDate));
                        $("#editendDate").val(changeDateFormat(data.rows.endDate));
                        $("#editdescribeInfo").val(data.rows.describeInfo);
                    }
                } else {
                    toastr.error('服务请求失败，请稍后再试！');
                }
            }
        });
    };


    //打开编辑页面
    $('#btn_edit').click(function () {
        var selectRow = $('#mytab').bootstrapTable('getSelections');
        if (selectRow.length == 1) {
            $("#iteEditPage").modal('show');
        } else {
            toastr.warning('请选择一条数据进行编辑');
            return false;
        }
        $.ajax({
            url: getRootPath() + '/iteration/editPage',
            type: 'post',
            data: {
                id: selectRow[0].id
            },
            success: function (data) {
                if (data.code == 'success') {
                    if (data.rows) {
                        $("#editId").val(data.rows.id);
                        $("#editproNo").val(data.rows.proNo);
                        $("#editisDeleted").val(data.rows.isDeleted);
                        $("#editcode").val(data.rows.code);
                        $("#editIteName").val(data.rows.iteName);
                        $("#editplanStartDate").val(changeDateFormat(data.rows.planStartDate));
                        $("#editplanEndDate").val(changeDateFormat(data.rows.planEndDate));
                        $("#editstartDate").val(changeDateFormat(data.rows.startDate));
                        $("#editendDate").val(changeDateFormat(data.rows.endDate));
                        $("#editdescribeInfo").val(data.rows.describeInfo);
                    }
                } else {
                    toastr.error('服务请求失败，请稍后再试！');
                }
            }
        });


    });

    $('#edit_backBtn').click(function () {
        $("#iteEditPage").modal('hide');
    });

    //修改保存
    $('#edit_saveBtn').click(function () {
        $('#editForm').bootstrapValidator('validate');
        if ($("#editForm").data('bootstrapValidator').isValid()) {
            $.ajax({
                url: getRootPath() + '/iteration/edit',
                type: 'post',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify($('#editForm').serializeJSON()),
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == 'success') {
                        $("#iteEditPage").modal('hide');
                        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/iteration/queryList'});
                        $('#addForm').data('bootstrapValidator').resetForm(true);
                        //隐藏修改与删除按钮
                        /*$('#btn_delete').css('display','none');
                        $('#btn_edit').css('display','none');*/
                        toastr.success('编辑成功！');
                    } else {
                        toastr.error('编辑失败!');
                    }
                }
            });
        }
    });


    function deletedata(id) {
        var ID = [];
        var dataArr = $('#mytab').bootstrapTable('getSelections');
        ID.push(id);
        for (var i = 0; i < dataArr.length; i++) {
            ID.push(dataArr[i].id);
        }
        if (ID.length <= 0) {
            toastr.warning('请选择有效数据');
            return;
        }
        Ewin.confirm({message: "确认删除已选择的迭代信息吗？"}).on(function (e) {
            if (!e) {
                return;
            } else {
                $.ajax({
                    url: getRootPath() + '/iteration/delete',
                    type: 'post',
                    dataType: "json",
                    contentType: 'application/json;charset=utf-8', //设置请求头信息
                    data: JSON.stringify(ID),
                    success: function (data) {
                        if (data.code == 'success') {
                            toastr.success('删除成功！');
                            $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/iteration/queryList'});
                        } else {
                            toastr.error('删除失败!');
                        }
                    }
                });
            }
        });
    }

    /************************************* 删除 **********************************/
    //删除事件按钮
    $('#btn_delete').click(function () {
        var ID = [];
        var dataArr = $('#mytab').bootstrapTable('getSelections');
        for (var i = 0; i < dataArr.length; i++) {
            ID.push(dataArr[i].id);
        }
        if (ID.length <= 0) {
            toastr.warning('请选择有效数据');
            return;
        }
        Ewin.confirm({message: "确认删除已选择的迭代信息吗？"}).on(function (e) {
            if (!e) {
                return;
            } else {
                $.ajax({
                    url: getRootPath() + '/iteration/delete',
                    type: 'post',
                    dataType: "json",
                    contentType: 'application/json;charset=utf-8', //设置请求头信息
                    data: JSON.stringify(ID),
                    success: function (data) {
                        if (data.code == 'success') {
                            toastr.success('删除成功！');
                            $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/iteration/queryList'});
                        } else {
                            toastr.error('删除失败!');
                        }
                    }
                });
            }
        });
    });

});


/*function tableHeight() {
    return $(window).height() - 30;
}*/

//日期转换函数
function changeDateFormat(cellval) {
    var dateVal = cellval + "";
    if (cellval != null) {
        var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();

        var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

        return date.getFullYear() + "-" + month + "-" + currentDate;
    } else {
        return '';
    }
}

//日期前后顺序比较
function compareDate(o1, o2) {
    o1 = new Date(Date.parse(o1));
    o2 = new Date(Date.parse(o2));
    if (o1 > o2) {
        return true;
    }
    return false;
}

function addDate(time) {
    var timestamp = Date.parse(new Date(time));
    timestamp = timestamp / 1000;
    timestamp += 86400;//加一天
    var newTime = new Date(timestamp * 1000).format('yyyy-MM-dd');
    return newTime;
}

//根据窗口调整表格高度
$(window).resize(function () {
    $(".fixed-table-body").css({"min-height": $(window).height() - 115});
});

$(document).ready(function () {
    $(".fixed-table-body").css({"min-height": $(window).height() - 115});
});
