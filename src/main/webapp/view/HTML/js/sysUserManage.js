var statusAssessment ="";
var statusAssess = "3";
var page = 0;
var checkboxs = [];
var collected = 0;
var whole = false;
var concernList = [];
var prohibitTitle = '<button id="prohibit" type="button" class="collection-background"></button>';
//ul导航栏的各触发函数
function loadOverviewReport(){
	$("#select1").hide();
	//$('#exportQMS').hide();
	$().loadzbTop();
}
function loadQualityReport(){
	$("#select1").hide();
	//$('#exportQMS').hide();
	$("#qualityReportTableDiv").css("display","");
	$("#qualityReport").css("display","block");
	$("#qualityReport li").removeClass("active");
	$("#qualityReport li[tabname='tab-dm']").addClass("active");
	initQualityReport();
	$("#reliableQualityTableDiv").css("display","none");
	$("#reviewQualityTableDiv").css("display","none");
	$("#processIndicatorsTableDiv").css("display","none");
	$("#concludingIndicatorsTableDiv").css("display","none");
	$("#exportKX").css("display","none");
}
function loadZrUser(){
	$("#select1").hide();
    zrsuerManage();
}
function loadHwUser() {
    $("#select1").hide();
    hwsuerManage();
}
function loadSysUser() {
	$("#select1").hide();
	sysuerManage();
}

function loadEfficiencyReport(){
	$("#select1").hide();
	//$('#exportQMS').hide();
	$("#efficiencyTitle").css("display","block");
	$("#efficiencyReport li").removeClass("active");
	$("#efficiencyReport li[tabname='tab-devtest']").addClass("active");
	initDevtestReport();
	$("#tab-devtest").css("display","block");
}
function problemAnalysis(){
	$("#select1").hide();
	qualityProblem();
}
function qmsReport(){
	qmsProblem();
	qmsDimension();
	$('#select1').show();
	$("#projectDimension").show();
	$('#exportQMS').show();
	$("#qmsProblem").hide();
	$("#qmsSelect").children().removeClass("active");
	$("#qmsSelect li[tabname='tab-wd']").addClass("active");
	qmsSector();
	qmsHistogram(null);
}
function loadNetSecReport(){
	$("#select1").hide();
	initNetSecTable();
}
function addConcernItems(proNo,val){	
	$.ajax({
		url: getRootPath() + '/projectOverview/addConcernItems',
		type: 'post',
		data:{
			proNo : proNo,
		},
		success: function (data) {
				if (data.status=='0'){
					var id = proNo+"Index"+val;
					$("#"+id)[0].outerHTML='<a href="javascript:void(0)" id="'+id+'" style="color: #721b77;" title="取消收藏" onclick="deleteConcernItems(\''+proNo+'\',\''+val+'\')"><img src="/mvp/view/HTML/images/collected.png"/></span></a>';
				}else{
					toastr.error('关注失败!');
				}
			}
	});
}
	
