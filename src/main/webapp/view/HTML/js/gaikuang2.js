var weekList  = new Array(7);
var weekList1  = new Array(7);
var weekList2  = new Array(7);
var weekList3  = new Array(7);
var weekList4  = new Array(7);
var weekList5  = new Array(7);
var weekListGr  = new Array(7);
var weekListGr1  = new Array(7);
var weekListGr2  = new Array(7);
var weekListGr3  = new Array(7);
var weekListGr4  = new Array(7);
var weekListGr5  = new Array(7);
var day = new Array(7);
var limitDayStr = "";
var limitDay = "";
var zrAccount = "";

//加载下拉列表的值
function getOPtion(){
//	 getSelectValue('rdpmExam',"通过","#rdpmExam","RDPM考试");
//	 getSelectValue('replyResults',"通过","#replyResults","答辩结果");
	 getSelectValue('position',"通过","#position","角色");
}
var proNo = window.parent.projNo;
$("#proNo").val(proNo);
function roleManage(){
	$('#mytab').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/GeneralSituation/queryByPage',
    	height:tableHeight(),//高度调整
    	toolbar: '#toolbar',
    	editable:true,//可行内编辑
    	sortable: true,                     //是否启用排序
    	sortOrder: "asc",
    	striped: true, //是否显示行间隔色
    	dataField: "rows",
    	pageNumber: 1, //初始化加载第一页，默认第一页
    	pagination:true,//是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,//单页记录数
    	pageList:[5,10,20,30],//分页步进值
    	//showRefresh:true,//刷新按钮
//    	showColumns:true,
    	minimumCountColumns: 2,             //最少允许的列数
    	//clickToSelect: true,//是否启用点击选中行
    	toolbarAlign:'right',
    	buttonsAlign:'right',//按钮对齐方式
    	toolbar:'#toolbar',//指定工作栏
    	queryParams: function(params){
        	var param = {
        			zrAccount:$("#zrAccount").val(),
//        			rolename : $("#roleName").val(),
        			rolename : encodeURI($("#roleName").val()),
//        			rdpmExam : $("#rdpmExam").val(),
//        			replyResults : $("#replyResults").val(),
        			position : encodeURI($("#position").val()),
        	        limit : params.limit, // 页面大小
        	        offset : params.offset, // 页码
        	        proNo : proNo,
        	        sort: params.sort,      //排序列名  
                    sortOrder: params.order //排位命令（desc，asc） 
        	    }
        	return param;
        },
        onEditableSave: function (field, row, oldValue, $el) {
        	if(field == "personLiable"){
        		row.personLiable = row.personLiable.toString();
        	}
        	if(field == 'iteId'){
        		if(row.iteId != oldValue ){
        			row.planStartTime = null;
        			row.planEndTime = null;
        			row.startTime = null;
        			row.endTime = null;
        		}
        	}
        	row.no = proNo;
            $.ajax({
                type: "post",
                url: getRootPath()+"/GeneralSituation/editOne",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data: JSON.stringify(row),
                success: function (data, status) {
                    if (data.code == "success") {
                    	var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
                    	$('#mytab').bootstrapTable('refresh',{pageNumber:pageNumber});
                    	$('#majorDuty').bootstrapTable('refresh');
                    	toastr.success('修改成功！');
                    	teamFormation();
                    	organizationalStructure();
                    }else{
                    	toastr.success('修改失败！');
                    }
                }
            });
        },
    	columns:[
        	{
        		title:'全选',
        		field:'select',
        		checkbox:true,
        		width:25,
        		align:'center',
        		valign:'middle'
        	},
        	{
        		title : '序号',
        		align: "center",
        		width: 30,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytab').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
             },     
        	{
        		title:'角色',
        		halign :'center',
        		align : 'center',
        		field:'role',
        		sortable:true,
        		width:100,
        		editable: {
                    type: "select",              
                    source:getSelectRoles('1'),
                    title: '角色',
                    emptytext:'&#12288',
                    placement: 'bottom'
                }
        	},
        	{
        		title:'姓名',
        		halign :'center',
        		align : 'center',
        		field:'name',
        		sortable:true,
        		width:120,
        		editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom',         //编辑框的模式：支持popup和inline两种模式，默认是popup
                    validate: function (v) {
                   	 	if(!checkMemberData(v, '姓名')){
                   	 		return '姓名只能包含汉字！';
                   	 	}
                   }
                }
        	},
        	{
        		title:'中软工号',
        		halign :'center',
        		align : 'center',
        		field:'zrAccount',
        		sortable:true,
        		width:80,
        		/*editable: {
                    type: "text",              //编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }*/
        	},
        	{
        		title:'华为工号',
        		halign :'center',
        		align : 'center',
        		field:'hwAccount',
        		width:80,
        		sortable:true,
        		editable: {
                    type: "text",              //编辑框的类型。支持text|textarea|select|date|checklist等
                    emptytext:'&#12288',
                    placement: 'bottom',         //编辑框的模式：支持popup和inline两种模式，默认是popup
                    validate: function (v) {
                   	 	if(!checkMemberData(v, '华为工号')){
                   	 		return '华为工号只能包含字母和数字！';
                   	 	}
                   }
                }
        	},
        	{
        		title:'RDPM考试',
        		halign :'center',
        		align : 'center',
        		field:'rdpmExam',
        		sortable:true,
        		width:80,
        		editable: {
                    type: "select",              
                    source:getSelectValue('rdpmExam'),
                    title: 'PDPM考试',
                    emptytext:'&#12288',
                    placement: 'bottom'    
                }
        	},
        	{
        		title:'答辩结果',
        		halign :'center',
        		align : 'center',
        		field:'replyResults',
        		width:90,
        		editable: {
                    type: "select",              //编辑框的类型。支持text|textarea|select|date|checklist等
                    source:getSelectValue('replyResults'),
                    title: '答辩结果',
                    emptytext:'&#12288',
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }
        	},
        	
        	{
        		title:'胜任度',
        		align : 'center',
        		halign :'center',
        		field:'proCompetence',
        		width:90,
        		editable: {
                    type: "select",              
                    source:getSelectValue('proCompetence'),
                    title: '胜任度',
                    emptytext:'&#12288',
                    placement: 'bottom'    
                }
        	},
        	{
        		title:'状态',
        		align : 'center',
        		halign :'center',
        		field:'status',
        		width:70,
        		editable: {
                    type: "select",              
                    source:getSelectValue('status'),
                    title: '状态',
                    emptytext:'&#12288',
                    placement: 'bottom'    
                }
        	},
        	{
        		title:'上级主管',
        		halign :'center',
        		align : 'center',
        		field:'superior',
        		sortable:true,
        		width:80,
        		/*editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }*/
        		editable: {
                        type: "select",              
                        source:getSelectNames(),
                        title: '上级主管',
                        emptytext:'&#12288',
                        placement: 'bottom'    
                    }
        	},
    	],
    	locale:'zh-CN',//中文支持,
    });
}
function roleManagement(){
	
	//根据窗口调整表格高度
	$(window).resize(function() {
	    $('#mytab').bootstrapTable('resetView', {
	        height: tableHeight()
	    })
	});
    //加载optiaon
	getOPtion();
	roleManage();
//    loadIteNameSelect('iteId');
//    getSelectValue('position');
	$('#addDemandName').bootstrapValidator({
       	feedbackIcons: {
               valid: 'glyphicon glyphicon-ok',
               invalid: 'glyphicon glyphicon-remove',
               validating: 'glyphicon glyphicon-refresh'
           },
           fields: {			
        	   demand_number: {
                   validators: {
                   	notEmpty:{
                   		message:'需求人数不能为空',
                   	}
                   }
               },
           }
     });
  $('#addForm').bootstrapValidator({
       	feedbackIcons: {
               valid: 'glyphicon glyphicon-ok',
               invalid: 'glyphicon glyphicon-remove',
               validating: 'glyphicon glyphicon-refresh'
           },
           fields: {			
               zrAccount: {
            	   validators: {
	                   	notEmpty:{
	                   		message:'中软工号不能为空',
	                   	},
	                   	regexp:{
	                   		regexp: queryRegularExpression('中软工号'),
	                   		message: '中软工号只能包含数字！'
	                   	}
                   }
               },
               name: {
                   validators: {
	                   	notEmpty:{
	                   		message:'姓名不能为空',
	                   	},
	                   	regexp:{
	                   		regexp: queryRegularExpression('姓名'),
	                   		message: '姓名只能包含汉字！'
	                   	}
                   }
               },
               hwAccount: {
                   validators: {
	                   	notEmpty:{
	                   		message:'华为工号不能为空',
	                   	},
                	   	regexp:{
	                   		regexp: queryRegularExpression('华为工号'),
	                   		message: '华为工号只能包含字母和数字！'
	                   	}
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
               name: {
                   validators: {
	                   	notEmpty:{
	                   		message:'姓名不能为空',
	                   	},
	                   	regexp:{
	                   		regexp: queryRegularExpression('姓名'),
	                   		message: '姓名只能包含汉字！'
	                   	}
                   }
               },
               hwAccount: {
                   validators: {
                	   notEmpty:{
	                   		message:'华为工号不能为空',
	                   	},
                	    regexp:{
	                   		regexp: queryRegularExpression('华为工号'),
	                   		message: '华为工号只能包含字母和数字！'
	                   	}
                   }
               },
           }
       });

    
   function operateFormatter(value,row,index){
    	if(value==2){
    		return '<i class="fa fa-lock" style="color:red"></i>'
    	}else if(value==1){
    		return '<i class="fa fa-unlock" style="color:green"></i>'
    	}else{
    		return '数据错误'
    	}
    };
    
    $(document).on("change", "#importInput", function () {
		$("#filePathInfo").val($(this).val());
	});
	
	$(document).on("change", "input:radio[name='radio1']", function () {
		if($("#work:checked").length<=0){
			$("#worktime").css('display','none');
		}else{
			$("#worktime").css('display','block');
		}
	});
	
	$(document).on("click", "#submitImport", function () {
		if($("input[name='radio1']:checked").length<=0){
			alert("请至少选中一个模板");
			return;
		}
		var filePath = $("#filePathInfo").val();
		if(filePath == ''){
			alert("请先选择要导入的文件!");
		}else if(!(filePath.endWith('.xlsx') || filePath.endWith('.xls'))){
			alert("文件格式需为xlsx或者xls");
		}else{	
			var option = {
				url: getRootPath() + $("input[name='radio1']:checked").val(),
				type: 'POST',
				dataType: 'json',
				data:{
				},
				success: function(data){
						 alert($("input[name='radio1']:checked").attr("th_value")+"成功！");
				}
			};
			$('#uploadDialog').modal('hide');
			$("#importForm").ajaxSubmit(option);
		}
		
	});
	
	$(document).on("click", "#templateDownload", function () {
		if($("input[name='radio1']:checked").length<=0){
			alert("请至少选中一个模板");
			return;
		}
		var downloadUrl = getRootPath() + '/project/projectTemplate?templateName='+$("input[name='radio1']:checked").attr("templateName");
		$("#templateDownload").attr('href', downloadUrl);
	});
    
    //项目导入按钮
    $('#btn_projectImport').click(function(){
    	$('#tableProjectListXM').bootstrapTable('refresh');
    	$("#addProjectPage").modal('show');
    });
    
    //导入历史项目的关键角色、团队成员和流程指标
    /*$('#insertBtn').click(function(){
    	var data=table.bootstrapTable('getSelections');
    	if(data.length==0){
    		toastr.warning('请选择一行数据!');
    	}else{
    		$.ajax({
 				url:getRootPath()+"/GeneralSituation/addProRolesAndTeamMembersAndProcessIndex",
 				type : 'post',
 				data:{
 					newNo:proNo,
 					oldNo:data[0].no	
 				},
 				success:function(data){
 					//后台返回成功导入历史项目的关键角色、团队成员和流程指标
 					if(data.code=='success'){
 						$("#addProjectPage").modal('hide');
 				    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
 						toastr.success('导入历史项目的关键角色、团队成员和流程指标成功！');
 					}
 					else{
 						toastr.error('导入历史项目的关键角色、团队成员和流程指标失败!');
 					}
 				}
 			});
    	}
    });*/
    
    //导入历史项目页面的查询按钮
	$(document).on("click", "#queryBtn", function () {
		$('#tableProjectListXM').bootstrapTable('refresh');
	});
    //隐藏项目导入页面
    $('#backBtn').click(function(){
    	$("#addProjectPage").modal('hide');
    });
    
    //查询按钮事件
    $('#search_btn').click(function(){
    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
    });
    //清空按钮事件
    $('#clear_btn').click(function(){
    	 $("#zrAccount").val("");
//    	 $("#iteName").val('');
//    	 $("#prior").val(""); 
    	 $("#roleName").val(""); 
    	 $("#position").val(""); 
    	 $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
    });
    
/******************************** 刷新 *********************************/
    $('#btn_refresh').click(function(){
    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
    });   
/******************************** 角色配置 *********************************/
    $('#btn_Setup').click(function(){
    	organizationalStructure();
    	$('#majorDuty').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/organizationalStructure'});
    	$("#roleConfiguration").modal('show');
    });
    $('#post_backBtn').click(function(){
    	$("#roleConfiguration").modal('hide');
    });
    $('#majorDuty_add').click(function(){
    	var proNo = window.parent.projNo;
    	$("#proNo").val(proNo);
    	$("#positionMajorDuty").empty();
    	getSelectRoles('2',"通过","#positionMajorDuty","岗位");
    	$("#majorDutyAddPage").modal('show');
    });
    //隐藏组织架构新增页面
    $('#MajorDuty_backBtn').click(function(){
    	$("#majorDutyAddPage").modal('hide');
    });
  //保存
    $('#MajorDuty_saveBtn').click(function() {
    	var demandNumber = $("#MajorDutyDemand").val();
    	var posts = $("#positionMajorDuty").val();
    	if((/(^[1-9]\d*$)/.test(demandNumber)) && demandNumber != ""){
    		if(posts == ""){
    			toastr.error('请选择岗位!');
    		}else{
    	 			$.ajax({
    	 				url:getRootPath() + '/GeneralSituation/addDemandName',
    	 				type : 'post',
    	 				data:{
    	 					no:$("#proNo").val(),
    	 					position:posts,
    	 					demand:demandNumber,
    	 				},
    	 				success:function(data){
    	 					//后台返回添加成功
    	 					if(data.code=='success'){
    	 						$("#majorDutyAddPage").modal('hide');
    	 						$("#mytab").bootstrapTable('destroy');
    	 						roleManage();
    	 						$('#majorDuty').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/organizationalStructure'});
    	 						$('#addDemandName').data('bootstrapValidator').resetForm(true);
    	 						toastr.success('添加成功！');
    	 					}
    	 					else{
    	 						toastr.error('添加失败!');
    	 					}
    	 				}
    	 			});
    		}
    	}else{
    		toastr.error('请输入正确的需求人数!');
    	}
    });
    /*********************************  新增   *******************************/
    //打开新增页面
    $('#btn_add').click(function(){
    	var proNo = window.parent.projNo;
    	$("#proNo").val(proNo);
    	$("#positionAdd").empty();
    	$("#rdpmExam").empty();
    	$("#replyResults").empty();
    	$("#proCompetence").empty();
    	$("#statusAdd").empty();
    	getSelectRoles('1',"通过","#positionAdd","角色");
	   	getSelectValue('rdpmExam',"通过","#rdpmExam","RDPM考试");
	  	getSelectValue('replyResults',"通过","#replyResults","答辩结果");
	   	getSelectValue('proCompetence',"通过","#proCompetence","胜任度");
	   	getSelectValue('status',"通过","#statusAdd","状态");
    	$("#iteAddPage").modal('show');
    	
    	$("#zrAccountAdd").blur(function() {
    		var r = /^\+?[1-9][0-9]*$/;
    		if(r.test($("#zrAccountAdd").val())){
    			$.ajax({
     				url:getRootPath() + '/GeneralSituation/editPage',
     				type : 'post',
     				data:{
     					no:$("#proNo").val(),
     					zrAccount:$("#zrAccountAdd").val()
     				},
     				success:function(data){
     					//后台返回成功
     					if(data.code=='success'){
     						$("#roleNameAdd").val(null == data.rows.name || "" == data.rows.name ? "" : data.rows.name);
     						$("#hwAccountAdd").val(null == data.rows.hwAccount || "" == data.rows.hwAccount ? "" : data.rows.hwAccount);
     						$("#positionAdd").val(null == data.rows.role || "" == data.rows.role ? "" : data.rows.role);
     						$("#rdpmExam").val(null == data.rows.rdpmExam || "" == data.rows.rdpmExam ? "" : data.rows.rdpmExam);
     						$("#replyResults").val(null == data.rows.replyResults || "" == data.rows.replyResults ? "" : data.rows.replyResults);
     						$("#proCompetence").val(null == data.rows.proCompetence || "" == data.rows.proCompetence ? "" : data.rows.proCompetence);
     						$("#statusAdd").val(null == data.rows.status || "" == data.rows.status ? "" : data.rows.status);
     					}
     				}
     			});
    		}
		})
    });
    //隐藏新增页面f
    $('#add_backBtn').click(function(){
    	$("#iteAddPage").modal('hide');
    	$('#addForm').data('bootstrapValidator').resetForm(true);
    });
    //新增保存   
    $('#add_saveBtn').click(function() {
    	//点击保存时触发表单验证
    	$('#addForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if($("#addForm").data('bootstrapValidator').isValid()){
 			$.ajax({
 				url:getRootPath() + '/GeneralSituation/add',
 				type : 'post',
// 				dataType: "json",
// 				contentType : 'application/json;charset=utf-8', //设置请求头信息
// 				data:JSON.stringify($('#addForm').serializeJSON()),
 				data:{
 					no:$("#proNo").val(),
 					name:$("#roleNameAdd").val(),
// 					position:$("#positionAdd").val(),
 					role:$("#positionAdd").val(),
 					zrAccount:$("#zrAccountAdd").val(),
 					hwAccount:$("#hwAccountAdd").val(),
 					rdpmExam:$("#rdpmExam").val(),
 					replyResults:$("#replyResults").val(),
 					proCompetence:$("#proCompetence").val(),
 					status:$("#statusAdd").val(),
 				},
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#iteAddPage").modal('hide');
 				    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
 				    	$('#majorDuty').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/organizationalStructure'});
 				    	$('#addForm').data('bootstrapValidator').resetForm(true);
 						toastr.success('保存成功！');
 					}
 					else{
 						toastr.error('保存失败!');
 					}
 				}
 			});
        }
    });
    
    
    /************************************* 编辑 *********************************/
    
    //打开修改页面
    $('#btn_edit').click(function(){
    	$("#editPosition").empty();
    	$("#editRdpmExam").empty();
    	$("#editReplyResults").empty();
    	$("#editProCompetence").empty();
    	$("#editStatus").empty();
    	getSelectValue('position',"通过","#editPosition","角色");
	   	getSelectValue('rdpmExam',"通过","#editRdpmExam","RDPM考试");
	  	getSelectValue('replyResults',"通过","#editReplyResults","答辩结果");
	   	getSelectValue('proCompetence',"通过","#editProCompetence","胜任度");
	   	getSelectValue('status',"通过","#editStatus","状态");
    	var selectRow = $('#mytab').bootstrapTable('getSelections');
    	if(selectRow.length == 1){
    		$("#editPage").modal('show');
    	}else{
    		toastr.warning('请选择一条数据进行编辑');
    		return;
    	}
    	$.ajax({
    		url:getRootPath() + '/GeneralSituation/editPage',
    		type:'post',
    		data:{
    			no : proNo,
    			zrAccount : selectRow[0].zrAccount
    		},
    		success : function(data){
    			if(data.code == 'success'){
    				if(data.rows){
                        $("#editItemNo").val(data.rows.no);
                        $("#editRoleName").val(data.rows.name);
                        $("#editZrAccount").val(data.rows.zrAccount);
//                        $("#editPosition").val(data.rows.position);
                        $("#editPosition").val(data.rows.role);
                        $("#editHwAccount").val(data.rows.hwAccount);
                        $("#editRdpmExam").val(data.rows.rdpmExam);
                        $("#editReplyResults").val(data.rows.replyResults);
                        $("#editProCompetence").val(data.rows.proCompetence);
                        $("#editStatus").val(data.rows.status);
                        /*var prior = data.rows.prior;
                        $("input[type='radio']").removeAttr('checked');
                        $("#editPrior"+prior).attr("checked", true);
                        document.getElementById('editIteId').value=data.rows.iteId;
                        document.getElementById('editImportance').value=data.rows.importance;
                        document.getElementById('editWrField').value=data.rows.wrField;*/
    				}
    			}else{
    				toastr.error('服务请求失败，请稍后再试！');
    			}
    		}
    	});
    	
    	
    });
    
    $('#edit_backBtn').click(function(){
    	$("#editPage").modal('hide');
    });
    
    //修改保存
    $('#edit_saveBtn').click(function(){
    	$('#editForm').bootstrapValidator('validate');
    	if($("#editForm").data('bootstrapValidator').isValid()){
    		$.ajax({
 				url:getRootPath()+"/GeneralSituation/edit",
 				type : 'post',
// 				dataType: "json",
// 				contentType : 'application/json;charset=utf-8', //设置请求头信息
// 				data:JSON.stringify($('#editForm').serializeJSON()),
 				data:{
 					no:$("#editItemNo").val(),
 					name:$("#editRoleName").val(),
// 					position:$("#editPosition").val(),
 					role:$("#editPosition").val(),
 					zrAccount:$("#editZrAccount").val(),
 					hwAccount:$("#editHwAccount").val(),
 					rdpmExam:$("#editRdpmExam").val(),
 					replyResults:$("#editReplyResults").val(),
 					proCompetence:$("#editProCompetence").val(),
 					status:$("#editStatus").val(),
 				},
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#editPage").modal('hide');
 				    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
 				    	$('#editForm').data('bootstrapValidator').resetForm(true);
 						toastr.success('编辑成功！');
 					}
 					else{
 						toastr.error('编辑失败!');
 					}
 				}
 			});
    	}
    });
    
