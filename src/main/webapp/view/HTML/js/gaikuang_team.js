//加载下拉列表的值
function getOPtion(){
	 getSelectValue('role',"通过","#role","岗位");
	 getSelectValue('status',"通过","#status","状态");
	 getSelectValue('rank',"通过","#rank","职级");
}

var path;
var buName;
var str;
var sel="";
var ids="363, 371, 366, 367, 356, 357, 358, 369, 275, 313, 326, 327, 329, 331, 332, 341";

$(function() {	
	path = window.parent.location.search.substr(1);
	str = path.substring(path.indexOf('=')+1,path.indexOf('&'));
	$("#teamId").val(str);
	//根据窗口调整表格高度
    $(window).resize(function() {
        $('#mytab').bootstrapTable('resetView', {
            height: tableHeight()
        })
    });
    //加载optiaon
    getOPtion();
    $('#mytab').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/GeneralSituation/getTeamMembers',
    	height:tableHeight(),//高度调整
    	toolbar: '#toolbar',
    	editable:true,//可行内编辑
    	sortable: true,//是否启用排序
    	sortOrder: "asc",
    	striped: true, //是否显示行间隔色
    	dataField: "rows",
    	pageNumber: 1, //初始化加载第一页，默认第一页
    	pagination:true,//是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,//单页记录数
    	pageList:[5,10,20,30],//分页步进值
    	minimumCountColumns: 2,//最少允许的列数
    	clickToSelect: true,//是否启用点击选中行
    	toolbarAlign:'right',
    	buttonsAlign:'right',//按钮对齐方式
    	toolbar:'#toolbar',//指定工作栏
    	uniqueId: 'zrAccount',
    	queryParams: function(params){
        	var param = {
        	        limit : params.limit, // 页面大小
        	        offset : params.offset, // 页码
        	        teamId : $("#teamId").val(),
        	        sort: params.sort,      //排序列名  
                    sortOrder: params.order //排位命令（desc，asc） 
        	    }
        	return param;
        },
        onEditableSave: function (field, row, oldValue, $el) {
        	if(field == "personLiable"){
        		row.personLiable = row.personLiable.toString();
        	}
            $.ajax({
                type: "post",
                url: getRootPath()+"/user/editTeamMemberInfo",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data: JSON.stringify(row),
                success: function (data, status) {
                    if (data.code == "success") {
                    	var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
                    	$('#mytab').bootstrapTable('refresh',{pageNumber:pageNumber});
                    	toastr.success('修改成功！');
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
        		width:34,
        		align:'center',
        		valign:'middle'
        	},
        	{
        		title : '序号',
        		align: "center",
        		width: 40,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytab').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
             },
             {
         		title:'中软工号',
         		halign :'center',
         		align : 'center',
         		field:'zrAccount',
//         		sortable:true,
         		width:107,
         		/*editable: {
                     type: "text",              //编辑框的类型。支持text|textarea|select|date|checklist等
                     placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                 }*/
         		formatter:function(value,row,index){
					if(value){
						return	'<a href="#" style="color:blue" onclick="editData(\'' + row.zrAccount + '\')">' + value + '</a>'; 
					}
					return '';
				}
         	},
        	{
        		title:'姓名',
        		halign :'center',
        		align : 'center',
        		field:'name',
//        		sortable:true,
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
        		title:'华为工号',
        		halign :'center',
        		align : 'center',
        		field:'hwAccount',
        		width:100,
//        		sortable:true,
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
        		title:'SVN/GIT帐号',
        		halign :'center',
        		align : 'center',
        		field:'svnGitNo',
        		width:100,
//        		sortable:true,
        		editable: {
                    type: "text",              //编辑框的类型。支持text|textarea|select|date|checklist等
                    emptytext:'&#12288',
                    placement: 'bottom',         //编辑框的模式：支持popup和inline两种模式，默认是popup
                    validate: function (v) {
                   	 	if(!checkMemberData(v, '华为工号')){
                   	 		return 'SVN/GIT帐号只能包含字母和数字！';
                   	 	}
                   }
                }
        	},
        	{
        		title:'职级',
        		halign :'center',
        		align : 'center',
        		field:'rank',
//        		sortable:true,
        		width:80,
        		editable: {
                    type: "select",              
                    source:getSelectValue('rank'),
                    title: '职级',
                    emptytext:'&#12288',
                    placement: 'bottom'    
                }
        	},
        	{
        		title:'岗位',
        		halign :'center',
        		align : 'center',
        		field:'role',
//        		sortable:true,
        		width:80,
        		editable: {
                    type: "select",              
                    source:getSelectValue('role'),
                    title: '岗位',
                    emptytext:'&#12288',
                    placement: 'bottom'    
                }
        	},
        	{
        		title:'状态',
        		halign :'center',
        		align : 'center',
        		field:'status',
//        		sortable:true,
        		width:80,
        		editable: {
                    type: "select",              
                    source:getSelectValue('status'),
                    title: '状态',
                    emptytext:'&#12288',
                    placement: 'bottom'    
                }
        	},
        	{
        		title:'操作',
        		halign :'center',
        		align : 'center',
        		field:'opera',
				width: 200,
				//'修改'响应id列
				formatter:function(value,row,index){ 
					return '<a href="#" style="color:blue" onclick="editData(\'' + row.zrAccount + '\')">' + '修改' + '</a>'
					+'&nbsp;&nbsp;&nbsp;<a href="#" style="color:blue" onclick="delData(\'' + row.zrAccount + '\')">' + '删除' + '</a>'
					+'&nbsp;&nbsp;&nbsp;<a href="#" style="color:blue" onclick="resetTeamRank(\'' + row.zrAccount + '\', \'' + row.rank + '\')">' + '重置职级' + '</a>';
				}
        	}
    	],
    	locale:'zh-CN',//中文支持,
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
                   		message:'中软工号不能为空'
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
               svnGitNo: {
                   validators: {
                	   	regexp:{
	                   		regexp: queryRegularExpression('华为工号'),
	                   		message: 'SVN/GIT帐号只能包含字母和数字！'
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
               svnGitNo: {
                   validators: {
                	   	regexp:{
	                   		regexp: queryRegularExpression('华为工号'),
	                   		message: 'SVN/GIT帐号只能包含字母和数字！'
	                   	}
                   }
               }
           }
       });
	
  //打开新增页面
    $('#btn_add').click(function(){
    	$("#roleAdd").empty();
    	$("#statusAdd").empty();
    	$("#rankAdd").empty();
    	getSelectValue('role',"通过","#roleAdd","岗位");
    	getSelectValue('status',"通过","#statusAdd","状态");
    	getSelectValue('rank',"通过","#rankAdd","职级");
    	$("#iteAddPage").modal('show');
    	
    	$("#zrAccountAdd").blur(function() {
    		var r = /^\+?[1-9][0-9]*$/;
    		if(r.test($("#zrAccountAdd").val())){
    			$.ajax({
     				url:getRootPath() + '/GeneralSituation/teamMemberEcho',
     				type : 'post',
     				data:{
     					teamId : $("#teamId").val(),
     					zrAccount:$("#zrAccountAdd").val()
     				},
     				success:function(data){
     					//后台返回成功
     					if(data){
     						$("#nameAdd").val(null == data.NAME || "" == data.NAME ? "" : data.NAME);
     						if(null == data.ROLE || "" == data.ROLE){
     							$("#roleAdd").val("");
     						}else{
     							$("#roleAdd").find("option[text="+data.ROLE+"]").attr("selected",true);
     						}
     						$("#svnGitNoAdd").val(null == data.SVN_GIT_NO || "" == data.SVN_GIT_NO ? "" : data.SVN_GIT_NO);
     						$("#hwAccountAdd").val(null == data.HW_ACCOUNT || "" == data.HW_ACCOUNT ? "" : data.HW_ACCOUNT);
     						$("#statusAdd").val(null == data.STATUS || "" == data.STATUS ? "" : data.STATUS);
     						$("#rankAdd").val(null == data.RANK || "" == data.RANK ? "" : data.RANK);
     					}
     				}
     			});
    		}
		})
    });
  //隐藏新增页面
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
 				url:getRootPath() + '/user/addTeamMember',
 				type : 'post',
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data:JSON.stringify($('#addForm').serializeJSON()),
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#iteAddPage").modal('hide');
 				    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/getTeamMembers'});
 				    	$('#addForm').data('bootstrapValidator').resetForm(true);
 						toastr.success('新增成功！');
 					}
 					else{
 						toastr.error('新增失败!');
 					}
 				}
 			});
        }
    });
    
  //点击'修改'启用编辑
    editData =  function (selectId){
    	getSelectValue('role',"通过","#roleEdit","岗位");
    	getSelectValue('status',"通过","#statusEdit","状态");
    	getSelectValue('rank',"通过","#rankEdit","职级");
    	$('#zrAccountEdit').attr("readonly",true);
    	
    	//直接关联单行数据
    	var selectRow = $('#mytab').bootstrapTable('getRowByUniqueId', selectId);
    	$("#editPage").modal('show');
    	$("#teamIdEdit").val(selectRow.teamId);
        $("#nameEdit").val(selectRow.name);
        $("#zrAccountEdit").val(selectRow.zrAccount);
        $("#roleEdit").selectpicker('val', selectRow.role);
        $("#hwAccountEdit").val(selectRow.hwAccount);
        $("#svnGitNoEdit").val(selectRow.svnGitNo);
        $("#statusEdit").selectpicker('val', selectRow.status);
        $("#rankEdit").selectpicker('val', selectRow.rank);
    };
    
  //打开编辑页面
    /*$('#btn_edit').click(function(){
    	$("#roleEdit").empty();
    	getSelectValue('role',"通过","#roleEdit","岗位");
    	var selectRow = $('#mytab').bootstrapTable('getSelections');
    	if(selectRow.length == 1){
    		$("#editPage").modal('show');
    	}else{
    		toastr.warning('请选择一条数据进行编辑');
    		return;
    	}
    	$.ajax({
    		url:getRootPath() + '/user/getTeamMemberInfo',
    		type:'get',
    		data:{
    			teamId : $("#teamId").val(),
    			zrAccount : selectRow[0].zrAccount
    		},
    		success : function(data){
    			if(data.code == 'success'){
    				if(data.rows){
    					$("#teamIdEdit").val(data.rows.teamId);
                        $("#nameEdit").val(data.rows.name);
                        $("#zrAccountEdit").val(data.rows.zrAccount);
                        $("#roleEdit").val(data.rows.role);
                        $("#hwAccountEdit").val(data.rows.hwAccount);
                        $("#svnGitNoEdit").val(data.rows.svnGitNo);
    				}
    			}else{
    				toastr.error('服务请求失败，请稍后再试！');
    			}
    		}
    	});
    });*/
  //隐藏编辑页面
    $('#edit_backBtn').click(function(){
    	$("#editPage").modal('hide');
    });
  //修改保存
    $('#edit_saveBtn').click(function(){
    	$('#editForm').bootstrapValidator('validate');
    	if($("#editForm").data('bootstrapValidator').isValid()){
    		$.ajax({
    	        url:getRootPath()+"/user/editTeamMemberInfo",
    	        type : 'post',
    	        dataType: "json",
    	        contentType : 'application/json;charset=utf-8', //设置请求头信息
    	        data:JSON.stringify($('#editForm').serializeJSON()),
    	        success:function(data){
    	          //后台返回添加成功
    	          if(data.code=='success'){
    	            $("#editPage").modal('hide');
    	              $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/getTeamMembers'});
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
  //删除团队成员按钮
    $('#btn_delete').click(function(){
    	var zrAccounts=[];
    	var teamIds=[];
    	var dataArr=$('#mytab').bootstrapTable('getSelections');
    	for(var i=0;i<dataArr.length;i++){
    		teamIds.push(dataArr[i].teamId);
    		zrAccounts.push(dataArr[i].zrAccount); 		
    	}
    	var hm = {teamIdss:teamIds,zrAccountss:zrAccounts};
    	if (zrAccounts.length <= 0) {
    		 toastr.warning('请选择有效数据');
    		 return;
    	}
    	Ewin.confirm({ message: "确认删除已选中成员吗？" }).on(function (e) {
			if (!e) {
				return;
			} else {
				$.ajax({
	        		url:getRootPath() + '/user/deleteTeamMembers',
	        		type:'post',
	        		data:{
	        			teamIds:JSON.stringify(teamIds),
	        			zrAccounts:JSON.stringify(zrAccounts),
	        		},
	        		success:function(data){
	        			if(data.code == 'success'){
	        				toastr.success('删除成功！');
	            			$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/getTeamMembers'});
	        			}else{
	        				toastr.error('删除失败!');
	        			}
	        		}
	        	});
			}
    	});
    });
    
    delData = function (selectId){ 
    	var zrAccounts=[];
    	var teamIds=[];
    	var dataArr = $('#mytab').bootstrapTable('getRowByUniqueId', selectId);
    	teamIds.push(dataArr.teamId);
    	zrAccounts.push(dataArr.zrAccount);
    	Ewin.confirm({ message: "确认删除已选中成员吗？" }).on(function (e) {
			if (!e) {
				return;
			} else {
				$.ajax({
	        		url:getRootPath() + '/user/deleteTeamMembers',
	        		type:'post',
	        		data:{
	        			teamIds:JSON.stringify(teamIds),
	        			zrAccounts:JSON.stringify(zrAccounts),
	        		},
	        		success:function(data){
	        			if(data.code == 'success'){
	        				toastr.success('删除成功！');
	            			$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/getTeamMembers'});
	        			}else{
	        				toastr.error('删除失败!');
	        			}
	        		}
	        	});
			}
    	});
    };
    
    /********************************************** 导入 *************************************/
    //打开文件上传页面
    $('#btn_import').click(function(){
    	$("#teamMembersImport").modal('show');
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
    //从Excel中导入团队成员
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
    				url : getRootPath()+'/teamInfo/importTeamMembers',
    				type:'post',
    				dataType: 'json',
    				data : {
    					teamId : $("#teamId").val()
    				},
    				success : function(data) {
    					if(data){
    						if(data.chinasoftAccountSize > 10){
    							toastr.error('中软工号长度大于10!');
    						}
    						if(data.account == "false"){
    							toastr.error('中软工号不为纯数字组成!');
    						}
    						if(data.huaweiAccount == "false"){
    							toastr.error('华为工号不为数字或不为数字和英文字母!');
    						}
    						if(data.account == "empty"){
    							toastr.error('中软工号为空!');
    						}
    						if(data.memberName == "empty"){
    							toastr.error('姓名为空!');
    						}
    						if(data.memberName == "false"){
    							toastr.error('姓名不为汉字组成!');
    						}
    						if(data.huaweiAccount == "empty"){
    							toastr.error('华为工号为空!');
    						}
    						if(data.station == "flase"){
								toastr.error('岗位不存在!');
							}
    						if(data.status == "flase"){
								toastr.error('人员状态不存在!');
							}
    						if(data.rank == "flase"){
								toastr.error('职级不存在!');
							}
    						if(Number(data.sucNum) > 0){
    							toastr.success('成功导入'+data.sucNum+'条数据!');
    						}else{
    							toastr.error('导入失败!');
    						}
    					}
    					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/getTeamMembers'});
    				}
    			};
    		$("#importForm").ajaxSubmit(option);
    	}
    });
    
   //团队成员-模板下载
    $(document).on("click", "#templateDownload", function () {
    	var modelName = 'team-member';
		var downloadUrl = getRootPath() + '/project/projectTemplate?templateName='+modelName;
		$("#templateDownload").attr('href', downloadUrl);
	});
    
  //手工指标录入
	$(document).on("click", "a[id='sgzbinfo']", function () {		
		$.ajax({
			url: getRootPath() + '/iteration/getMessage',
			type: 'post',
			data:{
				proNo : projNo
			},
			success: function (data) {
				
				var cyclelist = "";
				var sgzbColumns = [];
				var list = data.data;				
				var t1 = {title:'序号',align:'center',width:60,
						formatter:function(value, row, index) {
						    var pageSize=$('#sgzbtab').bootstrapTable('getOptions').pageSize;
							var pageNumber=$('#sgzbtab').bootstrapTable('getOptions').pageNumber;
							 return pageSize * (pageNumber - 1) + index + 1;
							}
					}
				sgzbColumns.push(t1);
				var t2 = {field: 'measure_id', title: '指标ID', visible:false,width:130};
				sgzbColumns.push(t2);
				var t3 = {field: 'measure_name', title: '指标名称', align: 'center',width:240};
				sgzbColumns.push(t3);
				var t4 = {field: 'unit', title: '单位', align: 'center',width:100};
				sgzbColumns.push(t4);
				var cycle = [];
				for (var i = 0; i < list.length; i++) {			
                    var temp = createColumns(list[i]);
                    cycle.push(list[i].id);
                    sgzbColumns.push(temp);
                }	
				cyclelist = cycle.join(',');
				createTable(sgzbColumns,cyclelist);
			}
		});
		
	});
    
	/*buName = window.parent.projBU;
	var units = {};*/
	/*function initData() {
		$.ajax({
			url : getRootPath() + "/bu/teamInfo",
			type : 'get',
			async: false,//是否异步，true为异步
			data : {
				teamNo : str
			},
			success : function(data) {
				$("#name").empty();
				$("#tm").empty();
				
				$("#name").append(data[0].teamName);
				$("#name").attr("title",data[0].teamName);
				$("#tm").append(data[0].pm);
			}
		})
	};*/
	
	

	/*$(document).ready(function(){
		initData();
	})*/
})
function initData() {
	$.ajax({
		url : getRootPath() + "/bu/teamInfo",
		type : 'get',
		async: false,//是否异步，true为异步
		data : {
			teamId : str
		},
		success : function(data) {
			$("#name").empty();
			$("#tm").empty();
			
			$("#name").append(data[0].teamName);
			$("#name").attr("title",data[0].teamName);
			$("#tm").append(data[0].pm);
		}
	})
};
var oldtable;
var oldtable2;
var oldtable3;
function tableAdd() {
	var tab = '<tr style="height: 52px;">'
		+'<td date-type="select" select-date="PM,产品经理,SE,MDE,BA,IA,TC,TSE,QA,TL">PM</td>'
		+'<td date-type="input" name="staffname">xxx</td>'
		+'<td date-type="input" name="zrAccount">000000</td><td date-type="input" name="hwAccount"></td>'
		+'<td date-type="select" select-date="未通过,通过,不涉及">未通过</td>'
		+'<td date-type="select" select-date="未通过,通过,不涉及">未通过</td>'
    	+'<td date-type="select" select-date="完全胜任,基本胜任,暂不胜任">完全胜任</td>'
    	+'<td date-type="select" select-date="在岗,后备">在岗</td>'
    	+'<td date-type="no"><div name="del"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td>'
    	+'</tr>';
	$("#guanjianjuese tbody").append(tab);
}
function tableAdd2() {
	var tab = '<tr style="height: 52px;">'
		+'<td date-type="select" select-date="CP1,CP2,CP3,迭代1,迭代2,迭代3,迭代4,迭代5,迭代6">CP1</td>'
		//+'<td date-type="select" select-date="CP,迭代点">CP</td>'
		+'<td date-type="date" name="planDate">2018-01-01</td>'
		+'<td date-type="date" name="actualDate">2018-01-01</td>'
		+'<td date-type="no"   name="deviationRate">0%</td>'
		+'<td date-type="no"><div name="del1"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td></tr>';
	$("#cp-review tbody").append(tab);
}
function tableAdd3() {
	var tab = '<tr style="height: 52px;">'
		+'<td date-type="select" select-date="开发投入,问题单修改投入,版本公共事务投入,测试执行投入,测试用例设计投入,自动化用例写作">开发投入</td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="no"><div name="del3"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td></tr>';
	$("#monthlyManpowerProportion tbody").append(tab);
}
function delToNone(del,display) {
	$("div[name='"+del+"']").each(function(){
		$(this).css('display',display);
	});
}
function tableCancel() {
	$("#guanjianjuese tbody").empty();
	$("#guanjianjuese tbody").append(oldtable);
	delToNone('del','none');
	var edit=$("#edit");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#guanjianjuesedo").css('display','none');
}
function tableCancel2() {
	$("#cp-review tbody").empty();
	$("#cp-review tbody").append(oldtable2);
	delToNone('del1','none');
	var edit=$("#edit2");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#cp-reviewdo").css('display','none');
}
function tableCancel3() {
	$("#monthlyManpowerProportion tbody").empty();
	$("#monthlyManpowerProportion tbody").append(oldtable3);
	delToNone('del3','none');
	var edit=$("#edit3");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#proportionAdjustment").css('display','none');
}

function showPopover(msg) {
	$('#dataAcquisition').text(msg);
	$('#submitImportmodalfooter').css('display','none');
	$('#savetoop').modal('show');
	//2秒后消失提示框
	var id = setTimeout(function () {
		$('#savetoop').modal('hide');
	}, 2000);
}
function showPopoverErr(msg) {
	$('#dataAcquisition').text(msg);
	$('#submitImportmodalfooter').css('display','block');
	$('#savetoop').modal('show');
}
function hidePopover() {
	$('#savetoop').modal('hide');
}
function tableSave() {
	var edit=$("#edit");
	var ret = tableSaveToDao();
	if(ret=="err"){
		showPopoverErr("中软工号不能有重复的，请修改之后再保存");
		return;
	}
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#guanjianjuesedo").css('display','none');
	delToNone('del','none');
	showPopover("关键角色保存成功");
}

function tableSaveToDao() {
	var tab = $('#guanjianjuese');
	var rows = tab[0].rows;
	var allTr = "";
	var zrAccounts=new Array()
	for(var i = 0; i<rows.length; i++ ){
		if(rows[i].cells[1].tagName=="TH"){
			continue;
		}
		zrAccounts[i]=rows[i].cells[2].innerHTML;
		allTr +='{"no":"'+projNo+'",'+
			'"name":"'+rows[i].cells[1].innerHTML+'",'+
			'"zrAccount":"'+rows[i].cells[2].innerHTML+'",'+
			'"hwAccount":"'+rows[i].cells[3].innerHTML+'",'+
			'"position":"'+rows[i].cells[0].innerHTML+'",'+
			'"rdpmExam":"'+rows[i].cells[4].innerHTML+'",'+
			'"replyResults":"'+rows[i].cells[5].innerHTML+'",'+
			'"proCompetence":"'+rows[i].cells[6].innerHTML+'",'+
			'"status":"'+rows[i].cells[7].innerHTML+
			'"},';
	}
	var count = 0;  
	for (var i = 0; i < zrAccounts.length;i++) {  
	    for (var j = 0; j < zrAccounts.length; j++) {
	    	if(i==j){
	    		continue;
	    	}
	        if (zrAccounts[i] == zrAccounts[j]) {  
	            count++;  
	        }  
	    }  
	}
	if(count>0){
		return "err";
	}
	var jsonStr = '{"projRoles":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url : getRootPath() + "/bu/insertInfo",
		type : 'POST',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data : jsonStr,
		success : function(data) {
			
		}
	})
}

function tableSave2() {
	var edit=$("#edit2");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#cp-reviewdo").css('display','none');
	delToNone('del1','none');
	tableSaveToDao2();
	showPopover("里程碑信息保存成功");
}

function tableSaveToDao2() {
	var tab = $('#cp-review');
	var rows = tab[0].rows;
	var allTr = "";
	for(var i = 0; i<rows.length; i++ ){
		if(rows[i].cells[1].tagName=="TH"){
			continue;
		}
		allTr +='{"no":"'+projNo+'",'+
			'"node":"'+rows[i].cells[0].innerHTML+'",'+
			//'"nodeType":"'+rows[i].cells[1].innerHTML+'",'+
			'"planDate":"'+rows[i].cells[1].innerHTML+'",'+
			'"actualDate":"'+rows[i].cells[2].innerHTML+'",'+
			'"deviationRate":"'+rows[i].cells[3].innerHTML+'"'+
			'},';
	}
	var jsonStr = '{"projSchedule":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url : getRootPath() + "/bu/insertInfoProjectSchedule",
		type : 'POST',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data : jsonStr,
		success : function(data) {
			
		}
	})
}

