/********************************************** 导入 *************************************/
//打开文件上传页面
function changeQuality(){
	initDateSection();
	$("#demandImport").modal('show');
	$("#filePathInfo").val('');
	$("#openLocalFile").val('');
};
//打开本地文件进行选择
function choosingPath(){
	$('#openLocalFile').click();
};
//选择文件路径
$(document).on("change", "#openLocalFile", function () {
	$("#filePathInfo").val($(this).val());
});
//上传附件前校验
function submitImportFile(){
	var filePath = $("#filePathInfo").val();
	if(filePath == ''){
		 toastr.warning('请先选择要导入的文件!');
		 return false;
	}else if(!(filePath.endsWith('.xlsx') || filePath.endsWith('.xls'))){
		toastr.warning("文件格式需为xlsx或者xls");
		return false;
	}else{
		var option = {
				url : getRootPath()+'/project/importAssessment',
				type:'post',
				dataType: 'json',
				data : {
					'createTime':$('#dateSection').val(),
					'template':$('#chengshuduSelected').val()
				},
				success : function(data) {
					if(data){
						if(Number(data.sucNum) > 0){
							toastr.success('成功导入'+data.sucNum+'条数据!');
						}else{
							toastr.error('导入失败，请检查数据格式');
						}
					}
				}
			};
		$("#importForm").ajaxSubmit(option);
	}
};

$(document).on("click", "#templateDownload1", function () {
	if($("input[name='radio1']:checked").length<=0){
		alert("请至少选中一个模板");
		return;
	}
	var downloadUrl = getRootPath() + '/project/projectTemplate?templateName='+$("input[name='radio1']:checked").attr("templateName");
	$("#templateDownload1").attr('href', downloadUrl);
});
$(document).on("click", "#submitImport1", function () {
	if($("input[name='radio1']:checked").length<=0){
		alert("请至少选中一个模板");
		return;
	}
	var filePath = $("#filePathInfo1").val();
	if(filePath == ''){
		alert("请先选择要导入的文件!");
	}else if(!(filePath.endsWith('.xlsx') || filePath.endsWith('.xls'))){
		alert("文件格式需为xlsx或者xls");
	}else{	
		var ss = $("input[name='radio1']:checked").val();
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
		$("#importForm1").ajaxSubmit(option);
	}
	
});
$(document).on("click", "#importBtn1", function () {
	$('#importInput').click();
});
$(document).on("change", "#importInput", function () {
	$("#filePathInfo1").val($(this).val());
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
			flag:true,
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
};

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
				if(page==i){
					select_option += "selected='selected'";
				} 
				select_option += ">"+data[i].key+"</option>";
			}
			$("#chengshuduSelected").html(select_option);
		    $('#chengshuduSelected').selectpicker('refresh');
		    $('#chengshuduSelected').selectpicker('render');
		}
	});
};
function datepickerCycle1(){
	$("#datepickerCycle1").empty();
	var month = $("#datepickerCycle").val();
	var array=new Array();
	array = month.split("-");
	var myDate = new Date(array[0], array[1], 0);
	var day = myDate.getDate();//当月有多少天
	myDate.setDate(1);//当月1号
	var week = myDate.getDay();
	var weeks = Math.ceil((day - 8 + week)/7);
	var startTime = new Array();
	myDate.setDate(9-week);
	datepickerTime(myDate,weeks,startTime);
	myDate = new Date(array[0], array[1], 0);//重置时间避免跨月
	myDate.setDate(15-week);
	var endTime = new Array();
	datepickerTime(myDate,weeks,endTime);
	var cycle = new Array();
	cycle.push(startTime[0]+" ~ "+endTime[0]);
	$("#datepickerCycle1").append("<option selected='selected' value='"+cycle[0]+"'>"+cycle[0]+"</option>");
	for(var allinfo = last_year_month(), i = 1; i < weeks; i++) {
		cycle.push(startTime[i]+" ~ "+endTime[i]);
		$("#datepickerCycle1").append("<option value='"+cycle[i]+"'>"+cycle[i]+"</option>");
	} 
};
var datepickerTime = function(myDate,weeks,time){//将时间填入数组
	var times = myDate.getFullYear()+"-"+(myDate.getMonth() + 1)+"-"+myDate.getDate();
	time.push(times);
	for(var i=0;i<weeks-1;i++){
		var day = myDate.getDate();
		var date = day+7;
		myDate.setDate(date);
		times = myDate.getFullYear()+"-"+(myDate.getMonth() + 1)+"-"+myDate.getDate();
		time.push(times);
	}
};
var formatDate = function(myDate){//将时间格式化
	var year = myDate.getFullYear();
	var month = myDate.getMonth() + 1;//月份是从0开始的
	var day = myDate.getDate();
	return ;
}
var last_year_month = function() {
	var d = new Date();
	var result = [];
	for(var i = 0; i < 2; i++) {
		d.setMonth(d.getMonth() - 1);
		var m = d.getMonth() + 1;
		m = m < 10 ? "0" + m : m;
		//在这里可以自定义输出的日期格式
		result.push(d.getFullYear() + "-" + m);
		//result.push(d.getFullYear() + "年" + m + '月');
	}
	return result;
}

