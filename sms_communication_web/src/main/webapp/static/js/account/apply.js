var path = $("#server_path").val();
$(function () {
    var i_id = $("#i_id").val();
    console.log(i_id)
    if (i_id == "") {
        $(".counttitle").text("申请账户")

    } else {
        $(".counttitle").text("申请账户修改")
    }

    rebind();//防止重复
    //取消按钮
    $("#cancel_data_btn").bind("click", function () {
        window.location.href = path + "/applyaccount/listview.do?id=" + $("#menuId").val();
    });
});
function rebind() {//防止重复
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
//申请账户提交
function saveif() {
    var username = $("#name").val();  //名称
    var id = $("#i_id").val();
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
    }
    if (!isPhone($("#phone").val())) {
        alert("手机号码输入有误或格式不正确");
        $("#phone").focus();
        return;
    }
    var data = {
        "name": username,
        "id": id
    };
    $.ajax({
        url: path + "/applyaccount/querySmsUserbyname.do",
        dataType: "json",
        type: "post",
        data: data,
        success: function (data) {
            if (checkRes(data)) {
                $("#add_form").submit();
            } else if (data["retCode"] == "000001") {
                alert("用户名已经存在，请重新输入");
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
};
function isEmail(arr) {
    var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/;
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
















