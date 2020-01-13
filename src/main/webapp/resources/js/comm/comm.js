var accountList = "";

function getAngularUrl() {
    var curWwwPath = window.document.location.href;
    // 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    // 获取主机地址，如： http://localhost:8083
    var localhostPath = curWwwPath.substring(0, pos);

    return localhostPath + "/angular/#/";
    // return "http://localhost:4200/#/";
}

var fomatFloat = function (num, n) {
    var f = parseFloat(num);
    if (isNaN(f)) {
        return false;
    }
    f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n); // n 幂
    var s = f.toString();
    var rs = s.indexOf('.');
    //判定如果是整数，增加小数点再补0
    if (rs < 0) {
        rs = s.length;
        s += '.';
    }
    while (s.length <= rs + n) {
        s += '0';
    }
    return parseFloat(s);
}

function getLamp(id, val) {
    if (val == "" || val == null) {
        val = -2;
    }
    var max, middle, min;
    if (id == "progress") {
        max = 10;
        midlle = 10;
        min = 9;
        if (val > 10) {
            val = 10;
        }
    } else if (id == "quality") {
        max = 15;
        midlle = 12;
        min = 10;
        if (val > 15) {
            val = 15;
        }
    } else if (id == "resources") {
        max = 25;
        midlle = 25;
        min = 22;
        if (val > 25) {
            val = 25;
        }
    } else {
        max = 100;
        midlle = 80;
        min = 70;
        if (val > 100) {
            val = 100;
        }
    }
    var lamp = "";
    var text = "";

    if (val >= midlle && val <= max) {
        lamp = "green"
        text = "正常";
    } else if (val >= min && val < midlle) {
        lamp = "#FFC000";
        text = "预警";
    } else if (val >= 0 && val < min) {
        lamp = "red";
        text = "风险";
    } else if (val == -2) {
        lamp = "#C0C0C0";
        text = "未评估";
    } else if (val == -1) {
        lamp = "";
        text = "";
    }
    return {"lamp": lamp, "text": text};
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}

// $(window).bind('scroll',function(){
//     var scrollHeight = document.documentElement.scrollHeight;
//     var scrollTop = window.scrollY;
//     var innerHeight =  window.innerHeight;
//     if ((innerHeight + scrollTop + 100) >= scrollHeight) {
//         if($("#sub-page", parent.parent.document)){
//             $("#sub-page", parent.parent.document).animate({"bottom":"40px"},50);//.css("bottom","40px");
//             $(".foot", parent.parent.document).animate({"height":"40px"},50);
//         }else{
//             $("#sub-page", parent.document).animate({"bottom":"40px"},50);
//             $(".foot", parent.document).animate({"height":"40px"},50);
//         }
//
//     }else{
//         if($("#sub-page", parent.parent.document)) {
//             $("#sub-page", parent.parent.document).animate({"bottom":"0px"},50);//.css("bottom", "0px");
//             $(".foot", parent.parent.document).animate({"height":"0px"},50);
//         }else{
//             $("#sub-page", parent.document).animate({"bottom":"0px"},50);
//             $(".foot", parent.document).animate({"height":"0px"},50);
//         }
//     }
// });
function toProjectHome() {
    $(window.parent.parent.document.getElementById("mainPage")).attr("src", "projectManagement.html");
    // $("#mainPage").attr("src","projectManagement.html");
}

function toTeamHome() {
    $(window.parent.parent.document.getElementById("mainPage")).attr("src", "teamManagement.html");
    // $("#mainPage").attr("src","projectManagement.html");
}

function setCookie(name, value) {
    var Days = 7;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + encodeURIComponent(value) + ";expires=" + exp.toGMTString() + ";path=/";
}

function getCookie(name) {
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) {
        return decodeURIComponent(arr[2]);
    } else {
        return null;
    }
};

$.ajaxSetup({
    //设置ajax请求结束后的执行动作
    complete:
        function (XMLHttpRequest, textStatus) {
            // 通过XMLHttpRequest取得响应头，sessionstatus
            var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
            if (sessionstatus == "TIMEOUT") {
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = XMLHttpRequest.getResponseHeader("CONTEXTPATH");
            }
        }
});

