var path = $('#server_path').val();
var data_update;
$(function () {
    load_data(); //加载列表
    loadAisleGroup(); //加载通道
    rebind();//防止重复点击
    //查询按钮
    $("#search_btn").bind("click", function () {
        refresh_data();
    });
    //短信充值确认提交弹出框
    $("#cz_allot_btn").bind("click", function () {
        $("#zh").text($("#czname").val()); //账号
        $("#sl").text($("#i_czmoney").val()); //短信数量
        $("#lx").text($("#type option:selected").text()); //充值类型
        $("#je").text($("#money").val()); //充值金额
        var cz = $("#i_czmoney").val();  //充值短信数量
        var symoney = $("#symoney").val(); //剩余短信数量
        var cztype = $("#type").val();
        var mn = $("#money").val();
        if (cz == "" || cz == 0 || cz < 0) {
            alert("充值条数不能为空,且不能为负数！");
            return;
        }
        if (cztype == "") {
            alert("请选择操作类型！");
            return;
        }
        if (cztype == 8 && symoney < cz) {
            alert("当前短信数量不足,请核对核减数量！");
            return;
        }

        $("#je_data_div").modal("hide");
        if (mn == '') {
            $("#je").parent().hide();
        } else {
            $("#je").parent().show();
        }
        $("#sureModel").modal("show");
    });


    $("#btn_cencel").bind("click", function () {
        $("#je_data_div").modal("show");
    });

    //充值类型 下拉框选择事件
    $("#type").change(function () {
        $("#money").val("");
        var type = $("#type").val();
        if (type == 0 && type != "") {
            $("#moneyDiv").show();
        } else {
            $("#moneyDiv").hide();
        }
    });

    //隐藏列表列
    hidemColumn();
});
//短信充值提交
function smsrechargeComfirm() {
    var data = {
        czid: $("#czid").val(),
        sysUserId: $("#sysUserId").val(),
        type: $("#type").val(),
        money: $("#money").val(),
        czmoney: $("#i_czmoney").val()
    }
    $.ajax({
        url: path + "/account/from/moneyMerge.do",
        type: "post",
        data: data,
        success: function (data) {
            if (data["retCode"] == "000000") {
                alert(data["retMsg"]);
                refresh_data();
                $("#sureModel").modal("hide");
            } else if (data["retCode"] == "000001") {
                alert(data["retMsg"]);
                return;
            }
        },
        error: function () {
            $(".ibox-content").prepend(alertview("danger", "操作失败."));
        },
        complete: function () {
            $("#sendsms-btn").removeAttr("disabled");
        }
    });
};
function rebind() {
    $("#btn_ok").bind("click", function () {
        $("this").attr("disabled", "disabled");
        Button_Click(smsrechargeComfirm, 200);
    });//短信充值提交
    $("#btn_update").bind("click", function () {
        $("this").attr("disabled", "disabled");
        Button_Click(updateState, 200);// 用户状态修改提交"开通","关闭"
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
/**
 * 控制列的隐藏
 */
function hidemColumn() {
    $('#tb_data').bootstrapTable('hideColumn', 'aisleGroupType');
    $('#tb_data').bootstrapTable('hideColumn', 'nickName');
    $('#tb_data').bootstrapTable('hideColumn', 'operate');
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
//刷新数据
function refresh_data() {
    $('#tb_data').bootstrapTable('destroy');
    load_data();
}

//查询配置参数
function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        email: $("#email").val(), //账号信息
        state: $("#state").val(),     //状态
        aisleType: $("#i_aisleType").val(), //通道类型
        nickName: $("#nickName").val() //账号归属
    };
    return temp;
}

//加载列表
function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'accountList.do?menuId=' + $("#menu_id").val(),
        dataType: "json",
        cache: false,
        striped: true,
        pageSize: 50,
        pageList: [50],
        pagination: true,
        paginationPreText: "上一页",
        paginationNextText: "下一页",
        paginationHAlign: "left",
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            if (totalRows > 50) {
                $(".minlid").show();
            } else {
                $(".minlid").hide();

            }
            return '总共 ' + totalRows + ' 条记录';
        },
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: [
            {
                title: "账号",
                field: "email",
                align: "center",
                valign: "middle",
                width: 100
            },
            {
                title: "总数量",
                field: "sumNum",
                align: "center",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "已用数量",
                field: "usedNum",
                align: "center",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "剩余数量",
                field: "surplusNum",
                align: "center",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (index == 0) {
                        $("#i_statistical").html(row.statistical);
                    }
                    return row.surplusNum;
                }
            },
            {
                title: "状态",
                field: "state",
                align: "center",
                valign: "middle",
                width: 45,
                formatter: function (value, row, index) {
                    if (!row.state || row.state == 0) {
                        return "正常";
                    } else if (row.state == 1) {
                        return "<span style='color:red;'>关闭</span>";
                    }
                }
            },
            {
                title: "客户名称",
                field: "name",
                align: "center",
                valign: "middle"
            },
            {
                title: "通道类型",
                field: "aisleGroupType",
                align: "center",
                valign: "middle",
                width: 150
            },
            {
                title: "账号归属",
                field: "nickName",
                align: "center",
                valign: "middle",
                width: 100
            },
            {
                title: '操作',
                field: 'operate',
                align: 'center',
                valign: "middle",
                width: 110,
                formatter: function (value, row) {
                    return loadBtn(row.sysMenuBtns, row.id, row.name, row.surplusNum, row.state, row.sysUserId, row.checkState);
                }
            }],
        responseHandler: function (a) {
            hidemColumn();
            var btn_arr = a["data"];
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/apply/from/';
                for (var k = 0; k < btn_arr.length; k++) {
                    var btn_obj = btn_arr[k];
                    //通道 查询条件输入框 权限
                    if (btn_obj["actionUrls"] == user_path + 'td.do') {
                        $("#tdDiv").show();
                    }
                    //归属 查询条件输入框 权限
                    if (btn_obj["actionUrls"] == user_path + 'gs.do') {
                        $("#gsDiv").show();
                    }
                    //通道类型列 权限
                    if (btn_obj["actionUrls"] == user_path + 'aisleType.do') {
                        $('#tb_data').bootstrapTable('showColumn', 'aisleGroupType');
                    }
                    //归属列 权限
                    if (btn_obj["actionUrls"] == user_path + 'bid.do') {
                        $('#tb_data').bootstrapTable('showColumn', 'nickName');
                    }
                    //操作列 权限
                    if (btn_obj["actionUrls"] == user_path + 'operate.do') {
                        $('#tb_data').bootstrapTable('showColumn', 'operate');
                    }
                }
            }
            return a;
        },
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });

}
//加载菜单对应的按钮
function loadBtn(btn_arr, id, email, surplusNum, state, sysUserId, checkState) {
    var html = '', edit_html = '', delete_html = '', addNum = '';
    add_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/apply/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            if (btn_obj["actionUrls"] == user_path + 'addNum.do' && checkState == 1) {
                addNum = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editjeData(\'' + id + '\',\'' + email + '\',\'' + surplusNum + '\',\'' + sysUserId + '\');"><i class="fa fa-paste"></i>配置短信</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'edit.do') {
                if (state == 0) {
                    edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\',\'' + state + '\');"><i class="fa fa-paste"></i> 关闭</a> ';
                } else {
                    edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\',\'' + state + '\');"><i class="fa fa-paste"></i> 开通</a> ';
                }
            }
        }
    }
    html = edit_html + delete_html + addNum;
    return html;
}


