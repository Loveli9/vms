$(function(){
//	var data=null;

	//查询团队
	$('#projSummaryTable').bootstrapTable({
		method: 'get',
    	contentType: "application/x-www-form-urlencoded; charset=UTF-8",
     	url : getRootPath() + '/teamInfo/teams',
        editable:false,// 可行内编辑
    	striped: true, // 是否显示行间隔色
    	responseHandler: function (res) {
    		return {
		        "rows": res.data,
		        "total": res.totalCount
	        }
        },
    	pageNumber: 1, // 初始化加载第一页，默认第一页
    	pagination:true,// 是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,// 单页记录数
    	pageList:[10,20,30],// 分页步进值
    	showColumns:false,
        dataType: "json",
        queryParams : function(params){
            var param = {
                'pageSize': params.limit,
                'pageNumber': params.offset,
                 teamName:encodeURI($("#projName").val()),
                 tm:encodeURI($("#projMgmr").val())
			}
			return param;
		},
		columns:[
			{
				title : '序号',
				field : 'teamId',
				align: "center",
				valign:'middle',
			},
			{
				title : '团队名称',
				field : 'teamName',
				width: '100%',
				align: "left",
				valign:'middle',
				formatter:function(val, row){
					  var path1 = getRootPath() + '/view/HTML/page.html?gl=2&url='
					  +getRootPath()+'/bu/teamView?teamId=' + row.teamId;
					  return '<a target="_parent" style="color: #721b77;" title="'+val +'" href="'+path1+'">'+val+'</a>';
				}
			},
			{
				title : '团队经理',
				field : 'tm',
				align: "left",
				valign:'middle'
			},
			{
				title : '团队人数',
				field : 'teamSize',
				align: "left",
				valign:'middle'
			}
		 ],
		 locale:'zh-CN'//中文支持
    });
	
	/*function loadGridData(init, sort, order){
		$('#savetoop').modal('show');
		var url = getRootPath() + "/teamInfo/teams";
		if(sort){
			url += "?sort="+ sort + "&order=" + order;
		}
		   $.ajax({
		        url: url,
		        type: 'GET',
		        async: false,//是否异步，true为异步
		        data: {
		        	'teamName': encodeURI($("#projName").val()),
		        	'tm': encodeURI($("#projMgmr").val()),
		        	'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
		        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
					'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
					'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
					'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
        			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
        			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
                    'clientType': $("#zrOrhwselect option:selected").val(),
		        },
		        success: function(data){
		        	var titleUnit = {};
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
		                    width:150, //当 fitColumns:true时，columns里的width变为改列宽度占表格总宽度大小的比例
		                },          
		            ]]
		            titleUnit = data.titleUnit;
                    var wdth = Math.round(100 / (data.gridTitles.length + 2));
                    var lastWd = wdth * (data.gridTitles.length + 2);
		            lastWd = 100 > lastWd ? (wdth + 100 - lastWd) : ( wdth - lastWd + 100);
		            _.each(data.gridTitles, function(val, index){
		            	val.sortable = false;
		            	val.width = wdth + '%';
		            	val.height = '52px';
		            	val.align= 'left';
		            	if(val.title == "团队名称"){
                            val.width = wdth * 3 + '%';
		            		val.formatter = function(val, row){
		            			var path1 = getRootPath() + '/view/HTML/page.html?gl=2&url='+getRootPath()+'/bu/teamView?teamNo=' + row['团队编码'];
		            			return '<a target="_parent" style="color: #721b77;" title="'+val +'" href="'+path1+'">'+val+'</a>';
		            			//return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="' +getRootPath()+ '/bu/teamView?teamNo=' + row['团队编码'] + '&a='+Math.random()+'">'+val+'</a>';
		            		};
		            	}

                        if(val.title == "质量"){
                            //val.width = wdth * 3 + '%';
                            val.formatter = function(val, row){
                            	if("no"==val){
                                    return '<a style="background: #bdb7b7;margin-left:46px;display: block;width: 10px;height: 10px;border-radius: 10px;" title="'+val +'"></a>';
								}else if("red"==val){
                                    return '<a style="background: #f57454;margin-left:46px;display: block;width: 10px;height: 10px;border-radius: 10px;" title="'+val +'"></a>';
								}else if("yellow"==val){
                                    return '<a style="background: #f7b547;margin-left:46px;display: block;width: 10px;height: 10px;border-radius: 10px;" title="'+val +'"></a>';
                                }else if("green"==val){
                                    return '<a style="background: #1adc21;margin-left:46px;display: block;width: 10px;height: 10px;border-radius: 10px;" title="'+val +'"></a>';
                                }
                            };
                        }
		            });
		           
		            data.gridTitles[data.gridTitles.length -1].width = lastWd  + '%';
		            options.columns = [data.gridTitles];
		            options.pageNumber = 1;
		            $('#projSummaryTable').datagrid(options).datagrid('clientPaging');
					var p = $('#projSummaryTable').datagrid('getPager');
					$(p).pagination({
						pageSize : 10,// 每页显示的记录条数，默认为10
						pageList : [10,20,30],// 可以设置每页记录条数的列表
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
	}*/
	
	$('#queryBtn').click(function(){
    	$('#projSummaryTable').bootstrapTable('refresh', {url: getRootPath() + '/teamInfo/teams'});
    });
//	function bindQueryEvent(){
//		$("#queryBtn").click(function(){
//			
//		})
//	};
	
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
            $eleForm.append($('<input type="hidden" name="tm" value="'+ $("#projMgmr").val() +'">'));
            $eleForm.append($('<input type="hidden" name="area" value="'+ areaVal +'">'));
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
		/*if($("input[name='radio1']:checked").length<=0){
			alert("请至少选中一个模板");
			return;
		}*/
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
						 $('#queryBtn').click();
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
    /*$(document).on("change", "#zrOrhwselect", function () {
        zrOrhwselectChenge();
    })
	function zrOrhwselectChenge(){
		var flag = $("#zrOrhwselect option:selected").val();
		if(flag=="0"){
			$("#hwselect").css("display","block");
			$("#zrselect").css("display","none");
			$('#usertype6').selectpicker('val', '');
			$('#usertype7').selectpicker('val', '');
			$('#usertype8').selectpicker('val', '');
		}else{
			$("#hwselect").css("display","none");
			$("#zrselect").css("display","block");
			$('#usertype3').selectpicker('val', '');
			$('#usertype4').selectpicker('val', '');
			$('#usertype5').selectpicker('val', '');
		}

	};*/
	
	$(document).ready(function(){
		/*$('#usertype2').selectpicker("val",["在行"]);
        var zrOrhwselect = getCookie("zrOrhwselect");
        $('#zrOrhwselect')[0].value=zrOrhwselect;
		zrOrhwselectChenge();*/

//		bindQueryEvent();
		bindExportEvent();

//		$('#queryBtn').click();
		showDateTime();
		$('.modal-backdrop').addClass("disabled");
	})
})