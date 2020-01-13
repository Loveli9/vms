$(function(){
/*	var area="";
	var hwpdu="";
	var hwzpdu="";
	var pduSpdt="";
	var bu="";
	var pdu="";
	var du="";
*/	var projectState = "在行";
	var myCharts1=echarts.init(document.getElementById("yanfaXL"));
	var pcbCategory = "研发效率";	
	var measureIds = ["237","238"];
	var nos = "";
	/*function menuChange(){
		var areaName=$(".list .active .areaActive",parent.document).text();
		if(''!=areaName&&undefined!=areaName){			
			area=areaName; 
		}
		var depId=$(".crumbs .active",parent.document).attr("data-id");
		
		if(''!=depId&&undefined!=depId){
			if(1==depId){
				var yijiId=$(".list .yiji>.active",parent.document).attr("data-id");
				var erjiId=$(".list .erji>.active",parent.document).attr("data-id");
				var sanjiId=$(".list .sanji>.active",parent.document).attr("data-id");
				var sijiId=$(".list .siji>.active",parent.document).attr("data-id");
				if(''!=erjiId&&undefined!=erjiId){
					bu=erjiId;
					if(''!=sanjiId&&undefined!=sanjiId){
						pdu=sanjiId;
						if(''!=sijiId&&undefined!=sijiId){
							du=sijiId;
						}
					}
				}				
			}else if(2==depId){
				//var yijiId=$(".list .yiji>.active",parent.document).attr("data-id");
				var erjiId=$(".list .erji>.active",parent.document).attr("data-id");
				var sanjiId=$(".list .sanji>.active",parent.document).attr("data-id");
				var sijiId=$(".list .siji>.active",parent.document).attr("data-id");
				if(''!=erjiId&&undefined!=erjiId){
					hwpdu=erjiId;
					if(''!=sanjiId&&undefined!=sanjiId){
						hwzpdu=sanjiId;
						if(''!=sijiId&&undefined!=sijiId){
							pduSpdt=sijiId;
						}
					}
				}		
			}
			loadCeshiXL();
		}
	};*/
	
	function exportbut(){
		$("#exportbut").click(function(){
			if(!nos){
				toastr.warning('导出信息为空');
			}else{
			var $eleForm = $("<form method='POST'></form>");
            $eleForm.attr("action",getRootPath() + "/pcbList/exportResearch");
            $eleForm.append($('<input type="hidden" name="nos" value="'+ nos +'">'));
            $(document.body).append($eleForm);
            $eleForm.submit();
            }
		})
	}
	$.fn.loadkaifaGuochengXL=function(){
        var clientType = "1";
        if($("#selectBig").selectpicker("val")=="2"){
            clientType = "0";
        }
		$.ajax({
			url: getRootPath() + '/pcbList/getPcbMeasureValue',
			type: 'post',
			async: false,//是否异步，true为异步
			data: {
				'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
	        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
				'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
				'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
				'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
    			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
    			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
				'projectState':projectState,
				'clientType':clientType,
    			'pcbCategory':pcbCategory,
				'measureId':measureIds.join()
			},
			success: function (data) {
				var types=new Array();
				var month=new Array();
				var val1=new Array();
				var val2=new Array();
				
				$("#tableYanfaXL tr").remove();
				var tab = "";
				var deng = "";
				_.each(data.data,function(lists,index){
					_.each(lists,function(list,index1){
						if(list.nowDate){
							if(list.num<list.lower || list.num>list.upper){
								deng = '<div style="display: block;float: left;margin-left: 30%;margin-right: 5px;'+
								'border-radius: 50px;width: 20px;height:20px;'+
								'background-color: #f57454; "></div>';	
							}else if (list.num==list.lower || list.num==list.upper){
								deng = '<div style="display: block;float: left;margin-left: 30%;margin-right: 5px;'+
								'border-radius: 50px;width: 20px;height:20px;'+
								'background-color: #f7b547; "></div>';
							}else{
								deng = '<div style="display: block;float: left;margin-left: 30%;margin-right: 5px;'+
								'border-radius: 50px;width: 20px;height:20px;'+
								'background-color: #1adc21; "></div>';
							}
							
							tab += 
								'<tr><td>'+list.name+'</td>'+
								
								'<td>' +"<input style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:40px;text-align:center;' onclick='inputFun(this.id)' type='text' id='outTar" + list.id + "' value=' " +list.target + "' >" 
								+"<div id='alloutTar" + list.id + "' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>"
								+"<div id='popoutTar" + list.id + "' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>"
								+"<span style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>目标值</span>"
								+"<input type='text' id='inoutTar" + list.id + "' onkeydown='esc(" + list.id + ")' style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:150px;float:left;'   value='" + list.target + "'>"
								+"<button onclick='saveChangeTar(" + list.id + ")' type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>"
								+"<button onclick='concelChangeTar("+ list.id + ")' style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button>"
								+'</div></td>'+
								
								'<td>'+"<input style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:40px;text-align:center;' onclick='inputFun(this.id)' type='text' id='outLow" + list.id + "' value=' " +list.lower + "' >" 
								+"<div id='alloutLow" + list.id + "' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>"
								+"<div id='popoutLow" + list.id + "' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>"
								+"<span style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>下限</span>"
								+"<input type='text' id='inoutLow" + list.id + "' onkeydown='esc(" + list.id + ")' style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:150px;float:left;'   value='" + list.lower + "'>"
								+"<button onclick='saveChangeLow(" + list.id + ")' type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>"
								+"<button onclick='concelChangeLow("+ list.id + ")' style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button>"
								+'</div></td>'+
								
								'<td>'+"<input style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:40px;text-align:center;' onclick='inputFun(this.id)' type='text' id='outUp" + list.id + "' value=' " +list.upper + "' >" 
								+"<div id='alloutUp" + list.id + "' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>"
								+"<div id='popoutUp" + list.id + "' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>"
								+"<span style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>上限</span>"
								+"<input type='text' id='inoutUp" + list.id + "' onkeydown='esc(" + list.id + ")' style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:150px;float:left;'   value='" + list.upper + "'>"
								+"<button onclick='saveChangeUp(" + list.id + ")' type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>"
								+"<button onclick='concelChangeUp("+ list.id + ")' style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button>"
								+'</div></td>'+
								
								'<td>'+ list.projectNum +'</td>'+
								'<td>'+ deng+"<div style='display: block;float: left;line-height: 20px;'>"+list.num +'</div></td></tr>';
						
						
						}
						types[index]=list.name;
						month[index1]=list.month;
						if(list.name=="JAVA端到端生产率"){
							val1[index1]=list.num;
						}else if(list.name=="C++端到端生产率"){
							val2[index1]=list.num;
						}
					});
				});
				
				$("#tableYanfaXL").append(tab);
				chartskaifaGuochengXL(types,month,val1,val2);
			}
		});
		$("#tableYanfaXL1").bootstrapTable({
			method: 'get',
	    	contentType: "application/x-www-form-urlencoded",
	    	url: getRootPath() + '/pcbList/getPcbMeasureValueXM',
	    	striped: true, //是否显示行间隔色
	    	//dataField: 'data',
            responseHandler: function (res) {
                return {
                    "rows": res.data,
                    "total": res.totalCount
                }
            },
	    	pageNumber: 1, //初始化加载第一页，默认第一页
	    	pagination:true,//是否分页
	    	queryParamsType:'limit',
	    	sidePagination:'server',
	    	pageSize:10,//单页记录数
	    	pageList:[5,10,20,30],//分页步进值
	    	toolbar:'#toolbar',//指定工作栏
	    	toolbarAlign:'right',
	        dataType: "json",			
			queryParams : function(params){
                var clientType = "1";
                if($("#selectBig").selectpicker("val")=="2"){
                    clientType = "0";
                }
				var param = {
						'pageSize': params.limit,
						'pageNumber': params.offset,
						'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
					    'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
						'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
						'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
						'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
				    	'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
				    	'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
				    	'projectState': encodeURI(projectState),
				    	'clientType': clientType,
				    	'pcbCategory':encodeURI(pcbCategory),
						'measureId':measureIds.join()	
					}
				return param;
			},
			columns:[
	        	{
	        		title : 'PDU',
	        		field : '',
	        		align: "center",
	        		width: 120,
	        		valign:'middle',
	        		formatter:function(value,row,index){
	    				nos += "'"+row.no+"'"+",";
            			return row.pdu;
	        		} 
	            },
				{
	        		title : '项目名称',
	        		field : 'name',
	        		align: "center",
	        		width: 200,
	        		valign:'middle',	        
	        		formatter:function(value,row,index){	
	        			var path1 = getRootPath() + '/view/HTML/page.html?url='+
	        						getRootPath()+'/bu/projView?projNo=' + row.no;
            			return '<a target="_blank" style="color: #721b77;" title="'+
            					row.name +'" href="'+path1+'">'+row.name+'</a>';
	        		}
	            },
				{
	        		title : 'JAVA端到端生产率',
	        		field : 'id237',
	        		align: "center",
	        		width: 180,
	        		valign:'middle'
	            },
				{
	        		title : 'C++端到端生产率',
	        		field : 'id238',
	        		align: "center",
	        		width: 180,
	        		valign:'middle'
	            }
	        ],
			locale:'zh-CN',//中文支持
		});
		
	}
	function chartskaifaGuochengXL(types,month,val1,val2){
		//折线图
		var options1={
				//定义标题
				title:{
					
				},	
				//鼠标悬停
				tooltip: {
					trigger: 'axis'
				},
				//图标
				legend:{
					data:types
				},
				//x轴设置
				xAxis:{
					name:'月份',
					splitLine:{show: false},//去除网格线	
					data:month,
					nameTextStyle:{//设置x轴标题属性
						color:'#000000',
						fontSize:14
					},
					axisTick:{//x轴刻度
						show:false
					},
					axisLabel:{//x轴标示属性
						interval: 0,
						textStyle:{
							color:'#000000',
							fontSize:14
						}
					},
					axisLine:{//设置轴线属性
						lineStyle:{
							color:'#2e8afc',
							width:2					
						}
					}
				},
				//y轴设置
				yAxis:{
					name:'指标值',
					splitLine:{show: false},//去除网格线
					nameTextStyle:{//设置y轴标题属性
						color:'#000000',
						fontSize:16
					},
					axisTick:{//y轴刻度
						show:false
					},
					axisLabel:{//y轴标示属性
						textStyle:{
							color:'#000000',
							fontSize:16
						}
					},
					axisLine:{//设置轴线属性
						lineStyle:{
							color:'#2e8afc',
							width:2						
						}
					}
				},
				//指定图表类型
				series:[
					{
						name:types[0],
						type:'line',
						symbolSize: 5,//拐点大小
						data:val1
					},{
						name:types[1],
						type:'line',
						symbolSize: 5,//拐点大小
						data:val2
					}
				]
		}
		myCharts1.setOption(options1);
	}
    $(document).on("change", "#zrOrhwselect", function () {
        zrOrhwselectChenge();
    });
    function zrOrhwselectChenge(){
		var flag = $("#zrOrhwselect option:selected").val();
		if(flag=="0"){
			$("#hwselect").css("display","block");
			$("#zrselect").css("display","none");
			$('#user_type6').selectpicker('val', '');
			$('#user_type7').selectpicker('val', '');
			$('#user_type8').selectpicker('val', '');

		}else{
			$("#hwselect").css("display","none");
			$("#zrselect").css("display","block");
			$('#user_type3').selectpicker('val', '');
			$('#user_type4').selectpicker('val', '');
			$('#user_type5').selectpicker('val', '');
		}
			
	}

    //加载区域下拉菜单
    function initArea() {
        $.ajax({
            url: getRootPath() + '/tblArea/getAllTblArea',
            type: 'post',
            async: false,//是否异步，true为异步
            success: function (data) {
                var select_option="";
                $("#user_type1").empty();
                if(null==data.data||""==data.data) return;
                _.each(data.data, function(val, index){//(值，下标)
                    select_option += "<option value="+val.areaCode+">"+val.areaName+"</option>";
                })
                $("#user_type1").html(select_option);
                $('#user_type1').selectpicker('refresh');
                $('#user_type1').selectpicker('render');
            }
        });
    };

    function initYeWuXian() {//默认加载业务线
        $.ajax({
            url: getRootPath() + '/opDepartment/getOpDepartment',
            type: 'post',
            async: false,
            data: {
                level: 2,
            },
            success: function (data) {
                $('#user_type6').selectpicker("val", []);
                $("#user_type6").empty();
                $('#user_type6').prev('div.dropdown-menu').find('ul').empty();
                $('#user_type7').selectpicker("val", []);
                $("#user_type7").empty();
                $('#user_type7').prev('div.dropdown-menu').find('ul').empty();
                $('#user_type8').selectpicker("val", []);
                $("#user_type8").empty();
                $('#user_type8').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#user_type6").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#user_type6').selectpicker('refresh');
                $('#user_type6').selectpicker('render');
            }
        })
    };

    $(document).on("change", "#user_type6", function () {
        $.ajax({
            url: getRootPath() + '/opDepartment/getOpDepartment',
            type: 'post',
            async: false,
            data: {level: 3, ids: $("#user_type6").selectpicker("val") == null ? null : $("#user_type6").selectpicker("val").join()},
            success: function (data) {
                $('#user_type7').selectpicker("val", []);
                $("#user_type7").empty();
                $('#user_type7').prev('div.dropdown-menu').find('ul').empty();
                $('#user_type8').selectpicker("val", []);
                $("#user_type8").empty();
                $('#user_type8').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#user_type7").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#user_type7').selectpicker('refresh');
                $('#user_type7').selectpicker('render');
            }
        })
    });
    $(document).on("change", "#user_type7", function () {
        $.ajax({
            url: getRootPath() + '/opDepartment/getOpDepartment',
            type: 'post',
            async: false,
            data: {
                level: 4,
                ids: $("#user_type7").selectpicker("val") == null ? null : $("#user_type7").selectpicker("val").join()
            },
            success: function (data) {
                $('#user_type8').selectpicker("val", []);
                $("#user_type8").empty();
                $('#user_type8').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#user_type8").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#user_type8').selectpicker('refresh');
                $('#user_type8').selectpicker('render');
            }
        })
    });

    function initHWCPX() {//默认加载华为产品线
        $.ajax({
            url: getRootPath() + '/bu/getHwdept',
            type: 'post',
            async: false,
            data: {
                level: 1,
            },
            success: function (data) {
                $('#user_type3').selectpicker("val", []);
                $("#user_type3").empty();
                $('#user_type3').prev('div.dropdown-menu').find('ul').empty();
                $('#user_type4').selectpicker("val", []);
                $("#user_type4").empty();
                $('#user_type4').prev('div.dropdown-menu').find('ul').empty();
                $('#user_type5').selectpicker("val", []);
                $("#user_type5").empty();
                $('#user_type5').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#user_type3").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#user_type3').selectpicker('refresh');
                $('#user_type3').selectpicker('render');
            }
        })
    };
    $(document).on("change", "#user_type3", function () {
        $.ajax({
            url: getRootPath() + '/bu/getHwdept',
            type: 'post',
            async: false,
            data: {level: 2, ids: $("#user_type3").selectpicker("val") == null ? null : $("#user_type3").selectpicker("val").join()},
            success: function (data) {
                $('#user_type4').selectpicker("val", []);
                $("#user_type4").empty();
                $('#user_type4').prev('div.dropdown-menu').find('ul').empty();
                $('#user_type5').selectpicker("val", []);
                $("#user_type5").empty();
                $('#user_type5').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#user_type4").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#user_type4').selectpicker('refresh');
                $('#user_type4').selectpicker('render');
            }
        })
    });
    $(document).on("change", "#user_type4", function () {
        $.ajax({
            url: getRootPath() + '/bu/getHwdept',
            type: 'post',
            async: false,
            data: {
                level: 3,
                ids: $("#user_type4").selectpicker("val") == null ? null : $("#user_type4").selectpicker("val").join()
            },
            success: function (data) {
                $('#user_type5').selectpicker("val", []);
                $("#user_type5").empty();
                $('#user_type5').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#user_type5").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#user_type5').selectpicker('refresh');
                $('#user_type5').selectpicker('render');
            }
        })
    });
	function judgeRow(list){
		var flag = true;
		for(var i=3; i<list.length; i++){
			if(list[i].childNodes[0].checked==false){
				flag = false;
				break;
			}
		}
		if(flag){
			list[0].childNodes[0].checked = true;
		}else{
			list[0].childNodes[0].checked = false;
		}
	}
	
	function formatterColumn(id,value){
		if(value==0){
			return "<input type='checkbox' name='"+id+"' checked='checked'/>";
		}else if(value==1){
			return "<input type='checkbox' name='"+id+"'/>";
		}
	}
	function loadProjectData(){
		$('#tableProject').bootstrapTable({
			method: 'GET',
	    	contentType: "application/x-www-form-urlencoded",
	    	url: getRootPath() + "/projects/getPcbProject",
	    	striped: true, //是否显示行间隔色
	    	//dataField: 'data',
            responseHandler: function (res) {
                return {
                    "rows": res.data,
                    "total": res.totalCount
                }
            },
	    	pageNumber: 1, //初始化加载第一页，默认第一页
	    	pagination:true,//是否分页
	    	queryParamsType:'limit',
	    	sidePagination:'server',
	    	pageSize:10,//单页记录数
	    	pageList:[5,10,20,30],//分页步进值
	    	minimumCountColumns: 2,             //最少允许的列数
			queryParams : function(params){
				var param = {
						'pageSize': params.limit,
						'pageNumber': params.offset,
						'client': $("#zrOrhwselect option:selected").val(),
				        'name': encodeURI($("#projName").val()),
				        'pm': encodeURI($("#projMgmr").val()),
			        	'area': $("#user_type1").selectpicker("val")==null?null:$("#user_type1").selectpicker("val").join(),//转换为字符串
			        	'hwpdu': $("#user_type3").selectpicker("val")==null?null:$("#user_type3").selectpicker("val").join(),//转换为字符串
						'hwzpdu': $("#user_type4").selectpicker("val")==null?null:$("#user_type4").selectpicker("val").join(),//转换为字符串
						'pduSpdt': $("#user_type5").selectpicker("val")==null?null:$("#user_type5").selectpicker("val").join(),//转换为字符串
						'bu': $("#user_type6").selectpicker("val")==null?null:$("#user_type6").selectpicker("val").join(),//转换为字符串
	        			'pdu': $("#user_type7").selectpicker("val")==null?null:$("#user_type7").selectpicker("val").join(),//转换为字符串
	        			'du': $("#user_type8").selectpicker("val")==null?null:$("#user_type8").selectpicker("val").join(),//转换为字符串
	        			'projectState': encodeURI($("#user_type2").selectpicker("val")==null?null:$("#user_type2").selectpicker("val").join()),//转换为字符串
	        			'pcbCategory':encodeURI(pcbCategory),
	    				'measureId':measureIds.join()
					}
				return param;
			},
			onClickCell :function(field, value, row, $element){
			    if(field.indexOf("status") == 0){//全选按钮
			    	var list = $element[0].parentNode.childNodes;
			    	if($element[0].childNodes[0].checked){//全选
			    		for(var i=3; i<list.length; i++){
			    			list[i].childNodes[0].checked = true;
			    			row[list[i].childNodes[0].name] = "0";
			    		}
			    	}else{//反选
			    		for(var i=3; i<list.length; i++){
			    			list[i].childNodes[0].checked = false;
			    			row[list[i].childNodes[0].name] = "1";
			    		}
			    	};
			    }else if(field.indexOf("id") == 0){//指标按钮
			    	var list = $element[0].parentNode.childNodes;
			    	if($element[0].childNodes[0].checked){
			    		row[field] = "0";
			    		judgeRow(list);
			    	}else{
			    		row[field] = "1";
			    		judgeRow(list);
			    	}			    	
			    }else{
			        return;
			    }
			},
			columns:[
				{
	        		title:'全选',
	        		field:'status',
	        		align : "center",
	        		width : 50,
	        		formatter: function (value,row,index){
						var flag = true;
						for(var i = 0;i<measureIds.length;i++){
							var index = "id"+measureIds[i];
							if(row[index] == "1"){
								flag = false;
								break;
							}
						}
						if(flag){
							return "<input type='checkbox' name='status' checked='checked'/>";
						}else{
							return "<input type='checkbox' name='status'/>";
						}
					}
	        	},
	        	{
	        		title : '项目编码',
	        		field : 'no',
	        		visible : false
	        		
	            },
				{
	        		title : '项目名称',
	        		field : 'name',
	        		align: "center",
	        		width: 150,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return '<span title="'+value+'">'+value +'</span>';
	        		}
	            },
				{
	        		title : '项目经理',
	        		field : 'pm',
	        		align: "center",
	        		width: 80,
	        		valign:'middle'
	            },
	            {
	        		title : 'JAVA端到端生产率',
	        		field : 'id237',
	        		align: "center",
	        		width: 180,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id237',value);
	    			}
	            },
				{
	        		title : 'C++端到端生产率',
	        		field : 'id238',
	        		align: "center",
	        		width: 180,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id238',value);
	    			}
	            }
	        ],
			locale:'zh-CN'//中文支持
		});
				
	}
	//查询按钮
	$(document).on("click", "#queryBtn", function () {
		$('#tableProject').bootstrapTable('refresh');
	});
	//隐藏配置页面
	$(document).on("click", "#add_backBtn", function () {	
		$("#iteAddPage").modal('hide');
	});	
	 //配置保存   
	$(document).on("click", "#add_saveBtn", function () {  
    	var list = [];
    	var dataArr=$('#tableProject').bootstrapTable('getData');
    	for(var i=0;i<dataArr.length;i++){		
    		for(var j = 0;j<measureIds.length;j++){
    			var index = 'id'+measureIds[j];   			
    			var data ={
    				'proNo' : dataArr[i].no,
    	    		'pcbCategory':pcbCategory,
    	    		'measureId' : measureIds[j],
    	    		'status':dataArr[i][index]
    			}
    			list.push(data);    			
			}
    	}
    	
    	$.ajax({
    		url: getRootPath() + '/pcbList/editPcbProjectConfig',
    		type:'post',
    		dataType:"json",  
    	    contentType : 'application/json;charset=utf-8', //设置请求头信息 
    		data:JSON.stringify(list),
    		success:function(data){
    			if(data.code == 'success'){
    				toastr.success('配置成功！');
    				$("#iteAddPage").modal('hide'); 
    				selectOnchange();   				        				
    			}else{
    				toastr.error('配置失败!');
    			}
    		}
    	});
    });
	$(document).ready(function(){
//		myCharts1=echarts.init(document.getElementById("ceshiXL"));
//		menuChange();
		$().loadkaifaGuochengXL();
		$("#projectPage").load("pcbProjectConfig.html",function(){
			$('#user_type2').selectpicker("val",["在行"]);
            var zrOrhwselect = getCookie("zrOrhwselect");
            $('#zrOrhwselect')[0].value=zrOrhwselect;
			zrOrhwselectChenge();
			initArea();
			initHWCPX();
			initYeWuXian();

			loadProjectData();
			exportbut();
		});
		//打开配置页面
		$('#btn_add').click(function(){
			$('#tableProject').bootstrapTable('refresh');
			$('.but2').click(function(){				
				if($('#advancedQuery').css('display')=='none'){
					$('#advancedQuery').css('display','block');
				}else if($('#advancedQuery').css('display')=='block'){
					$('#advancedQuery').css('display','none');
				}
			});
			$("#iteAddPage").modal('show');			
		});	
	})		
})
function esc(a){
	if ( event.keyCode=='27' ) //->右箭头
		  {
	this.concelChangeTar(a);
	this.concelChangeLow(a);
	this.concelChangeUp(a);
		  }
}

