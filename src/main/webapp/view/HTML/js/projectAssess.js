$(function(){
	var projectState = "在行";
    var bsTable;
    var mode1 = "popup"//编辑框的模式：支持popup和inline两种模式，默认是popup

    function initTableSave(){
        var columns=initColumn();
        var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
            'projectState':projectState,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
        };
        var url = getRootPath() + '/projectReport/getProjectAssessPageAll';
        bsTable = new BSTable('mytable', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(queryParams);
        bsTable.setPageSize(5);
        bsTable.setPageNumber(1);
        // bsTable.setEditable(true);//开启行内编辑
        bsTable.setEditable(false);
        bsTable.init();
	}
    function initColumn(){
        return [
            {title:'项目编号 ',field:'no',width:80,visible: false},
            {
        		title : '序号',
        		align: "center",
        		width: 60,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytable').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytable').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
            },
            {title:'项目名称',field:'name',width:300,
                formatter:function(value,row,index){
                    var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
                    var pduSpdt = row.pduSpdt==null?'/':('/'+row.pduSpdt);
                    var tab = '<a target="_blank" style="color: #000;;" title="'+value +'" href="'+path1+'">' +
                                '<div style="width: 100%;text-align: left;">' +
                                '<label style="font-size: 16px;font-weight: unset;">' +
                                    ''+value+ '-'+row.area+
                                '</label>' +
                                '<div style="font-size: 12px;color: #a3a3a3;">' +
                                    '<div style="width: 90%;">' +
                                        '<sqan>PM: '+row.pm+'</sqan>' +
                                        '<sqan style="float: right;">结项时间:'+new Date(row.endDate).format('yyyy-MM-dd')+'</sqan>' +
                                    '</div>' +
                                    '<div style="width: 90%;">' +
                                        '<sqan>'+row.hwzpdu+pduSpdt+'</sqan>' +
                                        '<sqan style="float: right;">'+row.area+'</sqan>' +
                                    '</div>' +
                                '</div>' +
                             '</div>' +
                            '</a>';
                    return tab
                },
            },
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
                	if(val4 < 0||val4==""||val4==null){
                		val4=0;
                	}
                    deng = getDeng(val,(val >= val1),(val < val2));
                    deng = '<div style="width: 100%;text-align: left;">'+
		                		'<div style="width: 100%;">'+
		                			'<span style="font-size: 12px;">'+'人员到位率'+'</span>'+
		                			'<span style="float: right;">'+deng+'</span>'+
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
		                					'<div>'+val4+'</div>'+
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
                    deng = '<div style="width: 100%;text-align: left;">'+
		                		'<div style="width: 100%;">'+
		                			'<span style="font-size: 12px;">'+'进度偏差率'+'</span>'+
		                			'<span style="float: right;">'+deng+'</span>'+
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
		                					'<div>'+val4+'%'+'</div>'+
		                				'</div>'+
		                			'</div>'+
		                		'</div>'+
		                	'</div>';
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
                    deng =  '<div style="width: 100%;text-align: left;">'+
		                		'<div style="width: 100%;">'+
		                			'<span style="font-size: 12px;">'+'需求变更率'+'</span>'+
		                			'<span style="float: right;">'+deng+'</span>'+
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
		                					'<div>'+val4+'</div>'+
		                				'</div>'+
		                			'</div>'+
		                		'</div>'+
		                	'</div>';
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
                	deng =  '<div style="width: 100%;text-align: left;">'+
		                		'<div style="width: 100%;">'+
		                			'<span style="font-size: 12px;">'+'红黄灯占比'+'</span>'+
		                			'<span style="float: right;">'+deng+'</span>'+
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
		                				'<div>'+val3+'</div>'+
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
            {title:'关键角色匹配',field:'keyRoles',width:120,
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
		                			'<span style="float: right;">'+deng+'</span>'+
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
		                					'<div>'+val1+'</div>'+
		                				'</div>'+
		                			'</div>'+
		                		'</div>'+
		                	'</div>';
                    return deng;
                },
            },
            {title:'关键角色答辩通过率',field:'keyRolesPass',width:110,
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
		                			'<span style="float: right;">'+deng+'</span>'+
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
		                					'<div>'+val1+'</div>'+
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
        var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
            'projectState':projectState,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
        };
        bsTable.setQueryParams(queryParams);
//        bsTable.refresh();
        $("#mytable").bootstrapTable('destroy');//先要将table销毁，否则会保留上次加载的内容
        bsTable.init();
	}
    
    function exportAssess(){
		$("#exportAssess").click(function(){
			var $eleForm = $("<form method='POST'></form>");
			var areaVal=$("#usertype1").selectpicker("val")==null?"":$("#usertype1").selectpicker("val").join();
//			var projectStateVal=$("#usertype2").selectpicker("val")==null?"":$("#usertype2").selectpicker("val").join();
			var hwpduVal=$("#usertype3").selectpicker("val")==null?"":$("#usertype3").selectpicker("val").join();
			var hwzpduVal=$("#usertype4").selectpicker("val")==null?"":$("#usertype4").selectpicker("val").join();
			var pduSpdtVal=$("#usertype5").selectpicker("val")==null?"":$("#usertype5").selectpicker("val").join();
			var buVal=$("#usertype6").selectpicker("val")==null?"":$("#usertype6").selectpicker("val").join();
			var pduVal=$("#usertype7").selectpicker("val")==null?"":$("#usertype7").selectpicker("val").join();
			var duVal=$("#usertype8").selectpicker("val")==null?"":$("#usertype8").selectpicker("val").join();
            var clientType=$("#selectBig").selectpicker("val")=="2"?"0":"1";
			$eleForm.append($('<input type="hidden" name="area" value="'+ areaVal +'">'));
			$eleForm.append($('<input type="hidden" name="projectState" value="'+ encodeURI(projectState) +'">'));
            $eleForm.append($('<input type="hidden" name="clientType" value="'+ clientType +'">'));
			$eleForm.append($('<input type="hidden" name="hwpdu" value="'+ hwpduVal +'">'));
			$eleForm.append($('<input type="hidden" name="hwzpdu" value="'+ hwzpduVal +'">'));
			$eleForm.append($('<input type="hidden" name="pduSpdt" value="'+ pduSpdtVal +'">'));
			$eleForm.append($('<input type="hidden" name="bu" value="'+ buVal +'">'));
			$eleForm.append($('<input type="hidden" name="pdu" value="'+ pduVal +'">'));
			$eleForm.append($('<input type="hidden" name="du" value="'+ duVal +'">'));
            $eleForm.attr("action",getRootPath() + "/projectReport/exportAssess");
            $(document.body).append($eleForm);
            //提交表单，实现下载
            $eleForm.submit();
		})
	}
    function getDeng(val,val1,val2){
    	if(val=="-1"){
    		//灰灯
    		deng = '<div style="display: block;float: right;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
            'background-color: #bdb7b7; "></div>';
    	}else if(val1){
        	//绿灯
            deng = '<div style="display: block;float: right;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #1adc21; "></div>';
        }else if(val2){
        	//红灯
            deng ='<div style=" display: block;float: right;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #f57454; "></div>';
        }else{
        	//黄灯
            deng = '<div style="display: block;float: right;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #f7b547; "></div>';
        }
        return deng;
    }
    $(document).ready(function(){
        initTableSave();
        exportAssess();
    })

});

function selectOnchange(){
	$().loadzbTop();
}


