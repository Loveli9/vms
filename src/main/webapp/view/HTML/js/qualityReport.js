$(function(){
	/********************************************** 导入 *************************************/
    //打开文件上传页面
    $('#importQMS').click(function(){
    	$("#demandImport").modal('show');
    	$("#filePathInfo").val('');
    	$("#openLocalFile").val('');
    });
    //打开本地文件进行选择
    $('#importBtn').click(function(){
    	$('#openLocalFile').click();
    });
    //添加文件路径
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
    				url : getRootPath()+'/qmsMaturity/importQMS',
    				type:'post',
    				dataType: 'json',
    				success : function(data) {
    					if(data){
    						if(Number(data.sucNum) > 0){
    							toastr.success('成功导入'+data.sucNum+'条数据!');
    						}else{
    							toastr.error('导入失败，请检查数据格式');
    						}
    					}
    				}
    			};
    		$("#importForm").ajaxSubmit(option);
    	}
    });
    
    /********************************************** 导出 *************************************/
    $("#exportQMS").click(function(){
        var $eleForm = $("<form method='POST'></form>");
        $eleForm.attr("action",getRootPath() + "/qmsMaturity/exportQMS");
        $eleForm.append($('<input type="hidden" name="date" value="'+$("#dateSection").val()+'">'));
        $(document.body).append($eleForm);
        //提交表单，实现下载
        $eleForm.submit();
    })
});
    function changeQuality(name){
    	if(name=="dm"){
    		$("#qualityReportTableDiv").css("display","");
    		initQualityReport();
    		$("#reliableQualityTableDiv").css("display","none");
    		$("#reviewQualityTableDiv").css("display","none");
    		$("#processIndicatorsTableDiv").css("display","none");
    		$("#concludingIndicatorsTableDiv").css("display","none");
    		$("#exportKX").css("display","none");
    	}else if(name=="kx"){
    		$("#reliableQualityTableDiv").css("display","");
    		initReliableQuality();
    		$("#qualityReportTableDiv").css("display","none");
    		$("#reviewQualityTableDiv").css("display","none");
    		$("#processIndicatorsTableDiv").css("display","none");
    		$("#concludingIndicatorsTableDiv").css("display","none");
    		$("#exportKX").css("display","");
    	}else if(name=="review"){
    		$("#reviewQualityTableDiv").css("display","");
    		initReviewQuality();
    		$("#qualityReportTableDiv").css("display","none");
    		$("#reliableQualityTableDiv").css("display","none");
    		$("#processIndicatorsTableDiv").css("display","none");
    		$("#concludingIndicatorsTableDiv").css("display","none");
    		$("#exportKX").css("display","none");
    	}else if(name=="gc"){
    		initProcessIndicators("过程指标");
    		$("#qualityReportTableDiv").css("display","none");
    		$("#reliableQualityTableDiv").css("display","none");
    		$("#reviewQualityTableDiv").css("display","none");
    		$("#processIndicatorsTableDiv").css("display","");
    		$("#concludingIndicatorsTableDiv").css("display","none");
    		$("#exportKX").css("display","none");
    	}else if(name=="jx"){
    		initProcessIndicators("结项指标");
    		$("#qualityReportTableDiv").css("display","none");
    		$("#reliableQualityTableDiv").css("display","none");
    		$("#reviewQualityTableDiv").css("display","none");
    		$("#processIndicatorsTableDiv").css("display","none");
    		$("#concludingIndicatorsTableDiv").css("display","");
    		$("#exportKX").css("display","none");
    	}
    }
       	
    function formatterValue(projectid, id, name, val, flag, measureMark){
    	var value = "-";
    	if(val != undefined){
    		value = val.value;
    		if(null != value && '' != value){
    			var index = value.indexOf('.');
    			if(index>0){
    				if(flag){
    					value= value.substr(0,index);
    				}else{
    					value= value.substr(0,index+3);
    				}
    			}    		
    		}
    	}
        return '<a href="javascript:void(0)" onclick="measureValueLine(\''+projectid+'\','+id+',\''+name+'\',\''+measureMark+'\')">'+value+'</a>';
    }
    
    function initQualityReport(){ 
    	$('#qualityReportTable').bootstrapTable('destroy');
    	$('#qualityReportTable').bootstrapTable({
    		method: 'GET',
            contentType: 'application/x-www-form-urlencoded',
            url: getRootPath() + '/projectOverview/queryProjectQualityReport',
            striped: true, //是否显示行间隔色
            pageNumber: 1, //初始化加载第一页，默认第一页
            pagination: false, //是否分页
            sortable: true, //是否启用排序
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
                    'order': params.order,
                    'sort': params.sort,
    			}
	    		return param;
    		},
    		columns:[
            	[	{title : '序号',align: "center",valign:'middle',width:'40',rowspan: 2,colspan: 1,
        			formatter: function (value, row, index) {
        				var pageSize=$('#qualityReportTable').bootstrapTable('getOptions').pageSize;  
        				var pageNumber=$('#qualityReportTable').bootstrapTable('getOptions').pageNumber;
        				return pageSize * (pageNumber - 1) + index + 1;
        			}
        		},
        		{title:'项目名称',field:'name',align: 'center',valign:'middle',width:'150',rowspan: 2, colspan: 1,sortable:true,
        			formatter:function(val, row){
        				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.proNo;
        				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
        			}
        		},
        		{title:'PM',field:'pm',align: "center",valign:'middle',width:'60',rowspan: 2, colspan: 1,sortable:true},
        		{title:'部门',field:'department',align: 'center',valign:'middle',width:'100',rowspan: 2, colspan: 1,sortable:true,
        			formatter:function(value,row,index){
  					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
  				    }
        		},
        		{title:'类型',field:'projectType',align: 'center',valign:'middle',width:'48',rowspan: 2, colspan: 1,sortable:true},
        		{title:'统计</br>周期',field:'statisticalCycle',align: 'center',valign:'middle',width:'48',rowspan: 2, colspan: 1,sortable:true},
        		{title:'代码质量',align: 'center',valign:'middle',width:'64%',rowspan: 1, colspan: 8},
        		{title: prohibitTitle,
        	        field:'collection',width:40,align: 'center',valign:'middle',rowspan: 2,colspan: 1,
        	    	   formatter : function(value, row, index) {
        	    		    if (value!=null & value!=''){
               				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexDM"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.proNo+'\',\''+"DM"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
               			    }else{
               				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexDM"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.proNo+'\',\''+"DM"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
               			    }
        	    	   }
        	    	   
        	     }
        	],
        	[
        		{title : '静态检查告警数',field : 'id223',align: 'center',valign:'middle',width : '8%',sortable:true,formatter: function (value,row,index){return formatterValue(row.proNo,223,'静态检查告警数',value,true,"163");}},
        		{title : '代码重复率(JAVA)',field : 'id309',align: 'center',valign:'middle',width : '8%',sortable:true,formatter: function (value,row,index){return formatterValue(row.proNo,309,'代码重复率（JAVA）',value,false,"163");}},
        		{title : '代码重复率(C/C++)',field : 'id311',align: 'center',valign:'middle',width : '8%',sortable:true,formatter: function (value,row,index){return formatterValue(row.proNo,311,'代码重复率（C/C++）',value,false,"163");}},
        		{title : '平均圈复杂度',field : 'id337',align: 'center',valign:'middle',width : '8%',sortable:true,formatter: function (value,row,index){return formatterValue(row.proNo,337,'平均圈复杂度',value,false,"163");}},
        		{title : '危险函数',field : 'id385',align: 'center',valign:'middle',width : '8%',sortable:true,formatter: function (value,row,index){return formatterValue(row.proNo,385,'危险函数',value,true,"163");}},
        		{title : 'CodeDEX</br>(Coverity)',field : 'id307',align: 'center',valign:'middle',width : '8%',sortable:true,formatter: function (value,row,index){return formatterValue(row.proNo,307,'CodeDEX</br>（Coverity）',value,true,"163");}},
       			{title : 'CodeDEX</br>(Fortify)',field : 'id308',align: 'center',valign:'middle',width : '8%',sortable:true,formatter: function (value,row,index){return formatterValue(row.proNo,308,'CodeDEX</br>（Fortify）',value,true,"163");}},
                {title : 'SAI',field : 'id387',align: 'center',valign:'middle',width : '8%',sortable:true,formatter: function (value,row,index){return formatterValue(row.proNo,387,'SAI',value,false,"163");}}    			
        		]
        	],
    	locale:'zh-CN'//中文支持
    	});
      }	
       
     $.fn.refreshQualityReport=function(){
    	 var active = $("#qualityReport li[class='active']");
    	 if(active.attr("tabname")=="tab-dm"){
    		 initQualityReport();
    	 }else if(active.attr("tabname")=="tab-kx"){
    		 initReliableQuality();
    	 }else if(active.attr("tabname")=="tab-gc"){
    		 initProcessIndicators("过程指标");
    	 }else if(active.attr("tabname")=="tab-jx"){
    		 initProcessIndicators("结项指标");
    	 }else if(active.attr("tabname")=="tab-review"){
    		 initReviewQuality();
    	 }
   	}
    
    $(document).on("click","#exportKX",function(){
    	var $eleForm = $("<form method='POST'></form>");
        $eleForm.attr("action",getRootPath() + "/export/exportKX");
        $eleForm.append($('<input type="hidden" name="date" value='+ ($("#dateSection").val()) +'>'));
        $eleForm.append($('<input type="hidden" name="area" value='+ ($("#usertype1").selectpicker("val")==null?"":$("#usertype1").selectpicker("val").join()) +'>'));
        $eleForm.append($('<input type="hidden" name="hwpdu" value='+ ($("#usertype3").selectpicker("val")==null?"":$("#usertype3").selectpicker("val").join()) +'>'));
        $eleForm.append($('<input type="hidden" name="hwzpdu" value='+ ($("#usertype4").selectpicker("val")==null?"":$("#usertype4").selectpicker("val").join()) +'>'));
        $eleForm.append($('<input type="hidden" name="pduSpdt" value='+ ($("#usertype5").selectpicker("val")==null?"":$("#usertype5").selectpicker("val").join()) +'>'));
        $eleForm.append($('<input type="hidden" name="bu" value='+ ($("#usertype6").selectpicker("val")==null?"":$("#usertype6").selectpicker("val").join()) +'>'));
        $eleForm.append($('<input type="hidden" name="pdu" value='+ ($("#usertype7").selectpicker("val")==null?"":$("#usertype7").selectpicker("val").join()) +'>'));
        $eleForm.append($('<input type="hidden" name="du" value='+ ($("#usertype8").selectpicker("val")==null?"":$("#usertype8").selectpicker("val").join()) +'>'));
        $eleForm.append($('<input type="hidden" name="clientType" value='+ ($("#selectBig").selectpicker("val")=="2"?"0":"1") +'>'));
        $(document.body).append($eleForm);
        //提交表单，实现下载
        $eleForm.submit();
    });
    
    function qualityProblem(){
    	$('#problemAnalysis').bootstrapTable('destroy');
    	$("#problemAnalysis").bootstrapTable({
        	method: 'GET',
        	contentType: "application/x-www-form-urlencoded",
        	url:getRootPath() + '/projectOverview/queryProblemAnalysis',
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
            	[	{title : '序号',align: "center",valign:'middle',width: 50,rowspan: 2,colspan: 1,
        			formatter: function (value, row, index) {
        				var pageSize=$('#problemAnalysis').bootstrapTable('getOptions').pageSize;  
        				var pageNumber=$('#problemAnalysis').bootstrapTable('getOptions').pageNumber;
        				return pageSize * (pageNumber - 1) + index + 1;
        			}
        		},
        		{title:'项目名称',field:'name',align: 'center',valign:'middle',width:"100%",rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
        				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
        			}
        		},
        		{title:'PM',field:'pm',align: "center",valign:'middle',width:80,rowspan: 2, colspan: 1},
        		{title:'部门',field:'department',align: 'center',valign:'middle',width:120,rowspan: 2, colspan: 1,
        			formatter:function(value,row,index){
  					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
  				    }
        		},
        		{title:'类型',field:'projectType',align: 'center',valign:'middle',width:60,rowspan: 2, colspan: 1},
        		{title:'及时闭环</br>率(汇总)',field:'closed',align: 'center',valign:'middle',width:100,rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				var v = val==null? "0%": val;
        				return v;
        			}
    			},
        		{title:'延期</br>(汇总)',field:'delay',align: 'center',valign:'middle',width:100,rowspan: 2, colspan: 1},
        		{title:'361',align: 'center',valign:'middle',width:120,rowspan: 1, colspan: 2},
        		{title:'AAR',align: 'center',valign:'middle',width:120,rowspan: 1, colspan: 2},
        		{title:'质量回溯',align: 'center',valign:'middle',width:120,rowspan: 1, colspan: 2},
        		{title:'开工会审计',align: 'center',valign:'middle',width:120,rowspan: 1, colspan: 2},
        		{title: prohibitTitle,
        	        field:'collection',width:40,align: 'center',valign:'middle',rowspan: 2,colspan: 1,
        	    	   formatter : function(value, row, index) {
        	    		    if (value!=null & value!=''){
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexWT"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.no+'\',\''+"WT"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
               			    }else{
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexWT"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.no+'\',\''+"WT"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
               			    }
        	    	   }
        	    	   
        	     }
        	],    	
        	[
        		{title : '闭环率',field : 'problemClosed',align: 'center',valign:'middle',width : 60,
        			formatter:function(val, row){
        				var v = val==null? "0": val;
        				return v+"%";
        			}
        		},
        		{title : '延期',field : 'problemDelay',align: 'center',valign:'middle',width : 60},
        		{title : '闭环率',field : 'aarClosed',align: 'center',valign:'middle',width : 60,
        			formatter:function(val, row){
        				var v = val==null? "0": val;
        				return v+"%";
        			}
        		},
        		{title : '延期',field : 'aarDelay',align: 'center',valign:'middle',width : 60},
        		{title : '闭环率',field : 'backClosed',align: 'center',valign:'middle',width : 60,
        			formatter:function(val, row){
        				var v = val==null? "0": val;
        				return v+"%";
        			}
        		},
        		{title : '延期',field : 'backDelay',align: 'center',valign:'middle',width : 60},
        		{title : '闭环率',field : 'auditClosed',align: 'center',valign:'middle',width : 60,
        			formatter:function(val, row){
        				var v = val==null? "0": val;
        				return v+"%";
        			}
        		},
        		{title : '延期',field : 'auditDelay',align: 'center',valign:'middle',width : 60},
        		]
        	],
    	locale:'zh-CN'//中文支持
    	});
    }
    
    //qms柱状图
    function qmsSector(){
    	$.ajax({
			url : getRootPath() + '/qmsMaturity/qmsSector',
			type : "post",
			dataType : "json",
			cache : false,
			data:{
				'month':$("#dateSection").val(),
				'area': ($("#usertype1").selectpicker("val")==null || $('#select1').is(':hidden'))?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
				'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
				'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
				'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
				'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
				'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
				'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
				'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
				'coopType': collected,//0:全部项目;1：已收藏项目;2:未收藏项目
			},					
			success : function(data){
				var myChart = echarts.init(document.getElementById("qmsSector"));
		    	var option = {
		    		    title: {
		    		        text: 'QMS数据统计',
		    		        left: 'center'
		    		    },
		    		    tooltip : {
		    		        trigger: 'item',
		    		        formatter: "{b} : {c} ({d}%)"
		    		    },
		    		    legend: {
		    		        bottom: 10,
		    		        left: 'center',
		    		        data: data.rows.sources
		    		    },
		    		    series : [
		    		        {
		    		            type: 'pie',
		    		            radius : '65%',
		    		            center: ['50%', '50%'],
		    		            selectedMode: 'single',
		    		            data:data.rows.values,
		    		            itemStyle:{ 
							        normal:{ 
							           label:{ 
							              show: true, 
							              formatter: '{b} : {c}' 
							              }, 
							              labelLine :{show:true} 
							        } 
							    } 
		    		        }
		    		    ],
		    		};
				myChart.setOption(option);
			}
		});
    }
    function qmsHistogram(name){
    	$.ajax({
			url : getRootPath() + '/qmsMaturity/qmsHistogram',
			type : "post",
			dataType : "json",
			cache : false,
			data:{
				'month':$("#dateSection").val(),
				'area': ($("#usertype1").selectpicker("val")==null || $('#select1').is(':hidden'))?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
				'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
				'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
				'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
				'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
				'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
				'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
				'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
				'name': name,
				'coopType': collected,//0:全部项目;1：已收藏项目;2:未收藏项目
			},					
			success : function(data){
				var myChart = echarts.init(document.getElementById("qmsHistogram"));
		    	var option = {
		    			title : {
		    		        text: data.rows.name+'-数据统计',
		    		        x: 'center',
		    		    },
		    		    xAxis: {
		    		        type: 'category',
		    		        data: data.rows.names,
		    		        axisLabel:{
		    		            formatter:function(value){
		    		            	var newParamsName = "";// 最终拼接成的字符串
		                            var paramsNameNumber = value.length;// 实际标签的个数
		                            var provideNumber = 3;// 每行能显示的字的个数
		                            var rowNumber = Math.ceil(paramsNameNumber / provideNumber);// 换行的话，需要显示几行，向上取整
		                            // 条件等同于rowNumber>1
		                            if (paramsNameNumber > provideNumber) {
		                                /** 循环每一行,p表示行 */
		                                for (var p = 0; p < rowNumber; p++) {
		                                    var tempStr = "";// 表示每一次截取的字符串
		                                    var start = p * provideNumber;// 开始截取的位置
		                                    var end = start + provideNumber;// 结束截取的位置
		                                    // 此处特殊处理最后一行的索引值
		                                    if (p == rowNumber - 1) {
		                                        // 最后一次不换行
		                                        tempStr = value.substring(start, paramsNameNumber);
		                                    } else {
		                                        // 每一次拼接字符串并换行
		                                        tempStr = value.substring(start, end) + "\n";
		                                    }
		                                    newParamsName += tempStr;// 最终拼成的字符串
		                                }

		                            } else {
		                                // 将旧标签的值赋给新标签
		                                newParamsName = value;
		                            }
		                            return newParamsName;
		    		            }
		    		        },
		    		    },
		    		    yAxis: {
		    		        type: 'value',
		    		        minInterval: 1,
		    		    },
		    		    series: [{
		    		        data: data.rows.value,
		    		        type: 'bar',
		    		        itemStyle: {
		    		            normal:{
		    		                label: {
		    						    show: true,
		    						    position: 'top',
		    		                },
		    		                color: function (params){
		    		                    var colorList =["#c23531",
		    		                                    "#2f4554",
		    		                                    "#61a0a8",
		    		                                    "#d48265",
		    		                                    "#91c7ae",
		    		                                    "#749f83",
		    		                                    "#ca8622",
		    		                                    "#bda29a",
		    		                                    "#6e7074",
		    		                                    "#546570",
		    		                                    "#c4ccd3",
		    		                                    "#4BABDE",
		    		                                    "#FFDE76",
		    		                                    "#E43C59",
		    		                                    "#37A2DA",
		    		                                    '#C33531',
		    		                                    '#EFE42A',
		    		                                    '#64BD3D',
		    		                                    '#EE9201',
		    		                                    '#29AAE3'
		    		                                    ];
		    		                    return colorList[params.dataIndex];
		    		            }
		    		                },
		    		        }
		    		    }]
		    		};
				myChart.setOption(option);
			}
		});
    }
    function qmsDimension(){
    	$('#qmsDimension').bootstrapTable('destroy');
    	$("#qmsDimension").bootstrapTable({
    		method: 'GET',
    		contentType: "application/x-www-form-urlencoded",
    		url:getRootPath() + '/qmsMaturity/qmsDimension',
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
    				'pageSize': params.limit,
					'pageNumber': params.offset,
					'month':$("#dateSection").val(),
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
    			[	{title : '序号',align: "center",valign:'middle',width:'40',rowspan: 3,colspan: 1,
        			formatter: function (value, row, index) {
        				var pageSize=$('#qmsDimension').bootstrapTable('getOptions').pageSize;  
        				var pageNumber=$('#qmsDimension').bootstrapTable('getOptions').pageNumber;
        				return pageSize * (pageNumber - 1) + index + 1;
        			}
        		},
        		{title:'项目名称',field:'name',align: 'center',valign:'middle',width:'200',rowspan: 3, colspan: 1,
        			formatter:function(val, row){
        				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
        				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
        			}
        		},
        		{title:'PM',field:'pm',align: "center",valign:'middle',width:'80',rowspan: 3, colspan: 1},
        		{title:'部门',field:'department',align: 'center',valign:'middle',width:'100',rowspan: 3, colspan: 1,
        			formatter:function(value,row,index){
  					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
  				    }
        		},
        		{title:'类型',field:'projectType',align: 'center',valign:'middle',width:'48',rowspan: 3, colspan: 1},
        		{title:'统计</br>周期',field:'statisticalCycle',align: 'center',valign:'middle',width:'48',rowspan: 3, colspan: 1},
        		{title:'总符</br>合度',field:'peopleLamp',align: 'center',valign:'middle',width:'48',rowspan: 3, colspan: 1},
        		{title:'闭环</br>率',field:'demandProgress',align: 'center',valign:'middle',width:'66',rowspan: 3, colspan: 1},
        		{title:'QMS',align: 'center',valign:'middle',width:'60%',rowspan: 1, colspan: 15},
        		{title: prohibitTitle,
        	        field:'comment',width:30,align: 'center',valign:'middle',rowspan: 3,colspan: 1,
        	    	   formatter : function(value, row, index) {
        	    		    if (value!=null & value!=''){
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexQMS"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.no+'\',\''+"QMS"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
               			    }else{
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexQMS"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.no+'\',\''+"QMS"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
               			    }
        	    	   }
        	     }
        	],
        	[
        		{title : '项目管理',align: 'center',valign:'middle',width : '12%',rowspan: 1, colspan: 3},
        		{title : '质量策划',align: 'center',valign:'middle',width : '12%',rowspan: 1, colspan: 3},
        		{title : '质量控制',align: 'center',valign:'middle',width : '12%',rowspan: 1, colspan: 3},
        		{title : '流程要求',align: 'center',valign:'middle',width : '12%',rowspan: 1, colspan: 3},
        		{title : '知识管理',align: 'center',valign:'middle',width : '12%',rowspan: 1, colspan: 3},        	 
        	 ],
        	 [
        		{title : '是',field : 'openAudit',align: 'center',valign:'middle',width : '3%'},
         		{title : '否',field : 'openAAR',align: 'center',valign:'middle',width : '3%'},
         		{title : '符合度',field : 'planLamp',align: 'center',valign:'middle',width : '6%'},
         		{title : '是',field : 'closedAudit',align: 'center',valign:'middle',width : '3%'},
         		{title : '否',field : 'closedAAR',align: 'center',valign:'middle',width : '3%'},
         		{title : '符合度',field : 'scopeLamp',align: 'center',valign:'middle',width : '6%'},
         		{title : '是',field : 'delayAudit',align: 'center',valign:'middle',width : '3%'},
         		{title : '否',field : 'delayAAR',align: 'center',valign:'middle',width : '3%'},
         		{title : '符合度',field : 'qualityLamp',align: 'center',valign:'middle',width : '6%'},
         		{title : '是',field : 'addedAudit',align: 'center',valign:'middle',width : '3%'},
         		{title : '否',field : 'addedAAR',align: 'center',valign:'middle',width : '3%'},
         		{title : '符合度',field : 'keyRoles',align: 'center',valign:'middle',width : '6%'},
         		{title : '是',field : 'sumAudit',align: 'center',valign:'middle',width : '3%'},
         		{title : '否',field : 'sumAAR',align: 'center',valign:'middle',width : '3%'},
         		{title : '符合度',field : 'keyRolesPass',align: 'center',valign:'middle',width : '6%'},
        	 ],
    		],
    		locale:'zh-CN'//中文支持
    	});
    }
    function qmsProblem(){
    	var ids = [];
    	$('#qmsReport').bootstrapTable('destroy');
    	$("#qmsReport").bootstrapTable({
    		method: 'GET',
    		contentType: "application/x-www-form-urlencoded",
    		url:getRootPath() + '/qmsMaturity/qmsReport',
    		striped: true, //是否显示行间隔色
    		pageNumber: 1, //初始化加载第一页，默认第一页
    		pagination: false, //是否分页
    		queryParamsType: 'limit',
    		sidePagination: 'server',
    		pageSize: 20, //单页记录数
    		pageList: [5, 10, 20, 30], //分页步进值
    		minimumCountColumns: 2, //最少允许的列数
    		queryParams : function(params){
    			var param = {
					'offset': params.limit,
					'pageNumber': params.pageNumber,
					'month':$("#dateSection").val(),
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
    		onEditableSave: function (field, row, oldValue, $el) {
                $.ajax({
                    type: "post",
                    url: getRootPath()+"/qmsMaturity/qmsReportSave",
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
    		columns:
        	[	
        		{title : '序号',field:'order',align: "center",valign:'middle',width: 50,
        			cellStyle : function cellStyle(value, row,index) {
						if (row.typeLevel == 0) {
							return {
								css : {
									"background-color" : "#97deff",
									"color" : "#000",
									"text-align" : "left !important;",
								}
							};
						}
						return {
							css : {
								"border-right" : "1px solid #e7eaec !important;",
								"border-left" : "1px solid #e7eaec !important;",
							}
						};
					},
        			formatter : function(value, row, index) {
						if (row.typeLevel == "0") {
							ids.push(index);
						}
						return value == null ? "" : value;
					}
        		},
        		{title:'维度细分',field:'question',align: 'center',valign:'middle',width:130,
        			formatter:function(val, row){
        				return val==null? "-": val;
        			}
        		},
        		{title:'数量',field:'number',align: 'center',valign:'middle',width:70,
        			formatter:function(val, row){
        				return val==null? "-": val;
        			}
        		},
        		{title:'主要问题',field:'mainProblems',align: 'center',valign:'middle',width:190,
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
                        emptytext:'&#12288',
                        placement: 'bottom',
                        noeditFormatter: function (value,row,index) {
                        	if(value == null || value == ""){
                        		value = "-";
                        	}
                        	if(row.type != "3"){
                        		return '<a href="javascript:void(0)" style="color: #000;">'+value+'</a>';
                        	}
                        	return '<a href="javascript:void(0)" style="white-space:nowrap;text-overflow:ellipsis;display: block;width: 100%;overflow: hidden;height: 24px;" data-name="mainProblems" '+
                        	'data-pk="undefined" data-value="'+value+'" class="editable editable-pre-wrapped editable-click">'+value+'</a>';
                        }
                    }
        		},
        		{title:'原因分析',field:'reason',align: 'center',valign:'middle',width:190,
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
                        emptytext:'&#12288',
                        placement: 'bottom',
                        noeditFormatter: function (value,row,index) {
                        	if(value == null || value == ""){
                        		value = "-";
                        	}
                        	if(row.type != "3"){
                        		return '<a href="javascript:void(0)" style="color: #000;">'+value+'</a>';
                        	}
                        	return '<a href="javascript:void(0)" style="white-space:nowrap;text-overflow:ellipsis;display: block;width: 100%;overflow: hidden;height: 24px;" data-name="mainProblems" '+
                        	'data-name="reason" data-pk="undefined" data-value="'+value+'" class="editable editable-pre-wrapped editable-click">'+value+'</a>';
                        }
                    }
        		},
        		{title:'级别',field:'level',align: 'center',valign:'middle',width:70,
        			cellStyle: function cellStyle(value, row, index) {                    
                        return {
                            css: {
                                "overflow": "hidden",
                                "text-overflow":"ellipsis",
                            }
                        };
                    },
                    editable: {
                        type: "text", 
                        emptytext:'&#12288',//编辑框的类型。支持text|textarea|select|date|checklist等
                        placement: 'bottom',         //编辑框的模式：支持popup和inline两种模式，默认是popup
                        noeditFormatter: function (value,row,index) {
                            if(value == null || value == ""){
                            	value = "-";
                            }
                            if(row.type != "3"){
                            	return '<a href="javascript:void(0)" style="color: #000;">'+value+'</a>';
                            }
                            return '<a href="javascript:void(0)" style="display: block;width: 100%;overflow: hidden;height: 24px;" data-name="mainProblems" '+
                        	'data-name="level" data-pk="undefined" data-value="'+value+'" class="editable editable-click" data-original-title="" title="">'+value+'</a>';
                        }

                    }
        		},
        		{title:'改进措施',field:'improvement',align: 'center',valign:'middle',width:190,
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
                        emptytext:'&#12288',
                        placement: 'bottom',
                        noeditFormatter: function (value,row,index) {
                        	if(value == null || value == ""){
                            	value = "-";
                            }
                            if(row.type != "3"){
                            	return '<a href="javascript:void(0)" style="color: #000;">'+value+'</a>';
                            }
                        	return '<a href="javascript:void(0)" style="white-space:nowrap;text-overflow:ellipsis;display: block;width: 100%;overflow: hidden;height: 24px;" data-name="mainProblems" '+
                        	'data-name="improvement" data-pk="undefined" data-value="'+value+'" class="editable editable-pre-wrapped editable-click">'+value+'</a>';
                        }
                    }
        		},
        		{title:'问题占比',field:'proportion',align: 'center',valign:'middle',width:70,
        			formatter:function(val, row){
        				return val==null ? "-": val;
        			}
        		},
        	],
        	onLoadSuccess: function (res) {
        		ids = unique(ids);
        		for (var i = 0; i < ids.length; i++) {
	                $('#qmsReport').bootstrapTable('mergeCells', {
	                    index: ids[i],
	                    field: "order",
	                    colspan: 8,
	                    rowspan: 1
	                });
	            }
        		ids = [];
        	},
    		locale:'zh-CN'//中文支持
    	});
    }
    
    function initReliableQuality(){
    	$('#reliableQualityTable').bootstrapTable('destroy');
    	$('#reliableQualityTable').bootstrapTable({
    		method: 'GET',
            contentType: 'application/x-www-form-urlencoded',
            url: getRootPath() + '/projectOverview/queryProjectReliableQuality',
            striped: true, //是否显示行间隔色
            pageNumber: 1, //初始化加载第一页，默认第一页
            pagination: false, //是否分页
            sortable:true,//是否排序
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
                    'order': params.order,
                    'sort': params.sort,
    			}
	    		return param;
    		},
    		columns:[
            	[	{title : '序号',align: "center",valign:'middle',width: 34,rowspan: 2,colspan: 1,
        			formatter: function (value, row, index) {
        				var pageSize=$('#reliableQualityTable').bootstrapTable('getOptions').pageSize;  
        				var pageNumber=$('#reliableQualityTable').bootstrapTable('getOptions').pageNumber;
        				return pageSize * (pageNumber - 1) + index + 1;
        			}
        		},
        		{title:'项目名称',field:'name',align: 'center',valign:'middle',width:150,rowspan: 2, colspan: 1,sortable:true,
        			formatter:function(val, row){
        				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.proNo;
        				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
        			}
        		},
        		{title:'PM',field:'pm',align: "center",valign:'middle',width:60,rowspan: 2, colspan: 1,sortable:true},
        		{title:'部门',field:'department',align: 'center',valign:'middle',width:100,rowspan: 2, colspan: 1,sortable:true,
        			formatter:function(value,row,index){
  					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
  				    }	
        		},
        		{title:'类型',field:'projectType',align: 'center',valign:'middle',width:63,rowspan: 2, colspan: 1,sortable:true},
        		{title:'统计</br>周期',field:'statisticalCycle',align: 'center',valign:'middle',width:63,rowspan: 2, colspan: 1,sortable:true},
        		{title:'可信质量',align: 'center',valign:'middle',width:2000,rowspan: 1, colspan: 18},
        		{title: prohibitTitle,
        	        field:'collection',width:40,align: 'center',valign:'middle',rowspan: 2,colspan: 1,
        	    	   formatter : function(value, row, index) {
        	    		    if (value!=null & value!=''){
               				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexKX"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.proNo+'\',\''+"KX"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
               			    }else{
               				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexKX"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.proNo+'\',\''+"KX"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
               			    }
        	    	   }
        	    	   
        	     }
        	],    	
        	[
        		{title : '当前阶段代码行数',field : 'id653',align: 'center',valign:'middle',width : 100,sortable:true,
    				    formatter:function(val, row){
        				    return kxMeasureValue(row.proNo,107,val,'当前阶段代码行数',"164");
        			    }
    			    },
        		{title : 'Review-规范问题数',field : 'id654',align: 'center',valign:'middle',width : 200,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,116,val,'Review-规范问题数',"164");
            			}
        			},
        		{title : 'Review-逻辑问题数',field : 'id655',align: 'center',valign:'middle',width : 200,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,115,val,'Review-逻辑问题数',"164");
            			}
        			},
        		{title : 'Review-架构问题数',field : 'id656',align: 'center',valign:'middle',width : 200,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,117,val,'Review-架构问题数',"164");
            			}
        			},
        		{title : 'Review-其他问题数',field : 'id657',align: 'center',valign:'middle',width : 200,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,118,val,'Review-其他问题数',"164");
            			}
        			},
        		{title : 'Review缺陷密度',field : 'id658',align: 'center',valign:'middle',width : 150,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,114,val,'Review缺陷密度',"164");
            			}
        			},
       			{title : '圈复杂度',field : 'id659',align: 'center',valign:'middle',width : 80,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,108,val,'圈复杂度',"164");
            			}
        			},
                {title : '函数代码行',field : 'id660',align: 'center',valign:'middle',width : 80,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,110,val,'函数代码行',"164");
            			}
        			},    			
       			{title : '文件代码行',field : 'id661',align: 'center',valign:'middle',width : 80,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,106,val,'文件代码行',"164");
            			}
        			},
       			{title : '代码规模',field : 'id662',align: 'center',valign:'middle',width : 80,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,113,val,'代码规模',"164");
            			}
        			},
       			{title : '危险函数',field : 'id663',align: 'center',valign:'middle',width : 80,sortable:true,
        				formatter:function(val, row){
            				return kxMeasureValue(row.proNo,109,val,'危险函数',"164");
            			}
        			},
       			{title : '文件重复率',field : 'id664',align: 'center',valign:'middle',width : 80,sortable:true,
    				formatter:function(val, row){
        				return kxMeasureValue(row.proNo,105,val,'文件重复率',"164");
        			}
    			},
       			{title : '冗余代码',field : 'id665',align: 'center',valign:'middle',width : 80,sortable:true,
    				formatter:function(val, row){
        				return kxMeasureValue(row.proNo,111,val,'冗余代码',"164");
        			}
    			},
       			{title : '编译告警',field : 'id666',align: 'center',valign:'middle',width : 80,sortable:true,
    				formatter:function(val, row){
        				return kxMeasureValue(row.proNo,103,val,'编译告警',"164");
        			}
    			},
       			{title : '白盒测试覆盖率',field : 'id667',align: 'center',valign:'middle',width : 80,sortable:true,
    				formatter:function(val, row){
        				return kxMeasureValue(row.proNo,104,val,'白盒测试覆盖率',"164");
        			}
    			},
       			{title : '黑盒测试覆盖率(HLT)',field : 'id668',align: 'center',valign:'middle',width : 100,sortable:true,
    				formatter:function(val, row){
        				return kxMeasureValue(row.proNo,101,val,'黑盒测试覆盖率(HLT)',"164");
        			}
    			},    			
       			{title : '静态分析告警',field : 'id669',align: 'center',valign:'middle',width : 80,sortable:true,
    				formatter:function(val, row){
        				return kxMeasureValue(row.proNo,102,val,'静态分析告警',"164");
        			}
    			},
       			{title : '代码重复率',field : 'id670',align: 'center',valign:'middle',width : 80,sortable:true,
    				formatter:function(val, row){
        				return kxMeasureValue(row.proNo,112,val,'代码重复率',"164");
        			}
    			},    			
        		]
        	],
    	locale:'zh-CN'//中文支持
    	});
      }
    
    function initReviewQuality(){
    	$('#reviewQualityTable').bootstrapTable('destroy');
    	$('#reviewQualityTable').bootstrapTable({
    		method: 'GET',
            contentType: 'application/x-www-form-urlencoded',
            url: getRootPath() + '/projectOverview/queryProjectReviewQuality',
            striped: true, //是否显示行间隔色
            pageNumber: 1, //初始化加载第一页，默认第一页
            pagination: false, //是否分页
            sortable:true,//是否排序
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
                    'order': params.order,
                    'sort': params.sort,
    			}
	    		return param;
    		},
    		columns:[
            	[	{title : '序号',align: "center",valign:'middle',width: 34,rowspan: 2,colspan: 1,
	        			formatter: function (value, row, index) {
	        				var pageSize=$('#reviewQualityTable').bootstrapTable('getOptions').pageSize;  
	        				var pageNumber=$('#reviewQualityTable').bootstrapTable('getOptions').pageNumber;
	        				return pageSize * (pageNumber - 1) + index + 1;
	        			}
	        		},
	        		{title:'项目名称',field:'name',align: 'center',valign:'middle',width:150,rowspan: 2, colspan: 1,sortable:true,
	        			formatter:function(val, row){
	        				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.proNo;
	        				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
	        			}
	        		},
	        		{title:'PM',field:'pm',align: "center",valign:'middle',width:60,rowspan: 2, colspan: 1,sortable:true},
	        		{title:'部门',field:'department',align: 'center',valign:'middle',width:100,rowspan: 2, colspan: 1,sortable:true,
	        			formatter:function(value,row,index){
	  					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
	  				    }
	        		},
	        		{title:'类型',field:'projectType',align: 'center',valign:'middle',width:63,rowspan: 2, colspan: 1,sortable:true},
	        		{title:'统计</br>周期',field:'statisticalCycle',align: 'center',valign:'middle',width:63,rowspan: 2, colspan: 1,sortable:true},
	        		{title:'代码Review质量',align: 'center',valign:'middle',width:1540,rowspan: 1, colspan: 14},
	        		{title: prohibitTitle,
	        	        field:'collection',width:40,align: 'center',valign:'middle',rowspan: 2,colspan: 1,
	        	    	   formatter : function(value, row, index) {
	        	    		    if (value!=null & value!=''){
	               				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexReview"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.proNo+'\',\''+"Review"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
	               			    }else{
	               				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexReview"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.proNo+'\',\''+"Review"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
	               			    }
	        	    	   }
	        	    	   
	        	     }
        		],    	
	        	[
					{title : '代码检视规模',field : 'id116',align: 'center',valign:'middle',width : 110,sortable:true,
					    formatter:function(val, row){
						    return measureValue(row.proNo,116,val,'代码检视规模',"reviewQuality");
					    }
					},
					{title : '代码检视覆盖率',field : 'id117',align: 'center',valign:'middle',width : 110,sortable:true,
					    formatter:function(val, row){
						    return measureValue(row.proNo,117,val,'代码检视覆盖率',"reviewQuality");
					    }
					},
					{title : '新增代码review覆盖率',field : 'id643',align: 'center',valign:'middle',width : 110,sortable:true,
					    formatter:function(val, row){
						    return measureValue(row.proNo,643,val,'新增代码review覆盖率',"reviewQuality");
					    }
					},
					{title : '代码检视发现问题总数',field : 'id118',align: 'center',valign:'middle',width : 110,sortable:true,
					    formatter:function(val, row){
						    return measureValue(row.proNo,118,val,'代码检视发现问题总数',"reviewQuality");
					    }
					},
					{title : '代码检视发现问题处理率',field : 'id120',align: 'center',valign:'middle',width : 110,sortable:true,
					    formatter:function(val, row){
						    return measureValue(row.proNo,120,val,'代码检视发现问题处理率',"reviewQuality");
					    }
					},
					{title : '检视投入工作量',field : 'id140',align: 'center',valign:'middle',width : 110,sortable:true,
					    formatter:function(val, row){
						    return measureValue(row.proNo,140,val,'检视投入工作量',"reviewQuality");
					    }
					},
					{title : '已闭环检视发现问题个数',field : 'id141',align: 'center',valign:'middle',width : 110,sortable:true,
					    formatter:function(val, row){
						    return measureValue(row.proNo,141,val,'已闭环检视发现问题个数',"reviewQuality");
					    }
					},
					{title : '资料转测试前Review缺陷密度',field : 'id275',align: 'center',valign:'middle',width : 110,sortable:true,
					    formatter:function(val, row){
						    return measureValue(row.proNo,275,val,'资料转测试前Review缺陷密度',"reviewQuality");
					    }
					},
	        		{title : '代码Review缺陷密度',field : 'id313',align: 'center',valign:'middle',width : 110,sortable:true,
    				    formatter:function(val, row){
        				    return measureValue(row.proNo,313,val,'代码Review缺陷密度',"reviewQuality");
        			    }
    			    },
	        		{title : 'Review-规范问题数',field : 'id654',align: 'center',valign:'middle',width : 110,sortable:true,
        				formatter:function(val, row){
            				return measureValue(row.proNo,654,val,'Review-规范问题数',"reviewQuality");
            			}
        			},
	        		{title : 'Review-逻辑问题数',field : 'id655',align: 'center',valign:'middle',width : 110,sortable:true,
        				formatter:function(val, row){
            				return measureValue(row.proNo,655,val,'Review-逻辑问题数',"reviewQuality");
            			}
        			},
	        		{title : 'Review-架构问题数',field : 'id656',align: 'center',valign:'middle',width : 110,sortable:true,
        				formatter:function(val, row){
            				return measureValue(row.proNo,656,val,'Review-架构问题数',"reviewQuality");
            			}
        			},
	        		{title : 'Review-其他问题数',field : 'id657',align: 'center',valign:'middle',width : 110,sortable:true,
        				formatter:function(val, row){
            				return measureValue(row.proNo,657,val,'Review-其他问题数',"reviewQuality");
            			}
        			},
	        		{title : '代码Review缺陷密度（罗素）',field : 'id751',align: 'center',valign:'middle',width : 110,sortable:true,
        				formatter:function(val, row){
            				return measureValue(row.proNo,751,val,'代码Review缺陷密度（罗素）',"reviewQuality");
            			}
        			}   			
        		]
        	],
    	locale:'zh-CN'//中文支持
    	});
      }

    function initProcessIndicators(indicators){
    	var tableId = "";
    	if(indicators =="过程指标"){
    		tableId = "#processIndicatorsTable" ;
    	}else{
    		tableId = "#concludingIndicatorsTable" ;
    	}
    	$(tableId).bootstrapTable('destroy');
    	$(tableId).bootstrapTable({
    		method: 'GET',
            contentType: 'application/x-www-form-urlencoded',
            url: getRootPath() + '/projectOverview/processNominalsTable',
            detailView: true, //父子表
            striped: true, //是否显示行间隔色
            pageNumber: 1, //初始化加载第一页，默认第一页
            pagination: true, //是否分页
            queryParamsType: 'limit',
            sidePagination: 'server',
            sortable: true, //是否启用排序
            pageSize: 20, //单页记录数
            pageList: [5, 10, 20, 30], //分页步进值
            minimumCountColumns: 2, //最少允许的列数
    		queryParams : function(params){
	    		var param = {
	    			'target': "3" ,
	    			'category':encodeURI(indicators),
	    			'statisticalTime':$("#dateSection").val(),
    				'limit': params.limit,
    				'offset': params.pageNumber,
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
                    'sort': params.sort,//排序列名  
                    'sortOrder': params.order //排位命令(DESC,ASC)
    			}
	    		return param;
    		},
    		columns:[
            	{title : '序号',align: "center",valign:'middle',width: 40,
        			formatter: function (value, row, index) {
        				var pageSize=$(tableId).bootstrapTable('getOptions').pageSize;  
        				var pageNumber=$(tableId).bootstrapTable('getOptions').pageNumber;
        				return pageSize * (pageNumber - 1) + index + 1;
        			}
        		},
        		{title:'项目名称',field:'name',align: 'center',valign:'middle',width:150,sortable:true,
        			formatter:function(val, row){
        				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
        				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
        			}
        		},
        		{title:'PM',field:'pm',align: "center",valign:'middle',width:50,sortable:true},
        		{title:'部门',field:'department',align: 'center',valign:'middle',width:100,sortable:true,
        			formatter:function(value,row,index){
  					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
  				    }	
        		},
        		{title:'类型',field:'projectType',align: 'center',valign:'middle',width:50,sortable:true},
        		{title:'统计周期',field:'statisticalCycle',align: 'center',valign:'middle',width:36,sortable:true},
        		{title:'近五次</br>状态评估',field:'fiveStatusAssessment',width:200,align: 'center',valign:'middle',
             	   formatter:function(value,row,index){
     	        	   var proNo = row.no;
     	        	   evaluationState = getEvaluationState(value);
                        evaluationState = '<div id="assess" style="margin-left:-1.5%;margin-top:3%;">'+
     		             '<span>'+evaluationState+'</span>'+
     		                '</div>';
     	               return evaluationState;
                    }
                },
                {title: prohibitTitle,
        	        field:'peopleLamp',width:40,align: 'center',valign:'middle',
        	    	   formatter : function(value, row, index) {
        	    		    if (value!=null & value!=''){
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexZB"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.no+'\',\''+"ZB"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
               			    }else{
               				     return '<a href="javascript:void(0)" id="'+row.no+"IndexZB"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.no+'\',\''+"ZB"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
               			    }
        	    	   }
        	    	   
        	     }
    		],
			onExpandRow: function(index, row, $detail) {
		
		        InitSubTable(indicators, row, $detail);
		
		     },
			locale:'zh-CN'//中文支持
    	});
      }
    InitSubTable = function(indicators, row, $detail) {
	   	var targetList = row.targetList;
	    var clos = composeCol(targetList);
	   	var cur_table = $detail.html('<table></table>').find('table');
    	$(cur_table).bootstrapTable({
    		method : 'get',
			contentType : 'application/x-www-form-urlencoded',
			url : getRootPath()+ '/projectOverview/queryMeasureValueList',
            striped: true, //是否显示行间隔色
            pageNumber: 1, //初始化加载第一页，默认第一页
            pagination: false, //是否分页
            queryParamsType: 'limit',
            sidePagination: 'server',
            pageSize: 20, //单页记录数
            pageList: [5, 10, 20, 30], //分页步进值
            minimumCountColumns: 2, //最少允许的列数
    		queryParams : function(params){
    			var param = {
    					'proNo' : row.no,
    					'category':encodeURI(indicators),
						'statisticalTime' : $("#dateSection").val()
    			}
	    		return param;
    		},
    		columns:clos,
    		locale:'zh-CN'//中文支持
    	});
    }
