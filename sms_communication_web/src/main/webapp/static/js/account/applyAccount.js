var path = $("#server_path").val();
$(function () {
    load_data();  //加载列表数据

    //查询 按钮
    $("#search_btn").bind("click", function () {
        refresh_data();
    });

    //新建按钮
    $("#addBtn").on('click', function () {
        operateDate("add");
    });

    //修改按钮
    $("#updateBtn").on('click', function () {
        operateDate("update");
    });
    //删除按钮
    $("#delBtn").on('click', function () {
        del();
    });

    //确认删除
    $("#btn_del").on('click', function () {
        var a = $('#tb_data').bootstrapTable('getSelections');
        var ids = new Array();
        var sysUserIds = new Array();
        for (var i = 0; i < a.length; i++) {
            sysUserIds.push(a[i].sysUserId);
            ids.push(a[i].id);
        }
        $.ajax({
            url: path + "/applyaccount/apply/delete.do",
            type: "POST",
            dataType: "json",
            data: {"ids": ids, "sysUserIds": sysUserIds},
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
    hidemColumn();
});
/**
 * 控制列的隐藏
 */
function hidemColumn() {
    $('#tb_data').bootstrapTable('hideColumn', 'operate');
}
//新增/修改
function operateDate(btnType) {
    if (btnType == "add") {
        window.location.href = path + "/applyaccount/applyview.do?menuId=" + $("#menu_id").val();
    } else {
        //修改
        var a = $('#tb_data').bootstrapTable('getSelections');
        if (a.length == 1) {
            window.location.href = path + "/applyaccount/applyview.do?menuId=" + $("#menu_id").val() + "&id=" + a[0].id;
        } else {
            alert("请选中一行");
            return;
        }
    }
};


//刷新数据
function refresh_data() {
    $('#tb_data').bootstrapTable('destroy');
    load_data();
}
//查询参数配置
function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        phone: $("#phone").val(), //电话号码
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'applyAccountlist.do?menuId=' + $("#menu_id").val(),
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
                field: "选择",
                checkbox: true,
                align: "center",
                valign: "middle",
                width: 30
            },
            {
                title: "申请人",
                field: "nickName",
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
                valign: "middle",
                width: 100
            },
            {
                title: "短信签名",
                field: "signature",
                align: "center",
                valign: "middle"
            },
            {
                title: "备注",
                field: "describes",
                align: "left",
                valign: "middle"
            },
            {
                title: "时间",
                field: "createTime",
                align: "center",
                valign: "middle",
                width: 80,
                formatter: function (value, row, index) {
                    if (!row.createTime) {
                        return "";
                    }
                    return new Date(row.createTime).Format("yyyy-MM-dd");
                }
            },
            {
                title: "状态",
                field: "checkState",
                align: "center",
                valign: "middle",
                width: 55,
                formatter: function (value, row, index) {
                    if (!row.checkState || row.checkState == 0) {
                        return "未审核";
                    } else if (row.checkState == 1) {
                        return "已注册";
                    }
                }
            },
            {
                title: '操作',
                align: 'center',
                field: 'operate',
                valign: "middle",
                width: 70,
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id, row.sysUserId, row.checkState);
                }
            }
        ],
        responseHandler: function (a) {
            hidemColumn();
            var btn_arr = a["data"];
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/applyaccount/from/';
                for (var k = 0; k < btn_arr.length; k++) {
                    var btn_obj = btn_arr[k];
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
function loadBtn(btn_arr, id, sysUserId, checkState) {
    var html = '', edit_html = '', delete_html = '', addNum = '';
    add_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/applyaccount/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            if (btn_obj["actionUrls"] == user_path + 'add.do') {
                addNum = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="registerData(\'' + id + '\',\'' + sysUserId + '\',\'' + checkState + '\');"><i class="fa fa-paste"></i>注册账户</a> ';
            }
        }
    }
    html = edit_html + delete_html + addNum;
    return html;
}

function add0(m) {
    return m < 10 ? '0' + m : m
}
function format(shijianchuo) {
//shijianchuo是整数，否则要parseInt转换
    var time = new Date(shijianchuo);
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
}


function del() {
    var a = $('#tb_data').bootstrapTable('getSelections');
    if (a.length > 0) {
        $("#del").modal("show");
    } else {
        alert("请选择要删除的数据");
        return;
    }
}

//跳转到账户注册页面
function registerData(id, sysUserId, checkState) {
    if (checkState == 0) {
        window.location.href = path + "/management/operate.do?menuId=" + $("#menu_id").val() + "&id=" + id + "&sysUserId=" + sysUserId + "&btnType=register";
    } else {
        alert("用户已经注册！");
        return;
    }
}
