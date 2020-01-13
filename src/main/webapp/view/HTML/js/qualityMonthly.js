$(function(){
    var proNo = window.parent.projNo;
    var bsTable;
    var mode = "inline"//编辑框的模式：支持popup和inline两种模式，默认是popup
    var mode1 = "popup"//编辑框的模式：支持popup和inline两种模式，默认是popup

	function projectStartTime() {//获取项目开始时间
		$.ajax({
			type : "post",
			url : getRootPath() + '/projectReport/projectStartTime',
			data : {
				proNo : proNo,
			},
			success : function(data) {
				qualityMonthlySection(data);
			}
		});
	}
    

    
	function qualityMonthlySection(data) {
		var myDate = new Date();
		var year = myDate.getYear() + 1900;
		var month = myDate.getMonth() + 1;
		var proStartTime = data.split("-");
		var startYear = proStartTime[0] < 2019 ? 2019 : proStartTime[0];
		var startMonth = proStartTime[0] < 2019 ? 1 : proStartTime[1];
		var months = (year - startYear) * 12 - startMonth + month;// 计算项目开始到现在一共多少个月
		$("#monthlySection").append("<option selected ='selected' value='" + (year + "-" + month + "-01")+ "'>" + (year + "年" + month + "月") + "</option>");
		for (var i = 0; i < months; i++) {
			myDate.setMonth(myDate.getMonth() - 1);
			var m = myDate.getMonth() + 1;
			m = m < 10 ? "0" + m : m;
			// 在这里可以自定义输出的日期格式
			$("#monthlySection").append("<option value='" + (myDate.getFullYear() + "-" + m + "-01") + "'>" + (myDate.getFullYear() + "年" + m + "月")+ "</option>");
		}
		initTableSave();
	}
	$('#monthlySection').change(function(){//刷新表格数据且使按钮和计划进度点灯及需求范围点灯列表更具时间变为不可编辑
		initTableSave();
    });
	function initTableSave() {
		$('#mytable').bootstrapTable('destroy');
		var columns = initColumn();
		var queryParams = {
			proNo : proNo,
			month : $("#monthlySection").val(),
		};
		var url = getRootPath() + '/projectReport/getQualityonthlyReportList';
		bsTable = new BSTable('mytable', url, columns);
		bsTable.setClasses('tableCenter');
		bsTable.setQueryParams(queryParams);
		bsTable.setPageSize(5);
		bsTable.setEditable(true);// 开启行内编辑
		// bsTable.setEditable(false);
		bsTable.setOnEditableSave(initOnEditableSave());
		bsTable.init();
	}
    function initOnEditableSave(){
        return function (field, row, oldValue, $el) {
            $.ajax({
                type: "post",
                url: getRootPath() + '/projectReport/editProjectAssess',
                dataType: "json",
                contentType: 'application/json;charset=utf-8',//设置请求头信息
                data: JSON.stringify(row),
                success: function (data, status) {
                    if (status == "success") {
                        bsTable.refresh();
                        toastr.success('修改成功！');
                    } else {
                        toastr.success('修改失败！');
                    }
                }
            });
        }
    }

    function initColumn(){
    	var month = $("#monthlySection").val();
		var myDate = new Date();
		var nowMonth = (myDate.getYear() + 1900) + "-" + (myDate.getMonth() + 1) + "-01";
		var editData = "";
		var demandProgressEdit = "";
        if(month != nowMonth){
        	editData = ' style="color:#676a6c;cursor:auto;" ';
        	demandProgressEdit = editData;
        	$("#refreshData").attr('disabled',true);
			$("#refreshData").css({"background-color": "#B5B5B5"});
        }else{
        	editData = ' id="editData" ';
        	demandProgressEdit = ' id="demandProgressEdit" ';
        	$("#refreshData").attr('disabled',false);
			$("#refreshData").css({"background-color": "#e41f2b"});
        }
        return [
            {title:'项目编号 ',field:'no',width:80,visible: false},
            {title:'项目名称',field:'name',width:170,visible: true},
            {title:'人员点灯',field:'peopleLamp',width:110,
                formatter:function(value,row,index){
                	if(value =="--"){
                		var val = 0 ;
                		var vals = value ;
                	}else{
                		var val = parseFloat(value==null?0:value);
                		if(val<0){
                			val = 0 ;
                		}
                		if(val>100){
                			val = 100 ;
                		}
                		var vals = val+'%';
                	}
                    var val1 = 85;
                    var val2 = 60;
                    var val3 = row.peopleDifferNumber;
                    var val4 = row.peopleLampTotal;
                	if(val3 < 0||val3==""||val3==null){
                		val3=0;
                	}
                	if(val4<0||val4==""||val4==null){
            			val4 = 0 ;
            		}
                    deng = getDeng(val,(val >= val1),(val < val2));
                    deng = '<div style="width: 100%;text-align: left;">'+
		                		'<div style="width: 100%;">'+
		                			'<span style="font-size: 12px;">'+'人员到位率'+'</span>'+
		                		'</div>'+
		                		'<div style="width: 100%;font-size: 20px;padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
		                			'<span>'+vals+'</span>'+
		                		'</div>'+
		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
		                			'<div style="width: 50%;float: left;">'+
		                				'<div>'+'人力缺口'+'</div>'+
		                				'<div>'+val3+'</div>'+
		                			'</div>'+
		                			'<div style="width: 50%;float: right;">'+
		                				'<div style="float: right;margin-right: 5px;">'+
		                					'<div>'+'诉求人力'+'</div>'+
		                					'<div>'+val4+'</div>'
		                					+deng+
		                				'</div>'+
		                			'</div>'+
		                		'</div>'+
		                	'</div>';
                    return deng;
                }
            },
            {title:'计划进度点灯',field:'planLamp',width:130,
            	formatter:function(value,row,index){
            		var val = parseFloat(value==null?0:value);
            		if(val<-100){
            			val = -100 ;
            		}
            		if(val>100){
            			val = 100 ;
            		}
            		 var val1 = 0;
                     var val2 = -10;
                    var val3 = row.demandProgress==null?0:row.demandProgress;
                    if(val3<0||val3==""||val3==null){
            			val3 = 0 ;
            		}
            		if(val3>100){
            			val3 = 100 ;
            		}
                    var val4 = row.developmentProgress==null?0:row.developmentProgress;
                    if(val4<0||val4==""||val4==null){
            			val4 = 0 ;
            		}
            		if(val4>100){
            			val4 = 100 ;
            		}
            		if(val == -1){
            			deng = getDeng(val+1,(val >= val1),(val < val2));
            		}else{
            			deng = getDeng(val,(val >= val1),(val < val2));
            		}
                    deng = '<a'+demandProgressEdit+'>' +
		                    	'<div style="width: 100%;text-align: left;">'+
		                    		'<div style="width: 100%;">'+
		                    			'<span style="font-size: 12px;">'+'进度偏差率'+'</span>'+
		                    		'</div>'+
		                    		'<div style="width: 100%;font-size: 20px;padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
		                    			'<span>'+val+'%'+'</span>'+
		                    		'</div>'+
		                    		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
		                    			'<div style="width: 50%;float: left;">'+
		                    				'<div>'+'需求完成率'+'</div>'+
		                    				'<div>'+val3+'%'+'</div>'+
		                    			'</div>'+
		                    			'<div style="width: 50%;float: right;">'+
		                    				'<div style="float: right;margin-right: 5px;">'+
		                    					'<div>'+'进度完成率'+'</div>'+
		                    					'<div>'+val4+'%'+'</div>'
		                    					+deng+
		                    				'</div>'+
		                    			'</div>'+
		                    		'</div>'+
		                    	'</div>'+
		                     '</a>';
                    return deng;
                }
            },
            {title:'需求范围点灯',field:'scopeLamp',width:110,
            	formatter:function(value,row,index){
            		var val = parseFloat(value==null?0:value);
            		if(val<0){
            			val = 0 ;
            		}
            		if(val>100){
            			val = 100 ;
            		}
                    var val1 = 5;
                    var val2 = 10;
                    var val3 = row.demandChangeNumber;
                    if(val3<0||val3==""||val3==null){
            			val3 = 0 ;
            		}
                    var val4 = row.demandTotal;
                    if(val4<0||val4==""||val4==null){
            			val4 = 0 ;
            		}
                    deng = getDeng(val,(val < val1),(val >= val2));
                    deng = '<a'+editData+'href="#" class="praise">' +
		                    	'<div style="width: 100%;text-align: left;">'+
		                    		'<div style="width: 100%;">'+
		                    			'<span style="font-size: 12px;">'+'需求变更率'+'</span>'+
		                    		'</div>'+
		                    		'<div style="width: 100%;font-size: 20px;padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
		                    			'<span>'+val+'%'+'</span>'+
		                    		'</div>'+
		                    		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
		                    			'<div style="width: 50%;float: left;">'+
		                    				'<div>'+'需求变更'+'</div>'+
		                    				'<div>'+val3+'</div>'+
		                    			'</div>'+
		                    			'<div style="width: 50%;float: right;">'+
		                    				'<div style="float: right;margin-right: 5px;">'+
		                    					'<div>'+'总需求数'+'</div>'+
		                    					'<div>'+val4+'</div>'
		                    					+deng+
		                    				'</div>'+
		                    			'</div>'+
		                    		'</div>'+
		                    	'</div>'+
		                     '</a>';
                    return deng;
                }
            },
            {title:'质量目标点灯',field:'qualityLamp',width:100,
            	formatter:function(value,row,index){
            		if(value =="-1"){
                		var val = value ;
                		var vals = "--" ;
                	}else{
                		var val = parseFloat(value==null?0:value);
                		if(val<0){
                			val = 0 ;
                		}
                		if(val>100){
                			val = 100 ;
                		}
                		var vals = val+'%';
                	}
                	var val1 = row.redLightNumber;
                	if(val1<0||val1==""||val1==null){
            			val1 = 0 ;
            		}
                	var val2 = row.yellowLightNumber;
                	if(val2<0||val2==""||val2==null){
            			val2 = 0 ;
            		}
                	var val3 = row.greenLightNumber;
                	if(val3<0||val3==""||val3==null){
            			val3 = 0 ;
            		}
                	deng = getDeng(val,val1==0 && val2==0,val1!=0);
                	deng = '<div style="width: 100%;text-align: left;">'+
		                		'<div style="width: 100%;">'+
		                			'<span style="font-size: 12px;">'+'红黄灯占比'+'</span>'+
		                		'</div>'+
		                		'<div style="width: 100%;font-size: 20px;padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
		                			'<span>'+vals+'</span>'+
		                		'</div>'+
		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
		                			'<div style="width: 33%;float: left;">'+
		                				'<div>'+'红灯'+'</div>'+
		                				'<div>'+val1+'</div>'+
		                			'</div>'+
		                			'<div style="width: 33%;float: right;">'+
		                				'<div>'+'绿灯'+'</div>'+
		                				'<div>'+val3+'</div>'
		                				 +deng+
		                			'</div>'+
		                			'<div style="width: 33%;float: left;">'+
		                				'<div>'+'黄灯'+'</div>'+
		                				'<div>'+val2+'</div>'+
		                			'</div>'+
		                		'</div>'+
		                	'</div>';
                    return deng;
                },
            },
            {title:'关键角色匹配',field:'keyRoles',width:110,
                formatter:function(value,row,index){
                	if(value =="--"){
                		var val = 0 ;
                		var vals = value ;
                	}else{
                		var val = parseFloat(value==null?0:value);
                		if(val<0){
                			val = 0 ;
                		}
                		if(val>100){
                			val = 100 ;
                		}
                		var vals = val+'%';
                	}
                	var val1 = row.keyRolesTotal;
                	if(val1<0||val1==""||val1==null){
            			val1 = 0 ;
            		}
                	var val2 = row.rolesDifferNumber;
                	if(val2 < 0||val2==""||val2==null){
                		val2=0;
                	}
                	var val3 = 85;
                    var val4 = 60;
                    deng =  getDeng(val,(val >= val3),(val < val4));
                    deng = '<div style="width: 100%;text-align: left;">'+
			                	'<div style="width: 100%;">'+
			                		'<span style="font-size: 12px;">'+'关键角色匹配度'+'</span>'+
			                	'</div>'+
			                	'<div style="width: 100%;font-size: 20px;padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
			                		'<span>'+vals+'</span>'+
			                	'</div>'+
			                	'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
			                		'<div style="width: 50%;float: left;">'+
			                			'<div>'+'人力缺口'+'</div>'+
			                			'<div>'+val2+'</div>'+
			                		'</div>'+
			                		'<div style="width: 50%;float: right;">'+
			                			'<div style="float: right;margin-right: 5px;">'+
			                				'<div>'+'诉求人力'+'</div>'+
			                				'<div>'+val1+'</div>'
			                				+deng+
			                			'</div>'+
			                		'</div>'+
			                	'</div>'+
			                '</div>';
                    return deng;
                },
            },
            {title:'关键角色答辩通过率',field:'keyRolesPass',width:120,
                formatter:function(value,row,index){
                	var val = parseFloat(value==null?0:value); 
                	if(val<0){
            			val = 0 ;
            		}
            		if(val>100){
            			val = 100 ;
            		}
                	var val1 = row.passNumber;
                	if(val1<0||val1==""||val1==null){
            			val1 = 0 ;
            		}
                	var val2 = row.notPassedNumber;
                	if(val2<0||val2==""||val2==null){
            			val2 = 0 ;
            		}
                	var val3 = 85;
                    var val4 = 60;
                    deng =  getDeng(val,(val >= val3),(val < val4));
                    deng = '<div style="width: 100%;text-align: left;">'+
		                    	'<div style="width: 100%;">'+
		                    		'<span style="font-size: 12px;">'+'答辩通过率'+'</span>'+
		                    	'</div>'+
		                    	'<div style="width: 100%;font-size: 20px;padding-top: 5px;padding-bottom: 10px;">'+
		                    		'<span>'+val+'%'+'</span>'+
		                    	'</div>'+
		                    	'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
		                    		'<div style="width: 50%;float: left;">'+
		                    			'<div>'+'待答辩'+'</div>'+
		                    			'<div>'+val2+'</div>'+
		                    		'</div>'+
		                    		'<div style="width: 50%;float: right;">'+
		                    			'<div style="float: right;margin-right: 5px;">'+
		                    				'<div>'+'已通过'+'</div>'+
		                    				'<div>'+val1+'</div>'
		                    				 +deng+
		                    			'</div>'+
		                    		'</div>'+
		                    	'</div>'+
		                    '</div>';
                    return deng;
                },
            },
        ]
    };

	$.fn.loadzbTop=function(){
        bsTable.refresh();
	}
    $('#refreshData').click(function(){
        $.ajax({
            type: "post",
            url: getRootPath() + '/projectReport/refreshData',
            data: {
                proNo:proNo,
                month: $("#monthlySection").val(),
            },
            success: function (data, status) {
                if (status == "success") {
                    bsTable.refresh();
                    toastr.success('修改成功！');
                } else {
                    toastr.success('修改失败！');
                }
            }
        });

    });
    function getDeng(val,val1,val2){
    	if(val=="-1"){
    		deng = '<div style="display: block;margin-top: -62px;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
            'background-color: #bdb7b7; "></div>';
    	}else if(val1){
            deng = '<div style="display: block;margin-top: -62px;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #1adc21; "></div>';
        }else if(val2){
            deng ='<div style=" display: block;margin-top: -62px;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #f57454; "></div>';
        }else{
            deng = '<div style="display: block;margin-top: -62px;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #f7b547; "></div>';
        }        
        return deng;
    }
    
    // 需求变更率编辑
    $(document).on("click", "#editData", function () {
    	var row = $('#mytable').bootstrapTable('getData')[0];
		$("#no").val(row.no);
		$("#demandTotal").val(row.demandTotal);
		$("#demandChangeNumber").val(row.demandChangeNumber);
		$("#scopeLamp").val(row.scopeLamp);
		$("#iteEditPage").modal('show');

    });
    $('#edit_saveBtn').click(function(){
    	$('#editForm').bootstrapValidator('validate');
    	if($("#editForm").data('bootstrapValidator').isValid()){
    		$.ajax({
 				url:getRootPath()+"/projectReport/edit",
 				type : 'post',
 				data:{
 					no :$("#no").val(),
 					demandTotal :$("#demandTotal").val(),
 					demandChangeNumber :$("#demandChangeNumber").val(),
 					scopeLamp :$("#scopeLamp").val(),
 					creationDate : $("#monthlySection").val(),
 				},
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#iteEditPage").modal('hide');
 				    	$('#mytable').bootstrapTable('refresh', {url: getRootPath() + '/projectReport/getQualityonthlyReportList'});
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
    
    $('#edit_backBtn').click(function(){
    	$("#iteEditPage").modal('hide');
    });
    
    change = function(){
    	var demandTotal = $("#demandTotal").val();
		var demandChangeNumber = $("#demandChangeNumber").val();
		var sp = 0.00 ; 
		if(demandTotal==0){
			 sp = 0.00 ;
		}else{
			 sp = parseFloat(demandChangeNumber/demandTotal*100).toFixed(2);		
		}
		if(sp < 0 ){
			sp = 0 ;
		}else if(sp > 100){
			sp = 100 ;
		}
		$("#scopeLamp").val(sp.toString());
    	
    };
    
    function demandchangerate(){
    	$.ajax({
			type : "post",
			url : getRootPath() + '/projectReport/demandchangerate',
			data : {
				proNo : proNo,
				
			},
			success : function(data) {
				var demandTotal = data.demandTotal;
				$("#demandTotal").val(demandTotal.toString());
				var demandChangeNumber = data.demandChangeNumber;
				$("#demandChangeNumber").val(demandChangeNumber.toString())
				var sp = 0.00 ; 
				if(demandTotal==0){
					 sp = 0.00 ;
				}else{
					 sp = parseFloat(demandChangeNumber/demandTotal*100).toFixed(2);		
				}
				if(sp < 0 ){
					sp = 0 ;
				}else if(sp > 100){
					sp = 100 ;
				}
				$("#scopeLamp").val(sp.toString());
			}
		});
    }
    //需求完成率编辑
    $(document).on("click", "#demandProgressEdit", function () {
    	var row = $('#mytable').bootstrapTable('getData')[0];
		$("#no").val(row.no);
		$("#planLamp").val(row.planLamp);
		$("#demandProgress").val(row.demandProgress);
		$("#developmentProgress").val(row.developmentProgress);
		$("#editDemandProgress").modal('show');

    });
    $('#editSaveBtn').click(function(){
    	$('#editDpForm').bootstrapValidator('validate');
    	if($("#editDpForm").data('bootstrapValidator').isValid()){
    		$.ajax({
 				url:getRootPath()+"/projectReport/editDemandProgress",
 				type : 'post',
 				data:{
 					no :$("#no").val(),
 					planLamp :$("#planLamp").val(),
 					demandProgress :$("#demandProgress").val(),
 					developmentProgress :$("#developmentProgress").val(),
 					creationDate : $("#monthlySection").val(),
 				},
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						$("#editDemandProgress").modal('hide');
 				    	$('#mytable').bootstrapTable('refresh', {url: getRootPath() + '/projectReport/getQualityonthlyReportList'});
 				    	$('#editDpForm').data('bootstrapValidator').resetForm(true);
 						toastr.success('编辑成功！');
 					}
 					else{
 						toastr.error('编辑失败!');
 					}
 				}
 			});
    	}
    });
    
    $('#editBackBtn').click(function(){
    	$("#editDemandProgress").modal('hide');
    });
    
    changePlanLamp = function(){
    	var demandProgress = $("#demandProgress").val();
		var developmentProgress = $("#developmentProgress").val();
		var pf = parseFloat((demandProgress-developmentProgress)/developmentProgress*100).toFixed(2);
		if(pf < -100){
			pf = -100 ;
		}else if(pf > 100){
			pf = 100 ;
		}
		$("#planLamp").val(pf.toString());
    	
    };
    
    myFunction=function(){
    	var a = $("#demandProgress").val();
    	if(a<0||a>100){
    		$("#demandProgress").val("");
    		$("#err1").css("display", "block");
			$("#err1")
			.html(
					'<font style="color:red;font-size:15px;">请输入0—100之间的数！</font>');
			setTimeout(function() {
				$("#err1").css("display", "none");
			}, '3000');
		}
    }

    $(document).ready(function(){
    	projectStartTime();
    	demandchangerate();
    	initTableSave()
    })

});