/************************************* 删除 **********************************/
    //删除事件按钮
    $('#btn_delete').click(function(){
    	var zrAccounts=[];
    	var nos=[];
    	var dataArr=$('#mytab').bootstrapTable('getSelections');
    	for(var i=0;i<dataArr.length;i++){
    		nos.push(proNo);
    		zrAccounts.push(dataArr[i].zrAccount);   		
    	}
    	var zh = {noss:nos,zrAccountss:zrAccounts};
    	if (zrAccounts.length <= 0) {
    		 toastr.warning('请选择有效数据');
    		 return;
    	}
    	Ewin.confirm({ message: "确认删除已选中角色吗？" }).on(function (e) {
			if (!e) {
				return;
			} else {
				$.ajax({
	        		url:getRootPath() + '/GeneralSituation/delete',
	        		type:'post',
//	        		dataType: "json",
//					contentType : 'application/json;charset=utf-8', //设置请求头信息
	        		data:{
	        			nos:JSON.stringify(nos),
	        			zrAccounts:JSON.stringify(zrAccounts),
	        		},
	        		success:function(data){
	        			if(data.code == 'success'){
	        				toastr.success('删除成功！');
	            			$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
	            			$('#majorDuty').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/organizationalStructure'});
	        			}else{
	        				toastr.error('删除失败!');
	        			}
	        		}
	        	});
			}
    	});
    });
    
