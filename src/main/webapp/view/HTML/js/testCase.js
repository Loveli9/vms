var proNo = getQueryString("projNo");
$(function(){
	$(document).ready(function(){
		codeAndCaseTable('testCaseTable');
	})
});
//测试用例
var twelves = [];
function codeAndCaseTable(tab) {
	var str = '测试工程师,TC,TSE';
	var arr1 = str.split(',');
	$("#userRoles").selectpicker('val', arr1);
	personalCodeCount1();

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
			if ($("#userRoles").val()) {
				roles = $("#userRoles").val().join(',');
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
//根据条件统计--测试用例
function personalCodeCount1() {
	$("#userRoles").change(function () {
		$('#testCaseTable').bootstrapTable('refresh', {url: getRootPath() + '/workload/metric'});
	});
}
