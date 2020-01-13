var projNo="";
$(function(){
	projNo = window.parent.projNo;
	
	function developmentProcess(){
		$(".wrap>div").click(function(){
			var tis=$(this);
			$(".wrap>div").attr("class","todo");
			tis.attr("class","finished");
            $("#myiframe").attr("src",tis.attr('myiframeName'));
		})
	}
	
	$(document).ready(function(){
		developmentProcess();
	})
	
})