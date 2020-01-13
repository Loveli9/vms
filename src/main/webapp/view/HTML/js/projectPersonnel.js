var proNo = getQueryString("projNo");
var kaifaCount = "";
var keyroleCount= "";
var renlinum  = "";
var guanjiannum ="";
$(function(){
	initRenliData();
	getProcessDtata();
})

function initRenliData(){
	$.ajax({
		url : getRootPath()+ '/user/getPersonnel',
		async: false,
		data : {
			proNo : proNo,
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

function formatResult(val,flag) {
	if (val < 0 || val == "" || val == null) {
		return 0;
	}else if (flag && val > 100) {
		return 100;
	}else {
		return val;
	}
}

function getProcessDtata() {
	var width1;
	var width2;
	$.ajax({
		url : getRootPath() + "/manpowerBudget/getManpowerBudgetByNo",
		type : 'POST',
		async: false,//是否异步，true为异步
		data : {
			proNo : proNo,
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
