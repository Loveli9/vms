var proNo = getQueryString("projNo");
var date;
var queryDate;
var realMap = {};
var dro = null;
var drop = 0;
var middleId = null;
var listTime=new Array(6);
var currentTime = currentDate();
var isedit = false;

$(function() {
	
	$(document).ready(function() {
		Menus = getCookie('Menulist');
        if (Menus.indexOf("-50-") != -1) {
        	isedit=true;
        	$("#btn_add").css("display","");
        }else{
        	$("#btn_add").css("display","none");
        }
	});
	
	document.body.onclick = function(e) {
		e = e || window.event;
		var target = e.target || e.srcElement;
		if (target != dro && dro != null) {
			dro.hide();// 隐藏
		}
	};
	/*var multiStep=window.parent.document.getElementById("multiStep");
	if(multiStep){
        $(multiStep.children).removeClass("active");
        $(multiStep.children[0]).addClass("active");
	}*/

	addDateSectionDiv();
	var dateSection = window.document.getElementById("dateSection");
	var index= dateSection.selectedIndex;
	date = dateSection[index].value;
	queryDate = date;
	if(index<dateSection.length-1){
		queryDate = queryDate + ',' + dateSection[index+1].value;
	}
	//指标点评数据列表
    var ids = new Array();
	var actualValue = new Array();
	var modify = 1;
	if(currentTime==date){
		modify = 0;
	}
	
	$.ajax({
		type : "post",
		url : getRootPath() + '/projectReview/getCloseDateByNo',
		async: false,//是否异步，true为异步
		data : {
			proNo : proNo
		},
		success : function(data) {
			if(""!= data && null!= data){
				closeDate = true;
			}else{
				closeDate = false;
			}
		}
	});
	if(modify==1 || closeDate==true){
		$('#btn_add').prop('disabled', true);
	}else{
		$('#btn_add').prop('disabled', false);
	}
	$('#measureTable').bootstrapTable({
		method : 'get',
		contentType : "application/x-www-form-urlencoded",
		url : getRootPath()
				+ "/measureComment/queryMeasureCommentList",
		striped : false, // 是否显示行间隔色
		// height: tableHeight(),
		dataField : 'data',
		toolbar:'#toolbar',//指定工作栏
		toolbarAlign:'right',
		minimumCountColumns : 2, // 最少允许的列数
		queryParams : function(params) {
			if(queryDate=="时间配置不在周期内"){
				return false;
			}
			var param = {
				'proNo' : proNo,
				'queryDate' : queryDate
			};
			return param;
		},
		onEditableSave : function(field, row, oldValue, $el) {
			var id = row.id;
			var flag = true;
			if ("realValue" == field) {
				if ($el[0].id) {
					id = $el[0].id;
				}
				var str = row.realValue;
				if(str.length>20){
					toastr.error('输入值不能超过20位！');
					flag = false;
				}else if (row.id == 314) {
					if (str.length == 0) {
						toastr.error('输入的CI全量回归实际值为空！');
						flag = false;
					} else if (str.length != 0) {
						var reg = /^(\d{1,2}):(\d{1,2}):(\d{1,2})$/;
						var r = str.match(reg);
						if (r == null) {
							toastr.error('输入的CI全量回归实际值格式有误！');
							flag = false;
						}
					}
				} else {
					str = str.trim();
					if (isNaN(str)) {
						toastr.error('输入的实际值格式有误！');
						flag = false;
					}
				}
			} else if ("comment" == field) {
				flag = true;
			}
			if (flag) {
				$.ajax({
						type : "post",
						url : getRootPath()
								+ "/measureComment/saveMeasureComment",
						data : {
							'proNo' : proNo,
							'id' : id,
							'value' : str,
							'comment' : row.comment,
							'date' : date,
							'field' : field
						},
						success : function(data, status) {
							if (data.code == "success") {
								toastr.success('保存成功！');
								realMap = {};
								$('#measureTable').bootstrapTable('refresh');
							} else {
								toastr.error('保存失败！');
							}
						}
						});
			}
		},
		columns : [
			{
				title : '序号',
				field : 'measureNo',
				align : "center",
				width : 60,
				valign : 'middle',
				cellStyle : function cellStyle(value, row,
						index) {
					if (row.level == 0) {
						return {
							css : {
								"background-color" : "#97deff",
								"color" : "#000",
								"text-align" : "left !important;",
							}
						};
					}

					return {
						css : {
							"border-right" : "1px solid #e7eaec !important;",
							"border-left" : "1px solid #e7eaec !important;",
						}
					};
				},
				formatter : function(value, row, index) {
					if (row.level == 0) {
						ids.push(index);
					}
					return value == null ? "" : value;
				}
			},
			{
				title : '指标类别',
				field : 'measureCategory',
				align : "center",
				width : 75,
				valign : 'middle',
				cellStyle : function cellStyle(value, row,index) {
					return {
						css : {
							"font-size" : "12px",
							"color" : "#000",
							"white-space" : "normal",
							"border-right" : "1px solid #e7eaec !important;",
							"width" : "75px !important;",
						}
					};

				},
				formatter : function(value, row, index) {
					return value == null ? "" : value;
				}

			},
			{
				title : '指标名称',
				field : 'line1',
				align : "center",
				width : 200,
				valign : 'middle',
				cellStyle : function cellStyle(value, row,
						index) {
					return {
						css : {
							"text-align" : "left !important;",
							//"border-right": "1px solid rgb(231, 234, 236) !important"
						}
					};
				},
				formatter : function(value, row, index) {
					if (row.level == 2) {
						var tmp5 = value.measureName;
						if (row.collect == "auto") {
							return '<div class="nolefttitle" style="cursor: pointer;color: #337ab7;" onclick="measureModalShow(\''+proNo+'\',\''+row.id+'\',\''+tmp5+'\')"><img style="float: left;margin-top: -20px;margin-left: -8px;width: 50px;" src="../../view/HTML/images/lefttitle.png" />'
									+ tmp5 + '</div>';
						}
						return '<div class="nolefttitle" style="cursor: pointer;color: #337ab7;" onclick="measureModalShow(\''+proNo+'\',\''+row.id+'\',\''+tmp5+'\')">'
								+ tmp5 + '</div>';
					}
					return value == null ? "" : value;

				}
			},
			{
				title : '质量目标',
				field : 'line1',
				align : "center",
				width : 210,
				valign : 'middle',
				cellStyle : function cellStyle(value, row,
						index) {
					return {
						css : {
							"text-align" : "left !important;",
							//"border-right": "1px solid rgb(231, 234, 236) !important"
						/* "padding-left": "20px", */
						}
					};
				},
				formatter : function(value, row, index) {
					if (row.level == 2) {
						var tmp1 = value.lower;
						var tmp2 = value.upper;
						var tmp4 = value.target;
						return '<div>' + '上限：' + tmp2
								+ '&nbsp;&nbsp;下限：' + tmp1
								+ '&nbsp;&nbsp;目标：' + tmp4
								+ '</div>';
					}
					return value == null ? "" : value;
				}
			},
			{
				title : '单位',
				field : 'line1',
				align : "center",
				width : 80,
				valign : 'middle',
				cellStyle : function cellStyle(value, row,index) {
					return {
						css : {
							//"border-right": "1px solid rgb(231, 234, 236) !important"
						}
					};
				},
				formatter : function(value, row, index) {
					if (row.level == 2) {
						var tmp3 = value.unit;
						return tmp3;

					}
					return value == null ? "" : value;
				}
			},
			{
				title : '优先级',
				field : 'line1',
				align : "center",
				width : 75,
				valign : 'middle',
				cellStyle : function cellStyle(value, row,index) {
					return {
						css : {
							//"border-right": "1px solid rgb(231, 234, 236) !important"
						}
					};
				},
				formatter : function(value, row, index) {
					if (row.level == 2) {
						var tmp6 = value.computeRule;
						if ("13" == tmp6) {
							tmp6 = "下限";
						} else if ("12" == tmp6) {
							tmp6 = "上限";
						} else {
							tmp6 = "目标";
						}
						return tmp6;
					}
					return value == null ? "" : value;
				}
			},
			{
				title : '实际值',
				field : 'realValue',
				align : "center",
				width : 210,
				valign : 'middle',
				cellStyle : function cellStyle(value, row,
						index) {
					return {
						css : {
							"text-align" : "right !important",
							"padding-right" : "0px",
							//"border-right": "1px solid rgb(231, 234, 236) !important"
						}
					};
				},
				formatter : function(value, row, index) {
					if (row.level == 2) {
						var val = value.measureValue;
						var light = value.light;
						if (val == null || "" == val) {
							return "";
						}
						if (light == null) {
							return val;
						} else {
							realMap[index] = light;
						}
						return val;
					}
					return "-";
				},
				editable: {
					onblur:"submit", //表示鼠标离开输入框即进行提交保存操作
					showbuttons:false, //不显示每个单元格后面的操作按钮
					disabled: false,  //是否生效
					type: 'text',//编辑框的类型。支持text|textarea|select|date|checklist等
					emptytext:'&#12288',//空值显示“...”,没有这个显示Empty
					mode: 'inline',//编辑框的模式：支持popup和inline两种模式，默认是popup
					placement: 'bottom',
					noeditFormatter: function (value,row,index) {
						if(row.level == 1){
							return "";
						}
						if(row.level == 3){
							return false;
						}
						if(row.collect=="auto" || (row.collect=="manual" && row.children)){

							if(value!=null && value!=""){
								var line = row.line1;
								var name = line.measureName;
								var val = value.measureValue;
								var unit = line.unit;
								if (row.collect == "auto") {
									if(val==null){
										val="";
									}
									return '<span title="'+val+'" style="overflow: hidden;margin-right: 17px;width:105px;display: inline-block;height: 30px;line-height: 28px;padding: 0px 0px 0px 23px;">'+val+'</span>';
								}
								if(actualValue.indexOf(index)==-1){
									actualValue.push(index);
								}
								if (val == null) {
									val = "";
									return '<a title="'+val+'" href="javascript:void(0)" id="middle'+row.id+'" class="actualValueStyle" onclick="processData('+row.id+',\''+name+'\','+'\''+unit+'\','+modify+')">'+val+'</a>';
								}
								if (value.light==null){
									return '<a title="'+val+'" href="javascript:void(0)" id="middle'+row.id+'"  class="actualValueStyle" onclick="processData('+row.id+',\''+name+'\','+'\''+unit+'\','+modify+')">'+val+'</a>';
								}
								var light = value.light.light;
								return '<a title="'+val+'" href="javascript:void(0)" id="middle'+row.id+'"  class="actualValueStyle" onclick="processData('+row.id+',\''+name+'\','+'\''+unit+'\','+modify+')">'+val+'</a>';
							} else {
								return "";
							}
						} else {
							var val = "";
							if(value!=null && value!=""){
								var val = value.measureValue;
							}
							if(modify==0 && closeDate==false && isedit==true){
								val='<a title="'+val+'" href="javascript:void(0)" style="overflow: hidden;margin-right: 17px;border: 1px solid #ddd;width:105px;display: inline-block;height: 30px;line-height: 28px;padding: 0px 0px 0px 23px;" data-name="realValue" data-pk="'+row.id+'" data-value="'+val+'" class="editable editable-click"></a>';
							}else{
								if(val==null){val=""}
								val = '<span title="'+val+'" style="overflow: hidden;margin-right: 17px;border: 0px;width:105px;display: inline-block;height: 30px;line-height: 28px;padding: 0px 0px 0px 23px;">'+val+'</span>'
							}
							return val;
						}
					}
				}
			},
			{
				title: '点评',
				field: 'comment',
				align: "center",
				width: '100%',
				valign: 'middle',
				cellStyle: function cellStyle(value, row, index) {
					var sty = {
						"text-align": "left !important",
						"padding": "0px",
						"word-wrap":"break-word",
						"-webkit-line-clamp": "2",
						"overflow": "hidden",
						"text-overflow": "ellipsis",
						"-webkit-box-orient": "vertical",
						"display": "-webkit-box",
						"padding-top": "6px"
					};
					if(row.comment == '-10'){
						sty["color"]="red";
					}
					return {
						css: sty
					};
				},
				editable: {
					onblur:"submit", //表示鼠标离开输入框即进行提交保存操作
					showbuttons:false, //不显示每个单元格后面的操作按钮
					disabled: false,  //是否生效
					type: 'textarea',//编辑框的类型。支持text|textarea|select|date|checklist等
					emptytext: '&#12288',
					mode: 'inline',//编辑框的模式：支持popup和inline两种模式，默认是popup
					placement: 'bottom',

					// validate: function (value) {
						// alert(value);
					//   if(value == '-10'){
					//       return '';
					 //  }
					// },
					noeditFormatter: function (value, row, index) {
						var desc = "";
						if(value == '-10'){
							desc = '指标实际值发生较大变化，请给出点评！';
							value = "";
						}
						if (row.level != 2) {
							if (value == null || value == "") {
								value = "";
							}
							return value;
						} else {
							if(modify==0 && closeDate==false && isedit==true){
								return '<a href="javascript:void(0)" style="color:inherit;border-bottom: 0px;display:block;width:100%;height:37px;"'+
								'data-name="comment" data-pk="'+row.id+'" data-value="'+value+'" class="editable editable-pre-wrapped editable-click editable-empty">' +
									'<span type="text">'+desc+'</span></a>';
							}else{
								if(value==null){value=""}
								return '<span title="'+value+'" style="color:inherit;border-bottom: 0px;display:block;width:100%;height:37px;overflow: hidden;text-overflow: ellipsis;white-space:nowrap;">'+value+'</span>';
							}
						}
					}
				}
			}
		],
		onLoadSuccess: function (res) {
			var data = res.data;
			if (data.length==0){
				return;
			}
			var kongs = new Array();
			for(var key in realMap){
				if(realMap.hasOwnProperty(key)){
					 var light = "img"+realMap[key].light;
					 var direction = light.substring(light.length-5);
					 var valueTd= $('#measureTable').find('tr[data-index='+ key+']> td')[6];
					 var id=data[key].id;
					 var str = "";
						 if(modify==0 && closeDate==false && isedit==true){
							 str1 = "";
									if(data[key].changeValue==1){
										str1 = "<div class='btn-group'><button type='button' onclick='dropdownid("+id+")' class='btn imggreen"+direction+" dropdown-toggle'></button>"
									}else if(data[key].changeValue==2){
										str1 = "<div class='btn-group'><button type='button' onclick='dropdownid("+id+")' class='btn imgyellow"+direction+" dropdown-toggle'></button>"
									}else if(data[key].changeValue==3){
										str1 = "<div class='btn-group'><button type='button' onclick='dropdownid("+id+")' class='btn imgred"+direction+" dropdown-toggle'></button>"
									}else if(data[key].changeValue==4 || data[key].changeValue==null){
										str1="<div class='btn-group'><button type='button' onclick='dropdownid("+id+")' class='btn img"+realMap[key].light+" dropdown-toggle'></button>"
									}
							 str +=str1+
							 "<div id='dropdown"+id+"' class='dropdown-menu'>"+
								"<li><a href='###' class='imggreenequalz' style='text-align: right' onclick='shows(this.text,this,"+id+",\""+light+"\")'>正常</a></li>" +
								"<li><a href='###' class='imgyellowequalz' style='text-align: right' onclick='shows(this.text,this,"+id+",\""+light+"\")'>预警</a></li>" +
								"<li><a href='###' class='imgredequalz' style='text-align: right' onclick='shows(this.text,this,"+id+",\""+light+"\")'>超标</a></li>" +
								"<li><a href='###' class='' style='text-align: right' onclick='shows(this.text,this,"+id+",\""+light+"\")'>实际</a></li>" +
							"</div>"+
							"</div>"
						 }else{
							 if(data[key].changeValue==1){
									str = "<div class='btn-group'><button type='button' class='btn imggreen"+direction+" dropdown-toggle'></button>"
								}else if(data[key].changeValue==2){
									str = "<div class='btn-group'><button type='button' class='btn imgyellow"+direction+" dropdown-toggle'></button>"
								}else if(data[key].changeValue==3){
									str = "<div class='btn-group'><button type='button' class='btn imgred"+direction+" dropdown-toggle'></button>"
								}else if(data[key].changeValue==4 || data[key].changeValue==null){
									str="<div class='btn-group'><button type='button' class='btn img"+realMap[key].light+" dropdown-toggle'></button>"
								}
						 }
					 $(valueTd).prepend(str);
					 kongs.push(key);
				}
			}
			for(var i = 0;i<data.length;i++){
				if(kongs.indexOf(i.toString())==-1){
					var valueKong= $('#measureTable').find('tr[data-index='+i+']> td')[6];
					var kong = "<div class='btn-group'><button style='cursor:default;background-color: transparent;' type='button' class='btn dropdown-toggle'></button></div>";
					$(valueKong).prepend(kong);
				}
			}
			for (var i = 0; i < ids.length; i++) {
				$('#measureTable').bootstrapTable('mergeCells', {
					index: ids[i],
					field: "measureNo",
					colspan: 9,
					rowspan: 1
				});
			}
			for (var i = 0; i < actualValue.length; i++) {
				var valueTd= $('#measureTable').find('tr[data-index='+ actualValue[i]+'] > td')[6];
				$(valueTd).addClass("rightactual");
			}
			var columnName="measureCategory";
			mergeTable(columnName);
			if (isedit==false) {
				$("#measureTable td a").attr("onclick", "return false");
			}
		},
		locale : 'zh-CN'// 中文支持
	});

});

