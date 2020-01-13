var organ = {
	area : "",
	hwpdu : "",
	hwzpdu : "",
	pduSpdt : "",
	bu : "",
	pdu : "",
	du : ""
};
var weeks = 1;
var weekSelection;
var bumen = 1;
var department;
var departmentList;
(function() {
	function currentWeek() {
		// 判断当前是本月第几周 ,当出现跨月时，月前日期算到上月
		var myDate = new Date();
		var day = myDate.getDate();
		var today = day - myDate.getDay() + 1;
		var todayWeek = 0;
		var month = 0;
		var year = 0;
		for (var i = 0; i < 5; i++) {
			myDate = new Date();
			myDate.setDate(today);
			myDate.setDate(today - 7 * i);
			year = myDate.getFullYear();
			month = myDate.getMonth() + 1;
			todayWeek = getWeekFromDate(myDate);// 当前是该月第几周
			var selectVaule = month + "-" + todayWeek;
			var selectShow = year + "年" + month + "月第" + todayWeek + "周";
			if (i == 0) {
				$("#currentWeek").append(
						"<option selected ='selected' value='" + selectVaule + "'>" + selectShow + "</option>");
			} else {
				$("#currentWeek").append(
						"<option value='" + selectVaule + "'>" + selectShow + "</option>");
			}
		}
	}
	function getWeekFromDate(date) {// 判断读取时间是当月第几周
		// 将字符串转为标准时间格式
		var w = date.getDay();// 周几
		if (w === 0) {
			w = 7;
		}
		var week = Math.ceil((date.getDate() + 6 - w) / 7) - 1;
		return week;
	}

	weekSelection = function(val) {
		if (val == 1) {
			document.getElementById('oneWeeks').style.background = "#3399ff";
			document.getElementById('oneWeeks').style.color = "#fff";
			document.getElementById('twoWeeks').style.background = "#fff";
			document.getElementById('twoWeeks').style.color = "";
			document.getElementById('threeWeeks').style.background = "#fff";
			document.getElementById('threeWeeks').style.color = "";
			weeks = 1;
		} else if (val == 2) {
			document.getElementById('twoWeeks').style.background = "#3399ff";
			document.getElementById('twoWeeks').style.color = "#fff";
			document.getElementById('oneWeeks').style.background = "#fff";
			document.getElementById('oneWeeks').style.color = "";
			document.getElementById('threeWeeks').style.background = "#fff";
			document.getElementById('threeWeeks').style.color = "";
			weeks = 2;
		} else if (val == 3) {
			document.getElementById('threeWeeks').style.background = "#3399ff";
			document.getElementById('threeWeeks').style.color = "#fff";
			document.getElementById('oneWeeks').style.background = "#fff";
			document.getElementById('oneWeeks').style.color = "";
			document.getElementById('twoWeeks').style.background = "#fff";
			document.getElementById('twoWeeks').style.color = "";
			weeks = 3;
		}
		departmentList();
		circularChartAssessment();
		lineChartAssessment();
	};
	department = function(val) {
		if (val == 1) {
			document.getElementById('twoLevel').style.background = "#3399ff";
			document.getElementById('twoLevel').style.color = "#fff";
			document.getElementById('threeLevel').style.background = "#fff";
			document.getElementById('threeLevel').style.color = "";
			bumen = 1;
		} else if (val == 2) {
			document.getElementById('threeLevel').style.background = "#3399ff";
			document.getElementById('threeLevel').style.color = "#fff";
			document.getElementById('twoLevel').style.background = "#fff";
			document.getElementById('twoLevel').style.color = "";
			bumen = 2;
		}
		departmentList();
	};
	departmentList = function(){
		$("#projectLight").bootstrapTable('destroy');
		$('#projectLight').bootstrapTable({
			method: 'post',
	    	contentType: "application/x-www-form-urlencoded",
	        url : getRootPath() + '/project/departmentList',
	     	editable:false,// 可行内编辑
	    	striped: true, // 是否显示行间隔色
	    	dataField: "rows",
	    	pageNumber: 1, // 初始化加载第一页，默认第一页
	    	pagination:true,// 是否分页
	    	queryParamsType:'limit',
	    	sidePagination:'client',
	    	pageSize:5,// 单页记录数
	    	pageList:[5,10,20,30],// 分页步进值
	    	showColumns:false,
	    	toolbar:'#toolbar',// 指定工作栏
	    	toolbarAlign:'right',
	        dataType: "json",
	        queryParams : function(params){
				var param = {
				     'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
				     'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
					 'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
					 'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
					 'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
				     'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
				     'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
				     'client': $("#selectBig").val(),
				     'departmentalLevel': bumen,
				     'cycle': $("#currentWeek").val(),
					}
				return param;
			},
		    columns:[
		    	[
	            {
	        		title : '序号',
	        		align: "center",
	        		rowspan: 2,
	        		colspan: 1,
	        		width: 80,
	        		valign: 'middle' ,
	        		formatter: function (value, row, index) {
	        		    var pageSize=$('#projectLight').bootstrapTable('getOptions').pageSize;  
	        		    var pageNumber=$('#projectLight').bootstrapTable('getOptions').pageNumber;
	        		    return pageSize * (pageNumber - 1) + index + 1;
	        		}
	            },
	            {title:'部门名称',field:'project',width:300,rowspan: 2,colspan: 1,align: "center",valign: 'middle' ,},
	            {title: '本周',colspan: 4,align: 'center',rowspan: 1,},
	    		{title: '上周',colspan: 4,align: 'center',rowspan: 1,}
	            ],
	            [
	            	{title: '红灯',field: 'red',align: "center",valign: 'middle' ,width:100,},
	            	{title: '黄灯',field: 'yellow',align: "center",valign: 'middle' ,width:100,},
	            	{title: '绿灯',field: 'green',align: "center",valign: 'middle' ,width:100,},
	            	{title: '未测评',field: 'noPros',align: "center",valign: 'middle' ,width:100,},
	            	{title: '红灯',field: 'lastRed',align: "center",valign: 'middle' ,width:100,},
	            	{title: '黄灯',field: 'lastYellow',align: "center",valign: 'middle' ,width:100,},
	            	{title: '绿灯',field: 'lastGreen',align: "center",valign: 'middle' ,width:100,},
	            	{title: '未测评',field: 'lastNoPros',align: "center",valign: 'middle' ,width:100,},
	            ]
		    	],
		    	locale:'zh-CN',//中文支持,
		    });
	};
	function getCookie(name) {
		var arr = document.cookie.match(new RegExp("(^| )" + name
				+ "=([^;]*)(;|$)"));
		if (arr != null) {
			return decodeURIComponent(arr[2]);
		} else {
			return null;
		}
	}

	// 加载区域下拉菜单
	function initArea() {
		$.ajax({
			url : getRootPath() + '/tblArea/getAllTblArea',
			type : 'post',
			async : false,// 是否异步，true为异步
			success : function(data) {
				var select_option = "";
				$("#usertype1").empty();
				if (null == data.data || "" == data.data)
					return;
				_.each(data.data, function(val, index) {// (值，下标)
					select_option += "<option value=" + val.areaCode + ">"
							+ val.areaName + "</option>";
				})
				$("#usertype1").html(select_option);
				$('#usertype1').selectpicker('refresh');
				$('#usertype1').selectpicker('render');
			}
		});
	}
	;

	function initYeWuXian() {// 默认加载业务线
		$.ajax({
					url : getRootPath() + '/opDepartment/getOpDepartment',
					type : 'post',
					async : false,
					data : {
						level : 2,
					},
					success : function(data) {
						$('#usertype6').selectpicker("val", []);
						$("#usertype6").empty();
						$('#usertype6').prev('div.dropdown-menu').find('ul').empty();
						$('#usertype7').selectpicker("val", []);
						$("#usertype7").empty();
						$('#usertype7').prev('div.dropdown-menu').find('ul').empty();
						$('#usertype8').selectpicker("val", []);
						$("#usertype8").empty();
						$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
						if (data != null && data.data != null
								&& data.data.length > 0) {
							for (var i = 0; i < data.data.length; i++) {
								$("#usertype6").append(
										'<option value="'
												+ data.data[i].dept_id + '">'
												+ data.data[i].dept_name
												+ '</option>');
							}
						}
						$('#usertype6').selectpicker('refresh');
						$('#usertype6').selectpicker('render');
					}
				})
	};

	$(document).on("change","#usertype6",function() {
						$.ajax({
									url : getRootPath()+ '/opDepartment/getOpDepartment',
									type : 'post',
									async : false,
									data : {
										level : 3,
										ids : $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join()
									},
									success : function(data) {
										$('#usertype7').selectpicker("val", []);
										$("#usertype7").empty();
										$('#usertype7').prev('div.dropdown-menu').find('ul').empty();
										$('#usertype8').selectpicker("val", []);
										$("#usertype8").empty();
										$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
										if (data != null && data.data != null
												&& data.data.length > 0) {
											for (var i = 0; i < data.data.length; i++) {
												$("#usertype7").append('<option value="'+ data.data[i].dept_id+ '">'+ data.data[i].dept_name+ '</option>');
											}
										}
										$('#usertype7').selectpicker('refresh');
										$('#usertype7').selectpicker('render');
										// addPie(data.data,1);//1为中软 pdu
									}
								})
					});
	$(document).on("change","#usertype7",function() {
						$.ajax({
									url : getRootPath()+ '/opDepartment/getOpDepartment',
									type : 'post',
									async : false,
									data : {
										level : 4,
										ids : $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join()
									},
									success : function(data) {
										$('#usertype8').selectpicker("val", []);
										$("#usertype8").empty();
										$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
										if (data != null && data.data != null
												&& data.data.length > 0) {
											for (var i = 0; i < data.data.length; i++) {
												$("#usertype8").append('<option value="'+ data.data[i].dept_id+ '">'+ data.data[i].dept_name+ '</option>');
											}
										}
										$('#usertype8').selectpicker('refresh');
										$('#usertype8').selectpicker('render');
										// addPie(data.data,2);//2为中软 du
									}
								})
					});

	function initHWCPX() {// 默认加载华为产品线
		$.ajax({
					url : getRootPath() + '/bu/getHwdept',
					type : 'post',
					async : false,
					data : {
						level : 1,
					},
					success : function(data) {
						$('#usertype3').selectpicker("val", []);
						$("#usertype3").empty();
						$('#usertype3').prev('div.dropdown-menu').find('ul').empty();
						$('#usertype4').selectpicker("val", []);
						$("#usertype4").empty();
						$('#usertype4').prev('div.dropdown-menu').find('ul').empty();
						$('#usertype5').selectpicker("val", []);
						$("#usertype5").empty();
						$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
						if (data != null && data.data != null
								&& data.data.length > 0) {
							for (var i = 0; i < data.data.length; i++) {
								$("#usertype3").append(
										'<option value="'
												+ data.data[i].dept_id + '">'
												+ data.data[i].dept_name
												+ '</option>');
							}
						}
						$('#usertype3').selectpicker('refresh');
						$('#usertype3').selectpicker('render');
					}
				})
	};
	$(document).on("change","#usertype3",function() {
						$.ajax({
									url : getRootPath() + '/bu/getHwdept',
									type : 'post',
									async : false,
									data : {
										level : 2,
										ids : $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join()
									},
									success : function(data) {
										$('#usertype4').selectpicker("val", []);
										$("#usertype4").empty();
										$('#usertype4').prev('div.dropdown-menu').find('ul').empty();
										$('#usertype5').selectpicker("val", []);
										$("#usertype5").empty();
										$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
										if (data != null && data.data != null
												&& data.data.length > 0) {
											for (var i = 0; i < data.data.length; i++) {
												$("#usertype4").append('<option value="'+ data.data[i].dept_id+ '">'+ data.data[i].dept_name+ '</option>');
											}
										}
										$('#usertype4').selectpicker('refresh');
										$('#usertype4').selectpicker('render');
										// addPie(data.data,3);//3为华为子产品线hwzpdu
									}
								})
					});
	$(document).on("change","#usertype4",function() {
						$.ajax({
									url : getRootPath() + '/bu/getHwdept',
									type : 'post',
									async : false,
									data : {
										level : 3,
										ids : $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join()
									},
									success : function(data) {
										$('#usertype5').selectpicker("val", []);
										$("#usertype5").empty();
										$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
										if (data != null && data.data != null
												&& data.data.length > 0) {
											for (var i = 0; i < data.data.length; i++) {
												$("#usertype5").append('<option value="'+ data.data[i].dept_id+ '">'+ data.data[i].dept_name+ '</option>');
											}
										}
										$('#usertype5').selectpicker('refresh');
										$('#usertype5').selectpicker('render');
										// addPie(data.data,4);//3为PDU/SPDT
									}
								})
					});

	String.prototype.endWith = function(str) {
		var reg = new RegExp(str + "$");
		return reg.test(this);
	};

	$(document).ready(
			function() {
				$('#usertype2').selectpicker("val", [ "在行" ]);
				var zrOrhwselect = getCookie("zrOrhwselect");
				$('#selectBig').selectpicker("val",
						[ zrOrhwselect == '1' ? '1' : '2' ]);
				currentWeek();
				selectChange();
				initArea();
				initHWCPX();
				initYeWuXian();

				$('.modal-backdrop').addClass("disabled");
				circularChartAssessment();
				lineChartAssessment();
				departmentList();
				var Menus = getCookie('Menulist');
				if (Menus.indexOf("-6-") != -1) {
					$("#guocheng").css("display", "");
				} else {
					$("#guocheng").css("display", "none");
				}
				if (Menus.indexOf("-7-") != -1) {
					$("#tjfsinfo").css("display", "");
				} else {
					$("#tjfsinfo").css("display", "none");
				}
				if (Menus.indexOf("-8-") != -1) {
					$("#zbtop5").css("display", "");
				} else {
					$("#zbtop5").css("display", "none");
				}
				if (Menus.indexOf("-64-") != -1) {
					$("#projectSurvey").css("display", "");
				} else {
					$("#projectSurvey").css("display", "none");
				}
				// 鼠标移入移出悬浮框事件
				$("#retop").mouseover(function() {
					document.getElementById("retop").style.display = "none";
					document.getElementById("retop2").style.display = "inline";
				});
				$("#retop2").mouseout(function() {
					document.getElementById("retop").style.display = "inline";
					document.getElementById("retop2").style.display = "none";
				});

				$("#feedback").mouseover(function() {
					document.getElementById("back").style.color = "#367FA9";
				});
				$("#feedback").mouseout(function() {
					document.getElementById("back").style.color = "white";
				});
			})
})()
function circularChartAssessment() {// 默认加载361成熟度圆环图
	$.ajax({
		url : getRootPath() + '/project/circularChartAssessment',
		type : 'post',
		async : false,// 是否异步，true为异步
		data : {
			'area' : $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(),// 转换为字符串
			'hwpdu' : $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),// 转换为字符串
			'hwzpdu' : $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),// 转换为字符串
			'pduSpdt' : $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),// 转换为字符串
			'bu' : $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),// 转换为字符串
			'pdu' : $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),// 转换为字符串
			'du' : $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),// 转换为字符串
			'continuousCycle' : weeks,
			'cycle' : $("#currentWeek").val(),
		},
		success : function(data) {
			loadPie(data);
		}
	});
};
function callback() {
	$("html,body").animate({
		scrollTop : 0
	}, 200);
};
function exportEvent(color) {
	var $eleForm = $("<form method='POST'></form>");
	$eleForm.attr("action", getRootPath() + "/project/export361");
	$eleForm.append($('<input type="hidden" name="color" value="' + color+ '">'));
	$eleForm.append($('<input type="hidden" name="area" value="'+ ($("#usertype1").selectpicker("val") == null ? "" : $("#usertype1").selectpicker("val").join()) + '">'));
	$eleForm.append($('<input type="hidden" name="hwpdu" value="'+ ($("#usertype3").selectpicker("val") == null ? "" : $("#usertype3").selectpicker("val").join()) + '">'));
	$eleForm.append($('<input type="hidden" name="hwzpdu" value="'+ ($("#usertype4").selectpicker("val") == null ? "" : $("#usertype4").selectpicker("val").join()) + '">'));
	$eleForm.append($('<input type="hidden" name="pduSpdt" value="'+ ($("#usertype5").selectpicker("val") == null ? "" : $("#usertype5").selectpicker("val").join()) + '">'));
	$eleForm.append($('<input type="hidden" name="bu" value="'+ ($("#usertype6").selectpicker("val") == null ? "" : $("#usertype6").selectpicker("val").join()) + '">'));
	$eleForm.append($('<input type="hidden" name="pdu" value="'+ ($("#usertype7").selectpicker("val") == null ? "" : $("#usertype7").selectpicker("val").join()) + '">'));
	$eleForm.append($('<input type="hidden" name="du" value="'+ ($("#usertype8").selectpicker("val") == null ? "" : $("#usertype8").selectpicker("val").join()) + '">'));
	$(document.body).append($eleForm);
	// 提交表单，实现下载
	$eleForm.submit();
};
function rebackPage() {
	document.getElementById("hideDiv").style.display = "";
	document.getElementById("tabsShow").style.display = "none";
}
function loadPie(data) {
	var echartsPie;
	var josn = [ {
		value : data.red,
		name : '红灯项目数'
	}, {
		value : data.yellow,
		name : '黄灯项目数'
	}, {
		value : data.green,
		name : '绿灯项目数'
	}, {
		value : data.noPros,
		name : '未测评项目数'
	}, ];
	var option = {
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b}: {c} ({d}%)"
		},
		legend : {
			orient : 'vertical',
			x : 'left',
			data : [ '红灯项目数', '黄灯项目数', '绿灯项目数', '未测评项目数' ]
		},
		series : [ {
			name : '361项目评估',
			type : 'pie',
			radius : [ '35%', '75%' ],
			avoidLabelOverlap : false,
			label : {
				normal : {
					show : true,
					formatter : '{c}'
				// position:'inside'//'center'
				},
				emphasis : {
					show : true,
					textStyle : {
						fontSize : '12',
						fontWeight : 'bold'
					}
				}
			},
			labelLine : {
				normal : {
					show : true
				}
			},
			data : josn,
			itemStyle : {
				emphasis : {
					shadowBlur : 10,
					shadowOffsetX : 0,
					shadowColor : 'rgba(0, 0, 0, 0.5)'
				},
				normal : {
					color : function(params) {
						// 自定义颜色
						var colorList = [ '#f57454', '#f7b547', '#1adc21',
								'#bdb7b7', ];
						return colorList[params.dataIndex]
					}
				}
			},
		} ]
	};
    var myChart=document.getElementById("divpie");
    myChart.style.width=(window.outerWidth-75)*0.35+'px';
    echartsPie=echarts.init(myChart);
	echartsPie.setOption(option);
}
function loadweek(data) {
	var echartsPie;
	var option = {
		title : {
			text : ''
		},
		tooltip : {
			trigger : 'axis'
		},
		color : [ '#f57454', '#f7b547', '#1adc21', '#bdb7b7' ],
		legend : {
			data : [ '红灯项目数', '黄灯项目数', '绿灯项目数', '未测评项目数' ]
		},
		grid : {
			left : '3%',
			right : '3%',
			bottom : '3%',
			containLabel : true
		},
		toolbox : {
			feature : {
				saveAsImage : {}
			}
		},
		xAxis : {
			type : 'category',
			boundaryGap : false,
			data : [ '第一周', '第二周', '第三周', '第四周', '第五周' ]
		},
		yAxis : {
			type : 'value'
		},
		series : [ {
			name : '红灯项目数',
			type : 'line',
			// stack: '总量',
			data : data[0]
		}, {
			name : '黄灯项目数',
			type : 'line',
			// stack: '总量',
			data : data[1]
		}, {
			name : '绿灯项目数',
			type : 'line',
			// stack: '总量',
			data : data[2]
		}, {
			name : '未测评项目数',
			type : 'line',
			// stack: '总量',
			data : data[3]
		}, ]
	};
    var myChart=document.getElementById("divWeek");
    myChart.style.width=(window.outerWidth-75)*0.55+'px';
    echartsPie=echarts.init(myChart);
	echartsPie.setOption(option);
}
function lineChartAssessment() {// 默认加载361成熟度折线图
	$.ajax({
		url : getRootPath() + '/project/lineChartAssessment',
		type : 'post',
		async : false,// 是否异步，true为异步
		data : {
			'area' : $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(),// 转换为字符串
			'hwpdu' : $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),// 转换为字符串
			'hwzpdu' : $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),// 转换为字符串
			'pduSpdt' : $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),// 转换为字符串
			'bu' : $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),// 转换为字符串
			'pdu' : $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),// 转换为字符串
			'du' : $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(),// 转换为字符串
			'cycle' : $("#currentWeek").val(),
		},
		success : function(data) {
			loadweek(data);
		}
	});
};
function selectOnchange() {
	$.ajax({
		url : getRootPath() + '/bu/queryMess',
		type : 'post',
		async : false,// 是否异步，true为异步
		data : {
			'name' : $("#projName").val(),
			'pm' : $("#projMgmr").val(),
			'area' : $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(),// 转换为字符串
			'hwpdu' : $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),// 转换为字符串
			'hwzpdu' : $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),// 转换为字符串
			'pduSpdt' : $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),// 转换为字符串
			'bu' : $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),// 转换为字符串
			'pdu' : $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),// 转换为字符串
			'du' : $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join()// 转换为字符串
		},
		success : function() {
		}
	});
	departmentList();
	circularChartAssessment();
	lineChartAssessment();
};
function selectChange() {
	$('#usertype1').selectpicker("val", []);
	$('#usertype2').selectpicker("val", [ "在行" ]);
	$('#usertype3').selectpicker("val", []);
	$('#usertype4').selectpicker("val", []);
	$("#usertype4").empty();
	$('#usertype4').prev('div.dropdown-menu').find('ul').empty();
	$('#usertype5').selectpicker("val", []);
	$("#usertype5").empty();
	$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
	$('#usertype6').selectpicker("val", []);
	$('#usertype7').selectpicker("val", []);
	$("#usertype7").empty();
	$('#usertype7').prev('div.dropdown-menu').find('ul').empty();
	$('#usertype8').selectpicker("val", []);
	$("#usertype8").empty();
	$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
	selectOnchange();
	if ($("#selectBig").val() == '1') {
		$("#select8").css('display', '');
		$("#select7").css('display', '');
		$("#select6").css('display', '');
		$("#select5").css('display', 'none');
		$("#select4").css('display', 'none');
		$("#select3").css('display', 'none');
		$("#select1").css('display', 'none');
	} else if ($("#selectBig").val() == '2') {
		$("#select8").css('display', 'none');
		$("#select7").css('display', 'none');
		$("#select6").css('display', 'none');
		$("#select5").css('display', '');
		$("#select4").css('display', '');
		$("#select3").css('display', '');
		$("#select1").css('display', 'none');
	} else if ($("#selectBig").val() == '3') {
		$("#select8").css('display', 'none');
		$("#select7").css('display', 'none');
		$("#select6").css('display', 'none');
		$("#select5").css('display', 'none');
		$("#select4").css('display', 'none');
		$("#select3").css('display', 'none');
		$("#select1").css('display', '');
	}
};
function queryMaturityAssessment(organ) {
	var result = "";
	$.ajax({
		url : getRootPath() + '/project/initMaturityAssessment',
		type : 'post',
		async : false,// 是否异步，true为异步
		data : {
			'area' : organ.area == null ? "" : organ.area,
			'hwpdu' : organ.hwpdu == null ? "" : organ.hwpdu,
			'hwzpdu' : organ.hwzpdu == null ? "" : organ.hwzpdu,
			'pduSpdt' : organ.pduSpdt == null ? "" : organ.pduSpdt,
			'bu' : organ.bu == null ? "" : organ.bu,
			'pdu' : organ.pdu == null ? "" : organ.pdu,
			'du' : organ.du == null ? "" : organ.du
		},
		success : function(data) {
			result = data;
		}
	});
	return result;
}
/*
 * 点击项目数加载项目列表
 */
