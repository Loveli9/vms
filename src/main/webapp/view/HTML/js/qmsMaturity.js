var projNo="";
$(function(){
	projNo = window.parent.projNo;
	
	function developmentProcess(){
		$(".wrap>div").click(function(){
			var tis=$(this);
			$(".wrap>div").attr("class","todo");
			tis.attr("class","finished");
            $("#myiframe").attr("src",tis.attr('myiframeName')+"?qmsName="+tis.text());
		})
	}
	
	function qmsTypes(){
		$.ajax({
			url: getRootPath()+"/qmsMaturity/qmsTypes",
	        type: 'POST',
	        async: false,//是否异步，true为异步
	        data: {
	        	
	        },
            success: function (data) {
            	$("#myiframe").attr("src","qmsProjectManagement.html?qmsName="+data.data[0]);
            	for(var i=0;i<data.data.length;i++){
            		if(i==0){
            			var div="<div class='wrap'>" +
            						"<div class='finished' myiframeName='qmsProjectManagement.html'>" +
            							data.data[i];
									"</div>" +
								"</div>";
            			$(".mvp-main .sui-steps").append(div);
            		}else{
            			if(data.data[i]=="问题跟踪"){
            				var div="<div style='display: inline-block;width: 1px;'></div>" +
        					"<div class='wrap'>" +
								"<div class='todo' style='background-color: #d35657;' myiframeName='qmsProjectManagement.html'>" +
									data.data[i];
								"</div>" +
							"</div>";
            			}else{
            				var div="<div style='display: inline-block;width: 1px;'></div>" +
        					"<div class='wrap'>" +
								"<div class='todo' myiframeName='qmsProjectManagement.html'>" +
									data.data[i];
								"</div>" +
							"</div>";
            			}
            			$(".mvp-main .sui-steps").append(div);
            		}
            	}
            }
        });
	}
	
	function changeFrameHeight() {
        var ifm = document.getElementById("myiframe");
        ifm.height = document.documentElement.clientHeight-38;
    };
	
	$(document).ready(function(){
		qmsTypes();
		changeFrameHeight();
		sumScore();
		developmentProcess();
	})
	
})
function sumScore(){
		$.ajax({
			url: getRootPath()+"/qmsMaturity/sumConform",
	        type: 'POST',
	        async: false,//是否异步，true为异步
	        data: {
	        	'no' : projNo,
	        	'source' : "",
	        },
            success: function (data) {
            	$("#score").text(data.data.conform);
            }
        });
	}