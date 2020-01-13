$(function(){
	var projNo = getQueryString("projNo");
	$('#dependableTable').bootstrapTable('destroy');
	$('#dependableTable').bootstrapTable({
		method: 'GET',
		contentType: 'application/x-www-form-urlencoded',
		url: getRootPath() + '/iteration/measureReport1',
		striped: false, //是否显示行间隔色
		pageNumber: 1, //初始化加载第一页，默认第一页
		pagination: false, //是否分页
		sortable:true,//是否排序
		queryParamsType: 'limit',
		sidePagination: 'server',
		pageSize: 20, //单页记录数
		pageList: [5, 10, 20, 30], //分页步进值
		minimumCountColumns: 2, //最少允许的列数
		rowStyle:rowStyle,//通过自定义函数设置行样式
		queryParams : function(params){
			var param = {
				label : "可信指标",//流程名称
				category: "度量指标",//菜单名称
				no: projNo
			}
			return param;
		},
		columns:[
			{title:'统计周期(迭代或月)',field:'name',align: 'center',valign:'middle',width:150},
			{title : '当前阶段代码行数',field : 'id653',align: 'center',valign:'middle',width : 100},
			{title : 'Review-规范问题数',field : 'id654',align: 'center',valign:'middle',width : 200},
			{title : 'Review-逻辑问题数',field : 'id655',align: 'center',valign:'middle',width : 200},
			{title : 'Review-架构问题数',field : 'id656',align: 'center',valign:'middle',width : 200},
			{title : 'Review-其他问题数',field : 'id657',align: 'center',valign:'middle',width : 200},
			{title : 'Review缺陷密度',field : 'id658',align: 'center',valign:'middle',width : 150},
			{title : '圈复杂度',field : 'id659',align: 'center',valign:'middle',width : 80},
			{title : '函数代码行',field : 'id660',align: 'center',valign:'middle',width : 80},
			{title : '文件代码行',field : 'id661',align: 'center',valign:'middle',width : 80},
			{title : '代码规模',field : 'id662',align: 'center',valign:'middle',width : 80},
			{title : '危险函数',field : 'id663',align: 'center',valign:'middle',width : 80},
			{title : '文件重复率',field : 'id664',align: 'center',valign:'middle',width : 80},
			{title : '冗余代码',field : 'id665',align: 'center',valign:'middle',width : 80},
			{title : '编译告警',field : 'id666',align: 'center',valign:'middle',width : 80},
			{title : '白盒测试覆盖率',field : 'id667',align: 'center',valign:'middle',width : 80},
			{title : '黑盒测试覆盖率(HLT)',field : 'id668',align: 'center',valign:'middle',width : 100},
			{title : '静态分析告警',field : 'id669',align: 'center',valign:'middle',width : 80},
			{title : '代码重复率',field : 'id670',align: 'center',valign:'middle',width : 80}

		],
		locale:'zh-CN'//中文支持
	});

	function rowStyle(row, index) {
		var style={css:{'background-color':'#f0f8ff'}};
		if (index < 3) {
			return style;
		}
		return {};
	}
});
