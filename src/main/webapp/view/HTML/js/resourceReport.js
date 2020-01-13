function resourceReport(){
	$('#resourceTable').bootstrapTable('destroy');
	$("#resourceTable").bootstrapTable({
    	method: 'GET',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/projectOverview/queryResourceReport',
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
	                'coopType': collected,//0:全部项目;1：已收藏项目;2:未收藏项目
				}
			return param;
		},
		columns:[
        	[	{title : '序</br>号',align: "center",valign:'middle',width: 36,rowspan: 2,colspan: 1,
        			formatter: function (value, row, index) {
        				var pageSize=$('#resourceTable').bootstrapTable('getOptions').pageSize;  
        				var pageNumber=$('#resourceTable').bootstrapTable('getOptions').pageNumber;
        				return pageSize * (pageNumber - 1) + index + 1;
        			}
        		},
        		{title:'项目名称',field:'projectName',align: 'center',valign:'middle',width:150,rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
        				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
        			}
        		},
        		{title:'PM',field:'pm',align: "center",valign:'middle',width:50,rowspan: 2, colspan: 1},
        		{title:'部门',field:'department',align: 'center',valign:'middle',width:80,rowspan: 2, colspan: 1,
        			formatter:function(value,row,index){
  					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
  				    }
        		},
        		{title:'人员稳定</br>度得分',field:'personnelStability',align: 'center',valign:'middle',width:71,rowspan: 2, colspan: 1},
        		{title:'关键角色',align: 'center',valign:'middle',width:350,rowspan: 1, colspan: 5},
        		{title:'全员',align: 'center',valign:'middle',width:350,rowspan: 1, colspan: 5},
        		{title:'361评估',align: 'center',valign:'middle',width:70,rowspan: 1, colspan: 1},
        		{title: prohibitTitle,
        	        field:'collection',width:40,align: 'center',valign:'middle',rowspan: 2,colspan: 1,
        	    	   formatter : function(value, row, index) {
        	    		    if (value!=null & value!=''){
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexZY"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.no+'\',\''+"ZY"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
               			    }else{
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexZY"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.no+'\',\''+"ZY"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
               			    }
        	    	   }
        	     }
    		],    	
        	[
        		{title : '到位率',field : 'keyArrivalRate',align: 'center',valign:'middle',width : 70},
        		{title : '人力需求',field : 'keyManpowerDemand',align: 'center',valign:'middle',width : 70,
        			formatter:function(value,row,index){
        				return getNumber(value);
        			}
        		},
        		{title : '人力缺口',field : 'keyManpowerGap',align: 'center',valign:'middle',width : 70,
        			formatter:function(value,row,index){
        				return getNumber(value);
        			}
        		},
        		{title : '离职率',field : 'keyTurnoverRate',align: 'center',valign:'middle',width : 70},
        		{title : '离职人数',field : 'keyTurnoverPerson',align: 'center',valign:'middle',width :70,
        			formatter:function(value,row,index){
        				return getNumber(value);
        			}
        		},
        		{title : '到位率',field : 'arrivalRate',align: 'center',valign:'middle',width : 70},
        		{title : '人力需求',field : 'manpowerDemand',align: 'center',valign:'middle',width : 70,
        			formatter:function(value,row,index){
	                    return getNumber(value);
        			}
        		},
        		{title : '人力缺口',field : 'manpowerGap',align: 'center',valign:'middle',width : 70,
        			formatter:function(value,row,index){
        				return getNumber(value);
        			}
        		},
        		{title : '离职率',field : 'turnoverRate',align: 'center',valign:'middle',width : 70},
        		{title : '离职人数',field : 'turnoverPerson',align: 'center',valign:'middle',width : 70,
        			formatter:function(value,row,index){
        				return getNumber(value);
        			}		
        		},
        		{title : '人员稳定度',field : 'personnelStability361',align: 'center',valign:'middle',width : 70,
        			formatter:function(value,row,index){
        				return getNumber(value);
        			}		
        		}
        	]
        ],
	locale:'zh-CN'//中文支持
	});
}

function getNumber(val){
	var demand = val;
	if(val == 0 || val == -1){
		demand = "-";
	}
	return demand;
}
