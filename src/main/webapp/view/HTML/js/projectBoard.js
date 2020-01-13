// var navigation = 0;
var problemType = 0;
var projectInfo = {
	'hwpdu': 32 ,
	'clientType':0
};
var monthList = new Array(6);
var YellowNumber = 0;
var RedNumber = 0;
var GreenNumber = 0;
var listRed = new Array(6);
var listgreen = new Array(6);
var listYellow = new Array(6);
$(function() {
    $(document).ready(function(){
        var Menus = getCookie('Menulist');
        var flag = false;
        if (Menus.indexOf("-28-") != -1) {
            var tab1 ='<li tabname="tab-kb" id="zonglan" onclick="loadZongLan()" style="border: 0px;"><a href="###">总览</a></li>';
            $("#projectBoard").append(tab1);
            if(!flag){
                $("#zonglan").click();
                flag = true;
            }
        }
        if (Menus.indexOf("-29-") != -1) {
            var tab2 ='<li tabname="tab-zhiliang" id="zhiliang" onclick="loadZhiLiang()" style="border: 0px;"><a href="###">质量</a></li>';
            $("#projectBoard").append(tab2);
            if(!flag){
                $("#zhiliang").click();
                flag = true;
            }
        }
        if (Menus.indexOf("-30-") != -1) {
            var tab3 ='<li tabname="tab-ziyuan" id="ziyuan" onclick="loadZiYuan()" style="border: 0px;"><a href="###">资源</a></li>';
            $("#projectBoard").append(tab3);
            if(!flag){
                $("#ziyuan").click();
                flag = true;
            }
        }
        if (Menus.indexOf("-31-") != -1) {
            var tab4 ='<li tabname="tab-jindu" id="jindu" onclick="loadJinDu()" style="border: 0px;"><a href="###">进度</a></li>';
            $("#projectBoard").append(tab4);
            if(!flag){
                $("#jindu").click();
                flag = true;
            }
        }
        if (Menus.indexOf("-32-") != -1) {
            var tab5 ='<li tabname="tab-biangeng" id="biangeng" onclick="loadBianGeng()" style="border: 0px;"><a href="###">变更</a></li>';
            $("#projectBoard").append(tab5);
            if(!flag){
                $("#biangeng").click();
                flag = true;
            }
        }
        if (Menus.indexOf("-33-") != -1) {
            var tab6 ='<li tabname="tab-xiaolv" id="xiaolv" onclick="loadXiaoLv()" style="border: 0px;"><a href="###">效率</a></li>';
            $("#projectBoard").append(tab6);
            if(!flag){
                $("#xiaolv").click();
                flag = true;
            }
        }
        if (Menus.indexOf("-34-") != -1) {
            var tab7 ='<li tabname="tab-problem" id="wenti" onclick="loadWenTi()" style="border: 0px;"><a href="###">问题</a></li>';
            $("#projectBoard").append(tab7);
            if(!flag){
                $("#wenti").click();
                flag = true;
            }
        }
        if (Menus.indexOf("-35-") != -1) {
            var tab8 ='<li tabname="qmsSelfchecking" id="qms" onclick="loadQms()" style="border: 0px;"><a href="###">QMS</a></li>';
            $("#projectBoard").append(tab8);
            if(!flag){
                $("#qms").click();
                flag = true;
            }
        }
    });
	/*$(".state .navigation").on('click',function(){
		var num = $(this).index();
		$(this).addClass("curent").siblings().removeClass("curent");
		if(num==0){
			$("#problem").hide();
			$("#tab-kb").show();
			$("#qmsSelfchecking").hide();
			echars();
			echars1();
			departmentList();
			navigation=0;
		}else if(num==6){
			$("#tab-kb").hide();
			$("#problem").show();
			$("#qmsSelfchecking").hide();
			projectStatesProblem();
			$('#zonglanAnalysis').bootstrapTable('refresh');
			navigation=6;
		}
		else if(num==7){
			$("#tab-kb").hide();
			$("#problem").hide();
			$("#qmsSelfchecking").show();
			$('#qmsTable').bootstrapTable('refresh');
			navigation=7;
		}
	});
	$("#question li").click(function() {
	if($(this).text()=="总览"){
		problemType = 0;
		$("#zonglanAnalysis").show();
		$("#StatesProblem").hide();
		$('#zonglanAnalysis').bootstrapTable('refresh');
	}else{
		if($(this).text()=="361"){
			problemType = 1;
		}else if($(this).text()=="AAR"){
			problemType = 2;
		}else if($(this).text()=="质量回溯"){
			problemType = 4;
		}else if($(this).text()=="开工会审计"){
			problemType = 3;
		}
		$("#zonglanAnalysis").hide();
		$("#StatesProblem").show();
		$('#StatesProblem').bootstrapTable('refresh');
	}
	projectStatesProblem();
		$(this).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
		$(this).addClass('selected');
	});*/
	initCurrentDate();
	echars();
	echars1();
	departmentList();
	qualityProblem();
	statesProblem();
	qmsSelfchecking();
});

