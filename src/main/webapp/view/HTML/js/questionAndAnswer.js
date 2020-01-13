(function(){
	var projNo = window.parent.projNo;
	var url=location.href;
	var labelId = url.substring(url.indexOf("=")+1);
	var pageSize=10;
	var countPage=1;
	var currentPage=1;
	var user=getCookie('username');
	var questionId=0;
	
	function loadGridData(username,userid){
		var url = getRootPath() + '/questionFeedback/questions';
		$.ajax({
	        url: url,
	        type: 'POST',
	        async: false,//是否异步，true为异步
	        data: {
	        	'name' : username,
	        	'userId' : userid
	        },
	        success: function(data){
	        	var titleUnit = {};
	            var options = {
	                rownumbers: true,
	                striped: true,
	                pagination : true,
	                nowrap: true,
					pagePosition : 'bottom',
		            onLoadSuccess:function(data){   
		                $('#projSummaryTable').datagrid('doCellTip',{cls:{},delay:1000, titleUnit:titleUnit, headerOverStyle:true});   
		            }  
	            };
	            columns:[[
	                {
	                    width:250, //当 fitColumns:true时，columns里的width变为改列宽度占表格总宽度大小的比例
	                },          
	            ]]
	            titleUnit = data.titleUnit;
                var wdth = Math.round(100 / (data.gridTitles.length + 2));
                var lastWd = wdth * (data.gridTitles.length + 2);
	            lastWd = 100 > lastWd ? (wdth + 100 - lastWd) : ( wdth - lastWd + 100);
	            _.each(data.gridTitles, function(val, index){
	            	val.sortable = false;
	            	val.width = wdth + '%';
	            	val.height = '52px';
	            	val.align= 'left';
	            	if(val.title == "问题描述"){
                        val.width = wdth * 2.5 + '%';
	            		val.formatter = function(val, row){
	            			return val;
	            		};
	            	}
	            	if(val.title == "问题回复"){
                        val.width = wdth * 1.5 + '%';
	            		val.formatter = function(val, row){
	            			return val;
	            		};
	            	}
	            	if(val.title == "反馈时间"){
	            		val.formatter = function(val, row){
	            			return new Date(val).format('yyyy-MM-dd hh:mm:ss');
	            		};
	            	}
	            	if(val.title == "回复时间"){
	            		val.formatter = function(val, row){
	            			return new Date(val).format('yyyy-MM-dd hh:mm:ss');
	            		};
	            	}
	            	var state=0;
	            	if(val.title == "问题状态"){
	            		val.formatter = function(val, row){
	            			state=val;
	            			return '<div style="width:20px;height:20px;border-radius:50%;background-color:'+(val=='未解决'?'#f05133':'#3ddc66')+';float:left;margin-left:20px;margin-right:5px;"></div>'+val;
	            		};
	            	}
					if(val.title == "操作"){
	            		val.formatter = function(val, row){
	            			var details='<a class="details" val='+val+'>查看</a>';
	            			var reply='<a class="reply" val='+val+'>回复</a>';
	            			if(user=='admin' && state==0){
	            				return details + '&nbsp;&nbsp;&nbsp;&nbsp;' + reply;
	    					}else{
	    						return details;
	    					}
	            		};
	            	}
	            });
	            data.gridTitles[data.gridTitles.length -1].width = lastWd  + '%';
	            options.columns = [data.gridTitles];
	            options.pageNumber = 1;
	            $('#projSummaryTable').datagrid(options).datagrid('clientPaging');
				var p = $('#projSummaryTable').datagrid('getPager');
				$(p).pagination({
					pageSize : 10,// 每页显示的记录条数，默认为10
					pageList : [10,20,30],// 可以设置每页记录条数的列表
					beforePageText : '第',// 页数文本框前显示的汉字
					afterPageText : '页    共 {pages} 页',
					displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
				});
	        	var gridDatas = {};
	        	$.extend(true, gridDatas, data.gridDatas);
	            $('#projSummaryTable').datagrid("loadData", gridDatas);
	        }
	    })
	}
	
	function changePage(page){
		if(page==1){
			currentPage=1;
		}else if(page==2){
			currentPage=currentPage-1;
		}else if(page==3){
			currentPage=currentPage+1;
		}else if(page==4){
			currentPage=countPage;
		}
		$.ajax({
			url: getRootPath() + '/questionFeedback/questions',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				'name' : user,
				'pageSize' : pageSize,
				'currentPage' : currentPage
			},
			success: function (data) {
				var tbody='';
				_.each(data.data,function(val, index){
					var div='<div style="width:20px;height:20px;border-radius:50%;background-color:'+(val.solve_state==0?'red':'green')+';float:left;margin-left:20px;margin-right:-25px;"></div>';
					var details='<a class="details" val='+val.id+'>查看</a>';
					var reply='<a class="reply" val='+val.id+'>回复</a>';
					tbody+='<tr style="height:30px;">' +
						   		'<td style="text-align:center;">' + (data.pageNumber*10-9+index) + '</td>' +
						   		'<td style="text-align:center;">' + (val.type==null?'':val.type) + '</td>' +
						   		'<td style="text-align:center;">' + (val.content==null?'':val.content) + '</td>' +
						   		'<td style="text-align:center;">' + (val.create_date==null?'':new Date(val.create_date).format('yyyy-MM-dd hh:mm:ss')) + '</td>' +
						   		'<td style="text-align:center;">' + (val.acontent==null?'':val.acontent) + '</td>' +
						   		'<td style="text-align:center;">' + (val.adate==null?'':new Date(val.adate).format('yyyy-MM-dd hh:mm:ss')) + '</td>' +
						   		'<td style="text-align:center;">' + div + (val.solve_state==null?'':val.solve_state) + '</td>';
					if(user=='admin' && val.solve_state==0){
						tbody+='<td style="text-align:center;">' + details + '&nbsp;&nbsp;&nbsp;&nbsp;' + reply + '</td>' +
						   '</tr>';
					}else{
						tbody+='<td style="text-align:center;">' + details + '</td>' +
						   '</tr>';
					}
				})
				$("#tbody").html(tbody);
				currentPage=data.pageNumber;
				countPage=data.pageCount;
				$("#currentPage").text(data.pageNumber);
				$("#countPage").text(data.pageCount==0?1:data.pageCount);
				$("#questions").text(data.totalCount);
			}
		})
	}
	
	$(document).on("click",".details",function(){
		var tis=$(this);
		$.ajax({
			url: getRootPath() + '/questionFeedback/details',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				questionId:tis.attr('val')
			},
			success: function (data) {
				var val=data.data;
				$(".memberName").val(val[0].name);
				$(".memberId").val(val[0].user_id);
				$(".memberPhone").val(val[0].phone);
				$(".memberMail").val(val[0].email);
				$(".questionType").val(val[0].type);
				$(".questionContent").val(val[0].content);
				$(".questionReply").val(val[0].acontent==null?'':val[0].acontent);
				$("#question").modal('show');
			}
		})
	});
	
	$(document).on("click",".reply",function(){
		var tis=$(this);
		questionId=tis.attr('val');
		$.ajax({
			url: getRootPath() + '/questionFeedback/details',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				questionId:questionId
			},
			success: function (data) {
				var val=data.data;
				$(".memberName").val(val[0].name);
				$(".memberId").val(val[0].user_id);
				$(".memberPhone").val(val[0].phone);
				$(".memberMail").val(val[0].email);
				$(".questionType").val(val[0].type);
				$(".questionContent").val(val[0].content);
				$(".questionReply").val('');
				$("#answer").modal('show');
			}
		})
	});
	
	function getQuestions(){
		$("#feedback").click(function(){
			changePage(1);
		})
		$("#firstPage").click(function(){
			changePage(1);
		})
		$("#previousPage").click(function(){
			changePage(2);
		})
		$("#nextPage").click(function(){
			changePage(3);
		})
		$("#lastPage").click(function(){
			changePage(4);
		})
	}
	
	function takeQuestion(){
		$("#takeQuestion").click(function(){
			$("#questionAndAnswer").modal('show');
		})
	}
	
	function saveQuestion(){
		$("#saveQuestion").click(function(){
			if($("#memberName").val()==null||$("#memberName").val().trim()==""){
				alert("姓名不能为空");
				return;
			}
			if($("#memberId").val()==null||$("#memberId").val().trim()==""){
				alert("工号不能为空");
				return;
			}
			if($("#memberPhone").val()==null||$("#memberPhone").val().trim()==""){
				alert("电话不能为空");
				return;
			}
			if($("#memberMail").val()==null||$("#memberMail").val().trim()==""){
				alert("邮箱不能为空");
				return;
			}
			if($("#questionContent").val()==null||$("#questionContent").val().trim()==""){
				alert("意见/问题不能为空");
				return;
			}
			$.ajax({
				url: getRootPath() + '/questionFeedback/questionAdd',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					'name' : $("#memberName").val(),
					'userId' : $("#memberId").val(),
					'phone' : $("#memberPhone").val(),
					'email' : $("#memberMail").val(),
					'type' : $("#questionType").val(),
					'content' : $("#questionContent").val()
				},
				success: function (data) {
					$("#questionAndAnswer").modal('hide');
					location.reload();
				}
			})
		})	
		$(".saveQuestion").click(function(){
			if($("#questionReply").val()==null||$("#questionReply").val().trim()==""){
				alert("回复不能为空");
				return;
			}
			$.ajax({
				url: getRootPath() + '/questionFeedback/answerAdd',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					'questionId' : questionId,
					'content' : $("#questionReply").val(),
					solveState : $("#solveState").val()
				},
				success: function (data) {
					$("#answer").modal('hide');
					location.reload();
				}
			})
		})	
	}
	
	function queryBtn(){
		$("#queryBtn").click(function(){
			loadGridData($("#username").val(),$("#userid").val());
		})
	}
	
	function getCookie(name){
	   var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	   if(arr != null){
	     return unescape(arr[2]); 
	   }else{
	     return null;
	   }
	}
	
  	$(document).ready(function(){
//  		if(getCookie('username')=='admin'){
//  			$("#takeQuestion").css("display","none");
//  		}
//  		changePage(1);
  		loadGridData('','');
  		getQuestions();
  		takeQuestion();
  		saveQuestion();
  		queryBtn();
	})
	
})()