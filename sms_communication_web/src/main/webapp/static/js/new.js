$(function(){
	
	adWindow("#adWindow",2000);
});
	function adWindow(obj,time){//obj为JQ选择器，time为延迟时间，	
		 ajaxCall({
		        url: "/msmSend/from/querySmsToAudit.do?",
		        type: "get",
		        success: function (data) {
		            if (checkRes(data)) {
		            	var values = data["data"]["data"];
		            	var num = 0;
		            	num = values;
		            	$("#ardValue").text(num);
		        		setTimeout(function(){	
		        			if($(obj).is(":hidden") && parseInt(num)>0){
		        					$(obj).slideDown();		
		        			}else{
		        				adWindow("#adWindow",60000);
		        			}
		        		},time)	
		            }
		        },
		        error: function () {
		        },
		        complete: function () {
		        }
		    });

		
		
		
		
	}
	$(".cencalcolose").click(function(){//广告弹窗关闭按钮
		$("#adWindow").slideUp();			
		adWindow("#adWindow",60000);
	})