function deleteConcernItems(proNo,val){
	$.ajax({
		url: getRootPath() + '/projectOverview/deleteConcernItems',
		type: 'post',
		data:{
			proNo : proNo,
		},
		success: function (data) {
				if (data.status=='0'){
					var id = proNo+"Index"+val;
					if($("#"+id)[0]){
						$("#"+id)[0].outerHTML='<a href="javascript:void(0)" id="'+id+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+proNo+'\',\''+val+'\')"><img src="/mvp/view/HTML/images/collection.png"/></a>';
					}
				}else{
					toastr.error('取消关注失败!');
				}
			}
	});
}
function queryConcernItems(){
    $.ajax({
		url: getRootPath() + '/projectOverview/queryConcernItems',
		type: 'post',
		data:{
			'statisticalTime':$("#dateSection").val(),
		    'area': ($("#usertype1").selectpicker("val")==null || $('#select1').is(':hidden'))?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
		    'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
		    'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
		    'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
		    'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
		    'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
		    'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
		    'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
		},
		success: function(data){
			if (data.code=='0'){
				concernList = data.data;
				collectionProject(data.data);
				$("#tableProject").bootstrapTable('destroy');
				checkboxOnclickData(1);
			}else{
				toastr.error('收藏项目查询失败!');
			}
		}
    });
}
function saveConcernItems(){
    $.ajax({
		url: getRootPath() + '/projectOverview/saveConcernItems',
		type: 'post',
		data:{
			'nos': collectionNos(),
		},
		success: function(data){
			if (data.code=='success'){
				toastr.success('收藏项目成功！');
			}else{
				toastr.error('收藏项目失败!');
			}
		}
    });
}
function concernItems(no,name,pm,val){
	//0:添加;1:删除
	if(val==0){
		$("#"+no+"sign")[0].outerHTML='<a href="javascript:void(0)" id="'+no+"sign"+'" style="color: #721b77;" title="取消收藏" onclick="concernItems(\''+no+'\',\''+name+'\',\''+pm+'\',1)"><img src="/mvp/view/HTML/images/collected.png"/></span></a>';
		var data ={
				'no' : no,
	    		'name': name,
	    		'pm' : pm,
		};
		concernList.push(data);
	}else{
		for(var i=0;i<concernList.length;i++){
			if(no == concernList[i].no){
				concernList.splice(i,1);
				break;
			}
		}
		var id = no+"sign";
		if($("#"+id)[0]){
			$("#"+id)[0].outerHTML='<a href="javascript:void(0)" id="'+no+"sign"+'" style="color: #721b77;" title="收藏" onclick="concernItems(\''+no+'\',\''+name+'\',\''+pm+'\',0)"><img src="/mvp/view/HTML/images/collection.png"/></span></a>';
		}else{
			$('#tableProject').bootstrapTable('refresh');
		}
	}
	collectionProject(concernList);
}
function collectionProject(list){
	$("#follow").empty();
	var tab = '<div class="swiper-button-prev"></div>'+
	          '<div class="state-inner clearfix swiper-container">'+
	          '<div class="content clearfix swiper-wrapper">';
	var name = '';
	for(var i=0;i<list.length;i++){
		var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + list[i].no;
		if(list[i].name.length>15){
			name = '<a target="_blank" class="over-flow-ellipsis" title="'+list[i].name +'" href="'+path+'"><h6 style="height: 26px;" class="overstep">'+list[i].name+'</h6></a>';
		}else{
			name = '<a target="_blank" class="over-flow-ellipsis" title="'+list[i].name +'" href="'+path+'"><h5 style="height: 26px;">'+list[i].name+'</h5></a>'
		}
		tab += '<div class="swiper-slide"><div class="project-card home-page-project-card">'+
		'<div class="is-my-favorite-project">'+
		'<div class="project-card-header project-card-background-image color-1 card-scrum" tabindex="0" onclick="window.open(\''+path+'\')">'+
			'<div class="card-animation">'+		
       		'</div>'+
   		'</div>'+
		'<div  class="project-card-info">'+name+										  
	        '<div class="project-card-more-info over-flow-ellipsis" style="margin-top: -6px;">'+                   
	        	'<h4 class="glyphicon glyphicon-user over-flow-ellipsis"style="float: left;"  title="PM:'+list[i].pm +'">'+list[i].pm+'</h4>'+
	        	'<span style=" cursor: pointer;margin-left: 43%;" title="取消收藏" onclick="concernItems(\''+list[i].no+'\',\''+list[i].name+'\',\''+list[i].pm+'\',1)"><img src="/mvp/view/HTML/images/collected.png"/></span>'+
	        '</div>'+
    	'</div>'+
    '</div>'+
'</div></div>';
	}
	tab+='</div></div><div class="swiper-button-next"></div>';
	$("#follow").append(tab);
	var swiper = new Swiper('.swiper-container', {
		slidesPerView: 4,
		spaceBetween: 10,
     	slidesPerGroup:4,
	    loopFillGroupWithBlank: true,
	    direction: 'horizontal',
	    loop: false,
	    observer: true,//调完接口不能翻页解决方法，修改swiper自己或子元素时，自动初始化swiper
        observeParents: true,//调完接口不能翻页解决方法，修改swiper的父元素时，自动初始化swiper
		navigation: {
	      nextEl: '.swiper-button-next',
	      prevEl: '.swiper-button-prev',
	    },
	});	
}
$(function(){
    var bsTable;
    var mode1 = "popup"//编辑框的模式：支持popup和inline两种模式，默认是popup
    var statusSet=new Array();
    var dengStatus=new Array();
    //qms饼状图触发函数
    
    var qmsEcharts = document.getElementById("qmsSector");
    qmsEcharts.style.width=window.outerWidth*0.39+'px';
    document.getElementById("qmsHistogram").style.width=window.outerWidth*0.6+'px';
    echarts.init(qmsEcharts).on('click', function (params) { 
    	var name = params.data.name;
    	qmsHistogram(name);
    });
    
    if(parent.parents){
    	page = parent.parents;
		parent.parents = null;
	}
    
    function initTableSave(){
        var columns=initColumn();
        var myParams = {
        	'statisticalTime':$("#dateSection").val(),
            'area': ($("#usertype1").selectpicker("val")==null || $('#select1').is(':hidden'))?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
            'status': statusAssess,
            'coopType': collected,//0:全部项目;1：已收藏项目;2:未收藏项目
        };
        var url = getRootPath() + '/projectOverview/getProjectOverview';
        bsTable = new BSTable('mytable', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(myParams);
        bsTable.setPageSize(20);
        bsTable.setPageNumber(1);
        bsTable.setSortable(true);
        bsTable.setEditable(false);
        bsTable.init();
	}
    
    $(document).on("click","#exportZonglan",function(){
    	$('#deployName').html("指标信息导出");
    	$('.but2').hide();
    	$('#seniors').show();
    	$("#follow").hide();
    	$('#collected').hide();
    	$('#user_type2').selectpicker("val",["在行"]);
        var zrOrhwselect = getCookie("zrOrhwselect");
        $('#zrOrhwselect').val(zrOrhwselect);
        zrOrhwselectChenge();
		initArea();
		initHWCPX();
		initYeWuXian();
		$("#tableProject").bootstrapTable('destroy');
		checkboxOnclickData(0);
    	$("#iteAddPage").modal('show');
    });
    
    function initColumn(){
        return [
            {title:'项目编号 ',field:'no',width:80,visible: false},
            {
        		title:'全</br>选',
        		field:'status',
        		align : "center",
        		width : 34,
        		formatter: function (value,row,index){
        			if(checkboxsContain(row.no,1) != null){
        				return "<input type='checkbox' onclick='checkboxOnclick(\""+row.no+"\",this)' name='zong' checked='checked'/>";
        			}else{
        				return "<input type='checkbox' onclick='checkboxOnclick(\""+row.no+"\",this)' name='zong'/>";
        			}
				}
        	},
            {
        		title : '序</br>号',
        		align: "center",
        		width: 45,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytable').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytable').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
            },
            {title:'项目名称',field:'name',width:150,sortable:true,
            	formatter:function(val, row){
					  var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no+ '&zlyb='+$("#dateSection").val()+'';
					  return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path1+'">'+val+'</a>';
				}
            },
            {title:'PM',field:'pm',width:60,sortable:true},
            {title:'QA',field:'qa',width:60,sortable:true},
            {title:'部门',field:'department',width:100,sortable:true,
            	formatter:function(value,row,index){
					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
				}
            },
            {title:'类型',field:'projectType',align: 'left',width:48,sortable:true},
            {title:'里程点',field:'mileagePoint',width:70,
            	formatter:function(value,row,index){
            		progress = getProgress(value);
            		overdue = getOverdue(row.overdueDate);
            		progress =  '<div style="width: 100%;height: 12px;margin-top: 7%;" class="progress">'+
		                			progress +
		                		'</div>'+
		                		overdue;
                    return progress;
                }
            },
            {title:'质</br>量',field:'qualityLamp',width:41,align: 'left',valign:'middle',sortable:true,
            	formatter:function(value,row,index){
            		var deng = getDeng(getLamp('quality',value).lamp);
                    return '<div style="width: 100%;" onclick="measureCommentModalShow(\''+row.no+'\')">'+'<span>'+deng+'</span>'+'</div>';
                }
            },
            {title:'资</br>源',field:'keyRoles',width:41,align: 'left',valign:'middle',sortable:true,
            	formatter:function(value,row,index){
            		var deng = getDeng(getLamp('resources',value).lamp);
                    return '<div style="width: 100%;">'+'<span>'+deng+'</span>'+'</div>';
                }
            },
            {title:'进</br>度',field:'planLamp',width:41,align: 'left',valign:'middle',sortable:true,
            	formatter:function(value,row,index){
            		var deng = getDeng(getLamp('progress',value).lamp);
                    return '<div style="width: 100%;">'+'<span>'+deng+'</span>'+'</div>';
                }
            },
            {title:'变</br>更',field:'scopeLamp',width:41,align: 'left',valign:'middle',sortable:true,
            	formatter:function(value,row,index){
            		var deng = getDeng(getLamp('changes',value).lamp);
                    return '<div style="width: 100%;">'+'<span>'+deng+'</span>'+'</div>';
                }
            },
            {title:'统计</br>周期',field:'statisticalCycle',align: 'center',width:58,sortable:true,
            	formatter:function(value,row,index){
	                return getStatisticalCycle(value);
            	}
            },
           {title:'近五次</br>状态评估',field:'fiveStatusAssessment',width:90,align: 'center',valign:'middle',
        	   formatter:function(value,row,index){
	        	   var proNo = row.no;
	        	   evaluationState = getEvaluationState(value);
                   evaluationState = '<div id="assess" onclick="openModal(\''+proNo+'\')" style="margin-left:-1.5%;margin-top:3%;">'+
		             '<span>'+evaluationState+'</span>'+
		                '</div>';
	               return evaluationState;
               }
           },
	       {title : '点评',field : 'comment',width : 260,
				formatter : function(value, row, index) {
					comment = getComment(value);
					comment = '<div style="width: 100%;">' + '<span>'
							+ comment + '</span>' + '</div>';
					return comment;
				}
	       },
	       {title: prohibitTitle,
	        field:'peopleLamp',width:40,align: 'center',valign:'middle',
	    	   formatter : function(value, row, index) {
	    		    if (value!=null & value!=''){
       				     return '<a href="javascript:void(0)" id="'+row.no+"IndexZL"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.no+'\',\''+"ZL"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
       			    }else{
       				     return '<a href="javascript:void(0)" id="'+row.no+"IndexZL"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.no+'\',\''+"ZL"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
       			    }
	    	   }
	    	   
	       }
        ]
    };
    function otherInitColumn(){
        return [
            {title:'项目编号 ',field:'no',width:80,visible: false},
            {
        		title : '序</br>号',
        		align: "center",
        		width: 45,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#mytable').bootstrapTable('getOptions').pageSize;  
        		    var pageNumber=$('#mytable').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
            },
            {title:'项目名称',field:'name',width:150,sortable:true,
            	formatter:function(val, row){
					  var path1 = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.no+ '&zlyb='+$("#dateSection").val()+'';
					  return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path1+'">'+val+'</a>';
				}
            },
            {title:'PM',field:'pm',width:60,sortable:true},
            {title:'QA',field:'qa',width:60,sortable:true},
            {title:'部门',field:'department',width:100,sortable:true,
            	formatter:function(value,row,index){
					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
				}	
            },
//            {title:'阶段',field:'',width:80},
            {title:'类</br>型',field:'projectType',width:48,align: 'left',sortable:true,},
            {title:'里程点',field:'mileagePoint',width:70,
            	formatter:function(value,row,index){
            		progress = getProgress(value);
            		overdue = getOverdue(row.overdueDate);
            		progress =  '<div style="width: 100%;height: 12px;margin-top: 7%;" class="progress">'+
		                			progress +
		                		'</div>'+
		                		overdue;
                    return progress;
                }
            },
            {title:'质</br>量',field:'qualityLamp',width:41,align: 'left',valign:'middle',sortable:true,
            	formatter:function(value,row,index){
            		var deng = getDeng(getLamp('quality',value).lamp);
                    return '<div style="width: 100%;" onclick="measureCommentModalShow(\''+row.no+'\')">'+'<span>'+deng+'</span>'+'</div>';
                }
            },
            {title:'资</br>源',field:'keyRoles',width:41,align: 'left',valign:'middle',sortable:true,
            	formatter:function(value,row,index){
            		var deng = getDeng(getLamp('resources',value).lamp);
                    return '<div style="width: 100%;">'+'<span>'+deng+'</span>'+'</div>';
                }
            },
            {title:'进</br>度',field:'planLamp',width:41,align: 'left',valign:'middle',sortable:true,
            	formatter:function(value,row,index){
            		var deng = getDeng(getLamp('progress',value).lamp);
                    return '<div style="width: 100%;">'+'<span>'+deng+'</span>'+'</div>';
                }
            },
            {title:'变</br>更',field:'scopeLamp',width:41,align: 'left',valign:'middle',sortable:true,
            	formatter:function(value,row,index){
            		var deng = getDeng(getLamp('changes',value).lamp);
                    return '<div style="width: 100%;">'+'<span>'+deng+'</span>'+'</div>';
                }
            },
            {title:'统计</br>周期',field:'statisticalCycle',width:59,sortable:true,
            	formatter:function(value,row,index){
	                return getStatisticalCycle(value);
            	}
            },
           {title:'近五次</br>状态评估',field:'fiveStatusAssessment',width:90,align: 'center',valign:'middle',
        	   formatter:function(value,row,index){
	        	   var proNo = row.no;
	        	   evaluationState = getEvaluationState(value);
                   evaluationState = '<div id="assess" onclick="openModal(\''+proNo+'\')" style="margin-left:-1.5%;margin-top:3%;">'+
		             '<span>'+evaluationState+'</span>'+
		                '</div>';
	               return evaluationState;
               }
           },
	       {title : '点评',field : 'comment',width : 290,
				formatter : function(value, row, index) {
					comment = getComment(value);
					comment = '<div style="width: 100%;">' + '<span>'
							+ comment + '</span>' + '</div>';
					return comment;
				}
	       },
	       {title: prohibitTitle,
	    	field:'peopleLamp',width:40,align: 'center',valign:'middle',
	    	   formatter : function(value, row, index) {
	    		    if (value!=null & value!=''){
       				     return '<a href="javascript:void(0)" id="'+row.no+"IndexZL"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.no+'\',\''+"ZL"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
       			    }else{
       				     return '<a href="javascript:void(0)" id="'+row.no+"IndexZL"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.no+'\',\''+"ZL"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
       			    }
	    	   }
	    	   
	       }
        ]
    };
    
	$.fn.loadzbTop=function(){
		var columns=initColumn();
		if(statusAssess != "3"){
        	columns=otherInitColumn();
        }
        var myParam = {
        	'statisticalTime':$("#dateSection").val(),
        	'area': ($("#usertype1").selectpicker("val")==null || $('#select1').is(':hidden'))?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
            'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
            'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
            'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
            'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
            'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
            'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
            'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
            'status': statusAssess,
            'coopType': collected,//0:全部项目;1：已收藏项目;2:未收藏项目
        };
        $("#mytable").bootstrapTable('destroy');//先要将table销毁，否则会保留上次加载的内容
        var url = getRootPath() + '/projectOverview/getProjectOverview';
        bsTable = new BSTable('mytable', url, columns);
        bsTable.setClasses('tableCenter');
        bsTable.setQueryParams(myParam);
        bsTable.setPageSize(20);
        bsTable.setPageNumber(1);
        bsTable.setSortable(true);
        bsTable.setEditable(false);
        bsTable.init();
	}
	

	function getStatisticalCycle(value) {
		var cycle = value;
		
		if (value == 0) {
			cycle = "";
		}
		return cycle;
	}
	
	function getDeng(lamp){
		var deng = "";
		if (lamp != "") {
			deng = '<div style="display: inline-block;border-radius: 50%;width: 15px;height:15px;'
				+'background-color:'+lamp+';"></div>';
		}
        return deng;
    }

    
    function getProgress(value){
    	if(value == "100"){
    		progress = '<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'+' style="width: 100%;background-color: #c00000">'+
            '<span class="sr-only"></span>'+
            '</div>';
    	}else if(value == "-1"){
    		progress = '<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'+' style="width: 100%;background-color: #bdb7b7">'+
            '<span class="sr-only"></span>'+
            '</div>';
    	}else{
    		progress = '<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"'+' style="width:'+value+';background-color: #009051">'+
            '<span class="sr-only"></span>'+
            '</div>';
    	}
        return progress;
    }
    function getOverdue(value){
    	if(value != -1 ){
    		overdue = '<span>逾期'+value+'天</span>';
    	}else{
    		overdue = '<span></span>';
    	}
    	return overdue;
    }
    
    function getEvaluation(lamp){
    	var evaluation = "";
    	if(lamp.text == ""){
    		return evaluation;
    	}else{
    		return '<span style="color:'+lamp.lamp+'"><b>'+lamp.text+'</b></span>';
    	}
    }
    
    function getComment(value){
    	if(value == "" || value == null){
    		comment = '<span ><p align="left" style="word-break:break-all;"></p></span>'
    	}else{
    		comment = '<span ><p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p></span>'
    	}
    	return comment;
    }

	function showIndex(name){
		$("#quality").attr("src",name);
		hideMenu();
		$("div[id^='sub_menu']").attr("class","sub-menu");
	}
    
    function getEvaluationState(value){
    	var result = "";
    	for(var i = 0; i < value.length; i++){
    		if(value[i] != -1){
    			evaluationState = '<div style="display: inline-block;width: 13px;height: 15px;margin-left: 1px;background-color: '+getColor(getLamp("statusAssessment",value[i]).text)+';"></div>'
    		}else{
    			evaluationState = '<div style="display: inline-block;width: 13px;height: 15px;margin-left: 1px;"></div>'
    		}
    		result += evaluationState;
    	}
    	return result;
    }
    
    $(document).on("click","#qualityReport li",function(){
    	var tis=$(this);
    	var tabname=tis.attr("tabname");
    	$("#qualityReport").children().removeClass("active");
    	$("#qualityReport li[tabname='"+tabname+"']").addClass("active");
    });
    
    $(document).on("click","#qmsSelect li",function(){
    	var tis=$(this);
    	var tabname=tis.attr("tabname");
    	if(tabname == "tab-wd"){
    		qmsDimension();
    		$('#qmsCollection').hide();
    		$("#projectDimension").show();
    		$('#exportQMS').show();
    		$("#qmsProblem").hide();
    	}else if(tabname == "tab-wt"){
    		$('#qmsReport').bootstrapTable('refresh');
    		qmsSector();
    		qmsHistogram(null);
    		$("#qmsCollection").empty();
    		$("#qmsCollection").append(prohibitTitle);
    		$("#qmsProblem").show();
    		$("#projectDimension").hide();
    		$('#exportQMS').hide();
    		$("#qmsCollection").show();
    	}
    	$("#qmsSelect").children().removeClass("active");
    	$("#qmsSelect li[tabname='"+tabname+"']").addClass("active");
    });
    //收藏页面触发函数
    $(document).on("click","#collection",function(){
    	queryConcernItems();
    	$('#deployName').html("已收藏项目");
    	$("#seniors").hide();
    	$('.but2').hide();
    	$("#follow").show();
    	$('#collected').show();
    	$('#user_type2').selectpicker("val",["在行"]);
        var zrOrhwselect = getCookie("zrOrhwselect");
        $('#zrOrhwselect').val(zrOrhwselect);
        zrOrhwselectChenge();
		initHWCPX();
		initYeWuXian();
    	$("#iteAddPage").modal('show');
    });
    $(document).on("click","#prohibit",function(){
    	switch(collected) {
        case 0:collected = 1;
               prohibitTitle = '<button id="prohibit" type="button" style="background-image: url(/mvp/view/HTML/images/collected.png);" class="collection-background"></button>';
               break;
        case 1:collected = 2;
               prohibitTitle = '<button id="prohibit" type="button" style="background-image: url(/mvp/view/HTML/images/collection.png);" class="collection-background"></button>';
               break;
        case 2:collected = 0;
               prohibitTitle = '<button id="prohibit" type="button" class="collection-background"></button>';
               break;
        }
    	selectOnchange();
    });
    
    $(document).ready(function(){
    	initDateSection();
    	var Menus = getCookie('Menulist');
    	var flag = false;
    	/*if (Menus.indexOf("-99-") != -1) {
    		var tab0 ='<li tabname="tab-zonglan" id="zongLan" class="active" onclick="loadOverviewReport()" style="border: 0px;"><a href="#">总览</a></li>';
    		$("#quality").append(tab0);
    		if(!flag){
    			$("#zongLan").click();
    			flag = true;
    		}
    	}
		 if (Menus.indexOf("-100-") != -1) {
    		var tab1 ='<li tabname="tab-zhiliang" id="zhiLiang" onclick="loadQualityReport()" style="border: 0px;margin-left: 15px;"><a href="###">质量</a></li>';
    		$("#quality").append(tab1);
    		if(!flag){
    			$("#zhiLiang").click();
    			flag = true;
    		}
    	}*/
    	if (Menus.indexOf("-45-") != -1) {
    		var tab2 ='<li tabname="tab-ziyuan" id="ziYuan" onclick="loadZrUser()" style="border: 0px;margin-left: 15px;"><a href="###"style="font-size: 14px">中软用户</a></li>';
    		$("#quality").append(tab2);
    		if(!flag){
    			$("#ziYuan").click();
    			flag = true;
    		}
    	}
    	if (Menus.indexOf("-46-") != -1) {
    		var tab3 ='<li tabname="tab-jindu" id="jinDu" onclick="loadHwUser()" style="border: 0px;margin-left: 15px;"><a href="###"style="font-size: 14px">华为用户</a></li>';
    		$("#quality").append(tab3);
    		if(!flag){
    			$("#jinDu").click();
    			flag = true;
    		}
    	}
    	if (Menus.indexOf("-47-") != -1) {
    		var tab4 ='<li tabname="tab-biangeng" id="bianGeng" onclick="loadSysUser()" style="border: 0px;margin-left: 15px;"><a href="###"style="font-size: 14px">系统用户</a></li>';
    		$("#quality").append(tab4);
    		if(!flag){
    			$("#bianGeng").click();
    			flag = true;
    		}
    	}
    	/*if (Menus.indexOf("-104-") != -1) {
    		var tab5 ='<li tabname="tab-xiaolv" id="xiaoLv" onclick="loadEfficiencyReport()" style="border: 0px;margin-left: 15px;"><a href="###">效率</a></li>';
    		$("#quality").append(tab5);
    		if(!flag){
    			$("#xiaoLv").click();
    			flag = true;
    		}
    	}
    	if (Menus.indexOf("-105-") != -1) {
    		var tab6 ='<li tabname="tab-wenti" id="wenTi" onclick="problemAnalysis()" style="border: 0px;margin-left: 15px;"><a href="###">问题</a></li>';
    		$("#quality").append(tab6);
    		if(!flag){
    			$("#wenTi").click();
    			flag = true;
    		}
    	}
    	if (Menus.indexOf("-106-") != -1) {
    		var tab7 ='<li tabname="tab-qms" id="qms" onclick="qmsReport()" style="border: 0px;margin-left: 15px;"><a href="###">QMS</a></li>';
    		$("#quality").append(tab7);
    		if(!flag){
    			$("#qms").click();
    			flag = true;
    		}
    	}
    	if (Menus.indexOf("-107-") != -1) {
    		var tab8 ='<li tabname="tab-netSec" id="netSec" onclick="loadNetSecReport()" style="border: 0px;margin-left: 15px;"><a href="###">网络安全</a></li>';
    		$("#quality").append(tab8);
    		if(!flag){
    			$("#netSec").click();
    			flag = true;
    		}
    	}
    	if ("管理员" == getCookie('Currentpername') || 
    		(-1 != Menus.indexOf("-108-"))) {
    		var tab9 ='<li tabname="tab-xmcb" id="xmcb" onclick="loadXMCB()" style="border: 0px;margin-left: 15px;"><a href="###">项目成本</a></li>';
    		$("#quality").append(tab9);
    		if(!flag){
    			$("#xmcb").click();
    			flag = true;
    		}
    	}*/
    	/*if(getCookie('Currentpername')!="管理员"){
    		$("#quality li[tabname='tab-ziyuan']").css("display","none");
    		$("#quality li[tabname='tab-jindu']").css("display","none");
    		$("#quality li[tabname='tab-biangeng']").css("display","none");
    		$("#quality li[tabname='tab-xmcb']").css("display","none");
    	}*/
        var obj_lis = document.getElementById("statusAssessment").getElementsByTagName("a");
        for(i=0;i<obj_lis.length;i++){
            obj_lis[i].onclick = function(){
                statusAssessment = this.innerHTML;
                if(statusAssessment == "未进行评估"){
                    statusAssess = "0";
                }else if(statusAssessment == "风险状态项目"){
                    statusAssess = "1";
                }else if(statusAssessment == "预警状态项目"){
                    statusAssess = "2";
                }else if(statusAssessment == "已评估"){
                    statusAssess = "4";
                }else{
                    statusAssess = "3";
                }
            }
        }

        var flag = $(window.parent.document.getElementById("zonglan")).attr("flag");
        if(flag && flag!==""){
            $(window.parent.document.getElementById("zonglan")).attr("flag","");
            obj_lis[parseInt(flag)].click();
		}
    	initTableSave();
    	qualityProblem();
    	resourceReport();
    })
});
//项目选择
var checkboxOnclick = function(val,id){
	if($(id).is(":checked")){
		checkboxs.push(val);
	}else {
		checkboxs.splice(checkboxsContain(val), 1);
	}
}
//判断是否属于checkboxs
function checkboxsContain(val){
	var num = null;
	for(var i = 0; i < checkboxs.length; i++) {
        if(checkboxs[i] == val) {
          num = i;
          break;
        }
    }
	return num;
}

