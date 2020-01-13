$(function () {
	
	var BSTableOur;
	
	var BSTable = function (bstableId,url,columns,tableclasses) {
    	this.btInstance = null;
    	this.bstableId = bstableId;
    	this.url = url;
        this.sortable=false;
        this.editable=false;
        this.pagination=true;
        this.showColumns=false;
        this.dataField='rows';

    	this.classes = 'table table-hover ';//自定义table级class
    	this.pageSize = 10;//单页记录数
    	this.pageList = '[5,10,20,30]';//分页步进值
    	this.queryParams={};// 向后台传递的自定义参数
    	this.columns = columns;//向后台传递表格数据结构
    	this.paginationType = "server";  
    	//设置分页方式：server 或者 client 
    	//如果为client，返回数据为 数组 实体 或  map 嵌套
    	this.toolbarId = "toolbar";//指定工作栏
    	this.height = undefined;
        this.detailView = false;
        this.detailFormatter = function(index,row){};//显示信息函数
    	this.onEditableSave = function (field, row, oldValue, $el) {};//编辑
    	this.onClickCell = function(field, value, row, $element) {};//点击表格时间
    	BSTableOur = this;
    }

    BSTable.prototype = {
    	init : function () {
    		 this.btInstance =
    			 $('#'+this.bstableId).bootstrapTable({
    				method: 'post',
    				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    				url:this.url,
    				height:this.height,//高度调整
    				editable:this.editable,//可行内编辑
    				sortable: this.sortable, //是否启用排序
    				//sortOrder: "asc",
    				striped: true, //是否显示行间隔色
    				dataField: this.dataField,
    				pageNumber: this.pageNumber, //初始化加载第一页，默认第一页
    				pagination:this.pagination,//是否分页
    				queryParamsType:'limit',
    				sidePagination:this.paginationType,
    				pageSize:this.pageSize,//单页记录数
    				pageList:this.pageList,//分页步进值
    				//showRefresh:true,//刷新按钮
    				showColumns:this.showColumns,
    				minimumCountColumns: 2,             //最少允许的列数
    				//clickToSelect: true,//是否启用点击选中行
    				classes:this.classes,
    				/*toolbarAlign:'left',
    				buttonsAlign:'left',//按钮对齐方式
    				toolbar:'#'+this.toolbarId,//指定工作栏
*/    				queryParams: function (param) {
                        return $.extend(BSTableOur.queryParams, param);
                    }, // 向后台传递的自定义参数
    		        columns:this.columns,
    				locale:'zh-CN',//中文支持,
    				onEditableSave:this.onEditableSave,//编辑
				 	onClickCell:this.onClickCell,//编辑
				 	detailView:this.detailView,//行可以展开
					detailFormatter:this.detailFormatter,//显示信息
    			});
    		 return this;
    	},
    	/**
    	 * 向后台传递的高度
    	 * @param param
    	 */
    	setHeight:function (height) {
    		this.height = height;
    	},
        /**
         * 行可以展开
         * @param param
         */
        setDetailView:function (detailView) {
            this.detailView = detailView;
        },
		/**
         * 显示信息
         * @param param
         */
        setDetailFormatter:function (detailFormatter) {
            this.detailFormatter = detailFormatter;
        },
    	/**
    	 * 向后台传递的自定义参数
    	 * @param param
    	 */
    	setQueryParams:function (param) {
    		this.queryParams = param;
    	},
		/**
    	 * 返回值参数表名称
    	 * @param param
    	 */
    	setDataField:function (param) {
    		this.dataField = param;
    	},
    	/**
    	 * 向后台传递的自定义参数
    	 * @param param
    	 */
    	setColumns:function(columns) {
    		this.columns = columns;
    	},
    	/**
    	 * 设置分页方式：server 或者 client
    	 * @param param
    	 */
    	setPaginationType:function(paginationType) {
    		this.paginationType = paginationType;
    	},
    	/**
    	 * 指定工作栏
    	 * @param param
    	 */
    	setToolbarId:function(toolbarId){
    		this.toolbarId = toolbarId;
    	},
    	/**
    	 * 编辑传入
    	 * @param param
    	 */
    	setOnEditableSave:function(onEditableSave){
    		this.onEditableSave = onEditableSave;
    	},
		/**
    	 * 点击表格事件传入
    	 * @param param
    	 */
    	setOnClickCell:function(onClickCell){
    		this.onClickCell = onClickCell;
    	},
    	/**
    	 * 自定义table级class
    	 * @param param
    	 */
    	setClasses:function(tableclasses){
    		this.classes += tableclasses;
    	},
    	/**
    	 * 自定义单页记录数
    	 * @param param
    	 */
    	setPageSize:function(pageSize){
    		this.pageSize = pageSize;
    	},
    	/**
    	 * 自定义初始页码
    	 * @param param
    	 */
    	setPageNumber:function(pageNumber){
    		this.pageNumber = pageNumber;
    	},
    	/**
    	 * 自定义分页进步值
    	 * @param param
    	 */
    	setPageList:function(pageList){
    		this.pageList = pageList;
    	},
		/**
    	 * 自定义是否启动排序
    	 * @param param
            */
    	setSortable:function(sortable){
    		this.sortable = sortable;
    	},
		/**
    	 * 自定义是否启动排序
    	 * @param param
            */
    	setEditable:function(editable){
    		this.editable = editable;
    	},
		/**
    	 * 自定义是否列显示功能
    	 * @param param
            */
    	setShowColumns:function(showColumns){
    		this.showColumns = showColumns;
    	},
    	/**
    	 * 自定义是否启动分页
    	 * @param param
            */
    	setPagination:function(pagination){
    		this.pagination = pagination;
    	},
    	/**
         * 刷新 bootstrap 表格
         * Refresh the remote server data,
         * you can set {silent: true} to refresh the data silently,
         * and set {url: newUrl} to change the url.
         * To supply query params specific to this request, set {query: {foo: 'bar'}}
         */
        refresh: function (parms) {
            if (typeof parms != "undefined") {
                this.btInstance.bootstrapTable('refresh', parms);
            } else {
                this.btInstance.bootstrapTable('refresh');
            }
        },
        /**
         * 刷新 bootstrap 表格
         * Refresh the remote server data,
         * you can set {silent: true} to refresh the data silently,
         * and set {url: newUrl} to change the url.
         * To supply query params specific to this request, set {query: {foo: 'bar'}}
         */
        load: function (parms) {
            if (typeof parms != "undefined") {
                this.btInstance.bootstrapTable('load', parms);
            } else {
                this.btInstance.bootstrapTable('load', this.btInstance.bootstrapTable('getData'));
            }
        }
    };
    window.BSTable = BSTable;

});

