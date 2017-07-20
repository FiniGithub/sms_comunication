var path = $("#server_path").val();
$(function () {
	if(window.location.hash=="#layout"){
		window.parent.location.href=path+"/logout.do";
		return;
	}
	if(window.location.hash=="#et"){
		window.location.href=path+"/logout.do";
		return;
	}	
	rebind();//防重复点击
	$("#account").focus();
	changeImg_Reg();
	

	getVerCode();// 获取验证码

	
    //回车
    $("#account,#password,#captcha").bind("keydown",function (e) {
        if(e.keyCode==13){
            $("#loginbtn").click();
        }
    });
});

/**
 * 登录
 */
function login(){
	 //登录按钮
     	
        var $btn = $(this);
        var account = $("#account").val();
        var password = $("#password").val();
        var captcha = $("#captcha").val();
      
        if(account==''){
        	alert("请输入用户名"); 
        	return;
        }else if(password==''){
        	alert("请输入密码");  
        	return;
        }
		/*else if(captcha==''){
        	alert("请输入验证码");
        	return;
        }*/
        $btn.attr("disabled","true")
       $btn.val("正在登录...");
        var data={"account": account, "password":password,"captcha":captcha,"type":"login"};
        
        login_ajax(data);
       // 登录
   
}

 

 
 
/**
 * 登录
 * @param data
 */
function login_ajax(data){
	var $btn = $("#loginbtn");
	var path = $("#server_path").val();	
	ajaxCall({
        url: "/login.do",
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if(checkRes(data)) {
                window.location.href = path + "/welcome.do"; 
         
            }else {
            	var errorNum = data["errorNum"];
            	var msg = data["msg"];
            	alert(msg);
            	$btn.removeAttr("disabled");
				$btn.val("登 录");
            	$('#captchaImage').attr("src", "captcha.do?timestamp=" + (new Date()).valueOf());
            	
            }
        },
        error: function () {
            alert("登录失败,请稍候再试");
			$btn.val("登 录");
			$("#loginbtn").removeAttr("disabled");
            $('#captchaImage').attr("src", "captcha.do?timestamp=" + (new Date()).valueOf());
        },
        complete: function () {
        	
        	$("#loginbtn").removeAttr("disabled");
        	
        }
    });
}

// 获取验证码  全网信通
function getVerCode(){
	var path = $("#server_path").val();
	$("#pwd-getcode").click(function() {
		var phone = $("#phone").val();
		if(phone==""){
			alert("请输入手机号码!");
			return;
		}
		if(!isPhone(phone)){
			alert("请输入正确的手机号码!");
			return;
		}
		var data = {"phone" : phone};
		$.post(path + "/smsUser/getVertifyCodeByFeedBack.do", data, function(data) {
			if(data["retCode"] == "000000"){
				countBtn("#pwd-getcode",60);// 60s倒计时
			}else{
				var msg = data["data"];
				alert(msg);
			}
		});
	});
}


function check_update_pwd(phone,code,pwd1,pwd2){
	if (!checkInputMsg(phone)) {
		alert("请输入号码!");
		return false;
	}
	if(!checkPhone(phone,"")){
		alert("请输入正确的手机号码!");
		return false;
	}
	if (!checkInputMsg(code)) {
		alert("请输入验证码!");
		return false;
	}
	if (!checkInputMsg(pwd1)) {
		alert("请输入密码!");
		return false;
	}
	if (!checkInputMsg(pwd2)) {
		alert("请输入确认密码!");
		return false;
	}
	return true;
}



/**
 * 表单校验
 * @returns {Boolean}
 */
function check_form_login( ){
	var aval = $("#account").val(); 
	var pval = $("#password").val();
    var cval = $("#captcha").val();
    
    var aobj = $("#sp-phone");
    var pobj = $("#sp-pwd");
    var cobj = $("#sp-code");
    
    var amsg = "请输入手机号!";
    var pmsg = "请输入密码!";
    var cmsg = "请输入验证码!";
    
    // 1. 校验号码
    if(!checkInput(aval,aobj,amsg)){
    	return false;
    }/*else if(!checkPhone(aval,aobj)){
       return false;
    }*/
    
    // 2. 校验密码
    if(!checkInput(pval,pobj,pmsg)){
    	return false;
    }
    
	// 3. 校验验证码
   /* if(loginCount >=3){
    	$(".r-n-inp-yz-box").show();
    	if(!checkInput(cval,cobj,cmsg)){
    		$("#sp-phone").html("请输入验证码!");
        	return false;
        } 
    }*/
    return true;
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




function queryErrorNum(val){
		var data = {"account":val};
		ajaxCall({
	        url: "/queryErrorNum.do",
	        type: "post",
	        data: JSON.stringify(data),
	        success: function (data) {
	        },
	        error: function () {
	        },
	        complete: function () {
	        }
	    });
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

$("#cbLisence").on('click',function(){
	
	var checkbox = $("#cbLisence");
	if(checkbox.is(':checked')){
		$("#loginbtn").removeAttr('disabled',false);
	
		
	}else{
		$("#loginbtn").attr('disabled', true);
	}
});

/**
 * 表单校验
 * @param val
 * @param obj
 * @param msg
 * @returns {Boolean}
 */
function checkInput(val,obj,msg){
	if(val==null || val=="" ){
		obj.html(msg);
		return false;
    }
    obj.html("");
    return true;
}

function checkInputMsg(val){
	if(val==null || val=="" ){
		return false;
    }
    return true;
}
$('#tz').on('show.bs.modal', function (e) {
	
	$(this).css('display', 'block');  
	fixMidel("#tz .modal-dialog")
});
//$('.awbtn').on('click', function () {
//	var tar=$(this).attr("data-tar");
//	$(".dxtype").hide();
//	$("."+tar).show();
//	$(".key-ul li").removeClass("key-on");
//	$("."+tar).parent().addClass("key-on");
//	$('#tz').modal('show');
//});

function rebind() {
  

    $("#loginbtn").bind("click", function () {
    	$("this").attr("disabled","disabled");
        Button_Click(login, 200);//登录按钮
    });

    var i = 0;  //判断点击次数寄存
    var closetimer = null;  //延时函数寄存
    function Button_Click(fn, time)//botton点击事件
    {
        i++;  //记录点击次数
        var action = fn;
        closetimer = window.setTimeout(function () {
            setout(action)
        }, time); //后执行事件 	提交
    }

    function setout(fn) {  //点击执行事件
        if (i > 1)   //如果点击次数超过1
        {
            alert("请勿频繁点击按钮!");
            window.clearTimeout(closetimer);  //清除延时函数
            closetimer = null;  //设置延时寄存为null
            i = 0;  //重置点击次数为0
        } else if (i == 1) {  //如果点击次数为1
            fn();// 咨询提交
            i = 0;  //重置点击次数为0
            //添加执行操作的代码
        }
    }
}