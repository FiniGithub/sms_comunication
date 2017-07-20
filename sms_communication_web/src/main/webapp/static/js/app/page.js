//index
var path = $("#path").val();
$(function() {
	headChoise();
	newsnum();// 消息数量
	menuOpen(); // 菜单变化
	pageInp();// 页码框
	// paswInp();// 密码框
	fixMidel(".regiger-box"); // 动态固定注册框的位置
	regBox(); // 注册弹窗
	tsAnimate(); // 提示动画
	slideAnimate(".ptsbtn", ".ptsbox"); // 滑动动画;
	moveAnimate(".uer-name-box", ".usermenu");// 移入显示
	alwdInp(); // 弹窗按钮
	selebox(); // 选择框
	alwdClose(); // 弹窗关闭
	selectDay(); // 日期选择插件
	alone();// 弹窗类型一
	inpLength();// 输入字符数
	fxdx_btn();// 发送短信提示
	qrfs_end();// 确认发送
	tsBtn();// 推送按钮
	message();// 消息
	api();// API配置
	logout();// 退出
	inpOutactive();//
	updatePwd();// 修改密码
	getVertifyCode();// 修改密码接收验证码
	enterUpdatePwd();// 确认修改
	getRegCode(60);// 注册获取验证码
	// countBtn(".key-inp-yzbtn", 60);//倒计时
})

// 顶部按钮自动选择
function headChoise() {
	$(".subbtn li a").each(
			function() {
				console.log(2)
				$this = $(this);
				if ($this[0].href == String(window.location)) {
					$(".head-btn").removeClass("head-onactive");
					$this.eq(0).parent().parent().parent()
							.siblings(".head-btn").addClass("head-onactive");
					var cont = $this.eq(0).html();
					$this.eq(0).parent().parent().parent()
							.siblings(".head-btn").html(cont);
				}
			});
}

// 登录页面413

function inpOutactive() {
	$(".r-n-inp").focus(function() {
		$(this).parent().removeClass("errorinp").addClass("changeinp");
	});
	$(".r-n-inp").blur(function() {
		$(this).parent().removeClass("errorinp changeinp")
	});

}
function pswCheack(arr) {// 密码正则验证
	var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/g;
	return reg.test(arr);
}
function isPhone(arr) {
	var reg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/;
	return reg.test(arr);
}
function pageInp() {
	$(".pagenum,#phone").on("keydown", onlyNum);
}
function textInp(obj, fn) {
	$(obj).blur(function() {
		console.log(fn($(this).val()));
		if (!fn($(this).val())) {
			$(this).parent().removeClass("changeinp").addClass("errorinp");
			$(this).siblings(".check-img").hide().siblings(".check-f").show();
			$(".pho-ts").text("请正确输入手机号码")
			return;
		}
		$(this).siblings(".check-img").hide().siblings(".check-t").show();
	});

}
$(":input[type='password']").on("keydown", onlyNumZ);
function onlyNumZ() {// 正则限制输入字母和数字
	var reg = /^[0-9a-zA-Z]*$/g;
	if (!reg.test(event.key)) {
		event.returnValue = false;
	}

}
/**
 * 注册获取验证码
 */
function getRegCode(time) {
	$("#getRegCode").click(function() {
				var path = $("#server_path").val();
				var phone = $("#phone").val();
				var ti = time;
				if (!isPhone(phone)) {
					$("#phone").parent().removeClass("changeinp").addClass(
							"errorinp");
					$("#phone").siblings(".check-img").hide().siblings(
							".check-f").show();
					$(".pho-ts").text("请正确输入手机号码!");
					return;
				}
				var captcha = $("#reg_captcha").val();
				if(captcha == null || captcha == ''){
					$("#reg_captcha").parent().removeClass("changeinp").addClass(
						"errorinp");
					$("#reg_captcha").siblings(".check-img").hide().siblings(
						".check-f").show();
					$(".pho-ts").text("请输入图形验证码!");
					return;
				}
				
				
				$("#phone").siblings(".check-img").hide().siblings(".check-t").show();
				var data = {
					"phone" : phone,
					"type":"registerCode",
					"captcha":captcha
				};
				var timer = $(this);
				registerUser(data);
	});
}

 
// 注册
function registerUser(data){
	$.post(path + "/smsUser/getVertifyCode.do", data,function(data) {
		console.log(data);
		if (data["retCode"] == "000000") {
			countBtn("#getRegCode", 60);// 60s倒计时
		} else {
			var msg = data["data"];
			alert(msg);
//			changeImg_Reg();
		}
	});
}


