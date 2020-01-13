$(function(){
	
	var projNo = window.parent.projNo;
	
	$("#sortable1").sortable({
  		connectWith: "#sortable1",
	});
	$("#sortable2").sortable({
	  connectWith: "#sortable1, #sortable2",
	});
	
	$("#sortable1, #sortable2").disableSelection();
	
	$("#control").click(function(){
		var status=$("#sortable2").css("display");
		if(status=="none"){
			$("#sortable1").sortable('enable');
			$("#sortable1").addClass('stripe');
			$("#sortable2").css("display","block");
//			$("#control").css("float","left");
			$(".del").addClass('del_block')
		}else if(status=="block"){
			var sort="";
			var lis=$("#sortable1").children();
			for(var i=0;i<lis.length;i++){
				var id=lis[i].id;
				sort=sort+id+",";
			}
			$.ajax({
				url: getRootPath() + '/instrumentPanel/changeInstrumentPanel',
				type: 'post',
				data:{
					no : projNo,
					sort : sort
				},
				success: function (data) {
					
				},
			});
			$("#sortable1").sortable('disable');
			$("#sortable1").removeClass('stripe');
			$("#sortable2").css("display","none");
//			$("#control").css("float","right");
//			$("#control").css("margin-right","12px");
			$(".del").removeClass('del_block');
		}	
	})
	
	$(document).on("click", ".del", function () {
		var tis=$(this);
		var li=tis.parent();
		tis.parent().remove();
		$("#sortable2").append(li);
	});
	
	$( "#sortable2" ).on( "sortstart", function( event, ui ) {
		$(ui.placeholder).css('width', ui.item[0].style.width);
	} );

	$(document).ready(function(e) {
		$("#sortable1").sortable('disable');
	});
});