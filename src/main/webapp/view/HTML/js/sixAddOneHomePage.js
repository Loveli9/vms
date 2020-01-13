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

	
	$(document).ready(function(){
		//初始化加载个人构建指标
		 divClick(1);
   	 load();
   	//设置年份的选择
	     var myDate = new Date();
	     var startYear = myDate.getFullYear();
	     var obj = document.getElementById('year')
	     for (var i = startYear; i >= startYear-5; i--) {
	         obj.options.add(new Option(i, i));
	     }	 
	     $('#year').selectpicker("val",[startYear]);
		$('#userName').text(getCookie('username'));

		selectOnchange();
	})
})();



function selectOnchange(){
	loadEcharts(ul_li_value);
	$.ajax({
		url: getRootPath() + '/icp-ci/queryIndex',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			'year': $("#year").selectpicker("val"),
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
        	'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
			'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
			'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
			'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join()//转换为字符串
		},
		success: function (result) {
			if(result){
				//个人构建时长
				 var ele= document .getElementById ("grgjsc");
				 if(!result.personBuild){
					 ele.innerHTML = "0</br>min";
				 }else{
					 ele.innerHTML = result.personBuild+"</br>min";
				 }
	        	 var grsc = Number(result.personBuild);
	        	 var qp1 = document.getElementById('qp1');
	        	 var gd = ((grsc/200))*312;
	        	 if(gd>312){
	        		gd = 312;
	        	 } 
		         qp1.style.bottom= gd+"px";
		         
				//版本构建时长
				 var ele= document .getElementById ("bbgjsc");
				 if(!result.versionBuild){
					 ele.innerHTML = "0</br>min";
				 }else{
					 ele.innerHTML = result.versionBuild+"</br>min";
				 }
	        	 var bbsc = Number(result.versionBuild);
	        	 var qp2 = document.getElementById('qp2');
	        	 var gd = (bbsc/200)*312;
				 if(gd>312){
	        		gd = 312;
	        	 } 
	        	qp2.style.bottom= gd+"px";
	        	
	        	//回归验证周期
				 var ele= document .getElementById ("hgyzzq");
				 if(!result.rebackTime){
					 ele.innerHTML = "0</br>min";
				 }else{
					 ele.innerHTML = result.rebackTime+"</br>min";
				 }
	        	
	        	 var hgzq = Number(result.rebackTime);
	        	 var qp3 = document.getElementById('qp3');
	        	 var gd = (hgzq/200)*312;
				 if(gd>312){
	        		gd = 312;
	        	 } 
	        	qp3.style.bottom= gd+"px";
	        	
	        	//全量功能验证周期
				 var ele= document .getElementById ("qlgnzq");
				 if(!result.quanliangTime){
					 ele.innerHTML = "0</br>min";
				 }else{
					 ele.innerHTML = result.quanliangTime+"</br>min";
				 }
	        	 
	        	 var qlzq = Number(result.quanliangTime);
	        	 var qp4 = document.getElementById('qp4');
	        	 var gd = (qlzq/200)*312;
				 if(gd>312){
	        		gd = 312;
	        	 } 
	        	qp4.style.bottom= gd+"px";
	        	
	        	//解决方案验证周期
				 var ele= document .getElementById ("jjfazq");
				 if(!result.solveTime){
					 ele.innerHTML = "0</br>min";
				 }else{
					 ele.innerHTML = result.solveTime+"</br>min";
				 }
	        	 
	        	 var jjzq = Number(result.solveTime);
	        	 var qp5 = document.getElementById('qp5');
	        	 var gd = (jjzq/200)*312;
				 if(gd>312){
	        		gd = 312;
	        	 } 
	        	qp5.style.bottom= gd+"px";
	        	
	        	
	        	
	        	//特性CycleTime
				 var ele= document .getElementById ("txzq");
				 if(!result.CycleTime){
					 ele.innerHTML = "0</br>min";
				 }else{
					 ele.innerHTML = result.CycleTime+"</br>min";
				 }
	        	 
	        	 var txzq = Number(result.CycleTime);
	        	 var qp6 = document.getElementById('qp6');
	        	 var gd = (txzq/200)*312;
				 if(gd>312){
	        		gd = 312;
	        	 } 
	        	qp6.style.bottom= gd+"px";
	        	
	        	//story/功能点/特性
	        	var ele= document .getElementById ("sgtzq");
	        	 if(!result.story){
					 ele.innerHTML = "0</br>min";
				 }else{
					 ele.innerHTML = result.story+"</br>min";
				 }
	        	 
	        	 var sgtzq = Number(result.story);
	        	 var qp7 = document.getElementById('qp7');
	        	 var gd = (sgtzq/200)*312;
				 if(gd>312){
	        		gd = 312;
	        	 } 
	        	qp7.style.bottom= gd+"px";
			}
			
		}
	});
};
