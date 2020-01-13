var projNo;
var buName;
var poster = [];
var proPage;
var nowDate;
var selectedDate;
var manualEdit;
var model = 0;
//加载361成熟度模板
var template = '1';
(function() {
	var bsTable;
	projNo = getQueryString("projNo");
	buName = window.parent.projBU;
	var units = {};
	function initData() {
		$.ajax({
			url : getRootPath() + "/bu/projOverviewData",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				no : projNo
			},
			success : function(data) {
				$("#name").empty();
				$("#pm").empty();
				$("#codeAmount").empty();
				$("#projectWorkload").empty();

				$("#startDate").empty();
				$("#endDate").empty();
				$("#progressDeviation").empty();
				$("#type").empty();

				$("#bu").empty();
				$("#pdu").empty();
				$("#du").empty();
				$("#hwpdu").empty();

				$("#hwzpdu").empty();
//				$("#qa").empty();
//				$("#se").empty();
				$("#projectState").empty();
				$("#isOffshore").empty();

				$("#projectType").empty();
				$("#coopType").empty();
				$("#teamName").empty();
				$("#pduSpdt").empty();
				$("#area").empty();

				$("#projectSynopsis").empty();
				$("#projectOffShore").empty();
				$("#projectBudget").empty();

				$("#name").append(data.name);
				$("#name").attr("title",data.name);
				$("#pm").append(data.pm);
				$("#startDate").append(new Date(data.startDate).format('yyyy-MM-dd'));
				$("#endDate").append(new Date(data.endDate).format('yyyy-MM-dd'));
				$("#type").append(data.type);
				proPage= data.type;
				$("#bu").append(data.bu);
				$("#pdu").append(data.pdu);
				$("#du").append(data.du);
				$("#hwpdu").append(data.hwpdu);
				$("#hwzpdu").append(data.hwzpdu);
//				$("#qa").append(data.qa);
				$("#projectState").append(data.projectState);
				$("#isOffshore").append(data.isOffshore);
				$("#projectType").append(data.projectType);
				$("#coopType").append(data.coopType);
				$("#teamName").append(data.teamName);
				$("#pduSpdt").append(data.pduSpdt);
				$("#area").append(data.area);
				$("#projectSynopsis").append(data.projectSynopsis);
				$("#projectSynopsis").attr("title",data.projectSynopsis);
				$("#projectOffShore").append(data.isOffshore);
				$("#projectBudget").attr("value",data.projectBudget);
				//给当前项目默认配置commit的代码统计方式
				//先判断当前项目是否已经配置过
				$.ajax({
					url:getRootPath() +'/codeType/codeTypeConfig',
					type:'post',
					data : {
						no : projNo
					},
					success : function(data){
						if((!data.data)||(data.data.length==0)){
							$.ajax({
								url:getRootPath() +'/codeType/saveCodeType',
								type:'post',
								data : {
									no : projNo,
									type : '0',
									textMeanType : '0'
								}
							});
						}
					}
				});
			}
		})
	}

	//日期转换函数
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
	$.fn.loadzbTop=function(){
		bsTable.refresh();
	};

	/*项目评估开始*/
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
					no : projNo,
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
					proNo : projNo,
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
					no : projNo,
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
					proNo : projNo,
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
					proNo : projNo,
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
					proNo : projNo,
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
	/*项目评估结束*/



	$(document).ready(function(){
		organizationalStructure();
		initData();
		/*teamFormation();
        roleManagement();
        $("#361chengshudu").load("gerennengli_361chengshudu.html",function(){
            everyScore();
            comments();
            commentsList();
        });*/

		/*项目评估开始*/
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
		addDateSectionDiv();
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
						proNo : projNo,
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
		/*项目评估结束*/
	});

    $(document).ready(function(){
        var Menus = getCookie('Menulist');
        var flag = false;
        if (Menus.indexOf("-56-") != -1) {
            var tab1 = '<li tabname="tab-lcb" id="lcb" onclick="lichengbei()" style="border: 0px;margin-left: 15px;"><a href="###">里程碑</a></li>';
            $("#gaiKuang").append(tab1);
            if (!flag) {
                $("#lcb").click();
                flag = true;
            }
        }
        if (Menus.indexOf("-57-") != -1) {
            var tab2 ='<li tabname="361chengshudu" id="361chengshudu1" onclick="chengshudu()" style="border: 0px;margin-left: 15px;"><a href="###">361成熟度</a></li>';
            $("#gaiKuang").append(tab2);
            if(!flag){
                $("#361chengshudu1").click();
                flag = true;
            }
        }
        if (Menus.indexOf("-58-") != -1) {
            var tab3 ='<li tabname="tab-xmpg" id="xmpg" onclick="projectEvaluation()" style="border: 0px;margin-left: 15px;"><a href="###">项目评估</a></li>';
            $("#gaiKuang").append(tab3);
            if(!flag){
                $("#xmpg").click();
                flag = true;
            }
        }
    })
})();

function tableHeight() {
	return $(window).height() - 30;
}

