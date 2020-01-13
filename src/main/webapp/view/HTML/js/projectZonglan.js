var num = 0;
var measureid = 223;//首页质量默认
var kexinMeasureId = 114;//首页可信质量默认
//var projectState = "在行";
var projectStatus = 0;//首页总览默认
var measure = 0;//指标默认
var temp = null;
var monthList = new Array(6);
var reliableMonthList = new Array(3);
var echars1 = new Array(6);
var echarsReliable = new Array(3);
var lastmonth1= new Array(6);
var lashtmonthReliable = new Array(3);
var lastmonth2= new Array(6);
var no=new Array(9);
var data1;
var actualTableSave = null;
var screen = true;
var screenSave = function (){//筛选按钮
	if(screen){
		$("#shaixuan").show();
		$("#screen").attr("class", "glyphicon glyphicon-triangle-top");
		screen = false;
	}else{
		$("#shaixuan").hide();
		$("#screen").attr("class", "glyphicon glyphicon-triangle-bottom");
		screen = true;
	}
}

$(function() {
	$("#quality").hide();
	var myDate = new Date();
	var month = myDate.getMonth() + 1;
	var year = myDate.getFullYear();
	var day=myDate.getDate();
	if(day<16){
		month=month-1;
		if(month==0){
			month=12;
		}
	}	
	temp = year+"年"+month+"月";	
	var bsTable;
    var mode1 = "popup"//编辑框的模式：支持popup和inline两种模式，默认是popup
    $("#dmzl").attr("style","display:none;");
    
	$(".state .tor1").on('click',function(){
		num = $(this).index();
		$(this).addClass("curent").siblings().removeClass("curent");
		averageBrokenline(num);
	})
	
	$(".ztstate .xiebian").on('click',function(){
		var menu = $(this).index();
		$(this).addClass("navigation").siblings().removeClass("navigation");
		if(menu==0){
			actual = 0;
			$("#estimate").hide();
			$("#actual").show();
			$("#quality").hide();
			$("#zongLan").show();
			$("#zhiLiang").hide();
			echars();
			Zonglanechars();
			initZongLanTableSave();
		}else if(menu==5){
			actual = 1;
			$("#actual").hide();
			$("#estimate").show();
			$("#quality").hide();
			$("#zongLan").show();
			$("#zhiLiang").hide();
			average();
			averageBrokenline(0);
		}else if(menu==1){
//			quailtyechars();
			queryTable("/projectReview/queryQualityState","163");//质量
			actual = 2;
			$("#actual").hide();
			$("#estimate").hide();
			$("#quality").show();
			$("#zhiLiang").show();
			$("#zongLan").hide();
			initZhiLiangTableSave("163");
		}else if(menu==6){
//			quailtyechars();
			queryTable("/projectReview/queryKXqualityState","164");//可信质量
			actual = 3;
			$("#actual").hide();
			$("#estimate").hide();
			$("#quality").show();
			$("#zhiLiang").show();
			$("#zongLan").hide();
			initZhiLiangTableSave("164");
		}
	})
	
	echars();
	Zonglanechars();
	
	function initZongLanTableSave(){
		$("#zongLanTable").bootstrapTable('destroy');
        var columns=initZongLanColumn();
        var queryParams = {
        	'statisticalTime':nowDate,
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
    		'projectStatus':projectStatus
        };
        var url = getRootPath() + '/projectOverview/getZongLanTopProject';
        bsTable = new BSTable('zongLanTable', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(queryParams);
        bsTable.setPageSize(5);
        bsTable.setPageNumber(1);
        bsTable.setEditable(false);
        bsTable.init();
        getTopProjectTitle();
	}
	
	actualTableSave = function(val){
		$("#zongLanTable").bootstrapTable('destroy');
        var columns=initZongLanColumn();
        var queryParams = {
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
            'number':val,
        };
        var url = getRootPath() + '/projectReview/actualTableSave';
        bsTable = new BSTable('zongLanTable', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(queryParams);
        bsTable.setPageSize(5);
        bsTable.setPageNumber(1);
        bsTable.setEditable(false);
        bsTable.init();
	}
	
	function initZongLanColumn(){
        return [
            {title:'项目编号 ',field:'no',width:80,visible: false},
            {title:'',field:'name',width:120,
                formatter:function(value,row,index){
                    var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
                    var pduSpdt = row.pduSpdt==null?'/':('/'+row.pduSpdt);
                    var tab = '<a target="_blank" style="color: #000;" title="'+value +'" href="'+path1+'">' +
			                    '<div style="width: 100%;text-align: left;">' +
			                    	'<label style="font-size: 12px;font-weight: unset;cursor: pointer;">' +
			                        	''+value+
			                        '</label>' +
			                        getProgress('name',row.statusAssessment)+
			                        '<div style="font-size: 12px;color: #000;margin-top:-15px;">' +
			                        	'<div style="width: 90%;">' +
			                            	'<sqan>'+row.hwzpdu+pduSpdt+'</sqan>' +
			                            '</div>' +
			                        '</div>' +
			                  '</div>' +
			                 '</a>';
                   return tab;
                },
            },
            {title:'',field:'statusAssessment',width:54,
            	formatter:function(value,row,index){
                    evaluation = getEvaluation('statusAssessment',value);
                    evaluation =  '<div style="width:100%;height:15px;margin-left:5px;">'+
		                		'<span >'+evaluation+'</span>'+
		                	'</div>';
                    return evaluation;
                }
            }
        ]
    };
    
    $.fn.loadZongLanTop=function(){
        var queryParams = {
        	'statisticalTime':nowDate,
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'number':num,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
    		'projectStatus':projectStatus
        };
        bsTable.setQueryParams(queryParams);
        $("#zongLanTable").bootstrapTable('destroy');//先要将table销毁，否则会保留上次加载的内容
        bsTable.init();
        getTopProjectTitle();
	}
    
    function zongLancolor(val){
    	var color = null;
    	if(val == "正常"){
    		color = "#00B050";
    	}else if(val == "预警"){
    		color = "#FFC000";
    	}else if(val == "风险"){
    		color = "#c00000";
    	}else if(val == "未评估"){
    		color = "#bdb7b7";
    	}
    	return color;
    }
    
    function getEvaluation(val,val1){
		evaluation = '<div style="border-radius: 50px;width:90px;height:42px;border:2px solid '+zongLancolor(getLamp(val,val1).text)+';color:'+zongLancolor(getLamp(val,val1).text)+';font-size:14px;line-height:36px;font-weight:600;padding-top:2px;'+
        'float: right;margin: 0% 11%;">'+val1+'</div>';
		return evaluation;
	}
	
	function getProgress(val,val1){
		progress = '<div style="width: 100%;height: 6px;background-color: #FFFFFF;"class="progress">'+
        '<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'+' style="width:'+toPercent(val1)+';background-color: '+zongLancolor(getLamp(val,val1).text)+'">'+
        '<span class="sr-only"></span>'+
        '</div>'+
		'</div>'
		return progress;
	}
	
	function toPercent(point){
        if (point==0) {
            return 0;
        }
        var str=Number(point).toFixed();
        str+="%";
        return str;
    }
	
	function getTopProjectTitle(){
		var title = "Top项目"+"("+temp+")";
		$("#zongLanTitle").html(title);
		$("#zhiLiangTitle").html(title);
	}
	
	//质量
	function initZhiLiangTableSave(measureMark){
		$("#zhiLiangTable").bootstrapTable('destroy');
        var columns=initZhiLiangColumn();
        var queryParams = {
        	'statisticalTime':nowDate,
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
    		'measure':measureid,
    		'measureMark':measureMark
        };
        var url = getRootPath() + '/projectOverview/getZhiLiangTopProject';
        bsTable = new BSTable('zhiLiangTable', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(queryParams);
        bsTable.setPageSize(5);
        bsTable.setPageNumber(1);
        // bsTable.setEditable(true);//开启行内编辑
        bsTable.setEditable(false);
        bsTable.init();
        getTopProjectTitle();
	}
	
	function initZhiLiangColumn(){
        return [
            {title:'项目编号 ',field:'no',width:80,visible: false},
            {title:'',field:'projectName',width:120,
                formatter:function(value,row,index){
                    var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
                    var pduSpdt = row.pduSpdt==null?'/':('/'+row.pduSpdt);
                    var tab = '<a target="_blank" style="color: #000;;" title="'+value +'" href="'+path1+'">' +
			                    '<div style="width: 100%;text-align: left;">' +
			                    	'<label style="font-size: 12px;font-weight: unset;cursor: pointer;">' +
			                        	''+value+
			                        '</label>' +
			                        getZhiLiangProgress(row.upper,row.lower,row.value,row.color)+
			                        '<div style="font-size: 12px;color: #000;margin-top:-15px;">' +
			                        	'<div style="width: 90%;">' +
			                        		'<a href="#" onclick="PageJump('+row.clientType+','+row.businessId+','+row.causeId+','+null+')" style="color:#999;display:none;">'+ row.businessUnit +'</a>' +
			                            	'<sqan>'+row.hwzpdu+pduSpdt+'</sqan>' +
			                            '</div>' +
			                        '</div>' +
			                  '</div>' +
			                 '</a>';
                   return tab;
                },
            },
            {title:'',field:'value',width:54,
            	formatter:function(value,row,index){
                    zhiLiangEvaluation = getZhiLiangEvaluation(value,row.color);
                    zhiLiangEvaluation =  '<div style="width:100%;height:15px;margin-left:5px;">'+
		                		'<span >'+zhiLiangEvaluation+'</span>'+
		                	'</div>';
                    return zhiLiangEvaluation;
                }
            }
        ]
    };
    
    $.fn.loadZhiLiangTop=function(measureMark){
        var queryParams = {
        	'statisticalTime':nowDate,
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
    		'measure':measureid,
    		'measureMark':measureMark
        };
        bsTable.setQueryParams(queryParams);
        $("#zhiLiangTable").bootstrapTable('destroy');//先要将table销毁，否则会保留上次加载的内容
        bsTable.init();
        getTopProjectTitle();
	}
	
    function getZhiLiangProgress(val,val1,val2,val3){
		progress = '<div style="width: 100%;height: 6px;background-color: #FFFFFF;"class="progress">'+
        '<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'+' style="width:'+zhiLiangPercent(val,val1,val2)+';background-color: '+getColor(val3)+'">'+
        '<span class="sr-only"></span>'+
        '</div>'+
		'</div>'
		return progress;
    }
    
    function getColor(val){
    	var color = null;
		if(val.indexOf("red") > -1){
    		color = "#c00000";
    	}else if(val.indexOf("yellow") > -1){
    		color = "#FFC000";
    	}else if(val.indexOf("green") > -1){
    		color = "#00B050";
    	}
    	return color;
    }
    
    function zhiLiangPercent(val,val1,val2){
        if (val2 == 0) {
            return 0;
        }
        var str=Number(((val2-val1)/(val-val1))*100).toFixed();
        str += "%";
        return str;
    }
    
    function getZhiLiangEvaluation(val,val1){
    	zhiLiangEvaluation = '<div style="border-radius: 50px;width:90px;height:42px;border:2px solid '+getColor(val1)+';color:'+getColor(val1)+';font-size:14px;line-height:36px;font-weight:600;padding-top:2px;'+
        'float: right;margin: 0% 11%;">'+val+'</div>';
    	return zhiLiangEvaluation;
    }
    
    $(document).ready(function(){
    	initCurrentDate();
    	initZongLanTableSave();
    	
    	verifyZrAccount(getCookie('username'), getCookie('Currentpername'), getCookie('zrOrhwselect'));
    	
    })
    $.ajax({
        type: "post",
        url: getRootPath() + '/login/visit',
        dataType: "json",
        success: function (data) {
        	var rs = data.data;
        	if (rs) {
               $("#totalNum").html(rs.totalNum);
               $("#current").html("本月浏览量(累计)&nbsp;&nbsp;&nbsp;"+rs.currentNum);
               $("#lastMonth").html("比上月增加&nbsp;&nbsp;&nbsp;"+rs.growRate+"%");
            } else {
                toastr.success('修改失败！');
            }
        }
    });
    
    $.ajax({
        type: "post",
        url: getRootPath() + '/projects/inLine',
        dataType: "json",
        success: function (data) {
        	var rs = data.data;
        	if (rs) {
               $("#configProject").html("<a class='release' onclick=toProjectHome()>"+rs.inLinePro+"</a>");
               $("#lineProjects").html("上月立项项目数&nbsp;&nbsp;&nbsp;"+rs.lxNum);
               $("#totalProject").html("当月结项项目数&nbsp;&nbsp;&nbsp;"+rs.jxNum);
            } else {
                toastr.success('修改失败！');
            }
        }
    });
    
    $.ajax({
        type: "post",
        url: getRootPath() + '/projects/releasePro',
        dataType: "json",
        success: function (data) {
        	var rs = data.data;
        	if (rs) {
               $("#doneProject").html("<a class='release' onclick=newReport()>"+rs.excute+"</a>");
               $("#outProjects").html("未采集项目数&nbsp;&nbsp;&nbsp;"+rs.noexcutePro);
               $("#addProjects").html("重点项目数&nbsp;&nbsp;&nbsp;"+rs.inline);
            } else {
                toastr.success('修改失败！');
            }
        }
    });
})
var actual = 0;

function selectOnchange(){
	if(actual==1){
		average();
		averageBrokenline(0);
	}else if(actual==2){	
		queryTable("/projectReview/queryQualityState","163");
//		quailtyechars();		
		$().loadZhiLiangTop("163");
	}else if(actual==3){
		queryTable("/projectReview/queryKXqualityState","164");
//		quailtyechars();		
		$().loadZhiLiangTop("164")
	}else {
		echars();
		Zonglanechars();
		$().loadZongLanTop();
	}
}

//加载当前日期	
function initCurrentDate() {
	$.ajax({
		url: getRootPath() + '/projectOverview/getTime',
		type: 'post',
		async: false,//是否异步，true为异步
		success: function (data) {
			nowDate = data.data;
		}
	});
};

function HomePageJump(val){
	if(val==1){
		newReport1();
	}else if(val==2){ 
		proManage(0);
	}
}

function newReport1(){
	window.parent.document.getElementById("report").onmouseover();
	window.parent.document.getElementById("zonglan").click();
	parent.parents = 1;
}

function echars() {
	var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'number':num,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
    };	
	$.ajax({
		url : getRootPath() + '/projectReview/queryProjectState',							
		type : "post",
		dataType : "json",
		cache : false,
		data:queryParams,	
		success : function(data){	
			$("#actual").empty();
				for(var i=0;i<data.length;i++){
					if(i==0){
						var tab='<div id="'+data[i].id+'" class="top1 curent">'+
									'<div style="font-size:15px;" class="top-child">'+data[i].name+'</div>'+
									'<div style="font-size:27px;" class="top-child">'+data[i].nowmonth+'</div>'+
									'<div style="margin-bottom: -5px;" class="top-child">'+
										'<span>'+"比上一个月:"+'</span>'+
										'<span>'+data[i].lastmonth+'</span>'+
									'</div>'+
									'<div class="top-child">'+
										'<span>'+"比上一个周期:"+'</span>'+
										'<span>'+data[i].green+'</span> '+
									'</div>'+
								'</div>';
					}else{
						var tab='<div id="'+data[i].id+'" class="top1">'+
							'<div style="font-size:15px;" class="top-child">'+data[i].name+'</div>'+
							'<div style="font-size:27px;" class="top-child">'+data[i].nowmonth+'</div>'+
							'<div style="margin-bottom: -5px;" class="top-child">'+
								'<span>'+"比上一个月:"+'</span>'+
								'<span>'+data[i].lastmonth+'</span>'+
							'</div>'+
							'<div class="top-child">'+
								'<span>'+"比上一个周期:"+'</span>'+
								'<span>'+data[i].green+'</span> '+
							'</div>'+
						'</div>';					
					}
					$("#actual").append(tab);	
			}
			$(".state .top1").on('click',function(){
				num = $(this).index();
				$(this).addClass("curent").siblings().removeClass("curent");
				projectStatus=num;
				Zonglanechars();
				$().loadZongLanTop();
			})
		}
	});
}

function Zonglanechars() {
	var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'number':num,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
    };	
	$.ajax({
		url : getRootPath() + '/projectReview/queryZonglanEcahrs',							
		type : "post",
		dataType : "json",
		cache : false,
		data:queryParams,	
		success : function(data){
			var monthList1=data.monthList;
			var echars=data.echars;
			var lastmonth=data.lastMonth;
			var day=monthList1[0].substring(3,5);
			document.getElementById('time').innerHTML = "统计截止日期"+'('+temp+day+"日"+')';
			for (var i = 0; i < 6; i++) {
					echars1[5 - i] = echars[i]; 
					monthList[5-i]=monthList1[i];
					lastmonth1[5-i]=lastmonth[i];
			}
			var myChart = echarts.init(document
					.getElementById("myechars"));
			var option = {
				tooltip : {
					trigger : 'axis',
				},
				color : ["#FFC000", "#7FB7F9"],
				legend : {
					data : [ '本月', '同比上月']
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
					name : '本月',
					data : echars1,
					itemStyle : {
						normal : {
							lineStyle : {
								color : "#FFC000",
							}
						}
					}
				}, {
					type : 'line',
					name : '同比上月',
					data : lastmonth1,
					symbol : 'circle',
					symbolSize : 4,
					itemStyle : {
						normal : {
							lineStyle : {
								color : "#7FB7F9",
							}
						}
					},
				}]
			};
			myChart.setOption(option);	
		}
	
	});
}