function stopPropagation(e) {
	if (e.stopPropagation) {
		e.stopPropagation();
	} else {
		e.cancelBubble = true;
	}
}
function selebox() { // 选择框
	$(".xlbtn").on(
			"click",
			function(e) {
				stopPropagation(e);
				$(".xlbox").hide();
				if (!$(this).siblings(".xlbox").is(":animated")) {
					$(this).siblings(".xlbox").is(":hidden") ? $(this)
							.siblings(".xlbox").show() : $(this).parent(
							".xlbox").hide();
				}

			})
}
$(document).on('click', function() {
	$(".newscontent,.xlbox").hide();
	});
function newsBtn(obj, id) {// 消息按钮
// alert(id);
// 

	// $(".news-ul").on("click",function(e) {
	// stopPropagation(e);
	if (!$(obj).children().children(".newscontent").is(":animated")) {
		$(obj).children().children(".newscontent").is(":hidden") ? $(obj)
				.children().children(".newscontent").show() : $(obj).children()
				.children(".newscontent").hide();
	}
	var data = {
		"id" : id,
		"state" : 1
	};
	$.ajax({
		url : path + "/smsUser/from/messageMerge.do",
		type : "post",
		data : JSON.stringify(data),
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			if (data["retCode"] == "000000") {
				var msgCount = data["data"];
				$(".newsnum").html(msgCount);
				if (msgCount == 0) {
					$(".newsnum").removeClass("newsnum2");
				}

				var contentObj = $(obj).children().children();
				$(contentObj).addClass("msg_bg");
				$(contentObj).children().hide();
			}
		},
		error : function() {
			alert("错误");
		},
		complete : function() {

		}
	});
	// });
}
function tsBtn() {// 推送按钮
	$(".sc-ul li").on("mouseover", tmoveIn);
	/*
	 * $(".sc-ul li").on("click", function() { alert(); $(".sc-ul
	 * li").off("mouseover"); var type = $(this).attr("data-dxtype");
	 * $("#dxdtype").val(type); $(".sc-ul li").off("click"); })
	 */
}
function tmoveIn() {
	$(this).addClass("key-on").siblings().removeClass("key-on")
	$(this).children(".dxtype").show()
	$(this).siblings().children(".dxtype").hide();
}

/**
 * 定时器
 */
var bfb = 0;
var t = '';

/**
 * 改变进度条
 * 
 * @param a
 */
function changeProgress(value) {
	$(".progebfb").html(value);
	$(".proge").css("width", value + "%");
}

function pageInp() {
	$(".pagenum").on("keydown", onlyNum)
}

function moveAnimate(btn, box) {
	$(btn).hover(function() {
		$(box).show()
	}, function() {
		$(box).hide()
	})
}
function inpLength() {
	$(".dxcontent-xg-txt").on("input", function() {
		var content = $("#content").val();
		var sign = $("#qminp").val();
		var signLength = sign.length;
		var allLength = content.length + signLength;
		if(allLength>300){
			content = content.substring(0, 300-signLength);
			$("#content").val(content);
			allLength = content.length + signLength;
		}
		$(".dxlength").text(allLength);// 短信字数
		
		// 短信条数
		var sigleSmsLength = 1;
		if(allLength>70){
			var smsTextLen = allLength-70;
			sigleSmsLength = parseInt((smsTextLen/66) + 1);
			if(smsTextLen % 66 >0){
				sigleSmsLength +=1;
			}
		}
		$(".smslength").html(sigleSmsLength);
	})
};

function menuOpen() { // 菜单变化

	$(".head-on").on("mousemove", moveIn);
	$(".d").on("mouseleave", moveOut)
}

function moveIn() {

	$(".head-cha,.subbtn").hide();
	$(".head-on").show();
	$(this).hide().siblings(".d").children("ul,.head-cha").css("display",
			"block");

}

function moveOut() {
	$(".head-cha,.subbtn").hide().parent().siblings(".head-on").show();
}

function onlyNum() {// 输入数字类型
	if (!((event.keyCode >= 48 && event.keyCode <= 57)
			|| (event.keyCode >= 96 && event.keyCode <= 105) || (event.keyCode == 8)))
		event.returnValue = false;
}