var oldtable;
var oldtable2;
var oldtable3;
function tableAdd() {
	var tab = '<tr style="height: 52px;">'
		+'<td date-type="select" select-date="PM,产品经理,SE,MDE,BA,IA,TC,TSE,QA,TL">PM</td>'
		+'<td date-type="input" name="staffname">xxx</td>'
		+'<td date-type="input" name="zrAccount">000000</td><td date-type="input" name="hwAccount"></td>'
		+'<td date-type="select" select-date="未通过,通过,不涉及">未通过</td>'
		+'<td date-type="select" select-date="未通过,通过,不涉及">未通过</td>'
		+'<td date-type="select" select-date="完全胜任,基本胜任,暂不胜任">完全胜任</td>'
		+'<td date-type="select" select-date="在岗,后备">在岗</td>'
		+'<td date-type="no"><div name="del"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td>'
		+'</tr>';
	$("#guanjianjuese tbody").append(tab);
}
function tableAdd2() {
	var tab = '<tr style="height: 52px;">'
		+'<td date-type="select" select-date="CP1,CP2,CP3,迭代1,迭代2,迭代3,迭代4,迭代5,迭代6">CP1</td>'
		//+'<td date-type="select" select-date="CP,迭代点">CP</td>'
		+'<td date-type="date" name="planDate">2018-01-01</td>'
		+'<td date-type="date" name="actualDate">2018-01-01</td>'
		+'<td date-type="no"   name="deviationRate">0%</td>'
		+'<td date-type="no"><div name="del1"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td></tr>';
	$("#cp-review tbody").append(tab);
}
function tableAdd3() {
	var tab = '<tr style="height: 52px;">'
		+'<td date-type="select" select-date="开发投入,问题单修改投入,版本公共事务投入,测试执行投入,测试用例设计投入,自动化用例写作">开发投入</td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="input" name="baifenbi"></td>'
		+'<td date-type="no"><div name="del3"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td></tr>';
	$("#monthlyManpowerProportion tbody").append(tab);
}
function delToNone(del,display) {
	$("div[name='"+del+"']").each(function(){
		$(this).css('display',display);
	});
}
function tableCancel() {
	$("#guanjianjuese tbody").empty();
	$("#guanjianjuese tbody").append(oldtable);
	delToNone('del','none');
	var edit=$("#edit");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#guanjianjuesedo").css('display','none');
}
function tableCancel2() {
	$("#cp-review tbody").empty();
	$("#cp-review tbody").append(oldtable2);
	delToNone('del1','none');
	var edit=$("#edit2");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#cp-reviewdo").css('display','none');
}
function tableCancel3() {
	$("#monthlyManpowerProportion tbody").empty();
	$("#monthlyManpowerProportion tbody").append(oldtable3);
	delToNone('del3','none');
	var edit=$("#edit3");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#proportionAdjustment").css('display','none');
}

function showPopover(msg) {
	$('#dataAcquisition').text(msg);
	$('#submitImportmodalfooter').css('display','none');
	$('#savetoop').modal('show');
	//2秒后消失提示框
	var id = setTimeout(function () {
		$('#savetoop').modal('hide');
	}, 2000);
}
function showPopoverErr(msg) {
	$('#dataAcquisition1').text(msg);
	$('#submitImportmodalfooter').css('display','block');
	$('#savetoop1').modal('show');
}
function hidePopover() {
	$('#savetoop').modal('hide');
}
function tableSave() {
	var edit=$("#edit");
	var ret = tableSaveToDao();
	if(ret=="err"){
		showPopoverErr("中软工号不能有重复的，请修改之后再保存");
		return;
	}
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#guanjianjuesedo").css('display','none');
	delToNone('del','none');
	showPopover("关键角色保存成功");
}

function tableSaveToDao() {
	var tab = $('#guanjianjuese');
	var rows = tab[0].rows;
	var allTr = "";
	var zrAccounts=new Array()
	for(var i = 0; i<rows.length; i++ ){
		if(rows[i].cells[1].tagName=="TH"){
			continue;
		}
		zrAccounts[i]=rows[i].cells[2].innerHTML;
		allTr +='{"no":"'+projNo+'",'+
			'"name":"'+rows[i].cells[1].innerHTML+'",'+
			'"zrAccount":"'+rows[i].cells[2].innerHTML+'",'+
			'"hwAccount":"'+rows[i].cells[3].innerHTML+'",'+
			'"position":"'+rows[i].cells[0].innerHTML+'",'+
			'"rdpmExam":"'+rows[i].cells[4].innerHTML+'",'+
			'"replyResults":"'+rows[i].cells[5].innerHTML+'",'+
			'"proCompetence":"'+rows[i].cells[6].innerHTML+'",'+
			'"status":"'+rows[i].cells[7].innerHTML+
			'"},';
	}
	var count = 0;
	for (var i = 0; i < zrAccounts.length;i++) {
		for (var j = 0; j < zrAccounts.length; j++) {
			if(i==j){
				continue;
			}
			if (zrAccounts[i] == zrAccounts[j]) {
				count++;
			}
		}
	}
	if(count>0){
		return "err";
	}
	var jsonStr = '{"projRoles":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url : getRootPath() + "/bu/insertInfo",
		type : 'POST',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data : jsonStr,
		success : function(data) {

		}
	})
}

function tableSave2() {
	var edit=$("#edit2");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#cp-reviewdo").css('display','none');
	delToNone('del1','none');
	tableSaveToDao2();
	showPopover("里程碑信息保存成功");
}

