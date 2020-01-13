var projNo = "";
(function() {
	projNo = window.parent.projNo;
	var buName = window.parent.projBU;
	
	String.prototype.endWith=function(str){    
		  var reg=new RegExp(str+"$");    
		  return reg.test(this);       
	};

})()


$(document).on("change", "#importInput", function () {
	$("#filePathInfo").val($(this).val());
});

$(document).on("click", "#templateDownload", function () {
	var downloadUrl = getRootPath() + '/project/projectTemplate?templateName=project-info';
	$("#templateDownload").attr('href', downloadUrl);
});
$(document).on("click", "#downloadProj", function () {
	var downloadUrl = getRootPath() + '/project/downloadNew';
	$("#downloadProj").attr('href', downloadUrl);
});

$(document).on("click", "#submitImport", function () {
	var filePath = $("#filePathInfo").val();
	if(filePath == ''){
		alert("请先选择要导入的文件!");
	}else if(!filePath.endWith('.xlsx')){
		alert("文件格式需为xlsx");
	}else{	
		var option = {
			url: getRootPath() + "/project/import",
			type: 'POST',
			dataType: 'json',
			data:{
			},
			success: function(data){
					 alert("导入成功的项目："+data.sess+"\n导入失败的项目:"+data.err);
			}
		};
		$('#uploadDialog').modal('hide');
		$("#importForm").ajaxSubmit(option);
	}
});

function setCookie(name,value){
	var Days = 7;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ encodeURIComponent(value) + ";expires=" + exp.toGMTString()+";path=/";
}
function getCookie(name){
	   var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	   if(arr != null){
	     return decodeURIComponent(arr[2]); 
	   }else{
	     return null;
	   }
};
//清除cookie    
function clearCookie(name) {    
	var exp = new Date();  
    exp.setTime(exp.getTime() - 1);  
    var cval=getCookie(name);  
    if(cval!=null) {
    	document.cookie= name + "="+cval+";expires="+exp.toGMTString()+";path=/";
    }   
}  
function getQueryString(name) {  
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");  
    var r = window.location.search.substr(1).match(reg);  
    if (r != null) return decodeURI(r[2]);  
    return null;  
}

//保存代码统计方式
function saveCodeType(){
	var val = $("input[name='subType']:checked").val();
	var valtextmea = $("input[name='testType']:checked").val();
	$.ajax({
		url : getRootPath()+'/codeType/saveCodeType',
		type: 'post',
		data : {	no: projNo,
			    	type: val,
			    	textMeanType:valtextmea
			    },
		success : function(data){
			if(data.code == 'success'){
				toastr.success('保存成功！');
				//location.reload();
				$("#codeType").modal('hide');
			}else{
				toastr.error('保存失败!');
			}
		}
	});
};

