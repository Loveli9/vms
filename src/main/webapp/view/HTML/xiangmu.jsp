<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>可视化度量平台</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resources/bootstrap/bootstrap.min.css" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/view/HTML/css/DefaultSkin.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/view/HTML/css/toastr.min.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/resources/bootstrap/bootstrap.min.css" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/view/HTML/js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/easyui/jquery.easyui.min.js"></script>
	<!-- 消息提示插件 -->
	<script type="text/javascript" src="<%=request.getContextPath() %>/view/HTML/js/toastr.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/comm/bootstrap-message.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/comm/comm.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/underscore/underscore.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/bootstrap/bootstrap.min.js"></script>

<style>

	.nav>li.active>a, .nav>li.active>a:focus, .nav>li.active>a:hover{ 
		color: #FF8850; border-left: 2px #FF8850 solid;
	}
	li.active>.nav>li.active>a, li.active>.nav>li.active>a:focus, li.active>.nav>li.active>a:hover {
		color: #FF8850;border-left: 2px #FF8850 solid;
	}
	.nav>li.active>a[name='oneNav'], .nav>li.active>a[name='oneNav']:focus, .nav>li.active>a[name='oneNav']:hover{
		color: #FF8850;border-left: 0px #FF8850 solid;
	}
	.nav>li.active>a[class='oneNavAct'], .nav>li.active>a[class='oneNavAct']:focus, .nav>li.active>a[class='oneNavAct']:hover{
		border-left: 2px #3399ff solid;
	}
	.nav>li>a:hover {
		color: #FF8850; background-color: #fff;
	}
	li.active>.nav>li>a:hover {
		color: #FF8850;background-color: #EBF3FF;
	}
	.nav>li>a { 
		color: #5AADFA; text-decoration: none;
	}
	li.active>.nav>li>a {
		color: #5AADFA;background-color: #EBF3FF;
	}
	.nav>li.active { 
		background-color: #EBF3FF;
	}
	.nav>li.active>ul>li { 
		margin-top: 0px;
	}
	.startCollect { 
		float: right; margin-right: 5px; border: 1px #e41f2b solid; background-color: #e41f2b; color: white; border-radius: 5px; width: 80px; height: 25px; line-height: 23px; margin-top: 5px; font-size: 12px; outline: none;
	}
    .startCollect:hover { 
    	background-color: #ff0000; color: #ffffff;
    }
    .breadcrumb {
		margin-left: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
		padding: 8px 0px;
     }
    .breadcrumb>li+li:before {
	    color: red;
	}
</style>

