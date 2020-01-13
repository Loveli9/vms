var po = "";
var zr = "";
var hw = "";
var demoList = "";
var pmZR = "";
var rank = new Array(6);
$(function(){
	 var projNo = window.parent.projNo;
	 
//    var projNo = getQueryString("projNo");
	var userManagerlocalnum=1;
	var projectestartDate="2018-01-01";
	var projectendDate="2018-01-01";
	var roles="";
	var ranks="";

	//分页功能
    function callBackPagination(data) {
        var totalCount = data.length
  
        if(totalCount==0){
        	$("#callBackPager").hide()
        }
         var  limit = 10 ;

        operationTable(1,limit, totalCount,data);

        $('#callBackPager').extendPagination({
            totalCount: totalCount,
//            showCount: showCount,
            limit: limit,
            callback: function (curr, limit, totalCount) {
                var index = curr-1
                $("#userManagerlocal").find("#tbody_"+index).show().siblings().hide()
            }
        });

    }
    //分割数组按照预定页数分组显示
    function sliceArr(array, size) {
        var result = [];
        for (var x = 0; x < Math.ceil(array.length / size); x++) {
              var start = x * size;
                 var end = start + size;
                  result.push(array.slice(start, end));
              }
          return result;
      }
    
    //
    function operationTable(currPage, limit, total,data) {
    	var str2 = ""
    	var container = sliceArr(data,limit)
    	//var currPageData = sliceArr(data,limit)[currPage-1]
    	//var pageData=sliceArr(data,limit);
    	for(var j=0;j<container.length;j++){
    		var tab="";
    		for(var i=0;i<container[j].length;i++){
    			var zraccount="";
//    			if(data[i].ZR_ACCOUNT&&data[i].ZR_ACCOUNT.length>6){
//    				zraccount = data[i].ZR_ACCOUNT.substring(data[i].ZR_ACCOUNT.length-6,data[i].ZR_ACCOUNT.length)
//    			}
    			tab += '<tr class="trtable">'+
    				'<td name="usernum" date-type="no" style="width: 30px;">'+userManagerlocalnum+'</td>'+
    				'<td date-type="input" name="staffname" style="width: 50px;">'+(container[j][i].NAME == undefined ? "" : container[j][i].NAME)+'</td>'+
    				'<td date-type="text" name="zrAccount" style="width: 80px;">'+container[j][i].ZR_ACCOUNT+'</td>'+
    	            '<td date-type="input" name="hwAccount" style="width: 80px;">'+(container[j][i].HW_ACCOUNT == "undefined" || container[j][i].HW_ACCOUNT == undefined || container[j][i].HW_ACCOUNT == "NA" ? "" : container[j][i].HW_ACCOUNT)+'</td>'+
    	            /*'<td date-type="input" name="svnGitNo" style="width: 100px;">'+(container[j][i].SVN_GIT_NO == null || container[j][i].SVN_GIT_NO == undefined ? "" : container[j][i].SVN_GIT_NO)+'</td>'+*/
    	            '<td style="width: 100px;">'+'<input id="svnGitNo" list="demoList" name="svnGitNo" onchange="changeValue(this)" style="width: 100px; text-align: center; padding-left: 16px;" value="'+(container[j][i].SVN_GIT_NO == null || container[j][i].SVN_GIT_NO == undefined ? "" : container[j][i].SVN_GIT_NO)+'" />'+
    	            '<datalist id="demoList" style="display:none;">'+demoList+'</datalist>'+
    	            '</td>'+
    	            '<td date-type="select" select-date="'+roles+'" style="width: 80px;">'+(container[j][i].ROLE == "" || container[j][i].ROLE == null ? "请选择" : container[j][i].ROLE)+'</td>'+
    	            '<td date-type="select" select-date="'+ranks+'" style="width: 46px;">'+(container[j][i].RANK == "" || container[j][i].RANK == null ? "请选择" : container[j][i].RANK)+'</td>'+
    	            '<td date-type="date" name="startDate" style="width: 91px;">'+(container[j][i].START_DATE == "NaN-aN-aN" || container[j][i].START_DATE == undefined ? "" : new Date(container[j][i].START_DATE).format('yyyy-MM-dd'))+'</td>'+
    	            '<td date-type="date" name="endDate" style="width: 91px;">'+(container[j][i].END_DATE == "NaN-aN-aN" || container[j][i].END_DATE == undefined ? "" : new Date(container[j][i].END_DATE).format('yyyy-MM-dd'))+'</td>'+
    	            '<td date-type="select" select-date="'+status+'" style="width: 38px;">'+(container[j][i].STATUS == undefined || container[j][i].STATUS == "" ? "请选择" : container[j][i].STATUS)+'</td>'+
    				'<td date-type="no" style="width: 92px;"><a name="del" style="cursor: pointer;">移除</a>&nbsp&nbsp&nbsp<a name="resetRank" style="cursor: pointer;">重置职级</a></td>'+
    		    	'</tr>';
    			userManagerlocalnum += 1;
    			
    		}
    		str2+="<tbody id='tbody_"+j+"'>"+tab+"</tbody>"
    	}
		$("#userManagerlocal").html(str2);
		$("#userManagerlocal tbody").hide();
		$("#userManagerlocal tbody").first().show();

    }
    
    function date(){ 	
    	alert(new date())
    }
	
	//请求全部团队员工
	function getSelectedUser(){
		var tab=""
		$.ajax({
			url: getRootPath() + '/user/getProjectMembers',
			type: 'get',
			data:{
				projNo : projNo
			},
			success: function (data) {
				userManagerlocalnum=1;
				$("#userManagerlocal tbody").html("");
				callBackPagination(data,userManagerlocalnum);
			}
		});
	}
	
	//查询团队下的项目
	$(document).on("click", "#multiplex", function () {
		var table=$('#tableProjectListXM').bootstrapTable({
			method: 'get',
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			url : getRootPath() + '/qualityModel/queryProjectByTeam',
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
			singleSelect: true,// 单选checkbox
			queryParamsType:'limit',
			sidePagination:'server',
			pageSize:5,// 单页记录数
			pageList:[5,10,20,30],// 分页步进值
			showColumns:false,
			dataType: "json",
			queryParams : function(params){
				var param = {
						'pageSize': params.limit,
						'pageNumber': params.offset,
						'projectId':projNo
				}
				return param;
			},
			columns:[
				[
					{checkbox: true},
					{title : '项目名称',field : 'name',align: "center",width: 190,valign:'middle'},
					{title : '项目经理',field : 'pm',align: "center",width: 40,valign:'middle'}
					]
				],
				locale:'zh-CN'//中文支持
		});
	});
	
	//指标复用按钮
    $('#multiplex').click(function(){
    	$("#inheritPage").modal('show');
    });
    
    //项目指标配置单选按钮
    $('#project').click(function(){
    	$("#inheritForm").modal('show');
    });
    
    //团队指标配置单选按钮
    $('#team').click(function(){
    	$("#inheritForm").modal('hide');
    	$(document.body).css({
    	    "overflow-x":"hidden",
    	    "overflow-y":"hidden"
    	});
    });
    
    //流程指标配置继承
    $('#insertBtn').click(function(){
    	if($('input[name="config"]:checked').val() == "project") {
	    	var data=$("#tableProjectListXM").bootstrapTable('getSelections');
	    	if(data.length==0){
	    		toastr.warning('请选择一个项目!');
	    	}else{
	    		//通过团队下的项目进行指标继承
	    		$.ajax({
	 				url:getRootPath()+'/user/modifyProcessIndicator',
	 				type : 'post',
	 				data:{
	 					proNo:projNo,
	 					oldNo:data[0].no	
	 				},
	 				success:function(data){
	 					//后台返回成功
	 					if(data.code=='success'){
	 						$("#inheritPage").modal('hide');
	 						location.reload();
	 						var val = saveMeasureConfig();
	 		            	if(val == 0){
	 		            		toastr.success('流程指标配置复用成功！');
	 		            	}else{
	 		            		toastr.error('历史数据保存异常!');
	 		            	}
	 					}
	 					else{
	 						toastr.error('流程指标配置复用失败!');
	 					}
	 				}
	 			});
	    	}
		}else{
			//通过团队进行指标继承
			$.ajax({
 				url:getRootPath()+'/user/modifyProcessIndicatorByTeam',
 				type : 'post',
 				data:{
 					proNo:projNo
 				},
 				success:function(data){
 					//后台返回成功
 					if(data.code=='success'){
 						$("#inheritPage").modal('hide');
 						location.reload();
 						var val = saveMeasureConfig();
 		            	if(val == 0){
 		            		toastr.success('流程指标配置复用成功！');
 		            	}else{
 		            		toastr.error('历史数据保存异常!');
 		            	}
 					}
 					else{
 						toastr.error('流程指标配置复用失败!');
 					}
 				}
 			});
		}
    });
    
    //指标复用页面的返回按钮
    $('#backBtn').click(function(){
    	$("#inheritPage").modal('hide');
    	$('.modal-backdrop').remove();
    });
    
    //查询团队下的项目
    $(document).on("click", "#multiplexMember", function () {
    	var memberTable=$('#tableMemberListXM').bootstrapTable({
    		method: 'get',
    		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    		url : getRootPath() + '/qualityModel/queryProjectByTeam',
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
    		singleSelect: true,// 单选checkbox
    		queryParamsType:'limit',
    		sidePagination:'server',
    		pageSize:5,// 单页记录数
    		pageList:[5,10,20,30],// 分页步进值
    		showColumns:false,
    		dataType: "json",
    		queryParams : function(params){
    			var param = {
    					'pageSize': params.limit,
    					'pageNumber': params.pageNumber,
    					'projectId':projNo
    			}
    			return param;
    		},
    		columns:[
    			[
    				{checkbox: true},
    				{title : '项目名称',field : 'name',align: "center",width: 190,valign:'middle'},
    				{title : '项目经理',field : 'pm',align: "center",width: 40,valign:'middle'}
    				]
    			],
    			locale:'zh-CN'//中文支持
    	});
    });
    
    //成员复用按钮
    $('#multiplexMember').click(function(){
    	$("#inheritMemberPage").modal('show');
    });
    
    //项目成员单选按钮
    $('#projectMember').click(function(){
    	$("#inheritMemberForm").modal('show');	
    });
    
    //团队成员单选按钮
    $('#teamMember').click(function(){
    	$("#inheritMemberForm").modal('hide');
    	$(document.body).css({
    	    "overflow-x":"hidden",
    	    "overflow-y":"hidden"
    	});
    });

    //成员继承
    $('#insertMemberBtn').click(function(){
    	if($('input[name="memberConfig"]:checked').val() == "projectMember") {
	    	var data=$("#tableMemberListXM").bootstrapTable('getSelections');
	    	if(data.length==0){
	    		toastr.warning('请选择一个项目!');
	    	}else{
	    		//通过团队下的项目进行成员继承
	    		$.ajax({
	 				url:getRootPath()+'/user/inheritProjectMember',
	 				type : 'get',
	 				data:{
	 					proNo:projNo,
	 					oldNo:data[0].no	
	 				},
	 				success:function(data){
	 					//后台返回成功
	 					if(data.code == "success"){
 			              $("#inheritMemberPage").modal('hide');
 			              $('.modal-backdrop').remove();
 			              $('#tdcyinfo').click();
 			              $('#callBackPager').show();
 			            }else{
 			              $("#inheritMemberPage").modal('hide');
 			              $('.modal-backdrop').remove();
 			              toastr.error('无项目成员可以复用!');
 			            }
	 				}
	 			});
	    	}
		}else{
			//通过团队进行成员继承
			$.ajax({
 				url:getRootPath()+'/user/inheritTeamMember',
 				type : 'get',
 				data:{
 					proNo:projNo
 				},
 				success:function(data){
 					//后台返回成功
 					if(data.code == "success"){
			            $("#inheritMemberPage").modal('hide');
			            $('#tdcyinfo').click();
			            $('#callBackPager').show();
		           }else{
			            $("#inheritMemberPage").modal('hide');
			            toastr.error('无团队成员可以复用!');
		           }
 				}
 			});
		}
    });
    
    //成员复用页面的返回按钮
    $('#backMemberBtn').click(function(){
    	$("#inheritMemberPage").modal('hide');
    	$('.modal-backdrop').remove();
    });
    
	//自动继承流程指标
	function inheritProcessIndicator(){
		$.ajax({
			url:getRootPath()+"/user/autoModifyProcessIndicator",
			type : 'post',
			data:{
				proNo : projNo
			},
			success:function(data){
				//后台返回成功修改新项目的流程指标配置
				if(data.code=='success'){
					location.reload();
				}
			}
		});
	}
	/*function modifyProcessIndicator(){
		$.ajax({
			url:getRootPath()+"/user/getProjectNo",
			type : 'post',
			data:{
				projNo : projNo
			},
			success:function(data){
				$.ajax({
					url:getRootPath()+"/user/modifyProcessIndicator",
					type : 'post',
					data:{
						newNo:projNo,
						oldNo:data
					},
					success:function(data){
						//后台返回成功修改新项目的流程指标配置
						if(data.code=='success'){
							toastr.success('修改新项目的流程指标配置成功！');
						}
						else{
							toastr.error('修改新项目的流程指标配置失败!');
						}
					}
				});
			}
		});
	}*/
	
	//手工指标录入
	$(document).on("click", "a[id='sgzbinfo']", function () {		
		$.ajax({
			url: getRootPath() + '/iteration/getMessage',
			type: 'post',
			data:{
				proNo : projNo
			},
			success: function (data) {
				
				var cyclelist = "";
				var sgzbColumns = [];
				var list = data.data;				
				var t1 = {title:'序号',align:'center',width:60,
						formatter:function(value, row, index) {
						    var pageSize=$('#sgzbtab').bootstrapTable('getOptions').pageSize;
							var pageNumber=$('#sgzbtab').bootstrapTable('getOptions').pageNumber;
							 return pageSize * (pageNumber - 1) + index + 1;
							}
					}
				sgzbColumns.push(t1);
				var t2 = {field: 'measure_id', title: '指标ID', visible:false,width:130};
				sgzbColumns.push(t2);
				var t3 = {field: 'measure_name', title: '指标名称', align: 'center',width:240};
				sgzbColumns.push(t3);
				var t4 = {field: 'unit', title: '单位', align: 'center',width:100};
				sgzbColumns.push(t4);
				var cycle = [];
				for (var i = 0; i < list.length; i++) {			
                    var temp = createColumns(list[i]);
                    cycle.push(list[i].id);
                    sgzbColumns.push(temp);
                }	
				cyclelist = cycle.join(',');
				createTable(sgzbColumns,cyclelist);
			}
		});
		
	});
	//团队成员
	$(document).on("click", "a[id='tdcyinfo']", function () {
		demoList = "";
		$.ajax({
			url : getRootPath() + "/manpowerBudget/getManpowerBudgetByProNo",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				proNo : projNo,
			},
			success : function(data) {
		
				if(data.data==null){
					$("#kaifaCount").val(""),
					$("#keyroleCount").val("")
				}else{
					$("#kaifaCount").val(data.data.headcount),
					$("#keyroleCount").val(data.data.keyRoleCount)
				}
			}
		})
		$("#OMPdata").css("display","none");
		$("#userManager").children().css("width","600px");
		//$("#actualMember").css("width","100%");
		$("#openOMPdata").css("display","block");
		$.ajax({
			url : getRootPath() + "/bu/projOverviewData",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				no : projNo
			},
			success : function(data) {
				projectstartDate = new Date(data.startDate).format('yyyy-MM-dd');
				projectendDate = new Date(data.endDate).format('yyyy-MM-dd');
			}
		});
		$.ajax({
			url : getRootPath() + "/codeMaster/getCodeMasterOrderByValue",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				codekey : "role"
			},
			success : function(data) {
				roles="请选择,";//
				var len = data.rows.length-1;
				_.each(data.rows, function(val, index){
					if(len!=index){
						roles = roles+val.name+",";
					}else{
						roles = roles+val.name;
					}
				});
			}
		});
		//成员状态
		$.ajax({
			url : getRootPath() + "/codeMaster/getMemberStatusValue",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				
			},
			success : function(data) {
				status="请选择,";
				var len = data.rows.length-1;
				_.each(data.rows, function(val, index){
					if(len!=index){
						status = status+val.NAME+",";
					}else{
						status = status+val.NAME;
					}
				});
			}
		});
		//成员职级
		$.ajax({
			url : getRootPath() + "/codeMaster/getMemberRankValue",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				
			},
			success : function(data) {
				ranks="请选择,";
				var len = data.rows.length-1;
				_.each(data.rows, function(val, index){
					if(len!=index){
						ranks = ranks+val.VALUE+",";
					}else{
						ranks = ranks+val.VALUE;
					}
				});
			}
		});
		// 团队成员SVN/GIT账号
		$.ajax({
			url : getRootPath() + "/codeMaster/getMemberSVN",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				no : projNo
			},
			success : function(data) {
				_.each(data.rows, function(val, index){
					demoList += '<option value="'+val.AUTHOR+'" text="'+val.AUTHOR+'">';
				});
			}
		});
		getSelectedUser();
		$(document).on("click", "#openOMPdata11", function () {
			$.ajax({
				url: getRootPath() + '/user/getOMPUser',
				type: 'post',
				data:{
					projNo : projNo
				},
				success: function (data) {
					$("#userManagerOMP").empty();
					for(var i=0;i<data.length;i++){
						var tab = '<tr class="trtable">'+
						'<td>'+"<input type='checkbox' name='checkbox' id='checkbox'>"+'</td>'+
						'<td>'+data[i].NAME+'</td>'+
						'<td>'+data[i].AUTHOR+'</td>'+
						'<td>'+data[i].ROLE+'</td>'+
						'</tr>';
						$("#userManagerOMP").append(tab);
					}
				}
			});
		});
	});
	//移除成员
	$(document).on("click", "a[name=del]", function () {
		var tds = $(this).parents('tr:first')[0].childNodes;
		$.ajax({
			url: getRootPath() + '/user/getOMPUserByAuthor',
			type: 'post',
			data:{
				projNo : projNo,
				author : tds[2].innerText
			},
			success: function (data) {
				var hwAccount = tds[3].innerText
				var rows = $('#userManagerOMP tr');
				for(var i = 0; i<rows.length; i++ ){
					if(hwAccount==rows[i].cells[3].innerHTML){
						rows[i].remove();
					}
				}
				if(data.AUTHOR==null){
					return
				}
				var tab = '<tr class="trtable">'+
					'<td>'+"<input type='checkbox' name='checkbox' id='checkbox'>"+'</td>'+
					'<td>'+data.NAME+'</td>'+
					'<td>'+data.AUTHOR+'</td>'+
					'<td>'+data.ROLE+'</td>'+
			    	'</tr>';
				$("#userManagerOMP").append(tab);
			}
		});
		
		$(this).parents('tr:first').remove();
		userManagerlocalnum -=1;
		var usernumtd =  $('#userManagerlocal td[name="usernum"]');
		for(var i=0;i<usernumtd.length;i++){
			$(usernumtd[i]).text(i+1);
		}
		
	});
	$(document).on("dblclick", "#userManagerOMP tr", function () {
		var tds = $(this)[0].childNodes;
		var hwAccount = tds[1].innerText;
		$(this).remove();
		var rows = $('# tr');
		var startDate = projectstartDate;
		var endDate = projectendDate;
		for(var i = 0; i<rows.length; i++ ){
			if(hwAccount==rows[i].cells[3].innerHTML){
				if(CompareDate(rows[i].cells[8].innerHTML,startDate)&&CompareDate(endDate,rows[i].cells[8].innerHTML)){
					startDate=rows[i].cells[8].innerHTML;
				}
				if(CompareDate(rows[i].cells[9].innerHTML,startDate)&&CompareDate(endDate,rows[i].cells[9].innerHTML)){
					endDate=rows[i].cells[9].innerHTML;
				}
				rows[i].remove();
			}
		}
		var tab = '<tr class="trtable">'+
		'<td name="usernum" date-type="no" style="width: 30px;">'+num+'</td>'+
		'<td date-type="input" name="staffname" style="width: 50px;">'+tds[1].innerText+'</td>'+
		'<td date-type="input" name="zrAccount" style="width: 80px;"></td>'+
		'<td date-type="input" name="hwAccount" style="width: 80px;">'+tds[2].innerText+'</td>'+
		'<td date-type="input" style="width: 100px;"></td>'+
		'<td date-type="select" select-date="'+roles+'" style="width: 80px;">'+tds[3].innerText+'</td>'+
		'<td date-type="date" name="startDate" style="width: 120px;">'+startDate+'</td>'+
		'<td date-type="date" name="endDate" style="width: 120px;">'+endDate+'</td>'+
		'<td date-type="no" style="width: 92px;"><a name="del">移除</a></td>'+
    	'</tr>';
		$("#userManagerlocal").append(tab);
		userManagerlocalnum +=1;
	});
	
	//添加omp数据
	$.fn.tianjia0=function(){
		var rows0 = $('#userManagerOMP tr');
		for(var j = 0; j<rows0.length; j++ ){
			var tds = $(rows0[j])[0].childNodes;
			var click = $(tds[0].childNodes[0]).prop("checked");
			if(click){
				var hwAccount = tds[1].innerText;
		  		$(rows0[j]).remove();
		  		var rows = $('#userManagerlocal tr');
		  		var startDate = projectstartDate;
		  		var endDate = projectendDate;
		  		for(var i = 0; i<rows.length; i++ ){
		  			if(hwAccount==rows[i].cells[3].innerHTML){
		  				if(CompareDate(rows[i].cells[8].innerHTML,startDate)&&CompareDate(endDate,rows[i].cells[8].innerHTML)){
		  					startDate=rows[i].cells[8].innerHTML;
		  				}
		  				if(CompareDate(rows[i].cells[9].innerHTML,startDate)&&CompareDate(endDate,rows[i].cells[9].innerHTML)){
		  					endDate=rows[i].cells[9].innerHTML;
		  				} 
		  				rows[i].remove();
		  			}
		  		}
		  		var tab = '<tr class="trtable">'+
		  		'<td name="usernum" date-type="no" style="width: 30px;">'+userManagerlocalnum+'</td>'+
		  		'<td date-type="input" name="staffname" style="width: 50px;">'+tds[1].innerText+'</td>'+
		  		'<td date-type="input" name="zrAccount" style="width: 80px;"></td>'+
		  		'<td date-type="input" name="hwAccount" style="width: 80px;">'+tds[2].innerText+'</td>'+
		  		'<td date-type="input" style="width: 100px;"></td>'+
		  		'<td date-type="select" select-date="'+roles+'" style="width: 80px;">'+tds[3].innerText+'</td>'+
		  		'<td date-type="date" name="startDate" style="width: 120px;">'+startDate+'</td>'+
		  		'<td date-type="date" name="endDate" style="width: 120px;">'+endDate+'</td>'+
		  		'<td date-type="no" style="width: 92px;"><a name="del">移除</a></td>'+
		      	'</tr>';
		  		$("#userManagerlocal").append(tab);
		  		userManagerlocalnum +=1;    	        
			}
			
		}
    }
	
	//导入omp数据
	function addAllOmpClick(){
		$("#addAllOmp").click(function() {
			var allOmpDate = $("#userManagerOMP tr");
			 _.each(allOmpDate, function(ompdate, index){
				 var tds = $(ompdate)[0].childNodes;
					var hwAccount = tds[1].innerText;
					$(ompdate).remove();
					var rows = $('#userManagerlocal tr');
					var startDate = projectstartDate;
					var endDate = projectendDate;
					for(var i = 0; i<rows.length; i++ ){
						if(hwAccount==rows[i].cells[3].innerHTML){
							if(CompareDate(rows[i].cells[8].innerHTML,startDate)&&CompareDate(endDate,rows[i].cells[8].innerHTML)){
								startDate=rows[i].cells[8].innerHTML;
							}
							if(CompareDate(rows[i].cells[9].innerHTML,startDate)&&CompareDate(endDate,rows[i].cells[9].innerHTML)){
								endDate=rows[i].cells[9].innerHTML;
							}
							rows[i].remove();
						}
					}
					var tab = '<tr class="trtable">'+
					'<td name="usernum" date-type="no" style="width: 30px;">'+userManagerlocalnum+'</td>'+
					'<td date-type="input" name="staffname" style="width: 50px;">'+tds[1].innerText+'</td>'+
					'<td date-type="input" name="zrAccount" style="width: 80px;"></td>'+
					'<td date-type="input" name="hwAccount" style="width: 80px;">'+tds[2].innerText+'</td>'+
					'<td date-type="input" style="width: 100px;"></td>'+
					'<td date-type="select" select-date="'+roles+'" style="width: 80px;">'+tds[3].innerText+'</td>'+
					'<td date-type="date" name="startDate" style="width: 120px;">'+startDate+'</td>'+
					'<td date-type="date" name="endDate" style="width: 120px;">'+endDate+'</td>'+
			    	'<td date-type="no" style="width: 92px;"><a name="del">移除</a></td>'+

			    	'</tr>';
					$("#userManagerlocal").append(tab);
					userManagerlocalnum +=1;
			 });
		});
	}
	$(document).on("click", "#userManagerlocal td", function () {
		var td = $(this);
	    editable(td);
	});
	function editable(td){
		if(td[0].childNodes.length>1){
			return;
		}
		var dateType = td.attr("date-type");
		var tableHeader = td.attr("name");
		var text = td.text();
		var txt = null;
		if(dateType=="input"){
			txt = $("<input class='form-control' style='width: 100%;height: 20px;padding: 0px;font-size: 12px;text-align: center;' type='text'>").val(text);
		}else if(dateType=="date"){
			txt = $("<input class='form-control' max='9999-12-30' style='width: 100%;height: 20px;padding: 0px;font-size: 12px;text-align: center;' type='date'>").val(text);
		}else if(dateType=="no"){
			return;
		}else if(dateType=="text"){
			txt = text;
		}else if(dateType=="select"){
				var text = td.text();
				var select = "<select class='form-control' style='width: 100%;height: 20px;padding: 0px;font-size: 12px;text-align: center;'>";
				var selectDate = td.attr("select-date").split(',');
				selectDate.forEach(function(i,index){
					select += "<option>"+i+"</option>";
				})
				select += "</select>";
				txt = $(select).val(text);
		}
		// 根据表格文本创建文本框 并加入表表中--文本框的样式自己调整
		if (null != txt) {
			txt.blur(function(){
			// 失去焦点，保存值。于服务器交互自己再写,最好ajax
			var newText = $(this).val();
			
			if("zrAccount" == tableHeader && null != newText){
				rank = projectMemberEcho(newText);
				if(!(null == rank || null == rank[0] || "" == rank[0])){
					td.parents('tr:first')[0].cells[6].innerText = rank[0];
				}
				if(!(null == rank || null == rank[1] || "" == rank[1])){
					td.parents('tr:first')[0].cells[9].innerText = rank[1];
				}
				if(!(null == rank || null == rank[2] || "" == rank[2])){
					td.parents('tr:first')[0].cells[5].innerText = rank[2];
				}
				if(!(null == rank || null == rank[4] || "" == rank[4])){
					td.parents('tr:first')[0].cells[3].innerText = rank[4];
				}
				if(!(null == rank || null == rank[5] || "" == rank[5])){
					td.parents('tr:first')[0].cells[1].innerText = rank[5];
				}
			}
			
			if(dateType=="date"){
				if(newText==null||newText==""){
					toastr.error("时间信息不能为空,默认输入项目起止时间");
					newText = updateDate(td,newText);
				}
				if(CompareDate(projectstartDate,newText)){
					toastr.error("时间信息小于项目启动时间,默认输入项目起止时间");
					newText = updateDate(td,newText);
				}
				if(CompareDate(newText,projectendDate)){
					toastr.error("时间信息大于项目结束时间,默认输入项目结束时间");
					newText = updateDate(td,newText);
				}
				if(td.attr("name")=="startDate" && CompareDate(newText,td.parents('tr:first').children('td[name="endDate"]').text())){
					toastr.error("离开项目时间不能小于加入项目时间,默认输入项目开始时间");
					newText = projectstartDate;
				}
				if(td.attr("name")=="endDate" && CompareDate(td.parents('tr:first').children('td[name="startDate"]').text(),newText)){
					toastr.error("离开项目时间不能小于加入项目时间,默认输入项目结束时间");
					newText = projectendDate;
				}
			}else if(dateType == "input"){
				if(tableHeader == "staffname"){
					if(newText == null || newText == ""){
						toastr.error("姓名不能为空！");
					}else if(!checkMemberData(newText, '姓名')){
						toastr.error("姓名只能包含汉字！");
					}
				}else if(tableHeader == "zrAccount"){
					if(newText == null || newText == ""){
						toastr.error("中软工号不能为空！");
					}else if(!checkMemberData(newText, '中软工号')){
						toastr.error("中软工号只能包含数字！");
					}
				}else if(tableHeader == "hwAccount"){
					if(newText == null || newText == ""){
						toastr.error("华为工号不能为空！");
					}else if(!checkMemberData(newText, '华为工号')){
						toastr.error("华为工号只能包含字母和数字！");
					}
				}
				/*else if(tableHeader == "svnGitNo"){
					if(!(newText == null || newText == "") && !checkMemberData(newText, '华为工号')){
						toastr.error("SVN/GIT账号只能包含字母和数字！");
					}
				}*/
			}
			// 移除文本框,显示新值
			$(this).remove();
			//td.text(newText);
			td.text(newText==null?"请选择":newText);
			if(dateType=="input"){
				$("#testDIV").fadeOut();
			}
			if (text == newText){
				return;
			}
			getOMPUserByAuthor(td,td.parents('tr:first')[0].cells[2].innerText);
			if(td[0].cellIndex==3){
				getOMPUserByAuthor(td,text);
			}
				
			}); 
		} 
		td.text("");
		td.append(txt);
		$(txt).focus();
		if(dateType=="input"){
			$(txt).select();
			testDIVdo(td,txt);
		}
	}
	function CompareDate(d1,d2){
	  return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
	}
	function updateDate(td,newText){
		if(td.attr("name")=="startDate"){
			newText = projectstartDate;
		}else if (td.attr("name")=="endDate"){
			newText = projectendDate;
		}
		return newText;
	}
	function getOMPUserByAuthor(td,author){
		$.ajax({
			url: getRootPath() + '/user/getOMPUserByAuthor',
			type: 'post',
			data:{
				projNo : projNo,
				author : author
			},
			success: function (data) {
				var flag = 0;
				
				if(data.NAME==td.parents('tr:first')[0].cells[2].innerHTML
						&&data.AUTHOR==td.parents('tr:first')[0].cells[3].innerHTML
						&&data.ROLE==td.parents('tr:first')[0].cells[6].innerHTML
						){
					flag = 1;
				}
				var rows = $('#userManagerOMP tr');
				for(var i = 0; i<rows.length; i++ ){
					if(data.AUTHOR==rows[i].cells[1].innerHTML){
						rows[i].remove();
					}
				}
				if(flag == 0){
					if(data.AUTHOR==null){
						return
					}
					var tab = '<tr class="trtable">'+
						'<td>'+data.NAME+'</td>'+
						'<td>'+data.AUTHOR+'</td>'+
						'<td>'+data.ROLE+'</td>'+
				    	'</tr>';
					$("#userManagerOMP").append(tab);
				}
			}
		});
	}
	
	$(document).on("click", "#testDIVtable td", function () {
		var selected = $(this).text();
		var value = selected.split("\t");
		var num = $('#testDIVtable').attr("num");
		$($('td[name="staffname"]')[num-1]).text(value[0]);
		$($('td[name="hwAccount"]')[num-1]).text(value[1]);
		$($('td[name="zrAccount"]')[num-1]).text(value[2]);
	});
	function testDIVdo(td,txt) {
		if(td.attr("name")=="staffname"){
			txt.keyup(function() {
				if(txt.val()!=""){
					$.ajax({
						url : getRootPath() + "/bu/getZRaccountByName",
						type : 'POST',
						dataType: "json",
						data : {
							name:txt.val()
						},
						success : function(data) {
							$("#testDIV").empty();
							var select_option="<table id='testDIVtable' num="+td[0].parentNode.rowIndex+" class='mousetable' style='font-size: 14px;border-spacing: 0px;'>";
							_.each(data, function(val, index){//(值，下标)
								var zraccount="";
								if(val.zrAccount&&val.zrAccount.length>6){
									zraccount = val.zrAccount.substring(val.zrAccount.length-6,val.zrAccount.length)
								}
								var value = val.name+"\t"+val.account+"\t"+zraccount;
								select_option += "<tr><td style='width: 160px'>"+value+"</td></tr>";
							})
							select_option +="</table>"
		                    $("#testDIV").append(select_option);
						}
					})
					var offset = txt.offset();
		        	$("#testDIV").css('left', offset.left + 'px')
		                     .css('top', offset.top+txt[0].clientHeight + 'px')
		                     .css('position', 'absolute')
		                     .css('background', '#ffffff')
		                     .css('z-index', '9999')
		                     .fadeIn();
				}
	        });
	        $(window).keyup(function(event) {
				
				switch (event.keyCode) {  
				case 13://回车
					$("#testDIV").fadeOut();
					txt.blur();
					break;  
				}  
	        });
			
		}
	}

	function openOMPdataClick(){
		$("#openOMPdata").click(function() {
			$("#OMPdata").css("display","block");
			$("#actualMember").css("width","60%");
			$("#openOMPdata").css("display","none");
		});
	}
	function hideOMPdataClick(){
		$("#hideOMPdata").click(function() {
			$("#OMPdata").css("display","none");
			$("#actualMember").css("width","778px");
			$("#openOMPdata").css("display","block");
		});
	}
	function addUserManagerClick(){
		var rows = $('#userManagerlocal tbody td');
		$("#addUserManager").click(function() {
			var tab = '<tr class="trtable">'+
			'<td name="usernum" date-type="no" style="width: 30px;"">'+userManagerlocalnum+'</td>'+
			'<td date-type="input" name="staffname" style="width: 50px;"></td>'+
			'<td date-type="input" name="zrAccount" style="width: 80px;"></td>'+
			'<td date-type="input" name="hwAccount" style="width: 80px;"></td>'+
			/*'<td date-type="input" name="svnGitNo" style="width: 100px;"></td>'+*/
			'<td style="width: 100px;">'+'<input list="demoList" onchange="changeValue(this)" name="svnGitNo" style="width: 100px;">'+
            '<datalist id="demoList" style="display:none;"></datalist>'+
            '</td>'+
			'<td date-type="select" select-date="'+roles+'" style="width: 80px;">请选择</td>'+
			'<td date-type="select" select-date="'+ranks+'" style="width: 46px;">请选择</td>'+
			'<td date-type="date" name="startDate" style="width: 91px;">'+projectstartDate+'</td>'+
			'<td date-type="date" name="endDate" style="width: 91px;">'+projectendDate+'</td>'+
			'<td date-type="select" select-date="'+status+'" style="width: 38px;">请选择</td>'+
	    	'<td date-type="no" style="width: 92px;"><a name="del" style="cursor: pointer;">移除</a>&nbsp&nbsp&nbsp<a name="resetRank" style="cursor: pointer;">重置职级</a></td>'+
	    	'</tr>';
			if($("#userManagerlocal tbody").length==0){
				var str = "<tbody>"+tab+"</tbody>"
					$("#userManagerlocal").append(str)
			}else{
				$("#actualMember tbody").last().append(tab);
			}
			userManagerlocalnum += 1;
			
			var fenyechili = $("#callBackPager li");
			$(fenyechili[fenyechili.length-3]).click();;
		});
	}	
	//保存添加的数据
	function saveUserManagerClick(){
		$("#saveUserManager").click(function() {
			var ret = tableSaveToDao();
			if(ret=="err"){
				toastr.error("华为工号不能有重复的，请修改之后再保存");
				return;
			}else if(ret == "verifyFalse"){
				return;
			}
			if($("#kaifaCount").val() < 0 || $("#keyroleCount").val() < 0){
				toastr.error("请输入正确的人数");
			}else{
				$.ajax({
					url : getRootPath() + "/manpowerBudget/add",
					type : 'POST',
					async: false,//是否异步，true为异步
					data : {
						'proNo' : projNo,
						'headcount' : $("#kaifaCount").val(),
						'keyRoleCount' : $("#keyroleCount").val()
					},
					success : function(data) {	
						 toastr.success("保存信息成功");
					}
				})	
				$("#tdcyinfo").click();
			}	
		});
	}

	function tableSaveToDao() {
		var rows = $('#userManagerlocal tbody tr');
		var allTr = "";
		var hwAccounts=new Array()
		for(var i = 0; i<rows.length; i++ ){
			hwAccounts[i]=rows[i].cells[3].innerHTML;
			var startDate = rows[i].cells[7].innerHTML;
			if(startDate==null||startDate=="NaN-aN-aN" || startDate == ""){
				startDate = projectstartDate;
			}
			var endDate = rows[i].cells[8].innerHTML;
			if(endDate==null||endDate=="NaN-aN-aN" || endDate == ""){
				endDate = projectendDate;
			}
			var roleValue=rows[i].cells[5].innerHTML;
			if(roleValue=="请选择"||roleValue=="null"){
				roleValue="";
			}
			var rankValue=rows[i].cells[6].innerHTML;
			if(rankValue=="请选择"||rankValue=="null"){
				rankValue="";
			}
			var statusValue=rows[i].cells[9].innerHTML;
			if(statusValue=="请选择"||statusValue=="null"){
				statusValue="";
			}
			
			if(rows[i].cells[2].getAttribute('name') == 'zrAccount'){
				if(rows[i].cells[2].innerHTML == null || rows[i].cells[2].innerHTML == ""){
					toastr.error("第"+(i+1)+"行中软工号为空！");
					return "verifyFalse";
				}else if(!checkMemberData(rows[i].cells[2].innerHTML, '中软工号')){
					toastr.error("第"+(i+1)+"行中软工号不为数字！");
					return "verifyFalse";
				}
			}
			
			if(rows[i].cells[1].getAttribute('name') == 'staffname'){
				if(rows[i].cells[1].innerHTML == null || rows[i].cells[1].innerHTML == ""){
					toastr.error("第"+(i+1)+"行姓名为空！");
					return "verifyFalse";
				}else if(!checkMemberData(rows[i].cells[1].innerHTML, '姓名')){
					toastr.error("第"+(i+1)+"行姓名不为汉字！");
					return "verifyFalse";
				}
			}
			if(rows[i].cells[3].getAttribute('name') == 'hwAccount'){
				if(rows[i].cells[3].innerHTML == null || rows[i].cells[3].innerHTML == ""){
					toastr.error("第"+(i+1)+"行华为工号为空！");
					return "verifyFalse";
				}else if(!checkMemberData(rows[i].cells[3].innerHTML, '华为工号')){
					toastr.error("第"+(i+1)+"行华为工号不为字母和数字！");
					return "verifyFalse";
				}
			}
		
			if(rows[i].cells[4].children.svnGitNo.name == 'svnGitNo'){
				if(!(rows[i].cells[4].children.svnGitNo.defaultValue == null || rows[i].cells[4].children.svnGitNo.defaultValue == "") 
						&& !checkMemberData(rows[i].cells[4].children.svnGitNo.defaultValue, '华为工号')){
					toastr.error("第"+(i+1)+"行SVN/GIT账号不为字母和数字！");
					return "verifyFalse";
				}
			}
			
			allTr +='{"no":"'+projNo+'",'+
				'"name":"'+rows[i].cells[1].innerHTML+'",'+
				'"zrAccount":"'+rows[i].cells[2].innerHTML+'",'+
				'"hwAccount":"'+rows[i].cells[3].innerHTML+'",'+
				'"svnGitNo":"'+rows[i].cells[4].children.svnGitNo.defaultValue+'",'+
				'"role":"'+roleValue+'",'+
				'"rank":"'+rankValue+'",'+
				'"startDate":"'+startDate+'",'+
				'"endDate":"'+endDate+'",'+
				'"status":"'+statusValue+'"'+
				'},';
		}
		var count = 0;  
		for (var i = 0; i < hwAccounts.length;i++) {  
		    for (var j = 0; j < hwAccounts.length; j++) {
		    	if(i==j){
		    		continue;
		    	}
		        if (hwAccounts[i] == hwAccounts[j]) {  
		            count++;  
		        }  
		    }  
		}
		if(count>0){
			return "err";
		}
		var jsonStr = '{"membersLocals":['+allTr.substring(0,allTr.length-1)+']}';
		if(allTr.substring(0,allTr.length-1)==""){
			jsonStr='{"membersLocals":[{"no":"'+projNo+'"}]}';
		}
		$.ajax({
			url : getRootPath() + "/user/selectedUserAdd",
			type : 'POST',
			dataType: "json",
			contentType : 'application/json;charset=utf-8', //设置请求头信息
			async: false,//是否异步，true为异步
			data : jsonStr,
			success : function(data) {
				
			}
		})
	}
	
	//获取项目PO
	function getSelectValue(type,isOptin,id,name){
		$.ajax({
			url:getRootPath() + '/codeMaster/getProjectPOCode',
			type:'post',
			async:false,
			dataType: "json",
			contentType : 'application/x-www-form-urlencoded', //设置请求头信息
			data:{
				no: projNo
			},
			success:function(data){
				if (data.length > 0) {
					if("通过" == isOptin){
						for(j = 0; j < data.length; j++) {
//							var text = data[j].NAME + ' PO: ' + data[j].PO;
							if(data[j].NAME == "" || data[j].NAME == undefined || data[j].NAME == null){
								continue;
							}else{
								$(id).append('<option value="'+data[j].NAME+'">'+data[j].NAME+'</option>');
							}
						}
					}
				}
			}
		});
	}
	
	function offlineControl(){
//		var poString = $("#po").find("option:selected").text();
//		var poIndex = poString.lastIndexOf("\: ");
//		po = poString.substring(poIndex + 2, poString.length);
		po = $("#po").find("option:selected").text();
	}
	//获取统计方式
	$(document).on("click", "#tjfsinfo", function () {
		offlineControl();
		if(!po){
			$("input[name=online][value=1]").attr("disabled", true);
		}
		/*$('#online input').bootstrapSwitch({
			onText:"是",
			offText:"否"
		});
		$('#online input').bootstrapSwitch('state',false);*/
		$.ajax({
			url : getRootPath()+'/codeType/codeTypeConfig',
			type : 'post',
			data : {
				no : projNo
			},
			success : function(data){
				_.each(data.data, function(record, index){
					if(record){
//						if(record.parameterId==160){
							$("input[name=subType][value=0]").attr("checked", true);
//						}
						if(record.parameterId==161){
							$("input[name=testType][value=" + record.type + "]").attr("checked", true);
						}
						if(record.parameterId==162){
							if(record.closeStatus == 1){
								$("input[name=online][value=1]").attr("checked", true);
					            $("input[name=online][value=0]").attr("disabled", true);
//					            $("input[name=online][value=1]").attr("disabled", true);
							}else{
								$("input[name=online][value=" + record.type + "]").attr("checked", true);
							}
							
							if(record.type == 0){
								$("input[name=online][value=0]").attr("checked", true);
								$("#po").find("option[value=" +record.po+ "]").attr("selected",true);
							}else if(record.type == 1){
								if(record.po){
									$("input[name=online][value=1]").attr("checked", true);
						            $("#po").find("option[value=" +record.po+ "]").attr("selected",true);
								}else{
									$("input[name=online][value=1]").attr("checked", true);
								}
								
							}
						}
						/*if(record.parameterId==165){
							$("input[name=trusted][value=" + record.type + "]").attr("checked", true);
						}*/
					}
					
				});
			}
		});
	});
	
	$(document).on("click", "#saveCodeType", function () {
		var val = $("input[name='subType']:checked").val();
		var valtextmea = $("input[name='testType']:checked").val();
		var online = $("input[name='online']:checked").val();

		offlineControl();
		if(!po){
			toastr.error('主PO为空,不能下线!');
			return false;
		}
		/*if($("#online input").bootstrapSwitch("state")){
			online = 0;
		}*/
		/*if($("#online input").bootstrapSwitch("state")){
			online = 0;
		}*/
		var trusted = $("input[name='trusted']:checked").val();

		$.ajax({
			url : getRootPath()+'/codeType/saveCodeType',
			type: 'post',
			data : {	
						no: projNo,
				    	type: val,
				    	textMeanType:valtextmea,
				    	online : online,
				    	trusted : trusted==null?"1":trusted,
				    	po: po
				    },
			success : function(data){
				if(data.code == 'success'){
					toastr.success('保存成功！');
					//location.reload();
					$("#codeType").modal('hide');
				}else{
					toastr.error('保存失败!');
				}
			}
		});
	});
	
	//获取邮箱配置
	$(document).on("click", "#yjpzinfo", function () {
		$('#mySwitch input').bootstrapSwitch({
			onText:"启动",
			offText:"停止"
		});
		//$('#mySwitch input').bootstrapSwitch('state',false);
		$.ajax({
			url : getRootPath()+'/codeType/getEmailConfig',
			type : 'post',
			data : {
				no : projNo
			},
			success : function(data){
				$('#emailurl').val(data.data.send_email);
				if(data.data.email_on_off == "0"){
					$('#mySwitch input').bootstrapSwitch('state',true);
				}else{
					$('#mySwitch input').bootstrapSwitch('state',false);
				}
			}
		});
	});
	$(document).on("click", "#saveEmailUrl", function () {
		//true/ 空
		var val = $('#mySwitch input').bootstrapSwitch('state');
		var emailUrlval = $('#emailurl')[0].value
		//toastr.error(val+"  "+emailUrlval);
//		var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
		if(emailUrlval == ""){ //输入不能为空
			toastr.error("输入不能为空!");
			return false;
//		}else if(!reg.test(emailUrlval)){ //正则验证不通过，格式不对
//			toastr.error("请输入邮箱地址!");
//			return false;
		}
		
		if(val){
			val="0";
		}else{
			val="1";
		}
		
		$.ajax({
			url : getRootPath()+'/codeType/emailConfig',
			type : 'post',
			data : {
				no : projNo,
				emailUrl : emailUrlval,
				emailOnOff : val
			},
			success : function(data){
				toastr.info("邮箱配置完成");
			}
		});
	});

    function bindExportEvent(){
        $("#exportBtn").click(function(){
            var $eleForm = $("<form method='POST'></form>");
            $eleForm.attr("action",getRootPath() + "/export/downloadIndicators");
            $eleForm.append($('<input type="hidden" name="no" value="'+ projNo +'">'));
            $(document.body).append($eleForm);
            //提交表单，实现下载
            $eleForm.submit();
        })
    };
    
    //重置职级
    $(document).on("click", "a[name=resetRank]", function () {
		var tds = $(this).parents('tr:first')[0].childNodes;
		
		resetRank(tds[2].innerText, tds[6].innerText, "project");
	});
    
    $(document).ready(function(){
        if(projNo==null || projNo == ""){
            projNo = window.parent.projNo;
        }
        loadConfigPage(projNo);
        bindExportEvent();
		openOMPdataClick();
		hideOMPdataClick();
		addUserManagerClick();
		saveUserManagerClick();
		addAllOmpClick();
		getSelectValue('po', "通过", "#po", "PO");
		//inheritProcessIndicator();	
	})
})


