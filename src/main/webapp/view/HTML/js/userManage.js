	
function zrsuerManage(){
	var proNo = window.parent.projNo;
	$("#proNo").val(proNo);
	var userType = '1';
	$('#userlvTable').bootstrapTable('destroy');
    $('#userlvTable').bootstrapTable({
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
    	toolbar:'#toolbar', //指定工作栏

    	uniqueId: 'userid',//uniqueId必须
    	queryParams: function(params){
        	var param = {
        			lobline :$("#lobline option:selected").val(),
        			lobdept :$("#lobdept option:selected").val(),
        			lobdepment :$("#lobdepment option:selected").val(),
        			lobrole :$("#lobrole option:selected").val(),
                	lobname : $("#lobname").val(),
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
                    	var pageNumber=$('#userlvTable').bootstrapTable('getOptions').pageNumber;
                    	$('#userlvTable').bootstrapTable('refresh',{pageNumber:pageNumber});
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
        		    var pageSize=$('#userlvTable').bootstrapTable('getOptions').pageSize;
        		    var pageNumber=$('#userlvTable').bootstrapTable('getOptions').pageNumber;
        		    return pageSize * (pageNumber - 1) + index + 1;
        		}
             },
        	{
        		title:'登录账号',
        		halign :'center',
        		align : 'center',
        		field:'userid',
				width : 50,
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
				width:50,
        	},
        	{
        		title:'业务线',
        		halign :'center',
        		align : 'center',
        		field:'buVal',
				width: 70,
        	},
        	{
        		title:'事业部',
        		halign :'center',
        		align : 'center',
        		field:'duVal',
				width: 70,
        	},
        	{
        		title:'交付部',
        		halign :'center',
        		align : 'center',
        		field:'deptVal',
				width: 70,
        	},
        	{
        		title:'角色名称',
        		halign :'center',
        		align : 'center',
        		field:'permissionNames',
				width: 70,
        	},
        	{
        		title:'操作',
        		halign :'center',
        		align : 'center',
        		field:'opera',
				width: 60,
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
    $('#clear_btn').click(function(){
    	$("#lobname").val('');
    	$("#lobline").val('');
    	$("#lobdept").val(""); 
    	$("#lobdepment").val(""); 
    	$("#lobrole").val(""); 
    	$('#userlvTable').bootstrapTable('refresh', {url: getRootPath() + '/user/queryList'});
    });
	
    //点击'修改'启用编辑
    editData =  function (selectId){ 
    	$('#userName').attr("readonly",true);
    	$('#permissionid1').selectpicker('deselectAll');
    	$('#buSearch1').selectpicker('deselectAll');
    	$('#duSearch1').selectpicker('deselectAll');
    	$('#deptSearch1').selectpicker('deselectAll');
    	
    	//直接关联单行数据
    	var selectRow = $('#userlvTable').bootstrapTable('getRowByUniqueId', selectId);
    	$("#editPage").modal('show');
    	
    	$("#userName").val(selectRow.username);
        $("input:hidden[id='userId']").val(selectRow.userid);
    	$("#permissionid1").selectpicker('val', selectRow.permissionNames.split(","));
    	if(selectRow.bu!=null){
    		 $("#buSearch1").selectpicker('val', selectRow.bu.split(","));
    		 getbuSearch1();
    		 if(selectRow.du!=null){
    			 $("#duSearch1").selectpicker('val', selectRow.du.split(","));
    			 getduSearch1();
    			 $("#deptSearch1").selectpicker('val', selectRow.dept==null?"":selectRow.dept.split(","));
    		 }
    	}
    };
    
    //点击'删除'启用删除（带提示框）
    delData =  function (selectId){ 
    	$('#userName').attr("readonly",true);
    	$('#permissionid1').selectpicker('deselectAll');
    	$('#buSearch1').selectpicker('deselectAll');
    	$('#duSearch1').selectpicker('deselectAll');
    	$('#deptSearch1').selectpicker('deselectAll');
    	var data; 
    	data={
    		"USERID":selectId,
    	}
        Ewin.confirm({title: "用户删除", message: "是否要删除用户：" + selectId + " ？"}).on(function (e) {
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
    						$('#userlvTable').bootstrapTable('refresh', {url: getRootPath() + '/user/queryList'});
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
		$('#userName').attr("readonly",true);
        $('#permissionid1').selectpicker('deselectAll');
        $('#buSearch1').selectpicker('deselectAll');
        $('#duSearch1').selectpicker('deselectAll');
        $('#deptSearch1').selectpicker('deselectAll');
		var selectRow = $('#userlvTable').bootstrapTable('getSelections');
        if(selectRow.length == 1){
            $("#editPage").modal('show');
        }else{
            toastr.warning('请选择一条数据进行编辑');
            return false;
        }
        $("#userName").val(selectRow[0].username);
        $("input:hidden[id='userId']").val(selectRow[0].userid);
        $("#permissionid1").selectpicker('val', selectRow[0].permissionNames.split(","));
        if(selectRow[0].bu!=null){
        	 $("#buSearch1").selectpicker('val', selectRow[0].bu.split(","));
             getbuSearch1();
             if(selectRow[0].du!=null){
            	 $("#duSearch1").selectpicker('val', selectRow[0].du.split(","));
            	 getduSearch1();
            	 $("#deptSearch1").selectpicker('val', selectRow[0].dept==null?"":selectRow[0].dept.split(","));
             }
        }
       
	}); 
	
	 $('#edit_backBtn').click(function(){
	  	$("#editPage").modal('hide');
	});	
	 
  	$(document).on("change", "#buSearch1", function () {
  		getbuSearch1();
	})
	
	function getbuSearch1(){
  		$.ajax({
            url: getRootPath() + '/opDepartment/getOpDepartmentBylevel',
            type: 'post',
            async: false,
            data: {level: 3, ids: $("#buSearch1").selectpicker("val") == null ? null : $("#buSearch1").selectpicker("val").join()},
            success: function (data) {
                $('#duSearch1').selectpicker("val", []);
                $("#duSearch1").empty();
                $('#duSearch1').prev('div.dropdown-menu').find('ul').empty();
                $('#deptSearch1').selectpicker("val", []);
                $("#deptSearch1").empty();
                $('#deptSearch1').prev('div.dropdown-menu').find('ul').empty();
                if (data != null && data.data != null && data.data.length > 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#duSearch1").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
                    }
                }
                $('#duSearch1').selectpicker('refresh');
                $('#duSearch1').selectpicker('render');
            }
        })
  	}
  	
	$(document).on("change", "#duSearch1", function () {
		getduSearch1();
	})
	
	function getduSearch1(){
		 $.ajax({
	            url: getRootPath() + '/opDepartment/getOpDepartmentBylevel',
	            type: 'post',
	            async: false,
	            data: {
	                level: 4,
	                ids: $("#duSearch1").selectpicker("val") == null ? null : $("#duSearch1").selectpicker("val").join()
	            },
	            success: function (data) {
	                $('#deptSearch1').selectpicker("val", []);
	                $("#deptSearch1").empty();
	                $('#deptSearch1').prev('div.dropdown-menu').find('ul').empty();
	                if (data != null && data.data != null && data.data.length > 0) {
	                    for (var i = 0; i < data.data.length; i++) {
	                        $("#deptSearch1").append('<option value="' + data.data[i].dept_id + '">' + data.data[i].dept_name + '</option>');
	                    }
	                }
	                $('#deptSearch1').selectpicker('refresh');
	                $('#deptSearch1').selectpicker('render');
	            }
	        })
	}
	//修改保存
    $('#saveUpdate').click(function(){
    	
    	var data;
        var userId = $("input:hidden[id='userId']").val();
		//var userName=$('#userName').val();
		//if (userName==null ||userName=="") {
		//	alert("用户名称不能为空！")
		//	return;
		//}

		var permissionName=$("#permissionid1").selectpicker("val")==null?null:$("#permissionid1").selectpicker("val").join();
		
        if(permissionName==null||permissionName==""){
            alert("角色名称不能为空！")
            return;
        }
        var buSearch=$("#buSearch1").selectpicker("val")==null?null:$("#buSearch1").selectpicker("val").join();

        if(buSearch==null||buSearch==""){
            alert("业务线不能为空！")
            return;
        }  
        data={
        	"userId":userId,
            "permissionName":permissionName,
            "buSearch":buSearch,
            "duSearch":$("#duSearch1").selectpicker("val")==null?null:$("#duSearch1").selectpicker("val").join(),
            "deptSearch":$("#deptSearch1").selectpicker("val")==null?null:$("#deptSearch1").selectpicker("val").join(),      	
        }
        $.ajax({
                url:getRootPath() + '/user/updateNew',
                type : 'post',
                async: false,
                data:data,
                success:function(data){
                    //后台返回添加成功
                    if(data.code=='success'){
                        $("#editPage").modal('hide');
                        toastr.success('编辑成功！');
                        $('#userlvTable').bootstrapTable('refresh', {url: getRootPath() + '/user/queryList'});
                    }
                    else{
                        toastr.error('编辑失败!');
                    }
                }
        });
    });
};
