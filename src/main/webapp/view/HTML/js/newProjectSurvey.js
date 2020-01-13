var work_severity;
var work_prior;
var members;
var work_probability;
var proNo;
var userId = getCookie('username')
var risk_type= {
            "1":"361评估问题",
            "2":"AAR",
            "3":"开工会审计",
            "4":"质量回溯"
        };
$(function(){
    initDateSection();
    var url = getRootPath() + '/projectOverview/getProjectAccess';
    work_severity = getSelectValueByType("work_severity");
    work_prior = getSelectValueByType("work_prior");

    work_probability = getSelectValueByType("work_probability");
    var myParam = {

    };

    //加载关注人员看板
    loadConcernedPerson();

    $('#mytable').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: url,
        queryParams: function(params){
            var param = {
                'area': '',
                'coopType': 0,//0:全部项目;1：已收藏项目;2:未收藏项目
                limit : params.limit, //页面大小
                offset : params.offset, //页码
            }
            return param;
        },
        responseHandler: function (res) {
            if(res.total > 0){
                proNo = res.rows[0].no;
                initReview();
                members = getMeberSelect(proNo);
            }
            loadProjectRisk();
            return {
                "rows": res.rows,
                "total": res.total
            }

        },
        rowStyle:rowStyle,
        height: $(window).height() - 255,//高度调整
        striped: true, //是否显示行间隔色
        // class:'tableCenter',
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        sidePagination: 'server',
        pagination: true,//是否分页
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 4,//单页记录数
        pageList: [5, 10, 15],//分页步进值
        clickToSelect: true,//是否启用点击选中行
        singleSelect: true,//设置True 将禁止多选
        onClickRow: function (row, tr, field) {
            proNo = row.no;
            initReview();
            loadProjectRisk();
            members = getMeberSelect(proNo);
            showDivModal();
        },
        columns:initColumn(),
        locale: 'zh-CN',//中文支持

    });
    loadMyMessage();
});

function showDivModal(){
    $("#projectAssess").css("display","block");//显示
    $("#asessSquer").css("display","block");//显示
    $("#problemRisk").css("display","block");//显示
}

function initColumn(){
    return [
        [{
            "title": "<div class='div-shu'></div><span style='	padding-left: inherit;'>关注项目</span>",
            "halign":"left",
            "align":"left",
            "colspan": 9,
            'class': 'tabHead-style'
        }],
        [{
            title : '序号',
            align: "center",
            width: 30,
            formatter: function (value, row, index) {
                var pageSize=$('#mytable').bootstrapTable('getOptions').pageSize;
                var pageNumber=$('#mytable').bootstrapTable('getOptions').pageNumber;
                return pageSize * (pageNumber - 1) + index + 1;
            }
        },
        {title:'项目名称',field:'name',width:400,
            formatter:function(val, row){
                return '<p title="'+val+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+val+'</p>';
            }
        },
        {title:'PM',field:'pm',width:100,
            formatter:function(val, row){
                return '<p title="'+val+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+val+'</p>';
            }
        },
        {title:'类型',field:'projectType',align: 'left'},
        {title:'质量',field:'qualityLamp',align: 'left',valign:'middle',
            formatter:function(value,row,index){
                var deng = getDeng(getLamp('quality',value).lamp);
                return '<div style="width: 100%;" onclick="measureCommentModalShow(\''+row.no+'\')">'+'<span>'+deng+'</span>'+'</div>';
            }
        },
        {title:'资源',field:'keyRoles',align: 'left',valign:'middle',
            formatter:function(value,row,index){
                var deng = getDeng(getLamp('resources',value).lamp);
                return '<div style="width: 100%;">'+'<span>'+deng+'</span>'+'</div>';
            }
        },
        {title:'进度',field:'planLamp',align: 'left',valign:'middle',
            formatter:function(value,row,index){
                var deng = getDeng(getLamp('progress',value).lamp);
                return '<div style="width: 100%;">'+'<span>'+deng+'</span>'+'</div>';
            }
        },
        {title:'变更',field:'scopeLamp',width:41,align: 'left',valign:'middle',
            formatter:function(value,row,index){
                var deng = getDeng(getLamp('changes',value).lamp);
                return '<div style="width: 100%;">'+'<span>'+deng+'</span>'+'</div>';
            }
        },
        // {title:'统计周期',field:'statisticalCycle',align: 'center',width:58,
        //     formatter:function(value,row,index){
        //         return getStatisticalCycle(value);
        //     }
        // },
        {title:'近五次状态评估',field:'fiveStatusAssessment',width:90,align: 'center',valign:'middle',
            formatter:function(value,row,index){
                var proNo = row.no;
                evaluationState = getEvaluationState(value);
                evaluationState = '<div id="assess" onclick="openModal(\''+proNo+'\')" style="margin-left:-1.5%;margin-top:3%;">'+
                    '<span>'+evaluationState+'</span>'+
                    '</div>';
                return evaluationState;
            }
        }
    ]]
};

