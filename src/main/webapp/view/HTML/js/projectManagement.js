function queryFavoritePro(){
		$.ajax({
			url: getRootPath() + '/projects/queryFavoriteProject',
			type: 'post',
			data:{
				
			},
			success: function (data) {
				if (data.code=='0'){
					if(data.totalCount==0){
						$("#favoriteList").empty();
						var inner= '<div class="dev-no-data"><div class="dev-no-data-img display-inline-block" style="margin-left:220px;height: 156px;">'
							+'<img  src="/mvp/view/HTML/images/empty-box.png" style="height: 156px;"></div>'
							+'<div class="project-dev-no-data-content"></div><div class="dev-no-data-content display-inline-block"> '
							+'<div class="new-project" style="line-height:32px;font-size:18px;color:#999;">您还没有已关注的项目</div></div></div>';
						$("#favoriteList").append(inner);
					}
					else{
						var content = data.data;
						$("#favoriteList").empty();
						var inner='';
						for (var n = 0; n<data.totalCount; n++){
							if(content[n].name.length>15){
								var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + content[n].no;
								var datefomt=new Date(content[n].startDate).format('yyyy-MM-dd')
								var endfomt=new Date(content[n].endDate).format('yyyy-MM-dd')
								inner+= '<div class="swiper-slide"><div class="project-card home-page-project-card">'+
											'<div class="is-my-favorite-project">'+
											'<div class="project-card-header project-card-background-image color-1 card-scrum" tabindex="0" onclick="window.open(\''+path+'\')">'+
												'<div class="card-animation">'+		
									       		'</div>'+
									   		'</div>'+
											'<div  class="project-card-info">'+										
											    '<a target="_blank" class="over-flow-ellipsis" title="'+content[n].name +'" href="'+path+'">'+
											        '<h6>'+content[n].name+'</h6>'+
											    '</a>'+										  
										    	'<span style=" cursor: pointer;" title="取消关注" onclick="deleteFromFavortie(\''+content[n].no+'\')"><img src="/mvp/view/HTML/images/redheart.png"/></span>'+	
										        '<div class="project-card-more-info over-flow-ellipsis">'+                   
										        	'<h4 class="glyphicon glyphicon-user over-flow-ellipsis"style="float: left;"  title="PM:'+content[n].pm +'">'+content[n].pm+'</h4>'+                       
										        	'<h4 class="glyphicon glyphicon-time over-flow-ellipsis"style="float: right;" title="结束时间:'+ endfomt+'">'+endfomt+'</h4>'+
										        '</div>'+
									    	'</div>'+
									    '</div>'+
									'</div></div>';					
							}else {
							var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + content[n].no;
							var datefomt=new Date(content[n].startDate).format('yyyy-MM-dd')
							var endfomt=new Date(content[n].endDate).format('yyyy-MM-dd')
							inner+= '<div class="swiper-slide"><div class="project-card home-page-project-card">'+
										'<div class="is-my-favorite-project">'+
										'<div class="project-card-header project-card-background-image color-1 card-scrum" tabindex="0" onclick="window.open(\''+path+'\')">'+
											'<div class="card-animation">'+		
								       		'</div>'+
								   		'</div>'+
										'<div  class="project-card-info">'+										
										    '<a target="_blank" class="over-flow-ellipsis" title="'+content[n].name +'" href="'+path+'">'+
										        '<h3>'+content[n].name+'</h3>'+
										    '</a>'+										  
									    	'<span style=" cursor: pointer;" title="取消关注" onclick="deleteFromFavortie(\''+content[n].no+'\')"><img src="/mvp/view/HTML/images/redheart.png"/></span>'+	
									        '<div class="project-card-more-info over-flow-ellipsis">'+                   
									        	'<h4 class="glyphicon glyphicon-user over-flow-ellipsis"style="float: left;"  title="PM:'+content[n].pm +'">'+content[n].pm+'</h4>'+                       
									        	'<h4 class="glyphicon glyphicon-time over-flow-ellipsis"style="float: right;" title="结束时间:'+ endfomt+'">'+endfomt+'</h4>'+
									        '</div>'+
								    	'</div>'+
								    '</div>'+
								'</div></div>';
							}
						}
						$("#favoriteList").append(inner);
						
						var swiper = new Swiper('.swiper-container', {
							slidesPerView: 5,
							spaceBetween: 14,
					      	slidesPerGroup: 5,
						    loopFillGroupWithBlank: true,
						    direction: 'horizontal',
						    loop: false,
						    navigation: {
						      nextEl: '.swiper-button-next',
						      prevEl: '.swiper-button-prev',
						    },
						  });	

						;
						
					}
					
				}
				else{
					$("#favoriteList").empty();
					var inner= '<div class="dev-no-data"><div class="dev-no-data-img display-inline-block" style="margin-left:220px;height: 156px;">'
						+'<img  src="/mvp/view/HTML/images/empty-box.png" style="height: 156px;"></div>'
						+'<div class="project-dev-no-data-content"></div><div class="dev-no-data-content display-inline-block"> '
						+'<div class="new-project" style="line-height:32px;font-size:18px;color:red;">查询关注项目失败，请重新操作</div></div></div>';
					$("#favoriteList").append(inner);
				 }
				}
			
		});
	}


