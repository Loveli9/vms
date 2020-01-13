(function(){
	
	var titleUnit = {};
	var data=null;

	function initMaturityAssessment() {//默认加载361成熟度
		$.ajax({
			url: getRootPath() + '/project/initMaturityAssessment',
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
	        },
			success: function (data) {
				$("#sumPros").text(data.sumPros);
				$("#redPros").text(data.redPros);
				$("#yellowPros").text(data.yellowPros);
				$("#greenPros").text(data.greenPros);
				$("#noPros").text(data.noPros);
			}
		});
	};
	
	function loadGridData(init, sort, order){
		$('#savetoop').modal('show');
		var url = getRootPath() + "/projectInfo/list";
		if(sort){
			url += "?sort="+ sort + "&order=" + order;
		}
		   $.ajax({
		        url: url,
		        type: 'GET',
		        async: false,//是否异步，true为异步,
		        data: {
		        	'name': encodeURI($("#projName").val()),
		        	'pm': encodeURI($("#projMgmr").val()),
		        	'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
		        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
					'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
					'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
					'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
        			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
        			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
        			'projectState': encodeURI($("#usertype2").selectpicker("val")==null?null:$("#usertype2").selectpicker("val").join())//转换为字符串
		        },
		        success: function(data){
		            var options = {
		                rownumbers: false,
		                striped: true,
		                pagination : true,
		                nowrap: true,
						pagePosition : 'bottom',
						onSortColumn: function(field, order){
							loadGridData(false, field, order);
						},
			            onLoadSuccess:function(data){   
			                $('#projSummaryTable').datagrid('doCellTip',{cls:{},delay:1000, titleUnit:titleUnit, headerOverStyle:true});   
			            }  
		            };
		            columns:[[
		                {
		                    width:250, //当 fitColumns:true时，columns里的width变为改列宽度占表格总宽度大小的比例
		                },          
		            ]]
		            titleUnit = data.titleUnit;
                    var wdth = Math.round(100 / (data.gridTitles.length + 2));
                    var lastWd = wdth * (data.gridTitles.length + 2);
		            lastWd = 100 > lastWd ? (wdth + 100 - lastWd) : ( wdth - lastWd + 100);
		           
		            _.each(data.gridTitles, function(val, index){
		            	val.sortable = false;
		            	val.width = wdth + '%';
		            	if(val.title == "项目名称"){
                            val.width = wdth * 3 + '%';
		            		val.formatter = function(val, row){
		            			return '<a style="color: #721b77;" title="'+val +'" href="' +getRootPath()+ '/bu/projView?projNo=' + row['项目编码'] + '&a='+Math.random()+'">'+val+'</a>';
		            		};
		            	}
		            });
		           
		            data.gridTitles[data.gridTitles.length -1].width = lastWd  + '%';
		            options.columns = [data.gridTitles];
		            $('#projSummaryTable').datagrid(options).datagrid('clientPaging');

					var p = $('#projSummaryTable').datagrid('getPager');
					$(p).pagination({
						pageSize : 20,// 每页显示的记录条数，默认为10
						pageList : [20,30,40],// 可以设置每页记录条数的列表
						beforePageText : '第',// 页数文本框前显示的汉字
						afterPageText : '页    共 {pages} 页',
						displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
					});
		        	var gridDatas = {};
		        	$.extend(true, gridDatas, data.gridDatas);
		            $('#projSummaryTable').datagrid("loadData", gridDatas);
		            if(data.gridDatas.total==0){
		            	showPopover("未查询到数据!");
		            }else{
		            	$('#savetoop').modal('hide');
		            }
		        }
		    })
	}

	function bindQueryEvent(){
		$("#queryBtn").click(function(){
			$.ajax({
				url: getRootPath() + '/bu/queryMess',
				type: 'post',
				async: false,//是否异步，true为异步
				data: {
					'name': $("#projName").val(),
		        	'pm': $("#projMgmr").val(),
		        	'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
		        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
					'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
					'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
					'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
					'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
					'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join()//转换为字符串
				},
				success: function () {
					
				}
			});
			initMaturityAssessment();
			loadGridData(false);
		})
	};
	
	function bindExportEvent(){
		$("#exportBtn").click(function(){
			var $eleForm = $("<form method='POST'></form>");
			var areaVal=$("#usertype1").selectpicker("val")==null?"":$("#usertype1").selectpicker("val").join();
			var projectStateVal=$("#usertype2").selectpicker("val")==null?"":$("#usertype2").selectpicker("val").join();
			var hwpduVal=$("#usertype3").selectpicker("val")==null?"":$("#usertype3").selectpicker("val").join();
			var hwzpduVal=$("#usertype4").selectpicker("val")==null?"":$("#usertype4").selectpicker("val").join();
			var pduSpdtVal=$("#usertype5").selectpicker("val")==null?"":$("#usertype5").selectpicker("val").join();
			var buVal=$("#usertype6").selectpicker("val")==null?"":$("#usertype6").selectpicker("val").join();
			var pduVal=$("#usertype7").selectpicker("val")==null?"":$("#usertype7").selectpicker("val").join();
			var duVal=$("#usertype8").selectpicker("val")==null?"":$("#usertype8").selectpicker("val").join();
            $eleForm.attr("action",getRootPath() + "/bu/download");
            $eleForm.append($('<input type="hidden" name="name" value="'+ $("#projName").val() +'">'));
            $eleForm.append($('<input type="hidden" name="pm" value="'+ $("#projMgmr").val() +'">'));
            $eleForm.append($('<input type="hidden" name="area" value="'+ areaVal +'">'));
            $eleForm.append($('<input type="hidden" name="projectState" value="'+ projectStateVal +'">'));
            $eleForm.append($('<input type="hidden" name="hwpdu" value="'+ hwpduVal +'">'));
            $eleForm.append($('<input type="hidden" name="hwzpdu" value="'+ hwzpduVal +'">'));
            $eleForm.append($('<input type="hidden" name="pduSpdt" value="'+ pduSpdtVal +'">'));
            $eleForm.append($('<input type="hidden" name="bu" value="'+ buVal +'">'));
            $eleForm.append($('<input type="hidden" name="pdu" value="'+ pduVal +'">'));
            $eleForm.append($('<input type="hidden" name="du" value="'+ duVal +'">'));
            $(document.body).append($eleForm);
            //提交表单，实现下载
            $eleForm.submit();
		})
	};
	function showPopover(msg) {
		$('#dataAcquisition').text(msg);
		$('#submitImportmodalfooter').css('display','none');
		$('#savetoop').modal('show');
		//2秒后消失提示框
		var id = setTimeout(function () {
			$('#savetoop').modal('hide');
		}, 2000);
	}
	function getCookie(name){
	   var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	   if(arr != null){
	     return decodeURIComponent(arr[2]); 
	   }else{
	     return null;
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
				$("#usertype1").empty();
				if(null==data.data||""==data.data) return;
				_.each(data.data, function(val, index){//(值，下标)
					select_option += "<option value="+val.areaCode+">"+val.areaName+"</option>";
				})
				$("#usertype1").html(select_option);
			}
		});
	};
	
	//获取部门信息树数据
	function returnDep(pid,url){
		var dataMsg;
		$.ajax({
	        url: url,
	        type: 'POST',
	        data: {"pId":pid},
	        async: false,
	        success: function(data){
	        	if(data.data!=[]&&null!=data.data){
	        		if(data.data.children&&null!=data.data.children){
		        		dataMsg=data.data.children;
	        		}
	        	}else{
                    dataMsg='';
                }
	        }        
		});	
		return dataMsg;
	}
	
	function initYeWuXian() {//默认加载业务线
		var url = getRootPath() + "/opDepartment/getOpDepartmentByCurrentUser";
		data=returnDep(0,url);
        if(''==data) return;
		var select_option="";
		$("#usertype6").empty();
		_.each(data, function(val, index){//(值，下标)
			select_option += "<option value="+val.id+">"+val.name+"</option>";
		})
		$("#usertype6").html(select_option);
			
	};
	
	function querySYB(){//根据业务线选择结果显示事业部
		$("#usertype6").change(function(){
			getZhongruanSYBData();
		});
	};
	
	function queryJFB(){//根据事业部选择结果显示交付部
		$("#usertype7").change(function(){
			getZhongruanJFBData();
		});
    };
	
    function getZhongruanSYBData(){//根据业务线选择结果显示事业部		
    	$("#usertype7").empty();
		var ul_li="";
		var select_option="";
		$('#usertype7').selectpicker("val",[]);
		$("#usertype7").empty();
		$('#usertype7').prev('div.dropdown-menu').find('ul').empty();
		$('#usertype8').selectpicker("val",[]);
		$("#usertype8").empty();
		$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
		$('#usertype3').selectpicker("val",[]);
		$('#usertype4').selectpicker("val",[]);
		$("#usertype4").empty();
		$('#usertype4').prev('div.dropdown-menu').find('ul').empty();
		$('#usertype5').selectpicker("val",[]);
		$("#usertype5").empty();
		$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
		var valId=$("#usertype6").selectpicker("val");
		for(var i=0;i<valId.length;i++){
			_.each(data, function(val, index){//(值，下标)
				if(valId[i]==val.id){
					_.each(val.children, function(val1, index1){//(值，下标)
						ul_li += "<li data-original-index="+index1+"><a tabindex='0' class data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+val1.name+"</span><span class='glyphicon glyphicon-ok check-mark'></span></a></li>" 
     					select_option += "<option value="+val1.id+">"+val1.name+"</option>";
					});
				};			
			});
		};
		$('#usertype7').prev('div.dropdown-menu').find('ul').html(ul_li);
		$("#usertype7").html(select_option);
		$('#usertype7').selectpicker('refresh');  
        $('#usertype7').selectpicker('render');
    };
	function getZhongruanJFBData(){//根据事业部选择结果显示交付部		
		$("#usertype8").empty();
		var ul_li="";
		var select_option="";
		$('#usertype8').selectpicker("val",[]);
		$("#usertype8").empty();
		$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
		var valId=$("#usertype7").selectpicker("val");
		for(var i=0;i<valId.length;i++){
			_.each(data, function(val, index){//(值，下标)		
				_.each(val.children, function(val1, index1){//(值，下标)
					if(valId[i]==val1.id){
						_.each(val1.children, function(val2, index2){//(值，下标)
							ul_li += "<li data-original-index="+index2+"><a tabindex='0' class data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+val2.name+"</span><span class='glyphicon glyphicon-ok check-mark'></span></a></li>"
							select_option += "<option value="+val2.id+">"+val2.name+"</option>";
						});
					}
				});
								
			});
		}			
		$('#usertype8').prev('div.dropdown-menu').find('ul').html(ul_li);
		$("#usertype8").html(select_option); 
		$('#usertype8').selectpicker('refresh');  
        $('#usertype8').selectpicker('render');
	};
	
	function initHWCPX() {//默认加载华为产品线
		$.ajax({
			url: getRootPath() + '/bu/getHWCPX',
			type: 'post',
			async: false,//是否异步，true为异步
			success: function (data) {
				var select_option="";
				$("#usertype3").empty();
				_.each(data, function(val, index){//(值，下标)
					select_option += "<option value="+data[index].HWCPXID+">"+data[index].HWCPXNAME+"</option>";
				})
				$("#usertype3").html(select_option);
			}
		});
	};	

	function queryZCPX(){//根据华为产品线选择结果显示子产品线
		$("#usertype3").change(function(){
			$.ajax({
				url: getRootPath() + '/bu/getZCPX',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					hwcpxval: $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join()//转换为字符串
				},
				success: function (data) {
					$('#usertype4').selectpicker("val",[]);
					$("#usertype4").empty();
					$('#usertype4').prev('div.dropdown-menu').find('ul').empty();
					$('#usertype5').selectpicker("val",[]);
					$("#usertype5").empty();
					$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
					$('#usertype6').selectpicker("val",[]);
					$('#usertype7').selectpicker("val",[]);
					$("#usertype7").empty();
					$('#usertype7').prev('div.dropdown-menu').find('ul').empty();
					$('#usertype8').selectpicker("val",[]);
					$("#usertype8").empty();
					$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
					for (var i = 0; i < data.length; i++) {  
			            $('#usertype4').append("<option value=" + data[i].ZCPXID + ">" + data[i].ZCPXNAME + "</option>");  
			        } 
			        $('#usertype4').selectpicker('refresh');  
			        $('#usertype4').selectpicker('render'); 
				}
			});			
		})
	};

	function queryPDUorSPDT(){//根据子产品线选择结果显示PDU/SPDT
		$("#usertype4").change(function(){
			$.ajax({
				url: getRootPath() + '/bu/getPDUorSPDT',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					zcpxval: $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join()//转换为字符串
				},
				success: function (data) {
					$('#usertype5').selectpicker("val",[]);
					$("#usertype5").empty();
					$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
					for (var i = 0; i < data.length; i++) {  
			            $('#usertype5').append("<option value=" + data[i].PDUID + ">" + data[i].PDUNAME + "</option>");  
			        } 
			        $('#usertype5').selectpicker('refresh');  
			        $('#usertype5').selectpicker('render'); 
				}
			});			
		})
	};
	
	//根据华为产品线选择结果显示子产品线
	function getZCPXData(){
		$.ajax({
			url: getRootPath() + '/bu/getZCPX',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				hwcpxval: $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join()//转换为字符串
			},
			success: function (data) {
				$('#usertype4').selectpicker("val",[]);
				$("#usertype4").empty();
				$('#usertype5').selectpicker("val",[]);
				$("#usertype5").empty();
				for (var i = 0; i < data.length; i++) {  
		            $('#usertype4').append("<option value=" + data[i].ZCPXID + ">" + data[i].ZCPXNAME + "</option>");  
		        } 
		        $('#usertype4').selectpicker('refresh');  
		        $('#usertype4').selectpicker('render'); 
			}
		});
	}
	//根据子产品线选择结果显示PDU/SPDT		
	function getPDUorSPDTData(){//根据子产品线选择结果显示PDU/SPDT		
		$.ajax({
			url: getRootPath() + '/bu/getPDUorSPDT',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				zcpxval: $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join()//转换为字符串
			},
			success: function (data) {
				$('#usertype5').selectpicker("val",[]);
				$("#usertype5").empty();
				for (var i = 0; i < data.length; i++) {  
		            $('#usertype5').append("<option value=" + data[i].PDUID + ">" + data[i].PDUNAME + "</option>");  
		        } 
		        $('#usertype5').selectpicker('refresh');  
		        $('#usertype5').selectpicker('render'); 
			}
		});
	}
	
	String.prototype.endWith=function(str){    
		  var reg=new RegExp(str+"$");    
		  return reg.test(this);       
	};
	
	$(document).on("change", "#importInput", function () {
		$("#filePathInfo").val($(this).val());
	});
	
	$(document).on("change", "input:radio[name='radio1']", function () {
		if($("#work:checked").length<=0){
			$("#worktime").css('display','none');
		}else{
			$("#worktime").css('display','block');
		}
	});
	
	$(document).on("click", "#submitImport", function () {
		if($("input[name='radio1']:checked").length<=0){
			alert("请至少选中一个模板");
			return;
		}
		var filePath = $("#filePathInfo").val();
		if(filePath == ''){
			alert("请先选择要导入的文件!");
		}else if(!(filePath.endWith('.xlsx') || filePath.endWith('.xls'))){
			alert("文件格式需为xlsx或者xls");
		}else{	
			var option = {
				url: getRootPath() + $("input[name='radio1']:checked").val(),
				type: 'POST',
				dataType: 'json',
				data:{
				},
				success: function(data){
						 alert($("input[name='radio1']:checked").attr("th_value")+"成功！");
				}
			};
			$('#uploadDialog').modal('hide');
			$("#importForm").ajaxSubmit(option);
		}
		
	});
	
	$(document).on("click", "#templateDownload", function () {
		if($("input[name='radio1']:checked").length<=0){
			alert("请至少选中一个模板");
			return;
		}
		var downloadUrl = getRootPath() + '/project/projectTemplate?templateName='+$("input[name='radio1']:checked").attr("templateName");
		$("#templateDownload").attr('href', downloadUrl);
	});

	$(document).ready(function(){
		$('#usertype2').selectpicker("val",["在行"]);
		initArea();
		initHWCPX();
		initYeWuXian();
		queryZCPX();
		querySYB();
		queryPDUorSPDT();
		queryJFB();

		initMaturityAssessment();
		bindQueryEvent();
		bindExportEvent();

		$('#queryBtn').click();
		showDateTime();
		$('.modal-backdrop').addClass("disabled");
	})
})()