function getDeng(lamp){
    var deng = "";
    if (lamp != "") {
        deng = '<div style="display: inline-block;border-radius: 50%;width: 15px;height:15px;'
            +'background-color:'+lamp+';"></div>';
    }
    return deng;
}


function getStatisticalCycle(value) {
    var cycle = value;

    if (value == 0) {
        cycle = "";
    }
    return cycle;
}

function getEvaluationState(value){
    var result = "";
    for(var i = 0; i < value.length; i++){
        if(value[i] != -1){
            evaluationState = '<div style="display: inline-block;width: 13px;height: 15px;margin-left: 1px;background-color: '+getColor(getLamp("statusAssessment",value[i]).text)+';"></div>'
        }else{
            evaluationState = '<div style="display: inline-block;width: 13px;height: 15px;margin-left: 1px;"></div>'
        }
        result += evaluationState;
    }
    return result;
}

function getColor(val){
    var color = "";
    if(val == "正常"){
        color = "#00B050";
    }else if(val == "预警"){
        color = "#FFC000";
    }else if(val == "风险"){
        color = "#c00000";
    }else if(val == "未评估"){
        color = "#bdb7b7";
    }else{
        color = "black";
    }
    return color;
}

//加载当前日期
function initDateSection() {
    $.ajax({
        url: getRootPath() + '/measureComment/getTime',
        type: 'post',
        data : {
            num:6,
            flag:true,
        },
        async: false,//是否异步，true为异步
        success: function (data) {
            $("#dateSection").empty();
            data = data.data;
            if(null==data||""==data) return;
            var select_option="";
            var length = data.length<5 ? data.length: 5;
            for(var i = 0; i < length; i++){
                select_option += "<option value='"+data[i]+"'";
                if(length>1){
                    if(1==i){
                        select_option += "selected='selected'";
                    }
                }else{
                    if(page==i){
                        select_option += "selected='selected'";
                    }
                }
                select_option += ">"+data[i]+"</option>";
            }
            $("#currentTime").html("当前周期:"+data[0]);
            $("#dateSection").html(select_option);
            $('#dateSection').selectpicker('refresh');
            $('#dateSection').selectpicker('render');
        }
    });
};


