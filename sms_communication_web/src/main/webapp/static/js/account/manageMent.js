var path = $("#server_path").val();
var array; //要转移用户id
var delIds; //要删除用户id
var sysUserIds; //要删除用户对应的系统表id(sys_user_id)
$(function () {
    load_data();  //加载列表数据

    loadAisleGroup(); //加载通道组

    hideColumn();  //隐藏列

    //保存归属转移
    $("#save_data_btn").bind("click", function () {
        var bid = $("#zy_bid").val();
        $.ajax({
            url: path + "/management/apply_edit/formEdit.do",
            type: "POST",
            dateType: "json",
            data: {"ids": array, "bid": bid},
            success: function (data) {
                if (checkRes(data)) {
                    $("#zy_div").modal("hide");
                    refresh_data();
                }
            },
            error: function () {
            },
            complete: function () {
            }
        });
    });

    //确认删除
    $("#btn_del").on('click', function () {
        $.ajax({
            url: path + "/management/apply/delete.do",
            type: "POST",
            dataType: "json",
            data: {"ids": delIds, "sysUserIds": sysUserIds},
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    refresh_data();
                }
            },
            error: function () {
            },
            complete: function () {
            }
        });
    });
});

//新建按钮
function addManage() {
    window.location.href = path + "/management/operate.do?menuId=" + $("#menu_id").val();
}

//查询 按钮
function search() {
    refresh_data();
    if ($("div.pagination").is(":visible")) {
        $(".minlid").show();
    } else {
        $(".minlid").hide();
    }
};
//另建按钮
$("#ljBtn").on('click', function () {
    btnTypeEvent("copy");
});
//设置账号按钮(修改)
$("#szBtn").on('click', function () {
    btnTypeEvent("update");
});
/**
 * 控制列的隐藏
 */
function hideColumn() {
    $('#tb_data').bootstrapTable('hideColumn', 'aisleGroup');
    //  $('#tb_data').bootstrapTable('hideColumn', 'networkChargingState');
    $('#tb_data').bootstrapTable('hideColumn', 'roleName');
    $('#tb_data').bootstrapTable('hideColumn', 'nickName');
}
/**
 * 加载通道组
 */
function loadAisleGroup() {
    $.post(path + "/smsUser/queryAisleGroup.do", function (data) {
        var listData = data["data"];
        $("#i_aisleType").empty();
        $("#i_aisleType").append("<option value=''>全部</option>");
        for (var i=0;i<listData.length;i++) {
            // select添加option属性
            var key = listData[i].id;
            var data = listData[i].name;
            $("#i_aisleType").append("<option value=" + key + " >" + data + "</option>");
        }
    });
}
//导出
function exportData() {
    var a = $('#tb_data').bootstrapTable('getSelections');
    var ids = new Array();
    if (a.length > 0) {
        for (var i = 0; i < a.length; i++)
            ids.push(a[i].id);
    }
    var datas = {
        ids: ids,
        email: $("#email").val(), //账号信息
        name: $("#name").val(), //用户名
        contact: $("#contact").val(), //联系人
        phone: $("#phone").val(), //手机号
        aisleType: $("#i_aisleType").val(), //通道类型
        networkChargingState: $("#networkChargingState").val(), //网上充值状态
        userType: $("#i_userType").val(), //用户类型
        nickName: $("#nickName").val() //归属
    }
    datas = JSON.stringify(datas),
        $.get(path + "/management/querySmsManagementData.do?datas=" + datas, function (data) {
            if (checkRes(data)) {
                window.open(
                    path + '/management/export.do',
                    'height=100,width=400,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'
                );
            } else {
                alert("没有可以导出的用户数据");
            }
        });
}

//刷新数据
function refresh_data() {
    $('#tb_data').bootstrapTable('destroy');
    load_data();
}

