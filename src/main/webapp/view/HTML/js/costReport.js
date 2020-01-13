function loadXMCB(){
	$('#costTable').bootstrapTable('destroy');
	$('#costTable').bootstrapTable({
    	method: 'GET',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/projectOverview/queryCostReport',
    	striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
		queryParams : function(params){
			var param = {
					'limit': params.limit,
					'offset': params.offset,
					'date':$("#dateSection").val(),
			        'area': ($("#usertype1").selectpicker("val")==null || $('#select1').is(':hidden'))?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
			        'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
			        'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
			        'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
			        'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
	                'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
	                'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
	                'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
	                'coopType': collected //0:全部项目;1：已收藏项目;2:未收藏项目
				}
			return param;
		},
		columns:[
        	[	{title : '序</br>号',align: "center",valign:'middle',width: '3.6%', rowspan: 2, colspan: 1,
        			formatter: function (value, row, index) {
        				var pageSize=$('#costTable').bootstrapTable('getOptions').pageSize;  
        				var pageNumber=$('#costTable').bootstrapTable('getOptions').pageNumber;
        				return pageSize * (pageNumber - 1) + index + 1;
        			}
        		},
        		{title:'项目名称',field:'name',align: 'center',valign:'middle',width:'21%',rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no+ '&xmcb=xmcb';
        				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
        			}
        		},
        		{title:'PM',field:'pm',align: "center",valign:'middle',width:'5%',rowspan: 2, colspan: 1},
        		{title:'部门',field:'department',align: 'center',valign:'middle',width:'9.4%',rowspan: 2, colspan: 1,
        			formatter:function(value,row,index){
  					  return '<p title="'+value+'" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
  				    }
        		},
        		{title:'产出(元)',align: 'center',valign:'middle',width:'22%',rowspan: 1, colspan: 3},
        		{title:'时间进度',field:'timeProgress',align: 'center',valign:'middle',width:'6%',rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				return '<p title="'+keepTwoDecimalFull(val*100) + '%'+'">'+keepTwoDecimalFull(val*100) + '%'+'</p>';
        			}
        		},
        		{title:'产出进度',field:'outputProgress',align: 'center',valign:'middle',width:'6%',rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				if(undefined == val || "Infinity" == val){
        					return '0.00%'
        				}
        				return '<p title="'+keepTwoDecimalFull(val*100) + '%'+'">'+keepTwoDecimalFull(val*100) + '%'+'</p>';
        			}
        		},
        		{title:'偏差与点灯',field:'deviationLight',align: 'center',valign:'middle',width:'3%',rowspan: 2, colspan: 1,
        			formatter:function(value, row, index){
                		var deng = getDeng(value);
                        return '<div style="width: 100%;">'+'<span title="'+keepTwoDecimalFull(value)+'">'+deng+'</span>'+'</div>';
                    }
        		},
        		/*{title:'总预算(元)',field:'projectBudget',align: 'center',valign:'middle',width:'9%',rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				if(val == 'nu' || undefined == val){
        					return '0.00';
        				}else{
        					return '<p title="'+keepTwoDecimalFull(val)+'">'+keepTwoDecimalFull(val)+'</p>';
        				}
        			}
        		},
        		{title:'剩余预算(元)',field:'surplusBudget',align: 'center',valign:'middle',width:'9%',rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				if(undefined == val){
							return '0.00';
						}
        				return '<p title="'+keepTwoDecimalFull(val)+'">'+keepTwoDecimalFull(val)+'</p>';
        			}
        		},*/
        		{title:'剩余预算可维持(天)',field:'budgetMaintenance',align: 'center',valign:'middle',width:'6%',rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				if(undefined == val || '' == val){
							return '0';
						}
        				return '<p title="'+parseInt(val)+'">'+parseInt(val)+'</p>';
        			}
        		},
        		{title:'剩余预算可维持日期',field:'maintenanceDate',align: 'center',valign:'middle',width:'8%',rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				if(null == val){
        					return '';
        				}
        				return '<p title="'+val+'">'+val+'</p>';
        			}
        		},
        		{title:'项目结束时间',field:'endDate',align: 'center',valign:'middle',width:'8%',rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				if(null == val){
        					return '';
        				}
        				return '<p title="'+val+'">'+val+'</p>';
        			}
        		},
        		{title: prohibitTitle,
        	        field:'collection',width:'3%',align: 'center',valign:'middle',rowspan: 2, colspan: 1,
        	    	   formatter : function(value, row, index) {
        	    		    if (value!=null & value!=''){
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexCB"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.no+'\',\''+"CB"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
               			    }else{
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexCB"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.no+'\',\''+"CB"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
               			    }
        	    	   }
    	        }
    		],
    		[
				{title : '正常理论产出',field : 'normalOut',align: 'center',valign:'middle',width : '33%',
					formatter:function(val, row){
						return projectCost(row.no, val, '正常理论产出,实际产出,出勤产出,加班产出');
        			}
				},
				{title : '出勤产出',field : 'attendMoney',align: 'center',valign:'middle',width : '33%',
					formatter:function(val, row){
						return projectCost(row.no, val, '正常理论产出,实际产出,出勤产出,加班产出');
        			}
				},
				{title : '加班产出',field : 'overMoney',align: 'center',valign:'middle',width : '33%',
					formatter:function(val, row){
						return projectCost(row.no, val, '正常理论产出,实际产出,出勤产出,加班产出');
        			}
				}
    		]
        ],
	locale:'zh-CN'//中文支持
	});
}