var loadZongLan = function() {
    $("#problem").hide();
    $("#tab-kb").show();
    $("#qmsSelfchecking").hide();
    echars();
    echars1();
    departmentList();
    qualityProblem();
    statesProblem();
    qmsSelfchecking();
};

var loadZhiLiang = function() {
    echars();
    echars1();
    departmentList();
    qualityProblem();
    statesProblem();
    qmsSelfchecking();
};

var loadZiYuan = function() {
    echars();
    echars1();
    departmentList();
    qualityProblem();
    statesProblem();
    qmsSelfchecking();
};

var loadJinDu = function() {
    echars();
    echars1();
    departmentList();
    qualityProblem();
    statesProblem();
    qmsSelfchecking();
};

var loadBianGeng = function() {
    echars();
    echars1();
    departmentList();
    qualityProblem();
    statesProblem();
    qmsSelfchecking();
};

var loadXiaoLv = function() {
    echars();
    echars1();
    departmentList();
    qualityProblem();
    statesProblem();
    qmsSelfchecking();
};

var loadWenTi = function() {
    $("#tab-kb").hide();
    $("#problem").show();
    $("#qmsSelfchecking").hide();
    projectStatesProblem();
    $('#zonglanAnalysis').bootstrapTable('refresh');
    $("#question li").click(function() {
        if($(this).text()=="总览"){
            problemType = 0;
            $("#zonglanAnalysis").show();
            $("#StatesProblem").hide();
            $('#zonglanAnalysis').bootstrapTable('refresh');
        }else{
            if($(this).text()=="361"){
                problemType = 1;
            }else if($(this).text()=="AAR"){
                problemType = 2;
            }else if($(this).text()=="质量回溯"){
                problemType = 4;
            }else if($(this).text()=="开工会审计"){
                problemType = 3;
            }
            $("#zonglanAnalysis").hide();
            $("#StatesProblem").show();
            $('#StatesProblem').bootstrapTable('refresh');
        }
        projectStatesProblem();
        $(this).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
        $(this).addClass('selected');
    });
};

var loadQms = function() {
    $("#tab-kb").hide();
    $("#problem").hide();
    $("#qmsSelfchecking").show();
    $('#qmsTable').bootstrapTable('refresh');
    qualityProblem();
    statesProblem();
    qmsSelfchecking();
};

var qmsSelfchecking = function(){
	$('#qmsTable').bootstrapTable({
		method : 'post',
		contentType : "application/x-www-form-urlencoded",
		url : getRootPath() + '/qmsMaturity/qmsSelfchecking',
		editable : false,// 可行内编辑
		striped : true, // 是否显示行间隔色
		dataField : "rows",
		pageNumber : 1, // 初始化加载第一页，默认第一页
		pagination : false,// 是否分页
		queryParamsType : 'limit',
		sidePagination : 'client',
		showColumns : false,
		toolbar : '#toolbar',// 指定工作栏
		toolbarAlign : 'right',
		dataType : "json",
		queryParams : function(params){
			var param = {
				month:$("#currentDate").val(),
			};
			return param;
		},
		columns : [
			[	{title : '序号',align: "center",valign:'middle',width:'40',rowspan: 2,colspan: 1,
    			formatter: function (value, row, index) {
    				console.log(row);
    				var pageSize=$('#qmsTable').bootstrapTable('getOptions').pageSize;  
    				var pageNumber=$('#qmsTable').bootstrapTable('getOptions').pageNumber;
    				return pageSize * (pageNumber - 1) + index + 1;
    			}
    		},
    		{title:'部门',field:'pduSpdt',align: 'center',valign:'middle',width:'300',rowspan: 2, colspan: 1},
    		{title:'汇总',field:'peopleLamp',align: 'center',valign:'middle',width:'13%',rowspan: 2, colspan: 1},
    		{title:'QMS符合度(满分:6分)',align: 'center',valign:'middle',width:'65%',rowspan: 1, colspan: 5}
    	],
    	[
    		{title : '项目管理',field : 'planLamp',align: 'center',valign:'middle',width : '13%',},
    		{title : '质量策划',field : 'scopeLamp',align: 'center',valign:'middle',width : '13%',},
    		{title : '质量控制',field : 'qualityLamp',align: 'center',valign:'middle',width : '13%',},
    		{title : '流程要求',field : 'keyRoles',align: 'center',valign:'middle',width : '13%',},
    		{title : '知识管理',field : 'keyRolesPass',align: 'center',valign:'middle',width : '13%',},        	 
    	 ]
		],
		locale : 'zh-CN',// 中文支持,
	});
}

