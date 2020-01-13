var proNo = window.parent.projNo;
var notEditDate;
$(function(){	
	$.ajax({
		url:getRootPath() + '/IteManage/meberSelect',
		type:'post',
		dataType: "json",
		async:false,
		data:{
			proNo : proNo
		},
		success:function(data){
			if(data.code == 'success'){
				var select_option="";
				$("#securityLiable").empty();
				if(null==data.data||""==data.data) return;
				_.each(data.data, function(item, index){//(值，下标)
					select_option += "<option value="+item.value+">"+item.text+"</option>";
				})
				$("#securityLiable").html(select_option);
                //$('#securityLiable').selectpicker('refresh');
                //$('#securityLiable').selectpicker('render');
			}
		}
	});
	
	$.ajax({
		type : "post",
//			url : getRootPath() + '/projectReview/queryProjectCycle',
		url : getRootPath() + "/networkSecurity/getMonths",
		async: false,//是否异步，true为异步
		data : {
			//proNo : proNo,
			num:3
		},
		success : function(data) {
			if (data.code = "200") {
				if(null==data.data||""==data.data) return;
				var option = ""
				_.each(data.data, function(item, index){//(值，下标)
					console.log(index);
					option += "<option value="+item.value;
					if(index==0){
						option += " selected ='selected'";
					}
					if(index==2){
						notEditDate = item.value;
					}
					option += ">"+item.text+"</option>";
				})
				$("#dateSection").append(option);
				initPage();
			}
		}
	});	
	
	
	$('#editForm').bootstrapValidator({
       	feedbackIcons: {
       		valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            errorCleared : {
	        	validators: {
	        		notEmpty: {
	                       message: 'error级当日清零不能为空'
	                },
	        		regexp: {
	        			regexp: /^[1-9]\d*|0$/,
	                    message: '请输入正确的整数'
	                }
	            }
	        },
	        warningCleared : {
	        	validators: {
	        		notEmpty: {
	                       message: 'warning级当日清零不能为空'
	                },
	        		regexp: {
	        			regexp: /^[1-9]\d*|0$/,
	                    message: '请输入正确的整数'
	                }
	            }
	        },
            satisfyRate : {
	        	validators: {
	        		notEmpty: {
	                       message: '工具测试覆盖满足度不能为空'
	                },
	        		regexp: {
	        			regexp: /(^[0-9]{1,2}$)|100|(^[0-9]{1,2}[\.]{1}[0-9]{1,2}$)/,
	                    message: '请输入正确的完成度百分比'
	                }
	            }
	        },
	        caseRate : {
	        	validators: {
	        		regexp: {
	        			regexp: /(^[0-9]{1,2}$)|100|(^[0-9]{1,2}[\.]{1}[0-9]{1,2}$)/,
	                    message: '请输入正确的完成度百分比'
	                }
	            }
	        }
        }   
       });
	
	
	$(document).on("click", "#save_btn", function () {
		var data = $('#editForm').serializeJSON();
		if(data["securityStatus"]=="2"){
			if($("#securityLiable").val()){
				data["securityLiable"]=$("#securityLiable").val();
			}else{
				toastr.warning('请选择网络专项责任人！！！');
				return;
			}
		}
		if(data["dangerStatus"]=="2"){
			if(!$("#dangerReport").val()){
				toastr.warning('请输入网络安全风险上报内容！！！');
				return;
			}
		}	
		if(data["caseStatus"]=="2"){
			if(!$("#caseRate").val()){
				toastr.warning('请输入用例测试满足度（自动化）值！！！');
				return;
			}
		}
		data["surveyDate"]=$("#dateSection").val();
		data["proNo"]=proNo;
		$('#editForm').bootstrapValidator('validate');
        //如果表单验证正确，则请求后台添加用户
        if($("#editForm").data('bootstrapValidator').isValid()){
        	$.ajax({
 				url:getRootPath() + "/networkSecurity/saveNetworkSecurity",
 				type : 'post',
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data:JSON.stringify(data),
 				success:function(data){
 					//后台返回添加成功
 					if(data.code=='success'){
 						toastr.success('保存成功！');
 					}
 					else{
 						toastr.error('保存失败!');
 					}
 				}
 			});
        }
		
	});

	radioChange();
})

function radioChange(){
	$("input[type=radio][name='securityStatus']").change(function() {
		formatForm("securityLiable", this.value);
	});
	
	$("input[type=radio][name='dangerStatus']").change(function() {
		formatForm("dangerReport", this.value);
	});
	
	$("input[type=radio][name='caseStatus']").change(function() {
		formatForm("caseRate", this.value);
	});
}

function initPage(){
	$.ajax({
        type: "post",
        url: getRootPath()+"/networkSecurity/getNetworkSecurityByKey",
        async: false,//是否异步，true为异步
		data : {
			proNo : proNo,
			date  : $("#dateSection").val()
		},
        success: function (data, status) {
            if (data.code == "success") {
            	var data = data.data;
            	$(".text").empty();
            	$("input[type=radio][value='1']").prop("checked", true);
            	if(notEditDate==$("#dateSection").val()){
					$("input").attr("disabled","disabled"); 
					$("#securityLiable").attr("disabled","disabled");
					$("#dangerReport").attr("disabled","disabled");
					$("#save_btn").attr("disabled","disabled");
				} else{
					$("input").removeAttr("disabled");  
					$("#securityLiable").removeAttr("disabled");  
					$("#dangerReport").removeAttr("disabled");  
					$("#save_btn").removeAttr("disabled");  
				}
            	$("#name").append(data.name);
				$("#name").attr("title",data.name);
				$("#bu").append(data.bu);
				$("#pdu").append(data.pdu);
				$("#du").append(data.du);
				$("#hwpdu").append(data.hwpdu);
				$("#hwzpdu").append(data.hwzpdu);
				$("#pduSpdt").append(data.pduSpdt);
				$("#qa").append(data.qa);
				$("#pm").append(data.pm);
				$("#type").append(data.type);
				$("#projectType").append(data.projectType);
				$("input[name='securityStatus'][value=" + data.securityStatus + "]").prop("checked", true);
				$("#securityLiable").selectpicker("val",data.securityLiable);
				formatForm("securityLiable", data.securityStatus);
				
				$("input[name='deliverStatus'][value=" + data.deliverStatus + "]").prop("checked", true);
				$("input[name='ukStatus'][value=" + data.ukStatus + "]").prop("checked", true);
				$("input[name='sowStatus'][value=" + data.sowStatus + "]").prop("checked", true);
				
				$("#dangerReport").val(data.dangerReport);
				$("#errorCleared").val(data.errorCleared);
				$("#warningCleared").val(data.warningCleared);
				$("#satisfyRate").val(data.satisfyRate);
				$("#caseRate").val(data.caseRate);
				$("input[name='caseStatus'][value=" + data.caseStatus + "]").prop("checked", true);
				formatForm("caseRate", data.caseStatus);
				$("input[name='dangerStatus'][value=" + data.dangerStatus + "]").prop("checked", true);
				formatForm("dangerReport", data.dangerStatus);
				
            }
        }
    });
}

function formatForm(id, value){
	if(notEditDate==$("#dateSection").val()){
		return;
	}
	if(value=="2"){
		$("#"+id).removeAttr("disabled");
	} else {
		if(id=="securityLiable"){
			$("#"+id).selectpicker("val","");
		}else{
			$("#"+id).val("");
		}
		$("#"+id).attr("disabled","disabled");
	}
}
function changeDateSection(){
	initPage();
}