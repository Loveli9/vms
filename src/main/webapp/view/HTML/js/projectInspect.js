var prohibitTitle = '<button id="prohibit" type="button" class="collection-background"></button>';
$(function(){
    $(document).ready(function(){
    	var Menus = getCookie('Menulist');
    	var flag = false;
		if (Menus.indexOf("-75-") != -1) {
			var tab2 ='<li tabname="tab-demandFinish" id="demandFinish" onclick="loadDemandFinish()" style="border: 0px;margin-left: 15px;"><a href="###">需求完成情况</a></li>';
			$("#quality").append(tab2);
			if(!flag){
				$("#demandFinish").click();
				flag = true;
			}
		}
    	if (Menus.indexOf("-76-") != -1) {
    		var tab3 ='<li tabname="tab-deliverables" id="deliverables" onclick="loadDeliverables()" style="border: 0px;margin-left: 15px;"><a href="###">交付物</a></li>';
    		$("#quality").append(tab3);
    		if(!flag){
    			$("#deliverables").click();
    			flag = true;
    		}
    	}
    	if (Menus.indexOf("-77-") != -1) {
    		var tab4 ='<li tabname="tab-leftOver" id="leftOver" onclick="loadLeftOver()" style="border: 0px;margin-left: 15px;"><a href="###">遗留DI</a></li>';
    		$("#quality").append(tab4);
    		if(!flag){
    			$("#leftOver").click();
    			flag = true;
    		}
    	}
    	if (Menus.indexOf("-78-") != -1) {
    		var tab4 ='<li tabname="tab-evaluation" id="evaluation" onclick="loadEvaluation()" style="border: 0px;margin-left: 15px;"><a href="###">验收评价</a></li>';
    		$("#quality").append(tab4);
    		if(!flag){
    			$("#evaluation").click();
    			flag = true;
    		}
    	}
    })
});
function loadDemandFinish(){
	resourceReport();
}
function loadDeliverables(){
	deliverablesTable();
}
function loadLeftOver(){
	leftOverTable();
}
function loadEvaluation(){
	evaluationTable();
}