var echars = function() {
	$.ajax({
			url : getRootPath() + '/projectReview/queryProjectReviewTop6',
			type : "post",
			dataType : "json",
			cache : false,
			data:{
				hwpdu:projectInfo.hwpdu,
				hwzpdu:projectInfo.hwzpdu,
				pduSpdt:projectInfo.pduSpdt,
				month:$("#currentDate").val()
			},
			success : function(data){
				parent.parents=null;
				var monthList1=data.monthList;
				listRed1 = data.red;
				listgreen1 = data.green;
				listyellow1 = data.yellow;
				for (var i = 0; i < 6; i++) {
					listRed[5 - i] = listRed1[i];
					listgreen[5 - i] = listgreen1[i];
					listYellow[5 - i] = listyellow1[i];
					monthList[5 - i]=monthList1[i];
				}
				YellowNumber = parseInt(listYellow[5]);
				RedNumber = parseInt(listRed[5]);
				GreenNumber = parseInt(listgreen[5]);
				var myChart = echarts.init(document
						.getElementById("projectstate"));
				var option = {
					tooltip : {
						trigger : 'axis',
					},
					color : [ "#FF0000", "#FFC000", "#008000" ],
					legend : {
						data : [ '红灯', '绿灯', '黄灯' ]
					},
					grid : {
						left : '3%', // 图表距边框的距离
						right : '4%',
						bottom : '3%',
						containLabel : true
					},
					// x轴信息样式
					xAxis : {
						type : 'category',
						boundaryGap : true,
						data : monthList,
					},
					yAxis : [ {
						type : 'value',
					} ],
					series : [ {
						type : 'line',
						symbolSize : 4, // 拐点圆的大小
						name : '红灯',
						data : listRed,
						smooth : false, // 关键点，为true是不支持虚线的，实线就用true
						itemStyle : {
							normal : {
								lineStyle : {
									color : "#FF0000",
									width : 2,
									type : 'solid' // 'dotted'虚线
													// 'solid'实线
								}
							}
						}
					}, {
						type : 'line',
						name : '黄灯',
						data : listYellow,
						symbol : 'circle',
						symbolSize : 4,
						itemStyle : {
							normal : {
								lineStyle : {
									color : "#FFC000",
								}
							}
						},
					}, {
						type : 'line',
						name : '绿灯',
						data : listgreen,
						symbolSize : 4,
						itemStyle : {
							normal : {

								lineStyle : {
									color : "#008000",
									width : 2,
									type : 'solid' // 'dotted'虚线
													// 'solid'实线
								}
							}
						},
					} ]
				};
				myChart.setOption(option);
				document.getElementById('yellow').innerHTML = YellowNumber;
				document.getElementById('red').innerHTML = RedNumber;
				document.getElementById('green').innerHTML = GreenNumber;
			}
		});
	};
