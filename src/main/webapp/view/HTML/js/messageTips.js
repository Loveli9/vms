$(function() {
	$(".state .navigation").on('click',function(){
		var num = $(this).index();
		$(this).addClass("curent").siblings().removeClass("curent");
		if(num==0){
			getOnsiteNews();
		}else if(num==1){
			getReadedNews();
		}
	})
	$(document).ready(function(){
		getOnsiteNews();
	})
})
function getOnsiteNews(){
	$.ajax({
		url: getRootPath() + '/projectOverview/getOnsiteNews',
		type: 'post',
		async: false,//是否异步，true为异步
		success: function (data) {
			$('#projectNews').empty();
			var news = '';
			if(data.code == "success" && data.data.length > 0){
				var j = 0;
				for(var i=0;i<data.data.length;i++){
    				news += '<div class="messagebox"><div class="message"><span style="font-weight: 600;font-family: cursive;">'+data.data[i].projectName+'</span>  该项目中有';
    				if(data.data[i].demand != null){
    					news += '<a target="_blank" style="color: #721b77;" href="'+getRootPath()+'/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo='+data.data[i].no+'&tips=xq">'+data.data[i].demand+'个需求任务</a>';
    					j++;
    				}
    				if(data.data[i].risk != null){
    					if(j>0){news += ',';}
    					j++;
    					news += '<a target="_blank" style="color: #721b77;" href="'+getRootPath()+'/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo='+data.data[i].no+'&tips=fx">'+data.data[i].risk+'个风险任务</a>';
    				}
    				if(data.data[i].qms != null){
    					if(j>0){news += ',';}
    					news += '<a target="_blank" style="color: #721b77;" href="'+getRootPath()+'/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo='+data.data[i].no+'&tips=qms">'+data.data[i].qms+'个QMS任务</a>';
    				}
    				j = 0;
    				news += '未关闭</div><span id="read'+data.data[i].no+'" onclick="readedInformation(\''+data.data[i].no+'\')" class="read">已读</span></div>';
				}
				
			}else{
				news += '<span>无未读消息</span>';
			}
			$('#projectNews').append(news);
		}
	})
}

function getReadedNews(){
	$.ajax({
		url: getRootPath() + '/projectOverview/getReadedNews',
		type: 'post',
		async: false,//是否异步，true为异步
		success: function (data) {
			$('#projectNews').empty();
			var news = '';
			if(data.code == "success" && data.data.length > 0){
				var j = 0;
				for(var i=0;i<data.data.length;i++){
    				news += '<div class="message" style="margin-left: 2.5%;margin-top: 10px;line-height: 20px;border-right:0px;"><span>'+data.data[i].readTime+'</span></div><div class="messagebox">'+
    				'<div class="message" style="width: 97%;border-right: 0px;"><span style="font-weight: 600;font-family: cursive;">'+data.data[i].projectName+'</span>  该项目中有';
    				if(data.data[i].demand != 0){
    					news += '<a target="_blank" style="color: #721b77;" href="'+getRootPath()+'/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo='+data.data[i].no+'&tips=xq">'+data.data[i].demand+'个需求任务</a>';
    					j++;
    				}
    				if(data.data[i].risk != 0){
    					if(j>0){news += ',';}
    					j++;
    					news += '<a target="_blank" style="color: #721b77;" href="'+getRootPath()+'/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo='+data.data[i].no+'&tips=fx">'+data.data[i].risk+'个风险任务</a>';
    				}
    				if(data.data[i].qms != 0){
    					if(j>0){news += ',';}
    					news += '<a target="_blank" style="color: #721b77;" href="'+getRootPath()+'/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo='+data.data[i].no+'&tips=qms">'+data.data[i].qms+'个QMS任务</a>';
    				}
    				j = 0;
    				news += '未关闭</div></div>';
				}
			}else{
				news += '<span>无已读消息</span>';
			}
			$('#projectNews').append(news);
		}
	})
}

function readedInformation(projectId){
	$.ajax({
		url: getRootPath() + '/projectOverview/saveReadedNews',
		type: 'post',
		data : {
			proNo : projectId
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			if(data.code == "success"){
				$('#read'+projectId).removeAttr("onclick");
				$('#read'+projectId).css("color","#808080");
				window.parent.getOnsiteNews();
			}
		}
	})
}