function average(){
	var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'number':num,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
    };
	$.ajax({
		url : getRootPath() + '/projectReview/queryProjectExpect',							
		type : "post",
		dataType : "json",
		cache : false,
		data:queryParams,	
		success : function(data){
			document.getElementById('average').innerHTML =data.average;
			document.getElementById('lastMonthAverage').innerHTML =data.lastMonthAverage;
			document.getElementById('lastAverage').innerHTML =data.lastAverage;
			document.getElementById('process').innerHTML =data.process;
			document.getElementById('lastMonthProcess').innerHTML =data.lastMonthProcess;
			document.getElementById('lastProcess').innerHTML =data.lastProcess;
			document.getElementById('deliver').innerHTML =data.deliver;
			document.getElementById('lastMonthDeliver').innerHTML =data.lastMonthDeliver;
			document.getElementById('lastDeliver').innerHTML =data.lastDeliver;
			document.getElementById('personnel').innerHTML =data.personnel;
			document.getElementById('lastMonthPersonnel').innerHTML =data.lastMonthPersonnel;
			document.getElementById('lastPersonnel').innerHTML =data.lastPersonnel;
		}
	});
}

function averageBrokenline(val){
	var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'number':val,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
    };	
	$.ajax({
		url : getRootPath() + '/projectReview/averageBrokenline',		
		type : "post",
		dataType : "json",
		cache : false,
		data:queryParams,	
		success : function(data){
			brokenline(data.date,data.thisMonth,data.lastMonth);
		}
	});
	actualTableSave(val);
}