var echars1 = function(){
		$.ajax({
			url : getRootPath() + '/projectReview/twoWeekState',
			type : "post",
			dataType : "json",
			cache : false,
			data:{
				hwpdu:projectInfo.hwpdu,
				hwzpdu:projectInfo.hwzpdu,
				pduSpdt:projectInfo.pduSpdt,
				month:$("#currentDate").val()
			},		
			success : function(data) {
				parent.parents=null;
				var monthList1=data.monthList;
				listRed1 = data.red;
				listyellow1 = data.yellow;
				for (var i = 0; i < 6; i++) {
					listRed[5 - i] = listRed1[i]; 
					listYellow[5 - i] = listyellow1[i];
					monthList[5 - i]=monthList1[i];
				}
				YellowNumber = parseInt(listYellow[5]);
				RedNumber = parseInt(listRed[5]);
				var option = {
					tooltip : {
						trigger : 'axis',
					},
					color : [ "#FF0000", "#FFC000", "#008000" ],
					legend : {
						data : [ '连续两周风险项目', '连续两周预警项目' ]
					},
					grid : {
						left : '3%', // 图表距边框的距离
						right : '4%',
						bottom : '3%',
						containLabel : true
					},
					// x轴信息样式
					xAxis : {
						type : 'category',
						boundaryGap : true,
						data : monthList,
						// 坐标轴颜色
						axisLine : {
							lineStyle : {
								color : 'black'
							}
						},
					},
					yAxis : [ {
						type : 'value',
					} ],
					visualMap : {
						top : 300,
						right : 300,
					},
					series : [ {
						type : 'line',
						symbolSize : 4, // 拐点圆的大小
						name : '连续两周风险项目',
						data : listRed,
						smooth : false, // 关键点，为true是不支持虚线的，实线就用true
						itemStyle : {
							normal : {
								lineStyle : {
									color : "#FF0000",
									width : 2,
									type : 'solid' // 'dotted'虚线 'solid'实线
								}
							}
						}
					}, {
						type : 'line',
						name : '连续两周预警项目',
						data : listYellow,
						symbolSize : 4,
						itemStyle : {
							normal : {
								lineStyle : {
									color : "#FFC000",
								}
							}
						},
					}, ]
				};
				var myChart = echarts.init(document.getElementById("divweek"));
				myChart.setOption(option);
				document.getElementById('yellow1').innerHTML = YellowNumber;
				document.getElementById('red1').innerHTML = RedNumber;
			}
		});
	}
var departmentList = function(){
		$("#mytable").bootstrapTable('destroy');
		$('#mytable').bootstrapTable({
			method : 'post',
			contentType : "application/x-www-form-urlencoded",
			url : getRootPath() + '/projects/projectBoard',
			editable : false,// 可行内编辑
			striped : true, // 是否显示行间隔色
			dataField : "rows",
			pageNumber : 1, // 初始化加载第一页，默认第一页
			pagination : false,// 是否分页
			queryParamsType : 'limit',
			sidePagination : 'client',
			showColumns : false,
			toolbar : '#toolbar',// 指定工作栏
			toolbarAlign : 'right',
			dataType : "json",
			queryParams : function(params){
					var param = {
							hwpdu:projectInfo.hwpdu,
							hwzpdu:projectInfo.hwzpdu,
							pduSpdt:projectInfo.pduSpdt,
							month:$("#currentDate").val()
						}
					return param;
			},
			columns : [ [ {
				title : '阶段',
				field : 'pdu',
				width : 300,
				rowspan : 1,
				colspan : 2,
				align : "center",
				valign : 'middle',
			}, {
				title : '本次',
				colspan : 5,
				align : 'center',
				rowspan : 1,
			}, {
				title : '上次',
				colspan : 5,
				align : 'center',
				rowspan : 1,
			} ], [	
				{
					title : '子产品线',
					field : 'pdu',
					align : "center",
					valign : 'middle',
					width : 100,

				},
				{
					title : 'PDU/SPDT',
					field : 'pduspt',
					align : "center",
					valign : 'middle',
					width : 100,

				},
				{
				title : '风险',
				field : 'redState',
				align : "center",
				valign : 'middle',
				width : 100,

			}, {
				title : '预警',
				field : 'yellowState',
				align : "center",
				valign : 'middle',
				width : 100,
			}, {
				title : '正常',
				field : 'greenState',
				align : "center",
				valign : 'middle',
				width : 100,
			}, {
				title : '连续两周</br>风险项目',
				field : 'red',
				align : "center",
				valign : 'middle',
				width : 100,
			}, {
				title : '连续两周</br>预警项目',
				field : 'yellow',
				align : "center",
				valign : 'middle',
				width : 100,
			}, {
				title : '风险',
				field : 'redState1',
				align : "center",
				valign : 'middle',
				width : 100,
			}, {
				title : '预警',
				field : 'yellowState1',
				align : "center",
				valign : 'middle',
				width : 100,
			}, {
				title : '正常',
				field : 'greenState1',
				align : "center",
				valign : 'middle',
				width : 100,
			}, {
				title : '连续两周</br>风险项目',
				field : 'red1',
				align : "center",
				valign : 'middle',
				width : 100,
			}, {
				title : '连续两周</br>预警项目',
				field : 'yellow1',
				align : "center",
				valign : 'middle',
				width : 100
			} ] ],
			locale : 'zh-CN'// 中文支持,
		});
	};
