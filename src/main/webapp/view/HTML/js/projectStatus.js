var projectState = "在行";
(function() {
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
				initCurrentDate();
				getTime();
				selectChange();
				initArea();
				initHWCPX();
				initYeWuXian();
			})
})()
function projectOverview(){
	$("#projectOverview").bootstrapTable('destroy'); 
    $('#projectOverview').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/project/projectOverview',
    	height: 330,
    	toolbar: '#toolbar',
    	editable:true,//可行内编辑
    	sortable: true, //是否启用排序
    	sortOrder: "asc",
    	striped: true, //是否显示行间隔色
    	dataField: "rows",
    	pageNumber: 1, //初始化加载第一页，默认第一页
    	pagination:true,//是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:5,//单页记录数
    	pageList:[5,10,15],//分页步进值
    	//showRefresh:true,//刷新按钮
    	toolbar:'#toolbar',//指定工作栏
        queryParams : function(params){
			var param = {
			        'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
			        'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
			        'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
			        'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
			        'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
			        'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
			        'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
			        'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
			        'date':$("#currentDate").val(),
			        limit : params.limit, //页面大小
			        offset : params.offset, //页码
			            };
			return param;
		},
    	columns:[     
    		{
        		title : '序号',
        		valign:'middle',
        		align: "center",
        		width: 45,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#projectOverview').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#projectOverview').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
            },
        	{
        		title: '项目名称', field: 'name', width: 500,valign:'middle',
                formatter: function (value, row) {
                	var path1 = getRootPath() + '/view/HTML/page.html?url=' + getRootPath() + '/bu/projView?projNo=' + row.no + '&zlyb=1';
                	var department = row.department==null?'/':('/'+row.department);
                	var tab = '<a target="_blank" style="color: #000;" title="'+value +'" href="'+path1+'">' +
                    '<div style="width: 100%;text-align: left;">' +
                    '<label style="font-weight: unset;cursor:pointer">' +
                        ''+value+ '-'+row.area+
                    '</label>' +
                    '<div style="color: #a3a3a3;">' +
                        '<div style="width: 90%;">' +
                            '<a href="#" onclick="PageJump('+row.clientType+','+row.businessId+','+row.causeId+','+null+')" style="color:#999;">'+ row.businessUnit +'</a>' +
                            '<a href="#" onclick="PageJump('+row.clientType+','+row.businessId+','+row.causeId+','+row.deliverId+')" style="color:#999;">'+ department +'</a>' +
                            '<sqan style="float: right;">PM:'+row.pm+'</sqan>' +
                        '</div>' +
                    '</div>' +
                 '</div>' +
                '</a>';
                	return tab;
                }
        	},
        	
        	{
        		title:'进度',
        		valign:'middle',
        		align : 'center',
        		field:'progressLamp',
        		width:50,
        		formatter: function (value, row) {
        			var val = value ;
        			deng = getLamp('progress', val);
                	return '<div style="display: block;margin-left: 11px;border-radius: 50%;width: 8px;height:8px;'+
                    'background-color: '+deng.lamp+'; "></div>';
                }
        	},
        	{
        		title:'资源',
        		valign:'middle',
        		halign :'center',
        		field:'resourcesLamp',
        		width:50,
        		formatter: function (value, row) {
        			var val = value ;
        			deng = getLamp('resources', val);
                	return '<div style="display: block;margin-left: 11px;border-radius: 50%;width: 8px;height:8px;'+
                    'background-color: '+deng.lamp+'; "></div>';
                }
        	},
        	{
        		title:'质量',
        		valign:'middle',
        		halign :'center',
        		field:'qualityLamp',
        		width:50,
        		formatter: function (value, row) {
        			var val = value ;
                	deng =  getLamp('quality', val);
                	return '<div style="display: block;margin-left: 11px;border-radius: 50%;width: 8px;height:8px;'+
                    'background-color: '+deng.lamp+'; "></div>';
                }
        	},
        	{
        		title:'状态',
        		valign:'middle',
        		align : 'center',
        		field:'statusReview',
        		width:50,
        		formatter: function (value, row) {
        			var val = value;
        			deng = getLamp('status', val);
                	return '<span style="color:'+ deng.lamp +';">'+ deng.text +'</span>';
                }
        	},
        	{
        		title:'点评',
        		field:'review',
        		width:580,
				'class':'wordWrap'
        		/*editable: {
                    type: 'textarea',
                    title: '点评',
                    emptytext:'&#12288',
                    placement: 'bottom',       //编辑框的模式：支持popup和inline两种模式，默认是popup
                    mode: 'popup',
        		}*/
        	},
    	],
    	locale:'zh-CN',// 中文支持,
    });
    $(window).resize(function () {
        $('#tableId').bootstrapTable('resetView');
    });
}
function getTime(){
	var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var time = "项目整体状态("+year+"年"+month+"月)";
    document.getElementById("titleStatus").innerText = time;
    /*var progress = "进度维度("+year+"年"+month+"月)";
    document.getElementById("progressStatus").innerText = progress;*/
}
function projectStatus(){
	var s;
	$.ajax({
		url : getRootPath() + '/project/projectStatus',
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
			'projectState' : projectState,
			'clientType' : $("#selectBig").selectpicker("val") == "2" ? "0": "1",
			'date':$("#currentDate").val(),
		},
		success : function(data) {
			s = data;
		}
	});
	
	var echartsPie;
	option = {
		    title: {
		    	//subtext: '项目状况',
		        //text: '项目状况',
		        left: 'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        // orient: 'vertical',
		        // top: 'middle',
		    	itemWidth: 8,
		        itemHeight: 8,
		        bottom: 10,
		        left: 'center',
		        data: ['风险', '预警','正常','未评测'],
		        textStyle: {
		            fontSize: 9,
		        }
		    },
		    series : [
		        {
		            type: 'pie',
		            radius : '53%',
		            center: ['50%', '50%'],
		            selectedMode: 'single',
		            label : {
						normal : {
							show : true,
							//position:'inner',
							formatter : '{c}'
						},
						emphasis : {
							show : true,
							textStyle : {
								fontSize : '12',
								fontWeight : 'bold'
							}
						}
					},
		            data:[
		                {value:s.red, name: '风险'},
		                {value:s.yellow, name: '预警'},
		                {value:s.green, name: '正常'},
		                {value:s.gray, name: '未评测'}
		            ],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                },
		                normal : {
							color : function(params) {
								var colorList = [ '#f57454', '#f7b547', '#1adc21',
										'#bdb7b7', ];
								return colorList[params.dataIndex]
							}
						}
		            }
		        }
		    ]
		};
	var myChart=document.getElementById("projectStatus");
    myChart.style.width=window.outerWidth*0.23+'px';
    echartsPie=echarts.init(myChart);
	echartsPie.setOption(option);
	projectOverview();
}
function getCookie(name) {
		var arr = document.cookie.match(new RegExp("(^| )" + name
				+ "=([^;]*)(;|$)"));
		if (arr != null) {
			return decodeURIComponent(arr[2]);
		} else {
			return null;
		}
	}
