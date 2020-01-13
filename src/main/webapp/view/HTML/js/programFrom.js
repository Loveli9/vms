var projNo = window.parent.projNo;
(function(){
	//projNo = getQueryString("projNo");
	var area="";
	var hwpdu="";
	var hwzpdu="";
	var pduSpdt="";
	var bu="";
	var pdu="";
	var tab="";
	var pagecount=0; //总页数
	var pagerow=5;	//每页10条默认
	var pagenum=1; //当前页
	var rowcount=0;	//总条数
	var chartsData ={   //总代码量
			january:0,
			february:0,
			march:0,
			april:0,
			may:0,
			june:0,
			july:0,
			august:0,
			september:0,
			october:0,
			november:0,
			december:0,
			sum:0
	};
	var tmssData ={    //实际工时(人天)
			january:0.0,
			february:0.0,
			march:0.0,
			april:0.0,
			may:0.0,
			june:0.0,
			july:0.0,
			august:0.0,
			september:0.0,
			october:0.0,
			november:0.0,
			december:0.0,
			sum:0.0
	};
	function searchByPj(){
			chartsData.january=0;
			chartsData.february=0;
			chartsData.march=0;
			chartsData.april=0;
			chartsData.may=0;
			chartsData.june=0;
			chartsData.july=0;
			chartsData.august=0;
			chartsData.september=0;
			chartsData.october=0;
			chartsData.november=0;
			chartsData.december=0;
			chartsData.sum=0;
			//初始化实际工时
			tmssData.january=0.0;
			tmssData.february=0.0;
			tmssData.march=0.0;
			tmssData.april=0.0;
			tmssData.may=0.0;
			tmssData.june=0.0;
			tmssData.july=0.0;
			tmssData.august=0.0;
			tmssData.september=0.0;
			tmssData.october=0.0;
			tmssData.november=0.0;
			tmssData.sum=0.0;
			var projNo = window.parent.projNo;
			getMonthlyManpower(projNo);
	};
	
//	function search(projNo){
//		$.ajax({
//				url: getRootPath() + '/svnTask/searchGeRen',
//				type: 'post',
//				async: false,
//				data: {
//					projNo: projNo,
//					type: '',//不分代码类型
//					codeType: 'all',//所有代码量
//					role:'all'
//				},
//				success: function (data) {
//					if(data!=null&&data!=""){
//						_.each(data, function(record, key){
//							chartsData.january+=record.january;
//							chartsData.february+=record.february;
//							chartsData.march+=record.march;
//							chartsData.april+=record.april;
//							chartsData.may+=record.may;
//							chartsData.june+=record.june;
//							chartsData.july+=record.july;
//							chartsData.august+=record.august;
//							chartsData.september+=record.september;
//							chartsData.october+=record.october;
//							chartsData.november+=record.november;
//							chartsData.december+=record.december;
//						})
//						chartsData.sum=chartsData.january
//						+chartsData.february
//						+chartsData.march
//						+chartsData.april
//						+chartsData.may
//						+chartsData.june
//						+chartsData.july
//						+chartsData.august
//						+chartsData.september
//						+chartsData.october
//						+chartsData.november
//						+chartsData.december;
//					}
//				}
//			});
//	};
	
	function getMonthlyManpower(proNo){
		$.ajax({
			url: getRootPath() + '/projectReport/list',
			type: 'post',
			async: false,
			data: {
				proNo: proNo,
				// actualFlag: '0'//实际工时（人天）
			},
			success: function (data) {
				for (var p in data){
                    tab += '<tbody>';
                    tab += '<tr>';
                    tab += '<td rowspan="' + data[p].length + '">' + p + '</td>'
                    var measures = data[p];
                    _.each(measures, function(obj, key){
                        tab += '<td style="padding-left: 5px; text-align: left">'+obj.name+'</td>';
                        if(obj.reportMap.JAN){
                            tmssData.january=obj.reportMap.JAN
                            tab += '<td>'+obj.reportMap.JAN+'</td>';
                        }else{
                            tmssData.january=0;
                            tab += '<td>'+''+'</td>';
                        }

                        if(obj.reportMap.FEB){
                            tmssData.february=obj.reportMap.FEB
                            tab += '<td>'+obj.reportMap.FEB+'</td>';
                        }else{
                            tmssData.february=0;
                            tab += '<td>'+''+'</td>';
                        }

                        if(obj.reportMap.MAR){
                            tmssData.march=obj.reportMap.MAR
                            tab += '<td>'+obj.reportMap.MAR+'</td>';
                        }else{
                            tmssData.march=0;
                            tab += '<td>'+''+'</td>';
                        }

                        if(obj.reportMap.APR){
                            tmssData.april=obj.reportMap.APR
                            tab += '<td>'+obj.reportMap.APR+'</td>';
                        }else{
                            tmssData.april=0;
                            tab += '<td>'+''+'</td>';
                        }
                        if(obj.reportMap.MAY){
                            tmssData.may=obj.reportMap.MAY
                            tab += '<td>'+obj.reportMap.MAY+'</td>';
                        }else{
                            tmssData.may=0;
                            tab += '<td>'+''+'</td>';
                        }
                        if(obj.reportMap.JUN){
                            tmssData.june=obj.reportMap.JUN
                            tab += '<td>'+obj.reportMap.JUN+'</td>';
                        }else{
                            tmssData.june=0;
                            tab += '<td>'+''+'</td>';
                        }
                        if(obj.reportMap.JUL){
                            tmssData.july=obj.reportMap.JUL
                            tab += '<td>'+obj.reportMap.JUL+'</td>';
                        }else{
                            tmssData.july=0;
                            tab += '<td>'+''+'</td>';
                        }
                        if(obj.reportMap.AUG){
                            tmssData.august=obj.reportMap.AUG
                            tab += '<td>'+obj.reportMap.AUG+'</td>';
                        }else{
                            tmssData.august=0;
                            tab += '<td>'+''+'</td>';
                        }
                        if(obj.reportMap.SEP){
                            tmssData.september=obj.reportMap.SEP
                            tab += '<td>'+obj.reportMap.SEP+'</td>';
                        }else{
                            tmssData.september=0;
                            tab += '<td>'+''+'</td>';
                        }
                        if(obj.reportMap.OCT){
                            tmssData.october=obj.reportMap.OCT
                            tab += '<td>'+obj.reportMap.OCT+'</td>';
                        }else{
                            tmssData.october=0;
                            tab += '<td>'+''+'</td>';
                        }
                        if(obj.reportMap.NOV){
                            tmssData.november=obj.reportMap.NOV
                            tab += '<td>'+obj.reportMap.NOV+'</td>';
                        }else{
                            tmssData.november=0;
                            tab += '<td>'+''+'</td>';
                        }
                        if(obj.reportMap.DEC){
                            tmssData.december=obj.reportMap.DEC
                            tab += '<td>'+obj.reportMap.DEC+'</td>';
                        }else{
                            tmssData.december=0;
                            tab += '<td>'+''+'</td>';
                        }
                        tab += '</tr>';
                    })
                    tab += '</tbody>';
				}
				if(data!=null&&data!=""){
                    $("#xiaolv").append(tab);

				}
			}
		});
	};
	
//	function menuChange(){
//		var areaName=$(".list .active .areaActive",parent.document).text();
//		if(''!=areaName&&undefined!=areaName){			
//			area=areaName; 
//		}
//		var depId=$(".crumbs .active",parent.document).attr("data-id");
//		
//		if(''!=depId&&undefined!=depId){		
//			if(1==depId){
//				var yijiId=$(".list .yiji>.active",parent.document).attr("data-id");
//				var erjiId=$(".list .erji>.active",parent.document).attr("data-id");
//				var sanjiId=$(".list .sanji>.active",parent.document).attr("data-id");				
//				if(''!=erjiId&&undefined!=erjiId){
//					bu=erjiId;
//					if(''!=sanjiId&&undefined!=sanjiId){
//						pdu=sanjiId;
//					}
//				}				
//			}else if(2==depId){
//				var yijiId=$(".list .yiji>.active",parent.document).attr("data-id");
//				var erjiId=$(".list .erji>.active",parent.document).attr("data-id");
//				var sanjiId=$(".list .sanji>.active",parent.document).attr("data-id");
//				if(''!=yijiId&&undefined!=yijiId){
//					hwpdu=yijiId;
//					if(''!=erjiId&&undefined!=erjiId){
//						hwzpdu=erjiId;
//						if(''!=sanjiId&&undefined!=sanjiId){
//							pduSpdt=sanjiId;
//						}
//					}
//				}		
//			}
//			searchByPj();
//		}
//	};
	
	$(document).ready(function(){
        // var projNo = window.parent.projNo;
        // getMonthlyManpower(projNo);
        searchByPj();
	})
})()
//刷新计算指标数据
function refreshMeasure(){
	$('#savetoop').modal('show');
	$.ajax({
		url : getRootPath()+'/monthMeasure/calculate',
		type : 'post',
		data : {
			proNo : projNo
		},
		success : function(data){
			$('#savetoop').modal('hide');
			if(data.code == 'success'){
				window.location.reload();
			}
		}
	});
}

