var proNo;
var nowDate;
var selectedDate;
var manualEdit;
var model = 0;
//加载361成熟度模板
var template = '1';
(function() {
	proNo = window.parent.projNo;
	var count = 0;
	var myRef;
	
	function refreshPage() {
		//弹出层结束后,刷新主页面
	    myRef = setTimeout("window.location.reload()", 1000); 
	}
	
	$(document).on("click", "#publishReview", function() {
		if(changeReview()){
			var status = $("#status").html();
			var review = $("#review").html();
			$("#projectStatus").val(status);
			$("#projectReview").val(review);
			$.ajax({
				url : getRootPath() + '/projectReview/getSelectProjectReview',
				type : 'post',
				data : {
					no : proNo,
				},
				success : function(data) {
					$("#selectReview").empty();
					$("#selectReview").append("<option value=''>往期点评</option>");
					for (var i = 0; i < data.length; i++) {
						var review = data[i].project_review;
						var time = data[i].statistical_time;
						if(review.length>10){
							$("#selectReview").append("<option value='"+review+"' title='"+review+"'>"+time+": "+review.substring(0,11)+"</option>");
						}else{
							$("#selectReview").append("<option value='"+review+"' title='"+review+"'>"+time+": "+review+"</option>");
						}
					}
				}
			});
			$("#publishReviewPage").modal('show');
		}
	});
	$(document).on("click", "#editReview", function() {
		num = ++count;
		$("#cancelReview").css("display","block");
		$("#closeReview").css("display","none");
		$("#editReview").css("display","none");	
		$("#resources").css("display","block");
		if(model == 1){				
			$("#progressEdit1").css("visibility","hidden");
			$("#changeEdit1").css("visibility","hidden");
			$("#quality").css("display","none");
			$("#progress").css("display","none");
			$("#changes").css("display","none");
		}else{
			$("#progressEdit1").css("visibility","hidden");
			$("#changeEdit1").css("visibility","hidden");
			$("#quality").css("display","block");
			$("#progress").css("display","block");
			$("#changes").css("display","block");	
		}
	});
	$(document).on("click", "#cancelReview", function() {
		$("#progressEdit1").css("visibility","hidden");
		$("#changeEdit1").css("visibility","hidden");
		$("#closeReview").css("display","block");
		$("#editReview").css("display","block");
		$("#cancelReview").css("display","none");
		$("#quality").css("display","none");
		$("#progress").css("display","none");
		$("#resources").css("display","none");
		$("#changes").css("display","none");
	});
	$(document).on("click", "#publishReview_saveBtn", function() {
		$("#progressEdit1").css("visibility","hidden");
		$("#changeEdit1").css("visibility","hidden");
		$("#closeReview").css("display","block");
		$("#editReview").css("display","block");
		$("#cancelReview").css("display","none");
		$("#quality").css("display","none");
		$("#progress").css("display","none");
		$("#resources").css("display","none");
		$("#changes").css("display","none");		
		var review = $("#projectReview").val();
		if(null!=review&& ""!=review){
			$.ajax({
				url : getRootPath() + '/projectReview/publishProjectReview',
				type : 'post',
				data : {
					proNo : proNo,
					date : selectedDate,
					review : review
				},
				success : function(data) {
					if (data.code == "200") {
						$("#review").html(review);
						toastr.success('项目周报发布成功！');
						$("#publishReviewPage").modal('hide');
					} else {
						toastr.error('项目周报发布失败!');
					}
				}
			});
		}else{
			toastr.error('请输入点评&趋势预测内容!');
		}
		
	});
	
	$(document).on("click", "#publishReview_backBtn", function() {
		$("#publishReviewPage").modal('hide');
	});
	
	//结项按钮
	$(document).on("click", "#closeReview", function() {
		if(changeReview()){
			var status = $("#status").html();
			var review = $("#review").html();
			$("#projectStatus1").val(status);
			$("#projectReview1").val(review);
			$.ajax({
				url : getRootPath() + '/projectReview/getSelectProjectReview',
				type : 'post',
				data : {
					no : proNo
				},
				success : function(data) {
					$("#selectProjectReview").empty();
					$("#selectProjectReview").append("<option value=''>往期点评</option>");
					for (var i = 0; i < data.length; i++) {
						var review = data[i].project_review;
						var time = data[i].statistical_time;
						if(review.length>10){
							$("#selectProjectReview").append("<option value='"+review+"' title='"+review+"'>"+time+": "+review.substring(0,11)+"</option>");
						}else{
							$("#selectProjectReview").append("<option value='"+review+"' title='"+review+"'>"+time+": "+review+"</option>");
						}
					}
				}
			});
			$("#closeReviewPage").modal('show');
		}
	});
	
	$(document).on("click", "#closeReview_saveBtn", function() {
		var review = $("#projectReview1").val();
		if(null!=review&& ""!=review){
			$.ajax({
				url : getRootPath() + '/projectReview/closeProjectReview',
				type : 'post',
				data : {
					proNo : proNo,
					date : selectedDate,
					review : review
				},
				success : function(data) {
					if (data.code == "200") {
						$("#review").html(review);
						
						toastr.success('项目结项配置成功！');
						$("#closeReviewPage").modal('hide');
						refreshPage();
					} else {
						toastr.error('项目结项配置失败!');
					}
				}
			});
		}else{
			toastr.error('请输入点评&趋势预测内容!');
		}
		
	});
	
	$(document).on("click", "#closeReview_backBtn", function() {
		$("#closeReviewPage").modal('hide');
	});

	/* --------变更（变更率）编辑---------- */
	changeDemand = function() {
		var demandChange = $("#changeNumberEdit").val();
		var demandTotal = $("#demandNumberEdit").val();
		var sp = 0.00;
		if (demandTotal == 0) {
			sp = 0.00;
		} else {
			sp = parseFloat(demandChange / demandTotal * 100).toFixed(2);
		}
		if (sp < 0) {
			sp = 0;
		} else if (sp > 100) {
			sp = 100;
		}
		$("#changesEdit").val(sp.toString());

	};
	$(document).on("click", "#changeEdit", function() {
		if(changeReview()){
			var demandTotal = $("#changeNumber").html();
			var demandChange = $("#demandNumber").html();
			var changes = $("#changesText").html();

			$("#changeNumberEdit").val(demandTotal);
			$("#demandNumberEdit").val(demandChange);
			$("#changesEdit").val(changes);
			$("#changeEditPage").modal('show');
		}
	});
	$(document).on("click", "#changeEdit_saveBtn", function() {
		$('#changeEditForm').bootstrapValidator('validate');
		var changeNumber = $("#changeNumberEdit").val();
		var demandNumber = $("#demandNumberEdit").val();
		var changes = $("#changesEdit").val();

		if ($("#changeEditForm").data('bootstrapValidator').isValid()) {

			$.ajax({
				url : getRootPath() + "/projectReview/changeEdit",
				type : 'post',
				data : {
					proNo : proNo,
					demandNumber : demandNumber,
					changeNumber : changeNumber,
					changes : changes,
					statisticalTime : nowDate
				},
				success : function(data) {
					// 后台返回添加成功
					if (data.code == 'success'&&data.data != null) {
						$("#changeNumber").html(formatResult(changeNumber, false));
						$("#demandNumber").html(formatResult(demandNumber, false));
						$("#changesText").html(formatResult(changes, true));
						formatLamp(data.data.lamp, 'changes');
						initReviewStatus(data.data.status);
						$("#changeEditPage").modal('hide');
						toastr.success('编辑成功！');
					} else {
						toastr.error('编辑失败!');
					}
				}
			});
		}
	});
	$(document).on("click", "#changeEdit_backBtn", function() {
		$("#changeEditPage").modal('hide');
	});

	checkManualValue = function(val) {
		var num;
		if(manualEdit=="quality"){
			num = 15;
		}else if(manualEdit=="progress"){
			num = 10;
		}else if(manualEdit=="resources"){
			num = 25;
		}else {
			num = 100;
		}
		
		if (val < 0 || val > num) {
			$("#manualValue").val("");
			$("#err2").css("display", "block");
			$("#err2").html('<font style="color:red;font-size:15px;">请输入0—'+num+'之间的数字！</font>');
			setTimeout(function() {
				$("#err2").css("display", "none");
			}, '3000');
		}
	};
	
	/*----------进度（偏差率）编辑---------------------*/
	changeDemandCompletion = function() {
		var demandCompletion = $("#demandCompletionEdit").val();
		var progressCompletion = $("#progressCompletionEdit").val();
		var pf = parseFloat(
				(demandCompletion - progressCompletion) / progressCompletion
						* 100).toFixed(2);
		if (pf < -100) {
			pf = -100;
		} else if (pf > 100) {
			pf = 100;
		}
		$("#progressDeviationEdit").val(pf.toString());

	};

	checkDemandCompletion = function(val) {
		if (val < 0 || val > 100) {
			$("#demandCompletionEdit").val("");
			$("#err1").css("display", "block");
			$("#err1").html('<font style="color:red;font-size:15px;">请输入0—100之间的数字！</font>');
			setTimeout(function() {
				$("#err1").css("display", "none");
			}, '3000');
		}
	};
	
	
	$(document).on("click", "#progressEdit", function() {
		if(changeReview()){
			var demandCompletion = $("#demandCompletion").html();
			var progressCompletion = $("#progressCompletion").html();
			var progressDeviation = $("#progressDeviation").html();

			$("#demandCompletionEdit").val(demandCompletion);
			$("#progressCompletionEdit").val(progressCompletion);
			$("#progressDeviationEdit").val(progressDeviation);
			$("#progressEditPage").modal('show');
		}
	});
	$(document).on("click","#progressEdit_saveBtn",function() {
		$('#progressEditForm').bootstrapValidator('validate');
		var demandCompletion = $("#demandCompletionEdit").val();
		var progressCompletion = $("#progressCompletionEdit").val();
		var progressDeviation = $("#progressDeviationEdit").val();
		if ($("#progressEditForm").data('bootstrapValidator').isValid()) {
			$.ajax({
					url : getRootPath()+ "/projectReview/progressEdit",
					type : 'post',
					data : {
							proNo : proNo,
							demandCompletion : demandCompletion,
							progressCompletion : progressCompletion,
							progressDeviation : progressDeviation,
							statisticalTime : nowDate
					},
					success : function(data) {
						// 后台返回添加成功
						if (data.code == 'success'&&data.data != null) {
							$("#demandCompletion").html(formatResult(demandCompletion, true));
							$("#progressCompletion").html(formatResult(progressCompletion, true));
							$("#progressDeviation").html(progressDeviation);
						
							formatLamp(data.data.lamp,'progress');
							initReviewStatus(data.data.status);
							$("#progressEditPage").modal('hide');
							toastr.success('编辑成功！');
						} else {
							toastr.error('编辑失败!');
						}
					}
				});
			}
		});
	$(document).on("click", "#progressEdit_backBtn", function() {
		$("input[type='radio']").removeAttr('checked');
		$("#progressEditPage").modal('hide');
	});
	
	$(document).ready(function() {
		getTemplateValue();
		Menus = getCookie('Menulist');
		if (Menus.indexOf("-58-") == -1) {
			$("#publishReview").css("display","none");
			$("button[name='lampEdit']").attr("disabled", "disabled");
			$("#editReview").css("display","none");
			$("#closeReview").css("display","none");
			$("#saveReview").css("display","none");
			$("#cancelReview").css("display","none");
		}
		initReview();

		$("input[name='lampMode']").change(function() {
			if (this.value == '0') {
				$("#autoValue").css("display","block");
				$("#manualValue").css("display","none");
				$("#importValue").css("display","none");
			}else if (this.value == '1') {
				$("#autoValue").css("display","none");
				$("#manualValue").css("display","block");
				$("#importValue").css("display","none");
			}else if (this.value == '2') {
				$("#autoValue").css("display","none");
				$("#manualValue").css("display","none");
				$("#importValue").css("display","block");
			}
		});

		$("#lampEdit_backBtn").click(function(){
			$("#lampEditPage").modal('hide');
		});
		$("#lampEdit_saveBtn").click(function(){
			$('#lampEditForm').bootstrapValidator('validate');
			var lampMode = $("input[name='lampMode']:checked").val();
			var manualValue = $("#manualValue").val();
			if(lampMode == '1'){
				if(manualValue == null || manualValue == ''){
					toastr.error('手动模式点灯得分不能为空！！！');
					return;
				}
			}else if(lampMode =='2'){
				manualValue = $("#importValue").val();
			}
			if ($("#lampEditForm").data('bootstrapValidator').isValid()) {
				$.ajax({
					url : getRootPath()+ "/projectReview/lampModeEdit",
					type : 'post',
					data : {
						proNo : proNo,
						statisticalTime : nowDate,
						field : $("#field").val(),
						autoValue : $("#autoValue").val(),
						manualValue: manualValue,
						lampMode : $("input[name='lampMode']:checked").val()=='2'?'1':$("input[name='lampMode']:checked").val()
					},
					success : function(data) {
						// 后台返回添加成功
						if (data.code == 'success'&&data.data!=null) {
							formatLamp(data.data.lamp,$("#field").val());
							initReviewStatus(data.data.status);
							$("#lampEditPage").modal('hide');
							toastr.success('编辑成功！');
						} else {
							toastr.error('编辑失败!');
						}
					}
				});
			}

		});
	});
	function getTemplateValue() {
		$.ajax({
			url: getRootPath() + '/project/getTemplateValue',
			type: 'post',
			data : {
				'proNo':proNo,
			},
			async: false,//是否异步，true为异步
			success: function (data) {
				if(null==data.data||""==data.data){
					$.ajax({
						url : getRootPath() + "/bu/projOverviewData",
						type : 'POST',
						async: false,//是否异步，true为异步
						data : {
							no : proNo,
						},
						success : function(data) {
							if(data.type =='FP'){
								template = '2';
							}else{
								template = '1';
							}
						}
					});
				}else{
					template = data.data;
				}
			}
		});
	}
})();

