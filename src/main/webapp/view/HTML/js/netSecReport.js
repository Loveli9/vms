var netSecTable;
function initNetSecTable(){
	$('#netSecReport').bootstrapTable('destroy');
	$('#netSecReport').bootstrapTable({
		method: 'GET',
        contentType: 'application/x-www-form-urlencoded',
        url: getRootPath() + '/networkSecurity/queryNetworkSecurity',
        striped: true, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
		queryParams : function(params){
    		var param = {
				'limit': params.limit,
				'offset': params.offset,
				'date':$("#dateSection").val(),
		        'area': ($("#usertype1").selectpicker("val")==null || $('#select1').is(':hidden'))?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
		        'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
		        'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
		        'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
		        'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
		        'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
		        'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
		        'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
		        'type':"2",
		        'coopType': collected,//0:全部项目;1：已收藏项目;2:未收藏项目
			}
    		return param;
		},
		columns:initNetSecColumn(),
    	locale:'zh-CN'//中文支持
		});
}

function initNetSecColumn(){
	return [{
		title : '序号',
		align: "center",
		field:'id',
		width: 50,
		formatter: function (value, row, index) {
		    var pageSize=$('#netSecReport').bootstrapTable('getOptions').pageSize;  
		    var pageNumber=$('#netSecReport').bootstrapTable('getOptions').pageNumber;
		    return pageSize * (pageNumber - 1) + index + 1;
		}
     },
     {title:'项目名称',field:'name',align: 'center',valign:'middle',width:150,
    	 formatter:function(val, row){
				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.proNo;
				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
		 }
	 },
     {title:'部门',field:'department',align: 'center',valign:'middle',width:150,
		 formatter:function(value,row,index){
			  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
		}
	 },
     /*
     {title:'华为子产品线',field:'hwzpdu',align: 'center',valign:'middle',width:150},
     {title:'PDU/SPDT',field:'pduSpdt',align: 'center',valign:'middle',width:150},*/
     {title:'审视月份',field:'surveyDate',align: 'center',valign:'middle',width:100},
     {title:'项目类型',field:'projectType',align: 'center',valign:'middle',width:80},
     {title:'商务模式',field:'type',align: 'center',valign:'middle',width:80},
     {title:'PM',field:'pm',align: 'center',valign:'middle',width:100},
     {title:'QA',field:'qa',align: 'center',valign:'middle',width:100},
     {title:'是否涉及一线交付',field:'deliverStatus',align: 'center',valign:'middle',width:100,
    	 formatter: function (value,row,index){
    		 if(value==2){
    			 return "是";
    		 }else{
    			 return "否";
    		 }
    	 }
     },
     {title:'是否是UK项目',field:'ukStatus',align: 'center',valign:'middle',width:100,
    	 formatter: function (value,row,index){
    		 if(value==2){
    			 return "是";
    		 }else{
    			 return "否";
    		 }
    	 }
     },
     {title:'与客户明确SOW中网络安全要求',field:'sowStatus',align: 'center',valign:'middle',width:180,
    	 formatter: function (value,row,index){
    		 if(value==4){
    			 return "已明确";
    		 }else if(value==3){
    			 return "确认中";
    		 }else if(value==2){
    			 return "暂未明确";
    		 }else{
    			 return "不涉及";
    		 }
    	 }
     }, 
     {title:'error级当日清零',field:'errorCleared',align: 'center',valign:'middle',width:100},
     {title:'warning级按版本清零（版本过点清零）',field:'warningCleared',align: 'center',valign:'middle',width:200},
     {title:'工具测试覆盖满足度',field:'satisfyRate',align: 'center',valign:'middle',width:100},
     {title:'用例测试满足度（自动化）',field:'caseRate',align: 'center',valign:'middle',width:100,
    	 formatter: function (value,row,index){
    		 if(row.caseStatus==1){
    			 return "不涉及"; 
    		 }else{
    			 return value;
    		 };
    	 }
     },
     {title:'是否存在网络安全风险',field:'dangerStatus',align: 'center',valign:'middle',width:100,
    	 formatter: function (value,row,index){
    		 if(value==2){
    			 return "是";
    		 }else{
    			 return "否";
    		 }
    	 }
     },
     {title:'网络安全风险上报',field:'dangerReport',align: 'center',valign:'middle',width:300},
     {title: prohibitTitle,
	        field:'coopType',width:40,align: 'center',valign:'middle',
	    	   formatter : function(value, row, index) {
	    		    if (value!=null & value!=''){
    				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexWL"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.proNo+'\',\''+"WL"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
    			    }else{
    				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexWL"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.proNo+'\',\''+"WL"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
    			    }
	    	   }
	    	   
	   }
	];
}




$(document).on("click","#exportInvolve",function(){
	exportList('2');
});
$(document).on("click","#exportAll",function(){
	exportList('');
});

function exportList(val){
	var $eleForm = $("<form method='POST'></form>");
    $eleForm.attr("action",getRootPath() + "/export/exportNetSecReport");
    $eleForm.append($('<input type="hidden" name="date" value='+ ($("#dateSection").val()) +'>'));
    $eleForm.append($('<input type="hidden" name="area" value='+ ($("#usertype1").selectpicker("val")==null?"":$("#usertype1").selectpicker("val").join()) +'>'));
    $eleForm.append($('<input type="hidden" name="hwpdu" value='+ ($("#usertype3").selectpicker("val")==null?"":$("#usertype3").selectpicker("val").join()) +'>'));
    $eleForm.append($('<input type="hidden" name="hwzpdu" value='+ ($("#usertype4").selectpicker("val")==null?"":$("#usertype4").selectpicker("val").join()) +'>'));
    $eleForm.append($('<input type="hidden" name="pduSpdt" value='+ ($("#usertype5").selectpicker("val")==null?"":$("#usertype5").selectpicker("val").join()) +'>'));
    $eleForm.append($('<input type="hidden" name="bu" value='+ ($("#usertype6").selectpicker("val")==null?"":$("#usertype6").selectpicker("val").join()) +'>'));
    $eleForm.append($('<input type="hidden" name="pdu" value='+ ($("#usertype7").selectpicker("val")==null?"":$("#usertype7").selectpicker("val").join()) +'>'));
    $eleForm.append($('<input type="hidden" name="du" value='+ ($("#usertype8").selectpicker("val")==null?"":$("#usertype8").selectpicker("val").join()) +'>'));
    $eleForm.append($('<input type="hidden" name="clientType" value='+ ($("#selectBig").selectpicker("val")=="2"?"0":"1") +'>'));
    $eleForm.append($('<input type="hidden" name="type" value='+ val +'>'));
    $(document.body).append($eleForm);
    //提交表单，实现下载
    $eleForm.submit();
}

$.fn.refreshNetSecReport=function(){	
	initNetSecTable();
}