function addToFavortie(proNo){	
	$.ajax({
		url: getRootPath() + '/projects/addFavoriteProject',
		type: 'post',
		data:{
			proNo : proNo,
		},
		success: function (data) {
				if (data.status=='0'){
					var id = proNo+"Index";
					$("#"+id)[0].outerHTML='<a href="#" id="'+proNo+"Index"+'" style="color: #721b77;" title="取消关注" onclick="deleteFromFavortie(\''+proNo+'\')"><img src="/mvp/view/HTML/images/redheart.png"/></span></a>';
					queryFavoritePro();
				}
				else{
					alert(data.msg);
				}
				
			}
	});
}
	
function deleteFromFavortie(proNo){
	$.ajax({
		url: getRootPath() + '/projects/deleteFromFavortie',
		type: 'post',
		data:{
			proNo : proNo,
		},
		success: function (data) {
				if (data.status=='0'){
					var id = proNo+"Index";
					if($("#"+id)[0]){
						$("#"+id)[0].outerHTML='<a href="#" id="'+proNo+"Index"+'" style="color: #721b77;" title="关注" onclick="addToFavortie(\''+proNo+'\')"><img src="/mvp/view/HTML/images/heart.png"/></a>';
					}
					queryFavoritePro();
				}else{
					alert(data.msg);
				}
				
			}
	});
	
}

