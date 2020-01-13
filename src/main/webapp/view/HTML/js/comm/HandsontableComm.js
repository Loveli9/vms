var colorBoxBackgroundConfig = {
    allowEmpty:false,
    color: "#ff0000",
    showButtons:false,
    showInput: true,
    containerClassName: "full-spectrum",
    showInitial: true,
    showPalette: true,
    showSelectionPalette: true,
    showAlpha: true,
    maxPaletteSize: 10,
    preferredFormat: "hex",
    localStorageKey: "spectrum.demo",
    move: function (color) {
        updateBordersBackground(color,selectRangeArr,hot);
    },
    show: function () {
    },
    beforeShow: function () {
    },
    hide: function (color) {
        updateBordersBackground(color,selectRangeArr,hot);
    },

    palette: [
        ["rgb(0, 0, 0)", "rgb(67, 67, 67)", "rgb(102, 102, 102)", "rgb(153, 153, 153)","rgb(183, 183, 183)",
            "rgb(204, 204, 204)", "rgb(217, 217, 217)", "rgb(239, 239, 239)", "rgb(243, 243, 243)","rgb(255, 255, 255)"],
        ["rgb(152, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 153, 0)", "rgb(255, 255, 0)", "rgb(0, 255, 0)",
            "rgb(0, 255, 255)", "rgb(74, 134, 232)", "rgb(0, 0, 255)", "rgb(153, 0, 255)", "rgb(255, 0, 255)"],
        ["rgb(230, 184, 175)", "rgb(244, 204, 204)", "rgb(252, 229, 205)", "rgb(255, 242, 204)", "rgb(217, 234, 211)",
            "rgb(208, 224, 227)", "rgb(201, 218, 248)", "rgb(207, 226, 243)", "rgb(217, 210, 233)", "rgb(234, 209, 220)",
            "rgb(221, 126, 107)", "rgb(234, 153, 153)", "rgb(249, 203, 156)", "rgb(255, 229, 153)", "rgb(182, 215, 168)",
            "rgb(162, 196, 201)", "rgb(164, 194, 244)", "rgb(159, 197, 232)", "rgb(180, 167, 214)", "rgb(213, 166, 189)",
            "rgb(204, 65, 37)", "rgb(224, 102, 102)", "rgb(246, 178, 107)", "rgb(255, 217, 102)", "rgb(147, 196, 125)",
            "rgb(118, 165, 175)", "rgb(109, 158, 235)", "rgb(111, 168, 220)", "rgb(142, 124, 195)", "rgb(194, 123, 160)",
            "rgb(166, 28, 0)", "rgb(204, 0, 0)", "rgb(230, 145, 56)", "rgb(241, 194, 50)", "rgb(106, 168, 79)",
            "rgb(69, 129, 142)", "rgb(60, 120, 216)", "rgb(61, 133, 198)", "rgb(103, 78, 167)", "rgb(166, 77, 121)",
            "rgb(133, 32, 12)", "rgb(153, 0, 0)", "rgb(180, 95, 6)", "rgb(191, 144, 0)", "rgb(56, 118, 29)",
            "rgb(19, 79, 92)", "rgb(17, 85, 204)", "rgb(11, 83, 148)", "rgb(53, 28, 117)", "rgb(116, 27, 71)",
            "rgb(91, 15, 0)", "rgb(102, 0, 0)", "rgb(120, 63, 4)", "rgb(127, 96, 0)", "rgb(39, 78, 19)",
            "rgb(12, 52, 61)", "rgb(28, 69, 135)", "rgb(7, 55, 99)", "rgb(32, 18, 77)", "rgb(76, 17, 48)"]
    ]
};
var colorBoxColorConfig = {
    allowEmpty:false,
    color: "#000000",
    showButtons:false,
    showInput: true,
    containerClassName: "full-spectrum",
    replacerClassName:"fontBoxColor",
    showInitial: true,
    showPalette: true,
    showSelectionPalette: true,
    showAlpha: true,
    maxPaletteSize: 10,
    preferredFormat: "hex",
    localStorageKey: "spectrum.demo",
    move: function (color) {
        updateBorderColor(color,selectRangeArr,hot);
    },
    show: function () {
    },
    beforeShow: function () {
    },
    hide: function (color) {
        updateBorderColor(color,selectRangeArr,hot);
    },

    palette: [
        ["rgb(0, 0, 0)", "rgb(67, 67, 67)", "rgb(102, 102, 102)", "rgb(153, 153, 153)","rgb(183, 183, 183)",
            "rgb(204, 204, 204)", "rgb(217, 217, 217)", "rgb(239, 239, 239)", "rgb(243, 243, 243)","rgb(255, 255, 255)"],
        ["rgb(152, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 153, 0)", "rgb(255, 255, 0)", "rgb(0, 255, 0)",
            "rgb(0, 255, 255)", "rgb(74, 134, 232)", "rgb(0, 0, 255)", "rgb(153, 0, 255)", "rgb(255, 0, 255)"],
        ["rgb(230, 184, 175)", "rgb(244, 204, 204)", "rgb(252, 229, 205)", "rgb(255, 242, 204)", "rgb(217, 234, 211)",
            "rgb(208, 224, 227)", "rgb(201, 218, 248)", "rgb(207, 226, 243)", "rgb(217, 210, 233)", "rgb(234, 209, 220)",
            "rgb(221, 126, 107)", "rgb(234, 153, 153)", "rgb(249, 203, 156)", "rgb(255, 229, 153)", "rgb(182, 215, 168)",
            "rgb(162, 196, 201)", "rgb(164, 194, 244)", "rgb(159, 197, 232)", "rgb(180, 167, 214)", "rgb(213, 166, 189)",
            "rgb(204, 65, 37)", "rgb(224, 102, 102)", "rgb(246, 178, 107)", "rgb(255, 217, 102)", "rgb(147, 196, 125)",
            "rgb(118, 165, 175)", "rgb(109, 158, 235)", "rgb(111, 168, 220)", "rgb(142, 124, 195)", "rgb(194, 123, 160)",
            "rgb(166, 28, 0)", "rgb(204, 0, 0)", "rgb(230, 145, 56)", "rgb(241, 194, 50)", "rgb(106, 168, 79)",
            "rgb(69, 129, 142)", "rgb(60, 120, 216)", "rgb(61, 133, 198)", "rgb(103, 78, 167)", "rgb(166, 77, 121)",
            "rgb(133, 32, 12)", "rgb(153, 0, 0)", "rgb(180, 95, 6)", "rgb(191, 144, 0)", "rgb(56, 118, 29)",
            "rgb(19, 79, 92)", "rgb(17, 85, 204)", "rgb(11, 83, 148)", "rgb(53, 28, 117)", "rgb(116, 27, 71)",
            "rgb(91, 15, 0)", "rgb(102, 0, 0)", "rgb(120, 63, 4)", "rgb(127, 96, 0)", "rgb(39, 78, 19)",
            "rgb(12, 52, 61)", "rgb(28, 69, 135)", "rgb(7, 55, 99)", "rgb(32, 18, 77)", "rgb(76, 17, 48)"]
    ]
};
function updateBordersBackground(color,selectRangeArr,hot) {
    var hexColor = "transparent";
    if(color) {
        hexColor = color.toHexString();
    }
    colorChange(selectRangeArr,0,hexColor,hot);
}
function updateBorderColor(color,selectRangeArr,hot) {
    var hexColor = "transparent";
    if(color) {
        hexColor = color.toHexString();
    }
    colorChange(selectRangeArr,1,hexColor,hot);
}

