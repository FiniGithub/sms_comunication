$(function () {
	
	$("#u_btn").bind("click", function () {
    	 var u_checkbox = document.getElementById('u_checkbox');//
    	  if(u_checkbox.checked){
    	    //选中了
    		  $("#key_data_div").modal('hide');
    		  u_checkbox.checked=false
    		  $("#u_key").val("");
    	  }
    });
	
	
	$("#i_btn").bind("click", function () {
		sbmitQueryKey(0);
   });
	
	$("#s_btn").bind("click", function () {
		var i_reportUrl = $("#s_reportUrl").val();
		var i_replyUrl = $("#s_replyUrl").val();
		var i_id = $("#smsUserId").val();
		
	        var param = {"reportUrl": i_reportUrl,"replyUrl": i_replyUrl,"id":i_id};
	        ajaxCall({
	            url: "/userMessage/from/userMerge.do",
	            type: "get",
	            data: param,
	            success: function (data) {
	            	if (checkRes(data)) {
	                    alert("地址修改成功！");
	                    $("#td_reportUrl").html(i_reportUrl);
	                    $("#td_replyUrl").html(i_replyUrl);
	                    $("#sx_data_div").modal('hide');
	                } else {
	                	alert("操作失败");
	                }
	            },
	            error: function () {
	            	alert("操作失败");
	            },
	            complete: function () {
	            }
	        });
	       
   });
	
	
	$("#key_btn").bind("click", function () {
		var i_phone = $("#i_phone").val();
		
		if(isPhoneNumber(i_phone)==false){
			alert("非手机号码,无法获取手机号码！");
			clearTimeout(t);
			countdown = 60; 
			$("#key_btn").removeAttr("disabled"); 
			$("#key_btn").val("获取验证码");
			return;
		}
		var param = {
				"phone" : i_phone
			};
	  		ajaxCall({
				url : "/userMessage/from/sundYzm.do",
				type : "get",
				data : param,
				success : function(data) {
					if (checkRes(data)) {
						alert("验证码发送成功，请注意接收！");
					} else {
						alert("验证码发送失败！");
						clearTimeout(t);
						countdown = 60; 
						$("#key_btn").removeAttr("disabled"); 
						$("#key_btn").val("获取验证码");
					}
				},
				error : function() {
				},
				complete : function() {
				}
			});
	});
	
	
});

function isPhoneNumber(string) {
	var pattern = /^1\d{10}$/;
	if (pattern.test(string)) {
		
		return true;
	}
	console.log('check mobile phone ' + string + ' failed.');
	return false;
};

function sbmitQueryKey(nums){
  	 var u_checkbox = document.getElementById('i_checkbox');//
   	 var i_key = $("#i_key").val();
   	 var smsUserId = $("#smsUserId").val();
   	  if(u_checkbox.checked || nums==1){
   	    //选中了
  		var params = {
				"i_key" : i_key,
				"smsUserId":smsUserId
		};
  		ajaxCall({
			url : "/userMessage/from/yzmQuery.do",
			type : "get",
			data : params,
			success : function(data) {
				if (checkRes(data)) {
					var  key = data["data"]
					$("#u_key").val(key);
					if(key!=null && key!=""){
						$("#smsUserKey").html(key.substr(0,16)+new Array(key.length-16).join('*'));
					}
					$("#cz_data_div").modal('hide');
			   		$("#key_data_div").modal("show");
			   		$("#i_key").val("");
			   		u_checkbox.checked=false
			   		if(nums==1){
			   			$("#a_key").html('<a href="javascript:czDataDiv();" style="color: blue;">重置KEY</a>');
			   		}
				} else {
					alert("验证码不正确！");
				}
			},
			error : function() {
			},
			complete : function() {
			}
		});
   		 
   	  }
}


function czDataDiv(){
	$("#cz_data_div").modal("show");
}

function tsDateDiv(){
	$("#sx_data_div").modal("show");
}



function copyUrl2()
{
var Url2=document.getElementById("u_key");
Url2.select(); // 选择对象
document.execCommand("Copy"); // 执行浏览器复制命令
alert("复制成功，可贴粘。");
}



//-----------------------------------验证码倒计时开始----------------------------------------
var t;
var countdown=60; 
function settime(val) { 
if (countdown == 0) { 
val.removeAttribute("disabled");    
val.value="获取验证码"; 
countdown = 60; 
return;
} else { 
val.setAttribute("disabled", true); 
val.value="重新发送(" + countdown + ")"; 
countdown--; 
} 

t=setTimeout(function() { 
settime(val) 
},1000) 
} 
//-----------------------------------验证码倒计时结束----------------------------------------