$(document).on("click", "#btn_add", function() {
	var dataArr= $('#measureTable').bootstrapTable('getData');
	var list = [];
	for(var i=0;i<dataArr.length;i++){	
		if(dataArr[i].level>=2){
			var d = dataArr[i];
			var val = d.realValue.measureValue;
			if(val){
				var children = d.children;
				var level = '2';
				if(children){
					for(var j=0;j<children.length;j++){
						level = '3';
						var data2 ={
							'proNo' : proNo,
                            'measureId' : ''+children[j].id,
                            'measureValue' : children[j].measureValue,
                            'date' : date,
                            'level': level
                    	};
						list.push(data2);
					}
					level = '2';	
					children = null;
				}
				var data1 ={
					'proNo' : proNo,
					'measureId' : ''+d.id,
					'measureValue' : d.realValue.measureValue,
					'date' : date
				};
				list.push(data1);
							
			}	   
		}
	}

	$.ajax({
		type : "post",
		url : getRootPath() + '/measureComment/saveMeasureList',
		dataType:"json",  
	    contentType : 'application/json;charset=utf-8', //设置请求头信息 
		data:JSON.stringify(list),
		success:function(data){
			if (data.code == "success") {
				toastr.success('整体保存成功！');
				realMap = {};
				$('#measureTable').bootstrapTable('refresh');
			} else {				
				toastr.error('整体保存失败！');
			}
		}
	});
});