function fixMidel(obj) { // obj为JQ选择器
	var w = $(obj).width() // 注册框宽度;
	var h = $(obj).height() // 注册高度；
	$(obj).css({
		position : "fixed",
		top : "50%",
		left : "50%",
		"margin-left" : -0.5 * w + "px",
		"margin-top" : -0.5 * h + "px",
		display : "block"
	})

}

function alwdInp() { // 弹窗按钮
	$(".alwd-inp").on("click", function() {
		fixMidel(".alwindow-box"); // 弹窗动态固定
		$(".alwindow-box").hide();
		$(".regiger-bg").fadeIn("200");
	})
}

function alwdClose() { // 弹窗关闭
	$(".alwd-btn-close,.alwd-close").on("click", function() {
		$(".regiger-bg,.alwindow-box").fadeOut("200");
	});

	$(".btn-alwd-btn-close").on("click", function() {
		$(".regiger-bg,.alwindow-box").fadeOut("200");
	});

}

function regBox() { // 注册弹窗
	$(".regiger-btn").on("click",function() {
				$(this).addClass("regiger-active").siblings(".enty-btn")
						.removeClass("regiger-active");
				$(".r-box").show().siblings(".e-box").hide();
				window.location.href = path + "/loginview.do#reg";
	});

	$(".enty-btn").on("click",function() {
				$(this).addClass("regiger-active").siblings(".regiger-btn")
						.removeClass("regiger-active");
				$(".e-box").show().siblings(".r-box").hide();
				window.location.href = path + "/loginview.do#login";
				var account = $("#account").val();
				if(account == null || account == ''){
					window.location.reload();//刷新当前页面
				}
	});

	$(".r-btn").on("click", function() {
		$(".regiger-box").hide();
		$(".regiger-bg").hide();
	})
}

function alone() {
	// 显示充值弹框
	$(".alwd-tc2").on("click",function() {
		$.post(path + "/smsUser/getAisleMoney.do",function(data) {
			if (data["retCode"] == "000000") {
					var obj = data["data"];
					var htmlData = '<ul>';
					htmlData += '<li><span style="margin-right:50px;">'+ obj.oneIntervalPrice+ ' 元/条</span>';
					htmlData += '<span>购买数量范围 '+ obj.oneIntervalStart+ '</span> ~ '+ obj.oneIntervalEnd+ ' 条</li>';
					htmlData += '<li><span style="margin-right:50px;">'+ obj.twoIntervalPrice+ ' 元/条</span>';
					htmlData += '<span>购买数量范围 '+ obj.twoIntervalStart+ '</span> ~ '+ obj.twoIntervalEnd+ ' 条</li>';
					htmlData += '<li><span style="margin-right:50px;">'+ obj.threeIntervalPrice+ ' 元/条</span>';
					htmlData += '<span>购买数量范围 '+ obj.threeIntervalStart+ '</span> ~ '+ obj.threeIntervalEnd+ ' 条</li>';
					htmlData += '</ul>';

					$("#money-div-i").html(htmlData);
					// 显示弹框
					fixMidel(".cztc");
					$(".alwindow-box").hide();
					$(".regiger-bg").fadeIn("200");
					var taget = $(".alwd-tc2").attr("data-inp");
					console.log(taget)
					$("." + taget).show();
			} else {
					if (data["data"] == -100) {
							alert("用户暂未分配通道!");
					} else {
						alert("系统异常,请稍后再试!");
					}
			}
		});
	});
	// 显示个人中心弹窗
	$(".userInfo").on("click",function(){
		$.post(path + "/smsUser/getAisleMoney.do",function(data) {
			if (data["retCode"] == "000000") {
					var obj = data["data"];
					// 短信条数价格段
					$("#lb_one_IntervalPrice").html(obj.oneIntervalPrice);
					$("#sp_oneIntervalStart").html(obj.oneIntervalStart);
					$("#sp_oneIntervalEnd").html(obj.oneIntervalEnd);
					
					$("#lb_two_IntervalPrice").html(obj.twoIntervalPrice);
					$("#sp_twoIntervalStart").html(obj.twoIntervalStart);
					$("#sp_twoIntervalEnd").html(obj.twoIntervalEnd);
					
					$("#lb_three_IntervalPrice").html(obj.threeIntervalPrice);
					$("#sp_threeIntervalStart").html(obj.threeIntervalStart);
					$("#sp_threeIntervalEnd").html(obj.threeIntervalEnd);
					

					// 显示弹框
					fixMidel("#userInfo_div");
					$(".alwindow-box").hide();
					$(".regiger-bg").fadeIn("200");
					$("#userInfo_div").show();
			} else {
					if (data["data"] == -100) {
							alert("用户暂未分配通道!");
					} else {
						alert("系统异常,请稍后再试!");
					}
			}
		});
	});
	
	
	

	$(".alwd-tc1").on("click", function() {
		var taget = $(this).attr("data-inp");
		// console.log(taget);
		$("." + taget).show();
		$("#text_phone").val("");
		$("#textfield").val("");
		$("#trialId").val("");
		$("#name").val("");
		$("#content").val("");
		$("#type").val("");
		$("#signature").val("");
		$("#reset_code").val("");
		$("#getkey-code").val("");
		$("#replyUrl").val("");
		$("#reportUrl").val("");
		
		leadInit();// 号码个数重置为0
		changeProgress(0);
	})
}
// 进度条初始化
function leadInit() {
	changeProgress(0);// 进度条0%
	$(".all").html("0");
	$(".repet").html("0");
	$(".error").html("0");
	$(".cghm").html("0");
	$("#font-allNum-once").html("");

	$(".lead-finish").hide();
	$(".flex-hint").hide();
	$("#enter_lead").addClass("disable_gray");// 按钮背景设置为灰色
}
// 进度条还原
function leadRecover() {
	changeProgress(100);// 进度条0%

	$(".lead-finish").show();
	$(".flex-hint").hide();// 隐藏gif效果图
	$("#enter_lead").removeClass("disable_gray");// 按钮背景样式恢复
}