function loadConfigPage(no){
	if(undefined == no || null == no){
		no = window.parent.projNo;
	}
    var url = getAngularUrl()+"measureConfig/"+no;
    var html = '<object type="text/html" data="'+url+'" style="width: 100%;height: 100%;"></object>';
    document.getElementById("lable-measure").innerHTML = html;
}
function createTable(sgzbColumns,cyclelist){
	$('#sgzbtab').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
        url : getRootPath() + '/iteration/iterationIndexVlaue',
    	editable:true,//可行内编辑
    	striped: true, //是否显示行间隔色
    	dataField: "rows",
    	pageNumber: 1, //初始化加载第一页，默认第一页
    	pagination:true,//是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,//单页记录数
    	pageList:[5,10,20,30],//分页步进值
    	showColumns:false,
    	toolbar:'#toolbar',//指定工作栏
    	toolbarAlign:'right',
        dataType: "json",
        queryParams : function(params){
			var param = {
				limit : params.limit, // 页面大小
    	        offset : params.offset, // 页码
    	        list : cyclelist,
    	        proNo : projNo,
				}
			return param;
		},
        columns: sgzbColumns,
        onEditableSave: function (field, row, oldValue, $el) {
            $.ajax({
                type: "post",
                url: getRootPath()+"/iteration/iterationIndex/edit",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data: JSON.stringify({
 					iterationId : field,
 					measureId : row.measure_id,
 					value : row[field],
 					unit : row.unit
 				}),
                success: function (data, status) {
                    if (status == "success") {
                    	toastr.success('修改成功！');                 
                    	$('#sgzbtab').bootstrapTable('refresh');
                    }else{
                    	toastr.success('修改失败！');
                    }
                }
            });
        },
		locale:'zh-CN',//中文支持
	});	
}
function createColumns(item){
	var editable ={type: 'text',title: item.iteName, placement: 'bottom',
			validate: function (v) {
					var reg = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;
						if(!reg.test(v)){
							return '请输入正确的指标值';
						}
					}
		}
	//手动拼接columns
	var temp = {field: item.id, title: item.iteName, align: 'center', width:80, editable: editable};
	return temp;
}
function tianjia(){
	$().tianjia0();
}

