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
                addPie(data.data,1);//1为中软 pdu
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
                addPie(data.data,2);//2为中软 du
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
                addPie(data.data,3);//3为华为子产品线hwzpdu
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
                addPie(data.data,4);//3为PDU/SPDT
            }
        })
    });


    String.prototype.endWith=function(str){
        var reg=new RegExp(str+"$");
        return reg.test(this);
    };
	/**
	 * 展示各个部门361成熟度饼状图
	 */
	function addPie(data,type){
		var onePro=0;
		var twoPro=0;
		var threePro=0;
		var fourPro=0;
		var fivePro=0;
		var notPro=0;
	    var json2 = [  
	    	 {value:fivePro,name:'五星项目数'},  
             {value:fourPro,name:'四星项目数'},  
             {value:threePro,name:'三星项目数'},
             {value:twoPro,name:'二星项目数'},  
             {value:onePro,name:'一星项目数'},  
             {value:notPro,name:'零星项目数'}
            ];
	    var option3={  
	            title : {
	            	text:'',
	            	x:'center',
	            	y:'bottom',
	            	itemGap:3,
	            	padding:30,
	            	textStyle:{
	                 	color:'#666',
	                 	fontWeight:50,
	                 	fontFamily:'sans-serif',
	                 	fontSize:14
	            	}
	            },
	            tooltip : {  
	                trigger: 'item',  
	                formatter: "项目数为0",
	                position:function(p){ //其中p为当前鼠标的位置
	                	return [p[0]-40, p[1]-60];
	                },
	                extraCssText:'width:90px;height:20px;z-index:9999;'  
	            },  
	            color: ['#f7f8f8'],
	            calculable : true,  
	            series : [
	                {  
	                    name:'星级评估',  
	                    type:'pie',  
	                    radius :['40%' ,'75%'],//饼图的半径大小  
	                    center: ['50%', '50%'],//饼图的位置  
	                    itemStyle : {
	                        normal : {
	                            label : {
	                                show : false   //隐藏标示文字
	                            },
	                            labelLine : {
	                                show : false   //隐藏标示线
	                            }
	                        }
	                    },
	                    data:[0]
	                }
	            ]
	    }
	    var option2={  
	            title : {
	            	text:'',
	            	x:'center',
	            	y:'bottom',
	            	itemGap:3,
	            	padding:30,
	            	textStyle:{
	                 	color:'#666',
	                 	fontWeight:50,
	                 	fontFamily:'sans-serif',
	                 	fontSize:14
	            	}
	            },
	            tooltip : {  
	                trigger: 'item',  
	                formatter: "{b} :<br/> {c}({d}%)",
	                position:function(p){ //其中p为当前鼠标的位置
	                	return [p[0]-40, p[1]-60];
	                },
	                extraCssText:'width:90px;height:40px;z-index:9999;'  
	            },  
	            color: ['#f7b547','#1adc21','#7afcd1','#c2fa85','#bdb7b7','#f57454'],
	            calculable : true,  
	            series : [
	                {  
	                    name:'星级评估',  
	                    type:'pie',  
	                    radius :['40%' ,'75%'],//饼图的半径大小  
	                    center: ['50%', '50%'],//饼图的位置  
	                    itemStyle : {
	                        normal : {
	                            label : {
	                                show : false   //隐藏标示文字
	                            },
	                            labelLine : {
	                                show : false   //隐藏标示线
	                            }
	                        }
	                    },
	                    data:json2
	                }
	            ]
	    }
			var html2="";
			if(data!=null&& data!=''&&data.length>0){
				var length=data.length;
				var yu=length%3;//取余数
				var yiji=Math.ceil(length/3);//向上取整
			for (var i = 1; i <=yiji; i++) {
				html2+="<li class='carousel-item'><div style='margin-left:12px;margin-right:18px;'>"
				for(var j=1;j<=3;j++){
					html2+="<div id='divStr"+(j+(i-1)*3)+"' style='float:left; width:150px; height:250px;'></div>";
				}
				html2+="</div></li>";
			}
			$("#carousel_2 .carousel-inner").html(html2);
			for(var i = 1; i <=yiji; i++){
				if(i!=yiji){
					for(var j=1;j<=3;j++){
                        organ.client="1";
						organ.area="";
					    organ.hwpdu="";
						organ.hwzpdu="";
						organ.pduSpdt="";
						organ.bu="";
						organ.pdu="";
						organ.du="";
						if(type==1){
							option2.title.text=data[((j+(i-1)*3)-1)].name;
							option3.title.text=data[((j+(i-1)*3)-1)].name;
							organ.pdu=data[((j+(i-1)*3)-1)].id;
                            organ.client="1";
						}else if(type==2){
							option2.title.text=data[((j+(i-1)*3)-1)].name;
							option3.title.text=data[((j+(i-1)*3)-1)].name;
							organ.du=data[((j+(i-1)*3)-1)].id;
                            organ.client="1";
							//var starResult=queryStarRating(organ);
						}else if(type==3){
							option2.title.text=data[((j+(i-1)*3)-1)].ZCPXNAME;
							option3.title.text=data[((j+(i-1)*3)-1)].ZCPXNAME;
							organ.hwzpdu=data[((j+(i-1)*3)-1)].ZCPXID;
                            organ.client="2";
						}else if(type==4){
							option3.title.text=data[((j+(i-1)*3)-1)].PDUNAME;
							option2.title.text=data[((j+(i-1)*3)-1)].PDUNAME;
							organ.pduSpdt=data[((j+(i-1)*3)-1)].PDUID;
                            organ.client="2";
						}
						var starResult=queryStarRating(organ);
						json2[4].value=starResult.single;
						json2[3].value=starResult.couple;
						json2[2].value=starResult.triple;
						json2[1].value=starResult.quadruple;
						json2[0].value=starResult.quintuple;
						json2[5].value=starResult.zero;
						if(json2[4].value!=0 || json2[3].value!=0 ||json2[2].value!=0 ||json2[1].value!=0
								||json2[0].value!=0	||json2[5].value!=0){
							echarts.init(document.getElementById('divStr'+(j+(i-1)*3))).setOption(option2);
						}else{
							echarts.init(document.getElementById('divStr'+(j+(i-1)*3))).setOption(option3);
						}
					}
				}else if(i==yiji){
					if(yu!=0){
						for(var j=1;j<=yu;j++){
                            organ.client="1";
							organ.area="";
						    organ.hwpdu="";
							organ.hwzpdu="";
							organ.pduSpdt="";
							organ.bu="";
							organ.pdu="";
							organ.du="";
							if(type==1){
								option2.title.text=data[((j+(i-1)*3)-1)].name;
								option3.title.text=data[((j+(i-1)*3)-1)].name;
								organ.pdu=data[((j+(i-1)*3)-1)].id;
                                organ.client="1";
							}else if(type==2){
								organ.du=data[((j+(i-1)*3)-1)].id;
								option2.title.text=data[((j+(i-1)*3)-1)].name;
								option3.title.text=data[((j+(i-1)*3)-1)].name;
                                organ.client="1";
							}else if(type==3){
								option2.title.text=data[((j+(i-1)*3)-1)].ZCPXNAME;
								option3.title.text=data[((j+(i-1)*3)-1)].ZCPXNAME;
								organ.hwzpdu=data[((j+(i-1)*3)-1)].ZCPXID;
                                organ.client="2";
							}else if(type==4){
								option2.title.text=data[((j+(i-1)*3)-1)].PDUNAME;
								option3.title.text=data[((j+(i-1)*3)-1)].PDUNAME;
								organ.pduSpdt=data[((j+(i-1)*3)-1)].PDUID;
                                organ.client="2";
							}
							var starResult=queryStarRating(organ);
                            json2[4].value=starResult.single;
                            json2[3].value=starResult.couple;
                            json2[2].value=starResult.triple;
                            json2[1].value=starResult.quadruple;
                            json2[0].value=starResult.quintuple;
                            json2[5].value=starResult.zero;
							if(json2[4].value!=0 || json2[3].value!=0 ||json2[2].value!=0 ||json2[1].value!=0
									||json2[0].value!=0	||json2[5].value!=0){
								echarts.init(document.getElementById('divStr'+(j+(i-1)*3))).setOption(option2);
							}else{
								echarts.init(document.getElementById('divStr'+(j+(i-1)*3))).setOption(option3);
							}
						}
					}else if(yu==0){
						for(var j=1;j<=3;j++){
                            organ.client="1";
							organ.area="";
						    organ.hwpdu="";
							organ.hwzpdu="";
							organ.pduSpdt="";
							organ.bu="";
							organ.pdu="";
							organ.du="";
							if(type==1){
								option2.title.text=data[((j+(i-1)*3)-1)].name;
								option3.title.text=data[((j+(i-1)*3)-1)].name;
								organ.pdu=data[((j+(i-1)*3)-1)].id;
                                organ.client="1";
							}else if(type==2){
								option2.title.text=data[((j+(i-1)*3)-1)].name;
								option3.title.text=data[((j+(i-1)*3)-1)].name;
								organ.du=data[((j+(i-1)*3)-1)].id;
                                organ.client="1";
							}else if(type==3){
								option2.title.text=data[((j+(i-1)*3)-1)].ZCPXNAME;
								option3.title.text=data[((j+(i-1)*3)-1)].ZCPXNAME;
								organ.hwzpdu=data[((j+(i-1)*3)-1)].ZCPXID;
                                organ.client="2";
							}else if(type==4){
								option2.title.text=data[((j+(i-1)*3)-1)].PDUNAME;
								option3.title.text=data[((j+(i-1)*3)-1)].PDUNAME;
								organ.pduSpdt=data[((j+(i-1)*3)-1)].PDUID;
                                organ.client="2";
							}
							var starResult=queryStarRating(organ);
                            json2[4].value=starResult.single;
                            json2[3].value=starResult.couple;
                            json2[2].value=starResult.triple;
                            json2[1].value=starResult.quadruple;
                            json2[0].value=starResult.quintuple;
                            json2[5].value=starResult.zero;
							if(json2[4].value!=0 || json2[3].value!=0 ||json2[2].value!=0 ||json2[1].value!=0
									||json2[0].value!=0	||json2[5].value!=0){
								echarts.init(document.getElementById('divStr'+(j+(i-1)*3))).setOption(option2);
							}else{
								echarts.init(document.getElementById('divStr'+(j+(i-1)*3))).setOption(option3);
							}
						}
					}
				}
			}
			$(".carousel-indicators").removeClass("carousel-indicators");
			$(".carousel-btn carousel-prev-btn").removeClass("carousel-btn carousel-prev-btn");
			$(".carousel-btn carousel-next-btn").removeClass("carousel-btn carousel-next-btn");
			$("#carousel_2").FtCarousel({
				index: 0,
				auto: false
			});
		}
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
		initStarRating();
	})
})()
	
