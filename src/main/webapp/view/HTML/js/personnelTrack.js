var prohibitTitle = '<button id="prohibit" type="button" class="collection-background"></button>';
$(function(){
	$(document).ready(function(){
		var Menus = getCookie('Menulist');
		var flag = false;
		if (Menus.indexOf("-79-") != -1) {
			var tab2 ='<li tabname="tab-actualMember" id="actualMember" onclick="loadActualMember()" style="border: 0px;margin-left: 15px;"><a href="###">整体评估</a></li>';
			$("#quality").append(tab2);
			if(!flag){
				$("#actualMember").click();
				flag = true;
			}
		}
		if (Menus.indexOf("-80-") != -1) {
			var tab4 ='<li tabname="tab-code" id="code" onclick="loadcode()" style="border: 0px;margin-left: 15px;"><a href="###">代码</a></li>';
			$("#quality").append(tab4);
			if(!flag){
				$("#code").click();
				flag = true;
			}
		}
		if (Menus.indexOf("-81-") != -1) {
			var tab4 ='<li tabname="tab-testCase" id="testCase" onclick="loadTestCase()" style="border: 0px;margin-left: 15px;"><a href="###">测试用例</a></li>';
			$("#quality").append(tab4);
			if(!flag){
				$("#testCase").click();
				flag = true;
			}
		}
		if (Menus.indexOf("-82-") != -1) {
			var tab4 ='<li tabname="tab-document" id="document" onclick="loadDocument()" style="border: 0px;margin-left: 15px;"><a href="###">文档</a></li>';
			$("#quality").append(tab4);
			if(!flag){
				$("#document").click();
				flag = true;
			}
		}
		if (Menus.indexOf("-83-") != -1) {
			var tab4 ='<li tabname="tab-attendance" id="attendance" onclick="loadAttendance()" style="border: 0px;margin-left: 15px;"><a href="###">出勤</a></li>';
			$("#quality").append(tab4);
			if(!flag){
				$("#attendance").click();
				flag = true;
			}
		}
		if (Menus.indexOf("-84-") != -1) {
			var tab4 ='<li tabname="tab-qualityProblem" id="qualityProblem" onclick="loadQualityProblem()" style="border: 0px;margin-left: 15px;"><a href="###">质量问题</a></li>';
			$("#quality").append(tab4);
			if(!flag){
				$("#qualityProblem").click();
				flag = true;
			}
		}
	})
});

//整体评估
function loadActualMember(){
	actualMemberTable();
}
//代码
function loadcode(){
    codeAndCaseTable('codeTable');
}

//出勤
function loadAttendance(){
	initCostMonth('project');
	initCostWeek('project');
	tabXmcb("project");
}

//测试用例
function loadTestCase(){
	codeAndCaseTable('testCaseTable');
}
//文档
function loadDocument(){
	codeAndCaseTable('documentTable');
}

//质量问题
function loadQualityProblem(){
	qualityProblemTable();
}
