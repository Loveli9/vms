var organ={
	area:"",
	hwpdu:"",
	hwzpdu:"",
	pduSpdt:"",
	bu:"",
	pdu:"",
	du:""
};
(function(){
	
	var titleUnit = {};
	var data=null;

	function getCookie(name){
	   var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	   if(arr != null){
	     return decodeURIComponent(arr[2]); 
	   }else{
	     return null;
	   }
	}
	
	
	
	/**
	 * 展示各部门关键角色胜任度
	 */
	function scheduleShow(stepId){
		if(null==stepId||""==stepId){
			stepId=7;
		}
		$.ajax({
			url: getRootPath() + '/projectSchedule/warning',
			type: 'get',
			async: false,//是否异步，true为异步
			data:{
				'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
	        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
				'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
				'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
				'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
    			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
    			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
                'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
				"step":stepId
			},
			success: function (data) {
				var dataMsg=data;
	            var html='';
	            var nosAll="";
				if(0==dataMsg.status){
					if(dataMsg.data!=null){
						if(dataMsg.data.detail!=null&&dataMsg.data.detail.length>0){
							var detail=dataMsg.data.detail;
							
							var length=detail.length;
							var yu=length%5;//取余数
							var yiji=Math.ceil(length/3);//向上取整
							for (var i = 1; i <=yiji; i++) {
								html+="<li class='carousel-item'>"
								for(var j=1;j<=5;j++){
									var INDEX=j+(i-1)*5-1;
									if(INDEX<length){
										var nos=""
										_.each(detail[INDEX].nos, function(val, index){//(值，下标)
											if(index==(detail[INDEX].num-1)){
												nos += val;
											}else{
												nos += val+",";
											}
										})
										nosAll += nos+",";
										if(3==j){
											html+='<div id="myPro'+INDEX+'" onclick="measureTableShow(\''+nos+'\')"><p>'+detail[INDEX].pdu+'</p><span class="active">'+detail[INDEX].num+'</span></div>';
										}else{
											html+='<div id="myPro'+INDEX+'" onclick="measureTableShow(\''+nos+'\')"><p>'+detail[INDEX].pdu+'</p><span>'+detail[INDEX].num+'</span></div>';
										}
																	
									}
								}
								html+="</li>";
							}
							
						}
						$('.key-role').html('<span style="font-size:24px;color:#F45150;font-weight:bold;">'+dataMsg.data.sum+'</span>&nbsp;&nbsp;&nbsp;个即将验收项目');
					}
				}else{
	            	$('.key-role').html('<span style="font-size:24px;color:#F45150;font-weight:bold;">0</span>&nbsp;&nbsp;&nbsp;个即将验收项目');
	            }
				if(nosAll.length>1){
					nosAll = nosAll.substring(0, nosAll.lastIndexOf(','));  
				}
				$('.key-role').attr("onclick","measureTableShow('"+nosAll+"')");
	            $(".carousel-inner").html(html);
				$(".carousel-indicators").removeClass("carousel-indicators");
				$(".carousel-btn carousel-prev-btn").removeClass("carousel-btn carousel-prev-btn");
				$(".carousel-btn carousel-next-btn").removeClass("carousel-btn carousel-next-btn");
				$("#carousel_1").FtCarousel({
					index: 0,
					auto: false
				});	
			}
		});
	}	
	
	
	/**
	 * 切换
	 */
	function changeTab(){
		$('.scheduleTab').on('click','a',function(e){
			e.preventDefault();
			var stepId=$(this).attr('data-id');
			$(this).addClass('active').siblings().removeClass('active');
			scheduleShow(stepId);
		});
	}
	
	
	$(document).ready(function(){
		$('#usertype2').selectpicker("val",["在行"]);

		scheduleShow();
		changeTab();
		$('.modal-backdrop').addClass("disabled");
	})
})()

function selectOnchange(){
	scheduleShow1();
};

/**
 * 展示各部门关键角色胜任度
 */
