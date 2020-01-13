var projNo="";
var buName="";
var zdhkfxl=0;
var ylsj=0;
var ylzxs=0;
$(function(){
	projNo = window.parent.projNo;
	buName = window.parent.projBU;
	var url=location.href;
	var labelId = url.substring(url.indexOf("=")+1);
	
	var option1 = "0";
	var option2 = "ALL";
	var isEdit = false;
	
	//JavaScript获取当前月份的前12个月，获取最近的12个月
    var fill = function (d) {
        return d < 10 ? '0' + d : d.toString();
    };
    var current = new Date();
    var year = current.getFullYear();
    var month = current.getMonth() + 1;

    var calendar = [];
    while ((year > current.getFullYear() - 1) || (month > current.getMonth() + 1)) {
        calendar.push(year + '-' + fill(month));
        month -= 1;
        if (month <= 0) {
            year -= 1;
            month = 12;
        }
    }
    calendar.reverse();//数组取反
    
//    var header = [
//		{
//    		title:'姓名',
//    		halign:'center',
//    		align:'center',
//    		field:'name',
//    		sortable:false,
//    		width:42,
//    	},
//    	{
//    		title:'工号',
//    		halign:'center',
//    		align:'center',
//    		field:'number',
//    		sortable:false,
//    		width:45,
//    	},
//    	{
//    		title:'岗位',
//    		halign:'center',
//    		align:'center',
//    		field:'role',
//    		sortable:false,
//    		width:65,
//    	},
//    	{
//    		title:calendar[0],
//    		halign:'center',
//    		align: 'center',
//    		field:'january',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes1,value);
//            },
//    	},
//    	{
//    		title:calendar[1],
//    		halign:'center',
//    		align:'center',
//    		field:'february',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes2,value);
//            },
//    	},
//    	{
//    		title:calendar[2],
//    		halign:'center',
//    		align:'center',
//    		field:'march',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes3,value);
//            },
//    	},
//    	{
//    		title:calendar[3],
//    		halign:'center',
//    		align:'center',
//    		field:'april',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes4,value);
//            },
//    	},
//    	{
//    		title:calendar[4],
//    		halign:'center',
//    		align:'center',
//    		field:'may',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes5,value);
//            },
//    	},
//    	{
//    		title:calendar[5],
//    		halign:'center',
//    		align:'center',
//    		field:'june',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes6,value);
//            },
//    	},
//    	{
//    		title:calendar[6],
//    		halign:'center',
//    		align:'center',
//    		field:'july',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes7,value);
//            },
//    	}, 
//    	{
//    		title:calendar[7],
//    		halign:'center',
//    		align:'center',
//    		field:'august',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes8,value);
//            },
//    	},
//    	{
//    		title:calendar[8],
//    		halign:'center',
//    		align:'center',
//    		field:'september',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes9,value);
//            },
//    	},
//    	{
//    		title:calendar[9],
//    		halign:'center',
//    		align:'center',
//    		field:'october',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes10,value);
//            },
//    	},
//    	{
//    		title:calendar[10],
//    		halign:'center',
//    		align:'center',
//    		field:'november',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes11,value);
//            },
//    	},
//    	{
//    		title:calendar[11],
//    		halign:'center',
//    		align:'center',
//    		field:'december',
//    		sortable:false,
//    		width:58,
//    		formatter: function (value, row, index, field) {
//                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
//                if (count == index) {
//                    return value;
//                }
//
//                return getShow(row.mes12,value);
//            },
//    	},
//    	{
//    		title:'合计',
//    		halign:'center',
//    		align:'center',
//    		field:'sum',
//    		sortable:false,
//    		width:45,
//    	}
//	];
   
    var headers = [
		{
    		title:'姓名',
    		halign:'center',
    		align:'center',
    		field:'name',
    		sortable:true,
    		width:42,
    	},
    	{
    		title:'工号',
    		halign:'center',
    		align:'center',
    		field:'number',
    		sortable:true,
    		width:45,
    	},
    	{
    		title:'岗位',
    		halign:'center',
    		align:'center',
    		field:'role',
    		sortable:true,
    		width:65,
    	},
    	{
    		title:calendar[0],
    		halign:'center',
    		align: 'center',
    		field:'january',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[0],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[1],
    		halign:'center',
    		align:'center',
    		field:'february',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[1],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[2],
    		halign:'center',
    		align:'center',
    		field:'march',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[2],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[3],
    		halign:'center',
    		align:'center',
    		field:'april',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[3],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[4],
    		halign:'center',
    		align:'center',
    		field:'may',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[4],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[5],
    		halign:'center',
    		align:'center',
    		field:'june',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[5],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[6],
    		halign:'center',
    		align:'center',
    		field:'july',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[6],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	}, 
    	{
    		title:calendar[7],
    		halign:'center',
    		align:'center',
    		field:'august',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[7],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[8],
    		halign:'center',
    		align:'center',
    		field:'september',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[8],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[9],
    		halign:'center',
    		align:'center',
    		field:'october',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[9],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[10],
    		halign:'center',
    		align:'center',
    		field:'november',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[10],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:calendar[11],
    		halign:'center',
    		align:'center',
    		field:'december',
    		sortable:true,
    		width:58,
    		editable: {
                type: 'text',
                title: calendar[11],
                placement: 'bottom',
                emptytext:'&#12288',
                validate: function (v) {
                	var reg=/^([1-9]\d*|[0]{1,1})$/; //含0正整数
                     if(!reg.test(v)){
                    	 return '请输入正确的用例数';
                     }
                }
            },
    	},
    	{
    		title:'合计',
    		halign:'center',
    		align:'center',
    		field:'sum',
    		sortable:true,
    		width:45,
    		formatter: function(value, row, index){
				sum = row.january + row.february + row.march + row.april + row.may + row.june + 
				row.july + row.august + row.september + row.october + row.november + row.december;
				return sum;//行合计
            }, 
    	}
	];
    
    function getTitleByField(field){
    	if(field == "january"){
    		date = calendar[0]+"-01";
    	}else if(field == "february"){
    		date = calendar[1]+"-01";
    	}else if(field == "march"){
    		date = calendar[2]+"-01";
    	}else if(field == "april"){
    		date = calendar[3]+"-01";
    	}else if(field == "may"){
    		date = calendar[4]+"-01";
    	}else if(field == "june"){
    		date = calendar[5]+"-01";
    	}else if(field == "july"){
    		date = calendar[6]+"-01";
    	}else if(field == "august"){
    		date = calendar[7]+"-01";
    	}else if(field == "september"){
    		date = calendar[8]+"-01";
    	}else if(field == "october"){
    		date = calendar[9]+"-01";
    	}else if(field == "november"){
    		date = calendar[10]+"-01";
    	}else if(field == "december"){
    		date = calendar[11]+"-01";
    	}
    	return date;
    }
    
//    function initGerenCodeTable(){
    $.fn.initGerenCodeTable=function(){
    	zdhkfxl++;
    	/*$('#gerenCode').bootstrapTable({
        	method: 'post',
        	contentType: "application/x-www-form-urlencoded",
        	url:getRootPath() + '/svnTask/monthly1',
//        	height:tableHeight(),//高度调整
        	toolbar: '#toolbar',
        	editable:true,//可行内编辑
//        	showToggle:true, 
//        	sortable: true, //是否启用排序
//        	sortOrder: "asc",
//        	striped: true, //是否显示行间隔色
        	dataField: "rows",
        	queryParamsType:'limit',
        	sidePagination:'server',
        	toolbarAlign:'right',
        	buttonsAlign:'right',//按钮对齐方式
        	queryParams: function(params){
        		var roles = ""
    			if ($("#userRole").val() != null) {
    				roles = $("#userRole").val().join(',');
    			}
            	var param = {
            			projNo: projNo,
    					type: option1,
    					codeType: option2,
    					role: encodeURI(roles),
            	    }
            	return param;
            },
            columns:header,
        	locale:'zh-CN',//中文支持,
        	onLoadSuccess: function () {
        		addRow("gerenCode");
                tip();
        	},
        }); */
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
            width: 50
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
                            var count = $('#gerenCode').bootstrapTable('getData').length - 1;
                            if (count == index) {
                                return value;
                            }

                            if (row && row['workloads'] && row['workloads'][field]) {
                                var load = row['workloads'][field];
                                if (load && load['times'] > 0) {
                                    value = load['amount'] == 0 ? "--" : load['amount'];
                                    var tips = '提交次数: ' + load['times'] + '</br>' + '最后提交时间: ' + load['lastCommitTime'] + '</br>';
                                    tips = load['amount'] > 0 ? ((load['type']=='ALL' ? '测试代码量' : load['type']) + ': ' + load['amount'] + '</br>' + tips) : tips;

                                    var show = '<a class="tooltip-test" data-toggle="tooltip"' +
                                        ' data-placement="bottom" data-html="true" title="' + tips + '" >'
                                        + value + '</a>';

                                    return show;
                                }
                            }
                        },
                        //footerFormatter: function (value) {
                        //    var count = 0;
                        //    for (var i in value) {
                        //        count += value[i];
                        //    }
                        //    return count;
                        //}
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
                var count = $('#gerenCode').bootstrapTable('getData').length - 1;
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

        $('#gerenCode').bootstrapTable({
            method: 'get',
            //contentType: "application/x-www-form-urlencoded",
            url: getRootPath() + '/workload/metric',
//        	height:tableHeight(),//高度调整
            toolbar: '#toolbar',
            //editable:true,//可行内编辑
//        	showToggle:true, 
//        	sortable: true, //是否启用排序
//        	sortOrder: "asc",
//            striped: true, //是否显示行间隔色
            dataField: "data",
//        	pageNumber: 1, //初始化加载第一页，默认第一页
//        	pagination:true,//是否分页
            queryParamsType: 'limit',
            sidePagination: 'server',
//        	pageSize:30,//单页记录数
//        	pageList:[5,10,20,30],//分页步进值
//        	clickToSelect: true,//是否启用点击选中行
            toolbarAlign: 'right',
            buttonsAlign: 'right',//按钮对齐方式
            queryParams: function (params) {
                var roles = "";
                if ($("#userRole").val()) {
                    roles = $("#userRole").val().join(',');
                }
                var param = {
                    projectId: projNo,
                    codeType: option2,
                    duty: encodeURI(roles)
                };
                return param;
            },
            //showFooter: true,
            columns: columnsArray,
            locale: 'zh-CN',//中文支持,
            onLoadSuccess: function () {
                addRow("gerenCode");
                tip();
            }
        });
    } 
    
//    function initSubmitTestCasesTable(){
    $.fn.initSubmitTestCasesTable=function(){
    	ylsj++;
    	$('#submitTestCases').bootstrapTable({
        	method: 'post',
        	contentType: "application/x-www-form-urlencoded",
        	url:getRootPath() + '/testMeasures/get2TestCases',
//        	height:tableHeight(),//高度调整
        	toolbar: '#toolbar',
        	editable:isEdit,//可行内编辑
//        	showToggle:true, 
        	sortable: true, //是否启用排序
        	sortOrder: "asc",
        	striped: true, //是否显示行间隔色
        	dataField: "rows",
        	queryParamsType:'limit',
        	sidePagination:'server',
        	toolbarAlign:'right',
        	buttonsAlign:'right',//按钮对齐方式
//        	showFooter: true,//底部合计
        	queryParams: function(params){
        		var roles = ""
    			if ($("#submitTestCasesRole").val() != null) {
    				roles = $("#submitTestCasesRole").val().join(',');
    			}
            	var param = {
            			projNo: projNo,
    					type: 0,
    					role: encodeURI(roles),
            	    }
            	return param;
            },
            //保存数据并刷新
            onEditableSave: function (field, row, oldValue, $el) {
            	var date = getTitleByField(field);
            	var value = row[field];
            	var type = 0;
            	var number = row.number;
            	$.ajax({
                    type: "post",
                    url: getRootPath()+"/testMeasures/input2TestCases",
     				dataType: "json",
     				contentType : 'application/json;charset=utf-8', //设置请求头信息
     				data: JSON.stringify({
     					'proNO': projNo,
            			'hwAccount': number,
            			'date': date,
    					'testCaseType': type,
    					'testCaseValue': value,
     				}),
                    success: function (data) {
                    	$('#submitTestCases').bootstrapTable('refresh', {url: getRootPath() + '/testMeasures/get2TestCases'});
                    }
                });
            },
            columns:headers,
        	locale:'zh-CN',//中文支持
        	onLoadSuccess: function () {
        		addRows("submitTestCases");
        	},
        });   
    }
    
//    function initExecuteTestCasesTable(){
    $.fn.initExecuteTestCasesTable=function(){
    	ylzxs++;
    	$('#executeTestCases').bootstrapTable({
        	method: 'post',
        	contentType: "application/x-www-form-urlencoded",
        	url:getRootPath() + '/testMeasures/get2TestCases',
//        	height:tableHeight(),//高度调整
        	toolbar: '#toolbar',
        	editable:isEdit,//可行内编辑
//        	showToggle:true, 
        	sortable: true, //是否启用排序
        	sortOrder: "asc",
        	striped: true, //是否显示行间隔色
        	dataField: "rows",
        	queryParamsType:'limit',
        	sidePagination:'server',
        	toolbarAlign:'right',
        	buttonsAlign:'right',//按钮对齐方式
//        	showFooter: true,//底部合计
        	queryParams: function(params){
        		var roles = ""
    			if ($("#executeTestCasesRole").val() != null) {
    				roles = $("#executeTestCasesRole").val().join(',');
    			}
            	var param = {
            			projNo: projNo,
    					type: 1,
    					role: encodeURI(roles),
            	    }
            	return param;
            },
            //保存数据并刷新
            onEditableSave: function (field, row, oldValue, $el) {
            	var date = getTitleByField(field);
            	var value = row[field];
            	var type = 1;
            	var number = row.number;
            	$.ajax({
                    type: "post",
                    url: getRootPath()+"/testMeasures/input2TestCases",
     				dataType: "json",
     				contentType : 'application/json;charset=utf-8', //设置请求头信息
     				data: JSON.stringify({
     					'proNO': projNo,
            			'hwAccount': number,
            			'date': date,
    					'testCaseType': type,
    					'testCaseValue': value,
     				}),
                    success: function (data) {
                    	$('#executeTestCases').bootstrapTable('refresh', {url: getRootPath() + '/testMeasures/get2TestCases'});
                    }
                });
            },
            columns:headers,
        	locale:'zh-CN',//中文支持
        	onLoadSuccess: function () {
        		addRows("executeTestCases");
        	},
        });  
    }
    
//    function addRow(id){
//        var count = $("#"+id).bootstrapTable('getData').length;
//        var summation = $("#"+id).bootstrapTable('getData');
//        if(count != 0){
//        	$("#"+id).bootstrapTable('insertRow',{index:count,row:{number:"合计",january:summation[count-1]["januarys"],february:summation[count-1]["februarys"],march:summation[count-1]["marchs"],april:summation[count-1]["aprils"],may:summation[count-1]["mays"],
//        		june:summation[count-1]["junes"],july:summation[count-1]["julys"],august:summation[count-1]["augusts"],september:summation[count-1]["septembers"],october:summation[count-1]["octobers"],november:summation[count-1]["novembers"],
//        		december:summation[count-1]["decembers"],sum:summation[count-1]["sums"]}});
//        }
//    }

    function addRow(id) {
        var summation = $("#"+id).bootstrapTable('getData');


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

            $("#"+id).bootstrapTable('insertRow', {
                index: count,
                row: total
            });

            $("#"+id).bootstrapTable('mergeCells', {
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
    
    function addRows(id){
    	var count = $("#"+id).bootstrapTable('getData').length;
        var summation = $("#"+id).bootstrapTable('getData');
        var januarys = 0;
        var februarys = 0;
        var marchs = 0;
        var aprils = 0;
        var mays = 0;
        var junes = 0;
        var julys = 0;
        var augusts = 0;
        var septembers = 0;
        var octobers = 0;
        var novembers = 0;
        var decembers = 0;
        for(i = 0; i < count; i++){
        	januarys += summation[i].january;
        	februarys += summation[i].february;
        	marchs += summation[i].march;
        	aprils += summation[i].april;
        	mays += summation[i].may;
        	junes += summation[i].june;
        	julys += summation[i].july;
        	augusts += summation[i].august;
        	septembers += summation[i].september;
        	octobers += summation[i].october;
        	novembers += summation[i].november;
        	decembers += summation[i].december;
        }
        sums = januarys + februarys + marchs + aprils + mays + junes + julys + augusts + septembers + octobers + novembers + decembers;
        if(count != 0){
        	$("#"+id).bootstrapTable('insertRow',{index:count,row:{number:"合计",january:januarys,february:februarys,march:marchs,april:aprils,may:mays,
        		june:junes,july:julys,august:augusts,september:septembers,october:octobers,november:novembers,
        		december:decembers,sum:sums}});
        }
    }
    
	function measure(model){
		$.ajax({
			url: getRootPath() + '/projectlable/changemodel',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				labId: labelId,
				proNo: projNo,
				flag: model
			},
			success: function () {
				
			}
		})
		if(model=="toManual"){
			location.reload(); 
		}else{
			$("#qualityTarget").children("tbody").empty();
			$.ajax({
				url: getRootPath() + '/projectlable/getLabMeasureByProject',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					labId: labelId,
					proNo: projNo,
					flag: "toAuto"
				},
				success: function (data) {
					if(data.data==null||data.data==""){
						$("#qualityTargetTable").css("display","none");
					}else{
						var tbody="<tbody>";
						_.each(data.data, function(tab, index){
							if(index%2==0){
								tbody+="<tr style='background-color:#ffffff;'>";
							}else{
								tbody+="<tr style='background-color:#ecf5ff;'>";
							}
							tbody+="<td style='text-align:left;padding-left:20px;'>" +tab.name+ "</td>" +
							"<td>" +tab.UNIT+ "</td>" +
							"<td>" +tab.UPPER+ "</td>" +
							"<td>" +tab.LOWER+ "</td>" +
							"<td>" +tab.TARGET+ "</td>";
							var flag=true;
							if($.trim(tab.UNIT)=="%"){
								var up=parseFloat(tab.UPPER.substring(0,tab.UPPER.lastIndexOf("%")));
								var low=parseFloat(tab.LOWER.substring(0,tab.LOWER.lastIndexOf("%")));
								if(tab.ACTUALVAL!=null&&tab.ACTUALVAL!=""){
									if(parseFloat(tab.ACTUALVAL*100)>up||parseFloat(tab.ACTUALVAL*100)<low){
										flag=false;
									}
									tab.ACTUALVAL=(tab.ACTUALVAL*100).toFixed(1)+"%";
								}
							}else{
								var up=parseFloat(tab.UPPER);
								var low=parseFloat(tab.LOWER);
								if(tab.ACTUALVAL!=null&&tab.ACTUALVAL!=""){
									if(parseFloat(tab.ACTUALVAL)>up||parseFloat(tab.ACTUALVAL)<low){
										flag=false;
									}
								}
							}       
							if(tab.COLLECTYPE=="手动输入"){
								if(flag==true){
									tbody+="<td class='td' style='padding:0px;'><input class='input' type='hidden' style='width: 100%;height: 30px;border-radius: 5px;border: 0px;' id= " +tab.ID+ " /><div>" +tab.ACTUALVAL+ "</div></td>";
								}else{
									tbody+="<td class='td' style='padding:0px;'><input class='input' type='hidden' style='width: 100%;height: 30px;border-radius: 5px;border: 0px;' id= " +tab.ID+ " /><div style='color: red;'>" +tab.ACTUALVAL+ "</div></td>";
								}
							}else{
								if(flag==true){
									tbody+="<td>" +tab.ACTUALVAL+ "</td>";
								}else{
									tbody+="<td style='color: red;'>" +tab.ACTUALVAL+ "</td>";
								}
							}
							tbody+="<td>" +tab.COLLECTYPE+ "</td>" +
							"<td style='text-align:left;padding-left:20px;'>" +tab.CONTENT+ "</td>" +
							"</tr>";
						});
						tbody+="</tbody>";
						$("#qualityTarget").append(tbody);
					}
				}
			})
		}
	}
	function inputVal(){
		$(".td").click(function(){
			var tis=$(this);
			tis.children("input").attr("type","text");
			tis.children("input").focus();
			tis.children("input").val(tis.children("div").html());
			tis.children("div").css("display","none");
		})
	}
	function saveVal(){
		$(".input").blur(function(){
			var tis=$(this);
			var val=tis.val();
			if(val==null||$.trim(val)==""){
				val="";
			}else{
				if(val.lastIndexOf("%")==-1){//无%
					if(isNaN($.trim(val))){//非数字
						val="";
					}else{
						val=val/100;
					}
				}else{//有%
					val=$.trim(val);
					if(isNaN(val.substring(0,val.lastIndexOf("%")))){//非数字
						val="";
					}else{
						val=val.substring(0,val.lastIndexOf("%"))/100;
					}
				}
			}
				$.ajax({
				url: getRootPath() + '/projectlable/saveActualVal',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					id: tis.attr("id"),
					actualVal: val
				},
				success: function () {
					tis.attr("type","hidden");
					tis.next("div").css("display","");
					if(val!=""){
						tis.next("div").html((val*100).toFixed(1)+"%");
					}else{
						tis.next("div").html("");
					}
				}
			});
		})
	}
	
	/*function search(op1,op2){
		var roles = ""
		if($("#userRole").val()!=null){
			roles = $("#userRole").val().join(',');
		}
		$.ajax({
			url: getRootPath() + '/svnTask/searchGeRenList',
			type: 'post',
			async: false,
			data: {
				projNo: projNo,
				type: op1,
				codeType: op2,
				role: encodeURI(roles)
			},
			success: function (data) {
				$("#gerenCode tr:not(:first)").remove();
				if(data!=null&&data!=""){
					var addNums = 0;
					var tab = "";
					if(!_.isEmpty(data)){
						var chartsData ={
								january:0,
								february:0,
								march:0,
								april:0,
								may:0,
								june:0,
								july:0,
								august:0,
								september:0,
								october:0,
								november:0,
								december:0,
								sum:0
						};
						_.each(data, function(record, key){
							record.sum= record.january
							+record.february+record.march
							+record.april+record.may
							+record.june+record.july
							+record.august+record.september
							+record.october+record.november
							+record.december;
								tab += '<tr><td>'+ record.name +'</td>'
									   +'<td>'+ record.number +'</td>'
									   +'<td>'+ record.role +'</td>'
									   +'<td>'+ (record.january==0?"":record.january) +'</td>'
									   +'<td>'+ (record.february==0?"":record.february) +'</td>'
									   +'<td>'+ (record.march==0?"":record.march) +'</td>'
									   +'<td>'+ (record.april==0?"":record.april) +'</td>'
									   +'<td>'+ (record.may==0?"":record.may) +'</td>'
									   +'<td>'+ (record.june==0?"":record.june) +'</td>'
									   +'<td>'+ (record.july==0?"":record.july) +'</td>'
									   +'<td>'+ (record.august==0?"":record.august) +'</td>'
									   +'<td>'+ (record.september==0?"":record.september) +'</td>'
									   +'<td>'+ (record.october==0?"":record.october) +'</td>'
									   +'<td>'+ (record.november==0?"":record.november) +'</td>'
									   +'<td>'+ (record.december==0?"":record.december) +'</td>'
									   +'<td>'+ record.sum +'</td></tr>';
							chartsData.january+=record.january;
							chartsData.february+=record.february;
							chartsData.march+=record.march;
							chartsData.april+=record.april;
							chartsData.may+=record.may;
							chartsData.june+=record.june;
							chartsData.july+=record.july;
							chartsData.august+=record.august;
							chartsData.september+=record.september;
							chartsData.october+=record.october;
							chartsData.november+=record.november;
							chartsData.december+=record.december;
							chartsData.sum+=record.sum;
						})
					}
					tab += '<tr style="border-bottom: 1px #000 solid;"><td>-</td>'
					   +'<td>合计</td>'+'<td>-</td>'
					   +'<td>'+ chartsData.january +'</td>'
					   +'<td>'+ chartsData.february +'</td>'
					   +'<td>'+ chartsData.march +'</td>'
					   +'<td>'+ chartsData.april +'</td>'
					   +'<td>'+ chartsData.may +'</td>'
					   +'<td>'+ chartsData.june +'</td>'
					   +'<td>'+ chartsData.july +'</td>'
					   +'<td>'+ chartsData.august +'</td>'
					   +'<td>'+ chartsData.september +'</td>'
					   +'<td>'+ chartsData.october +'</td>'
					   +'<td>'+ chartsData.november +'</td>'
					   +'<td>'+ chartsData.december +'</td>'
					   +'<td>'+ chartsData.sum+'</td></tr>';
					$("#gerenCode tbody").append(tab);
				}
				if($("#gerenCode tbody tr").length<6){
					tab = '<tr><td colspan ="16">&nbsp;</td></tr>';
					for (var i = $("#gerenCode tbody tr").length; i < 6; i++) {
						$("#gerenCode tfoot").append(tab);
					}
				}
			}
		});
	};*/
	
	/*function getTestCases(type,testCasesType,role){
		$.ajax({
			url: getRootPath() + '/testMeasures/getTestCases',
			type: 'post',
			async: false,
			data: {
				projNo: projNo,
				type: type,
				role: encodeURI(role)
			},
			success: function (data) {
				$("#"+testCasesType+" tr:not(:first)").remove();
				if(data!=null&&data!=""){
					var addNums = 0;
					var tab = "";
					if(!_.isEmpty(data)){
						var chartsData ={
								january:0,
								february:0,
								march:0,
								april:0,
								may:0,
								june:0,
								july:0,
								august:0,
								september:0,
								october:0,
								november:0,
								december:0,
								sum:0
						};
						_.each(data, function(record, key){
							record.sum= record.january
							+record.february+record.march
							+record.april+record.may
							+record.june+record.july
							+record.august+record.september
							+record.october+record.november
							+record.december;
								tab += '<tr><td date-type="no">'+ record.name +'</td>'
									   +'<td date-type="no">'+ record.number +'</td>'
									   +'<td date-type="no">'+ record.role +'</td>'
									   +'<td date-type="input">'+ (record.january==0?"":record.january) +'</td>'
									   +'<td date-type="input">'+ (record.february==0?"":record.february) +'</td>'
									   +'<td date-type="input">'+ (record.march==0?"":record.march) +'</td>'
									   +'<td date-type="input">'+ (record.april==0?"":record.april) +'</td>'
									   +'<td date-type="input">'+ (record.may==0?"":record.may) +'</td>'
									   +'<td date-type="input">'+ (record.june==0?"":record.june) +'</td>'
									   +'<td date-type="input">'+ (record.july==0?"":record.july) +'</td>'
									   +'<td date-type="input">'+ (record.august==0?"":record.august) +'</td>'
									   +'<td date-type="input">'+ (record.september==0?"":record.september) +'</td>'
									   +'<td date-type="input">'+ (record.october==0?"":record.october) +'</td>'
									   +'<td date-type="input">'+ (record.november==0?"":record.november) +'</td>'
									   +'<td date-type="input">'+ (record.december==0?"":record.december) +'</td>'
									   +'<td date-type="no">'+ record.sum +'</td></tr>';
							chartsData.january+=record.january;
							chartsData.february+=record.february;
							chartsData.march+=record.march;
							chartsData.april+=record.april;
							chartsData.may+=record.may;
							chartsData.june+=record.june;
							chartsData.july+=record.july;
							chartsData.august+=record.august;
							chartsData.september+=record.september;
							chartsData.october+=record.october;
							chartsData.november+=record.november;
							chartsData.december+=record.december;
							chartsData.sum+=record.sum;
						})
					}
					tab += '<tr style="border-bottom: 1px #000 solid;"><td date-type="no">-</td>'
					   +'<td date-type="no">合计</td>'+'<td date-type="no">-</td>'
					   +'<td date-type="no">'+ chartsData.january +'</td>'
					   +'<td date-type="no">'+ chartsData.february +'</td>'
					   +'<td date-type="no">'+ chartsData.march +'</td>'
					   +'<td date-type="no">'+ chartsData.april +'</td>'
					   +'<td date-type="no">'+ chartsData.may +'</td>'
					   +'<td date-type="no">'+ chartsData.june +'</td>'
					   +'<td date-type="no">'+ chartsData.july +'</td>'
					   +'<td date-type="no">'+ chartsData.august +'</td>'
					   +'<td date-type="no">'+ chartsData.september +'</td>'
					   +'<td date-type="no">'+ chartsData.october +'</td>'
					   +'<td date-type="no">'+ chartsData.november +'</td>'
					   +'<td date-type="no">'+ chartsData.december +'</td>'
					   +'<td date-type="no">'+ chartsData.sum+'</td></tr>';
					$("#"+testCasesType+" tbody").append(tab);
				}
				if($("#"+testCasesType+" tbody tr").length<6){
					tab = '<tr><td date-type="no" colspan ="16">&nbsp;</td></tr>';
					for (var i = $("#"+testCasesType+" tbody tr").length; i < 6; i++) {
						$("#"+testCasesType+" tfoot").append(tab);
					}
				}
			}
		});
	};*/
	
	function search(op1,op2){
		var roles = ""
		if($("#userRole").val()!=null){
			roles = $("#userRole").val().join(',');
		}
		$.ajax({
			url: getRootPath() + '/svnTask/monthly1',
			type: 'post',
			async: false,
			data: {
				projNo: projNo,
				type: op1,
				codeType: op2,
				role: encodeURI(roles)
			},
			success: function (data) {
				$('#gerenCode').bootstrapTable('refresh', {url: getRootPath() + '/svnTask/monthly1'});
			}
		});
	};
	
	
	function getTestCases(type,testCasesType,role){
		$.ajax({
			url: getRootPath() + '/testMeasures/get2TestCases',
			type: 'post',
			async: false,
			data: {
				projNo: projNo,
				type: type,
				role: encodeURI(role)
			},
			success: function (data) {
				$('#'+testCasesType).bootstrapTable('refresh', {url: getRootPath() + '/testMeasures/get2TestCases'});
			}
		});
	};
	
	//根据条件统计员工个人代码量、并根据代码类别筛选
	function personalCodeCount(){
		var option2 = "all";
		search(option1,option2);
		$('#gerenCode').bootstrapTable('refresh', {url: getRootPath() + '/svnTask/monthly1'});
	};
	
	function selectconfiguration(str){
	    var arr=str.split(',');
		$("#userRole").selectpicker('val', arr);
		$("#userRole").change(function() {
//			search(option1,"all");
			$('#gerenCode').bootstrapTable('refresh');
		});
		
		$("#submitTestCasesRole").selectpicker('val', arr);
		$("#submitTestCasesRole").change(function() {
			var roles = ""
			if($("#submitTestCasesRole").val()!=null){
				roles = $("#submitTestCasesRole").val().join(',');
			}
			getTestCases("0","submitTestCases",roles);
		});
		
		$("#executeTestCasesRole").selectpicker('val', arr);
		$("#executeTestCasesRole").change(function() {
			var roles = ""
			if($("#executeTestCasesRole").val()!=null){
				roles = $("#executeTestCasesRole").val().join(',');
			}
			getTestCases("1","executeTestCases",roles);
		});
	};
	
	function getTestCaseType(){
		$.ajax({
			url : getRootPath()+'/codeType/codeTypeConfig',
			type: 'post',
			async: false,
			data : {
				no : projNo
			},
			success:function(data){
				_.each(data.data, function(record, index){
					if(record){
						if(record.parameterId==161){
							if(record.type == '1'){
//								$("#edit").css('display','none');
//								$("#edit1").css('display','none');
								isEdit = false;
							}
						}
						if(record.parameterId==160){
							option1=record.type;
							isEdit = true;
						}
					}
				});
				return isEdit;
			}
		});
	}
	
	$(document).ready(function(){
		getTestCaseType();
		
		var str='测试工程师,TC,TSE,TL';
		selectconfiguration(str);
		
		$().initGerenCodeTable();
//		initSubmitTestCasesTable();
//		initExecuteTestCasesTable();
//		personalCodeCount();
//		getTestCases("0","submitTestCases",str);
//		getTestCases("1","executeTestCases",str);
		
		inputVal();
		saveVal();
	})
})

