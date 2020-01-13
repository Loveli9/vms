$(function(){
	

	function getValue(url){
		//首先获取地址
		var url = url || window.location.href;
		//获取传值
		var arr = url.split("?");
		//判断是否有传值
		if(arr.length == 1){
			return null;
		}
		//获取get传值的个数
		var value_arr = arr[1].split("&");
		//循环生成返回的对象
		var obj = {};
		for(var i = 0; i < value_arr.length; i++){
			var key_val = value_arr[i].split("=");
			obj[key_val[0]]=key_val[1];
		}
		return obj;
	}
	
	var projNo = getValue().serviceId;
	var jobid = "";
	var jobids = "";
	var serviceId = getValue().serviceId;
	function info(){
		$("div[name=isSelect]").css("background-color","#c5c5c5");
		$("div[name=isSelect]").attr("isSelectFlag","1");
		$.ajax({
			url: getRootPath() + '/project/searchConfigJob',
			type: 'post',
			data:{
				no : serviceId
			},
			success: function (data) {
				var tab = "";
				var newtime = new Date()
				$("div[name=collectionJobModu]").remove();
				_.each(data, function(config, index){
					var tab1 = "";
					if(config.types){
						var types = config.types.split(";");
						tab1 = '<div class="caijiType">';
						for(var i=0;i<types.length;i++){
							var msg = "";
							if(types[i]=="svn"){
								msg="SVN";
							}else if(types[i]=="dts"){
								msg="DTS";
							}else if(types[i]=="git"){
								msg="Git";
							}else if(types[i]=="tmss"){
								msg="TMSS";
							}else if(types[i]=="icp-ci"){
								msg="ICP-CI";
							}else if(types[i]=="smartIDE"){
								msg="SmartIDE";
							}else if(types[i]=="yunlong"){
								msg="yunlong";
							}
							
							
							tab1 += '<div>'+msg+'</div>';
							if((i+1)%3==0){
								tab1 += '</div>'+
								'<div class="caijiType">';
							}
						}
						tab1+='</div>';
					}
					
					var date = new Date(config.date);
					var days = newtime.getTime() - date.getTime();
					var time = parseInt(days / (1000 * 60 * 60 * 24));
					var color = '';
					if(time>10 && time<30){
						color = 'yellow';
					}else if(time>30){
						color = 'red';
					}else{
						color = 'green';
					}
					var configname = config.name;
					if(configname.length>20){
						configname = configname.substring(0,19)+"...";
					}
					tab+='<div name="collectionJobModu" class="divgezi">'+
						'<a id="startCollect" jobid="'+config.id+'" jobtypes="'+config.types+'" jobids="'+config.ids+'" href="#" style="width: auto;height: auto;">'+
							'<div style="height: 100px;color: #b1b1b1;">'+
								'<div class="JobName">'+configname+'<div class="sign sign-'+color+'"></div></div>'+
								'<div style="margin-left: 20px;">最近采集时间：'+new Date(config.date).format('yyyy-MM-dd hh:mm:ss')+'</div>'+
								'<div class="cjwcd">'+
									'<div style="font-size: 24px;color: #000000;">'+config.progressLast+'%</div>'+
									'<div style="font-size: 12px;">采集完成度</div>'+
								'</div>'+tab1+
							'</div>'+
						'</a>'+
						'<div class="footType">'+
							'<button type="button" id="startCollect" jobid="'+config.id+'" jobids="'+config.ids+'" class="startCollect" >开始采集</button>'+
							'<a id="delJob" jobid="'+config.id+'" jobids="'+config.ids+'" href="#">'+
								'<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>'+
							'</a>'+
							'<a id="copyJob" jobid="'+config.id+'" jobids="'+config.ids+'" href="#">'+
								'<span class="glyphicon glyphicon-duplicate" aria-hidden="true"></span>'+
							'</a>'+
							'<a id="editJob1" jobid="'+config.id+'" jobtypes="'+config.types+'" jobids="'+config.ids+'" href="#">'+
								'<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>'+
							'</a>'+
						'</div>'+
					'</div>';
				});
				$("#tab-one").append(tab);
			}
		});
	}
    $(document).on("click", "#returnStep1", function () {
    	
    	$("#collectionName1").html();
		$("#collectionName1").attr("colId","");
		$("#collectionType1").html();
		$("#collectionDate1").html();
		$("#collectionNum1").html();
		$("#collectionNum1").attr("num","");
		$("#collectionStart").attr("jobids","");
		
		$("#collectionStart").css("background-color","");
		$("#collectionStart").css("color","");
		$("#collectionStart").html("采集开始");
		$('#collectionJdt').css('display','none');
		$('#logMessage').empty();
		info();
		jobid="";
    	$("#step-3").css('display','none');
    	$("#step-1").css('display','block');
    });
    
    var insertIds;
	$(document).on("click", "#startCollect", function () {
		insertIds = $(this).attr("jobids");
		var id = $(this).attr("jobid");
		$.ajax({
			url: getRootPath() + '/project/searchConfigJobById',
			type: 'post',
			data:{
				id : id
			},
			success: function (data) {
				_.each(data, function(config, index){
					var msg = "";
					if(config.types.indexOf("svn")!=-1){
						msg += "SVN"+"/";
					}
					if(config.types.indexOf("dts")!=-1){
						msg += "DTS"+"/";
					}
					if(config.types.indexOf("git")!=-1){
						msg += "Git"+"/";
					}
					if(config.types.indexOf("tmss")!=-1){
						msg += "TMSS"+"/";
					}
					if(config.types.indexOf("icp-ci")!=-1){
						msg += "ICP-CI"+"/";
					}
					if(config.types.indexOf("smartIDE")!=-1){
						msg += "SmartIDE"+"/";
					}
					if(config.types.indexOf("yunlong")!=-1){
						msg += "yunlong"+"/";
					}
					if(msg.indexOf("/")){
						msg = msg.substring(0,msg.length-1)
					}
					
					var configname = config.name;
					if(configname.length>20){
						configname = configname.substring(0,19)+"...";
					}
					$("#collectionName1").html(configname);
					$("#collectionType1").html("采集项："+msg);
					$("#collectionDate1").html("最近启动时间："+new Date(config.date).format('yyyy-MM-dd hh:mm:ss'));
					$("#collectionNum1").html("总采集次数："+config.num);
					$("#collectionNum1").attr("num",config.num);
					$("#collectionStart").attr("jobids",config.selectedIds);
				});
				$("#collectionName1").attr("colId",id);
				$("#collectionTime1").html("采集耗时：00:00:00");
				$("#collectionResult").html("未开始");
				$("#collectionResult").css("background-color","#cecece");
			}
		});
		$("#step-1").css('display','none');
    	$("#step-3").css('display','block');
	});
	
	$(document).on("click", "#saveCollectionUrl", function () {
//		var ids = "";
		var selectedIds = "";
//		var types = "";
		var name = $("#colletionName").val();
		if(!name){
			toastr.error("采集名称不能为空，请修改之后再保存");
			return;
		}
		
		_.each($("div[name=isSelect]"), function(config, index){
			var tabname = $(config).parent("li").attr("tabname");
			tabname = tabname.substring(4);
			if($(config).attr("isSelectFlag")!="0"){
				return true;
			}
			if(tabname=='svn'){
				selectedIds += '2';
			}else if(tabname=='git') {
				selectedIds += '3';
			}else if(tabname=='dts') {
				selectedIds += '1';
			}else if(tabname=='tmss') {
				selectedIds += '4';
			}else if(tabname=='icp-ci') {
				selectedIds += '5';
			}else if(tabname=='smartIDE') {
				selectedIds += '6';
			}else if(tabname=='yunlong'){
				selectedIds += '7';
			}
		});
		
		var repositoryInfos = [];
		
		_.each($('#svnTable tbody tr'), function(tr, index){
			repositoryInfos.push({
				type:2,
				id:$(tr.cells[0].children[0]).attr("value"),
				url:tr.cells[0].children[0].innerHTML,
				baseLineVersion:tr.cells[1].children[0].innerHTML,
				excludeRevision:tr.cells[2].children[0].innerHTML,
				excludePath:tr.cells[3].children[0].innerHTML,
				otherAccount:$(tr.cells[4].children[0]).attr("value")
			});
		});
		_.each($('#gitTable tbody tr'), function(tr, index){
			repositoryInfos.push({
				type:3,
				id:$(tr.cells[0].children[0]).attr("value"),
				url:tr.cells[0].children[0].innerHTML,
				branch:tr.cells[1].children[0].innerHTML,
				baseLineVersion:tr.cells[2].children[0].innerHTML,
				excludeRevision:tr.cells[3].children[0].innerHTML,
				excludePath:tr.cells[4].children[0].innerHTML,
				otherAccount:$(tr.cells[5].children[0]).attr("value")
			});
		});
		_.each($('#dtsTable tbody tr'), function(tr, index){
			repositoryInfos.push({
				type:1,
				id:$(tr.cells[0].children[0]).attr("value"),
				url:tr.cells[0].children[0].innerHTML,
				otherAccount:$(tr.cells[1].children[0]).attr("value")
			});
		});
		_.each($('#tmssTable tbody tr'), function(tr, index){
			repositoryInfos.push({
				type:4,
				id:$(tr.cells[0].children[0]).attr("value"),
				url:tr.cells[0].children[0].innerHTML,
				branch:tr.cells[1].children[0].innerHTML,
				otherAccount:$(tr.cells[2].children[0]).attr("value")
			});
		});
		_.each($('#icp-ciTable tbody tr'), function(tr, index){
			repositoryInfos.push({
				type:5,
				id:$(tr.cells[0].children[0]).attr("value"),
				url:tr.cells[0].children[0].innerHTML,
				excludePath:tr.cells[1].children[0].innerHTML,
				excludeRevision:tr.cells[2].children[0].innerHTML,
			});
		});
		_.each($('#smartIDETable tbody tr'), function(tr, index){
			repositoryInfos.push({
				type:6,
				id:$(tr.cells[0].children[0]).attr("value"),
				url:tr.cells[0].children[0].innerHTML,
				branch:tr.cells[1].children[0].innerHTML,
			});
		});
		_.each($('#yunlongTable tbody tr'), function(tr, index){
			repositoryInfos.push({
				type:7,
				id:$(tr.cells[0].children[0]).attr("value"),
				url:tr.cells[0].children[0].innerHTML,
				branch:tr.cells[1].children[0].innerHTML,
				excludeRevision:$(tr.cells[0].children[0]).attr("data-id"),
			});
		});
		
		var input={
				projNo:projNo,
				selectedIds:selectedIds,
				name : name,
				id : jobid,
				repositoryInfos:repositoryInfos
		};
		
		$.ajax({
			url: getRootPath() + '/svnTask/saveurl1',
			type: 'post',
			async: false,//是否异步，true为异步
			dataType:"json",      
            contentType:"application/json",       
			data:JSON.stringify(input),
			success: function (data) {
			}
		});
		

		info();
		jobid="";
		$("#step-1").css('display','block');
    	$("#step-2").css('display','none');
	});
	$(document).on("click", "#retButton", function () {
		Ewin.confirm({ message: "此操作会撤销当前配置的采集数据，确认执行吗？" }).on(function (e) {
			if (!e) {
                return;
            }
//			_.each($("span[name=del]"), function(config, index){
//				$(config).click();
//			});
//			var name = $("#colletionName").val();
//			if(name&&jobid!=""){
//				$.ajax({
//					url: getRootPath() + '/project/saveConfigJob',
//					type: 'post',
//					async: false,//是否异步，true为异步
//					data:{
//						id : jobid,
//						no : projNo,
//						name : name
//					},
//					success: function (data) {
//					}
//				});
//			}
//			info();
			$("div[name=isSelect]").css("background-color","#c5c5c5");
			$("div[name=isSelect]").attr("isSelectFlag","1");
			jobid="";
			$("#step-1").css('display','block');
	    	$("#step-2").css('display','none');
		});
	});
	$(document).on("click", "a[id^='editJob']", function () {
		jobid = $(this).attr("jobid");
		var jobtypes = $(this).attr("jobtypes");
		var types = jobtypes.split(";");
		for(var i=0;i<types.length;i++){
			$("#isSelect-"+types[i]).css("background-color","#ff0000");
			$("#isSelect-"+types[i]).attr("isSelectFlag","0");
		}
		var jobname = "";
		if($(this).attr("id")=="editJob1"){
			jobname = $(this).parent('div').parent('div').find("div[class='JobName']").text()
		}else{
			jobname = $(this).find("div[class='JobName']").text()
		}
		$("#colletionName").val(jobname);
		$.ajax({
			url: getRootPath() + '/project/searchConfigInfoByJob',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				ids : $(this).attr("jobids")
			},
			success: function (data) {
				$("#svnTable tbody tr").remove();
				$("#gitTable tbody tr").remove();
				$("#dtsTable tbody tr").remove();
				$("#tmssTable tbody tr").remove();
				$("#icp-ciTable tbody tr").remove();
				$("#smartIDETable tbody tr").remove();
				$("#yunlongTable tbody tr").remove();
				_.each(data, function(config, index){
					var configname = config.type;
					//1:DTS,2:SVN,3:Git,4:TMSS,5:ci,6:smartIDE,7:yunlong
					if(configname=="1"){
						dtsAddDo(config,"dts");
					}else if(configname=="2"){
						svnAddDo(config,"svn");
					}else if(configname=="3"){
						gitAddDo(config,"git");
					}else if(configname=="4"){
						tmssAddDo(config,"tmss");
					}else if(configname=="5"){
						icpCiAddDo(config,"icp-ci");
					}else if(configname=="6"){
						smartIDEAddDo(config,"smartIDE");
					}else if(configname=="7"){
						yunlongAddDo(config,"yunlong");
					}
				});
				hasDate("dts");
				hasDate("svn");
				hasDate("git");
				hasDate("tmss");
				hasDate("icp-ci");
				hasDate("smartIDE");
				hasDate("yunlong");
			}
		});
		$("#step-1").css('display','none');
    	$("#step-2").css('display','block');
	});
	$(document).on("click", "a[id=delJob]", function () {
		var id = $(this).attr("jobid");
		var ids = $(this).attr("jobids");
		Ewin.confirm({ message: "确认删除这个采集任务吗？" }).on(function (e) {
			if (!e) {
                return;
            }
			$.ajax({
				url: getRootPath() + '/project/delJob',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					id : id,
					ids : ids
				},
				success: function (data) {
					
				}
			});
			info();
		});
	});
	$(document).on("click", "a[id=copyJob]", function () {
		var id = $(this).attr("jobid");
		var ids = $(this).attr("jobids");
		$.ajax({
			url: getRootPath() + '/project/copyJob',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				id : id,
				ids : ids
			},
			success: function (data) {
				
			}
		});
		info();
	});
	$(document).on("click", "span[name=del]", function () {
		var id = $(this).parent('td').attr("value");
		var del = this;
		Ewin.confirm({ message: "此操作会删除当前配置的采集数据，确认执行吗？" }).on(function (e) {
			if (!e) {
                return;
            }
//			$.ajax({
//				url: getRootPath() + '/svnTask/delurl1',
//				type: 'post',
//				async: false,//是否异步，true为异步
//				data:{
//					no : projNo,
//					id : id
//				},
//				success: function (data) {
//					
//				}
//			});
			$(del).parents('tr:first').remove();
			hasDate($(del).attr('tabid'));
		});
	});
	
	var editTrData='';
	$(document).on("click", "span[name=edit]", function () {
		var id = $(this).attr('tabid');
		$("div[id$='Modal']").css('display','none');
		$("#"+id+"Modal").css('display','flex');
		$("#"+id+"Modal").find("input").val("");
		$('#'+id+'OtherAcc').prop("checked",false);
		editTr(id,$(this).parent('td').parent('tr').children("td"));
		$("#configuroute").modal("show");
		$("#configuroute").attr("data_obj",id);
		editTrData = $(this).parent('td').parent('tr');
		$(this).parent('td').parent('tr').remove();
	});
	
	$(document).on("click","button[id^='addData']",function(){
		editTrData='';
		var id = $(this).parent('div').parent('div').attr('tabid');
		$("div[id$='Modal']").css('display','none');
		$("#"+id+"Modal").css('display','flex');
		$("#"+id+"Modal").find("input").val("");
		$('#'+id+'OtherAcc').prop("checked",false);
		$("#configuroute").modal("show");
		$("#configuroute").attr("data_obj",id);
	});
	$(document).on("click","button[id='retsaveUrl']",function(){

		var id = $("#configuroute").attr("data_obj");
		$("#"+id+"Table tbody").append(editTrData);
		$("#configuroute").attr("data_id","");
		$("#configuroute").modal("hide");
		editTrData='';
		$("#microServiceName").empty();
		$("#microServiceVersion").empty();
	});
	$(document).on("click","button[id='saveUrl']",function(){
		var id = $("#configuroute").attr("data_obj");
		var data_id = $("#configuroute").attr("data_id");
		var flag = '1';
		if(id=="dts"){
			flag = dtsAdd(data_id);
		}else if(id=="svn"){
			flag = svnAdd(data_id);
		}else if(id=="git"){
			flag = gitAdd(data_id);
		}else if(id=="tmss"){
			flag = tmssAdd(data_id);
		}else if(id=="icp-ci"){
			flag = icpCiAdd(data_id);
		}else if(id=="smartIDE"){
			flag = smartIDEAdd(data_id);
		}else if(id=="yunlong"){
			flag = yunlongAdd(data_id);
		}
		
		if(flag != '0'){
			//$("span[value='"+data_id+"']").parent('td').parent('tr').remove();
			hasDate(id);
			$("#configuroute").attr("data_id","");
			$("#configuroute").modal("hide");
		}
	});
	$(document).on("click", "div[name=isSelect]", function () {
		var isSelect = $(this).attr("isSelectFlag");
		if(isSelect=="0"){
			$(this).css("background-color","#c5c5c5");
			$(this).attr("isSelectFlag","1");
		}else if(isSelect=="1"){
			$(this).css("background-color","#ff0000");
			$(this).attr("isSelectFlag","0");
		}else{
			$(this).css("background-color","#ff0000");
			$(this).attr("isSelectFlag","0");
		}
	});
	function hasDate(id){
		var len = $("#"+id+"Table tbody tr").length;
		if(len>0){
			$("li[tabname=tab-"+id+"] a").addClass("hasDate");
			$("li[tabname=tab-"+id+"] a div").css("display","block");
			$("li[tabname=tab-"+id+"] a div").html(len);
		}else{
			$("li[tabname=tab-"+id+"] a").removeClass("hasDate");
			$("li[tabname=tab-"+id+"] a div").css("display","none");
		}
	}
	function editTr(configname,tds){
		$("#configuroute").attr("data_id",$($(tds[0]).children("span")[0]).attr("value"));
		$('#'+configname+'urlNew').val($($(tds[0]).children("span")[0]).attr("title"));
		if(configname=="svn"){
			$('#'+configname+'Base').val($($(tds[1]).children("span")[0]).attr("title"));
			$('#'+configname+'Version').val($($(tds[2]).children("span")[0]).attr("title"));
			$('#'+configname+'Path').val($($(tds[3]).children("span")[0]).attr("title"));
			editOtherAcc(configname,$($(tds[4]).children("span")[0]).attr("value"));
		}else if(configname=="git"){
			$('#'+configname+'Version').val($($(tds[1]).children("span")[0]).attr("title"));
			$('#'+configname+'Base').val($($(tds[2]).children("span")[0]).attr("title"));
			$('#'+configname+'Commit').val($($(tds[3]).children("span")[0]).attr("title"));
			$('#'+configname+'Path').val($($(tds[4]).children("span")[0]).attr("title"));
			editOtherAcc(configname,$($(tds[5]).children("span")[0]).attr("value"));
		}else if(configname=="dts"){
			//$('#'+configname+'Version').val($($(tds[1]).children("span")[0]).attr("title"));
			editOtherAcc(configname,$($(tds[1]).children("span")[0]).attr("value"));
		}else if(configname=="tmss"){
			$('#'+configname+'Version').val($($(tds[1]).children("span")[0]).attr("title"));
			editOtherAcc(configname,$($(tds[2]).children("span")[0]).attr("value"));
		}else if(configname=="icp-ci"){
			$('#icpVersion').val($($(tds[1]).children("span")[0]).attr("title"));
			$('#icpPath').val($($(tds[2]).children("span")[0]).attr("title"));
		}else if(configname=="smartIDE"){
			$('#pbiName').val($($(tds[1]).children("span")[0]).attr("title"));
		}
	}
	
	function editOtherAcc(configname,value){
		if(value==0){
			$('#'+configname+'OtherAcc').prop('checked',true);
		}else{
			$('#'+configname+'OtherAcc').prop("checked",false);
		}
	}
	function dtsAddDo(config,counter){
		var tab = "";
		tab+='<tr>'+
			'<td><span value="'+config.id+'" title="'+valueIsNull(config.url)+'">'+valueIsNull(config.url)+'</span></td>';
			/*'<td><span title="'+valueIsNull(config.branch)+'">'+valueIsNull(config.branch)+'</span></td>';*/
		if(config.otherAccount==0){
			tab+='<td><span value="'+config.otherAccount+'">是</span></td>';
		}else{
			tab+='<td><span value="'+config.otherAccount+'">否</span></td>';
		}
		tab+='<td value="'+config.id+'">'+
			'<span name="edit" tabid="dts" class="glyphicon glyphicon-edit glyphiconNew" aria-hidden="true"></span>'+
			'<span name="del" tabid="dts" class="glyphicon glyphicon-trash glyphiconNew" aria-hidden="true"></span></td>'+
			"</tr>";
		$("#dtsTable tbody").append(tab);
	}
	function svnAddDo(config,counter){
		var tab = "";
		tab+='<tr>'+
				'<td><span value="'+config.id+'" title="'+valueIsNull(config.url)+'">'+valueIsNull(config.url)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.baseLineVersion)+'">'+valueIsNull(config.baseLineVersion)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.excludeRevision)+'">'+valueIsNull(config.excludeRevision)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.excludePath)+'">'+valueIsNull(config.excludePath)+'</span></td>';
		if(config.otherAccount==0){
			tab+='<td><span value="'+config.otherAccount+'">是</span></td>';
		}else{
			tab+='<td><span value="'+config.otherAccount+'">否</span></td>';
		}
		tab+='<td value="'+config.id+'">'+
			'<span name="edit" tabid="svn" class="glyphicon glyphicon-edit glyphiconNew" aria-hidden="true"></span>'+
			'<span name="del" tabid="svn" class="glyphicon glyphicon-trash glyphiconNew" aria-hidden="true"></span></td>'+
			"</tr>";
		$("#svnTable tbody").append(tab);
	}
	function gitAddDo(config,counter){
		var tab = "";
		tab+='<tr>'+
				'<td><span value="'+config.id+'" title="'+valueIsNull(config.url)+'">'+valueIsNull(config.url)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.branch)+'">'+valueIsNull(config.branch)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.baseLineVersion)+'">'+valueIsNull(config.baseLineVersion)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.excludeRevision)+'">'+valueIsNull(config.excludeRevision)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.excludePath)+'">'+valueIsNull(config.excludePath)+'</span></td>';
		if(config.otherAccount==0){
			tab+='<td><span value="'+config.otherAccount+'">是</span></td>';
		}else{
			tab+='<td><span value="'+config.otherAccount+'">否</span></td>';
		}
		tab+='<td value="'+config.id+'">'+
			'<span name="edit" tabid="git" class="glyphicon glyphicon-edit glyphiconNew" aria-hidden="true"></span>'+
			'<span name="del" tabid="git" class="glyphicon glyphicon-trash glyphiconNew" aria-hidden="true"></span></td>'+
			"</tr>";
		$("#gitTable tbody").append(tab);
	}
	function tmssAddDo(config,counter){
		var tab = "";
		tab+='<tr>'+
				'<td><span value="'+config.id+'" title="'+valueIsNull(config.url)+'">'+valueIsNull(config.url)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.branch)+'">'+valueIsNull(config.branch)+'</span></td>';
		if(config.otherAccount==0){
			tab+='<td><span value="'+config.otherAccount+'">是</span></td>';
		}else{
			tab+='<td><span value="'+config.otherAccount+'">否</span></td>';
		}
		tab+='<td value="'+config.id+'">'+
			'<span name="edit" tabid="tmss" class="glyphicon glyphicon-edit glyphiconNew" aria-hidden="true"></span>'+
			'<span name="del" tabid="tmss" class="glyphicon glyphicon-trash glyphiconNew" aria-hidden="true"></span></td>'+
			"</tr>";
		$("#tmssTable tbody").append(tab);
	}
	function icpCiAddDo(config,counter){
		var tab = "";
		tab+='<tr>'+
				'<td><span value="'+config.id+'" title="'+valueIsNull(config.url)+'">'+valueIsNull(config.url)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.excludePath)+'">'+valueIsNull(config.excludePath)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.excludeRevision)+'">'+valueIsNull(config.excludeRevision)+'</span></td>';
		tab+='<td value="'+config.id+'">'+
			'<span name="edit" tabid="icp-ci" class="glyphicon glyphicon-edit glyphiconNew" aria-hidden="true"></span>'+
			'<span name="del" tabid="icp-ci" class="glyphicon glyphicon-trash glyphiconNew" aria-hidden="true"></span></td>'+
			"</tr>";
		$("#icp-ciTable tbody").append(tab);
	}
	function smartIDEAddDo(config,counter){
		var tab = "";
		tab+='<tr>'+
				'<td><span value="'+config.id+'" title="'+valueIsNull(config.url)+'">'+valueIsNull(config.url)+'</span></td>'+
				'<td><span title="'+valueIsNull(config.branch)+'">'+valueIsNull(config.branch)+'</span></td>';
		tab+='<td value="'+config.id+'">'+
			'<span name="edit" tabid="smartIDE" class="glyphicon glyphicon-edit glyphiconNew" aria-hidden="true"></span>'+
			'<span name="del" tabid="smartIDE" class="glyphicon glyphicon-trash glyphiconNew" aria-hidden="true"></span></td>'+
			"</tr>";
		$("#smartIDETable tbody").append(tab);
	}
	function yunlongAddDo(config,counter) {
		var tab = "";
		tab+='<tr>'+
		'<td><span data-id="' + valueIsNull(config.excludeRevision) + '" value="'+config.id+'" title="'+valueIsNull(config.url)+'">'+valueIsNull(config.url)+'</span></td>'+
		'<td><span title="'+valueIsNull(config.branch)+'">'+valueIsNull(config.branch)+'</span></td>';
	
		tab+='<td value="'+config.id+'">'+
		'<span name="edit" tabid="yunlong" class="glyphicon glyphicon-edit glyphiconNew" aria-hidden="true"></span>'+
		'<span name="del" tabid="yunlong" class="glyphicon glyphicon-trash glyphiconNew" aria-hidden="true"></span></td>'+
		"</tr>";
		$("#yunlongTable tbody").append(tab);
		
	}
	
	
	function valueIsNull(value){
		if(value){
			return value;
		}else{
			return "";
		}
	}
	
	
	function getRequestJson(ids,allTr,type,urls,branchs,scopes,otherAccounts,svnVersions,svnPaths){
		allTr +='{"type":"'+type+'",'+
				'"url":"'+urls[0].value+'",';
		if(ids!=null){
			allTr +='"id":"'+ids+'",';
		}
		if(branchs!=null){
			allTr +='"branch":"'+branchs[0].value+'",';
		}
		if(scopes!=null){
			allTr +='"scope":"'+scopes[0].value+'",';
		}
		if(svnVersions!=null){
			allTr +='"excludeRevision":"'+svnVersions[0].value+'",';
		}
		if(svnPaths!=null){
			allTr +='"excludePath":"'+svnPaths[0].value+'",';
		}
		if(otherAccounts!=null){
			if($(otherAccounts[0]).prop("checked")){
				allTr +='"otherAccount":"0"';
			}else{
				allTr +='"otherAccount":"1"';
			}
		}else{
			allTr = allTr.substring(0,allTr.length-1)
		}
		allTr +='},';
		return allTr;
	}
	
	function svnAdd(id){
		var allTr = "";
		var svnurls = $('#svnurlNew');
		var svnBase = $('#svnBase')
		var svnVersions = $('#svnVersion');//版本过滤
		var svnPaths = $('#svnPath');//路径过滤条件
		var svnOtherAccs = $('#svnOtherAcc');
		
		var flag=false;
		_.each($('#svnTable tbody tr'), function(tr, index){
			if(svnurls[0].value==tr.cells[0].children[0].innerHTML){
				flag=true;
				return false;
			};
		});
		if(flag){
			toastr.error("采集路径不能和已配置的路径重复");
			return '0';
		}
		if(svnBase[0].value == null || svnBase[0].value ==""){
			toastr.error("基线版本号不能为空！");
			return '0';
		}
		
		var config = {
				id:id?id:'',
				url:svnurls[0].value,
				baseLineVersion:svnBase[0].value,
				excludeRevision:svnVersions[0].value,
				excludePath:svnPaths[0].value,
				otherAccount:$(svnOtherAccs[0]).prop("checked")?'0':'1'
		}
		svnAddDo(config,"svn");
//		allTr = getRequestJson(id,allTr,2,svnurls,null,null,svnOtherAccs,svnVersions,svnPaths);
//		
//		var jsonStr = '{"projNo":"'+projNo+'","repositoryInfos":['+allTr.substring(0,allTr.length-1)+']}';
//		$.ajax({
//			url: getRootPath() + '/svnTask/saveurl1',
//			type: 'post',
//			dataType: "json",
//			contentType : 'application/json;charset=utf-8', //设置请求头信息
//			data:jsonStr,
//			success: function (data) {
//				_.each(data, function(config, index){
//					svnAddDo(config,"svn");
//				});
//				hasDate("svn");
//				$("#configuroute").modal("hide");
//			}
//		});
	}
	function dtsAdd(id){
		var allTr = "";
		var dtsurls = $('#dtsurlNew');
		//var dtsVersions = $('#dtsVersion');//dts为版本
		var dtsOtherAccs = $('#dtsOtherAcc');
		
		var flag=false;
		_.each($('#dtsTable tbody tr'), function(tr, index){
			if(dtsurls[0].value==tr.cells[0].children[0].innerHTML){
				flag=true;
				return false;
			};
		});
		if(flag){
			toastr.error("采集路径不能和已配置的路径重复");
			return '0';
		}
		
		
		var config = {
				id:id?id:'',
				url:dtsurls[0].value,
				otherAccount:$(dtsOtherAccs[0]).prop("checked")?'0':'1'
		}
		dtsAddDo(config,"dts");
//		allTr = getRequestJson(id,allTr,1,dtsurls,null,null,dtsOtherAccs);
//		
//		var jsonStr = '{"projNo":"'+projNo+'","repositoryInfos":['+allTr.substring(0,allTr.length-1)+']}';
//		$.ajax({
//			url: getRootPath() + '/svnTask/saveurl1',
//			type: 'post',
//			dataType: "json",
//			contentType : 'application/json;charset=utf-8', //设置请求头信息
//			data:jsonStr,
//			success: function (data) {
//				_.each(data, function(config, index){
//					dtsAddDo(config,"dts");
//				});
//				hasDate("dts");
//				$("#configuroute").modal("hide");
//			}
//		});
	}
	function gitAdd(id){
		var allTr = "";
		var giturls = $('#giturlNew');
		var gitVersions = $('#gitVersion');//git为分支
		var gitOtherAccs = $('#gitOtherAcc');
		var gitPaths = $('#gitPath');//路径过滤条件
		var gitCommit = $('#gitCommit');//版本过滤条件
		var gitBase = $('#gitBase');// 基线版本号
		var flag=false;
		_.each($('#gitTable tbody tr'), function(tr, index){
			if(giturls[0].value==tr.cells[0].children[0].innerHTML && 
					gitVersions[0].value==tr.cells[1].children[0].innerHTML ){
				flag=true;
				return false;
			};
		});
		if(flag){
			toastr.error("采集路径不能和已配置的路径重复");
			return '0';
		}
		if(gitBase[0].value == null || gitBase[0].value ==""){
			toastr.error("基线版本号不能为空！");
			return '0';
		}
		
		var config = {
				id:id?id:'',
				url:giturls[0].value,
				branch:gitVersions[0].value,
				baseLineVersion:gitBase[0].value,
				excludeRevision:gitCommit[0].value,
				excludePath:gitPaths[0].value,
				otherAccount:$(gitOtherAccs[0]).prop("checked")?'0':'1'
		}
		gitAddDo(config,"git");
//		allTr = getRequestJson(id,allTr,3,giturls,gitVersions,null,gitOtherAccs,gitCommit,gitPaths);
//		
//		var jsonStr = '{"projNo":"'+projNo+'","repositoryInfos":['+allTr.substring(0,allTr.length-1)+']}';
//		$.ajax({
//			url: getRootPath() + '/svnTask/saveurl1',
//			type: 'post',
//			dataType: "json",
//			contentType : 'application/json;charset=utf-8', //设置请求头信息
//			data:jsonStr,
//			success: function (data) {
//				_.each(data, function(config, index){
//					gitAddDo(config,"git");
//				});
//				hasDate("git");
//				$("#configuroute").modal("hide");
//			}
//		});
	}
	function tmssAdd(id){
		var allTr = "";
		var tmssurls = $('#tmssurlNew');
		var tmssVersions = $('#tmssVersion');//tmss为版本
		var tmssOtherAccs = $('#tmssOtherAcc');
		
		var flag=false;
		_.each($('#tmssTable tbody tr'), function(tr, index){
			if(tmssurls[0].value==tr.cells[0].children[0].innerHTML && 
					tmssVersions[0].value==tr.cells[1].children[0].innerHTML ){
				flag=true;
				return false;
			};
		});
		if(flag){
			toastr.error("采集路径不能和已配置的路径重复");
			return '0';
		}
		
		var config = {
				id:id?id:'',
				url:tmssurls[0].value,
				branch:tmssVersions[0].value,
				otherAccount:$(tmssOtherAccs[0]).prop("checked")?'0':'1'
		}
		tmssAddDo(config,"tmss");
	}
	function icpCiAdd(id){
		var allTr = "";
		var ciurls = $('#icp-ciurlNew');
		var icpVersions = $('#icpVersion');//版本过滤条件
		var icpPaths = $('#icpPath');//路径过滤条件
		
		var config = {
				id:id?id:'',
				url:ciurls[0].value,
				excludeRevision:icpPaths[0].value,
				excludePath:icpVersions[0].value,
		}
		icpCiAddDo(config,"icp-ci");
	}
	//1  = ${'#microServiceIdInput'}
	function yunlongAdd(id){
		var microServiceName = $('#microServiceName');
		var microServiceVersion = $('#microServiceVersion');
		var microServiceId = $('#microServiceIdInput')[0].value;
		var config;
		if(microServiceId){
			config = {
					id:id?id:'',
					url:microServiceName[0].value,
					branch:microServiceVersion[0].value,
					excludeRevision : microServiceId
			}
		} else {
			config = {
					id:id?id:'',
					url:microServiceName[0].value,
					branch:microServiceVersion[0].value,
			}
			
		}
		$("#microServiceName").empty();
		$("#microServiceVersion").empty();
		yunlongAddDo(config,"yunlong");
		
	}
	
	function smartIDEAdd(id){
		var allTr = "";
		var pbiId = $('#smartIDEurlNew');
		var pbiName = $('#pbiName');
		
		var flag=false;
		_.each($('#smartIDETable tbody tr'), function(tr, index){
			if(tr.cells[0].children[0].innerHTML !='' && 
					pbiId[0].value==tr.cells[0].children[0].innerHTML ){
				flag=true;
				return false;
			};
			if(tr.cells[1].children[0].innerHTML !='' && 
					pbiName[0].value==tr.cells[1].children[0].innerHTML ){
				flag=true;
				return false;
			};
		});
		if(flag){
			toastr.error("采集路径不能和已配置的路径重复");
			return '0';
		}
		
		
		var config = {
				id:id?id:'',
				url:pbiId[0].value,
				branch:pbiName[0].value,
		}
		smartIDEAddDo(config,"smartIDE");
//		allTr = getRequestJson(id,allTr,6,pbiId,pbiName);
//		
//		var jsonStr = '{"projNo":"'+projNo+'","repositoryInfos":['+allTr.substring(0,allTr.length-1)+']}';
//		$.ajax({
//			url: getRootPath() + '/svnTask/saveurl1',
//			type: 'post',
//			dataType: "json",
//			contentType : 'application/json;charset=utf-8', //设置请求头信息
//			data:jsonStr,
//			success: function (data) {
//				_.each(data, function(config, index){
//					smartIDEAddDo(config,"smartIDE");
//				});
//				hasDate("smartIDE");
//				$("#configuroute").modal("hide");
//			}
//		});
	}
	
	$(document).on("click", "#collectionStart", function () {
		var jobids = insertIds;
		$.ajax({
			url: getRootPath() + '/project/searchConfigInfoByJob',
			type: 'post',
			data: {
				ids: jobids
			},
			success: function (data) {
				if(data==null||data.length<=0){
					return;
				}
				$("#prog").css("width","0%");
				$("#prog1").text("0%");
				$("#userandpassTable").children().remove(); 
				var i = 0;
				_.each(data, function(config, index){
					var configname = config.type;
					if(configname=="1"){
						configname="dts";
					}else if(configname=="2"){
						configname="svn";
					}else if(configname=="3"){
						configname="git";
					}else if(configname=="4"){
						configname="tmss";
					}else if(configname=="5"){
						configname="icp-ci";
					}else if(configname=="6"){
						console.log("PPPPPPPP");
					}
					else if(configname=="7"){
						configname="yunlong";
						$.ajax({
							url: getRootPath() + '/yunlongMicroController/getPipelineInfoBatch',
							type: 'post',
							data: {
								ids: jobids,
								serviceId:serviceId
							},
							success : function(data){
								conosole.log("sucesssssss");
							}
						});
					}
					if(config.otherAccount=="1"){
						clearCookie(configname+'user'+config.id);
						clearCookie(configname+'pass'+config.id);
						return true;
					}
					var tab = '<tr align="center" valign="middle">'+
							'<th width="100" style="text-align: right; font-size: 12px;padding-right: 8px">'+configname.toUpperCase()+'路径：</th>'+
							'<th title="'+config.url+'" style="display: inline-block;width: 260px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">'+
							config.url+'</th>'+
							'</tr>'+
							'<tr align="center" valign="middle">'+
							'<th width="100" style="text-align: right; font-size: 12px;padding-right: 8px">'+configname.toUpperCase()+'帐户：</th>'+
							'<th><input type="text" name="needSaveCookic" id="'+configname+'user'+config.id+'" style="height: 28px; width: 259px;" /></th>'+
							'</tr>'+
							'<tr>'+
							'<th width="100" style="text-align: right; font-size: 12px;padding-right: 8px">'+configname.toUpperCase()+'密码：</th>'+
							'<th><input type="password" name="needSaveCookic" id="'+configname+'pass'+config.id+'" style="height: 28px; width: 259px;" /></th>'+
							'</tr>';
					$("#userandpassTable").append(tab);
					$("#"+configname+'user'+config.id).val(getCookie(configname+'user'+config.id));
					$("#"+configname+'pass'+config.id).val(getCookie(configname+'pass'+config.id));
					i++;
				});
				if(i==0){
					$("#saveuserandpass").click();
					return;
				}
				$('#userandpass').modal({
					backdrop: 'static',
					keyboard: false
			    });
				$('#userandpass').modal('show');
			}
		});
		
	});
	
	//定时器对象
	var timer = null;
	var logid = "";//日志id
	var token = "";
	var totalCount = 0;//总记录
	var haveExcute = 0;
	var succ = 0;
	$(document).on("click", "#saveuserandpass", function () {
		haveExcute = 0;
		$("#collectionStart").html("采集中...")
		$("#collectionStart").css("background-color","#ffffff");
		$("#collectionStart").css("color","#e41f2b");
		
		$('#collectionJg').css('display','none');
		$('#collectionJdt').css('display','block');
		//定时器对象
		timer = null;
		logNum = 1;
		logid = "";
		totalCount = 0;
		token = new Date().getTime();
		
		var users = $("input[name='needSaveCookic']");
		_.each(users, function(user, index){
			setCookie($(user).attr("id"),$(user).val());
		});
		var msg = "";
		//判断是否配置了对应的采集路径
		$.ajax({
			url: getRootPath() + '/project/searchConfigInfoByJob',
			type: 'post',
			data: {
				ids: jobids
			},
			success: function (data) {
				if(data==null||data.length<=0){
					return false;
				}
				totalCount = data.length;
				_.each(data, function(config, index){
					if(config.type=="6"){
						if(config.url && config.branch){
							totalCount +=1
						}
					}
				});
				_.each(data, function(config, index){
					var configname = config.type;
					msg = 'url:'+config.url;
					if(msg.length>30){
						msg = msg.substring(0,29)+"...";
					}
					if(configname=="1"){
						logmessage('',new Date().format('yyyy-MM-dd hh:mm:ss')+'  dts采集('+msg+")",'Start');
						$.ajax({
							url: getRootPath() + '/acquire/dts',
							type: 'post',
							data: {
								no: projNo,
								token : token,
								id: config.id ,//项目配置ID
							},
							success : function(data){
							}
						});
					}else if(configname=="2"){
						logmessage('',new Date().format('yyyy-MM-dd hh:mm:ss')+'  svn采集('+msg+")",'Start');
						$.ajax({
							url: getRootPath() + '/acquire/svn',
							type: 'post',
							data: {
								no: projNo,
								token : token,
								id: config.id, //项目配置ID
							},
							success:function(data){
							}
						});
					}else if(configname=="3"){
						logmessage('',new Date().format('yyyy-MM-dd hh:mm:ss')+'  git采集('+msg+")",'Start');
						$.ajax({
							url: getRootPath() + '/git/gitTask',
							type: 'post',
							data: {
								no: projNo,
								token : token,
								id: config.id, //项目配置ID
							},
							success:function(data){
								haveExcute +=1;
								if(data.code == 'success'){//成功标识
									succ+=1;
								}
							}
					});
					}else if(configname=="4"){
						logmessage('',new Date().format('yyyy-MM-dd hh:mm:ss')+'  tmss采集('+msg+")",'Start');
						$.ajax({
							url: getRootPath() + '/tmss/tmssTask',
							type: 'post',
							data: {
								no: projNo,
								token : token,
								id: config.id, //项目配置ID
								tmsUrl : config.url
							},
							success:function(data){
								haveExcute +=1;
								if(data.code == 'success'){//成功标识
									succ+=1;
								}
							}
						});
					}else if(configname=="5"){
						logmessage('',new Date().format('yyyy-MM-dd hh:mm:ss')+'  icp-ci采集('+msg+")",'Start');
						$.ajax({
							url: getRootPath() + '/acquire/icp-ci',
							type: 'post',
							data: {
								no: projNo,
								token : token,
								id: config.id
							},
							success:function(data){
							}
						});
					}else if(configname=="6"){
						logmessage('',new Date().format('yyyy-MM-dd hh:mm:ss')+'  SmartIDE采集('+msg+")",'Start');
						$.ajax({
							url: getRootPath() + '/personalBulidTime/insertBuildTime',
							type: 'post',
							data: {
								no: projNo,
								token : token,
								id: config.id
							},
							success:function(data){
								haveExcute +=1;
								if(data.code == 'success'){//成功标识
									succ+=1;
								}
							}
						});
						
						$.ajax({
							url: getRootPath() + '/codeCheck/insertCodeCheck',
							type: 'post',
							data: {
								no: projNo,
								token : token,
								id: config.id
							},
							success:function(data){
								haveExcute +=1;
								if(data.code == 'success'){//成功标识
									succ+=1;
								}
							}
						});
					}
				});
				
			}
		});
		//定时器对象
		timer = setInterval(closeJdt,5000);
	});
	//获取采集实时日志
	function closeJdt(){
		$.ajax({
			url: getRootPath() + '/getErrorMessage/getMessage',
			type: 'post',
			data : {
				proNo : projNo,
				token : token
			},
			success: function (data) {
				var record = data.data;
				var count = Number(data.message);//执行总数
				var sucCount = Number(data.code);//执行成功数
				var tab = "";
				if(!_.isEmpty(record)){
					_.each(record, function(config, index){
						logmessage(config.MES_TYPE,new Date().format('yyyy-MM-dd hh:mm:ss')+" > ",config.MESSAGE);
						logid += config.ID + ";";
					});
				}
				var  completeCount = count + haveExcute;
				var baifenbi = parseInt(completeCount/totalCount*100);
				var sc = succ+sucCount;
				if(totalCount >0 && completeCount == totalCount){//代表执行完成
					var endtime = new Date().getTime();
					var runtime = parseInt((endtime-token)/1000);
					var h = format2wei(parseInt(runtime/60/60));
					var m = format2wei(parseInt(runtime/60%60));
					var s = format2wei(runtime%60);
					var num = parseInt($("#collectionNum1").attr("num"))+1;
					$("#collectionTime1").html("采集耗时："+h+":"+m+":"+s);
					$("#collectionNum1").html("总采集次数："+num);
					$("#collectionNum1").attr("num",num)
					if(sc == totalCount){//全部执行成功
						$("#prog").css("width","100%");
						$("#prog1").text("100%");
						$("#collectionResult").html("成功");
						$("#collectionResult").css("background-color","#1adc21");
						clearInterval(timer);
					}
					else if(sc > 0 && sc < totalCount){
						$("#prog").css("width",baifenbi + "%");
						$("#prog1").text(baifenbi + "%");
						$("#collectionResult").html("异常");
						$("#collectionResult").css("background-color","#ff0000");
						clearInterval(timer);
					}else{
						$("#prog").css("width",baifenbi + "%");
						$("#prog1").text(baifenbi + "%");
						$("#collectionResult").html("失败");
						$("#collectionResult").css("background-color","#ff0000");
						clearInterval(timer);
					}
					
					$.ajax({
						url: getRootPath() + '/project/searchConfigReNum',
						type: 'post',
						data:{
							id : $("#collectionName1").attr("colId"),
							progressLast : baifenbi,
							num : num
						},
						success: function () {
							
						}
					});
					$("#collectionDate1").html("最近启动时间："+new Date().format('yyyy-MM-dd hh:mm:ss'));
					$("#collectionStart").html("采集开始");
					$("#collectionStart").css("background-color","");
					$("#collectionStart").css("color","");
					$("#collectionJdt").css("display","none");
					$('#collectionJg').css('display','block');
				}else{
					$("#prog").css("width",baifenbi + "%");
					$("#prog1").text(baifenbi + "%");
				}
			}
		}); 
	}
	
	var logNum = 1;
	function logmessage(type,name,message){
		var color = 'color:#ffffff;';
		if(type == 'info'){
			color =  'color: #2aff03;';
		}else if(type == 'error'){
			color = 'color: #f10000;';
		}
		var tab = '<div>'+
					'<div style="width: 20px;float:  left;border-right: 2px #ffffff solid;">'+(logNum++)+'</div>'+
					'<div style="'+color+'margin-left: 25px;">'+name+'：'+message+'</div>'+
				'</div>';
		$('#logMessage').append(tab);
	}
	
	function format2wei(val){
		if(val<9){
			val = "0"+val;
		}
		return val;
	}
	$(document).ready(function(){
		info();

	})
	
	
})
microNameInput = function(inputData) {
	
	var input_select = inputData.value;
	var option_length=$(".nameOp").length;
	
	if(option_length){
		for(var i = 0;i < option_length;i++){
			var option_value=$(".nameOp").eq(i).attr('value');
			if(input_select==option_value){
				// 这里查询服务版本
				$.ajax({
					url: getRootPath() + '/yunlongMicroController/queryVersionList',
					type: 'post',
					data:{
						microServiceId : $(".nameOp").eq(i).attr('data-id')
					},
					success: function (data) {
						$("#microServiceVersion").empty();
						$("#microServiceIdInput").attr("value",$(".nameOp").eq(i).attr('data-id'));
						
						$.each(data.data, function(i,val){
						      $("#microServiceVersion").append('<option class="versionOp" value= "' + val + '">' + val + '</option>');
						  }); 
						
					}
				});
				return;
			}
		}
	}
	$.ajax({
		url: getRootPath() + '/yunlongMicroController/queryServiceList',
		type: 'post',
		data:{
			name : input_select,
			pageNo : 0,
			pageSize : 10
		},
		success: function (data) {
			$("#browsers").empty();
			$.each(data.data, function(i,val){      
			      $("#browsers").append('<option class="nameOp" data-id= "'+val.microServiceId +'" value= "' + val.microServiceName + '"></option>');
			  }); 
			
		}
	});
	
}


