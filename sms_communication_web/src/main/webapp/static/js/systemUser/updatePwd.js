$(function(){
	$("#update_pwd_btn").unbind("click");
	$("#update_pwd_btn").bind("click", function() {
		$("#update_pwd_btn").attr("disabled","disabled");
		if (checkPass()) {
			updatePassword();
		}
	});
});

function checkPass(){
	var pwd1=$("#newPwd").val();
	var pwd2=$("#newPwd2").val();
	if(pwd1!=pwd2){
		alert("两次密码输入不一致！");
	    return false;
	}else{
		return true;
	}
}

function updatePassword(){
    var oldPwd = $("#oldPwd").val();
    var newPwd = $("#newPwd").val();
    var id = $("#update_pwd_id").val();
    var canCommit = true;
    if (!id) {
        canCommit = false;
    }
    if (!oldPwd) {
        canCommit = false;
    }
    if (!newPwd) {
        canCommit = false;
    }
    if (!canCommit) {
    	alert("密码不能为空，请重新输入！");
        return;
    }
    var data = {
        "oldPwd": oldPwd,
        "newPwd": newPwd,
        "id": id
    };
    ajaxCall({
        url: "/updatePwd.do",
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
            	alert("修改成功，请重新登录！");
            	top.location = "logout.do";
            }else {
            	alert("修改失败，请检查原密码是否正确！");
            }
        },
        error: function () {
        	alert("修改失败，请检查原密码是否正确！");
        },
        complete: function () {
        	$("#update_pwd_btn").removeAttr("disabled");
            //$btn.text("确 认");
        }
    });

}