function initReview() {	
	var dateSection = window.parent.document.getElementById("dateSection");
	nowDate = dateSection[0].value;
	selectedDate = dateSection.value;
	if(selectedDate=="时间配置不在周期内"){
		return false;
	}else{
		$.ajax({
			type : "post",
			url : getRootPath()+ '/projectReview/getProjectReviewDetail',
			data : {
				proNo : proNo,
				date : selectedDate,
				flag : changeReview()
			},
			success : function(data) {
				if (data.code == "success") {
					var report = data.data;
					if(report){
						model = report.type;
						if(model!=1){
							formatLamp(report.qualityLamp, 'quality');
							formatLamp(report.progressLamp, 'progress');
							formatLamp(report.changesLamp, 'changes');
							$("#redLight").html(formatResult(report.redLight, false));
							$("#yellowLight").html(formatResult(report.yellowLight, false));
							$("#greenLight").html(formatResult(report.greenLight, false));
							$("#progressDeviation").html(report.progressDeviation);
							$("#demandCompletion").html(formatResult(report.demandCompletion, true));
							$("#progressCompletion").html(formatResult(report.progressCompletion, true));
							$("#changesText").html(formatResult(report.changes, true));
							$("#changeNumber").html(formatResult(report.changeNumber, false));
							$("#demandNumber").html(formatResult(report.demandNumber, false));
						}else{
							$("#measureCommentTree").css("display","none");
						}
						initMeasureList(report.measureList);
						
						$("#memberDiffer").html(formatResult(report.memberDiffer, false));
						$("#memberArrival").html(formatResult(report.memberArrival, true));
						$("#keyRolesDiffer").html(formatResult(report.keyRolesDiffer, false));
						$("#keyRolesArrival").html(formatResult(report.keyRolesArrival, true));
						$("#keyRolesPass").html(formatResult(report.keyRolesPass, false));
						$("#keyRolesfail").html(formatResult(report.keyRolesfail, false));
						$("#resourcesText").html(formatResult(report.resources, true));
						formatLamp(report.resourcesLamp, 'resources');
		 		
						$("#review").html(report.projectReview);
						initReviewStatus(report.projectStatus);
					}
				}else{
					toastr.error('服务请求失败，请稍后再试！');
				}

			}
		});
	}
}

