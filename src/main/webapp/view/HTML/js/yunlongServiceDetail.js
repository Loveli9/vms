$(function(){
	
	function getValue(url){
		//首先获取地址
		var url = url || window.location.href;
		//获取传值
		var arr = url.split("?");
		//判断是否有传值
		if(arr.length == 1){
			return null;
		}
		//获取get传值的个数
		var value_arr = arr[1].split("&");
		//循环生成返回的对象
		var obj = {};
		for(var i = 0; i < value_arr.length; i++){
			var key_val = value_arr[i].split("=");
			obj[key_val[0]]=key_val[1];
		}
		return obj;
	}
	
	var serviceId = getValue().serviceId;
	
		$.ajax({
		url: getRootPath() + '/yunlongProject/getServiceDetail',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			serviceId : serviceId
		},
		success: function(data){
			dataAll = data;
			var tab = '';
			var dataSo = data.scope.tableRows;
			loadTables(dataSo);
			var tabl = document.getElementById("tableBody").offsetWidth;//获取表格宽度
			document.getElementById("chartWidth").style.width = tabl + "px";
			loadCharts(data.scope.chartRows);
			
			if(data.scopeColor == "red"){
				document.getElementById("scopeA").style.color = "#F00";
			}
			if(data.progressColor == "red"){
				document.getElementById("progressA").style.color = "#F00";
			}
			if(data.processQualityColor == "red"){
				document.getElementById("processQualityA").style.color = "#F00";
			}
			if(data.efficiencyColor == "red"){
				document.getElementById("efficiencyA").style.color = "#F00";
			}
			if(data.processQuality.Build构建Color == "red"){
				document.getElementById("buildA").style.color = "#F00";
			}
			if(data.processQuality.AlphaColor == "red"){
				document.getElementById("alphaA").style.color = "#F00";
			}
			if(data.processQuality.BetaColor == "red"){
				document.getElementById("betaA").style.color = "#F00";
			}
			if(data.processQuality.GammaColor == "red"){
				document.getElementById("gammaA").style.color = "#F00";
			}
			if(data.processQuality.发布Color == "red"){
				document.getElementById("pushA").style.color = "#F00";
			}
			if(data.processQuality.迭代周期内Color == "red"){
				document.getElementById("tTimeA").style.color = "#F00";
			}
			
		}
	});	
		
		

});
var dataAll;

function loadData(title) {
	
	var dataTable = null;
	var dataChart = null;
	document.getElementById("qpDiv").style.display = "none";
	
	if(title == "scope" && dataAll.scope){
		dataTable = dataAll.scope.tableRows;
		dataChart = dataAll.scope.chartRows;
	} else if (title == "efficiency" && dataAll.efficiency){
		dataTable = dataAll.efficiency.tableRows;
		dataChart = dataAll.efficiency.chartRows;
	} else if (title == "progress" && dataAll.progress){
		dataTable = dataAll.progress.tableRows;
		dataChart = dataAll.progress.chartRows;
	} 
	$("#tableBody thead").html("");
	$("#tableBody tbody").html("");
	var myCharts1=echarts.init(document.getElementById("developIndex"));
	myCharts1.clear();
	if(dataTable && dataChart){
		loadTables(dataTable);
		var tabl = document.getElementById("tableBody").offsetWidth;//获取表格宽度
		document.getElementById("chartWidth").style.width = tabl + "px";
		loadCharts(dataChart);
	}
	
	
}
function loadPQ(title) {
	document.getElementById("qpDiv").style.display = "";
	$("#tableBody thead").html("");
	$("#tableBody tbody").html("");
	var dataTable = null;
	var dataChart = null;
	if(dataAll.processQuality.Build构建){
		dataTable = dataAll.processQuality.Build构建.tableRows;
		dataChart = dataAll.processQuality.Build构建.chartRows;
	}
	
	if(dataTable && dataChart){
		loadTables(dataTable);
		var tabl = document.getElementById("tableBody").offsetWidth;//获取表格宽度
		document.getElementById("chartWidth").style.width = tabl + "px";
		loadCharts(dataChart);
	}
}

