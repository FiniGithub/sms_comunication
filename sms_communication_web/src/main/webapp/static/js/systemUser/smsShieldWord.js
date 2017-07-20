// var ChineseDistricts_city = {};
$(function () {
    $('#start_input').datetimepicker({
        lang: 'ch',
        format: "Y-m-d H:i",
        // timeFormat: 'hh:mm:ss',
        timepicker: true,    //关闭时间选项
        yearStart: 2000,     //设置最小年份
        yearEnd: 2050        //设置最大年份
    });//设置中文;
    $('#end_input').datetimepicker({
        lang: 'ch',
        format: "Y-m-d H:i",
        // timeFormat: 'hh:mm:ss',
        timepicker: true,    //关闭时间选项
        yearStart: 2000,     //设置最小年份
        yearEnd: 2050        //设置最大年份
    });//设置中文;
    $.datetimepicker.setLocale('ch');//设置中文
    load_data();

    $("#search_btn").bind("click", function () {
        refresh_data();
    });

    $("#add_data").bind("click", function () {

        $("#i_id").val("");
        $("#i_type").val(0);
        $("#i_name").val("");
        $("#i_comment").val("");
        $("#i_wordName").val("");


        $("#add_data_div").modal("show");
    });


    $("#save_data_btn").bind("click", function () {
        $("#upload_form").submit();
    });


    $('#i_instruct').change(function () {
        var p1 = $(this).children('option:selected').val();//这就是selected的值
        if (p1 == "install" || p1 == "uninstall") {
            $("#i_content").hide();
            $("#i_claim").show();

        } else {
            $("#i_claim").hide();
            $("#i_content").show();
        }
    })

    $('#i_claim').change(function () {
        var p1 = $(this).children('option:selected').val();//这就是selected的值
        $("#i_content").val(p1);
    })


    $("#reset").bind("click", function () {
        // $('#phone_input').val("");
        $('#start_input').val("");
        $('#end_input').val("");
    });

    $("#delAll").bind("click", function () {
        delMore();
    });
});

//刷新数据
function refresh_data() {
    var start_input = $('#start_input').val();
    var end_input = $('#end_input').val();
    //开始时间不为空
    if (start_input != "" && end_input == "") {
        if (!end_input) {
            alert("请选择结束时间！");
            return;
        }
    }
    $('#tb_data').bootstrapTable('destroy');
    load_data();
}

function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        typeId: $("#typeId").val(),// 类型id
        name: $("#user_input").val(),  //名称
        level: $("#levelId").val()// 等级
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'shieldWordList.do?menuId=' + $("#menu_id").val(),
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
                title: "屏蔽词类型",
                field: "type",
                align: "center",
                valign: "middle",
                width: 150,
                formatter: function (value, row, index) {
                    if (!row.type || row.type == 0) {
                        return "通用屏蔽词";
                    } else if (row.type == 1) {
                        return "特殊屏蔽词";
                    }
                }
            },
            {
                title: "等级",
                field: "level",
                align: "center",
                valign: "middle",
                formatter: function (value, row, index) {
                    if (row.level == 1) {
                        return "一级";
                    } else if (row.level == 2) {
                        return "二级";
                    }
                }
            },
            {
                title: "屏蔽词",
                field: "wordName",
                align: "center",
                valign: "middle",

                formatter: function (value, row, index) {
                    if (!row.wordName) {
                        return "";
                    }
                    if (row.wordName.length <= 30) {
                        return row.wordName;
                    } else {
                        var ss = row.wordName.substr(0, 30);
                        return '<a title="' + row.wordName + '">' + ss + '......</a>'
                    }
                }
            },

            {
                title: "创建时间",
                field: "createTime",
                align: "center",
                valign: "middle",
                width: 200,
                formatter: function (value, row, index) {
                    if (!row.createTime) {
                        return "";
                    }
                    return new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                title: '操作',
                align: 'center',
                valign: "middle",
                width: 200,
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id);
                }
            }
        ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}

//加载菜单对应的按钮
function loadBtn(btn_arr, id) {
    var html = '', edit_html = '', delete_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/shieldWord/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];

            if (btn_obj["actionUrls"] == user_path + 'add.do') {
                $("#add_button").show();
            }
            if (btn_obj["actionUrls"] == user_path + 'edit.do') {
                edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                //delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + id + '\');"><i class="fa fa-paste"></i> 删除</a> ';
                $("#delete_selected").show();
            }
        }
    }
    html = edit_html + delete_html;
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

function editData(id) {
    $(".fileinput-remove-button").trigger("click");
    $('#add_data_div').modal("show");
    ajaxCall({
        url: "/wordShielding/formEdit.do?id=" + id,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                $("#i_id").val(obj["id"]);
                $("#i_type").val(obj["type"]);
                $("#i_name").val(obj["name"]);
                $("#i_comment").val(obj["comment"]);
                $("#i_wordName").val(obj["wordName"]);
                $("#level").val(obj["level"]);
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}


//删除
function delMore() {
    var a = $('#tb_data').bootstrapTable('getSelections');
    if (a.length > 0) {
        $("#del").modal("show");
    } else {
        alert("请选择要删除的数据");
        return;
    }
}

/**
 * 全选删除
 */
var $table = $('#tb_data'), $remove = $('#delete_selected');
$(function () {
    $remove.click(function () {
        var ids = $.map($table.bootstrapTable('getSelections'), function (row) {
            return row.id;
        });
        if (ids == "") {
            alert("请先选择需要删除的项!");
            return;
        }
        deleteMoreInfo(ids);
    });
});
//批量删除屏蔽词
function deleteMoreInfo(ids) {
    $('#del').modal("show");
    var data = {
        "ids": ids
    };
    $("#btn_del").bind("click", function () {
        ajaxCall({
            url: "/wordShielding/formDelete.do",
            type: "post",
            data: JSON.stringify(data),
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    $('#tb_data').bootstrapTable('refresh', {
                        url: 'shieldWordList.do?menuId=' + $("#menu_id").val(),
                        queryParams: queryParamsVal
                    });
                } else {
                    $('#delmodal').modal("show");
                }
            },
            error: function () {
            },
            complete: function () {
            }
        });
    });
}


function deleteData(id) {
    $("#del_h4_title").html("确定删除");
    $("#del_p_title").html("确认删除?删除后将无法复原");
    $('#del').modal("show");
    del(id)
}
function del(id) {
    $("#btn_del").bind("click", function () {
        var param = {"id": id};
        ajaxCall({
            url: "/wordShielding/from/delete.do",
            type: "get",
            data: param,
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
}