/********************************************** 导入 *************************************/
    //打开文件上传页面
    $('#btn_import').click(function(){
    	$("#demandImport").modal('show');
    	$("#filePathInfo").val('');
    	$("#openLocalFile").val('');
    });
    //打开本地文件进行选择
    $('#importBtn').click(function(){
    	$('#openLocalFile').click();
    });
    //选择文件路径
    $(document).on("change", "#openLocalFile", function () {
		$("#filePathInfo").val($(this).val());
	});
    //上传附件前校验
    $('#submitImport').click(function(){
    	var filePath = $("#filePathInfo").val();
		if(filePath == ''){
			 toastr.warning('请先选择要导入的文件!');
			 return false;
		}else if(!(filePath.endsWith('.xlsx') || filePath.endsWith('.xls'))){
			toastr.warning("文件格式需为xlsx或者xls");
			return false;
		}else{	
			var option = {
					url : getRootPath()+'/IteManage/import',
					type:'post',
					dataType: 'json',
					data : {
						proNo:proNo
					},
					success : function(data) {
						if(data){
							if(Number(data.sucNum) > 0){
								toastr.success('成功导入'+data.sucNum+'条数据!');
							}else{
								toastr.error('导入失败，请检查数据格式');
							}
						}
						//根据条件判断，执行成功的需求记录
						$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
					}
				};
			$("#importForm").ajaxSubmit(option);
		}
    });
    
    //工作内容-模板下载
    /*$(document).on("click", "#templateDownload", function () {
    	var modelName = 'demand';
		var downloadUrl = getRootPath() + '/project/projectTemplate?templateName='+modelName;
		$("#templateDownload").attr('href', downloadUrl);
	});*/
    
