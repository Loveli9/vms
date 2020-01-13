	var measureIds = ["266","267","254","255","256","257","258","259"];
	var pcbCategory = "软件开发/测试类";
	
	$(document).on("click", "#efficiencyReport .navbar-nav li", function() {
		$("#efficiencyReport .navbar-nav li").removeClass("active");
		_.each($("#efficiencyReport .navbar-nav li"), function(tab, index) {
			$("#" + $(tab).attr("tabname")).css('display', 'none');
		});
		$(this).addClass("active");
		var id = $(this).attr("tabname");
		$("#" + id).css('display', 'block');
	});
	
	function changeEfficiency(name){
    	if(name=="dt"){
    		$('#devtestReportTable').bootstrapTable('refresh');
    	}
  	}

	$.fn.refreshEfficiencyReport=function(){
   	 var active = $("#efficiencyReport li[class='active']");
   	 if(active.attr("tabname")=="tab-devtest"){
   		initDevtestReport();
   	 }	
  	}
	
	function initDevtestReport(){     
		$('#devtestReportTable').bootstrapTable('destroy');
    	$('#devtestReportTable').bootstrapTable({
    		method: 'GET',
            contentType: 'application/x-www-form-urlencoded',
            url: getRootPath() + '/projectOverview/queryProjectDevtestReport',
            striped: true, //是否显示行间隔色
            pageNumber: 1, //初始化加载第一页，默认第一页
            pagination: true, //是否分页
            queryParamsType: 'limit',
            sidePagination: 'server',
            toolbar:'#toolbar',//指定工作栏
            toolbarAlign:'right',
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
                    'coopType': collected,//0:全部项目;1：已收藏项目;2:未收藏项目
    			}
	    		return param;
    		},
    		columns:[
            	[	{title : '序</br>号',align: "center",valign:'middle',width: '3%',rowspan: 2,colspan: 1,
        			formatter: function (value, row, index) {
        				var pageSize=$('#devtestReportTable').bootstrapTable('getOptions').pageSize;  
        				var pageNumber=$('#devtestReportTable').bootstrapTable('getOptions').pageNumber;
        				return pageSize * (pageNumber - 1) + index + 1;
        			}
        		},
        		{title:'项目名称',field:'name',align: 'center',valign:'middle',width:'16%',rowspan: 2, colspan: 1,
        			formatter:function(val, row){
        				var path = getRootPath() + '/view/HTML/page.html?gl=1&url='+getRootPath()+'/bu/projView?projNo=' + row.proNo;
        				return '<a target="_blank" style="color: #721b77;" title="'+val +'" href="'+path+'">'+val+'</a>';
        			}
        		},
        		{title:'PM',field:'pm',align: "center",valign:'middle',width:'5%',rowspan: 2, colspan: 1},
        		{title:'部门',field:'department',align: 'center',valign:'middle',width:'10%',rowspan: 2, colspan: 1,
        			formatter:function(value,row,index){
  					  return '<p title="'+value+'" align="left" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">'+value+'</p>';
  				    }
        		},
        		{title:'类</br>型',field:'projectType',align: 'center',valign:'middle',width:'4%',rowspan: 2, colspan: 1},
        		{title:'迭代开发效率',align: 'center',valign:'middle',width:'16%',rowspan: 1, colspan: 2},
        		{title:'端到端开发效率',align: 'center',valign:'middle',width:'16%',rowspan: 1, colspan: 2},
        		{title:'测试',align: 'center',valign:'middle',width:'16%',rowspan: 1, colspan: 2},
        		{title:'自动化',align: 'center',valign:'middle',width:'16%',rowspan: 1, colspan: 2},
        		{title: prohibitTitle,
        	        field:'collection',width:40,align: 'center',valign:'middle',rowspan: 2,colspan: 1,
        	    	   formatter : function(value, row, index) {
        	    		    if (value!=null & value!=''){
               				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexXL"+'" style="color: #721b77;" title="取消" onclick="deleteConcernItems(\''+row.proNo+'\',\''+"XL"+'\')"> <img src="/mvp/view/HTML/images/collected.png" /></a>';
               			    }else{
               				     return '<a href="javascript:void(0)" id="'+row.proNo+"IndexXL"+'" style="color: #721b77;" title="收藏" onclick="addConcernItems(\''+row.proNo+'\',\''+"XL"+'\')"> <img src="/mvp/view/HTML/images/collection.png"/></a>';
               			    }
        	    	   }
        	    	   
        	     }
        	],
        	[
        		//{title : '华为投入比例(%)',field : 'id447',align: 'center',valign:'middle',width : 100,formatter: function (value,row,index){return formatterValue(value,true);}},
        		{title : 'JAVA</br>迭代生产率</br>(LOC/人天)',field : 'id254',align: 'center',valign:'middle',width : '8%'},
        		{title : 'C/C++</br>迭代生产率</br>(LOC/人天)',field : 'id255',align: 'center',valign:'middle',width : '8%'},
        		{title : 'JAVA</br>端到端生产率</br>(LOC/人天)',field : 'id266',align: 'center',valign:'middle',width : '8%'},
        		{title : 'C/C++</br>端到端生产率</br>(LOC/人天)',field : 'id267',align: 'center',valign:'middle',width : '8%'},
        		{title : '迭代测试用例</br>设计效率</br>(用例/人天)',field : 'id256',align: 'center',valign:'middle',width : '8%'},
       			{title : '迭代测试用例</br>执行效率</br>(用例/人天)',field : 'id257',align: 'center',valign:'middle',width : '8%'},
                {title : '自动化用例</br>写作效率</br>(个/人天)',field : 'id258',align: 'center',valign:'middle',width : '8%'},
                {title : '自动化脚本</br>连跑通过率（%）',field : 'id259',align: 'center',valign:'middle',width : '8%'}
        		]
        	],
        	locale:'zh-CN'//中文支持
    	});
    	
     }		
	
	function loadProjectData(){
		$('#tableProject').bootstrapTable({
			method: 'GET',
	    	contentType: "application/x-www-form-urlencoded",
	    	url: getRootPath() + "/projects/getPcbProject",
	    	striped: true, //是否显示行间隔色
	    	//dataField: 'data',
            responseHandler: function (res) {
                return {
                    "rows": res.data,
                    "total": res.totalCount
                }
            },
	    	pageNumber: 1, //初始化加载第一页，默认第一页
	    	pagination:true,//是否分页
	    	queryParamsType:'limit',
	    	sidePagination:'server',
	    	pageSize:10,//单页记录数
	    	pageList:[5,10,20,30],//分页步进值
	    	minimumCountColumns: 2,//最少允许的列数
			queryParams : function(params){
				var param = {
						'pageSize': params.limit,
						'pageNumber': params.offset,
						'client': $("#zrOrhwselect option:selected").val(),
						'name': encodeURI($("#projName").val()),
						'pm': encodeURI($("#projMgmr").val()),
						'area': $("#user_type1").selectpicker("val")==null?null:$("#user_type1").selectpicker("val").join(),//转换为字符串
						'hwpdu': $("#user_type3").selectpicker("val")==null?null:$("#user_type3").selectpicker("val").join(),//转换为字符串
						'hwzpdu': $("#user_type4").selectpicker("val")==null?null:$("#user_type4").selectpicker("val").join(),//转换为字符串
						'pduSpdt': $("#user_type5").selectpicker("val")==null?null:$("#user_type5").selectpicker("val").join(),//转换为字符串
						'bu': $("#user_type6").selectpicker("val")==null?null:$("#user_type6").selectpicker("val").join(),//转换为字符串
						'pdu': $("#user_type7").selectpicker("val")==null?null:$("#user_type7").selectpicker("val").join(),//转换为字符串
						'du': $("#user_type8").selectpicker("val")==null?null:$("#user_type8").selectpicker("val").join(),//转换为字符串
						'projectState': encodeURI($("#user_type2").selectpicker("val")==null?null:$("#user_type2").selectpicker("val").join()),//转换为字符串
						'measureId':measureIds.join()
					}
				return param;
			},
			onClickCell :function(field, value, row, $element){
			    if(field.indexOf("status") == 0){//全选按钮
			    	var list = $element[0].parentNode.childNodes;
			    	if($element[0].childNodes[0].checked){//全选
			    		for(var i=3; i<list.length; i++){
			    			list[i].childNodes[0].checked = true;
			    			row[list[i].childNodes[0].name] = "0";
			    		}
			    	}else{//反选
			    		for(var i=3; i<list.length; i++){
			    			list[i].childNodes[0].checked = false;
			    			row[list[i].childNodes[0].name] = "1";
			    		}
			    	};
			    }else if(field.indexOf("id") == 0){//指标按钮
			    	var list = $element[0].parentNode.childNodes;
			    	if($element[0].childNodes[0].checked){
			    		row[field] = "0";
			    		judgeRow(list);
			    	}else{
			    		row[field] = "1";
			    		judgeRow(list);
			    	}			    	
			    }else{
			        return;
			    }
			},
			columns:[
				{
	        		title:'全选',
	        		field:'status',
	        		align : "center",
	        		width : 50,
	        		formatter: function (value,row,index){
						var flag = true;
						for(var i = 0;i<measureIds.length;i++){
							var index = "id"+measureIds[i];
							if(row[index] == "1"){
								flag = false;
								break;
							}
						}
						if(flag){
							return "<input type='checkbox' name='status' checked='checked'/>";
						}else{
							return "<input type='checkbox' name='status'/>";
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
	        		field : 'name',
	        		align: "center",
	        		width: 150,
	        		valign:'middle'
	            },
				{
	        		title : '项目经理',
	        		field : 'pm',
	        		align: "center",
	        		width: 80,
	        		valign:'middle'
	            },
	            {
	        		title : 'JAVA迭代生产率',
	        		field : 'id254',
	        		align: "center",
	        		width: 150,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id254',value);
	    			}
	            },
				{
	        		title : 'C/C++迭代生产率',
	        		field : 'id255',
	        		align: "center",
	        		width: 150,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id255',value);
	    			}
	            },
	            {
	        		title : 'JAVA端到端生产率',
	        		field : 'id266',
	        		align: "center",
	        		width: 150,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id266',value);
	    			}
	            },
				{
	        		title : 'C/C++端到端生产率',
	        		field : 'id267',
	        		align: "center",
	        		width: 150,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id267',value);
	    			}
	            },
	            {
	        		title : '迭代测试用例设计效率',
	        		field : 'id256',
	        		align: "center",
	        		width: 150,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id256',value);
	    			}
	            },
				{
	        		title : '迭代测试用例执行效率',
	        		field : 'id257',
	        		align: "center",
	        		width: 150,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id257',value);
	    			}
	            },
	            {
	        		title : '自动化用例写作效率',
	        		field : 'id258',
	        		align: "center",
	        		width: 150,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id258',value);
	    			}
	            },
				{
	        		title : '自动化脚本连跑通关率',
	        		field : 'id259',
	        		align: "center",
	        		width: 150,
	        		valign:'middle',
	        		formatter: function (value,row,index){
	        			return formatterColumn('id259',value);
	    			}
	            }
	        ],
			locale:'zh-CN'//中文支持
		});
				
	}
	//查询按钮
	$(document).on("click", "#queryBtn", function () {
		$('#tableProject').bootstrapTable('refresh');
	});
	//隐藏配置页面
	$(document).on("click", "#add_backBtn", function () {
		var display =$('#seniors').css('display');
		if(display != 'none'){
			$('#mytable').bootstrapTable('refresh');
		}
		$("#iteAddPage").modal('hide');
	});	
	 //配置保存   
	$(document).on("click", "#add_saveBtn", function () {
		var display =$('#seniors').css('display');
		if(display != 'none'){
			$('#mytable').bootstrapTable('refresh');
			var nos = checkboxs.join();
			var $eleForm = $("<form method='POST'></form>");
	        $eleForm.attr("action",getRootPath() + "/projectOverview/exportProjectIndex");
	        $eleForm.append($('<input type="hidden" name="nos" value="'+nos+'">'));
	        $eleForm.append($('<input type="hidden" name="date" value="'+$("#dateSection").val()+'">'));
	        $(document.body).append($eleForm);
	        //提交表单，实现下载
	        $eleForm.submit();
			$("#iteAddPage").modal('hide');  
		}else if($('#collected').css('display') != 'none'){
			//$('#mytable').bootstrapTable('refresh');
			selectOnchange();
			saveConcernItems();
			$("#iteAddPage").modal('hide');
		}else{
			var list = [];
	    	var dataArr=$('#tableProject').bootstrapTable('getData');
	    	for(var i=0;i<dataArr.length;i++){		
	    		for(var j = 0;j<measureIds.length;j++){
	    			var index = 'id'+measureIds[j];   			
	    			var data ={
	    				'proNo' : dataArr[i].no,
	    				'pcbCategory':pcbCategory,
	    	    		'measureId' : measureIds[j],
	    	    		'status':dataArr[i][index]
	    			}
	    			list.push(data);    			
				}
	    	}
	    	
	    	$.ajax({
	    		url: getRootPath() + '/pcbList/insertPcbProjectConfig',
	    		type:'post',
	    		dataType:"json",  
	    	    contentType : 'application/json;charset=utf-8', //设置请求头信息 
	    		data:JSON.stringify(list),
	    		success:function(data){
	    			if(data.code == 'success'){
	    				toastr.success('配置成功！');
	    				$().refreshEfficiencyReport();
	    				$("#iteAddPage").modal('hide');  				        				
	    			}else{
	    				toastr.error('配置失败!');
	    			}
	    		}
	    	});
		}
    });
	 $(document).on("change", "#zrOrhwselect", function () {
	        zrOrhwselectChenge();
	    });
	    function zrOrhwselectChenge(){
	        var flag = $("#zrOrhwselect option:selected").val();
	        if(flag=="0"){
	            $("#hwselect").css("display","block");
	            $("#zrselect").css("display","none");
	            $('#user_type6').selectpicker('val', '');
	            $('#user_type7').selectpicker('val', '');
	            $('#user_type8').selectpicker('val', '');

	        }else{
	            $("#hwselect").css("display","none");
	            $("#zrselect").css("display","block");
	            $('#user_type3').selectpicker('val', '');
	            $('#user_type4').selectpicker('val', '');
	            $('#user_type5').selectpicker('val', '');
	        }

	    }

	    //加载区域下拉菜单
	    function initArea() {
	        $.ajax({
	            url: getRootPath() + '/tblArea/getAllTblArea',
	            type: 'post',
	            async: false,//是否异步，true为异步
	            success: function (data) {
	                var select_option="";
	                $("#user_type1").empty();
	                if(null==data.data||""==data.data) return;
	                _.each(data.data, function(val, index){//(值，下标)
	                    select_option += "<option value="+val.areaCode+">"+val.areaName+"</option>";
	                })
	                $("#user_type1").html(select_option);
	                $('#user_type1').selectpicker('refresh');
	                $('#user_type1').selectpicker('render');
	            }
	        });
	    };

	    function initYeWuXian() {//默认加载业务线
	        $.ajax({
	            url: getRootPath() + '/opDepartment/getOpDepartment',
	            type: 'post',
	            async: false,
	            data: {
	                level: 2,
	            },
	            success: function (data) {
	                $('#user_type6').selectpicker("val", []);
	                $("#user_type6").empty();
	                $('#user_type6').prev('div.dropdown-menu').find('ul').empty();
	                $('#user_type7').selectpicker("val", []);
	                $("#user_type7").empty();
	                $('#user_type7').prev('div.dropdown-menu').find('ul').empty();
	                $('#user_type8').selectpicker("val", []);
	                $("#user_type8").empty();
	                $('#user_type8').prev('div.dropdown-menu').find('ul').empty();
	                if (data != null && data.data != null && data.data.length > 0) {
	                    for (var i = 0; i < data.data.length; i++) {
	                        $("#user_type6").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
	                    }
	                }
	                $('#user_type6').selectpicker('refresh');
	                $('#user_type6').selectpicker('render');
	            }
	        })
	    };

	    $(document).on("change", "#user_type6", function () {
	        $.ajax({
	            url: getRootPath() + '/opDepartment/getOpDepartment',
	            type: 'post',
	            async: false,
	            data: {level: 3, ids: $("#user_type6").selectpicker("val") == null ? null : $("#user_type6").selectpicker("val").join()},
	            success: function (data) {
	                $('#user_type7').selectpicker("val", []);
	                $("#user_type7").empty();
	                $('#user_type7').prev('div.dropdown-menu').find('ul').empty();
	                $('#user_type8').selectpicker("val", []);
	                $("#user_type8").empty();
	                $('#user_type8').prev('div.dropdown-menu').find('ul').empty();
	                if (data != null && data.data != null && data.data.length > 0) {
	                    for (var i = 0; i < data.data.length; i++) {
	                        $("#user_type7").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
	                    }
	                }
	                $('#user_type7').selectpicker('refresh');
	                $('#user_type7').selectpicker('render');
	            }
	        })
	    });
	    $(document).on("change", "#user_type7", function () {
	        $.ajax({
	            url: getRootPath() + '/opDepartment/getOpDepartment',
	            type: 'post',
	            async: false,
	            data: {
	                level: 4,
	                ids: $("#user_type7").selectpicker("val") == null ? null : $("#user_type7").selectpicker("val").join()
	            },
	            success: function (data) {
	                $('#user_type8').selectpicker("val", []);
	                $("#user_type8").empty();
	                $('#user_type8').prev('div.dropdown-menu').find('ul').empty();
	                if (data != null && data.data != null && data.data.length > 0) {
	                    for (var i = 0; i < data.data.length; i++) {
	                        $("#user_type8").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
	                    }
	                }
	                $('#user_type8').selectpicker('refresh');
	                $('#user_type8').selectpicker('render');
	            }
	        })
	    });

	    function initHWCPX() {//默认加载华为产品线
	        $.ajax({
	            url: getRootPath() + '/bu/getHwdept',
	            type: 'post',
	            async: false,
	            data: {
	                level: 1,
	            },
	            success: function (data) {
	                $('#user_type3').selectpicker("val", []);
	                $("#user_type3").empty();
	                $('#user_type3').prev('div.dropdown-menu').find('ul').empty();
	                $('#user_type4').selectpicker("val", []);
	                $("#user_type4").empty();
	                $('#user_type4').prev('div.dropdown-menu').find('ul').empty();
	                $('#user_type5').selectpicker("val", []);
	                $("#user_type5").empty();
	                $('#user_type5').prev('div.dropdown-menu').find('ul').empty();
	                if (data != null && data.data != null && data.data.length > 0) {
	                    for (var i = 0; i < data.data.length; i++) {
	                        $("#user_type3").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
	                    }
	                }
	                $('#user_type3').selectpicker('refresh');
	                $('#user_type3').selectpicker('render');
	            }
	        })
	    };
	    $(document).on("change", "#user_type3", function () {
	        $.ajax({
	            url: getRootPath() + '/bu/getHwdept',
	            type: 'post',
	            async: false,
	            data: {level: 2, ids: $("#user_type3").selectpicker("val") == null ? null : $("#user_type3").selectpicker("val").join()},
	            success: function (data) {
	                $('#user_type4').selectpicker("val", []);
	                $("#user_type4").empty();
	                $('#user_type4').prev('div.dropdown-menu').find('ul').empty();
	                $('#user_type5').selectpicker("val", []);
	                $("#user_type5").empty();
	                $('#user_type5').prev('div.dropdown-menu').find('ul').empty();
	                if (data != null && data.data != null && data.data.length > 0) {
	                    for (var i = 0; i < data.data.length; i++) {
	                        $("#user_type4").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
	                    }
	                }
	                $('#user_type4').selectpicker('refresh');
	                $('#user_type4').selectpicker('render');
	            }
	        })
	    });
	    $(document).on("change", "#user_type4", function () {
	        $.ajax({
	            url: getRootPath() + '/bu/getHwdept',
	            type: 'post',
	            async: false,
	            data: {
	                level: 3,
	                ids: $("#user_type4").selectpicker("val") == null ? null : $("#user_type4").selectpicker("val").join()
	            },
	            success: function (data) {
	                $('#user_type5').selectpicker("val", []);
	                $("#user_type5").empty();
	                $('#user_type5').prev('div.dropdown-menu').find('ul').empty();
	                if (data != null && data.data != null && data.data.length > 0) {
	                    for (var i = 0; i < data.data.length; i++) {
	                        $("#user_type5").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
	                    }
	                }
	                $('#user_type5').selectpicker('refresh');
	                $('#user_type5').selectpicker('render');
	            }
	        })
	    });
		function judgeRow(list){
			var flag = true;
			for(var i=3; i<list.length; i++){
				if(list[i].childNodes[0].checked==false){
					flag = false;
					break;
				}
			}
			if(flag){
				list[0].childNodes[0].checked = true;
			}else{
				list[0].childNodes[0].checked = false;
			}
		}
		
		function formatterColumn(id,value){
			if(value==0){
				return "<input type='checkbox' name='"+id+"' checked='checked'/>";
			}else if(value==1){
				return "<input type='checkbox' name='"+id+"'/>";
			}
		}

    //打开配置页面
    $(document).on("click", "#btn_add", function () {
    	$('#deployName').html("PCB样本项目配置");
    	$('.but2').show();
    	$('#seniors').hide();
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
		loadProjectData();
		$('.but2').click(function(){				
			if($('#advancedQuery').css('display')=='none'){
				$('#advancedQuery').css('display','block');
			}else if($('#advancedQuery').css('display')=='block'){
				$('#advancedQuery').css('display','none');
			}
		});
    	$("#iteAddPage").modal('show');	
	});	
	