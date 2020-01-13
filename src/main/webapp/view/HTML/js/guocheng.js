(function(){

    var projNo = window.parent.projNo;
    var buName = window.parent.projBU;
    
    //初始化新增问题
    function newProblemAdd(){
    	$.ajax({
			url: getRootPath() + '/dtsTaskController/DtsSeverity',
			type: 'post',
			//async: false,
			data: {
				projNo: projNo,
				isqueryNow : '1' 
			},
			success: function (data) {//后台传来的值是Map<String, Object>
				$("#critNum").empty();
				$("#critNum").append(data.critNum);
				$("#majNum").empty();
				$("#majNum").append(data.majNum);
				$("#minNum").empty();
				$("#minNum").append(data.minNum);
				$("#sugNum").empty();
				$("#sugNum").append(data.sugNum);
				$("#dtsLeaveDINum").empty();
				$("#dtsLeaveDINum").append(data.dtsLeaveDINum);
			}
		});		
	};
    // 初始化代码质量表格
    function initCodeQualityGrid(){
        if(!$("#codeQualityGrid").data('datagrid')){
            $.when(initGrid(projNo, "交付过程质量", "代码质量", "codeQualityGrid", 'quality-sel')).done(function(){
	           	 reloadChart($("#codeQualityGrid").data("gridData"), $("#quality-sel option:selected:first").text(), option1, myChart1);
           });
        }
    };

    // 初始化生产效率表格
    function initIterationQualityGrid() {
        if(!$("#iterationQualityGird").data('datagrid')){
            $.when(initGrid(projNo, "交付过程质量", "迭代质量", "iterationQualityGird", 'interation-sel')).done(function(){
	           	 reloadChart($("#iterationQualityGird").data("gridData"), $("#interation-sel option:selected:first").text(), option1, myChart2);
          });
        }
    };

        // 初始化测试质量表格
    function initTestQualityGrid() {
        if(!$("#testQualityGird").data('datagrid')){
            $.when(initGrid(projNo, "交付过程质量", "测试质量", "testQualityGird", 'test-quality-sel')).done(function(){
	           	 reloadChart($("#testQualityGird").data("gridData"), $("#test-quality-sel option:selected:first").text(), option1, myChart3);
            });
        }
    };

        // 初始化资料质量表格
    function initDocQualityGrid() {
        if(!$("#docQualityGird").data('datagrid')){
            $.when(initGrid(projNo, "交付过程质量", "资料质量", "docQualityGird", 'doc-quality-sel')).done(function(){
	           	 reloadChart($("#docQualityGird").data("gridData"), $("#doc-quality-sel option:selected:first").text(), option1, myChart4);
           });
        }
    };

    function bindSelChangeEvt(){
    	$("#quality-sel").on('change',function(){
    		reloadChart($("#codeQualityGrid").data("gridData"), $("#quality-sel option:selected").text(), option1, myChart1);
    	});
        $("#interation-sel").on('change',function(){
            reloadChart($("#iterationQualityGird").data("gridData"), $("#interation-sel option:selected").text(), option1, myChart2);
        })
        $("#test-quality-sel").on('change',function(){
            reloadChart($("#testQualityGird").data("gridData"), $("#test-quality-sel option:selected").text(), option1, myChart3);
        })
        $("#doc-quality-sel").on('change',function(){
            reloadChart($("#docQualityGird").data("gridData"), $("#doc-quality-sel option:selected").text(), option1, myChart4);
        })
    };
    $(document).ready(function(){
        initCodeQualityGrid();
        initIterationQualityGrid();
        initTestQualityGrid();
        initDocQualityGrid();
        bindSelChangeEvt();
        newProblemAdd();
    })
})()