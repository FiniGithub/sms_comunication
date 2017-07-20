$(function(){
	placeholder();
	maxLenth()
})

function placeholder(){//文本框提示，不含密码框
	
	var a= $(":input")
	for(i in a){		
		if(typeof a.eq(i).attr("placeholder")!="undefined"&&a.eq(i).attr("type")!="password"){
			var v=a.eq(i).attr("placeholder");			
			a.eq(i).val(v);	
			a.eq(i).bind("focus",function(){
				$(this).val("");
				$(this).css("color","#000000");
				$(this).unbind("focus");
			})
		}		
	}
	var p=$("input[type='password']")
}
function maxLenth(){
	$(".dxcontent-xg-txt").on("keydown",function(){
		var l=$(this).val().length;		
		if(l>300){			
			if(event.keyCode!=8){
				event.returnValue=false; 
			}			
			l=300;			
		}		
		$(".dxlength").text(l);
	})
	
}