function openModal(obj) {
    $("#assessStatus").modal('show');
    var dateArr = $("#mytable").bootstrapTable("getData");
    var data;
    for (var i = 0; i < dateArr.length; i++) {
        if(dateArr[i].no == obj){
            data = dateArr[i];
        }
    }
    //状态评估
    var statusAssessments = data.fiveStatusAssessment;
    for(var i = 0; i < statusAssessments.length; i++){
        oneWeekStatus = getModalStatusAssessment(statusAssessments[0]);
        twoWeekStatus = getModalStatusAssessment(statusAssessments[1]);
        threeWeekStatus = getModalStatusAssessment(statusAssessments[2]);
        fourWeekStatus = getModalStatusAssessment(statusAssessments[3]);
        fiveWeekStatus = getModalStatusAssessment(statusAssessments[4]);
        $("#oneWeekStatus").html(oneWeekStatus);
        $("#twoWeekStatus").html(twoWeekStatus);
        $("#threeWeekStatus").html(threeWeekStatus);
        $("#fourWeekStatus").html(fourWeekStatus);
        $("#fiveWeekStatus").html(fiveWeekStatus);
    }
    //质量
    var qualityLamps = data.fiveQualityLamp;
    for(var i = 0; i < qualityLamps.length; i++){
        oneWeekQuality = getModalLamp(qualityLamps[0],"quality");
        twoWeekQuality = getModalLamp(qualityLamps[1],"quality");
        threeWeekQuality = getModalLamp(qualityLamps[2],"quality");
        fourWeekQuality = getModalLamp(qualityLamps[3],"quality");
        fiveWeekQuality = getModalLamp(qualityLamps[4],"quality");
        $("#oneWeekQuality").html(oneWeekQuality);
        $("#twoWeekQuality").html(twoWeekQuality);
        $("#threeWeekQuality").html(threeWeekQuality);
        $("#fourWeekQuality").html(fourWeekQuality);
        $("#fiveWeekQuality").html(fiveWeekQuality);
    }
    //资源
    var keyRoles = data.fiveKeyRoles;
    for(var i = 0; i < keyRoles.length; i++){
        oneWeekRole = getModalLamp(keyRoles[0],"resources");
        twoWeekRole = getModalLamp(keyRoles[1],"resources");
        threeWeekRole = getModalLamp(keyRoles[2],"resources");
        fourWeekRole = getModalLamp(keyRoles[3],"resources");
        fiveWeekRole = getModalLamp(keyRoles[4],"resources");
        $("#oneWeekRole").html(oneWeekRole);
        $("#twoWeekRole").html(twoWeekRole);
        $("#threeWeekRole").html(threeWeekRole);
        $("#fourWeekRole").html(fourWeekRole);
        $("#fiveWeekRole").html(fiveWeekRole);
    }
    //进度
    var planLamps = data.fivePlanLamp;
    for(var i = 0; i < planLamps.length; i++){
        oneWeekPlan = getModalLamp(planLamps[0],"progress");
        twoWeekPlan = getModalLamp(planLamps[1],"progress");
        threeWeekPlan = getModalLamp(planLamps[2],"progress");
        fourWeekPlan = getModalLamp(planLamps[3],"progress");
        fiveWeekPlan = getModalLamp(planLamps[4],"progress");
        $("#oneWeekPlan").html(oneWeekPlan);
        $("#twoWeekPlan").html(twoWeekPlan);
        $("#threeWeekPlan").html(threeWeekPlan);
        $("#fourWeekPlan").html(fourWeekPlan);
        $("#fiveWeekPlan").html(fiveWeekPlan);
    }
    //变更
    var scopeLamps = data.fiveScopeLamp;
    for(var i = 0; i < scopeLamps.length; i++){
        oneWeekScope = getModalLamp(scopeLamps[0],"scope");
        twoWeekScope = getModalLamp(scopeLamps[1],"scope");
        threeWeekScope = getModalLamp(scopeLamps[2],"scope");
        fourWeekScope = getModalLamp(scopeLamps[3],"scope");
        fiveWeekScope = getModalLamp(scopeLamps[4],"scope");
        $("#oneWeekScope").html(oneWeekScope);
        $("#twoWeekScope").html(twoWeekScope);
        $("#threeWeekScope").html(threeWeekScope);
        $("#fourWeekScope").html(fourWeekScope);
        $("#fiveWeekScope").html(fiveWeekScope);
    }
    //点评
    // var comments = data.fiveComment;
    // for(var i = 0; i < comments.length; i++){
    //     if(comments[0] == undefined || comments[0] == "-1"){
    //         $("#oneWeekComment").html("");
    //     }else{
    //         $("#oneWeekComment").html(comments[0]);
    //         $("#oneWeekComment").attr("title", comments[0]);
    //     }
    //     if(comments[1] == undefined || comments[1] == "-1"){
    //         $("#twoWeekComment").html("");
    //     }else{
    //         $("#twoWeekComment").html(comments[1]);
    //         $("#twoWeekComment").attr("title", comments[1]);
    //     }
    //     if(comments[2] == undefined || comments[2] == "-1"){
    //         $("#threeWeekComment").html("");
    //     }else{
    //         $("#threeWeekComment").html(comments[2]);
    //         $("#threeWeekComment").attr("title", comments[2]);
    //     }
    //     if(comments[3] == undefined || comments[3] == "-1"){
    //         $("#fourWeekComment").html("");
    //     }else{
    //         $("#fourWeekComment").html(comments[3]);
    //         $("#fourWeekComment").attr("title", comments[3]);
    //     }
    //     if(comments[4] == undefined || comments[4] == "-1"){
    //         $("#fiveWeekComment").html("");
    //     }else{
    //         $("#fiveWeekComment").html(comments[4]);
    //         $("#fiveWeekComment").attr("title", comments[4]);
    //     }
    // }
}

