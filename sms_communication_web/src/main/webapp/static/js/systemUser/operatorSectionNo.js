$(function() {
	
	querySectionNo();

	var oldmobile = $("#mobile").val(); 
	var oldunicom = $("#unicom").val(); 
	var oldtelecom = $("#telecom").val(); 

	$("#btn_submit").unbind();
	$("#btn_submit").bind("click", function() {
		updateSectionNo(oldmobile, oldunicom, oldtelecom);
	});

});

function querySectionNo(){
	$.get(
			"queryoperatorsectionno.do",
	 		function(data) {
				for(var i=0;i<data.length;i++){
					if(0 == data[i].type){
						$("#unicom").val(data[i].sectionNo);
					}
					if(1 == data[i].type){
						$("#mobile").val(data[i].sectionNo);					
					}
					if(2 == data[i].type){
						$("#telecom").val(data[i].sectionNo);
					}
				}				
		});	
}

function updateSectionNo(oldmobile, oldunicom, oldtelecom) {
	
	var newmobile = "";
	var newunicom = "";
	var newtelecom = "";
	var change = true;
	if($("mobile").val()!=oldmobile){
		newmobile = "1:" + $("#mobile").val();
		change = false;
	}
	if($("unicom").val()!=oldunicom){
		newunicom = "0:" + $("#unicom").val();
		change = false;
	}
	if($("telecom").val()!=oldtelecom){
		newtelecom = "2:" + $("#telecom").val();
		change = false;
	}
	
	if (change) {
		alert("数据未修改，请先修改后确定保存！");
		return;
	}
	
	var data = {
			"mobileOperator" : newmobile,
			"unicomOperator" : newunicom,
			"telecomOperator" : newtelecom
		};
			
		$.post(
				"updateoperatorsectionno.do",
				data,
		 		function(data) {
					querySectionNo();				
			});	
	
}