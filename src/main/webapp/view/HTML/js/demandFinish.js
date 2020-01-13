function resourceReport() {

	//需求为完成情况
	$('#demandFinishTable').bootstrapTable('destroy');
	$("#demandFinishTable").bootstrapTable({
		method: 'GET',
		contentType: "application/x-www-form-urlencoded",
		url: getRootPath() + '/projectInspect/queryDemandFinish',
		striped: false, //是否显示行间隔色
		pageNumber: 1, //初始化加载第一页，默认第一页
		pagination: true, //是否分页
		paginationPreText: '<上一页',//指定分页条中上一页按钮的图标或文字
		paginationNextText: '下一页>',//指定分页条中下一页按钮的图标或文字
		queryParamsType: 'limit',
		sidePagination: 'server',
		pageSize: 20, //单页记录数
		pageList: [5, 10, 20, 30], //分页步进值
		minimumCountColumns: 2, //最少允许的列数
		queryParams: function (params) {
			var param = {
				'limit': params.limit,
				'offset': params.offset,
			}
			return param;
		},
		columns: [
			[{
				title: '需求编号',
				field: 'demandNumber',
				align: 'center',
				valign: 'middle',
				width: 120,
				rowspan: 2,
				colspan: 1
			},
				{
					title: '需求\\特性名称',
					field: 'demandName',
					align: 'center',
					valign: 'middle',
					width: 125,
					rowspan: 2,
					colspan: 1
				},
				{title: '状态', field: 'state', align: "center", valign: 'middle', width: 71, rowspan: 2, colspan: 1},
				{title: '代码检视', align: 'center', valign: 'middle', width: 360, rowspan: 1, colspan: 3},
				{title: '测试', align: 'center', valign: 'middle', width: 360, rowspan: 1, colspan: 3}
			],
			[
				{
					title: '严重问题个数</br>(个)', field: 'keyArrivalRate', align: 'center', valign: 'middle', width: 120,
					formatter: function (value, row, index) {
						return getNumber(value);
					}
				},
				{
					title: '一般问题个数</br>(个)', field: 'keyManpowerDemand', align: 'center', valign: 'middle', width: 120,
					formatter: function (value, row, index) {
						return getNumber(value);
					}
				},
				{
					title: '提示问题个数</br>(个)', field: 'keyManpowerGap', align: 'center', valign: 'middle', width: 120,
					formatter: function (value, row, index) {
						return getNumber(value);
					}
				},
				{
					title: '严重问题个数</br>(个)', field: 'keyTurnoverRate', align: 'center', valign: 'middle', width: 120,
					formatter: function (value, row, index) {
						return getNumber(value);
					}
				},
				{
					title: '一般问题个数</br>(个)', field: 'keyTurnoverPerson', align: 'center', valign: 'middle', width: 120,
					formatter: function (value, row, index) {
						return getNumber(value);
					}
				},
				{
					title: '提示问题个数</br>(个)', field: 'arrivalRate', align: 'center', valign: 'middle', width: 120,
					formatter: function (value, row, index) {
						return getNumber(value);
					}
				}
			]
		],
		locale: 'zh-CN'//中文支持
	});
}
function deliverablesTable() {
	//交付物
	$('#deliverablesTable').bootstrapTable('destroy');
	$("#deliverablesTable").bootstrapTable({
		method: 'post',
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		url: getRootPath() + '/deliver/queryList',
		striped: false, //是否显示行间隔色
		pageNumber: 1, //初始化加载第一页，默认第一页
		pagination: true, //是否分页
		paginationPreText: '<上一页',//指定分页条中上一页按钮的图标或文字
		paginationNextText: '下一页>',//指定分页条中下一页按钮的图标或文字
		queryParamsType: 'limit',
		sidePagination: 'server',
		pageSize: 10, //单页记录数
		pageList: [5, 10, 20, 30], //分页步进值
		minimumCountColumns: 2, //最少允许的列数
		queryParams: function (params) {
			var param = {
				'proNo': getQueryString("projNo"),
				'limit': params.limit, // 页面大小
				'offset': params.offset // 页码
			}
			return param;
		},
		columns: [
			{
				title: '序号', align: "center", valign: 'middle', width: 50,
				formatter: function (value, row, index) {
					var pageSize = $('#deliverablesTable').bootstrapTable('getOptions').pageSize;
					var pageNumber = $('#deliverablesTable').bootstrapTable('getOptions').pageNumber;
					return pageSize * (pageNumber - 1) + index + 1;
				}
			},
			{title: '交付件名称', field: 'name', align: 'center', valign: 'middle', width: 140},
			{title: '验收形式', field: 'shape', align: 'center', valign: 'middle', width: 140},
			{title: '状态', field: 'status', align: "center", valign: 'middle', width: 70,
				formatter: function (value, row, index) {
					if (row.path == null || row.path == "") {
						return '<div class="glyphicon glyphicon-exclamation-sign">' +
							'</div>'
					} else {
						return '<div class="glyphicon glyphicon-ok-circle">' +
							'</div>'
					}

				}
			},
			{title: '存档路劲', field: 'path', align: 'center', valign: 'middle', width: 175},
			{title: '最后更新时间', field: 'endTime', align: 'center', valign: 'middle', width: 160,
				formatter: function (value, row, index) {
					return changeDateFormat(value);
				}
			},
			{title: '提交人', field: 'submitter', align: 'center', valign: 'middle', width: 150},
			{title: '备注', field: 'remark', align: 'center', valign: 'middle', width: 150}
		],
		locale: 'zh-CN'//中文支持
	});
}
function leftOverTable() {
	//遗留DI
	$('#leftOverTable').bootstrapTable('destroy');
	$("#leftOverTable").bootstrapTable({
		method: 'GET',
		contentType: "application/x-www-form-urlencoded",
		url: getRootPath() + '/projectInspect/queryLeftOver',
		striped: false, //是否显示行间隔色
		pageNumber: 1, //初始化加载第一页，默认第一页
		pagination: false, //是否分页
		queryParamsType: 'limit',
		sidePagination: 'server',
		pageSize: 20, //单页记录数
		pageList: [5, 10, 20, 30], //分页步进值
		minimumCountColumns: 2, //最少允许的列数
		queryParams: function (params) {
			var param = {
				'limit': params.limit,
				'offset': params.offset,
			}
			return param;
		},
		columns: [
			[
				{
					title: '序号', align: "center", valign: 'middle', width: 50,
					formatter: function (value, row, index) {
						var pageSize = $('#leftOverTable').bootstrapTable('getOptions').pageSize;
						var pageNumber = $('#leftOverTable').bootstrapTable('getOptions').pageNumber;
						return pageSize * (pageNumber - 1) + index + 1;
					}
				},
				{title: '验收指标', field: 'acceptanceIndicators', align: 'center', valign: 'middle', width: 230},
				{title: '指标值', field: 'indicatorsValue', align: 'center', valign: 'middle', width: 220},
				{title: '责任人', field: 'personLiable', align: "center", valign: 'middle', width: 220},
				{title: '备注', field: 'remarks', align: 'center', valign: 'middle', width: 300},
			]
		],
		locale: 'zh-CN'//中文支持
	});
}
function evaluationTable() {
	//验收评价
	$('#evaluationTable').bootstrapTable('destroy');
	$("#evaluationTable").bootstrapTable({
		method: 'GET',
		contentType: "application/x-www-form-urlencoded",
		url:getRootPath() + '/projectInspect/queryEvaluation',
		striped: false, //是否显示行间隔色
		pageNumber: 1, //初始化加载第一页，默认第一页
		pagination: false, //是否分页
		queryParamsType: 'limit',
		sidePagination: 'server',
		pageSize: 20, //单页记录数
		pageList: [5, 10, 20, 30], //分页步进值
		minimumCountColumns: 2, //最少允许的列数
		queryParams : function(params){
			var param = {
				'limit': params.limit,
				'offset': params.offset,
			}
			return param;
		},
		columns:[
			[
				{title:'评价项',field:'evaluateItem',align: 'center',valign:'middle',width:260},
				{title:'权重',field:'weight',align: 'center',valign:'middle',width:220},
				{title:'最终得分',field:'score',align: "center",valign:'middle',width:220},
				{title:'理由',field:'reason',align: 'center',valign:'middle',width:330},
			]
		],
		locale:'zh-CN'//中文支持
	});
}

function getNumber(val){
	var demand = val;
	if(val == 0 || val == -1){
		demand = "-";
	}
	return demand;
}

//日期转换函数
function changeDateFormat(cellval) {
	var dateVal = cellval + "";
	if (cellval != null) {
		var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
		var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
		var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();

		var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
		var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
		var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

		return date.getFullYear() + "-" + month + "-" + currentDate;
	} else {
		return '';
	}
}