function getModalStatusAssessment(score){
    if(getLamp("statusAssessment",score).text == "未评估"){
        week = '<div style="display: block;margin-top: 8px;width: 36px;height: 20px;line-height: 20px;background-color: '+getColor(getLamp("statusAssessment",score).text)+';">'+
            '<span style="padding-left:5px;color:black;">未评</span>'+
            '</div>'
    }else if(getLamp("statusAssessment",score).text == "风险"){
        week = '<div style="display: block;margin-top: 8px;width: 36px;height: 20px;line-height: 20px;background-color: '+getColor(getLamp("statusAssessment",score).text)+';">'+
            '<span style="padding-left:5px;">风险</span>'+
            '</div>'
    }else if(getLamp("statusAssessment",score).text == "预警"){
        week = '<div style="display: block;margin-top: 8px;width: 36px;height: 20px;line-height: 20px;background-color: '+getColor(getLamp("statusAssessment",score).text)+';">'+
            '<span style="padding-left:5px;color:black;">预警</span>'+
            '</div>'
    }else if(getLamp("statusAssessment",score).text == "正常"){
        week = '<div style="display: block;margin-top: 8px;width: 36px;height: 20px;line-height: 20px;background-color: '+getColor(getLamp("statusAssessment",score).text)+';">'+
            '<span style="padding-left:5px;">正常</span>'+
            '</div>'
    }else{
        week = '<span></span>'
    }
    return week;
}

function getModalLamp(score,val){
    if(getLamp(val,score).text == "未评估"){
        week = '<div style="display: block;width: 6px;height: 6px;border-radius: 50%;margin-top: 10px;margin-left: -60px;background-color: '+getColor(getLamp(val,score).text)+';"></div>'
    }else if(getLamp(val,score).text == "风险"){
        week = '<div style="display: block;width: 6px;height: 6px;border-radius: 50%;margin-top: 10px;margin-left: -60px;background-color: '+getColor(getLamp(val,score).text)+';"></div>'
    }else if(getLamp(val,score).text == "预警"){
        week = '<div style="display: block;width: 6px;height: 6px;border-radius: 50%;margin-top: 10px;margin-left: -60px;background-color: '+getColor(getLamp(val,score).text)+';"></div>'
    }else if(getLamp(val,score).text == "正常"){
        week = '<div style="display: block;width: 6px;height: 6px;border-radius: 50%;margin-top: 10px;margin-left: -60px;background-color: '+getColor(getLamp(val,score).text)+';"></div>'
    }else{
        week = '<span></span>'
    }
    return week;
}

$(document).on("click", "#closeBtn", function () {
    $("#assessStatus").modal('hide');
});


//项目评估

function initReview() {
        $.ajax({
            type : "post",
            url : getRootPath()+ '/projectReview/getProjectReviewDetail',
            data : {
                proNo : proNo,
                date : $("#dateSection").val(),
                flag : changeReview(),
                userId :userId
            },
            success : function(data) {
                if (data.code == "success") {
                    var report = data.data;
                    if(report){
                        model = report.type;
                        if(model!=1){
                            formatLamp(report.qualityLamp, 'quality');
                            formatLamp(report.progressLamp, 'progress');
                            formatLamp(report.changesLamp, 'changes');
                            $("#redLight").html(formatResult(report.redLight, false));
                            $("#yellowLight").html(formatResult(report.yellowLight, false));
                            $("#greenLight").html(formatResult(report.greenLight, false));
                            $("#progressDeviation").html(report.progressDeviation);
                            $("#demandCompletion").html(formatResult(report.demandCompletion, true));
                            $("#progressCompletion").html(formatResult(report.progressCompletion, true));
                            $("#changesText").html(formatResult(report.changes, true));
                            $("#changeNumber").html(formatResult(report.changeNumber, false));
                            $("#demandNumber").html(formatResult(report.demandNumber, false));
                        }else{
                            $("#measureCommentTree").css("display","none");
                        }
                        initMeasureList(report.measureList);

                        $("#memberDiffer").html(formatResult(report.memberDiffer, false));
                        $("#memberArrival").html(formatResult(report.memberArrival, true));
                        $("#keyRolesDiffer").html(formatResult(report.keyRolesDiffer, false));
                        $("#keyRolesArrival").html(formatResult(report.keyRolesArrival, true));
                        $("#keyRolesPass").html(formatResult(report.keyRolesPass, false));
                        $("#keyRolesfail").html(formatResult(report.keyRolesfail, false));
                        $("#resourcesText").html(formatResult(report.resources, true));
                        formatLamp(report.resourcesLamp, 'resources');

                        $("#review").html(report.projectReview);
                        initReviewStatus(report.projectStatus);
                    }
                }else{
                    toastr.error('服务请求失败，请稍后再试！');
                }

            }
        });
}

