var po = "";
var zr = "";
var hw = "";
var demoList = "";
var pmZR = "";
var rank = new Array(6);
var userids = getCookie('username');
var role = getSelectValueByType("role");
var statusedit = getSelectValueByType("status");
var kaifaCount = "";
var keyroleCount= "";
var renlinums  = "";
var guanjiannums ="";
var no = "";
var projectestartDateedit = "";
var projectendDateedit = "";
var code ="";
$(function(){
	 var projNo = window.parent.projNo;
	 var username = getCookie('username');
	var userid = getCookie('username')
	 
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
         var  limit = 5 ;

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
    				'<td date-type="input" name="staffname" style="width: 50px;"id="staffname"><a href="#" onclick="loadstaffinfo()">'+(container[j][i].NAME == undefined ? "" : container[j][i].NAME)+'</a></td>'+
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

	function changeDateFormat(cellval) {
		var dateVal = cellval + "";
		if (cellval != null) {
			var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
			var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
			var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();

			var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
			var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
			var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

			return date.getFullYear() + "-" + month + "-" + currentDate;
		}else{
			return '';
		}
	}
	
	//请求全部团队员工
	function getSelectedUser(){

		$('#userManagerlocal').bootstrapTable('destroy');
		$('#userManagerlocal').bootstrapTable({
			method: 'post',
			contentType: "application/x-www-form-urlencoded",
			url:getRootPath() + '/user/getProjectMembers',
			height:tableHeight(),//高度调整
			editable:true,//可行内编辑
			striped: false, //是否显示行间隔色
			dataField: "rows",
			pageNumber: 1, //初始化加载第一页，默认第一页
			pagination:true,//是否分页
			queryParamsType:'limit',
			sidePagination:'server',
			pageSize:5,//单页记录数
			pageList:[5,10,20,30],//分页步进值
			//showRefresh:true,//刷新按钮
			//showColumns:true,
			minimumCountColumns: 2,             //最少允许的列数
			//clickToSelect: true,//是否启用点击选中行
			toolbarAlign:'right',
			buttonsAlign:'right',//按钮对齐方式
			toolbar:'#toolbars',//指定工作栏
//			uniqueId: 'userid',//uniqueId必须
			queryParams: function(params){
				var param = {
                    userid : userid,
					limit : params.limit, // 页面大小
					offset : params.offset, // 页码
				}
				return param;
			},
		onEditableSave: function (field, row, oldValue, $el) {
			if(field == "personLiable"){
				row.personLiable = row.personLiable.toString();
			}
			if(field == 'iteId'){
				if(row.iteId != oldValue ){
					row.planStartTime = null;
					row.planEndTime = null;
					row.startTime = null;
					row.endTime = null;
				}
			}
			row.userid = userid;
				$.ajax({
					type: "post",
					url: getRootPath()+"/user/editOne",
					dataType: "json",
					contentType : 'application/json;charset=utf-8', //设置请求头信息
					data: JSON.stringify(row),
					success: function (data, status) {
						if (status == "success") {
							var pageNumber=$('#userManagerlocal').bootstrapTable('getOptions').pageNumber;
							$('#userManagerlocal').bootstrapTable('refresh',{pageNumber:pageNumber});
							toastr.success('修改成功！');
						}else{
							toastr.success('修改失败！');
						}
					}
				});
			},

			columns:[
				{
					title:'全选',
					field:'select',
					checkbox:true,
					width:25,
					align:'center',
					valign:'middle',
				},
				{
					title : '序号',
					align: "center",
					field:'id',
					width: 30,
					formatter: function (value, row, index) {
						var pageSize=$('#userManagerlocal').bootstrapTable('getOptions').pageSize;
						var pageNumber=$('#userManagerlocal').bootstrapTable('getOptions').pageNumber;
						return pageSize * (pageNumber - 1) + index + 1;
					}
				},
				{
					title:'姓名',
					halign :'center',
					align : 'center',
					field:'name',
					width : 45,
				},
				{
					title:'中软工号',
					halign :'center',
					align : 'center',
					field:'zrAccount',
					width : 70,
				},
				{
						title:'华为工号',
					halign :'center',
					align : 'center',
					field:'hwAccount',
					width: 70,
				},

				{
					title:'SVN/GIT账号',
					halign :'center',
					align : 'center',
					field:'svnGitNo',
					width: 70,
				},

				{
					title:'岗位',
					halign :'center',
					align : 'center',
					field:'role',
					width: 65,
					formatter: function (value, row, index) {
						return formatColumnVal(value, role);
					}
				},
				{
					title:'职级',
					halign :'center',
					align : 'center',
					field:'rank',
					width: 65,
				},
				{
					title:'加入项目时间',
					halign :'center',
					align : 'center',
					field:'startDate',
					width: 70,
					formatter:function(value,row,index){
						return changeDateFormat(value);
					},
				},
				{
					title:'离开项目时间',
					halign :'center',
					align : 'center',
					field:'endDate',
					width: 70,
					formatter:function(value,row,index){
						return changeDateFormat(value);
					},
				},
				{
					title:'状态',
					halign :'center',
					align : 'center',
					field:'status',
					width: 65,
				},
				{
					title:'操作',
					halign :'center',
					align : 'center',
					field:'opera',
					width: 100,
					//'修改,删除'响应id列
					formatter:function(value,row,index){
						return '<a  style="color:blue" onclick="editData1(\'' + row.no + '\',\'' + row.zrAccount + '\')">' + '修改' + '</a>'
							+'&nbsp;&nbsp;&nbsp;<a href="#" style="color:blue" onclick="delrowData(\'' + row.no + '\',\'' + row.zrAccount + '\')">' + '移除' + '</a>'
							+'&nbsp;&nbsp;&nbsp;<a href="#" style="color:blue" onclick="restRank(\'' + row.rank + '\',\'' + row.zrAccount + '\')">' + '重置职级' + '</a>';
					}
				}
			],
			locale:'zh-CN',//中文支持,
		});

	}

	function tableHeight() {
		return $(window).height() - 170;
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
 /**   $('#insertBtn').click(function(){
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
    });*/
    
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
	/**$(document).on("click", "a[id='sgzbinfo']", function () {
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
		
	});*/
	function initRenliData(){
		var userid = getCookie('username');
		$.ajax({
			url : getRootPath()+ '/user/getZongrenliData',
			async: false,
			data : {
				userid : userid,
			},
			success : function(data) {
				if (data.code == "success") {
					var renyuan = data.data;
					$("#zrl").html(formatResult(renyuan.renlinum,false));
					$("#gjjs").html(formatResult(renyuan.guanjiannum,false));
					$("#wtry").html(formatResult(renyuan.wentinum,false));
					renlinums = formatResult(renyuan.renlinums,false);
					guanjiannums = formatResult(renyuan.guanjiannums,false);
				}else{
					toastr.error('服务请求失败，请稍后再试！');
				}

			}
		});
	}
	//团队成员
	function Loadingtdcy(){
		getSelectedUser();
		$('#addForms').bootstrapValidator({
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			},
			fields: {
				zrAccount: {
					validators: {
						notEmpty:{
							message:'中软工号不能为空',
						},
						regexp:{
							regexp: queryRegularExpression('中软工号'),
							message: '中软工号只能包含数字！'
						}
					}
				},
				name: {
					validators: {
						notEmpty:{
							message:'姓名不能为空',
						},
						regexp:{
							regexp: queryRegularExpression('姓名'),
							message: '姓名只能包含汉字！'
						}
					}
				},
				hwAccount: {
					validators: {
						notEmpty:{
							message:'华为工号不能为空',
						},
						regexp:{
							regexp: queryRegularExpression('华为工号'),
							message: '华为工号只能包含字母和数字！'
						}
					}
				},
				projects: {
					validators: {
						notEmpty:{
							message:'待入项目不能为空',
						}
					}
				},
				positionAdds: {
					validators: {
						notEmpty:{
							message:'岗位不能为空',
						}
					}
				},
				status1: {
					validators: {
						notEmpty:{
							message:'状态不能为空',
						}
					}
				},
				startdate: {
					validators: {
						notEmpty:{
							message:'加入项目时间不能为空',
						}
					}
				},
				enddate: {
					validators: {
						notEmpty:{
							message:'离开项目时间不能为空',
						}
					}
				},
			}
		});
		$('#editForms').bootstrapValidator({
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			},
			fields: {
				name: {
					validators: {
						notEmpty:{
							message:'姓名不能为空',
						},
						regexp:{
							regexp: queryRegularExpression('姓名'),
							message: '姓名只能包含汉字！'
						}
					}
				},
				hwAccount: {
					validators: {
						notEmpty:{
							message:'华为工号不能为空',
						},
						regexp:{
							regexp: queryRegularExpression('华为工号'),
							message: '华为工号只能包含字母和数字！'
						}
					}
				},
				positionedit: {
					validators: {
						notEmpty:{
							message:'岗位不能为空',
						}
					}
				},
				status1: {
					validators: {
						notEmpty:{
							message:'状态不能为空',
						}
					}
				},
				startdate: {
					validators: {
						notEmpty:{
							message:'加入项目时间不能为空',
						}
					}
				},
				enddate: {
					validators: {
						notEmpty:{
							message:'离开项目时间不能为空',
						}
					}
				},
			}
		});

		demoList = "";
		var width1;
		var width2;
		$.ajax({
			url : getRootPath() + "/manpowerBudget/getManpowerBudgetByPmid",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				userid : userid,
			},
			success : function(data) {

				if(data.data==null){
					kaifaCount = 0,
						keyroleCount =0
				}else{
					kaifaCount = (data.data.headcount),
					keyroleCount = (data.data.keyRoleCount)
				}
			}
		})
		if (kaifaCount==0){
			width1 = 0
			$("#percet1").html(Math.round(width1)+"%");
			$("#width1").css("width",Math.round(width1)+"%");
		} else {
			width1= formatResult((renlinums/kaifaCount)*100,true)
			$("#percet1").html(Math.round(width1)+"%");
			$("#width1").css("width",Math.round(width1)+"%");
		}
		if (keyroleCount==0){
			width2 = 0
			$("#percet2").html(Math.round(width2)+"%");
			$("#width2").css("width",Math.round(width2)+"%");
		} else {
			width2= formatResult((guanjiannums/keyroleCount)*100,true)
			$("#percet2").html(Math.round(width2)+"%");
			$("#width2").css("width",Math.round(width2)+"%");
		}
	}

	function CompareDate(d1,d2){
	  return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
	}

	function formatResult(val,flag) {
		if (val < 0 || val == "" || val == null) {
			return 0;
		}else if (flag && val > 100) {
		return 100;
	}else {
			return val;
		}
	}
	//打开新增页面
	$('#btn_adds').click(function(){
		var userid = getCookie("username");
		document.getElementById("addForms").reset();
		initFormsData()
		getMemberRank();
		getRelateProjects();
		$("#sgAccountAdds").empty();
//		$('#sgAccountAdds').selectpicker('val','')
        $("#iteAddPages").modal('show');
		$("#zrAccountAdds").blur(function() {

			$.ajax({
				url:getRootPath() + '/user/getMemberinfo',
				type : 'post',
				data:{
					zrAccount:$("#zrAccountAdds").val()
				},
				success:function(data){
					if (data.code=="success") {
						$("#memberNameAdd").val(data.NAME==null?"": data.NAME);
						$("#hwAccountAdds").val(data.HW_ACCOUNT==null?"":  data.HW_ACCOUNT);
					}

				}
			})

		});
        $("#projects").blur(function() {
			demoList=null;
                $.ajax({
                    url:getRootPath() + '/bu/projOverviewDatas',
                    type : 'post',
                    data:{
                        reprname:$("#projects").val()
                    },
                    success:function(data){
						projectestartDate = new Date(data.startDate).format('yyyy-MM-dd');
						projectendDate = new Date(data.endDate).format('yyyy-MM-dd');
						$("#startdate").val(projectestartDate);
						$("#enddate").val(projectendDate);

						no = data.no;
						$.ajax({
							url : getRootPath() + "/codeMaster/getMemberSVN",
							type : 'POST',
							async: false,//是否异步，true为异步
							data : {
								no : no
							},
							success : function(data) {
								demoList ='<option label="'+"请选择"+'" value="">'+"请选择"+'</option>'
								_.each(data.rows, function(val, index){
									demoList +='<option label="'+val.AUTHOR+'" value="'+val.AUTHOR+'">'+val.AUTHOR+'</option>';
								});
								$("#sgAccountAdds").html(demoList);
								$('#sgAccountAdds').selectpicker('refresh');

							}
						});
                    }
                })
	    });
	});
	//隐藏新增页面f
	$('#add_backBtns').click(function(){
		$("#iteAddPages").modal('hide');
		$('#addForms').data('bootstrapValidator').resetForm(true);
	});

	//新增保存
	$('#add_saveBtns').click(function() {

		//点击保存时触发表单验证
	    $('#addForms').bootstrapValidator('validate');
		//如果表单验证正确，则请求后台添加用户
		if($("#addForms").data('bootstrapValidator').isValid()){
			if (CompareDate(projectestartDate,$("#startdate").val())) {
				toastr.warning("入项时间不得早于项目起始日期！");
				return;
			}
			if (CompareDate($("#enddate").val(),projectendDate)) {
				toastr.warning("离项时间不得晚于项目结束日期！");
				return;
			}
			if (CompareDate($("#startdate").val(),$("#enddate").val())) {
				toastr.warning("离项时间不得早于入项日期！");
				return;
			}
			// 华为账号校验
           verifyHwAccount($("#zrAccountAdds").val(),$("#hwAccountAdds").val());
			if (code == "error") {
                toastr.warning("华为账号与中软账号匹配有误，请核实后重新配置")
                return ;
            }
			$.ajax({
				url:getRootPath() + '/user/Membersadd',
				type : 'post',
				dataType: "json",
				contentType : 'application/x-www-form-urlencoded', //设置请求头信息
				async:false,
// 				data:JSON.stringify($('#addForm').serializeJSON()),
				data:{
					userid:$("#useridss").val(),
					name:$("#memberNameAdd").val(),
// 					position:$("#positionAdd").val(),
					zrAccount:$("#zrAccountAdds").val(),
					hwAccount:$("#hwAccountAdds").val(),
					reprname:$("#projects").val(),
					role:$("#positionAdds").val(),
					rank:$("#rank").val(),
					startDate:$("#startdate").val(),
					endDate:$("#enddate").val(),
					svnGitNo:$("#sgAccountAdds").val(),
					status:$("#statusAdds").val(),
				},
				success:function(data){
					//后台返回添加成功
					if(data.code=='success'){
						$("#iteAddPages").modal('hide');
						$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembers'})
						initRenliData();
						Loadingtdcy();
						$('#addForms').data('bootstrapValidator').resetForm(true);
						toastr.success('保存成功！');
					}else if(data.code=='norelateprojects'){
						toastr.error('团队成员待入项目不能为空！');
					} else{
						toastr.error('添加失败!');
					}
				}
			});
		}
	});

	$('#edit_backBtns').click(function(){
		$("#editPages").modal('hide');
	});

	//修改保存
	$('#edit_saveBtns').click(function(){
		$('#editForms').bootstrapValidator('validate');
		if($("#editForms").data('bootstrapValidator').isValid()){
            if (CompareDate(projectestartDateedit,$("#startdateedit").val())) {
                toastr.warning("入项时间不得早于项目起始日期！");
                return;
            }
            if (CompareDate($("#enddateedit").val(),projectendDateedit)) {
                toastr.warning("离项时间不得晚于项目结束日期！");
                return;
            }
            if (CompareDate($("#startdateedit").val(),$("#enddateedit").val())) {
                toastr.warning("离项时间不得早于入项日期！");
                return;
            }
			$.ajax({
				url:getRootPath()+"/user/saveedit",
				type : 'post',
// 				dataType: "json",
// 				contentType : 'application/json;charset=utf-8', //设置请求头信息
// 				data:JSON.stringify($('#editForm').serializeJSON()),
				data:{
					   userid : $("#useridsss").val(),
		                 name : $("#memberNameedit").val(),
					zrAccount : $("#zrAccountedit").val(),
		            hwAccount : $("#hwAccountedit").val(),
		            reprname : $("#projectsedit").val(),
		                role : $("#positionedit").val(),
		                rank : $("#rankedit").val(),
		        startDatestr : $("#startdateedit").val(),
		          endDatestr : $("#enddateedit").val(),
		            svnGitNo : $("#sgAccountedit").val(),
		              status : $("#statusedit").val(),
				},
				success:function(data){
					//后台返回添加成功
					if(data.code=='success'){
						$("#editPages").modal('hide');
						$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembers'});
						initRenliData();
						Loadingtdcy();
						$('#editForms').data('bootstrapValidator').resetForm(true);
						toastr.success('编辑成功！');
					}
					else{
						toastr.error('编辑失败!');
					}
				}
			});
		}
	});

	//删除事件按钮
	$('#btn_deletes').click(function(){
		var zrAccounts=[];
		var nos=[];
		var dataArr=$('#userManagerlocal').bootstrapTable('getSelections');
		for(var i=0;i<dataArr.length;i++){
			nos.push(dataArr[i].no);
			zrAccounts.push(dataArr[i].zrAccount);
		}
		var zh = {nos:nos,zrAccountss:zrAccounts};
		if (zrAccounts.length <= 0) {
			toastr.warning('请选择有效数据');
			return;
		}
		Ewin.confirm({ message: "确认删除已选中角色吗？" }).on(function (e) {
			if (!e) {
				return;
			} else {
				$.ajax({
					url:getRootPath() + '/user/membersdelete',
					type:'post',
//	        		dataType: "json",
//					contentType : 'application/json;charset=utf-8', //设置请求头信息
					data:{
						nos:JSON.stringify(nos),
						zrAccounts:JSON.stringify(zrAccounts),
					},
					success:function(data){
						if(data.code == 'success'){
							toastr.success('删除成功！');
							$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembers'});
							initRenliData();
							Loadingtdcy();
						}else{
							toastr.error('删除失败!');
						}
					}
				});
			}
		});
	});

    $(document).ready(function(){
        if(projNo==null || projNo == ""){
            projNo = window.parent.projNo;
        }
		getRelateProjects()
		initRenliData();
		Loadingtdcy();
	})
})

