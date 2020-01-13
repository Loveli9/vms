var projNo;
(function() {
	projNo = window.parent.parent.projNo;

	function getMonthlyManpower(){	
		$.ajax({
			url: getRootPath() + '/testMeasures/getMonthlyManpowerList',
			type: 'post',
			async: false,
			data: {
				projNo: projNo,
				actualFlag: $('#monthly-manpower-count').val()
			},
			success: function (data) {
				var head = data.head;
				var body = data.body;
				$('#monthlyManpower tr').html('');
				var headTab="<td style='line-height: 30px;width: 7%;'>姓名</td>";
				headTab+="<td style='line-height: 30px;width: 7%;'>工号</td>";
				headTab+="<td style='line-height: 30px;width: 8%;'>岗位</td>";
				for (var i = 0; i < 12; i++) {
					headTab+="<td style='line-height: 30px;width: 6%;'>"+head[i]+"</td>";	
				}
				headTab+="<td style='line-height: 30px;width: 5%;'>合计</td>";

				$("#monthlyManpower thead tr").append(headTab);
				
				if(body!=null&&body!=""){
					var addNums = 0;
					var bodyTab = "";					
					if(!_.isEmpty(body)){
						var chartsData ={
								first:0.0,
								second:0.0,
								third:0.0,
								fourth:0.0,
								fifth:0.0,
								sixth:0.0,
								seventh:0.0,
								eighth:0.0,
								ninth:0.0,
								tenth:0.0,
								eleventh:0.0,
								twelfth:0.0,
								sum:0.0
							};
						_.each(body, function(record, index){
							bodyTab += '<tr><td>'+ record.name +'</td>'
								   +'<td>'+ record.number +'</td>'
								   +'<td>'+ record.role +'</td>'
								   +'<td>'+ (record.first==0.0?"":record.first) +'</td>'
								   +'<td>'+ (record.second==0.0?"":record.second) +'</td>'
								   +'<td>'+ (record.third==0.0?"":record.third) +'</td>'
								   +'<td>'+ (record.fourth==0.0?"":record.fourth) +'</td>'
								   +'<td>'+ (record.fifth==0.0?"":record.fifth) +'</td>'
								   +'<td>'+ (record.sixth==0.0?"":record.sixth) +'</td>'
								   +'<td>'+ (record.seventh==0.0?"":record.seventh) +'</td>'
								   +'<td>'+ (record.eighth==0.0?"":record.eighth) +'</td>'
								   +'<td>'+ (record.ninth==0.0?"":record.ninth) +'</td>'
								   +'<td>'+ (record.tenth==0.0?"":record.tenth) +'</td>'
								   +'<td>'+ (record.eleventh==0.0?"":record.eleventh) +'</td>'
								   +'<td>'+ (record.twelfth==0.0?"":record.twelfth) +'</td>'
								   +'<td>'+ record.sum +'</td></tr>';
						chartsData.first+=record.first;
						chartsData.second+=record.second;
						chartsData.third+=record.third;
						chartsData.fourth+=record.fourth;
						chartsData.fifth+=record.fifth;
						chartsData.sixth+=record.sixth;
						chartsData.seventh+=record.seventh;
						chartsData.eighth+=record.eighth;
						chartsData.ninth+=record.ninth;
						chartsData.tenth+=record.tenth;
						chartsData.eleventh+=record.eleventh;
						chartsData.twelfth+=record.twelfth;
						chartsData.sum+=record.sum;
					})
					}
					$("#monthlyManpower tbody").append(bodyTab);
					var footTab = '<tr><td colspan ="3">合计</td>'
					   +'<td>'+ chartsData.first.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.second.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.third.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.fourth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.fifth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.sixth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.seventh.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.eighth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.ninth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.tenth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.eleventh.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.twelfth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.sum.toFixed(2)+'</td></tr>';
					$("#monthlyManpower tfoot").append(footTab);
					$("#monthlyManpower tbody").css('display','none');
				}
			}
		});
	};
		
	function getMonthlyManpowerProportion(){
		$.ajax({
			url: getRootPath() + '/bu/getMonthlyManpowerProportion',
			type: 'post',
			async: false,
			data: {
				projNo: projNo,
			},
			success: function (data) {
				var head = data.head;
				var body = data.body;
				$('#monthlyManpowerProportion tr').html('');
				var headTab="<td style='line-height: 30px;width: 22%;'>姓名</td>";
				for (var i = 0; i < 12; i++) {
					headTab+="<td style='line-height: 30px;width: 6%;'>"+head[i]+"</td>";
				}
				headTab+="<td style='line-height: 30px;width: 5%;'>操作</td>";
				$("#monthlyManpowerProportion thead tr").append(headTab);
				
				if(body!=null&&body!=""){
					var addNums = 0;
					var bodyTab = "";	
					if(!_.isEmpty(body)){
						var chartsData ={
								first:0.0,
								second:0.0,
								third:0.0,
								fourth:0.0,
								fifth:0.0,
								sixth:0.0,
								seventh:0.0,
								eighth:0.0,
								ninth:0.0,
								tenth:0.0,
								eleventh:0.0,
								twelfth:0.0
						};
						_.each(body, function(record, index){
							bodyTab+= '<tr bgcolor="#b4aeb5"><td date-type="select" select-date="开发投入,问题单修改投入,版本公共事务投入,测试执行投入,测试用例设计投入,自动化用例写作">'+ record.name +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.first==0.0?"":record.first+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.second==0.0?"":record.second+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.third==0.0?"":record.third+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.fourth==0.0?"":record.fourth+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.fifth==0.0?"":record.fifth+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.sixth==0.0?"":record.sixth+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.seventh==0.0?"":record.seventh+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.eighth==0.0?"":record.eighth+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.ninth==0.0?"":record.ninth+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.tenth==0.0?"":record.tenth+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.eleventh==0.0?"":record.eleventh+"%") +'</td>'
								   +'<td date-type="input" name="baifenbi">'+ (record.twelfth==0.0?"":record.twelfth+"%") +'</td>'
								   +'<td date-type="no"><div name="del3" style="display: none;"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td></tr>';
							chartsData.first+=record.first;
							chartsData.second+=record.second;
							chartsData.third+=record.third;
							chartsData.fourth+=record.fourth;
							chartsData.fifth+=record.fifth;
							chartsData.sixth+=record.sixth;
							chartsData.seventh+=record.seventh;
							chartsData.eighth+=record.eighth;
							chartsData.ninth+=record.ninth;
							chartsData.tenth+=record.tenth;
							chartsData.eleventh+=record.eleventh;
							chartsData.twelfth+=record.twelfth;
						});
					}
					$("#monthlyManpowerProportion tbody").append(bodyTab);
					
					var footTab = '<tr><td>合计</td>'
					   +'<td>'+ chartsData.first.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.second.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.third.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.fourth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.fifth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.sixth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.seventh.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.eighth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.ninth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.tenth.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.eleventh.toFixed(2) +'</td>'
					   +'<td>'+ chartsData.twelfth.toFixed(2) +'</td>'
					   +'<td></td></tr>';
					$("#monthlyManpowerProportion tfoot").append(footTab);	
				}
			}	
		});
	};
		
	//根据条件统计员工个人代码量、并根据代码类别筛选
//	function MonthlyManpowerchange(){
//		$("#monthly-manpower-count").change(function() {
//			getMonthlyManpower();
//		});
//	};
	
	//添加change事件
	$(document).on("change", "#monthly-manpower-count", function () {
		getMonthlyManpower();
	});
	
	
	$(document).ready(function(){
		getMonthlyManpower();
//		MonthlyManpowerchange();
		getMonthlyManpowerProportion();
	})
})()

