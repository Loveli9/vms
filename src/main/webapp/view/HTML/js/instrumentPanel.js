$(function(){
	
	getComment();
	var projNo = window.parent.projNo;
	var divPieceLength = 0;//1个迭代的长度
	var myCharts;
	var proEndDate;
	
	$("#exportbut").click(function(){
		if(!projNo){
			toastr.warning('导出信息为空');
		}else{
			var $eleForm = $("<form method='POST'></form>");
	        $eleForm.attr("action",getRootPath() + "/measureComment/qualityReviewExport");
	        $eleForm.append($('<input type="hidden" name="proNo" value="'+ projNo +'">'));
	        $(document.body).append($eleForm);
	        $eleForm.submit();
		}
	})
   shezhi = function (){
		$("#taskSelection").modal('show');
		$("#completion").bootstrapTable('destroy');
		$('#completion').bootstrapTable({
			method: 'post',
	    	contentType: "application/x-www-form-urlencoded",
	        url : getRootPath() + '/IteManage/completion',
	     	editable:false,// 可行内编辑
	    	striped: true, // 是否显示行间隔色
	    	/*responseHandler: function (res) {
                return {
                    "rows": res.data,
                    "total": res.totalCount
                }
            },*/
	    	pageNumber: 1, // 初始化加载第一页，默认第一页
	    	pagination:true,// 是否分页
	    	queryParamsType:'limit',
	    	sidePagination:'client',//server
	    	pageSize:10,//单页记录数
	    	pageList:[10,15,20],//分页步进值
	        queryParams : function(params){
	        	var param = {
	        			proNo : projNo,
	        			iteId : $('#diedai_ranjin_sel option:selected').val(),
						}
					return param;
			},
			columns:[
				{
	        		title:'全选',
	        		field:'display',
	        		align : "center",
	        		width : 50,
	        		checkbox:true,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			if(value==0){
	        				var result = new Object;
					        result.checked =true;
					        result.disabled =false;
					        return result;
	        			}else{
	        				var result = new Object;
					        result.checked =false;
					        result.disabled = false;
					        return result;
	        			}
	        			
	    			}
	        	},
				{
	        		title : '标题',
	        		field : 'topic',
	        		align: "center",
	        		width: 180,
	        		height:50,
	        		valign:'middle',
	            },
				{
	        		title : '优先级',
	        		field : 'prior',
	        		align: "center",
	        		width: 80,
	        		height:50,
	        		valign:'middle'
	            },
	            {
	        		title : '重要级',
	        		field : 'importance',
	        		align: "center",
	        		width: 100,
	        		height:50,
	        		valign:'middle',
	            },
				{
	        		title : '状态',
	        		field : 'status1',
	        		align: "center",
	        		width: 100,
	        		height:50,
	        		valign:'middle',
	            },
	            {
	        		title : '完成度(%)',
	        		field : 'finalimit',
	        		align: "center",
	        		width: 100,
	        		height:50,
	        		valign:'middle',
	            },
	            {
	        		title : '责任人',
	        		field : 'person_liable',
	        		align: "center",
	        		width: 150,
	        		height:50,
	        		valign:'middle',
	            }
	        ],
			locale:'zh-CN'//中文支持
		});
	}
	
	add_backBtn = function(){
		$("#taskSelection").modal('hide');
	}
	add_saveBtn = function(){
		var selectRow = $('#completion').bootstrapTable('getData');
		var lm = $('#completion').bootstrapTable('getOptions').pageNumber; 
		var ofs = $('#completion').bootstrapTable('getOptions').pageSize;
		var tot = $('#completion').bootstrapTable('getOptions').totalRows;
		var list = [];
		var j = 0;
		var m = 0;var n =0;
		if(lm*ofs>tot){
			n=tot;
		}else{n = lm*ofs}
		for(m = (lm-1)*ofs;m<n;m++){
			if((selectRow[m].display)== true){
				selectRow[m].display='0';
			}else{
				selectRow[m].display='1';
			}
		}
		for(var i=0;i<selectRow.length;i++){
			if((selectRow[i].display)=='0'){j++;}
    			var data ={
    	    		'ids' :selectRow[i].ids,
    	    		'display':selectRow[i].display,
    			}
    			list.push(data);
		}
		if(j < 5){
			$.ajax({
	    		url: getRootPath() + '/IteManage/editCompletion',
	    		type:'post',
	    		dataType:"json",  
	    	    contentType : 'application/json;charset=utf-8', //设置请求头信息 
	    		data:JSON.stringify(list),
	    		success:function(data){
	    			if(data.code == 'success'){
	    				toastr.success('配置成功！');
	    				$("#taskSelection").modal('hide');
	    				diedaiRanjinSelChenged();   				        				
	    			}else{
	    				toastr.error('配置失败!');
	    			}
	    		}
	    	});
		}else{
			toastr.warning('最多选择4条数据');
			shezhi();
		}
		
	}
	function getProjNoName(){
		$.ajax({
			url: getRootPath() + '/iteration/getProjNoName',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				proNo : projNo
			},
			success: function (data) {
				$("#projName").text(data.name+"("+new Date().format('yyyy/MM/dd')+")");
				proEndDate = data.endDate;
			}
		});
	}
	
	function info(){
		divPieceLength = parseInt($("#milestone").width()/3);
		$.ajax({
			url: getRootPath() + '/iteration/getMessage',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				proNo : projNo
			},
			success: function (data) {
				var len = data.data.length;
				var i = 0;
				var tab="";
				if(len>=3){
					$("#next").css("display","block")
					$("#milestone").children("ul").css("width",divPieceLength*len+"px");
				}else{
					$("#next").css("display","none")
					$("#milestone").children("ul").css("width",divPieceLength*len+"px");
				}
				var time = new Date().getTime();
				var proday = Math.ceil((proEndDate-time)/(24*3600*1000));
				$("#notification").html("");
				if(proday < 14){
					$("#notification").append("●距离项目验收还有"+proday+"天，请尽快完成相关工作！</br>");
				}
				_.each(data.data, function(config, index){
					if(time>=config.planStartDate || new Date(config.planStartDate).format('yyyy/MM/dd')==new Date(time).format('yyyy/MM/dd')){
						i++;
					}
					//****添加通知，下拉选项框信息,里程碑描述，里程碑进度*************************
					if((time>=config.planStartDate && time<=config.planEndDate) || new Date(config.planStartDate).format('yyyy/MM/dd')==new Date(time).format('yyyy/MM/dd')){
						var day = Math.ceil((config.planEndDate-time)/(24*3600*1000));
						if(day > 7 && proday > 14){
							$("#notificationMsg").css("display","none");
						}
						if(day <= 7){
							$("#notification").append("●距离迭代结束还有"+day+"天，请尽快完成相关工作！");
						}
						$("#diedai_ranjin_sel").append('<option value="'+config.id+'" selected>'+config.iteName+'(当前迭代)</option>');
						$("#diedai_story_sel").append('<option value="'+config.id+'" selected>'+config.iteName+'(当前迭代)</option>');
						$("#diedai_bug_sel").append('<option value="'+config.id+'" selected>'+config.iteName+'(当前迭代)</option>');
						
						var day1 = parseInt((config.planEndDate-config.planStartDate)/(24*3600*1000));
						var day2 = parseInt((time-config.planStartDate)/(24*3600*1000));
						var left = 0;
						if(day1!=0){
							left=(day2/day1*100).toFixed(2);
						}
						tab = '<div name="diedai_message" class="diedai_message" '+
						'style="width:'+divPieceLength+'px">'+
						'<span id="notificationClose" title="'+new Date(time).format('yyyy/MM/dd') +
						'" class="glyphicon glyphicon-map-marker" style="left:calc('+left+'% - 5px);top: -10px; '+
						'font-size: 12px;float: left;color: #ff0000;-webkit-transform: rotate(-180deg);" aria-hidden="true"></span>'+
						new Date(config.planStartDate).format('yyyy/MM/dd')+'-'+
						new Date(config.planEndDate).format('yyyy/MM/dd')+'<br>'+config.iteName+'</div>';
					}else if(time>=config.planStartDate && new Date(config.planEndDate).format('yyyy/MM/dd')==new Date(time).format('yyyy/MM/dd')){
						$("#notification").html("●该项目迭代结束为今天，请尽快完成相关工作！");
						$("#diedai_ranjin_sel").append('<option value="'+config.id+'" selected>'+config.iteName+'(当前迭代)</option>');
						$("#diedai_story_sel").append('<option value="'+config.id+'" selected>'+config.iteName+'(当前迭代)</option>');
						$("#diedai_bug_sel").append('<option value="'+config.id+'" selected>'+config.iteName+'(当前迭代)</option>');
						
						tab = '<div name="diedai_message" class="diedai_message" '+
						'style="width:'+divPieceLength+'px">'+
						'<span id="notificationClose" title="'+new Date(config.planEndDate).format('yyyy/MM/dd')+
						'" class="glyphicon glyphicon-map-marker" style="left:calc(100% - 10px);top: -10px;'+
						'font-size: 12px;float: left;color: #ff0000;-webkit-transform: rotate(-180deg);" aria-hidden="true"></span>'+
						new Date(config.planStartDate).format('yyyy/MM/dd')+'-'+
						new Date(config.planEndDate).format('yyyy/MM/dd')+'<br>'+config.iteName+'</div>';
					}else{
						$("#diedai_ranjin_sel").append('<option value="'+config.id+'">'+config.iteName+'</option>');
						$("#diedai_story_sel").append('<option value="'+config.id+'">'+config.iteName+'</option>');
						$("#diedai_bug_sel").append('<option value="'+config.id+'">'+config.iteName+'</option>');
						
						tab = '<div name="diedai_message" class="diedai_message" '+
						'style="width:'+divPieceLength+'px">'+
						new Date(config.planStartDate).format('yyyy/MM/dd')+'-'+
						new Date(config.planEndDate).format('yyyy/MM/dd')+'<br>'+config.iteName+'</div>';
					}
					//*********************************************
					$("#diedai_mes").append(tab);
				});
				$("div[name=diedai]").css("width",divPieceLength*i+"px");
				$("div[name=diedai_message]").css("width",divPieceLength+"px");
				
				var left = 0;
				if(len>3 && i>1){
					if((len-i)>=3){
						$("#prev").css("display","block");
						$("#next").css("display","block");
						left = -1*divPieceLength*(i-1);
					}else{
						$("#prev").css("display","block");
						$("#next").css("display","none");
						left = -1*divPieceLength*(len-3);
					}
					$("#milestone").children("ul").animate({"left":left+"px"},300);
				}
			}
		});
	}
	$(document).on("click", "#notificationClose", function () {
		$("#notificationMsg").css("display","none");
	});
	$(document).on("click", "#prev", function () {
		var left = $("#milestone").children("ul").css("left");
		left = parseInt(left.substring(0,left.length-2));
		left = left+divPieceLength*3;
		if(left>=0){
			left=0;
			$("#prev").css("display","none");
		}
		$("#milestone").children("ul").animate({"left":left+"px"},300);
		$("#next").css("display","block");
	});
	$(document).on("click", "#next", function () {
		var left = $("#milestone").children("ul").css("left");
		left = parseInt(left.substring(0,left.length-2));
		left = left-divPieceLength*3;
		var width = $("#milestone").children("ul").css("width");
		width = parseInt(width.substring(0,width.length-2));
		if((width+left)<=(divPieceLength*3)){
			left=divPieceLength*3-width;
			$("#next").css("display","none");
		}
		$("#milestone").children("ul").animate({"left":left+"px"},300);
		$("#prev").css("display","block");
	});
	
	function diedaiRanjinSelChenged(){
		$.ajax({
			url: getRootPath() + '/IteManage/burnoutFigure',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				iteId : $('#diedai_ranjin_sel option:selected').val()
			},
			success: function (data) {
				var divLabel="";
				types = ['实际进度'];
				month = data.data.month;
				ids = data.data.ids;
				names = data.data.names;
				finalimit = data.data.finalimit;
				var series = [
					{
						type:'line',
						symbolSize: 2,//拐点大小
						showAllSymbol:true,
						itemStyle:{
							normal:{
								lineStyle:{
									color:'#367FA9',
									width: 1,
								},	
								areaStyle:{
									
								},
								color: new echarts.graphic.LinearGradient(0, 0, 0, 1,[
									{offset: 0, color: '#367FA9'},
									{offset: 1, color: 'white'}
			               		])
							}	
						},
						data:data.data.value 
					}
				]
				burnDownCharts(types,month,series);
				completionDegree(ids,names,finalimit);
				
			}
		});
	}
	function completionDegree(ids,names,finalimit){
		$("#completionDegree").html("");
		var divLabel = "";
		var color = "";
		for(var i=0;i<names.length;i++){
			if (i == 0) {
				color = "#0A9DBB";
			} else if (i == 1) {
				color = "#C92519";
			} else if (i == 2) {
				color = "#039E6E";
			} else if (i == 3) {
				color = "#DC871A";
			}
			divLabel +=
				   "<div id='"+ids[i]+"' style='margin-top:10px;border: 0px;width:100%;height:40px;'>" +
				   "<span class='BurnoutChartConfiguration'>"+ names[i] +"</span>" +
				   "<span style='float:right;margin:0 auto;'>"+ finalimit[i] +"%</span>" +
				   "<div style='background-color: #E6E6E6;margin:0 auto;width:100%;height:11px;margin-top:3px;'>" +
				   "<div style='float:left;background-color: "+color+";border: 0px;margin:0 auto;width:"+finalimit[i]+"%;height:11px;'></div>" +
				   "</div></div>";
		}
		$("#completionDegree").html(divLabel);
	}
	function burnDownCharts(types,month,seriesval){
		//折线图
		var l = parseInt(month.length / 4);
		var options1={
				//定义标题
				title:{
					
				},
				//调整图表大小
				grid:{
                    x:40,
                    //y:45,
                    x2:25,
                    //y2:20,
                    //borderWidth:1
                },
				//鼠标悬停
				tooltip: {
					trigger: 'axis'
				},
				//图标
				legend:{
					data:types
				},
				//x轴设置
				xAxis:{
					name:'',
					splitLine:{show: false},//去除网格线			
					data:month,
					nameTextStyle:{//设置x轴标题属性
		            	color:'#000000',
						fontSize:14
		            },
					axisTick:{//x轴刻度
		       			show:false
		   			},
					axisLabel:{//x轴标示属性
						textStyle:{
							color:'#000000',
							fontSize:12
						},
		   			    interval:l 
					},
					axisLine:{//设置轴线属性
						lineStyle:{
							color:'#367FA9',
							width:2					
						}
		            }
				},
				//y轴设置
				yAxis:{
					name:'Story数',
					splitLine:{show: false},//去除网格线
					nameTextStyle:{//设置y轴标题属性
						color:'#000000',
						fontSize:16
					},
					axisTick:{//y轴刻度
						show:false
					},
					axisLabel:{//y轴标示属性
						textStyle:{
							color:'#000000',
							fontSize:12,
						}
						},
					axisLine:{//设置轴线属性
						lineStyle:{
							color:'#2e8afc',
							width:2						
						}
					}
				},
				series:seriesval
		}
		myCharts.setOption(options1);
	}
	function storyCirclifulCharts(){
		$.ajax({
			url: getRootPath() + '/IteManage/getStoryData',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				iteId : $('#diedai_story_sel option:selected').val()
			},
			success: function (data) {
				$("#storyCharts").html('<div id="myStat1" '+ 
						'data-dimension="150" data-text="'+(valueIsNull(data.data.closedLoopRate,0)*100).toFixed(2)+'%" data-info="闭环率" '+
						'data-width="20" data-fontsize="20" data-percent="'+(valueIsNull(data.data.closedLoopRate,0)*100).toFixed(2)+'" '+
						'data-fgcolor="#F45150" data-bgcolor="#cacaca" class="circliful"></div>');
				$('#myStat1').circliful();
				$('#storyOk').html('<span  style="font-size: 20px;">'+valueIsNull(data.data.num,0)+'/</span>'+valueIsNull(data.data.total,0)+'个');
				$('#storyData').html('<div style="float: left;margin-right: 10px;padding-right: 10px;border-right: 2px solid #dcdcdc;">'+
		       			'平均周期时间<span style="font-size: 20px;">'+valueIsNull(data.data.days,0)+'</span>天'+
			       		'</div>'+
			       		'<div>超期<span style="font-size: 20px;">'+valueIsNull(data.data.overDue,0)+'</span>个</div>');
			}
		});
		
	}
	function bugCirclifulCharts(){
		$.ajax({
			url: getRootPath() + '/IteManage/getBugData',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				iteId : $('#diedai_bug_sel option:selected').val()
			},
			success: function (data) {
				$("#bugCharts").html('<div id="myStat0" '+ 
						'data-dimension="150" data-text="'+(valueIsNull(data.data.closedLoopRate,0)*100).toFixed(2)+'%" data-info="闭环率" '+
						'data-width="20" data-fontsize="20" data-percent="'+(valueIsNull(data.data.closedLoopRate,0)*100).toFixed(2)+'" '+
				'data-fgcolor="#F45150" data-bgcolor="#cacaca" class="circliful"></div>');
				$('#myStat0').circliful();
				$('#bugOk').html('<span  style="font-size: 20px;">'+valueIsNull(data.data.num,0)+'/</span>'+valueIsNull(data.data.total,0)+'个');
				
				$('#bugData').html('<div style="float: left;margin-right: 10px;padding-right: 10px;border-right: 2px solid #dcdcdc;">'+
						'平均周期时间<span style="font-size: 20px;">'+valueIsNull(data.data.days,0)+'</span>天'+
						'</div>'+
						'<div>超期<span style="font-size: 20px;">'+valueIsNull(data.data.overDue,0)+'</span>个</div>');
			}
		});
	}
	function bugSubmission(){
		$.ajax({
			url: getRootPath() + '/IteManage/bugSubmission',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				no : projNo,
			},
			success: function (data) {
				$("#bugSubmission").html('<div id="bugsta" '+ 
						'data-dimension="150" data-text="'+(valueIsNull(data.data.closedLoopRate,0)*100).toFixed(2)+'%" data-info="中软占比" '+
						'data-width="20" data-fontsize="20" data-percent="'+(valueIsNull(data.data.closedLoopRate,0)*100).toFixed(2)+'" '+
				'data-fgcolor="#F45150" data-bgcolor="#cacaca" class="circliful"></div>');
				$('#bugsta').circliful();
				$('#bugCritical').html('<span  style="font-size: 20px;">'+valueIsNull(data.data.critical,0)+'/</span>'+valueIsNull(data.data.sum,0)+'个');
				$('#bugMajor').html('<span  style="font-size: 20px;">'+valueIsNull(data.data.major,0)+'/</span>'+valueIsNull(data.data.sum,0)+'个');
				$('#bugMinor').html('<span  style="font-size: 20px;">'+valueIsNull(data.data.minor,0)+'/</span>'+valueIsNull(data.data.sum,0)+'个');
				$('#bugSuggestion').html('<span  style="font-size: 20px;">'+valueIsNull(data.data.suggestion,0)+'/</span>'+valueIsNull(data.data.sum,0)+'个');
				$('#bugren').html('<div >'+
						'个人平均提交数<span style="font-size: 20px;">'+valueIsNull(data.data.number,0)+'</span>个/人</div>');
			}
		});
	}
	$(document).on("change", "#diedai_ranjin_sel", function () {
		diedaiRanjinSelChenged();
	});
	$(document).on("change", "#diedai_story_sel", function () {
		storyCirclifulCharts();
	});
	$(document).on("change", "#diedai_bug_sel", function () {
		bugCirclifulCharts();
	});
	function mytab1(){
		$('#mytab1').bootstrapTable({
	    	method: 'post',
	    	contentType: "application/x-www-form-urlencoded",
	    	url:getRootPath() + '/PMpaper/bugCount',
	    	toolbar: '#toolbar',
	    	height: 235,
	    	sortable: false, //是否启用排序
	    	sortOrder: "asc",
	    	striped: true, //是否显示行间隔色
	    	dataField: "rows",
	    	queryParamsType:'limit',
	    	sidePagination:'server',
	    	showRefresh:false,//刷新按钮
	    	showColumns:false,
	    	minimumCountColumns: 2,//最少允许的列数
	    	toolbarAlign:'right',
	    	toolbar:'#toolbar',//指定工作栏
	    	queryParams: function(params){
	        	var param = {
	        	        no : projNo,
	        	        sort: params.sort,//排序列名  
	                    sortOrder: params.order //排位命令（desc，asc） 
	        	    }
	        	return param;
	        },
	    	columns:[
	        	{
	        		title:'迭代名称',
	        		halign :'center',
	        		align : 'center',
	        		field:'iteName',
	        	},
	        	{
	        		title:'致命',
	        		halign :'center',
	        		align : 'center',
	        		field:'critical',
	        		sortable:true,
	        	},
	        	{
	        		title:'严重',
	        		halign :'center',
	        		align : 'center',
	        		field:'major',
	        		sortable:true,
	        	},
	        	{
	        		title:'一般',
	        		halign :'center',
	        		align : 'center',
	        		field:'minor',
	        		sortable:true,
	        	},
	        	{
	        		title:'提示',
	        		halign :'center',
	        		align : 'center',
	        		field:'suggestion',
	        		sortable:true,
	        	},
	        	{
	        		title:'总计',
	        		halign :'center',
	        		align : 'center',
	        		field:'alls',
	        		sortable:true
	        	},
	        	{
	        		title:'遗留DI',
	        		halign :'center',
	        		align : 'center',
	        		field:'di',
	        		sortable:true
	        	},
	    	],
	    	locale:'zh-CN',//中文支持,
	    });
	}
	function mytab2(){
		$('#mytab2').bootstrapTable({
			method: 'post',
			contentType: "application/x-www-form-urlencoded",
			url:getRootPath() + '/PMpaper/storyCount',
			toolbar: '#toolbar',
			height: 235,
			sortable: false, //是否启用排序
			sortOrder: "asc",
			striped: true, //是否显示行间隔色
	//		responseHandler: function (res) {
//                return {
//                    "rows": res.data,
//                    "total": res.totalCount
//                }
 //           },
			dataField: "rows",
			queryParamsType:'limit',
			sidePagination:'server',
			showRefresh:false,//刷新按钮
			showColumns:false,
			minimumCountColumns: 2,//最少允许的列数
			toolbarAlign:'right',
			toolbar:'#toolbar',//指定工作栏
			queryParams: function(params){
				var param = {
						no : projNo,
						sort: params.sort,//排序列名  
						sortOrder: params.order //排位命令（desc，asc） 
				}
				return param;
			},
			columns:[
				{
					title:'迭代名称',
					halign :'center',
					align : 'center',
					field:'iteName',
				},
				{
					title:'新建',
					halign :'center',
					align : 'center',
					field:'status1',
					sortable:true,
				},
				{
					title:'进行中',
					halign :'center',
					align : 'center',
					field:'status2',
					sortable:true,
				},
				{
					title:'已解决',
					halign :'center',
					align : 'center',
					field:'status3',
					sortable:true,
				},
				{
					title:'测试中',
					halign :'center',
					align : 'center',
					field:'status4',
					sortable:true,
				},
				{
					title:'已拒绝',
					halign :'center',
					align : 'center',
					field:'status5',
					sortable:true,
				},
				{
					title:'已关闭',
					halign :'center',
					align : 'center',
					field:'status6',
					sortable:true,
				},
				{
					title:'总计',
					halign :'center',
					align : 'center',
					field:'alls',
					sortable:true
				},
			],
			locale:'zh-CN',//中文支持,
		});
	}

	function mytab3(){
		$('#mytab3').bootstrapTable({
	    	method: 'post',
	    	contentType: "application/x-www-form-urlencoded",
	    	url:getRootPath() + '/PMpaper/bugSubmission',
	    	toolbar: '#toolbar',
	    	height: 235,
	    	sortable: false, //是否启用排序
	    	sortOrder: "asc",
	    	striped: true, //是否显示行间隔色
	    	dataField: "rows",
	    	queryParamsType:'limit',
	    	sidePagination:'server',
	    	showRefresh:false,//刷新按钮
	    	showColumns:false,
	    	minimumCountColumns: 2,//最少允许的列数
	    	toolbarAlign:'right',
	    	toolbar:'#toolbar',//指定工作栏
	    	queryParams: function(params){
	        	var param = {
	        	        no : projNo,
	        	        sort: params.sort,//排序列名  
	                    sortOrder: params.order //排位命令（desc，asc） 
	        	    }
	        	return param;
	        },
	    	columns:[
	    		{
					title:'提交人',
					halign :'center',
					align : 'center',
					field:'creator',
				},
				{
					title:'致命',
					halign :'center',
					align : 'center',
					field:'critical',
					sortable:true,
				},
				{
					title:'严重',
					halign :'center',
					align : 'center',
					field:'major',
					sortable:true,
				},
				{
					title:'一般',
					halign :'center',
					align : 'center',
					field:'minor',
					sortable:true,
				},
				{
					title:'提示',
					halign :'center',
					align : 'center',
					field:'suggestion',
					sortable:true,
				},
				{
					title:'总计',
					halign :'center',
					align : 'center',
					field:'alls',
					sortable:true,
				},
	    		],
			locale:'zh-CN',//中文支持,
		});
	}
    var prTable;
	function prTab(){
        $('#prTab').bootstrapTable({
			method: 'post',
			contentType: "application/json",
			url:getRootPath() + '/projectReport/getProjectDscPageByNo',
			toolbar: '#toolbar',
			height: 235,
			striped: true, //是否显示行间隔色
			queryParamsType:'limit',
			sidePagination:'server',
			showRefresh:false,//刷新按钮
			showColumns:false,
			minimumCountColumns: 2,//最少允许的列数
			toolbarAlign:'right',
			toolbar:'#toolbar',//指定工作栏
			queryParams: function(params){
				var param = {
						'no': projNo,
						'flag': 1
				}
				return param;
			},
            detailView:true,
            detailFormatter:function(index,row){
				var imprMeasure=row.imprMeasure==null?'-':row.imprMeasure;
				var progressDesc=row.progressDesc==null?'-':row.progressDesc;
                var str = "改进措施:"+imprMeasure+"</br>进展&效果描述:"+progressDesc;
                return str;
            },
			columns:[
				{title:'问题描述',field:'question',width:900,align : 'left',
	            	// formatter:function(value,row,index){
	        		// 	if(value){
	        		// 		return	'<span style="width:300px;float:left;overflow:hidden;text-overflow:ellipsis;" title="' + value + '">' + value + '</span>';
	        		// 	}
	        		// 	return '-';
	        		// },
	            },
	            {title:'改进措施',field:'imprMeasure',width:300,align : 'left',visible: false},
	            {title:'进展&效果描述',field:'progressDesc',width:300,align : 'left',visible: false},
	            {title:'计划完成时间',field:'finishTime',width:100,
	                formatter:function(value,row,index){
	                    if (value == null){
	                        return "-";
	                    }
	                    var dd=new Date(value).format('yyyy-MM-dd');
	                    return	'<span title="' + dd + '">' + dd + '</span>';
	                },
	            },
	            {title:'实际完成时间',field:'actualFinishTime',width:100,
	                formatter:function(value,row,index){
	                    if (value == null){
	                        return "-";
	                    }
	                    var dd=new Date(value).format('yyyy-MM-dd');
	                    return	'<span title="' + dd + '">' + dd + '</span>';
	                },
	            },
	            {title:'状态',field:'state',width:100,
	            	formatter:function(value,row,index){
	        			if(value){
	        				return	'<span title="' + value + '">' + value + '</span>';  
	        			}
	        			return '-';
	        		},
	            }
			],
			locale:'zh-CN',//中文支持,
		});

        prTable = $('#prTab').data('bootstrap.table');

    }
	function prCogTab(){	
		$('#prCogTab').bootstrapTable({
			method: 'post',
			contentType: "application/json",
			url:getRootPath() + '/projectReport/getProjectDscPageByNo',
			height: 235,
			striped: true, //是否显示行间隔色
			pageNumber: 1, //初始化加载第一页，默认第一页
            pagination: true, //是否分页
            queryParamsType: 'limit',
            sidePagination: 'server',
			showRefresh:false,//刷新按钮
			showColumns:false,
			minimumCountColumns: 2,//最少允许的列数
			queryParams: function(params){
				var param = {
						'no': projNo,
						'limit': params.limit,
						'offset': params.offset, 
				}
				return param;
			},
			columns:[
				{title:'全选',field:'flag',width:25,checkbox:true,
	        		formatter: function (value,row,index){
	        			if(value==1){
	        				var result = new Object;
					        result.checked =true;
					        result.disabled = false;
					        return result;
	        			}else{
	        				var result = new Object;
					        result.checked =false;
					        result.disabled = false;
					        return result;
	        			}
					}},
	            {title : '序号',align: "center",width: 60,
	        		formatter: function (value, row, index) {
	        		    var pageSize=$('#prCogTab').bootstrapTable('getOptions').pageSize;  
	        		    var pageNumber=$('#prCogTab').bootstrapTable('getOptions').pageNumber;
	        		    return pageSize * (pageNumber - 1) + index + 1;
	        		}
	            },
	            {title:'问题id ',field:'id',width:80,visible: false},
	            {title:'项目编号 ',field:'no',width:80,visible: false},
	            {title:'问题描述',field:'question',width:300,align : 'left',
	            	formatter:function(value,row,index){
	        			if(value){
	        				return	'<span style="width:300px;float:left;overflow:hidden;text-overflow:ellipsis;" title="' + value + '">' + value + '</span>'; 
	        			}
	        			return '-';
	        		},
	            },
	            {title:'改进措施',field:'imprMeasure',width:300,align : 'left',
	            	formatter:function(value,row,index){
	        			if(value){
	        				return	'<span style="width:300px;float:left;overflow:hidden;text-overflow:ellipsis;" title="' + value + '">' + value + '</span>'; 
	        			}
	        			return '-';
	        		},
	            },
	            {title:'进展&效果描述',field:'progressDesc',width:300,align : 'left',
	            	formatter:function(value,row,index){
	        			if(value){
	        				return	'<span style="width:300px;float:left;overflow:hidden;text-overflow:ellipsis;" title="' + value + '">' + value + '</span>'; 
	        			}
	        			return '-';
	        		},
	            },
	            {title:'计划完成时间',field:'finishTime',width:100,
	                formatter:function(value,row,index){
	                    if (value == null){
	                        return "-";
	                    }
	                    var dd=new Date(value).format('yyyy-MM-dd');
	                    return	'<span title="' + dd + '">' + dd + '</span>';
	                },
	            },
	            {title:'实际完成时间',field:'actualFinishTime',width:100,
	                formatter:function(value,row,index){
	                    if (value == null){
	                        return "-";
	                    }
	                    var dd=new Date(value).format('yyyy-MM-dd');
	                    return	'<span title="' + dd + '">' + dd + '</span>';
	                },
	            },
	            {title:'状态',field:'state',width:100,
	            	formatter:function(value,row,index){
	        			if(value){
	        				return	'<span title="' + value + '">' + value + '</span>';  
	        			}
	        			return '-';
	        		},
	            }
			],
			locale:'zh-CN',//中文支持,
		});		
	}
	//打开问题列表定制配置页面
	$(document).on("click", "#btn_cog", function () {
		$('#prCogTab').bootstrapTable('refresh');
		$("#cogPage").modal('show');
    });

	//打开问题列表定制配置页面
	$(document).on("click", "#cog_saveBtn", function () {
		var dataArr=$('#prCogTab').bootstrapTable('getData');
		var list = [];
    	for(var i=0;i<dataArr.length;i++){	
    		var flag ;
    		if(dataArr[i].flag){
    			flag = 1;
    		}else{
    			flag = 0;
    		}
    		list.push(
    			{id:dataArr[i].id,flag :flag}
    				);
    	}
    	$.ajax({
    		url: getRootPath() + '/projectReport/editProjectDscConfig',
    		type:'post',
    		dataType:"json",  
    	    contentType : 'application/json;charset=utf-8', //设置请求头信息 
    		data:JSON.stringify(list),
    		success:function(data){
    			if(data.code == 'success'){
    				$('#prTab').bootstrapTable('refresh',{url:getRootPath() + '/projectReport/getProjectDscPageByNo'});
    				toastr.success('配置成功！');
    				$("#cogPage").modal('hide');
                    prTable.refresh();
    				// $("#instrumentPanel", parent.document).click();

    			}else{
    				toastr.error('配置失败!');
    			}
    		}
    	});
    	
    });
	
	var myCharts1;
	var myCharts2;
	var myCharts3;
	var myCharts4;
	var option1;
	var option2;
	var option3;
	var option4;
	function gaugeInfo(){
		$.ajax({
			url: getRootPath() + '/instrumentPanel/board',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				no : projNo
			},
			success: function (data) {
				option1 = gaugeChartsInfo(valueIsNull(data.loc,0),valueIsNull(data.locStand,80),"累计总代码量");
				option2 = gaugeChartsInfo(valueIsNull(data.eeRate,0),valueIsNull(data.eeRateStand,80),"E2E生产率");
				option3 = gaugeChartsInfo(valueIsNull(data.month,0),valueIsNull(data.monthStand,80),"累计人力投入");
				option4 = gaugeChartsInfo(valueIsNull(data.payRate,0),valueIsNull(data.payRateStand,80),"累计需求交付率");
				myCharts1.setOption(option1, true);
				myCharts2.setOption(option2, true);
				myCharts3.setOption(option3, true);
				myCharts4.setOption(option4, true);
				$("#standardValue1").html(valueIsNull(data.locStand,80));
				$("#standardValue2").html(valueIsNull(data.eeRateStand,80));
				$("#standardValue3").html(valueIsNull(data.monthStand,80));
				$("#standardValue4").html(valueIsNull(data.payRateStand,80));
			},
		});
		
	}
	
	function gaugeChartsInfo(value,max,name){
		max = parseInt(max / 8 * 10);
		var option = {
				tooltip : {
				    formatter: "{a} <br/>{b} : {c}"
				},
				series : [
				    {
				        name:name,
				        max:max,
				        type:'gauge',
				        axisLine: {// 坐标轴线
			                lineStyle: {
			        			color: [
			        				[0.2, '#228b22'],
			        				[0.8, '#48b'],
			        				[1, '#ff4500']
			        			],
			        			width:20
			        		}
			            },
			            axisLabel:{
			            	show:false,
			            	fontSize:10,
			                distance :0
			            },
			            splitLine: {
			            	length:20
			            },
			            pointer:{
			            	width:4
			            },
				        detail : {formatter:'{value}',fontSize:14},
				        data:[{value: value, name: ""}]
				    }
				]
			};
			return option;
	}
	
	$(document).on("click", "#saveValue", function () {
		var stId = $("#saveStandardValue").attr('stId');
		var id;
		if(stId=='1'){
			id = '170';
		}else if (stId=='2'){
			id = '172';
		}else if (stId=='3'){
			id = '171';
		}else if (stId=='4'){
			id = '173';
		}
		//发送到后台
		$.ajax({
			url: getRootPath() + '/instrumentPanel/saveStandardValue',
			type: 'post',
			data:{
				id : id,
				no : projNo,
				value : $("#StandardValueMax").val()
			},
			success: function (data) {
				
			}
		});
		var max = valueIsNull($("#StandardValueMax").val(),80);
		max = parseInt(max / 8 * 10);
		eval("option" + stId).series[0].max=max;
		eval("myCharts" + stId).setOption(eval("option" + stId), true);
		$("#standardValue"+stId).html($("#StandardValueMax").val());
		$("#saveStandardValue").modal('hide');
	});
	
	function instrumentPanelValue(){
		$.ajax({
			url: getRootPath() + '/instrumentPanel/instrumentPanelValue',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				no : projNo
			},
			success: function (data) {
				var lis1=new Array();
				for(var i=0;i<data.length;i++){
					var li=$('li[id='+data[i]+']');
					lis1[i]=li;
				}
				$("#sortable1").append($("#sortable2").children());
				$("#sortable2").empty();
				var lis=$("#sortable1").children();
				$("#sortable1").empty();
				var lis2=new Array();
				for(var i=0;i<lis.length;i++){
					var flag=true;
					for(var j=0;j<lis1.length;j++){
						if(lis[i].id==lis1[j].get(0).id){
							flag=false;
							break;
						}
					}
					if(flag==true){
						lis2[i]=lis[i];
					}
				}
				$("#sortable1").append(lis1);
				$("#sortable2").append(lis2);
			},
		});
	}
	
	function exportPMpaper(){
		$("#exportPMpaper").click(function(){
			$("#headImage").css('display','block');
			canvasImage();
		})
	}
	
	function canvasImage(){
		var canvas = document.createElement("canvas");
		var options = {
			    //检测每张图片都已经加载完成
			    tainttest:true,
			    canvas: canvas,
		};
		html2canvas(document.getElementById("all_div"),options).then(function (canvas) {
			var dataURL = canvas.toDataURL('images/png');//获取图片数据URL，获取后是data:image/png;base64,iVBORw0KGgoA..后面一堆.. 
			$("#preview").attr("src",dataURL);
			$('#codeType').modal({
				backdrop: 'static',
				keyboard: false
		    });
			$("#codeType").modal('show');
		});  
	}
	
	$(document).on("click", "#quxiaosendImage", function () {
		$("#headImage").css('display','none');
		$("#codeType").modal('hide');
	});
	$(document).on("click", "#sendImage", function () {
		dataURL = $("#preview").attr("src");
		var imageDataB64 = dataURL.substring(22);
		//发送到后台
		$.ajax({
			url: getRootPath() + '/iteration/saveImage',
			type: 'post',
			data:{
				imageDataB64 : imageDataB64,
				proNo : projNo
			},
			success: function (data) {
				toastr.info(data.code);
			},
		});
		$("#headImage").css('display','none');
		$("#codeType").modal('hide');
	});
	
	function saveWidth(){
		var width = $("#sortable1").width();
		$("#fired").css('width', width*1.0);
		$("#locs").css('width', width*0.25);
		$("#eTe").css('width', width*0.25);
		$("#months").css('width', width*0.25);
		$("#pays").css('width', width*0.25);
		$("#storys").css('width', width*0.667);
		$("#story").css('width', width*0.333);
		$("#bugs").css('width', width*0.667);
		$("#bug").css('width', width*0.333);
		$("#comment").css('width', width*0.667);
		$("#comment1").css('width', width*0.999);
		$("#problemANDrisk").css('width', width*0.999);
		$("#bugSta").css('width', width*0.667);
		$("#buga").css('width', width*0.333);
		
	}
	
	function valueIsNull(value,defaultVal){
		if(value){
			return value;
		}else{
			return defaultVal;
		}
	}	
	$(document).ready(function(){
		saveWidth();
		myCharts=echarts.init(document.getElementById("burnDownCharts"));
		myCharts1 = echarts.init(document.getElementById("gaugeCharts1"));
		myCharts2 = echarts.init(document.getElementById("gaugeCharts2"));
		myCharts3 = echarts.init(document.getElementById("gaugeCharts3"));
		myCharts4 = echarts.init(document.getElementById("gaugeCharts4"));
		//获取项目名
		getProjNoName();
		//初始化迭代图
		info();
		//燃尽图数据获取，绘图
		diedaiRanjinSelChenged();
		//需求数据获取
		storyCirclifulCharts();
		//bug数据获取
		bugCirclifulCharts();
		//bug提交统计
		bugSubmission();
		//导出按钮
		exportPMpaper();
		//bug表格
		mytab1();
		//bug提交表格
		mytab3();
		//story表格
		mytab2();
		//问题&风险表格
		prTab();
		//问题&风险列表定制
		prCogTab();
	
		//仪表盘
		gaugeInfo();
		//按照保存的顺序排列
		instrumentPanelValue();
	})
})