function tableSaveToDao2() {
	var tab = $('#cp-review');
	var rows = tab[0].rows;
	var allTr = "";
	for(var i = 0; i<rows.length; i++ ){
		if(rows[i].cells[1].tagName=="TH"){
			continue;
		}
		allTr +='{"no":"'+projNo+'",'+
			'"node":"'+rows[i].cells[0].innerHTML+'",'+
			//'"nodeType":"'+rows[i].cells[1].innerHTML+'",'+
			'"planDate":"'+rows[i].cells[1].innerHTML+'",'+
			'"actualDate":"'+rows[i].cells[2].innerHTML+'",'+
			'"deviationRate":"'+rows[i].cells[3].innerHTML+'"'+
			'},';
	}
	var jsonStr = '{"projSchedule":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url : getRootPath() + "/bu/insertInfoProjectSchedule",
		type : 'POST',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data : jsonStr,
		success : function(data) {

		}
	})
}
function organizationalStructure(){
	$('#majorDuty').bootstrapTable({
		method: 'post',
		contentType: "application/x-www-form-urlencoded",
		url:getRootPath() + '/GeneralSituation/organizationalStructure',
		editable:true,// 可行内编辑
		striped: true, // 是否显示行间隔色
		dataField: "rows",
		pageNumber: 1, // 初始化加载第一页，默认第一页
		pagination:true,// 是否分页
		queryParamsType:'limit',
		sidePagination:'client',
		pageSize:5,// 单页记录数
		pageList:[5,10,20,30],// 分页步进值
		showColumns:false,
		toolbar:'#toolbar',// 指定工作栏
		toolbarAlign:'right',
		dataType: "json",
		queryParams : function(params){
			var param = {
				projNo : projNo,
			}
			return param;
		},
		onEditableSave: function (field, row, oldValue, $el) {
			$.ajax({
				type: "post",
				url: getRootPath()+"/GeneralSituation/demandUpdata",
				dataType: "json",
				async: false,
				data: {
					Id : row.id,
					value : row.demand,
					no: projNo
				},
				success: function (data, status) {
					if (data.code == "success") {
						var pageNumber=$('#majorDuty').bootstrapTable('getOptions').pageNumber;
						$('#majorDuty').bootstrapTable('refresh',{pageNumber:pageNumber});
						toastr.success('修改成功！');
					}else{
						toastr.success('修改失败！');
					}
				}
			});
		},
		columns:[
			{
				title:'岗位',
				halign :'center',
				align : 'center',
				valign: 'middle',
				field:'position',
				sortable:true,
				width:120
			},
			{
				title:'主要职责',
				halign :'center',
				align : 'center',
				valign: 'middle',
				field:'name',
				sortable:true,
				width:250
			},
			{
				title:'在岗',
				halign :'center',
				align : 'center',
				valign: 'middle',
				field:'onDuty',
				width:70,
				sortable:true
			},
			{
				title:'需求',
				halign :'center',
				align : 'center',
				field:'demand',
				valign: 'middle',
				width:70,
				sortable:true,
				editable: {
					type: "text",              //编辑框的类型。支持text|textarea|select|date|checklist等
					emptytext:'&#12288',
					placement: 'bottom',      //编辑框的模式：支持popup和inline两种模式，默认是popup
					noeditFormatter: function (value,row,index) {
						return '<a href="javascript:void(0)" style="padding-top: 0px;" data-name="demand" data-pk="undefined" data-value="'+value+'" class="editable editable-click">'+value+'</a>';
					}
				}
			},
			{
				title:'操作',
				halign :'center',
				align : 'center',
				valign: 'middle',
				field:'opera',
				width: 100,
				//'修改,删除'响应id列
				formatter:function(value,row,index){
					return '<a href="#" style="color:blue" onclick="delDemandPopulation(\'' + row.id + '\')">' + '删除' + '</a>';
				}
			}
		],
		locale:'zh-CN' // 中文支持,
	});
}
function teamFormation() {
	//层级排序PM:1, SE:2, QA:3, PL:4, TSE:5, MDE:6, TL:7。
	$("#chart-container").html("");
	var datascource = {};
	$.ajax({
		type: "post",
		url: getRootPath()+"/GeneralSituation/organizationalHierarchical",
		dataType: "json",
		async: false,
		data : {
			projNo : projNo
		},
		success: function (data) {
			var superiors = ["PM"];
			arborescence(datascource,data,superiors);
			function arborescence(v,data,superiors){
				for(var i = 0; i < data.length; i++){
					var superior = data[i].superior;
					if(superiors.indexOf(superior) != -1){
						var menu = {'name' : data[i].position,'title' : data[i].name,'children':[]};
						var names = [data[i].name];
						arborescence(menu.children,data,names);
						if(superior == "PM"){
							datascource = menu;
						}else{
							v.push(menu);
						}
					}
				}
			}
		}
	});
	$('#chart-container').orgchart({
		'data' : datascource,
		'nodeContent': 'title'
	});
}
function tableHeight() {
	return $(window).height() - 30;
}
function delDemandPopulation(v){
	/*var ID=[];
	ID.push(v);
	console.log(ID);*/
	Ewin.confirm({title: "角色删除", message: "是否要删除当前角色？"}).on(function (e) {
		if (!e) {
			return;
		} else {
			$.ajax({
				url:getRootPath() + '/GeneralSituation/delDemandPopulation',
				type:'post',
				dataType: "json",
				async: false,
				data: {
					ID : v
				},
				/*dataType: "json",
               contentType : 'application/json;charset=utf-8', //设置请求头信息
               data:JSON.stringify(ID),*/
				success:function(data){
					if(data.code == 'success'){
						toastr.success('删除成功！');
						$("#mytab").bootstrapTable('destroy');
						roleManage();
						$('#majorDuty').bootstrapTable('refresh', {url: getRootPath() + '/GeneralSituation/organizationalStructure'});
					}else{
						toastr.error('删除失败!');
					}
				}
			});
		}
	});
}
function tableSave3() {
	var edit=$("#edit3");
	var ret = tableSaveToDao3();
	if(ret=="err"){
		showPopoverErr("选择名称不能有重复的，请修改之后再保存");
		return;
	}
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#proportionAdjustment").css('display','none');
	delToNone('del3','none');

	showPopover("比例信息保存成功");
}

