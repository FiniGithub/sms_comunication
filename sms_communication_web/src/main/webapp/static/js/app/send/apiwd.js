var path = $("#path").val();
$(function() {
	getRealTimeOrSmsNum();
	showOrHideBtn();// 判断显示填写/修改按钮
	copy_text();// 复制文本
	online_read();// 打开文档
	down_wd();// 下载文档
	margeApi();// 修改推送地址
	getcode_next();// 重置key,获取验证码
	getkey_next();// 重置key,下一步
	
	gaincode_next();// 获取key,获取验证码
	gainKey_next();// 获取key,下一步
});

// 显示按钮文字
function showOrHideBtn(){
	var s = $(".sxdz").html();
	var x = $(".xzdz").html();
	if( (s == null || s == '') || (x == null || x == '') || s == '未填写'){
		$("#upd_btn").val("填写");
	}else{
		$("#upd_btn").val("修改");
	}
}

// 复制文本 
function copy_text(){
	$("#copy_btn").click(function(){
		var Url2=document.getElementById("input-key"); 
		Url2.select(); // 选择对象 
		document.execCommand("Copy"); // 执行浏览器复制命令 
		alert("已复制，可贴粘!"); 
	});
}

// 修改、新增推送地址
function margeApi() {
	$("#commit_apiurl").click(function() {
		var id = $(".qyid").html();// 用户id
		var replyUrl = $("#replyUrl").val();// 上行推送地址
		var reportUrl = $("#reportUrl").val();// 报告推送地址
		if (replyUrl == null || replyUrl == '') {
			alert("请填写上行推送地址!");
			return;
		}
		if (reportUrl == null || reportUrl == '') {
			alert("请填写报告推送地址!");
			return;
		}
		var data = {
			"id" : id,
			"replyUrl" : replyUrl,
			"reportUrl" : reportUrl
		};
		$.ajax({
			url : path + "/userMessage/from/userMerge.do",
			type : "get",
			data : data,
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				if (checkRes(data)) {
					alert("地址修改成功！");
					$(".sxdz").html(replyUrl);
					$(".xzdz").html(reportUrl);
					$(".tsdz").hide();
					$(".regiger-bg").hide();

				} else {
					alert("操作失败");
				}
			},
			error : function() {
				alert("操作失败");
			},
			complete : function() {
			}
		});
	});
}



// 重置key,获取验证码
function getcode_next() {
	$("#getcode_reset").click(function() {
		var phone = $(".session_phone").html();
		getCode(this,phone);
	});
}
// 获取key,获取验证码
function gaincode_next(){
	$("#gaincode_next").click(function() {
		var phone = $(".session_phone").html();
		getCode(this,phone);
	});
}



// 重置,下一步
function getkey_next(){
	$("#getkey_next").click(function(){
		var code = $("#reset_code").val();
		if(code == null || code == ''){
			alert("请输入验证码!");
			return;
		}
		var phone = $(".session_phone").html();
		var data = {"verifyCode":code,"phone":phone,"type":"1"};
		if($("#reset_checkbox").is(':checked')){
			go_next(data);// 下一步	
		}else{
			alert("请勾选重置后原key将失效!");
		}
	});
}

// 获取key,下一步
function gainKey_next(){
	$("#gion_next").click(function(){
		var code = $("#getkey-code").val();
		if(code == null || code == ''){
			alert("请输入验证码!");
			return;
		}
		var phone = $(".session_phone").html();
		var data = {"verifyCode":code,"phone":phone,"type":"0"};
		go_next(data);// 下一步	
	});
}


// 获取验证码
function getCode(obj,phone) {
	var timer = $(obj);

	var data = {
		"phone" : phone
	};
	console.log(data);
	var ti = 60;
	$.post(path + "/smsUser/getVertifyCode.do", data, function(data) {
		console.log(data);
		if(data["retCode"] == "000000"){
			$(obj).attr("disabled", "disabled");
			$(obj).css("background","#d1d1d1");
			$(obj).css("border-color","#d1d1d1");
			$(obj).css("font-color","#aaaaaa");
			
			var cleart = setInterval(function() {
				ti--;
				$(timer).val(ti+"秒");
				if (ti == 0) {
					ti = 60;
					clearInterval(cleart);
					timer.val("重新获取")
					timer.removeAttr("disabled");
					timer.removeAttr("style");
				}
			}, 1000);
		}else{
			var msg = data["data"];
			alert(msg);
		}
	});
}

// 下一步
function go_next(data){
	$.post(path + "/smsUserApiConfig/resetKey.do", data, function(data) {
		console.log(data);
		var obj = data["data"];
		if(data["retCode"] == "000000") {
			$(".hqkeytwo").show();// 显示key
			$("#getkey-code").val("");
			$("#reset_code").val("");
			$("#reset_checkbox").attr("checked",false);
			
			var key = data["data"];
			$("#input-key").val(key);
			getkey(key);
		} else {
			var msg = obj;
			alert(msg);
		}
	});
}

function getkey(key){
	var n = 10;
	var k = key.substring(0,key.length - n);
	var x = '';
	for(var i = 0;i<n;i++){
		x +='*';
	}
	var j = k + x;
	$("#span_key").html(j);
}

// 打开文档
function online_read(){
	$("#online_read").click(function() {
		window.open(path + "/smsUserApiConfig/apiDetail.do");
	});
}



// 下载文档、sdk
function down_wd(){
	// 下载sdk
	$("#java_sdk").click(function(){
		window.location.href=path + '/static/sdk/qxxt.zip';
	});
	
	// 下载文档
	$("#down_wd").click(function(){
		window.location.href='http://c.dzd.com/plugin_path//calim/api_manual.docx';
	});
}