/***************迭代名称下拉列表值 ******************/
   function loadIteNameSelect(id){
	   $.ajax({
			url:getRootPath() + '/iteration/getIteNameSelect',
			type:'post',
			dataType: "json",
			data:{
				proNo : proNo
			},
			success:function(data){
				if(data.code == 'success'){
					s= data.data;
					if(s){
						$("#"+id).append('<option label="'+"请选择迭代"+'" value=""></option>');
						for(j = 0; j < s.length; j++) {
							$("#"+id).append('<option label="'+data.data[j].text+'" value="'+data.data[j].value+'"></option>');
						} 
					}
				}else{
					toastr.error('加载失败!');
				}
			}
		});
   }
   
   function loadIteNameSelectData(){
	   var s = "";
	   $.ajax({
			url:getRootPath() + '/iteration/getIteNameSelect',
			type:'post',
			dataType: "json",
			async:false,
			data:{
				proNo : proNo
			},
			success:function(data){
				if(data.code == 'success'){
					s= data.data;
				}else{
					toastr.error('加载失败!');
				}
			}
		});
	   return s;
   }
   //加载项目成员下拉列表
   function getMeberSelect(){
	   var s = "";
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
					s= data.data;
				}else{
					toastr.error('加载失败!');
				}
			}
		});
	   return s;
   }
};