(function(){
	var opts1;
	var data=null;
	function loadGridData(init, sort, order, number, size){
		$('#savetoop').modal('show');
        var url = getRootPath() + "/projects/page?";
        if (sort) {
            url += "sort=" + sort + "&order=" + order;
        }
        if (size) {
            url += "pageSize=" + size + "&pageNumber=" + number;
        }

		var treePath = getQueryString("clickTreePath");
		var treeId = ""+getQueryString("clickTreeId");
		var treeIdList = treeId.split(">");
		var treeLevel = getQueryString("clickTreeLevel");
		if(treePath && treeId && treeLevel){
			if(treePath == 0){
				switch (treeLevel) {
					case "1":
						data = {
							'client': "1",
							'bu': treeIdList[0],
							'projectState': $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join())//转换为字符串
						};
						break;
					case "2":
						var treePid = getQueryString("clickTreePid");
						data = {
							'client': "1",
							'bu': treeIdList[1],
							'pdu': treeIdList[0],
							'projectState': $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join())//转换为字符串
						};
						break;
					case "3":
						var treePid = getQueryString("clickTreePid");
						data = {
							'client': "1",
							'bu': treeIdList[2],//转换为字符串
							'pdu': treeIdList[1],
							'du':treeIdList[0],
							'projectState': $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join())//转换为字符串
						};
						break;
					default:
						data = {
							'client': "1",
							'projectState': $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join())//转换为字符串
						};
						break;
				}

			}else{
				switch (treeLevel) {
					case "1":
						data = {
							'client': "0",
							'hwpdu': treeIdList[0],
							'projectState': $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join())//转换为字符串
						};
						break;
					case "2":
						var treePid = getQueryString("clickTreePid");
						data = {
							'client': "0",
							'hwpdu': treeIdList[1],
							'hwzpdu': treeIdList[0],
							'projectState': $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join())//转换为字符串
						};
						break;
					case "3":
						var treePid = getQueryString("clickTreePid");
						data = {
							'client': "0",
							'hwpdu': treeIdList[2],
							'hwzpdu': treeIdList[1],
							'pduSpdt': treeIdList[0],
                            'projectState': $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join())//转换为字符串
						};
						break;
					default:
						data = {
							'client': "0",
							'projectState': $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join())//转换为字符串
						};
						break;
				}
			}
		}else{
			data = {
				'client': $("#zrOrhwselect option:selected").val(),
				'name': encodeURI($("#projName").val()),
				'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
				'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
				'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
				'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
				'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
				'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
				'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
				'projectState': $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join())//转换为字符串
			};
		}
		   $.ajax({
		        url: url,
		        type: 'GET',
		        async: false,//是否异步，true为异步
		        data: data,
		        success: function(data){
		        	var client=$("#zrOrhwselect option:selected").val();
		        	var titleUnit = {};
		            var options = {
		                rownumbers: false,
		                striped: false,
		                pagination : true,
//		                colWidths: colWidths,
		                nowrap: true,
						pagePosition : 'bottom',
						singleSelect:true,
						rowStyler:function(index,row){
							return 'background-color:#ffffff;';
						},
						//onSortColumn: function(field, order){
						//	loadGridData(false, field, order);
						//},
			            onLoadSuccess:function(data){
			                $('#projSummaryTable').datagrid('doCellTip',{cls:{},delay:1000, titleUnit:titleUnit, headerOverStyle:true});
			            }
		            };
		            columns:[[
		                {
		                    width:250, //当 fitColumns:true时，columns里的width变为改列宽度占表格总宽度大小的比例
		                },
		            ]];
		            titleUnit = 'title';
		            if(client==1 && data.loginer==1){
		            	 var titles = [
							{
								"field": "myNo",
								"title": "序号",
							},
		            		{
	                            "field": "name",
	                            "title": "项目名称"
	                        },
	                        {
	                            "field": "pm",
	                            "title": "项目经理"
	                        },
	                        {
	                            "field": "area",
	                            "title": "地域"
	                        },
	                        {
	                            "field": "hwpdu",
	                            "title": "华为产品线"
	                        },
	                        {
	                            "field": "hwzpdu",
	                            "title": "子产品线"
	                        },
	                        {
	                            "field": "pduSpdt",
	                            "title": "PDU/SPDT"
	                        },
	                        {
	                            "field": "bu",
	                            "title": "业务线"
	                        },
	                        {
	                            "field": "pdu",
	                            "title": "事业部"
	                        },
	                        {
	                            "field": "du",
	                            "title": "交付部"
	                        },
	                        {
	                            "field": "type",
	                            "title": "计费类型"
	                        },
	                        {
	                            "field": "projectState",
	                            "title": "项目状态"
	                        },
	                        {
	                        	"field": "startStr",
	                        	"title": "项目启动时间"
	                        },
	                        {
	                        	"field": "endStr",
	                        	"title": "项目结项时间"
	                        }

	                    ];
		            }else{
	                    var titles = [
							{
								"field": "myNo",
								"title": "序号",
							},
	                        {
	                            "field": "name",
	                            "title": "项目名称"
	                        },
	                        {
	                            "field": "pm",
	                            "title": "项目经理"
	                        },
	                        {
	                            "field": "area",
	                            "title": "地域"
	                        },
	                        {
	                            "field": "hwpdu",
	                            "title": "华为产品线"
	                        },
	                        {
	                            "field": "hwzpdu",
	                            "title": "子产品线"
	                        },
	                        {
	                            "field": "pduSpdt",
	                            "title": "PDU/SPDT"
	                        },
	                        {
	                            "field": "type",
	                            "title": "计费类型"
	                        },
	                        {
	                            "field": "projectState",
	                            "title": "项目状态"
	                        },
	                        {
	                        	"field": "startStr",
	                        	"title": "项目启动时间"
	                        },
	                        {
	                        	"field": "endStr",
	                        	"title": "项目结项时间"
	                        }

	                    ];
		            }

		            	if(client==0 ||data.loginer!=1 ){
		            		 _.each(titles, function(val, index){
		 		            	val.sortable = false;
		 		            	val.width = 8 + '%';
		 		            	val.height = '56px';
		 		            	val.align= 'left';
		 		            	if(val.title == "项目名称"){
		                             val.width = 25 + '%';
		 		            		val.formatter = function(val, row){
		 		            			var path1 = getRootPath() + '/view/HTML/page.html?projNo=' + row.no;
		 		            			return '<a target="_parent" style="color: #721b77;" title="'+val +'" href="'+path1+'">'+val+'</a>';
		 		            			//return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="' +getRootPath()+ '/bu/projView?projNo=' + row['项目编码'] + '&a='+Math.random()+'">'+val+'</a>';
		 		            		};
		 		            	}

		 		            	if(val.title == "序号"){
									val.width = 5.6 + '%';
									val.formatter= function(value, row, index) {
										if (!opts1) {
											opts1 = $('#projSummaryTable').datagrid('options');
										}
										return opts1.pageSize * (opts1.pageNumber - 1) + index + 1;
									}
								}

				            	//列宽调整
				            	if(val.title == "项目经理" || val.title == "地域" ||
				            			val.title == "项目状态" || val.title == "计费类型"){
		                            val.width = 7 + '%';
				            	}

				            	if(val.title == "华为产品线"){
		                            val.width = 8 + '%';
				            	}

				            	if(val.title == "子产品线"){
		                            val.width = 8 + '%';
				            	}

				            	if(val.title == "PDU/SPDT"){
		                            val.width = 10 + '%';
				            	}
								 if(val.title == "项目启动时间" || val.title == "项目结项时间" ){
									 val.width = 8+ '%';
								 }
				            });
		            	}else if(client==1  && data.loginer==1){
		            		 _.each(titles, function(val, index){
			 		            	val.sortable = false;
			 		            	val.width = 6 + '%';
			 		            	val.height = '56px';
			 		            	val.align= 'left';
			 		            	if(val.title == "项目名称"){
			                            val.width = 20 + '%';
			 		            		val.formatter = function(val, row){
			 		            			var path1 = getRootPath() + '/view/HTML/page.html?projNo=' + row.no;
			 		            			return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path1+'">'+val+'</a>';
			 		            			//return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="' +getRootPath()+ '/bu/projView?projNo=' + row['项目编码'] + '&a='+Math.random()+'">'+val+'</a>';
			 		            		};
			 		            	}

									 if(val.title == "序号"){
										 val.width = 4.6 + '%';
										 val.formatter= function(value, row, index) {
											 if (!opts1) {
												 opts1 = $('#datagrid_1').datagrid('options');
											 }
											 return opts1.pageSize * (opts1.pageNumber - 1) + index + 1;
										 }
									 }

					            	//列宽调整
					            	if(val.title == "项目经理" || val.title == "地域" ||
					            			val.title == "项目状态" || val.title == "计费类型"){
			                            val.width = 5.5 + '%';
					            	}
					            	if(val.title == "项目启动时间" || val.title == "项目结项时间" ){
			                            val.width = 8+ '%';
					            	}
					            	if(val.title == "华为产品线"){
					            		val.width = 6 + '%';
					            	}
					            	if(val.title == "子产品线"){
			                            val.width = 6 + '%';
					            	}

					            	if(val.title == "PDU/SPDT"){
			                            val.width = 6 + '%';
					            	}
					            	if(val.title == "事业部" ||val.title == "业务线" ||val.title == "交付部"){
			                            val.width = 6 + '%';
					            	}
					            });
		            	}
		            options.columns = [titles];
                    options.pageNumber = parseInt(data.pageNumber)+1;

		            $('#projSummaryTable').datagrid(options);

                    var gridDatas = {};
                    $.extend(true, gridDatas, {"rows": data.data, "total": data.totalCount});
                    $('#projSummaryTable').datagrid("loadData", gridDatas);

					var p = $('#projSummaryTable').datagrid('getPager');
					$(p).pagination({
						pageSize : data.pageSize,// 每页显示的记录条数，默认为10
                        //current_page : parseInt(data.pageNumber)+1,
						pageList : [10,20,30],// 可以设置每页记录条数的列表
						beforePageText : '第',// 页数文本框前显示的汉字
						afterPageText : '页    共 {pages} 页',
						displayMsg : '显示 {from} 到 {to} 条记录  &nbsp;&nbsp; 总共 {total} 条记录',
                        //pageNumber: parseInt(data.pageNumber)+1,
                        onSelectPage: function(pageNumber, pageSize){
                            loadGridData(false, null, null, pageNumber-1, pageSize);
                        },
					});
                    $(p).pagination('refresh', {pageNumber: parseInt(data.pageNumber) + 1});

		            if(data.totalCount==0){
		            	showPopover("未查询到数据!");
		            }else{
		            	$('#savetoop').modal('hide');
		            }
		        }
		    })
	}

    function setCookie(name, value, time) {
        var exp = new Date();
        exp.setTime(exp.getTime() + time * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    }
    
    function bindQueryEvent() {
        $("#queryBtn").click(function () {
            setCookie("name", $("#projName").val(), 60);
            setCookie("area", $("#usertype1").selectpicker("val") == null ? null : $("#usertype1").selectpicker("val").join(), 60);
            setCookie("hwpdu", $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join(), 60);
            setCookie("hwzpdu", $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join(), 60);
            setCookie("pduSpdt", $("#usertype5").selectpicker("val") == null ? null : $("#usertype5").selectpicker("val").join(), 60);
            setCookie("bu", $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join(), 60);
            setCookie("pdu", $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join(), 60);
            setCookie("du", $("#usertype8").selectpicker("val") == null ? null : $("#usertype8").selectpicker("val").join(), 60);

            loadGridData(false);
        })
    };
	
	function bindExportEvent(){
        $("#exportBtn").click(function(){
    		var $eleForm = $("<form method='POST'></form>");
            $eleForm.attr("action",getRootPath() + "/export/downloadWeekly");
            $(document.body).append($eleForm);
                //提交表单，实现下载
            $eleForm.submit();
    	})
    	$("#exportIndex").click(function(){
    		var $eleForm = $("<form method='POST'></form>");
            $eleForm.attr("action",getRootPath() + "/export/downloadIndex");
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
	
	$(document).on("change", "#importInput", function () {
		$("#filePathInfo").val($(this).val());
	});
	
	$(document).on("change", "input:radio[name='radio1']", function () {
		if($("#work:checked").length<=0){
			$("#worktime").css('display','none');
		}else{
			$("#worktime").css('display','block');
		}
		//361周期选择
		if($("#361:checked").length<=0){
			$("#361time").css('display','none');
		}else{
			$("#361time").css('display','block');
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

    $(document).on("change", "#zrOrhwselect", function () {
        zrOrhwselectChenge();
	});
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
	};


	$(document).ready(function(){
		$('#usertype2').selectpicker("val",["在行"]);
		var zrOrhwselect = getCookie("zrOrhwselect");
        $('#zrOrhwselect')[0].value=zrOrhwselect;
		zrOrhwselectChenge();


		bindQueryEvent();
		bindExportEvent();

		$('#queryBtn').click();
		showDateTime();
		$('.modal-backdrop').addClass("disabled");
        queryFavoritePro();
		
		
	})
})()