function colorChange(selectRangeArr,_index,hex,hot) {
    // 定义改变样式方法
    var changeCellStyle = function(){
        if( _index === 0 ){
        	if(hex==='#ffffff'){
                $(rangeCell).css("background","");
                hot.setCellMeta(selectRangeArr[i].row, selectRangeArr[i].col,"bkColor",undefined);
			}else{
                $(rangeCell).css({"background":hex});
                hot.setCellMeta(selectRangeArr[i].row, selectRangeArr[i].col,"bkColor",hex);
			}
        }
        if( _index === 1 ){
            if(hex==='#000000'){
                $(rangeCell).css("color","");
                hot.setCellMeta(selectRangeArr[i].row, selectRangeArr[i].col,"ftColor",undefined);
            }else{
				$(rangeCell).css({"color":hex});
				hot.setCellMeta(selectRangeArr[i].row, selectRangeArr[i].col,"ftColor",hex);
            }
        }
        // if( _index == 2 ){
        //     $(rangeCell).css({"border":"solid 1px "+hex});
        //     hot.setCellMeta(selectRangeArr[i].row, selectRangeArr[i].col,"bdColor",hex);
        // }
    };
    for( var i=0;i<selectRangeArr.length;i++ ){
        var rangeCell = hot.getCell(selectRangeArr[i].row, selectRangeArr[i].col);
        // var checkMergeCell = $(rangeCell).attr("rowspan");
        // if( checkMergeCell != undefined ){
        //     if( toggleSwitch ){
        //         changeCellStyle();
        //         toggleSwitch = false;
        //     }else{
        //         continue;
        //     }
        // }else{
        changeCellStyle();
        // }
    }
}

function afterOnCellMouseUpComm(event, cellCoords,hot) {
    var Crow = cellCoords.row;
    var Ccol = cellCoords.col;
   	var selectRangeArr = []; // 所选区域所有单元格数组
    var Ccell = hot.getCell(Crow, Ccol);
    var selectRange = hot.getSelected(); // 获取所选区域范围
    if(!selectRange){
        return selectRangeArr;
    }
    // var txt = hot.getDataAtCell(selectRange[0][0],selectRange[0][1]); // 获取所选区域第一个单元格值
    // 单击任意单元格取消编辑状态
    // $(".handsontableInputHolder").css({
    //     "display":"none"
    // });
    var rangeRowArr = []; // 所选区域行数组
    var rangeColArr = []; // 所选区域列数组
    for( var i=selectRange[0][0];i<selectRange[0][2]+1;i++ ){
        rangeRowArr.push(i);
    }
    for( var i=selectRange[0][1];i<selectRange[0][3]+1;i++ ){
        rangeColArr.push(i);
    }
    for( var i=0;i<rangeRowArr.length;i++ ){
        for( var n=0;n<rangeColArr.length;n++ ){
            var selectRangeCell = { row:rangeRowArr[i],col:rangeColArr[n] };
            selectRangeArr.push(selectRangeCell);
        }
    }
    // 添加表格失去焦点时的当前单元格类
    $("td").removeClass("currentTd");
    for( var i=0;i<selectRangeArr.length;i++ ){
        var rangeCell = hot.getCell(selectRangeArr[i].row, selectRangeArr[i].col);
        $(rangeCell).addClass("currentTd");
    }

    return selectRangeArr;
}

function negativeValueRendererComm(instance, td, row, col, prop, value, cellProperties) {
    if(instance.getCellMeta(row, col).bkColor){
        var rangeCell = instance.getCell(row, col);
        $(rangeCell).css({"background":instance.getCellMeta(row, col).bkColor});
    };
    if(instance.getCellMeta(row, col).ftColor){
        var rangeCell = instance.getCell(row, col);
        $(rangeCell).css({"color":instance.getCellMeta(row, col).ftColor});
    };
}