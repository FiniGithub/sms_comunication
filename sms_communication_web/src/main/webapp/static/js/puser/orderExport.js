function orderExport12(path) {
$.get(path + "/export/querySmsData.do?email=" + $('#user_input').val(),function(data){
	if (checkRes(data)) {
		
    	window.open(
    			path + '/export/orderExport.do?email=' + $("#user_input").val(),
				'height=100,width=400,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'
			);
    }else{
    	alert("上月没有可以导出数据");
    }
});
	
}