var oldtable;
var oldtable1;
function tableEdit(editid,divid,name) {
	var edit=$("#"+editid);
	edit.css('display','none');
	oldtable=$("#"+name+" tbody").html();
	if(edit.attr("data_obj")=="1"){
		$("#"+divid).css('display','block'); 
		edit.attr("data_obj","0")
	}
	
}
function tableCancel(editid,divid,name) {
	$("#"+name+" tbody").empty();
	$("#"+name+" tbody").append(oldtable);
	var edit=$("#"+editid);
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#"+divid).css('display','none');
}
function tableSave(editid,divid,name) {
	var edit=$("#"+editid);
	if(name=="submitTestCases"){
		tableSaveToDao("submitTestCases","0");
	}else if (name=="executeTestCases"){
		tableSaveToDao("executeTestCases","1");
	}
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#"+divid).css('display','none');
	showPopover("测试用例信息保存成功");
}
function tableSaveToDao(testCases,type) {
	var tab = $('#'+testCases+' tbody');
	var rows = tab[0].rows;
	var allTr = "";
	var zrAccounts=new Array()
	for(var i = 0; i<rows.length; i++ ){
		allTr +='{'+
			'"number":"'+rows[i].cells[1].innerHTML+'",'+
			'"january":"'+isnull2double(rows[i].cells[3].innerHTML)+'",'+
			'"february":"'+isnull2double(rows[i].cells[4].innerHTML)+'",'+
			'"march":"'+isnull2double(rows[i].cells[5].innerHTML)+'",'+
			'"april":"'+isnull2double(rows[i].cells[6].innerHTML)+'",'+
			'"may":"'+isnull2double(rows[i].cells[7].innerHTML)+'",'+
			'"june":"'+isnull2double(rows[i].cells[8].innerHTML)+'",'+
			'"july":"'+isnull2double(rows[i].cells[9].innerHTML)+'",'+
			'"august":"'+isnull2double(rows[i].cells[10].innerHTML)+'",'+
			'"september":"'+isnull2double(rows[i].cells[11].innerHTML)+'",'+
			'"october":"'+isnull2double(rows[i].cells[12].innerHTML)+'",'+
			'"november":"'+isnull2double(rows[i].cells[13].innerHTML)+'",'+
			'"december":"'+isnull2double(rows[i].cells[14].innerHTML)+'"'+
			'},';
	}
	var jsonStr = '{"proNo":"'+projNo+'","type":"'+type+'","gerenCodes":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url : getRootPath() + "/testMeasures/inputTestCases",
		type : 'POST',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data : jsonStr,
		success : function(data) {
			
		}
	})
}

