/**
 * 初始化
 */
$(function () {
    //加载类目
    var act = 0;
    $('#branch').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/dict/entries',
        queryParams: function (params) {
            var param;
            param = {
                pageSize: params.limit,
                pageNumber: params.pageNumber - 1,
                name: $("#search_entry_name").val()
            };
            return param;
        },

        //dataField: "data",
        responseHandler: function (res) {
            return {
                "rows": res.data,
                "total": res.totalCount
            }
        },

        //editable: true,//可行内编辑
        height: tableHeight(),//高度调整
        striped: false, //是否显示行间隔色

        toolbar: '#toolbar_branch',//指定工作栏
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        //showRefresh:true,//刷新按钮

        sidePagination: 'server',
        //设置为undefined可以获取 pageNumber，pageSize，searchText，sortName，sortOrder
        //设置为limit可以获取limit, offset, search, sort, order
        //queryParamsType: 'limit',
        //queryParamsType: 'undefined',
        pagination: true,//是否分页
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 10,//单页记录数
        pageList: [10, 20, 30],//分页步进值
        paginationPreText: '&lt上一页',//指定分页条中上一页按钮的图标或文字
        paginationNextText: '下一页&gt',//指定分页条中下一页按钮的图标或文字

        clickToSelect: true,//是否启用点击选中行
        singleSelect: true,//设置True 将禁止多选
        //自定义分页字符串显示为中文
        formatShowingRows:function(pageFrom, pageTo, totalRows) {
            return "总共 "+totalRows+" 条记录";
        },
        //自定义分页字符串显示为中文
        formatRecordsPerPage:function(pageNumber) {
            return '每页显示 '+pageNumber+' 条记录';
        },

        onClickRow: function (row, tr, field) {
            act = 1;
            $("#search_item_name").val('');
            loadEntryItems(row.id);
        },

        onCheck: function (row, tr) {
            if (act === 0) {
                $("#search_item_name").val('');
                loadEntryItems(row.id);
            } else {
                act = 0;
            }
        },
        columns: [
            {
                //title: '全选',
                //field: 'select',
                //checkbox: true,
                radio: true,
                width: 30,
                align: 'center',
                valign: 'middle'
            },
            {
                title: 'ID',
                align: 'center',
                field: 'id',
                width: 60
            },
            {
                title: '类目名称',
                align: 'center',
                field: 'name'
            },
            {
                title: '编码',
                align: 'center',
                field: 'code'
                //width: 60
            }
        ],
        locale: 'zh-CN'//中文支持,
    });

    $('#entryDetailForm').bootstrapValidator({
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
                    },
                    stringLength: {
                        min: 2,
                        max: 15,
                        message: '请填写长度为2-12的字符'
                    },
                }
            },
            code: {
                validators: {
                    notEmpty: {
                        message: '编码不能为空'
                    }
                }
            }
        }
    });

    /*******字典类目********/

    $("#search_entry_name").keypress(function (e) {
        if (e.which == 13) {
            loadEntries();
        }
    });

    $("#btn_search_entry").click(function () {
        loadEntries();
    });

    /*$("#btn_reset_entry").click(function () {
        $("#search_entry_name").val('');
        loadEntries();
    });*/

    $('#btn_add_entry').click(function () {
        resetForm("entryDetailForm");
        $("#entryHeader").html("新建类目");

        $("#entryDetail").modal('show');
    });

    $('#btn_edit_entry').click(function () {
        var selectRow = $('#branch').bootstrapTable('getSelections');
        if (selectRow.length != 1) {
            toastr.warning('请选择一条数据进行编辑');
            return false;
        }

        resetForm("entryDetailForm");
        $("#entryHeader").html("编辑类目");
        //$("#id").val(selectRow[0].id);

        $("#entryDetail").modal('show');

        $.ajax({
            url: getRootPath() + '/dict/entries/' + selectRow[0].id,
            type: 'get',
            success: function (data) {
                var entry = data.data;
                if (entry) {
                    $("#id").val(entry.id);
                    $("#name").val(entry.name);
                    $("#code").val(entry.code);
                    $("#description").val(entry.description);
                } else {
                    toastr.error('服务请求失败，请稍后再试！');
                }
            }
        });
    });

    $('#btnEntryAction').click(function () {
        //点击保存时触发表单验证
        $('#entryDetailForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#entryDetailForm").data('bootstrapValidator').isValid()) {
            var index = $("#id").val();
            var action = (index != null && index != "") ? (index + "/update") : "create";
            $.ajax({
                url: getRootPath() + '/dict/entries/' + action,
                type: 'post',
                dataType: "json",
                contentType: 'application/json', //设置请求头信息
                data: JSON.stringify($('#entryDetailForm').serializeJSON()),
                success: function (data) {
                    //后台操作成功
                    if (data.code == '200') {
                        hideForm("entryDetail", "entryDetailForm");

                        loadEntries();
                        // $("#btn_reset_entry").click();

                        toastr.success('保存成功！');
                    }
                    else {
                        toastr.error('保存失败！' + data.message);
                    }
                }
            });
        }
    });

    $('#btnEntryReturn').click(function () {
        hideForm("entryDetail", "entryDetailForm")
    });

    $("#btn_delete_entry").click(function () {
        var selectRow = $('#branch').bootstrapTable('getSelections');
        var l = selectRow.length;
        if (l <= 0) {
            toastr.warning('请选择要删除的类目。');
            return false;
        }

        Ewin.confirm({message: "确认删除已选中的类目吗？"}).on(function (e) {
            if (!e) {
                return;
            }
            var ids = [];
            for (var i = 0; i < l; i++) {
                ids.push(Number(selectRow[i].id));
            }
            $.ajax({
                url: getRootPath() + '/dict/entries/delete',
                type: 'post',
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(ids),
                success: function (data) {
                    if (data.code == '200') {
                        toastr.success('删除成功！');
                        loadEntries();
                    } else {
                        toastr.error('删除失败！' + data.message);
                    }
                }
            });
        });
    });

    //加载条目
    $('#detail').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: '',
        queryParams: function (params) {
            var param;
            param = {
                entryId: getEntryId(),
                key: $("#search_item_name").val()
            };
            return param;
        },
        //dataField: "data",
        responseHandler: function (res) {
            return {
                "rows": res.data,
                "total": res.totalCount
            }
        },
        onLoadError: function () {
            $('#detail').bootstrapTable('removeAll');
        },
        //editable: true,//可行内编辑
        height: tableHeight(),//高度调整
        striped: false, //是否显示行间隔色
        toolbar: '#toolbar_item',//指定工作栏
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        //showRefresh:true,//刷新按钮
        sidePagination: 'server',
        pagination: true,//是否分页
        //onlyInfoPagination: true,
        clickToSelect: true,//是否启用点击选中行
        singleSelect: false,//设置True 将禁止多选
        //自定义分页字符串显示为中文
        formatShowingRows:function(pageFrom, pageTo, totalRows) {
            return "总共 "+totalRows+" 条记录";
        },
        //自定义分页字符串显示为中文
        formatRecordsPerPage:function(pageNumber) {
            return '';
        },

        // 当选中行，拖拽时的哪行数据，并且可以获取这行数据的上一行数据和下一行数据
        onReorderRowsDrag: function (table, row) {
            // 取索引号
            dragbeforeidx = Number($(row).attr("data-index"));
        },
        // 拖拽完成后的这条数据，并且可以获取这行数据的上一行数据和下一行数据
        onReorderRowsDrop: function (table, row) {
            // 取索引号
            draglateridx = Number($(row).attr("data-index"));
        },
        // 当拖拽结束后，整个表格的数据
        onReorderRow: function (newData) {
            var searched = $("#search_item_name").val();
            if (searched != null && searched != "") {
                toastr.error('只能在完整条目列表下调整排序，请重置检索条件！');
                return;
            }
            var adjusts = {};
            if (dragbeforeidx != draglateridx) {
                adjusts[newData[draglateridx].id] = draglateridx + 1;
                var j;
                var element;
                for (j = 0; j < draglateridx; j++) {
                    element = newData[j];
                    if (j + 1 != element.order) {
                        adjusts[element.id] = j + 1;
                    }
                }
                for (j = draglateridx + 1; j < newData.length; j++) {
                    element = newData[j];
                    if (j + 1 <= element.order) {
                        break;
                    }
                    adjusts[element.id] = j + 1;
                }
                $.ajax({
                    url: getRootPath() + '/dict/items/' + getEntryId() + '/adjust',
                    type: 'post',
                    dataType: "json",
                    contentType: 'application/json',
                    data: JSON.stringify(adjusts),
                    success: function (data) {
                        if (data.code == '200') {
                            toastr.success('保存成功！');
                            //后台返回刷新
                            loadEntryItems();
                        } else {
                            toastr.success('保存失败！');
                        }
                    }
                });
            }
        },

        columns: [
            {
                //title: '全选',
                //field: 'select',
                checkbox: true,
                width: 30,
                align: 'center',
                valign: 'middle'
            },
            {
                title: '序号',
                align: "center",
                field: 'order',
                width: 60,
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {
                title: '键（key）',
                align: "center",
                field: 'key'
            },
            {
                title: '键值（value）',
                align: "center",
                field: 'value'
            },
            {
                title: '最后更新时间',
                width: 160,
                align: "center",
                field: 'modifyTime'
            }
        ],
        locale: 'zh-CN'//中文支持,
    });

    $('#itemDetailForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            key: {
                validators: {
                    notEmpty: {
                        message: '键名（key）不能为空'
                    }
                }
            },
            value: {
                validators: {
                    notEmpty: {
                        message: '键值（value）不能为空'
                    },
                    stringLength: {
                        //min: 2,
                        max: 15,
                        message: '最大长度不能超过15'
                    }
                }
            }
        }
    });

    /*******字典条目********/

    $("#search_item_name").keypress(function (e) {
        if (e.which == 13) {
            loadEntryItems();
        }
    });

    $('#btn_search_item').click(function () {
        loadEntryItems();
    });

    /*$("#btn_reset_item").click(function () {
        $("#search_item_name").val('');
        loadEntryItems();
    });*/

    //打开新增页面
    $("#btn_add_item").click(function () {
        var selectRow = $('#branch').bootstrapTable('getSelections');
        if (selectRow.length != 1) {
            toastr.warning("请先选择类目!");
            return;
        }
        resetForm("itemDetailForm");
        $("#itemHeader").html("新建条目");
        $("#parentId").val(selectRow[0].id);

        $("#itemDetail").modal('show');
    });

    $("#btn_edit_item").click(function () {
        var selectRow = $('#detail').bootstrapTable('getSelections');
        if (selectRow.length != 1) {
            toastr.warning('请选择一条数据进行编辑');
            return false;
        }
        resetForm("itemDetailForm");
        $("#itemHeader").html("编辑条目");
        $("#itemDetail").modal('show');
        $.ajax({
            url: getRootPath() + '/dict/items/' + selectRow[0].id,
            type: 'get',
            success: function (data) {
                var item = data.data;
                if (item) {
                    $("#itemId").val(item.id);
                    $("#parentId").val(item.entryId);
                    $("#key").val(item.key);
                    $("#value").val(item.value);
                    //$("#sysDictEditOrder").val(rs.order);
                } else {
                    toastr.error('服务请求失败，请稍后再试！');
                }
            }
        });
    });

    $('#btnItemAction').click(function () {
        //点击保存时触发表单验证
        $('#itemDetailForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#itemDetailForm").data('bootstrapValidator').isValid()) {
            var index = $("#itemId").val();
            var action = (index != null && index != "") ? (index + "/update") : "create";
            $.ajax({
                url: getRootPath() + '/dict/items/' + action,
                type: 'post',
                dataType: "json",
                contentType: 'application/json', //设置请求头信息
                data: JSON.stringify($('#itemDetailForm').serializeJSON()),
                success: function (data) {
                    //后台操作成功
                    if (data.code == '200') {
                        hideForm("itemDetail", "itemDetailForm");
                        loadEntryItems();
                        // $("#btn_reset_item").click();
                        toastr.success('保存成功！');
                    }
                    else {
                        toastr.error('保存失败！' + data.message);
                    }
                }
            });
        }
    });

    $('#btnItemReturn').click(function () {
        hideForm("itemDetail", "itemDetailForm")
    });

    $('#btn_delete_item').click(function () {
        var selectRow = $('#detail').bootstrapTable('getSelections');
        var l = selectRow.length;
        if (l <= 0) {
            toastr.warning('请选择要删除的条目。');
            return false;
        }
        Ewin.confirm({message: "确认删除已选中的数据吗？"}).on(function (e) {
            if (!e) {
                return;
            }
            var ids = [];
            for (var i = 0; i < l; i++) {
                ids.push(Number(selectRow[i].id));
            }
            $.ajax({
                url: getRootPath() + '/dict/items/delete',
                type: 'post',
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(ids),
                //traditional: true,
                success: function (data) {
                    if (data.code == '200') {
                        toastr.success('删除成功！');
                        loadEntryItems();
                    } else {
                        toastr.error('删除失败！' + data.message);
                    }
                }
            });
        });
    });
});

function loadEntries() {
    $('#branch').bootstrapTable('refresh', {url: getRootPath() + '/dict/entries'});
    $('#detail').bootstrapTable('removeAll');
    $("#search_item_name").val('');
}

function loadEntryItems(entryId) {
    var id = entryId == null ? getEntryId() : entryId;
    var url = "/dict/entries/" + id + "/items";
    $('#detail').bootstrapTable('refresh', {url: getRootPath() + url});
}

function resetForm(form) {
    $('#' + form)[0].reset();
    $('#' + form).data('bootstrapValidator').resetForm();
}

function hideForm(id, form) {
    $('#' + id).modal('hide');
    resetForm(form);
}

function getEntryId() {
    var selectRow = $('#branch').bootstrapTable('getSelections');
    return selectRow.length == 1 ? selectRow[0].id : null;
}

function tableHeight() {
    return $(window).height() - 80;
}


