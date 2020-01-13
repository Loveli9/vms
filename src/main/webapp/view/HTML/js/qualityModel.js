$(function(){
	$('#tableXiaoLvXM').bootstrapTable({
		method: 'post',
    	contentType: "application/x-www-form-urlencoded; charset=UTF-8",
     	url : getRootPath() + '/qualityModel/queryNO',
        editable:false,// 可行内编辑
    	striped: true, // 是否显示行间隔色
    	responseHandler: function (res) {
            return {
                "rows": res.data==null?[]:res.data,
                "total": res.totalCount
            }
        },
    	pageNumber: 1, // 初始化加载第一页，默认第一页
    	pagination:true,// 是否分页
    	queryParamsType:'limit',
    	sidePagination:'server',
    	pageSize:10,// 单页记录数
    	pageList:[5,10,30,50],// 分页步进值
    	showColumns:false,
    	toolbar:'#toolbar',// 指定工作栏
    	toolbarAlign:'right',
        dataType: "json",
        queryParams : function(params){
        	var param = {
	    	        'pageSize': params.limit,
					'pageNumber': params.offset,
                    'clientType':$("#selectBig").selectpicker("val")=="2"?"0":"1",
	    	        'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串*/
    				'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
    				'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
    				'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串 
    				'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
    				'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
    				'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
    				'projectState': $("#usertype2").selectpicker("val")==null?null:$("#usertype2").selectpicker("val").join(),
					}
				return param;
		},
		onLoadSuccess: function (data) {
            //数据加载成功后 进行合并
			if(data.rows!=null){
				mergeTable(data,"tableXiaoLvXM");
			}else{
				return;
			}
        },
		columns:[
					[
						{title : '序号',field : 'id',align: "center",width: 150,valign:'middle'},
						{title : 'PDU/SPDT',field : 'pduSpdt',align: "center",width: 80,valign:'middle'},
						{title : '外包项目名称',field : 'name',align: "center",width: 45,valign:'middle',	        
			        		formatter:function(value,row,index){	
			        			var path1 = getRootPath() + '/view/HTML/page.html?url='+
			        						getRootPath()+'/bu/projView?projNo=' + row.no;
		            			return '<a target="_blank" style="color: #721b77;" title="'+
		            					row.name +'" href="'+path1+'">'+row.name+'</a>';
			        		}
						},
						{title : '地域',field : 'area',align: "center",width: 90,valign:'middle'},
						{title : '合作方经理',field : 'pm',align: "center",width: 85,valign:'middle'},		            						
						{title: '月份/度量项',field: 'measure',align: "center",width: 100,valign:'middle'},
						
						{title : '版本效率',field : 'measureValue',align: "center",width: 80,valign:'middle'},
						{title : '1月',field : '01',align: "center",width: 45,valign:'middle'},
						{title : '2月',field : '02',align: "center",width: 90,valign:'middle'},
						{title : '3月',field : '03',align: "center",width: 85,valign:'middle'},
						{title : '4月',field : '04',align: "center",width: 100,valign:'middle'},						
						{title : '5月',field : '05',align: "center",width: 150,valign:'middle'},
						{title : '6月',field : '06',align: "center",width: 80,valign:'middle'},
						{title : '7月',field : '07',align: "center",width: 45,valign:'middle'},
						{title : '8月',field : '08',align: "center",width: 90,valign:'middle'},
						{title : '9月',field : '09',align: "center",width: 85,valign:'middle'},
						{title : '10月',field : '10',align: "center",width: 100,valign:'middle'},
						{title : '11月',field : '11',align: "center",width: 85,valign:'middle'},
						{title : '12月',field : '12',align: "center",width: 100,valign:'middle'}
		            ]
		        ],
				locale:'zh-CN'//中文支持	
    });	
});
var idCount="";
var pduSpdtCount="";
var nameCount="";
var areaCount="";
var pmCount="";
var parameterNameCount="";