function getColor(val){
	var color = "";
	if(val == "正常"){
		color = "#00B050";
	}else if(val == "预警"){
		color = "#FFC000";
	}else if(val == "风险"){
		color = "#c00000";
	}else if(val == "未评估"){
		color = "#bdb7b7";
	}else{
		color = "black";
	}
	return color;
}
function selectOnchange(){
	var active = $("#quality li[class='active']");
	if(active.attr("tabname")=="tab-zonglan"){
		$().loadzbTop();
	}else if(active.attr("tabname")=="tab-zhiliang"){
		$().refreshQualityReport();
	}else if(active.attr("tabname")=="tab-wenti"){
		qualityProblem();
	}else if(active.attr("tabname")=="tab-qms"){
		qmsDimension();
		$('#qmsReport').bootstrapTable('refresh');
		$("#qmsCollection").empty();
		$("#qmsCollection").append(prohibitTitle);
		qmsSector();
    	qmsHistogram(null);
	}else if(active.attr("tabname")=="tab-ziyuan"){
		resourceReport();
	}else if(active.attr("tabname")=="tab-xiaolv"){
		$().refreshEfficiencyReport();
	}else if(active.attr("tabname")=="tab-netSec"){
		$().refreshNetSecReport();
	}else if(active.attr("tabname")=="tab-xmcb"){
		loadXMCB();
	}
	
	$("#currentTime").html("当前时间:"+$("#dateSection").val());
}

