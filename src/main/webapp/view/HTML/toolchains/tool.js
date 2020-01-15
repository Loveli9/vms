$(function () {
    $(document).on("click", ".topNav li", function () {
        $(this.parentNode.children).removeClass("active");
        $(this.parentNode.children).each(function () {
            var tab = $(this).attr("tabname");
            $("#" + tab).css('display', 'none');
        });
        $(this).addClass("active");
        var id = $(this).attr("tabname");
        $("#" + id).css('display', 'block');
    });

    //报表配置列表初始化
    $('#toolTable').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + "/toolchains/tool/get_by_name",
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        clickToSelect: true,//是否启用点击选中行
        onDblClickRow: function () {
            edittoolHandler();
        },
        responseHandler: function (res) {
            return {
                "rows": res.data,
                "total": res.totalCount
            }
        },
        toolbar: '#toolToolbar',
        toolbarAlign: 'right',
        pageSize: 10, //单页记录数
        pageList: [5, 10, 20, 40], //分页步进值
        queryParams: function (params) {
            var param = {
                'size': params.limit,
                'current': params.pageNumber,
                'name': $('#queryName').val()
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
                title: '版本',
                field: 'version',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '支持工序',
                field: 'supportProcess',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '商业特性',
                field: 'businessProperty',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '管理特性',
                field: 'manageProperty',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '已提供接口',
                field: 'provideInterface',
                align: "center",
                width: 100,
                valign: 'middle',
                formatter: function (value, row, index) {
                    return (value == true || value == "true") ? "是" : "否";
                }
            },
            {
                title: '项目数量',
                field: 'prjAmount',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '上报方式',
                field: 'reportMethod',
                align: "center",
                width: 100,
                valign: 'middle'
            },
            {
                title: '状态',
                field: 'status',
                align: "center",
                width: 100,
                valign: 'middle'
            }
        ],
        locale: 'zh-CN'//中文支持
    });
    //报表配置模糊查询
    $('#tool_search_btn').click(function () {
        $('#toolTable').bootstrapTable('refresh');
    });

    /*********************************  编辑页面   *******************************/

    function inittoolEditPage(data) {
        if (data.code == '200') {
            if (data.data) {
                $("#id").val(data.data.id);
                $("#name").val(data.data.name);
                $("#version").val(data.data.version);
                $("#supportProcess").val(data.data.supportProcess);
                $("#businessProperty").val(data.data.businessProperty);
                $("#manageProperty").val(data.data.manageProperty);
                if(data.data.provideInterface == true || data.data.provideInterface == "true") {
                    $("#provideInterface").val("true");
                } else {
                    $("#provideInterface").val("false");
                }
                $("#prjAmount").val(data.data.prjAmount);
                $("#reportMethod").val(data.data.reportMethod);
                $("#status").val(data.data.status);
            }
        } else {
            toastr.error('服务请求失败，请稍后再试！');
        }
    }

    //新增
    $('#tool_add_btn').click(function () {
        document.getElementById("toolEditForm").reset();
        $("#toolEditPage").modal('show');
    });

    $('#toolEditPage').on('hidden.bs.modal', function () {
        $("#toolEditForm").data('bootstrapValidator').destroy();
        $('#toolEditForm').data('bootstrapValidator', null);
    });
    //点击返回隐藏添加弹出框
    $('#tool_back_btn').click(function () {
        $("#toolEditPage").modal('hide');
    });
    //打开报表配置修改页面
    $('#tool_edit_btn').click(function () {
        edittoolHandler();
    });
    //打开修改页面
    var edittoolHandler = function () {
        var id = $('#toolTable').bootstrapTable('getSelections')[0];
        if (id) {
            $.ajax({
                url: getRootPath() + '/toolchains/tool/get_by_id?id=' + id.id,
                type: 'get',
                dataType: "json",
                success: function (resp) {
                    inittoolEditPage(resp);
                    $("#toolEditPage").modal('show');
                }
            });
        } else {
            toastr.warning('请先选择一条数据');
            return;
        }
    }

    //删除
    $('#tool_delete_btn').click(function () {
        var id = $('#toolTable').bootstrapTable('getSelections')[0];
        if (id) {
            $.ajax({
                url: getRootPath() + '/toolchains/tool/delete?id=' + id.id,
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        $('#toolTable').bootstrapTable('refresh');
                        toastr.success('删除成功！');
                    } else {
                        toastr.error('删除失败!');
                    }
                }
            });
        } else {
            toastr.error('请选择要删除的数据');
        }
    });

    //报表配置保存
    $('#tool_save_btn').click(function () {
        toolFormValidator();//保存时检查输入的参数
        $('#toolEditForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#toolEditForm").data('bootstrapValidator').isValid()) {
            var formJSON = $('#toolEditForm').bootstrapTable('getData').serializeJSON();
            $.ajax({
                url: getRootPath() + '/toolchains/tool/save',
                type: 'post',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify(formJSON),
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == '200') {
                        toastr.success('保存成功！');
                        $('#toolTable').bootstrapTable('refresh');
                        $("#toolEditPage").modal('hide');
                    } else {
                        toastr.error('保存失败！' + data.message);
                    }
                }
            });
        }
    });

    function toolFormValidator() {
        $('#toolEditForm').bootstrapValidator({
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
                version: {
                    validators: {
                        notEmpty: {
                            message: '版本不能为空'
                        }
                    }
                },
                supportProcess: {
                    validators: {
                        notEmpty: {
                            message: '支持工序不能为空'
                        }
                    }
                },
                prjAmount: {
                    validators: {
                        notEmpty: {
                            message: '项目数量不能为空'
                        }
                    }
                }
            }
        });
    }
})