//团队成员页面
$(window).resize(function() {
	$("#tab-tdcy").css({"min-height":$(window).height() - 180});
});

$(document).ready(function(){
	$("#tab-tdcy").css({"min-height":$(window).height() - 180});
});

//关键角色页面
$(window).resize(function() {
	$("#tab-gjjs").css({"min-height":$(window).height() - 180});
});

$(document).ready(function(){
	$("#tab-gjjs").css({"min-height":$(window).height() - 180});
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

function loadstaffinfo(){

	$("#editPages").modal('show');

}
/**$('#edit_backBtns').click(function(){
	alert("11111");
	$("#editPages").modal('hide');
	$('#tab-tdcy').bootstrapTable('refresh');
});*/
function backfor(){
	$("#editPages").modal('hide');
	 window.location.reload();
}

function initFormsData(){
	setOption(role, "#positionAdds", "岗位");
	setOption(role, "#positionedit", "岗位");
	setOption(statusedit, "#statusAdds", "状态");
	setOption(statusedit, "#statusedit", "状态");
}

//获取职级下拉菜单
function getMemberRank(){
	var s;
	$.ajax({
		url:getRootPath() + '/codeMaster/getMemberRankValue',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{

		},
		success:function(data){
			s=data.rows;
			if(null != s){
				var option = '<option label="'+"请选择职级"+'" value="">'+"请选择职级"+'</option>';
				var options = '<option label="'+"请选择职级"+'" value="">'+"请选择职级"+'</option>';
				for(j = 0; j < s.length; j++) {
					option += '<option label="'+s[j].rank_name+'" value="'+s[j].VALUE+'">'+s[j].rank_name+'</option>';
					options += '<option label="'+s[j].rank_name+'" value="'+s[j].VALUE+'">'+s[j].rank_name+'</option>';
				}
				$('#rank').html(option);
				$('#rankedit').html(options);
			}else{
				toastr.error('加载失败!');
			}
		}
	});
	return s;
}
//获取待入/所属项目下拉菜单
function getRelateProjects(){
	var s;
	$.ajax({
		url:getRootPath() + '/user/getRelateProjects',
		type:'post',
		async:false,
		dataType: "json",
		contentType : 'application/x-www-form-urlencoded', //设置请求头信息
		data:{
			userid : userid
		},
		success:function(data){
			s=data.rows;
			if(null != s){
				var option = '<option label="'+"请选择待入项目"+'" value="">'+"请选择待入项目"+'</option>';
				var options = '<option label="'+"请选择待入项目"+'" value="">'+"请选择所属项目"+'</option>';

				for(j = 0; j < s.length; j++) {
					option +='<option label="'+s[j].NO+'" value="'+s[j].NAME+'">'+s[j].NAME+'</option>';
					options +='<option label="'+s[j].NO+'" value="'+s[j].NAME+'">'+s[j].NAME+'</option>';
				}
				$('#projects').html(option);
				$('#projectsedit').html(options);
			}else{
				toastr.warning('当前用户无关联项目，无权查看与操作此页面！');
			}
		}
	});
	return s;
}
function editData1(no,zrAccount){
	document.getElementById("editForms").reset();
	initFormsData();
	getMemberRank();
	getRelateProjects();
	$("#sgAccountedit").empty();
	$("#editPages").modal('show');
	$.ajax({
		url : getRootPath() + "/codeMaster/getMemberSVN",
		type : 'POST',
		async: false,//是否异步，true为异步
		data : {
			no : no
		},
		success : function(data) {
			var option ='<option label="'+"请选择"+'" value="">'+"请选择"+'</option>'
			_.each(data.rows, function(val, index){
				option +='<option label="'+val.AUTHOR+'" value="'+val.AUTHOR+'">'+val.AUTHOR+'</option>';
			});
			$("#sgAccountedit").html(option);
//			$('#sgAccountedit').selectpicker('refresh');

		}
	});
    $.ajax({
        url:getRootPath() + '/bu/projOverviewData',
        type : 'post',
        data:{
            no:no
        },
        success:function(data){
             projectestartDateedit = new Date(data.startDate).format('yyyy-MM-dd');
             projectendDateedit = new Date(data.endDate).format('yyyy-MM-dd');
        }
    })
	$.ajax({
		url:getRootPath() + '/user/editPages',
		type:'post',
		data:{
			no : no,
			userid : userids,
			zrAccount : zrAccount
		},
		success : function(data){
			if(data.code == 'success'){
				if(data.rows){
					$("#useridsss").val(userid);
					$("#memberNameedit").val(data.rows.name);
					$("#zrAccountedit").val(data.rows.zrAccount);
					$("#hwAccountedit").val(data.rows.hwAccount);
					$("#projectsedit").val(data.rows.reprname);
					$("#positionedit").val(data.rows.role);
					$("#rankedit").val(data.rows.rank);
					$("#startdateedit").val(data.rows.startDatestr);
					$("#enddateedit").val(data.rows.endDatestr);
					$("#sgAccountedit").val(data.rows.svnGitNo);
					$("#statusedit").val(data.rows.status);

				}
			}else{
				toastr.error('服务请求失败，请稍后再试！');
			}
		}
	});


}

function delrowData(no,zrAccount){
	Ewin.confirm({ message: "确认删除已选中角色吗？" }).on(function (e) {
		if (!e) {
			return;
		} else {
			$.ajax({
				url:getRootPath() + '/user/membersdeleteone',
				type:'post',
//	        		dataType: "json",
//					contentType : 'application/json;charset=utf-8', //设置请求头信息
				data:{
					no:no,
					zrAccount:zrAccount,
				},
				success:function(data){
					if(data.code == 'success'){
						toastr.success('删除成功！');
						$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembers'});
						initRenliData();
						window.location.reload();
					}else{
						toastr.error('删除失败!');
					}
				}
			});
		}
	});
}
function restRank(rank,zrAccount) {
	resetRank(zrAccount,rank,"project");
}

function verifyHwAccount(zrAccount,hwAccount) {

    $.ajax({
        url:getRootPath() + '/user/verifyHwAccount',
        type : 'post',
        data:{
            zrAccount:zrAccount,
            hwAccount:hwAccount,
        },
        success:function(data){
            if(data.code=='fail'){
                code = "error";
            }
        }
    });
}
