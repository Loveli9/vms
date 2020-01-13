var proNo = window.parent.projNo;
var ids = new Array();
var question = null;
var demoData = null;
var demoList = '';
$(function() {
	
	//转换日期格式(时间戳转换为datetime格式)
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
        	return "";
        }
    }
    
    $(document).on("click","#saveQMS",function(){
    	var list = [];
    	var qmsIds = [];
    	for(var i=0;i<ids.length;i++){
    		var conform = ids[i].involve;
    		var text = $("#id"+ids[i].qmsId).data('value');
    		var problemType = $("#type"+ids[i].qmsId).val();
    		if(((text=="-" || text=="") && (conform==""||conform==null) && (problemType==""||problemType==undefined)) || qmsIds.indexOf(ids[i].qmsId) != -1){
    			continue;
    		}
    		if(conform == "否" || conform == "免"){
    			if(text == null || text == "" || text == "　" || text == '符合度为"否"时,请填写问题描述！' || text == '符合度为"免"时,请填写相应原因！'){
    				toastr.error('符合度不为"是"时,问题描述不能为空！');
        			return;
    			}else if(conform == "否" && problemType == ""){
    				toastr.error('符合度为"否"时,问题类型不能为空！');
        			return;
    			}
			}
    		if(conform==""||conform==null){
    			toastr.error('符合度不能为空！');
    			return;
    		}
    		var myDate = new Date();
    		var date = myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate()+" "+myDate.getHours()+":"+myDate.getMinutes()+":"+myDate.getSeconds();
    		var planClosedTime = null;
    		if(ids[i].planClosedTime != null){
    			planClosedTime = (new Date(ids[i].planClosedTime)).format("yyyy-MM-dd hh:mm:ss");
    		}
			var creationTime = date;
			if(ids[i].creationTime != null){
				creationTime = (new Date(ids[i].creationTime)).format("yyyy-MM-dd hh:mm:ss");
			}
			var data ={
    				'no' : ids[i].no,
    	    		'qmsId':ids[i].qmsId,
    	    		'source' : ids[i].source,
    	    		'involve' : conform,
    	    		'evidence':text,
    	    		'problemType' : problemType,
    	    		'majorProblem' : ids[i].majorProblem,
    	    		'improvementMeasure':ids[i].improvementMeasure,
    	    		'planClosedTime' : planClosedTime,
    	    		'dutyPerson' : ids[i].dutyPerson,
    	    		'progress':ids[i].progress,
    	    		'state' : ids[i].state,
    	    		'creationTime' : creationTime,
    	    		'type' : 3,
    			};
			qmsIds.push(ids[i].qmsId);
    		list.push(data);
    	}
    	$.ajax({
            type: "post",
            url: getRootPath()+"/qmsMaturity/queryQMSresultsSaves",
				dataType: "json",
				contentType : 'application/json;charset=utf-8', //设置请求头信息
				data: JSON.stringify(list),
            success: function (data, status) {
                if (data.code == "success") {
                	sumConform();
                	parent.sumScore();
                	$('#qmsProjectManagement').bootstrapTable('refresh');
                	toastr.success('修改成功！');
                }else{
                	toastr.error('修改失败！');
                }
            }
        });
    });
    
    $("#exportQMS").click(function(){
        var $eleForm = $("<form method='POST'></form>");
        $eleForm.attr("action",getRootPath() + "/export/downloadProblemQMS");
        $eleForm.append($('<input type="hidden" name="no" value="'+ proNo +'">'));
        $(document.body).append($eleForm);
        //提交表单，实现下载
        $eleForm.submit();
    })
    
    $(document).ready(function(){
    	selectionTags();
	})
})

