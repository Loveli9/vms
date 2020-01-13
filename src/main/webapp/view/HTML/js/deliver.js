$(function () {
    var hwAccount = getCookie("username");
    var projNo = getQueryString("projNo");
    $('#mytab').bootstrapTable({
        method: 'post',
        showColumns: false,
        pagination: true,
        sortable: false,
        sortOrder: "asc",
        pageNumber: 1,
        pageSize: 10,
        height: 750,
        pageList: [2, 5, 10, 20],
        dataField: "rows",
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true,//是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        minimumCountColumns: 2,
        paginationPreText: '<上一页',//指定分页条中上一页按钮的图标或文字
        paginationNextText: '下一页>',//指定分页条中下一页按钮的图标或文字
        clickToSelect: true,
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        url: getRootPath() + '/deliver/queryList',        // 表格数据来源
        queryParams: function (params) {
            var param = {
                iteName: $("#iteName").val(),
                username: getCookie("username"),
                proNo:getCookie("projNo_comm"),
                projNo:projNo,
                limit: params.limit, // 页面大小
                offset: params.offset, // 页码
                sort: params.sort,      //排序列名
                sortOrder: params.order, //排位命令（desc，asc）
            }
            return param;
        },
        columns: displayColumn(projNo),
        locale: 'zh-CN',//中文支持,
    });

    function displayColumn(id) {
        var column = [
            {
                title: '全选',
                field: 'select',
                radio: true,
                align: 'center',
                visible: false
            },
            {
                field: 'no',
                align: 'center',
                title: '项目编号',
                align: 'center',
                visible: false
            },
            {
                field: 'id',
                align: 'center',
                title: '序号',
                width: 80,
                formatter: function (value, row, index) {
                    var pageSize = $('#mytab').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#mytab').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {
                field: 'projectName',
                align: 'center',
                title: '项目名称',
                width: '15%',
            }, {
                field: 'name',
                align: 'center',
                title: '交付件名称',
                width: '15%',
            }, {
                field: 'shape',
                align: 'center',
                title: '验收形式'
            }, {
                field: 'status',
                align: 'center',
                title: '状态',
                width: '5%',
                formatter: function (value, row, index) {
                    if (row.path == null || row.path == "") {
                        return '<div class="glyphicon glyphicon-exclamation-sign">' +
                            '</div>'
                    } else {
                        return '<div class="glyphicon glyphicon-ok-circle">' +
                            '</div>'
                    }

                }

            }, {
                field: 'path',
                align: 'center',
                title: '存档路径',
                width: '20%',

            },
            {
                title: '最后更新时间',
                align: 'center',
                halign: 'center',
                field: 'endTime',
                formatter: function (value, row, index) {
                    return changeDateFormat(value);
                },
                editable: {
                    type: 'date',
                    placement: 'bottom',
                    title: '最后更新时间',
                    format: 'yyyy-mm-dd',
                    viewformat: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    emptytext: '&#12288',
                    datepicker: {
                        weekStart: 1,
                    },
                }
            }, {
                field: 'submitter',
                align: 'center',
                title: '提交人'
            }, {
                field: 'remark',
                align: 'center',
                title: '备注'
            }, {
                field: 'operator',
                title: '操作',
                align: 'center',
                valign: 'middle',
                width: '10%',
                formatter: function (value, row, index) {
                    return '<a href="#editProject" data-toggle="modal" title="修改" style="color: blue;">修改' +
                        '</a> ' +
                        ' <a href="#editProject" data-toggle="modal" title="删除" style="color: blue;">删除' +
                        '</a>';
                },
                events: {
                    'click  a[title=删除]': function (e, value, row, index) {
                        deletedata(row.id);
                    },
                    'click a[title=修改]': function (e, value, row, index) {
                        $("#edit_deliverPage").modal('show');
                        $.ajax({
                            url: getRootPath() + '/deliver/edit',
                            type: 'post',
                            data: {
                                id: row.id
                            },
                            success: function (data) {
                                if (data.code == 'success') {
                                    if (data.rows) {
                                        $("#editId").val(data.rows.id);
                                        $("#editNo").val(data.rows.no);
                                        $("#editdeliverName").val(data.rows.name);
                                        $("#editdeliverShape").val(data.rows.shape);
                                        $("#editdeliverPath").val(data.rows.path);
                                        $("#editEndDate").val(changeDateFormat(data.rows.endTime));
                                        $("#editdeliverSubmitter").val(data.rows.submitter);
                                        $("#editdescribeInfo").val(data.rows.remark);
                                    }
                                } else {
                                    toastr.error('服务请求失败，请稍后再试！');
                                }
                            }
                        });
                    }
                }
            }
        ];
        if(id != "" && id != null){
            column.splice(3,1);
        }
        return column;
    }

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
        Ewin.confirm({message: "确认删除已选择的支付件信息吗？"}).on(function (e) {
            if (!e) {
                return;
            } else {
                $.ajax({
                    url: getRootPath() + '/deliver/delete',
                    type: 'post',
                    dataType: "json",
                    contentType: 'application/json;charset=utf-8', //设置请求头信息
                    data: JSON.stringify(ID),
                    success: function (data) {
                        if (data.code == 'success') {
                            toastr.success('删除成功！');
                            $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/deliver/queryList'});
                        } else {
                            toastr.error('删除失败!');
                        }
                    }
                });
            }
        });
    }

    $('#add_saveBtn').click(function () {
        //点击保存时触发表单验证
        $('#addForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#addForm").data('bootstrapValidator').isValid()) {
            var proNo = (projNo == null || projNo == "") ? getCookie("projNo_comm") : projNo;
            $("#no").val(proNo);
            $.ajax({
                url: getRootPath() + '/deliver/add',
                type: 'post',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify($('#addForm').serializeJSON()),
                success: function (data) {  // data: JSON.stringify($('#addForm').serializeJSON()),
                    //后台返回添加成功
                    if (data.code == 'success') {
                        $("#deliverAddPage").modal('hide');
                        // demand_list = "all";
                        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/deliver/queryList'});
                        $('#addForm').data('bootstrapValidator').resetForm(true);
                        toastr.success('新增成功！');
                    } else {
                        //提示topic有相同值存在，不允许添加
                        $('#err').css("display", "block");
                        $('#err').html('<font style="color:#a94442;font-size:10px;">支付件名称已存在!</font>');
                        setTimeout(function () {
                            $('#err').css("display", "none");
                        }, '3000');
                        toastr.error('新增失败!');
                    }
                }
            });
        }
    });
    //修改保存按钮

    $('#edit_saveBtn').click(function () {
        $('#editForm').bootstrapValidator('validate');
        if ($("#editForm").data('bootstrapValidator').isValid()) {
            $.ajax({
                url: getRootPath() + '/deliver/update',
                type: 'post',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify($('#editForm').serializeJSON()),
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == 'success') {
                        $("#edit_deliverPage").modal('hide');
                        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/deliver/queryList'});
                        $('#addForm').data('bootstrapValidator').resetForm(true);
                        toastr.success('编辑成功！');
                    } else {
                        toastr.error('编辑失败!');
                    }
                }
            });
        }
    });

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

    $("#editEndDate").datetimepicker({//实际开始日期
        format: 'yyyy-mm-dd',
        minView: 'month',
        language: 'zh-CN',
        autoclose: true,
        endDate: $("#endDate").val()
    }).on("click", function () {
        $("#endDate").datetimepicker("endDate", $("#endDate").val());
    })

    $('#addForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '交付件名称不能为空'
                    }
                }
            },
            shape: {
                validators: {
                    notEmpty: {
                        message: '验收形式不能为空'
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
            name: {
                validators: {
                    notEmpty: {
                        message: '交付件名称不能为空'
                    }
                }
            },
            shape: {
                validators: {
                    notEmpty: {
                        message: '验收形式不能为空'
                    }
                }
            }
        }
    });
    //查询按钮事件
    $('#search_btn').click(function () {
        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/deliver/queryList'});
    });

    //清空按钮事件
    $('#clear_btn').click(function () {
        $("#iteName").val('');
        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/deliver/queryList'});
    });
    //刷新按钮事件
    $('#btn_refresh').click(function () {
        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/deliver/queryList'});
    });
})