function tableHeight() {
    return $(window).height() - 30;
}
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
    }else{
    	return '';
    }
}

//日期转换函数
function changeDateFormatHMS(cellval) {
    var dateVal = cellval + "";
    if (cellval != null) {
        var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();

        var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        return date.getFullYear() + "-" + month + "-" + currentDate+" "+hours+":"+minutes+":"+seconds;
    }else{
    	return '';
    }
}

/*//日期前后顺序比较
function compareDate(o1,o2){
	o1= new Date(Date.parse(o1));
	o2=new Date(Date.parse(o2));
	if(o1>o2){
		return true;
	}
	return false;
}
*/
//获取角色下拉菜单
function getSelectRoles(v,isOptin,id,name){
	var s;
	$.ajax({
		url:getRootPath() + '/GeneralSituation/getSelectRoles',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{
			projNo : projNo,
			type : v,
		},
		success:function(data){
			s=data.data;
			if(data.code == 'success'){
				if("通过"==isOptin){
					$(id).append('<option label="'+"请选择"+name+'" value=""></option>');
					for(j = 0; j < s.length; j++) {
						$(id).append('<option label="'+data.data[j].text+'" value="'+data.data[j].value+'"></option>');
					} 
				}
			}else{
				toastr.error('加载失败!');
			}
		}
	});
	return s;
}
//获取上级主管下拉列表人员姓名
function getSelectNames(){
	var s;
	$.ajax({
		url:getRootPath() + '/GeneralSituation/getSelectNames',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{
			projNo : projNo,
		},
		success:function(data){
			s=data.data;
		}
	});
	return s;
}
//获取下拉列表的值
function getSelectValue(type,isOptin,id,name){
	var s="";
	$.ajax({
		url:getRootPath() + '/GeneralSituation/getSelectVal1',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{
			"type":type
		},
		success:function(data){
			if(data.code == 'success'){
				s= data.data;
				if("通过"==isOptin){
					$(id).append('<option label="'+"请选择"+name+'" value=""></option>');
					for(j = 0; j < s.length; j++) {
						$(id).append('<option label="'+data.data[j].text+'" value="'+data.data[j].value+'"></option>');
					} 
				}
			}else{
				toastr.error('加载失败!');
			}
		}
	});
	return s;
}
function structureNone(){
	$('#structure_but').css("color","#491bff");
    $('#keyName_but').css("color","#000000");
    $('#structure').css("display","block");
    $('#keyName').css("display","none");
}
function keyNameNone(){
	$('#keyName_but').css("color","#491bff");
    $('#structure_but').css("color","#000000");
    $('#keyName').css("display","block");
    $('#structure').css("display","none");
}
function clearForm(formId){
	document.getElementById(formId).reset()
}
//日期大小比较
function compareDate(endTime,planEndTime){
	/*//把字符串格式转化为日期类
	var end = new Date(Date.parse(endTime));
	var planEnd = new Date(Date.parse(planEndTime));*/
	//进行比较
	return endTime > planEndTime;
}