function count(td,testCasesName,newText){
	var tdssel = $("#"+testCasesName+" tbody td:nth-child("+(td[0].cellIndex+1)+")");
	var allbaifenbi = 0.0;
	_.each(tdssel, function(seltd, index){//(值，下标)
		allbaifenbi += parseInt(isnull2double($(seltd).text()));
	});
	allbaifenbi+=parseInt(newText);
	$("#"+testCasesName+" tfoot tr:nth-child(1) td:nth-child("+(td[0].cellIndex-1)+")").text(allbaifenbi);
	
	var tessel = td.parents('tr:first').children('td');
	allbaifenbi = 0.0;
	_.each(tessel, function(seltd, index){//(值，下标)
		if(index>2 && index<15){
			allbaifenbi += parseInt(isnull2double($(seltd).text()));
		}
	});
	allbaifenbi+=parseInt(newText);
	$(tessel[15]).text(allbaifenbi);
	
	tdssel = $("#"+testCasesName+" tbody td:nth-child(16)");
	allbaifenbi = 0.0;
	_.each(tdssel, function(seltd, index){//(值，下标)
		allbaifenbi += parseInt(isnull2double($(seltd).text()));
	});
	$("#"+testCasesName+" tfoot tr:nth-child(1) td:nth-child(14)").text(allbaifenbi);
}