var oldtable3;
function tableAdd3() {
	var tab = '<tr style="height: 30px;">'
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
	$('#dataAcquisition').text(msg);
	$('#submitImportmodalfooter').css('display','block');
	$('#savetoop').modal('show');
}
function hidePopover() {
	$('#savetoop').modal('hide');
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
			'"first":"'+isnull2double(rows[i].cells[1].innerHTML)+'",'+
			'"second":"'+isnull2double(rows[i].cells[2].innerHTML)+'",'+
			'"third":"'+isnull2double(rows[i].cells[3].innerHTML)+'",'+
			'"fourth":"'+isnull2double(rows[i].cells[4].innerHTML)+'",'+
			'"fifth":"'+isnull2double(rows[i].cells[5].innerHTML)+'",'+
			'"sixth":"'+isnull2double(rows[i].cells[6].innerHTML)+'",'+
			'"seventh":"'+isnull2double(rows[i].cells[7].innerHTML)+'",'+
			'"eighth":"'+isnull2double(rows[i].cells[8].innerHTML)+'",'+
			'"ninth":"'+isnull2double(rows[i].cells[9].innerHTML)+'",'+
			'"tenth":"'+isnull2double(rows[i].cells[10].innerHTML)+'",'+
			'"eleventh":"'+isnull2double(rows[i].cells[11].innerHTML)+'",'+
			'"twelfth":"'+isnull2double(rows[i].cells[12].innerHTML)+'"'+
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
	oldtable3=$("#monthlyManpowerProportion tbody").html();
	delToNone('del3','block');
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
		var txt = $("<input class='form-control' style='width: 100%;height: 30px;' type='text'>").val(text);
	}else if(dateType=="date"){
		var text = td.text();
		var txt = $("<input class='form-control' max='9999-12-30' style='width: 100%;height: 30px;' type='date'>").val(text);
	}else if(dateType=="no"){
		return;
	}else if(dateType=="select"){
		var text = td.text();
		var select = "<select class='form-control' style='height: 30px;'>";
		var selectDate = td.attr("select-date").split(',');
		selectDate.forEach(function(i,index){
			select += "<option>"+i+"</option>";
		})
		select += "</select>";
		var txt = $(select).val(text);
	}else{
		var text = td.text();
		var txt = $("<input class='form-control' style='width: 100%;height: 30px;' type='text'>").val(text);
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
	});
	td.text("");
	td.append(txt);
	$(txt).focus();
	if(dateType=="input"){
		$(txt).select();
	}
}

$(document).on("click", "#monthlyManpowerProportion tbody td", function () {
	if($("#edit3").attr("data_obj")=="1"){
		return;
	}
	var td = $(this);
	editable(td);
	
});

$(document).on("click", "div[name=del3]", function () {
	var obj= $(this).parents('tr:first');
	var name = $(this).parents('tr:first')[0].childNodes[0].innerHTML;
	var jsonStr = '{"proNo":"'+projNo+'","name":['+name+']}';
	$.ajax({
		url : getRootPath() + "/bu/deleteInvestmentProportion",
		type : 'POST',
		async: true,
		data : {'projNo':projNo,'name':name},
		success : function(data) {			
			if(data.code=="success"){
				obj.remove();
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
					showPopover("比例信息删除成功");
				}
			}	
		}
	});
	
});

function datedifference(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式  
    var dateSpan,
        tempDate,
        iDays;
    sDate1 = Date.parse(sDate1);
    sDate2 = Date.parse(sDate2);
    dateSpan = sDate2 - sDate1;
    dateSpan = Math.abs(dateSpan);
    iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
    if(sDate1>sDate2){
    	return iDays*(-1);
    }
    return iDays
};