//流程指标页面
$(window).resize(function() {
	$("#lable-measure").css({"min-height":$(window).height() - 115});
});

$(document).ready(function(){
	$("#lable-measure").css({"min-height":$(window).height() - 115});
});

//团队成员页面
$(window).resize(function() {
	$("#tab-tdcy").css({"min-height":$(window).height() - 65});
});

$(document).ready(function(){
	$("#tab-tdcy").css({"min-height":$(window).height() - 65});
});

//工时页面
$(window).resize(function() {
	$("#myiframe").css({"min-height":$(window).height() - 85});
});

$(document).ready(function(){
	$("#myiframe").css({"min-height":$(window).height() - 85});
});

//统计方式页面
$(window).resize(function() {
	$(".thumb").css({"min-height":$(window).height() - 65});
});

$(document).ready(function(){
	$(".thumb").css({"min-height":$(window).height() - 65});
});

//邮件配置页面
$(window).resize(function() {
	$("#tab-yjpz").css({"min-height":$(window).height() - 65});
});

$(document).ready(function(){
	$("#tab-yjpz").css({"min-height":$(window).height() - 65});
});

function changeValue(obj){
    $(obj).attr("value", $(obj).val());
}

function getMemberAccount(){
	return getProjectAccount(window.parent.projNo);
}