function brokenline(monthList,echars1,lastmonth1){
	var myChart = echarts.init(document.getElementById("myechars"));
	var option = {
		tooltip : {
			trigger : 'axis',
		},
		color : ["#FFC000", "#7FB7F9"],
		legend : {
			data : [ '本月', '同比上月']
		},
		grid : {
			left : '5%', // 图表距边框的距离
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
			name : '本月',
			data : echars1,
			itemStyle : {
				normal : {
					lineStyle : {
						color : "#FFC000",
					}
				}
			}
		}, {
			type : 'line',
			name : '同比上月',
			data : lastmonth1,
			symbol : 'circle',
			symbolSize : 4,
			itemStyle : {
				normal : {
					lineStyle : {
						color : "#7FB7F9",
					}
				}
			},
		}]
	};
	myChart.setOption(option);
}

function queryTable(url,measureMark) {
	var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
//            'number':measureid,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
            'measureMark':measureMark
    };	
	$.ajax({
		url : getRootPath() + url,							
		type : "post",
		async:false, 
		dataType : "json",
		cache : false,
		data:queryParams,	
		success : function(data){
			$("#quality").empty();
			var tab='<div class="swiper-button-prev" style="margin-left: -4%;"></div>'+
					'<div class="state-inner clearfix swiper-container">'+
						'<div class="content clearfix swiper-wrapper">';
			for(var i=0;i<data.length;i++){
				if(i==0){
					tab+='<div id="'+data[i].id+'" class="top1 curent swiper-slide">';
					measureid=data[i].id;
				}else{
					tab+='<div id="'+data[i].id+'" class="top1 swiper-slide">';
				}
				tab+='<div style="font-size:15px;" class="top-child">'+data[i].name+'</div>'+
						'<div style="font-size:32px;" class="top-child">'+data[i].nowmonth+'</div>'+
						'<div style="margin-top:3%;" class="top-child">'+
							'<span>'+"比上一个月:"+'</span>'+
							'<span>'+data[i].lastmonth+'</span>'+
						'</div>'+
						'<div style="margin-top:-3%;" class="top-child">'+
							'<span>'+"达标项目数:"+'</span>'+
							'<span>'+data[i].green+'</span> '+
						'</div>'+
					'</div>';
			}
			tab+='</div></div><div class="swiper-button-next"></div>';
			$("#quality").append(tab);				
			var swiper = new Swiper('.swiper-container', {
				slidesPerView: 4,
				//spaceBetween: 9,
		     	slidesPerGroup:4,
			    loopFillGroupWithBlank: true,
			    direction: 'horizontal',
			    loop: false,
			    observer: true,//调完接口不能翻页解决方法，修改swiper自己或子元素时，自动初始化swiper
	            observeParents: true,//调完接口不能翻页解决方法，修改swiper的父元素时，自动初始化swiper
				navigation: {
			      nextEl: '.swiper-button-next',
			      prevEl: '.swiper-button-prev',
			    },
			});	
			quailtyechars(measureMark);
			$(".top1").on("click",function(){
				measureid = this.id;
				quailtyechars(measureMark);
				$(this).addClass("curent").siblings().removeClass("curent");
				$().loadZhiLiangTop(measureMark);				
			})
		}
	});
}