//添加点评
function saveComment(){
	var projNo = window.parent.projNo;
	var cdate=new Date();
	var mdate=new Date();
	var del=0;
	$.ajax({
		url: getRootPath() + '/comment/insertComment',
		type: 'post',
		data:{	
				create_time:cdate,
				modify_time:mdate,
				is_deleted:del,
				project_id : projNo,
				comment : $("#save").val()
				
		},
		success: function (data) {
			$("#saveNew").hide();
			$("#update").show();
			$("#clean").hide();	
			$("#giveUp").hide();	
			$("#save").css("border","none");
			toastr.success("添加信息成功");
			getComment();
		}		
	});
	$("#save").attr("readonly","true")
}
function saveComment1(){
	var projNo = window.parent.projNo;
	var cdate=new Date();
	var mdate=new Date();
	var del=0;
	$.ajax({
		url: getRootPath() + '/comment/insertComment',
		type: 'post',
		data:{	
				create_time:cdate,
				modify_time:mdate,
				is_deleted:del,
				project_id : projNo,
				comment : $("#save1").val()				
		},
		success: function (data) {
			$("#saveNew1").hide();
			$("#update1").show();
			$("#clean1").hide();	
			$("#giveUp1").hide();	
			$("#save1").css("border","none");
			toastr.success("添加信息成功");
			getComment();
		}		
	});
	$("#save1").attr("readonly","true")
}

