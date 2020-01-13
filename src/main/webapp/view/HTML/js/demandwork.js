$(function(){
	var proNo = window.parent.projNo;
	var role = getCookie("Currentpername");
	var hwAccount = getCookie("username");
	var demand_list="all";
	$("#proNo").val(proNo);
	
	var work_status = getSelectValueByType("work_status");
	var work_prior = getSelectValueByType("work_prior");
	var work_area = getSelectValueByType("work_area");
	var work_importance = getSelectValueByType("work_importance");
	var work_change = getSelectValueByType("work_change");
	var iteList = loadIteNameSelectData();
	var members = getMeberSelect();
	
	//加载显示页面中下拉列表的值
	function initPage(){
		setIteOption('iteId');
		setOption(work_status,"#status","状态");
	}
	//加载新增页面中下拉列表的值
	function initAddForm(){
		setIteOption('addIteId');
		setMeberOption('personLiable');
		setOption(work_prior,"#prior","优先级");
		setOption(work_area,"#wrField","领域");
		setOption(work_importance,"#importance","重要性");
	}
	//加载编辑页面中下拉列表的值
	function initEditForm(){
		setIteOption('editIteId');
		setMeberOption('editPersonLiable');
		setOption(work_prior,"#editPrior","优先级");
		setOption(work_area,"#editWrField","领域");
		setOption(work_importance,"#editImportance","重要性");
		setOption(work_status,"#editStatus","状态");
		setOption(work_change,"#editChange","工作量变更");
	}
	
    initPage();
    $('#mytab').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/IteManage/queryByPage',
    	editable:false,//可行内编辑
    	sortable: true, //是否启用排序
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
    	showColumns:true,
    	minimumCountColumns: 2, //最少允许的列数
    	clickToSelect: true,//是否启用点击选中行
    	toolbarAlign:'right',
    	buttonsAlign:'right',//按钮对齐方式
    	toolbar:'#toolbar',//指定工作栏
    	queryParams: function(params){
        	var param = {
        			topic:$("#topic").val(),
        			iteId : $("#iteId").val(),
        			prior : $("#prior").val(),
        			status : $("#status").val(),
        	        limit : params.limit, //页面大小
        	        offset : params.offset, //页码
        	        proNo : proNo,
        	        hwAccount : hwAccount,
        	        sort: params.sort,//排序列名  
                    sortOrder: params.order //排位命令（desc，asc） 
        	    }
        	return param;
        },
        onEditableSave: function (field, row, oldValue, $el) {
        	if(field == "personLiable"){
        		row.personLiable = row.personLiable.toString();
        	}
//        	if(field == 'iteId'){
//        		if(row.iteId != oldValue ){
//        			row.planStartTime = null;
//        			row.planEndTime = null;
//        			row.startTime = null;
//        			row.endTime = null;
//        		}
//        	}
        	//当row.status为6时，row.finalimit值赋为100
        	if(field=='status'){
    			if(row.status =='6'){
    				row.finalimit = 100 ;
    			}
    		}
        	$("#editCreator").val(hwAccount);
            $.ajax({
                type: "post",
                url: getRootPath()+"/IteManage/edit",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data: JSON.stringify(row),
                success: function (data, status) {
                    if (status == "success") {
                    	var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
                    	$('#mytab').bootstrapTable('refresh',{pageNumber:pageNumber});
                    	toastr.success('修改成功！');
                    }else{
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
        		width:30,
        		align:'center',
        		valign:'middle'
        	},
        	{
        		title : '序号',
        		align: "center",
        		field:'id',
        		width: 30,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytab').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
             },
             {title:'需求/Story',align: "center",field:'topic',width:350,
                 formatter:function(value,row,index){
//                 	var prior = row.prior;
//                 	if(prior == 3){
//                 		prior = "高";
//                 	}else if(prior == 2){
//                 		prior = "中";
//                 	}else if(prior == 1){
//                 		prior = "低";
//                 	}else{	
//                 		prior = "低";
//                 	}
                 	var planStart = row.planStartTime;
                 	if(planStart =="" || planStart ==null){
                 		planStart = "-----  ---  ---";
                 	}else{
                 		planStart = new Date(row.planStartTime).format('yyyy-MM-dd');
                 	}
                 	var planEnd = row.planEndTime;
                 	if(planEnd =="" || planEnd ==null){
                 		planEnd = "-----  ---  ---";
                 	}else{
                 		planEnd = new Date(row.planEndTime).format('yyyy-MM-dd');
                 	}
//                 	var start = row.startTime;
//                 	if(start =="" || start ==null){
//                 		start = "-----  ---  ---";
//                 	}else{
//                 		start = new Date(row.startTime).format('yyyy-MM-dd');
//                 	}
//                 	var end = row.endTime;
//                 	if(end =="" || end ==null){
//                 		end = "-----  ---  ---";
//                 	}else{
//                 		end = new Date(row.endTime).format('yyyy-MM-dd');
//                 	}
                 	var tab = '<div style="width: 100%;text-align: left;margin-left:10px;">' +
 			                    	'<div style="width: 100%;">' +
 			                    		'<a href="#" title='+ value +' style="font-size: 14px;color:blue" onclick="editData(\'' + row.id + '\')">' + value + '</a>' +
 		                            '</div>' +
 		                    		'<div style="font-size: 12px;">' +
 	                                    '<div style="width: 100%;margin-top: 5px;">' +
// 	                                        '<div style="float:left;">优先级: '+prior+'</div>' +
 	                                        '<div style="float:left;position:relative;width:150px;">计划启动时间:'+planStart+'</div>' +
 	                                        '<div style="float:left;position:relative;width:150px;">计划完成时间:'+planEnd+'</div><br />' +
// 	                                        '<div style="float:left;position:relative;margin-left:75px;width:150px;">实际启动时间:'+start+'</div>' +
// 	                                        '<div style="float:left;position:relative;margin-left:20px;width:150px;">实际完成时间:'+end+'</div>' +
 	                                    '</div>' +
 	                                '</div>' +
 		                      '</div>';
                     return tab
                 },
         	},
        	
        	{
        		title:'优先级',
        		halign :'center',
        		align : 'center',
        		field:'prior',
        		sortable:false,
        		width:70,
//        		editable: {
//                    type: "select",              
//                    source: work_status,
//                    title: '优先级',
//                    placement: 'bottom' ,
//                    emptytext:'&#12288',
//                },
                formatter:function(value,row,index){
                	var color = "低";
                 	if(value == "高"){
                 		color = "#f50c0c";
                 	}else if(value == "中"){
                 		color = "#0e0ea0";
                 	}else if(value == "低"){
                 		color = "#8e8e71";
                 	}
                 	var tab = '<div style="width: 100%;text-align: center;">' +
			                  	'<div style="width: 30%; border: 0px;margin: 0 auto;border-radius: 10%;color: white; background-color: '+ color +'">'+ value +'</div>' +
			                  '</div>';
                 	return tab;
                }
        	},
        	{
        		title:'状态',
        		halign :'center',
        		align : 'center',
        		field:'status',
        		sortable:false,
        		width:70,
        		editable: {
                    type: "select",              
                    source: work_status,
                    title: '状态',
                    placement: 'bottom' ,
                    emptytext:'&#12288',
                }
        	},
        	{
        		title:'迭代',
        		halign :'center',
        		align : 'center',
        		field:'iteId',
        		sortable:false,
        		visible: false,
        		width:70,
        		editable: {
                    type: "select",              //编辑框的类型。支持text|textarea|select|date|checklist等
                    source: iteList,
                    title: '请选择迭代',
                    placement: 'bottom' ,        //编辑框的模式：支持popup和inline两种模式，默认是popup
                    emptytext:'&#12288',	
                }
        	},
        	{
        		title:'责任人',
        		halign :'center',
        		align : 'center',
        		field:'personLiable',
//        		visible: false,
        		width:90,
        		editable: {
        			type: "checklist",
        			separator:",",
        			liveSearch: true,
                    emptytext:'&#12288',
                    source: members,
                    title: '请选择负责人',
                    //multiple : true,//多选
                    placement: 'bottom',
                    noeditFormatter: function (value,row,index) {
                    	var names = "";
                    	valStr = value;
                    	if(!value){
                    		value = '&#12288';
                    		result={filed:"personLiable",
		                    		value:value,
                                	values:value
                    		};
                    	}else{
                    		value = value.split(',');
                    		for(var i = 0; i< value.length;i++){
                        		for(var j = 0;j<members.length;j++){
                    				if(members[j].value == value[i]){
                    					if(i != value.length-1){
                    						names += members[j].text+",";
                    					}else{
                    						names += members[j].text
                    					}
                    				}
                    			}
                        	}
                    		result={filed:"personLiable",
		                    		value:names+"<span style='display:none'>,"+valStr+",</span>",
									values:names+"<span style='display:none'>,"+valStr+",</span>"
                    		};
                    	}
                    	return result;
                    }
                }
        	},
        	{
        		title:'工作量(/人天)',
        		halign :'center',
        		align : 'center',
        		field:'expectHours',
        		sortable:false,
        		visible: false,
        		width:70,
        		editable: {
                    type: 'text',
                    title: '工作量(/人天)',
                    placement: 'bottom',
                    emptytext:'&#12288',
                    validate: function (v) {
                   	 var reg = /^[0-9]+(.[0-9]{1})?$/;
                        if(!reg.test(v)){
                        	return '请输入正确的工作量百分比，仅支持保留一位小数';
                        }
                   }
                },
                
        	},
        	{
        		title:'工作量变更',
        		halign :'center',
        		align : 'center',
        		field:'change',
        		sortable:false,
//        		visible: false,
        		width:70,
        		editable: {
                    type: "select",              
                    source: work_change,
                    title: '工作量变更',
                    placement: 'bottom' ,
                    emptytext:'&#12288',
                }
                
        	},
        	{
        		title:'审核结果',
        		halign :'center',
        		align : 'center',
        		field:'remarks',
        		sortable:false,
        		width:70,
        		formatter:function(value,row,index){
    				var tab = '<div style="width: 100%;text-align: center;">' +
		        				'<div style="width: 100%;">'+ 
		        					'<span title=' + (value==null?'-':value) + '>' + (value==null?'-':value) + '</span>' +
		        				'</div>' +
		        			  '</div>';
    				return tab;
                }
        	},
        	{
        		title:'操作',
        		halign :'center',
        		align : 'center',
        		sortable:false,
        		width:70,
        		formatter:function(value,row,index){
        			var tab='<div style="width: 100%;text-align: center;"><div style="width: 100%;">';
        			if(row.personLiable!=null && ""!=row.personLiable && row.design=="PersonLiable" && row.remarks=="待审核" && role!="普通员工"){
        				tab += '<a href="#" style="font-size: 14px;text-decoration: underline;" onclick="approve(\'' + row.id + '\')">' + '审核' + '</a>';
        			}
        			if((row.remarks=="待审核" || row.remarks=="待其他责任人审核") && role!="普通员工" && row.luNumber=="creator"){
        				tab += '  <a href="#" style="font-size: 14px;text-decoration: underline;" onclick="revocation(\'' + row.id + '\')">' + '撤销' + '</a>';
        			}
        			tab += '</div></div>';
        			return tab;
                }
        	},
        	{
        		title:'计划结束时间',
        		align : 'center',
        		halign :'center',
        		field:'planEndTime',
        		width:120,
        		visible: false,
         		switchable: false,
        		formatter:function(value,row,index){
        			return changeDateFormat(value);
        		},
        		editable: {
	                type: 'date',
	                placement: 'bottom',
	                title: '计划结束时间',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',    
	                language: 'zh-CN',
	                emptytext:'&#12288',
	                datepicker: {
	                    weekStart: 1,
	                },
	                validate: function (v) {
                 	if(!v){
                 		return '计划结束时间不能为空！';
                 	}
                 }
        		}
        	},
        	{
        		title:'实际开始时间',
        		align : 'center',
        		halign :'center',
        		field:'startTime',
        		width:120,
        		visible: false,
         		switchable: false,
        		formatter:function(value,row,index){
        			return changeDateFormat(value);
        		},
        		editable: {
	                type: 'date',
	                placement: 'bottom',
	                title: '实际开始时间',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',  
	                language: 'zh-CN',
	                datepicker: {
	                    weekStart: 1,
	                },
	                emptytext:'&#12288'
        		}
        	},
        	{
        		title:'实际完成时间',
        		align : 'center',
        		halign :'center',
        		field:'endTime',
        		width:120,
        		visible: false,
         		switchable: false,
        		formatter:function(value,row,index){
        			//判断实际结束时间与计划结束时间比较大小
        			return changeDateFormat(value);
        		},
        		editable: {
	                type: 'date',
	                placement: 'bottom',
	                title: '实际完成时间',
	                emptytext:'&#12288',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',    
	                language: 'zh-CN',
	                datepicker: {
	                    weekStart: 1
	                }, 
	                noeditFormatter: function (value,row,index) {
	                	if(!value){
	                		value="&#12288";
	                	}else{
	                		value=changeDateFormat(value);
	                	}
	                	 var result= {filed:"endTime",
		                    		value:value,
							 		values:value,
		                    };
	                	if(value){
	                		if(row.endTime != null && row.planEndTime != null){
	            				if(compareDate(row.endTime,row.planEndTime)){
	            					  result={filed:"endTime",
	     		                    		value:value+"&nbsp;超期",
	     		                    		values:value+"&nbsp;超期",
	     		                    		style:"color: red !important;"
	     		                    };
	            				} else{
	            					  result={filed:"endTime",
		     		                    		value:value,
		     		                    		values:value,
		     		                    };
	            				}
	            			}
	                	}
	                    return result;
	                }
        		}
        	},
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
	                       message: '需求名称不能为空'
	                   }
	               }
	           },
	           expectHours : {
	        	   validators: {
	        		   notEmpty: {
	                       message: '预计工作量不能为空'
	                   },
	        		   regexp: { //只需加此键值对，包含正则表达式，和提示 
	                       regexp: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
	                       message: '请输入正整数或小数'
	                   },
	               }
	           },
	           planStartTime: {
	        	   validators: {
	                   notEmpty: {
	                       message: '计划启动日期不能为空'
	                   }
	               }
	           },
	           planEndTime: {
	        	   validators: {
	                   notEmpty: {
	                       message: '计划完成不能为空'
	                   },
	                   callback: {
	                        message: '开始时间必须小于结束时间',
	                        callback: function (value, validator, $field) {
	                            var other = $("#planStartTime").val();//获得另一个的值
	                            var start = new Date(other.replace("-", "/").replace("-", "/"));
	                            var end = new Date(value.replace("-", "/").replace("-", "/"));
	                            if (start <= end) {
	                                return true;
	                            }
	                            return false;
	                        }
	                    }
	               }
	           },
	           prior: {
	               validators: {
	                   notEmpty: {
	                       message: '优先级不能为空'
	                   }
	               }
	           },
	           importance: {
	               validators: {
	                   notEmpty: {
	                       message: '重要程度不能为空'
	                   }
	               }
	           },
	           endTime: {
	        	   validators: {
	                   callback: {
	                        message: '开始时间必须小于结束时间',
	                        callback: function (value, validator, $field) {
	                            var other = $("#startTime").val();//获得另一个的值
	                            var start = new Date(other.replace("-", "/").replace("-", "/"));
	                            var end = new Date(value.replace("-", "/").replace("-", "/"));
	                            if(other!=null && other!="" && value!=null && value!=""){
	                            	if (start <= end) {
	                            		return true;
	                            	}
	                            	return false;
	                            }
	                            return true;
	                        }
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
	    	   topic: {
	               validators: {
	                   notEmpty: {
	                       message: '需求名称不能为空'
	                   }
	               }
	           },
	           expectHours : {
	        	   validators: {
	        		   notEmpty: {
	                       message: '预计工作量不能为空'
	                   },
	        		   regexp: { //只需加此键值对，包含正则表达式，和提示 
	                       regexp: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
	                       message: '请输入正整数或小数'
	                   },
	               }
	           },
	           planStartTime: {
	        	   validators: {
	                   notEmpty: {
	                       message: '计划启动日期不能为空'
	                   }
	               }
	           },
	           planEndTime: {
	        	   validators: {
	                   notEmpty: {
	                       message: '计划完成不能为空'
	                   },
	                   callback: {
	                        message: '开始时间必须小于结束时间',
	                        callback: function (value, validator, $field) {
	                            var other = $("#editPlanStartTime").val();//获得另一个的值
	                            var start = new Date(other.replace("-", "/").replace("-", "/"));
	                            var end = new Date(value.replace("-", "/").replace("-", "/"));
	                            if (start <= end) {
	                                return true;
	                            }
	                            return false;
	                        }
	                    }
	               }
	           },
	           prior: {
	               validators: {
	                   notEmpty: {
	                       message: '优先级不能为空'
	                   }
	               }
	           },
	           status: {
	        	   validators: {
	        		   notEmpty: {
	        			   message: '状态不能为空'
	        		   }
	        	   }
	           },
	           change: {
	        	   validators: {
	        		   notEmpty: {
	        			   message: '工作量变更不能为空'
	        		   }
	        	   }
	           },
	           importance: {
	               validators: {
	                   notEmpty: {
	                       message: '重要程度不能为空'
	                   }
	               }
	           },
	           endTime: {
	        	   validators: {
	                   callback: {
	                        message: '开始时间必须小于结束时间',
	                        callback: function (value, validator, $field) {
	                            var other = $("#editStartTime").val();//获得另一个的值
	                            var start = new Date(other.replace("-", "/").replace("-", "/"));
	                            var end = new Date(value.replace("-", "/").replace("-", "/"));
	                            if(other!=null && other!="" && value!=null && value!=""){
	                            	if (start <= end) {
	                            		return true;
	                            	}
	                            	return false;
	                            }
	                            return true;
	                        }
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

    //查询按钮事件
    $('#search_btn').click(function(){
    	demand_list="all";
    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
    });
    
    //清空按钮事件
    $('#clear_btn').click(function(){
    	demand_list="all";
    	$("#topic").val('');
    	$("#status").val(''); 
    	$("#iteId").val('');
    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
    });
    
    /******************************** 刷新 *********************************/
    $('#btn_refresh').click(function(){
    	demand_list="all";
    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
    });   
    /******************************** 我的待办 *********************************/
    $('#btn_ready').click(function(){
    	demand_list="ready";
    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage?type=ready'});
    });   
    /*********************************  新增   *******************************/
    //打开新增页面
    $('#btn_add').click(function(){
    	$("#addIteId").empty();
    	$("#prior").empty();
    	$("#wrField").empty();
    	$("#importance").empty();
    	$("#personLiable").empty();
    	
    	initAddForm();
    	$("#iteAddPage").modal('show');
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
        	
        	//添加时将#personLiable多选值存入隐藏的input
        	var str = "";
        	var selectedValues = "";
        	$("#personLiable option:selected").each(function(i){
                str += $(this).val() + ",";
                selectedValues = str.substring(0,str.length-1);
            });
        	$("#personLiableAdd").val(selectedValues);
        	
        	$.ajax({
 				url:getRootPath() + '/IteManage/add',
 				type : 'post',
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data:JSON.stringify($('#addForm').serializeJSON()),
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#iteAddPage").modal('hide');
 						demand_list="all";
 				    	$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
 				    	$('#addForm').data('bootstrapValidator').resetForm(true);
 						toastr.success('新增成功！');
 					}else{
 						//提示topic有相同值存在，不允许添加
 						$('#err').css("display","block");
 						$('#err').html('<font style="color:#a94442;font-size:10px;">需求名称已存在!</font>');
 						setTimeout(function() {
 							$('#err').css("display", "none");
 						}, '3000');
						toastr.error('新增失败!');
 					}
 				}
 			});
        }
    });
    
    
    /************************************* 编辑 *********************************/
    function initEditPage(data){
    	if(data.code == 'success'){
			if(data.rows){
				$("#editId").val(data.rows.id);
				$("#editProNo").val(data.rows.proNo);
				$("#editIsDeleted").val(data.rows.isDeleted);
				$("#editStatus").val(data.rows.status);
				$("#editChange").val(data.rows.change);
				$("#editVersion").val(data.rows.version);
				$("#editTopic").val(data.rows.topic);
                $("#editPlanStartTime").val(changeDateFormat(data.rows.planStartTime));
                $("#editPlanEndTime").val(changeDateFormat(data.rows.planEndTime));
                $("#editExpectHours").val(data.rows.expectHours);
                $("#editActualHours").val(data.rows.actualHours);
                $("#editDescribeInfo").val(data.rows.describeInfo);
                $("#editCreator").val(data.rows.creator);
//                $("#editCreateTime").val(changeDateFormatHMS(data.rows.createTime));
//                $("#editUpdateTime").val(changeDateFormatHMS(data.rows.updateTime));
                $("#editStartTime").val(changeDateFormat(data.rows.startTime));
                $("#editEndTime").val(changeDateFormat(data.rows.endTime));
                $("#editIteType").val(data.rows.iteType);
                $("#editCodeAmount").val(data.rows.codeAmount);
                $("#editFinalimit").val(data.rows.finalimit);
                
                var prior = data.rows.prior;
                $("input[type='radio']").removeAttr('checked');
                $("#editPrior"+prior).attr("checked", true);
                
                document.getElementById('editIteId').value=data.rows.iteId;
                document.getElementById('editImportance').value=data.rows.importance;
                document.getElementById('editStatus').value=data.rows.status;
                document.getElementById('editChange').value=data.rows.change;
                
                $("#editDemandCode").val(data.rows.demandCode);
                $("#editDocument").val(data.rows.document);
                $("#editAlertContent").val(data.rows.alertContent);
                $("#editDesign").val(data.rows.design);
                $("#editLuNumber").val(data.rows.luNumber);
                $("#editTestNumber").val(data.rows.testNumber);
                $("#editCheckResult").val(data.rows.checkResult);
                $("#editRemarks").val(data.rows.remarks);
                document.getElementById('editDemandType').value=data.rows.demandType;
                
                //处理责任人
                if(data.rows.personLiable){
                	var option = (data.rows.personLiable).split(",");
                	$("#editPersonLiable").selectpicker('val', option);
                }
                
                if("admin"==hwAccount || role=="普通员工" || data.rows.remarks!="finished"){
                	$("#edit_saveBtn").css("display","none");
                	$("#editForm").find("input").attr("disabled","disabled");
                	$("#editForm").find("textarea").attr("disabled","disabled");
//                	$("#editForm").find("select").attr("disabled","disabled");
                	$("#editStatus").attr("disabled","disabled");
                	$("#editImportance").attr("disabled","disabled");
                	$("#editIteId").attr("disabled","disabled");
                	$("#editChange").attr("disabled","disabled");
                	$("#personLiableCopy").attr("type","text");
                	$("#editPersonLiableDiv").css("display","none");
//                	$("#personLiableCopy").next("div").css("display","none");
                	$("#personLiableCopy").val($("#editPersonLiable").prev().prev("button").attr("title"));
                }else{
                	$("#edit_saveBtn").css("display","");
                	$("#editForm").find("input").removeAttr("disabled");
                	$("#editForm").find("textarea").removeAttr("disabled");
//                	$("#editForm").find("select").removeAttr("disabled");
                	$("#editStatus").removeAttr("disabled");
                	$("#editImportance").removeAttr("disabled");
                	$("#editIteId").removeAttr("disabled");
                	$("#editChange").removeAttr("disabled");
                	$("#personLiableCopy").attr("type","hidden");
                	$("#editPersonLiableDiv").css("display","");
//                	$("#personLiableCopy").next("div").css("display","");
                	$("#personLiableCopy").val($("#editPersonLiable").prev().prev("button").attr("title"));
                }
                
			}
		}else{
			toastr.error('服务请求失败，请稍后再试！');
		}
    }
    
  //点击启用审核
    approve =  function (selectId){ 
    	$("#checkPage").modal('show');
    	$.ajax({
    		url:getRootPath() + '/IteManage/compareCheck',
    		type:'post',
    		data:{
				id : selectId,
				proNo : proNo
			},
    		success : function(data){
    			$("#checkId").val(data.checkId);
    			var tab="";
    			if(data.data==null || data.data.length==0){
    				tab+="<span>没有待审核内容</span><br />";
    			}else{
    				for(var i=0;i<data.data.length;i++){
    					tab+="<span>" + data.data[i] + "</span><br />";
    				}
    			}
    			$("#checkForm").html(tab);
    		}
    	});	
    };
    
    //审核撤销
    revocation =  function (selectId){ 
    	Ewin.confirm({ message: "确认撤销当前审核内容吗？" }).on(function (e) {
			if (!e) {
				return;
			} else {
				$.ajax({
	        		url:getRootPath() + '/IteManage/revocation',
	        		type:'post',
	        		data:{
	    				id : selectId,
	    			},
	        		success:function(data){
	        			if(data.code == '200'){
	        				toastr.success('审核内容已撤销');
	        				if(demand_list=="all"){
	        					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
	        				}else if(demand_list=="ready"){
	        					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage?type=ready'});
	        				}
	        			}else{
	        				toastr.error('审核内容撤销失败!');
	        			}
	        		}
	        	});
			}
    	});
    };
    
    //审核拒绝
    $('#check_refuseBtn').click(function(){
    	$.ajax({
    		url:getRootPath() + '/IteManage/checkResult',
    		type:'post',
    		data:{
				id : $("#checkId").val(),
				result : "refuse"
			},
    		success : function(data){
    			if(data.code=='200'){
					toastr.error('审核未通过！');
				}else{
					toastr.error('审核失败！');
				}
    			$("#checkPage").modal('hide');
    			if(demand_list=="all"){
					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
				}else if(demand_list=="ready"){
					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage?type=ready'});
				}
    		}
    	});	
    });
    
    //审核通过
    $('#check_saveBtn').click(function(){
    	$.ajax({
    		url:getRootPath() + '/IteManage/checkResult',
    		type:'post',
    		data:{
    			id : $("#checkId").val(),
    			result : "pass",
    			person : hwAccount,
    			no : proNo
    		},
    		success : function(data){
    			if(data.code=='200'){
    				toastr.success('审核通过！');
    			}else{
    				toastr.error('审核失败！');
    			}
    			$("#checkPage").modal('hide');
    			if(demand_list=="all"){
					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
				}else if(demand_list=="ready"){
					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage?type=ready'});
				}
    		}
    	});	
    });
    
    //点击标题启用编辑
    editData =  function (selectId){ 
    	$("#editIteId").empty();
    	$("#editWrField").empty();
    	$("#editPrior").empty();
    	$("#editImportance").empty();
    	$("#editStatus").empty();
    	$("#editChange").empty();
    	$("#editPersonLiable").empty();
    	
    	initEditForm();
    	$("#editPage").modal('show');
    	$.ajax({
    		url:getRootPath() + '/IteManage/editPage',
    		type:'post',
    		data:{
				id : selectId
			},
    		success : function(data){
    			initEditPage(data)
    		}
    	});	
    };
    
    //打开编辑页面
    $('#btn_edit').click(function(){
    	$("#editIteId").empty();
    	$("#editWrField").empty();
    	$("#editPrior").empty();
    	$("#editImportance").empty();
    	$("#editStatus").empty();
    	$("#editChange").empty();
    	$("#editPersonLiable").empty();
    	
    	initEditForm();
    	var selectRow = $('#mytab').bootstrapTable('getSelections');
    	if(selectRow.length == 1){
    		$("#editPage").modal('show');
    	}else{
    		toastr.warning('请先选择一条数据');
    		return;
    	}
    	$.ajax({
    		url:getRootPath() + '/IteManage/editPage',
    		type:'post',
    		data:{
				id : selectRow[0].id
			},
    		success : function(data){
    			initEditPage(data)
    		}
    	});	
    });
    
    //打开查看页面
    $('#btn_look').click(function(){
    	$('#btn_edit').click();
    });
    
    $('#edit_backBtn').click(function(){
    	$("#editPage").modal('hide');
    	$('#editForm').data('bootstrapValidator').resetForm(true);
    });
    
    $('#check_cancelBtn').click(function(){
    	$("#checkPage").modal('hide');
    });
    
    //修改保存
    $('#edit_saveBtn').click(function(){
    	if("admin"==hwAccount){
			toastr.error('系统用户无权限修改需求内容！');
			return;
		}
    	$('#editForm').bootstrapValidator('validate');
    	if($("#editForm").data('bootstrapValidator').isValid()){
    		//编辑时将#personLiable多选值存入隐藏的input
        	var str = "";
        	var selectedValues = "";
        	$("#editPersonLiable option:selected").each(function(i){
                str += $(this).val()+",";
                selectedValues = str.substring(0,str.length-1);
            });
        	$("#personLiableEdit").val(selectedValues);
        	$("#editCreator").val(hwAccount);
    		
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
 						if(demand_list=="all"){
        					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
        				}else if(demand_list=="ready"){
        					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage?type=ready'});
        				}
 				    	$('#editForm').data('bootstrapValidator').resetForm(true);
 						toastr.success('需求修改已提交，待审核！');
 					}else if(data.code=='error'){
 						toastr.error('需求内容变更必须选择责任人审核！');
 					}else if(data.code=='self'){
 						toastr.error('审核责任人不能为自己！');
 					}else if(data.code=='only'){
 						$("#editPage").modal('hide');
 						if(demand_list=="all"){
        					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
        				}else if(demand_list=="ready"){
        					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage?type=ready'});
        				}
 				    	$('#editForm').data('bootstrapValidator').resetForm(true);
 				    	toastr.success('责任人已修改！');
 					}else{
 						toastr.error('需求修改提交失败!');
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
    	ID.push(hwAccount);
    	Ewin.confirm({ message: "确认删除当前工作内容吗？" }).on(function (e) {
			if (!e) {
				return;
			} else {
				$.ajax({
	        		url:getRootPath() + '/IteManage/delete',
	        		type:'post',
	        		dataType: "json",
					contentType : 'application/json;charset=utf-8', //设置请求头信息
	        		data:JSON.stringify(ID),
	        		success:function(data){
	        			if(data.code == 'success'){
	        				toastr.success(data.succeed+'条需求删除已提交，待审核！');
	        				if(data.failed>0){
	        					toastr.error(data.failed+'条需求删除审核责任人选择有误！');
	        				}
	        				if(demand_list=="all"){
	        					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage'});
	        				}else if(demand_list=="ready"){
	        					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/IteManage/queryByPage?type=ready'});
	        				}
	        			}else{
	        				toastr.error('需求删除提交失败!');
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
    					proNo:proNo,
    					hwAccount:hwAccount
    				},
    				success : function(data) {
    					if(data){
    						if(Number(data.sucNum) > 0){
    							toastr.success('成功导入'+data.sucNum+'条新数据!');
    							if(data.repeatNum>0){
    								toastr.error(data.repeatNum+'条已有数据不可重复导入!');
    							}
    						}else{
    							toastr.error('没有新数据导入');
    						}
    					}
    					//根据条件判断，执行成功的需求记录
    					demand_list="all";
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
    
    /********************************************** 导出 *************************************/

    $("#btn_export").click(function(){
    	var $eleForm = $("<form method='POST'></form>");
        $eleForm.attr("action",getRootPath() + "/IteManage/export");
        $eleForm.append($('<input type="hidden" name="proNo1" value="'+ proNo +'">'));
        $(document.body).append($eleForm);
        //提交表单，实现下载
        $eleForm.submit();
    });
    
    /***************迭代名称下拉列表值 ******************/
   //迭代名称下拉列表值
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
				if(data.code == '200'){
					s= data.data;
				}else{
					toastr.error('加载失败!');
				}
			}
		});
	   return s;
   }
   
   //迭代列表加载
   function setIteOption(id){
	   $("#"+id).append("<option value=''>" + "请选择迭代" + "</option>");
		for(j = 0; j < iteList.length; j++) {
			$("#"+id).append("<option value='" + iteList[j].value + "'>" + iteList[j].text + "</option>");
		} 
   }
   
   //加载项目成员下拉列表
   function getMeberSelect(){
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
   
   //项目成员列表加载
   function setMeberOption(id){
	   if(id == "personLiable"){
		   for (var i = 0; i < members.length; i++) {  
			   $("#"+id).append("<option value='"+members[i].value+"'>"+ members[i].text +"</option>");
	       }
	       // 缺一不可
	       $('.selectpicker').selectpicker('val', '');  
	       $('.selectpicker').selectpicker('refresh');
	   }else{
		   $('#editPersonLiable').selectpicker("val", []);
	       $("#editPersonLiable").empty();
	       $('#editPersonLiable').prev('div.dropdown-menu').find('ul').empty();
	       for (var i = 0; i < members.length; i++) {
	    	   $("#editPersonLiable").append("<option value='" + members[i].value + "'>" + members[i].text + "</option>");
	       }
	       $('#editPersonLiable').selectpicker('refresh');
	       $('#editPersonLiable').selectpicker('render');
	   }
   }
   
   $(document).ready(function(){
		$.ajax({
			url:getRootPath() + '/IteManage/members',
			type:'post',
			async:false,
			data:{
				proNo : proNo,
				hwAccount : hwAccount
			},
			success:function(data){
				if(data==null || data==""){
					$("#btn_ready").css("display","none");
					$("button[id^='btn_']").css("display","none");
					$("#btn_refresh").css("display","");
					$("#btn_look").css("display","");
					hwAccount="admin";
				}
			}
		});
	});
   
});

//获取下拉列表的值
function getSelectValueByType(code) {
    var s = "";
    $.ajax({
        url: getRootPath() + '/dict/items?entryCode=' + code,
        type: 'get',
        async: false,
        dataType: "json",
        contentType: 'application/x-www-form-urlencoded', //设置请求头信息
        success: function (data) {
            if (data.code == '200') {
                s = data.data;
                for(var i=0;i<s.length;i++){
                	s[i].text=s[i].key;
                }
            } else {
                toastr.error('查询失败!');
            }
        }
    });
    return s;
}

//获取下拉列表的值
function setOption(s,id,name){
	$(id).append("<option value=''>" + "请选择"+name + "</option>");
	for(j = 0; j < s.length; j++) {
		$(id).append("<option value='" + s[j].value + "'>" + s[j].key + "</option>");
	} 
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

//日期前后顺序比较
function compareDate(o1,o2){
	o1= new Date(Date.parse(o1));
	o2=new Date(Date.parse(o2));
	if(o1>o2){
		return true;
	}
	return false;
}

function clearForm(formId){
	document.getElementById(formId).reset()
}
//日期大小比较
function compareDate(endTime,planEndTime){
	//进行比较
	return endTime > planEndTime;
}

//根据窗口调整表格高度
$(window).resize(function() {
	$(".fixed-table-body").css({"min-height":$(window).height()-115});
});

$(document).ready(function(){
	$(".fixed-table-body").css({"min-height":$(window).height()-115});
	if(getCookie("username")=="admin"){
		$("#btn_ready").css("display","none");
		$("button[id^='btn_']").css("display","none");
		$("#btn_refresh").css("display","");
		$("#btn_look").css("display","");
	}
	if(getCookie("Currentpername")=="普通员工"){
		$("button[id^='btn_']").css("display","none");
		$("#btn_refresh").css("display","");
		$("#btn_look").css("display","");
		$("#btn_ready").css("display","");
	}else{
		$("#btn_look").css("display","none");
	}
});