//根据返回的标签页名称调用函数
function selectionTags(){
		if(getQueryString("qmsName") == '问题跟踪'){
			$("#multipleMenu").show();
			$("#saveQMS").hide();
			$("#exportQMS").show();
			$("#conforms").hide();
			//责任人账号
			$.ajax({
				url : getRootPath() + "/qmsMaturity/getPersonLiable",
				type : 'POST',
				async: false,//是否异步，true为异步
				data : {
					no : proNo
				},
				success : function(data) {
					demoData = data;
			        $.each(data, function (index, item) {
						demoList += '<option value="'+item.dutyPerson+'">'+item.followPerson+'</option>';
					});
				}
			});
			auditTrail();
			multipleMenu();
		}else if(getQueryString("qmsName") == '质量控制'){
			$("#multipleMenu").show();
			$("#saveQMS").show();
			$("#exportQMS").hide();
			$("#conforms").show();
			sumConform();
			auditResults();
			multipleMenu();
			getSelectQuestion(getQueryString("qmsName"));
		}else{
			$("#multipleMenu").hide();
			$("#saveQMS").show();
			$("#exportQMS").hide();
			$("#conforms").show();
			sumConform();
			auditResults();
			getSelectQuestion(getQueryString("qmsName"));
		}
}
//获取多选下列菜单数据
function multipleMenu(){
	$.ajax({
		url: getRootPath()+"/qmsMaturity/multipleMenu",
        type: 'POST',
        async: false,//是否异步，true为异步
        data: {
        	'source' : getQueryString("qmsName")
        },
        success: function (data) {
        	for(var i=0;i<data.data.length;i++){
        		$("#multiple").append("<option value='"+data.data[i]+"'>" + data.data[i] + "</option>");
        	}
        }
    });
}
//多选菜单触发函数
function selectMultipleMenu(){
	$('#qmsProjectManagement').bootstrapTable('refresh');
}
//获取每个标签页的符合度
function sumConform(){
		$.ajax({
			url: getRootPath()+"/qmsMaturity/sumConform",
	        type: 'POST',
	        async: false,//是否异步，true为异步
	        data: {
	        	'no' : proNo,
	        	'source' : getQueryString("qmsName")
	        },
            success: function (data) {
            	$("#conform").text(data.data.conform);
            }
        });
}