//合并表格
function mergeTable(data,tableId){
    //每次合并表格前 都要将全局变量清空
	idCount="";
	pduSpdtCount="";
	nameCount="";
	areaCount="";
	pmCount="";
	parameterNameCount="";

    //计算每一列相同内容出现的次数
    mergeCells(data.rows,0,data.rows.length,"id",$('#'+tableId));

    //对字符串进行分割
    var strArr = idCount.split(",");
    var strArr1 = pduSpdtCount.split(",");
    var strArr2 = nameCount.split(",");
    var strArr3 = areaCount.split(",");
    var strArr4 = pmCount.split(",");
    var strArr5 = parameterNameCount.split(",");
    //进行合并
    var index = 0;
    for(var i=0;i<strArr.length;i++){
        var count = strArr[i] * 1;
        $('#tableXiaoLvXM').bootstrapTable('mergeCells',{index:index, field:"id", colspan: 1, rowspan: count});
        index += count;
    }
    
    var index = 0;
    for(var i=0;i<strArr1.length;i++){
        var count = strArr1[i] * 1;
        $('#tableXiaoLvXM').bootstrapTable('mergeCells',{index:index, field:"pduSpdt", colspan: 1, rowspan: count});
        index += count;
    }

    var index = 0;
    for(var i=0;i<strArr2.length;i++){
        var count = strArr2[i] * 1;
        $('#tableXiaoLvXM').bootstrapTable('mergeCells',{index:index, field:"name", colspan: 1, rowspan: count});
        index += count;
    }

    var index = 0;
    for(var i=0;i<strArr3.length;i++){
        var count = strArr3[i] * 1;
        $('#tableXiaoLvXM').bootstrapTable('mergeCells',{index:index, field:"area", colspan: 1, rowspan: count});
        index += count;
    }
    
    var index = 0;
    for(var i=0;i<strArr4.length;i++){
        var count = strArr4[i] * 1;
        $('#tableXiaoLvXM').bootstrapTable('mergeCells',{index:index, field:"pm", colspan: 1, rowspan: count});
        index += count;
    }
    
    var index = 0;
    for(var i=0;i<strArr5.length;i++){
        var count = strArr5[i] * 1;
        $('#tableXiaoLvXM').bootstrapTable('mergeCells',{index:index, field:"parameterName", colspan: 1, rowspan: count});
        index += count;
    }
}

function mergeCells(datas,startIndex,size,fieldName,target) {
    //声明一个数组计算相同属性值在data对象出现的次数和
    //这里不能使用map，因为如果涉及到排序后，相同的属性并不是紧挨在一起,那么后面的次数会覆盖前面的次数，故这里用数组
    var sortArr = new Array();
    for (var i = startIndex; i < size - 1; i++) {
        for (var j = i + 1; j < size; j++) {
            if (datas[i][fieldName] != datas[j][fieldName]){
                //相同属性值不同
                if (j - i > 1) {
                    sortArr.push(j - i);
                    i = j - 1;
                    //如果是最后一个元素 把最后一个元素的次数也装进去
                    if(i == size-1-1){
                        sortArr.push(1);
                    }
                }else{
                    sortArr.push(j - i);
                    //如果j是最后一个元素 把最后一个元素的次数装进去
                    if(j == size - 1){
                        sortArr.push(1);
                    }
                }
                break;
            }else {
                //相同属性值相同 直到最后一次的时候才会装 否则在他们的值不同时再装进去
                if (j == size - 1) {
                    sortArr.push(j - i+1);
                    i = j;
                }
            }
        }
    }

    var index = 0;
    for(var prop in sortArr){
   //列名为id的记录下来
        if(fieldName == "id"){
            var count = sortArr[prop] * 1;
            idCount += count +",";
        }
        //列名为pduSpdt的记录下来
        if(fieldName == "pduSpdt"){
            var count = sortArr[prop] * 1;
            pduSpdtCount += count +",";
        }
        //列名为name的记录下来
        if(fieldName == "name"){
            var count = sortArr[prop] * 1;
            nameCount += count +",";
        }
        //列名为area的记录下来
        if(fieldName == "area"){
            var count = sortArr[prop] * 1;
            areaCount += count +",";
        }
        //列名为pm的记录下来
        if(fieldName == "pm"){
            var count = sortArr[prop] * 1;
            pmCount += count +",";
        }
        //列名为parameterName的记录下来
        if(fieldName == "parameterName"){
            var count = sortArr[prop] * 1;
            parameterNameCount += count +",";
        }
    }

    //循环遍历第一列的每个次数，然后基于第一列出现的次数去计算记录下第二列相同出现的次数
    for(var prop in sortArr){
    	if(fieldName == "id"){
            if(sortArr[prop] >1){
                mergeCells(datas,startIndex,startIndex+sortArr[prop],"pduSpdt",target)
                startIndex = startIndex+sortArr[prop];
            }else{
            	pduSpdtCount +=1+",";
            }
        }
        if(fieldName == "pduSpdt"){
            if(sortArr[prop] >1){
                mergeCells(datas,startIndex,startIndex+sortArr[prop],"name",target)
                startIndex = startIndex+sortArr[prop];
            }else{
            	nameCount +=1+",";
            }
        }
        if(fieldName == "name"){
            if(sortArr[prop] >1){
            	mergeCells(datas,startIndex,startIndex+sortArr[prop],"area",target)
                startIndex = startIndex+sortArr[prop];
            }else{
            	areaCount +=1+",";
            }
        }
        if(fieldName == "area"){
            if(sortArr[prop] >1){
                mergeCells(datas,startIndex,startIndex+sortArr[prop],"pm",target)
                startIndex = startIndex+sortArr[prop];
            }else{
            	pmCount +=1+",";
            }
        }
    }
}

function selectOnchange(){
	$('#tableXiaoLvXM').bootstrapTable('refresh');
}