$(document).on("click", "#refrush", function () {
	$.ajax({
		url: getRootPath() + '/project/searchConfigInfo',
		type: 'post',
		data: {
			no: projNo
		},
		success: function (data) {
			if(data==null||data.length<=0){
				$("#saveuserandpass").click();
				return;
			}
			
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
var token = "";
var countAd = 0;
var totalCount = 0;//总记录
var allCount = 0;//是否执行完成
$(document).on("click", "#saveuserandpass", function () {
	var tab = "";
	$("#cjTabs tbody").html("");
	$("#cwTabs tbody").html("");
	$('#jdt').html('<span class="cjfont">开始数据采集...</span><img src="/mvp/view/HTML/images/jdt.gif" alt="进度条" width="550px" height="40px"/><br />');
	
	//定时器对象
	 timer = null;
	 token = "";
	 countAd = 0;
	 totalCount = 0;//总记录
	 allCount = 0;//是否执行完成
	
	token = new Date().getTime();
	var jdt1 = '/mvp/view/HTML/images/jdt.gif';//正在加载进度条
	var jdt2 = '/mvp/view/HTML/images/jdt1.png';//加载完成进度条
	
	
	var scpng = '/mvp/view/HTML/images/success.png';
	var flpng = '/mvp/view/HTML/images/fail.png';
	
	var users = $("input[name='needSaveCookic']");
	_.each(users, function(user, index){
		setCookie($(user).attr("id"),$(user).val());
	});

	$('#successDiv').css('display','none');
	$('#dataAcquisition').css('display','block');
	$('#jindu').modal({
		backdrop: 'static',
		keyboard: false
    });
	$('#jindu').modal('show');
	
	//判断是否配置了对应的采集路径
	$.ajax({
		url: getRootPath() + '/project/searchConfigInfo',
		type: 'post',
		data: {
			no: projNo
		},
		success: function (data) {
			if(data==null||data.length<=0){
				$('#jdt').html('<span class="cjfont">数据采集失败...</span><img src="'+jdt2+'" alt="进度条" width="550px" height="40px"/><br />');
				var mess = "请到配置-采集参数中配置相关的采集路径" ;
				var tab = '<tr><td>&nbsp;&nbsp;<img src="/mvp/view/HTML/images/jg.png" alt="失败" width="18px" height="18px"/>'+mess+'</td></tr>';
				$("#cjTabs tbody").html(tab);
				return false;
			}
			var tab = "";
			totalCount = data.length+2;
			_.each(data, function(config, index){
				$('#jdt').html('<span class="cjfont">开始数据采集...</span><img src="'+jdt1+'" alt="进度条" width="550px" height="40px"/><br />');
				var configname = config.type;
				if(configname=="1"){
					$("#cjTabs tbody").append("<p id='dtscj' class = 'zzcj'>正在进行DTS数据采集<img src='/mvp/view/HTML/images/jzfh.gif' class='center-png'/></p>");
					$.ajax({
						url: getRootPath() + '/acquire/dts',
						type: 'post',
						data: {
							no: projNo,
							token : token,
							id: config.id ,//项目配置ID
						},
						success : function(data){
							$("#dtscj").remove();
							allCount+=1;
							var png_url = '';
							if(data.code == 'success'){
								countAd+=1;
								png_url = scpng;
							}else{
								png_url = flpng;
							}
							var record = data.data;
							if(record){
								$("#cjTabs tr:not(:first)").remove();
								tab += '<tr style="display:flex;"><td width="40%" align="left"><img src="'+png_url+'" alt="成功" width="18px" height="18px"/>&nbsp;'+ record.mesType +'</td>'
								+'<td width="30%">'+ record.zxResult +'</td>'
								+'<td width="30%">'+ record.workTime +'</td></tr>';
								$("#cjTabs tbody").prepend(tab);
							}
						}
					});
				}else if(configname=="2"){
					$("#cjTabs tbody").append("<p id='svncj' class = 'zzcj'>正在进行SVN数据采集<img src='/mvp/view/HTML/images/jzfh.gif' class='center-png'/></p>");
					$.ajax({
						url: getRootPath() + '/svnTask/svn',
						type: 'post',
						data: {
							no: projNo,
							token : token,
							id: config.id, //项目配置ID
						},
						success:function(data){
							$("#svncj").remove();
							allCount+=1;
							var png_url = '';
							if(data.code == 'success'){
								countAd+=1;
								png_url = scpng;
							}else{
								png_url = flpng;
							}
							var record = data.data;
							if(record){
								$("#cjTabs tr:not(:first)").remove();
								tab += '<tr style="display:flex;"><td width="40%" align="left"><img src="'+png_url+'" alt="成功" width="18px" height="18px"/>&nbsp;'+ record.mesType +'</td>'
								+'<td width="30%">'+ record.zxResult +'</td>'
								+'<td width="30%">'+ record.workTime +'</td></tr>';
								$("#cjTabs tbody").prepend(tab);
							}
						}
					});
				}else if(configname=="3"){
					$("#cjTabs tbody").append("<p id='gitcj' class = 'zzcj'>正在进行GIT数据采集<img src='/mvp/view/HTML/images/jzfh.gif' class='center-png'/></p>");
					$.ajax({
						url: getRootPath() + '/git/gitTask',
						type: 'post',
						data: {
							no: projNo,
							token : token,
							id: config.id, //项目配置ID
						},
						success:function(data){
							$("#gitcj").remove();
							allCount+=1;
							var png_url = '';
							if(data.code == 'success'){
								countAd+=1;
								png_url = scpng;
							}else{
								png_url = flpng;
							}
							var record = data.data;
							if(record){
								$("#cjTabs tr:not(:first)").remove();
								tab += '<tr style="display:flex;"><td width="40%" align="left"><img src="'+png_url+'" alt="成功" width="18px" height="18px"/>&nbsp;'+ record.mesType +'</td>'
								+'<td width="30%">'+ record.zxResult +'</td>'
								+'<td width="30%">'+ record.workTime +'</td></tr>';
								$("#cjTabs tbody").prepend(tab);
							}
						}
				});
				}else if(configname=="4"){
					$("#myTable tbody").append("<p id='tmscj' class = 'zzcj'>正在进行TMS数据采集<img src='/mvp/view/HTML/images/jzfh.gif' class='center-png'/></p>");
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
							$("#tmscj").remove();
							allCount+=1;
							var png_url = '';
							if(data.code == 'success'){
								countAd+=1;
								png_url = scpng;
							}else{
								png_url = flpng;
							}
							$("#cjTabs tr:not(:first)").remove();
							tab += '<tr style="display:flex;"><td width="40%" align="left"><img src="'+png_url+'" alt="成功" width="18px" height="18px"/>&nbsp;'+ data.mesType +'</td>'
							+'<td width="30%">'+ data.zxResult +'</td>'
							+'<td width="30%">'+ data.workTime +'</td></tr>';
							$("#cjTabs tbody").prepend(tab);
						}
					});
				}else if(configname=="5"){
					$("#myTable tbody").append("<p id='icpcicj' class = 'zzcj'>正在进行ICPCI数据采集<img src='/mvp/view/HTML/images/jzfh.gif' class='center-png'/></p>");
					$.ajax({
						url: getRootPath() + '/acquire/icp-ci',
						type: 'post',
						data: {
							no: projNo,
							token : token,
							id: config.id
						},
						success:function(data){
							$("#icpcicj").remove();
							allCount+=1;
							var png_url = '';
							if(data.code == 'success'){
								countAd+=1;
								png_url = scpng;
							}else{
								png_url = flpng;
							}
							var record = data.data;
							$("#cjTabs tr:not(:first)").remove();
							tab += '<tr style="display:flex;"><td width="40%" align="left"><img src="'+png_url+'" alt="成功" width="18px" height="18px"/>&nbsp;'+ record.mesType +'</td>'
							+'<td width="30%">'+ record.zxResult +'</td>'
							+'<td width="30%">'+ record.workTime +'</td></tr>';
							$("#cjTabs tbody").prepend(tab);
						}
					});
				}
			});
			
			//判断smartIde是否进行了配置
			$.ajax({
				url: getRootPath() + '/personalBulidTime/isHaveConfigIde',
				type: 'post',
				data: {
					proNo: projNo
				},
				success:function(data){
					if(data.code == '200'){
						$("#cjTabs tbody").append("<p id='buildcj' class = 'zzcj'>正在进行个人构建时长数据采集<img src='/mvp/view/HTML/images/jzfh.gif' class='center-png'/></p>");
						$.ajax({
							url: getRootPath() + '/personalBulidTime/insertBuildTime',
							type: 'post',
							data: {
								projNo: projNo,
							},
							success:function(data){
								$("#buildcj").remove();
								allCount+=1;
								var png_url = '';
								if(data.code == 'success'){
									countAd+=1;
									png_url = scpng;
								}else{
									png_url = flpng;
								}
								var record = data.data;
								if(record){
									$("#cjTabs tr:not(:first)").remove();
									tab += '<tr style="display:flex;"><td width="40%" align="left"><img src="'+png_url+'" alt="成功" width="18px" height="18px"/>&nbsp;'+ record.mesType +'</td>'
									+'<td width="30%">'+ record.zxResult +'</td>'
									+'<td width="30%">'+ record.workTime +'</td></tr>';
									$("#cjTabs tbody").prepend(tab);
								}
							}
						});
						
						$("#cjTabs tbody").append("<p id='codecheckcj' class = 'zzcj'>正在进行代码检视意见数据采集<img src='/mvp/view/HTML/images/jzfh.gif' class='center-png'/></p>");
						$.ajax({
							url: getRootPath() + '/codeCheck/insertCodeCheck',
							type: 'post',
							data: {
								no: projNo,
							},
							success:function(data){
								$("#codecheckcj").remove();
								allCount+=1;
								var png_url = '';
								if(data.code == 'success'){
									countAd+=1;
									png_url = scpng;
								}else{
									png_url = flpng;
								}
								var record = data.data;
								if(record){
									$("#cjTabs tr:not(:first)").remove();
									tab += '<tr style="display:flex;"><td width="40%" align="left"><img src="'+png_url+'" alt="成功" width="18px" height="18px"/>&nbsp;'+ record.mesType +'</td>'
									+'<td width="30%">'+ record.zxResult +'</td>'
									+'<td width="30%">'+ record.workTime +'</td></tr>';
									$("#cjTabs tbody").prepend(tab);
								}
							}
						});
					}else{
						allCount+=2;
						return false;
					}
				}
			});
			
			
			
		}
	});
	timer = window.setInterval("closeJdt()",5000);
	
});
//关闭进度条窗口
function closeJdt(){
	var jdt2 = '/mvp/view/HTML/images/jdt1.png';//加载完成进度条
	if(totalCount >0 && allCount == totalCount){//代表执行完成
		if(countAd == totalCount){//全部执行成功
			$('#jdt').html('<span class="cjfont">数据采集成功...</span><img src="'+jdt2+'" alt="进度条" width="550px" height="40px"/><br />');
			setTimeout("HideJdt()",5000); 
			window.clearInterval(timer);
		}else{
			window.clearInterval(timer);
			$('#jdt').html('<span class="cjfont">数据采集异常...</span><img src="'+jdt2+'" alt="进度条" width="550px" height="40px"/><br />');
		}
	}
}
//关闭进度条
function HideJdt(){
	$('#jindu').modal('hide')
}

//获取svn采集错误消息日志   
function getSvnErrorMessage() {
	var flpng = '/mvp/view/HTML/images/fail.png';
	var myTable= document.getElementById("cjTabs");
	myTable.style.display="none";
  	
	var myTable= document.getElementById("cwTabs");
	myTable.style.display="";
  
	var rw= document.getElementById("rw");
	rw.className = "";
	var cw= document.getElementById("cw");
	cw.className = "sel";
	$.ajax({
		url: getRootPath() + '/getErrorMessage/getMessage',
		type: 'post',
		data : {
			proNo : projNo,
			token : token
		},
		success: function (data) {
			var record = data.data;
			var tab = "";
			if(!_.isEmpty(record)){
				$("#cwTabs tr:not(:first)").remove();
				_.each(record, function(rs, index){
					tab += '<tr><td align="left"><img src="/mvp/view/HTML/images/fail.png" alt="失败" width="18px" height="18px"/>&nbsp;&nbsp;'+ rs.MESSAGE +'</td></tr>';
				});
				$("#cwTabs tbody").append(tab);
			}
		}
	}); 
};

function getCjList(){
	var tab = "";
	$("#cwTabs tbody").html("");
	var myTable= document.getElementById("cjTabs");
	myTable.style.display="";
	
	var myTable= document.getElementById("cwTabs");
	myTable.style.display="none";
	var rw = document.getElementById("rw");
	rw.className = "sel";
	var cw= document.getElementById("cw");
	cw.className = "";
}
var num = 1;
$(document).on("click","a[name='configAdd']",function(){
	var configname = $(this).attr("configname");
	configAddDo(configname,"del");
	num++;
});

function icpCiAddDo(configname,configa){
	tab += '<div>'+
	'<span>'+configname.toUpperCase()+configval+' </span>'+
	'<input type="text" id="'+configname+'Version'+num+'" class="configInput" />'+
	'</div>';
	$("#"+configname).append(tab);
}

function configAddDo(configname,configa){
	var configval = "版本";
	if(configname=="svn"){
		configval="过滤";
	}else if(configname=="git"){
		configval="分支";
	}
	var tab = "";
	tab += '<div id="'+configname+'-'+num+'" class="input-group" style="display: flex;">'+
		'<div class="config1">'+
			'<div>'+
				'<span>'+configname.toUpperCase()+'路径 </span>'+
				'<input type="text" id="'+configname+'urlNew'+num+'" class="configInput" />'+
			'</div>';
	if(configname!="icp-ci" && configname!="svn"){
		tab += '<div>'+
		'<span>'+configname.toUpperCase()+configval+' </span>'+
		'<input type="text" id="'+configname+'Version'+num+'" class="configInput" />'+
		'</div>';
	}
	if(configname=="svn"){
		tab += '<div>'+
			'<span>版本过滤 </span>'+
			'<input type="text" id="'+configname+'Version'+num+'" class="configInput" />'+
		'</div>'+
		'<div>'+
			'<span>路径过滤 </span>'+
			'<input type="text" id="'+configname+'Path'+num+'" class="configInput" />'+
		'</div>';
	}
	if(configname=="git"){
		tab += '<div>'+
			'<span>版本过滤 </span>'+
			'<input type="text" id="'+configname+'Commit'+num+'" class="configInput" />'+
			'</div>'+
			'<div>'+
			'<span>路径过滤 </span>'+
			'<input type="text" id="'+configname+'Path'+num+'" class="configInput" />'+
		'</div>';
	}
	if(configname=="icp-ci"){
		tab += '<div>'+
			'<span>版本构建 </span>'+
			'<input type="text" id="'+configname+'Path'+num+'" class="configInput" />'+
			'</div>'+
			'<div>'+
			'<span>工程能力</span>'+
			'<input type="text" id="'+configname+'Version'+num+'" class="configInput" />'+
		'</div>';
	}
	tab += '<div style="line-height: 20px;float: left;margin-left: 40px;">'+
				'<input type="checkbox" id="'+configname+'OtherAcc'+num+'" value="0" style="" />'+
				'<span>是否使用其他用户密码</span>'+
			'</div>'+
		'</div>';
	if(configa=="del"){
		tab += '<a name="configDel" delname="'+configname+'-'+num+'" class="btn btn-danger configAdd" style="">移除</a>';
	}else{
		tab += '<a name="configAdd" configname="'+configname+'" class="btn btn-primary configAdd" style="">添加</a>';
	}
	
	tab += '</div>'
	$("#"+configname).append(tab);
}

$(document).on("click","a[name='configDel']",function(){
	var delname = $(this).attr("delname");
	$("#"+delname).remove();    
});

function getRequestJson(allTr,type,urls,branchs,scopes,otherAccounts,svnVersions,svnPaths){
	for(var i=0;i<urls.length;i++){
		allTr +='{"type":"'+type+'",'+
				'"url":"'+urls[i].value+'",';
		if(branchs!=null){
			allTr +='"branch":"'+branchs[i].value+'",';
		}
		if(scopes!=null){
			allTr +='"scope":"'+scopes[i].value+'",';
		}
		if(svnVersions!=null){
			allTr +='"excludeRevision":"'+svnVersions[i].value+'",';
		}
		if(svnPaths!=null){
			allTr +='"excludePath":"'+svnPaths[i].value+'",';
		}
		if($(otherAccounts[i]).prop("checked")){
			allTr +='"otherAccount":"0"';
		}else{
			allTr +='"otherAccount":"1"';
		}
		allTr +='},';
	}
	return allTr;
}

function saveUrl(){
//$(document).on("click","#saveUrl']",function(){
	var allTr = "";
	var svnurls = $('input[id^=svnurlNew]');
	var svnVersions = $('input[id^=svnVersion]');//版本过滤条件
	var svnPaths = $('input[id^=svnPath]');//路径过滤条件
	var svnOtherAccs = $('input[id^=svnOtherAcc]');
	allTr = getRequestJson(allTr,2,svnurls,null,null,svnOtherAccs,svnVersions,svnPaths);
	var giturls = $('input[id^=giturlNew]');
	var gitVersions = $('input[id^=gitVersion]');//git为分支
	var gitOtherAccs = $('input[id^=gitOtherAcc]');
	var gitPaths = $('input[id^=gitPath]');//路径过滤条件
	var gitCommit = $('input[id^=gitCommit]');//版本过滤条件
	allTr = getRequestJson(allTr,3,giturls,gitVersions,null,gitOtherAccs,gitCommit,gitPaths);
	var dtsurls = $('input[id^=dtsurlNew]');
	var dtsVersions = $('input[id^=dtsVersion]');//dts为版本
	var dtsOtherAccs = $('input[id^=dtsOtherAcc]');
	allTr = getRequestJson(allTr,1,dtsurls,dtsVersions,null,dtsOtherAccs);
	var tmssurls = $('input[id^=tmssurlNew]');
	var tmssVersions = $('input[id^=tmssVersion]');//tmss为版本
	var tmssOtherAccs = $('input[id^=tmssOtherAcc]');
	allTr = getRequestJson(allTr,4,tmssurls,tmssVersions,null,tmssOtherAccs);
	
	var ciurls = $('input[id^=icp-ciurlNew]');
	var icpVersions = $('input[id^=icp-ciVersion]');//版本构建名称
	var icpPaths = $('input[id^=icp-ciPath]');//工程能力名称
	var ciOtherAccs = $('input[id^=icp-ciOtherAcc]');
	allTr = getRequestJson(allTr,5,ciurls,null,null,ciOtherAccs,icpVersions,icpPaths);
	
	var jsonStr = '{"projNo":"'+projNo+'","repositoryInfos":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url: getRootPath() + '/svnTask/saveurl0',
		type: 'post',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data:jsonStr,
		success: function () {
			$("#configuroute").modal("hide");
		}
	});
//});
}