//清除cookie
function clearCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null) {
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() + ";path=/";
    }
}

// 描述：获取url当前项目的url地址头信息
function getRootPath() {
    // 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    // 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    // 获取主机地址，如： http://localhost:8083
    var localhostPath = curWwwPath.substring(0, pos);
    // 获取带"/"的项目名，如：/uimcardprj
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);

    return (localhostPath + projectName);
}

function getParameter(param) {
    var query = window.location.search;// 获取URL地址中？后的所有字符
    var iLen = param.length;// 获取你的参数名称长度
    var iStart = query.indexOf(param);// 获取你该参数名称的其实索引
    if (iStart == -1)// -1为没有该参数
        return "";
    iStart += iLen + 1;
    var iEnd = query.indexOf("&", iStart);// 获取第二个参数的其实索引
    if (iEnd == -1)// 只有一个参数
        return query.substring(iStart);// 获取单个参数的参数值
    return query.substring(iStart, iEnd);// 获取第二个参数的值
}

var dateFormat = function (fmt, date) {
    let ret;
    let opt = {
        "Y+": date.getFullYear().toString(),        // 年
        "m+": (date.getMonth() + 1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "H+": date.getHours().toString(),           // 时
        "M+": date.getMinutes().toString(),         // 分
        "S+": date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
    };
    for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        }
    }
    return fmt;
}

/**
 *
 * @param value
 * @returns
 *为项目详情页面各页签获取数据
 */
function initGrid(projNo, bigType, smallType, gridId, selId) {
    var defer = $.Deferred();
    $.ajax({
        url: getRootPath() + "/bu/meausreResult",
        type: 'POST',
        data: {
            projNo: projNo,
            bigType: bigType,
            smallType: smallType
        },
        success: function (data) {
            var options = {
                rownumbers: false,
                striped: true,
                fitColumns: false,
                nowrap: true,
                onLoadSuccess: function (data) {
                    $("#" + gridId).datagrid('doCellTip', {
                        cls: {},
                        delay: 1000,
                        titleUnit: $("#" + gridId).data('gridUnit')
                    });
                }
            };
            var wdth = Math.round(100 / data.title.length);
            var lastWd = Math.round(100 / data.title.length) * data.title.length;
            lastWd = 100 > lastWd ? (wdth + 100 - lastWd) : (wdth - lastWd + 100);
            _.each(data.title, function (val, index) {
                val.width = wdth + '%';
            })
            data.title[data.title.length - 1].width = lastWd + '%';
            $("#" + gridId).data('gridUnit', data.units);
            options.columns = [data.title];
            $("#" + gridId).datagrid(options);
            var gridDatas = {};
            $.extend(true, gridDatas, data.gridDatas);
            $("#" + gridId).datagrid("loadData", gridDatas);
            if (selId) {
                var options = "";
                _.each(data.title, function (val, index) {
                    if (val.field != 'month') {
                        options += '<option value="' + val.title + '">' + val.title + '</option>';
                    }
                })
                $('#' + selId).html(options);
            }
            $("#" + gridId).data('gridData', data.gridDatas);

            defer.resolve();
        }
    })
    return defer;
}

/**
 *
 * @param indicatorId
 * @param name
 * 绘制柱状图
 */
function reloadChart(gridData, name, chartOpt, chartId) {
    chartOpt.xAxis = [];
    chartOpt.series = [];
    if (gridData && !_.isEmpty(gridData.rows)) {
        chartOpt.xAxis = [{
            data: _.pluck(gridData.rows, 'month')
        }];
        chartOpt.series = [{
            name: name,
            type: 'bar',
            data: _.pluck(gridData.rows, name)
        }];
    }
    chartOpt.legend = {data: [name]};
    chartId.setOption(chartOpt);
};

