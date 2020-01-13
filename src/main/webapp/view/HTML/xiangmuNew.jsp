<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>可视化度量平台</title>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/view/HTML/css/DefaultSkin.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/resources/bootstrap/bootstrap.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/view/HTML/css/toastr.min.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/resources/js/nevigate/css/style.css" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/view/HTML/js/jquery-1.11.1.min.js"></script>
	<!-- 消息提示插件 -->
	<script type="text/javascript" src="<%=request.getContextPath() %>/view/HTML/js/toastr.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/comm/bootstrap-message.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/comm/comm.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/bootstrap/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/underscore/underscore.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/nevigate/js/modernizr.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/nevigate/js/velocity.min.js"></script>
<style>
	.nevigate-icon{
		font-size: 18px;
		margin-bottom: 3px;
	}
	.nav>li.active>a, .nav>li.active>a:focus, .nav>li.active>a:hover {
		border-bottom: 5px #EAEBEB solid;
		font-weight: bold;
		padding-top: 5px;
		padding-bottom: 5px;
		background-color: #2A9AF0 !important; 
		color:white;
		border-radius: 5%;
	}
</style>

<script type="text/javascript">
    var baselocation=' <%=request.getContextPath()%>';
    var projNo = getQueryString("projNo");
    var Menus;
    var multiStep ="0";
    function changeFrameHeight() {
        var ifm = document.getElementById("myiframe");
        if(multiStep==="1"){
            ifm.height = document.documentElement.clientHeight-38-38;
		}else{
            ifm.height = document.documentElement.clientHeight-38;
		}
    };
    var page = [];
    var zcd = [];
    var projName;

    window.onresize=function(){ changeFrameHeight();}
    $(function(){changeFrameHeight();});

    $(document).ready(function(){
    	$.ajax({
    		url: getRootPath() + '/project/getProjNameByNo',
    		type: 'get',
    		async: false,//是否异步，true为异步
    		data:{
    			projectId : projNo
    		},
    		success: function (data) {
    			projName = data;
    			$("#tabsTitle").append(projName);
    		}
    	});
    });
    
    $(document).on("click", "ul.navbar-nav li", function() {
        $("ul.navbar-nav li").removeClass("active");
        _.each($("ul.navbar-nav li"), function(tab, index) {
            $("#" + $(tab).attr("tabname")).css('display', 'none');
        });
        $(this).addClass("active");
        var id = $(this).attr("tabname");
        $("#" + id).css('display', 'block');
    });

    function loadProjectLabs(){
        $.ajax({
            type: 'POST',
            dataType: 'json',
            async:false,
            url: baselocation+'/projectlable/loadLabels',
            data : {
                projNo : projNo
            },
            success : function(data){
                var map = data.data;
                for(var key in map){
                    var categorys = map[key];
					if('开发' == key){
						addMyTab("noMenus","openPage('develop','开发','"+map[key]+"')","开发","","glyphicon-stats");
					}else if('测试' == key){
						addMyTab("noMenus","openPage('test','测试','"+map[key]+"')","测试","","glyphicon-text-width");
					}else{
						addMyTab("noMenus","openPage('automatic','"+key+"','"+map[key]+"')",key,"","glyphicon-list-alt");
					}

                }
            }
        });
    }
    function changePageName(labPath,name,changeName,isMultiStep,page) {
        if(changeName==null||changeName==""){
            $("#tabsTitle").html(name);
            $("#tabsTitle").css("color", "black");
            $("#tabsTitleOne").css("display", "none");
        }else{
            var obj_lis = document.getElementById("test").getElementsByTagName("li");
            var namepd = obj_lis[1].innerText;
            if(name!=namepd){
                named = name ;
                $("#tabsTitle").html("<a name='oneNav' data-toggle='tab' onclick=\"openPage('"+page+"','"+name+"','"+name+"')\" aria-expanded='true'>"+name+"</a>");
                $("#tabsTitle").css("color", "#3399ff");
            }
            $("#tabsTitleOne").css("display", "inline");
            $("#tabsTitleOne").html(changeName);
        }
        if(isMultiStep==="1"){
            $("#multiStep").css('display','block');
        }else{
            $("#dateSectionDiv").css('display','none');
            $("#multiStep").css('display','none');
        }
        multiStep = isMultiStep;
        changeFrameHeight();
        var ifm = document.getElementById("myiframe");
        ifm.src = baselocation+labPath;
    };
    //动态加载子菜单信息

	var label = "";
	var cate = "";
    function loadMenuInfo(category,title,name,isMultiStep,page) {
		label = title;
        cate = category;
		if(name==null||name==""){
            $("#tabsTitle").html(title);
            $("#tabsTitleOne").css("display", "none");
            $("#tabsTitle").css("color", "black");
        }else{
            var obj_lis = document.getElementById("test").getElementsByTagName("li");
            var namepd = obj_lis[1].innerText;
            if(title!=namepd){
                named = name ;
                $("#tabsTitle").html("<a name='oneNav' data-toggle='tab' onclick=\"openPage('"+page+"','"+title+"',)\" aria-expanded='true'>"+title+"</a>");
                $("#tabsTitle").css("color", "#3399ff");
            }
            $("#tabsTitleOne").css("display", "inline");
            $("#tabsTitle").css("color", "#3399ff");
            $("#tabsTitleOne").html(name);
        }
        if(isMultiStep==="1"){
            $("#multiStep").css('display','block');
        }else{

            $("#multiStep").css('display','none');
        }
        multiStep = isMultiStep;
        changeFrameHeight();
        var url = getAngularUrl()+"measureView/"+projNo+"/"+label+"/"+cate;
        var html = '<iframe src="'+url+'" width="100%" id="myiframe" onload="changeFrameHeight()" frameborder="0"></iframe>';
        $("#myiframePre").html("").html(html);
    };



    function openPage(page,objTitle,zcd){
       if(zcd!=undefined && zcd != ""){
           zcd = zcd.split(",");
	   }
        $("#multiStep").empty();
        $("#dateSectionDiv").empty();
        $("#dateSectionDiv").css('display','none');
        var tar ="";
        if(page==="quality"){
            addMultiStep("-81-","changePageName('/view/HTML/measureCommentTree.html','质量','质量点评','1','"+page+"')","质量点评","active");
            addMultiStep("-82-","changePageName('/view/HTML/projectReview.html','质量','OSG双周报','1','"+page+"')","OSG双周报","");
            addDateSectionDiv();
            changePageName('/view/HTML/measureCommentTree.html','质量','质量点评','1',page);
        }else if(page==="develop"){
            addMultiStep("-57-","changePageName('/view/HTML/gerennengli.html','开发','个人效率','1','"+page+"')","个人效率","active");
            for(var j = 0; j < zcd.length; j++){
                addMultiStep("noMenus","loadMenuInfo('"+zcd[j]+"','"+objTitle+"','"+zcd[j]+"','1','"+page+"')",zcd[j],"");
            }
            changePageName('/view/HTML/gerennengli.html','开发','个人效率','1',page);
        }else if(page==="test"){
            addMultiStep("-57-","changePageName('/view/HTML/testmeasures.html','测试','测试效率','1','"+page+"')","测试效率","active");
            for(var j = 0; j < zcd.length; j++){
                addMultiStep("noMenus","loadMenuInfo('"+zcd[j]+"','"+objTitle+"','"+zcd[j]+"','1','"+page+"')",zcd[j],"");
            }
            changePageName('/view/HTML/testmeasures.html','测试','测试效率','1',page);
        }else if(page==="automatic"){
            for(var j = 0; j < zcd.length; j++){
                if(j==0){
                    addMultiStep("noMenus","loadMenuInfo('"+zcd[j]+"','"+objTitle+"','"+zcd[j]+"','1','"+page+"')",zcd[j],"active");
                    loadMenuInfo(zcd[j],objTitle,zcd[j],'1',page);
                }else{
                    addMultiStep("noMenus","loadMenuInfo('"+zcd[j]+"','"+objTitle+"','"+zcd[j]+"','1','"+page+"')",zcd[j],"");
                }
            }
        }
    }
    function changeDateSection(){
    	var id =$("#multiStep li[class='active']")[0].id;
    	if(id=="multiStep-81-"){
    		changePageName('/view/HTML/measureCommentTree.html','质量','质量点评','1',"quality");
    	}else if(id=="multiStep-82-"){
    		changePageName('/view/HTML/projectReview.html','质量','OSG双周报','1',"quality");
    	}
    }
    function addDateSectionDiv(){
    	$.ajax({
			type : "post",
// 			url : getRootPath() + '/projectReview/queryProjectCycle',
			url : getRootPath() + '/measureComment/getTimeQ',
			async: false,//是否异步，true为异步
			data : {
				proNo : projNo,
				num:6,
				flag:true,
			},
			success : function(data) {
				if (data.code = "200") {
					data= data.data;
					if(data.length > 0){
						var nowDate = data[0];
						var option = "<select id='dateSection'  style='font-size: 16px; width: 140px; height: 30px; padding-left: 10px; padding-right: 10px; border-radius: 4px; outline: none;' onchange='changeDateSection()'>"
							+"<option selected ='selected' value='" + nowDate + "'>" + nowDate + "</option>";
						for (var i = 1; i < data.length; i++) {
							option += "<option value='" + data[i] + "'>" + data[i]
									+ "</option>";
						}
						option += "</select>"
						$("#dateSectionDiv").append(option);
						$("#dateSectionDiv").css('display','block');
					} else{
						$("#dateSectionDiv").append("<select id='dateSection'  style='font-size: 8px; width: 140px; height: 30px; padding-left: 10px; padding-right: 10px; border-radius: 4px; outline: none;' onchange='changeDateSection()'>"
								+"<option selected ='selected' value='" + '时间配置不在周期内' + "'>" + '时间配置不在周期内'+ "</option>");
						$("#dateSectionDiv").css('display','block');
					}
				}
			}
		});	
        
	}
    function addMultiStep(id,onclickVel,name,active){
        if (Menus.indexOf(id) != -1 || id=="noMenus") {
            var tab =  "<li id='multiStep"+id+"' class=\""+active+"\" style=\"border: 0px;margin-top:3px;\" onclick=\""+onclickVel+"\"><a href=\"#\">"+name+"</a></li>"
            $("#multiStep").append(tab);
        }
	}

    function addMyTab(id,onclickVel,name,selected,icon){
        if (Menus.indexOf(id) != -1 || id=="noMenus") {
            var tab = '<li><a id="myTab'+id+'" class="'+selected+'" onclick="'+onclickVel+'">' +
                '<div class="nevigate-icon"><span class="glyphicon '+icon+'" aria-hidden="true"></span></div>' +
                '<div>'+name+'</div>' +
                '</a></li>';
            $("#myTab").append(tab);
        }
    }

    $(document).on('click', '.cd-side-navigation a', function(){
        $(".cd-side-navigation a").removeClass("selected");
        $(this).addClass("selected");
    });

    $(document).ready(function () {
        Menus = getCookie('Menulist');
        
        var xmcb = getQueryString("xmcb");
        if(xmcb){
        	addMyTab("-78-","changePageName('/view/HTML/gaikuang.html?test=gaiKuang',projName)","概况","selected","glyphicon-home");
        	$("#myTab-78-").click();
        }else{
        	addMyTab("-78-","changePageName('/view/HTML/gaikuang.html',projName)","概况","selected","glyphicon-home");
        }
        var tips = getQueryString("tips");
        
        addMyTab("-54-","changePageName('/view/HTML/collection.html','采集','采集配置任务')","采集","","glyphicon-glass");
        addMyTab("-76-","changePageName('/view/HTML/instrumentPanel.html','仪表盘')","仪表盘","","glyphicon-signal");
        if (tips == 'xq') {
        	addMyTab("-79-","changePageName('/view/HTML/demandwork.html?','需求')","需求","","glyphicon-align-justify");
        	$("#myTab-79-").click();
		}else{
        	addMyTab("-79-","changePageName('/view/HTML/demandwork.html','需求')","需求", "", "glyphicon-align-justify");
        }
        addMyTab("-80-","changePageName('/view/HTML/iterationwork.html','迭代')","迭代","","glyphicon-time");
        addMyTab("-55-","openPage('quality')","质量","","glyphicon-star");
        if (tips == 'fx') {
			addMyTab("-83-","changePageName('/view/HTML/problemDscription.html?','风险')","风险","","glyphicon-map-marker");
			$("#myTab-83-").click();
		}else{
			addMyTab("-83-","changePageName('/view/HTML/problemDscription.html','风险')","风险", "", "glyphicon-map-marker");
		}
        addMyTab("-84-","changePageName('/view/HTML/projectManagementProcess.html','流程')","流程","","glyphicon-tasks");
        loadProjectLabs();
        if (tips == 'qms') {
			addMyTab("-91-","changePageName('/view/HTML/qmsMaturity.html?','QMS')","QMS","","glyphicon-calendar");
			$("#myTab-91-").click();
		} else{
			addMyTab("-91-","changePageName('/view/HTML/qmsMaturity.html','QMS')","QMS", "", "glyphicon-calendar");
		}
        addMyTab("-92-","changePageName('/view/HTML/networkSecurity.html','网络安全')","网络安全","","glyphicon-calendar");
        addMyTab("-97-","changePageName('/view/HTML/projectOperation.html','操作记录')","操作记录","","glyphicon-calendar");
        addMyTab("-58-","changePageName('/view/HTML/config.html','配置')","配置","","glyphicon-cog");

        var zlyb = getQueryString("zlyb");
        var obj = document.getElementsByTagName("option");
        
        if(zlyb){
            $("#myTab-55-").click();
            for(i = 0; i < obj.length; i++){
           		if(obj[i].value == zlyb){
           			$('#dateSection option[value='+obj[i].value+']').prop('selected', 'selected');
           		}
           	}
            $("#multiStep-82-").click();
        }

	})
    </script>
