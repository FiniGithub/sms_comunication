// var ChineseDistricts_city = {};
$(function () {
    $("#save_pwd_but").bind("click", function () {
    	editData();
    });
    
    //给所有A标签绑定点击触发事件
    $('#logout_form').click(function() {
    	window.location.href="/sms-platform/logout.do";
    });
   
});

function editData() {
	var i_pwd = $("#i_pwd").val();
	var i_pwd1 = $("#i_pwd1").val();
	var i_pwd2 = $("#i_pwd2").val();
	var i_email = $("#i_email").val();
	if(i_pwd1.length<6){
		alert("新密码不能少于6位！")
		return;
	}
	if(i_pwd==null || i_pwd=="" ){
		alert("请填写当前密码！");
		return;
	}
	if(i_pwd1==null || i_pwd1=="" ){
		alert("请填写新密码！");
		return;
	}
	if(i_pwd2==null || i_pwd2=="" ){
		alert("请填写确认密码！");
		return;
	}
	if(i_pwd1!=i_pwd2){
		alert("确认密码和新密码不相同,请重新填写！");
		return;
	}
        var param = {"oldPwd": i_pwd,"newPwd": i_pwd1};
        ajaxCall({
            url: "/userMessage/from/merge.do",
            type: "get",
            data: param,
            success: function (data) {
            	if (checkRes(data)) {
                    alert("密码修改成功,请重新登录！");
                    //触发所有A标签的点击事件
                    $('#logout_form').click();
                } else {
                	alert("操作失败");
                }
            },
            error: function () {
            	alert("操作失败");
            },
            complete: function () {
            }
        });
}

