$(function(){
	
	function getValue(url){
		//首先获取地址
		var url = url || window.location.href;
		//获取传值
		var arr = url.split("?");
		//判断是否有传值
		if(arr.length == 1){
			return null;
		}
		//获取get传值的个数
		var value_arr = arr[1].split("&");
		//循环生成返回的对象
		var obj = {};
		for(var i = 0; i < value_arr.length; i++){
			var key_val = value_arr[i].split("=");
			obj[key_val[0]]=key_val[1];
		}
		return obj;
	}

	serviceId = getValue().serviceId;
	
	
	$.ajax({
		url: getRootPath() + '/yunlongProject/getInsertIndex',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
			serviceId : serviceId
		},
		success: function(data){
			
			$("#indexInsert tr").remove();
			var tab = "";
			_.each(data.data,function(list, index){
				tab += 
					'<tr><td>' + list.name + '</td>'
					
					+ '<td>' +"<input style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:40px;text-align:center;' onclick='inputFun(this.id)' type='text' id='outTar" + list.id + "' value=' " +list.value + "' >" 
					+"<div id='alloutTar" + list.id + "' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>"
					+"<div id='popoutTar" + list.id + "' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>"
					+"<span style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>设置值</span>"
					+"<input type='text' id='inoutTar" + list.id + "' onkeydown='esc(" + list.id + ")' style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:150px;float:left;'   value='" + list.value + "'>"
					+"<button onclick='saveChangeTar(" + list.id + ")' type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>"
					+"<button onclick='concelChangeTar("+ list.id + ")' style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button>"
					+'</div></td>'
					
					
					+ '<td>' + list.unit + '</td></tr>';
				
			});
			$("#indexInsert").append(tab);
			
		}
	});	
	
});
var serviceId;

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
		console.log($("#inoutTar"+val).attr('placeholder'));
		$.ajax({
            type: "post",
            url: getRootPath()+"/yunlongProject/insertServiceIndex",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				indexId:val,
				value:$("#inoutTar"+val).val(),
				serviceId : serviceId
			}),
            success: function (data) {
            	console.log(data);
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
            url: getRootPath()+"/yunlongProject/insertServiceIndex",
			dataType: "json",
			async: false,
			contentType : 'application/json;charset=utf-8', 
			data: JSON.stringify({
				indexId:val,
				value:$("#inoutTar"+val).val(),
				serviceId : serviceId
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