function composeCol(titles){
	var clos = [];
	if(titles.length>0){
		clos.push({field:'cycle',title:'周期',align:"center",width:100});
        for (var i = 0; i < titles.length; i++) {
            clos.push({field:titles[i].ID,title:titles[i].name,align:"center",width:100});
        }
	}else{
		clos.push({field:'cycle',title:'周期',align:"center",width:10});
		clos.push({field:' ',title:' ',align:"center",width:100});
	}
    return clos;
}
function measureValue(projectid, id, val, name, measureMark){
	var value = "-";
	if(val != undefined){
		if(val.value != undefined){
			value = val.value;
		}
	}
	return '<a href="javascript:void(0)" onclick="measureValueLine(\''+projectid+'\','+id+',\''+name+'\',\''+measureMark+'\')">'+value+'</a>';
}
function kxMeasureValue(projectid, id, val, name, measureMark){
	var deng = "";
	var value = "-";
	if(val != undefined){
		if(val.value != undefined){
			value = val.value;
		}
		deng = getKxDeng(val.light);
	}
    return deng+'<div style="float: left;margin-left: 10px"><a href="javascript:void(0)" onclick="measureValueLine(\''+projectid+'\','+id+',\''+name+'\',\''+measureMark+'\')">'+value+'</a></div>';
}

