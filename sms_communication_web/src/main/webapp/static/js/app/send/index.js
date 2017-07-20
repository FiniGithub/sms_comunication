var path = $("#path").val();
$(function() {
	$("#smsNum").focus();
	loadTodayRecord();// 统计今日消费
	send_target();// 跳转群发短信
	send_detail();// 跳转发送任务
	send_statistical();// 跳转发送统计
	send_bill();// 跳转发送流水
	getGroupTypeState();// 获取用户账户类型
	updateAliseGroup();// 选择通道组
	// loadIndexMsg();// 查询推送到首页的消息
	gotoPay();// 跳转到支付界面
	getSmsMoneyInput();// 获取短信条数价格
	$(".sc-ul li").on("mouseover", function() {
		var li_index = $(this).index() + 1;
		$("#atypeId").val(li_index);
	});
});

/**
 * 查询推送到首页的消息
 */
function loadIndexMsg() {
	$.post(path + "/smsUser/queryMsgForIndex.do", function(data) {
		var obj = data["data"];
		var id = obj["id"];
		var title = obj["title"];
		var content = obj["content"];
		alert("标题:" + title + ",内容:" + content);
		updateMsgState(id);
	});
}

// 更改消息状态为已读
function updateMsgState(id) {
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
				alert("成功,隐藏弹窗!");
			}
		},
		error : function() {
			alert("错误");
		},
		complete : function() {

		}
	});
}

/**
 * 今日消费统计
 */
function loadTodayRecord() {
	$(".yytime").html(0);
	$(".kytime").html(0);
	var url = path + "/smsUser/today.do";
	$.post(url, function(data) {
		var obj = data["data"];
		var todayCount = obj["todayCount"];// 今日消费短信条数
		var surplusNum = obj["surplusNum"];// 剩余短信条数
		$(".yytime").html(todayCount);
		$(".kytime").html(surplusNum);
	});
}

/**
 * 获取账户类型,没有类型,则让用户选择类型
 */
function getGroupTypeState() {
	$.post(path + "/smsUser/getGroupTypeState.do", function(data) {
		var obj = data["data"];
		if (obj == null || obj == '') {
			fixMidel(".sctc");
			$(".alwindow-box").hide();
			$(".regiger-bg").show();
			$(".sctc").show();
		}
	});
}

/**
 * 点击确定，用户增加通道组
 */
function updateAliseGroup() {
	$("#enter-type").on("click", function() {
		var tid = $("#atypeId").val();
		var data = {
			"tid" : tid
		};
		$.ajax({
			url : path + "/smsUser/updateGroupType.do",
			type : "post",
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				if (checkRes(data)) {
					var count = data["data"];
					$(".kytime").html(count);
					$(".utime").html(count);

					$(".sctc").hide();
					fixMidel(".zccg");
					$(".zccg").show();
				}
			},
			error : function() {
				alert("错误");
			},
			complete : function() {

			}
		});

	})

}

/**
 * 群发短信
 */
function send_target() {
	$(".send").click(function() {
		var path = $("#path").val();
		window.location.href = path + "/smsUser/sendView.do";
	});
}

/**
 * 发送任务
 */
function send_detail() {
	$(".sendDetail").click(function() {
		var path = $("#path").val();
		window.location.href = path + "/smsUser/sendListview.do";
	});
}

/**
 * 发送统计
 */
function send_statistical() {
	$(".sendStatistical").click(function() {
		var path = $("#path").val();
		window.location.href = path + "/smsUser/statisticalList.do";
	});
}

/**
 * 发送账单流水
 * 
 * @returns
 */
function send_bill() {
	$(".puserBill").click(function() {
		var path = $("#path").val();
		window.location.href = path + "/smsUser/puserBill.do";
	});

}

// 获取短信条数价格
function getSmsMoneyInput() {
	$('#smsNum').bind('input propertychange', function() {
		var smsNum = $("#smsNum").val();
		var data = {
			"smsNum" : smsNum
		};
		getMoney(data);
	});
}

// 点击购买短信条数
function btn_addSmsNum(num) {
	var smsnum = num + '0000';
	var data = {
		"smsNum" : smsnum
	};
	$("#smsNum").val(smsnum);
	getMoney(data);
}
// 得到短信条数的金额
function getMoney(data) {
	$.post(path + "/smsUser/calculateMoney.do", data, function(data) {
		var obj = data["data"];
		var code = obj["code"];
		var money = obj["money"];
		if (code == 0) {// 返回金额
			$("#smsMoney").html(money);

		} else if (code == 1) {// 短信条数为空
			$("#smsMoney").html(money);
		}
	});
}

// 跳转到支付界面
function gotoPay() {
	$("#pay_index").click(function() {
		var path = $("#path").val();
		var money = $("#smsMoney").html();
		var smsNum = $("#smsNum").val();
		if (money == '0' || smsNum == null || smsNum == '') {
			return;
		}
		window.open(path + "/smsUserPay/pay_index.do?money=" + money);
		$("#smsMoney").html("0");
		$("#smsNum").val("");
	});
}