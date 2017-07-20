$(function(){
	selebox();
})
function isEmai(arr){
	var reg=/^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/;
	return reg.test(arr);
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
$(":input[type='password']").on("keydown", onlyNumZ);
function onlyNumZ() {// 正则限制输入字母和数字
	var reg = /^[0-9a-zA-Z]*$/g;
	if (!reg.test(event.key)) {
		event.returnValue = false;
	}

}
function stopPropagation(e) {
	if (e.stopPropagation) {
		e.stopPropagation();
	} else {
		e.cancelBubble = true;
	}
}
function selebox() { // 选择框
	$(".xlbtn").on("click",function(e) {
				stopPropagation(e);
				$(".xlbox").hide();
				if (!$(this).siblings(".xlbox").is(":animated")) {
					$(this).siblings(".xlbox").is(":hidden") ? $(this)
							.siblings(".xlbox").show() : $(this).parent(".xlbox").hide();
				}

    });
}

$(document).on('click', function() {
	$(".xlbox").hide();
	});
function onlyNum() {// 输入数字类型
	if (!((event.keyCode >= 48 && event.keyCode <= 57)
			|| (event.keyCode >= 96 && event.keyCode <= 105) || (event.keyCode == 8)))
		event.returnValue = false;
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
}