function tableSave3() {
	var edit=$("#edit3");
	var ret = tableSaveToDao3();
	if(ret=="err"){
		showPopoverErr("选择名称不能有重复的，请修改之后再保存");
		return;
	}
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#proportionAdjustment").css('display','none');
	delToNone('del3','none');
	
	showPopover("比例信息保存成功");
}

function isnull2double(str) {
	str = (str==null||str=="")?"0":str;
	str=str.replace("%","");
	var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
	if(!re.test(str)){
		str = "0"
	}
	return str;
}
function isnotnum2num(str) {
	var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
	if(!re.test(str)){
		return "0";
	}
	return str;
}


function tableSaveToDao3() {
	var rows = $('#monthlyManpowerProportion tbody tr');
	var allTr = "";
	var zrAccounts=new Array()
	for(var i = 0; i<rows.length; i++ ){
		if(rows[i].cells[0].innerHTML=="合计"){
			continue;
		}
		zrAccounts[i]=rows[i].cells[0].innerHTML;
		allTr +='{"name":"'+rows[i].cells[0].innerHTML+'",'+
			'"january":"'+isnull2double(rows[i].cells[1].innerHTML)+'",'+
			'"february":"'+isnull2double(rows[i].cells[2].innerHTML)+'",'+
			'"march":"'+isnull2double(rows[i].cells[3].innerHTML)+'",'+
			'"april":"'+isnull2double(rows[i].cells[4].innerHTML)+'",'+
			'"may":"'+isnull2double(rows[i].cells[5].innerHTML)+'",'+
			'"june":"'+isnull2double(rows[i].cells[6].innerHTML)+'",'+
			'"july":"'+isnull2double(rows[i].cells[7].innerHTML)+'",'+
			'"august":"'+isnull2double(rows[i].cells[8].innerHTML)+'",'+
			'"september":"'+isnull2double(rows[i].cells[9].innerHTML)+'",'+
			'"october":"'+isnull2double(rows[i].cells[10].innerHTML)+'",'+
			'"november":"'+isnull2double(rows[i].cells[11].innerHTML)+'",'+
			'"december":"'+isnull2double(rows[i].cells[12].innerHTML)+'"'+
			'},';
	}
	var count = 0;  
	for (var i = 0; i < zrAccounts.length;i++) {  
	    for (var j = 0; j < zrAccounts.length; j++) {
	    	if(i==j){
	    		continue;
	    	}
	        if (zrAccounts[i] == zrAccounts[j]) {  
	            count++;  
	        }  
	    }  
	}
	if(count>0){
		return "err";
	}
	var jsonStr = '{"proNo":"'+projNo+'","manpowers":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url : getRootPath() + "/bu/insertInvestmentProportion",
		type : 'POST',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data : jsonStr,
		success : function(data) {
			
		}
	})
}

