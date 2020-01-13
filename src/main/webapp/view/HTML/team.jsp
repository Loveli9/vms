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
	</style>

	<script type="text/javascript">
        var baselocation=' <%=request.getContextPath()%>';
        var projNo = getQueryString("projNo");
        function changeFrameHeight() {
            var ifm = document.getElementById("myiframe");
			ifm.height = document.documentElement.clientHeight;
        };

        window.onresize=function(){ changeFrameHeight();}
        $(function(){changeFrameHeight();});


        function changePageName(labId,labPath,name) {
            var ifm = document.getElementById("myiframe");
            ifm.src = baselocation+labPath;
        };

        function addMyTab(id,onclickVel,name,selected,icon){
			var tab = '<li><a id="myTab'+id+'" class="'+selected+'" onclick="'+onclickVel+'">' +
				'<div class="nevigate-icon"><span class="glyphicon '+icon+'" aria-hidden="true"></span></div>' +
				'<div>'+name+'</div>' +
				'</a></li>';
			$("#myTab").append(tab);
        }
        $(document).on('click', '.cd-side-navigation a', function(){
            $(".cd-side-navigation a").removeClass("selected");
            $(this).addClass("selected");
        });

        $(document).ready(function () {
            addMyTab("null","changePageName('null','/view/HTML/gaikuang_team.html','概况')","概况","selected","glyphicon-home");
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
	<div class="frame-content" style="float: right;width: 100%;">
		<iframe src="<%=request.getContextPath() %>/view/HTML/gaikuang_team.html" width="100%" id="myiframe" onload="changeFrameHeight()" frameborder="0"></iframe>
	</div>
</div>
</body>
</html>