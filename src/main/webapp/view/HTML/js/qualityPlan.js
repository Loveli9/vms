var projNo="";
$(function(){
	projNo = window.parent.projNo;

    var value = [
        ["","","",""],
        ["","","",""],
        ["","","",""],
    ];
    var colWidths = [150,150,500,150];
    var mergeCells=true;
    var cellsMeta = [];
	var hot;
	// 列出全局变量
    var selectRangeArr = [];

    function changeFrameHeight() {
        var ifm =  window.parent.document.getElementById("myiframe");
        ifm.height = document.documentElement.offsetHeight+300;
    };
    
	function init(moduleName) {
		$(".htMenu").remove();
		Handsontable.renderers.registerRenderer('negativeValueRenderer', negativeValueRenderer);
        hot = new Handsontable(document.getElementById(moduleName), {
            data: value,
            language: 'zh-CN',
            colHeaders: true,
            // colHeaders: ['项目阶段', '关键活动', '活动要求', '输出件'], // 使用自定义列头
            rowHeaders: true,
            colWidths: colWidths, // 设置所有列宽
            filters: true,
            columnSorting: false,
            sortIndicator: false,
            autoColumnSize: true,
            manualColumnResize: true,
            undo: true,
            redo: true,
            wordWrap: true,
            copyable: true,
            //mergeCells: true,
            mergeCells: mergeCells,
            manualRowResize: true,
            manualRowMove: true,
            manualColumnMove: false,
            renderAllRows: true,
            allowInsertRow: true,
            allowInsertColumn: true,
            // allowInsertColumn: false,//禁止动态添加列
            //fixedColumnsLeft: 1,
            contextMenu: true,
            // contextMenu: ['row_above', 'row_below', '---------', 'remove_row', '---------', 'cut', 'copy','undo', 'redo', '---------', 'make_read_only', '---------', 'alignment','mergeCells'],
            //dropdownMenu: true,
            dropdownMenu: ['filter_by_condition', 'filter_by_value', 'filter_action_bar'],
            cells: function (row, col, prop) {//调用方法遍历数据，添加样式
                var cellProperties = {};
                cellProperties.renderer = "negativeValueRenderer";
                for (var i=0;i<cellsMeta.length;i++) {
                    if(cellsMeta[i].row===row&&cellsMeta[i].col===col){
                        if(cellsMeta[i].bkColor){
                            cellProperties.bkColor=cellsMeta[i].bkColor;
                        }
                        if(cellsMeta[i].ftColor){
                            cellProperties.ftColor=cellsMeta[i].ftColor;
                        }
                        if(cellsMeta[i].className){
                            cellProperties.className=cellsMeta[i].className;
                        }
                        cellsMeta.splice(i,1);
                        break;
                    }
                }
                return cellProperties;
            },
        });

        function negativeValueRenderer(instance, td, row, col, prop, value, cellProperties) {
            Handsontable.renderers.TextRenderer.apply(this, arguments);
            negativeValueRendererComm(instance, td, row, col, prop, value, cellProperties);
        }
        
      //添加筛选成功后的触发事件
        hot.addHook('afterFilter', function () {
            var plugin = hot.getPlugin('trimRows').trimmedRows;//获取被筛选掉的行号
            if (plugin.length <= 0) {
                return;
            }
            console.log(plugin);

            var DataArray = new Array();

            for (var i = 0; i < plugin.length; i++) {
                //通过行号获取数据
                DataArray.push(hot.getSourceDataAtRow(plugin[i]));
            }

            console.log(DataArray);
        });

        // 获取所选区域单元格数组 当前高亮
        hot.addHook('afterOnCellMouseUp', function (event, cellCoords) {
            selectRangeArr = afterOnCellMouseUpComm(event, cellCoords,hot);
        });

    }
	
	var tid="codeManage";
	
	function savaQualityPlan(){
		$("#savaQualityPlan").click(function(){
	        var col = $(hot.table).find('colgroup').find('col');
	        var colWidths = [];
	        for (var i=1;i<col.length;i++) {
	            colWidths.push($(col[i]).css('width'));
	        }
	        var cellsMetaOld=hot.getCellsMeta();
            var cellsMeta = [];
            for (var i=0;i<cellsMetaOld.length;i++){
                if(cellsMetaOld[i].className||cellsMetaOld[i].bkColor||cellsMetaOld[i].ftColor){
                    cellsMeta.push({
                        row:cellsMetaOld[i].row,
                        col:cellsMetaOld[i].col,
                        className:cellsMetaOld[i].className,
                        bkColor:cellsMetaOld[i].bkColor,
                        ftColor:cellsMetaOld[i].ftColor,
                    });
                }
            }
	        var ret ={
	            no:projNo,
	            module:tid,
	            data:hot.getData(),
	            mergeCells:hot.getPlugin('MergeCells').mergedCellsCollection.mergedCells,
	            colWidths:colWidths,
                cellsMeta:cellsMeta
	        }
	        $.ajax({
	            url: getRootPath() + '/handsontableController/saveHandsontable',
	            type: 'post',
	            dataType: "json",
	            contentType : 'application/json;charset=utf-8', //设置请求头信息
	            data: JSON.stringify(ret),
	            success: function (data) {
	                //后台返回添加成功
	                if (data.code == '0') {
	                    toastr.success('保存成功！');
	                }
	                else {
	                    toastr.error('保存失败!');
	                }
	            }
	        });
		})
	}
	
    function queryHandsontable(moduleName) {
        var ret ={
            no:projNo,
            module:moduleName,
        }
        $.ajax({
            url: getRootPath() + '/handsontableController/queryHandsontable',
            type: 'post',
            async: false,
            dataType: "json",
            contentType : 'application/json;charset=utf-8', //设置请求头信息
            data: JSON.stringify(ret),
            success: function (data) {
                //后台返回添加成功
                if (data.code == '0') {
                	if(data.data.data){
                        value = data.data.data;
                    }else{
                    	value = [
        							["","",""],
        						];
                    }
                    if(data.data.mergeCells){
                        mergeCells = data.data.mergeCells;
                    }else{
                    	mergeCells = true;
                    }
                    if(data.data.colWidths){
                        colWidths = data.data.colWidths;
                    }else{
                    	colWidths = [200,500,100];
                    }
                    if(data.data.cellsMeta){
                        cellsMeta = data.data.cellsMeta;
                    }else{
                    	cellsMeta = [];
                    }
                }
                else {
                    toastr.error('查询数据失败!');
                }
            }
        });
    }
    
    var categories=new Array();
    
    function categorys() {
    	$.ajax({
    		url: getRootPath() + '/projectManagement/categorys',
    		type: 'post',
    		async: false,
    		success: function (data) {
    			categories=data;
    		}
    	});
    }
    
    function resultQuality() {
    	$.ajax({
    		url: getRootPath() + '/projectManagement/resultQuality',
    		type: 'post',
    		async: false,
    		data:{
    			proNo: projNo,
			},
    		success: function (data) {
    			var tab="";
    			var lab=data.data.lab;
    			var categor=data.data.categor;
    			var measures=data.data.measures;
    			for(var i=0;i<lab.length;i++){
    				var lname=lab[i];
    				tab+="<tr><th colspan=8>" + lname + "</th></tr>";
    				var cate=categor[lname];
    				for(var m=0;m<cate.length;m++){
    					var flag=0;
    					for(var k=0;k<measures.length;k++){
    						var mea=measures[k];
    						if(cate[m].category==mea.measureCategory&&lname==mea.labelTitle){
    							if(flag==0){
        							tab+="<tr>" +
    		    							"<td rowspan=" + cate[m].num + ">" + mea.measureCategory + "</td>" +
    		    							"<td>" + mea.measureName + "</td>" +
    		    							"<td>" + mea.unit + "</td>" +
    		    							"<td>" + mea.upper + "</td>" +
    		    							"<td>" + mea.lower + "</td>" +
    		    							"<td>" + mea.challenge + "</td>" +
    		    							"<td>" + mea.target + "</td>" +
    		    							"<td>" + mea.collectType + "</td>" +
    		    						 "</tr>";
        							flag++;
        						}else{
        							tab+="<tr>" +
    			    						"<td>" + mea.measureName + "</td>" +
    			    						"<td>" + mea.unit + "</td>" +
    			    						"<td>" + mea.upper + "</td>" +
    			    						"<td>" + mea.lower + "</td>" +
    			    						"<td>" + mea.challenge + "</td>" +
    			    						"<td>" + mea.target + "</td>" +
    			    						"<td>" + mea.collectType + "</td>" +
    			    					 "</tr>";
        						}
    						}
    					}
    				}
    			}
    			$("#tbody").append(tab);
    		}
    	});
    }
    
    function saveModule(){
		$("#saveModule").click(function(){
	        var ret ={
	            no:projNo,
	            module:$("#moduleName").val(),
	        }
	        $.ajax({
	            url: getRootPath() + '/projectManagement/saveModule',
	            type: 'post',
	            dataType: "json",
	            contentType : 'application/json;charset=utf-8', //设置请求头信息
	            data: JSON.stringify(ret),
	            success: function (data) {
	                //后台返回添加成功
	                if (data.code == '200') {
	                    toastr.success('保存成功！');
	                    $("ul.navbar-nav").append("<li tabname=" + data.message + "><a href='###'>" + $("#moduleName").val() + "</a></li>");
	                    $("ul.navbar-nav").children("li:last-child").children("a:first-child").click();
	                }else if(data.code == '404') {
	                    toastr.error('模块名重复!');
	                }else{
	                	toastr.error('保存失败!');
	                }
	            }
	        });
		})
	}
    
    function queryModules() {
    	$.ajax({
    		url: getRootPath() + '/projectManagement/queryModules',
    		type: 'post',
    		async: false,
    		data:{
    			no: projNo,
			},
    		success: function (data) {
    			var lis="";
    			for(var i=0;i<data.data.length;i++){
    				var li=data.data[i];
    				lis+="<li tabname=" + li.moduleName + " title=" + li.module + "><a href='###'>" + li.module + "</a></li>";
    			}
    			$("ul.navbar-nav").append(lis);
			 	$('ul.navbar-nav li a').each(function() {
			        var words = $(this).text().length;
			        if(words > 4){
			            $(this).text($(this).text().slice(0,4)+"...");
			        }
    			});
    		}
    	});
    }
    
    function removeModule(){
    	$("#deleQualityPlan").click(function(){
    		if($("#deleQualityPlan").text()=="取消"){
    			$("ul.navbar-nav li a").children("span").remove();
    			$("#deleQualityPlan").text("删除");
    		}else if($("#deleQualityPlan").text()=="删除"){
    			var span="<span class='glyphicon glyphicon-remove' data-toggle='modal' data-target='#deletemodule' style='font-size: 10px;color: #e41f2b;margin-left: 3px;position: relative;top: -8px;'></span>";
    			$("ul.navbar-nav li a:not('ul.navbar-nav li a:first')").append(span);
    			$("#deleQualityPlan").text("取消");
    		}
    	})
    }
    
    var deleteid="";
    
    function sureDeletemodule(){
    	$("#sureDeletemodule").click(function(){
    		var ret ={
    				no: projNo,
        			moduleName: deleteid
    	        }
    		$.ajax({
        		url: getRootPath() + '/projectManagement/sureDeletemodule',
        		type: 'post',
	            dataType: "json",
	            contentType : 'application/json;charset=utf-8', //设置请求头信息
	            data: JSON.stringify(ret),
        		success: function (data) {
        			//后台返回添加成功
	                if (data.code == '200') {
	                	toastr.success('删除成功！');
                        location.reload();
                    }else{
	                	toastr.error('删除失败!');
	                }
        		}
        	});
    	})
    }

	$(document).ready(function(){
        colorBoxBackgroundConfig.move=function (color) {
            updateBordersBackground(color,selectRangeArr,hot);
        };
        colorBoxBackgroundConfig.hide=function (color) {
            updateBordersBackground(color,selectRangeArr,hot);
        };
        colorBoxColorConfig.move=function (color) {
            updateBorderColor(color,selectRangeArr,hot);
        };
        colorBoxColorConfig.hide=function (color) {
            updateBorderColor(color,selectRangeArr,hot);
        };
        $("#color-box-background").spectrum(colorBoxBackgroundConfig);
        $("#color-box-color").spectrum(colorBoxColorConfig);
		resultQuality();
		queryModules();
		queryHandsontable("codeManage");
        init("codeManage");
        savaQualityPlan();
        saveModule();
        removeModule();
        sureDeletemodule();
        changeFrameHeight();
	})
	
	$(document).on("click", "ul.navbar-nav li a span", function () {
		var id = $(this).parent().parent().attr("tabname");
		deleteid=id;
	});
	
	$(document).on("click", "ul.navbar-nav li", function () {
    	$("ul.navbar-nav li").removeClass("active");
    	$(this).addClass("active");
    	var id = $(this).attr("tabname");
    	$("#"+tid).empty();
    	$("#"+tid).attr("id",id);
    	tid=id;
    	queryHandsontable(id);
        init(id);
        changeFrameHeight();
    });
	
})