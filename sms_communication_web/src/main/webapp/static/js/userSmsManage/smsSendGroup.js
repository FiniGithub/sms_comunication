var ctx = $("#i_ctx").val();
$(function () {
	//$("#loading").hide();
	$("#text_phone").val("");
	$("#text_count").val("");
	
	$("#i_empty").bind("click", function () {
		$("#text_phone").val("");
		$("#number_num").html(0);
   });
	
	$("#text_phone").blur(function(){
		jsNum();
	});
    
	
	$("#i_dxfas").bind("click", function () {
		var text_phone = $("#text_phone").val();
		var text_count = $("#text_count").val();
		var gid = $("#text_gid").val();
		var menuId = $("#menuId").val();     
		if(text_phone=="" ){
			alert("请填写发送号码！");
			return;
		}
		if(text_count=="" ){
			alert("请填写发送内容！");
			return;
		}
		if(gid==""){
			alert("请选择发送通道！");
			return;
		}
	        var data = {
	        		"phones":text_phone,
	        		"counts":text_count,
	        		"gid":gid
    		};
	        
	        $("#dr_modal-content").hide();
	        $("#dr_data_div").modal("show");
	        $("#loading").html("发送加载");
	        $("#loading").show();
	        ajaxCall({
	            url: "/userMsmSend/smsGroupSend.do?id="+menuId,
	            type: "post",
	            data: JSON.stringify(data),
	            success: function (data) {
	            	if (checkRes(data)) {
	                    alert("提交成功！");
	                    $("#text_phone").val("");
	                    $("#number_num").html(0);
	                    $("#dr_data_div").modal("hide");
	                	$("#loading").hide();
	                	
	                } else {
	                	var ztNum =data["data"];
	                	if(ztNum=="-1"){
	                		alert("操作失败");
	                	}else if(ztNum=="1"){
	                		alert("Key无效");
	                	}else if(ztNum=="2"){
	                		alert("没有分配用户通道");
	                	}else if(ztNum=="3"){
	                		alert("手机号码无效");
	                	}else if(ztNum=="4"){
	                		alert("账户余额不足");
	                	}else if(ztNum=="5"){
	                		alert("扣费失败");
	                	}else if(ztNum=="6"){
	                		alert("内容包含敏感词");
	                	}else if(ztNum=="7"){
	                		alert("参数不足或参数内容为空");
	                	}else if(ztNum=="8"){
	                		alert("账号已停用");
	                	}else if(ztNum=="9"){
	                		alert("短信签名内容有误");
	                	}else if(ztNum=="21"){
	                		alert("账户不存在");
	                	}
	                	$("#dr_data_div").modal("hide");
	                	$("#loading").hide();
	                }
	            	
	            },
	            error: function () {
	                $(".ibox-content").prepend(alertview("danger", "操作失败."));
	                $("#loading").hide();
	                $("#dr_data_div").modal("hide");
	            },
	            complete: function () {
	            }
	        });
	});

});

function drDataDiv(){
	$("#dr_data_div").modal("show");
	$("#dr_modal-content").show();
}


function countFrom(txtValue){
	var num =txtValue.length;
	$("#shuzi").html(num);
}

function jsNum(){
	var s=$("#text_phone").val().trim();
	var n=0;
	if(s!=""){
		n=(s.split(',')).length-1;
		n=n+1;
	}
	$("#number_num").html(n);
}

function upload(){
	var mobileFile = document.getElementById("mobileFile").value;
	
	if(mobileFile == ""){
		alert("请选择要导入的文件!");
		return;
	}else if(mobileFile.indexOf(".txt") == -1){
		alert("只能导入txt格式的文件!");
		return;
	}
	document.getElementById("uploadbtn").disabled=true;
	$("#fr_merge_form").submit();

}



if (!window.com) {  
    window.com = {};  
}  
if (!window.com.company) {  
    window.com.company= {};  
}  
if (!window.com.company.project) {  
    window.com.company.project= {};  
}  
if (!window.com.company.project.services) {  
    window.com.company.project.services = {};  
}  
if (!window.com.company.project.services.newCase) {  
    window.com.company.project.services.newCase = {};  
}  
  
//生成随机guid数(参考网上)  
com.company.project.services.newCase.getGuidGenerator = function() {   
    var S4 = function() {   
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);   
    };   
    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());   
};  
  
//上传文件  
com.company.project.services.newCase.fileUpLoad = function(){  
    var fileName = $("#btnFile").val();//文件名  
    fileName = fileName.split("\\");  
    fileName = fileName[fileName.length-1];  
    var guid = com.company.project.services.newCase.getGuidGenerator();//唯一标识guid  
    var data = {guid:guid};  
    jQuery.ajaxSettings.traditional = true; 
    $("#loading").html("上传加载");
    $("#loading").show();
    $("#dr_modal-content").hide(); 
    $.ajaxFileUpload({  
        url : ctx+'/userMsmSend/fileUpLoad.do',  
        secureuri : false,//安全协议  
        fileElementId:'btnFile',//id  
        type : 'POST',  
        //dataType : 'json',  
        data:data,  
        async : false,  
        error : function(datas,status,e) {  
            alert('Operate Failed!');  
            $("#loading").hide();
        },  
        success : function(json) {  
        	var t = $(json).text();
        	if(t==null || t==""){
        		alert("导入失败！");
        		 $("#loading").hide();
        		return;
        	}
        	var obj = JSON.parse(t);
        	if (checkRes(obj)) {
            	var map =obj["data"];
            	var invalidNum = map["invalidNum"];
            	var duplicateNum = map["duplicateNum"];
            	var validNum = map["validNum"];
            	var validLsit= map["validLsit"];
            	var text_phone="";
            	text_phone = $("#text_phone").val().trim();
            	var html = "";
            	
            	
            	if(text_phone==""){
            		var  nodesStr = validLsit.join(",");   
            		text_phone=nodesStr;
            	}else{
            		for(var i =0;i<validLsit.length;i++){
                		if(text_phone.indexOf(validLsit[i])>-1){
                			validNum--;
                			duplicateNum++;
                		}else{
                			
                			html+=validLsit[i]+",";
                		}
                	}
                	html =html.substr(0, html.length - 1);
    	            if(text_phone!="" && html!=""){
    	            	html=","+html;
                	}
    	            text_phone+=html
            	}
            	
            	
	            $("#text_phone").val(text_phone);
            	alert("无效号码："+invalidNum+"个，重复号码："+duplicateNum+"个，有效号码："+validNum+"个");
            	$("#dr_data_div").modal('hide');
            	jsNum();
            	 $("#loading").hide();
            }else{
            	alert("导入失败！");
            	 $("#loading").hide();
            }
        }  
    });  
};  
  
//文件删除  
com.company.project.services.newCase.filedelete = function(guid,fileName){  
    jQuery.ajaxSettings.traditional = true;  
    var data = {guid:guid,fileName:fileName};  
    $.ajax({  
        url : ctx+'/userMsmSend/filedelete.do',  
        type : 'POST',  
        //dataType : 'json',  
        data:data,  
        async : false,  
        error : function() {  
            alert('Operate Failed!');  
        },  
        success : function(json) {  
            if (json.resultFlag==false){  
                alert(json.resultMessage);  
            }else{  
                alert('删除成功!');  
                $("#"+guid).remove();  
            }  
        }  
    });  
};  