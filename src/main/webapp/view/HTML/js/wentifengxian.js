
(function(){
	function getRequest() {
		  var url = window.location.search; //获取url中"?"符后的字串
		  var theRequest = new Object();
		  if (url.indexOf("?") != -1) {
		    var str = url.substr(1);
		    strs = str.split("&");
		    for(var i = 0; i < strs.length; i ++) {
		      theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
		    }
		  }
		  return theRequest;
		}
	
	var projNo = window.parent.projNo;
	var buName1 = window.parent.projBU;
	
	
	function wentihuizong(){
		$.ajax({
			url: getRootPath() + '/dtsTaskController/DtsSeverity',
			type: 'post',
			//async: false,
			data: {
				projNo: projNo
			},
			success: function (data) {//后台传来的值是Map<String, Object>
				$("#critNum").empty();
				$("#critNum").append(data.critNum);
				$("#majNum").empty();
				$("#majNum").append(data.majNum);
				$("#minNum").empty();
				$("#minNum").append(data.minNum);
				$("#sugNum").empty();
				$("#sugNum").append(data.sugNum);
				$("#dtsLeaveDINum").empty();
				$("#dtsLeaveDINum").append(data.dtsLeaveDINum);
			}
		});		
	};
	
	function initWorkflowStatus(){
		$.ajax({
			url: getRootPath() + '/dtsTaskController/getDtsVersion',
			type: 'post',
			async: false,
			data: {
				projNo: projNo
			},
			success: function (data) {//后台传来的值是Map<String, Object>
				var select_option="";
				var version=new Array();
				$("#edition1").empty();
				_.each(data, function(val, index){//(值，下标)
					select_option += "<option value='"+val.value+"'>"+val.name+"</option>";
//					if(val.value!="Finish"){
					version[index]=val.value;
//					}
				})
				$("#edition1").html(select_option);
				$('#edition1').selectpicker("val",version);
			}
		});		
	}

	function initCurentStatus(){
		$.ajax({
			url: getRootPath() + '/codeMaster/list',
			type: 'post',
			async: false,
			data: {
				"codekey": "curent_Status"
			},
			success: function (data) {//后台传来的值是Map<String, Object>
				var select_option="";
				var curentStatus=new Array();
				$("#stage0").empty();
				_.each(data.rows, function(val, index){//(值，下标)
					select_option += "<option value='"+val.value+"'>"+val.name+"</option>";
					if(val.value!="Cancel"&&val.value!="Close"){
						curentStatus[index]=val.value;
					}
				})
				$("#stage0").html(select_option);
				$('#stage0').selectpicker("val",curentStatus);
			}
		});		
	}
	
	$(document).ready(function(){
		//refresh();
		initWorkflowStatus();
		initCurentStatus();
		wentihuizong();
//		tongjitu();
	})
})()