function isnull2double(str) {
	str = (str==null||str=="")?"0":str;
	str=str.replace("%","");
	var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
	if(!re.test(str)){
		str = "0"
	}
	return str;
}
function isnotnum2num(str) {
	var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
	if(!re.test(str)){
		return "0";
	}
	return str;
}


function tableSaveToDao3() {
	var rows = $('#monthlyManpowerProportion tbody tr');
	var allTr = "";
	var zrAccounts=new Array()
	for(var i = 0; i<rows.length; i++ ){
		if(rows[i].cells[0].innerHTML=="合计"){
			continue;
		}
		zrAccounts[i]=rows[i].cells[0].innerHTML;
		allTr +='{"name":"'+rows[i].cells[0].innerHTML+'",'+
			'"january":"'+isnull2double(rows[i].cells[1].innerHTML)+'",'+
			'"february":"'+isnull2double(rows[i].cells[2].innerHTML)+'",'+
			'"march":"'+isnull2double(rows[i].cells[3].innerHTML)+'",'+
			'"april":"'+isnull2double(rows[i].cells[4].innerHTML)+'",'+
			'"may":"'+isnull2double(rows[i].cells[5].innerHTML)+'",'+
			'"june":"'+isnull2double(rows[i].cells[6].innerHTML)+'",'+
			'"july":"'+isnull2double(rows[i].cells[7].innerHTML)+'",'+
			'"august":"'+isnull2double(rows[i].cells[8].innerHTML)+'",'+
			'"september":"'+isnull2double(rows[i].cells[9].innerHTML)+'",'+
			'"october":"'+isnull2double(rows[i].cells[10].innerHTML)+'",'+
			'"november":"'+isnull2double(rows[i].cells[11].innerHTML)+'",'+
			'"december":"'+isnull2double(rows[i].cells[12].innerHTML)+'"'+
			'},';
	}
	var count = 0;
	for (var i = 0; i < zrAccounts.length;i++) {
		for (var j = 0; j < zrAccounts.length; j++) {
			if(i==j){
				continue;
			}
			if (zrAccounts[i] == zrAccounts[j]) {
				count++;
			}
		}
	}
	if(count>0){
		return "err";
	}
	var jsonStr = '{"proNo":"'+projNo+'","manpowers":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url : getRootPath() + "/bu/insertInvestmentProportion",
		type : 'POST',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data : jsonStr,
		success : function(data) {

		}
	})
}

function tableEdit(editid,divid) {
	var edit=$("#"+editid);
	edit.css('display','none');
	if(divid=='guanjianjuesedo'){
		oldtable=$("#guanjianjuese tbody").html();
		delToNone('del','block');
	}else if(divid=='proportionAdjustment'){
		oldtable3=$("#monthlyManpowerProportion tbody").html();
		delToNone('del3','block');
	}else{
		oldtable2=$("#cp-review tbody").html();
		delToNone('del1','block');
	}
	if(edit.attr("data_obj")=="1"){
		$("#"+divid).css('display','block');
		edit.attr("data_obj","0")
	}

}

function editable(td){
	if(td[0].childNodes.length>1){
		return;
	}
	var dateType = td.attr("date-type");
	if(dateType=="input"){
		var text = td.text();
		if(td.attr("name")=="baifenbi"){
			text = isnull2double(text);
		}
		var txt = $("<input class='form-control' style='width: 100%;height: 52px;' type='text'>").val(text);
	}else if(dateType=="date"){
		var text = td.text();
		var txt = $("<input class='form-control' max='9999-12-30' style='width: 100%;height: 52px;' type='date'>").val(text);
	}else if(dateType=="no"){
		return;
	}else if(dateType=="select"){
		var text = td.text();
		var select = "<select class='form-control' style='height: 52px;'>";
		var selectDate = td.attr("select-date").split(',');
		selectDate.forEach(function(i,index){
			select += "<option>"+i+"</option>";
		})
		select += "</select>";
		var txt = $(select).val(text);
	}else{
		var text = td.text();
		var txt = $("<input class='form-control' style='width: 100%;height: 52px;' type='text'>").val(text);
	}
	// 根据表格文本创建文本框 并加入表表中--文本框的样式自己调整
	txt.blur(function(){
		// 失去焦点，保存值。于服务器交互自己再写,最好ajax
		var newText = $(this).val();
		// 移除文本框,显示新值
		$(this).remove();
		if(dateType=="input"){
			if(td.attr("name")=="baifenbi"){
				newText = isnotnum2num(newText);
				//if(newText != "0"){
				var tdssel = $("#monthlyManpowerProportion tbody td:nth-child("+(td[0].cellIndex+1)+")");
				var allbaifenbi = 0.0;
				_.each(tdssel, function(seltd, index){//(值，下标)
					allbaifenbi += parseFloat(isnull2double($(seltd).text()));
				});
				allbaifenbi+=parseFloat(newText);
				if(allbaifenbi>100){
					showPopover("总比例不能大于100%");
					newText="0";
				}else{
					$("#monthlyManpowerProportion tfoot td:nth-child("+(td[0].cellIndex+1)+")").text(allbaifenbi+"%");
				}
				//}
				if(newText==0){
					newText = "";
				}else{
					newText += "%";
				}
			}
		}
		td.text(newText);
		if(dateType=="date"){
			var planDate = td.parents('tr:first').children('td[name="planDate"]').text()
			var actualDate = td.parents('tr:first').children('td[name="actualDate"]').text()
			var deviationRate = td.parents('tr:first').children('td[name="deviationRate"]');
			if(planDate!="" && actualDate!=""){
				var startDay = $("#startDate").text();
				//			var endDay = $("#endDate").text();
				var a = datedifference(planDate,actualDate);
				var b = datedifference(startDay,planDate);
				var num = 100 + "";
				if(b!=0){
					num = (a/b)*100+"";
				}
				if(num.indexOf(".")>0){
					num = num.substring(0,num.indexOf("."));
				}

				deviationRate.text(num+"%");
			}else{
				deviationRate.text("");
			}
		}else if(dateType=="input"){
			if(td.attr("name")=="staffname"){
				$("#testDIV").fadeOut();
			}
		}
	});
	td.text("");
	td.append(txt);
	$(txt).focus();
	if(dateType=="input"){
		$(txt).select();
		testDIVdo(td,txt);
	}
}