function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        email: $("#email").val(), //账号信息
        name: $("#name").val(), //用户名
        contact: $("#contact").val(), //联系人
        phone: $("#phone").val(), //手机号
        aisleType: $("#i_aisleType").val(), //通道类型
        networkChargingState: $("#networkChargingState").val(), //网上充值状态
        userType: $("#i_userType").val(), //用户类型
        nickName: $("#nickName").val() //归属

    };
    return temp;
}
//列表加载
function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'managementList.do?menuId=' + $("#menu_id").val(),
        dataType: "json",
        cache: false,
        striped: true,
        pageSize: 50,
        pageList: [50],
        pagination: true,
        paginationPreText: "上一页",
        paginationNextText: "下一页",
        paginationHAlign: "left",
        clickToSelect: true,
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            if (totalRows > 50) {
                $(".minlid").show();
            } else {
                $(".minlid").hide();

            }
            return '总共 ' + totalRows + ' 条记录';
        },
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: [
            {
                field: "选择",
                checkbox: true,
                align: "center",
                valign: "middle",
                width: 30
            },
            {
                title: "账号",
                field: "email",
                align: "center",
                valign: "middle",
                width: 100
            },
            {
                title: "用户名称",
                field: "name",
                align: "center",
                valign: "middle"
            },
            {
                title: "联系人",
                field: "contact",
                align: "center",
                valign: "middle",
                width: 70
            },
            {
                title: "手机号",
                field: "phone",
                align: "center",
                width: 120,
                valign: "middle",
                width: 100
            },
            {
                title: "注册日期",
                field: "createTime",
                align: "center",
                valign: "middle",
                width: 80,
                formatter: function (value, row) {
                    if (!row.createTime) {
                        return "";
                    }
                    return new Date(row.createTime).Format("yyyy-MM-dd");
                }
            }, {
                title: "短信签名",
                field: "signature",
                align: "left",
                valign: "middle"
            },
            {
                title: "通道类型",
                field: "aisleGroup",
                align: "center",
                valign: "middle",
                width: 150
            },
            {
                title: "网充状态",
                field: "networkChargingState",
                align: "center",
                valign: "middle",
                width: 45,
                formatter: function (value, row) {
                    if (!row.networkChargingState || row.networkChargingState == 0) {
                        return "开通";
                    } else if (row.networkChargingState == 1) {
                        var close = "<span style='color:red'>关闭</span>";
                        return close;
                    }
                }
            },
            {
                title: "用户类型",
                field: "roleName",
                align: "center",
                valign: "middle",
                width: 80
            },
            {
                title: "账号归属",
                field: "nickName",
                align: "left",
                valign: "middle",
                width: 100
            }],
        responseHandler: function (a) {
            hideColumn();
            var btn_arr = a["data"];
            var html_content = "";
            html_content = "<input class='f-btn' type='button' value='查询' onclick='search();'>";
            var n = 0;
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/management/';
                for (var k = 0; k < btn_arr.length; k++) {
                    var btn_obj = btn_arr[k];
                    if (btn_obj["actionUrls"] == user_path + 'from/merge.do') {
                        if (n == 0) {
                            html_content += "<input class='f-btn' type='button' value='新建' onclick='addManage();'>";
                            html_content += "<input class='f-btn' type='button' value='另建' onclick='btnTypeEvent(this);'>";
                            html_content += "<input class='f-btn' type='button' value='设置' onclick='btnTypeEvent(this);'>";
                            n++;
                        }
                    } else if (btn_obj["actionUrls"] == user_path + 'apply_edit/formEdit.do') {
                        html_content += "<input class='f-btn' type='button' value='转移' onclick='zy();'>";
                    } else if (btn_obj["actionUrls"] == user_path + 'apply/delete.do') {
                        html_content += "<input class='f-btn' type='button' value='删除' onclick='del();'>";
                    } else if (btn_obj["actionUrls"] == user_path + 'querySmsManagementData.do') {
                        html_content += "<input class='f-btn' type='button' value='导出' onclick='exportData();'>";
                    } else if (btn_obj["actionUrls"] == user_path + 'aisleGroup.do') {
                        $('#tb_data').bootstrapTable('showColumn', 'aisleGroup');
                    } else if (btn_obj["actionUrls"] == user_path + 'roleName.do') {
                        $('#tb_data').bootstrapTable('showColumn', 'roleName');
                    } else if (btn_obj["actionUrls"] == user_path + 'nickName.do') {
                        $('#tb_data').bootstrapTable('showColumn', 'nickName');
                    } else if (btn_obj["actionUrls"] == user_path + 'td.do') {
                        $('#tdDiv').show();
                    } else if (btn_obj["actionUrls"] == user_path + 'user.do') {
                        $('#userDiv').show();
                    } else if (btn_obj["actionUrls"] == user_path + 'gs.do') {
                        $('#gsDiv').show();
                    }
                }
            }
            $("#btnMessage").html(html_content);
            return a;
        },
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
    if ($("div.pagination").is(":visible")) {
        $(".minlid").show();

    } else {
        $(".minlid").hide();
    }
}