function tableEdit(editid,divid) {
	var edit=$("#"+editid);
	edit.css('display','none');
	if(divid=='guanjianjuesedo'){
		oldtable=$("#guanjianjuese tbody").html();
		delToNone('del','block');
	}else if(divid=='proportionAdjustment'){
		oldtable3=$("#monthlyManpowerProportion tbody").html();
		delToNone('del3','block');
	}else{
		oldtable2=$("#cp-review tbody").html();
		delToNone('del1','block');
	}
	if(edit.attr("data_obj")=="1"){
		$("#"+divid).css('display','block'); 
		edit.attr("data_obj","0")
	}
	
}

function editable(td){
	if(td[0].childNodes.length>1){
		return;
	}
	var dateType = td.attr("date-type");
	if(dateType=="input"){
		var text = td.text();
		if(td.attr("name")=="baifenbi"){
			text = isnull2double(text);
		}
		var txt = $("<input class='form-control' style='width: 100%;height: 52px;' type='text'>").val(text);
	}else if(dateType=="date"){
		var text = td.text();
		var txt = $("<input class='form-control' max='9999-12-30' style='width: 100%;height: 52px;' type='date'>").val(text);
	}else if(dateType=="no"){
		return;
	}else if(dateType=="select"){
		var text = td.text();
		var select = "<select class='form-control' style='height: 52px;'>";
		var selectDate = td.attr("select-date").split(',');
		selectDate.forEach(function(i,index){
			select += "<option>"+i+"</option>";
		})
		select += "</select>";
		var txt = $(select).val(text);
	}else{
		var text = td.text();
		var txt = $("<input class='form-control' style='width: 100%;height: 52px;' type='text'>").val(text);
	}
	// 根据表格文本创建文本框 并加入表表中--文本框的样式自己调整
	txt.blur(function(){
		// 失去焦点，保存值。于服务器交互自己再写,最好ajax
		var newText = $(this).val();
		// 移除文本框,显示新值
		$(this).remove();
		if(dateType=="input"){
			if(td.attr("name")=="baifenbi"){
				newText = isnotnum2num(newText);
				//if(newText != "0"){
					var tdssel = $("#monthlyManpowerProportion tbody td:nth-child("+(td[0].cellIndex+1)+")");
					var allbaifenbi = 0.0;
					_.each(tdssel, function(seltd, index){//(值，下标)
						allbaifenbi += parseFloat(isnull2double($(seltd).text()));
					});
					allbaifenbi+=parseFloat(newText);
					if(allbaifenbi>100){
						showPopover("总比例不能大于100%");
						newText="0";
					}else{
						$("#monthlyManpowerProportion tfoot td:nth-child("+(td[0].cellIndex+1)+")").text(allbaifenbi+"%");
					}
				//}
				if(newText==0){
					newText = "";
				}else{
					newText += "%";
				}
			}
		}
		td.text(newText);
		if(dateType=="date"){
			var planDate = td.parents('tr:first').children('td[name="planDate"]').text()
			var actualDate = td.parents('tr:first').children('td[name="actualDate"]').text()
			var deviationRate = td.parents('tr:first').children('td[name="deviationRate"]');
			if(planDate!="" && actualDate!=""){
				var startDay = $("#startDate").text();
	//			var endDay = $("#endDate").text();
				var a = datedifference(planDate,actualDate);
				var b = datedifference(startDay,planDate);
				var num = 100 + "";
				if(b!=0){
					num = (a/b)*100+"";
				}
				if(num.indexOf(".")>0){
					num = num.substring(0,num.indexOf("."));
				}
				
				deviationRate.text(num+"%");
			}else{
				deviationRate.text("");
			}
		}else if(dateType=="input"){
			if(td.attr("name")=="staffname"){
				$("#testDIV").fadeOut();
			}
		}
	});
	td.text("");
	td.append(txt);
	$(txt).focus();
	if(dateType=="input"){
		$(txt).select();
		testDIVdo(td,txt);
	}
}