function initStarRating() {//默认加载星级评估
	$.ajax({
		url: getRootPath() + '/starRating/summarize',
		type: 'GET',
		async: false,//是否异步，true为异步
		data: {
            'type': $("#selectBig").val(),
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
        	'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
			'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
			'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
			'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join()//转换为字符串
        },
		success: function (data) {
			$("#onePro").text(data.data.single);
			$("#twoPro").text(data.data.couple);
			$("#threePro").text(data.data.triple);
			$("#fourPro").text(data.data.quadruple);
			$("#fivePro").text(data.data.quintuple);
			$("#notPro").text(data.data.zero);
		}
	});
};	
function selectOnchange(){

			initStarRating();
			loadStarPie();
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
    selectOnchange();
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
};
function queryStarRating(organ){
	var result="";
	$.ajax({
		url: getRootPath() + '/starRating/summarize',
		type: 'GET',
		async: false,//是否异步，true为异步
		data: {
            'type':organ.client==null?"":organ.client,
            'area':organ.area==null?"":organ.area,
        	'hwpdu': organ.hwpdu==null?"":organ.hwpdu, 
			'hwzpdu':organ.hwzpdu==null?"":organ.hwzpdu, 
			'pduSpdt':organ.pduSpdt==null?"":organ.pduSpdt, 
			'bu': organ.bu==null?"":organ.bu, 
			'pdu':organ.pdu==null?"":organ.pdu, 
			'du':organ.du==null?"":organ.du
        },
		success: function (data) {
			result=data.data;
		}
	});
	return result;
}