/**
 * 添加css
 * .bootstrap-table .table:not(.table-condensed)>tbody>tr>td{
			padding: 0px;
			height: 34px;//高度需要根据情况自定义
		}
 * columns:[{}]里添加编辑配置
 * edit:{
			type:"select",//text|input|select
			selectDate:{
				'1':"迭代1",
				'2':"迭代2",
				'3':"迭代3",
				'4':"迭代4",
			},
			selectKey:"iteId",
			selectValue:"iteName",
		},
 *  方法调用bootstrapTable的onClickCell标签
 *  onClickCell: function(field, value, row, $element) {
            onClickEditTable('mytab',field, value, row, $element);
    }
 * 点击编辑表格
 * @param id
 * @param field
 * @param value
 * @param row
 * @param $element
 * @param OnEditableSave 传入方法，用于在外部自定义修改后操作 onEditableSave(edit,field, row, $element);
 */
function onClickEditTable(id, field, value, row, $element,onEditableSave) {
    let columns = $('#'+id).data('bootstrap.table').columns.find(function getStu(element){return element.field == field});
    if(!columns.edit||$element.find('.form-control').length>0){
        return;
    }
    let edit = columns.edit;
    let txt="";
    if(edit.type=="input"){
        txt = $("<input class='form-control' style='width: 100%;height: 100%;' type='text'>").val(value);
    }else if(edit.type=="text"){
        txt = $("<textarea  class='form-control' style='width: 100%;height: 100%;resize: vertical;'>").val(value);
    }else if(edit.type=="select"){
        let select = "<select class='form-control' style='width: 100%;height: 100%;padding: 1px;'>";
        let selectDate = edit.selectDate;
        for(var key in selectDate){
            select += "<option value='"+key+"'>"+selectDate[key]+"</option>";
        }
        select += "</select>";
        txt = $(select).val(value);
    }
    txt.blur(function() {
        let index = $element.parent().data('index');
        let tdValue = $(this).val();
        $(this).remove();
        // saveData(index, field, tdValue);
        if(edit.type=="input"||edit.type=="text"){
            row[field]=tdValue;
        }else if(edit.type=="select"){
        	if(tdValue!=null){
                row[edit.selectKey] = tdValue;
                tdValue = edit.selectDate[tdValue];
                row[edit.selectValue] = tdValue;
			}else{
                tdValue = row[edit.selectValue];
			}
        }
        onEditableSave(edit,field, row, $element);
        $($element).html(tdValue==null?"请选择":tdValue);
    });
    $element.text("");
    $element.append(txt);
    $(txt).focus();
}
