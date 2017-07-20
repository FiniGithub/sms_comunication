var path = $("#path").val();
$(function () {
	changeImg_Reg();	
	
	$("#email").focus();
	
	
    
    rebind()
});

//修改密码 - 获取验证码
function getVerCode(){
		var phone = $("#phone").val();		
		if(!checkPhone(phone,"")){
			alert("请输入正确的手机号码!");
			return;
		}
		var data = 
		{
			"phone" : phone,
			"imgCode" : "checkImgCode",
			"captcha" : $("#imgCode").val()
		};
	$.post(path + "/smsUser/getVertifyCode.do", data, function(data) {
		if(data["retCode"] == "000000"){
			var data = 
				{
					"email" : $("#email").val(),
					"phone" : $("#phone").val()
				};
			$.post(path + "/smsUser/getVertifyCode.do", data, function(data) {
				if(data["retCode"] == "000000"){
					countBtn("#pwd-getcode",60);// 60s倒计时
				}else{
					var msg = data["data"];
					alert(msg);
				}
			});
		}else{
			var msg = data["data"];
			alert(msg);
		}
	});
	
}
//修改密码 - 确定修改
function getPassword() {

	
		var isEmpty = false;
		$(".imp").each(function() {
			if ($(this).val() == '') {
				alert("请输入必填选项");
				$(this).focus();
				isEmpty = true;
				return false;
			}
		});
		if (isEmpty) {
			return;
		}

		var email = $("#email").val()// 账号
		var phone = $("#phone").val();// 号码
		var imgcode = $("#imgCode").val();// 图文验证码
		var phocode = $("#verifyCode").val();// 手机验证码
		var reg = /^[0-9a-zA-Z]*$/g;
		if (!checkPhone(phone, "")) {
			alert("请输入正确的手机号码!");
			return;
		}
		var data = {
			"email" : email,
			"phone" : phone,
			"phocode" : phocode,
			"imgCode" : imgcode
		}

		$.post(path + "/smsUser/find/findbackpwd.do", data, function(data) {
			var jsonDate = data["data"];
			if (jsonDate["code"] == "0") {
				alert(jsonDate["msg"]);
				$("#zxForm")[0].reset();
				window.location.href = "logout.do";
			} else {
				alert(jsonDate["msg"]);
			}
		});

	}	
function checkInputMsg(val){
	if(val==null || val=="" ){
		return false;
    }
    return true;
}

// 校验密码
function checkPassword(pwd1,pwd2) {
	
	var msg = '';	
	if (pwd1 != pwd2) {
		$("#input-pwdenter").focus();
		alert("两次密码输入不一致!");
		return false;
	}
	if (pwd1.length < 6 || pwd1.length > 12) {
		$("#input-pwd").focus();
		alert("密码长度在6~12位之间!");
		return false;
	}

	/*
	 * if (!reg.test(pwd1)) { $("#input-pwd").focus();
	 * $("#check-msg").html("请输入6~12位数字及英文组合密码"); return false; }
	 * 
	 * if (!reg.test(pwd2)) { $("#input-pwdenter").focus();
	 * $("#check-msg").html("请输入6~12位数字及英文组合密码"); return false; }
	 */
	
	
}


/**
 *  验证手机号码
 * @param v
 * @param obj
 * @returns {Boolean}
 */
function checkPhone(v,obj){
	var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/;  
	if(!myreg.test(v)){
		$(obj).html("请输入正确手机号码!");
		return false;
	}
	$(obj).html("");
	return true;
}


function changeImg_Reg(){
	$("#reg_captcha").val("");
	var imgSrc = $("#reg_captchaImage");  
    imgSrc.attr("src", changeUrl_Reg(path + "/captcha.do"));
}
//为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳  
function changeUrl_Reg(url) {  
    var timestamp = (new Date()).valueOf();  
    var index = url.indexOf("?",url);  
    if (index > 0) {  
        url = url.substring(0, url.indexOf(url, "?"));  
    }  
    if ((url.indexOf("&") >= 0)) {  
        url = url + "×tamp=" + timestamp;  
    } else {  
        url = url + "?timestamp=" + timestamp;  
    }  
    return url;  
}  

function rebind(){	
	$("#pwd-getcode").bind("click",function(){
		Button_Click(getVerCode,200);
	});//验证按钮防止重复点击
	$("#updatePwdEnter").bind("click",function(){
		$("this").attr("disabled","disabled");
		Button_Click(getPassword,200);//提交按钮
	});	
	var i=0;  //判断点击次数寄存
	var closetimer = null;  //延时函数寄存
	function Button_Click(fn,time)//botton点击事件
	{ 	
	i++;  //记录点击次数
	var action=fn;	
	closetimer = window.setTimeout(function(){	setout(action)	},time); //后执行事件 	提交
	}
	function setout(fn){  //点击执行事件	
		if(i>1)   //如果点击次数超过1
		{
			alert("请勿频繁点击按钮!");
			window.clearTimeout(closetimer);  //清除延时函数
			closetimer = null;  //设置延时寄存为null		 
			i=0;  //重置点击次数为0
		}	else if(i==1){  //如果点击次数为1
			fn();// 咨询提交
		i=0;  //重置点击次数为0
		//添加执行操作的代码
		}
	}
}