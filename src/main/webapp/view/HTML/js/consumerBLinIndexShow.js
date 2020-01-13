var myChart;
$(function(){
	var projectState = "在行";
    var bsTable;
    var mode1 = "popup"//编辑框的模式：支持popup和inline两种模式，默认是popup
    
    function initPDUTable(){
        var columns = initPDUColumn();
        var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
            'projectState':projectState,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
        };
        var url = getRootPath() + '/projectReport/getCousumerPDUIndex';
        bsTable = new BSTable('mytable0', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(queryParams);
        bsTable.setPageSize(5);
        // bsTable.setEditable(true);//开启行内编辑
        bsTable.setEditable(false);
        
        bsTable.init();
	}
    
    function initProjectTable(){
        var columns = initProjectColumn();
        var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
            'projectState':projectState,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
        };
        var url = getRootPath() + '/projectReport/getCousumerQualityIndex';
        bsTable = new BSTable('mytable1', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(queryParams);
        bsTable.setPageSize(5);
        // bsTable.setEditable(true);//开启行内编辑
        bsTable.setEditable(false);
        
        bsTable.init();
	}
    
    
    
    function initPDUColumn(){
        return [
            {title:'项目编号 ',field:'no',width:80,visible: false},
            {
        		title : '序号',
        		align: "center",
        		width: 60,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytable0').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytable0').bootstrapTable('getOptions').pageNumber;
        		    var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath();
        		    var num = pageSize*(pageNumber-1)+index + 1;
        		    var tab = '<a target="_blank" style="color: #000";>' +
                                  '<div style="width: 100%;text-align: center;border-right: 2px dashed #3101f3;">' +
                                       '<sqan >'+ num +'</sqan>' +
                                  '</div>' +
                              '</a>';
        		    return tab;
        		}
            },
            {
            	title:'PDU',
            	halign :'center',
        		align : 'center',
        		field:'pduSpdt',
        		sortable:true,
        		width:150,
        		formatter:function(value,row,index){
                    var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
                    var pduSpdt = row.pdu_spdt==null?' ':(''+row.pdu_spdt);
                    var tab = '<a target="_blank" style="color: #000;;" title="'+value +'" href="'+path1+'">' +
                                '<div style="width: 100%;text-align: center;border-right: 2px dashed #3101f3;">' +
                                '<sqan >'+pduSpdt+'</sqan>' +
                                '</div>' +
                              '</a>';
                    return tab
                },
        	},
            {title:'无效版本数',field:'id528',width:110,
                formatter:function(value,row,index){
                	var vals = value== null? '0' :(''+ value);
                	deng = getDeng2(vals);
                    resultTatil = '<div style="width: 100%;text-align: left;">'+
					                 '<div style="width: 100%;">'+
						        		'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
						        		'<span style="float: right;">'+ deng +'</span>'+
						             '</div>'+
		                	        '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
		                		      '<span style="float: center;">'+'总数 ：'+'</span>'+
		                		      '<span>'+vals +'</span>'+
		                		    '</div>'+
		                	     '</div>';
                    return resultTatil;
                }
            },
            {title:'回归不通过问题单数',field:'id591',width:130,
            	 formatter:function(value,row,index){
            		 var vals = value== null? '0' :(''+ value);
                 	deng = getDeng2(vals);
                     resultTatil = '<div style="width: 100%;text-align: left;">'+
 					                 '<div style="width: 100%;">'+
 						        		'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
 						        		'<span style="float: right;">'+ deng +'</span>'+
 						             '</div>'+
 		                	        '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
 		                		      '<span style="float: center;">'+'总数 ：'+'</span>'+
 		                		      '<span>'+vals +'</span>'+
 		                		    '</div>'+
 		                	     '</div>';
                     return resultTatil;
              
                  }
            },
            {title:'质量事故个数',field:'id577',width:110,
            	 formatter:function(value,row,index){
            		 var vals = value== null? '0' :(''+ value);
                 	deng = getDeng2(vals);
                     resultTatil = '<div style="width: 100%;text-align: left;">'+
 					                 '<div style="width: 100%;">'+
 						        		'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
 						        		'<span style="float: right;">'+ deng +'</span>'+
 						             '</div>'+
 		                	        '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
 		                		      '<span style="float: center;">'+'总数 ：'+'</span>'+
 		                		      '<span>'+vals +'</span>'+
 		                		    '</div>'+
 		                	     '</div>';
                     return resultTatil;
                  }
            },
            {title:'质量预警个数',field:'id544',width:100,
            	 formatter:function(value,row,index){
            		 var vals = value== null? '0' :(''+ value);
                 	deng = getDeng2(vals);
                     resultTatil = '<div style="width: 100%;text-align: left;">'+
 					                 '<div style="width: 100%;">'+
 						        		'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
 						        		'<span style="float: right;">'+ deng +'</span>'+
 						             '</div>'+
 		                	        '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
 		                		      '<span style="float: center;">'+'总数 ：'+'</span>'+
 		                		      '<span>'+vals +'</span>'+
 		                		    '</div>'+
 		                	     '</div>';
                     return resultTatil;
                  }
            },
            {title:'严重网上问题个数',field:'id546',width:120,
            	 formatter:function(value,row,index){
            		 var vals = value== null? '0' :(''+ value);
                 	deng = getDeng2(vals);
                     resultTatil = '<div style="width: 100%;text-align: left;">'+
 					                 '<div style="width: 100%;">'+
 						        		'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
 						        		'<span style="float: right;">'+ deng +'</span>'+
 						             '</div>'+
 		                	        '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
 		                		      '<span style="float: center;">'+'总数 ：'+'</span>'+
 		                		      '<span>'+vals +'</span>'+
 		                		    '</div>'+
 		                	     '</div>';
                     return resultTatil;
                  }
            },
            {title:'生产批量问题',field:'id545',width:110,
            	 formatter:function(value,row,index){
            		 var vals = value== null? '0' :(''+ value);
                 	deng = getDeng2(vals);
                     resultTatil = '<div style="width: 100%;text-align: left;">'+
 					                 '<div style="width: 100%;">'+
 						        		'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
 						        		'<span style="float: right;">'+ deng +'</span>'+
 						             '</div>'+
 		                	        '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
 		                		      '<span style="float: center;">'+'总数 ：'+'</span>'+
 		                		      '<span>'+vals +'</span>'+
 		                		    '</div>'+
 		                	     '</div>';
                     return resultTatil;
                  }
            },
            {title:'交付后漏测问题',field:'id558',width:110,
            	 formatter:function(value,row,index){
            		 var vals = value== null? '0' :(''+ value);
                 	deng = getDeng2(vals);
                     resultTatil = '<div style="width: 100%;text-align: left;">'+
 					                 '<div style="width: 100%;">'+
 						        		'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
 						        		'<span style="float: right;">'+ deng +'</span>'+
 						             '</div>'+
 		                	        '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
 		                		      '<span style="float: center;">'+'总数 ：'+'</span>'+
 		                		      '<span>'+vals +'</span>'+
 		                		    '</div>'+
 		                	     '</div>';
                     return resultTatil;
                  }
            },
            {title:'低质量、进度偏差严重，导致华为方的投诉',field:'id550',width:110,
            	 formatter:function(value,row,index){
            		 var vals = value== null? '0' :(''+ value);
                 	 deng = getDeng2(vals);
                     resultTatil = '<div style="width: 100%;text-align: left;">'+
 					                 '<div style="width: 100%;">'+
 						        		'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
 						        		'<span style="float: right;">'+ deng +'</span>'+
 						             '</div>'+
 		                	        '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
 		                		      '<span style="float: center;">'+'总数 ：'+'</span>'+
 		                		      '<span>'+vals +'</span>'+
 		                		    '</div>'+
 		                	     '</div>';
                     return resultTatil;
                  }
            },
        ]
    };
    
    function initProjectColumn(){
        return [
            {title:'项目编号 ',field:'no',width:80,visible: false},
            {
        		title : '序号',
        		align: "center",
        		width: 60,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytable1').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytable1').bootstrapTable('getOptions').pageNumber;
        		    var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath();
        		    var num = pageSize*(pageNumber-1)+index + 1;
        		    var tab = '<a target="_blank" style="color: #000";>' +
                                  '<div style="width: 100%;text-align: center;border-right: 2px dashed #3101f3;">' +
                                       '<sqan >'+ num +'</sqan>' +
                                  '</div>' +
                              '</a>';
        		    return tab;
        		}
            },
            {
            	title:'PDU',
            	halign :'center',
        		align : 'center',
        		field:'pduSpdt',
        		sortable:true,
        		width:150,
        		formatter:function(value,row,index){
                    var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
                    var pduSpdt = row.pdu_spdt==null?' ':(''+row.pdu_spdt);
                    var tab = '<a target="_blank" style="color: #000;;" title="'+value +'" href="'+path1+'">' +
                                '<div style="width: 100%;text-align: center;border-right: 2px dashed #3101f3;">' +
                                '<sqan >'+pduSpdt+'</sqan>' +
                                '</div>' +
                              '</a>';
                    return tab
                },
        	},
            {title:'项目名称',field:'name',width:300,
                formatter:function(value,row,index){
                    var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no;
                    var tab = '<a target="_blank" style="color: #000;;" title="'+value +'" href="'+path1+'">' +
                                '<div style="width: 100%;text-align: left;border-right: 2px dashed #3101f3;">' +
                                '<label style="font-size: 16px;font-weight: unset;">' +
                                    ''+value+ '-'+row.area+
                                '</label>' +
                                '<div style="font-size: 12px;color: #a3a3a3;">' +
                                    '<div style="width: 90%;">' +
                                        '<sqan>PM: '+row.pm+'</sqan>' +
                                        '<sqan style="float: right;">结项时间:'+new Date(row.end_date).format('yyyy-MM-dd')+'</sqan>' +
                                    '</div>' +
                                    '<div style="width: 90%;">' +
                                        '<sqan>'+row.hwzpdu +'</sqan>' +
                                        '<sqan style="float: right;">'+row.area+'</sqan>' +
                                    '</div>' +
                                '</div>' +
                             '</div>' +
                            '</a>';
                    return tab
                },
            },
            {title:'无效版本数',field:'id528',width:110,
                formatter:function(value,row,index){
                	if(value =="--"){
                		var val = 0 ;
                		var vals = value ;
                	}else{
                		var val = value.measureValue==null?0:value.measureValue;
                		if(val<0){
                			val = 0 ;
                		}
                		var vals = val;
                	}
                    var val3 = value.upper;
                    var val4 = value.lower;
                	if(val3==null || val3 < 0||val3==""){
                		val3=0;
                	}
                	if( val4==null || val4 < 0||val4==""){
                		val4=0;
                	}
                	 deng =  getDeng(vals,val3,val4);
                    deng = '<div style="width: 100%;text-align: left;">'+
			                    '<div style="width: 100%;">'+
			        			'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
			        			'<span style="float: right;">'+deng+'</span>'+
			        		    '</div>'+
		                	    '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
		                		 '<span style="float: center;">'+'实际值：'+'</span>'+
		                		  '<span>'+vals+'</span>'+
		                		'</div>'+
		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
		                			'<div style="width: 50%;float: left;text-align: center">'+
		                				'<div>'+'上限值'+'</div>'+
		                				'<div>'+val3+'</div>'+
		                			'</div>'+
		                			'<div style="width: 50%;float: right;">'+
		                				'<div style="float: right;margin-right: 5px;text-align: center">'+
		                					'<div>'+'下限值'+'</div>'+
		                					'<div>'+val4+'</div>'+
		                				'</div>'+
		                			'</div>'+
		                		'</div>'+
		                	'</div>';
                    return deng;
                }
            },
            {title:'回归不通过问题单数',field:'id591',width:130,
            	  formatter:function(value,row,index){
                  	if(value =="--"){
                  		var val = 0 ;
                  		var vals = value ;
                  	}else{
                  		var val = value.measureValue==null?0:value.measureValue;
                  		if(val<0){
                  			val = 0 ;
                  		}
                  		var vals = val;
                  	}
                      var val3 = value.upper;
                      var val4 = value.lower;
                      if(val3==null || val3 < 0||val3==""){
                  		val3=0;
                  	}
                  	if( val4==null || val4 < 0||val4==""){
                  		val4=0;
                  	}
                  	 deng =  getDeng(vals,val3,val4);
                      deng = '<div style="width: 100%;text-align: left;">'+
  			                    '<div style="width: 100%;">'+
  			        			'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
  			        			'<span style="float: right;">'+deng+'</span>'+
  			        		    '</div>'+
  		                	    '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
  		                		 '<span style="float: center;">'+'实际值：'+'</span>'+
  		                		  '<span>'+vals+'</span>'+
  		                		'</div>'+
  		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
  		                			'<div style="width: 50%;float: left;text-align: center">'+
  		                				'<div>'+'上限值'+'</div>'+
  		                				'<div>'+val3+'</div>'+
  		                			'</div>'+
  		                			'<div style="width: 50%;float: right;">'+
  		                				'<div style="float: right;margin-right: 5px;text-align: center">'+
  		                					'<div>'+'下限值'+'</div>'+
  		                					'<div>'+val4+'</div>'+
  		                				'</div>'+
  		                			'</div>'+
  		                		'</div>'+
  		                	'</div>';
                      return deng;
                  }
            },
            {title:'质量事故个数',field:'id577',width:110,
            	  formatter:function(value,row,index){
                  	if(value =="--"){
                  		var val = 0 ;
                  		var vals = value ;
                  	}else{
                  		var val = value.measureValue==null?0:value.measureValue;
                  		if(val<0){
                  			val = 0 ;
                  		}
                  		var vals = val;
                  	}
                      var val3 = value.upper;
                      var val4 = value.lower;
                      if(val3==null || val3 < 0||val3==""){
                  		val3=0;
                  	}
                  	if( val4==null || val4 < 0||val4==""){
                  		val4=0;
                  	}
                  	 deng =  getDeng(vals,val3,val4);
                      deng = '<div style="width: 100%;text-align: left;">'+
  			                    '<div style="width: 100%;">'+
  			        			'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
  			        			'<span style="float: right;">'+deng+'</span>'+
  			        		    '</div>'+
  		                	    '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
  		                		 '<span style="float: center;">'+'实际值：'+'</span>'+
  		                		  '<span>'+vals+'</span>'+
  		                		'</div>'+
  		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
  		                			'<div style="width: 50%;float: left;text-align: center">'+
  		                				'<div>'+'上限值'+'</div>'+
  		                				'<div>'+val3+'</div>'+
  		                			'</div>'+
  		                			'<div style="width: 50%;float: right;">'+
  		                				'<div style="float: right;margin-right: 5px;text-align: center">'+
  		                					'<div>'+'下限值'+'</div>'+
  		                					'<div>'+val4+'</div>'+
  		                				'</div>'+
  		                			'</div>'+
  		                		'</div>'+
  		                	'</div>';
                      return deng;
                  }
            },
            {title:'质量预警个数',field:'id544',width:100,
            	  formatter:function(value,row,index){
                  	if(value =="--"){
                  		var val = 0 ;
                  		var vals = value ;
                  	}else{
                  		var val = value.measureValue==null?0:value.measureValue;
                  		if(val<0){
                  			val = 0 ;
                  		}
                  		var vals = val;
                  	}
                      var val3 = value.upper;
                      var val4 = value.lower;
                  	if(val3 < 0||val3==""||val3==null){
                  		val3=0;
                  	}
                  	if(val4 < 0||val4==""||val4==null){
                  		val4=0;
                  	}
                  	 deng =  getDeng(vals,val3,val4);
                      deng = '<div style="width: 100%;text-align: left;">'+
  			                    '<div style="width: 100%;">'+
  			        			'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
  			        			'<span style="float: right;">'+deng+'</span>'+
  			        		    '</div>'+
  		                	    '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
  		                		 '<span style="float: center;">'+'实际值：'+'</span>'+
  		                		  '<span>'+vals+'</span>'+
  		                		'</div>'+
  		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
  		                			'<div style="width: 50%;float: left;text-align: center">'+
  		                				'<div>'+'上限值'+'</div>'+
  		                				'<div>'+val3+'</div>'+
  		                			'</div>'+
  		                			'<div style="width: 50%;float: right;">'+
  		                				'<div style="float: right;margin-right: 5px;text-align: center">'+
  		                					'<div>'+'下限值'+'</div>'+
  		                					'<div>'+val4+'</div>'+
  		                				'</div>'+
  		                			'</div>'+
  		                		'</div>'+
  		                	'</div>';
                      return deng;
                  }
            },
            {title:'严重网上问题个数',field:'id546',width:120,
            	  formatter:function(value,row,index){
                  	if(value =="--"){
                  		var val = 0 ;
                  		var vals = value ;
                  	}else{
                  		var val = value.measureValue==null?0:value.measureValue;
                  		if(val<0){
                  			val = 0 ;
                  		}
                  		var vals = val;
                  	}
                      var val3 = value.upper;
                      var val4 = value.lower;
                  	if(val3 < 0||val3==""||val3==null){
                  		val3=0;
                  	}
                  	if(val4 < 0||val4==""||val4==null){
                  		val4=0;
                  	}
                  	 deng =  getDeng(vals,val3,val4);
                      deng = '<div style="width: 100%;text-align: left;">'+
  			                    '<div style="width: 100%;">'+
  			        			'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
  			        			'<span style="float: right;">'+deng+'</span>'+
  			        		    '</div>'+
  		                	    '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
  		                		 '<span style="float: center;">'+'实际值：'+'</span>'+
  		                		  '<span>'+vals+'</span>'+
  		                		'</div>'+
  		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
  		                			'<div style="width: 50%;float: left;text-align: center">'+
  		                				'<div>'+'上限值'+'</div>'+
  		                				'<div>'+val3+'</div>'+
  		                			'</div>'+
  		                			'<div style="width: 50%;float: right;">'+
  		                				'<div style="float: right;margin-right: 5px;text-align: center">'+
  		                					'<div>'+'下限值'+'</div>'+
  		                					'<div>'+val4+'</div>'+
  		                				'</div>'+
  		                			'</div>'+
  		                		'</div>'+
  		                	'</div>';
                      return deng;
                  }
            },
            {title:'生产批量问题',field:'id545',width:110,
            	  formatter:function(value,row,index){
                  	if(value =="--"){
                  		var val = 0 ;
                  		var vals = value ;
                  	}else{
                  		var val = value.measureValue==null?0:value.measureValue;
                  		if(val<0){
                  			val = 0 ;
                  		}
                  		var vals = val;
                  	}
                      var val3 = value.upper;
                      var val4 = value.lower;
                  	if(val3 < 0||val3==""||val3==null){
                  		val3=0;
                  	}
                  	if(val4 < 0||val4==""||val4==null){
                  		val4=0;
                  	}
                  	 deng =  getDeng(vals,val3,val4);
                      deng = '<div style="width: 100%;text-align: left;">'+
  			                    '<div style="width: 100%;">'+
  			        			'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
  			        			'<span style="float: right;">'+deng+'</span>'+
  			        		    '</div>'+
  		                	    '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
  		                		 '<span style="float: center;">'+'实际值：'+'</span>'+
  		                		  '<span>'+vals+'</span>'+
  		                		'</div>'+
  		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
  		                			'<div style="width: 50%;float: left;text-align: center">'+
  		                				'<div>'+'上限值'+'</div>'+
  		                				'<div>'+val3+'</div>'+
  		                			'</div>'+
  		                			'<div style="width: 50%;float: right;">'+
  		                				'<div style="float: right;margin-right: 5px;text-align: center">'+
  		                					'<div>'+'下限值'+'</div>'+
  		                					'<div>'+val4+'</div>'+
  		                				'</div>'+
  		                			'</div>'+
  		                		'</div>'+
  		                	'</div>';
                      return deng;
                  }
            },
            {title:'交付后漏测问题',field:'id558',width:110,
            	  formatter:function(value,row,index){
                  	if(value =="--"){
                  		var val = 0 ;
                  		var vals = value ;
                  	}else{
                  		var val = value.measureValue==null?0:value.measureValue;
                  		if(val<0){
                  			val = 0 ;
                  		}
                  		var vals = val;
                  	}
                      var val3 = value.upper;
                      var val4 = value.lower;
                  	if(val3 < 0||val3==""||val3==null){
                  		val3=0;
                  	}
                  	if(val4 < 0||val4==""||val4==null){
                  		val4=0;
                  	}
                  	 deng =  getDeng(vals,val3,val4);
                      deng = '<div style="width: 100%;text-align: left;">'+
  			                    '<div style="width: 100%;">'+
  			        			'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
  			        			'<span style="float: right;">'+deng+'</span>'+
  			        		    '</div>'+
  		                	    '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
  		                		 '<span style="float: center;">'+'实际值：'+'</span>'+
  		                		  '<span>'+vals+'</span>'+
  		                		'</div>'+
  		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
  		                			'<div style="width: 50%;float: left;text-align: center">'+
  		                				'<div>'+'上限值'+'</div>'+
  		                				'<div>'+val3+'</div>'+
  		                			'</div>'+
  		                			'<div style="width: 50%;float: right;">'+
  		                				'<div style="float: right;margin-right: 5px;text-align: center">'+
  		                					'<div>'+'下限值'+'</div>'+
  		                					'<div>'+val4+'</div>'+
  		                				'</div>'+
  		                			'</div>'+
  		                		'</div>'+
  		                	'</div>';
                      return deng;
                  }
            },
            {title:'低质量、进度偏差严重，导致华为方的投诉',field:'id550',width:110,
            	  formatter:function(value,row,index){
                  	if(value =="--"){
                  		var val = 0 ;
                  		var vals = value ;
                  	}else{
                  		var val = value.measureValue==null?0:value.measureValue;
                  		if(val<0){
                  			val = 0 ;
                  		}
                  		var vals = val;
                  	}
                      var val3 = value.upper;
                      var val4 = value.lower;
                  	if(val3 < 0||val3==""||val3==null){
                  		val3=0;
                  	}
                  	if(val4 < 0||val4==""||val4==null){
                  		val4=0;
                  	}
                  	 deng =  getDeng(vals,val3,val4);
                      deng = '<div style="width: 100%;text-align: left;">'+
  			                    '<div style="width: 100%;">'+
  			        			'<span style="font-size: 12px;">'+'红绿灯类型'+'</span>'+
  			        			'<span style="float: right;">'+deng+'</span>'+
  			        		    '</div>'+
  		                	    '<div style="width: 100%;font-size: 15px; text-align: center; padding-top: 5px;padding-bottom: 10px;border-right: 2px dashed #3101f3;">'+
  		                		 '<span style="float: center;">'+'实际值：'+'</span>'+
  		                		  '<span>'+vals+'</span>'+
  		                		'</div>'+
  		                		'<div style="font-size: 12px;line-height:15px;height: 24px;">'+
  		                			'<div style="width: 50%;float: left;text-align: center">'+
  		                				'<div>'+'上限值'+'</div>'+
  		                				'<div>'+val3+'</div>'+
  		                			'</div>'+
  		                			'<div style="width: 50%;float: right;">'+
  		                				'<div style="float: right;margin-right: 5px;text-align: center">'+
  		                					'<div>'+'下限值'+'</div>'+
  		                					'<div>'+val4+'</div>'+
  		                				'</div>'+
  		                			'</div>'+
  		                		'</div>'+
  		                	'</div>';
                      return deng;
                  }
            },
        ]
    };

	$.fn.loadzbTop=function(){
        var queryParams = {
            'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
            'projectState':projectState,
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
        };
        bsTable.setQueryParams(queryParams);
        bsTable.refresh();
	}
    
    function exportAssess(){
		$("#exportAssess").click(function(){
			var $eleForm = $("<form method='POST'></form>");
			var areaVal=$("#usertype1").selectpicker("val")==null?"":$("#usertype1").selectpicker("val").join();
			var hwpduVal=$("#usertype3").selectpicker("val")==null?"":$("#usertype3").selectpicker("val").join();
			var hwzpduVal=$("#usertype4").selectpicker("val")==null?"":$("#usertype4").selectpicker("val").join();
			var pduSpdtVal=$("#usertype5").selectpicker("val")==null?"":$("#usertype5").selectpicker("val").join();
			var buVal=$("#usertype6").selectpicker("val")==null?"":$("#usertype6").selectpicker("val").join();
			var pduVal=$("#usertype7").selectpicker("val")==null?"":$("#usertype7").selectpicker("val").join();
			var duVal=$("#usertype8").selectpicker("val")==null?"":$("#usertype8").selectpicker("val").join();
            var clientType=$("#selectBig").selectpicker("val")=="2"?"0":"1";
			$eleForm.append($('<input type="hidden" name="area" value="'+ areaVal +'">'));
			$eleForm.append($('<input type="hidden" name="projectState" value="'+ encodeURI(projectState) +'">'));
            $eleForm.append($('<input type="hidden" name="clientType" value="'+ clientType +'">'));
			$eleForm.append($('<input type="hidden" name="hwpdu" value="'+ hwpduVal +'">'));
			$eleForm.append($('<input type="hidden" name="hwzpdu" value="'+ hwzpduVal +'">'));
			$eleForm.append($('<input type="hidden" name="pduSpdt" value="'+ pduSpdtVal +'">'));
			$eleForm.append($('<input type="hidden" name="bu" value="'+ buVal +'">'));
			$eleForm.append($('<input type="hidden" name="pdu" value="'+ pduVal +'">'));
			$eleForm.append($('<input type="hidden" name="du" value="'+ duVal +'">'));
            $eleForm.attr("action",getRootPath() + "/projectReport/exportAssess");
            $(document.body).append($eleForm);
            //提交表单，实现下载
            $eleForm.submit();
		})
	}
    
    function getDeng(vals,val3,val4){
        if(parseFloat(vals) >= parseFloat(val4) && parseFloat(vals) <= parseFloat(val3)){
        	//绿灯
            deng = '<div style="display: block;float: right;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #1adc21; "></div>';
        }else {
        	//红灯
            deng ='<div style=" display: block;float: right;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #f57454; "></div>';
        }
        return deng;
    }
    
    function getDeng2(vals){
        if(vals==0){
        	//绿灯
            deng = '<div style="display: block;float: right;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #1adc21; "></div>';
        	
        }else {
        	//红灯
            deng ='<div style=" display: block;float: right;margin-right: 5px;border-radius: 50%;width: 15px;height:15px;'+
                'background-color: #f57454; "></div>';
        }
        return deng;
    }
    
    $(document).ready(function(){
    	initPage("");

    })
    
    $(document).on("click", ".weekSelection", function () {
    	$(".weekSelection").removeClass("tab_active");
    	_.each($(".weekSelection"), function(tab, index){
    		$("#"+$(tab).attr("name")).css('display','none');
    	});
    	$(this).addClass("tab_active");
    	var id = $(this).attr("name");
    	initPage(id);
    	$("#"+id).css('display','block');
    }); 
    
    function initPage(index){
    	if(index == 'projectIndex'){
    		initProjectTable();
    	}else{
    		initPDUTable();
    	}
    }

});

function selectOnchange(){
	$().loadzbTop();
}