//$(document).on("dblclick", "#guanjianjuese td", function () {
$(document).on("click", "#guanjianjuese td", function () {
	if($("#edit").attr("data_obj")=="1"){
		return;
	}
    var td = $(this);
    editable(td);
	
});
//$(document).on("dblclick", "#cp-review td", function () {
$(document).on("click", "#cp-review td", function () {
	if($("#edit2").attr("data_obj")=="1"){
		return;
	}
    var td = $(this);
    editable(td);
	
});
$(document).on("click", "#monthlyManpowerProportion tbody td", function () {
	if($("#edit3").attr("data_obj")=="1"){
		return;
	}
	var td = $(this);
	editable(td);
	
});

$(document).on("click", "div[name=del]", function () {
	$(this).parents('tr:first').remove();
});
$(document).on("click", "div[name=del1]", function () {
	$(this).parents('tr:first').remove();
});
$(document).on("click", "div[name=del3]", function () {
	$(this).parents('tr:first').remove();
	for (var i = 2; i <= 13; i++) {
		var tdssel = $("#monthlyManpowerProportion tbody td:nth-child("+i+")");
		var allbaifenbi = 0.0;
		_.each(tdssel, function(seltd, index){//(值，下标)
			allbaifenbi += parseFloat(isnull2double($(seltd).text()));
		});
		if(allbaifenbi>100){
			showPopover("总比例不能大于100%");
			newText="0";
		}else{
			$("#monthlyManpowerProportion tfoot td:nth-child("+i+")").text(allbaifenbi+"%");
		}
	}
});

$(document).on("click", "#testDIVtable td", function () {
	var selected = $(this).text();
	var value = selected.split("\t");
	var num = $('#testDIVtable').attr("num");
	$($('td[name="staffname"]')[num-1]).text(value[0]);
	$($('td[name="hwAccount"]')[num-1]).text(value[1]);
	$($('td[name="zrAccount"]')[num-1]).text(value[2]);
});
function testDIVdo(td,txt) {
	if(td.attr("name")=="staffname"){
		txt.keyup(function() {
			if(txt.val()!=""){
				$.ajax({
					url : getRootPath() + "/bu/getZRaccountByName",
					type : 'POST',
					dataType: "json",
					data : {
						name:txt.val()
					},
					success : function(data) {
						$("#testDIV").empty();
						var select_option="<table id='testDIVtable' num="+td[0].parentNode.rowIndex+" class='mousetable' style='font-size: 14px;border-spacing: 0px;'>";
						_.each(data, function(val, index){//(值，下标)
							var zraccount="";
							if(val.zrAccount&&val.zrAccount.length>6){
								zraccount = val.zrAccount.substring(val.zrAccount.length-6,val.zrAccount.length)
							}
							var value = val.name+"\t"+val.account+"\t"+zraccount;
							select_option += "<tr><td style='width: 160px'>"+value+"</td></tr>";
						})
						select_option +="</table>"
	                    $("#testDIV").append(select_option);
					}
				})
				var offset = txt.offset();
	        	$("#testDIV").css('left', offset.left + 'px')
	                     .css('top', offset.top+txt[0].clientHeight + 'px')
	                     .css('position', 'absolute')
	                     .css('background', '#ffffff')
	                     .css('z-index', '99')
	                     .fadeIn();
			}
//	        $("#testDIV").mouseleave(function() {  
//	        			//置空div  
//	        	$("#testDIV").empty(); 
//	        	$("#testDIV").fadeOut();
//	        });
        });
    	//回车事件  
//    	var tdIndex = 1;
        $(window).keyup(function(event) {
			
			switch (event.keyCode) {  
			      // ...   
				  // 不同的按键可以做不同的事情   
				  // 不同的浏览器的keycode不同   
//			case 38://上
//				_.each($("#testDIVtable td"), function(val, index){//(值，下标)
//					val.style.backgroundColor='#FFFFFF';
//				});
//				$($("#testDIVtable td")[tdIndex-1]).css('background', '#d9dada');
//				if(tdIndex<=1){
//					tdIndex = 1;
//				}else{
//					tdIndex -= 1;
//				}
//				break;  
//			case 40://下
//				var max = $("#testDIVtable td").length;
//				_.each($("#testDIVtable td"), function(val, index){//(值，下标)
//					val.style.backgroundColor='#FFFFFF';
//				});
//				$($("#testDIVtable td")[tdIndex-1]).selected;
//				if(tdIndex>=max){
//					tdIndex = max;
//				}else{
//					tdIndex += 1;
//				}
//				break;  
			case 13://回车
//				var selected = $($("#testDIVtable td")[tdIndex-1]).text();
//				var value = selected.split("\t");
//				var num = $('#testDIVtable').attr("num");
//				$($('td[name="staffname"]')[num-1]).text(value[0]);
//				$($('td[name="hwAccount"]')[num-1]).text(value[1]);
				$("#testDIV").fadeOut();
				txt.blur();
				break;  
			}  
        });
		
	}
}