function concelChangeTar(v){
	var pop = document.getElementById("popoutTar" + v);
	pop.style.display = "none";
	document.getElementById("alloutTar"+v).style.display = "none";
	$("#outTar" + v)[0].style.border="0px";
}

function concelChangeLow(v){
	var pop = document.getElementById("popoutLow" + v);
	pop.style.display = "none";
	document.getElementById("alloutLow"+v).style.display = "none";
	$("#outLow" + v)[0].style.border="0px";
}

function concelChangeUp(v){
	var pop = document.getElementById("popoutUp" + v);
	pop.style.display = "none";
	document.getElementById("alloutUp"+v).style.display = "none";
	$("#outUp" + v)[0].style.border="0px";
}




function saveChangeTar(val){
	$("#outTar"+val)[0].style.border="0px";
	
	var c = $("#inoutTar"+val).val();
	var b = document.getElementById("popoutTar"+val);
	b.style.display = "none";
	document.getElementById("alloutTar"+val).style.display = "none";
	var reg = /^\d+(\.\d{0,2})?$/; 

	if(!c){
		console.log(c);
		$.ajax({
            type: "post",
            url: getRootPath()+"/pcbList/updateParameterInfoById",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				id:val,
				target:$("#inoutTar"+val).attr('placeholder')
			}),
            success: function (data) {
            	console.log(data);
            }
        });
		toastr.success('保存成功！');
		document.getElementById("outTar"+val).value=document.getElementById("inoutTar"+val).placeholder;
		$("#outTar"+val)[0].style.color="red";
	}else if(!reg.test(c)){     
        toastr.warning('输入错误');
        $("#inoutTar"+val).val("");
    } else {
    	document.getElementById("outTar"+val).value=document.getElementById("inoutTar"+val).value;
    	$.ajax({
            type: "post",
            url: getRootPath()+"/pcbList/updateParameterInfoById",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				id:val,
				target:$("#inoutTar"+val).val()
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
		$("#outTar"+val)[0].style.color="red";
    	
    }
}

function saveChangeLow(val){
	$("#outLow"+val)[0].style.border="0px";
	
	var c = $("#inoutLow"+val).val();
	var b = document.getElementById("popoutLow"+val);
	b.style.display = "none";
	document.getElementById("alloutLow"+val).style.display = "none";
	var reg = /^\d+(\.\d{0,2})?$/; 

	if(!c){
		console.log(c);
		$.ajax({
            type: "post",
            url: getRootPath()+"/pcbList/updateParameterInfoById",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				id:val,
				lower:$("#inoutLow"+val).attr('placeholder')
			}),
            success: function (data) {
            	console.log(data);
            }
        });
		toastr.success('保存成功！');
		document.getElementById("outLow"+val).value=document.getElementById("inoutLow"+val).placeholder;
		$("#outLow"+val)[0].style.color="red";
	}else if(!reg.test(c)){     
        toastr.warning('输入错误');
        $("#inoutLow"+val).val("");
    } else {
    	document.getElementById("outLow"+val).value=document.getElementById("inoutLow"+val).value;
    	$.ajax({
            type: "post",
            url: getRootPath()+"/pcbList/updateParameterInfoById",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				id:val,
				lower:$("#inoutLow"+val).val()
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
		$("#outLow"+val)[0].style.color="red";
    	
    }
}


function saveChangeUp(val){
	$("#outUp"+val)[0].style.border="0px";
	
	var c = $("#inoutUp"+val).val();
	var b = document.getElementById("popoutUp"+val);
	b.style.display = "none";
	document.getElementById("alloutUp"+val).style.display = "none";
	var reg = /^\d+(\.\d{0,2})?$/; 

	if(!c){
		console.log(c);
		$.ajax({
            type: "post",
            url: getRootPath()+"/pcbList/updateParameterInfoById",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				id:val,
				Upget:$("#inoutUp"+val).attr('placeholder')
			}),
            success: function (data) {
            	console.log(data);
            }
        });
		toastr.success('保存成功！');
		document.getElementById("outUp"+val).value=document.getElementById("inoutUp"+val).placeholder;
		$("#outUp"+val)[0].style.color="red";
	}else if(!reg.test(c)){     
        toastr.warning('输入错误');
        $("#inoutUp"+val).val("");
    } else {
    	document.getElementById("outUp"+val).value=document.getElementById("inoutUp"+val).value;
    	$.ajax({
            type: "post",
            url: getRootPath()+"/pcbList/updateParameterInfoById",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				id:val,
				upper:$("#inoutUp"+val).val()
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
		$("#outUp"+val)[0].style.color="red";
    	
    }
}







function inputFun(v){
	
	var out = document.getElementById(v);
	out.style.border = "1px solid #B5B5B5;";
	var pop = document.getElementById("pop" + v);
	pop.style.display = "block";
	var popInput = document.getElementById("in"+v);
	popInput.focus();
	var len = popInput.value.length;
	if (document.selection){
		var sel = popInput.createTextRange();
		sel.collapse();
		sel.select();
	} else if(typeof popInput.selectionStart == 'number'
	    && typeof popInput.selectionEnd == 'number'){
		popInput.selectionStart = popInput.selectionEnd = len;
	}
	document.getElementById("all"+v).style.display = "block";
	$("#"+v)[0].style.border="2px solid #81D2FA";
}

function selectOnchange(){
	 $().loadkaifaGuochengXL();
	 $('#tableYanfaXL1').bootstrapTable('refresh');
}