function projectCost(projectid, value, name){
	if(undefined == value || "" == value){
		value = "-";
	}
	return '<a title="'+value+'" href="javascript:void(0)" onclick="projectCostLine(\''+projectid+'\',\''+name+'\')">'+value+'</a>';
}

function projectCostLine(projectid, name){
	$("#projectCostTitle").html($("#dateSection").val());
	$.ajax({
		url : getRootPath() + '/projectOverview/projectCostLine',							
		type : "post",
		dataType : "json",
		cache : false,
		data:{
				'projectId':projectid,
	            'nowDate':$("#dateSection").val()
            },	
		success : function(data){
			$("#projectCostModal").modal('show');
			outPutTrendMap(data, name, '产出趋势(');
		}
	});
}

function outPutTrendMap(data, name, title){
	if (data.code == '200') {
        $("#projectCostModal").modal('show');
        $("#projectCostDiagram").text(title);
        var divId = "projectCostPage";
        var name = name.split(",");
        var axisName = ['时间', null];
        var types = [name[0], name[1], name[2], name[3]];
        var xAxis = data.data.monthList;
        var values = [];
        /*var dateArr = $("#dateSection").val().split("-");
        var selectedDate = dateArr[0] + "-" + dateArr[1];*/
        
        values.push(data.data.normalValueList);
        values.push(data.data.aoValueList);
        values.push(data.data.attendValueList);
        values.push(data.data.overValueList);
        var markPointData =[];      
        /*for (var i = 0; i < xAxis.length; i++){
			if(selectedDate === xAxis[i]){
                markPointData.push({name: '选择时间', value: values[0][i], xAxis: xAxis[i], yAxis: values[0][i]});
                markPointData.push({name: '选择时间', value: values[1][i], xAxis: xAxis[i], yAxis: values[1][i]});
                markPointData.push({name: '选择时间', value: values[2][i], xAxis: xAxis[i], yAxis: values[2][i]});
                markPointData.push({name: '选择时间', value: values[3][i], xAxis: xAxis[i], yAxis: values[3][i]});
			}
		}*/

        //不传入颜色为默认颜色
        lineCharts(divId,axisName,types,xAxis,values,null,markPointData);
    }else{
        toastr.error('查询得分异常!');
    }
}

function getDeng(value){
	var deng = "";
	if (undefined != value) {
		deng = '<div style="display: inline-block;border-radius: 50%;width: 15px;height:15px;'
			+'background-color:'+getDeviationLight(keepTwoDecimalFull(value))+';"></div>';
	}
    return deng;
}

function keepTwoDecimalFull(num) {
	 var result = parseFloat(num);

	 if (isNaN(result)) {
		 alert('传递参数错误，请检查！');
		 return false;
	 }
	 result = Math.round(num * 100) / 100;

	 var s_x = result.toString();
	 var pos_decimal = s_x.indexOf('.');

	 if (pos_decimal < 0) {
		 pos_decimal = s_x.length;
		 s_x += '.';
	 }
	 while (s_x.length <= pos_decimal + 2) {
		 s_x += '0';
	 }
	 return s_x;
}