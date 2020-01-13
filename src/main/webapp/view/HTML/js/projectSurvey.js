(function(){
	
	function initBasicIndex(){
		$.ajax({
			url: getRootPath() + '/yunlongPromotion/getBasic',
			type: 'post',
			async: false,//是否异步，true为异步
			data: {
				bu : "云应用与服务业务线",
				client : 1
			},
			success: function(data){
				basicData = data.data;
				var list = 	basicData[1].list;
				
				var select_option="";
				$("#selectIndex").empty();
				_.each(list, function(val, index){//(值，下标)
					select_option += "<option value="+list[index].id+">"+list[index].name+"</option>";
				})
				$("#selectIndex").html(select_option);
				
			}
		});
		
	};
	function queryBasicYear(){
		$("#selectIndex").change(function(){
			var list = basicData[1].list;
			var indexId =  $("#selectIndex").val();
			$('#selectYear').selectpicker("val",[]);
			$("#selectYear").empty();
			$('#selectYear').prev('div.dropdown-menu').find('ul').empty();
			
			if(list != null && list.length > 0){
				for (var i = 0; i < list.length; i++) {
					if(list[i].id == indexId){
						var yearList = list[i].years;
						if(yearList != null && yearList.length > 0){
							for(var j = 0; j < yearList.length; j++){
								$('#selectYear').append("<option value=" + yearList[j] + ">" + yearList[j] + "</option>"); 
							}
						}
					}
		        } 
			}
	        $('#selectYear').selectpicker('refresh');  
	        $('#selectYear').selectpicker('render'); 
			
		})
	};
	
	$(document).ready(function(){
		loadCountColor();
		loadPie();
		initBasicIndex();
		queryBasicYear();
	})
})();

/*
 * 点击项目数加载项目列表
 */
function loadDatagrid(osel){
	document.getElementById("hideDivYL").style.display="none";
	document.getElementById("tabsShowYL").style.display="";
	
	var type = '';
	if(osel == 'red'){
		$("#tabsTitle").html("红灯项目列表");
		type = 'red';
		var backPage = '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color: #f57454; "></div>';
	}else if(osel == 'yellow'){
		$("#tabsTitle").html("黄灯项目列表");
		type = 'yellow';
		var backPage = '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color: #f7b547; "></div>';
	}else if(osel == 'green'){
		$("#tabsTitle").html("绿灯项目列表");
		type = 'green';
		var backPage = '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color: #1adc21; "></div>';
	}else{
		$("#tabsTitle").html("未评测项目列表");
		type = 'wcp';
		var backPage = '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color:#bdb7b7; "></div>';
	}
	$.ajax({
		url: getRootPath() + '/yunlongSearchProject/searchProjectByColor',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			'color' : osel,
        },
		success: function (data) {
			var dataList = data.data;
			$("#tableYL tr").remove();
			for(var i=0;i<dataList.length;i++){
				var tab = '<tr">'+
					'<td><a target="_blank" style="color: #721b77;" title="'+
					dataList[i].NAME+'" href="'+getRootPath() +'/bu/projView?projNo='+
					dataList[i].NO+'&a='+Math.random()+'">'+convertDataVal(dataList[i].NAME)+'</a></td>'+
					'<td>'+convertDataVal(dataList[i].PM)+'</td>'+
					'<td>'+convertDataVal(dataList[i].AREA)+'</td>'+
					'<td>'+convertDataVal(dataList[i].HWPDU)+'</td>'+
					'<td>'+convertDataVal(dataList[i].HWZPDU)+'</td>'+
					'<td>'+convertDataVal(dataList[i].PDU_SPDT)+'</td>'+
					'<td>'+convertDataVal(dataList[i].PROJECT_STATE)+'</td>'+
					'<td>'+convertDataVal(dataList[i].scope)+'</td>'+
					'<td>'+convertDataVal(dataList[i].progress)+'</td>'+
					'<td>'+convertDataVal(dataList[i].processquality)+'</td>'+
					'<td>'+convertDataVal(dataList[i].efficiency)+'</td></tr>'
					;
				
				$("#tableYL").append(tab);
			}
		}
	});
	tablelswInfo(10,'tableYL');
	hideMenu();
}

//隐藏左侧菜单栏
function hideMenu(){
	$(".fold").html("<p>〉</p>").addClass("active").animate({"left":"0px"},300).find("p").css("marginLeft","2px");
	$(".fold").siblings(".left").animate({"width":"0px"},300);
	$(".fold").siblings(".right").animate({"left":"0px"},300).find("iframe").animate({"width":"100%"},300);
	$("#leftDiv").attr("style","display:none");
	$("#leftFold").attr("style","display:none");
} 

function rebackPage(){
	document.getElementById("hideDivYL").style.display="";
	document.getElementById("tabsShowYL").style.display="none";
}

function convertDataVal(val){
	
	
	if(val){
		
		if(val == 'red'){
			return '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color: #f57454; "></div>';
		}else if(val == 'yellow'){
			return '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color: #f7b547; "></div>';
			
		}else if ((val == 'green')){
			return '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color: #1adc21; "></div>'
		}
		
		return val;
	}else {
		return "";
	}
}
function loadPie(){
	
	var redPros = $("#redPros").text();
	var yellowPros = $("#yellowPros").text();
	var greenPros = $("#greenPros").text();
	var echartsPie;
	
	var json = [ {
		value : redPros,
		name : '红灯项目数'
	}, {
		value : yellowPros,
		name : '黄灯项目数'
	}, {
		value : greenPros,
		name : '绿灯项目数'
	}];
	
	var option = {
			title : {},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c}({d}%)"
			},
			legend : {
				orient : 'vertical',
				x : 'left',
				textStyle : {
					fontSize : 10
				},
				height : 80,
				itemHeight : 10,
				itemWidth : 14,
				data : [ '红灯项目数', '黄灯项目数', '绿灯项目数' ]
			},
			color : [ '#f57454', '#f7b547', '#1adc21'],
			calculable : true,
			series : [ {
				name : '项目亮灯',
				type : 'pie',
				radius : '75%',//饼图的半径大小  
				center : [ '55%', '55%' ],//饼图的位置  
				itemStyle : {
					normal : {
						label : {
							show : false, //隐藏标示文字
						},
						labelLine : {
							show : false
						//隐藏标示线
						}
					}
				},
				data : json
			} ]
		};
		echartsPie = echarts.init(document.getElementById('divpie'));
		echartsPie.setOption(option);
	
	
}