function projectStatesProblem(){
	var queryParams = {
		'currentDate':$("#currentDate").val(),
		'type':problemType
     };
	$.ajax({
		url : getRootPath() + '/measureComment/projectStatesProblem',
		type : "post",
		dataType : "json",
		cache : false,
		data:queryParams,	
		success : function(data){
			histogram(data);
		}
	});
}
function qualityProblem(){
	$("#zonglanAnalysis").bootstrapTable({
    	method : 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/measureComment/kanbanProblemAnalysis',
		editable : false,// 可行内编辑
		striped : true, // 是否显示行间隔色
		dataField : "rows",
		pageNumber : 1, // 初始化加载第一页，默认第一页
		pagination : false,// 是否分页
		queryParamsType : 'limit',
		sidePagination : 'client',
		showColumns : false,
		toolbar : '#toolbar',// 指定工作栏
		toolbarAlign : 'right',
		dataType : "json",
		queryParams : function(params){
			var param = {
				'currentDate':$("#currentDate").val()
			};
			return param;
		},
		columns:[
        	[	{title : '序号',align: "center",valign:'middle',width: 50,rowspan: 2,colspan: 1,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#zonglanAnalysis').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#zonglanAnalysis').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
    		},
    		{title:'部门',field:'department',align: 'center',valign:'middle',width:"100%",rowspan: 2, colspan: 1},
    		{title:'及时闭环</br>率(汇总)',field:'closed',align: 'center',valign:'middle',width:120,rowspan: 2, colspan: 1,
    			formatter:function(val, row){
    				var v = val==null? "0%": val;
    				return v;
    			}
    			},
    		{title:'延期</br>(汇总)',field:'delay',align: 'center',valign:'middle',width:120,rowspan: 2, colspan: 1},
    		{title:'361',align: 'center',valign:'middle',width:150,rowspan: 1, colspan: 2},
    		{title:'AAR',align: 'center',valign:'middle',width:150,rowspan: 1, colspan: 2},
    		{title:'质量回溯',align: 'center',valign:'middle',width:150,rowspan: 1, colspan: 2},
    		{title:'开工会审计',align: 'center',valign:'middle',width:150,rowspan: 1, colspan: 2}
    	],    	
    	[
    		{title : '闭环率',field : 'problemClosed',align: 'center',valign:'middle',width : 75,
    			formatter:function(val, row){
    				var v = val==null? "0": val;
    				return v+"%";
    			}
    		},
    		{title : '延期',field : 'problemDelay',align: 'center',valign:'middle',width : 75},
    		{title : '闭环率',field : 'aarClosed',align: 'center',valign:'middle',width : 75,
    			formatter:function(val, row){
    				var v = val==null? "0": val;
    				return v+"%";
    			}
    		},
    		{title : '延期',field : 'aarDelay',align: 'center',valign:'middle',width : 75},
    		{title : '闭环率',field : 'backClosed',align: 'center',valign:'middle',width : 75,
    			formatter:function(val, row){
    				var v = val==null? "0": val;
    				return v+"%";
    			}
    		},
    		{title : '延期',field : 'backDelay',align: 'center',valign:'middle',width : 75},
    		{title : '闭环率',field : 'auditClosed',align: 'center',valign:'middle',width : 75,
    			formatter:function(val, row){
    				var v = val==null? "0": val;
    				return v+"%";
    			}
    		},
    		{title : '延期',field : 'auditDelay',align: 'center',valign:'middle',width : 75},
    		]
    	],
	locale:'zh-CN'//中文支持
	});
}
function statesProblem(){
	$('#StatesProblem').bootstrapTable({
		method : 'post',
		contentType : "application/x-www-form-urlencoded",
		url : getRootPath() + '/measureComment/StatesProblemAnalysis',
		editable : false,// 可行内编辑
		striped : true, // 是否显示行间隔色
		dataField : "rows",
		pageNumber : 1, // 初始化加载第一页，默认第一页
		pagination : false,// 是否分页
		queryParamsType : 'limit',
		sidePagination : 'client',
		showColumns : false,
		toolbar : '#toolbar',// 指定工作栏
		toolbarAlign : 'right',
		dataType : "json",
		queryParams : function(params){
			var param = {
				'problemType':problemType,
				'currentDate':$("#currentDate").val()
			};
			return param;
		},
		columns : [
			[
				{
	        		title : '序号',
	        		align: "center",
	        		rowspan: 2,colspan: 1,
	        		width: 50,
	        		valign: 'middle',
	        		formatter: function (value, row, index) {
	        		    var pageSize=$('#StatesProblem').bootstrapTable('getOptions').pageSize;  
	        		    var pageNumber=$('#StatesProblem').bootstrapTable('getOptions').pageNumber;
	        		    return pageSize * (pageNumber - 1) + index + 1;
	        		}
	            },
	    		{title: '部门',field: 'section',rowspan: 2,colspan: 1,align: "center",valign: 'middle',width: 280,},
	    		{title: '及时闭</br>环率',field: 'timely',rowspan: 2,colspan: 1,align: "center",valign: 'middle',width:'100%' },
	    		{title: '延期问题数',colspan: 2,align: 'center',rowspan: 1,width:240},
	    		{title: 'Open',colspan: 2,align: 'center',rowspan: 1,width:240},
	    		{title: 'Closed',colspan: 2,align: 'center',rowspan: 1,width:240}
	    		  ],
	    		  [
	    		{title: '本月新增',field: 'delayMonth',align: "center",valign: 'middle' ,sortable: true,width:120,},
	    		{title: '全年累计',field: 'delayYear',align: "center",valign: 'middle', sortable: true,width:120,},
	    		{title: '本月新增',field: 'openMonth',align: "center",valign: 'middle' ,sortable: true,width:120, },
	    		{title: '全年累计',field: 'openYear',align: "center",valign: 'middle',sortable: true,width:120,},
	    		{title: '本月新增',field: 'closedMonth',align: "center",valign: 'middle' ,sortable: true,width:120,},
	    		{title: '全年累计',field: 'closedYear',align: "center",valign: 'middle',sortable: true,width:120,	},
	    		  ]
		],
		locale : 'zh-CN',// 中文支持,
	});
}
function histogram(data){
	var posList = [
	    'left', 'right', 'top', 'bottom',
	    'inside',
	    'insideTop', 'insideLeft', 'insideRight', 'insideBottom',
	    'insideTopLeft', 'insideTopRight', 'insideBottomLeft', 'insideBottomRight'
	];
	var labelOption = {
	    normal: {
	        show: true,
	        position: 'insideBottom',
	        distance: 15,
	        align: 'left',
	        verticalAlign: 'middle',
	        rotate: 90,
	        formatter: '{c}%',
	        fontSize: 16,
	        rich: {
	            name: {
	                textBorderColor: '#fff'
	            }
	        }
	    }
	};

	option = {
	    color: ['#FF7322', '#00AC89'],
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    legend: {
	    	y: 'bottom',
	    	itemWidth: 10,
	        itemHeight: 10,
	        data: ['上上周期', '本周期']
	    },
	    calculable: true,
	    xAxis: [
	        {
	            type: 'category',
	            axisTick: {show: false},
	            data: data.du
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            name: '上上周期',
	            type: 'bar',
	            barWidth : 40,
	            barGap: 0,
	            label: labelOption,
	            data: data.lastMonth
	        },
	        {
	            name: '本周期',
	            type: 'bar',
	            barWidth : 40,
	            label: labelOption,
	            data: data.thisMonth
	        },
	    ]
	};
	var myChart = echarts.init(document.getElementById("histogram"));
	myChart.setOption(option);
}
//加载当前日期	
function initCurrentDate() {
	$.ajax({
		url: getRootPath() + '/measureComment/getTime',
		type: 'post',
		data : {
			num:6,
			flag:true
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			$("#currentDate").empty();
			data = data.data;
			if(null==data||""==data) return;
			var select_option="";
			var length = data.length<5 ? data.length: 5;
			for(var i = 1; i < length; i++){
				select_option += "<option value='"+data[i]+"'";
				if(1==i){
					select_option += "selected='selected'";
				} 
				select_option += ">"+data[i]+"</option>";
			}
			$("#currentDate").html(select_option);
            $('#currentDate').selectpicker('refresh');
            $('#currentDate').selectpicker('render');
		}
	});
}
//时间选择触发函数
function DateOnchange(){
    var tab = $("ul .active").get(0).innerText;
	if(tab == "问题"){
		projectStatesProblem();
		if(problemType == 0){
			$('#zonglanAnalysis').bootstrapTable('refresh');
		}else{
			$('#StatesProblem').bootstrapTable('refresh');
		}
	}else if(tab == "总览"){
		echars();
		echars1();
		departmentList();
	}else if(tab == "QMS"){
		$('#qmsTable').bootstrapTable('refresh');
	}
}