function filterProject(){
	$().loadzbTop();
}

function openModal(obj) {
	$("#assessStatus").modal('show');
	var dateArr = $("#mytable").bootstrapTable("getData");
	var data;
	for (var i = 0; i < dateArr.length; i++) {
		if(dateArr[i].no == obj){
			data = dateArr[i];
		}
	}
	//状态评估
	var statusAssessments = data.fiveStatusAssessment;
	for(var i = 0; i < statusAssessments.length; i++){
		oneWeekStatus = getModalStatusAssessment(statusAssessments[0]);
		twoWeekStatus = getModalStatusAssessment(statusAssessments[1]);
		threeWeekStatus = getModalStatusAssessment(statusAssessments[2]);
		fourWeekStatus = getModalStatusAssessment(statusAssessments[3]);
		fiveWeekStatus = getModalStatusAssessment(statusAssessments[4]);
		$("#oneWeekStatus").html(oneWeekStatus);
		$("#twoWeekStatus").html(twoWeekStatus);
		$("#threeWeekStatus").html(threeWeekStatus);
		$("#fourWeekStatus").html(fourWeekStatus);
		$("#fiveWeekStatus").html(fiveWeekStatus);
	}
	//质量
	var qualityLamps = data.fiveQualityLamp;
	for(var i = 0; i < qualityLamps.length; i++){
		oneWeekQuality = getModalLamp(qualityLamps[0],"quality");
		twoWeekQuality = getModalLamp(qualityLamps[1],"quality");
		threeWeekQuality = getModalLamp(qualityLamps[2],"quality");
		fourWeekQuality = getModalLamp(qualityLamps[3],"quality");
		fiveWeekQuality = getModalLamp(qualityLamps[4],"quality");
		$("#oneWeekQuality").html(oneWeekQuality);
		$("#twoWeekQuality").html(twoWeekQuality);
		$("#threeWeekQuality").html(threeWeekQuality);
		$("#fourWeekQuality").html(fourWeekQuality);
		$("#fiveWeekQuality").html(fiveWeekQuality);
	}
	//资源
	var keyRoles = data.fiveKeyRoles;
	for(var i = 0; i < keyRoles.length; i++){
		oneWeekRole = getModalLamp(keyRoles[0],"resources");
		twoWeekRole = getModalLamp(keyRoles[1],"resources");
		threeWeekRole = getModalLamp(keyRoles[2],"resources");
		fourWeekRole = getModalLamp(keyRoles[3],"resources");
		fiveWeekRole = getModalLamp(keyRoles[4],"resources");
		$("#oneWeekRole").html(oneWeekRole);
		$("#twoWeekRole").html(twoWeekRole);
		$("#threeWeekRole").html(threeWeekRole);
		$("#fourWeekRole").html(fourWeekRole);
		$("#fiveWeekRole").html(fiveWeekRole);
	}
	//进度
	var planLamps = data.fivePlanLamp;
	for(var i = 0; i < planLamps.length; i++){
		oneWeekPlan = getModalLamp(planLamps[0],"progress");
		twoWeekPlan = getModalLamp(planLamps[1],"progress");
		threeWeekPlan = getModalLamp(planLamps[2],"progress");
		fourWeekPlan = getModalLamp(planLamps[3],"progress");
		fiveWeekPlan = getModalLamp(planLamps[4],"progress");
		$("#oneWeekPlan").html(oneWeekPlan);
		$("#twoWeekPlan").html(twoWeekPlan);
		$("#threeWeekPlan").html(threeWeekPlan);
		$("#fourWeekPlan").html(fourWeekPlan);
		$("#fiveWeekPlan").html(fiveWeekPlan);
	}
	//变更
	var scopeLamps = data.fiveScopeLamp;
	for(var i = 0; i < scopeLamps.length; i++){
		oneWeekScope = getModalLamp(scopeLamps[0],"scope");
		twoWeekScope = getModalLamp(scopeLamps[1],"scope");
		threeWeekScope = getModalLamp(scopeLamps[2],"scope");
		fourWeekScope = getModalLamp(scopeLamps[3],"scope");
		fiveWeekScope = getModalLamp(scopeLamps[4],"scope");
		$("#oneWeekScope").html(oneWeekScope);
		$("#twoWeekScope").html(twoWeekScope);
		$("#threeWeekScope").html(threeWeekScope);
		$("#fourWeekScope").html(fourWeekScope);
		$("#fiveWeekScope").html(fiveWeekScope);
	}
	//点评
	var comments = data.fiveComment;
	for(var i = 0; i < comments.length; i++){
		if(comments[0] == undefined || comments[0] == "-1"){
			$("#oneWeekComment").html("");
		}else{
			$("#oneWeekComment").html(comments[0]);
			$("#oneWeekComment").attr("title", comments[0]);
		}
		if(comments[1] == undefined || comments[1] == "-1"){
			$("#twoWeekComment").html("");
		}else{
			$("#twoWeekComment").html(comments[1]);
			$("#twoWeekComment").attr("title", comments[1]);
		}
		if(comments[2] == undefined || comments[2] == "-1"){
			$("#threeWeekComment").html("");
		}else{
			$("#threeWeekComment").html(comments[2]);
			$("#threeWeekComment").attr("title", comments[2]);
		}
		if(comments[3] == undefined || comments[3] == "-1"){
			$("#fourWeekComment").html("");
		}else{
			$("#fourWeekComment").html(comments[3]);
			$("#fourWeekComment").attr("title", comments[3]);
		}
		if(comments[4] == undefined || comments[4] == "-1"){
			$("#fiveWeekComment").html("");
		}else{
			$("#fiveWeekComment").html(comments[4]);
			$("#fiveWeekComment").attr("title", comments[4]);
		}
	}
}
function getModalStatusAssessment(score){
	if(getLamp("statusAssessment",score).text == "未评估"){
		week = '<div style="display: block;margin-top: 8px;width: 36px;height: 20px;line-height: 20px;background-color: '+getColor(getLamp("statusAssessment",score).text)+';">'+
		  '<span style="padding-left:5px;color:black;">未评</span>'+
		  '</div>'
	}else if(getLamp("statusAssessment",score).text == "风险"){
		week = '<div style="display: block;margin-top: 8px;width: 36px;height: 20px;line-height: 20px;background-color: '+getColor(getLamp("statusAssessment",score).text)+';">'+
		  '<span style="padding-left:5px;">风险</span>'+
		  '</div>'
	}else if(getLamp("statusAssessment",score).text == "预警"){
		week = '<div style="display: block;margin-top: 8px;width: 36px;height: 20px;line-height: 20px;background-color: '+getColor(getLamp("statusAssessment",score).text)+';">'+
		  '<span style="padding-left:5px;color:black;">预警</span>'+
		  '</div>'
	}else if(getLamp("statusAssessment",score).text == "正常"){
		week = '<div style="display: block;margin-top: 8px;width: 36px;height: 20px;line-height: 20px;background-color: '+getColor(getLamp("statusAssessment",score).text)+';">'+
		  '<span style="padding-left:5px;">正常</span>'+
		  '</div>'
	}else{
		week = '<span></span>'
	}
	return week;
}
function getModalLamp(score,val){
	if(getLamp(val,score).text == "未评估"){
		week = '<div style="display: block;width: 6px;height: 6px;border-radius: 50%;margin-top: 10px;margin-left: -60px;background-color: '+getColor(getLamp(val,score).text)+';"></div>'
	}else if(getLamp(val,score).text == "风险"){
		week = '<div style="display: block;width: 6px;height: 6px;border-radius: 50%;margin-top: 10px;margin-left: -60px;background-color: '+getColor(getLamp(val,score).text)+';"></div>'
	}else if(getLamp(val,score).text == "预警"){
		week = '<div style="display: block;width: 6px;height: 6px;border-radius: 50%;margin-top: 10px;margin-left: -60px;background-color: '+getColor(getLamp(val,score).text)+';"></div>'
	}else if(getLamp(val,score).text == "正常"){
		week = '<div style="display: block;width: 6px;height: 6px;border-radius: 50%;margin-top: 10px;margin-left: -60px;background-color: '+getColor(getLamp(val,score).text)+';"></div>'
	}else{
		week = '<span></span>'
	}
	return week;
}

