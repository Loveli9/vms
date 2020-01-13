$(function(){
	$('#tableZhiliangXM').bootstrapTable({
		method: 'post',
    	contentType: "application/x-www-form-urlencoded; charset=UTF-8",
     	url : getRootPath() + '/qualityModel/queryTable',
        editable:false,// 可行内编辑
    	striped: true, // 是否显示行间隔色
    	responseHandler: function (res) {
            return {
            	"rows": res.data,
                "total": res.totalCount
            }
        },
    	pageNumber: 1, // 初始化加载第一页，默认第一页
    	pagination:true,// 是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,// 单页记录数
    	pageList:[5,10,20,30],// 分页步进值
    	showColumns:false,
    	toolbar:'#toolbar',// 指定工作栏
    	toolbarAlign:'right',
        dataType: "json",
        queryParams : function(params){
            var param = {
                'pageSize': params.limit,
                'pageNumber': params.offset,
	    	        'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串*/
    				'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
    				'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
    				'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串 
    				'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
    				'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
    				'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
    				'projectState': $("#usertype2").selectpicker("val")==null?null:$("#usertype2").selectpicker("val").join(),
                	'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
					}
				return param;
		},
		columns:[
					[
						{title : '序号',field : 'id',align: "center",width: 150,valign:'middle',colspan: 1,rowspan: 2,formatter: function (value, row, index) {
		                    var pageSize=$('#tableZhiliangXM').bootstrapTable('getOptions').pageSize;
		                    var pageNumber=$('#tableZhiliangXM').bootstrapTable('getOptions').pageNumber;
		                    return pageSize * (pageNumber - 1) + index + 1;
		                    }
						},
						{title : 'PDU/SPDT',field : 'pduSpdt',align: "center",width: 80,valign:'middle',colspan: 1,rowspan: 2},
						{title : '外包项目名称',field : 'name',align: "center",width: 45,valign:'middle',colspan: 1,rowspan: 2,formatter:function(value,row,index){	
			        			var path1 = getRootPath() + '/view/HTML/page.html?url='+
	    						getRootPath()+'/bu/projView?projNo=' + row.no;
			        			return '<a target="_blank" style="color: #721b77;" title="'+
			        			row.name +'" href="'+path1+'">'+row.name+'</a>';
							}
						},
						{title : '供应商',field : 'supplier',align: "center",width: 90,valign:'middle',colspan: 1,rowspan: 2},
						{title : '地域',field : 'area',align: "center",width: 85,valign:'middle',colspan: 1,rowspan: 2},
						{title : '合作方经理',field : 'pm',align: "center",width: 100,valign:'middle',colspan: 1,rowspan: 2},
			            {title : '人力需求',field : 'humanNeeds',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 2},
			            {title : '当前人力',field : 'nowHuman',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 2},
			            
			            {title : '累计开发代码规模(Kloc)',field : 'accDevCode',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '累计开发用例个数',field : 'accDevCase',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '累计发现问题单数',field : 'accFindPro',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '累计处理问题数',field : 'accDealPro',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '累计投入人月',field : 'accPutHuman',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '代码累计生产率(loc/人天)',field : 'accCodePdt',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '用例累计生产率(个/人天)',field : 'accCasePdt',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '测试发现有效问题效率(个/人天)',field : 'testFindEffPro',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '处理问题效率(个/人天)',field : 'dealProEff',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '代码检视缺陷密度(个/Kloc)',field : 'codeReview',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '测试用例密度(个/Kloc)',field : 'testCase',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '测试缺陷密度(个/Kloc)',field : 'testDefects',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '版本转测试不通过(累计)',field : 'versionToTest',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '问题单回归不通过(累计)',field : 'proReturn',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '代码重复率',field : 'codeRepeat',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '代码最大圈复杂度',field : 'maxComplex',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '平均圈复杂度',field : 'averageComplex',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : 'LLT覆盖率',field : 'lltcover',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '静态检查Pc-lintError\warning级别清零',field : 'pcLintErrorReset',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '静态检查FindBugsHigh级别清零',field : 'lineErrorReset',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : '静态检查Coverity',field : 'coverity',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            {title : 'SAI架构度量',field : 'sai',align: "center",width: 70,valign:'middle',colspan: 1,rowspan: 1},
			            
			            {title : '已完成自动化用例个数',field : 'completeAutomaticCase',align: "center",width: 85,valign:'middle',colspan: 1,rowspan: 1},
						{title : '计划进行自动化的用例个数',field : 'proceedAutomaticCase',align: "center",width: 100,valign:'middle',colspan: 1,rowspan: 1},
						{title : '自动化用例执行率',field : 'automaticCaseExcute',align: "center",width: 85,valign:'middle',colspan: 1,rowspan: 1},
						{title : '自动化工厂执行用例数',field : 'automaticFactoryExcute',align: "center",width: 100,valign:'middle',colspan: 1,rowspan: 1},
						{title : '自动化工厂总用例数',field : 'automaticFactoryTotal',align: "center",width: 85,valign:'middle',colspan: 1,rowspan: 1},
						{title : '全量自动化脚本连跑通过率',field : 'totalAutomaticScript',align: "center",width: 100,valign:'middle',colspan: 1,rowspan: 1},
						{title : '一次自动化连跑执行成功总用例数',field : 'oneAutomaticExcuteSuccess',align: "center",width: 85,valign:'middle',colspan: 1,rowspan: 1},
						{title : '一次自动化连跑执行总用例数',field : 'oneAutomaticExcuteCase',align: "center",width: 100,valign:'middle',colspan: 1,rowspan: 1}
			            
		            ],
		            [
			            {title : '每月',field : 'accDevCode',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'accDevCase',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'accFindPro',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'accDealPro',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'accPutHuman',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'accCodePdt',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'accCasePdt',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'testFindEffPro',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'dealProEff',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'codeReview',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'testCase',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'testDefects',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'versionToTest',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'proReturn',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'codeRepeat',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'maxComplex',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'averageComplex',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'lltcover',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'pcLintErrorReset',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'lineErrorReset',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'coverity',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'sai',align: "center",width: 70,valign:'middle'},
			            
			            {title : '每月',field : 'completeAutomaticCase',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'proceedAutomaticCase',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'automaticCaseExcute',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'automaticFactoryExcute',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'automaticFactoryTotal',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'totalAutomaticScript',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'onceAutomaticExcuteSuccess',align: "center",width: 70,valign:'middle'},
			            {title : '每月',field : 'onceAutomaticExcuteCase',align: "center",width: 70,valign:'middle'} 
		            ],
		        ],
				locale:'zh-CN'//中文支持
    });
});

function selectOnchange(){
	$('#tableZhiliangXM').bootstrapTable('refresh');
}

