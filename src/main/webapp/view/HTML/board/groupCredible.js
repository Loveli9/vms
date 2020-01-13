$(function () {
    $(document).ready(function () {
        var Menus = getCookie('Menulist');
        var flag = false;

        if (Menus.indexOf("-6-") != -1) {
            var tab = '<li tabname="tab-groupCredible" id="groupCredible" onclick="loadGroupCredible()" style="border: 0px;margin-left: 15px;"><a href="###">项目群可信跟踪</a></li>';
            $("#ul-groupCredible").append(tab);

            if (!flag) {
                $("#groupCredible").click();
                flag = true;
            }
        }
    })
});

function loadGroupCredible() {
    groupCredible();
}

function groupCredible() {
    $('#groupCredibleTable').bootstrapTable('destroy');
    $("#groupCredibleTable").bootstrapTable({
        method: 'GET',
        contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/personnelTrack/overallAssessment',
        striped: false, //是否显示行间隔色
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination: false, //是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
        pageSize: 20, //单页记录数
        pageList: [5, 10, 20, 30], //分页步进值
        minimumCountColumns: 2, //最少允许的列数
        queryParams: function (params) {
            var param = {
                projectId: 'HWHZP5FF1606F03X11',
                duty: '开发工程师',
            }
            return param;
        },
        columns: [
            [
                {title: '姓名', field: 'name', align: 'center', valign: 'middle', width: '10%', rowspan: 2, colspan: 1},
                {
                    title: '工号',
                    field: 'account',
                    align: 'center',
                    valign: 'middle',
                    width: '10%',
                    rowspan: 2,
                    colspan: 1
                },
                {title: '岗位', field: 'role', align: "center", valign: 'middle', width: '10%', rowspan: 2, colspan: 1},
                {title: '需求', align: 'center', valign: 'middle', width: '10%', rowspan: 1, colspan: 1},
                {title: '产出', align: 'center', valign: 'middle', width: '30%', rowspan: 1, colspan: 3},
                {title: '风险', align: 'center', valign: 'middle', width: '33%', rowspan: 1, colspan: 3}
            ],
            [
                {
                    title: '需求完成率', field: 'demandCompletion', align: 'center', valign: 'middle'
                },
                {
                    title: '代码量', field: 'codeQuantity', align: 'center', valign: 'middle'
                },
                {
                    title: '测试用例', field: 'testCase', align: 'center', valign: 'middle'
                },
                {
                    title: '文档', field: 'file', align: 'center', valign: 'middle'
                },
                {
                    title: '个人缺陷数', field: 'personalDefects', align: 'center', valign: 'middle'
                },
                {
                    title: '风险人员', field: 'riskPersonnel', align: 'center', valign: 'middle'
                },
                {
                    title: '风险原因', field: 'riskReason', align: 'center', valign: 'middle'
                }
            ]
        ],
        locale: 'zh-CN'//中文支持
    });
}