
var projNo = window.parent.projNo;
var sel="";
var ids="363, 371, 366, 367, 356, 357, 358, 369, 275, 313, 326, 327, 329, 331, 332, 341";

function esc(a,divId1,divId2,s){
    if ( event.keyCode=='27' ){//->右箭头
        this.x(a,divId1,divId2,s);
    }
}
//自动/手动录入
function collect(v){
    var type= "";
    if($("#auto"+v).css("background-color") == "rgb(26, 220, 33)"){
        $("#auto"+v).css("background-color","#fff");
        $("#manual"+v).css("background-color","#f7b547");
        $("#m"+v).show();
        $("#a"+v).hide();
        type= 1;
    }else{
        $("#auto"+v).css("background-color","#1adc21");
        $("#manual"+v).css("background-color","#fff");
        $("#a"+v).show();
        $("#m"+v).hide();
        type= 0;
    }
    saveDatas(v);
}
var symbol = [">","=","≥","<","≤"];
function input(v,s,id,al){
    var res = [];
    var actual = $("#"+v).val();
    $("#"+al+id).val(actual);
    for(var i=0;i<symbol.length;i++ ){
        if(actual.indexOf(symbol[i]) != -1){
            res = actual.split(symbol[i]);
            $("#"+s+id).val(symbol[i]);
            $("#"+al+id).val(res[1]);
        }
    }
    //浮现
    var c = document.getElementById(v);
    c.style.border = "1px solid #B5B5B5;";
    var a = document.getElementById("q"+v);
    a.style.display = "block";
    var obj = document.getElementById("a"+v);
    obj.focus();
    var len = obj.value.length;
    if (document.selection) {
        var sel = obj.createTextRange();
        sel.moveStart('character', len);
        sel.collapse();
        sel.select();
    } else if (typeof obj.selectionStart == 'number'
        && typeof obj.selectionEnd == 'number') {
        obj.selectionStart = obj.selectionEnd = len;
    }
    document.getElementById("all"+v).style.display = "block";//遮盖层
    $("#"+v)[0].style.border="2px solid #81D2FA";
}

function x(a,divId1,divId2,s){
    //隐藏
    var b = document.getElementById(divId1+a);
    b.style.display = "none";
    document.getElementById(divId2+a).style.display = "none";
    //$("#ab"+a).val("");
    $("#"+s+a)[0].style.border="0px";
}

function saveWnValue(measuteId,wnId,hiId,divId1,divId2,selectId){
    var selectedval = $("#"+selectId+measuteId).val();
    $("#"+wnId+measuteId)[0].style.border="0px";
    var c = $("#"+hiId+measuteId).val();
    var b = document.getElementById(divId2+measuteId);
    b.style.display = "none";
    document.getElementById(divId1+measuteId).style.display = "none";
    var reg = /^([>≥=<≤])?\d+(\.\d{0,2})?$/;
    if(!c){
        if(!selectedval){
            saveDatas(measuteId,selectId,"");
            var val = document.getElementById(hiId+measuteId)
            document.getElementById(wnId+measuteId).value=document.getElementById(hiId+measuteId).placeholder;
            $("#"+wnId+measuteId)[0].style.color="red";
        }else{
            toastr.warning('无效输入');
        }

    }
    else if(!reg.test(c)){
        toastr.warning('输入错误');
        $("#"+hiId+measuteId).val("");
    }else{
        document.getElementById(wnId+measuteId).value=selectedval+document.getElementById(hiId+measuteId).value;
        saveDatas(measuteId,selectId,selectedval);
        $("#"+wnId+measuteId)[0].style.color="red";
    }
}


function restore(){
    document.getElementById("b"+b).value = d;
}