function loadPQData(title) {
	
	var dataTable = null;
	var dataChart = null;
	
	if(title == "build" && dataAll.processQuality.Build构建 != null){
		dataTable = dataAll.processQuality.Build构建.tableRows;
		dataChart = dataAll.processQuality.Build构建.chartRows;
	} else if (title == "alpha" && dataAll.processQuality.Alpha != null){
		dataTable = dataAll.processQuality.Alpha.tableRows;
		dataChart = dataAll.processQuality.Alpha.chartRows;
	} else if (title == "beta" && dataAll.processQuality.Beta != null){
		dataTable = dataAll.processQuality.Beta.tableRows;
		dataChart = dataAll.processQuality.Beta.chartRows;
	} else if (title == "gamma" && dataAll.processQuality.Gamma != null){
		dataTable = dataAll.processQuality.Gamma.tableRows;
		dataChart = dataAll.processQuality.Gamma.chartRows;
	} else if (title == "push" && dataAll.processQuality.发布 != null){
		dataTable = dataAll.processQuality.发布.tableRows;
		dataChart = dataAll.processQuality.发布.chartRows;
	} else if (title == "tTime" && dataAll.processQuality.迭代周期内 != null){
		dataTable = dataAll.processQuality.迭代周期内.tableRows;
		dataChart = dataAll.processQuality.迭代周期内.chartRows;
	} 
	$("#tableBody thead").html("");
	$("#tableBody tbody").html("");
	var myCharts1=echarts.init(document.getElementById("developIndex"));
	myCharts1.clear();
	if(dataTable && dataChart){
		loadTables(dataTable);
		var tabl = document.getElementById("tableBody").offsetWidth;//获取表格宽度
		document.getElementById("chartWidth").style.width = tabl + "px";
		loadCharts(dataChart);
	}
	
}


function loadTables(dataSo){
	var tab = '';
	if(dataSo){
		var title = dataSo[0];
		var lg = dataSo[0].length;
		for(var i = 0; i < 4; i++){
			tab = '<tr>';
			for(var j=0;j<dataSo[i].length;j++){
				for(key in dataSo[i][j]){
					tab += '<td>'+key+'</td>';
				}
			}
			tab+='</tr>';
			$("#tableBody thead").append(tab);
		}
		
		for(var i = 4; i<dataSo.length; i++){
			tab = '<tr>';
			for(var j=0;j<dataSo[i].length;j++){
				if(dataSo[i][j]){
					for(key in dataSo[i][j]){
						tab += '<td><font color=' + dataSo[i][j][key] + '>'+key+'</font></td>';
					}
				} else {
					tab += '<td>NA</td>';
				}
				
				
			}
			tab+='</tr>';
			$("#tableBody tbody").append(tab);
		}
	}
	
}

	
	//图标参数：x-values y-values lengNames
	function loadCharts(dataSo){
		var myCharts1=echarts.init(document.getElementById("developIndex"));
		var lengNames = [];
		var zxt = [];
		var data = [];
		var name = '';
		var xValues = [];
		if(dataSo){
			for(var i = 0;i<dataSo.length;i++){
				for(var j = 0;j<dataSo[i].length-1;j++){
					if(i == 0){//指标名称
						lengNames.push(dataSo[i][j+1]);
					}
				}
				if(i > 3){
					var names = dataSo[i][0];
					xValues.push(names);//x轴迭代周期
				}
			}
		}
		for(var k = 0;k < lengNames.length; k++){
			data = [];
			name = lengNames[k];
			for(var i = 4; i < dataSo.length; i++){
				data.push(dataSo[i][k+1]);	
			}
			zxt.push({
			    name:name,
	            type:'bar',
	            data:data
		    });
		}
		var option = {
				marginRight: 120,
			    title: {
			        text: parent.cate_gory+"指标参数",
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
					name:'迭代日期',
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
					name:'指标值',
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
		myCharts1.setOption(option);
		window.onresize = function () {
			getWidth(myCharts1);
	    }
	}
	
function getWidth(myCharts1){
	var tabl = document.getElementById("tableBody").offsetWidth;//获取表格宽度
	document.getElementById("chartWidth").style.width = tabl + "px";
	myCharts1.resize();
}
//
//function isRealNum(val){
//    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
//    if(val === "" || val ==null){
//        return false;
//    }
//    if(!isNaN(val)){
//        return true;
//    }else{
//        return false;
//    }
//}   