//获取后台点评数据
function getComment(){
	var projNo = window.parent.projNo;
	$.ajax({
		url:getRootPath()+'/comment/selectComment',
		type:'post',
		data:{
			project_id : projNo			
		},
		success: function (data) {
			$("#save").val(data);
			$("#save1").val(data);
				$("#saveNew").hide();
				$("#saveUpdate").hide();
				$("#giveUp").hide();
				$("#clean").hide();
				$("#save").attr("readonly","true");
				$("#save").css("border","none");
				$("#saveNew1").hide();
				$("#saveUpdate1").hide();
				$("#giveUp1").hide();
				$("#clean1").hide();
				$("#save1").attr("readonly","true");
				$("#save1").css("border","none");	
		}		
	})
}

//修改点评
function updateComment(){
	if($("#save").val()==""){
		$("#save").css("border","");
		$("#save").removeAttr("readonly")
		$("#giveUp").show();
		$("#saveNew").show();
		$("#clean").show();
		$("#update").hide();
		$("#saveUpdate").hide();;
		
	}else{
		$("#save").css("border","");
		$("#save").removeAttr("readonly")
		$("#giveUp").show();
		$("#saveUpdate").show();
		$("#clean").show();
		$("#update").hide();
	}
	
}

function updateComment1(){
	if($("#save1").val()==""){
		$("#save1").css("border","");
		$("#save1").removeAttr("readonly")
		$("#giveUp1").show();
		$("#saveNew1").show();
		$("#clean1").show();
		$("#update1").hide();
		$("#saveUpdate1").hide();;
		
	}else{
		$("#save1").css("border","");
		$("#save1").removeAttr("readonly")
		$("#giveUp1").show();
		$("#saveUpdate1").show();
		$("#clean1").show();
		$("#update1").hide();
	}
	
}

