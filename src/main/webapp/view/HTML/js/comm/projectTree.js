
    var opts1;
    var data=null;
    var projNo_comm;
    var path;
    var clickTreeId;
    var level;
    var projectName = null;
    /*bootstrap-table实现项目列表*/
    function loadGridData1(){
        var url = getRootPath() + "/projects/page?";
        var treeIdList = "";
        if(clickTreeId){
            treeIdList = clickTreeId.split(">");
        }
        if(path == 0){
            switch (level) {
                case 1:
                    data = {
                        'client': "1",
                        'bu': treeIdList[0],
                    };
                    break;
                case 2:
                    var treePid = clickTreeId;
                    data = {
                        'client': "1",
                        'bu': treeIdList[1],
                        'pdu': treeIdList[0],
                    };
                    break;
                case 3:
                    var treePid = clickTreeId;
                    data = {
                        'client': "1",
                        'bu': treeIdList[2],//转换为字符串
                        'pdu': treeIdList[1],
                        'du':treeIdList[0],
                    };
                    break;
                default:
                    data = {
                        'client': "1",
                    };
                    break;
            }

        }else{
            switch (level) {
                case 1:
                    data = {
                        'client': "0",
                        'hwpdu': treeIdList[0],
                    };
                    break;
                case 2:
                    var treePid = clickTreeId;
                    data = {
                        'client': "0",
                        'hwpdu': treeIdList[1],
                        'hwzpdu': treeIdList[0],
                    };
                    break;
                case 3:
                    var treePid = clickTreeId;
                    data = {
                        'client': "0",
                        'hwpdu': treeIdList[2],
                        'hwzpdu': treeIdList[1],
                        'pduSpdt': treeIdList[0],
                    };
                    break;
                default:
                    data = {
                        'client': "0",
                    };
                    break;
            }
        }
        $("#projectTable").bootstrapTable('destroy');
        $('#projectTable').bootstrapTable({
            method : 'get',
            url : url,//请求路径
            striped : true, //是否显示行间隔色
            pageNumber : 1, //初始化加载第一页
            pagination : true,//是否分页
            sidePagination : 'server',//server:服务器端分页|client：前端分页
            pageSize : 10,//单页记录数
            pageList : [10,20,30],//可选择单页记录数
            queryParams : function(params) {//上传服务器的参数
                var temp = data;
                temp.pageSize = params.limit;
                temp.pageNumber = params.pageNumber-1;
                temp.name = projectName;
                return temp;
            },
            responseHandler: function (res) {
                var result = {};
                result.total = res.totalCount;
                result.rows = res.data;
                return result;
            },
            columns : [ {
                title : '名称',
                field : 'name',
                width:150,
                formatter:function(value,row,index){
                    return '<a target="_parent" style="color: #721b77;" title="'+value +'" onclick="setProjectInfos(\''+row.no+'\',\''+row.name+'\')">'+value+'</a>';
                }
            }, {
                title : '项目经理',
                field : 'pm',
                width:100
            }, {
                title : '地域',
                field : 'area',
                width:100
            },  {
                title : '项目状态',
                field : 'projectState',
                width:80
            }]
        });
    }


function setProjectInfos(no,name){
    projNo_comm = no;
    $("#queryBtn").html(name);
    $("#choseBtn").css('display','');
    $("#projectModal").modal('hide');
    setCookie('projNo_comm',no);
    setCookie('proj_name',name);
}



var zNodes_comm = [];// 树节点数组
var setting_comm = {
    view: {
        selectedMulti: false, // 设置是否能够同时选中多个节点
        showIcon: false,      // 设置是否显示节点图标
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
        enable: false
    },
    callback: {
        onClick: clickTree_comm,
    },
};

function getZNodes_comm() {
    $.ajax({
        url: getRootPath() + '/auth/getTree',
        type: 'post',
        async: false,
        data: {},
        success: function (data) {
            zNodes_comm = [];
            // 注意数据获取vz
            handleData_comm(data);
        }
    });
    console.log(zNodes_comm);
    zTreeObj = $.fn.zTree.init($("#organizaTree"), setting_comm, zNodes_comm);
    fillter(zTreeObj);
}



var curLocation_comm = "";//当前位置
function clickTree_comm(event, treeId, treeNode) {
    var treeObj = $.fn.zTree.getZTreeObj("organizaTree");

    var nodes = treeObj.getSelectedNodes();
    if (nodes.length > 0) {
        var allNode = nodes[0]['id'];//获取当前选中节点
        var node = nodes[0].getParentNode();
        getParentNodes_comm(node, allNode);
    }
    var nodeArrs = curLocation_comm.split(">");
    path=treeNode.path;
    clickTreeId=curLocation_comm;
    level=treeNode.level;
    loadGridData1();
};
    function getParentNodes_comm(node, allNode) {
        if (node != null) {
            allNode += ">" + node['id'];
            var curNode = node.getParentNode();
            getParentNodes_comm(curNode, allNode);
        } else {
            //根节点
            curLocation_comm = allNode;
        }
    }
// 处理返回的树数据
function handleData_comm(dataLise) {
    if (null != dataLise && dataLise.length > 0) {
        for (var i = 0; i < dataLise.length; i++) {
            var dataPiece = dataLise[i];
            zNodes_comm.push({
                id: dataPiece.departmentId,
                pId: dataPiece.departmentParentId,
                name: dataPiece.departmentName,
                level: dataPiece.level,
                path: dataPiece.seq
            });
        }
    }
}

    $(function(){
        var proName = getCookie("proj_name");
        if(proName){
            $("#queryBtn").html(proName);
            $("#choseBtn").css('display','');
        }else{
            $("#queryBtn").html("请选择");
        }


        $('#queryBtn').click(function(){
             getZNodes_comm();
            loadGridData1();
            $("#projectModal").modal('show');
        });

        // 键盘松开的时候触发联想功能
        $("#queryName").keyup(function() {
            projectName = $("#queryName").val();
            loadGridData1();
        });

    });

    //情况cookie项目信息
    function clearProCookieInfo(){
        Ewin.confirm({ message: "确认重置当前查询条件吗？" }).on(function (e) {
            if (!e) {
                return;
            } else {
                setCookie('projNo_comm','');
                setCookie('proj_name','');
                $("#queryBtn").html("请选择");
                $("#choseBtn").css('display','none');
            }
        });
    }