function slideAnimate(obj, hd) { // obj为JQ选择器 //滑动动画
	$(obj).on("click", function() {
		if (!$(hd).is(":animated")) {
			$(hd).is(":hidden") ? $(hd).slideDown() : $(hd).slideUp();
		}
	})

}

function tsAnimate() { // 提示动画
	$('.dx-tishi-img').mousemove(function() {
		$(this).hide()
	})
	setTimeout(function() {
		$('.dx-tishi-img').fadeOut(1000);

	}, 2000)

}

function selectDay() { // 日期选择插件
	if ($('#stardate').length == 0 || $("#seleday").length == 0) {
		return;
	}

	$('#stardate').datetimepicker({
		format : 'yyyy-mm-dd ',
		minView : "month",
		initialDate : new Date(), // 初始化当前日期
		autoclose : true, // 选中自动关闭
		todayBtn : true, // 显示今日按钮
		language : 'zh-CN'
	});
	$('#enddate').datetimepicker({
		format : 'yyyy-mm-dd ',
		minView : "month", // 设置只显示到月份
		initialDate : new Date(), // 初始化当前日期
		autoclose : true, // 选中自动关闭
		todayBtn : true, // 显示今日按钮
		language : 'zh-CN'
	});

	$('#seleday').datetimepicker({
		format : 'yyyy-mm-dd hh:ii',
		initialDate : new Date(), // 初始化当前日期
		autoclose : true, // 选中自动关闭
		todayBtn : true, // 显示今日按钮
		language : 'zh-CN',
		pickerPosition : "top-right"
	});
}

function adrouter(obj) {
	$("#textfield").val($(obj).val());

	/*
	 * $(".proge").animate({ width : "100%", }, 2000)
	 */

	// ajax{(
	// type : "POST",
	// url : "",
	// data : $(obj).serialize(),
	// )}
}

startInterval = function() {
	var sum = $("#jdall").html();
	var cgnum = $("#jdall").html();
	var jerror = $("#jderror").html();
	var repet = $("#jdrepet").html();

	bfb = parseInt(bfb) + 10;
	if (bfb >= 100) {
		bfb = 99;
	}
	changeProgress(bfb);// 改变进度条状态
	if (bfb == 100) {
		clearInterval(t);
		bfb = 0;
	}
}

