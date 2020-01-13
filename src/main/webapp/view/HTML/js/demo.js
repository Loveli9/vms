$(function(){
	var projNo = getQueryString("projNo");
	var divPieceLength = 0;//1个迭代的长度
	var myCharts=echarts.init(document.getElementById("burnDownCharts"));
	
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
				_.each(data.data, function(config, index){
					if(time>=config.planStartDate){
						i++;
					}
					//****添加通知，下拉选项框信息,里程碑描述，里程碑进度*************************
					if(time>=config.planStartDate && time<=config.planEndDate){
						var day = parseInt((config.planEndDate-time)/(24*3600*1000));
						if(day > 7 && proday > 14){
							$("#notificationMsg").css("display","none");
						}
						if(day < 7){
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
						'" class="glyphicon glyphicon-map-marker" style="left:calc('+left+'% - 10px);top: -10px; '+
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
				types = ['实际进度'];
				month = data.data.month;
				var series = [
					{
						type:'line',
						symbolSize: 5,//拐点大小
						showAllSymbol:true,
						itemStyle:{
							normal:{
								lineStyle:{
									color:'#367FA9'	
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
			}
		});
		
	}
	function burnDownCharts(types,month,seriesval){
		//折线图
		var options1={
				//定义标题
				title:{
					
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
					name:'时间',
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
							fontSize:14
						}
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
					name:'任务数',
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
							fontSize:16
						}
					},
					axisLine:{//设置轴线属性
						lineStyle:{
							color:'#2e8afc',
							width:2						
						}
					}
				},
				//指定图表类型
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
						'data-dimension="150" data-text="'+data.data.closedLoopRate*100+'%" data-info="闭环率" '+
						'data-width="20" data-fontsize="20" data-percent="'+data.data.closedLoopRate*100+'" '+
						'data-fgcolor="#F45150" data-bgcolor="#cacaca" class="circliful"></div>');
				$('#myStat1').circliful();
				$('#storyOk').html('<span  style="font-size: 20px;">'+data.data.num+'/</span>'+data.data.total+'个');
				$('#storyData').html('<div style="float: left;margin-right: 10px;padding-right: 10px;border-right: 2px solid #dcdcdc;">'+
		       			'平均周期时间<span style="font-size: 20px;">'+data.data.days+'</span>天'+
			       		'</div>'+
			       		'<div>超期<span style="font-size: 20px;">'+data.data.overDue+'</span>个</div>');
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
						'data-dimension="150" data-text="'+data.data.closedLoopRate*100+'%" data-info="闭环率" '+
						'data-width="20" data-fontsize="20" data-percent="'+data.data.closedLoopRate*100+'" '+
				'data-fgcolor="#F45150" data-bgcolor="#cacaca" class="circliful"></div>');
				$('#myStat0').circliful();
				$('#bugOk').html('<span  style="font-size: 20px;">'+data.data.num+'/</span>'+data.data.total+'个');
				$('#bugData').html('<div style="float: left;margin-right: 10px;padding-right: 10px;border-right: 2px solid #dcdcdc;">'+
						'平均周期时间<span style="font-size: 20px;">'+data.data.days+'</span>天'+
						'</div>'+
						'<div>超期<span style="font-size: 20px;">'+data.data.overDue+'</span>个</div>');
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
	    	sortable: true,                     //是否启用排序
	    	sortOrder: "asc",
	    	striped: true, //是否显示行间隔色
	    	dataField: "rows",
	    	queryParamsType:'limit',
	    	sidePagination:'server',
	    	showRefresh:true,//刷新按钮
	    	showColumns:true,
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
	        		title:'DI',
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
			sortable: true,                     //是否启用排序
			sortOrder: "asc",
			striped: true, //是否显示行间隔色
			dataField: "rows",
			queryParamsType:'limit',
			sidePagination:'server',
			showRefresh:true,//刷新按钮
			showColumns:true,
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
	
	function getProjNoName(){
		$.ajax({
			url: getRootPath() + '/iteration/getProjNoName',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				proNo : projNo
			},
			success: function (data) {
				$("#projName").text(data+"("+new Date().format('yyyy/MM/dd')+")");
			}
		});
	}
	
	var myCharts1 = echarts.init(document.getElementById("gaugeCharts1"));
	var myCharts2 = echarts.init(document.getElementById("gaugeCharts2"));
	var myCharts3 = echarts.init(document.getElementById("gaugeCharts3"));
	var myCharts4 = echarts.init(document.getElementById("gaugeCharts4"));
	var option1;
	var option2;
	var option3;
	var option4;
	function gaugeInfo(){
		$.ajax({
			url: getRootPath() + '/instrumentPanel/board',
			type: 'post',
			data:{
				no : projNo
			},
			success: function (data) {
				option1 = gaugeChartsInfo(valueIsNull(data.loc,0),valueIsNull(data.locStand,80),"累计总代码量");
				option2 = gaugeChartsInfo(valueIsNull(data.eeRate,0),valueIsNull(data.eeRateStand,80),"E2E生产率");
				option3 = gaugeChartsInfo(valueIsNull(data.month,0),valueIsNull(data.monthStand,80),"累计投入人月");
				option4 = gaugeChartsInfo(valueIsNull(data.payRate,0),valueIsNull(data.payRateStand,80),"项目总需求交付率");
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
				        axisLine: {            // 坐标轴线
			                lineStyle: {
			        			color: [
			        				[0.2, '#228b22'],
			        				[0.8, '#48b'],
			        				[1, '#ff4500']
			        			],
			        			width:20
//			        			width:10
			        		}
			            },
			            axisLabel:{
			            	show:false,
			            	fontSize:10,
			                distance :0
//			                distance :-30
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
	
	function pdfTest(){
		var canvas = document.createElement("canvas");
//		var sourceContent = $("#all_div");
//		var width = sourceContent.width();
//		var height = sourceContent.height();
//		var offsetTop = sourceContent.offset().top;
//		var offsetLeft = sourceContent.offset().left;
//		// 不能小于1，否则图片不完整，通过获取设备的像素密度不能统一得到清晰的图片，建议写固定值
//		var scale = 1;
//		canvas.width = (width + offsetLeft) * scale;
//		canvas.height = (height + offsetTop) * scale;
//		var options = {
//		    //检测每张图片都已经加载完成
//		    tainttest:true,
//		    canvas: canvas,
//		    scale: scale,
//		    //dom 放大的宽度，放大倍数和清晰度在一定范围内成正相关
//		    width: width + offsetLeft,
//		    // 开启日志，可以方便调试
//		    logging: true,
//		    //dom 放大的宽度，放大倍数和清晰度在一定范围内成正相关
//		    height: height + offsetTop
//		};
//		html2canvas(document.getElementById("all_div"), options).then(function (canvas) {
		var options = {
			    //检测每张图片都已经加载完成
			    tainttest:true,
			    canvas: canvas,
		};
		html2canvas(document.getElementById("all_div"),options).then(function (canvas) {
		//html2canvas(document.body).then(function (canvas) {
			var dataURL = canvas.toDataURL('images/png');//获取图片数据URL，获取后是data:image/png;base64,iVBORw0KGgoA..后面一堆.. 
			var imageDataB64 = dataURL.substring(22);//这里我们需要将上面获取的一堆东西截掉前面的“data:image/png;base64,”，只保留后面的  
			//发送到后台
			$.ajax({
				url: getRootPath() + '/iteration/saveImage',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					imageDataB64 : imageDataB64,
					proNo : projNo
				},
				success: function (data) {
					closeWindow()
				},
				error: function() {
					closeWindow()
				}
			});
		});  
	}
	
	function closeWindow(){
		window.opener=null;
		window.open('','_self');
		window.close();
	}
	
	function valueIsNull(value,defaultVal){
		if(value){
			return value;
		}else{
			return defaultVal;
		}
	}
	
	$(document).ready(function(){
		//初始化迭代图
		info();
		//获取项目名
		getProjNoName();
		//燃尽图数据获取，绘图
		diedaiRanjinSelChenged();
		//需求数据获取
		storyCirclifulCharts();
		//bug数据获取
		bugCirclifulCharts();
		//bug表格
		mytab1();
		//story表格
		mytab2();
		//仪表盘
		gaugeInfo();
		//按照保存的顺序排列
		instrumentPanelValue();
		
		setTimeout(function(){
			//截图
			pdfTest();
		},2000)
		/* window.opener=null;
		window.open('','_self');
		window.close(); */
	})
})

function standardVlaue(stId){
	$("#saveStandardValue").attr('stId',stId);
	$("#saveStandardValue").modal('show');
	$("#StandardValueMax").val($("#standardValue"+stId).text());
	
}