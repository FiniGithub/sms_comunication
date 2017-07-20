$(function () {
    $("#update_pwd").bind("click", function () {
        $("#oldPwd").val("");
        $("#newPwd").val("");
        $('#update_pwd_div').modal("show");
    });

    $("#update_pwd_btn").bind("click", function () {
        update_pwd_save();
    });
});


//新增
function update_pwd_save() {
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
                $('#update_pwd_div').modal("hide");
            } else {
                $(".ibox-content").prepend(alertview("danger", "操作失败."));
            }
        },
        error: function () {
            $(".ibox-content").prepend(alertview("danger", "操作失败."));
        },
        complete: function () {
            //$btn.removeAttr("disabled");
            //$btn.text("确 认");
        }
    });
}