function editable(td){
	if(td[0].childNodes.length>1){
		return;
	}
	var dateType = td.attr("date-type");
	if(dateType=="input"){
		var text = td.text();
		var txt = $("<input class='form-control' maxlength='4' style='width: 100%;height: 30px;' type='text'>").val(text);
	}else if(dateType=="no"){
		return;
	}else{
		return;
//		var text = td.text();
//		var txt = $("<input class='form-control' style='width: 100%;height: 30px;' type='text'>").val(text);
	}
	// 根据表格文本创建文本框 并加入表表中--文本框的样式自己调整
	txt.blur(function(){
		// 失去焦点，保存值。于服务器交互自己再写,最好ajax
		var newText = $(this).val();
		// 移除文本框,显示新值
		$(this).remove();
		if(dateType=="input"){
			newText = isnotnum2num(newText);
			if(td.parents("#submitTestCases").length>0){
				count(td,"submitTestCases",newText);
			}else{
				count(td,"executeTestCases",newText);
			}
			if(newText==0){
				newText = "";
			}
		}
		td.text(newText);
	});
	td.text("");
	td.append(txt);
	$(txt).focus();
	if(dateType=="input"){
		$(txt).select();
	}
}

//$(document).on("dblclick", "#guanjianjuese td", function () {
$(document).on("click", "#submitTestCases td", function () {
	if($("#edit").attr("data_obj")=="1"){
		return;
	}
    var td = $(this);
    editable(td);
	
});
$(document).on("click", "#executeTestCases td", function () {
	if($("#edit1").attr("data_obj")=="1"){
		return;
	}
	var td = $(this);
	editable(td);
	
});