//获取前5个标签页的数据
function auditResults(){
	$('#qmsProjectManagement').bootstrapTable({
		method : 'post',
		contentType : "application/x-www-form-urlencoded",
		url : getRootPath() + "/qmsMaturity/queryQMSresults",
		height : tableHeight(),
		striped : false, // 是否显示行间隔色
		dataField: "data",
		minimumCountColumns : 2, // 最少允许的列数
		queryParams : function(params) {
			var param = {
				'no' : proNo,
				'source' : getQueryString("qmsName"),
				'select' : encodeURI($('#multiple').val())
			}
			return param;
		},
		onEditableSave: function (field, row, oldValue, $el) {
			evidenceJudge(row);
        },
		columns : [
			{
				title : '序号',align : "center",width : "5%",valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "center !important","overflow": "hidden",
                            "text-overflow":"ellipsis","height": "50px"
                        }
                    };
                },
				formatter: function (value, row, index) {
					row.no = proNo;
                	return index+1;
                },
			},
			{
				title : '主要评估项及内容',field : 'content',align : "left",width : "16%",valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "left !important","padding-left": "5px",
                        }
                    };
                },
                formatter: function (value, row, index) {
                	if(value==null){
                		return "";
                	}else{
                		return "<span title=\""+value+"\">"+value+"</span>";
                	}
                },
			},
			{
				title : '查证参考',field : 'reference',align : "left",width : "22%",valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "left !important","padding-left": "5px",
                        }
                    };
                },
                formatter: function (value, row, index) {
                	if(value==null){
                		return "";
                	}else{
                		return "<span title=\""+value+"\">"+value+"</span>";
                	}
                },
			},
			{
				title : '访谈对象',field : 'interviewee',align : "center",width : "9%",valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "center !important","overflow": "hidden",
                            "text-overflow":"ellipsis",
                        }
                    };
                },
                formatter: function (value, row, index) {
                	if(value==null){
                		return "";
                	}else{
                		return "<span title=\""+value+"\">"+value+"</span>";
                	}
                },
			},
			{
				title : '是否符合',field : 'involve',align : "center",width : "5%",valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "center !important","overflow": "hidden",
                            "text-overflow":"ellipsis",
                        }
                    };
                },
                formatter: function (value, row, index) {
                	var types = ["是","否","免"];
                	var select = '<select id="conformTo'+row.qmsId+'" style="width:auto;text-align-last: center;" onchange=conformChange('+row.qmsId+','+index+');>';
                	if(value == null){
                		select = '<select id="conformTo'+row.qmsId+'" style="width:auto;text-align-last: center;" onchange=conformChange('+row.qmsId+','+index+');><option value="">未选择</option>';
                	}
                	for(var i=0;i<3;i++){
                		if(value == types[i]){
                			select += '<option value="'+types[i]+'" selected>'+types[i]+'</option>';
                		}else{
                			select += '<option value="'+types[i]+'">'+types[i]+'</option>';
                		}
                	}
                	select += '</select>';
                	return select;
                }
			},
			{
				title : '问题类别',field : 'problemType',align : "left",width : "13%",valign : 'middle',align : "center",
                formatter: function (value, row, index) {
                	var select = '<select id="type'+row.qmsId+'" style="width:90%;"><option value="">请选择类型</option>';
                	for(var i=0;i<question.length;i++){
                		if(value == question[i].text){
                			select += '<option value="'+question[i].text+'" selected = "selected">'+question[i].value+'</option>';
                		}else{
                			select += '<option value="'+question[i].text+'">'+question[i].value+'</option>';
                		}
                	}
                	select += '</select>';
                	return select;
                },
			},
			{
				title : '问题描述',field : 'evidence',align : "left",width: '25%',valign: 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "left !important",
                            "overflow": "hidden",
                            "text-overflow":"ellipsis",
                        }
                    };
                },
                editable: {
                    type: 'textarea',
                    title: '问题描述',
                    emptytext:'&#12288',
                    placement: 'bottom',
                    noeditFormatter: function (value,row,index) {
                    	var dataValue = value;
                    	if(value == null || value == ""){
                    		value = "-";
                    		dataValue = "";
                    	}
                    	return '<a id="id'+row.qmsId+'" class="describe" style="width:100%;" href="javascript:void(0)" data-name="evidence" data-pk="1" data-value="'+dataValue+'" class="editable editable-pre-wrapped editable-click" style="width: max-content;" data-original-title="" title="">'+value+'</a>';
                    }
                }
			},
		],
		onLoadSuccess: function (res) {
        	ids = res.data;
        },
		locale : 'zh-CN'// 中文支持
	});
}
function conformChange(id,index){
	ids[index].involve = $('#conformTo'+id).val();
	evidenceJudge(ids[index]);
}
function evidenceJudge(row){
	var proof = $("#id"+row.qmsId);
	if((row.involve == "否" || row.involve == "免") && (row.evidence == "-" || row.evidence == "" || row.evidence == null)){
		proof.css("color","red");
		if(row.involve == "否"){
			proof.text('符合度为"否"时,请填写问题描述！');
		}else if(row.involve == "免"){
			proof.text('符合度为"免"时,请填写相应原因！');
		}
	}else if(row.involve == "是" && (row.evidence == null || row.evidence == "")){
		proof.css("color","blue");
		proof.text('-');
	}else if(row.evidence != null || row.evidence != ""){
		proof.css("color","blue");
	}
}
//审计问题跟踪数据
function auditTrail(){
	var str = encodeURI($('#multiple option:selected').val());
	$('#qmsProjectManagement').bootstrapTable({
		method : 'post',
		contentType : "application/x-www-form-urlencoded",
		url : getRootPath() + "/qmsMaturity/queryQMSresults",
		height : tableHeight(),
		striped : false, // 是否显示行间隔色
		dataField: "data",
		minimumCountColumns : 2, // 最少允许的列数
		queryParams : function(params) {
			var param = {
				'no' : proNo,
				'source' : getQueryString("qmsName"),
				'select' : encodeURI($('#multiple').val())
			}
			return param;
		},
		onEditableSave: function (field, row, oldValue, $el) {
            $.ajax({
                type: "post",
                url: getRootPath()+"/qmsMaturity/queryQMSresultsSave",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data: JSON.stringify(row),
                success: function (data, status) {
                    if (data.code == "success") {
                    	toastr.success('修改成功！');
                    }else{
                    	toastr.error('修改失败！');
                    }
                }
            });
        },
		columns : [
			{
				title : '序号',align : "center",width : 35,valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "center !important","overflow": "hidden",
                            "text-overflow":"ellipsis","height": "50px"
                        }
                    };
                },
				formatter: function (value, row, index) {
                	return index+1;
                },
			},
			{
				title : '评估要素',field : 'source',align : "center",width : 50,valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                    	css: {
                            "text-align": "left !important","padding-left": "5px",
                        }
                    };
                },
                formatter: function (value, row, index) {
                	return '<span id="pinggu'+index+'">'+value+'</span>';
                },
			},
			{
				title : '主要评估项及内容',field : 'content',align : "left",width : 150,valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "left !important","padding-left": "5px",
                        }
                    };
                },
                formatter: function (value, row, index) {
                	if(value==null){
                		return "";
                	}else{
                		return "<span title=\""+value+"\">"+value+"</span>";
                	}
                },
			},
			{
				title : '问题描述',field : 'evidence',align : "left",width: 150,valign: 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
					return {
                        css: {
                            "text-align": "left !important","padding-left": "5px",
                        }
                    };
                },
                formatter: function (value, row, index) {
                	if(value==null){
                		return "";
                	}else{
                		return "<span class='describe' title=\""+value+"\">"+value+"</span>";
                	}
                },
			},
			{
				title : '问题类别',field : 'problemType',align : "left",width : "13%",valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "left !important","padding-left": "5px",
                        }
                    };
                },
                formatter: function (value, row, index) {
                	if(value==null){
                		return "<span class='describe' title='请选择问题类别'>请选择问题类别</span>";
                	}
                	var data = getSelectQuestion(row.source);
                	for(var i=0;i<data.length;i++){
                		if(value == data[i].text){
                			return "<span class='describe' title=\""+data[i].value+"\">"+data[i].value+"</span>";
                		}
                	}
                },
			},
			{
				title : '原因分析',field : 'majorProblem',align : "left",width : 150,valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
					return {
                        css: {
                            "text-align": "left !important",
                            "overflow": "hidden",
                            "text-overflow":"ellipsis",
                        }
                    };
                },
                editable: {
                    type: 'textarea',
                    title: '原因分析',
                    emptytext:'&#12288',
                    placement: 'bottom',
                    noeditFormatter: function (value,row,index) {
                    	var dataValue = value;
                    	if(value == null || value == ""){
                    		value = "-";
                    		dataValue = "";
                    	}
                    	return '<a href="javascript:void(0)" class="describe" data-name="majorProblem" data-pk="1" data-value="'+dataValue+'" class="editable editable-pre-wrapped editable-click">'+value+'</a>';
                    }
                }
			},
			{
				title : '改进措施',field : 'improvementMeasure',align : "left",width: 150,valign: 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "left !important",
                            "overflow": "hidden",
                            "text-overflow":"ellipsis",
                        }
                    };
                },
                editable: {
                    type: 'textarea',
                    title: '改进措施',
                    emptytext:'&#12288',
                    placement: 'bottom',
                    noeditFormatter: function (value,row,index) {
                    	var dataValue = value;
                    	if(value == null || value == ""){
                    		value = "-";
                    		dataValue = "";
                    	}
                    	return '<a href="javascript:void(0)" class="describe" data-name="improvementMeasure" data-pk="1" data-value="'+dataValue+'" class="editable editable-pre-wrapped editable-click">'+value+'</a>';
                    }
                }
			},
			{
				title : '责任人',field : 'dutyPersonName',align : "center",width : 95,valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "center !important","overflow": "hidden",
                            "text-overflow":"ellipsis",
                        }
                    };
                },
                formatter:function(value,row,index){
        			return '<input id="dutyPersonName" list="demoList" name="dutyPersonName" onchange="changeValue(this,'+row.qmsId+')" style="width: 90px; text-align: center;padding-left: 15px;" value="'+(value == null || value == undefined ? "" : value)+'" />'+
    	            '<datalist id="demoList" style="display:none;">'+demoList+'</datalist>';
        		},
			},
			{
				title : '计划关闭时间',field : 'planClosedTime',align : "center",width : 70,valign : 'middle',
        		formatter:function(value,row,index){
        			return changeDateFormat(value);
        		},
        		editable: {
	                type: 'date',
	                placement: 'bottom',
	                title: '计划关闭时间',
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
				title : '状态',field : 'state',align : "center",width : 40,valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "center !important","overflow": "hidden",
                            "text-overflow":"ellipsis",
                        }
                    };
                },
                editable: {
                    type: "select",              
                    title: '状态',
                    emptytext:'&#12288',
                    placement: 'bottom',
                    source: {
                        "Open":"Open",
                        "Closed":"Closed",
                    }
                }
			},
			{
				title : '进展',field : 'progress',align : "left",width: 150,valign: 'middle',
				cellStyle: function cellStyle(value, row, index) {                    
                    return {
                        css: {
                            "text-align": "left !important",
                            "overflow": "hidden",
                            "text-overflow":"ellipsis",
                        }
                    };
                },
                editable: {
                    type: 'textarea',
                    title: '进展',
                    emptytext:'&#12288',
                    placement: 'bottom',
                    noeditFormatter: function (value,row,index) {
                    	var dataValue = value;
                    	if(value == null || value == ""){
                    		value = "-";
                    		dataValue = "";
                    	}
                    	return '<a href="javascript:void(0)" class="describe" data-name="progress" data-pk="1" data-value="'+dataValue+'" class="editable editable-pre-wrapped editable-click">'+value+'</a>';
                    }
                }
			},
		],
		locale : 'zh-CN'// 中文支持
	});
}
//获取问题类别下拉菜单
function getSelectQuestion(val){
	var s;
	$.ajax({
		url:getRootPath() + '/qmsMaturity/getSelectQuestion',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{
			'source' : encodeURI(val)
		},
		success:function(data){
			question = data.data;
			s = data.data;
		}
	});
	return s;
}

