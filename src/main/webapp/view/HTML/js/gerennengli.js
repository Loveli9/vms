(function(){
	var projNo = window.parent.projNo;
	var buName = window.parent.projBU;

	function search(op1,op2){
		var roles = ""
		if($("#userRole").val()!=null){
			roles = $("#userRole").val().join(',');
		}
        $.ajax({
            url: getRootPath() + '/workload/summary',
            type: 'get',
            async: false,
            data: {
                projectId: projNo,
                type: op1,
                codeType: op2,
                role: encodeURI(roles)
            },
            success: function (data) {
                option1.xAxis = {
                    data:data.months
                };
                option1.series=[{
                    data:data.abilities
                },{
                    data:data.commits
                }];
                //console.log(data);
                myChart1.setOption(option1);
                $("[data-toggle='tooltip']").tooltip();
            }
        });
	};
	var myChart1;
	var myCharts;
	var option1;
	function myChart1new(){
		 // 基于准备好的dom，初始化echarts实例
        myChart1 = echarts.init(document.getElementById('Div1'));
        // 指定图表的配置项和数据
        option1 = {
        	title:{
                trigger: 'axis',
            },
            tooltip: {},
            xAxis: {
                type: 'category',
                splitLine: {show: false},//去除网格线
                //name: "2018 年",
                nameLocation: "middle",
                nameTextStyle: {
                    color: '#333',
                    padding: 15
                },
                axisLabel: {
                    textStyle: {
                        color: '#333',
                    }
                },
                axisLine: {
                    color: '#333',
                    lineStyle: {
                        color: '#2e8afc',
                        width: 2,
                    }
                },
                data: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            },
            yAxis: [{
            	type:'value',
            	splitLine:{show: false},//去除网格线
            	name:"代码产出 (LOC)",
                nameTextStyle:{
                    padding: 10
                }
            }, {
                type: 'value',
                name: '提交次数 (次)',
                splitLine:{show: false},
                position: 'right',
                nameTextStyle:{
                    padding: 10
                }
            }],
            series: [{
                type: 'bar',
                data: [],
                barWidth: 30,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: '#2e8afc'
                        }]),
                    }
                }
            },{
                type: 'line',
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                    }
                },
                yAxisIndex: 1,
                lineStyle: {
                    normal: {
                        color: "#e41f2b",
                        width: 3,
                        shadowColor: 'rgba(0,0,0,0.4)',
                        shadowBlur: 10,
                        shadowOffsetY: 10
                    }
                },
                //data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart1.setOption(option1);
      	//初始化图标标签
	}
  	//折线图
	function zhexiantu(riqi,shuju){
		myCharts=echarts.init(document.getElementById("Div2"));
		var options2={
			//定义标题
			title:{
				
			},	
			//鼠标悬停
			tooltip:{
				
			},
			//x轴设置
			xAxis:{
				name:'时间',
				splitLine:{show: false},//去除网格线			
				data:riqi,
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
						color:'#2e8afc',
						width:2					
					}
	            }
			},
			//y轴设置
			yAxis:{
				//name:'数量',
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
			series:[{
				type:'line',
				symbolSize: 5,//拐点大小
				showAllSymbol:true,
				itemStyle:{
					normal:{
						lineStyle:{
							color:'#2e8afc'	
						},	
						areaStyle:{
							
						},
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1,[
							{offset: 0, color: '#2e8afc'},
							{offset: 1, color: 'white'}
	               		])
					}	
				},
				data:shuju
			}]
		}
		//调用统计图
		myCharts.setOption(options2);
	}
	
	//工程能力折线图
	function processCapability(val){
		$.ajax({
			url: getRootPath() + '/paraInfo/processCapability',
			type: 'post',
			async: false,//是否异步，true为异步
			data: {
				projNo: projNo,
				paraName: val
			},
			success: function (data) {
				console.log(data)
				var riqi=new Array();
				var shuju=new Array();
				for(var i=0;i<data.length;i++){
					riqi[i]=new Date(data[i].MONTH).format('yyyy-MM');
					shuju[i]=data[i].VALUE;
				}
				zhexiantu(riqi,shuju);
			}
		});
		
	}
	//工程能力表格
	function processCapabilityTable(){
		$.ajax({
			url: getRootPath() + '/paraInfo/getProcessCapability',
			type: 'post',
			async: false,//是否异步，true为异步
			data: {
				projNo: projNo,
				parameters: "103,102,101,100,118,120,119,108"
			},
			success: function (data) {
				
			}
		});
		
	}
	
	function processCapabilitysel(){
		$("#process-capability-sel").change(function(){
			processCapability($("#process-capability-sel").val())
		});	
	}
	//根据条件统计员工个人代码量、并根据代码类别筛选
    function personalCodeCount() {
        var param1 = "0";//0:commit 1:message
        var param2 = "all";//所有代码量
        //$.ajax({
        //	url : getRootPath()+'/codeType/codeTypeConfig',
        //	type: 'post',
        //	data : {
        //		no : projNo
        //	},
        //	success:function(data){
        //		_.each(data.data, function(record, index){
        //			if(record){
        //				if(record.parameterId==160){
        //					if(record.type == '1'){
        //						option1 = '1';
        //					}
        //				}
        //			}
        //		});
        //		search(option1,option2);
        //		$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
        //	}
        //});

        $("#userRole").change(function () {
            //option1 = $('#personal-code-count').val();
            //search("0","all");
            //debugger;
            param2 = $('#personal-code-type').val();
            search(param1, param2);
            $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
        });

        /*$("#personal-code-count").change(function() {
         option1 = $('#personal-code-count').val();
         search(option1,option2);
         });*/

        $("#personal-code-type").change(function () {
            param2 = $('#personal-code-type').val();
            search(option1, param2);
            $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
        });

        //var str = '开发工程师,PM,产品经理,SE,MDE,BA,IA,运维工程师';
        //var arr = str.split(',');
        //$("#userRole").selectpicker('val', arr);

        search(param1, param2);
        //$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
    };
	
	function getStartDate(){
		  
		$.ajax({
			url: getRootPath() + '/svnTask/searchStartDate',
			type: 'post',
			data:{
				no : projNo
			},
			success: function (data) {
				$("#datepicker").val(data);
				
			}
		});
	};

	$(document).ready(function(){
		$("#kaifaxiaolv").load("gerennengli_kaifaxiaolv.html",function(){
			myChart1new();
			personalCodeCount();
//			myCharts.clear();
		});
/*		$("#361chengshudu").load("gerennengli_361chengshudu.html",function(){
			everyScore();
			comments();
		});
*/		
		//search(0,all);
		
	
//		processCapability($("#process-capability-sel").val());
//		processCapabilitysel();	
//		getStartDate();

	})
})()


//根据窗口调整表格高度
$(window).resize(function() {
	$("#tab-kfxl").css({"min-height":$(window).height() - 35});
});

$(document).ready(function(){
	$("#tab-kfxl").css({"min-height":$(window).height() - 35});
});



