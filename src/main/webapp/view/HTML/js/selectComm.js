(function(){
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
	
	
	$(document).ready(function(){
		$('#usertype2').selectpicker("val",["在行"]);
        var zrOrhwselect = getCookie("zrOrhwselect");
        $('#selectBig').selectpicker("val",[zrOrhwselect=='0'?'2':'1']);
        selectChange();
		initArea();
		initHWCPX();
		initYeWuXian();
	})
})()

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
  		$("#select8").css('display','');
  		$("#select7").css('display','');
  		$("#select6").css('display','');
  		$("#select5").css('display','none');
  		$("#select4").css('display','none');
  		$("#select3").css('display','none');
  		$("#select1").css('display','none');
	}else if($("#selectBig").val()=='2'){
  		$("#select8").css('display','none');
  		$("#select7").css('display','none');
  		$("#select6").css('display','none');
  		$("#select5").css('display','');
  		$("#select4").css('display','');
  		$("#select3").css('display','');
  		$("#select1").css('display','none');
	}else if($("#selectBig").val()=='3'){
  		$("#select8").css('display','none');
  		$("#select7").css('display','none');
  		$("#select6").css('display','none');
  		$("#select5").css('display','none');
  		$("#select4").css('display','none');
  		$("#select3").css('display','none');
  		$("#select1").css('display','');
	}
};