$(document).on("click", ".imgredupper", ".imgyellowupper", function() {
	$(this).find(".dropdown-menu").toggle();
	if(drop==1){
		dro.hide();
	}
});

$(document).on("click", ".imggreenupper", ".imgredequal", function() {
	$(this).find(".dropdown-menu").toggle();
	if(drop==1){
		dro.hide();
	}
});

$(document).on("click", ".imggreenequal", ".imgyellowequal", function() {
	$(this).find(".dropdown-menu").toggle();
	if(drop==1){
		dro.hide();
	}
});

$(document).on("click", ".imgyellowlower", ".imgredlower", function() {
	$(this).find(".dropdown-menu").toggle();
	if(drop==1){
		dro.hide();
	}
});

$(document).on("click", ".imggreenlower", function() {
	$(this).find(".dropdown-menu").toggle();
	if(drop==1){
		dro.hide();
	}
});

$(document).on("click", ".btn-group", function() {
	$(this).find(".dropdown-menu").toggle();
	if(drop==1){
		dro.hide();
	}
});

function mergeTable(field) {
	$table = $("#measureTable");
	var obj = getObjFromTable($table, field);
	for ( var item in obj) {
		$("#measureTable").bootstrapTable('mergeCells', {
			index : obj[item].index,
			field : field,
			colspan : 1,
			rowspan : obj[item].row
		});
	}
}

