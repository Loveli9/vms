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
var keyrenlinum = "";
var keyguanjiannum = "";
var userid = getCookie("username");

var rolekey = getSelectValueByType("position");

var projectestartDate = "";
var projectendDate = "";

//加载下拉列表的值
function getOPtion(){
//	 getSelectValue('rdpmExam',"通过","#rdpmExam","RDPM考试");
//	 getSelectValue('replyResults',"通过","#replyResults","答辩结果");
//	 getSelectValue('position',"通过","#position","角色");
	inintDatas();
}
var proNo = window.parent.projNo;
$("#proNo").val(proNo);
function roleManage(){
	$('#mytabs').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/GeneralSituation/queryByPage',
    	height:tableHeight(),//高度调整
    	toolbar: '#toolbar',
    	editable:true,//可行内编辑
    	sortable: true,                     //是否启用排序
    	sortOrder: "asc",
    	striped: false, //是否显示行间隔色
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
        	        userid : userid,
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
        	row.userid = userid;
            $.ajax({
                type: "post",
                url: getRootPath()+"/GeneralSituation/editOne",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data: JSON.stringify(row),
                success: function (data, status) {
                    if (data.code == "success") {
                    	var pageNumber=$('#mytabs').bootstrapTable('getOptions').pageNumber;
                    	$('#mytabs').bootstrapTable('refresh',{pageNumber:pageNumber});
                    	$('#majorDuty').bootstrapTable('refresh');
                    	toastr.success('修改成功！');
//                    	teamFormation();
 //                   	organizationalStructure();
						initRenliData();
						getProcessDtata();
                    }else if(data.code == "error"){
                        var pageNumber=$('#mytabs').bootstrapTable('getOptions').pageNumber;
                        $('#mytabs').bootstrapTable('refresh',{pageNumber:pageNumber});
                        $('#majorDuty').bootstrapTable('refresh');
                    	toastr.warning("华为账号与中软账号匹配有误，请核实后重新修改！");
                    }else {
                        var pageNumber=$('#mytabs').bootstrapTable('getOptions').pageNumber;
                        $('#mytabs').bootstrapTable('refresh',{pageNumber:pageNumber});
                        $('#majorDuty').bootstrapTable('refresh');
                        toastr.error('修改失败！');
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
        		width: 45,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytabs').bootstrapTable('getOptions').pageSize;
        		    var pageNumber=$('#mytabs').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
             },     
        	{
        		title:'角色',
        		halign :'center',
        		align : 'center',
        		field:'role',
        		sortable:true,
        		width:70,
        		editable: {
                    type: "select",              
                    source:rolekey,
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
        		width:70,
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
        		width:75,
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
        		width:75,
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
        		width:60,
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
        		width:60,
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
        		width:60,
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
        		width:65,
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
        		width:70,
        		editable: {
                        type: "select",              
                        source:getSelectNames(),
                        title: '上级主管',
                        emptytext:'&#12288',
                        placement: 'bottom'    
                    }
        	},
			{
				title:'操作',
				halign :'center',
				align : 'center',
				field:'opera',
				width: 80,
				//'修改,删除'响应id列
				formatter:function(value,row,index){
					return '<a  style="color:blue" onclick="editData2(\'' + row.no + '\',\'' + row.zrAccount + '\')">' + '修改' + '</a>'
						+'&nbsp;&nbsp;&nbsp;<a href="#" style="color:blue" onclick="delrowkeyData(\'' + row.no + '\',\'' + row.zrAccount + '\')">' + '移除' + '</a>';
				}
			}
    	],
    	locale:'zh-CN',//中文支持,
    });
}
function roleManagement(){
	
	//根据窗口调整表格高度
	$(window).resize(function() {
	    $('#mytabs').bootstrapTable('resetView', {
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
			   projects: {
				   validators: {
					   notEmpty:{
						   message:'待入项目不能为空',
					   },
				   }
			   },
			   position: {
				   validators: {
					   notEmpty:{
						   message:'角色不能为空',
					   },
				   }
			   },
			   status1: {
				   validators: {
					   notEmpty:{
						   message:'状态不能为空',
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
    	$('#mytabs').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
    });
    //清空按钮事件
    $('#clear_btn').click(function(){
    	 $("#zrAccount").val("");
//    	 $("#iteName").val('');
//    	 $("#prior").val(""); 
    	 $("#roleName").val(""); 
    	 $("#position").val(""); 
    	 $('#mytabs').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
    });
    
/******************************** 刷新 *********************************/
    $('#btn_refresh').click(function(){
    	$('#mytabs').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
    });   

    /*********************************  新增   *******************************/
    //打开新增页面
    $('#btn_add').click(function(){
    	//var proNo = window.parent.projNo;
		var userid = getCookie("username");
    	$("#userids").val(userid);
    	$("#positionAdd").empty();
    	$("#rdpmExam").empty();
    	$("#replyResults").empty();
    	$("#proCompetence").empty();
    	$("#statusAdd").empty();
		$("#keyproject").empty();
//    	getSelectRoles('1',"通过","#positionAdd","角色");
		inintDatas();
	   	getSelectValue('rdpmExam',"通过","#rdpmExam","RDPM考试");
	  	getSelectValue('replyResults',"通过","#replyResults","答辩结果");
	   	getSelectValue('proCompetence',"通过","#proCompetence","胜任度");
	   	getSelectValue('status',"通过","#statusAdd","状态");
		getRelateProjectskeyrole();
    	$("#iteAddPage").modal('show');
    	
    	$("#zrAccountAdd").blur(function() {
    		var r = /^\+?[1-9][0-9]*$/;
    		if(r.test($("#zrAccountAdd").val())){
    			$.ajax({
     				url:getRootPath() + '/GeneralSituation/editPage',
     				type : 'post',
     				data:{
     					userid:$("#userids").val(),
     					zrAccount:$("#zrAccountAdd").val()
     				},
     				success:function(data){
     					//后台返回成功
     					if(data.code=='success'){
     						$("#roleNameAdd").val(null == data.rows.name || "" == data.rows.name ? "" : data.rows.name);
     						$("#hwAccountAdd").val(null == data.rows.hwAccount || "" == data.rows.hwAccount ? "" : data.rows.hwAccount);
     						$("#positionAdd").val(null == data.rows.role || "" == data.rows.role ? "" : data.rows.role);
							$("#keyproject").val(null == data.rows.reproject || "" == data.rows.reproject ? "" : data.rows.reproject);
     						$("#rdpmExam").val(null == data.rows.rdpmExam || "" == data.rows.rdpmExam ? "" : data.rows.rdpmExam);
     						$("#replyResults").val(null == data.rows.replyResults || "" == data.rows.replyResults ? "" : data.rows.replyResults);
     						$("#proCompetence").val(null == data.rows.proCompetence || "" == data.rows.proCompetence ? "" : data.rows.proCompetence);
     						$("#statusAdd").val(null == data.rows.status || "" == data.rows.status ? "" : data.rows.status);
     					}
     				}
     			});
    		}
		});

		$("#keyproject").blur(function() {
			demoList=null;
			$.ajax({
				url:getRootPath() + '/bu/projOverviewDatas',
				type : 'post',
				data:{
					reprname:$("#keyproject").val()
				},
				success:function(data){
					projectestartDate = new Date(data.startDate).format('yyyy-MM-dd');
					projectendDate = new Date(data.endDate).format('yyyy-MM-dd');
				}
			})
		});
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
			// 华为账号校验
			if (!verifyHwAccount($("#zrAccountAdd").val(),$("#hwAccountAdd").val())) {
				toastr.warning("华为账号与中软账号匹配有误，请核实后重新配置")
				return ;
			}
 			$.ajax({
 				url:getRootPath() + '/GeneralSituation/add',
 				type : 'post',
				async:false,
// 				dataType: "json",
// 				contentType : 'application/json;charset=utf-8', //设置请求头信息
// 				data:JSON.stringify($('#addForm').serializeJSON()),
 				data:{
 					userid:$("#userids").val(),
 					name:$("#roleNameAdd").val(),
// 					position:$("#positionAdd").val(),
 					role:$("#positionAdd").val(),
 					zrAccount:$("#zrAccountAdd").val(),
 					hwAccount:$("#hwAccountAdd").val(),
					reproject:$("#keyproject").val(),
 					rdpmExam:$("#rdpmExam").val(),
 					replyResults:$("#replyResults").val(),
 					proCompetence:$("#proCompetence").val(),
 					status:$("#statusAdd").val(),
					startDate : projectestartDate,
					endDate : projectendDate,
 				},
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#iteAddPage").modal('hide');
 				    	$('#mytabs').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
 			    	    $('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembers'});
 				    	$('#addForm').data('bootstrapValidator').resetForm(true);
						initRenliData();
						getProcessDtata();
 						toastr.success('保存成功！');
 					}else if(data.code=='nopmid'){
						toastr.error('用户暂无匹配中软工号信息，角色添加失败!');
					} else{
 						toastr.error('保存失败!');
 					}
 				}
 			});
        }
    });
    
    
    /************************************* 编辑 *********************************/




    $('#edit_backBtn').click(function(){
    	$("#editPage").modal('hide');
    });
    
    //修改保存
    $('#edit_saveBtn').click(function(){
    	$('#editForm').bootstrapValidator('validate');
    	if($("#editForm").data('bootstrapValidator').isValid()){
			if (!verifyHwAccount($("#editZrAccount").val(),$("#editHwAccount").val())) {
				toastr.warning("华为账号与中软账号匹配有误，请核实后重新修改")
				return ;
			}
    		$.ajax({
 				url:getRootPath()+"/GeneralSituation/edit",
 				type : 'post',
// 				dataType: "json",
// 				contentType : 'application/json;charset=utf-8', //设置请求头信息
// 				data:JSON.stringify($('#editForm').serializeJSON()),
 				data:{
 					userid:$("#editItemNo").val(),
 					name:$("#editRoleName").val(),
// 					position:$("#editPosition").val(),
 					role:$("#editPosition").val(),
 					zrAccount:$("#editZrAccount").val(),
 					hwAccount:$("#editHwAccount").val(),
					reproject:$("#keyprojectedit").val(),
 					rdpmExam:$("#editRdpmExam").val(),
 					replyResults:$("#editReplyResults").val(),
 					proCompetence:$("#editProCompetence").val(),
 					status:$("#editStatus").val(),
 				},
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#editPage").modal('hide');
 				    	$('#mytabs').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
						$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembers'})
 				    	$('#editForm').data('bootstrapValidator').resetForm(true);
						$('#addForms').data('bootstrapValidator').resetForm(true);
						initRenliData();
						getProcessDtata();
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
    	var dataArr=$('#mytabs').bootstrapTable('getSelections');
    	for(var i=0;i<dataArr.length;i++){
    		nos.push(dataArr[i].no);
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
	            			$('#mytabs').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
	        //    			$('#majorDuty').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/organizationalStructure'});
							initRenliData();
							getProcessDtata();
	        			}else{
	        				toastr.error('删除失败!');
	        			}
	        		}
	        	});
			}
    	});
    });

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



