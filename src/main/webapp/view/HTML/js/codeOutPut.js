var proNo = getQueryString("projNo");
$(function(){
	$(document).ready(function(){
		codeAndCaseTable('codeTable');
	})
});
//代码
var twelves = [];
function codeAndCaseTable(tab) {
	var str = '开发工程师,SE,MDE';
	var arr = str.split(',');
	$("#userRole").selectpicker('val', arr);
	//折线图
	myChart1new();
	personalCodeCount();

	var columnsArray = [];
	columnsArray.push({
		"title": '姓名',
		"field": 'name',
		halign: 'center',
		align: 'center',
		width: 42
	});
	columnsArray.push({
		"title": '工号',
		"field": 'account',
		halign: 'center',
		align: 'center',
		width: 42
	});
	columnsArray.push({
		"title": '岗位',
		"field": 'role',
		halign: 'center',
		align: 'center',
		width: 42
	});

	$.ajax({
		async: false,
		type: "GET",
		url: getRootPath() + '/workload/abscissa',
		contentType: "application/json;charset=utf-8",
		dataType: "json",
		json: 'callback',
		success: function (json) {
			twelves = json.data;
			for (var i = 0; i < twelves.length; i++) {
				var title = twelves[i];
				columnsArray.push({
					title: title,
					field: title,
					halign: 'center',
					align: 'center',
					width: 42,
					formatter: function (value, row, index, field) {
						var count = $('#'+tab).bootstrapTable('getData').length - 1;
						if (count == index) {
							return value;
						}

						if (row && row['workloads'] && row['workloads'][field]) {
							var load = row['workloads'][field];
							if (load && load['times'] > 0) {
								value = load['amount'] == 0 ? "--" : load['amount'];
								var tips = '提交次数: ' + load['times'] + '</br>' + '最后提交时间: ' + load['lastCommitTime'] + '</br>';
								tips = load['amount'] > 0 ? (load['type'] + ': ' + load['amount'] + '</br>' + tips) : tips;

								var show = '<a class="tooltip-test" data-toggle="tooltip"' +
									' data-placement="bottom" data-html="true" title="' + tips + '" >'
									+ value + '</a>';

								return show;
							}
						}
					},
				});
			}
		}
	});

	columnsArray.push({
		"title": '合计',
		"field": 'quantity',
		halign: 'center',
		align: 'center',
		width: 42,
		formatter: function (value, row, index) {
			var count = $('#'+tab).bootstrapTable('getData').length - 1;
			if (count == index) {
				return value;
			}

			var total = 0;
			if (row && row['workloads']) {
				var loads = row['workloads'];
				for (var key in loads) {
					total += loads[key]['amount'];
				}
			}
			return total > 0 ? total : "-";
		},
	});

	$('#'+tab).bootstrapTable({
		method: 'get',
		url: getRootPath() + '/workload/metric',
		toolbar: '#toolbar',
		striped: false, //是否显示行间隔色
		dataField: "data",
		queryParamsType: 'limit',
		sidePagination: 'server',
		toolbarAlign: 'right',
		buttonsAlign: 'right',//按钮对齐方式
		queryParams: function (params) {
			var roles = "";
			if("codeTable" == tab){
				if ($("#userRole").val()) {
					roles = $("#userRole").val().join(',');
				}
			}else if("testCaseTable" == tab){
				if ($("#userRoles").val()) {
					roles = $("#userRoles").val().join(',');
				}
			}else{
				if ($("#documentUserRole").val()) {
					roles = $("#documentUserRole").val().join(',');
				}
			}
			var param = {
				projectId: proNo,
				codeType: $('#personal-code-type').val(),
				duty: encodeURI(roles)
			};
			return param;
		},
		columns: columnsArray,
		locale: 'zh-CN',//中文支持,
		onLoadSuccess: function () {
			addRow(tab);
			tip();
		}
	});
}
function addRow(tab1) {
	var summation = $('#'+tab1).bootstrapTable('getData');


	if (summation && summation.length > 0) {
		var count = summation.length;

		var total = {};


		for (var j = 0, len = count; j < len; j++) {
			var loads = summation[j]['workloads'];
			if (loads) {
				for (var key in loads) {
					var amount = loads[key]['amount'];
					if (amount > 0) {
						var monthAmount = total[key];
						total[key] = monthAmount ? monthAmount + amount : amount;
					}
				}
			}
		}

		for (var key in total) {
			var amount = total[key];
			if (amount > 0) {
				var totalAmount = total['quantity'];
				total['quantity'] = totalAmount ? totalAmount + amount : amount;
			}
		}
		total['name'] = "合计";

		$('#'+tab1).bootstrapTable('insertRow', {
			index: count,
			row: total
		});

		$('#'+tab1).bootstrapTable('mergeCells', {
			index: count,
			field: 'name',
			colspan: 3,
			rowspan: 1
		});
	}
}

function tip() {
	$("[data-toggle='tooltip']").tooltip();
}
var myChart1;
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
		}
		]
	};

	// 使用刚指定的配置项和数据显示图表。
	myChart1.setOption(option1);
	//初始化图标标签
}
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
			projectId: proNo,
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
//根据条件统计员工个人代码量、并根据代码类别筛选
function personalCodeCount() {
	var param1 = "0";//0:commit 1:message
	var param2 = "all";//所有代码量

	$("#userRole").change(function () {
		param2 = $('#personal-code-type').val();
		search(param1, param2);
		$('#codeTable').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
	});

	$("#personal-code-type").change(function () {
		param2 = $('#personal-code-type').val();
		search(option1, param2);
		$('#codeTable').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
	});

	search(param1, param2);
}
