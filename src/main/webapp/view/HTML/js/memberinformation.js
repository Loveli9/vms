var proNo = window.parent.projNo;
$("#proNo").val(proNo);
var dict = "0";
$(function(){
	//根据窗口调整表格高度
    $(window).resize(function() {
        $('#mytab').bootstrapTable('resetView', {
            height: tableHeight()
        })
    });
    //projectInfo();部分项目信息
    memberinformation();
	$('#mytab2').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/MemberInforController/queryMembersInformation',
    	height:400,//高度调整
    	toolbar: '#toolbar',
    	editable:true,//可行内编辑
    	sortable: true, //是否启用排序
    	sortOrder: "asc",
    	striped: true, //是否显示行间隔色
    	dataField: "rows",
    	pageNumber: 1, //初始化加载第一页，默认第一页
    	pagination:true,//是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,//单页记录数
    	pageList:[10,15,20,30],//分页步进值
    	//showRefresh:true,//刷新按钮
    	showColumns:false,//列选择按钮
    	minimumCountColumns: 2, //最少允许的列数
    	clickToSelect: true,//是否启用点击选中行
    	toolbarAlign:'right',
    	buttonsAlign:'right',//按钮对齐方式
    	toolbar:'#toolbar',//指定工作栏
    	queryParams: function(params){
        	var param = {
        	        limit : params.limit, //页面大小
        	        offset : params.offset, //页码
        	        projectId : proNo,
        	        //sort: params.sort,//排序列名  
                    //sortOrder: params.order //排位命令（desc，asc） 
        	    }
        	return param;
        },
    	columns:[
        	{
        		title : '序号',
        		align: "center",
        		valign : 'middle',
        		field:'id',
        		width: 50,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytab').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
             },
             {
          		title : '工号',
          		align: "center",
          		valign : 'middle',
          		field:'zrAccount',
          		width: 100,
              },
             {
            		title : '姓名',
            		align: "center",
            		valign : 'middle',
            		field:'name',
            		width: 100,
             },
             {
            		title : '工作年限',
            		align: "center",
            		valign : 'middle',
            		field:'workingLift',
            		width: 100,
             },
             {
                 title : '正常通</br>道职级',
                 valign : 'middle',
             	align: "center",
             	field:'channelRank',
             	width: 100,
             },
             {
                  title : '调薪间隔',
                  valign : 'middle',
              	 align: "center",
              	 field:'payInterval',
              	 width: 100,
             },
             {
          		title : '当前岗位</br>工作时长',
          		valign : 'middle',
          		align: "center",
          		field:'workingHours',
          		width: 100,
              },
             {
            		title : '绩效评分',
            		valign : 'middle',
            		align: "center",
            		field:'performanceScore',
            		width: 100,
             },
             {
                 title : '是否审视</br>职级倒挂',
                 valign : 'middle',
             	align: "center",
             	field:'rankInversion',
             	width: 100,
             	formatter:function(value,row,index){
             		if(value=="0"){
           				return "是";
           			}else if(value=="1"){
           				return "否";
           			}
           		}
             },
             {
                  title : '调薪指数',
                  valign : 'middle',
              	 align: "center",
              	 field:'payAdjusetmment',
              	 width: 100,
             },
             {
                 title : '是否建</br>议换岗',
                 valign : 'middle',
             	align: "center",
             	field:'changeOfPost',
             	width: 100,
             	formatter:function(value,row,index){
             		if(value=="0"){
           				return "是";
           			}else if(value=="1"){
           				return "否";
           			}
           		}
             },
             {
                  title : '是否建</br>议淘汰',
                  valign : 'middle',
              	 align: "center",
              	 field:'elimination',
              	 width: 100,
              	formatter:function(value,row,index){
             		if(value=="0"){
           				return "是";
           			}else if(value=="1"){
           				return "否";
           			}
           		}
             },
    	],
    	locale:'zh-CN',//中文支持,
    });
	$('#mytab').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/MemberInforController/queryMembersInformation',
    	height:400,//高度调整
    	toolbar: '#toolbar',
    	editable:true,//可行内编辑
    	sortable: true, //是否启用排序
    	sortOrder: "asc",
    	striped: true, //是否显示行间隔色
    	dataField: "rows",
    	pageNumber: 1, //初始化加载第一页，默认第一页
    	pagination:true,//是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,//单页记录数
    	pageList:[10,15,20,30],//分页步进值
    	//showRefresh:true,//刷新按钮
    	showColumns:false,//列选择按钮
    	minimumCountColumns: 2, //最少允许的列数
    	clickToSelect: true,//是否启用点击选中行
    	toolbarAlign:'right',
    	buttonsAlign:'right',//按钮对齐方式
    	toolbar:'#toolbar',//指定工作栏
    	queryParams: function(params){
        	var param = {
        	        limit : params.limit, //页面大小
        	        offset : params.offset, //页码
        	        projectId : proNo,
        	        //sort: params.sort,//排序列名  
                    //sortOrder: params.order //排位命令（desc，asc） 
        	    }
        	return param;
        },
        onEditableSave: function (field, row, oldValue, $el) {
            $.ajax({
                type: "post",
                url: getRootPath()+"/MemberInforController/saveMembersInformation",
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
        		title : '序号',
        		align: "center",
        		field:'id',
        		valign : 'middle',
        		width: 50,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytab').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
             },
             {
            		title : '姓名',
            		align: "center",
            		valign : 'middle',
            		field:'name',
            		width: 100,
            		formatter:function(value,row,index){
            			return '<a href="#" style="font-size: 14px;color:blue" onclick="editData(\'' + row.id + '\')">' + value + '</a>';
            		}
             },
             {
         		title : '工号',
         		align: "center",
         		valign : 'middle',
         		field:'zrAccount',
         		width: 100,
             },
            {
                title : '岗位',
             	align: "center",
             	valign : 'middle',
             	field:'post',
             	width: 100,
             },
            {
          		title : '是否SOW要</br>求的关键角色',
          		align: "center",
          		field:'keyRole',
          		width: 100,
          		formatter:function(value,row,index){
           			var val = value == null ? "-":value;
           			if(val=="-"){
           				return "-";
           			}else if(val=="0"){
           				return "是";
           			}else if(val=="1"){
           				return "否";
           			}
           		}
            },
            {
                title : '地域',
            	align: "center",
            	valign : 'middle',
            	field:'area',
            	width: 100,
            	editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }
            },
            {
                 title : '学历',
             	 align: "center",
             	valign : 'middle',
             	 field:'education',
             	 width: 100,
             	/*editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }*/
            },
            {
         		title : '是否211',
         		align: "center",
         		valign : 'middle',
         		field:'school',
         		width: 100,
         		sortable:true,
         		editable: {
         			type: 'select',
                    title: '是否211',
                    emptytext:'&#12288',
                    placement: 'bottom',
                    source:{
                    	"0":"是",
                    	"1":"否",
                    }
                },
             },
            {
           		title : '毕业时间',
           		align: "center",
           		valign : 'middle',
           		field:'graduation',
           		width: 100,
           		formatter:function(value,row,index){
           			if(value){
           				var val = value.split(" ");
            			return val[0];
           			}else{
           				return null;
           			}
        		},
        		/*editable: {
                    type: 'date',
                    placement: 'bottom',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',    
	                language: 'zh-CN',
	                emptytext:'&#12288',
	                datepicker: {
	                    weekStart: 1,
	                },
	                validate: function (v) {
                    	if(!v){
                    		return '毕业时间不能为空!';
                    	}
                    	
                    }
                }*/
            },
            {
                title : '入职日期',
            	align: "center",
            	valign : 'middle',
            	field:'entry',
            	width: 100,
            	formatter:function(value,row,index){
            		if(value){
           				var val = value.split(" ");
            			return val[0];
           			}else{
           				return null;
           			}
        		},
        		/*editable: {
                    type: 'date',
                    placement: 'bottom',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',    
	                language: 'zh-CN',
	                emptytext:'&#12288',
	                datepicker: {
	                    weekStart: 1,
	                },
	                validate: function (v) {
                    	if(!v){
                    		return '毕业时间不能为空!';
                    	}
                    	
                    }
                }*/
            },
            {
                 title : '离职日期',
             	 align: "center",
             	 valign : 'middle',
             	 field:'quitday',
             	 width: 100,
             	formatter:function(value,row,index){
             		if(value){
           				var val = value.split(" ");
            			return val[0];
           			}else{
           				return null;
           			}
        		},
        		editable: {
                    type: 'date',
                    placement: 'bottom',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',    
	                language: 'zh-CN',
	                emptytext:'&#12288',
	                datepicker: {
	                    weekStart: 1,
	                },
	                validate: function (v) {
                    	if(!v){
                    		return '毕业时间不能为空!';
                    	}
                    	
                    }
                }
            },
            {
         		title : '司龄',
         		align: "center",
         		valign : 'middle',
         		field:'siling',
         		width: 100,
         		editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }
             },
            {
           		title : '倒数第四</br>次绩效',
           		align: "center",
           		valign : 'middle',
           		field:'achievementsFour',
           		width: 100,
           		/*editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }*/
            },
            {
                title : '倒数第三</br>次绩效',
            	align: "center",
            	valign : 'middle',
            	field:'achievementsThree',
            	width: 100,
            	/*editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }*/
            },
            {
                 title : '倒数第二</br>次绩效',
             	 align: "center",
             	 field:'achievementsTwo',
             	 width: 100,
             	/*editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }*/
            },
            {
         		title : '最近一</br>次绩效',
         		align: "center",
         		valign : 'middle',
         		field:'achievementsOne',
         		width: 100,
         		/*editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }*/
             },
            {
           		title : '职级',
           		align: "center",
           		valign : 'middle',
           		field:'rank',
           		width: 100,
           		/*editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }*/
            },
            {
                 title : '后备人员',
             	 align: "center",
             	valign : 'middle',
             	 field:'backupPersonel',
             	 width: 100,
             	editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }
            },
            {
         		title : '员工导师',
         		align: "center",
         		valign : 'middle',
         		field:'staffMentor',
         		width: 100,
         		editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }
             },
            {
           		title : '是否骨干',
           		align: "center",
           		valign : 'middle',
           		field:'backbone',
           		width: 100,
           		formatter:function(value,row,index){
           			var val = value == null ? "-":value;
           			if(val=="-"){
           				return "-";
           			}else if(val=="0"){
           				return "是";
           			}else if(val=="1"){
           				return "否";
           			}
           		}
           		/*editable: {
         			type: 'select',
                    title: '是否211',
                    emptytext:'&#12288',
                    placement: 'bottom',
                    source:{
                    	"0":"是",
                    	"1":"否",
                    }
                },*/
            },
            {
                title : '是否OMP首期验</br>收的关键角色',
            	align: "center",
            	valign : 'middle',
            	field:'ompKeyrles',
            	width: 130,
            	editable: {
         			type: 'select',
                    title: '是否211',
                    emptytext:'&#12288',
                    placement: 'bottom',
                    source:{
                    	"0":"是",
                    	"1":"否",
                    }
                },
            },
            {
                 title : 'OMP是否在项目</br>启动两周内入项',
             	 align: "center",
             	 valign : 'middle',
             	 field:'entryData',
             	 width: 130,
             	editable: {
         			type: 'select',
                    title: '是否211',
                    emptytext:'&#12288',
                    placement: 'bottom',
                    source:{
                    	"0":"是",
                    	"1":"否",
                    }
                },
            },
            {
         		title : '当前项目</br>入项时间',
         		align: "center",
         		valign : 'middle',
         		field:'projectEntry',
         		width: 100,
         		formatter:function(value,row,index){
         			if(value){
           				var val = value.split(" ");
            			return val[0];
           			}else{
           				return null;
           			}
        		},
        		/*editable: {
                    type: 'date',
                    placement: 'bottom',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',    
	                language: 'zh-CN',
	                emptytext:'&#12288',
	                datepicker: {
	                    weekStart: 1,
	                },
	                validate: function (v) {
                    	if(!v){
                    		return '毕业时间不能为空!';
                    	}
                    	
                    }
                }*/
             },
            {
           		title : '当前项目</br>出项时间',
           		align: "center",
           		valign : 'middle',
           		field:'projectOutput',
           		width: 100,
           		formatter:function(value,row,index){
           			if(value){
           				if(value){
               				var val = value.split(" ");
                			return val[0];
               			}else{
               				return null;
               			}
           			}else{
           				return null;
           			}
        		},
        		/*editable: {
                    type: 'date',
                    placement: 'bottom',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',    
	                language: 'zh-CN',
	                emptytext:'&#12288',
	                datepicker: {
	                    weekStart: 1,
	                },
	                validate: function (v) {
                    	if(!v){
                    		return '毕业时间不能为空!';
                    	}
                    	
                    }
                }*/
            },
            {
                title : '从事当前岗</br>位起始时间',
            	align: "center",
            	valign : 'middle',
            	field:'jobStart',
            	width: 120,
            	formatter:function(value,row,index){
            		if(value){
           				var val = value.split(" ");
            			return val[0];
           			}else{
           				return null;
           			}
        		},
        		/*editable: {
                    type: 'date',
                    placement: 'bottom',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',    
	                language: 'zh-CN',
	                emptytext:'&#12288',
	                datepicker: {
	                    weekStart: 1,
	                },
	                validate: function (v) {
                    	if(!v){
                    		return '毕业时间不能为空!';
                    	}
                    	
                    }
                }*/
            },
            {
                 title : '上次调</br>薪时间',
             	 align: "center",
             	 valign : 'middle',
             	 field:'lastAdjustmeny',
             	 width: 100,
             	formatter:function(value,row,index){
             		if(value){
           				var val = value.split(" ");
            			return val[0];
           			}else{
           				return null;
           			}
        		},
        		/*editable: {
                    type: 'date',
                    placement: 'bottom',
	                format: 'yyyy-mm-dd',    
	                viewformat: 'yyyy-mm-dd',    
	                language: 'zh-CN',
	                emptytext:'&#12288',
	                datepicker: {
	                    weekStart: 1,
	                },
	                validate: function (v) {
                    	if(!v){
                    		return '毕业时间不能为空!';
                    	}
                    }
                }*/
            },
            {
          		title : '激励矩</br>阵分类',
          		align: "center",
          		field:'incentiveMatrix',
          		width: 100,
          		editable: {
                    type: "text", 
                    emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                    placement: 'bottom'         //编辑框的模式：支持popup和inline两种模式，默认是popup
                }
            },
    	],
    	locale:'zh-CN',//中文支持,
    });
	//返回
	$('#edit_backBtn').click(function(){
		$("#editPage").modal('hide');
		$('#editForm').data('bootstrapValidator').resetForm(true);
	});
	//保存
	$('#edit_saveBtn').click(function(){
		var cars=["A","B+","B","C","D","0",""];
		var val = [];
    	$('#editForm').bootstrapValidator('validate');
    	if($("#editForm").data('bootstrapValidator').isValid()){
    		val.push($("#achievementsOne").val());
    		val.push($("#achievementsTwo").val());
    		val.push($("#achievementsThree").val());
    		val.push($("#achievementsFour").val());
    		console.log(val);
    		var a = 0;
    		for(var i=0;i<val.length;i++){
    			if(cars.indexOf(val[i])<0){
    				a = -1;
    			}
    		}
    		if(a==-1){
    			toastr.error('绩效打分类型错误!');
    		}else{
    			$.ajax({
     				url:getRootPath()+"/MemberInforController/saveProjectInformation",
     				type : 'post',
     				dataType: "json",
     				contentType : 'application/json;charset=utf-8', //设置请求头信息
     				data:JSON.stringify($('#editForm').serializeJSON()),
     				success:function(data){
     					//后台返回添加成功
     					if(data.code=='success'){
     						$("#editPage").modal('hide');
     						var pageNumber=$('#mytab').bootstrapTable('getOptions').pageNumber;
                        	$('#mytab').bootstrapTable('refresh',{pageNumber:pageNumber});
                        	pageNumber=$('#mytab2').bootstrapTable('getOptions').pageNumber;
                        	$('#mytab2').bootstrapTable('refresh',{pageNumber:pageNumber});
     						toastr.success('编辑成功！');
     					}
     					else{
     						toastr.error('编辑失败!');
     					}
     				}
     			});
    		}
    	}
    });
	
	/*************************************** 导入 *************************************/

	  //打开文件上传页面
	  $('#btn_import').click(function(){
	    $("#membersImport").modal('show');
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
	  //从Excel中导入成员
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
	          url : getRootPath()+'/MemberInforController/importMembers',
	          type:'post',
	          dataType: 'json',
	          data : {
	            proNo : proNo
	          },
	          success : function(data) {
	            if(data){
	              if(data.zrAccountSize > 10){
	                toastr.error('中软工号长度大于10!');
	              }
	              if(data.post == "false"){
	                toastr.error('岗位不存在!');
	              }
	              if(data.education == "false"){
		            toastr.error('学历不存在!');
		          }
	              if(data.graduation == "false"){
		            toastr.error('毕业时间为空!');
		          }
	              if(data.entry == "false"){
		            toastr.error('入职时间为空!');
		          }
	              if(data.achievementsFour == "false"){
		            toastr.error('倒数第四次绩效为空!');
		          }
	              if(data.achievementsThree == "false"){
		            toastr.error('倒数第三次绩效为空!');
		          }
	              if(data.achievementsTwo == "false"){
		            toastr.error('倒数第二次绩效为空!');
		          }
	              if(data.achievementsOne == "false"){
		            toastr.error('最近一次绩效为空!');
		          }
	              if(data.rank == "false"){
		            toastr.error('职级为空!');
		          }
	              if(data.backbone == "false"){
		            toastr.error('是否骨干为空!');
		          }
	              if(Number(data.memberCount) > 0){
	                toastr.success('成功导入'+data.memberCount+'条数据!');
	              }
	              $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/MemberInforController/queryMembersInformation'});
	              $('#mytab2').bootstrapTable('refresh', {url: getRootPath() + '/MemberInforController/queryMembersInformation'});
	              $('#memberinformation').bootstrapTable('refresh', {url: getRootPath() + '/MemberInforController/memberinformation'});
	            }
	          }
	        };
	      $("#importForm").ajaxSubmit(option);
	        }
	      });
	      
	  //成员-模板下载
	  $(document).on("click", "#templateDownload", function () {
	    var modelName = 'member';
	    var downloadUrl = getRootPath() + '/project/projectTemplate?templateName='+modelName;
	    $("#templateDownload").attr('href', downloadUrl);
	  });

	  /********************************************** 导出 *************************************/

	  $("#btn_export").click(function(){
	    var $eleForm = $("<form method='POST'></form>");
	      $eleForm.attr("action",getRootPath() + "/MemberInforController/exportMembers");
	      $eleForm.append($('<input type="hidden" name="proNo1" value="'+ proNo +'">'));
	      $(document.body).append($eleForm);
	      //提交表单，实现下载
	      $eleForm.submit();
	  });
});
function tableHeight() {
    return $(window).height() - 30;
}
function projectInfo(){
	$.ajax({
		url : getRootPath() + '/MemberInforController/projectInformation',
		type : 'post',
		async : false,// 是否异步，true为异步
		data : {
			projectId : proNo,
		},
		success : function(data) {
			$("#pmName").empty();
			$("#interest").empty();
			$("#electronFlow").empty();
			$("#nowInterest").empty();
			$("#operate").empty();
			$("#tydOperate").empty();
			$("#projectPo").empty();
			$("#money").empty();
			$("#pmName").append(data.pm+'/'+data.pmid);
			$("#interest").append(data.interestRate);
			$("#electronFlow").append(data.electronFlow);
			$("#nowInterest").append(data.actualInterest);
			$("#operate").append(data.operate);
			$("#tydOperate").append(data.ytdOperate);
			$("#projectPo").append(data.op);
			$("#money").append(data.money);
		}
	});
}
function memberinformation(){
	$('#memberinformation').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/MemberInforController/memberinformation',
     	editable:true,// 可行内编辑
    	striped: true, // 是否显示行间隔色
    	dataField: "rows",
    	pageNumber: 1, // 初始化加载第一页，默认第一页
    	pagination:false,// 是否分页
    	queryParamsType:'limit',
    	sidePagination:'client',
    	pageSize:5,// 单页记录数
    	pageList:[5,10,20,30],// 分页步进值
    	showColumns:false,
    	toolbar:'#toolbar',// 指定工作栏
    	toolbarAlign:'right',
        dataType: "json",
        queryParams : function(params){
			var param = {
					projectId : proNo,
				}
			return param;
		},
    	columns:[     
    		{
         		title : '预算岗位',
         		align: "center",
         		field:'post',
         		width: 80,
             },
             {
          		title : '预算职级',
          		align: "center",
          		field:'rank',
          		width: 80,
              },
              {
           		title : '预算人数',
           		align: "center",
           		field:'budget',
           		width: 80,
               },
               {
            		title : '实际匹配人数',
            		align: "center",
            		field:'actual',
            		width: 100,
                },
    	],
    	locale:'zh-CN',// 中文支持,
    });
}
//function getSelectPosts(v){
//	$.ajax({
//		url:getRootPath() + '/MemberInforController/getSelectPosts',
//		type:'post',
//		async:false,
//		dataType: "json",
//		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
//		data:{
//			"val":v,
//		},
//		success:function(data){
//			dict = data.data
//		}
//	});
//}
function editData(id){
	$.ajax({
		url:getRootPath() + '/MemberInforController/editDataPosts',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{
			"id":id,
		},
		success:function(data){
			$("#id").val(data.id);
			$("#nameMember").val(data.name);
			$("#education").val(data.education);
			$("#rank").val(data.rank);
			$("#graduation").val(timeConversion(data.graduation));
			$("#entry").val(timeConversion(data.entry));
			var backbone =  (Number(data.backbone)+1);
            $("input[type='radio']").removeAttr('checked');
            $("#backbone"+backbone).attr("checked", true);
			$("#siling").val(data.siling);
			$("#achievementsFour").val(data.achievementsFour);
			$("#achievementsThree").val(data.achievementsThree);
			$("#achievementsTwo").val(data.achievementsTwo);
			$("#achievementsOne").val(data.achievementsOne);
			$("#jobStart").val(timeConversion(data.jobStart));
			$("#lastAdjustmeny").val(timeConversion(data.lastAdjustmeny));
		}
	});
	$("#editPage").modal('show');
}
function timeConversion(value){
	if(value){
		var val = value.split(" ");
		return val[0];
	}else{
		return null;
	}
	
}