function quailtyechars(measureMark) {
	var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
//            'projectState':projectState,
            'number':measureid,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
            'measureMark':measureMark
    };	
	$.ajax({
		url : getRootPath() + '/projectReview/queryQualityEcahrs',						
		type : "post",
		dataType : "json",
		cache : false,
		data:queryParams,	
		success : function(data){
			var monthList1=data.listMonth1;
			var echars=data.echars;
			var lastmonth=data.lastMonth;
			if(measureMark == 163){
				for (var i = 0; i < 6; i++) {
					echars1[5 - i] = echars[i]; 
					monthList[5-i]=monthList1[i];
					lastmonth1[5-i]=lastmonth[i];
				}
			}else if(measureMark == 164){
				for (var i = 0; i < 3; i++) {
					echarsReliable[2 - i] = echars[i]; 
					reliableMonthList[2-i]=monthList1[i];
					lashtmonthReliable[2-i]=lastmonth[i];
				}
			}
			var myChart = echarts.init(document
					.getElementById("myechars"));
			if(measureMark == 163){
				var option = {
						tooltip : {
							trigger : 'axis',
						},
						color : ["#FFC000", "#7FB7F9"],
						legend : {
							data : [ '本周期', '比上月']
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
							name : '本周期',
							data : echars1,
							itemStyle : {
								normal : {
									lineStyle : {
										color : "#FFC000",
									}
								}
							}
						}, {
							type : 'line',
							name : '比上月',
							data : lastmonth1,
							symbol : 'circle',
							symbolSize : 4,
							itemStyle : {
								normal : {
									lineStyle : {
										color : "#7FB7F9",
									}
								}
							},
						}]
					};
				myChart.setOption(option);
			}else if(measureMark == 164){
				var option = {
						tooltip : {
							trigger : 'axis',
						},
						color : ["#FFC000", "#7FB7F9"],
						legend : {
							data : [ '本月', '比上月']
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
							data : reliableMonthList,
						},
						yAxis : [ {
							type : 'value',
						} ],
						series : [ {
							type : 'line',
							symbolSize : 4, // 拐点圆的大小
							name : '本月',
							data : echarsReliable,
							itemStyle : {
								normal : {
									lineStyle : {
										color : "#FFC000",
									}
								}
							}
						}, {
							type : 'line',
							name : '比上月',
							data : lashtmonthReliable,
							symbol : 'circle',
							symbolSize : 4,
							itemStyle : {
								normal : {
									lineStyle : {
										color : "#7FB7F9",
									}
								}
							},
						}]
					};
				myChart.setOption(option);
			}
					
		}
	});
}
function newReport(){
	window.parent.document.getElementById("report").onmouseover();
    $(window.parent.document.getElementById("zonglan")).attr("flag","3");
	window.parent.document.getElementById("zonglan").click();
    window.parent.document.getElementById("report").onmouseout();
	parent.parents = 0;
}

function verifyZrAccount(username, Currentpername, zrOrhwselect) {
	if(0 == zrOrhwselect && -1 != costAuthors().indexOf(Currentpername)){
		$.ajax({
			url : getRootPath() + '/GeneralSituation/verifyHWAccount',
			type : 'post',
			data : {
				'username' : username
			},
			async : false,// 是否异步，true为异步
			success : function(data) {
				if(0 == data){
					$('#zrModal').modal("show");
				}
			}
		});
	}
}

function addZRAccount() {
	var zrAccount = $("#zrAccount").val();
	
	if(zrAccount.length <1 || zrAccount.length > 10){
        toastr.warning("请确认中软账号长度是否准确！");
		return;
	}
	if(!(/^[0-9]+$/.test(zrAccount))){
        toastr.warning("中软账号必须为纯数字！");
		return;
	}

	$.ajax({
		url : getRootPath() + '/GeneralSituation/addZRAccount',
		type : 'post',
		data : {
			'zrAccount' : zrAccount,
			'hwAccount' : getCookie('username')
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			if("success" == data.code){
				toastr.success('修改成功！');
				$('#zrModal').modal("hide");
			}else {
			    toastr.warning("此中软账号已存在,请核实后重新配置");
            }
		}
	});
};