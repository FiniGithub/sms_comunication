var path = $("#server_path").val();
$(function () {
    showPage();

    rebind();//按钮防止重复点击


});
function showPage() {//页面切换
    $(".dxtype").hide();
    $(".pagechange a").removeClass();
    ;
    if (window.location.hash == "#consult") {
        $("#content").focus();
        $("#consult").addClass("act");
        $(".consult").show();
    } else if (window.location.hash == "#forgetpsw") {
        $("#phonec").focus();
        $("#forgetpsw").addClass("act");
        $(".forgetpsw").show();
    } else {
        $("#notice").addClass("act");
        $(".notice").show();
    }
    ;
    $(".pagechange a").click(function () {
        var tar = $(this).attr("data-tar");
        $(".pagechange a").removeClass();
        $(".dxtype").hide();
        $("#" + tar).addClass("act")
        $("." + tar).show();
    });


}
/**
 * 咨询确认
 * */
function consultBtn() {
    var t = 0;
    $(".imp").each(function () {
        if ($(this).val() == '') {
            alert("请输入必填选项");
            $(this).focus();
            t = 1;
            return false;
        }
    });
    if (t == 1) {
        return;
    } else {
        var data = {
            email: $("#email").val(),
            content: $("#content").val(),
            contact: $("#contact").val(),
            phone: $("#phone").val(),
            verifyCode: $("#verifyCode").val(),
            imgCode: $("#imgCode").val()
        }
        $("#subBtn").attr("disabled", "disabled");
        $.ajax({
            url: path + "/zixun/insert.do",
            type: "post",
            data: data,
            success: function (response) {
                if (response["retCode"] == "000000") {
                    alert("操作成功！");
                    window.location.reload();
                } else {
                    var msg = response["retMsg"];
                    alert(msg);
                }
            },
            error: function () {
            },
            complete: function () {
                $("#pwd-subBtn").removeAttr("disabled");
            }
        });
    }
}
$(".newcontent").keyup(function () {
    var content = $(".newcontent").val();
    var num = content.length;
    if (num > 300) {
        content = content.substring(0, 300);
        $(".newcontent").val(content);
    }
    $(".dxlength").text(num);

    getSmsSurplusNumber(num);// 剩余输入字数
});

function getSmsSurplusNumber(allLength) {
    var surplusNum = 300 - allLength;
    $(".s-dxlength").html(surplusNum);
}
function getSmsNumber(allLength, content, signLength) {
    // 内容长度超过300，则截取前300位
    if (allLength > 300) {
        content = content.substring(0, 300 - signLength);
        $(".newcontent").val(content);
    }
    allLength = content.length + signLength;
    $(".dxlength").text(allLength);// 短信字数
}


// 获取验证码  全网信通
function getVerCode() {
    var path = $("#server_path").val();

    var phone = $("#phone").val();
    if (phone == "") {
        $("#phone").focus();
        alert("请输入手机号码!");
        return;
    }
    if (!isPhone(phone)) {
        $("#phone").focus();
        alert("请输入正确的手机号码!");
        return;
    }
    $("#pwd-getcode").attr("disabled", "disabled");
    var data = {"phone": phone, "type": 1};

    $.ajax({
        url: path + "/smsVertifyCode/getSmsVertifyCode.do",
        type: "post",
        data: data,
        success: function (response) {
            if (response["retCode"] == "000000") {
                countBtn("#pwd-getcode", 60);// 60s倒计时
            } else if (response["retCode"] == "000002") {
                var msg = response["data"];
                alert(msg);
            } else {
                var msg = response["data"];
                alert(msg);
            }
        },
        error: function () {
        },
        complete: function () {
            $("#pwd-getcode").removeAttr("disabled");
        }
    });
}


/**
 *  验证手机号码
 * @param v
 * @param obj
 * @returns {Boolean}
 */
function checkPhone(v, obj) {
    var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/;
    if (!myreg.test(v)) {
        $(obj).html("请输入正确手机号码!");
        return false;
    }
    $(obj).html("");
    return true;
}

//为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳  
function changeUrl_Reg(url) {
    var timestamp = (new Date()).valueOf();
    var index = url.indexOf("?", url);
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
//修改密码 - 获取验证码
function getVerCodec() {
    var email = $("#emailc").val()// 账号
    var path = $("#server_path").val();
    var phone = $("#phonec").val();

    if (!checkInputMsg(email)) {
        $("#email").focus();
        alert("请输入账号!");
        return;
    }
    if (!checkInputMsg(phone)) {
        $("#phonec").focus();
        alert("请输入手机号码!");
        return;
    }
    if (!checkPhone(phone, "")) {
        $("#phonec").focus();
        alert("手机号码无效!");
        return;
    }
    $("#pwd-getcodec").attr("disabled", "disabled")
    var data = {"phone": phone,"email":email};

    $.ajax({
        url: path + "/smsUser/getVertifyCode.do",
        type: "post",
        data: data,
        success:function(data){
            if (data["retCode"] == "000000") {
                alert("成功获取验证码！请留意手机信箱。");
                countBtn("#pwd-getcodec", 60);// 60s倒计时
            } else if(data["retCode"] == "000001"){
                var msg = data["data"];
                alert(msg);
            }
        },
        error: function () {
        },
        complete: function () {
            $("#pwd-getcodec").removeAttr("disabled");
        }
    });
}
//修改密码 - 确定修改
function getPassword() {
    var isEmpty = false;
    $(".pimp").each(function () {
        if ($(this).val() == '') {
            $(this).focus();
            alert("请输入必填选项");
            isEmpty = true;
            return false;
        }
    });
    if (isEmpty) {
        return;
    }
    var email = $("#emailc").val()// 账号
    var phone = $("#phonec").val();// 号码
    var imgcode = $("#imgCodec").val();// 图文验证码
    var phocode = $("#verifyCodec").val();// 手机验证码
    var reg = /^[0-9a-zA-Z]*$/g;

    if (!checkPhone(phone, "")) {
        alert("手机号码无效!");
        return;
    }
    $("#updatePwdEnter").attr("disabled", "disabled")
    var data = {
        "email": email,
        "phone": phone,
        "phocode": phocode,
        "imgCode": imgcode
    }
    $.ajax({
        url: path + "/smsUser/find/findbackpwd.do",
        type: "post",
        data: data,
        success:function(data){
            var jsonDate = data["data"];
            if (jsonDate["code"] == "0") {
                alert(jsonDate["msg"]);
                $("#zxForm")[0].reset();
                window.location.href = "logout.do";
            } else {
                alert(jsonDate["msg"]);
            }
        },
          error: function () {
        },
        complete: function () {
            $("#updatePwdEnter").removeAttr("disabled");
        }
    });
}
function checkInputMsg(val) {
    if (val == null || val == "") {
        return false;
    }
    return true;
}

// 校验密码
function checkPassword(pwd1, pwd2) {

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
function rebind() {
    $("#pwd-getcode").bind("click", function () {
        Button_Click(getVerCode, 200);
    });//验证按钮防止重复点击
    $("#subBtn").bind("click", function () {
        Button_Click(consultBtn, 200);//提交按钮
    });
    $("#pwd-getcodec").bind("click", function () {
        Button_Click(getVerCodec, 200);
    });//验证按钮防止重复点击

    $("#updatePwdEnter").bind("click", function () {
        Button_Click(getPassword, 200);//提交按钮
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
