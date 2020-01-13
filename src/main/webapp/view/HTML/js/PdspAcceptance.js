$(function(){
	var projNo = window.parent.projNo;
	$.fn.loadIndex=function(){
		//开发结项验收
		$.ajax({
			url: getRootPath() + '/IndexList/getDevelopPdspIterative',
			type: 'post',
			async: false,//是否异步，true为异步
			data: {
				'proNo' : projNo
			},
			success: function (data) {
				$("#developPdspIterative thead tr").not(':eq(0)').remove();
				$("#developPdspIterative tbody tr").remove();
				var tab = "";
				var deng = "";
				var upper = "<tr><td>上限值</td>";
				var lower = "<tr><td>下限值</td>";
				var target = "<tr><td>目标值</td>";
				var id = "";
				_.each(data.data.parameters,function(lists,index){
					upper += "<td>"+lists.UPPER+"</td>";
					lower += "<td>"+lists.LOWER+"</td>";
					target += "<td>"+lists.TARGET+"</td>";
				});
				var id = "";
				var months = data.data.month;
				_.each(months,function(month,index1){
					tab += "<tr>";
					id = "data.data.month"+month;
					if(!eval(id)){
						return false;
					}
					tab += "<td>"+eval(id+".month")+"</td>";
					_.each(data.data.parameters,function(lists,index){
						id = "data.data.month"+month+".num"+lists.ID;
						var value = eval(id);
						if(lists.UNIT=="%"){
							value = value*100;
							value = value.toFixed(2);
							value += lists.UNIT;
						}else {
							value = value.toFixed(2);
						}
						tab += '<td>'+ value +'</td>';
					});
					tab +="</tr>";
				});
				
				tab += "<tr><td>合计</td>";
				_.each(data.data.parameters,function(lists,index){
					var value = 0;
					_.each(months,function(month,index1){
						id = "data.data.month"+month;
						if(!eval(id)){
							return false;
						}
						id = "data.data.month"+month+".num"+lists.ID;
						var value1 = eval(id);
						value = value + value1;
					});
					if(lists.UNIT=="%"){
						value = value*100;
						value = value.toFixed(2)
						value += lists.UNIT;
					}else{
						value = value.toFixed(2)
					}
					tab += '<td>'+ value +'</td>';
				});
				tab +="</tr>";
				
				
				upper += "</tr>";
				lower += "</tr>";
				target += "</tr>";
				target = upper + lower + target;
				$("#developPdspIterative thead").append(target);
				$("#developPdspIterative tbody").append(tab);
			}
		});
		//开发遗留验收
		$.ajax({
			url: getRootPath() + '/IndexList/getDevelopLeaveIterative',
			type: 'post',
			async: false,//是否异步，true为异步
			data: {
				'proNo' : projNo
			},
			success: function (data) {
				$("#developLeaveIterative thead tr").not(':eq(0)').remove();
				$("#developLeaveIterative tbody tr").remove();
				var tab = "";
				var deng = "";
				var upper = "<tr><td>上限值</td>";
				var lower = "<tr><td>下限值</td>";
				var target = "<tr><td>目标值</td>";
				var id = "";
				_.each(data.data.parameters,function(lists,index){
					upper += "<td>"+lists.UPPER+"</td>";
					lower += "<td>"+lists.LOWER+"</td>";
					target += "<td>"+lists.TARGET+"</td>";
				});
				var id = "";
				var months = data.data.month;
				_.each(months,function(month,index1){
					tab += "<tr>";
					id = "data.data.month"+month;
					if(!eval(id)){
						return false;
					}
					tab += "<td>"+eval(id+".month")+"</td>";
					_.each(data.data.parameters,function(lists,index){
						id = "data.data.month"+month+".num"+lists.ID;
						var value = eval(id);
						if(lists.UNIT=="%"){
							value = value*100;
							value = value.toFixed(2);
							value += lists.UNIT;
						}else {
							value = value.toFixed(2);
						}
						tab += '<td>'+ value +'</td>';
					});
					tab +="</tr>";
				});
				
				tab += "<tr><td>合计</td>";
				_.each(data.data.parameters,function(lists,index){
					var value = 0;
					_.each(months,function(month,index1){
						id = "data.data.month"+month;
						if(!eval(id)){
							return false;
						}
						id = "data.data.month"+month+".num"+lists.ID;
						var value1 = eval(id);
						value = value + value1;
					});
					if(lists.UNIT=="%"){
						value = value*100;
						value = value.toFixed(2)
						value += lists.UNIT;
					}else{
						value = value.toFixed(2)
					}
					tab += '<td>'+ value +'</td>';
				});
				tab +="</tr>";
				
				
				upper += "</tr>";
				lower += "</tr>";
				target += "</tr>";
				target = upper + lower + target;
				$("#developLeaveIterative thead").append(target);
				$("#developLeaveIterative tbody").append(tab);
			}
		});
		//测试结项验收
		$.ajax({
			url: getRootPath() + '/IndexList/getTestPdspIteration',
			type: 'post',
			async: false,//是否异步，true为异步
			data: {
				'proNo' : projNo
			},
			success: function (data) {
				$("#testPdspIteration thead tr").not(':eq(0)').remove();
				$("#testPdspIteration tbody tr").remove();
				var tab = "";
				var deng = "";
				var upper = "<tr><td>上限值</td>";
				var lower = "<tr><td>下限值</td>";
				var target = "<tr><td>目标值</td>";
				var id = "";
				_.each(data.data.parameters,function(lists,index){
					upper += "<td>"+lists.UPPER+"</td>";
					lower += "<td>"+lists.LOWER+"</td>";
					target += "<td>"+lists.TARGET+"</td>";
				});
				var id = "";
				var months = data.data.month;
				_.each(months,function(month,index1){
					tab += "<tr>";
					id = "data.data.month"+month;
					if(!eval(id)){
						return false;
					}
					tab += "<td>"+eval(id+".month")+"</td>";
					_.each(data.data.parameters,function(lists,index){
						id = "data.data.month"+month+".num"+lists.ID;
						var value = eval(id);
						if(lists.UNIT=="%"){
							value = value*100;
							value = value.toFixed(2);
							value += lists.UNIT;
						}else {
							value = value.toFixed(2);
						}
						tab += '<td>'+ value +'</td>';
					});
					tab +="</tr>";
				});
				
				tab += "<tr><td>合计</td>";
				_.each(data.data.parameters,function(lists,index){
					var value = 0;
					_.each(months,function(month,index1){
						id = "data.data.month"+month;
						if(!eval(id)){
							return false;
						}
						id = "data.data.month"+month+".num"+lists.ID;
						var value1 = eval(id);
						value = value + value1;
					});
					if(lists.UNIT=="%"){
						value = value*100;
						value = value.toFixed(2)
						value += lists.UNIT;
					}else{
						value = value.toFixed(2)
					}
					tab += '<td>'+ value +'</td>';
				});
				tab +="</tr>";
				
				
				upper += "</tr>";
				lower += "</tr>";
				target += "</tr>";
				target = upper + lower + target;
				$("#testPdspIteration thead").append(target);
				$("#testPdspIteration tbody").append(tab);
			}
		});
	}
	$(document).ready(function(){
		$().loadIndex();
	})
})