//获取待入/所属项目下拉菜单
function getRelateProjectskeyrole(){
	var s;
	$.ajax({
		url:getRootPath() + '/user/getRelateProjects',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{
			userid : userid
		},
		success:function(data){
			s=data.rows;
			if(null != s){
				var option = '<option label="'+"请选择待入项目"+'" value="">'+"请选择待入项目"+'</option>';
				var options = '<option label="'+"请选择所属项目"+'" value="">'+"请选择所属项目"+'</option>';

				for(j = 0; j < s.length; j++) {
					option +='<option label="'+s[j].NO+'" value="'+s[j].NAME+'">'+s[j].NAME+'</option>';
					options +='<option label="'+s[j].NO+'" value="'+s[j].NAME+'">'+s[j].NAME+'</option>';
				}
				$('#keyproject').html(option);
      			$('#keyprojectedit').html(options);
			}else{
				toastr.warning('当前用户无关联项目，无权查看与操作此页面！');
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
			userid : userid,
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
			if(data.code == '200'){
				s= data.data;
				if("通过"==isOptin){
					$(id).append('<option label="'+"请选择"+name+'" value="">'+"请选择"+name+'</option>');
					for(j = 0; j < s.length; j++) {
						$(id).append('<option label="'+data.data[j].text+'" value="'+data.data[j].value+'">'+data.data[j].text+'</option>');
					} 
				}
			}else{
				toastr.error('加载失败!');
			}
		}
	});
	return s;
}
function inintDatas() {
	setOption(rolekey, "#position", "角色");
	setOption(rolekey, "#positionAdd", "角色");
	setOption(rolekey, "#editPosition", "角色");
}

function initRenliData(){
	var userid = getCookie('username');
	$.ajax({
		url : getRootPath()+ '/user/getZongrenliData',
		async: false,
		data : {
			userid : userid,
		},
		success : function(data) {
			if (data.code == "success") {
				var renyuan = data.data;
				$("#zrl").html(formatResult(renyuan.renlinum,false));
				$("#gjjs").html(formatResult(renyuan.guanjiannum,false));
				$("#wtry").html(formatResult(renyuan.wentinum,false));
				keyrenlinum = formatResult(renyuan.renlinums,false);
				keyguanjiannum = formatResult(renyuan.guanjiannums,false);
			}else{
				toastr.error('服务请求失败，请稍后再试！');
			}

		}
	});
}

function formatResult(val,flag) {
	if (val < 0 || val == "" || val == null) {
		return 0;
	}else if (flag && val > 100) {
		return 100;
	}else {
		return val;
	}
}

function getProcessDtata() {
	var width1;
	var width2;
	$.ajax({
		url : getRootPath() + "/user/getMembersinfoBypm",
		type : 'POST',
		async: false,//是否异步，true为异步
		data : {
			userid : userid,
		},
		success : function(data) {

			if(data.data==null){
				kaifaCount = 0,
					keyroleCount =0
			}else{
				kaifaCount = (data.data.rowsMembercount),
					keyroleCount = (data.data.rowsRoleCount)
			}
		}
	})
	if (kaifaCount==0){
		width1 = 0
		$("#percet1").html(Math.round(width1)+"%");
		$("#width1").css("width",Math.round(width1)+"%");
	} else {
		width1= formatResult((keyrenlinum/kaifaCount)*100,true)
		$("#percet1").html(Math.round(width1)+"%");
		$("#width1").css("width",Math.round(width1)+"%");
	}
	if (keyroleCount==0){
		width2 = 0
		$("#percet2").html(Math.round(width2)+"%");
		$("#width2").css("width",Math.round(width2)+"%");
	} else {
		width2= formatResult((keyguanjiannum/keyroleCount)*100,true)
		$("#percet2").html(Math.round(width2)+"%");
		$("#width2").css("width",Math.round(width2)+"%");
	}

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
    $('#position').empty();
	roleManagement();
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







$(document).ready(function(){
//	initCostMonth('project');
//	initCostWeek('project');
//	initCostMonth('person');
//	initCostWeek('person');
	/*limitDayStr = (day[0] + "").split("-");
	limitDay = limitDayStr[0] + "-" + limitDayStr[1] + "-01";*/
});




function formatMemberHour(val){	
	if(val.toString().split(".").length == 1 && val != 0){
		return val.toString()+".0";
	}else if(val == 0){
		return '';
	}else{
		return val;
	}
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
function editData2(no,zrAccount){

	$("#editPosition").empty();
	$("#editRdpmExam").empty();
	$("#editReplyResults").empty();
	$("#editProCompetence").empty();
	$("#editStatus").empty();
	inintDatas();
	getSelectValue('rdpmExam',"通过","#editRdpmExam","RDPM考试");
	getSelectValue('replyResults',"通过","#editReplyResults","答辩结果");
	getSelectValue('proCompetence',"通过","#editProCompetence","胜任度");
	getSelectValue('status',"通过","#editStatus","状态");
	getRelateProjectskeyrole();
     $("#editPage").modal('show');
	$.ajax({
		url:getRootPath() + '/GeneralSituation/editPage',
		type:'post',
		data:{
			userid : userid,
			no : no,
			zrAccount : zrAccount
		},
		success : function(data){
			if(data.code == 'success'){
				if(data.rows){
					$("#editItemNo").val(data.rows.userid);
					$("#editRoleName").val(data.rows.name);
					$("#editZrAccount").val(data.rows.zrAccount);
//                        $("#editPosition").val(data.rows.position);
					$("#editPosition").val(data.rows.role);
					$("#editHwAccount").val(data.rows.hwAccount);
					$("#keyprojectedit").val(data.rows.reproject);
					$("#editRdpmExam").val(data.rows.rdpmExam);
					$("#editReplyResults").val(data.rows.replyResults);
					$("#editProCompetence").val(data.rows.proCompetence);
					$("#editStatus").val(data.rows.status);
				}
			}else{
				toastr.error('服务请求失败，请稍后再试！');
			}
		}
	});

}
function delrowkeyData(no,zrAccount) {
	Ewin.confirm({ message: "确认删除已选中角色吗？" }).on(function (e) {
		if (!e) {
			return;
		} else {
			$.ajax({
				url:getRootPath() + '/GeneralSituation/deleteone',
				type:'post',
//	        		dataType: "json",
//					contentType : 'application/json;charset=utf-8', //设置请求头信息
				data:{
					no:no,
					zrAccount:zrAccount,
				},
				success:function(data){
					if(data.code == 'success'){
						toastr.success('删除成功！');
						$('#mytabs').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/queryByPage'});
						//    			$('#majorDuty').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/organizationalStructure'});
						initRenliData();
						getProcessDtata();
					}else{
						toastr.error('删除失败!');
					}
				}
			});
		}
	});

}