function getObjFromTable($table, field) {
	var obj = [];
	var maxV = $table.find("th").length;

	var columnIndex = 0;
	var filedVar;
	for (columnIndex = 0; columnIndex < maxV; columnIndex++) {
		filedVar = $table.find("th").eq(columnIndex).attr("data-field");
		if (filedVar == field)
			break;

	}
	var $trs = $table.find("tbody > tr");
	var $tr;
	var index = 0;
	var content = "";
	var row = 1;
	for (var i = 0; i < $trs.length; i++) {
		$tr = $trs.eq(i);
		var contentItem = $tr.find("td").eq(columnIndex).html();
		// exist
		if (contentItem.length > 0 && content == contentItem) {
			row++;
		} else {
			// save
			if (row > 1) {
				obj.push({
					"index" : index,
					"row" : row
				});
			}
			index = i;
			content = contentItem;
			row = 1;
		}
	}
	if (row > 1)
		obj.push({
			"index" : index,
			"row" : row
		});
	return obj;
}
var processId;
function processData(id,name,unit,modify) {
	middleId = "middle"+id;
	$("#calculation").text(name+":");
	var val = $("#"+middleId).text();
	processId = new Array();
	$("#processId").val(id);
	$("#processValues").val(val);
	$("#processUnit").html(unit);
	$("#processDataPage").modal('show');
	if(modify==1 || closeDate==true){
		$("#processSave").hide();
	}
	$('#processData').bootstrapTable('destroy');
	$('#processData').bootstrapTable({
		method : 'get',
		contentType : "application/x-www-form-urlencoded",
		url : getRootPath()
				+ "/measureComment/queryMeasureProcessData",
		striped : false, // 是否显示行间隔色
		//height : 260,
		dataField : 'data',
		minimumCountColumns : 2, // 最少允许的列数
		queryParams : function(params) {
			var param = {
				'proNo' : proNo,
				'measureId' : id,
				'date' : date,
			};
			return param;
		},
		columns : [
			{
				title : '指标名称',
				field : 'name',
				align : "center",
				width : 377,
				'class' : "measureStyle",
				valign : 'middle',
				cellStyle: function cellStyle(value, row, index) {
					return {
						css: {
							"background-color": "#fff",
							"color":"#000",
						}
					};
				}
			}, {
				title : '指标值',
				field : 'measureValues',
				align : "center",
				width : 222,
				valign : 'middle',
				'class' : "measureStyle",
				cellStyle: function cellStyle(value, row, index) {
					return {
						css: {
							"background-color": "#fff",
							"color":"#000"
						}
					};
				},
				formatter : function(value, row, index) {
					if(processId.indexOf(row.id)){
						processId.push(row.id);
					}
					if(value==null){
						value='';
					}
					var str = '<span style="text-align: center;padding:7px;width: 80px;margin: 0px auto;display:block;">'+value+'</span>';
					if(modify==0 && closeDate==false && isedit==true){
						str = '<input id="process'+row.id+'" oninput="processOperation('+id+','+row.id+')" style="text-align: right;width: 80px;margin: 0px auto;" type="text" class="form-control input-sm" value="'+value+'">';
					}
					return str;
				}
			}, {
				title : '单位',
				field : 'unit',
				align : "center",
				width : 133,
				valign : 'middle',
				'class' : "measureStyle",
				cellStyle: function cellStyle(value, row, index) {
					return {
						css: {
							"background-color": "#fff",
							"color":"#000",
						}
					};
				},
				formatter : function(value, row, index) {
					return '<span id="unit'+row.id+'" >'+row.unit+'</span>';
				}
			}
		],
		locale : 'zh-CN'// 中文支持
	});
}
var cessId = null;
var flag = true;
function processOperation(id,rowid){
	var value = $("#process"+rowid).val().trim();
	var processIds = [366,367,369];
	if(value=="" && processIds.indexOf(id)==-1){
		$("#processValues").val("");
		return;
	}
	
	if(value!=""){
		var unit = ($("#unit"+rowid).html()).replace(/\s*/g,"");
		var rea = /^(0|\+?[1-9][0-9]*)$/;//0和正整数
		var reb = /^[0-9]+.?[0-9]*$/;//正小数
		var a = value.match(rea);
		var b = value.match(reb);
		if((unit=="KLOC" || unit=="千字") && a==null && b==null){
			toastr.error('输入的指标值格式有误！');
			flag = false;
		}else if((unit=="个" || unit=="页") && a==null){
			toastr.error('输入的指标值格式有误！');
			flag = false;
		}
	}
	
	if(flag){
		$("#process"+rowid).css("font-weight","bold");
		cessId = id;
		var value = 0;
		
		if(processIds.indexOf(id)==-1){
			var denominator =[10083,10100,10096,10097,10098,10095,10112,10009,10101,10075,10099];
			var a = 0;
			var b = 0;
			for(var i=0;i<processId.length;i++){
				var processVal1 = $("#process"+processId[i]).val().trim();
				if(processVal1!=""){
					if(denominator.indexOf(processId[i])!=-1){
						b = parseFloat(processVal1);
					}else{
						a = parseFloat(processVal1);
					}
				}else{
					return;
				}
			}
			if(b!=0){
				value = a/b;
			}
		}else{
			if(id==366 || id==367){
				var num = 0;
				for(var i=0;i<processId.length;i++){
					var processVal2 = $("#process"+processId[i]).val().trim();
					if(processVal2!=""){
						value += parseFloat(processVal2);
						num++;
					}
				}
				if(num==0){
					$("#processValues").val("");
					return;
				}
			}else if(id==369){
				var a = 0;
				var b = 0;
				var num1 = 0, num2 = 0;
				for(var i=0;i<processId.length;i++){
					var processVal3 = $("#process"+processId[i]).val().trim();
					if(processVal3!=""){
						if(processId[i]==10099){
							num2++;
							b = parseFloat(processVal3);
						}else if(processId[i]==10037){
							a += parseFloat(processVal3)*10;
							num1++;
						}else if(processId[i]==10038){
							a += parseFloat(processVal3)*3;
							num1++;
						}else if(processId[i]==10039){
							a += parseFloat(processVal3);
							num1++;
						}else if(processId[i]==10040){
							a += parseFloat(processVal3)*0.1;
							num1++;
						}	
					}					
				}
				if(num1==0 || num2==0){
					$("#processValues").val("");
					return;
				}
				if(b!=0){
					value = a/b;
				}
			}
		}
		if($("#processUnit").html()=="%"){
			value = value*100;
		}
		value = value.toFixed(2);
		$("#processValues").val(value);
	}
}
function processSave() {
	if(flag){
		var val = $("#processValues").val();
		/*$("#middle"+cessId).text(val);
		$("#middle"+cessId).css("font-weight","bold");*/
		$("#processValues").val("");
		var measure = new Array();
		for(var i=0;i<processId.length;i++){
			var children = {'measureId' : processId[i],'measureValue' : $("#process"+processId[i]).val()};
			measure.push(children);
		}
		$.ajax({
			type : "post",
			url : getRootPath()+ "/measureComment/saveMeasureProcessData",
			dataType:'json',
			data : {
				'proNo': proNo,
				'middleId' : cessId,
				'middleValue' : val,
				'date' : date,
				'measure' : JSON.stringify(measure)
			},
			success : function(data) {
				if (data.code == "success") {
					toastr.success('保存成功！');
					realMap = {};
					$('#measureTable').bootstrapTable('refresh');
				} else {
					toastr.error('保存失败！');
				}
			}
		});
		$("#processDataPage").modal('hide');
	}
}
function processCancel() {
	$("#processDataPage").modal('hide');
	$("#processValues").val("");
}
function dropdownid(id) {
	drop = 0;
	if (dro != null) {
		dro.hide();
		dro = $("#dropdown" + id);
	} else {
		dro = $("#dropdown" + id);
	}
}
function shows(a, b, c, d) {
	var val=a;
	var changeValue;
	if(val=="正常"){
		changeValue=1;
	}else if(val=="预警"){
		changeValue=2;
	}else if (val=="超标"){
		changeValue=3;
	}else if (val=="实际"){
		changeValue=4;
	}
	var direction = d.substring(d.length-5);
	$.ajax({url : getRootPath() + '/measureComment/measureCommentChange',
				type : 'post',
				dataType : "json",
				async : false,
				data : {
					changeValue : changeValue,
					measureId : c,
					proNo : proNo,
					changeDate : date
				},
				success : function(data) {
					$(b).parents('.btn-group').find(".dropdown-toggle").removeClass("imgredequal imggreenequal imgyellowequal imgredupper imggreenupper imgyellowupper imgyellowlower imgredlower imggreenlower")
					if (a == "超标") {
						$(b).parents('.btn-group').find(".dropdown-toggle")
								.addClass("imgred"+direction)
					} else if (a == "正常") {
						$(b).parents('.btn-group').find(".dropdown-toggle")
								.addClass("imggreen"+direction)
					} else if (a == "预警") {
						$(b).parents('.btn-group').find(".dropdown-toggle")
								.addClass("imgyellow"+direction)
					} else if (a == "实际") {
						$(b).parents('.btn-group').find(".dropdown-toggle")
								.addClass(d)
					}
				}
			});
	drop = 1;
}
function currentDate(){
	var myDate = new Date();
	var day = myDate.getDate();
	var current = myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-";
	if(myDate.getMonth()<9){
		current = myDate.getFullYear()+"-0"+(myDate.getMonth()+1)+"-";
	}
	if(day<16){
		current = current+15;
	}else{
		myDate = new Date(myDate.getFullYear(),(myDate.getMonth()+1),0);
		current += myDate.getDate();
	}
	return current;
}

