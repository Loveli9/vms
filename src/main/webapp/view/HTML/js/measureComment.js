var proNo = window.parent.projNo;
$(function(){
	//指标点评查询区间列表下拉菜单初始化
	initCateGory(proNo);
	//指标点评列表初始化
	initMeasureTable(proNo);
	//指标点评查询区间配置列表初始化
	initconfigTable(proNo);
	//导出指标点评列表
	/*$("#exportbut").click(function(){
		var text = $('#labelSel option:selected').val();
		if(!text){
			toastr.warning('导出信息为空');
		}else{
			var $eleForm = $("<form method='POST'></form>");
	        $eleForm.attr("action",getRootPath() + "/measureComment/qualityReviewExport");
	        $eleForm.append($('<input type="hidden" name="proNo" value="'+ proNo +'">'));
	        $(document.body).append($eleForm);
	        $eleForm.submit();
		}
	})*/
	//打开点评查询配置页面
	$('#configBtn').click(function(){		
		$('#configTable').bootstrapTable('refresh');
		$("#iteConfigPage").modal('show');	
	});	
	//新增一行点评查询配置
	$('#addBtn').click(function(){
		var count = $('#configTable').bootstrapTable('getData').length;
	    $('#configTable').bootstrapTable('insertRow',{index:count,row:{proNo:proNo}});	
	});
	//保存点评查询配置
	$('#saveBtn').click(function(){
		var dataArr = $('#configTable').bootstrapTable('getData');
		var list=[];
		for(var i=0;i<dataArr.length;i++){
			var data ={
    				'proNo' : dataArr[i].proNo,
    	    		'startDate' : formatDate(dataArr[i].startDate),
    	    		'endDate':formatDate(dataArr[i].endDate)
    			}
    			list.push(data);    
		}
		$.ajax({
			url: getRootPath() + "/measureComment/saveMeasureCommentConfig",
    		type:'post',
    		dataType:"json",  
    	    contentType : 'application/json;charset=utf-8', //设置请求头信息 
    		data:JSON.stringify(list),
    		success:function(data){
    			if(data.code == 'success'){
    				toastr.success('配置成功！');
    				initCateGory(proNo);
    				$('#measureTable').bootstrapTable('refresh');
    				$("#iteConfigPage").modal('hide'); 			
    			}else{
    				toastr.error('配置失败!');
    			}
    		}
    	});
	});
})
//类目选择事件
function cateGoryChange(){
	$('#measureTable').bootstrapTable('refresh');
}

//日期转换函数
function formatDate(val){
	if (val != null) {
        var date = new Date(val);
        return date.format("yyyy-MM-dd");
    }
}

/**
 * 合并单元格
 * @param data  原始数据（在服务端完成排序）
 * @param fieldName 合并属性名称
 * @param colspan   合并列
 * @param target    目标表格对象
 */ 
function mergeCells(data,fieldName,colspan,target){ 
	//声明一个map计算相同属性值在data对象出现的次数和 
	var sortMap = {};
	for(var i = 0 ; i < data.length ; i++){
		for(var prop in data[i]){
			if(prop == fieldName){
				var key = data[i][prop];
				if(sortMap.hasOwnProperty(key)){
                    sortMap[key] = sortMap[key] * 1 + 1;
				} else {
                    sortMap[key] = 1;
                }
				break;
			}
		}		    	
	}
	var index = 0;
    for(var prop in sortMap){
    	var count = sortMap[prop] * 1;
    	$(target).bootstrapTable('mergeCells',{index:index, field:fieldName, 
    										   colspan: colspan, rowspan: count});
    	index += count;
    }
}

function initCateGory(proNo){
	//查询区间下拉框
	$.ajax({
		url: getRootPath() + "/measureComment/queryMeasureCommentConfig",
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			'proNo':proNo
		},
		success: function (data) {
			var select_option="";
			$("#labelSel").empty();
			if(null==data.data||""==data.data) return;
			var dataArr = data.data;
			for(var i=0;i<dataArr.length;i++){
				var val =formatDate(dataArr[i].startDate)+"~" +formatDate(dataArr[i].endDate);
				select_option += "<option value="+val+">"+val+"</option>";
			}			
			$("#labelSel").html(select_option);
		}
	});
}