/*
 * 点击项目数加载项目列表
 */
function loadDatagrid(osel){
	
	document.getElementById("hideDiv").style.display="none";
	document.getElementById("tabsShow").style.display="";
	var stars = '<img src="/mvp/view/HTML/images/timg.jpg" style="height:15px;"/>';
	var num = '';
	if(osel == '5'){
		$("#tabsTitle").html("5星项目列表");
		num = '5';
		var star = '五星';
	}else if(osel == '4'){
		$("#tabsTitle").html("4星项目列表");
		num = '4';
		var star = '四星';
	}else if(osel == '3'){
		$("#tabsTitle").html("3星项目列表");
		num = '3';
		var star = '三星';
	}else if(osel == '2'){
		$("#tabsTitle").html("2星项目列表");
		num = '2';
		var star = '二星';
	}else if(osel == '1'){
		$("#tabsTitle").html("1星项目列表");
		num = '1';
		var star = '一星';
	}else if(osel == '0'){
		$("#tabsTitle").html("0星项目列表");
		num = '0';
		var star = '零星';
	}
	var yrPicture = '';
	for(var i=0;i<Number(num);i++){
		yrPicture+=stars;
	}
	/*$('#tabs').modal({
		backdrop: 'static',
		keyboard: false
    });
	$("#tabs").modal('show');*/
	$.ajax({
		url: getRootPath() + '/starRating/page',
		type: 'GET',
		async: false,//是否异步，true为异步
		data: {
			'starType' : num,
            'type': $("#selectBig").val(),
        	'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
			'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
			'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
			'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join()//转换为字符串
        },
		success: function (data) {
			$("#tablelsw tr").remove();

            var list = data.data;
			for(var i=0;i<list.length;i++){
				var tab = '<tr>'+
					'<td><a style="color: #721b77;" title="'+
                    list[i].name+'" href="'+getRootPath() +'/bu/projView?projNo='+
                    list[i].no+'&a='+Math.random()+'">'+convertDataVal(list[i].name)+'</a></td>'+
					'<td>'+convertDataVal(list[i].pm)+'</td>'+
					'<td>'+convertDataVal(list[i].area)+'</td>'+
					'<td>'+convertDataVal(list[i].hwpdu)+'</td>'+
					'<td>'+convertDataVal(list[i].hwzpdu)+'</td>'+
					'<td>'+convertDataVal(list[i].pduSpdt)+'</td>'+
					'<td>'+convertDataVal(list[i].projectState)+'</td>'+
					'<td>'+yrPicture+'</td></tr>';
				$("#tablelsw").append(tab);
			}
		}
	});
	tablelswInfo(10,'tablelsw');
	parent.hideMenu();
}
function convertDataVal(val){
	if(val){
		return val;
	}else {
		return "";
	}
}