/**
 * 用户关闭/开通
 * @param id
 */
function editData(id, state) {
    data_update = {
        id: id,
        state: ""
    };
    if (state == 0) {
        data_update.state = 1;
    } else {
        data_update.state = 0;
    }
    $("#updateState").modal("show");
}
//修改用户状态"开通","关闭"
function updateState() {
    ajaxCall({
        url: "/account/editState.do",
        type: "post",
        data: JSON.stringify(data_update),
        success: function (data) {
            if (data["retCode"] == "000000") {
                alert("操作成功！");
                refresh_data();
                $("#updateState").modal("hide");
                return;
            } else if (data["retCode"] == "000001") {
                alert("操作失败！");
                return;
            }
        },
        error: function () {
            $(".ibox-content").prepend(alertview("danger", "操作失败."));
        },
        complete: function () {
            $("#btn_update").removeAttr("disabled");

        }
    });
}
//短信充值
function editjeData(id, email, surplusNum, sysUserId) {
    $("#czname").val("");
    $("#czid").val("");
    $("#symoney").val("");
    $("#money").val(""); //充值金额
    $("#type").val(""); //操作类型
    $("#i_czmoney").val("");//充值条数
    $("#czname").val(email); //账号
    $("#czid").val(id);  //充值用户id
    $("#symoney").val(surplusNum);  //剩余数量
    $("#sysUserId").val(sysUserId);
    $("#moneyDiv").hide();
    $('#je_data_div').modal("show");
}
