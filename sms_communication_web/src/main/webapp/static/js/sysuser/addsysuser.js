$(function () {
    //展开菜单
    $("li[key='/sysUser/listview.do']").parent().parent().addClass("active");

    var $btn = $('#btn_sub');
    //提交按钮
    $("#btn_sub").bind("click", function () {
        var id = $("#id").val();
        var email = $("#email").val();
        var nickName = $("#nickName").val();
        var canCommit = true;
        if (!email) {
            //$("#lb_err_name").text("员工名称不能为空");
            //alert(1);
            canCommit = false;
        }
        if (!nickName) {
            //$("#lb_err_posts").text("岗位不能为空");
            //alert(2);
            canCommit = false;
        }
        if (!canCommit) {
            return;
        }

        var data = {
            "email": email,
            "nickName": nickName
        };

        if (id)
            data["id"] = id;
        $btn.attr("disabled", "disabled");
        $btn.text("正在提交...");

        ajaxCall({
            url: "/sysUser/from/merge.do",
            type: "post",
            data: JSON.stringify(data),
            success: function (data) {
                if (checkRes(data)) {
                    window.location.href = SERVER_PATH + "/sysUser/listview.do";
                } else {
                    $(".ibox-content").prepend(alertview("danger", "操作失败."));
                }
            },
            error: function () {
                $(".ibox-content").prepend(alertview("danger", "操作失败."));
            },
            complete: function () {
                $btn.removeAttr("disabled");
                $btn.text("确 认");
            }
        });
    });
});