function scheduleShow1(){
    var stepId=$('.scheduleTab .active').attr('data-id');
    $.ajax({
        url: getRootPath() + '/projectSchedule/warning',
        type: 'get',
        async: false,//是否异步，true为异步
        data:{
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
            "step":stepId
        },
        success: function (data) {
        	var dataMsg=data;
            var html='';
            var nosAll="";
			if(0==dataMsg.status){
				if(dataMsg.data!=null){
					if(dataMsg.data.detail!=null&&dataMsg.data.detail.length>0){
						var detail=dataMsg.data.detail;
						
						var length=detail.length;
						var yu=length%5;//取余数
						var yiji=Math.ceil(length/3);//向上取整
						for (var i = 1; i <=yiji; i++) {
							html+="<li class='carousel-item'>"
							for(var j=1;j<=5;j++){
								var INDEX=j+(i-1)*5-1;
								if(INDEX<length){
									var nos=""
									_.each(detail[INDEX].nos, function(val, index){//(值，下标)
										if(index==(detail[INDEX].num-1)){
											nos += val;
										}else{
											nos += val+",";
										}
									})
									nosAll += nos+",";
									if(3==j){
										html+='<div id="myPro'+INDEX+'" onclick="measureTableShow(\''+nos+'\')"><p>'+detail[INDEX].pdu+'</p><span class="active">'+detail[INDEX].num+'</span></div>';
									}else{
										html+='<div id="myPro'+INDEX+'" onclick="measureTableShow(\''+nos+'\')"><p>'+detail[INDEX].pdu+'</p><span>'+detail[INDEX].num+'</span></div>';
									}
																
								}
							}
							html+="</li>";
						}
						
					}
					$('.key-role').html('<span style="font-size:24px;color:#F45150;font-weight:bold;">'+dataMsg.data.sum+'</span>&nbsp;&nbsp;&nbsp;个即将验收项目');
				}
			}else{
            	$('.key-role').html('<span style="font-size:24px;color:#F45150;font-weight:bold;">0</span>&nbsp;&nbsp;&nbsp;个即将验收项目');
            }
			if(nosAll.length>1){
				nosAll = nosAll.substring(0, nosAll.lastIndexOf(','));  
			}
			$('.key-role').attr("onclick","measureTableShow('"+nosAll+"')");
            $(".carousel-inner").html(html);
			$(".carousel-indicators").removeClass("carousel-indicators");
			$(".carousel-btn carousel-prev-btn").removeClass("carousel-btn carousel-prev-btn");
			$(".carousel-btn carousel-next-btn").removeClass("carousel-btn carousel-next-btn");
			$("#carousel_1").FtCarousel({
				index: 0,
				auto: false
			});	
        }
    });
    
}


function measureTableShow(nos){
	document.getElementById("hideDiv").style.display="none";
	document.getElementById("tabsShow").style.display="";
	//$("#scheduleShowTable").modal('show');
	$.ajax({
		url: getRootPath() + '/projectSchedule/warningList',
		type: 'post',
		async: false,//是否异步，true为异步
		data:{
			nos: nos
		},
		success: function (data) {
			$("#tablelsw tr").remove();
			if(data.data!=null&&data.data!=""&&data.data.length>0){
				var tab = "";
				_.each(data.data, function(record, key){
					tab += '<tr><td><a style="color: #721b77;" title="'+
						record.name+'" href="'+getRootPath() +'/bu/projView?projNo='+
						record.no+'&a='+Math.random()+'">'+record.name+'</a></td>'+
						'<td>'+ record.pm +'</td>'+
						'<td>'+ record.area +'</td>'+
						'<td>'+ record.hwpdu +'</td>'+
						'<td>'+ record.hwzpdu +'</td>'+
						'<td>'+ record.pduSpdt +'</td>'+
						'<td>'+ record.startDate +'</td>'+
						'<td>'+ record.endDate +'</td>'+
						'<td>'+ record.projectState +'</td></tr>';
				});
				$("#tablelsw").append(tab);
			}
		}
	});
	tablelswInfo(10,'tablelsw');
	parent.hideMenu();
}
