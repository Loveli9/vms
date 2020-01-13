var weekList  = new Array(7);
var weekList1  = new Array(7);
var weekList2  = new Array(7);
var weekList3  = new Array(7);
var weekList4  = new Array(7);
var weekList5  = new Array(7);
var weekListGr  = new Array(7);
var weekListGr1  = new Array(7);
var weekListGr2  = new Array(7);
var weekListGr3  = new Array(7);
var weekListGr4  = new Array(7);
var weekListGr5  = new Array(7);
var day = new Array(7);
var zrAccount = "";
var proNo = getQueryString("projNo");
$(function(){
	$(document).ready(function(){
		initCostMonth('project');
		initCostWeek('project');
		tabXmcb("project");
	})
});
//出勤
function tabXmcb(flag){
	var week = 'person' == flag ? '#weekListPer option:selected' : '#weekListPro option:selected';
	
	if("0" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr : weekList;
	}else if("1" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr1 : weekList1;
	}else if("2" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr2 : weekList2;
	}else if("3" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr3 : weekList3;
	}else if("4" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr4 : weekList4;
	}else if("5" == $(week).attr('index')){
		day = 'person' == flag ? weekListGr5 : weekList5;
	}
	var table = 'person' == flag ? '#grcbTable' : '#xmcbTable';

	$(table).bootstrapTable('destroy');
	$(table).bootstrapTable({
    	method: 'GET',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/GeneralSituation/projectCost',
    	striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 10, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
		queryParams : function(params){
			var param = {
					'limit': params.limit,
    				'offset': params.offset,
					'projNo' : proNo,
					'statisticalTime' : day[0],
					'nextTime' : day[6],
					'flag' : 'person' == flag ? ('1' == getCookie('zrOrhwselect') ? getCookie('username') : getZRAccount()) : ''
				}
			return param;
		},
		onEditableSave: function (field, row, oldValue, $el) {
        	row.no = proNo;
        	row.statisticalTime = $(week).attr('label');
        	if("attendenceFirst" == field || "overtimeFirst" == field){
        		row.day = 0;
        	}else if("attendenceSecond" == field || "overtimeSecond" == field){
        		row.day = 1;
        	}else if("attendenceThird" == field || "overtimeThird" == field){
        		row.day = 2;
        	}else if("attendenceFourth" == field || "overtimeFourth" == field){
        		row.day = 3;
        	}else if("attendenceFifth" == field || "overtimeFifth" == field){
        		row.day = 4;
        	}else if("attendenceSixth" == field || "overtimeSixth" == field){
        		row.day = 5;
        	}else if("attendenceSeventh" == field || "overtimeSeventh" == field){
        		row.day = 6;
        	}
            $.ajax({
                type: "post",
                url: getRootPath()+"/GeneralSituation/updateMemberCost",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', // 设置请求头信息
 				data: JSON.stringify(row),
                success: function (data, status) {
                    if (data.code == "success") {
                    	var pageNumber = $(table).bootstrapTable('getOptions').pageNumber;
                    	$(table).bootstrapTable('refresh',{pageNumber : pageNumber});
                    	toastr.success('修改成功！');
                    }else{
                    	toastr.success('修改失败！');
                    }
                }
            });
        },
		columns:[
        	[	
	    	 	{title : '序号',align: "center",valign:'middle',width:'3.5%',rowspan: 2,colspan: 1,
	    			formatter: function (value, row, index) {
	    				var pageSize=$(table).bootstrapTable('getOptions').pageSize;  
	    				var pageNumber=$(table).bootstrapTable('getOptions').pageNumber;
	    				return pageSize * (pageNumber - 1) + index + 1;
	    			}
	    		},
	    		{title:'姓名',field:'name',align: "center",valign:'middle',width:'6.5%',rowspan: 2, colspan: 1},
	    		{title:'中软工号',field:'zrAccount',align: 'center',valign:'middle',width:'8%',rowspan: 2, colspan: 1},
	    		{title:'华为工号',field:'hwAccount',align: 'center',valign:'middle',width:'8%',rowspan: 2, colspan: 1},
	    		{title:'星期一<br/>('+day[0]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[0]+'\', 0, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[0]+'\', 0, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期二<br/>('+day[1]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[1]+'\', 1, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[1]+'\', 1, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期三<br/>('+day[2]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[2]+'\', 2, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[2]+'\', 2, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期四<br/>('+day[3]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[3]+'\', 3, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[3]+'\', 3, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期五<br/>('+day[4]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[4]+'\', 4, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[4]+'\', 4, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期六<br/>('+day[5]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[5]+'\', 5, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[5]+'\', 5, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
    			{title:'星期日<br/>('+day[6]+')'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: right; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[6]+'\', 6, \''+"cz"+'\', \''+flag+'\')">重置</button>'+'<button style="background-color:#239CD2; color:white; border-radius:5px; float: left; width: 46%;"'+
	    			'onclick="editHourByDay(\''+day[6]+'\', 6, \''+"qq"+'\', \''+flag+'\')">全勤</button>'
	    			,align: 'center',valign:'middle',width:'9%',rowspan: 1, colspan: 2},
	    		{title:'合计',field:'totalHours',align: "center",valign:'middle',width:'4.5%',rowspan: 2, colspan: 1,
                    formatter:function(val, row){
        				return formatMemberHour(val);
        			}
	    		},
	    		{title:'操作',align: 'center',valign:'middle',width:'8.5%',rowspan: 2, colspan: 1,
	    			formatter:function(value,row,index){ 
						return '<button style="background-color:#239CD2; color:white; border-radius:5px; margin-left: -6px;" onclick="editHourByZrAccount(\'' + row.zrAccount + '\', \''+"qq"+'\', \''+flag+'\')">全勤</button>'
						+ '<button style="background-color:#239CD2; color:white; border-radius:5px; margin-left: 5px;margin-right: 5px" onclick="editHourByZrAccount(\'' + row.zrAccount + '\', \''+"cz"+'\', \''+flag+'\')">重置</button>';
					}
	    		}
        	],
        	[
        		{title : '出勤<br/>(小时)',field : 'attendenceFirst',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text",
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[0]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                       	 	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeFirst',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text",
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[0]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceSecond',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[1]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeSecond',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[1]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceThird',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[2]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeThird',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[2]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceFourth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[3]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeFourth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[3]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceFifth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[4]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                       	 	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeFifth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[4]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceSixth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[5]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                       	 	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeSixth',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[5]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '出勤<br/>(小时)',field : 'attendenceSeventh',align: 'center',valign:'middle',width : '6%',
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[6]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                       	 	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		},
        		{title : '加班<br/>(小时)',field : 'overtimeSeventh',align: 'center',valign:'middle',width : '6%', 
        			editable: {
                        type: "text", 
                        emptytext:'&#12288',
                        disabled: limitHourInput(day[6]),
                        placement: 'bottom',
                        validate: function (v) {
                        	if(! /^\d*\.{0,1}\d{0,1}$/.test(v)){
                         		return '工时只能有1位小数点！';
                         	}
                        	return limitWorkHour(v);
                       }
                    },
                    formatter:function(val, row){
                    	return formatMemberHour(val);
        			}
        		}
    		]
    	],
	locale:'zh-CN'//中文支持
	});
};

var page = 0 ;
if(parent.parents){
	page = parent.parents;
	parent.parents = null;
}
function initCostMonth(flag) {
	var startDate = "";
	var month = 'person' == flag ? '#monthListPer' : '#monthListPro';
	$.ajax({
		url : getRootPath() + "/bu/projOverviewData",
		type : 'POST',
		async: false,//是否异步，true为异步
		data : {
			no : proNo
		},
		success : function(data) {
			startDate = new Date(data.startDate).format('yyyy-MM-dd').toString();
		}
	});
	$.ajax({
		url: getRootPath() + '/measureComment/getCostMonth',
		type: 'post',
		data : {
			startDate : startDate
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			$(month).empty();
			data = data.data;
			if( null == data || "" == data ) return;
			var select_option="";
			var length = data.length;
			for(var i = 0; i < length; i++){
				select_option += "<option value='"+data[i]+"'";
				if( page == i ){
					select_option += "selected='selected'";
				} 
				select_option += ">"+data[i]+"</option>";
			}
			
			$("#currentTime").html("当前月份:"+data[0]);
			$(month).html(select_option);
            $(month).selectpicker('refresh');
            $(month).selectpicker('render');
		}
	});
};

function initCostWeek(flag) {
	var month = 'person' == flag ? '#monthListPer' : '#monthListPro';
	var week = 'person' == flag ? '#weekListPer' : '#weekListPro';
	
	$.ajax({
		url: getRootPath() + '/measureComment/getCostWeek',
		type: 'post',
		data : {
			selectDate : $(month).val(),
			num : 6
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			$(week).empty();
			data = data.data;
			if( null == data || "" == data ) return;
			var select_option="";
			var length = data.length;
			for(var i = 0; i < length; i++){
				select_option += "<option value='"+data[i].week+"' label='"+data[i].cycle+"' index='"+i+"'";
				if( page == i ){
					select_option += "selected='selected'";
				} 
				select_option += ">"+data[i].week+"</option>";
			}
			
			for(var i = 0; i < 7; i++){
				if(data[0]){
					if('person' == flag){
						weekListGr[i] = data[0].day[i];
					}else{
						weekList[i] = data[0].day[i];
					}
				}
				if(data[1]){
					if('person' == flag){
						weekListGr1[i] = data[1].day[i];
					}else{
						weekList1[i] = data[1].day[i];
					}
				}
				if(data[2]){
					if('person' == flag){
						weekListGr2[i] = data[2].day[i];
					}else{
						weekList2[i] = data[2].day[i];
					}
				}
				if(data[3]){
					if('person' == flag){
						weekListGr3[i] = data[3].day[i];
					}else{
						weekList3[i] = data[3].day[i];
					}
				}
				if(data[4]){
					if('person' == flag){
						weekListGr4[i] = data[4].day[i];
					}else{
						weekList4[i] = data[4].day[i];
					}
				}
				if(data[5]){
					if('person' == flag){
						weekListGr5[i] = data[5].day[i];
					}else{
						weekList5[i] = data[5].day[i];
					}
				}
			}
			
			$("#currentTime").html("当前周:"+data[0].week);
			$(week).html(select_option);
            $(week).selectpicker('refresh');
            $(week).selectpicker('render');
		}
	});
};

function selectMonthchange(flag){
	initCostWeek(flag);
	tabXmcb(flag);
}

function selectWeekchange(flag){
	tabXmcb(flag);
}

function formatMemberHour(val){	
	if(val.toString().split(".").length == 1 && val != 0){
		return val.toString()+".0";
	}else if(val == 0){
		return '';
	}else{
		return val;
	}
}

function limitWorkHour(v){
	if(parseFloat(v) < 0 || parseFloat(v) > 24){
		return '工时只能输入0-24小时！';
	}
}

function limitHourInput(v){
	return (new Date(v).getTime() - new Date($("#startDate").text()).getTime() < 0) ||
	(new Date(v).getTime() - new Date($("#endDate").text()).getTime() > 0) ? true : false;
}

function editHourByDay(day, week, mark, flag){
	var msg = hourTip(day) ? "本周部分工时日期不在项目开始时间和结束时间内！" : '';
	
	$.ajax({
		url : getRootPath() + '/GeneralSituation/updateHourByDay',
		type : 'post',
		data : {
			'day' : day,
			'week' : week,
			'projNo' : proNo,
			'mark' : mark,
			'flag' : 'person' == flag ? ('1' == getCookie('zrOrhwselect') ? getCookie('username') : getZRAccount()) : ''
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			if (data.code == "success") {
				if('' == msg){
					toastr.success('修改成功!');
				}else{
					toastr.warning('修改成功, ' + msg);
				}
				var table = 'person' == flag ? '#grcbTable' : '#xmcbTable';
				$(table).bootstrapTable('refresh', {pageNumber:1});
			} else {
				toastr.error('修改失败！');
			}
		}
	});
}

function hourTip(date){
	return (new Date(date).getTime() - new Date($("#startDate").text()).getTime() < 0) || 
	(new Date(date).getTime() - new Date($("#endDate").text()).getTime() > 0) ? true : false;
}

function editHourByZrAccount(zrAccount, mark, flag){
	var msg = (hourTip(day[0]) || hourTip(day[4])) ? "本周部分工时日期不在项目开始时间和结束时间内！" : '';
	
	$.ajax({
		url : getRootPath() + '/GeneralSituation/updateHourByZrAccount',
		type : 'post',
		traditional:true,
		data : {
			'zrAccount' : zrAccount,
			'dayArr' : day,
			'projNo' : proNo,
			'mark' : mark
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			if (data.code == "success") {
				if('' == msg){
					toastr.success('修改成功!');
				}else{
					toastr.warning('修改成功, ' + msg);
				}
				var table = 'person' == flag ? '#grcbTable' : '#xmcbTable';
				$(table).bootstrapTable('refresh', {pageNumber:1});
			} else {
				toastr.error('修改失败！');
			}
		}
	});
}

function getZRAccount(){
	$.ajax({
		url : getRootPath() + '/GeneralSituation/getPMZRAccountByHW',
		type : 'post',
		traditional:true,
		data : {
			'hwAccount' : getCookie('username')
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			zrAccount = data;
		}
	});
	return zrAccount;
}
function organizationalStructure(){
	$('#majorDuty').bootstrapTable({
		method: 'post',
		contentType: "application/x-www-form-urlencoded",
		url:getRootPath() + '/GeneralSituation/organizationalStructure',
		editable:true,// 可行内编辑
		striped: false, // 是否显示行间隔色
		dataField: "rows",
		pageNumber: 1, // 初始化加载第一页，默认第一页
		pagination:true,// 是否分页
		queryParamsType:'limit',
		sidePagination:'client',
		pageSize:5,// 单页记录数
		pageList:[5,10,20,30],// 分页步进值
		showColumns:false,
		// toolbar:'#toolbar',// 指定工作栏
		toolbarAlign:'right',
		dataType: "json",
		queryParams : function(params){
			var param = {
				projNo : proNo,
			}
			return param;
		},
		onEditableSave: function (field, row, oldValue, $el) {
			$.ajax({
				type: "post",
				url: getRootPath()+"/GeneralSituation/demandUpdata",
				dataType: "json",
				async: false,
				data: {
					Id : row.id,
					value : row.demand,
					no: proNo
				},
				success: function (data, status) {
					if (data.code == "success") {
						var pageNumber=$('#majorDuty').bootstrapTable('getOptions').pageNumber;
						$('#majorDuty').bootstrapTable('refresh',{pageNumber:pageNumber});
						toastr.success('修改成功！');
					}else{
						toastr.success('修改失败！');
					}
				}
			});
		},
		columns:[
			{
				title:'岗位',
				halign :'center',
				align : 'center',
				valign: 'middle',
				field:'position',
				sortable:true,
				width:120,
			},
			{
				title:'主要职责',
				halign :'center',
				align : 'center',
				valign: 'middle',
				field:'name',
				sortable:true,
				width:250,
			},
			{
				title:'在岗',
				halign :'center',
				align : 'center',
				valign: 'middle',
				field:'onDuty',
				width:70,
				sortable:true,
			},
			{
				title:'需求',
				halign :'center',
				align : 'center',
				field:'demand',
				valign: 'middle',
				width:70,
				sortable:true,
				editable: {
					type: "text",              //编辑框的类型。支持text|textarea|select|date|checklist等
					emptytext:'&#12288',
					placement: 'bottom',      //编辑框的模式：支持popup和inline两种模式，默认是popup
					noeditFormatter: function (value,row,index) {
						return '<a href="javascript:void(0)" style="padding-top: 0px;" data-name="demand" data-pk="undefined" data-value="'+value+'" class="editable editable-click">'+value+'</a>';
					},
				}
			},
			{
				title:'操作',
				halign :'center',
				align : 'center',
				valign: 'middle',
				field:'opera',
				width: 100,
				//'修改,删除'响应id列
				formatter:function(value,row,index){
					return '<a href="#" style="color:blue" onclick="delDemandPopulation(\'' + row.id + '\')">' + '删除' + '</a>';
				}
			}
		],
		locale:'zh-CN',// 中文支持,
	});
}
