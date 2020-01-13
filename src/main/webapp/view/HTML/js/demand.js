$(function(){
	var projNo = window.parent.projNo;
	//加载表格
	$.ajax({
		url: getRootPath() + '/projectlable/queryMetricIndex',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			name : '需求',
			proNo: projNo
		},
		success: function(data){
			var tab = '';
			for(var i = 0;i<4;i++){
				tab = '';
				tab = '<tr>';
				for(var key in data.data[i]){
					var result = data.data[i];
					tab += '<td>'+result[key]+'</td>';
	             }
				tab+='</tr>';
				$("#tableBody thead").append(tab);
			}
			for(var i = 4;i<data.data.length-1;i++){
				tab = '';
				tab = '<tr>';
				for(var key in data.data[i]){
					var result = data.data[i];
					tab += '<td>'+result[key]+'</td>';
	             }
				tab+='</tr>';
				$("#tableBody tbody").append(tab);
			}
			loadCharts(data);
		}
	});
	
	//加载图表
	function loadCharts(data){
		var myCharts1=echarts.init(document.getElementById("developIndex"));
		var dataLength = data.data.length;
		var echartData = data.data[dataLength-1];
		if(echartData != null && echartData != undefined){
			lengNames = echartData.topFiveQYK;
			xvalues = echartData.daysBetweenToStrArray;
			yvalues = echartData.qymcByMCs;
		}
		var zxt = [];
		var data = [];
		var name = '';
		for(var i = 0;i<yvalues.length;i++){
			data = [];
			var dd = yvalues[i];
			for(var j = 0;j<dd.length;j++){
				name = dd[0].QYMC;
				data.push(dd[j].NM);
			}
			zxt.push({
			    name:name,
	            type:'line',
	            data:data
		    });
		}
		
		var option = {
			    title: {
			        text: '指标参数'
			    },
			    tooltip: {
			        trigger: 'axis'
			    },
			    legend: {
			        data:lengNames
			    },
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
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
			    xAxis: {
			        type: 'category',
			        boundaryGap: false,
			        data: xvalues,
			        axisLine:{
    	                lineStyle:{
    	                    color:'#479de6',
    	                    width:2
    	                }
    	            }
			    },
			    yAxis: {
			        type: 'value',
			        axisLine:{
                        lineStyle:{
                            color:'#479de6',
                            width:2,//这里是为了突出显示加上的
                        }
                    } 
			    },
			    series: zxt
			};
		myCharts1.setOption(option);
	}
});

