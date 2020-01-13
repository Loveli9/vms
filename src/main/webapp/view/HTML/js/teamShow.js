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

    //加载区域下拉菜单
    function initArea() {
        $.ajax({
            url: getRootPath() + '/tblArea/getAllTblArea',
            type: 'post',
            async: false,//是否异步，true为异步
            success: function (data) {
                var select_option="";
                $("#usertype1").empty();
                if(null==data.data||""==data.data) return;
                _.each(data.data, function(val, index){//(值，下标)
                    select_option += "<option value="+val.areaCode+">"+val.areaName+"</option>";
                })
                $("#usertype1").html(select_option);
                $('#usertype1').selectpicker('refresh');
                $('#usertype1').selectpicker('render');
            }
        });
    };

    function initYeWuXian() {//默认加载业务线
        $.ajax({
            url: getRootPath() + '/opDepartment/getOpDepartment',
            type: 'post',
            async: false,
            data: {
                level: 2,
            },
            success: function (data) {
                $('#usertype6').selectpicker("val", []);
                $("#usertype6").empty();
                $('#usertype6').prev('div.dropdown-menu').find('ul').empty();
                $('#usertype7').selectpicker("val", []);
                $("#usertype7").empty();
                $('#usertype7').prev('div.dropdown-menu').find('ul').empty();
                $('#usertype8').selectpicker("val", []);
                $("#usertype8").empty();
                $('#usertype8').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#usertype6").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#usertype6').selectpicker('refresh');
                $('#usertype6').selectpicker('render');
            }
        })
    };

    $(document).on("change", "#usertype6", function () {
        $.ajax({
            url: getRootPath() + '/opDepartment/getOpDepartment',
            type: 'post',
            async: false,
            data: {level: 3, ids: $("#usertype6").selectpicker("val") == null ? null : $("#usertype6").selectpicker("val").join()},
            success: function (data) {
                $('#usertype7').selectpicker("val", []);
                $("#usertype7").empty();
                $('#usertype7').prev('div.dropdown-menu').find('ul').empty();
                $('#usertype8').selectpicker("val", []);
                $("#usertype8").empty();
                $('#usertype8').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#usertype7").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#usertype7').selectpicker('refresh');
                $('#usertype7').selectpicker('render');
            }
        })
    });
    $(document).on("change", "#usertype7", function () {
        $.ajax({
            url: getRootPath() + '/opDepartment/getOpDepartment',
            type: 'post',
            async: false,
            data: {
                level: 4,
                ids: $("#usertype7").selectpicker("val") == null ? null : $("#usertype7").selectpicker("val").join()
            },
            success: function (data) {
                $('#usertype8').selectpicker("val", []);
                $("#usertype8").empty();
                $('#usertype8').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#usertype8").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#usertype8').selectpicker('refresh');
                $('#usertype8').selectpicker('render');
            }
        })
    });

    function initHWCPX() {//默认加载华为产品线
        $.ajax({
            url: getRootPath() + '/bu/getHwdept',
            type: 'post',
            async: false,
            data: {
                level: 1,
            },
            success: function (data) {
                $('#usertype3').selectpicker("val", []);
                $("#usertype3").empty();
                $('#usertype3').prev('div.dropdown-menu').find('ul').empty();
                $('#usertype4').selectpicker("val", []);
                $("#usertype4").empty();
                $('#usertype4').prev('div.dropdown-menu').find('ul').empty();
                $('#usertype5').selectpicker("val", []);
                $("#usertype5").empty();
                $('#usertype5').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#usertype3").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#usertype3').selectpicker('refresh');
                $('#usertype3').selectpicker('render');
            }
        })
    };
    $(document).on("change", "#usertype3", function () {
        $.ajax({
            url: getRootPath() + '/bu/getHwdept',
            type: 'post',
            async: false,
            data: {level: 2, ids: $("#usertype3").selectpicker("val") == null ? null : $("#usertype3").selectpicker("val").join()},
            success: function (data) {
                $('#usertype4').selectpicker("val", []);
                $("#usertype4").empty();
                $('#usertype4').prev('div.dropdown-menu').find('ul').empty();
                $('#usertype5').selectpicker("val", []);
                $("#usertype5").empty();
                $('#usertype5').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#usertype4").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#usertype4').selectpicker('refresh');
                $('#usertype4').selectpicker('render');
            }
        })
    });
    $(document).on("change", "#usertype4", function () {
        $.ajax({
            url: getRootPath() + '/bu/getHwdept',
            type: 'post',
            async: false,
            data: {
                level: 3,
                ids: $("#usertype4").selectpicker("val") == null ? null : $("#usertype4").selectpicker("val").join()
            },
            success: function (data) {
                $('#usertype5').selectpicker("val", []);
                $("#usertype5").empty();
                $('#usertype5').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#usertype5").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#usertype5').selectpicker('refresh');
                $('#usertype5').selectpicker('render');
            }
        })
    });

	String.prototype.endWith=function(str){    
		  var reg=new RegExp(str+"$");    
		  return reg.test(this);       
	};
	
	
	
	/**
	 * 展示各部门关键角色胜任度
	 */
    $.fn.roleCompetent=function(){
		$.ajax({
			url: getRootPath() + '/manpowerReport/keyRole',
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
			},
			success: function (data) {
				var dataMsg=data;
                var html='';
				if(0==dataMsg.status){
					if(dataMsg.data!=null){
						var percent1=(dataMsg.data.competencySumNum/dataMsg.data.workingSumNum)*100;
						$('.key-role').html('<span style="font-size:24px;color:#F45150;font-weight:bold;">'+percent1.toFixed(2)+'</span>   '+$('#select6 :selected').text()+'关键角色胜任度');
						if(dataMsg.data.detail!=null&&dataMsg.data.detail.length>0){	
							var detail=dataMsg.data.detail;
							var length=detail.length;
							var yu=length%3;//取余数
							var yiji=Math.ceil(length/3);//向上取整
								for (var i = 1; i <=yiji; i++) {
									html+="<li class='carousel-item'>"
									for(var j=1;j<=3;j++){
										var INDEX=j+(i-1)*3-1;
										if(INDEX<length){
											var text=detail[INDEX].competenceNum+'/'+detail[INDEX].workingNum;
											var percent=(detail[INDEX].competenceNum/detail[INDEX].workingNum)*100;
											html+="<div id='myStat"+INDEX+"' data-dimension='150' data-text='"+percent.toFixed(2)+"'  data-info='"+detail[INDEX].name+"' data-width='10' data-fontsize='20' data-percent='"+percent+"' data-fgcolor='#F45150' data-bgcolor='#F7F7F7' style='margin-left:12px;margin-right:18px;float:left;'></div>";							
										}
									}
									html+="</li>";
								}
							
						}						 
					}
				}
                $(".carousel-inner").html(html);
                for(var j=0;j<$('.carousel-item div').length;j++){
                    $('#myStat'+j).circliful();
                }
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
	 * 展示人员到位率
	 */
    $.fn.roleArrival=function(){
		$.ajax({
			url: getRootPath() + '/manpowerReport/positionRate',
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
			},
			success: function (data) {
				var dataMsg=data;
				var html='';
				if(0==dataMsg.status){

					if(dataMsg.data!=null){
						if(dataMsg.data!=null&&dataMsg.data.length>0){
							var roleData=dataMsg.data;
							for(var i=0;i<roleData.length;i++){
								if(0 == roleData[i].memberRate){
									roleData[i].memberRate = '0%';
								}
								if(0 == roleData[i].keyRoleRate){
									roleData[i].keyRoleRate = '0%';
								}
								html += '<tr><td><a style="color: #721b77;" href="'+ getRootPath() + '/bu/projView?projNo=' + roleData[i].no + '&a=' + Math.random() + '">'+roleData[i].name+'</a></td><td>'+roleData[i].keyRoleRate+'</td><td>'+roleData[i].memberRate+'</td></tr>';
							}
						}
					}
                    $('#roleArrival tbody').html(html);
				}
			}
		});
	}
	
	
	$(document).ready(function(){
		$('#usertype2').selectpicker("val",["在行"]);
        var zrOrhwselect = getCookie("zrOrhwselect");
        $('#selectBig').selectpicker("val",[zrOrhwselect=='0'?'2':'1']);
        selectChange();
		initArea();
		initHWCPX();
		initYeWuXian();
		
		$('.modal-backdrop').addClass("disabled");
	})
})()