function initMouseOverMenu() {
    $('.list > .yiji ul li').hover(function () {
        var eq = $('.list > .yiji ul li').index(this), 			//获取当前滑过是第几个元素
            h = $('.list').offset().top, 					//获取当前下拉菜单距离窗口多少像素
            s = $(window).scrollTop(), 								//获取游览器滚动了多少高度
            i = $(this).offset().top, 								//当前元素滑过距离窗口多少像素
            item = $(this).children('.item').height(), 			//下拉菜单子类内容容器的高度
            sort = $('.list').height(); 					//父类分类列表容器的高度

        if (item < sort) {												//如果子类的高度小于父类的高度
            if (eq == 0) {
                $(this).children('.item').css('top', (i - h));
            } else {
                $(this).children('.item').css('top', (i - h) + 1);
            }
        } else {
            if (s > h) {												//判断子类的显示位置，如果滚动的高度大于所有分类列表容器的高度
                if (i - s > 0) {											//则 继续判断当前滑过容器的位置 是否有一半超出窗口一半在窗口内显示的Bug,
                    $(this).children('.item').css('top', (s - h) + 2);
                } else {
                    $(this).children('.item').css('top', (s - h) - (-(i - s)) + 2);
                }
            } else {
                $(this).children('.item').css('top', 3);
            }
        }

        $(this).addClass('hover');
        $(this).children('.item').css('display', 'block');
    }, function () {
        $(this).removeClass('hover');
        $(this).children('.item').css('display', 'none');
    });
};

function initDownMenu() {
    $('.inactive').click(function (e) {
        e.preventDefault();
        if ($(this).siblings('ul').css('display') == 'none') {
            $(this).parent('li').siblings('li').children('a').removeClass('inactives');
            $(this).addClass('inactives');
            $(this).parents('li').siblings('li').children('ul').slideUp(100);
            $(this).siblings('ul').slideDown(100).children('li');
        } else {
            //控制自身变成+号
            $(this).removeClass('inactives');
            //控制自身菜单下子菜单隐藏
            $(this).siblings('ul').slideUp(100);
            //控制自身子菜单变成+号
//                $(this).siblings('ul').children('li').children('ul').parent('li').children('a').addClass('inactives');
//                //控制自身菜单下子菜单隐藏
            $(this).siblings('ul').children('li').children('ul').slideUp(100);

            //控制同级菜单只保持一个是展开的（-号显示）
            $(this).siblings('ul').children('li').children('a').removeClass('inactives');
        }
    });
};

/**
 *
 * @param value
 * @returns
 * 为easyui提供显示时间格式的数据
 */

function formatDatebox(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
        if (isNaN(dt)) {
            value = value.replace(/\/Date(−?\d+)\//, '$1'); //标红的这段是关键代码，将那个长字符串的日期值转换成正常的JS日期格式  
            dt = new Date();
            dt.setTime(value);
        }
    }

    return dt.format("yyyy-MM-dd");   //这里用到一个javascript的Date类型的拓展方法，这个是自己添加的拓展方法，在后面的步骤3定义  
}


$.extend(
    $.fn.datagrid.defaults.editors, {
        datebox: {
            init: function (container, options) {
                var input = $('').appendTo(container);
                input.datebox(options);
                return input;
            },
            destroy: function (target) {
                $(target).datebox('destroy');
            },
            getValue: function (target) {
                return $(target).datebox('getValue');
            },
            setValue: function (target, value) {
                $(target).datebox('setValue', formatDatebox(value));
            },
            resize: function (target, width) {
                $(target).datebox('resize', width);
            }
        }
    });
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month   
        "d+": this.getDate(),    //day   
        "h+": this.getHours(),   //hour   
        "m+": this.getMinutes(), //minute   
        "s+": this.getSeconds(), //second   
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter   
        "S": this.getMilliseconds() //millisecond   
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
        (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
            RegExp.$1.length == 1 ? o[k] :
                ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}


