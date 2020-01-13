(function(){
	function initMaturityAssessment() {//默认加载361成熟度
		$.ajax({
			url: getRootPath() + '/project/initMaturityAssessment',
			type: 'post',
			async: false,//是否异步，true为异步
			success: function (data) {
				$("#sumPros").text(data.sumPros);
				$("#redPros").text(data.redPros);
				$("#yellowPros").text(data.yellowPros);
				$("#greenPros").text(data.greenPros);
				$("#noPros").text(data.noPros);
			}
		});
	};	
	
	$(document).ready(function(){
		initMaturityAssessment();
	})
	
})()