function showPopover(msg) {
	$('#dataAcquisition').text(msg);
	$('#submitImportmodalfooter').css('display','none');
	$('#savetoop').modal('show');
	//2秒后消失提示框
	var id = setTimeout(function () {
		$('#savetoop').modal('hide');
	}, 2000);
}

function isnotnum2num(str) {
	var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
	if(!re.test(str)){
		return "0";
	}
	var i = parseInt(str);
	if(i>9999){
		i=9999;
	}
	return i;
}
function isnull2double(str) {
	str = (str==null||str=="")?"0":str;
	str=str.replace("%","");
	var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
	if(!re.test(str)){
		str = "0"
	}
	return str;
}

//自动化开发效率页面
$(window).resize(function() {
	$("#tab-zdhkfxl").css({"min-height":$(window).height()-55});
});

$(document).ready(function(){
	$("#tab-zdhkfxl").css({"min-height":$(window).height()-55});
});

//用例设计页面
$(window).resize(function() {
	$("#tab-ylsj").css({"min-height":$(window).height()-55});
});

$(document).ready(function(){
	$("#tab-ylsj").css({"min-height":$(window).height()-55});
});

//用例执行数页面
$(window).resize(function() {
	$("#tab-ylzxs").css({"min-height":$(window).height()-55});
});