//另建（复制用户信息 然后保存）
function btnTypeEvent(type) {
    var value = type.value;
    var temp;
    if (value == "另建") {
        temp = "copy";
    }
    if (value == "设置") {
        temp = "update";
    }
    var a = $('#tb_data').bootstrapTable('getSelections');
    var id = 0;
    var roleId;
    if (a.length == 1) {
        id = a[0].id;
        roleId = a[0].roleId;
    } else {
        alert("请选中一行")
        return;
    }
    $(".fileinput-remove-button").trigger("click");
    window.location.href = path + "/management/operate.do?menuId=" + $("#menu_id").val() + "&id=" + id + "&btnType=" + temp + "&roleId=" + roleId;
}

//删除
function del() {
    sysUserIds = new Array();
    delIds = new Array();
    var a = $('#tb_data').bootstrapTable('getSelections');
    if (a.length > 0) {
        for (var i = 0; i < a.length; i++) {
            sysUserIds.push(a[i].sysUserId);
            delIds.push(a[i].id);
        }
        if (sysUserIds.indexOf(1) > -1) {
            alert("超级管理员不能删除！");
        } else {
            $("#del").modal("show");
        }
    } else {
        alert("请选择要删除的数据");
        return;
    }
}
/**
 * 转移
 */
function zy() {
    array = new Array(); //保存要转移用户id
    var zyIds = new Array(); //保存选择的用户级别userLevel
    $("#zy_bid").val("请选择");
    var a = $('#tb_data').bootstrapTable('getSelections');

    for (var i = 0; i < a.length; i++) {
        zyIds.push(a[i].userLevel);
        array.push(a[i].id);
    }
    var roleId = a[0].roleId; //要转移用户较色roleId
    if (zyIds.length > 0) {
        if (zyIds.length > 1) {
            if (!checkLevel(zyIds)) {
                alert("不同级别的用戶之间不能同时转移");
                return;
            } else if (zyIds.indexOf(1) > -1) {
                alert("1级用户一次只能转移一条");
                return;
            } else if (zyIds.indexOf(2) > -1) {
                alert("2级用户一次只能转移一条");
                return;
            } else {
                $("#userType").text("");
                $('#zy_div').modal("show");
            }
        } else {
            $('#zy_div').modal("show");
            $("#userType").text(a[0].nickName);
        }
    } else {
        alert("请选择转移用户");
        return;
    }

    //如果同时多条转移 隐藏原归属
    if (array.length > 1) {
        $("#ygsDiv").hide();
    } else {
        $("#ygsDiv").show();
    }

    //根据y要转移的用户级别查询用户归属
    if (zyIds[0] == 3) {
        //三级用户（归属---一二级用户）
        queryGSlevel(60); //60 三级用户角色roleId
    } else if (zyIds[0] == 2) {
        //二级用户（归属---一二级用户）
        queryGSlevel(51); //51 二级用户角色roleId（51，52，59 都可以）
    } else {
        //一级用户
        if (roleId == 3 || roleId == 58) {
            //要转移的用户是“超级管理员”和“客服”
            queryGSlevel(roleId); //如果选择的是“超级管理员”和“客服” 归属查询 "超级管理员" 用户
        } else {
            queryGSlevel(roleId); //如果选择的是"一级管理员" 查询“超级管理员”和“客服”
        }
    }
}
//查询归属
function queryGSlevel(level) {
    $("#zy_bid").empty();
    ajaxCall({
        url: "/management/queryUserByLevel.do?level=" + level,
        type: "post",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]
                var bid = $("#zy_bid");
                var htl = "<option value=\"\">请选择</option>";
                for (var i = 0; i < obj.length; i++) {
                    var ss = obj[i];
                    htl += "<option value=\"" + ss["sysUserId"] + "\">" + ss["email"] + "</option>";
                }
                bid.html(htl);
            }
        },
        error: function () {
            alert("操作失败");
        }
    });
}

//判断所选择的用户是否包含不同级别
var checkLevel = function (zyIds) {
    var a = 0;
    var b = 0;
    var c = 0;
    for (var i in zyIds) {
        if (zyIds[i] == 1) {
            a += 1;
        } else if (zyIds[i] == 2) {
            b += 1;
        } else if (zyIds[i] == 3) {
            c += 1;
        }
    }
    if (a >= 1 && (b >= 1 || c >= 1)) {
        return false;
    } else if (b >= 1 && c >= 1) {
        return false;
    } else {
        return true;
    }
}