Date.prototype.Format = function(fmt) { // author: meizz
	var o = {
		'M+' : this.getMonth() + 1, // 月份
		'd+' : this.getDate(), // 日
		'h+' : this.getHours(), // 小时
		'm+' : this.getMinutes(), // 分
		's+' : this.getSeconds(), // 秒
		'q+' : Math.floor((this.getMonth() + 3) / 3), // 季度
		'S' : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '')
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp('(' + k + ')').test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (('00' + o[k]).substr(('' + o[k]).length)));
	return fmt;
}

var STATUS_CODE = {
	"success" : "000000"
};
function checkRes(data) {
	return data && data["retCode"] == STATUS_CODE.success;
}

/**
 * 请求后台操作
 * 
 * @author CHENCHAO
 * @date 2017-04-01 10:46:00
 * 
 */

var send_flag = false;
// 发送短信之前的操作 - 将短信号码分类
function fxdx_btn() {
	$(".fxdx-btn").on("click", function() {
		send_flag = false;
		var uuid = $("#uuid").val();// 唯一标识
		enterRefresh();// 初始化控件
		var leadCount = $("#font-leadCount").html();

		// 2.内容
		var content = $("#content").val();// 发送内容
		// 表单校验
		var phone = $("#phone_show").html();
		if (!check_input_send(content, leadCount, phone)) {
			return;
		}

		var data = {
			"content" : content,
			"uuid" : uuid
		};
		$.ajax({
			url : path + "/smsUser/phoneVerify.do",
			type : "post",
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8",
			success : function(obj) {
				if(obj["retCode"] == "000000"){
					var data = obj["data"];
					var smsCount = data["smsCount"];
					var allNumber = data["allNumber"];// 所有号码数量
					var invalidNum = data["invalidPhoneNum"];// 错误号码数量
					var repeatNum = data["repeatPhoneNum"];// 重复号码数量
					var validNum = data["validPhoneNum"];// 有效号码数量
					var validDataList = data["validList"];// 有效号码集合

					$(".proge-all-num").html(allNumber);
					$(".proge-repet-num").html(repeatNum);
					$(".proge-invalid-num").html(invalidNum);
					$(".valid-num").html(validNum);

					// 计算短信条数
					if (validNum != 0) {
						$(".span-smsLength").html(smsCount);
					} else {
						$(".span-smsLength").html('0');
					}
					$(".p-manage-msg").hide();
					$(".qrfs_end").removeClass("disable_gray");
					send_flag = true;
				}else{
					$(".p_text").html(obj["retMsg"]);
					$(".check_send").show();
				}
				

			},
			error : function() {
				$(".p-manage-msg").html("处理发生异常,请重试...");
				alert("错误");
			},
			complete : function() {

			}
		});

	});
}

// 确认发送
function qrfs_end() {
	$(".qrfs_end").on("click", function() {
		if (!send_flag) {
			e.preventDefault();
		} else {
			var phone_show = $("#phone_show").html();
			if (phone_show == '' || phone_show == null) {
				alert("无可发送号码!");
				return;
			}
			$("#send-close-btn").hide();
			$("#state-show").hide();
			fixMidel(".alwindow-box"); // 弹窗动态固定
			$(".alwindow-box").hide();
			$(".regiger-bg").fadeIn("200");
			onSendBtn();// 发送短信
		}
	});
}

// 定时发送复选框被选中
$(function() {
	$('#check_send_time').attr("checked", false);
	$("#check_send_time").click(function() {
		if ($('#check_send_time').is(':checked')) {
			$("#seleday").show();
		} else {
			$("#seleday").hide();
		}
	});
});
// 消息
function message() {
	$(".newsnum").click(function() {
		window.location.href = path + '/smsUser/msglist.do';
	});
}
// api配置
function api() {
	$(".apiWord").click(function() {
		window.location.href = path + '/smsUserApiConfig/apiView.do';
	});
}

// 注销
function logout() {
	$(".logout").click(function() {
		window.location.href = path + '/logout.do';
	});
}

/**
 * 改变消息提示状态
 */
function newsnum() {
	var count = $(".newsnum").html();
	if (count != '' && count != null && count != 'undefined' && count != 0) {
		$(".newsnum").addClass("newsnum2");
	} else {
		$(".newsnum").removeClass("newsnum2");
	}
}