/***************************************** 人员职级导入 *************************************/
//打开文件上传页面
function openRankModal(){
	$("#MembersRankImport").modal('show');
	$("#filePathInfoRank").val('');
	$("#openLocalFileRank").val('');
}

//打开本地文件进行选择
function rankImport(){
	$('#openLocalFileRank').click();
}

//选择文件路径
$(document).on("change", "#openLocalFileRank", function () {
	$("#filePathInfoRank").val($(this).val());
});

//从Excel中导入人员职级
function submitRank(){
	var filePath = $("#filePathInfoRank").val();
	
	if(filePath == ''){
		 toastr.warning('请先选择要导入的文件!');
		 return false;
	}else if(!(filePath.endsWith('.xlsx') || filePath.endsWith('.xls'))){
		toastr.warning("文件格式需为xlsx或者xls");
		return false;
	}else{	
		var option = {
				url : getRootPath()+'/project/importRank',
				type:'post',
				dataType: 'json',
				success : function(data) {
					if(data){
						if(data.numberList){
							var line = "";
							for(var i = 0; i < data.numberList.length; i++){
								line += data.numberList[i];
								
								if(i != data.numberList.length - 1){
									line += ", ";
								}
							}
							toastr.error('第[' +　line + ']行数据有误!');
						}
						/*if(data.chinasoftAccountSize > 10){
							toastr.error('第' +　data.rowNumber + '行中软工号长度大于10!');
						}
						if(data.account == "false"){
							toastr.error('第' +　data.rowNumber + '行中软工号不为纯数字组成!');
						}
						if(data.account == "empty"){
							toastr.error('第' +　data.rowNumber + '行中软工号为空!');
						}
						if(data.memberName == "empty"){
							toastr.error('第' +　data.rowNumber + '行姓名为空!');
						}
						if(data.memberName == "false"){
							toastr.error('第' +　data.rowNumber + '行姓名不为汉字组成!');
						}
						if(data.rank == "false"){
							toastr.warning('第' +　data.rowNumber + '行职级不存在!');
						}
						if(data.idCardLen == "fault"){
							toastr.warning('第' +　data.rowNumber + '行身份证号位数不正确!');
						}
						if(data.idCard == "false"){
							toastr.warning('第' +　data.rowNumber + '行身份证号不为数字或不为数字和英文字母!');
						}*/
						if(data.sucNum > 0){
							toastr.success('成功导入'+data.sucNum+'条数据!');
						}else{
							toastr.error('导入失败!');
						}
					}
				}
		};
		$("#importRankForm").ajaxSubmit(option);
	}
}

//人员职级-模板下载
$(document).on("click", "#templateDownloadRank", function () {
	var modelName = 'archive';
	var downloadUrl = getRootPath() + '/project/projectTemplate?templateName='+modelName;
	$("#templateDownloadRank").attr('href', downloadUrl);
});