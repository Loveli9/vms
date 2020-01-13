	var proNo = window.parent.projNo;
	var myTable;
	$(function(){
		initTableSave();
	})

	function initTableSave(){
        var columns=initColumn();
        var myParams = {
        	'proNo':proNo
        };
        var url = getRootPath() + '/projectOperation/queryProjectOperation';
        myTable = new BSTable('myTable', url, columns);
        myTable.setClasses('tableCenter');
        myTable.setQueryParams(myParams);
        myTable.setPageSize(20);
        myTable.setPageNumber(1);
        // myTable.setEditable(true);//开启行内编辑
        myTable.setEditable(false);
        myTable.init();
	}
	function initColumn(){
        return [
            {title:'操作时间',field:'time',width:100},
            {title:'操作用户',field:'userName',width:100},
            {title:'操作行为',field:'message',width:200}
        ]
    }