function tabXmcb(flag){
	var week = 'person' == flag ? '#weekListPer option:selected' : '#weekListPro option:selected';
	
	if("0" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr : weekList;
	}else if("1" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr1 : weekList1;
	}else if("2" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr2 : weekList2;
	}else if("3" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr3 : weekList3;
	}else if("4" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr4 : weekList4;
	}else if("5" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr5 : weekList5;
	}
	var table = 'person' == flag ? '#grcbTable' : '#xmcbTable';

	$(table).bootstrapTable('destroy');
	$(table).bootstrapTable({
    	method: 'GET',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/GeneralSituation/projectCost',
    	striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 10, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
		queryParams : function(params){
			var param = {
					'limit': params.limit,
    				'offset': params.offset,
					'projNo' : projNo,
					'statisticalTime' : day[0],
					'nextTime' : day[6],
					'flag' : 'person' == flag ? ('1' == getCookie('zrOrhwselect') ? getCookie('username') : getZRAccount()) : ''
				}
			return param;
		},
		onEditableSave: function (field, row, oldValue, $el) {
        	row.no = projNo;
        	row.statisticalTime = $(week).attr('label');
        	if("attendenceFirst" == field || "overtimeFirst" == field){
        		row.day = 0;
        	}else if("attendenceSecond" == field || "overtimeSecond" == field){
        		row.day = 1;
        	}else if("attendenceThird" == field || "overtimeThird" == field){
        		row.day = 2;
        	}else if("attendenceFourth" == field || "overtimeFourth" == field){
        		row.day = 3;
        	}else if("attendenceFifth" == field || "overtimeFifth" == field){
        		row.day = 4;
        	}else if("attendenceSixth" == field || "overtimeSixth" == field){
        		row.day = 5;
        	}else if("attendenceSeventh" == field || "overtimeSeventh" == field){
        		row.day = 6;
        	}
            $.ajax({
                type: "post",
                url: getRootPath()+"/GeneralSituation/updateMemberCost",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', // 设置请求头信息
 				data: JSON.stringify(row),
                success: function (data, status) {
                    if (data.code == "success") {
                    	var pageNumber = $(table).bootstrapTable('getOptions').pageNumber;
                    	$(table).bootstrapTable('refresh',{pageNumber : pageNumber});
                    	toastr.success('修改成功！');
                    }else{
                    	toastr.success('修改失败！');
                    }
                }
            });
        },
		columns:[
        	[	
	    	 	{title : '序号',align: "center",valign:'middle',width:'3.5%',rowspan: 2,colspan: 1,
	    			formatter: function (value, row, index) {
	    				var pageSize=$(table).bootstrapTable('getOptions').pageSize;  
	    				var pageNumber=$(table).bootstrapTable('getOptions').pageNumber;
	    				return pageSize * (pageNumber - 1) + index + 1;
	    			}
	    		},
	    		{title:'姓名',field:'name',align: "center",valign:'middle',width:'5.5%',rowspan: 2, colspan: 1},
	    		{title:'中软工号',field:'zrAccount',align: 'center',valign:'middle',width:'8%',rowspan: 2, colspan: 1},
	    		{title:'华为工号',field:'hwAccount',align: 'center',valign:'middle',width:'7%',rowspan: 2, colspan: 1},
	    		{title:'星期一<br/>('+day[0]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[0]+'\', 0, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[0]+'\', 0, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期二<br/>('+day[1]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[1]+'\', 1, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[1]+'\', 1, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期三<br/>('+day[2]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[2]+'\', 2, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[2]+'\', 2, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期四<br/>('+day[3]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[3]+'\', 3, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[3]+'\', 3, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期五<br/>('+day[4]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[4]+'\', 4, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[4]+'\', 4, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期六<br/>('+day[5]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[5]+'\', 5, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[5]+'\', 5, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期日<br/>('+day[6]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[6]+'\', 6, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[6]+'\', 6, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
	    		{title:'合计',field:'totalHours',align: "center",valign:'middle',width:'4.5%',rowspan: 2, colspan: 1,
                    formatter:function(val, row){
        				return formatMemberHour(val);
        			}
	    		},
	    		{title:'操作',align: 'center',valign:'middle',width:'8.5%',rowspan: 2, colspan: 1,
	    			formatter:function(value,row,index){ 
						return '<button style="background-color:#239CD2; color:white; border-radius:5px; margin-left: -6px;" onclick="editHourByZrAccount(\'' + row.zrAccount + '\', \''+"qq"+'\', \''+flag+'\')">全勤</button>'
						+ '<button style="background-color:#239CD2; color:white; border-radius:5px; margin-left: 10px;" onclick="editHourByZrAccount(\'' + row.zrAccount + '\', \''+"cz"+'\', \''+flag+'\')">重置</button>';
					}
	    		}
        	],
        	[
        		{title : '出勤<br/>(小时)',field : 'attendenceFirst',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text",
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[0]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                       	 	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeFirst',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text",
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[0]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceSecond',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[1]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeSecond',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[1]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceThird',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[2]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeThird',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[2]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceFourth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[3]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeFourth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[3]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceFifth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[4]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                       	 	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeFifth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[4]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceSixth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[5]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                       	 	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeSixth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[5]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceSeventh',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[6]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                       	 	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeSeventh',align: 'center',valign:'middle',width : '6%', 
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[6]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		}
    		]
    	],
	locale:'zh-CN'//中文支持
	});
};

function initCostMonth(flag) {
	var month = 'person' == flag ? '#monthListPer' : '#monthListPro';
	
	$.ajax({
		url: getRootPath() + '/measureComment/getCostMonth',
		type: 'post',
		data : {
			startDate : $("#startDate").text()
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			$(month).empty();
			data = data.data;
			if( null == data || "" == data ) return;
			var select_option="";
			var length = data.length;
			for(var i = 0; i < length; i++){
				select_option += "<option value='"+data[i]+"'";
				if( page == i ){
					select_option += "selected='selected'";
				} 
				select_option += ">"+data[i]+"</option>";
			}
			
			$("#currentTime").html("当前月份:"+data[0]);
			$(month).html(select_option);
            $(month).selectpicker('refresh');
            $(month).selectpicker('render');
		}
	});
};

function initCostWeek(flag) {
	var month = 'person' == flag ? '#monthListPer' : '#monthListPro';
	var week = 'person' == flag ? '#weekListPer' : '#weekListPro';
	
	$.ajax({
		url: getRootPath() + '/measureComment/getCostWeek',
		type: 'post',
		data : {
			selectDate : $(month).val(),
			num : 6
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			$(week).empty();
			data = data.data;
			if( null == data || "" == data ) return;
			var select_option="";
			var length = data.length;
			for(var i = 0; i < length; i++){
				select_option += "<option value='"+data[i].week+"' label='"+data[i].cycle+"' index='"+i+"'";
				if( page == i ){
					select_option += "selected='selected'";
				} 
				select_option += ">"+data[i].week+"</option>";
			}
			
			for(var i = 0; i < 7; i++){
				if(data[0]){
					if('person' == flag){
						weekListGr[i] = data[0].day[i];
					}else{
						weekList[i] = data[0].day[i];
					}
				}
				if(data[1]){
					if('person' == flag){
						weekListGr1[i] = data[1].day[i];
					}else{
						weekList1[i] = data[1].day[i];
					}
				}
				if(data[2]){
					if('person' == flag){
						weekListGr2[i] = data[2].day[i];
					}else{
						weekList2[i] = data[2].day[i];
					}
				}
				if(data[3]){
					if('person' == flag){
						weekListGr3[i] = data[3].day[i];
					}else{
						weekList3[i] = data[3].day[i];
					}
				}
				if(data[4]){
					if('person' == flag){
						weekListGr4[i] = data[4].day[i];
					}else{
						weekList4[i] = data[4].day[i];
					}
				}
				if(data[5]){
					if('person' == flag){
						weekListGr5[i] = data[5].day[i];
					}else{
						weekList5[i] = data[5].day[i];
					}
				}
			}
			
			$("#currentTime").html("当前周:"+data[0].week);
			$(week).html(select_option);
            $(week).selectpicker('refresh');
            $(week).selectpicker('render');
		}
	});
};

$(document).ready(function(){
	initCostMonth('project');
	initCostWeek('project');
	initCostMonth('person');
	initCostWeek('person');
	/*limitDayStr = (day[0] + "").split("-");
	limitDay = limitDayStr[0] + "-" + limitDayStr[1] + "-01";*/
});

function selectMonthchange(flag){
	initCostWeek(flag);
	tabXmcb(flag);
}

function selectWeekchange(flag){
	tabXmcb(flag);
}

function formatMemberHour(val){	
	if(val.toString().split(".").length == 1 && val != 0){
		return val.toString()+".0";
	}else if(val == 0){
		return '';
	}else{
		return val;
	}
}

/********************************************** 项目成本导出 *************************************/
$(document).on("click","#exportXMCB",function(){
	var $eleForm = $("<form method='POST'></form>");
    $eleForm.attr("action",getRootPath() + "/export/exportXMCB");
    $eleForm.append($('<input type="hidden" name="proNo" value='+ projNo +'>'));
    $eleForm.append($('<input type="hidden" name="date" value='+ day[0] +'>'));
    $eleForm.append($('<input type="hidden" name="nextDate" value='+ day[6] +'>'));
    $(document.body).append($eleForm);
    //提交表单，实现下载
    $eleForm.submit();
});

function updateProjectBudget(value){
	if('' != value && !/^\d+(?=\.{0,1}\d+$|$)/ .test(value)){
		toastr.error('数据格式错误！');
		return false;
	}
	
	$.ajax({
		url : getRootPath() + '/GeneralSituation/updateProjectBudget',
		type : 'post',
		data : {
			'projectBudget' : value,
			'projNo' : projNo
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			if (data.code == "success") {
				toastr.success('修改成功！');
				setTimeout("$('#xmcb').click()", "500");
			} else {
				toastr.error('修改失败！');
			}
		}
	});
}

function limitWorkHour(v){
	if(parseFloat(v) < 0 || parseFloat(v) > 24){
		return '工时只能输入0-24小时！';
	}
}

function limitHourInput(v){
	return (new Date(v).getTime() - new Date($("#startDate").text()).getTime() < 0) ||
	(new Date(v).getTime() - new Date($("#endDate").text()).getTime() > 0) ? true : false;
}

function editHourByDay(day, week, mark, flag){
	var msg = hourTip(day) ? "本周部分工时日期不在项目开始时间和结束时间内！" : '';
	
	$.ajax({
		url : getRootPath() + '/GeneralSituation/updateHourByDay',
		type : 'post',
		data : {
			'day' : day,
			'week' : week,
			'projNo' : projNo,
			'mark' : mark,
			'flag' : 'person' == flag ? ('1' == getCookie('zrOrhwselect') ? getCookie('username') : getZRAccount()) : ''
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			if (data.code == "success") {
				if('' == msg){
					toastr.success('修改成功!');
				}else{
					toastr.warning('修改成功, ' + msg);
				}
				var table = 'person' == flag ? '#grcbTable' : '#xmcbTable';
				$(table).bootstrapTable('refresh', {pageNumber:1});
			} else {
				toastr.error('修改失败！');
			}
		}
	});
}

function hourTip(date){
	return (new Date(date).getTime() - new Date($("#startDate").text()).getTime() < 0) || 
	(new Date(date).getTime() - new Date($("#endDate").text()).getTime() > 0) ? true : false;
}

function editHourByZrAccount(zrAccount, mark, flag){
	var msg = (hourTip(day[0]) || hourTip(day[4])) ? "本周部分工时日期不在项目开始时间和结束时间内！" : '';
	
	$.ajax({
		url : getRootPath() + '/GeneralSituation/updateHourByZrAccount',
		type : 'post',
		traditional:true,
		data : {
			'zrAccount' : zrAccount,
			'dayArr' : day,
			'projNo' : projNo,
			'mark' : mark
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			if (data.code == "success") {
				if('' == msg){
					toastr.success('修改成功!');
				}else{
					toastr.warning('修改成功, ' + msg);
				}
				var table = 'person' == flag ? '#grcbTable' : '#xmcbTable';
				$(table).bootstrapTable('refresh', {pageNumber:1});
			} else {
				toastr.error('修改失败！');
			}
		}
	});
}

function oneClickInput(mark, flag){
	var msg = (hourTip(day[0]) || hourTip('cz' == mark ? day[6] : day[4])) ? "本周部分工时日期不在项目开始时间和结束时间内！" : '';
	
	$.ajax({
		url : getRootPath() + '/GeneralSituation/oneClickInput',
		type : 'post',
		traditional:true,
		data : {
			'dayArr' : day,
			'projNo' : projNo,
			'mark' : mark,
			'flag' : 'person' == flag ? ('1' == getCookie('zrOrhwselect') ? getCookie('username') : getZRAccount()) : ''
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			if (data.code == "success") {
				if('' == msg){
					toastr.success('修改成功!');
				}else{
					toastr.warning('修改成功, ' + msg);
				}
				var table = 'person' == flag ? '#grcbTable' : '#xmcbTable';
				$(table).bootstrapTable('refresh', {pageNumber:1});
			} else {
				toastr.error('修改失败！');
			}
		}
	});
}

function getMemberAccount(){
	return getProjectAccount(projNo);
}

function getZRAccount(){
	$.ajax({
		url : getRootPath() + '/GeneralSituation/getPMZRAccountByHW',
		type : 'post',
		traditional:true,
		data : {
			'hwAccount' : getCookie('username')
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			zrAccount = data;
		}
	});
	return zrAccount;
}