function measureValueLine(projectid, id, name, measureMark){
	$("#stateEvaluationTitle").html($("#dateSection").val());
	$.ajax({
		url : getRootPath() + '/projectOverview/measureValueLine',							
		type : "post",
		dataType : "json",
		cache : false,
		data:{
			'projectId':projectid,
            'id':id,
            'measureMark':measureMark,
            'nowDate':$("#dateSection").val()
            },	
		success : function(data){
			$("#stateEvaluationModal").modal('show');
			brokenLineDiagram(data,name,'指标趋势(');
		}
	});
	
    }
//数组除重
function unique(arr){
    var res=[];
    for(var i=0,len=arr.length;i<len;i++){
        var obj = arr[i];
        for(var j=0,jlen = res.length;j<jlen;j++){
            if(res[j]===obj) break;            
        }
        if(jlen===j)res.push(obj);
    }
    return res;
}
function getEvaluationState(value){
	var result = "";
	for(var i = 0; i < value.length; i++){
		if(value[i] != -1){
			evaluationState = '<div style="display: inline-block;width: 43px;height: 15px;margin-left: 1px;background-color: '+getColor(getLamp("statusAssessment",value[i]).text)+';"></div>'
		}else{
			evaluationState = '<div style="display: inline-block;width: 43px;height: 15px;margin-left: 1px;"></div>'
		}
		result += evaluationState;
	}
	return result;
}
function getKxDeng(light){
	var deng = "";
	if (light != "") {
		deng = '<div style="display: inline-block;border-radius: 50%;width: 15px;height:15px;'
			+'background-color:'+light+';margin-top: 6px;float: left;margin-left: 20px;"></div>';
	}
    return deng;
}