function changeReview() {
	var flag = true;
	$.ajax({
		type : "post",
		url : getRootPath() + '/projectReview/getCloseDateByNo',
		async: false,//是否异步，true为异步
		data : {
			proNo : proNo
		},
		success : function(data) {
			if (data != null && data != "") {
				$("#publishReview").attr("disabled", "disabled");
				$("a[name='edit']").css("color", "#ece4e4");
				$("button[name='lampEdit']").attr("disabled", "disabled");
				$("#editReview").attr("style","display:none;");
				$("#publishReview").attr("style","display:none;");
				$("#closeReview").attr("style","display:none;");
				flag = false;
			}else{
				if (nowDate != selectedDate) {
					$("#publishReview").attr("disabled", "disabled");
					$("a[name='edit']").css("color", "#ece4e4");
					$("button[name='lampEdit']").attr("disabled", "disabled");
					$("#editReview").attr("style","display:none;");
					$("#publishReview").attr("style","display:none;");
					$("#closeReview").attr("style","display:none;");
					flag = false;
				} else {
					$("#publishReview").removeAttr("disabled");
					$("a[name='edit']").css("color", "#23527c");
					$("button[name='lampEdit']").removeAttr("disabled");
				}
			}
		}
	});	
	return flag;	
}

function lampEdit(field){
	$.ajax({
		type : "post",
		url : getRootPath()+ '/projectReview/getProjectLampMode',
		data : {
			proNo : proNo,
			date : nowDate,
			field : field
		},
		success : function(data) {
			if (data.code == "success") {
				var lamp = data.data;
				if(field=="quality"){
					$("#lampText").html("质量")
				}else if(field=="progress"){
					$("#lampText").html("进度")
				}else if(field=="resources"){
					$("#lampText").html("资源")
				}else if(field=="changes"){
					$("#lampText").html("变更")
				}
				manualEdit = field;
				$("#autoValue").val(lamp.autoValue);
				$("#manualValue").val(lamp.manualValue);
				if(field =="resources"){
					$("#labelMode2").css("display","");
					$.ajax({
						type : "post",
						url : getRootPath()+ '/projectReview/getProjectManualValue',
						data : {
							proNo : proNo,
							date : nowDate,
							template : template
						},
						success : function(data) {
							if (data.code == "success") {
								var value = data.data*1;
								if(template=="1"){
									value = (value/40*25);
								}else{
									value = (value/30*25);
								}
								if(value > 25){
									value = 25;
								}else if(value < 0){
									value = 0;
								}
								$("#importValue").val(parseFloat(value.toFixed(2)));
							}else{
								toastr.error('服务请求失败，请稍后再试！');
							}
						}
					});
				}else{
					$("#importValue").val("");
					$("#labelMode2").css("display","none");
				}
				$("#field").val(field);

				$("input[type='radio']").removeAttr('checked');
				if(lamp.lampMode==0){
					$("#autoValue").css("display","block");
					$("#manualValue").css("display","none");
					$("#importValue").css("display","none");
					$("#mode0").prop('checked','checked');
				}else if(lamp.lampMode==1){
					$("#autoValue").css("display","none");
					$("#manualValue").css("display","block");
					$("#importValue").css("display","none");
					$("#mode1").prop('checked','checked');
				}
				
				$("#lampEditPage").modal('show');			
			}else{
				toastr.error('服务请求失败，请稍后再试！');
			}
		}
	});
	
}

