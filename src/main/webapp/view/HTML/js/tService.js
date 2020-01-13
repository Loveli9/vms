//加载下拉列表的值
function getOPtion(){
	 getSelectValue('work_prior',"1","#prior","优先级");
	 getSelectValue('work_status',"1","#status","状态");
}
window.operateEvents = {
		
    	"click #collectionMicroService":function(e,vlaue,row,index){
    		window.location.href=getRootPath()+'/view/HTML/collectionService.html?serviceId='+row.id;
    	},
		
		"click #insertIndex":function(e,vlaue,row,index){
			window.location.href=getRootPath()+'/view/HTML/insertIndex.html?serviceId='+row.id;
		},
		
		"click #serviceDetail":function(e,vlaue,row,index){
			window.location.href=getRootPath()+'/view/HTML/yunlongServiceDetail.html?serviceId='+row.id;
		},

    };

$(function(){
	var proNo = window.parent.projNo;
	$("#proNo").val(proNo);
	//根据窗口调整表格高度
    $(window).resize(function() {
        $('#mytab').bootstrapTable('resetView', {
            height: tableHeight()
        })
    });

    $('#mytab').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/tService/searchByCondition',
    	height:tableHeight(),//高度调整
    	toolbar: '#toolbar',
    	editable:true,//可行内编辑
    	sortable: true,                     //是否启用排序
    	sortOrder: "asc",
    	striped: true, //是否显示行间隔色
    	dataField: "list",
    	pageNumber: 1, //初始化加载第一页，默认第一页
    	pagination:true,//是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,//单页记录数
    	pageList:[5,10,20,30],//分页步进值
    	//showRefresh:true,//刷新按钮
    	showColumns:true,
    	minimumCountColumns: 2,             //最少允许的列数
    	//clickToSelect: true,//是否启用点击选中行
    	toolbarAlign:'right',
    	buttonsAlign:'right',//按钮对齐方式
    	toolbar:'#toolbar',//指定工作栏
    	queryParams: function(params){
        	var param = {
        			name : $("#topic").val(),
        			rows : params.limit, // 页面大小
        	        page : params.offset, // 页码
        	        projectId : proNo,
        	    }
        	return param;
        },
        onEditableSave: function (field, row, oldValue, $el) {
            $.ajax({
                type: "post",
                url: getRootPath()+"/tService/updateTServiceById",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data: JSON.stringify(row),
                success: function (data, status) {
                    if (status == "success") {
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
        		width:25,
        		align:'center',
        		valign:'middle'
        	},
        	{
        		title : '序号',
        		align: "center",
        		width: 60,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytab').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
             },
        	{
        		title:'服务名',
        		halign :'center',
        		align : 'center',
        		field:'name',
        		width:200,
        		editable: {
        			type: "text",
        			title: '修改服务名',
        			placement: 'bottom'
        		}
        	},
        	
        	{
        		title:'创建时间',
        		align : 'center',
        		halign :'center',
        		field:'createTime',
        		sortable:true,
        		width:150,
        		formatter:function(value,row,index){
        			return changeDateFormatHMS(value);
        		}
        	},
        	{
        		field:'Button',
        		align : 'center',
        		halign :'center',
        		title:'操作',
        		width:150,
        		events: operateEvents,
        		formatter:function(value,row,index){
        			return '<button id="serviceDetail"  type="button" class="startCollect" data-toggle="dropdown">查看服务详情</button> <button id="collectionMicroService"  type="button" class="startCollect" data-toggle="dropdown">采集微服务</button> <button id="insertIndex"  type="button" class="startCollect" data-toggle="dropdown">手工指标录入</button>';
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
			        	   topic: {
			                   validators: {
			                       notEmpty: {
			                           message: '服务名不能为空'
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
			        	   topic: {
			                   validators: {
			                       notEmpty: {
			                           message: '服务名不能为空'
			                       }
			                   }
			               }
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

    //查询按钮事件
    $('#search_btn').click(function(){
    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/tService/searchByCondition'});
    });
    
    //清空按钮事件
    $('#clear_btn').click(function(){
    	$("#topic").val('');
    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/tService/searchByCondition'});
    });
    
/******************************** 刷新 *********************************/
    $('#btn_refresh').click(function(){
    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/tService/searchByCondition'});
    });   
/*********************************  新增   *******************************/
    //打开新增页面
    $('#btn_add').click(function(){
    	$("#iteAddPage").modal('show');
    	var proNo = window.parent.projNo;
    	$("#projectId").val(proNo);
    	
    });
    //隐藏新增页面
    $('#add_backBtn').click(function(){
    	$("#iteAddPage").modal('hide');
    });
    //新增保存   
    $('#add_saveBtn').click(function() {
    	//点击保存时触发表单验证
    	$('#addForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if($("#addForm").data('bootstrapValidator').isValid()){
 			$.ajax({
 				url:getRootPath() + '/tService/addTService',
 				type : 'post',
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data:JSON.stringify($('#addForm').serializeJSON()),
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#iteAddPage").modal('hide');
 				    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/tService/searchByCondition'});
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
    
    //打开编辑页面
    $('#btn_edit').click(function(){
    	$("#editIteId").empty();
    	$("#editWrField").empty();
    	$("#editImportance").empty();
    	loadIteNameSelect('editIteId');//迭代
    	getSelectValue('work_area',"1","#editWrField","领域");//领域
    	getSelectValue('work_importance',"1","#editImportance","重要性");//重要程度
    	var selectRow = $('#mytab').bootstrapTable('getSelections');
    	if(selectRow.length == 1){
    		$("#editPage").modal('show');
    	}else{
    		toastr.warning('请选择一条数据进行编辑');
    		return;
    	}
    	$.ajax({
    		url:getRootPath() + '/IteManage/editPage',
    		type:'post',
    		data:{
    			id : selectRow[0].id
    		},
    		success : function(data){
    			if(data.code == 'success'){
    				if(data.rows){
    					$("#editId").val(data.rows.id);
    					$("#editProNo").val(data.rows.proNo);
    					$("#editisDeleted").val(data.rows.isDeleted);
    					$("#editStatus").val(data.rows.status);
    					$("#editTopic").val(data.rows.topic);
                        $("#editPlanStartTime").val(changeDateFormat(data.rows.planStartTime));
                        $("#editPlanEndTime").val(changeDateFormat(data.rows.planEndTime));
                        $("#editExpectHours").val(data.rows.expectHours);
                        $("#editActualHours").val(data.rows.actualHours);
                        $("#editDescribeInfo").val(data.rows.describeInfo);
                        $("#editCreator").val(data.rows.creator);
                        $("#editCreateTime").val(data.rows.createTime);
                        $("#editStartTime").val(data.rows.startTime);
                        $("#editEndTime").val(data.rows.endTime);
                        $("#editIteType").val(data.rows.iteType);
                        $("#editCodeAmount").val(data.rows.codeAmount);
                        $("#editPersonLiable").val(data.rows.personLiable);
                        var prior = data.rows.prior;
                        $("input[type='radio']").removeAttr('checked');
                        $("#editPrior"+prior).attr("checked", true);
                        document.getElementById('editIteId').value=data.rows.iteId;
                        document.getElementById('editImportance').value=data.rows.importance;
                        document.getElementById('editWrField').value=data.rows.wrField;
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
 				url:getRootPath()+"/IteManage/edit",
 				type : 'post',
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data:JSON.stringify($('#editForm').serializeJSON()),
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#editPage").modal('hide');
 				    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
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
    	var ID=[];
    	var dataArr=$('#mytab').bootstrapTable('getSelections');
    	for(var i=0;i<dataArr.length;i++){
    		ID.push(dataArr[i].id);
    	}
    	if (ID.length <= 0) {
    		 toastr.warning('请选择有效数据');
    		 return;
    	}
    	Ewin.confirm({ message: "确认删除当前工作内容吗？" }).on(function (e) {
			if (!e) {
				return;
			} else {
				$.ajax({
	        		url:getRootPath() + '/tService/deleteTServiceByIds',
	        		type:'post',
	        		dataType: "json",
					contentType : 'application/json;charset=utf-8', //设置请求头信息
	        		data:JSON.stringify(ID),
	        		success:function(data){
	        			if(data.code == 'success'){
	        				toastr.success('删除成功！');
	            			$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/tService/searchByCondition'});
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
    $(document).on("click", "#templateDownload", function () {
    	var modelName = 'demand';
		var downloadUrl = getRootPath() + '/project/projectTemplate?templateName='+modelName;
		$("#templateDownload").attr('href', downloadUrl);
	});
    
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
});



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
//获取下拉列表的值
function getSelectValue(code, isInit, id, name) {
    var s = "";
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
                s = data.data;
                if ("1" == isInit) {
                    $(id).append('<option label="' + "请选择" + name + '" value=""></option>');
                    for (j = 0; j < s.length; j++) {
                        $(id).append('<option label="' + data.data[j].key + '" value="' + data.data[j].value + '"></option>');
                    }
                }
            } else {
                toastr.error('加载失败!');
            }
        }
    });
    return s;
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