function pagerFilter(data) {
    if ($.isArray(data)) {	// is array
        data = {
            total: data.length,
            rows: data
        }
    }
    var target = this;
    var dg = $(target);
    var state = dg.data('datagrid');
    var opts = dg.datagrid('options');
    if (!state.allRows) {
        state.allRows = (data.rows);
    }
    if (!opts.remoteSort && opts.sortName) {
        var names = opts.sortName.split(',');
        var orders = opts.sortOrder.split(',');
        state.allRows.sort(function (r1, r2) {
            var r = 0;
            for (var i = 0; i < names.length; i++) {
                var sn = names[i];
                var so = orders[i];
                var col = $(target).datagrid('getColumnOption', sn);
                var sortFunc = col.sorter || function (a, b) {
                    return a == b ? 0 : (a > b ? 1 : -1);
                };
                r = sortFunc(r1[sn], r2[sn]) * (so == 'asc' ? 1 : -1);
                if (r != 0) {
                    return r;
                }
            }
            return r;
        });
    }
    var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = state.allRows.slice(start, end);
    return data;
}

var loadDataMethod = $.fn.datagrid.methods.loadData;
var deleteRowMethod = $.fn.datagrid.methods.deleteRow;
$.extend($.fn.datagrid.methods, {
    clientPaging: function (jq) {
        return jq.each(function () {
            var dg = $(this);
            var state = dg.data('datagrid');
            var opts = state.options;
            opts.loadFilter = pagerFilter;
            var onBeforeLoad = opts.onBeforeLoad;
            opts.onBeforeLoad = function (param) {
                state.allRows = null;
                return onBeforeLoad.call(this, param);
            }
            var pager = dg.datagrid('getPager');
            pager.pagination({
                onSelectPage: function (pageNum, pageSize) {
                    opts.pageNumber = pageNum;
                    opts.pageSize = pageSize;
                    pager.pagination('refresh', {
                        pageNumber: pageNum,
                        pageSize: pageSize
                    });
                    dg.datagrid('loadData', state.allRows);
                }
            });
            $(this).datagrid('loadData', state.data);
            if (opts.url) {
                $(this).datagrid('reload');
            }
        });
    },
    loadData: function (jq, data) {
        jq.each(function () {
            $(this).data('datagrid').allRows = null;
        });
        return loadDataMethod.call($.fn.datagrid.methods, jq, data);
    },
    deleteRow: function (jq, index) {
        return jq.each(function () {
            var row = $(this).datagrid('getRows')[index];
            deleteRowMethod.call($.fn.datagrid.methods, $(this), index);
            var state = $(this).data('datagrid');
            if (state.options.loadFilter == pagerFilter) {
                for (var i = 0; i < state.allRows.length; i++) {
                    if (state.allRows[i] == row) {
                        state.allRows.splice(i, 1);
                        break;
                    }
                }
                $(this).datagrid('loadData', state.allRows);
            }
        });
    },
    getAllRows: function (jq) {
        return jq.data('datagrid').allRows;
    }
})

/**
 * 验证字符串是否包含特殊字符
 * @param str
 * @param condition
 * @returns
 */
function checkMemberData(str, condition) {
    if (queryRegularExpression(condition).test(str)) {
        return true;
    } else {
        return false;
    }
}

/**
 * 查询成员正则表达式
 * @param condition
 * @returns
 */
function queryRegularExpression(condition) {
    var re = null;
    if ('姓名' == condition) {
        re = /^[\u4e00-\u9fa5\s]+$/;
    } else if ('中软工号' == condition) {
        re = /^[\d\s]*$/;
    } else if ('华为工号' == condition) {
        re = /^[A-Za-z0-9\s]+$/;
    }

    return re;
}

function getDeviationLight(val) {
    var color = "";
    if (parseFloat(val) > 0.15 || parseFloat(val) < -0.15) {
        color = '#C00000';
    } else if (parseFloat(val) > 0.10 || parseFloat(val) < -0.10) {
        color = '#FFC000';
    } else {
        color = '#008000';
    }

    return color;
}

/**
 * 成本录入权限
 * @returns
 */
function costAuthors() {
    return "项目PM, 项目经理pm";
}

/**
 * 成本报表权限
 * @returns
 */
function costReportAuthors() {
    return "业务线PO, 交付部PO, 事业部PO";
}

/**
 * 获取项目人员中软工号、华为工号
 * @returns
 */
function getProjectAccount(projNo) {
    $.ajax({
        url: getRootPath() + '/GeneralSituation/getMemberAccount',
        type: 'post',
        traditional: true,
        data: {
            'projNo': projNo
        },
        async: false,// 是否异步，true为异步
        success: function (data) {
            accountList = data;
        }
    });
    return accountList;
}