function save(){
    //刷新sel，传向后台
    $(document).on("click","#saveLabelMeasure",function(){
        var labs = '';
        var flag = false;
        /*var option = $("#labelSel option:selected");
        var text = option.val();*/
        var text = $("#orgName").val();
        if("无指标流程" != text){
            refreshDatas();
            for(var i = 0;i < sel.data.length;i++){
                if(sel.data[i].isSelect == 1){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                toastr.warning("请选择要配置的指标");
                return ;
            }
        }
        $.ajax({
            url : getRootPath() + '/projectlable/queryMeasureConfigNum',
            type : 'post',
            async: false,
            data : {
                proNo : projNo,
                labs : text
            },
            success : function(data){
                if(data.code == 'fail'){
                    Ewin.confirm({ message: "该操作将会清空旧模板配置，确认操作吗？" }).on(function (e) {
                        if (!e) {
                            return;
                        } else {
                            $.ajax({
                                url: getRootPath() + '/projectlable/deleteMeasureConfig',
                                type: 'post',
                                async: false,
                                dataType: "json",
                                data : {
                                    proNo : projNo,
                                    labs : text
                                },
                                success: function (data) {
                                    if(data.code == 'success'){
                                        saveConfig();
                                    }else{
                                        toastr.error('指标配置异常!');
                                    }
                                }
                            });
                        }
                    });
                }else{
                    saveConfig();
                }
            }
        });

    });
};



function refreshDatas(){
    _.each(sel.data, function(label, index){
        label.isSelect=$('#label-'+label.id).attr("check");
        _.each(label.measureCatList, function(wd, index){
            wd.isSelect=$('#wd-'+wd.measureCategory).attr("check");
            _.each(wd.measureList, function(measure, index){
                measure.isSelect=$('#measure-'+measure.measureId).attr("check");
            })
        })
    });
}

function saveConfig(){
    refreshDatas();
    $.ajax({
        url: getRootPath() + '/projectlable/updateMeasureConfig',
        type: 'post',
        async: false,
        dataType: "json",
        contentType : 'application/json;charset=utf-8', //设置请求头信息
        data: JSON.stringify({
            proNo: projNo,
            projectLabelMeasures: sel.data
        }),
        success: function (data) {
            if(data.code == 'success'){
            	var val = saveMeasureConfig();
            	if(val == 0){
            		toastr.success('指标配置成功！');
            	}else{
            		toastr.error('历史数据保存异常!');
            	}
                $("#labelMeasure").modal('hide');
                location.reload();
                parent.location.reload();
            }else{
                toastr.error('指标配置异常!');
            }
        }
    });
}
function saveMeasureConfig(){
	var val = 0;
	$.ajax({
        url: getRootPath() + '/projectlable/saveMeasureConfig',
        type: 'post',
        async: false,
        dataType: "json",
        contentType : 'application/json;charset=utf-8', //设置请求头信息
        data: JSON.stringify({
        	no: projNo,
        }),
        success: function (data) {
            if(data.code != 'success'){
            	val = 1;
            }
        }
    });
	return val;
}

function yesOrNo(){
    //点击流程全选
    $(document).on("click","a[id^='label']",function(){
        var cur=$(this);
        var check = curChange(cur);
        var labelId = cur.attr("id");
        var wds = $('a[fatherId="'+labelId+'"]');
        _.each(wds, function(wd, index){
            childrenChange($(wd),check);
            var measureId = $(wd).attr("id");
            var measures = $('a[fatherId="'+measureId+'"]');
            _.each(measures, function(measure, index){
                childrenChange($(measure),check);
            });
        });
    })
    //点击分类指标全选，流程颜色判断
    $(document).on("click","a[id^='wd']",function(){
        var cur=$(this);
        var check = curChange(cur);
        var wdId = cur.attr("id");
        var measures = $('a[fatherId="'+wdId+'"]');
        _.each(measures, function(measure, index){
            childrenChange($(measure),check);
        });
        var labelId = cur.attr("fatherId");
        var wds = $('a[fatherId="'+labelId+'"]');
        var num = 0;
        _.each(wds, function(wd, index){
            if($(wd).attr("check")==1){
                num += 1;
            }
        });
        var labels = $('a[id="'+labelId+'"]');
        if(num==0){
            childrenChange($(labels[0]),0);
        }else if (num==wds.length){
            childrenChange($(labels[0]),1);
        }else{
            childrenChange($(labels[0]),2);
        }
    })
    //点击指标分类颜色判断，流程颜色判断
    $(document).on("click","a[id^='measure']",function(){
        var cur=$(this);
        var check = curChange(cur);
        var wdId = cur.attr("id");
        var measures = $('a[fatherId="'+wdId+'"]');

        var wdId = cur.attr("fatherId");
        var measures = $('a[fatherId="'+wdId+'"]');
        var num = 0;
        _.each(measures, function(measure, index){
            if($(measure).attr("check")==1){
                num += 1;
            }
        });
        var measure_wds = $('a[id="'+wdId+'"]');
        if(num==0){
            childrenChange($(measure_wds[0]),0);
        }else if (num==measures.length){
            childrenChange($(measure_wds[0]),1);
        }else{
            childrenChange($(measure_wds[0]),2);
        }

        var labelId = $(measure_wds[0]).attr("fatherId");
        var wds = $('a[fatherId="'+labelId+'"]');
        var num = 0;
        var allnum = 0;
        _.each(wds, function(wd, index){
            var wdId = $(wd).attr("id");
            var measures = $('a[fatherId="'+wdId+'"]');
            allnum+=measures.length;
            _.each(measures, function(measure, index){
                if($(measure).attr("check")==1){
                    num += 1;
                }
            });
        });
        var labels = $('a[id="'+labelId+'"]');
        if(num==0){
            childrenChange($(labels[0]),0);
        }else if (num==allnum){
            childrenChange($(labels[0]),1);
        }else{
            childrenChange($(labels[0]),2);
        }
    })
}

//a[id^='label']
function curChange(cur){
    var url=getRootPath()+ "/view/HTML/images/";
    if(cur.attr("check")==0){
        cur.attr("check",1);
        cur.children().attr("src",url+"yesicon.png");
        return 1;
    }else if(cur.attr("check")==1){
        cur.attr("check",0);
        cur.children().attr("src",url+"noicon.png");
        return 0;
    }
}

//a[id^='label']
function childrenChange(cur,check){
    var url=getRootPath()+ "/view/HTML/images/";
    if(check==0){
        cur.attr("check",0);
        cur.children().attr("src",url+"noicon.png");
    }else if(check==1){
        cur.attr("check",1);
        cur.children().attr("src",url+"yesicon.png");
    }else if (check==2){
        cur.attr("check",1);
        cur.children().attr("src",url+"someicon.png");
    }
}

function pushOrPull(){
    $(document).on("click",".zksq",function(){
        var label=$(this).parent().parent().parent().parent().parent().next();//table/tbody/tr/th
        if($(this).children().html()=="收起"){
            label.css("display","none");
            $(this).children().html("展开");
        }else if($(this).children().html()=="展开"){
            label.css("display","");
            $(this).children().html("收起");
        }
    });
}

/*流程树状目录tree*/
var orgList = [
               { id:11, pId:1, name:"业务线通用"},
               { id:12, pId:1, name:"无指标流程"},
               { id:13, pId:1, name:"2012实验室"},
               { id:131, pId:13, name:"高斯"},
               { id:132, pId:13, name:"欧拉"},
               { id:133, pId:13, name:"高斯维护"},
               { id:134, pId:13, name:"欧拉维护"},
               { id:135, pId:13, name:"硬件结构"},
               { id:136, pId:13, name:"测试装备"},
               { id:14, pId:1, name:"消费者业务线"},
               { id:141, pId:14, name:"消费者业务线"},
               { id:142, pId:14, name:"DevOps"}
         ];
var setting = {
  data: {
      simpleData: {
          enable: true
      }
  },
  view: {
    showIcon: false,
    fontCss: { fontSize: "18px" }
  },
  //回调
  callback: {
      onClick: zTreeOnClick
  },
};
function getAllLabCategory(){
  var select = "无指标流程";
  $.ajax({
    url: getRootPath() + '/projectlable/getAllLabCategory',
    type: 'post',
    async: false,//是否异步，true为异步
    data : {
      proNo : projNo,
    },
    success: function (data) {
      _.each(data.data, function(lab, index){
        if(lab=="selected"){
          $('#orgName').val(index);
          select = index;
          return false;
        }else{
          $('#orgName').val(select);
        }
      });
    }
  });
  return select;
}
//节点点击事件
function zTreeOnClick(event, treeId, treeNode) {
    if(treeNode.id != 13 && treeNode.id != 14){
      $('#orgName').val(treeNode.name);
      $('#orgCode').val(treeNode.id);
      hideTree();
      var text = treeNode.name;
      labelMeasure(text);
    }
};
//下拉框显示 隐藏
function showTree(){
  if($('.ztree').css('display') == 'none'){
       $('.ztree').css('display','block');
   } else{
       $('.ztree').css('display','none');
   }
   $("body").bind("mousedown", onBodyDownByActionType);
}
function hideTree() {
  $('.ztree').css('display','none');
  $("body").unbind("mousedown", onBodyDownByActionType);
  return false;
}
//区域外点击事件
function onBodyDownByActionType(event) {
  if (event.target.id.indexOf('treeDemo') == -1){
      if(event.target.id != 'selectDevType'){
          hideTree();
      }
  }
}

//类目选择事件
function cateGoryChange(){
    var options=$("#labelSel option:selected");
    var text = options.val();
    labelMeasure(text);
}

var selecteds = "<option value=''></option>"+
    "<option value='='> = </option>"+
    "<option value='>'> > </option>"+
    "<option value='≥'> ≥ </option>"+
    "<option value='≤'> ≤ </option>"+
    "<option value='<'> < </option>";


function labelMeasure(text){ 

    $.ajax({
        url: getRootPath() + '/projectlable/getMeasureConfigPageInfo',
        type: 'post',
        async: false,//是否异步，true为异步
        data: {
            proNo: projNo,
            selCategory: text,
            version: "",
            ite: ""
        },
        success: function (data) {

            sel=data;
            var option = [];
            var divLabel="";
            _.each(data.data, function(label, index){
				/*if(label.measureCatList.length==0){
				 return true;//相当于continue
				 }*/
                var url=getRootPath()+ "/view/HTML/images/";
                //判断流程颜色
                var isNot=0;
                var len=0;
                _.each(label.measureCatList, function(is, index){
                    len+=is.measureList.length;
                    _.each(is.measureList, function(os, index){
                        if(os.isSelect==1){
                            isNot+=1;
                        }
                    })
                })
                if(isNot==0 && label.isSelect == 0){
                    url=url+"noicon.png";
                }else if(isNot==len){
                    url=url+"yesicon.png";
                }else {
                    url=url+"someicon.png";
                }

                divLabel=divLabel+
                    "<div style='background-color:#e8f2ff; border:1px #2e8afc solid; margin-top:5px;'>" +
                    "<table style='width: 100%;'>" +
                    "<tr height='30px'>" +
                    "<th width='10%' style='text-align:center;'><a class='yesOrNo' id='label-" +label.id+ "' check='" +label.isSelect+ "'><img src='" +url+ "' /></a></th>" +
                    "<th width='10%' style='text-align:center;font-family: Microsoft Yahei;font-size: 12px'>" + label.labelName + "</th>" +
                    "<th colspan='2' style='text-align:center;'><a class='zksq' style='color:#3B91FE;font-family: Microsoft Yahei;font-size: 12px'><div style='padding-left:420px;'>收起</div></a></th>" +
                    "</tr>" +
                    "</table>" +
                    "</div>" +
                    "<table border='1' style='border-top:0px; width: 100%; border-collapse:collapse;'>" +
                    "<tr height='26px' style='color: #999999;font-family: Microsoft Yahei;font-size: 12px'>" +
                    "<td colspan='2' style='text-align:center;'>指标分类</td>" +
                    "<td colspan='2' style='text-align:center;'>指标名称</td>" +
                    "<td colspan='1' style='text-align:center;'>自动/手动</td>"+
                    "<td colspan='1' style='text-align:center;'>上限值</td>"+
                    "<td colspan='1' style='text-align:center;'>挑战值</td>"+
                    "<td colspan='1' style='text-align:center;'>目标值</td>"+
                    "<td colspan='1' style='text-align:center;'>下限值</td>"+
                    "<td colspan='1' style='text-align:center;'>单位</td>"+
                    "<td colspan='1' style='text-align:center;'>优先级</td>"+
                    "</tr>";
                _.each(label.measureCatList, function(wd, index){

                    url=getRootPath()+ "/view/HTML/images/";
                    //判断分类颜色
                    isNot=0;
                    _.each(wd.measureList, function(is, index){
                        if(is.isSelect==1){
                            isNot+=1;
                        }
                    })
                    if(isNot==0){
                        url=url+"noicon.png";
                    }else if(isNot==wd.measureList.length){
                        url=url+"yesicon.png";
                    }else{
                        url=url+"someicon.png";
                    }

                    var d=wd.labelId;
                    divLabel=divLabel+
                        "<tr>" +
                        "<td width='5%'style='text-align:center;' rowspan='" +wd.measureList.length+ "'><a class='yesOrNo' id='wd-" +wd.measureCategory+wd.labelId+ "' fatherId='label-" +wd.labelId+ "' check='" +wd.isSelect+ "'><img src='" +url+ "' /></a></td>" +
                        "<td width='20%' style='font-family: Microsoft Yahei;font-size:12px;text-align:center;' rowspan='" +wd.measureList.length+ "'>" +wd.measureCategory + "</td>";
                    _.each(wd.measureList, function(measure, index){
                        option.push(measure.measureId);
                        url=getRootPath()+ "/view/HTML/images/";
                        if(measure.isSelect==1){
                            url=url+"yesicon.png";
                        }else if(measure.isSelect==0){
                            url=url+"noicon.png";
                        }
                        if(index==0){
                            divLabel=divLabel+

                                "<td width='5%' style='text-align:center;'><a class='yesOrNo' id='measure-" +measure.measureId+ "' fatherId='wd-" +measure.measureCategory+d+ "' check='" +measure.isSelect+ "'><img src='" +url+ "' /></a></td>" +
                                "<td width='24%' height='28px'style='font-family: Microsoft Yahei;font-size:12px;text-align:center;'>" +measure.measureName + "</td>" ;

                        }else{
                            divLabel=divLabel+

                                "<td width='5%' style='text-align:center;' ><a class='yesOrNo' id='measure-" +measure.measureId+ "' fatherId='wd-" +measure.measureCategory+d+ "' check='" +measure.isSelect+ "'><img src='" +url+ "' /></a></td>" +
                                "<td height='28px' width='24%' style='font-family: Microsoft Yahei;font-size:12px;text-align:center;'>" +measure.measureName + "</td>" ;

                        }
                        var type = "";
                        if("自动采集" == measure.collectType){
                            type = "<div onclick='collect("+measure.measureId+")' style='border: 1px;border-radius: 3px;margin:0 auto;width:60px;height:18px;cursor:pointer;'>" +
                                "<div id='auto"+measure.measureId+"' style='float:left;background-color: #1adc21;border: 1px solid #B5B5B5;border-top-left-radius: 2px;border-bottom-left-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
                                "<span id='a"+measure.measureId+"' style='color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>自动</span></div>" +
                                "<div id='manual"+measure.measureId+"' style='float:left;background-color: #fff;border: 1px solid #B5B5B5;border-top-right-radius: 2px;border-bottom-right-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
                                "<span id='m"+measure.measureId+"' style='display:none;color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>手动</span></div></div>";
                        }else if("手工录入" == measure.collectType && ids.indexOf(measure.measureId) != -1){//33个含过程指标，不能修改
                            type = "<div style='border: 1px;border-radius: 3px;margin:0 auto;width:60px;height:18px;'>" +
                                "<div id='auto"+measure.measureId+"' style='float:left;background-color: #fff;border: 1px solid #B5B5B5;border-top-left-radius: 2px;border-bottom-left-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
                                "<span id='a"+measure.measureId+"' style='display:none;color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>自动</span></div>" +
                                "<div id='manual"+measure.measureId+"' style='float:left;background-color: #B5B5B5;border: 1px solid #B5B5B5;border-top-right-radius: 2px;border-bottom-right-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
                                "<span id='m"+measure.measureId+"' style='color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>手动</span></div></div>";
                        }else if("手工录入" == measure.collectType && ids.indexOf(measure.measureId) == -1){
                            type = "<div onclick='collect("+measure.measureId+")' style='border: 1px;border-radius: 3px;margin:0 auto;width:60px;height:18px;cursor:pointer;'>" +
                                "<div id='auto"+measure.measureId+"' style='float:left;background-color: #fff;border: 1px solid #B5B5B5;border-top-left-radius: 2px;border-bottom-left-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
                                "<span id='a"+measure.measureId+"' style='display:none;color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>自动</span></div>" +
                                "<div id='manual"+measure.measureId+"' style='float:left;background-color: #f7b547;border: 1px solid #B5B5B5;border-top-right-radius: 2px;border-bottom-right-radius: 2px;margin:0 auto;width:30px;height:18px;'>" +
                                "<span id='m"+measure.measureId+"' style='color:#fff;margin:0 auto;font-family: Microsoft Yahei;font-size:12px;'>手动</span></div></div>";
                        }
                        divLabel=divLabel+
                            "<td style='text-align:center;width='8%'>" +type+ "</td>"+

                            "<td width='6%' style='text-align:center;'><input id='b"+measure.measureId+"' style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:55px;text-align:center;' onclick=input(this.id,'sx',"+measure.measureId+",'ab') type='text' value='"+measure.upper+"' >" +
                            "<div id='allb"+measure.measureId+"' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>" +
                            "<div id='qb"+measure.measureId+"' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>" +
                            "<span id='p"+measure.measureId+"' style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#F8F8F8;width:100%;height:35px;line-height:35px;display:block;'>上限值</span>" +
                            "<select id='sx"+measure.measureId+"'  class='inselected'>" +selecteds
                            +"<select><input type='text' id='ab"+measure.measureId+"' onkeydown=esc("+measure.measureId+",'qb','allb','b') style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:100px;float:left;' >" +
                            "<button onclick=saveWnValue("+measure.measureId+",'b','ab','allb','qb','sx') type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>" +
                            "<button onclick=x("+measure.measureId+",'qb','allb','b') style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button></div></td>" +

                            "<td style='text-align:center;' width='6%'> <input id='h"+measure.measureId+"' style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:55px;text-align:center;' onclick=input(this.id,'tz',"+measure.measureId+",'ah') type='text' value='"+measure.challenge+"' >" +
                            "<div id='allh"+measure.measureId+"' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>" +
                            "<div id='qh"+measure.measureId+"' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>" +
                            "<span style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>挑战值</span>" +
                            "<select id='tz"+measure.measureId+"' class='inselected'>" +selecteds
                            +"<select><input type='text' id='ah"+measure.measureId+"' onkeydown=esc("+measure.measureId+",'qh','allh','h') style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:100px;float:left;'   >" +
                            "<button onclick=saveWnValue("+measure.measureId+",'h','ah','allh','qh','tz') type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>" +
                            "<button onclick=x("+measure.measureId+",'qh','allh','h') style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button></div></td>" +

                            "<td style='text-align:center;' width='6%'> <input id='d"+measure.measureId+"' style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:55px;text-align:center;' onclick=input(this.id,'mb',"+measure.measureId+",'ad') type='text' value='"+measure.target+"' >" +
                            "<div id='alld"+measure.measureId+"' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>" +
                            "<div id='qd"+measure.measureId+"' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 2px solid #CACACA;'>" +
                            "<span style='border-top-left-radius: 10px;border-top-right-radius: 10px;border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>目标值</span>" +
                            "<select id='mb"+measure.measureId+"' class='inselected'>" +selecteds
                            +"</select><input type='text' id='ad"+measure.measureId+"' onkeydown=esc("+measure.measureId+",'qd','alld','d') style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:100px;float:left;' >" +
                            "<button onclick=saveWnValue("+measure.measureId+",'d','ad','alld','qd','mb') type='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>" +
                            "<button onclick=x("+measure.measureId+",'qd','alld','d') style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button></div></td>" +

                            "<td style='text-align:center;width='6%'> <input id='f"+measure.measureId+"' style='font-family: Microsoft Yahei;font-size:12px;border:0px;width:55px;text-align:center;' onclick=input(this.id,'xx',"+measure.measureId+",'af') type='text' value='"+measure.lower+"' >" +
                            "<div id='allf"+measure.measureId+"' style=' display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;height: 100%;opacity:0.0;z-index:1001;-moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);'></div>" +
                            "<div id='qf"+measure.measureId+"' style='border-radius:10px;background-color: #fff;width:250px;height:95px; display:none; position:absolute;z-index:9999;border: 1px solid #CACACA;'>" +
                            "<span style='border-top-left-radius: 10px;border-top-right-radius: 10px; border-bottom: 1px solid #B5B5B5;font-size:10px;font-family:sans-serif;text-align:left;text-indent:10px; background-color:#EAEBED;width:100%;height:35px;line-height:35px;display:block;'>下限值</span>" +
                            "<select id='xx"+measure.measureId+"' class='inselected'>" +selecteds
                            +"<select><input type='text' id='af"+measure.measureId+"' onkeydown=esc("+measure.measureId+",'qf','allf','f') style='margin-top:10px;height:30px;outline:none;border:1px solid #81D2FA; margin-left:5px;width:100px;float:left;'>" +
                            "<button onclick=saveWnValue("+measure.measureId+",'f','af','allf','qf','xx') ptye='submit' style='margin-top:12px;' class='btn btn-primary btn-sm editable-submit'><i class='glyphicon glyphicon-ok'></i></button>" +
                            "<button onclick=x("+measure.measureId+",'qf','allf','f') style='margin-top:12px;margin-left:10px;' type='button' class='btn btn-default btn-sm editable-cancel'><i class='glyphicon glyphicon-remove'></i></button></div></td>" +

                            "<td style='text-align:center;font-family: Microsoft Yahei;font-size: 12px;' width='8%' >" +measure.unit + "</td>" +
                            "<td style='text-align:center;font-family: Microsoft Yahei;font-size: 12px;' width='8%' >" +
                            "<input id=hide"+measure.measureId +" style='display:none' value="+measure.computeRule +">" +
                            "<select class='selector' id='yx"+measure.measureId+"'  onchange='changeDix("+measure.measureId+");'>" +
                            "<option value='11' id='d'>目标优先</option>" +
                            "<option value='12' id='b'>上限优先</option>" +
                            "<option value='13' id='f'>下限优先</option>" +
                            "</select>" +
                            "</td>"+
                            "</tr>";
                    });
                });
                divLabel=divLabel+"</table>";
            });
            $("#lable-measure").html(divLabel);
            setOption(option);

        }
    });
}

function changeDix(id){
    var value=document.getElementById("hide"+id).value; //得到select的ID
    var optionId =  $("#yx"+id).find("option:selected").attr("id");
    var text =  $("#yx"+id).find("option:selected").text();
    var toVal = $("#"+optionId+id).val();
    if("" == toVal.trim() || null == toVal){
        toastr.warning(text+'实际参考值为空！');
        $("#yx"+id).val(value);
        return false;
    }
    saveDatas(id);
}
function run(value,rule){
    if(value==rule){
        return true;
    }else{
        return false;
    }
}
var sxValue = "";
var xxValue = "";
var muValue = "";
var changeValue = "";
function saveDatas(id,selectedId,selectedval){
    sxValue = $("#b"+id).val();
    xxValue = $("#f"+id).val();
    muValue = $("#d"+id).val();
    changeValue = $("#h"+id).val();
    if(selectedId == 'sx'){
        sxValue = selectedval+$("#ab"+id).val();
    }else if(selectedId == 'xx'){
        xxValue = selectedval+$("#af"+id).val();
    }else if(selectedId == 'mb'){
        muValue = selectedval+$("#ad"+id).val();
    }else if(selectedId == 'tz'){
        changeValue =  selectedval+$("#ah"+id).val();
    }
    var type= "";
    if($("#auto"+id).css("background-color") == "rgb(26, 220, 33)"){
        type= 0;
    }else{
        type= 1;
    }
    $.ajax({
        type: "post",
        url: getRootPath()+"/projectlable/update",
        dataType: "json",
        async: false,
        contentType : 'application/json;charset=utf-8',
        data: JSON.stringify({
            no:projNo,
            mesureId:id,
            upper:sxValue,
            target:muValue,
            lower:xxValue,
            challenge:changeValue,
            collectType:type,
            computeRule:$("#yx"+id).val()
        }),
        success: function (data) {
            console.log(data);
        }
    });
    toastr.success('保存成功！');
}
$(document).ready(function(e) {
	$.fn.zTree.init($("#treeDemo"), setting, orgList);
    var text = getAllLabCategory();
    labelMeasure(text);
    yesOrNo();
    pushOrPull();
    save();
});

function setOption(option){
  if(option){
      for(var i=0;i<option.length;i++){
          var value=document.getElementById("hide"+option[i]).value; //得到select的ID
          $("#yx"+option[i]).val(value);
      }
  }
}