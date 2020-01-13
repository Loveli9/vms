$(function () {
    $(document).ready(function () {
        var Menus = getCookie('Menulist');
        var flag = false;

        if (Menus.indexOf("-6-") != -1) {
            var tab = '<li tabname="tab-groupPerson" id="groupPerson" onclick="loadGroupPerson()" style="border: 0px;margin-left: 15px;"><a href="###">项目群人员跟踪</a></li>';
            $("#ul-groupPerson").append(tab);

            if (!flag) {
                $("#groupPerson").click();
                flag = true;
            }
        }
    })
});

function loadGroupPerson() {
    groupPerson();
}

function groupPerson() {
    $('#groupPersonTable').bootstrapTable('destroy');
    $("#groupPersonTable").bootstrapTable({
        method: 'GET',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/personnelTrack/groupPersonTrack',
        striped: false, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: true, //是否分页
        paginationPreText: '<上一页',//指定分页条中上一页按钮的图标或文字
        paginationNextText: '下一页>',//指定分页条中下一页按钮的图标或文字
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
        queryParams: function (params) {
            var param = {
                'limit': params.limit,
                'offset': params.offset
            }
            return param;
        },
        columns: [
            [
                {
                    title: '序号', align: "center", valign: 'middle', width: '6%',
                    formatter: function (value, row, index) {
                        var pageSize = $('#groupPersonTable').bootstrapTable('getOptions').pageSize;
                        var pageNumber = $('#groupPersonTable').bootstrapTable('getOptions').pageNumber;
                        return pageSize * (pageNumber - 1) + index + 1;
                    }
                },
                {
                    title: '项目名称', field: 'NAME', align: "center", valign: 'middle', width: '20%',
                    formatter: function (value, row, index) {
                        return '<p title="' + value + '" align="center" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">' + value + '</p>';
                    }
                },
                {title: 'PM', field: 'PM', align: "center", valign: 'middle', width: '10%'},
                {
                    title: '部门', field: 'DEPARTMENT', align: "center", valign: 'middle', width: '20%',
                    formatter: function (value, row, index) {
                        return '<p title="' + value + '" align="center" style="word-break:break-all;overflow: hidden;text-overflow: ellipsis;">' + value + '</p>';
                    }
                },
                {title: '类型', field: 'PROJECT_TYPE', align: "center", valign: 'middle', width: '8%'},
                {title: '其他', field: '', align: "center", valign: 'middle', width: '36%'}
            ]
        ],
        locale: 'zh-CN'//中文支持
    });
}