$(document).ready(function(){
	$("#tab-ylzxs").css({"min-height":$(window).height()-55});
});

function changeTest(name){
	if(name=="zdhkfxl"){
		$("#tab-zdhkfxl").css("display","");
		if(zdhkfxl==0){
			$().initGerenCodeTable();
		}else{
			$('#gerenCode').bootstrapTable('refresh');
		}
		$("#tab-ylsj").css("display","none");
		$("#tab-ylzxs").css("display","none");
	}else if(name=="ylsj"){
		$("#tab-ylsj").css("display","");
		if(ylsj==0){
			$().initSubmitTestCasesTable();
		}else{
			$('#submitTestCases').bootstrapTable('refresh');
		}
		$("#tab-ylzxs").css("display","none");
		$("#tab-zdhkfxl").css("display","none");
	}else if(name=="ylzxs"){
		$("#tab-ylzxs").css("display","");
		if(ylzxs==0){
			$().initExecuteTestCasesTable();
		}else{
			$('#executeTestCases').bootstrapTable('refresh');
		}
		$("#tab-ylsj").css("display","none");
		$("#tab-zdhkfxl").css("display","none");
	}
}

function getShow(mes,value) {
    if(value == 0){
    	mes = "--";
    }
    var show = '<a class="tooltip-test" data-toggle="tooltip"' +
        ' data-placement="auto bottom" data-html="true" title="' + mes + '" >'
        + value + '</a>';

    return show;
}
