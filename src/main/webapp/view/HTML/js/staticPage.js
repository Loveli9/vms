var theTable;    
var totalPage;    
var pageNum;    
   
var spanPre;    
var spanNext;    
var spanFirst;    
var spanLast;    
   
var numberRowsInTable;    
var pageSize = 4;    
var page = 1; 

	function tablelswInfo(pageSizeNew,theTableId){    
		theTable = document.getElementById(theTableId);    
		totalPage = document.getElementById("spanTotalPage");    
		pageNum = document.getElementById("spanPageNum");    
		   
		spanPre = document.getElementById("spanPre");    
		spanNext = document.getElementById("spanNext");    
		spanFirst = document.getElementById("spanFirst");    
		spanLast = document.getElementById("spanLast");    
		   
		numberRowsInTable = theTable.rows.length;
		pageSize = pageSizeNew;    
		page = 1; 
		
		hide();
	}
	   
	//下一页    
	function next(){    
	   
	    hideTable();    
	        
	    currentRow = pageSize * page;    
	    maxRow = currentRow + pageSize;    
	    if ( maxRow > numberRowsInTable ) maxRow = numberRowsInTable;    
	    for ( var i = currentRow; i< maxRow; i++ ){    
	        theTable.rows[i].style.display = '';    
	    }    
	    page++;    
	        
	    if ( maxRow == numberRowsInTable ) { nextText(); lastText(); }  
	    showPage();    
	    preLink();    
	    firstLink();    
	}    
	   
	//上一页    
	function pre(){    
	   
	    hideTable();    
	        
	    page--;    
	        
	    currentRow = pageSize * page;    
	    maxRow = currentRow - pageSize;    
	    if ( currentRow > numberRowsInTable ) currentRow = numberRowsInTable;    
	    for ( var i = maxRow; i< currentRow; i++ ){    
	        theTable.rows[i].style.display = '';    
	    }    
	        
	        
	    if ( maxRow == 0 ){ preText(); firstText(); }    
	    showPage();    
	    nextLink();    
	    lastLink();    
	}    
	   
	//第一页    
	function first(){    
	    hideTable();    
	    page = 1;    
	    for ( var i = 0; i<pageSize; i++ ){    
	        theTable.rows[i].style.display = '';    
	    }    
	    showPage();    
	        
	    preText();    
	    nextLink();    
	    lastLink();
	    firstText();
	}    
	   
	//最后一页    
	function last(){    
	    hideTable();    
	    page = pageCount();    
	    currentRow = pageSize * (page - 1);    
	    for ( var i = currentRow; i<numberRowsInTable; i++ ){    
	        theTable.rows[i].style.display = '';    
	    }    
	    showPage();    
	        
	    preLink();    
	    nextText();    
	    firstLink(); 
	    lastText();
	}    
	   
	function hideTable(){    
	    for ( var i = 0; i<numberRowsInTable; i++ ){    
	        theTable.rows[i].style.display = 'none';    
	    }    
	}    
	   
	function showPage(){     
	 pageNum.innerHTML = page;
	}    
	   
	//总共页数    
	function pageCount(){    
	    var count = 0;    
	    if ( numberRowsInTable%pageSize != 0 ) count = 1;     
	    return parseInt(numberRowsInTable/pageSize) + count;    
	}    
	   
	//显示链接    
	function preLink(){ spanPre.innerHTML = "<a href='javascript:pre();' style='color: #428bca;text-decoration: none;'>上一页</a>";}    
	function preText(){ spanPre.innerHTML = "上一页";}    
	   
	function nextLink(){ spanNext.innerHTML = "<a href='javascript:next();' style='color: #428bca;text-decoration: none;'>下一页</a>";}    
	function nextText(){ spanNext.innerHTML = "下一页";}    
	   
	function firstLink(){ spanFirst.innerHTML = "<a href='javascript:first();' style='color: #428bca;text-decoration: none;'>首页</a>";}    
	function firstText(){ spanFirst.innerHTML = "首页";}    
	   
	function lastLink(){ spanLast.innerHTML = "<a href='javascript:last();' style='color: #428bca;text-decoration: none;'>尾页</a>";}    
	function lastText(){ spanLast.innerHTML = "尾页";}    
	   
	//隐藏表格    
	function hide(){    
	    for ( var i = pageSize; i<numberRowsInTable; i++ ){    
	        theTable.rows[i].style.display = 'none';    
	    }    
	        
	    totalPage.innerHTML = pageCount();    
	    pageNum.innerHTML = '1';    
	        
	    if(totalPage.innerHTML<=1){
	    	nextText();
	    	lastText();
	    }else{
	    	nextLink();    
	    	lastLink();    
	    }
	}
	
