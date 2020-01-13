var initMaintainList = function(num){
			$("#paramList").removeClass('current');
			$('#infoList').addClass('current');
			var url = getRootPath() + "/project/maintain?no=" + num;
			var title = '项目参数列表' + num;
			if ($('#tbs').tabs('exists', title)){
				$('#tbs').tabs('select', title);
			} else {
				$('#tbs').tabs('add',{});
	
		        // tab刷新内容 fix 自适应高度
		        var tab = $('#tbs').tabs('getSelected');
		        $("#tbs").tabs('update', {
		            tab: tab,
		            options: {
		                title: title,
		                content: '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>',
		                closable: true,
		                selected:true
		            }
		        });
			}
	};
var parameterEditorPage = function(num){
	$("#paramList").removeClass('current');
	$('#infoList').addClass('current');
	var url = getRootPath() + "/project/parameterEditor?projNo=" + num;
	var title = num + '参数编辑';
	if ($('#tbs').tabs('exists', title)){
		$('#tbs').tabs('select', title);
	} else {
		$('#tbs').tabs('add',{});

        // tab刷新内容 fix 自适应高度
        var tab = $('#tbs').tabs('getSelected');
        $("#tbs").tabs('update', {
            tab: tab,
            options: {
                title: title,
                content: '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>',
                closable: true,
                selected:true
            }
        });
	}
};

var initUpdataUserInfo = function(username){
	var url = getRootPath() + "/user/update?username=" + username;
	var title = '用户参数列表' + username;
	if ($('#tbs').tabs('exists', title)){
		$('#tbs').tabs('select', title);
	} else {
		$('#tbs').tabs('add',{});
        var tab = $('#tbs').tabs('getSelected');
        $("#tbs").tabs('update', {
            tab: tab,
            options: {
                title: title,
                content: '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>',
                closable: true,
                selected:true
            }
        });
	}
};

var initAddUserInfo = function(){
	var parma="add";
	var url = getRootPath() + "/user/add?parma=" + parma;
	var title = '用户基本信息';
	if ($('#tbs').tabs('exists', title)){
		$('#tbs').tabs('select', title);
	} else {
		$('#tbs').tabs('add',{});

        var tab = $('#tbs').tabs('getSelected');
        $('#tbs').tabs('update', {
            tab: tab,
            options: {
                title: title,
                content: '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>',
                closable: true,
                selected:true
            }
        });
	}
};

var initAddInfo = function(){
	var parma="add";
	var url = getRootPath() + "/project/addInfo?parma=" + parma;
	var title = '项目基本信息';
	if ($('#tbs').tabs('exists', title)){
		$('#tbs').tabs('select', title);
	} else {
		$('#tbs').tabs('add',{});

        var tab = $('#tbs').tabs('getSelected');
        $('#tbs').tabs('update', {
            tab: tab,
            options: {
                title: title,
                content: '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>',
                closable: true,
                selected:true
            }
        });
	}
};

var initUpdateAddInfo = function(num){
	var url = getRootPath() + "/project/updateInfo?no=" + num;
	var title = '项目基本信息'+num;
	if ($('#tbs').tabs('exists', title)){
		$('#tbs').tabs('select', title);
	} else {
		$('#tbs').tabs('add',{});

        var tab = $('#tbs').tabs('getSelected');
        $('#tbs').tabs('update', {
            tab: tab,
            options: {
                title: title,
                content: '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>',
                closable: true,
                selected:true
            }
        });
	}
};

(function(){
	function MainFrame(){

	};

	var mainFrame = new MainFrame();
	
	MainFrame.prototype.initProjList = function(){
		$("#infoList").click(function(){
			$(this).addClass('current');
			$('#paramList').removeClass('current');
			mainFrame.addTable('项目信息列表', getRootPath() + "/project/proj");
		});
	};

	MainFrame.prototype.initUserList = function(){
		$("#userList").click(function(){
			$(this).addClass('current');
			$('#paramList').removeClass('current');
			mainFrame.addTable('用户信息列表', getRootPath() + "/user/users");
		});
	};
	
	MainFrame.prototype.initParamList = function(){
		$("#paramList").click(function(){
			$(this).addClass('current');
			$('#infoList').removeClass('current');
			mainFrame.addTable('度量参数列表', getRootPath() + "/parameter/param");
		});
	};
	
	MainFrame.prototype.addTable = function(title, url){
		if ($('#tbs').tabs('exists', title)){
			$('#tbs').tabs('select', title);
		} else {
			$('#tbs').tabs('add',{});

	        var tab = $('#tbs').tabs('getSelected');
	        $("#tbs").tabs('update', {
	            tab: tab,
	            options: {
	                title: title,
	                content: '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>',
	                closable: true,
	                selected:true
	            }
	        });
		}
	}

    $(document).on("change", "#zrOrhwselect", function () {
        zrOrhwselectChenge();
    });
	
	$(document).ready(function(){
		mainFrame.initProjList();
		mainFrame.initUserList();
		mainFrame.initParamList();
		mainFrame.addTable('项目信息列表', getRootPath() + "/project/proj");
		
		initDownMenu();
	});
})();

function zrOrhwselectChenge(){
    var flag = $("#zrOrhwselect option:selected").val();
    if(flag=="0"){
        $("#hwselect").css("display","block");
        $("#zrselect").css("display","none");
        $('#usertype6').selectpicker('val', '');
        $('#usertype7').selectpicker('val', '');
        $('#usertype8').selectpicker('val', '');
    }else{
        $("#hwselect").css("display","none");
        $("#zrselect").css("display","block");
        $('#usertype3').selectpicker('val', '');
        $('#usertype4').selectpicker('val', '');
        $('#usertype5').selectpicker('val', '');
    }
};

function setCookieValues(){
    setCookie('client_common',$("#zrOrhwselect option:selected").val());
    setCookie('name_common', encodeURI($("#projName").val()));
	setCookie('area_common', $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join());//转换为字符串
	setCookie('hwpdu_common', $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join());//转换为字符串
	setCookie('hwzpdu_common', $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join());//转换为字符串
	setCookie('pduSpdt_common', $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join());//转换为字符串
	setCookie('bu_common', $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join());//转换为字符串
	setCookie('pdu_common', $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join());//转换为字符串
	setCookie('du_common', $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join());//转换为字符串
	setCookie('projectState_common', $("#usertype2").selectpicker("val")==null?null:encodeURI($("#usertype2").selectpicker("val").join()));//转换为字符串
}