function loadCountColor() {
	$.ajax({
		url: getRootPath() + '/yunlongSearchProject/searchProjectResult',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			
			
		},
		success: function (data) {
			$("#redPros").text(data.data.redPros);
			$("#yellowPros").text(data.data.yellowPros);
			$("#greenPros").text(data.data.greenPros);
		}
	});
	
}


var basicData;
var bu = "云应用与服务业务线";
var pdu;
var du;

function selectOnchangeBu(){
	var indexId =  $("#selectIndex").val();
	var year =  $("#selectYear").val();
	
	$.ajax({
		url: getRootPath() + '/yunlongPromotion/getSeasonData',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			bu : bu,
			client : 1,
			indexId : indexId,
			year : year
		},
		success: function(data){
			loadCharts(data.data.sourceData,"sourceChart","数据趋势图","指标值","BU");
			loadCharts(data.data.promotionData,"promotionChart","提升项趋势图","提升值","BU");
		}
	});
	
};

function selectOnchangePdu(pduName){
	pdu = pduName;
	var indexId =  $("#selectIndex").val();
	var year =  $("#selectYear").val();
	
	$.ajax({
		url: getRootPath() + '/yunlongPromotion/getSeasonData',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			bu : bu,
			pdu : pdu,
			client : 1,
			indexId : indexId,
			year : year
		},
		success: function(data){
			loadCharts(data.data.sourceData,"sourceChart","数据趋势图","指标值","PDU");
			loadCharts(data.data.promotionData,"promotionChart","提升项趋势图","提升值","PDU");
		}
	});
	
};

function selectOnchangeDu(duName){
	
	du = duName;
	var indexId =  $("#selectIndex").val();
	var year =  $("#selectYear").val();
	
	$.ajax({
		url: getRootPath() + '/yunlongPromotion/getSeasonData',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			bu : bu,
			pdu : pdu,
			client : 1,
			du : du,
			indexId : indexId,
			year : year
		},
		success: function(data){
			loadCharts(data.data.sourceData,"sourceChart","数据趋势图","指标值","DU");
			loadCharts(data.data.promotionData,"promotionChart","提升项趋势图","提升值","DU");
		}
	});
	
};


var noIdMap = {};
//图标参数：x-values y-values lengNames
function loadCharts(sourceData, chartId, title, yName,dept){
	var myCharts1=echarts.init(document.getElementById(chartId));
	var lengNames = [];
	var zxt = [];
	var data = [];
	var name = '';
	var xValues = [];
	var xData = sourceData.xAxis;
	var yData = sourceData.series;
	
	if(xData){
		for(var i = 0; i < xData.length; i++){
			xValues.push(xData[i]);
		}
	}
	if(yData){
		for(var i = 0; i < yData.length; i++){
			
			lengNames.push(yData[i].name);
			zxt.push({
			    name:yData[i].name,
	            type:'bar',
	            data:yData[i].data,
		    });
			
			if(yData[i].noId){
				noIdMap[yData[i].name] = yData[i].noId;
			}
		}
		
	}
	
	
	var option = {
			marginRight: 120,
		    title: {
		        text: title,
		        left:'center',
		        textStyle:{
	        		 color:'blue',
		        	 fontStyle:'normal',
		        	 fontWeight:'bold',
		        	 fontFamily:'sans-serif',
		        	 fontSize:18
		        }
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		    	 type:'scroll',
		    	 orient : 'vertical',
		    	 right:'right',
		    	 data:lengNames,
		    	 y:30
		    },
		    grid: {
		        left: '0%',
		        right: '25%',
		        bottom: '0%',
		        containLabel: true,
		    },
		    toolbox: {
		    	show : true,
    	        feature : {
    	            mark : {show: true},
    	            dataView : {show: true, readOnly: false},
    	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
    	            restore : {show: true},
    	            saveAsImage : {show: true}
    	        }
		    },
		  //x轴设置
			xAxis:{
				name:'日期(季度)',
				splitLine:{show: false},//去除网格线	
				data:xValues,
				nameTextStyle:{//设置x轴标题属性
					color:'#000000',
					fontSize:14
				},
				axisTick:{//x轴刻度
					show:false
				},
				axisLabel:{//x轴标示属性
					interval: 0,
					textStyle:{
						color:'#000000',
						fontSize:14
					}
				},
				axisLine:{//设置轴线属性
					lineStyle:{
						color:'#2e8afc',
						width:2					
					}
				}
			},
			//y轴设置
			yAxis:{
				name:yName,
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
		    series: zxt
		};
	myCharts1.clear();
	myCharts1.setOption(option,true);
	myCharts1.on('click', function(param) {
		myCharts1.off('click');	
		if(dept == "DU"){
			window.open(getRootPath() + '/view/HTML/page.html?url='+getRootPath()+'/bu/projView?projNo=' + noIdMap[param.seriesName]);
		}else if (dept == "PDU"){
			selectOnchangeDu(param.seriesName);
		}else if (dept == "BU"){
			selectOnchangePdu(param.seriesName);
		}
		
	 });
	
}