function initconfigTable(proNo){
	//点评查询配置列表
	$('#configTable').bootstrapTable({
		method: 'get',
    	contentType: "application/x-www-form-urlencoded",
    	url: getRootPath() + "/measureComment/queryMeasureCommentConfig",
    	striped: true, //是否显示行间隔色
    	dataField: 'data',
    	minimumCountColumns: 2,             //最少允许的列数
		queryParams : function(params){
			var param = {
        			'proNo':proNo
				}
			return param;
		},
		columns:[
			{
				title:'序号',
				align:'center',
				width:60,
				formatter:function(value, row, index) {
				    var pageSize=$('#configTable').bootstrapTable('getOptions').pageSize;
					var pageNumber=$('#configTable').bootstrapTable('getOptions').pageNumber;
					 return pageSize * (pageNumber - 1) + index + 1;
					}
        	},
        	{title:'id',
        		filed:'id',
        		visible : false
        	},
        	{
				title:'操作',
				align:'center',
				width:60,
				formatter:function(value, row, index) {	
					 return '<a name="del" onclick="remove(' + row.id + ')">移除</a>';
					}
        	},
        	{
        		title:'开始时间',
        		halign :'center',
        		align : 'center',
        		field:'startDate',
        		sortable:true,
        		width:120,
        		formatter:function(value,row,index){
        			return formatDate(value);
        		},
        		editable: {
        			type: 'date',
                    placement: 'bottom',
                    format: 'yyyy-mm-dd',
                    viewformat: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    datepicker: {
                        weekStart: 1,
                    },
                    validate: function (v) {
                      	if(!v){
                      		return '开始时间不能为空！';
                      	}
                    }  	                
                }        		  
        	},
        	{
        		title:'结束时间',
        		halign :'center',
        		align : 'center',
        		field:'endDate',
        		sortable:true,
        		width:120,
        		formatter:function(value,row,index){
        			return formatDate(value);
        		},
        		editable: {
        			type: 'date',
                    placement: 'bottom',
                    format: 'yyyy-mm-dd',
                    viewformat: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    datepicker: {
                        weekStart: 1,
                    },
                    validate: function (v) {
                      	if(!v){
                      		return '结束时间不能为空！';
                      	}
                    }  	                
                }
        	}
        ],
		locale:'zh-CN'//中文支持
	});
}

function remove(id){
	if(id != undefined){
		$.ajax({
            type: "post",
            url: getRootPath()+"/measureComment/cleanMeasureCommentConfig",
				data: {
					'id' : id
				},
            success: function (data, status) {
                if (status == "success") {
                	toastr.success('移除成功！');
                	$('#configTable').bootstrapTable('refresh');
                	$('#measureTable').bootstrapTable('refresh');
                }else{
                	toastr.success('移除失败！');
                }
            }
        });
	}else{
		$('#configTable').bootstrapTable('refresh');
	}	
}

//指标点评数据列表查询条件
function queryParams(params) {
	var labelSel = $('#labelSel')[0];
	var text = $('#labelSel option:selected').val();
	if(labelSel.selectedIndex<labelSel.length-1){
		text = text + ',' + labelSel[(labelSel.selectedIndex+1)].value;
	}
	var param = {
			'proNo':proNo,
			'text':text
		}
	return param;
}