//$(document).on("dblclick", "#guanjianjuese td", function () {
$(document).on("click", "#guanjianjuese td", function () {
	if($("#edit").attr("data_obj")=="1"){
		return;
	}
	var td = $(this);
	editable(td);

});
//$(document).on("dblclick", "#cp-review td", function () {
$(document).on("click", "#cp-review td", function () {
	if($("#edit2").attr("data_obj")=="1"){
		return;
	}
	var td = $(this);
	editable(td);

});
$(document).on("click", "#monthlyManpowerProportion tbody td", function () {
	if($("#edit3").attr("data_obj")=="1"){
		return;
	}
	var td = $(this);
	editable(td);

});

$(document).on("click", "div[name=del]", function () {
	$(this).parents('tr:first').remove();
});
$(document).on("click", "div[name=del1]", function () {
	$(this).parents('tr:first').remove();
});
$(document).on("click", "div[name=del3]", function () {
	$(this).parents('tr:first').remove();
	for (var i = 2; i <= 13; i++) {
		var tdssel = $("#monthlyManpowerProportion tbody td:nth-child("+i+")");
		var allbaifenbi = 0.0;
		_.each(tdssel, function(seltd, index){//(值，下标)
			allbaifenbi += parseFloat(isnull2double($(seltd).text()));
		});
		if(allbaifenbi>100){
			showPopover("总比例不能大于100%");
			newText="0";
		}else{
			$("#monthlyManpowerProportion tfoot td:nth-child("+i+")").text(allbaifenbi+"%");
		}
	}
});

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
						});
						select_option +="</table>";
						$("#testDIV").append(select_option);
					}
				});
				var offset = txt.offset();
				$("#testDIV").css('left', offset.left + 'px')
					.css('top', offset.top+txt[0].clientHeight + 'px')
					.css('position', 'absolute')
					.css('background', '#ffffff')
					.css('z-index', '99')
					.fadeIn();
			}
//	        $("#testDIV").mouseleave(function() {
//	        			//置空div
//	        	$("#testDIV").empty();
//	        	$("#testDIV").fadeOut();
//	        });
		});
		//回车事件
//    	var tdIndex = 1;
		$(window).keyup(function(event) {

			switch (event.keyCode) {
				// ...
				// 不同的按键可以做不同的事情
				// 不同的浏览器的keycode不同
//			case 38://上
//				_.each($("#testDIVtable td"), function(val, index){//(值，下标)
//					val.style.backgroundColor='#FFFFFF';
//				});
//				$($("#testDIVtable td")[tdIndex-1]).css('background', '#d9dada');
//				if(tdIndex<=1){
//					tdIndex = 1;
//				}else{
//					tdIndex -= 1;
//				}
//				break;
//			case 40://下
//				var max = $("#testDIVtable td").length;
//				_.each($("#testDIVtable td"), function(val, index){//(值，下标)
//					val.style.backgroundColor='#FFFFFF';
//				});
//				$($("#testDIVtable td")[tdIndex-1]).selected;
//				if(tdIndex>=max){
//					tdIndex = max;
//				}else{
//					tdIndex += 1;
//				}
//				break;
				case 13://回车
//				var selected = $($("#testDIVtable td")[tdIndex-1]).text();
//				var value = selected.split("\t");
//				var num = $('#testDIVtable').attr("num");
//				$($('td[name="staffname"]')[num-1]).text(value[0]);
//				$($('td[name="hwAccount"]')[num-1]).text(value[1]);
					$("#testDIV").fadeOut();
					txt.blur();
					break;
			}
		});

	}
}

function comments() {//默认加载361成熟度评价
	$.ajax({
		url: getRootPath() + '/project/comments',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			projNo: projNo
		},
		success: function (data) {
			//灯颜色：红：f05132    黄：ecb739   绿：3ddc66   白：f8f8f8
			$("#totalScore").text(data==null?"":data.TOTALSCORE),
				$("#mark").text(data==null?"":data.MARK),
				$("#difference").text(data==null?"":data.DIFFERENCE)
		}
	});
}

