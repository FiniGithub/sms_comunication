var path = $("#server_path").val();
var btnType = $("#btnType").val();
function rebind() {
    $("#save_data_btn").bind("click", function () {
        $("this").attr("disabled", "disabled");
        Button_Click(saveif, 200);
    });//提交   

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
function saveif() {  //保存信息

    var id = $("#i_id").val();
    if (btnType == "copy") {
        //另建---复制信息
        id = "";
    }
    var t = 0;
    $(".imp").each(function () {
        if ($(this).val() == '') {
            alert("请选择或输入必填选项");
            $(this).focus();
            t = 1;
            return false;
        }
    });
    if (t == 1) {
        return;
    }
    var email = $("#email").val();
    var name = $("#name").val();
    if (btnType != "update") {
        var pwd1 = $("#pwd").val();// 第一次密码
        var pwd2 = $("#pwdOk").val();// 第二次密码
        var reg = /^[0-9a-zA-Z]*$/g;
        var msg = '';

        if (pwd1 == null || pwd1 == '') {
            $("#pwd").focus();
            alert("新密码不能为空!");
            return false;
        }
        if (pwd2 == null || pwd2 == '') {
            $("#pwdOk").focus();
            alert("确认密码不能为空!");
            return false;
        }
        if (pwd1 != pwd2) {
            alert("两次密码输入不一致!");
            return false;
        }
        if (pwd1.length < 6 || pwd1.length > 12) {
            $("#pwd").focus();
            alert("密码长度在6~12位之间!");
            return false;
        }

    }

    if (!isPhone($("#phone").val())) {
        alert("手机号码输入有误或格式不正确");
        $("#phone").focus();
        return;
    }
    if ($("#signature_type1").is(":checked")) {
        var arr = $("#signature").val();
        //var sub="【】";
        /*if(isContains(arr,sub)){
         alert("填写签名必须包含签名符号【】");
         $("#signature").focus();
         return;
         }*/
        if (arr == "") {
            alert("填写签名");
            return;
        }
    } else if (!$("#signature_type2").is(":checked")) {
        alert("请选择或输入必填选项");
        $("#signature_type1").focus();
        return;
    }
    var data = {
        "email": email,
        "name": name
    };
    ajaxCall({
        url: "/management/querySysUserbyMsmuser.do?id=" + id,
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
                $("#roleName").attr("disabled", false);
                $("#_form").submit();
            } else if (data["retCode"] == "000001") {
                alert("系统账号已分配，请更换账号名称！");
                return;
            } else if (data["retCode"] == "000002") {
                alert("用户名已经存在，请更换用户名称！");
                return;
            }
        },
        error: function () {
            $(".ibox-content").prepend(alertview("danger", "操作失败."));
        },
        complete: function () {
            $("#save_data_btn").removeAttr("disabled");
        }
    });

}
$(function () {
    //操作按钮类型（新增（add），另建（copy）,设置（update））
    if (btnType == "update") {
        $(".counttitle").text("修改账户")
        $(".uppas").remove();//隐藏密码输入框
    } else if (btnType == "copy") {
        $(".counttitle").text("另建（复制）账户")
    }else{
        loadAisleGroup(); //加载通道
    }
    $("#roleName").val(60); //用户类型默认设置为"用户"

    $("#signature_type2").bind("click", function () {
        $("#signature").val("");
    })
    rebind();
    //用户类型 改变 事件
    $('#roleName').change(function () {
        var level = $("#roleName").val();
        queryGS(level, null);
    })

    //查询用户新
    if ($("#i_id").val() != "") {
        querySmsUserById();
    }

    //取消按钮
    $("#cancel_data_btn").on('click', function () {
        if (btnType == "register") { //如果是"注册账户"
            //跳转到申请账户页面
            window.location.href = "/applyaccount/listview.do?id=" + $("#menu_id").val();
        } else {
            //跳转到账户管理页面
            window.location.href = "/management/listview.do?id=" + $("#menu_id").val();
        }
    });
});

//查询归属
function queryGS(level, sysUserId) {
    $("#bid").empty();
    ajaxCall({
        url: "/management/queryUserByLevel.do?level=" + level,
        type: "post",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]
                var bid = $("#bid");
                var htl = "<option value=\"\">请选择</option>";
                for (var i = 0; i < obj.length; i++) {
                    var ss = obj[i];
                    if (sysUserId != undefined && sysUserId != null && ss["sysUserId"] == sysUserId) {
                        htl += "<option selected=\"selected\"  value=\"" + ss["sysUserId"] + "\">" + ss["email"] + "</option>";
                    } else {
                        htl += "<option  value=\"" + ss["sysUserId"] + "\">" + ss["email"] + "</option>";
                    }
                }
                bid.html(htl);
            }
        },
        error: function () {
            alert("操作失败");
        }
    });

}
//查询sms_usre信息
function querySmsUserById() {
    ajaxCall({
        url: "/management/formEdit.do?id=" + $("#i_id").val(),
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                var btnType = $("#btnType").val();
                if (btnType == "update") {
                    $("#i_id").val(obj["id"]);
                    $("#sysUserId").val(obj["sysUserId"]);
                }
                $("#email").val(obj["email"]); //账号
                if (obj["roleName"] != null) {
                    $("#roleName").val(obj["roleName"]);
                    queryGS($("#roleName").val(), obj["bid"]);
                }
                $("#name").val(obj["name"]);      //用户姓名
                $("#address").val(obj["address"]);
                $("#contact").val(obj["contact"]);
                $("#phone").val(obj["phone"]);
                $("#telphone").val(obj["telphone"]);
                $("#qq").val(obj["qq"]);
                $("#userEmail").val(obj["userEmail"]);
                var signature = obj["signature"];
                if (signature != null) {
                    $("#signature_type1").prop("checked", "checked");
                    $("#signature").val(signature);
                } else {
                    $("#signature_type2").prop("checked", true);
                }

                var network_charging_state = obj["networkChargingState"];
                if (network_charging_state == 0) {
                    $("#network_charging_state1").prop("checked", "checked");
                } else {
                    $("#network_charging_state2").prop("checked", "checked");
                }

                var sms_reply = obj["smsReplyState"];
                if (sms_reply == 0) {
                    $("#sms_reply1").prop("checked", "checked");
                } else {
                    $("#sms_reply2").prop("checked", "checked");
                }

                $("#firmIp").val(obj["firmIp"]);
                var aisleGroupId = obj["aisleGroupId"]; //通道类型
                $("#aisleGroupId").val(aisleGroupId);
                // $("#bid").val(obj["bid"]);//账号归属;
                loadAisleGroup(aisleGroupId);
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}


function isContains(str, substr) {//是否包含 
    return new RegExp(substr).test(str);
}
function isPhone(arr) {
    var reg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/;
    return reg.test(arr);
}
/**
 * 加载通道组
 */
function loadAisleGroup(aisleGroupId) {
    $.post(path + "/smsUser/queryAisleGroup.do", function (data) {
        var listData = data["data"];
        $("#aisleGroupId").empty();
        $("#aisleGroupId").append("<option value=''>请选择</option>");
        for (var i=0;i<listData.length;i++) {
            // select添加option属性
            var key = listData[i].id;
            var data = listData[i].name;
            if(aisleGroupId !=undefined && key==aisleGroupId ){
                $("#aisleGroupId").append("<option selected=\"selected\" value=" + key + " >" + data + "</option>");
            }else{
                $("#aisleGroupId").append("<option value=" + key + " >" + data + "</option>");
            }

        }
    });
}