</head>
<body style="background-color: #eaebeb;">
<div style="float: left;width: 70px;">
	<nav class="cd-side-navigation">
		<ul id="myTab">
		</ul>
	</nav>
</div>
<div style="float: right;width:calc(100% - 70px);">
	<ol class="breadcrumb" id ="test" style="margin-left: -20px;height: 36px;margin-bottom:0px;line-height: 20px;border-radius: 0px;background-color: #eaebeb;">
		<li><a href="#" style="color: #3399ff;margin-left: 20px;" onclick="toProjectHome()">项目</a></li>
		<li class="active" id="tabsTitle" style="color: black;"></li>
		<li class="active" id="tabsTitleOne" style="color: black;display:none;"></li>
	</ol>
	<ul id="multiStep"  class="nav navbar-nav" style="font-size: 16px;margin-left: 10px;">
	</ul>
	<div id="dateSectionDiv" style="display: none;float: right;margin-right: 30px;margin-top: 5px;">
    </div>
	<div id="myiframePre" class="frame-content" style="float: right;width: 100%;">
		<iframe src="<%=request.getContextPath() %>/view/HTML/gaikuang.html" width="100%" id="myiframe" onload="changeFrameHeight()" frameborder="0"></iframe>
	</div>
</div>
</body>
</html>