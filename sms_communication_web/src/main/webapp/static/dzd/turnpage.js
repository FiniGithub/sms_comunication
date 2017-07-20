$(function(){
	page();
})
function page(){
	
	$("#pagenum").bind("keydown", onlyNum);
$("#firstpage").bind("click",function(){	
	 $('#tb_data,#com-table,#fs-table').bootstrapTable('selectPage',1);		
})
$("#lastpage").bind("click",function(){	
	var num=$(".page-last a").text();
	 $('#tb_data,#com-table,#fs-table').bootstrapTable('selectPage',num);		
})
	$("#turnpage").bind("click",function(){
		var num=$("#pagenum").val();
		console.log(num)
		 $('#tb_data,#com-table,#fs-table').bootstrapTable('selectPage',num);		
	})
	
	
	
}


function onlyNum() {// 输入数字类型
	if (!((event.keyCode >= 48 && event.keyCode <= 57)
			|| (event.keyCode >= 96 && event.keyCode <= 105) || (event.keyCode == 8))&&event.keyCode!=13){
		event.returnValue = false;
		
	}
 if(event.keyCode==13){
 
	$("#turnpage").click();
	}
		
	
}
$('.modal').modal({backdrop: 'static',keyboard: false,show:false});