$(document).on("click", "#closeBtn", function () {
	$("#assessStatus").modal('hide');
});

//加载当前日期	
function initDateSection() {
	$.ajax({
		url: getRootPath() + '/measureComment/getTime',
		type: 'post',
		data : {
			num:6,
			flag:true,
		},
		async: false,//是否异步，true为异步
		success: function (data) {
			$("#dateSection").empty();
			data = data.data;
			if(null==data||""==data) return;
			var select_option="";
			var length = data.length<5 ? data.length: 5;
			for(var i = 0; i < length; i++){
				select_option += "<option value='"+data[i]+"'";
				if(length>1){
					if(1==i){
						select_option += "selected='selected'";
					}
				}else{
					if(page==i){
						select_option += "selected='selected'";
					} 
				}
				select_option += ">"+data[i]+"</option>";
			}
			$("#currentTime").html("当前周期:"+data[0]);
			$("#dateSection").html(select_option);
            $('#dateSection').selectpicker('refresh');
            $('#dateSection').selectpicker('render');
		}
	});
};


function changeFrameHeight() {
    var ifm = document.getElementById("myiframe");
	ifm.height = 500;
};
var projNo="";

$(document).on("click", "#closeBtnMeasureCommentModal", function () {
    $("#measureCommentModal").modal('hide');
    $("#measureCommentPage").empty();
});

