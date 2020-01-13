$(function(){
	var dom=document.getElementById("ZBtopchart");
	dom.style.width=(window.outerWidth-75)+'px';
	var myCharts=echarts.init(dom);

	var projectState = "在行";
	
	$.fn.loadzbTop=function(){
		$.ajax({
			url: getRootPath() + '/measureTop/getTopMeasure',
			type: 'post',
			async: false,//是否异步，true为异步
			data: {
				'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
	        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
				'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
				'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
				'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
    			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
    			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
                'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
    			'projectState':projectState
				
			},
			success: function (data) {
				if(data.status =="0"){
					if (data.msg=="无在行项目"){
						document.getElementById("ZBtopchart").style.display="none";
						document.getElementById("message").style.display=""; 
					}else{
						document.getElementById("ZBtopchart").style.display="";
						document.getElementById("message").style.display="none"; 
						document.getElementById("errorMsg").style.display="none"; 
						var values = [];
						var types = data.types;
						var months = data.months;
						var mapList = data.dataList;
						var size = types.length;
						for (var num =0; num<size; num++) {
							for(var n =0; n<size; n++){
								if (mapList[n].name==types[num]){
									values.push(mapList[n].value);
									break;
									}		
								}
							}
						
						chartsZBtop(types,months,values);
					
					}
					
				}
				else{
					document.getElementById("message").style.display="none"; 
					document.getElementById("errorMsg").style.display=""; 
				}
				
			}
		});
		
	}
	
	function chartsZBtop(types,months,values){
		var seriesval = [];
		for (var i = 0; i < types.length; i++) {
			seriesval.push(
				{	
					name:types[i],
					type:'line',
					smooth: true,
					data: values[i]
				}
			)
		}
		
		option = {
			    tooltip: {
			        trigger: 'none',
			        axisPointer: {
			            type: 'cross'
			        }
			    },
			    legend: {
			        data:types
			    },
			    grid: {
			        top: 70,
			        bottom: 50
			    },
			    xAxis: [
			        {
			            type: 'category',
			            name: "月份",
			            nameTextStyle:{
							color:'#000000',
							fontSize:16
						},
			            axisTick: {
			                alignWithLabel: true
			            },
			            axisLine: {
			                onZero: false,
			            },
			            axisPointer: {
			                label: {
			                    formatter: function (params) {
			                        return  params.value + (params.seriesData.length ? 
			                        		'：'+ params.seriesData[4].seriesName+ ':'+params.seriesData[4].data + 
			                        		'; '+ params.seriesData[3].seriesName + ':' + params.seriesData[3].data + 
			                        		'; '+ params.seriesData[2].seriesName + ':' + params.seriesData[2].data +
			                        		'; '+ params.seriesData[1].seriesName + ':' + params.seriesData[1].data +
			                        		'; '+ params.seriesData[0].seriesName + ':' + params.seriesData[0].data 			                        		
			                        		: '');
			                    }
			                }
			            },
			            data: months
			        }
			       
			    ],
			    yAxis: [
			        {
			        	name: "指标选用项目数",
			            type: 'value',
			            nameTextStyle:{
							color:'#000000',
							fontSize:16
						}
			            	
			        }
			    ],
			    series: seriesval
			};
			;
			if (option && typeof option === "object") {
				myCharts.setOption(option, true);
				myCharts
			}
			
	}

		
	$(document).ready(function(){
		$().loadzbTop();
	})
	
});

function selectOnchange(){
	$.ajax({
		url: getRootPath() + '/bu/queryMess',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
        	'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
			'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
			'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
			'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join()//转换为字符串
		},
		success: function () {
			
		}
	});
	 $().loadzbTop();
}


