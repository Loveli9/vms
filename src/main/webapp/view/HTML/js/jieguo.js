(function(projNo, buName){

	var gridData = {};
	
    //初始化交付结果质量表格
    function initResultQualityGrid(){
        if(!$("#resultQualityGrid").data('datagrid')){
               initGrid(projNo, "交付结果", "交付结果质量", "resultQualityGrid"); 
        }
    };

    //初始化生产效率表格
    function initProductyGrid() {
        if(!$("#productyGrid").data('datagrid')){
            $.when(initGrid(projNo, "交付结果", "生产效率", "productyGrid", 'indicator-sel')).done(function(){
            	 gridData = $("#productyGrid").data("gridData");
            	 reloadChart(gridData, $("#indicator-sel option:selected:first").text(), option1, myChart1);
            });
           
        }
    };
    function bindSelChangeEvt(){
    	$("#indicator-sel").on('change',function(){
    		reloadChart(gridData, $("#indicator-sel option:selected").text(), option1, myChart1);
    	})
    };
    $(document).ready(function(){
        initResultQualityGrid();
        initProductyGrid();
        bindSelChangeEvt();
    })
})(window.parent.projNo, window.parent.projBU)