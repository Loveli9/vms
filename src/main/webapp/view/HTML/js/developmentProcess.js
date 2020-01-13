var projNo="";
$(function(){
	projNo = window.parent.projNo;
	
	var value = [
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

	function init() {
		Handsontable.renderers.registerRenderer('negativeValueRenderer', negativeValueRenderer);
        hot = new Handsontable(document.getElementById('developmentProcess'), {
            data: value,
            language: 'zh-CN',
            //colHeaders: true,
            colHeaders: ['项目阶段', '关键活动', '活动要求', '输出件'], // 使用自定义列头
            rowHeaders: true,
            colWidths: colWidths, // 设置所有列宽
            filters: true,
            columnSorting: true,
            sortIndicator: true,
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
            //allowInsertColumn: true,
            allowInsertColumn: false,//禁止动态添加列
            //fixedColumnsLeft: 1,
            // contextMenu: true,
            contextMenu: ['row_above', 'row_below', '---------', 'remove_row', '---------', 'cut', 'copy','undo', 'redo', '---------', 'make_read_only', '---------', 'alignment','mergeCells'],
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
	
	function savaDevelopmentProcess(){
		$("#savaDevelopmentProcess").click(function(){
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
	            module:'developmentProcess',
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
	
    function queryHandsontable() {
        var ret ={
            no:projNo,
            module:'developmentProcess',
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
                    }
                    if(data.data.mergeCells){
                        mergeCells = data.data.mergeCells;
                    }
                    if(data.data.colWidths){
                        colWidths = data.data.colWidths;
                    }
                    if(data.data.cellsMeta){
                        cellsMeta = data.data.cellsMeta;
                    }
                }
                else {
                    toastr.error('查询数据失败!');
                }
            }
        });
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
		queryHandsontable();
        init();
        savaDevelopmentProcess();
        changeFrameHeight();
	})
	
})