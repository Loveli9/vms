/**折线图*/
function lineCharts(divId,axisName,types,xAxis,values,color,markPointData){
	var seriesval = [];
	for (var i = 0; i < types.length; i++) {
		seriesval.push(
			{	
				name:types[i],
				type:'line',
				symbolSize: 5,//拐点大小
				showAllSymbol:true,
				data:values[i],
                markPoint : {
                    data : markPointData
                },
			}
		)
	}
	lineCharts0(divId,axisName,types,xAxis,seriesval,color)
}

function lineCharts0(divId,axisName,types,xAxis,seriesval,color){
	var myCharts=echarts.init(document.getElementById(divId));
	//折线图
	var option={
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
				name:axisName[0],
				splitLine:{show: false},//去除网格线			
				data:xAxis,
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
				name:axisName[1],
				splitLine:{show: true},//去除网格线
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
	if(color){
		option.color=color;
	}
	myCharts.setOption(option);
}

/**饼状图*/
function pieCharts(divId,name,rate,num,color){
	var myCharts=echarts.init(document.getElementById(divId));
	//饼图
	var option={
		//定义标题
		title:{
		},	
		//鼠标悬停
		tooltip: {
        	trigger: 'item',
			formatter: '{a} <br/>{b} : {c} ({d}%)'
    	},
		//图标
		legend:{
			data:[rate[0],rate[1]]
		},
		//指定图表类型
		series:[
			{
				name:name,
				type: 'pie',
            	radius : '65%',
            	center: ['50%', '50%'],
            	selectedMode: 'single',
				data:[
					{
						name: rate[0],
						value:num[0]
					},{
						name: rate[1],
						value:num[1]
					}
				]
			}
		]
	}
	if(color){
		option.color=color;
	}
	myCharts.setOption(option);
}

/**树状图*/
function barCharts(divId,axisName,types,xAxis,values,color){
	var seriesval = [];
	for (var i = 0; i < types.length; i++) {
		if(color){
			seriesval.push(
				{	
					name:types[i],
					type:'bar',
					data:values[i],
					itemStyle:{
						normal:{
							color:color[i]
						}
					}
				}
			)
		}else{
			seriesval.push(
					{	
						name:types[i],
						type:'bar',
						data:values[i],
					}
				)
		}
	}
	barCharts0(divId,axisName,types,xAxis,seriesval);
}

function barCharts0(divId,axisName,types,xAxis,seriesval){
	var myCharts=echarts.init(document.getElementById(divId));
	//柱状图
	var option = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data:types
		    },
		    xAxis : [
		        {
		        	name : axisName[0],
		            type : 'category',
		            data : xAxis,
//		            axisLabel : {//坐标轴刻度标签的相关设置。
//		                interval:0,
//		                rotate:"45"
//		            }
		        },
		    ],
		    yAxis : [
		        {
		        	name : axisName[1],
		            type : 'value'
		        }
		    ],
		    series : seriesval
		};
	myCharts.setOption(option);
}

/**树状图带选项卡*/
function barToolCharts(divId,axisName,types,xAxis,values,color){
	var seriesval = [];
	for (var i = 0; i < types.length; i++) {
		if(color){
			seriesval.push(
				{
					name:types[i],
					type:'bar',
					data:values[i],
					itemStyle:{
						normal:{
							color:color[i]
						}
					}
				}
			)
		}else{
			seriesval.push(
					{
						name:types[i],
						type:'bar',
						data:values[i],
					}
				)
		}
	}
	barToolCharts0(divId,axisName,types,xAxis,seriesval);
}

function barToolCharts0(divId,axisName,types,xAxis,seriesval){
	var myCharts=echarts.init(document.getElementById(divId));
	var option = {
	    title: {
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
//	    	 type:'scroll',
//	    	 orient : 'vertical',
//	    	 right:'right',
	    	 data:types,
//	    	 y:30
	    },
//	    grid: {
//	        left: '0%',
//	        right: '25%',
//	        bottom: '0%',
//	        containLabel: true,
//	    },
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
			name:axisName[0],
			splitLine:{show: false},//去除网格线	
			data:xAxis,
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
			name:axisName[1],
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
	    series: seriesval
	};
	myCharts.setOption(option);
}

/**仪表盘*/
function gaugeCharts(divId,name,dataName,value,max,lineStyleIn,unit){
	var myCharts=echarts.init(document.getElementById(divId));
	if(!lineStyleIn){
		lineStyleIn={
		        color: [
		            [0.2, '#228b22'],
		            [0.8, '#48b'],
		            [1, '#ff4500']
		        ]
		}
	}
	if(!max){
		max=100;
	}
	if(!unit){
		unit='%';
	}
	//仪表盘
	var option = {
		tooltip : {
		    formatter: "{a} <br/>{b} : {c}"+unit
		},
//		toolbox: {
//		    show : true,
//		    feature : {
//		        mark : {show: true},
//		        restore : {show: true},
//		        saveAsImage : {show: true}
//		    }
//		},
		series : [
		    {
		        name:name,
		        max:max,
		        type:'gauge',
		        axisLine: {            // 坐标轴线
	                lineStyle: lineStyleIn
	            },
		        detail : {formatter:'{value}'+unit},
		        data:[{value: value, name: dataName}]
		    }
		]
	};
	myCharts.setOption(option);
}