// 修改密码
function updatePwd() {
	$(".updatePwd").click(function() {
		$("#input-code").val("");
		$("#check-msg").html("");
		$(".xgmm").show();
		getSmsUserPhone();
	});
}
// 1.修改密码 - 获取账户电话号码
function getSmsUserPhone() {
	$.post(path + "/smsUser/getSmsUserPhone.do", function(data) {
		if (checkRes(data)) {
			var phone = data["data"];
			$("#sp-phone").html(phone);
		} else {
			alert("发生错误,请重试!");
		}
	});
}

// 2.修改密码 - 获取验证码
function getVertifyCode() {
	$("#input-getcode").click(function() {
		var phone = $("#sp-phone").html();
		var data = {
			"phone" : phone
		};
		$.post(path + "/smsUser/getVertifyCode.do", data, function(data) {
			if (data["retCode"] == "000000") {
				countBtn("#input-getcode", 60);// 60s倒计时
			} else {
				var msg = data["data"];
				alert(msg);
			}
		});
	});
}

// 3.修改密码 - 确定修改
function enterUpdatePwd() {
	$("#updatePwd").click(function() {
		if (!checkPassword()) {
			return;
		}

		var phone = $("#sp-phone").html();// 号码
		var code = $("#input-code").val();// 验证码
		var pwd1 = $("#input-pwd").val();// 第一次密码
		var pwd2 = $("#input-pwdenter").val();// 第二次密码
		var data = {
			"phone" : phone,
			"code" : code,
			"oldPwd" : pwd1,
			"newPwd" : pwd2
		};
		$.ajax({
			url : path + "/smsUser/updatePwd.do",
			type : "post",
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				if (checkRes(data)) {
					var msg = data["data"]["msg"];
					var code = data["data"]["code"];
					if (code == 1) {
						$("#check-msg").html(msg);
					} else {
						alert(msg);
						window.location.href = path + '/logout.do';
					}
				}
			},
			error : function() {
				alert("错误");
			},
			complete : function() {

			}
		});
	});
}

// 校验密码
function checkPassword() {
	var code = $("#input-code").val();// 验证码
	var pwd1 = $("#input-pwd").val();// 第一次密码
	var pwd2 = $("#input-pwdenter").val();// 第二次密码
	var reg = /^[0-9a-zA-Z]*$/g;
	var msg = '';
	if (code == null || code == '') {
		$("#input-code").focus();
		$("#check-msg").html("验证码不能为空!");
		return false;
	}
	if (pwd1 == null || pwd1 == '') {
		$("#input-pwd").focus();
		$("#check-msg").html("新密码不能为空!");
		return false;
	}
	if (pwd2 == null || pwd2 == '') {
		$("#input-pwdenter").focus();
		$("#check-msg").html("确认密码不能为空!");
		return false;
	}
	if (pwd1 != pwd2) {
		$("#check-msg").html("两次密码输入不一致!");
		return false;
	}
	if (pwd1.length < 6 || pwd1.length > 12) {
		$("#input-pwd").focus();
		$("#check-msg").html("密码长度在6~12位之间!");
		return false;
	}

	/*
	 * if (!reg.test(pwd1)) { $("#input-pwd").focus();
	 * $("#check-msg").html("请输入6~12位数字及英文组合密码"); return false; }
	 * 
	 * if (!reg.test(pwd2)) { $("#input-pwdenter").focus();
	 * $("#check-msg").html("请输入6~12位数字及英文组合密码"); return false; }
	 */
	$("#check-msg").html("");
	return true;
}

function countBtn(obj, time) {// 倒计时
	var ti = time;

	$(obj).attr("disabled", "disabled");
	$(obj).css("background", "#d1d1d1");
	$(obj).css("border-color", "#d1d1d1");
	$(obj).css("font-color", "#aaaaaa");
	var timer = $(obj);
	var cleart = setInterval(function() {
		ti--;
		timer.val(ti + "秒");
		if (ti == 0) {
			ti = time;
			clearInterval(cleart);
			timer.val("重新获取")
			timer.removeAttr("disabled");
			timer.removeAttr("style");

		}
	}, 1000);
};

// 获取用户的短信条数和消息条数
function getRealTimeOrSmsNum() {
	$.post(path + "/smsUser/getSmsUserMsgOrSmsNum.do", function(data) {
		var surplusNum = data["data"].surplusNum;
		var msg = data["data"].msgCount;
		$(".utime").html(surplusNum);
		$(".newsnum").html(msg);
	});
}