$(function(){
	var data=null;
	function loadGridData(init, sort, order, number, size){
		$('#savetoop').modal('show');
        var url = getRootPath() + "/projects/page?";
        if (sort) {
            url += "sort=" + sort + "&order=" + order;
        }
        if (size) {
            url += "pageSize=" + size + "&pageNumber=" + number;
        }
		   $.ajax({
		        url: url,
		        type: 'GET',
		        async: false,//是否异步，true为异步
		        data: {
                    'teamId':str,
		        	/*'client': $("#zrOrhwselect option:selected").val(),
		        	'name': encodeURI($("#projName").val()),
		        	'pm': encodeURI($("#projMgmr").val()),
		        	'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
		        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
					'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
					'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
					'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
        			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
        			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
*/        			'projectState': encodeURI($("#usertype2").selectpicker("val")==null?null:$("#usertype2").selectpicker("val").join())//转换为字符串
		        },
		        success: function(data){
		        	var titleUnit = {};
		            var options = {
		                rownumbers: false,
		                striped: true,
		                pagination : true,
		                nowrap: true,
						pagePosition : 'bottom',
						//onSortColumn: function(field, order){
						//	loadGridData(false, field, order);
						//},
			            onLoadSuccess:function(data){   
			                $('#projSummaryTable').datagrid('doCellTip',{cls:{},delay:1000, titleUnit:titleUnit, headerOverStyle:true});   
			            }  
		            };
		            columns:[[
		                {
		                    width:150, //当 fitColumns:true时，columns里的width变为改列宽度占表格总宽度大小的比例
		                },          
		            ]];
		            titleUnit = 'title';

                    var titles = [
                    	{
                            "field": "no",
                            "title": "项目编号"
                        },
                        {
                            "field": "name",
                            "title": "项目名称"
                        },
                        {
                            "field": "pm",
                            "title": "项目经理"
                        },
                        {
                            "field": "area",
                            "title": "地域"
                        },
                        {
                            "field": "hwpdu",
                            "title": "华为产品线"
                        },
                        {
                            "field": "hwzpdu",
                            "title": "子产品线"
                        },
                        {
                            "field": "pduSpdt",
                            "title": "PDU/SPDT"
                        },
                        {
                            "field": "projectState",
                            "title": "项目状态"
                        },
                        {
                        	"field": "startStr",
                        	"title": "项目启动时间"
                        },
                        {
                        	"field": "endStr",
                        	"title": "项目结项时间"
                        }  
                    ];

                    var wdth = Math.round(100 / (titles.length + 2));
                    var lastWd = wdth * (titles.length + 2);
                   
		            lastWd = 100 > lastWd ? (wdth + 100 - lastWd) : ( wdth - lastWd + 100);
		            _.each(titles, function(val, index){
		            	val.sortable = false;
		            	val.width = wdth + '%';
		            	val.height = '32px';
		            	val.align= 'left';
		            	if(val.title == "项目名称"){
                            val.width = wdth * 3.6 + '%';
		            		val.formatter = function(val, row){
		            			var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
		            			return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path1+'">'+val+'</a>';
		            			//return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="' +getRootPath()+ '/bu/projView?projNo=' + row['项目编码'] + '&a='+Math.random()+'">'+val+'</a>';
		            		};
		            	}
		            	
		            	//no悬浮显示
		            	if(val.title == "项目编号"){
                            val.width = wdth * 1.7 + '%';
		            		val.formatter = function(val, row){
		            			return '<span title="'+val +'" >'+val+'</span>';
		            			//return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="' +getRootPath()+ '/bu/projView?projNo=' + row['项目编码'] + '&a='+Math.random()+'">'+val+'</a>';
		            		};
		            	}
		            	//列宽调整
		            	if(val.title == "项目经理" || val.title == "地域" || val.title == "项目状态"){
                            val.width = wdth * 0.7 + '%';
		            	}
		            	
		            	if(val.title == "子产品线"){
                            val.width = wdth * 1.1 + '%';
		            	}
		            	
		            	if(val.title == "PDU/SPDT"){
                            val.width = wdth * 1.2 + '%';
		            	}
		            	
		            	if(val.title == "项目启动时间" || val.title == "项目结项时间"){
                            val.width = wdth * 0.9 + '%';
		            	}
		            });

                    titles[titles.length -1].width = lastWd  + '%';
		            options.columns = [titles];
                    options.pageNumber = parseInt(data.pageNumber)+1;

		            $('#projSummaryTable').datagrid(options);

                    var gridDatas = {};
                    $.extend(true, gridDatas, {"rows": data.data, "total": data.totalCount});
                    $('#projSummaryTable').datagrid("loadData", gridDatas);

					var p = $('#projSummaryTable').datagrid('getPager');
					$(p).pagination({
						pageSize : data.pageSize,// 每页显示的记录条数，默认为10
                        //current_page : parseInt(data.pageNumber)+1,
						pageList : [10,20,30],// 可以设置每页记录条数的列表
						beforePageText : '第',// 页数文本框前显示的汉字
						afterPageText : '页    共 {pages} 页',
						displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
                        //pageNumber: parseInt(data.pageNumber)+1,
                        onSelectPage: function(pageNumber, pageSize){
                            loadGridData(false, null, null, pageNumber-1, pageSize);
                        },
					});
                    $(p).pagination('refresh', {pageNumber: parseInt(data.pageNumber) + 1});

		            if(data.totalCount==0){
		            	showPopover("未查询到数据!");
		            }else{
		            	$('#savetoop').modal('hide');
		            }
		        }
		    })
	}

    function setCookie(name, value, time) {
        var exp = new Date();
        exp.setTime(exp.getTime() + time * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    }
    
    function bindQueryEvent() {
        $("#queryBtn").click(function () {
            setCookie("name", $("#projName").val(), 60);
            /*setCookie("pm", $("#projMgmr").val(), 60);
            setCookie("area", $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(), 60);
            setCookie("hwpdu", $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(), 60);
            setCookie("hwzpdu", $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(), 60);
            setCookie("pduSpdt", $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(), 60);
            setCookie("bu", $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(), 60);
            setCookie("pdu", $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(), 60);
            setCookie("du", $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(), 60);*/

            loadGridData(false);
        })
    };
    bindQueryEvent();
    /*$(document).on("change", "#zrOrhwselect", function () {
        zrOrhwselectChenge();
	});
	function zrOrhwselectChenge(){
		var flag = $("#zrOrhwselect option:selected").val();
		if(flag=="0"){
			$("#hwselect").css("display","block");
			$("#zrselect").css("display","none");
			$('#usertype6').selectpicker('val', '');
			$('#usertype7').selectpicker('val', '');
			$('#usertype8').selectpicker('val', '');
		}else{
			$("#hwselect").css("display","none");
			$("#zrselect").css("display","block");
			$('#usertype3').selectpicker('val', '');
			$('#usertype4').selectpicker('val', '');
			$('#usertype5').selectpicker('val', '');
		}
	};*/


	/*$(document).ready(function(){
		$('#usertype2').selectpicker("val",["在行"]);
		var zrOrhwselect = getCookie("zrOrhwselect");
        $('#zrOrhwselect')[0].value=zrOrhwselect;
		zrOrhwselectChenge();

		bindQueryEvent();

		$('#queryBtn').click();
		showDateTime();
		$('.modal-backdrop').addClass("disabled");
        
        initData();
	})*/
})

function datedifference(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式  
    var dateSpan,
        tempDate,
        iDays;
    sDate1 = Date.parse(sDate1);
    sDate2 = Date.parse(sDate2);
    dateSpan = sDate2 - sDate1;
    dateSpan = Math.abs(dateSpan);
    iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
    if(sDate1>sDate2){
    	return iDays*(-1);
    }
    return iDays
};

function tableHeight() {
//	return window.innerHeight - 30;
	return $(window).height() - 30;
}
//获取下拉列表的值
function getSelectValue(type,isOptin,id,name){
	var s="";
	$.ajax({
		url:getRootPath() + '/codeMaster/getCodeMasterOrder',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{
			"codekey":type
		},
		success:function(data){
			if(data.code == 'success'){
				s= data.data;
				if("通过"==isOptin){
//					$(id).append('<option label="'+"请选择"+name+'" value="">'+"请选择"+name+'</option>');
					$(id).append('<option value="">'+"请选择"+name+'</option>');
					for(j = 0; j < s.length; j++) {
						$(id).append('<option value="'+data.data[j].value+'" text="'+data.data[j].text+'">'+data.data[j].text+'</option>');
					}
				}
			}else{
				toastr.error('加载失败!');
			}
		}
	});
	return s;
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
//日期前后顺序比较
function compareDate(o1,o2){
	o1= new Date(Date.parse(o1));
	o2=new Date(Date.parse(o2));
	if(o1>o2){
		return true;
	}
	return false;
}

function addDate(time) {
    var timestamp = Date.parse(new Date(time));
    timestamp = timestamp /1000;
    timestamp += 86400;//加一天
    var newTime =new Date(timestamp * 1000).format('yyyy-MM-dd');
    return newTime;
}

/*----------------------流程指标配置继承--------------------------*/
function esc(a){
	if ( event.keyCode=='27' ){//->右箭头
		this.x(a);
		this.x2(a);
		this.x3(a);
		this.x4(a);
	} 

}
//自动/手动录入
function collect(v){
	var type= "";
	if($("#auto"+v).css("background-color") == "rgb(26, 220, 33)"){
		$("#auto"+v).css("background-color","#fff"); 
		$("#manual"+v).css("background-color","#f7b547");
		$("#m"+v).show();
		$("#a"+v).hide();
		type= 1;
	}else{
		$("#auto"+v).css("background-color","#1adc21");
		$("#manual"+v).css("background-color","#fff");
		$("#a"+v).show();
		$("#m"+v).hide();
		type= 0;
	}
	$.ajax({
		type: "post",
		url: getRootPath()+"/projectlable/updateByTeamId",
		dataType: "json",
		async: false,
		contentType : 'application/json;charset=utf-8', 
		data: JSON.stringify({
			teamId:str,
			mesureId:v,
			upper:$("#ab"+v).val(),
			target:$("#d"+v).val(),
			lower:$("#f"+v).val(),
			collectType:type,
		}),
		success: function (data) {
			console.log(data);
		}
	});
}

function input(v){
	//浮现
	var c = document.getElementById(v);
	c.style.border = "1px solid #B5B5B5;";
	var a = document.getElementById("q"+v);
    a.style.display = "block";
    var obj = document.getElementById("a"+v);
    obj.focus();
    var len = obj.value.length;
    if (document.selection) {
    var sel = obj.createTextRange();
    sel.moveStart('character', len);
    sel.collapse();
    sel.select();
    } else if (typeof obj.selectionStart == 'number'
    && typeof obj.selectionEnd == 'number') {
    obj.selectionStart = obj.selectionEnd = len;
    }
    document.getElementById("all"+v).style.display = "block";//遮盖层
    $("#"+v)[0].style.border="2px solid #81D2FA";
}

function x(a){
	//隐藏
	var b = document.getElementById("qb"+a);
    b.style.display = "none";
    document.getElementById("allb"+a).style.display = "none";
    //$("#ab"+a).val("");
    $("#b"+a)[0].style.border="0px";
}

function x2(a){
	var b = document.getElementById("qd"+a);
    b.style.display = "none";
    document.getElementById("alld"+a).style.display = "none";
    //$("#ad"+a).val("");//清空
    $("#d"+a)[0].style.border="0px";
}

function x3(a){
	var b = document.getElementById("qf"+a);
    b.style.display = "none";
    document.getElementById("allf"+a).style.display = "none";
    //$("#af"+a).val("");
    $("#f"+a)[0].style.border="0px";
}

function x4(a){
	var b = document.getElementById("qh"+a);
    b.style.display = "none";
    document.getElementById("allh"+a).style.display = "none";
    //$("#ad"+a).val("");//清空
    $("#h"+a)[0].style.border="0px";
}

function saves(val){
	//上限值保存
	var type= "";
	if($("#auto"+val).css("background-color") == "rgb(26, 220, 33)"){
		type= 0;
	}else{
		type= 1;
	}
	$("#b"+val)[0].style.border="0px";
	var a = $("#ab"+val).val();
	var b = document.getElementById("qb"+val);
    b.style.display = "none";
    document.getElementById("allb"+val).style.display = "none";
	var reg = /^([>≥=<≤])?\d+(\.\d{0,2})?$/;
	if(!a){
		$.ajax({
            type: "post",
            url: getRootPath()+"/projectlable/updateByTeamId",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				teamId:str,
				mesureId:val,
				upper:$("#ab"+val).attr('placeholder'),
				target:$("#d"+val).val(),
				lower:$("#f"+val).val(),
				collectType:type,
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
    	document.getElementById("b"+val).value=document.getElementById("ab"+val).placeholder;
    	$("#b"+val)[0].style.color="red";
	}
	else if(!reg.test(a)){     
        toastr.warning('输入错误');
        $("#ab"+val).val("");
    }
    else{
    	document.getElementById("b"+val).value=document.getElementById("ab"+val).value;
    	$.ajax({
            type: "post",
            url: getRootPath()+"/projectlable/updateByTeamId",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				teamId:str,
				mesureId:val,
				upper:$("#ab"+val).val(),
				target:$("#d"+val).val(),
				lower:$("#f"+val).val(),
				collectType:type,
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
    	$("#b"+val)[0].style.color="red";
    }
};

function save3(val){
	//下限值保存
	var type= "";
	if($("#auto"+val).css("background-color") == "rgb(26, 220, 33)"){
		type= 0;
	}else{
		type= 1;
	}
	$("#f"+val)[0].style.border="0px";
	var e = $("#af"+val).val();
	var b = document.getElementById("qf"+val);
    b.style.display = "none";
    document.getElementById("allf"+val).style.display = "none";
    var reg = /^([>≥=<≤])?\d+(\.\d{0,2})?$/;
	if(!e){
		$.ajax({
            type: "post",
            url: getRootPath()+"/projectlable/updateByTeamId",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				teamId:str,
				mesureId:val,
				upper:$("#b"+val).val(),
				target:$("#d"+val).val(),
				lower:$("#af"+val).attr('placeholder'),
				collectType:type,
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
    	document.getElementById("f"+val).value=document.getElementById("af"+val).placeholder;
    	$("#f"+val)[0].style.color="red";
	}
	else if(!reg.test(e)){     
        toastr.warning('输入错误');
        $("#af"+val).val("");
    }else{
    	document.getElementById("f"+val).value=document.getElementById("af"+val).value;
    	$.ajax({
            type: "post",
            url: getRootPath()+"/projectlable/updateByTeamId",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				teamId:str,
				mesureId:val,
				upper:$("#b"+val).val(),
				target:$("#d"+val).val(),
				lower:$("#af"+val).val(),
				collectType:type,
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
    	$("#f"+val)[0].style.color="red";
    }
};

function save2(val){
	//目标值保存
	var type= "";
	if($("#auto"+val).css("background-color") == "rgb(26, 220, 33)"){
		type= 0;
	}else{
		type= 1;
	}
	$("#d"+val)[0].style.border="0px";
	var c = $("#ad"+val).val();
	var b = document.getElementById("qd"+val);
    b.style.display = "none";
    document.getElementById("alld"+val).style.display = "none";
    var reg = /^([>≥=<≤])?\d+(\.\d{0,2})?$/;
	if(!c){
		$.ajax({
            type: "post",
            url: getRootPath()+"/projectlable/updateByTeamId",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				teamId:str,
				mesureId:val,
				upper:$("#b"+val).val(),
				target:$("#ad"+val).attr('placeholder'),
				lower:$("#f"+val).val(),
				collectType:type,
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
    	document.getElementById("d"+val).value=document.getElementById("ad"+val).placeholder;
    	$("#d"+val)[0].style.color="red";
	}
    else if(!reg.test(c)){     
        toastr.warning('输入错误');
        $("#ad"+val).val("");
    }else{
    	document.getElementById("d"+val).value=document.getElementById("ad"+val).value;
    	$.ajax({
            type: "post",
            url: getRootPath()+"/projectlable/updateByTeamId",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				teamId:str,
				mesureId:val,
				upper:$("#b"+val).val(),
				target:$("#ad"+val).val(),
				lower:$("#f"+val).val(),
				collectType:type,
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
    	$("#d"+val)[0].style.color="red";
    }
};

function save4(val){
	//目标值保存
	var type= "";
	if($("#auto"+val).css("background-color") == "rgb(26, 220, 33)"){
		type= 0;
	}else{
		type= 1;
	}
	$("#h"+val)[0].style.border="0px";
	var c = $("#ah"+val).val();
	var b = document.getElementById("qh"+val);
    b.style.display = "none";
    document.getElementById("allh"+val).style.display = "none";
    var reg = /^([>≥=<≤])?\d+(\.\d{0,2})?$/;
	if(!c){
		$.ajax({
            type: "post",
            url: getRootPath()+"/projectlable/updateByTeamId",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				teamId:str,
				mesureId:val,
				upper:$("#b"+val).val(),
				target:$("#ad"+val).attr('placeholder'),
				lower:$("#f"+val).val(),
				challenge:$("#ah"+val).val(),
				collectType:type,
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
    	document.getElementById("h"+val).value=document.getElementById("ah"+val).placeholder;
    	$("#h"+val)[0].style.color="red";
	}
    else if(!reg.test(c)){     
        toastr.warning('输入错误');
        $("#ah"+val).val("");
    }else{
    	document.getElementById("h"+val).value=document.getElementById("ah"+val).value;
    	$.ajax({
            type: "post",
            url: getRootPath()+"/projectlable/updateByTeamId",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				teamId:str,
				mesureId:val,
				upper:$("#b"+val).val(),
				target:$("#ad"+val).val(),
				lower:$("#f"+val).val(),
				challenge:$("#ah"+val).val(),
				collectType:type,
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
    	$("#h"+val)[0].style.color="red";
    }
};

function restore(){
	
	document.getElementById("b"+b).value = d;
}

function save(){
	//刷新sel，传向后台
	$(document).on("click","#saveLabelMeasure",function(){
		var labs = '';
		var option = $("#labelSel option:selected");
		var text = option.val();
		 $.ajax({
			url : getRootPath() + '/projectlable/queryMeasureConfigByTeamId',
			type : 'post',
			async: false,
			data : {
				teamId : str,
				labs : text
			},
			success : function(data){
				if(data.code == 'fail'){
                    Ewin.confirm({ message: "该操作将会清空旧模板配置，确认操作吗？" }).on(function (e) {
                        if (!e) {
                            return;
                        } else {
                            $.ajax({
                                url: getRootPath() + '/projectlable/deleteMeasureConfigByTeamId',
                                type: 'post',
                                async: false,
                                dataType: "json",
                                data : {
                                	teamId : str,
                                    labs : text
                                },
                                success: function (data) {
                                    if(data.code == 'success'){
                                        saveConfig();
                                    }else{
                                        toastr.error('指标配置异常!');
                                    }
                                }
                            });
                        }
                    });
				}else{
                    saveConfig();
				}
			}
		 });
		
	});
};

function saveConfig(){
    _.each(sel.data, function(label, index){
        label.isSelect=$('#label-'+label.id).attr("check");
        _.each(label.measureCatList, function(wd, index){
            wd.isSelect=$('#wd-'+wd.measureCategory).attr("check");
            _.each(wd.measureList, function(measure, index){
                measure.isSelect=$('#measure-'+measure.measureId).attr("check");
            })
        })
    });
    $.ajax({
        url: getRootPath() + '/projectlable/updateMeasureConfigByTeamId',
        type: 'post',
        async: false,
        dataType: "json",
        contentType : 'application/json;charset=utf-8', //设置请求头信息
        data: JSON.stringify({
            teamId: str,
            teamLabelMeasures: sel.data
        }),
        success: function (data) {
            if(data.code == 'success'){
                toastr.success('指标配置成功！');
                $("#labelMeasure").modal('hide');
                location.reload();
                parent.location.reload();
            }else{
                toastr.error('指标配置异常!');
            }
        }
    });
}


function yesOrNo(){
	//点击流程全选
	$(document).on("click","a[id^='label']",function(){
		var cur=$(this);
		var check = curChange(cur);
		var labelId = cur.attr("id");
		var wds = $('a[fatherId="'+labelId+'"]');
		_.each(wds, function(wd, index){
			childrenChange($(wd),check);
			var measureId = $(wd).attr("id");
			var measures = $('a[fatherId="'+measureId+'"]');
			_.each(measures, function(measure, index){
				childrenChange($(measure),check);
			});
		});
	})
	//点击分类指标全选，流程颜色判断
	$(document).on("click","a[id^='wd']",function(){
		var cur=$(this);
		var check = curChange(cur);
		var wdId = cur.attr("id");
		var measures = $('a[fatherId="'+wdId+'"]');
		_.each(measures, function(measure, index){
			childrenChange($(measure),check);
		});
		var labelId = cur.attr("fatherId");
		var wds = $('a[fatherId="'+labelId+'"]');
		var num = 0;
		_.each(wds, function(wd, index){
			if($(wd).attr("check")==1){
				num += 1;
			}
		});
		var labels = $('a[id="'+labelId+'"]');
		if(num==0){
			childrenChange($(labels[0]),0);
		}else if (num==wds.length){
			childrenChange($(labels[0]),1);
		}else{
			childrenChange($(labels[0]),2);
		}
	})
	//点击指标分类颜色判断，流程颜色判断
	$(document).on("click","a[id^='measure']",function(){
		var cur=$(this);
		var check = curChange(cur);
		var wdId = cur.attr("id");
		var measures = $('a[fatherId="'+wdId+'"]');
		
		var wdId = cur.attr("fatherId");
		var measures = $('a[fatherId="'+wdId+'"]');
		var num = 0;
		_.each(measures, function(measure, index){
			if($(measure).attr("check")==1){
				num += 1;
			}
		});
		var measure_wds = $('a[id="'+wdId+'"]');
		if(num==0){
			childrenChange($(measure_wds[0]),0);
		}else if (num==measures.length){
			childrenChange($(measure_wds[0]),1);
		}else{
			childrenChange($(measure_wds[0]),2);
		}
		
		var labelId = $(measure_wds[0]).attr("fatherId");
		var wds = $('a[fatherId="'+labelId+'"]');
		var num = 0;
		var allnum = 0;
		_.each(wds, function(wd, index){
			var wdId = $(wd).attr("id");
			var measures = $('a[fatherId="'+wdId+'"]');
			allnum+=measures.length;
			_.each(measures, function(measure, index){
				if($(measure).attr("check")==1){
					num += 1;
				}
			});
		});
		var labels = $('a[id="'+labelId+'"]');
		if(num==0){
			childrenChange($(labels[0]),0);
		}else if (num==allnum){
			childrenChange($(labels[0]),1);
		}else{
			childrenChange($(labels[0]),2);
		}
	})
}

//a[id^='label']
function curChange(cur){
	var url=getRootPath()+ "/view/HTML/images/";
	if(cur.attr("check")==0){
		cur.attr("check",1);
		cur.children().attr("src",url+"yesicon.png");
		return 1;
	}else if(cur.attr("check")==1){
		cur.attr("check",0);
		cur.children().attr("src",url+"noicon.png");
		return 0;
	}
}

//a[id^='label']
function childrenChange(cur,check){
	var url=getRootPath()+ "/view/HTML/images/";
	if(check==0){
		cur.attr("check",0);
		cur.children().attr("src",url+"noicon.png");
	}else if(check==1){
		cur.attr("check",1);
		cur.children().attr("src",url+"yesicon.png");
	}else if (check==2){
		cur.attr("check",1);
		cur.children().attr("src",url+"someicon.png");
	}
}

function pushOrPull(){
	$(document).on("click",".zksq",function(){
		var label=$(this).parent().parent().parent().parent().parent().next();//table/tbody/tr/th
		if($(this).children().html()=="收起"){
			label.css("display","none");	
			$(this).children().html("展开");
		}else if($(this).children().html()=="展开"){
			label.css("display","");	
			$(this).children().html("收起");				
		}
	});
}

function getAllLabCategory(){
	var select = "业务线通用";
	$.ajax({
		url: getRootPath() + '/projectlable/getAllLabCategoryByTeamId',
		type: 'post',
		async: false,//是否异步，true为异步
		data : {
			teamId : str
		},
		success: function (data) {
			var labelSel="";
			_.each(data.data, function(lab, index){
				if(lab=="selected"){
					labelSel=labelSel+"<option selected='selected'>" +index+ "</option>";
					select = index;
				}else{
					labelSel=labelSel+"<option>" +index+ "</option>";
				}
			});
			$("#labelSel").html(labelSel);
		}
	});
	return select;
}

//类目选择事件
function cateGoryChange(){
	var options=$("#labelSel option:selected");
	var text = options.val();
	labelMeasure(text);
}

function labelMeasure(text){
	$.ajax({
		url: getRootPath() + '/projectlable/getMeasureConfigPageInfoByTeamId',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			teamId : str,
			selCategory: text,
			version: "",
			ite: "" 
		},
		success: function (data) {
			sel=data;
			var divLabel="";
			_.each(data.data, function(label, index){
				/*if(label.measureCatList.length==0){
					return true;//相当于continue
				}*/
				var url=getRootPath()+ "/view/HTML/images/";
				//判断流程颜色
				var isNot=0;
				var len=0;
				_.each(label.measureCatList, function(is, index){
					len+=is.measureList.length;
					_.each(is.measureList, function(os, index){
						if(os.isSelect==1){
							isNot+=1;
						}
					})
				})
				if(isNot==0 && label.isSelect == 0){
						url=url+"noicon.png";
				}else if(isNot==len){
					url=url+"yesicon.png";
				}else {
					url=url+"someicon.png";
				}	
				
				divLabel=divLabel+
						 "<div style='background-color:#e8f2ff; border:1px #2e8afc solid; margin-top:5px;'>" +
							"<table style='width: 100%;'>" +
								"<tr height='30px'>" +
					        		"<th width='10%' style='text-align:center;'><a class='yesOrNo' id='label-" +label.id+ "' check='" +label.isSelect+ "'><img src='" +url+ "' /></a></th>" +
					        		"<th width='10%' style='text-align:center;font-family: Microsoft Yahei;font-size: 12px'>" + label.labelName + "</th>" +
					        		"<th colspan='2' style='text-align:center;'><a class='zksq' style='color:#3B91FE;font-family: Microsoft Yahei;font-size: 12px'><div style='padding-left:420px;'>收起</div></a></th>" +
								"</tr>" +
							"</table>" +
						 "</div>" +
							"<table border='1' style='border-top:0px; width: 100%; border-collapse:collapse;'>" +
								"<tr height='26px' style='color: #999999;font-family: Microsoft Yahei;font-size: 12px'>" +
							 		"<td colspan='2' style='text-align:center;'>指标分类</td>" +
							 		"<td colspan='2' style='text-align:center;'>指标名称</td>" +
							 		"<td colspan='1' style='text-align:center;'>自动/手动</td>"+
							 		"<td colspan='1' style='text-align:center;'>上限值</td>"+
							 		"<td colspan='1' style='text-align:center;'>挑战值</td>"+
							 		"<td colspan='1' style='text-align:center;'>目标值</td>"+
							 		"<td colspan='1' style='text-align:center;'>下限值</td>"+
							 		"<td colspan='1' style='text-align:center;'>单位</td>"+
							 	"</tr>";
				_.each(label.measureCatList, function(wd, index){
					/*if(wd.measureList.length==0){
						return true;//相当于continue
					}*/
					url=getRootPath()+ "/view/HTML/images/";
					//判断分类颜色
					isNot=0;
					_.each(wd.measureList, function(is, index){
						if(is.isSelect==1){
							isNot+=1;
						}
					})
					if(isNot==0){
						url=url+"noicon.png";
					}else if(isNot==wd.measureList.length){
						url=url+"yesicon.png";
					}else{
						url=url+"someicon.png";
					}
					
					var d=wd.labelId;
					divLabel=divLabel+
			        		 "<tr>" +
			        		 	"<td width='5%'style='text-align:center;' rowspan='" +wd.measureList.length+ "'><a class='yesOrNo' id='wd-" +wd.measureCategory+wd.labelId+ "' fatherId='label-" +wd.labelId+ "' check='" +wd.isSelect+ "'><img src='" +url+ "' /></a></td>" +
			        		 	"<td width='20%' style='font-family: Microsoft Yahei;font-size:12px;text-align:center;' rowspan='" +wd.measureList.length+ "'>" +wd.measureCategory + "</td>";
					_.each(wd.measureList, function(measure, index){
						url=getRootPath()+ "/view/HTML/images/";
						if(measure.isSelect==1){
							url=url+"yesicon.png";
						}else if(measure.isSelect==0){
							url=url+"noicon.png";
						}
						if(index==0){
							divLabel=divLabel+
							
								"<td width='5%' style='text-align:center;'><a class='yesOrNo' id='measure-" +measure.measureId+ "' fatherId='wd-" +measure.measureCategory+d+ "' check='" +measure.isSelect+ "'><img src='" +url+ "' /></a></td>" +
								"<td width='20%' height='28px'style='font-family: Microsoft Yahei;font-size:12px;text-align:center;'>" +measure.measureName + "</td>" ;
							
						}else{
							divLabel=divLabel+
							
								 	"<td width='5%' style='text-align:center;' ><a class='yesOrNo' id='measure-" +measure.measureId+ "' fatherId='wd-" +measure.measureCategory+d+ "' check='" +measure.isSelect+ "'><img src='" +url+ "' /></a></td>" +
								 	"<td height='28px' width='24%' style='font-family: Microsoft Yahei;font-size:12px;text-align:center;'>" +measure.measureName + "</td>" ;
							
						}
						var type = "";
						if("自动采集" == measure.collectType){
							type = "<div onclick='collect("+measure.measureId+")' style='border: 1px;border-radius: 3px;margin:0 auto;width:60px;height:18px;cursor:pointer;'>" +
							   "<div id='auto"+measure.measureId+"' style='float:left;background-color: #1adc21;border: 1px solid #B5B5B5;border-top-left-radius: 2px;border-bottom-left-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
							   "<span id='a"+measure.measureId+"' style='color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>自动</span></div>" +
							   "<div id='manual"+measure.measureId+"' style='float:left;background-color: #fff;border: 1px solid #B5B5B5;border-top-right-radius: 2px;border-bottom-right-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
							   "<span id='m"+measure.measureId+"' style='display:none;color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>手动</span></div></div>";
						}else if("手工录入" == measure.collectType && ids.indexOf(measure.measureId) != -1){//33个含过程指标，不能修改
							type = "<div style='border: 1px;border-radius: 3px;margin:0 auto;width:60px;height:18px;'>" +
							"<div id='auto"+measure.measureId+"' style='float:left;background-color: #fff;border: 1px solid #B5B5B5;border-top-left-radius: 2px;border-bottom-left-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
							"<span id='a"+measure.measureId+"' style='display:none;color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>自动</span></div>" +
							"<div id='manual"+measure.measureId+"' style='float:left;background-color: #B5B5B5;border: 1px solid #B5B5B5;border-top-right-radius: 2px;border-bottom-right-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
							"<span id='m"+measure.measureId+"' style='color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>手动</span></div></div>";
						}else if("手工录入" == measure.collectType && ids.indexOf(measure.measureId) == -1){
							type = "<div onclick='collect("+measure.measureId+")' style='border: 1px;border-radius: 3px;margin:0 auto;width:60px;height:18px;cursor:pointer;'>" +
							"<div id='auto"+measure.measureId+"' style='float:left;background-color: #fff;border: 1px solid #B5B5B5;border-top-left-radius: 2px;border-bottom-left-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
							"<span id='a"+measure.measureId+"' style='display:none;color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>自动</span></div>" +
							"<div id='manual"+measure.measureId+"' style='float:left;background-color: #f7b547;border: 1px solid #B5B5B5;border-top-right-radius: 2px;border-bottom-right-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
							"<span id='m"+measure.measureId+"' style='color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>手动</span></div></div>";
						}
						divLabel=divLabel+
						"<td style='text-align:center;width='8%'>" +type+ "</td>"+

						"<td width='8%' style='text-align:center;'> <input id='b"+measure.measureId+"' style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:55px;text-align:center;' onclick='input(this.id)' type='text' value='"+measure.upper+"' >" +
						"<div id='allb"+measure.measureId+"' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>" +
						"<div id='qb"+measure.measureId+"' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>" +
						"<span id='p"+measure.measureId+"' style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#F8F8F8;width:100%;height:35px;line-height:35px;display:block;'>上限值</span>" +
						"<input type='text' id='ab"+measure.measureId+"' onkeydown='esc("+measure.measureId+")' style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:150px;float:left;'   value='"+measure.upper+"'>" +
						"<button onclick='saves("+measure.measureId+")' type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>" +
						"<button onclick='x("+measure.measureId+")' style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button></div></td>" +

						"<td style='text-align:center;' width='8%'> <input id='h"+measure.measureId+"' style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:55px;text-align:center;' onclick='input(this.id)' type='text' value='"+measure.challenge+"' >" +
						"<div id='allh"+measure.measureId+"' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>" +
						"<div id='qh"+measure.measureId+"' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>" +
						"<span style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>挑战值</span>" +
						"<input type='text' id='ah"+measure.measureId+"' onkeydown='esc("+measure.measureId+")' style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:150px;float:left;'   value='"+measure.challenge+"'>" +
						"<button onclick='save4("+measure.measureId+")' type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>" +
						"<button onclick='x4("+measure.measureId+")' style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button></div></td>" +

						"<td style='text-align:center;' width='8%'> <input id='d"+measure.measureId+"' style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:55px;text-align:center;' onclick='input(this.id)' type='text' value='"+measure.target+"' >" +
						"<div id='alld"+measure.measureId+"' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>" +
						"<div id='qd"+measure.measureId+"' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>" +
						"<span style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>目标值</span>" +
						"<input type='text' id='ad"+measure.measureId+"' onkeydown='esc("+measure.measureId+")' style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:150px;float:left;'   value='"+measure.target+"'>" +
						"<button onclick='save2("+measure.measureId+")' type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>" +
						"<button onclick='x2("+measure.measureId+")' style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button></div></td>" +
						
						"<td style='text-align:center;width='8%'> <input id='f"+measure.measureId+"' style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:55px;text-align:center;' onclick='input(this.id)' type='text' value='"+measure.lower+"' >" +
						"<div id='allf"+measure.measureId+"' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>" +
						"<div id='qf"+measure.measureId+"' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 1px solid #CACACA;'>" +
						"<span style='border-top-left-radius: 10px;border-top-right-radius: 10px; border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>下限值</span>" +
						"<input type='text' id='af"+measure.measureId+"' onkeydown='esc("+measure.measureId+")' style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:150px;float:left;'   value='"+measure.lower+"'>" +
						"<button onclick='save3("+measure.measureId+")' type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>" +
						"<button onclick='x3("+measure.measureId+")' style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button></div></td>" +	
						
						"<td style='text-align:center;font-family: Microsoft Yahei;font-size: 12px;' width='8%' >" +measure.unit + "</td>" +		 
						"</tr>";
						
					});
				});
				divLabel=divLabel+"</table>";
			});
			$("#lable-measure").html(divLabel);
		}
	});
}
function bindExportEvent(){
    $("#exportBtn").click(function(){
        var $eleForm = $("<form method='POST'></form>");
        $eleForm.attr("action",getRootPath() + "/export/downloadIndicatorsByTeamId");
        $eleForm.append($('<input type="hidden" name="no" value="'+ str +'">'));
        $(document.body).append($eleForm);
        //提交表单，实现下载
        $eleForm.submit();
    })
};

function init() {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        async:false,
        url: getRootPath()+'/projectlable/getTeamLabs',
        data : {
            teamId : str
        },
        success : function(data){
        	var num = 0;
            var dataLg = data.data.length;//返回数据长度
            var zcd = data.data[dataLg-1];//子菜单
            var obj = data.data;
            obj.reverse();
            if(obj){
            	if(obj.length > 0){
	                for (var i = 0; i < obj.length; i++) {
	                    if(obj[i].LAB_PATH == undefined || obj[i].TITLE == undefined){
	                        continue;
	                    }
	                    var plid = obj[i].plId;
	                    if(plid != '' && plid != undefined && plid != null){
	                        var cd = zcd[plid];
	                        if(cd){
	                        	for(var j = 0; j < cd.length; j++){
		                            initColumn(num+"id",cd[j],obj[i].TITLE,cd[j]);
		                            num ++;
		                        }
	                        }
	                    }
	
	                }
	            }
            }
        }
    })
}
$(document).ready(function(e) {
	var text = getAllLabCategory();
	// labelMeasure(text);
    yesOrNo();
	pushOrPull();
	save();
	init();
	$('#usertype2').selectpicker("val",["在行"]);
	/*var zrOrhwselect = getCookie("zrOrhwselect");
    $('#zrOrhwselect')[0].value=zrOrhwselect;
	zrOrhwselectChenge();*/
	bindExportEvent();
//	bindQueryEvent();

	$('#queryBtn').click();
	showDateTime();
	$('.modal-backdrop').addClass("disabled");
    
    initData();
    loadConfigPage();
});

function loadConfigPage(){
    var url = getAngularUrl()+"measureConfig/"+str+"_team";
    var html = '<object type="text/html" data="'+url+'" style="width: 100%;height: 100%;"></object>';
    document.getElementById("lable-measure").innerHTML = html;
}

function resetTeamRank(zrAccount, rank){
	resetRank(zrAccount, rank, "team");
}
