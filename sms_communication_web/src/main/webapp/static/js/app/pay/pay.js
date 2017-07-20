/**
 * Created by Administrator on 2017/5/23.
 */
var path = $("#path").val();
$(function () {
    getUserRechargeMoney();// 获取用户对应的价格表
    sel_recharge_sms_num();// 选择条数，计算出对应的价格
});


/**
 * 按钮获取充值用户的价格段位
 */
function btnChangeUser() {
    var account = $("#rechargeAccount").val();
    if (account == null || account == "") {
        account = $("#pay_account").html();
    }
    if (account.trim() == null || account.trim() == "") {
        return;
    }
    getUserRechargeMoney(account.trim());
    $("#money").val("0");
}

/**
 * 选择条数,计算出对应的价格
 */
function sel_recharge_sms_num() {
    $("#recharge_users").change(function () {
        var smsNum = $(this).val();
        var uid = $("#recharge_user").val();
        var data = {"smsNum": smsNum, "uid": uid};
        $.post(path + "/smsUserPay/getMoneyBySmsNum.do", data, function (data) {
            var money = data["data"];
            $("#money").val(money);
        });
    });
}


/**
 * 获取用户对应的充值价格表
 * @param uid
 */
function getUserRechargeMoney(account) {
    $.post(path + "/smsUserPay/getSmsUserRechargeMoney.do", {"account": account}, function (data) {
        var obj = data["data"];
        if (obj != null) {
            $("#smsNumber_div").show();
            $("#sp_money_section").show();
            $("#sp_money_none").hide();

            var searchAccount = $("#rechargeAccount").val();
            if (searchAccount != null && searchAccount != "") {
                $("#pay_account").html(searchAccount);
                $("#rechargeAccount").val("");
            }


            var onePrice = obj.oneIntervalPrice;
            var oneStart = obj.oneIntervalStart;
            var oneEnd = obj.oneIntervalEnd;
            var twoPrice = obj.twoIntervalPrice;
            var twoStart = obj.twoIntervalStart;
            var twoEnd = obj.twoIntervalEnd;
            var threePrice = obj.threeIntervalPrice;
            var threeStart = obj.threeIntervalStart;
            var threeEnd = obj.threeIntervalEnd;

            $("#ontPrice").html(onePrice);
            $("#oneStart").html(oneStart);
            $("#oneEnd").html(oneEnd);

            $("#twoPrice").html(twoPrice);
            $("#twoStart").html(twoStart);
            $("#twoEnd").html(twoEnd);

            $("#threePrice").html(threePrice);
            $("#threeStart").html(threeStart);
            $("#threeEnd").html(threeEnd);
        } else {
            $("#pay_account").html(account);
            $("#smsNumber_div").hide();
            $("#sp_money_section").hide();
            $("#sp_money_none").show();

            $("#ontPrice").html("");
            $("#oneStart").html("");
            $("#oneEnd").html("");

            $("#twoPrice").html("");
            $("#twoStart").html("");
            $("#twoEnd").html("");

            $("#threePrice").html("");
            $("#threeStart").html("");
            $("#threeEnd").html("");
        }

    });
}


/**
 * 提交订单
 */
function saveOrder() {
    var smsNum = $("#recharge_users").val();// 短信条数
    var money = $("#money").val();// 价格
    var smsUserId = $("#recharge_user").val();// 充值的用户
    if (smsNum == null || smsNum == "0" || money == null || money == "0") {
        alert("请选择充值条数!");
        return;
    }

    if ($('input[name=bankId]').is(':checked')) {
    } else {
        alert("请选择银行!");
        return;
    }
    if (window.confirm("确定进行充值？")) {
        $("#payForm").submit();
    }
}