/**
 * 字符串前面补零
 * @param code
 * @param n
 * @returns
 */
function zeroFill(code, n) {
    var res = code;

    if (code.length >= n) {
        return res;
    }
    return zeroFill("0" + code, n);
}

/**
 * 更改职员职级为初始职级
 * @param zrAccount
 * @returns
 */
function resetRank(zrAccount, rank, flag){
	$.ajax({
		url : getRootPath() + '/GeneralSituation/resetRank',
		type : 'post',
		traditional:true,
		data : {
			'zrAccount' : zrAccount,
			'rank' : rank,
		},
		async : false,// 是否异步，true为异步
		success : function(data) {
			if(data && "success" == data.code){
				toastr.success("职级重置成功!");
				
				if("team" == flag){
					setTimeout("$('#mytab').bootstrapTable('refresh',{pageNumber:0})", "500");
				}else{
					setTimeout("$('#tdcyinfo').click()", "500");
					$('#userManagerlocal').bootstrapTable('refresh', {url: getRootPath() + '/user/getProjectMembers'})
				}
			}else{
				toastr.warning("无初始职级!");
			}
		}
	});
}

//获取下拉列表的值
function getSelectValueByType(code) {
    var s = "";
    $.ajax({
        url: getRootPath() + '/dict/items?entryCode=' + code,
        type: 'get',
        async: false,
        dataType: "json",
        contentType: 'application/x-www-form-urlencoded', //设置请求头信息
        success: function (data) {
            if (data.code == '200') {
                s = data.data;
                for (var i = 0; i < s.length; i++) {
                    s[i].text = s[i].key;
                }
            } else {
                toastr.error('查询失败!');
            }
        }
    });
    return s;
}

//组装select下拉列表
function setOption(arr, id, name) {
    var option = "<option value=''>" + "请选择" + name + "</option>";
    for (j = 0, len = arr.length; j < len; j++) {
        option += "<option value='" + arr[j].value + "'>" + arr[j].key + "</option>";
    }
    $(id).html(option);
}

//表格展示时根据列value与数据字典匹配，返回对应key值
function formatColumnVal(val, arr) {
    var v = '';
    for (j = 0, len = arr.length; j < len; j++) {
        if (arr[j].value == val) {
            v = arr[j].key;
        }
    }
    return v;
}

/**
 * 加载组织看板 & 项目看板日期
 */
function initDateSection() {
    $.ajax({
        url: getRootPath() + '/measureComment/getTime',
        type: 'post',
        data: {
            num: 6,
            flag: true,
        },
        async: false,//是否异步，true为异步
        success: function (data) {
            $("#dateSection").empty();
            data = data.data;
            if (null == data || "" == data) return;
            var select_option = "";
            var length = data.length < 5 ? data.length : 5;
            for (var i = 0; i < length; i++) {
                select_option += "<option value='" + data[i] + "'";
                if (length > 1) {
                    if (1 == i) {
                        select_option += "selected='selected'";
                    }
                } else {
                    if (page == i) {
                        select_option += "selected='selected'";
                    }
                }
                select_option += ">" + data[i] + "</option>";
            }
            $("#currentTime").html("当前周期:" + data[0]);
            $("#dateSection").html(select_option);
            $('#dateSection').selectpicker('refresh');
            $('#dateSection').selectpicker('render');
        }
    });
};

/**
 * 补全Bootstrap表格显示不完整的信息
 * @param value
 * @param style
 * @returns {string}
 */
function completeInformation(value, style) {
    if (null != value) {
        return '<p title= "' + value + '" align= ' + style + ' style="word-break: break-all; overflow: hidden; text-overflow: ellipsis;">' + value + '</p>';
    }
}

/**
 * 项目看板项目状态显示转换, 0: 否; 1: 是
 * @param value
 * @returns {string}
 */
function projectBoardDisplayTransform(value) {
    var res = value;

    if (0 == value) {
        res = "否";
    } else if (1 == value) {
        res = "是";
    }
    return res;
}