function changeReview() {
    var flag = true;
    $.ajax({
        type : "post",
        url : getRootPath() + '/projectReview/getCloseDateByNo',
        async: false,//是否异步，true为异步
        data : {
            proNo : proNo
        },
        success : function(data) {
            if (data != null && data != "") {
                $("#publishReview").attr("disabled", "disabled");
                $("a[name='edit']").css("color", "#ece4e4");
                $("button[name='lampEdit']").attr("disabled", "disabled");
                $("#editReview").attr("style","display:none;");
                $("#publishReview").attr("style","display:none;");
                $("#closeReview").attr("style","display:none;");
                flag = false;
            }else{
                // if (nowDate != selectedDate) {
                //     $("#publishReview").attr("disabled", "disabled");
                //     $("a[name='edit']").css("color", "#ece4e4");
                //     $("button[name='lampEdit']").attr("disabled", "disabled");
                //     $("#editReview").attr("style","display:none;");
                //     $("#publishReview").attr("style","display:none;");
                //     $("#closeReview").attr("style","display:none;");
                //     flag = false;
                // } else {
                //     $("#publishReview").removeAttr("disabled");
                //     $("a[name='edit']").css("color", "#23527c");
                //     $("button[name='lampEdit']").removeAttr("disabled");
                // }
            }
        }
    });
    return flag;
}

function formatLamp(val, id) {
    $("#" + id + 'Lamp').css("border-left-color", getLamp(id, val).lamp);
    $("#" + id + 'Mark').html(val);
}

function formatResult(val, flag) {
    if (val < 0 || val == "" || val == null) {
        return 0;
    } else if (flag && val > 100) {
        return 100;
    } else {
        return val;
    }
}
function initMeasureList(list) {
    var data= JSON.parse(list);
    var ret = "";

    _.each(JSON.parse(list), function(item, index) {
        ret += "<div><span>" + item.measureName + "：</span><span>"
            + item.measureValue + "</span></div>";
    });
    $("#measures").html("");
    $("#measures").append(ret);
}
function initReviewStatus(status) {
    var data = getLamp('status', status);
    $("#statusText").html(data.text);
    $("#statusColor").css("background-color", data.lamp);
    $("#status").html(status);
}
function measureCommentModalShow(no){
    projNo=no;
    $("#MeasureCommentTitle").html($("#dateSection").val());
    $("#measureCommentPage").html('<iframe src="measureCommentTree.html" width="100%" id="myiframe" onload="changeFrameHeight()" frameborder="0"></iframe>');

    $("#measureCommentModal").modal('show');
}