function saveSelectQuestion(id){
	var val = $("#type"+id).val();
	if(val != 0){
		$.ajax({
			url:getRootPath() + '/qmsMaturity/saveSelectQuestion',
			type:'post',
			async:false,
			dataType: "json",
			contentType : 'application/x-www-form-urlencoded', //设置请求头信息
			data:{
				'qmsId' : id,
				'pid' : val,
				'no' : proNo,
			},
			success: function (data, status) {
	            if (data.code == "success") {
	            	toastr.success('修改成功！');
	            }else{
	            	toastr.error('修改失败！');
	            }
	        }
		});
	}
}
function tableHeight() {
	return $(window).height() - 1;
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
function getQueryString(name){
	var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null){
		return decodeURIComponent(r[2]); 
	}
	return '';
}
function changeValue(obj,id){
	var account = "";
    $(obj).attr("value", $(obj).val());
    $.each(demoData, function (index, item) {
    	if(item.dutyPerson == $(obj).val()){
    		account = item.followPerson;
    		return;
    	}
	});
    account = account=="" ? $(obj).val() : account;
    if(account == ""){
    	return;
    }
    $.ajax({
		url:getRootPath() + '/qmsMaturity/savePersonLiable',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{
			'qmsId' : id,
			'account' : account,
			'no' : proNo,
		},
		success: function (data, status) {
            if (data.code == "success") {
            	toastr.success('修改成功！');
            	
            }else{
            	toastr.error('修改失败！');
            }
        }
	});
}