function commentsList() {//默认加载361成熟度评价
	var columns=initColumn();
	var queryParams = {
		projNo:projNo
	};
	var url = getRootPath() + '/project/commentsList';
	bsTable = new BSTable('mytable', url, columns);
	bsTable.setClasses('tableCenter');
	bsTable.setQueryParams(queryParams);
	bsTable.setPageSize(5);
	bsTable.setPageNumber(1);
	bsTable.setEditable(true);//开启行内编辑
	// bsTable.setEditable(false);
	bsTable.setOnEditableSave(initOnEditableSave());
	bsTable.init();
}
function initColumn(){
	return [
		{title:'项目编号 ',field:'id',width:80,visible: false},
		{title:'问题描述',field:'question',width:200,align : 'left',
			editable: {
				type: 'textarea',
				title: '问题描述',
				placement: 'bottom',
				validate: function (v) {
					if (!v) return '问题不能为空';
				}
			}
		},
		{title:'改进措施',field:'imprMeasure',width:300,align : 'left',
			editable: {
				type: 'textarea',
				title: '改进措施',
				placement: 'bottom',
				emptytext:'&#12288',
				validate: function (v) {
					if (!v) return '改进措施不能为空';
				}
			}
		},
		{title:'进展&效果描述',field:'progressDesc',width:200,align : 'left',
			editable: {
				type: 'textarea',
				title: '进展&效果描述',
				placement: 'bottom',
				emptytext:'&#12288',
			}
		},
		{title:'计划完成时间',field:'finishTime',width:80,
			formatter:function(value,row,index){
				return changeDateFormat(value);
			},
			editable: {
				type: 'date',
				title: '计划完成时间',
				placement: 'bottom',
				format: 'yyyy-mm-dd',
				viewformat: 'yyyy-mm-dd',
				language: 'zh-CN',
				emptytext:'&#12288',
				datepicker: {
					weekStart: 1,
				},
				validate: function (v) {
					if (!v) return '计划完成时间不能为空';
				}
			}
		},
		{title:'实际完成时间',field:'actualFinishTime',width:80,
			formatter:function(value,row,index){
				return changeDateFormat(value);
			},
			editable: {
				type: 'date',
				title: '实际完成时间',
				placement: 'bottom',
				format: 'yyyy-mm-dd',
				viewformat: 'yyyy-mm-dd',
				language: 'zh-CN',
				emptytext:'&#12288',
				datepicker: {
					weekStart: 1,
				},
				validate: function (v) {
					if (!v) return '实际完成时间不能为空';
				}
			}
		},
		{title:'责任人',field:'personLiable',width:65,
			editable: {
				type: 'checklist',
				separator:",",
				emptytext:'&#12288',
				source:getMeberSelect(),
				title: '请选择负责人',
				placement: 'bottom'
			}
		},
		{title:'状态',field:'state',width:65,
			editable: {
				type: 'select',
				title: '状态',
				emptytext:'&#12288',
				placement: 'bottom',
				source: {
					"Open":"Open",
					"Closed":"Closed"
				},
				validate: function (v) {
					if (!v) return '状态不能为空';
				}
			}
		}
	]
}
//加载项目成员下拉列表
function getMeberSelect(){
	var s = "";
	$.ajax({
		url:getRootPath() + '/IteManage/meberSelect',
		type:'post',
		dataType: "json",
		async:false,
		data:{
			proNo : projNo
		},
		success:function(data){
			if(data.code == 'success'){
				s= data.data;
			}else{
				toastr.error('加载失败!');
			}
		}
	});
	return s;
}
function initOnEditableSave(){
	return function (field, row, oldValue, $el) {
		if(field == "personLiable"){
			row.personLiable = row.personLiable.toString();
		}
		$.ajax({
			type: "post",
			url: getRootPath() + '/project/editCommentsList',
			dataType: "json",
			contentType: 'application/json;charset=utf-8', //设置请求头信息
			data: JSON.stringify(row),
			success: function (data, status) {
				if (status == "success") {
					bsTable.refresh({silent : true});
					toastr.success('修改成功！');
				} else {
					toastr.success('修改失败！');
				}
			}
		});
	}
}
var ta = 0;
var cheng = 0;
var li = 0;
var tabGjjs = function(){
	displaySkill();
	if(ta==0){
		teamFormation();
		roleManagement();
		ta++;
	}
};
var lichengbei = function() {
    displaySkill();
	if (li==0) {
        $("#tab-lcb").load("projectScheduling.html", function () {
            setOption(scheduleType, "#addSelScheduleType", "类型");
            setOption(scheduleIcon, "#addSelScheduleIcon", "图标");
            setOption(scheduleType, "#editSelScheduleType", "类型");
            setOption(scheduleIcon, "#editSelScheduleIcon", "图标");
            lcbInit();
            lcbInitData();
        });
        li++;
    }
};
var chengshudu = function(){
	displaySkill();
	if(cheng==0){
		$("#361chengshudu").load("gerennengli_361chengshudu.html",function(){
			comments();
			commentsList();
			initDateSection();
			getTemplateValue();
			initSection();
			everyScore();
		});
		cheng++;
	}
};

