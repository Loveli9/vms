$(function () {
    var proNo = window.parent.projNo;
//    var bsTable;

    var members = getMeberSelect();
    var iteList = loadIteNameSelectData();
    var work_prior = getSelectValueByType("work_prior");
    var work_severity = getSelectValueByType("work_severity");
    var work_probability = getSelectValueByType("work_probability");

    //加载新增页面中下拉列表的值
    function initAddForm() {
        setIteOption('iteIdAdd');
        setMeberOption('personLiableAdd');
        setOption(work_prior, "#priorAdd", "优先级");
        setOption(work_severity, "#severityAdd", "严重级别");
        setOption(work_probability, "#probabilityAdd", "发生概率");
    }

    //加载编辑页面中下拉列表的值
    function initEditForm() {
        setIteOption('iteIdEdit');
        setMeberOption('personLiableEdit');
        setOption(work_prior, "#priorEdit", "优先级");
        setOption(work_severity, "#severityEdit", "严重级别");
        setOption(work_probability, "#probabilityEdit", "发生概率");
    }

    $('#mytable').bootstrapTable({
        method: 'post',
        contentType: 'application/json',
        url: getRootPath() + '/projectReport/getProjectDscPageByNo',
        editable: true,//可行内编辑
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
        //showRefresh:true,//刷新按钮
        showColumns: false,
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        toolbar: '#toolbar',//指定工作栏
        queryParams: function (params) {
            var param = {
                is361str: $("#is361").val(),
                no:getCookie("projNo_comm"),
                limit: params.limit, //页面大小
                offset: params.offset, //页码
                sortOrder: params.order //排位命令（desc，asc）
            }
            return param;
        },
        onEditableSave: function (field, row, oldValue, $el) {
            if (field == "personLiable") {
                row.personLiable = row.personLiable.toString();
            }
            $.ajax({
                type: "post",
                url: getRootPath() + '/projectReport/editProjectDsc',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify(row),
                success: function (data, status) {
                    if (status == "success") {
                        var pageNumber = $('#mytable').bootstrapTable('getOptions').pageNumber;
                        $('#mytable').bootstrapTable('refresh', {pageNumber: pageNumber});
                        toastr.success('修改成功！');
                    } else {
                        toastr.success('修改失败！');
                    }
                }
            });
        },
        columns: [
            {title: '全选', field: 'select', checkbox: true, width: 25, visible: false},
            {
                title: '序号',
                field: 'num',
                align: "center",
                width: 45,
                formatter: function (value, row, index) {
                    var pageSize = $('#mytable').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#mytable').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {title: '问题id ', field: 'id', width: 80, visible: false, switchable: false},

            {title: '项目编号 ', field: 'no', width: 80, visible: false, switchable: false},

            {
                title: '标题',
                field: 'topic',
                visible: false,
                switchable: false,
                width: 200,
                align: 'left',
                editable: {
                    title: '标题',
                    emptytext: '&#12288',
                    placement: 'bottom',
                    validate: function (v) {
                        if (!v) return '标题不能为空';
                    }
                }
            },
            {title: '项目名称 ', field: 'name', align: 'center', width: 190},
            {
                title: '问题描述', field: 'question', width: 190, align: 'left',
                editable: {
                    type: 'textarea',
                    title: '问题描述',
                    emptytext: '&#12288',
//                    mode: mode,
                    placement: 'bottom',
                    validate: function (v) {
                        if (!v) return '问题描述不能为空';
                    }
                }
            },
            {
                title: '解决措施', field: 'imprMeasure', width: 185, align: 'left',
                editable: {
                    type: 'textarea',
                    title: '解决措施',
                    emptytext: '&#12288',
//                    mode: mode,
                    placement: 'bottom',
                }
            },
            {
                title: '当前进展', field: 'progressDesc', width: 80, align: 'left',
                editable: {
                    type: 'textarea',
                    title: '当前进展',
                    emptytext: '&#12288',
                    placement: 'bottom',
//                    mode: mode,
                }
            },

            {
                title: '类型',
                field: 'is361',
                width: 80,
                halign: 'center',
                align: 'center',
                editable: {
                    type: 'select',
                    title: '类型',
                    emptytext: '&#12288',
                    placement: 'bottom',
//                    mode: mode,
                    source: {
                        "1": "361评估问题",
                        "2": "AAR",
                        "3": "开工会审计",
                        "4": "质量回溯"
                    },
                    validate: function (v) {
                        if (!v) return '类型不能为空';
                    }
                }
            },
            {
                title: '严重程度',
                halign: 'center',
                align: 'center',
                field: 'severity',
                /*visible: false,
                switchable: false,*/
                width: 75,
                editable: {
                    type: "select",
                    source: work_severity,
                    title: '严重程度',
                    placement: 'bottom',
                    emptytext: '&#12288',
                }
            },
            {
                title: '优先级',
                halign: 'center',
                align: 'center',
                field: 'prior',
                /*visible: false,
                switchable: false,*/
                width: 60,
                editable: {
                    type: "select",
                    source: work_prior,
                    title: '选择优先级',
                    placement: 'bottom',
                    emptytext: '&#12288',
                }
            },
            {
                title: '责任人',
                field: 'personLiable',
                width: 70, 'class': 'personLiablClass',
                halign: 'center',
                align: 'center',
                editable: {
                    type: "checklist",
                    separator: ",",
                    liveSearch: true,
                    emptytext: '&#12288',
                    source: members,
                    title: '请选择负责人',
                    //multiple : true,//多选
                    placement: 'bottom',
                    noeditFormatter: function (value, row, index) {
                        var names = "";
                        valStr = value;
                        if (!value) {
                            value = '&#12288';
                            result = {
                                filed: "personLiable",
                                value: value,
                                values: value
                            };
                        } else {
                            value = value.split(',');
                            for (var i = 0; i < value.length; i++) {
                                for (var j = 0; j < members.length; j++) {
                                    if (members[j].value == value[i]) {
                                        if (i != value.length - 1) {
                                            names += members[j].text + ",";
                                        } else {
                                            names += members[j].text
                                        }
                                    }
                                }
                            }
                            result = {
                                filed: "personLiable",
                                value: names + "<span style='display:none'>," + valStr + ",</span>",
                                values: names + "<span style='display:none'>," + valStr + ",</span>"
                            };
                        }
                        return result;
                    }
                }
            },
            {
                title: '计划完成时间',
                field: 'finishTime',
                width: 110,
                halign: 'center',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == null) {
                        return "-";
                    }
                    return new Date(value).format('yyyy-MM-dd');
                },
                editable: {
                    type: 'date',
                    title: '计划完成时间',
                    emptytext: '&#12288',
                    placement: 'bottom',
                    format: 'yyyy-mm-dd',
                    viewformat: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    datepicker: {
                        weekStart: 1,
                    },
                    validate: function (v) {
                        if (!v) {
                            return '计划完成时间不能为空!';
                        }
                    }
                }
            },
            {
                title: '实际完成时间',
                field: 'actualFinishTime',
                width: 110,
                halign: 'center',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == null) {
                        return "-";
                    }
                    return new Date(value).format('yyyy-MM-dd');
                },
                editable: {
                    type: 'date',
                    title: '实际完成时间',
                    emptytext: '&#12288',
                    placement: 'bottom',
                    format: 'yyyy-mm-dd',
                    viewformat: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    datepicker: {
                        weekStart: 1,
                    },
                    validate: function (v) {
                        /*if(!v){
                            return '实际完成时间不能为空!';
                        }*/
                    }
                }
            },
            {
                title: '状态',
                field: 'state',
                width: 55,
                halign: 'center',
                align: 'center',
                editable: {
                    type: 'select',
                    title: '状态',
                    emptytext: '&#12288',
                    placement: 'bottom',
//                    mode: mode,
                    source: {
                        "Open": "Open",
                        "Closed": "Closed"
                    },
                    validate: function (v) {
                        if (!v) return '状态不能为空';
                    }
                }
            },
            {
                title: '发生概率',
                halign: 'center',
                align: 'center',
                field: 'probability',
                visible: false,
                switchable: false,
                width: 80,
                editable: {
                    type: "select",
                    source: work_probability,
                    title: '发生概率',
                    placement: 'bottom',
                    emptytext: '&#12288',
                }
            },
            {
                title: '进度',
                halign: 'center',
                align: 'center',
                field: 'speed',
                visible: false,
                switchable: false,
                width: 80,
                editable: {
                    type: 'text',
                    title: '进度',
                    placement: 'bottom',
                    emptytext: '&#12288',
                    validate: function (v) {
                        var reg = /^(0|[0-9][0-9]?|100)$/;//包括0和100
                        if (!reg.test(v)) {
                            return '请输入100以内的整数';
                        }
                    }
                },
                formatter: function (value, row, index) {
                    var tab = value + '%';
                    return tab
                },
            },
            {
                title: '迭代',
                halign: 'center',
                align: 'center',
                field: 'iteId',
                visible: false,
                switchable: false,
                width: 80,
                editable: {
                    type: "select",
                    source: iteList,
                    title: '请选择迭代',
                    placement: 'bottom',
                    emptytext: '&#12288',
                }
            },
            {
                title: '操作',
                halign: 'center',
                align: 'center',
                sortable: false,
                width: '5%',
                formatter: function (value, row, index) {
                    return '<a href="#editProject" data-toggle="modal" title="修改" style="color: blue;">修改' +
                        '</a> ' +
                        ' <a href="#editProject" data-toggle="modal" title="删除" style="color: blue;">删除' +
                        '</a>';
                },
                events: {
                    'click  a[title=删除]': function (e, value, row, index) {
                        var ID = [];
                        ID.push(row.id)
                        Ewin.confirm({message: "确认删除已选择的条目吗？"}).on(function (e) {
                            if (!e) {
                                return;
                            } else {
                                $.ajax({
                                    url: getRootPath() + '/projectReport/deleteProjectDsc',
                                    type: 'post',
                                    dataType: "json",
                                    contentType: 'application/json;charset=utf-8', //设置请求头信息
                                    data: JSON.stringify(ID),
                                    success: function (data) {
                                        if (data.code == 'success') {
                                            toastr.success('删除成功！');
                                            $('#mytable').bootstrapTable('refresh', {url: getRootPath() + '/projectReport/getProjectDscPageByNo'});
                                        } else {
                                            toastr.error('删除失败!');
                                        }
                                    }
                                });
                            }
                        });

                    },
                    'click a[title=修改]': function (e, value, row, index) {
                        $("#severityEdit").empty();
                        $("#priorEdit").empty();
                        $("#personLiableEdit").empty();
                        $("#iteIdEdit").empty();
                        $("#probabilityEdit").empty();
                        $("#oldPage").css("display", "none");
                        $("#problemDescEdit").css("display", "block");
                        $.ajax({
                            url: getRootPath() + '/projectReport/getProjectEdit',
                            type: 'post',
                            data: {
                                id: row.id
                            },
                            success: function (data) {
                                initEditForm();
                                $("#idEdit").val(data.rows.id);
                                $("#versionEdit").val(data.rows.version);
                                $("#proNoEdit").val(data.rows.no);
                                $("#questionEdit").val(data.rows.question);
                                $("#imprMeasureEdit").val(data.rows.imprMeasure);
                                $("#progressDescEdit").val(data.rows.progressDesc);
                                $("#finishTimeEdit").val(new Date(data.rows.finishTime).format('yyyy-MM-dd'));
                                if (data.rows.actualFinishTime) {
                                    $("#actualFinishTimeEdit").val(new Date(data.rows.actualFinishTime).format('yyyy-MM-dd'));
                                } else {
                                    $("#actualFinishTimeEdit").val('');
                                }
                                $("#is361Edit").val(data.rows.is361);
                                $("#stateEdit").val(data.rows.state);

                                $("#topicEdit").val(data.rows.topic);
                                $("#probabilityEdit").val(data.rows.probability);
                                $("#speedEdit").val(data.rows.speed);

                                $("#severityEdit").val(data.rows.severity);
                                $("#priorEdit").val(data.rows.prior);
                                $("#iteIdEdit").val(data.rows.iteId);
                                //处理责任人
                                if (data.rows.personLiable) {
                                    var option = (data.rows.personLiable).split(",");
                                    $("#personLiableEdit").selectpicker('val', option);
                                }
                            }
                        });


                    }
                },
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
            topic: {
                validators: {
                    /*notEmpty: {
                        message: '标题不能为空'
                    }*/
                }
            },
            question: {
                validators: {
                    notEmpty: {
                        message: '问题描述不能为空'
                    }
                }
            },
            finishTime: {
                validators: {
                    notEmpty: {
                        message: '计划完成时间不能为空'
                    }
                }
            },
            actualFinishTime: {
                validators: {}
            },
            is361: {
                validators: {
                    notEmpty: {
                        message: '类型不能为空'
                    }
                }
            },
            state: {
                validators: {
                    notEmpty: {
                        message: '状态不能为空'
                    }
                }
            },
            speed: {
                validators: {
                    regexp: { //只需加此键值对，包含正则表达式，和提示
                        regexp: /^(0|[0-9][0-9]?|100)$/,//包括0和100
                        message: '请输入100以内的整数'
                    },
                }
            },
        }
    });
    $('#editForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            topic: {
                validators: {
                    /*notEmpty: {
                        message: '标题不能为空'
                    }*/
                }
            },
            question: {
                validators: {
                    notEmpty: {
                        message: '问题描述不能为空'
                    }
                }
            },
            finishTime: {
                validators: {
                    notEmpty: {
                        message: '计划完成时间不能为空'
                    }
                }
            },
            is361: {
                validators: {
                    notEmpty: {
                        message: '类型不能为空'
                    }
                }
            },
            state: {
                validators: {
                    notEmpty: {
                        message: '状态不能为空'
                    }
                }
            },
            speed: {
                validators: {
                    regexp: { //只需加此键值对，包含正则表达式，和提示
                        regexp: /^(0|[0-9][0-9]?|100)$/,//包括0和100
                        message: '请输入100以内的整数'
                    },
                }
            },
        }
    });

    //清空按钮事件
    $('#clear_btn').click(function () {
        $("#is361").val('');
        $('#mytable').bootstrapTable('refresh', {url: getRootPath() + '/projectReport/getProjectDscPageByNo'});
    });

    //查询按钮事件
    $('#search_btn').click(function () {
        $('#mytable').bootstrapTable('refresh', {url: getRootPath() + '/projectReport/getProjectDscPageByNo'});
    });

    //打开新增页面
    $('#btn_add').click(function () {
        $("#severityAdd").empty();
        $("#priorAdd").empty();
        $("#personLiableAdd").empty();
        $("#iteIdAdd").empty();
        $("#probabilityAdd").empty();

        $("#proNoAdd").val(proNo);
        initAddForm();

        $("#oldPage").css("display", "none");
        $("#problemDescAdd").css("display", "block");
    });

    //隐藏新增页面
    $('#add_backBtn').click(function () {
        $('#addForm').data('bootstrapValidator').resetForm(true);//在页面隐藏前重置校验
        formReset('addForm');
        $("#oldPage").css("display", "block");
        $("#problemDescAdd").css("display", "none");
    });

    //新增保存
    $('#add_saveBtn').click(function () {
        //点击保存时触发表单验证
        $('#addForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#addForm").data('bootstrapValidator').isValid()) {

            //添加时将#personLiable多选值存入隐藏的input
            var str = "";
            var selectedValues = "";
            $("#personLiableAdd option:selected").each(function (i) {
                str += $(this).val() + ",";
                selectedValues = str.substring(0, str.length - 1);
            });
            $("#personLiableAdd0").val(selectedValues);
            $("#no").val(getCookie("projNo_comm"));
            //迭代名称唯一校验
            $.ajax({
                url: getRootPath() + '/projectReport/addProjectDsc',
                type: 'post',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify($('#addForm').serializeJSON()),
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == 'success') {
                        /*refresh();
                        formReset();*/
                        /*//隐藏修改与删除按钮
                        $('#btn_delete').css('display','none');
                        $('#btn_edit').css('display','none');*/
                        $('#addForm').data('bootstrapValidator').resetForm(true);
                        formReset('addForm');
                        $("#oldPage").css("display", "block");
                        $("#problemDescAdd").css("display", "none");
                        $('#mytable').bootstrapTable('refresh', {url: getRootPath() + '/projectReport/getProjectDscPageByNo'});
                        toastr.success('保存成功！');
                    } else if (data.code == 'repeat') {
                        toastr.warning('当前迭代名称已经存在，请勿重复插入');
                    }
                }
            });
        }
    });

    $('#add_saveBtn1').click(function () {
        //点击保存时触发表单验证
        $('#addForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if ($("#addForm").data('bootstrapValidator').isValid()) {

            //添加时将#personLiable多选值存入隐藏的input
            var str = "";
            var selectedValues = "";
            $("#personLiable option:selected").each(function (i) {
                str += $(this).val() + ",";
                selectedValues = str.substring(0, str.length - 1);
            });
            $("#personLiableAdd").val(selectedValues);

            //迭代名称唯一校验
            $.ajax({
                url: getRootPath() + '/projectReport/addProjectDsc',
                type: 'post',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify($('#addForm').serializeJSON()),
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == 'success') {
                        /*refresh();
                        formReset();*/
                        $('#addForm').data('bootstrapValidator').resetForm(true);
                        formReset('addForm');
                        $('#mytable').bootstrapTable('refresh', {url: getRootPath() + '/projectReport/getProjectDscPageByNo'});
                        toastr.success('保存成功！');
                    } else if (data.code == 'repeat') {
                        toastr.warning('当前迭代名称已经存在，请勿重复插入');
                    }
                }
            });
        }
    });

    //修改保存
    $('#edit_saveBtn').click(function () {
        $('#editForm').bootstrapValidator('validate');
        if ($("#editForm").data('bootstrapValidator').isValid()) {

            //编辑时将#personLiable多选值存入隐藏的input
            var str = "";
            var selectedValues = "";
            $("#personLiableEdit option:selected").each(function (i) {
                str += $(this).val() + ",";
                selectedValues = str.substring(0, str.length - 1);
            });
            $("#personLiableEdit0").val(selectedValues);

            $.ajax({
                url: getRootPath() + '/projectReport/editProjectDsc',
                type: 'post',
                dataType: "json",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify($('#editForm').serializeJSON()),
                success: function (data) {
                    //后台返回添加成功
                    if (data.code == 'success') {
                        /*refresh();*/
                        //隐藏修改与删除按钮
                        /*$('#btn_delete').css('display','none');
                        $('#btn_edit').css('display','none');*/
                        $('#editForm').data('bootstrapValidator').resetForm(true);
                        formReset('editForm');
                        $("#oldPage").css("display", "block");
                        $("#problemDescEdit").css("display", "none");
                        $('#mytable').bootstrapTable('refresh', {url: getRootPath() + '/projectReport/getProjectDscPageByNo'});
                        toastr.success('编辑成功！');
                    } else {
                        toastr.error('编辑失败!');
                    }
                }
            });
        }
    });
    //隐藏编辑页面
    $('#edit_backBtn').click(function () {
        $('#editForm').data('bootstrapValidator').resetForm(true);
        formReset('editForm');
        $("#oldPage").css("display", "block");
        $("#problemDescEdit").css("display", "none");
    });

    $('#btn_refresh').click(function () {
        $('#mytable').bootstrapTable('refresh', {url: getRootPath() + '/projectReport/getProjectDscPageByNo'});
    });

    function formReset(id) {
        $(':input', "#" + id)
            .not(':button, :submit, :reset, :hidden,:radio') // 去除不需要重置的input类型
            .val('')
            .removeAttr('checked')
            .removeAttr('selected');
    }

    /***************迭代名称下拉列表值 ******************/
    //迭代名称下拉列表值
    function loadIteNameSelectData() {
        var s = "";
        $.ajax({
            url: getRootPath() + '/iteration/getIteNameSelect',
            type: 'post',
            dataType: "json",
            async: false,
            data: {
                proNo: proNo
            },
            success: function (data) {
                if (data.code == '200') {
                    s = data.data;
                } else {
                    toastr.error('加载失败!');
                }
            }
        });
        return s;
    }

    //迭代列表加载(解决Firefox中列表不显示问题)
    function setIteOption(id) {
        $("#" + id).append("<option value=''>" + "请选择迭代" + "</option>");
        for (j = 0; j < iteList.length; j++) {
            $("#" + id).append("<option value='" + iteList[j].value + "'>" + iteList[j].text + "</option>");
        }
    }

    //加载项目成员下拉列表
    function getMeberSelect() {
        var s1 = "";
        $.ajax({
            url: getRootPath() + '/IteManage/meberSelect',
            type: 'post',
            dataType: "json",
            async: false,
            data: {
                proNo: proNo
            },
            success: function (data) {
                if (data.code == 'success') {
                    s1 = data.data;
                } else {
                    toastr.error('加载失败!');
                }
            }
        });
        return s1;
    }

    //项目成员列表加载
    function setMeberOption(id) {
        if (id == "personLiableAdd") {
            for (var i = 0; i < members.length; i++) {
                $("#" + id).append("<option value='" + members[i].value + "'>" + members[i].text + "</option>");
            }
            // 缺一不可
            $('.selectpicker').selectpicker('val', '');
            $('.selectpicker').selectpicker('refresh');
        } else {
            $("#" + id).selectpicker("val", []);
            $("#" + id).empty();
            $("#" + id).prev('div.dropdown-menu').find('ul').empty();
            for (var i = 0; i < members.length; i++) {
                $("#" + id).append("<option value='" + members[i].value + "'>" + members[i].text + "</option>");
            }
            $("#" + id).selectpicker('refresh');
            $("#" + id).selectpicker('render');
        }
    }

    //获取下拉列表的值
    function getSelectValueByType(code) {
        var s = [];
        $.ajax({
            url: getRootPath() + '/dict/items?entryCode=' + code,
            type: 'get',
            async: false,
            dataType: "json",
            contentType: 'application/x-www-form-urlencoded', //设置请求头信息
            //data:{
            //	"type":type
            //},
            success: function (data) {
                if (data.code == '200') {
                    for (var i = 0; i < data.data.length; i++) {
                        s.push({value: data.data[i].value, text: data.data[i].key});
                    }
                } else {
                    toastr.error('查询失败!');
                }
            }
        });
        return s;
    }

    //获取下拉列表的值
    function setOption(s, id, name) {
        $(id).append("<option value=''>" + "请选择" + name + "</option>");
        for (j = 0; j < s.length; j++) {
            $(id).append("<option value='" + s[j].value + "'>" + s[j].text + "</option>");
        }
    }


    $(document).ready(function () {
//    	initProMember();
//    	$( '#mytable' ).bootstrapTable( { height: 460 } );
        $("#finishTimeAdd").datetimepicker({
            minView: "month", //选择日期后，不会再跳转去选择时分秒
            language: 'zh-CN',
            format: 'yyyy-mm-dd',
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: true
            // }).on("click",function(){
            //     $("#finishTimeAdd").datetimepicker("setEndDate",$("#finishTimeAdd").val());
        }).on('hide', function (e) {
            $('#addForm')
                .bootstrapValidator('updateStatus', $('#finishTimeAdd'), 'NOT_VALIDATED')
                .bootstrapValidator('validateField', $('#finishTimeAdd'));
        });
        $("#finishTimeEdit").datetimepicker({
            minView: "month", //选择日期后，不会再跳转去选择时分秒
            language: 'zh-CN',
            format: 'yyyy-mm-dd',
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: true
            // }).on("click",function(){
            //     $("#finishTimeEdit").datetimepicker("setEndDate",$("#finishTimeEdit").val());
        }).on('hide', function (e) {
            $('#addForm')
                .bootstrapValidator('updateStatus', $('#finishTimeEdit'), 'NOT_VALIDATED')
                .bootstrapValidator('validateField', $('#finishTimeEdit'));
        });
        $("#actualFinishTimeAdd").datetimepicker({
            minView: "month", //选择日期后，不会再跳转去选择时分秒
            language: 'zh-CN',
            format: 'yyyy-mm-dd',
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: true
            // }).on("click",function(){
            //     $("#finishTimeAdd").datetimepicker("setEndDate",$("#finishTimeAdd").val());
        }).on('hide', function (e) {
            $('#addForm')
                .bootstrapValidator('updateStatus', $('#actualFinishTimeAdd'), 'NOT_VALIDATED')
                .bootstrapValidator('validateField', $('#actualFinishTimeAdd'));
        });
        $("#actualFinishTimeEdit").datetimepicker({
            minView: "month", //选择日期后，不会再跳转去选择时分秒
            language: 'zh-CN',
            format: 'yyyy-mm-dd',
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: true
            // }).on("click",function(){
            //     $("#finishTimeEdit").datetimepicker("setEndDate",$("#finishTimeEdit").val());
        }).on('hide', function (e) {
            $('#addForm')
                .bootstrapValidator('updateStatus', $('#actualFinishTimeEdit'), 'NOT_VALIDATED')
                .bootstrapValidator('validateField', $('#actualFinishTimeEdit'));
        });

//        initTableSave();
    })

});

//根据窗口调整表格高度
$(window).resize(function () {
    $(".fixed-table-body").css({"min-height": $(window).height() - 115});
});

$(document).ready(function () {
    $(".fixed-table-body").css({"min-height": $(window).height() - 115});
});
