$(function(){
	//http://localhost:8080/mvp/view/HTML/developIndexYH.html?projNo=100.0
    var projNo = getQueryString("projNo");

    /*function init() {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            async:false,
            url: getRootPath()+'/projectlable/getProjectLabs',
            data : {
                projNo : projNo
            },
            success : function(data){
            	var num = 0;
                var dataLg = data.data.length;//返回数据长度
                var zcd = data.data[dataLg-1];//子菜单
                var obj = data.data;
                obj.reverse();
                if(obj.length > 0){
                    for (var i = 0; i < obj.length; i++) {
                        if(obj[i].LAB_PATH == undefined || obj[i].TITLE == undefined){
                            continue;
                        }
                        var plid = obj[i].plId;
                        if(plid != '' && plid != undefined && plid != null){
                            var cd = zcd[plid];
                            for(var j = 0; j < cd.length; j++){
                                initColumn(num+"id",cd[j],obj[i].TITLE,cd[j]);
                                num ++;
                        }
                        }

                    }
                }
            }
        })
	}*/
    function initColumn(tableId,type,assortment,name) {
    	var tabs = '<div style="margin: 20px;">' +
            '<div>' +
            '<span>'+name+'</span>' +
            '</div>' +
            '<div class="modal-body change-chart"' +
            'style="width: 100%; padding: 0px;">' +
            '<table id="'+tableId+'" style="width: 1000px" border="0" cellspacing="0" cellpadding="0" class="data-table">' +
            '<thead></thead><tbody></tbody></table></div>' +
            '</div>'
        $("#allTable").append(tabs);

        $.ajax({
            url: getRootPath() + '/iteration/report',
            type: 'post',
            async: false,//是否异步，true为异步
            data: {
                labId : parent.lab_ID,//流程名称
                measureCate: parent.cate_gory,//菜单名称
                proNo : projNo
            },
            success: function (data) {
                var tab = '';
                var dataSo = data.data.tableData;
                var length = data.data.tableData.length;
                if (dataSo) {
                    var title = data.data.tableData[0];
                    var lg = data.data.tableData[0].length;
                    var max=new Array;
                    var min=new Array;
                    for (var i = 0; i < 4; i++) {
                        tab = '<tr>';
                        for (var j = 0; j < dataSo[i].length; j++) {
                            tab += '<td>' + dataSo[i][j] + '</td>';
                        }
                        tab += '</tr>';
                        $("#"+tableId+" thead").append(tab);
                    }
                    for(var k=1;k<lg;k++){
    					max[k] = dataSo[1][k]; 
    					min[k] = dataSo[2][k]; 	
    				}
                    for (var i = 4; i < length; i++) {
                        tab = '<tr>';
                        for (var j = 0; j < dataSo[i].length; j++) {
                        	var deng="";
    						if(j!=0){
    							var val = parseFloat(dataSo[i][j]);
    							var minval = parseFloat(min[j]);
    							var maxval = parseFloat(max[j]);
    							
    							if(val<minval || val>maxval){		
    								deng ='<div style=" display: block;float: right;margin-right: 5px;border-radius: 50%;width: 20px;height:20px;'+
    								'background-color: #f57454; "></div>';	
    							}else if(val == minval || val == maxval){
    								deng = '<div style="display: block;float: right;margin-right: 5px;border-radius: 50%;width: 20px;height:20px;'+
    								'background-color: #f7b547; "></div>';
    							}else{
    								deng = '<div style="display: block;float: right;margin-right: 5px;border-radius: 50%;width: 20px;height:20px;'+
    								'background-color: #1adc21; "></div>';
    							}
    						}
                            if (isRealNum(dataSo[i][j])) {
                                if (title[j].indexOf("/") == -1 && title[j].indexOf("%") == -1 && title[j].indexOf("KLOC") == -1) {
                                	tab += '<td><div style="width: 50%;float: left;height: 20px;">'+ deng+"</div>"+
                                		"<div style='display: block;width: 50%;float: left;line-height: 20px;text-align: left;padding-left: 5px;'>"+dataSo[i][j]+'</div></td>';
                                } else {
                                	tab += '<td><div style="width: 50%;float: left;height: 20px;">'+ deng+"</div>"+
                                		"<div style='display: block;width: 50%;float: left;line-height: 20px;text-align: left;padding-left: 5px;'>"+Number(dataSo[i][j]).toFixed(2)+'</div></td>';
                                }
                            } else {
                                tab += '<td>' + dataSo[i][j] + '</td>';
                            }
                        }
                        tab += '</tr>';
                        $("#"+tableId+" tbody").append(tab);
                    }
                    tab = '<tr><td>合计</td>';
                    for (var i = 1; i <= lg - 1; i++) {
                        var num = 0;
                        for (var j = 4; j < length; j++) {
                            num += Number(dataSo[j][i]);
                        }
                        if (isNaN(num)) {
                            num = '';
                            tab += '<td>' + num + '</td>';
                        } else {
                            if (title[i].indexOf("/") == -1 && title[i].indexOf("%") == -1 && title[i].indexOf("KLOC") == -1) {
                                tab += '<td>' + num + '</td>';
                            } else {
                                tab += '<td>' + (num / (length - 4)).toFixed(2) + '</td>';
                            }
                        }
                    }
                    $("#"+tableId+" tbody").append(tab);
                }
                // var tabl = document.getElementById(tableId).offsetWidth;//获取表格宽度
                // document.getElementById("chartWidth").style.width = tabl + "px";
                //loadCharts(dataSo);
            }
        });
    }
    $(document).ready(function(){
//        init();
	})
});

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
					xValues.push(names.substring(0,names.indexOf('(')));//x轴迭代周期
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
					name:'迭代周期',
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

function isRealNum(val){
    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
    if(val === "" || val ==null){
        return false;
    }
    if(!isNaN(val)){
        return true;
    }else{
        return false;
    }
}        