//项目评估
var pe=0;
var projectEvaluation = function(){
	displaySkill();
	if(pe==0){
		// $("tab-xmpg").load("projectReview.html",function(){
		// });
		pe++;
	}
};
//隐藏技能分布
function displaySkill() {
	var sbtitle=document.getElementById("skillSpread");
	sbtitle.style.display='none';
}
//显示技能分布
function skillSpread() {
	var sbtitle=document.getElementById("skillSpread");
	sbtitle.style.display='';
	var url = getAngularUrl()+"skillSpread/"+projNo;
	var html = '<object type="text/html" data="'+url+'" style="width: 100%;height: 100%;"></object>';
	document.getElementById("skill").innerHTML = html;
}
function datedifference(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式
	var dateSpan, tempDate, iDays;
	sDate1 = Date.parse(sDate1);
	sDate2 = Date.parse(sDate2);
	dateSpan = sDate2 - sDate1;
	dateSpan = Math.abs(dateSpan);
	iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
	if(sDate1>sDate2){
		return iDays*(-1);
	}
	return iDays
}

//根据窗口调整表格高度
$(window).resize(function() {
	$("#tab-lcb").css({"min-height":$(window).height()-225});
});

$(document).ready(function(){
	$("#tab-lcb").css({"min-height":$(window).height()-225});
});

function everyScore(){
	$("#chenghsuduTable").bootstrapTable({
		method: 'GET',
		contentType: "application/x-www-form-urlencoded",
		url:getRootPath() + '/project/getAssessmentCategory',
		striped: true, //是否显示行间隔色
		pageNumber: 1, //初始化加载第一页，默认第一页
		pagination: false, //是否分页
		queryParamsType: 'limit',
		sidePagination: 'server',
		minimumCountColumns: 2, //最少允许的列数
		queryParams : function(params){
			var param = {
				'projNo':projNo,
				'template':$('#chengshuduSelected').val(),
				'createTime':$('#dateSection').val()
			};
			return param;
		},
		columns:[
			{
				title:'评估项大类',
				align:'center',
				valign:'middle',
				field:'largeCategory',
				width:'120'
			},
			{
				title:'评估项类别',
				align:'center',
				valign:'middle',
				field:'category',
				width:'120'
			},
			{
				title:'评估项小类',
				align:'center',
				valign:'middle',
				field:'name',
				width:'120'
			},
			{
				title:'评估结果',
				align:'center',
				valign:'middle',
				field:'result',
				width:'120',
				formatter:function(value,row,index){
					if(value >= 3){
						//绿灯,完全具备或完全做到
						deng = '<div style="display: block;float: right;margin-right: 150px;border-radius: 50%;width: 15px;height:15px;'+
							'background-color: #1adc21; "></div>';
					}else if(value == 1){
						//红灯，不具备或未做到
						deng ='<div style=" display: block;float: right;margin-right: 150px;border-radius: 50%;width: 15px;height:15px;'+
							'background-color: #f57454; "></div>';
					}else if(value == 2){
						//黄灯，基本具备或基本做到
						deng = '<div style="display: block;float: right;margin-right: 150px;border-radius: 50%;width: 15px;height:15px;'+
							'background-color: #f7b547; "></div>';
					}else{
						//灰灯，不涉及或未到此阶段，不计分
						deng = '<div style="display: block;float: right;margin-right: 150px;border-radius: 50%;width: 15px;height:15px;'+
							'background-color: #bdb7b7; "></div>';
					}
					return deng;
				},
			}
		],
		onLoadSuccess: function (data) {
			mergeCells(data.rows, 'largeCategory', 1,  '#chenghsuduTable');
			mergeCells(data.rows, 'category', 1,  '#chenghsuduTable');
			mergeColspan(data.rows, ['category', 'name'], '#chenghsuduTable');
			mergeColspan(data.rows, ['largeCategory', 'category', 'name'], '#chenghsuduTable');//列合并
		},
		locale:'zh-CN'//中文支持
	});
};
function selectTemplate(){
	$('#chenghsuduTable').bootstrapTable('refresh');
	$.ajax({
		url: getRootPath() + '/project/updateTemplateValue',
		type: 'post',
		data : {
			'proNo':projNo,
			'template':$('#chengshuduSelected').val(),
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			if(data.code != 'success'){
				toastr.error('加载失败!');
			}
		}
	});
}

/**
 * 合并行
 * @param data  原始数据（在服务端完成排序）
 * @param fieldName 合并属性名称数组
 * @param colspan 列数
 * @param target 目标表格对象
 */
function mergeCells(data, fieldName, colspan, target) {
	if (data.length == 0) {
		$("#chenghsuduTableDiv .fixed-table-container").css({"height":250});
		return;
	}
	$("#chenghsuduTableDiv .fixed-table-container").css({"height":'100%'});
	var numArr = [];
	var value = data[0][fieldName];
	var num = 0;
	for (var i = 0; i < data.length; i++) {
		if (value != data[i][fieldName]) {
			numArr.push(num);
			value = data[i][fieldName];
			num = 1;
			continue;
		}
		num++;
	}
	numArr.push(num);
	var merIndex = 0;
	for (var i = 0; i < numArr.length; i++) {
		$(target).bootstrapTable('mergeCells', { index: merIndex, field: fieldName, colspan: colspan, rowspan: numArr[i] })
		merIndex += numArr[i];
	}
}
//加载当前日期
var page = 0 ;
if(parent.parents){
	page = parent.parents;
	parent.parents = null;
}
function initDateSection() {
	$.ajax({
		url: getRootPath() + '/measureComment/getCrreateTime',
		type: 'post',
		data : {
			num:5,
			flag:true
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			$("#dateSection").empty();
			data = data.data;
			if(null==data||""==data) return;
			var select_option="";
			var length = data.length<5 ? data.length: 5;
			for(var i = 0; i < length; i++){
				select_option += "<option value='"+data[i]+"'";
				if(page==i){
					select_option += "selected='selected'";
				}
				select_option += ">"+data[i]+"</option>";
			}
			$("#dateSection").html(select_option);
			$('#dateSection').selectpicker('refresh');
			$('#dateSection').selectpicker('render');
		}
	});
}

