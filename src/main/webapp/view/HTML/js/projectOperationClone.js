var proNo = window.parent.projNo;
var myTable;
$(function(){
    $('#myTable').bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/projectOperation/queryProjectOperationClone',
        editable:false,//可行内编辑
        sortable: true, //是否启用排序
        sortOrder: "asc",
        striped: false, //是否显示行间隔色
        dataField: "rows",
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination:true,//是否分页
        queryParamsType:'limit',
        sidePagination:'server',
        pageSize:10,//单页记录数
        pageList:[5,10,20,30],//分页步进值
        paginationPreText: '&lt上一页',//指定分页条中上一页按钮的图标或文字
        paginationNextText: '下一页&gt',//指定分页条中下一页按钮的图标或文字
        paginationLoop: false, // 关闭分页条无限循环的功能
        //showRefresh:true,//刷新按钮
        showColumns:true,
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        toolbarAlign:'right',
        buttonsAlign:'right',//按钮对齐方式
        toolbar:'#toolbar',//指定工作栏
        queryParams: function(params){
            var param = {
                limit : params.limit, //页面大小
                offset : params.offset //页码
            };
            return param;
        },
        columns:[
            {title:'序号',align: "center",field:'id',width: 30,visible: false,switchable: false,
                formatter: function (value, row, index) {
                    var pageSize=$('#myTable').bootstrapTable('getOptions').pageSize;
                    var pageNumber=$('#myTable').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {title:'操作项目',field:'name',width:150},
            {title:'操作时间',field:'time',width:150},
            {title:'操作用户',field:'userName',width:150},
            {title:'操作行为',field:'message',width:150}
        ],
        locale:'zh-CN'//中文支持,
    });
});

//根据窗口调整表格高度
$(window).resize(function() {
    $(".fixed-table-body").css({"min-height":$(window).height()-105});
});
$(document).ready(function(){
    $(".fixed-table-body").css({"min-height":$(window).height()-105});
});



