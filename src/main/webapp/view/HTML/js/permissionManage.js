$(function(){
	
	var zNodes=[];// 树节点数组
	var saveState=1;// 弹窗状态 新增1 编辑2

	// 根据窗口调整表格高度
    $(window).resize(function() {
        $('#mytab').bootstrapTable('resetView', {
            height: tableHeight()
        })
    });

    $('#mytab').bootstrapTable({
        method: 'get',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/auth/',
        queryParams: function (params) {
            var param;
            param = {
                pageSize: params.pageSize,
                pageNumber: params.pageNumber - 1,
                name: encodeURI($("#importRoleName").val())
                //name: $("#importRoleName").val()
            };
            return param;
        },
        //dataField: "data",
        responseHandler: function (res) {
            return {
                "rows": res.data,
                "total": res.totalCount
            }
        },

        //editable: true,//可行内编辑
        height: tableHeight(),//高度调整
        striped: false, //是否显示行间隔色

        toolbar: '#toolbar',//指定工作栏
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        //showRefresh:true,//刷新按钮

        sidePagination: 'server',
        //设置为undefined可以获取 pageNumber，pageSize，searchText，sortName，sortOrder
        //设置为limit可以获取limit, offset, search, sort, order
        //queryParamsType: 'limit',
        //queryParamsType: 'undefined',
        pagination: true,//是否分页
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 10,//单页记录数
        pageList: [10, 20, 30],//分页步进值

        editable: true,// 可行内编辑
        uniqueId: 'id',

        columns: [
            {
                title: '序号',
                align: "center",
                width: 30,
                formatter: function (value, row, index) {
                    var pageSize = $('#mytab').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#mytab').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {
                title: '角色名称',
                halign: 'center',
                align: 'center',
                field: 'name',
                width: 55,
                formatter: function (value, row, index) {
                    if (value) {
                        return '<a href="#" style="color:blue" onclick="editData(\'' + row.id + '\')">' + value + '</a>';
                    }
                    return '';
                }
            },
            {
                title: '角色描述',
                halign: 'center',
                align: 'center',
                field: 'description',
                width: 55
            },
            {
                title: '创建时间',
                halign: 'center',
                align: 'center',
                field: 'createTime',
                width: 60
            },
            {
                title: '创建人',
                halign: 'center',
                align: 'center',
                field: 'creator',
                width: 55
            },
            {
                title: '操作',
                halign: 'center',
                align: 'center',
                width: 50,
                //'修改,删除'响应id列
                formatter: function (value, row, index) {
                    return '<a href="#" style="color:blue" onclick="editData(\'' + row.id + '\')">' + '修改' + '</a>'
                        + '&nbsp;&nbsp;&nbsp;<a href="#" style="color:blue" onclick="delData(\'' + row.id + '\')">' + '删除' + '</a>';
                }
            }
        ],

        // 中文支持,
        locale: 'zh-CN'
    });

    // 查询按钮事件
    $('#search_btn').click(function () {
        $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/auth/'});
    });
    
    // 清空按钮事件
    $('#clear_btn').click(function(){
    	 $("#importRoleName").val(""); 
    	 $('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/auth/'});
    });

    /** ******************************* 新增 ****************************** */
        // 打开新增页面
    $('#btn_add').click(function () {
        $('#saveLabelMeasure').css('display', 'inline-block');
        $('#roleName').val('');
        $('#roleDet').val('');
        $('#roleName').attr("placeholder", "请输入角色名称");
        $('#roleDet').attr("placeholder", "请输入角色描述");
        $('#roleName').attr("readonly", false);
        $('#roleDet').attr("readonly", false);
        $('#authHeader').text('新增角色');
        saveState = 1;
        getZNodes();
        $('#labelMeasure').modal({
            backdrop: 'static',//点击空白处不关闭对话框
            keyboard: false  //键盘关闭对话框
        });
        $("#labelMeasure").modal('show');
    });
    
    /**
	 * 保存
	 */
    $('#saveLabelMeasure').click(function(){
    	var url,data;
		var perName=$('#roleName').val();
        var perAuth='';
		var managePer=$('#roleDet').val();
        var permissionid=$('#roleId').val();
		var perAuthArr=onCheck();
		$.each(perAuthArr,function(i,obj){
			perAuth+=obj+','
		});
		perAuth=perAuth.substr(0,perAuth.length-1);
		var creator=getCookie('username');
		if(''==$.trim(perName)){ toastr.error("角色名称不能为空！");return}	
		if(1==saveState){
			url=getRootPath() + '/auth/addPer';
			data={
				"name":perName,
				"description":managePer,
				"creator":creator,
				"authorized":perAuth,					
			};
		}else if(2==saveState){
			url=getRootPath() + '/auth/editPer';
            data={
        		"id":permissionid,
        		"name":perName,
				"description":managePer,
				"operator":creator,
				"authorized":perAuth,
            };
		}	
		$.ajax({
			url: url,
			type: 'post',
			async: false,
			data:data,
			success: function (data) {
				if (data.code == '200') {
					toastr.success('保存成功！');
					$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/auth/'});
					$("#labelMeasure").modal('hide');
				}
				else{
					toastr.error(data.message);
                    return
				}
			}
		});
    });
    
    // 获取cookie
	function getCookie(name){
	   var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	   if(arr != null){
	     return unescape(arr[2]); 
	   }else{
	     return null;
	   }
	};
	
	// 查看
	function lookOverRole(permissionid){
		$.ajax({
			url: getRootPath() + '/auth/viewPer',
			type: 'post',
			async: false,
			data: {id:permissionid},
			success: function (data) {
                zNodes=[];
				handleData(data.data[0].children[0]);					
			}
		});
	  zTreeObj = $.fn.zTree.init($("#permissionTree"), setting, zNodes);
	}
	
	function showPopover(msg) {
		$('#dataAcquisition').text(msg);
		$('#submitImportmodalfooter').css('display','none');
		$('#savetoop').modal('show');
		// 2秒后消失提示框
		var id = setTimeout(function () {
			$('#savetoop').modal('hide');
		}, 2000);
	}
	
	function showPopoverErr(msg) {
		$('#dataAcquisition').text(msg);
		$('#submitImportmodalfooter').css('display','block');
		$('#savetoop').modal('show');
	}
	
	function onCheck() {
	   var treeObj = $.fn.zTree.getZTreeObj("permissionTree"); // 获取树对象
	   var nodes = treeObj.getCheckedNodes(true);  // 获取勾选状态改变的节点
	   var designIds = [];		   
	   $.each(nodes, function (i, item) {
		   if(!item.isParent){
			  designIds.push(item.path);      // 将状态改变的节点id输出到数组
		   }
		   item.checkedOld = item.checked;    // 这句话很关键，将节点的初始状态置为当前状态。否则每次勾选操作获取状态改变节点时只会跟树初始化的状态相比较。
	   });
	   return designIds;
	 };

    var setting = {
        view: {
            selectedMulti: true, // 设置是否能够同时选中多个节点
            showIcon: true,      // 设置是否显示节点图标
            showLine: true,      // 设置是否显示节点与节点之间的连线
            showTitle: true,     // 设置是否显示节点的title提示信息
        },
        data: {
            simpleData: {
                enable: true,   // 设置是否启用简单数据格式（zTree支持标准数据格式跟简单数据格式，上面例子中是标准数据格式）
                idKey: "id",     // 设置启用简单数据格式时id对应的属性名称
                pidKey: "pId"    // 设置启用简单数据格式时parentId对应的属性名称,ztree根据id及pid层级关系构建树结构
            }
        },
        check: {
            enable: true
        },
        /*
         * async: { enable: true, //设置启用异步加载 type: "get",
         * //异步加载类型:post和get contentType: "application/json",
         * //定义ajax提交参数的参数类型，一般为json格式 url: "/Design/Get", //定义数据请求路径
         * autoParam: ["id=id", "name=name"]
         * //定义提交时参数的名称，=号前面标识节点属性，后面标识提交时json数据中参数的名称 }
         */

    };
 	
      function getZNodes(){
    	  $.ajax({
				url: getRootPath() + '/auth/perTree',
				type: 'post',
				async: false,
				data: {},
				success: function (data) {
                    zNodes=[];
                    // 注意数据获取
					handleData(data.data[0].children[0]);						
				}
			});
    	  console.log(zNodes);
    	  zTreeObj = $.fn.zTree.init($("#permissionTree"), setting, zNodes);
      }
      
      // 处理返回的树数据
      function  handleData(dataPiece){	
		if(dataPiece.children&&null!=dataPiece.children&&''!=dataPiece.children){
			if(dataPiece.checked&&true==dataPiece.checked){
				var check = true;
			}else{
				var check = false;
			}
			if(0==dataPiece.level){
                zNodes.push({id:dataPiece.id,pId:dataPiece.pId,name:dataPiece.name,path:dataPiece.path,isParent:true,open:true,checked:check});
			}else{
                zNodes.push({id:dataPiece.id,pId:dataPiece.pId,name:dataPiece.name,path:dataPiece.path,isParent:true,checked:check});
			}
			$.each(dataPiece.children,function(i,obj){
				handleData(obj);
			});
		}else{
			if(dataPiece.checked&&true==dataPiece.checked){
				var check = true;
			}else{
				var check = false;
			}
			zNodes.push({id:dataPiece.id,pId:dataPiece.pId,name:dataPiece.name,path:dataPiece.path,checked:check});
		}			
	  }

    /** *********************************** 编辑 ******************************** */
    //点击'修改'启用编辑
	editData =  function (selectId){ 
    	//直接关联单行数据
    	var selectRow = $('#mytab').bootstrapTable('getRowByUniqueId', selectId);
    	$('#saveLabelMeasure').css('display','inline-block');
   	 	var permissionid=selectRow.id;
		var roleName=selectRow.name;
		var roleDet=selectRow.description;
		$('#roleName').val(roleName);
		$('#roleDet').val(roleDet);
        $('#roleId').val(permissionid);
		$('#roleName').attr("placeholder","请输入角色名称");
        $('#roleDet').attr("placeholder","请输入角色描述");
        $('#roleName').attr("readonly",false);            
        $('#roleDet').attr("readonly",false);
		$('#authHeader').text('角色权限');
		saveState=2;
        lookOverRole(permissionid);
		$('#labelMeasure').modal({
			backdrop: 'static',
			keyboard: false
	    });
    	$("#labelMeasure").modal('show');
	};  
	
    $('#closeLabelMeasure').click(function(){
    	$("#labelMeasure").modal('hide');
    });
    
    /** *********************************** 删除 ********************************* */
    // 删除事件按钮
    delData =  function (selectId){
    	Ewin.confirm({title: "角色删除", message: "是否要删除当前角色？"}).on(function (e) {
			if (!e) {
				return;
			} else {
				$.ajax({
	        		url:getRootPath() + '/auth/delPer',
	        		type:'post',
	        		dataType: "json",
					contentType : 'application/json;charset=utf-8', //设置请求头信息
					data:JSON.stringify(selectId),
	        		success:function(data){
	        			if(data.code == '200'){
	        				toastr.success('删除成功！');
	            			$('#mytab').bootstrapTable('refresh', {url: getRootPath() + '/auth/'});
	        			}else if(data.code == '-101'){
	        				toastr.error('角色下用户不为空，无法进行删除操作!');
	        			}else{
	        				toastr.error('删除失败!');
	        			}
	        		}
	        	});
			}
    	});
    }
});

function tableHeight() {
    return $(window).height() - 30;
}
