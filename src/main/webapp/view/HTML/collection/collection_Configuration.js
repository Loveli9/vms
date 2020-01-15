; $(function () {
    var projectId = 16043;
    $.ajax({
        contentType: "application/x-www-form-urlencoded", //设置请求头信息
        url: getRootPath()+"/datacollection/config/collection_config/get_by_project_id",
        type: 'post',
        method: 'post',
        data: { pid: projectId },
        success: function (res) {
            
        }
    });
});