function measureCommentModalShow(no){
    projNo=no;
    $("#MeasureCommentTitle").html($("#dateSection").val());
    $("#measureCommentPage").html('<iframe src="measureCommentTree.html" width="100%" id="myiframe" onload="changeFrameHeight()" frameborder="0"></iframe>');

    $("#measureCommentModal").modal('show');
}


$(document).on("click", "#closeBtnStateEvaluationModal", function () {
    $("#stateEvaluationModal").modal('hide');
});

$(document).on("click", "#closeBtnProjectCostModal", function () {
    $("#projectCostModal").modal('hide');
});

function stateEvaluationModalShow(no){
    $("#stateEvaluationTitle").html($("#dateSection").val());

    $.ajax({
        url: getRootPath() + '/projectReview/queryProjectStateScore',
        type: 'post',
        data : {
            no:no,
        },
        async: false,//是否异步，true为异步
        success: function (data) {
        	brokenLineDiagram(data,'评估得分','状态评估(');
        }
    });
}
function brokenLineDiagram(data,name,title){
	if (data.code == '200') {
        $("#stateEvaluationModal").modal('show');
        $("#diagram").text(title);
        var divId = "stateEvaluationPage";
        var axisName = ['时间',name];
        var types = [name];
        var xAxis = data.data.monthList;
        var values = [];
        values.push(data.data.valueList);
        var markPointData =[];
        for (var i=0;i<xAxis.length;i++){
			if($("#dateSection").val() === xAxis[i]){
                markPointData.push({name: '选择时间', value: values[0][i], xAxis: xAxis[i], yAxis: values[0][i]});
			}
		}

        //不传入颜色为默认颜色
        lineCharts(divId,axisName,types,xAxis,values,null,markPointData);
    }else{
        toastr.error('查询得分异常!');
    }
}
function checkboxOnclickData(val){
	$('#tableProject').bootstrapTable({
        method: 'GET',
        contentType: 'application/x-www-form-urlencoded',
        url: getRootPath() + "/projects/checkboxOnclickData",
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        toolbarAlign:'right',
        pageSize: 5, //单页记录数
        pageList: [5, 10, 20], //分页步进值
        minimumCountColumns: 2, //最少允许的列数		
		queryParams : function(params){
			var param = {
					'pageSize': params.limit,
					'pageNumber': params.offset,
					'month':$("#dateSection").val(),
					'clientType': $("#zrOrhwselect option:selected").val(),
					'area': $("#user_type1").selectpicker("val")==null?null:$("#user_type1").selectpicker("val").join(),//转换为字符串
					'hwpdu': $("#user_type3").selectpicker("val")==null?null:$("#user_type3").selectpicker("val").join(),//转换为字符串
					'hwzpdu': $("#user_type4").selectpicker("val")==null?null:$("#user_type4").selectpicker("val").join(),//转换为字符串
					'pduSpdt': $("#user_type5").selectpicker("val")==null?null:$("#user_type5").selectpicker("val").join(),//转换为字符串
					'bu': $("#user_type6").selectpicker("val")==null?null:$("#user_type6").selectpicker("val").join(),//转换为字符串
					'pdu': $("#user_type7").selectpicker("val")==null?null:$("#user_type7").selectpicker("val").join(),//转换为字符串
					'du': $("#user_type8").selectpicker("val")==null?null:$("#user_type8").selectpicker("val").join(),//转换为字符串
					'type': $("#seniorSelect option:selected").val(),
					'measureId':checkboxs.join(),
					'coopType': val==0 ? null:$("#collectedSelect option:selected").val(),
					'teamId' : collectionNos(),
				}
			return param;
		},
		columns:checkboxCollection(val),
		locale:'zh-CN'//中文支持
	});
}
function collectionNos(){
	var nos = "";
	if(concernList.length > 0){
		nos += concernList[0].no;
		for(var i=1;i<concernList.length;i++){
			nos += ","+concernList[i].no;
		}
	}
	return nos;
}
function checkboxCollection(val){
	if(val == 0){
		return [
			{
        		title:'全选',
        		field:'status',
        		align : "center",
        		width : 50,
        		formatter: function (value,row,index){
        			if(checkboxsContain(row.no,2) != null){
        				return "<input type='checkbox' onclick='checkboxOnclick(\""+row.no+"\",this)' name='zongSelect' checked='checked'/>";
        			}else{
        				return "<input type='checkbox' onclick='checkboxOnclick(\""+row.no+"\",this)' name='zongSelect'/>";
        			}
				}
        	},
        	{
        		title : '项目编码',
        		field : 'no',
        		visible : false
        		
            },
			{
        		title : '项目名称',
        		field : 'NAME',
        		align: "center",
        		width: 250,
        		valign:'middle'
            },
			{
        		title : '项目经理',
        		field : 'PM',
        		align: "center",
        		width: 80,
        		valign:'middle'
            },
        ];
	}else{
		return [
        	{
        		title : '项目编码',
        		field : 'no',
        		visible : false
        		
            },
			{
        		title : '项目名称',
        		field : 'NAME',
        		align: "center",
        		width: 250,
        		valign:'middle'
            },
			{
        		title : '项目经理',
        		field : 'PM',
        		align: "center",
        		width: 80,
        		valign:'middle'
            },
            {title: '收藏</br>项目',field:'',width:40,align: 'center',valign:'middle',
	 	    	   formatter : function(value, row, index) {
	 	    		    value = null;
	 	    		    for(var i=0;i<concernList.length;i++){
	 	    				if(row.no == concernList[i].no){
	 	    					value = "1";
	 	    					break;
	 	    				}
	 	    			}
	 	    		    if (value!=null & value!=''){
	        				     return '<a href="javascript:void(0)" id="'+row.no+"sign"+'" style="color: #721b77;" title="取消" onclick="concernItems(\''+row.no+'\',\''+row.NAME+'\',\''+row.PM+'\',1)"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
	        			    }else{
	        				     return '<a href="javascript:void(0)" id="'+row.no+"sign"+'" style="color: #721b77;" title="收藏" onclick="concernItems(\''+row.no+'\',\''+row.NAME+'\',\''+row.PM+'\',0)"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
	        			}
	 	    	   }
	 	    	   
	 	       }
        ];
	}
}
