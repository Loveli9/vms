(function(){
	var projNo = window.parent.projNo;
	var url=location.href;
	var labelId = url.substring(url.indexOf("=")+1);
	function measure(){
		$.ajax({
			url: getRootPath() + '/projectlable/changemodel',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				labId: labelId,
				proNo: projNo,
				flag: "toManual"
			},
			success: function () {
				
			}
		})
		$("#qualityTarget").children("tbody").empty();
		$.ajax({
			url: getRootPath() + '/projectlable/getLabMeasureByProject',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				labId: labelId,
				proNo: projNo,
				flag: "toManual"
			},
			success: function (data) {
				if(data.data==null||data.data==""){
					$("#qualityTargetTable").css("display","none");
				}else{
					var tbody="<tbody>";
					_.each(data.data, function(tab, index){
						if(index%2==0){
							tbody+="<tr style='background-color:#ffffff;height: 52px;line-height: 52px;'>";
						}else{
							tbody+="<tr style='background-color:#f7f8f8;height: 52px;line-height: 52px;'>";
						}
						tbody+="<td style='text-align:left;padding-left:20px;'>" +tab.name+ "</td>" +
						       "<td>" +tab.UNIT+ "</td>" +
						       "<td>" +tab.UPPER+ "</td>" +
						       "<td>" +tab.LOWER+ "</td>" +
						       "<td>" +tab.TARGET+ "</td>";
						var flag=true;
						if($.trim(tab.UNIT)=="%"){
							var up=parseFloat(tab.UPPER.substring(0,tab.UPPER.lastIndexOf("%")));
							var low=parseFloat(tab.LOWER.substring(0,tab.LOWER.lastIndexOf("%")));
							if(tab.ACTUALVAL!=null&&tab.ACTUALVAL!=""){
								if(parseFloat(tab.ACTUALVAL*100)>up||parseFloat(tab.ACTUALVAL*100)<low){
									flag=false;
								}
								tab.ACTUALVAL=(tab.ACTUALVAL*100).toFixed(1)+"%";
							}
						}else{
							var up=parseFloat(tab.UPPER);
							var low=parseFloat(tab.LOWER);
							if(tab.ACTUALVAL!=null&&tab.ACTUALVAL!=""){
								if(parseFloat(tab.ACTUALVAL)>up||parseFloat(tab.ACTUALVAL)<low){
									flag=false;
								}
							}
						}       
						if(tab.COLLECTYPE=="手动输入"){
							if(flag==true){
								tbody+="<td class='td' style='padding:0px;'><input class='input' type='hidden' style='width: 100%;height: 30px;border-radius: 5px;border: 0px;' id= " +tab.ID+ " /><div>" +tab.ACTUALVAL+ "</div></td>";
							}else{
								tbody+="<td class='td' style='padding:0px;'><input class='input' type='hidden' style='width: 100%;height: 30px;border-radius: 5px;border: 0px;' id= " +tab.ID+ " /><div style='color: red;'>" +tab.ACTUALVAL+ "</div></td>";
							}
						}else{
							if(flag==true){
								tbody+="<td>" +tab.ACTUALVAL+ "</td>";
							}else{
								tbody+="<td style='color: red;'>" +tab.ACTUALVAL+ "</td>";
							}
						}
						tbody+="<td>" +tab.COLLECTYPE+ "</td>" +
							   "<td style='text-align:left;padding-left:20px;'>" +tab.CONTENT+ "</td>" +
							"</tr>";
					});
					tbody+="</tbody>";
					$("#qualityTarget").append(tbody);
				}
			}
		});
	}
	
	function inputVal(){
		$(".td").click(function(){
			var tis=$(this);
			tis.children("input").attr("type","text");
			tis.children("input").focus();
			tis.children("input").val(tis.children("div").html());
			tis.children("div").css("display","none");
		})
	}
	
	function saveVal(){
		$(".input").blur(function(){
			var tis=$(this);
			var val=tis.val();
			if(val==null||$.trim(val)==""){
				val="";
			}else{
				if(val.lastIndexOf("%")==-1){//无%
					if(isNaN($.trim(val))){//非数字
						val="";
					}else{
						val=val/100;
					}
				}else{//有%
					val=$.trim(val);
					if(isNaN(val.substring(0,val.lastIndexOf("%")))){//非数字
						val="";
					}else{
						val=val.substring(0,val.lastIndexOf("%"))/100;
					}
				}
			}
				$.ajax({
				url: getRootPath() + '/projectlable/saveActualVal',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					id: tis.attr("id"),
					actualVal: val
				},
				success: function () {
					tis.attr("type","hidden");
					tis.next("div").css("display","");
					if(val!=""){
						tis.next("div").html((val*100).toFixed(1)+"%");
					}else{
						tis.next("div").html("");
					}
				}
			});
		})
	}
	
  	$(document).ready(function(){
  	    measure();
  	    inputVal();
  	    saveVal();
	})
})()