<script type="text/javascript">
    var baselocation=' <%=request.getContextPath()%>';
    var projNo = getQueryString("projNo");
    var projName ='';
    function changeFrameHeight() {
        var ifm = document.getElementById("myiframe");
        ifm.height = document.documentElement.clientHeight-36;
    };
    var page = [];
    window.onresize=function(){ changeFrameHeight();}
    $(function(){changeFrameHeight();});
    
  //记载项目流程及菜单
    function loadProjectLabs(){
    	var Menus = getCookie('Menulist');
    	$.ajax({
    		type: 'POST',
    		dataType: 'json',
    		async:false, 
    		url: baselocation+'/projectlable/getProjectLabs',
    		data : {
    			projNo : projNo
    		},
    		success : function(data){
    			var dataLg = data.data.length;//返回数据长度
    			var zcd = data.data[dataLg-1];//子菜单
				var obj = data.data;
			 	obj.reverse();
				if (Menus.indexOf("-58-") != -1) {
					var object = {
							"ID":'0',
							"LAB_PATH":'/view/HTML/config.html',
							"TITLE":'配置'
							}; 
					obj.push(object);
				}

    			if(obj.length > 0){
    				var isDevOps = true;
    				for (var i = 0; i < obj.length; i++) {
    					if(obj[i].LAB_PATH == undefined || obj[i].TITLE == undefined){
    						continue;
    					}
    					var menu = '';	
    					
    					if(isDevOps && obj[i].CATEGORY == 'DevOps' ){
    						isDevOps = false;
    						document.getElementById("collectButtonDiv").style.display = "none";
    						menu += '<li style="text-align: left;">';
    						menu += '<a name="oneNav"  onclick=changePage("' + obj[i].ID +'","' + '/view/HTML/yunlongProjectDetail.html") style="padding-left: 40px;" data-toggle="tab">项目详情</a></li>';
    						menu += '<li style="text-align: left;">';
    						menu += '<a name="oneNav"  onclick=changePage("' + obj[i].ID +'","' + '/view/HTML/tService.html") style="padding-left: 40px;" data-toggle="tab">服务配置</a></li>';
						}
    					
    					menu += '<li style="text-align: left;">';
    					if(isDevOps || obj[i].TITLE == "配置"){
    						menu += '<a name="oneNav" style="padding-left: 40px;" data-toggle="tab" onclick=changePage("'+obj[i].ID+'","'+obj[i].LAB_PATH+'")>'+obj[i].TITLE+'</a>';
    					}
    					
    					menu += '<ul class="nav nav-default nav-stacked" style="display: none">';
    					if(obj[i].TITLE == '开发' && Menus.indexOf("-57-") != -1){
    						menu += '<li style="text-align: left;">';
    						menu += '<a style="padding-left: 60px;" onclick=changePageName("'+obj[i].ID+'","'+obj[i].LAB_PATH+'","'+obj[i].TITLE+'","个人效率") data-toggle="tab">个人效率</a></li>';
    					}
    					if(obj[i].TITLE == '测试'){
    						menu += '<li style="text-align: left;">';
    						menu += '<a style="padding-left: 60px;" onclick=changePageName("'+obj[i].ID+'","'+obj[i].LAB_PATH+'","'+obj[i].TITLE+'","测试效率") data-toggle="tab">测试效率</a></li>';
    					} 
    					
    					var plid = obj[i].plId;
        				if(plid != '' && plid != undefined && plid != null){
        					var cd = zcd[plid];
            				for(var j = 0; j < cd.length; j++){
            					menu += '<li style="text-align: left;">';
            					menu += '<a style="padding-left: 60px;" onclick=loadMenuInfo("'+plid+'","'+cd[j]+'","'+obj[i].TITLE+'","'+cd[j]+'") data-toggle="tab">'+cd[j]+'</a></li>';
            				}
        				}
    					
    					menu += '</ul></li>';
    					if(obj[i].ID!='0'){
    						page.push("lab"+obj[i].ID);
    					}
    					$("#myTab").append(menu);
    				}
    			}	
    			obj.pop();
    		}
    	});
    }
    //配置页面选择
    function changePage(labId,labPath) {
        if(labId=="0"){
            $("#tabsTitle").html("配置");
            $("#tabsTitle").css("color", "black");
            $("#tabsTitleOne").css("display", "none");
        }
        var ifm = document.getElementById("myiframe");
        ifm.src = baselocation+labPath+"?labId="+labId;
    };
    function changePageName(labId,labPath,name,changeName) {
        if(changeName==null||changeName==""){
            $("#tabsTitle").html(name);
            $("#tabsTitle").css("color", "black");
            $("#tabsTitleOne").css("display", "none");
        }else{
            var obj_lis = document.getElementById("test").getElementsByTagName("li");
            var namepd = obj_lis[1].innerText;
            if(name!=namepd){
                named = name ;
                $("#tabsTitle").html("<a name='oneNav' data-toggle='tab' onclick='changePageName(\""+labId+"\",\""+labPath+"\",\""+name+"\",\""+changeName+"\")' aria-expanded='true'>"+name+"</a>");
                $("#tabsTitle").css("color", "#3399ff");
            }
            $("#tabsTitleOne").css("display", "inline");
            $("#tabsTitleOne").html(changeName);
        }
        var ifm = document.getElementById("myiframe");
        ifm.src = baselocation+labPath+"?labId="+labId;
    };
    //动态加载子菜单信息
    var menu_plid = '';
    var  = '';
    var lc_title = '';
    var lab_code = '333';
    function loadMenuInfo(plId,category,title,name) {
        menu_plid = plId;
         = category;
        lc_title = title;
        if(name==null||name==""){
            $("#tabsTitle").html(title);
            $("#tabsTitleOne").css("display", "none");
            $("#tabsTitle").css("color", "black");
        }else{
            var obj_lis = document.getElementById("test").getElementsByTagName("li");
            var namepd = obj_lis[1].innerText;
            if(title!=namepd){
                named = name ;
                $("#tabsTitle").html("<a name='oneNav' data-toggle='tab' onclick='loadMenuInfo(\""+plId+"\",\""+category+"\",\""+title+"\",\""+name+"\")' aria-expanded='true'>"+title+"</a>");
                $("#tabsTitle").css("color", "#3399ff");
            }
            $("#tabsTitleOne").css("display", "inline");
            $("#tabsTitle").css("color", "#3399ff");
            $("#tabsTitleOne").html(name);
        }
        var ifm = document.getElementById("myiframe");
        ifm.src = baselocation+"/view/HTML/developIndex.html";
    };

	function getProName(){
		$.ajax({
			url: baselocation + '/bu/projOverviewData',
			type: 'post',
			data:{
				no : projNo
			},
			success: function (data) {
				projName =data.name; 
				$('#userName').text(getCookie('username'));
				$('#projName').html(projName);
				
			}
		});
	};
	

	function getCookie(name){
		   var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
		   if(arr != null){
		     return unescape(arr[2]); 
		   }else{
		     return null;
		   }
	};
	
	$(document).on("click", "ul.oneNav>li>a", function () {
		$("ul.nav li ul li").removeClass("active");
		$("ul.nav li ul").css("display","none");
	});
	/* $(document).on("click", "ul.oneNav>li>ul>li>a", function () {
		$(this).parent('li').parent('ul').parent('li').children('a').removeClass("oneNavAct");
	}); */
	$(document).on("click", "ul.nav li a[name='oneNav']", function () {
		$("ul.nav li ul").css("display","none");
		$(this).parents('li:first').children("ul").css("display","block");
		//$(this).addClass("oneNavAct");
		$(this).parents('li:first').children("ul").children("li:first").addClass("active");
		$(this).parents('li:first').children("ul").children("li:first").children("a").click();
	});  
 	$(document).on("click", "ol li a[name='oneNav']", function (e) {
 		$("ul.nav li ul li").removeClass("active");
 		$("ul.nav li[class='active'] ul li:first").addClass("active");
			 
	});   
    $(document).ready(function () {
	    	var Menus = getCookie('Menulist');
	    	if (Menus.indexOf("-54-") != -1) {
	    		var tab = '<button id="collection" onclick="changePageName(\'1\',\'/view/HTML/collection.html\',\'采集\',\'采集配置任务\')" type="button" class="startCollect" data-toggle="dropdown">采集</button>';
	    		$("#collectButtonDiv").append(tab);
	    	}
	    	if (Menus.indexOf("-76-") != -1) {
	    		var tab0 = '<li style="text-align: left;"><a id="instrumentPanel" onclick="changePageName(\'null\',\'/view/HTML/instrumentPanel.html\',\'仪表盘\')" style="padding-left: 40px;" data-toggle="tab">仪表盘</a></li>';
	    		$("#myTab").append(tab0);
	    	}
	    	if (Menus.indexOf("-78-") != -1) {
	    		var tab1 ='<li class="active" style="text-align: left;"><a id="gk" onclick="changePageName(\'null\',\'/view/HTML/gaikuang.html\',\'概况\')" style="padding-left: 40px;" data-toggle="tab">概况</a></li>';
	    		$("#myTab").append(tab1);
	    	}
	    	if (Menus.indexOf("-55-") != -1) {
	    		var tab2 = '<li><a name="oneNav" style="padding-left: 40px;" data-toggle="tab" id="work">工作项</a><ul class="nav nav-default nav-stacked" style="display: none" id="workmanages"></ul></li>';
	    		$("#myTab").append(tab2);
	    	}
	    	if (Menus.indexOf("-79-") != -1) {
	    		var tab3 = '<li style="text-align: left;"><a style="padding-left: 60px;" onclick="changePageName(\'null\',\'/view/HTML/demandwork.html\',\'工作项\',\'需求管理\')" data-toggle="tab">需求管理</a></li>';
	    		$("#workmanages").append(tab3);
	    	}
	    	if (Menus.indexOf("-80-") != -1) {
	    		var tab4 = '<li><a style="padding-left: 60px;" onclick="changePageName(\'null\',\'/view/HTML/iterationwork.html\',\'工作项\',\'迭代管理\')" data-toggle="tab">迭代管理</a></li>';
	    		$("#workmanages").append(tab4);
	    	}
	    	if (Menus.indexOf("-81-") != -1) {
	    		var tab5 ='<li><a style="padding-left: 60px;" onclick="changePageName(\'null\',\'/view/HTML/measureCommentTree.html\',\'工作项\',\'质量点评\')" data-toggle="tab">质量点评</a></li>';
	    		$("#workmanages").append(tab5);
	    	}
	    	if (Menus.indexOf("-82-") != -1) {
	    		var tab6 ='<li><a id="myQuality" style="padding-left: 60px;" onclick="changePageName(\'null\',\'/view/HTML/projectReview.html\',\'工作项\',\'质量月报\')" data-toggle="tab">质量月报</a></li>';
	    		$("#workmanages").append(tab6);
	    	}
	    	if (Menus.indexOf("-83-") != -1) {
	    		var tab7 ='<li><a style="padding-left: 60px;" onclick="changePageName(\'null\',\'/view/HTML/problemDscription.html\',\'工作项\',\'问题&风险\')" data-toggle="tab">问题&风险</a></li>';
	    		$("#workmanages").append(tab7);
	    	}
	    	/* if (Menus.indexOf("-87-") != -1) {
	    		var tab9 ='<li><a style="padding-left: 60px;" onclick="changePageName(\'null\',\'/view/HTML/memberinformation.html\',\'工作项\',\'成员信息\')" data-toggle="tab">成员信息</a></li>';
	    		$("#workmanages").append(tab9);
	    	} */
	    	if (Menus.indexOf("-84-") != -1) {
	    		var tab8 ='<li style="text-align: left;"><a id="projectManagement" onclick="changePageName(\'null\',\'/view/HTML/projectManagementProcess.html\',\'项目管理\')" style="padding-left: 40px;" data-toggle="tab">项目管理</a></li>';
	    		$("#myTab").append(tab8);
	    	}
	    	if (Menus.indexOf("-87-") != -1) {
	    		var tab1 ='<li style="text-align: left;"><a onclick="changePageName(\'null\',\'/view/HTML/memberinformation.html\',\'绩效\')" style="padding-left: 40px;" data-toggle="tab">绩效</a></li>';
	    		$("#myTab").append(tab1);
	    	}
    		$('.fold', window.parent.document).html("<p>〉</p>").addClass("active").animate({"left":"0px"},300).find("p").css("marginLeft","2px");
    		$('.fold', window.parent.document).siblings(".left").animate({"width":"0px"},300);
    		$('.fold', window.parent.document).siblings(".right").animate({"left":"0px"},300).find("iframe").animate({"width":"100%"},300);
    		$('.fold', window.parent.document).css("display","none");
    		//$('#myTab li:eq(1) a').tab('show');
    		loadProjectLabs();
    
        	getProName();

        	var zlyb = getQueryString("zlyb");
        	if(zlyb){
        		$("#work").click(); 
        		$("#myQuality").click(); 
        		//changePageName('null','/view/HTML/qualityMonthly.html','工作项','质量月报');
			}
        	
        })
    </script>
