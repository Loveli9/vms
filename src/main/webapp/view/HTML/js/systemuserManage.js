function sysuerManage(){
	var proNo = window.parent.projNo;
	$("#proNo").val(proNo);
	var userType = '0';
	$('#usersysTable').bootstrapTable('destroy');
    $('#usersysTable').bootstrapTable({
    	method: 'post',
    	contentType: "application/x-www-form-urlencoded",
    	url:getRootPath() + '/user/queryList',
    	editable:true,//可行内编辑
    	sortable: true,                     //是否启用排序
    	sortOrder: "asc",
    	striped: false, //是否显示行间隔色
    	dataField: "rows",
    	pageNumber: 1, //初始化加载第一页，默认第一页
    	pagination:true,//是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,//单页记录数
    	pageList:[5,10,20,30],//分页步进值
    	//showRefresh:true,//刷新按钮
    	showColumns:true,
    	minimumCountColumns: 2,             //最少允许的列数
    	clickToSelect: true,//是否启用点击选中行
    	toolbarAlign:'right',
    	buttonsAlign:'right',//按钮对齐方式
    	toolbar:'#toolbarss',//指定工作栏
		toolbarAlign:'right',//工具栏的位置
    	uniqueId: 'userid',//uniqueId必须
	//	uniqueId: 'PASSWORD',//uniqueId必须
    	queryParams: function(params){
        	var param = {
        			lobrole :$("#lobroless option:selected").val(),
                	lobname : $("#lobnamess").val(),
                	userType: userType,
        	        limit : params.limit, // 页面大小
        	        offset : params.offset, // 页码
        	        proNo : proNo,
        	    }
        	return param;
        },
        onEditableSave: function (field, row, oldValue, $el) {
        	if(field == "username"){
        		row.username = row.username.toString();
        	}
            $.ajax({
                type: "post",
                url: getRootPath()+"/user/query",
 				dataType: "json",
 				contentType : 'application/json;charset=utf-8', //设置请求头信息
 				data: JSON.stringify(row),				
                success: function (data, status) {
                    if (status == "success") {
                    	var pageNumber=$('#usersysTable').bootstrapTable('getOptions').pageNumber;
                    	$('#usersysTable').bootstrapTable('refresh',{pageNumber:pageNumber});
                    	toastr.success('修改成功！');
                    }else{
                    	toastr.success('修改失败！');
                    }
                }
            });
        },
    
    	columns:[
        	{
        		title:'全选',
        		field:'select',
        		checkbox:true,
        		width:25,
        		align:'center',
        		valign:'middle',
        		visible: false,
        	},
        	{
        		title : '序号',
        		align: "center",
        		field:'id',
        		width: 30,
        		formatter: function (value, row, index) {
        		    var pageSize=$('#usersysTable').bootstrapTable('getOptions').pageSize;
        		    var pageNumber=$('#usersysTable').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
             },
        	{
        		title:'登录账号',
        		halign :'center',
        		align : 'center',
        		field:'userid',
				width : 45,
        		formatter:function(value,row,index){
					if(value){
						return	'<a href="#" style="color:blue" onclick="editData(\'' + row.userid + '\')">' + value + '</a>'; 
					}
					return '';
				},
        	},
        	{
        		title:'用户名称',
        		halign :'center',
        		align : 'center',
        		field:'username',
				width : 45,
        	},
        	{
        		title:'用户密码',
        		halign :'center',
        		align : 'center',
        		field:'password',
				width: 150,
        	},
        	/**{
        		title:'子产品线',
        		halign :'center',
        		align : 'center',
        		field:'hwzpduVal',
				width: 300,
        	},
        	{
        		title:'PDU/SPDT',
        		halign :'center',
        		align : 'center',
        		field:'pduspdtVal',
				width: 300,
        	},*/
        	{
        		title:'角色名称',
        		halign :'center',
        		align : 'center',
        		field:'permissionNames',
				width: 150,
        	},
        	{
        		title:'操作',
        		halign :'center',
        		align : 'center',
        		field:'opera',
				width: 100,
				//'修改,删除'响应id列
        		formatter:function(value,row,index){ 
					return '<a href="#" style="color:blue" onclick="editData(\'' + row.userid + '\')">' + '修改' + '</a>'
					+'&nbsp;&nbsp;&nbsp;<a href="#" style="color:blue" onclick="delData(\'' + row.userid + '\')">' + '删除' + '</a>';
				}
        	}
    	],
    	locale:'zh-CN',//中文支持,
    });
    
	//重置按钮事件
    $('#clear_btnss').click(function(){
    	$("#lobnamess").val('');
    	$("#lobroless").val("");
    	$('#usersysTable').bootstrapTable('refresh', {url: getRootPath() + '/user/queryList'});
    });
	
    //点击'修改'启用编辑
    editData =  function (selectId){ 
    	$('#userNamess').attr("readonly",true);
    	$('#permissionid1ss').selectpicker('deselectAll');
    	$('#hwproline1').selectpicker('deselectAll');
    	$('#hwzproline1').selectpicker('deselectAll');
    	$('#pduspdtline1').selectpicker('deselectAll');
    	
    	//直接关联单行数据
    	var selectRow = $('#usersysTable').bootstrapTable('getRowByUniqueId', selectId);
    	$("#editPagess").modal('show');
    	
    	$("#userNamess").val(selectRow.userid);
        $("input:hidden[id='userId']").val(selectRow.userid);
		$("#passWord").val(selectRow.password);
    	$("#permissionid1ss").selectpicker('val', selectRow.permissionNames.split(","));
    	/**if(selectRow.hwpdu!=null){
       	 $("#hwpduSearch1").selectpicker('val', selectRow.hwpdu.split(","));
            gethwpduSearch1();
            if(selectRow.hwzpdu!=null){
           	 $("#hwzpduSearch1").selectpicker('val', selectRow.hwzpdu.split(","));
           	 gethwzpduSearch1();
           	 $("#pduspdtSearch1").selectpicker('val', selectRow.pduspdt==null?"":selectRow.pduspdt.split(","));
            }
       }*/
    };
    
    //点击'删除'启用删除
    delData =  function (selectId){ 
    	$('#userNames').attr("readonly",true);
        $('#permissionid1s').selectpicker('deselectAll');
        $('#hwproline1').selectpicker('deselectAll');
        $('#hwzproline1').selectpicker('deselectAll');
        $('#pduspdtline1').selectpicker('deselectAll');
        var data; 
        data={
        	"USERID":selectId,
        }
        Ewin.confirm({title: "客户删除", message: "是否要删除客户：" + selectId + " ？"}).on(function (e) {
			if (!e) {
				return;
			} else {
				$.ajax({
	                url:getRootPath() + '/user/delete',
	                type : 'post',
	                async: false,
	                data:data,
	                success:function(data){
	                    //后台返回添加成功
	                    if(data.code=='success'){                        
	                        toastr.success('删除成功！');
	                        $('#usersysTable').bootstrapTable('refresh', {url: getRootPath() + '/user/queryList'});
	                    }
	                    else{
	                        toastr.error('删除失败!');
	                    }
	                }
				});
			}
    	});
    };
    
	$(document).on("click", "#btn_edit", function () {
		$('#userNames').attr("readonly",true);
        $('#permissionid1s').selectpicker('deselectAll');
        $('#hwpduSearch1').selectpicker('deselectAll');
        $('#hwzpduSearch1').selectpicker('deselectAll');
        $('#pduspdtSearch1').selectpicker('deselectAll');
		var selectRow = $('#usersysTable').bootstrapTable('getSelections');
        if(selectRow.length == 1){
            $("#editPagess").modal('show');
        }else{
            toastr.warning('请选择一条数据进行编辑');
            return false;
        }
        $("#userNames").val(selectRow[0].username);
        $("input:hidden[id='userId']").val(selectRow[0].userid);
        $("#permissionid1s").selectpicker('val', selectRow[0].permissionNames.split(","));
        if(selectRow[0].hwpdu!=null){
        	 $("#hwpduSearch1").selectpicker('val', selectRow[0].hwpdu.split(","));
             gethwpduSearch1();
             if(selectRow[0].hwzpdu!=null){
            	 $("#hwzpduSearch1").selectpicker('val', selectRow[0].hwzpdu.split(","));
            	 gethwzpduSearch1();
            	 $("#pduspdtSearch1").selectpicker('val', selectRow[0].pduspdt==null?"":selectRow[0].pduspdt.split(","));
             }
        }
	}); 
	
	$('#edit_backBtn').click(function(){
	  	$("#editPagess").modal('hide');
	});	
	 
  	$(document).on("change", "#hwpduSearch1", function () {
  		gethwpduSearch1();
	})
	
	function gethwpduSearch1(){
  		$.ajax({
            url: getRootPath() + '/opDepartment/getHwDepartmentBylevel',
            type: 'post',
            async: false,
            data: {level: 2, ids: $("#hwpduSearch1").selectpicker("val") == null ? null : $("#hwpduSearch1").selectpicker("val").join()},
            success: function (data) {
                $('#hwzpduSearch1').selectpicker("val", []);
                $("#hwzpduSearch1").empty();
                $('#hwzpduSearch1').prev('div.dropdown-menu').find('ul').empty();
                $('#pduspdtSearch1').selectpicker("val", []);
                $("#pduspdtSearch1").empty();
                $('#pduspdtSearch1').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#hwzpduSearch1").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#hwzpduSearch1').selectpicker('refresh');
                $('#hwzpduSearch1').selectpicker('render');
            }
        })
  	}
	$(document).on("change", "#hwzpduSearch1", function () {
		gethwzpduSearch1();
	})
	function gethwzpduSearch1(){
		 $.ajax({
	            url: getRootPath() + '/opDepartment/getHwDepartmentBylevel',
	            type: 'post',
	            async: false,
	            data: {
	                level: 3,
	                ids: $("#hwzpduSearch1").selectpicker("val") == null ? null : $("#hwzpduSearch1").selectpicker("val").join()
	            },
	            success: function (data) {
	                $('#pduspdtSearch1').selectpicker("val", []);
	                $("#pduspdtSearch1").empty();
	                $('#pduspdtSearch1').prev('div.dropdown-menu').find('ul').empty();
	                if (data != null && data.data != null && data.data.length > 0) {
	                    for (var i = 0; i < data.data.length; i++) {
	                        $("#pduspdtSearch1").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
	                    }
	                }
	                $('#pduspdtSearch1').selectpicker('refresh');
	                $('#pduspdtSearch1').selectpicker('render');
	            }
	        })
	}
	
	//修改保存
    $('#saveUpdatess').click(function(){
    	
    	var data;
		var userName=$('#userNamess').val();
        var userId = $("input:hidden[id='userId']").val();
		if (userName==null ||userName=="") {
			alert("客户名称不能为空！")
			return;
		}
		var password=$('#passWord').val();
		if (password==null ||password=="") {
			alert("用户密码不能为空！")
			return;
		}
		//var permissionName=$("#permissionid1ss").selectpicker("val")==null?null:$("#permissionid1ss").selectpicker("val").join();
		var permissionName=$("#permissionid1ss").selectpicker("val")==null?null:$("#permissionid1ss").selectpicker("val").join();
		
        if(permissionName==null||permissionName==""){
            alert("角色名称不能为空！")
            return;
        }
      /**  var hwpduSearch=$("#hwpduSearch1").selectpicker("val")==null?null:$("#hwpduSearch1").selectpicker("val").join();

        if(hwpduSearch==null||hwpduSearch==""){
            alert("华为产品线不能为空！")
            return;
        } */
        data={
        	"userId":userId,
            "userName":userName,
            "permissionName":permissionName,
            "password": password,
       }
        $.ajax({
                url:getRootPath() + '/user/updateNews',
                type : 'post',
                async: false,
                data:data,
                success:function(data){
                    //后台返回添加成功
                    if(data.code=='success'){
                        $("#editPagess").modal('hide');
                        toastr.success('编辑成功！');;
                        $('#usersysTable').bootstrapTable('refresh', {url: getRootPath() + '/user/queryList'});
                    }
                    else{
                        toastr.error('编辑失败!');
                    }
                }
        }); 
    });   
};