//加载361成熟度模板
var templatePage = '1';
function getTemplateValue() {
	$.ajax({
		url: getRootPath() + '/project/getTemplateValue',
		type: 'post',
		data : {
			'proNo':projNo,
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			if(null==data.data||""==data.data){
				if(proPage =='FP'){
					templatePage = '2';
				}else{
					templatePage = '1';
				}
			}else{
				templatePage = data.data;
			}
		}
	});
}
function initSection() {
	$.ajax({
		url: getRootPath() + '/project/getTemplateIs361',
		type: 'post',
		async: false,//是否异步，true为异步
		success: function (data) {
			$("#chengshuduSelected").empty();
			data = data.data;
			if(null==data||""==data) return;
			var select_option="";
			for(var i = 0; i < data.length; i++){
				select_option += "<option value='"+data[i].value+"'";
				if(templatePage==data[i].value){
					select_option += "selected='selected'";
				}
				select_option += ">"+data[i].key+"</option>";
			}
			$("#chengshuduSelected").html(select_option);
			$('#chengshuduSelected').selectpicker('refresh');
			$('#chengshuduSelected').selectpicker('render');
		}
	});
}
function selectDateSection(){
	$('#chenghsuduTable').bootstrapTable('refresh');
}

/**
 * 合并列
 * @param data  原始数据（在服务端完成排序）
 * @param fieldName 合并属性数组
 * @param target    目标表格对象
 */
function mergeColspan(data, fieldNameArr, target) {
	if (data.length == 0) {
		return;
	}
	if (fieldNameArr.length == 0) {
		return;
	}
	var num = -1;
	var index = 0;
	for (var i = 0; i < data.length; i++) {
		num++;
		for (var v in fieldNameArr) {
			index = 1;
			if (data[i][fieldNameArr[v]] != data[i][fieldNameArr[0]]) {
				index = 0;
				break;
			}
		}
		if (index == 0) {
			continue;
		}
		$(target).bootstrapTable('mergeCells', { index: num, field: fieldNameArr[0], colspan: fieldNameArr.length, rowspan: 1 });
	}
}
//项目成本刷新
function costCalculate(){
	$.ajax({
		url: getRootPath() + '/salary/calculate',
		type: 'post',
		async: false,//是否异步，true为异步
		data : {
			'proNo':projNo,
			'selectDate':$("#monthListPro option:selected").val(),
			//    'selectDate':$("#dateList option:selected").attr("label"),
		},
		success: function (data) {
			if(data.code == 'success'){
				toastr.success('项目成本计算完成！');
			}else{
				toastr.success('项目成本计算异常！');
			}
		}
	});
}

/*项目评估*/
function initReview() {
	var dateSection = window.document.getElementById("dateSection");
	nowDate = dateSection[0].value;
	selectedDate = dateSection.value;
	if(selectedDate=="时间配置不在周期内"){
		return false;
	}else{
		$.ajax({
			type : "post",
			url : getRootPath()+ '/projectReview/getProjectReviewDetail',
			data : {
				proNo : projNo,
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
			proNo : projNo
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
			proNo : projNo,
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
							proNo : projNo,
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
	//$("#statusColor").css("background-color", data.lamp);
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

function addDateSectionDiv(){
	$.ajax({
		type : "post",
// 			url : getRootPath() + '/projectReview/queryProjectCycle',
		url : getRootPath() + '/measureComment/getTimeQ',
		async: false,//是否异步，true为异步
		data : {
			proNo : projNo,
			num:6,
			flag:true
		},
		success : function(data) {
			if (data.code = "200") {
				data= data.data;
				if(data.length > 0){
					var nowDate = data[0];
					var option = "<span style='font-size: 16px;margin-left: 5px'>周期：</span>";
					option += "<select id='dateSection'  style='font-size: 16px; width: 140px; height: 35px; padding-left: 10px; padding-right: 10px; border-radius: 4px; outline: none;' onchange='changeDateSection()'>"
						+"<option selected ='selected' value='" + nowDate + "'>" + nowDate + "</option>";
					for (var i = 1; i < data.length; i++) {
						option += "<option value='" + data[i] + "'>" + data[i]
							+ "</option>";
					}
					option += "</select>"
					$("#dateSectionDiv").append(option);
					$("#dateSectionDiv").css('display','block');
				} else{
					$("#dateSectionDiv").append("<select id='dateSection'  style='font-size: 8px; width: 140px; height: 30px; padding-left: 10px; padding-right: 10px; border-radius: 4px; outline: none;' onchange='changeDateSection()'>"
						+"<option selected ='selected' value='" + '时间配置不在周期内' + "'>" + '时间配置不在周期内'+ "</option>");
					$("#dateSectionDiv").css('display','block');
				}
			}
		}
	});

}

function changeDateSection(){
	initReview();
}
/*项目评估结束*/