</head>
<body>
   	<div class="main" style="border-left: 0px #e2e3e4 solid;overflow: hidden;">
		<ol class="breadcrumb" id ="test" style="height: 36px;line-height: 20px;border-radius: 0px;background-color: #EBF3FF;border-right: 6px #999999 solid;">
			<li><a href="#" style="color: #3399ff;margin-left: 20px;" onclick="toProjectHome()">项目</a></li>
		    <li class="active" id="tabsTitle" style="color: black;">概况</li>
		    <li class="active" id="tabsTitleOne" style="color: black;display:none;"></li>
		</ol>
       	<div class="main-content" style="height: calc(100% - 36px);">
           	<div style="float: left;height: 100%;border-left: 6px #EBF3FF solid;width: 15%;overflow: auto;background: #fff;">
				<center>
					<img src="<%=request.getContextPath()%>/view/HTML/images/xiangmu.png" alt="项目图标" style="padding: 5px;margin-top:15px" />
				</center>
				<div style="text-align: center">
					<span id="projName" style="color: #5AADFA; text-align: center"></span>
				</div>
				<!-- 采集、配置 -->
                <div style="text-align: center;padding: 10px;border-bottom: 1px solid #999;">
                	<div class="btn-group" style="display: " id="collectButtonDiv"></div>
                </div>
				<ul id="myTab" class="oneNav nav nav-default nav-stacked" style="font-size: 14px;float: left;width: 100%;"></ul>
			</div>
            <div class="frame-content" style="float: right;width: 85%;height: 100%;">
                <iframe src="<%=request.getContextPath() %>/view/HTML/gaikuang.html" width="100%" id="myiframe" onload="changeFrameHeight()" frameborder="0"></iframe>
            </div>
           	<div id="testDIV" style="display: none;height: 150px; width: 160px; overflow: auto; border-color: #ddd;border-width: 1px;border-radius: 3px;border-style: solid;"></div>
       	</div>
   	</div>
</body>
</html>