function selectOnchange() {
	$.ajax({
		url : getRootPath() + '/bu/queryMess',
		type : 'post',
		async : false,// 是否异步，true为异步
		data : {
			'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
		    'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
		    'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
		    'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
		    'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
		    'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
		    'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
		    'projectState':projectState,
		    'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
		},
		success : function() {
		}
	});
	projectStatus();
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
//加载当前日期	
function initCurrentDate() {
	$.ajax({
		url: getRootPath() + '/projectReview/queryProjectCycle',
		type: 'post',
		async: false,//是否异步，true为异步
		success: function (data) {
			$("#currentDate").empty();
			data = data.data;
			if(null==data||""==data) return;
			var select_option="<option selected = selected value="+data[1]+">"+data[1]+"</option>";
			var length = data.length<6 ? data.length: 6;
			for(var i = 2; i < length; i++){
				select_option += "<option value="+data[i]+">"+data[i]+"</option>";
			}
			$("#currentDate").html(select_option);
            $('#currentDate').selectpicker('refresh');
            $('#currentDate').selectpicker('render');
		}
	});
};
function PageJump(client,business,cause,deliver){
	var data={};
	if(client==1){
		data = {
				'bu': business,
				'pdu': cause,
				'du': deliver,
				'projectState':projectState,
				'clientType':client,
				};
	}else if(client==0){
		data = {
				'hwpdu': business,
				'hwzpdu': cause,
				'pduSpdt': deliver,
				'projectState':projectState,
				'clientType':client,
				};
	}
	parent.parents = data;
	window.parent.proManage(1);
}
function HomePageJump(val){
	if(val==1){
		window.parent.newReport();
	}else if(val==2){ 
		window.parent.proManage(0);
	}
}