function loadDatagrid(osel) {
	document.getElementById("hideDiv").style.display = "none";
	document.getElementById("tabsShow").style.display = "";

	var type = '';
	if (osel == 'red') {
		$("#tabsTitle").html("红灯项目列表");
		type = 'red';
		var backPage = '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color: #f57454; "></div>';
	} else if (osel == 'yell') {
		$("#tabsTitle").html("黄灯项目列表");
		type = 'yellow';
		var backPage = '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color: #f7b547; "></div>';
	} else if (osel == 'green') {
		$("#tabsTitle").html("绿灯项目列表");
		type = 'green';
		var backPage = '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color: #1adc21; "></div>';
	} else {
		$("#tabsTitle").html("未评测项目列表");
		type = 'wcp';
		var backPage = '<div style="display: inline-block; margin-right: 5px; border-radius: 50px;width: 20px;height:20px;background-color:#bdb7b7; "></div>';
	}
	/*
	 * $('#tabs').modal({ backdrop: 'static', keyboard: false });
	 * $("#tabs").modal('show');
	 */
	$.ajax({
		url : getRootPath() + '/project/loadTabsByTypes',
		type : 'post',
		async : false,// 是否异步，true为异步
		data : {
			'colorType' : type,
			'area' : $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(),// 转换为字符串
			'hwpdu' : $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(),// 转换为字符串
			'hwzpdu' : $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(),// 转换为字符串
			'pduSpdt' : $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(),// 转换为字符串
			'bu' : $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(),// 转换为字符串
			'pdu' : $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(),// 转换为字符串
			'du' : $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join()// 转换为字符串
		},
		success : function(data) {
			$("#tablelsw tr").remove();
			for (var i = 0; i < data.length; i++) {
				var tab = '<tr">' + '<td><a style="color: #721b77;" title="'
						+ data[i].PRO_NAME + '" href="' + getRootPath()
						+ '/bu/projView?projNo=' + data[i].NO + '&a='
						+ Math.random() + '">'
						+ convertDataVal(data[i].PRO_NAME) + '</a></td>'
						+ '<td>' + convertDataVal(data[i].PM_NAME) + '</td>'
						+ '<td>' + convertDataVal(data[i].AREA) + '</td>'
						+ '<td>' + convertDataVal(data[i].HWPDU) + '</td>'
						+ '<td>' + convertDataVal(data[i].HWZPDU) + '</td>'
						+ '<td>' + convertDataVal(data[i].PDU_SPDT) + '</td>'
						+ '<td>' + convertDataVal(data[i].PROJECT_STATE)
						+ '</td>' + '<td>' + backPage + '</td>' + '<td>'
						+ convertDataVal(data[i].TOTAL_SCORE) + '</td></tr>';

				$("#tablelsw").append(tab);
			}
		}
	});
	tablelswInfo(10, 'tablelsw');
	parent.hideMenu();
}
function convertDataVal(val) {
	if (val) {
		return val;
	} else {
		return "";
	}
}

