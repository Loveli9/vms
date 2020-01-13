(function(){	
	//获取角色信息列表
	function getRoleInfo(){
		var projNo = window.parent.projNo;
		$.ajax({
			url: getRootPath() + '/projectReport/list',
			type: 'post',
			async: false,
			data: {
				proNo: proNo,
				// actualFlag: '0'//实际工时（人天）
			},
			success: function (data) {
				
			}
		});
	};
	
	$(document).ready(function(){
		getRoleInfo();
	})
})()

