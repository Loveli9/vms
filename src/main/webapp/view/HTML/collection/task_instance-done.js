; $(function () {
    $('#interface_table').bootstrapTable({
        method: 'post',
        contentType: "application/x-www-form-urlencoded",
        url: "http://192.168.0.11:8080/datacollection/task_instance/query",
        editable: false,//可行内编辑
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        sortable: false, //是否启用排序
        sortOrder: "asc",
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 40], //分页步进值
        striped: false, //是否显示行间隔色
        dataField: "rows",
        queryParamsType: 'limit',
        sidePagination: 'server',
        queryParams: function (params) {
            var param = {
                "params.status_notin": 1,
                "params.status_notin": 2,
                "pageable.size": params.limit, //页面大小
                "pageable.start": params.limit * (params.pageNumber - 1),
                "pageable.page": params.pageNumber - 1, //页码
                "pageable.sort": '[{"property":"startTime","direction":"DESC"}]' //排位命令（desc，asc）
            }
            return param;
        },
        responseHandler: function (res) {
            return {
                "rows": res.data,
                "total": res.total
            }
        },
        // showColumns: true,
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true,//是否启用点击选中行
        columns: [
            {
                title: '全选',
                field: 'select',
                radio: true,
                width: 30,
                align: 'center',
                valign: 'middle',
            },
            {
                title: '任务分组',
                halign: 'center',
                align: 'center',
                field: 'group',
                sortable: false,
                width: 70,
            },
            {
                title: '任务名称',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'name',
                width: 70,
            },
            {
                title: '开始时间',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'startTime',
                width: 70,
            },
            {
                title: '结束时间',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'endTime',
                width: 70,
            },
            {
                title: '状态',
                halign: 'center',
                align: 'center',
                sortable: false,
                field: 'status',
                width: 70,
                formatter: function (value, row, index) {
                    var status = "";
                    switch (row.status) {
                        case 1:
                            status = "等执行"
                            break;
                        case 2:
                            status = "执行中"
                            break;
                        case 3:
                            status = "已结束"
                            break;

                    }
                    return status
                }
            },

        ],
        locale: 'zh-CN',//中文支持,
    });

})