//修改保存
function saveUpdate(){
	var projNo = window.parent.projNo;
	var mdate=new Date();
	$("#saveUpdate").hide();
	if($("#save").val()==""){
		var projNo = window.parent.projNo;
		var mdate=new Date();
			$.ajax({
					url:getRootPath()+'/comment/cleanComment',
					type:'post',
					data:{
						modify_time:mdate,
						project_id : projNo,	
						is_deleted:1
					},
					success: function (data) {
						getComment();
						 $("#save").removeAttr("readonly")
					}
			})
	}
	$.ajax({
			url:getRootPath()+'/comment/updateComment',
			type:'post',
			data:{
				modify_time:mdate,
				project_id : projNo,
				comment:$("#save").val()
			},
			success: function (data) {
				$("#update").show();
				$("#giveUp").hide();
				$("#clean").hide();
				$("#save").attr("readonly","true");
				$("#save").css("border","none");
				toastr.success("修改信息成功");
				getComment();
			}	
			
		});
	
}


function saveUpdate1(){
	var projNo = window.parent.projNo;
	var mdate=new Date();
	$("#saveUpdate1").hide();
	if($("#save1").val()==""){
		var projNo = window.parent.projNo;
		var mdate=new Date();
			$.ajax({
					url:getRootPath()+'/comment/cleanComment',
					type:'post',
					data:{
						modify_time:mdate,
						project_id : projNo,	
						is_deleted:1
					},
					success: function (data) {
						getComment();
						 $("#save1").removeAttr("readonly")
					}
			})
			//return;
	}
	$.ajax({
			url:getRootPath()+'/comment/updateComment',
			type:'post',
			data:{
				modify_time:mdate,
				project_id : projNo,
				comment:$("#save1").val()
			},
			success: function (data) {
				$("#update1").show();
				$("#giveUp1").hide();
				$("#clean1").hide();
				$("#save1").attr("readonly","true");
				$("#save1").css("border","none");
				toastr.success("修改信息成功");
				getComment();
			}			
		});
}

//删除点评数据
function cleanComment(){	
	 $("#save").val(""); 
}
function cleanComment1(){	
	 $("#save1").val(""); 
}
//取消修改
function giveUp(){	
	getComment();
	$("#clean").show();
	$("#update").show();
}
function giveUp1(){	
	getComment();
	$("#clean1").show();
	$("#update1").show();
}
function standardVlaue(stId){
	$("#saveStandardValue").attr('stId',stId);
	$("#saveStandardValue").modal('show');
	$("#StandardValueMax").val($("#standardValue"+stId).text());
	
}

