var projNo=window.parent.projNo;
var qmsId="";

$(function(){
	
	function getQueryString(name){
		var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r!=null){
			return decodeURIComponent(r[2]); 
		}
		return '';
	}
	
	function queryQMS(qid){
		var ret ={
			no:projNo,
			id:qid,
	    }
		$.ajax({
            type: "post",
            url: getRootPath()+"/qmsMaturity/queryQMS",
            async: false,//是否异步，true为异步
            dataType: "json",
			contentType : 'application/json;charset=utf-8', //设置请求头信息
			data: JSON.stringify(ret),
            success: function (data) {
            	qmsId=data.data.id;
            	qmsProblemType(data.data.source);
            	$("#content").text(data.data.content==null?"暂无":data.data.content);
            	$("#reference").text(data.data.reference==null?"暂无":data.data.reference);
            	$("#interviewee").text(data.data.interviewee==null?"暂无":data.data.interviewee);
            	$("#evidence").val(data.data.evidence==null?"":data.data.evidence);
            	$("#majorProblem").val(data.data.majorProblem==null?"":data.data.majorProblem);
            	$("#improvementMeasure").val(data.data.improvementMeasure==null?"":data.data.improvementMeasure);
            	$("#planClosedTime").val(data.data.planClosedTime==null?"":changeDateFormat(data.data.planClosedTime));
            	$("#actualClosedTime").text(data.data.actualClosedTime==null?"暂无":changeDateFormat(data.data.actualClosedTime));
            	$("#dutyPerson").val(data.data.dutyPerson==null?"":data.data.dutyPerson);
            	$("#followPerson").val(data.data.followPerson==null?"":data.data.followPerson);
            	$("#progress").val(data.data.progress==null?"":data.data.progress);
            	$("#involve").val(data.data.involve==null?"null":data.data.involve);
            	$("#score").val(data.data.score==null?"null":data.data.score);
            	$("#problemType").val(data.data.problemType==null?"null":data.data.problemType);
            	$("#state").val(data.data.state==null?"null":data.data.state);
            }
        });
	}
	
	/*$(document).on("click",".wrap>div",function(){
		var tis=$(this);
		$(".wrap>div").attr("class","todo");
		tis.attr("class","finished");
		if(tis.text()=="初评"){
			$("#initialScore").css("display","inline-block");
			$("#initialScore").prev().css("display","");
			$("#initialEvidence").css("display","block");
			$("#initialEvidence").prev().css("display","");
			$("#initialMajorProblem").css("display","block");
			$("#initialMajorProblem").prev().css("display","");
			$("#finalScore").css("display","none");
			$("#finalScore").prev().css("display","none");
			$("#finalEvidence").css("display","none");
			$("#finalEvidence").prev().css("display","none");
			$("#finalMajorProblem").css("display","none");
			$("#finalMajorProblem").prev().css("display","none");
		}else if(tis.text()=="终评"){
			$("#initialScore").css("display","none");
			$("#initialScore").prev().css("display","none");
			$("#initialEvidence").css("display","none");
			$("#initialEvidence").prev().css("display","none");
			$("#initialMajorProblem").css("display","none");
			$("#initialMajorProblem").prev().css("display","none");
			$("#finalScore").css("display","inline-block");
			$("#finalScore").prev().css("display","");
			$("#finalEvidence").css("display","block");
			$("#finalEvidence").prev().css("display","");
			$("#finalMajorProblem").css("display","block");
			$("#finalMajorProblem").prev().css("display","");
		}
	});*/
	
	$(document).on("change","#state",function(){
		if($("#state").val()=="Closed"){
			$("#actualClosedTime").text(new Date().format("yyyy-MM-dd"));
		}else if($("#state").val()=="Open"){
			$("#actualClosedTime").text("暂无");
		}
	});
	
	$(document).on("click","#saveQMS",function(){
		var ret ={
			no:projNo,
			qmsId:qmsId,
			involve:$("#involve").val()=="null"?null:$("#involve").val(),
			score:$("#score").val()=="null"?null:$("#score").val(),
			problemType:$("#problemType").val()=="null"?null:$("#problemType").val(),
			state:$("#state").val()=="null"?null:$("#state").val(),
			evidence:$("#evidence").val().trim()==""?null:$("#evidence").val().trim(),
			majorProblem:$("#majorProblem").val().trim()==""?null:$("#majorProblem").val().trim(),
			improvementMeasure:$("#improvementMeasure").val().trim()==""?null:$("#improvementMeasure").val().trim(),
			planClosedTime:$("#planClosedTime").val().trim()==""?null:$("#planClosedTime").val().trim(),
			dutyPerson:$("#dutyPerson").val().trim()==""?null:$("#dutyPerson").val().trim(),
			followPerson:$("#followPerson").val().trim()==""?null:$("#followPerson").val().trim(),
			progress:$("#progress").val().trim()==""?null:$("#progress").val().trim(),
	    }
		$.ajax({
            type: "post",
            url: getRootPath()+"/qmsMaturity/replaceQMSresult",
            async: false,//是否异步，true为异步
            dataType: "json",
			contentType : 'application/json;charset=utf-8', //设置请求头信息
			data: JSON.stringify(ret),
            success: function (data) {
            	if (data.code == "200") {
                	toastr.success('修改成功！');
                	sumScore();
                }else{
                	toastr.error('修改失败！');
                }
            }
        });
	});
	
	function sumScore(){
		$.ajax({
			url: getRootPath()+"/qmsMaturity/sumConform",
	        type: 'POST',
	        async: false,//是否异步，true为异步
	        data: {
	        	'no' : proNo,
	        	'source' : "",
	        },
            success: function (data) {
            	$("#score", parent.document).text(data.data.conform);
            }
        });
	}
	
	$(document).on("click","#savePrve",function(){
		$("#saveQMS").click();
		$.ajax({
			url: getRootPath()+"/qmsMaturity/qmsRanges",
	        type: 'POST',
	        async: false,//是否异步，true为异步
	        data: {
	        	'qmsId' : qmsId,
	        	'type' : "prve"
	        },
            success: function (data) {
            	if (data.code == "1") {
                	toastr.error('已经是第一条！');
                }else if(data.code == "10"){
                	toastr.error('已经是最后一条！');
                }else{
                	queryQMS(qmsId-1);
                }
            }
        });
	});
	
	$(document).on("click","#saveNext",function(){
		$("#saveQMS").click();
		$.ajax({
			url: getRootPath()+"/qmsMaturity/qmsRanges",
	        type: 'POST',
	        async: false,//是否异步，true为异步
	        data: {
	        	'qmsId' : qmsId,
	        	'type' : "next"
	        },
            success: function (data) {
            	if (data.code == "1") {
                	toastr.error('已经是第一条！');
                }else if(data.code == "10"){
                	toastr.error('已经是最后一条！');
                }else{
                	queryQMS(qmsId+1);
                }
            }
        });
	});
	
	function qmsProblemType(source){
		$("#problemType").find("option:not(:first)").remove();
		$.ajax({
			url: getRootPath()+"/qmsMaturity/qmsProblemType",
	        type: 'POST',
	        async: false,//是否异步，true为异步
	        data: {
	        	'source' : source,
	        },
            success: function (data) {
            	var options="";
            	for(var i=0;i<data.data.length;i++){
            		options+="<option value="+data.data[i].pid+">"+data.data[i].question+"</option>";
            	}
            	$("#problemType").append(options);
            }
        });
	}
	
	$(document).ready(function(){
		$("#planClosedTime").datepicker({
   	      changeMonth: true,
   	      changeYear: true,
   	      dateFormat: 'yy-mm-dd',
   	      todayButton: true,           
   	      prevButton: true,            
   	      nextButton: true
		});
		queryQMS(getQueryString("qmsId"));
	})
	
	//转换日期格式(时间戳转换为datetime格式)
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
        	return "";
        }
    }
	
})