function inputUrl(){
	$.ajax({
		url: getRootPath() + '/project/searchConfigInfo',
		type: 'post',
		data:{
			no : projNo
		},
		success: function (data) {
			num = 1;
			var counter = new Array(0,0,0,0,0);
			_.each(data, function(config, index){
				var configname = config.type;
				//1:DTS,2:SVN,3:Git,4:TMSS,5:ci,6:iso
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
				}
				if(counter[config.type-1]>0){
					configAddDo(configname,"del");
				}else{
					$("#"+configname).children().remove(); 
					configAddDo(configname,"add");
				}
				$('#'+configname+'urlNew'+num).val(config.url);
				if(configname=="svn"){
					$('#'+configname+'Version'+num).val(config.excludeRevision);
					$('#'+configname+'Path'+num).val(config.excludePath);
				}else if(configname=="git"){
					$('#'+configname+'Path'+num).val(config.excludePath);
					$('#'+configname+'Version'+num).val(config.branch);
					$('#'+configname+'Commit'+num).val(config.excludeRevision);
				}else if(configname=="icp-ci"){
					$('#'+configname+'Path'+num).val(config.excludePath);
					$('#'+configname+'Version'+num).val(config.excludeRevision);
				}else{
					$('#'+configname+'Version'+num).val(config.branch);
				}
				if(config.otherAccount==0){
					$('#'+configname+'OtherAcc'+num).attr("checked",true);
				}else{
					$('#'+configname+'OtherAcc'+num).attr("checked",false);
				}
				counter[config.type-1]++;
				num++;
			});
		}
	});
}