/*问题风险描述*/
function loadProjectRisk(){
    $('#problem-risk-tab').bootstrapTable('destroy');
    $('#problem-risk-tab').bootstrapTable({
        method: 'post',
        contentType: 'application/json',
        url:getRootPath() + '/projectReport/getProjectDscPageByNo',
        rowStyle:rowStyle,
        sortable: true, //是否启用排序
        sortOrder: "asc",
        striped: true, //是否显示行间隔色
        dataField: "rows",
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination:true,//是否分页
        queryParamsType:'limit',
        sidePagination:'server',
        pageSize:3,//单页记录数
        pageList:[5,10,20,30],//分页步进值
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign:'right',
        buttonsAlign:'right',//按钮对齐方式
        queryParams: function(params){
            var param = {
                no : proNo,
                limit : params.limit, //页面大小
                offset : params.offset, //页码
                sortOrder: params.order //排位命令（desc，asc）
            }
            return param;
        },
        formatNoMatches:function(){
            return "没有相关数据";
        },
        columns: [
            [{
                "title": "<div class='div-shu'></div><span style='padding-left: inherit;'>问题&风险描述</span>",
                "halign":"left",
                "align":"left",
                "colspan":8,
                'class': 'tabHead-style'
            }],
            [
            {
                title : '序号',
                field: 'num',
                align: "center",
                width: 100,
                formatter: function (value, row, index) {
                    var pageSize=$('#mytable').bootstrapTable('getOptions').pageSize;
                    var pageNumber=$('#mytable').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {title:'问题描述',field:'question',width:200,align : 'left',
                formatter:function(value,row,index){
                   return strSplit(value,20);
                }
            },
            {title:'解决措施',field:'imprMeasure',width:200,align : 'left',
                formatter:function(value,row,index){
                    return strSplit(value,20);
                }
            },
            {title:'当前进展',field:'progressDesc',width:200,align : 'left',},
            {
                title:'类型',
                field:'is361',
                width:80,
                halign :'center',
                align : 'center',
                editable:false,
                formatter:function(value,row,index){
                    return setMapVal(value,risk_type);
                }
            },
            {
                title:'严重程度',
                halign :'center',
                align : 'center',
                field:'severity',
                width:65,
                formatter:function(value,row,index){
                    return setMapText(value,work_severity);
                }
            },
            {
                title: '责任人',
                field: 'personLiable',
                width: 70,
                'class': 'personLiablClass',
                halign: 'center',
                align: 'center',
                formatter: function (value, row, index) {
                    var names="";
                    value = (null == value || "" == value)?"":value.split(',');
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
                    return names;
                }
             },
            {
                title:'计划完成时间',
                field:'finishTime',
                width:120,
                halign :'center',
                align : 'center',
                formatter:function(value,row,index){
                    if (value == null){
                        return "-";
                    }
                    return new Date(value).format('yyyy-MM-dd');
                }
            }
        ]
     ],
        locale:'zh-CN',//中文支持,
    });
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
//加载项目成员下拉列表
function getMeberSelect(proNo){
    var s1 = "";
    $.ajax({
        url:getRootPath() + '/IteManage/meberSelect',
        type:'post',
        dataType: "json",
        async:false,
        data:{
            proNo : proNo
        },
        success:function(data){
            if(data.code == 'success'){
                s1 = data.data;
            }else{
                toastr.error('加载失败!');
            }
        }
    });
    return s1;
}

function strSplit(value,num){
    if(value){
        if(value.length > num){
            return value.substr(0,num)+"......";
        }else{
            return value;
        }
    }
    return "";
}

function setMapVal(value,listMap) {
    for (var val in listMap) {
        if(value.toString() === val){
            return listMap[val];
        }
    }
    return "";
}
function setMapText(value,listMap) {
    for (var val in listMap) {
        if(value.toString() === val){
            return listMap[val].text;
        }
    }
    return "";
}

function rowStyle(row, index) {
    var bgColor = '#F7F8FA';
    if(index%2 === 0){
        bgColor = '#ffffff';
    }
    var style = {};
    style={css:{'background-color':bgColor}};
    return style;
}

function loadConcernedPerson(){
    $('#concernPerson').bootstrapTable({
        method: 'post',
        contentType: 'application/json',
        url: getRootPath() + '/attention/personList',
        rowStyle:rowStyle,
        height:400,
        sortable: true, //是否启用排序
        sortOrder: "asc",
        striped: true, //是否显示行间隔色
        dataField: "rows",
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination:true,//是否分页
        queryParamsType:'limit',
        sidePagination:'server',
        pageSize:3,//单页记录数
        pageList:[5,10,20,30],//分页步进值
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign:'right',
        buttonsAlign:'right',//按钮对齐方式
        queryParams: function(params){
            var param = {
                no : proNo,
                limit : params.limit, //页面大小
                offset : params.offset, //页码
                sortOrder: params.order //排位命令（desc，asc）
            }
            return param;
        },
        formatNoMatches:function(){
            return "没有相关数据";
        },
        onClickRow: function (row, tr, field) {
        },
        columns: [
            [{
                "title": "<div class='div-shu'></div><span style='padding-left: inherit;'>关注人员列表</span>",
                "halign":"left",
                "align":"left",
                "colspan":4,
                'class': 'tabHead-style'
            }],
            [
                {
                    title : '工号',
                    field: 'userid',
                    align: "center",
                    width: 100,
                },
                {title:'姓名',field:'username',width:200,align : 'left'},
                {title:'角色',field:'permissionNames',width:200,align : 'left',
                    formatter:function(val, row){
                        return '<p title="'+val+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+strSplit(val,8)+'</p>';
                    }
                },
                {
                    title:'操作',
                    field:'',
                    width:50,
                    halign :'center',
                    align : 'center',
                    formatter:function(value,row,index){
                        return  '<a onclick="removeConcern(\''+row.userid+'\')" ><span data-toggle="tooltip" title="取消关注当前账号"  class="glyphicon glyphicon-heart" style="color: red;font-size: 18px"></span></a>'+
                            '<a onclick="openMessWin(\''+row.userid+'\')" ><span data-toggle="tooltip" title="发送消息给该账号"  class="glyphicon glyphicon-comment" class="glyphicon glyphicon-comment"style="color: blue; font-size: 18px;padding-left: 10px"></span></a>';
                    }
                }
            ]
        ],
        locale:'zh-CN',//中文支持,
    });
}
var userId = "";
function openMessWin(concernedId){
    userId = concernedId;
    $("#receiveAccount").html(concernedId);
    $("#messageContext").val('');
    $("#messageModal").modal('show');
}
function sendMessage(){
    $.ajax({
        url:getRootPath() + '/projectOverview/saveInformation',
        type:'post',
        dataType : "json",
        cache : false,
        data:{
            'job' : userId,
            'content': $("#messageContext").val()
        },
        success:function(data){
            if(data.code == 'success'){
                $("#messageModal").modal('hide');
                toastr.success("消息已成功发送给"+userId+"用户！");
                loadConcernedPerson();
            }else{
                toastr.error('操作异常!');
            }
        }
    });
}
function removeConcern(concernedId){
    Ewin.confirm({ message: "确认取消关注当前人员吗？" }).on(function (e) {
        if (!e) {
            return;
        } else {
            $.ajax({
                url:getRootPath() + '/attention/removeConcern',
                type:'post',
                dataType: "json",
                data:{
                    id : concernedId
                },
                success:function(data){
                    if(data.code == 'success'){
                        $("#messageModal").modal('hide');
                        toastr.success('工号为'+concernedId+'的人员已成功取消关注！');
                        $('#concernPerson').bootstrapTable('refresh', {url: getRootPath() + '/attention/personList'});
                    }else{
                        toastr.error('操作异常!');
                    }
                }
            });
        }
    });
}



function loadMyMessage(){
    $('#myMessage').bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/projectOverview/getAllNews',
        editable:false,//可行内编辑
        sortable: true, //是否启用排序
        sortOrder: "asc",
        height:400,
        rowStyle:rowStyle,
        striped: true, //是否显示行间隔色
        dataField: "rows",
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination:true,//是否分页
        queryParamsType:'limit',
        sidePagination:'server',
        pageSize:5,//单页记录数
        pageList:[5,10,20,30],//分页步进值
        // paginationPreText: '&lt上一页',//指定分页条中上一页按钮的图标或文字
        // paginationNextText: '下一页&gt',//指定分页条中下一页按钮的图标或文字
        paginationLoop: false, // 关闭分页条无限循环的功能
        // showColumns:true,
        // minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        buttonsAlign:'right',//按钮对齐方式
        queryParams: function(params){
            var param = {
                limit : params.limit, //页面大小
                offset : params.offset //页码
            };
            return param;
        },
        columns:[
            [{
                "title": "<div class='div-shu'></div><span style='padding-left: inherit;'>个人消息列表</span>",
                "halign":"left",
                "align":"left",
                "colspan":4,
                'class': 'tabHead-style'
            }],
            [
            {title:'发送人',field:'sender',width:150},
            {title:'消息内容',field:'information',width:200,
                formatter:function(val, row){
                    return '<p title="'+val+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+strSplit(val,10)+'</p>';
                }
            },
            {title:'读取时间',field:'receivingTime',width:150,
                //获取日期列的值进行转换
                formatter: function (value, row, index) {
                    return changeDateFormat(value)
                }
            },
            {title:'发送时间',field:'sendTime',width:150,
                formatter: function (value, row, index) {
                    return changeDateFormat(value)
                }
            }
            ]
        ],
        locale:'zh-CN'//中文支持,
    });
}

function changeDateFormat(cellval) {
    var dateVal = cellval + "";
    if (cellval != null) {
        var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        return date.getFullYear() + "-" + month + "-" + currentDate + " " + hours + ":" + minutes + ":" + seconds;
    }
}

