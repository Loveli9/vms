var twelves = [];

$(function () {
    var projectId = window.parent.projNo;
    //calendar.reverse();//数组取反
    var str = '开发工程师,PM,产品经理,SE,MDE,BA,IA,运维工程师';
    var arr = str.split(',');
    $("#userRole").selectpicker('val', arr);

    var columnsArray = [];
    columnsArray.push({
        "title": '姓名',
        "field": 'name',
        halign: 'center',
        align: 'center',
        width: 42
    });
    columnsArray.push({
        "title": '工号',
        "field": 'account',
        halign: 'center',
        align: 'center',
        width: 42
    });
    columnsArray.push({
        "title": '岗位',
        "field": 'role',
        halign: 'center',
        align: 'center',
        width: 42
    });

    $.ajax({
        async: false,
        type: "GET",
        url: getRootPath() + '/workload/abscissa',
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        json: 'callback',
        success: function (json) {
            twelves = json.data;
            for (var i = 0; i < twelves.length; i++) {
                var title = twelves[i];
                columnsArray.push({
                    title: title,
                    field: title,
                    halign: 'center',
                    align: 'center',
                    width: 42,
                    formatter: function (value, row, index, field) {
                        var count = $('#mytab').bootstrapTable('getData').length - 1;
                        if (count == index) {
                            return value;
                        }

                        if (row && row['workloads'] && row['workloads'][field]) {
                            var load = row['workloads'][field];
                            if (load && load['times'] > 0) {
                                value = load['amount'] == 0 ? "--" : load['amount'];
                                var tips = '提交次数: ' + load['times'] + '</br>' + '最后提交时间: ' + load['lastCommitTime'] + '</br>';
                                tips = load['amount'] > 0 ? (load['type'] + ': ' + load['amount'] + '</br>' + tips) : tips;

                                var show = '<a class="tooltip-test" data-toggle="tooltip"' +
                                    ' data-placement="bottom" data-html="true" title="' + tips + '" >'
                                    + value + '</a>';

                                return show;
                            }
                        }
                    },
                    //footerFormatter: function (value) {
                    //    var count = 0;
                    //    for (var i in value) {
                    //        count += value[i];
                    //    }
                    //    return count;
                    //}
                });
            }
        }
    });

    columnsArray.push({
        "title": '合计',
        "field": 'quantity',
        halign: 'center',
        align: 'center',
        width: 42,
        formatter: function (value, row, index) {
            var count = $('#mytab').bootstrapTable('getData').length - 1;
            if (count == index) {
                return value;
            }

            var total = 0;
            if (row && row['workloads']) {
                var loads = row['workloads'];
                for (var key in loads) {
                    total += loads[key]['amount'];
                }
            }
            return total > 0 ? total : "-";
        },
    });

    $('#mytab').bootstrapTable({
        method: 'get',
        //contentType: "application/x-www-form-urlencoded",
        url: getRootPath() + '/workload/metric',
//    	height:tableHeight(),//高度调整
        toolbar: '#toolbar',
        //editable:true,//可行内编辑
//    	showToggle:true, 
//    	sortable: true, //是否启用排序
//    	sortOrder: "asc",
        striped: true, //是否显示行间隔色
        dataField: "data",
//    	pageNumber: 1, //初始化加载第一页，默认第一页
//    	pagination:true,//是否分页
        queryParamsType: 'limit',
        sidePagination: 'server',
//    	pageSize:30,//单页记录数
//    	pageList:[5,10,20,30],//分页步进值
//    	clickToSelect: true,//是否启用点击选中行
        toolbarAlign: 'right',
        buttonsAlign: 'right',//按钮对齐方式
        queryParams: function (params) {
            var roles = "";
            if ($("#userRole").val()) {
                roles = $("#userRole").val().join(',');
            }
            var param = {
                projectId: projectId,
                //type: option1,
                codeType: $('#personal-code-type').val(),
                duty: encodeURI(roles)
            };
            return param;
        },
        //showFooter: true,
        columns: columnsArray,
        locale: 'zh-CN',//中文支持,
        onLoadSuccess: function () {
            addRow();
            tip();
        }
    });
});

function addRow() {
    //var count = $('#mytab').bootstrapTable('getData').length;
    var summation = $('#mytab').bootstrapTable('getData');


    if (summation && summation.length > 0) {
        var count = summation.length;

        var total = {};


        for (var j = 0, len = count; j < len; j++) {
            var loads = summation[j]['workloads'];
            if (loads) {
                for (var key in loads) {
                    var amount = loads[key]['amount'];
                    if (amount > 0) {
                        var monthAmount = total[key];
                        total[key] = monthAmount ? monthAmount + amount : amount;
                    }
                }
            }
        }

        for (var key in total) {
            var amount = total[key];
            if (amount > 0) {
                var totalAmount = total['quantity'];
                total['quantity'] = totalAmount ? totalAmount + amount : amount;
            }
        }
        total['name'] = "合计";

        $('#mytab').bootstrapTable('insertRow', {
            index: count,
            row: total
        });

        $('#mytab').bootstrapTable('mergeCells', {
            index: count,
            field: 'name',
            colspan: 3,
            rowspan: 1
        });
    }
}

function tip() {
    $("[data-toggle='tooltip']").tooltip();
}



