$(function(){
    $('#myTable').bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/projectOverview/getAllNews',
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
            {title:'发送人',field:'sender',width:150},
            {title:'消息内容',field:'information',width:400},
            {title:'读取时间',field:'receivingTime',width:150,
                //获取日期列的值进行转换
                formatter: function (value, row, index) {
                    return changeDateFormat(value)
                }
            },
            {title:'发送时间',field:'sendTime',width:150,
                formatter: function (value, row, index) {
                    return changeDateFormat(value)
                }
            }
        ],
        locale:'zh-CN'//中文支持,
    });

    //转换日期格式(时间戳转换为datetime格式)
    function changeDateFormat(cellval) {
        var dateVal = cellval + "";
        if (cellval != null) {
            var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
            var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            return date.getFullYear() + "-" + month + "-" + currentDate + " " + hours + ":" + minutes + ":" + seconds;
        }
    }

    $(document).on("click","#information li",function(){
        var tis=$(this);
        var tabname=tis.attr("tabname");
        $("#information").children().removeClass("active");
        $("#information li[tabname='"+tabname+"']").addClass("active");
    });
});

//发送消息
function changeQuality(){
    $("#addMilestonePage").modal("show");
}

//清除发送内容
function backBtn(){
    $("#addMilestonePage").modal("hide");
    $("#jobNumber").val("");
    $("#content").val("");
}

//发送内容
function saveBtn(){
    var job = $("#jobNumber").val().replace(/\s*/g,"");
    var content = $("#content").val().replace(/\s*/g,"");
    if((job == "" || job == null) || (content == "" || content == null)) {
        toastr.warning('接收人工号和消息内容不能为空！');
    }else{
        $.ajax({
            url:getRootPath() + '/projectOverview/saveInformation',
            type : "post",
            dataType : "json",
            cache : false,
            data:{
                'job':job,
                'content':content
            },
            success:function(data){
                console.log(data);
                if(data.code=='success'){
                    toastr.success('发送成功！');
                    backBtn();
                }
                else{
                    toastr.error(data.message);
                }
            }
        });
    }
}

//根据窗口调整表格高度
$(window).resize(function() {
    $(".fixed-table-body").css({"min-height":$(window).height()-175});
});
$(document).ready(function(){
    $(".fixed-table-body").css({"min-height":$(window).height()-175});
});



