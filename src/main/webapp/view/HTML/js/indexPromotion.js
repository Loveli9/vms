$(function(){
	var projNo = window.parent.projNo;
	noId = projNo;
	$.ajax({
		
		url: getRootPath() + '/yunlongPromotion/getIndexPromotion',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			noId : noId
		},
		success: function(data){
			
			var tab = "";
			$("#tabtsx tr").remove();
			
			_.each(data.data,function(list, index){
				tab += 
					'<tr><td>' + list.measureName + '</td>';
				
				var type = "";
				
				if("0" == list.isPromotion){
					type = "<div onclick='changeIsPromotion("+list.measureId+")' style='border: 1px;border-radius: 3px;margin:0 auto;width:60px;height:18px;'>" +
					   "<div id='auto"+list.measureId+"' style='float:left;background-color: #1adc21;border: 1px solid #B5B5B5;border-top-left-radius: 2px;border-bottom-left-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
					   "<span id='a"+list.measureId+"' style='color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>是</span></div>" +
					   "<div id='manual"+list.measureId+"' style='float:left;background-color: #fff;border: 1px solid #B5B5B5;border-top-right-radius: 2px;border-bottom-right-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
					   "<span id='m"+list.measureId+"' style='display:none;color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>否</span></div></div>";
				}else{
					type = "<div onclick='changeIsPromotion("+list.measureId+")' style='border: 1px;border-radius: 3px;margin:0 auto;width:60px;height:18px;'>" +
				       "<div id='auto"+list.measureId+"' style='float:left;background-color: #fff;border: 1px solid #B5B5B5;border-top-left-radius: 2px;border-bottom-left-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
				       "<span id='a"+list.measureId+"' style='display:none;color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>是</span></div>" +
				       "<div id='manual"+list.measureId+"' style='float:left;background-color: #f7b547;border: 1px solid #B5B5B5;border-top-right-radius: 2px;border-bottom-right-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
				       "<span id='m"+list.measureId+"' style='color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>否</span></div></div>";
				}
				tab +=  '<td>' + type + '</td>';	
					
					tab += '<td>' +"<input style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:40px;text-align:center;' onclick='inputFun(this.id)' type='text' id='outTar" + list.measureId + "' value=' " +list.promotionValue + "' >" 
					+"<div id='alloutTar" + list.measureId + "' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>"
					+"<div id='popoutTar" + list.measureId + "' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>"
					+"<span style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>设置值</span>"
					+"<input type='text' id='inoutTar" + list.measureId + "' onkeydown='esc(" + list.measureId + ")' style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:150px;float:left;'   value='" + list.promotionValue + "'>"
					+"<button onclick='saveChangeTar(" + list.measureId + ")' type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>"
					+"<button onclick='concelChangeTar("+ list.measureId + ")' style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button>"
					+'</div></td>'
					
					
					+ '<td>' + list.unit + '</td></tr>';
				
			});
			$("#tabtsx").append(tab);
		
		}
		
	});	
	
	
});

var noId;

function changeIsPromotion(v) {
	
	var type= "";
	if($("#auto"+v).css("background-color") == "rgb(26, 220, 33)"){
		$("#auto"+v).css("background-color","#fff"); 
		$("#manual"+v).css("background-color","#f7b547");
		$("#m"+v).show();
		$("#a"+v).hide();
		type= 1;
	}else{
		$("#auto"+v).css("background-color","#1adc21");
		$("#manual"+v).css("background-color","#fff");
		$("#a"+v).show();
		$("#m"+v).hide();
		type= 0;
	}
	
	$.ajax({
        type: "post",
        url: getRootPath()+"/yunlongPromotion/updatePromotion",
		dataType: "json",
		async: false,
		contentType : 'application/json;charset=utf-8', 
		data: JSON.stringify({
			no:noId,
			measureId:v,
			isPromotion:type,
		}),
        success: function (data) {
        }
    });
	
	
	
}


function esc(a){
	if ( event.keyCode=='27' ) //->右箭头
		  {
	this.concelChangeTar(a);
	this.concelChangeLow(a);
	this.concelChangeUp(a);
		  }
}

function concelChangeTar(v){
	var pop = document.getElementById("popoutTar" + v);
	pop.style.display = "none";
	document.getElementById("alloutTar"+v).style.display = "none";
	$("#outTar" + v)[0].style.border="0px";
}

function saveChangeTar(val){
	$("#outTar"+val)[0].style.border="0px";
	
	var c = $("#inoutTar"+val).val();
	var b = document.getElementById("popoutTar"+val);
	b.style.display = "none";
	document.getElementById("alloutTar"+val).style.display = "none";
	var reg = /^\d+(\.\d{0,2})?$/; 

	if(!c){
		$.ajax({
            type: "post",
            url: getRootPath()+"/yunlongPromotion/updatePromotion",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				measureId:val,
				promotionValue:$("#inoutTar"+val).val(),
				no : noId
			}),
            success: function (data) {
            }
        });
		toastr.success('保存成功！');
		document.getElementById("outTar"+val).value=document.getElementById("inoutTar"+val).placeholder;
		$("#outTar"+val)[0].style.color="red";
	}else if(!reg.test(c)){     
        toastr.warning('输入错误');
        $("#inoutTar"+val).val("");
    } else {
    	document.getElementById("outTar"+val).value=document.getElementById("inoutTar"+val).value;
    	$.ajax({
            type: "post",
            url: getRootPath()+"/yunlongPromotion/updatePromotion",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				measureId:val,
				promotionValue:$("#inoutTar"+val).val(),
				no : noId
			}),
            success: function (data) {
            	console.log(data);
            }
        });
    	toastr.success('保存成功！');
		$("#outTar"+val)[0].style.color="red";
    	
    }
}



function inputFun(v){
	
	var out = document.getElementById(v);
	out.style.border = "1px solid #B5B5B5;";
	var pop = document.getElementById("pop" + v);
	pop.style.display = "block";
	var popInput = document.getElementById("in"+v);
	popInput.focus();
	var len = popInput.value.length;
	if (document.selection){
		var sel = popInput.createTextRange();
		sel.collapse();
		sel.select();
	} else if(typeof popInput.selectionStart == 'number'
	    && typeof popInput.selectionEnd == 'number'){
		popInput.selectionStart = popInput.selectionEnd = len;
	}
	document.getElementById("all"+v).style.display = "block";
	$("#"+v)[0].style.border="2px solid #81D2FA";
}
