function selectOnchange(){

    $().roleCompetent();
    $().roleArrival();
};
function selectChange(){
	$('#usertype1').selectpicker("val",[]);
	$('#usertype2').selectpicker("val",["在行"]);
	$('#usertype3').selectpicker("val",[]);
	$('#usertype4').selectpicker("val",[]);
	$("#usertype4").empty();
	$('#usertype4').prev('div.dropdown-menu').find('ul').empty();
	$('#usertype5').selectpicker("val",[]);
	$("#usertype5").empty();
	$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
	$('#usertype6').selectpicker("val",[]);
	$('#usertype7').selectpicker("val",[]);
	$("#usertype7").empty();
	$('#usertype7').prev('div.dropdown-menu').find('ul').empty();
	$('#usertype8').selectpicker("val",[]);
	$("#usertype8").empty();
	$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
	if($("#selectBig").val()=='1'){
  		$("#select7").css('display','');
  		$("#select6").css('display','');
  		$("#select4").css('display','none');
  		$("#select3").css('display','none');
  		$("#select1").css('display','none');
	}else if($("#selectBig").val()=='2'){
  		$("#select7").css('display','none');
  		$("#select6").css('display','none');
  		$("#select4").css('display','');
  		$("#select3").css('display','');
  		$("#select1").css('display','none');
	}else if($("#selectBig").val()=='3'){
  		$("#select7").css('display','none');
  		$("#select6").css('display','none');
  		$("#select4").css('display','none');
  		$("#select3").css('display','none');
  		$("#select1").css('display','');
	}
    selectOnchange();
};
function queryMaturityAssessment(organ){
	var result="";
	$.ajax({
		url: getRootPath() + '/project/initMaturityAssessment',
		type: 'post',
		async: false,//是否异步，true为异步
		data: {
        	'area':organ.area==null?"":organ.area, 
        	'hwpdu': organ.hwpdu==null?"":organ.hwpdu, 
			'hwzpdu':organ.hwzpdu==null?"":organ.hwzpdu, 
			'pduSpdt':organ.pduSpdt==null?"":organ.pduSpdt, 
			'bu': organ.bu==null?"":organ.bu, 
			'pdu':organ.pdu==null?"":organ.pdu, 
			'du':organ.du==null?"":organ.du
        },
		success: function (data) {
//			$("#sumPros").text(data.sumPros);
//			$("#redPros").text(data.redPros);
//			$("#yellowPros").text(data.yellowPros);
//			$("#greenPros").text(data.greenPros);
//			$("#noPros").text(data.noPros);
			result=data;
		}
	});
	return result;
}