function formatResult(val, flag) {
	if (val < 0 || val == "" || val == null) {
		return 0;
	} else if (flag && val > 100) {
		return 100;
	} else {
		return val;
	}
}

function formatLamp(val, id) {
	$("#" + id + 'Lamp').css("border-left-color", getLamp(id, val).lamp);
	$("#" + id + 'Mark').html(val);
}

function initMeasureList(list) {
	var data= JSON.parse(list);
	var ret = "";
	
	_.each(JSON.parse(list), function(item, index) {
		ret += "<div><span>" + item.measureName + "：</span><span>"
				+ item.measureValue + "</span></div>";
	});
	$("#measures").html("");
	$("#measures").append(ret);
}

function initReviewStatus(status) {
	var data = getLamp('status', status);
	$("#statusText").html(data.text);
	$("#statusColor").css("background-color", data.lamp);
	$("#status").html(status);	
}

function selectChange(){
	var tes = $("#selectProjectReview").find("option:selected").attr("title");
	if(tes =="往期点评"){
		$("#projectReview1").val("");
	}else{
		$("#projectReview1").val(tes);
		
	}
}
function selectChangeReview(){
	var tes = $("#selectReview").find("option:selected").attr("title");
	if(tes =="往期点评"){
		$("#projectReview").val("");
	}else{
		$("#projectReview").val(tes);
	}
}