function getPMZRAccountByNo(){
	$.ajax({
		url : getRootPath() + '/GeneralSituation/getPMZRAccountByNo',
		type : 'post',
		traditional:true,
		data : {
			'projNo' : window.parent.projNo
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			$("#pmZR").val(data);
		}
	});
}

function getPMZRAccountByHW(){
	if(0 == getCookie('zrOrhwselect')){
		$.ajax({
			url : getRootPath() + '/GeneralSituation/getPMZRAccountByHW',
			type : 'post',
			traditional:true,
			data : {
				'hwAccount' : getCookie('username')
			},
			async : false,// 是否异步，true为异步
			success : function(data) {
				pmZR = data;
			}
		});
	}else if(1 == getCookie('zrOrhwselect')){
		pmZR = zeroFill(getCookie('username'), 10);
	}
	
	return pmZR;
}

function projectMemberEcho(zrAccount){
	rank = new Array(6);
	
	$.ajax({
		url : getRootPath() + '/GeneralSituation/projectMemberEcho',
		type : 'post',
		traditional:true,
		data : {
			'projNo' : window.parent.projNo,
			'zrAccount' : zrAccount
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			if(data){
				rank[0] = data.RANK;
				rank[1] = data.STATUS;
				rank[2] = data.ROLE;
				rank[3] = data.SVN_GIT_NO;
				rank[4] = data.HW_ACCOUNT;
				rank[5] = data.NAME;
			}
		}
	});
	return rank;
}
