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
var renlinum  = "";
var guanjiannum ="";
var no = "";
var projNo = getQueryString("projNo");
$(function(){
	var userid = getCookie('username')
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
			url:getRootPath() + '/user/getProjectMembersByNo',
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
					proNo : projNo,
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
					width: 31,
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
					width: 75,
				},

				{
					title:'岗位',
					halign :'center',
					align : 'center',
					field:'role',
					width: 60,
					formatter: function (value, row, index) {
						return formatColumnVal(value, role);
					}
				},
				{
					title:'职级',
					halign :'center',
					align : 'center',
					field:'rank',
					width: 60,
				},
				{
					title:'加入项目时间',
					halign :'center',
					align : 'center',
					field:'startDate',
					width: 73,
					formatter:function(value,row,index){
						return changeDateFormat(value);
					},
				},
				{
					title:'离开项目时间',
					halign :'center',
					align : 'center',
					field:'endDate',
					width: 73,
					formatter:function(value,row,index){
						return changeDateFormat(value);
					},
				},
				{
					title:'状态',
					halign :'center',
					align : 'center',
					field:'status',
					width: 60,
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
							+'&nbsp;&nbsp;&nbsp;<a href="###" style="color:blue" onclick="delrowData(\'' + row.no + '\',\'' + row.zrAccount + '\')">' + '移除' + '</a>'
							+'&nbsp;&nbsp;&nbsp;<a href="###" style="color:blue" onclick="restRank(\'' + row.rank + '\',\'' + row.zrAccount + '\')">' + '重置职级' + '</a>';
					}
				}
			],
			locale:'zh-CN',//中文支持,
		});
	}

	function tableHeight() {
		return $(window).height() - 30;
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

	function initRenliData(){
		$.ajax({
			url : getRootPath()+ '/user/getPersonnel',
			async: false,
			data : {
				proNo : projNo,
			},
			success : function(data) {
				if (data.code == "success") {
					var renyuan = data.data;
					$("#zrl").html(formatResult(renyuan.renlinum,false));
					$("#gjjs").html(formatResult(renyuan.guanjiannum,false));
					$("#wtry").html(formatResult(renyuan.wentinum,false));
					renlinum = formatResult(renyuan.renlinum,false);
					guanjiannum = formatResult(renyuan.guanjiannum,false);
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
			url : getRootPath() + "/manpowerBudget/getManpowerBudgetByNo",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				proNo : projNo,
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
			width1= formatResult((renlinum/kaifaCount)*100,true)
			$("#percet1").html(Math.round(width1)+"%");
			$("#width1").css("width",Math.round(width1)+"%");
		}
		if (keyroleCount==0){
			width2 = 0
			$("#percet2").html(Math.round(width2)+"%");
			$("#width2").css("width",Math.round(width2)+"%");
		} else {
			width2= formatResult((guanjiannum/keyroleCount)*100,true)
			$("#percet2").html(Math.round(width2)+"%");
			$("#width2").css("width",Math.round(width2)+"%");
		}
	}
	//移除成员
	$(document).on("click", "a[name=del]", function () {
		var tds = $(this).parents('tr:first')[0].childNodes;
		$.ajax({
			url: getRootPath() + '/user/getOMPUserByAuthor',
			type: 'post',
			data:{
				userid : userid,
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
		var rows = $('#tr');
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

	function CompareDate(d1,d2){
		return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
	}
	function getOMPUserByAuthor(td,author){
		$.ajax({
			url: getRootPath() + '/user/getOMPUserByAuthor',
			type: 'post',
			data:{
				userid : userid,
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



	//重置职级
	$(document).on("click", "a[name=resetRank]", function () {
		var tds = $(this).parents('tr:first')[0].childNodes;

		resetRank(tds[2].innerText, tds[6].innerText, "project");
	});
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
		document.getElementById("addForms").reset();
		initFormsData()
		getMemberRank();
		$("#sgAccountAdds").empty();
		$.ajax({
			url : getRootPath() + "/codeMaster/getMemberSVN",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				no : projNo
			},
			success : function(data) {
				var option ='<option label="'+"请选择"+'" value="">'+"请选择"+'</option>'
				_.each(data.rows, function(val, index){
					option +='<option label="'+val.AUTHOR+'" value="'+val.AUTHOR+'">'+val.AUTHOR+'</option>';
				});
				$("#sgAccountAdds").html(option);

			}
		});
		$("#iteAddPages").modal('show');
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
			$.ajax({
				url:getRootPath() + '/user/projectMembersAdd',
				type : 'post',
				dataType: "json",
				contentType : 'application/x-www-form-urlencoded', //设置请求头信息
				async:false,
// 				data:JSON.stringify($('#addForm').serializeJSON()),
				data:{
					name:$("#memberNameAdd").val(),
					zrAccount:$("#zrAccountAdds").val(),
					hwAccount:$("#hwAccountAdds").val(),
					proNo:projNo,
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
						$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembersByNo'})
						initRenliData();
						Loadingtdcy();
						$('#addForms').data('bootstrapValidator').resetForm(true);
						toastr.success('保存成功！');
					} else{
						toastr.error('添加失败!');
					}
				}
			});
		}
	});


	//打开修改页面
	$('#btn_edits').click(function(){
		document.getElementById("editForms").reset();
		initFormsData()
		getMemberRank();
		var selectRow = $('#userManagerlocal').bootstrapTable('getSelections');
		if(selectRow.length == 1){
			$("#editPages").modal('show');
		}else{
			toastr.warning('请选择一条数据进行编辑');
			return;
		}
		$.ajax({
			url:getRootPath() + '/user/editProjectPages',
			type:'post',
			data:{
				no : projNo,
				zrAccount : selectRow[0].zrAccount
			},
			success : function(data){
				if(data.code == 'success'){
					if(data.rows){
						$("#memberNameedit").val(data.rows.name);
						$("#zrAccountedit").val(data.rows.zrAccount);
						$("#hwAccountedit").val(data.rows.hwAccount);
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


	});

	$('#edit_backBtns').click(function(){
		$("#editPages").modal('hide');
	});

	//修改保存
	$('#edit_saveBtns').click(function(){
		$('#editForms').bootstrapValidator('validate');
		if($("#editForms").data('bootstrapValidator').isValid()){
			$.ajax({
				url:getRootPath()+"/user/updateProjectMembers",
				type : 'post',
				data:{
					userid : $("#useridsss").val(),
					name : $("#memberNameedit").val(),
					zrAccount : $("#zrAccountedit").val(),
					hwAccount : $("#hwAccountedit").val(),
					no : projNo,
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
						$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembersByNo'});
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
					data:{
						nos:JSON.stringify(nos),
						zrAccounts:JSON.stringify(zrAccounts),
					},
					success:function(data){
						if(data.code == 'success'){
							toastr.success('删除成功！');
							$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembersByNo'});
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
			projNo = getQueryString("projNo");
		}
		initRenliData();
		Loadingtdcy();
	})
})

function changeValue(obj){
	$(obj).attr("value", $(obj).val());
}

function getMemberAccount(){
	return getProjectAccount(window.parent.projNo);
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

function loadstaffinfo(){

	$("#editPages").modal('show');

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
function editData1(no,zrAccount){
	document.getElementById("editForms").reset();
	initFormsData();
	getMemberRank();
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
		url:getRootPath() + '/user/editProjectPages',
		type:'post',
		data:{
			no : no,
			zrAccount : zrAccount
		},
		success : function(data){
			if(data.code == 'success'){
				if(data.rows){
					$("#memberNameedit").val(data.rows.name);
					$("#zrAccountedit").val(data.rows.zrAccount);
					$("#hwAccountedit").val(data.rows.hwAccount);
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
						$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembersByNo'});
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