//根据窗口调整表格高度
$(window).resize(function() {
	$(".fixed-table-container").css({"min-height":$(window).height() - 30});
});

/**
 *质量点评指标趋势图表
 */
$(document).on("click", "#closeBtnQualityCommentModal", function () {
    $("#qualityCommentModal").modal('hide');
});
function measureModalShow(proNo, measureId, measureName) {
	$("#qualityCommentTitle").html(date);

	$.ajax({
		url : getRootPath() + '/measureComment/queryMeasureValue',
		type : 'post',
		data : {
			proNo : proNo,
			measureId : measureId,
			date : date
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			brokenLineDiagram(data, measureName);
		}
	});
}
function brokenLineDiagram(data, name) {
	if (data.code == '200') {
		$("#qualityCommentModal").modal('show');
		var divId = "qualityCommentPage";
		var axisName = [ '时间', name ];
		var types = [ name ];
		
		var xAxis = data.data.monthList;
		var values = [];
		values.push(data.data.valueList);
		var markPointData = [];
		for (var i = 0; i < xAxis.length; i++) {
			if (date === xAxis[i]) {
				markPointData.push({
					name : '选择时间',
					value : values[0][i],
					xAxis : xAxis[i],
					yAxis : values[0][i]
				});
			}
		}

		//不传入颜色为默认颜色
		lineCharts(divId, axisName, types, xAxis, values, null, markPointData);
	} else {
		toastr.error('查询指标趋势异常!');
	}
}
function addDateSectionDiv(){
	$.ajax({
		type : "post",
// 			url : getRootPath() + '/projectReview/queryProjectCycle',
		url : getRootPath() + '/measureComment/getTimeQ',
		async: false,//是否异步，true为异步
		data : {
			proNo : proNo,
			num:6,
			flag:true,
		},
		success : function(data) {
			if (data.code = "200") {
				data= data.data;
				if(data.length > 0){
					var nowDate = data[0];
					var option = "<select id='dateSection'  style='font-size: 16px; width: 140px; height: 35px; padding-left: 10px; padding-right: 10px; border-radius: 4px; outline: none;' onchange='changeDateSection()'>"
						+"<option selected ='selected' value='" + nowDate + "'>" + nowDate + "</option>";
					for (var i = 1; i < data.length; i++) {
						option += "<option value='" + data[i] + "'>" + data[i]
							+ "</option>";
					}
					option += "</select>"
					$("#dateSectionDiv").append(option);
					$("#dateSectionDiv").css('display','block');
				} else{
					$("#dateSectionDiv").append("<select id='dateSection'  style='font-size: 8px; width: 140px; height: 35px; padding-left: 10px; padding-right: 10px; border-radius: 4px; outline: none;' onchange='changeDateSection()'>"
						+"<option selected ='selected' value='" + '时间配置不在周期内' + "'>" + '时间配置不在周期内'+ "</option>");
					$("#dateSectionDiv").css('display','block');
				}
			}
		}
	});

}
function changeDateSection(){
	var dateSection = window.document.getElementById("dateSection");
	var index= dateSection.selectedIndex;
	date = dateSection[index].value;
	queryDate=date;
	if(index<dateSection.length-1){
		queryDate = queryDate + ',' + dateSection[index+1].value;
	}
	$('#measureTable').bootstrapTable('refresh');
}