function initMeasureTable(proNo){	
	//指标点评数据列表
	$('#measureTable').bootstrapTable({
		method: 'get',
    	contentType: "application/x-www-form-urlencoded",
    	url: getRootPath() + "/measureComment/queryMeasureCommentList",
    	striped: true, //是否显示行间隔色
    	dataField: 'data',
    	minimumCountColumns: 2,             //最少允许的列数
		queryParams : queryParams,
        onEditableSave: function (field, row, oldValue, $el) {
        	$.ajax({
                type: "post",
                url: getRootPath()+"/measureComment/saveMeasureComment",
 				data: {
 					'proNo':proNo,
 					'comment':row.comment,
 					'id':row.id,
        			'text':$('#labelSel option:selected').val()
 				},
                success: function (data, status) {
                    if (status == "success") {
                      	$('#measureTable').bootstrapTable('refresh');
                    	toastr.success('保存成功！');
                    }else{
                    	toastr.success('保存失败！');
                    }
                }
            });
        },
		columns:[
			{
				field : 'id',
	    		align: "center",
	    		visible : false	
			},
			{
				title : '指标分类',
				field : 'measureCategory',
				align: "center",
				width: 80,
				valign:'middle'
			},
			{
        		title : '指标名称',
        		field : 'measureName',
        		align: "center",
        		width: 80,
        		valign:'middle'
            },
            {
        		title : '单位',
        		field : 'unit',
        		align: "center",
        		width: 80,
        		valign:'middle'
            },
            {
        		title : '目标',
        		field : 'target',
        		align: "center",
        		width: 80,
        		valign:'middle'
            },
            {
        		title : '下限',
        		field : 'lower',
        		align: "center",
        		width: 80,
        		valign:'middle'
            },
            {
        		title : '上限',
        		field : 'upper',
        		align: "center",
        		width: 80,
        		valign:'middle'
            },
            {
        		title : '采集时间',
        		field : 'updateTime',
        		align: "center",
        		width: 80,
        		valign:'middle',
        		formatter:function(value,row,index){
        			if (value != null) {
        		        var date = new Date(value);
        		        return date.format("yyyy-MM-dd hh:mm:ss");
        		    }
        		}
            },
            {
        		title : '实际值',
        		field : 'measureValue',
        		align: "left",
        		width: 80,
        		valign:'middle',
        		formatter:function(value,row,index){
        			if(value){
        				if(row.oldValue){
        					if(parseFloat(value)>parseFloat(row.oldValue)){
        						if(parseFloat(value)<=parseFloat(row.upper)&&parseFloat(value)>=parseFloat(row.lower)){
        							return '<img src="/mvp/view/HTML/images/tendency/greenupper.png"/>&nbsp;&nbsp;'+value;
            					}else{
            						return '<img src="/mvp/view/HTML/images/tendency/redupper.png"/>&nbsp;&nbsp;'+value;
            					}
        					}else if(parseFloat(value)==parseFloat(row.oldValue)){
        						if(parseFloat(value)<=parseFloat(row.upper)&&parseFloat(value)>=parseFloat(row.lower)){
        							return '<img src="/mvp/view/HTML/images/tendency/greenequal.png"/>&nbsp;&nbsp;'+value;
            					}else{
            						return '<img src="/mvp/view/HTML/images/tendency/redequal.png"/>&nbsp;&nbsp;'+value;
            					}
        					}else if(parseFloat(value)<parseFloat(row.oldValue)){
        						if(parseFloat(value)<=parseFloat(row.upper)&&parseFloat(value)>=parseFloat(row.lower)){
        							return '<img src="/mvp/view/HTML/images/tendency/greenlower.png"/>&nbsp;&nbsp;'+value;
            					}else{
            						return '<img src="/mvp/view/HTML/images/tendency/redlower.png"/>&nbsp;&nbsp;'+value;
            					}
        					}
        				}else{
        					return value;
        				}
        				
        			}
        		}
            },
            {
        		title : '点评',
        		field : 'comment',
        		align: "center",
        		width: 280,
        		valign:'middle',
        		editable: {
                    type: 'textarea',//编辑框的类型。支持text|textarea|select|date|checklist等
                    title: '点评',
                    emptytext:'&#12288',
                    mode: 'popup',//编辑框的模式：支持popup和inline两种模式，默认是popup
                    placement: 'bottom'
                }
            }
        ],
        onLoadSuccess : function(data) { 
        	var data = $('#measureTable').bootstrapTable('getData', true); 
        	//合并单元格
        	mergeCells(data, "measureCategory", 1, $('#measureTable')); 
        },
		locale:'zh-CN'//中文支持
	});
}