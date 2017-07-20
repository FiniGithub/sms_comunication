var path = $("#path").val();
$(function () {
    $("#end_input").val(laydate.now());
    $("#start_input").val(laydate.now(-6));
    showState();
    load_data();
    export_excel();// 导出消费记录
    hidemColumn();
    bindDatetimepicker();
    $("#search_btn").bind("click", function () {
        refresh_data();
    });

});

/**
 * 控制列的隐藏
 */
function hidemColumn() {
    $('#fs-table').bootstrapTable('hideColumn', 'uaccount');
    $('#fs-table').bootstrapTable('hideColumn', 'bname');
    $('#fs-table').bootstrapTable('hideColumn', 'money');
}


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
    $('#fs-table').bootstrapTable('destroy');
    load_data();
    hidemColumn();
}

function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        email: $("#email").val(),// 账户
        type: $("#type").val(),      //类型
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val(),//结束时间
        bid: $("#bid").val()
    };
    return temp;
}

function load_data() {
    var roleId = $("#roleId").val();
    var url = 'puserBillList.do?menuId=' + $("#menuId").val();
    if (roleId != null && roleId != "") {
        url += '&roleId=' + $("#roleId").val();
    }
    $('#fs-table').bootstrapTable({
        url: url,
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
                title: "操作者",
                field: "uaccount",
                align: "center",
                valign: "middle",
                width: 100,
                formatter: function (value, row, index) {
                    if (row.uaccount == null || row.uaccount == "") {
                        return " ";
                    }
                    return row.uaccount;
                }
            },
            {
                title: "账号",
                field: "email",
                align: "center",
                valign: "middle",
                width: 100
            },
            {
                title: "时间",
                field: "createTime",
                align: "center",
                valign: "middle",
                width: 140,
                formatter: function (value, row, index) {
                    if (!row.createTime) {
                        return "";
                    }
                    return new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                title: "充值金额",
                field: "money",
                align: "center",
                valign: "middle"
            },
            {
                title: "数量",
                field: "operateNum",
                align: "center",
                valign: "middle"
            },
            {
                title: "充值前",
                field: "beforeNum",
                align: "center",
                valign: "middle"
            },
            {
                title: "充值后",
                field: "afterNum",
                align: "center",
                valign: "middle"
            },
            {
                title: "类型",
                field: "type",
                align: "center",
                valign: "middle",
                width: 80,
                formatter: function (value, row, index) {
                    if (!row.type || row.type == 0) {
                        return '<span class="status1">人工充值</span>';
                    } else if (row.type == 2) {
                        return '<span class="status1">失败返还</span>';
                    } else if (row.type == 3) {
                        return '<span class="status1">快钱充值</span>';
                    } else if (row.type == 4) {
                        return '<span class="status1">赠送</span>';
                    } else if (row.type == 5) {
                        return '<span class="status1">转出</span>';
                    } else if (row.type == 6) {
                        return '<span class="status1">转入</span>';
                    } else if (row.type == 7) {
                        return '<span class="status1">退费</span>';
                    }

                    if (row.type == 8) {
                        return '<span class="status1">核减</span>';
                    }

                }
            },
            {
                title: "归属",
                field: "bname",
                align: "center",
                valign: "middle",
                width: 100
            }
        ],
        responseHandler: function (obj) {
            hidemColumn();// 控制列的隐藏

            var btn_arr = obj["data"];
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/puserBill';
                for (var k = 0; k < btn_arr.length; k++) {

                    var btn_obj = btn_arr[k];
                    if (btn_obj["actionUrls"] == user_path + '/account.do') {// 操作者
                        $('#fs-table').bootstrapTable('showColumn', 'uaccount');
                    }
                    if (btn_obj["actionUrls"] == user_path + '/money.do') {// 金额
                        $('#fs-table').bootstrapTable('showColumn', 'money');
                    }
                    if (btn_obj["actionUrls"] == user_path + '/belong.do') {// 归属
                        $("#belongDiv").show();
                        $('#fs-table').bootstrapTable('showColumn', 'bname');
                    }
                }
            }
            return obj;
        },
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}


function bindDatetimepicker() {

    var startday = {//历史
        elem: '#start_input',
        format: 'YYYY-MM-DD',
        max: laydate.now(), //设定最大日期为当前日期
        istime: false,
        istoday: true,
        issure: false,
        choose: function (datas) {
            endtday.min = datas; //开始日选好后，重置结束日的最小日期
            endtday.start = datas //将结束日的初始值设定为开始日
        }
    };
    var endtday = {
        elem: '#end_input',
        format: 'YYYY-MM-DD',
        max: laydate.now(), //设定最大日期为当前日期
        istime: false,
        istoday: true,
        issure: false,
        choose: function (datas) {
            startday.max = datas //将结束日的值设定为开始日的最大值
        }
    }
    laydate.skin('yahui');
    laydate(startday);
    laydate(endtday);
}

function addKey(obj, arr, key) {
    var id = $(obj).attr("data-inp");
    $(obj).parent().hide();
    $("#" + id).val(arr);
    $("#i_type").val(key);
}

function showState() {
    var type = $("#i_type").val();
    if (type == 0 && type != null && type != '') {
        var id = $("#cz").attr("data-inp");
        $("#cz").parent().hide();
        $("#qminp").val("充值");
    }
}

/**
 * 导出消费记录
 */
function export_excel() {
    $("#dc_btn").click(function () {
        var uri = path + "/export/exportRecharge.do";
        var email = $("#email").val();// 账户
        var type = $("#type").val();      //类型
        var startInput = $("#start_input").val();//开始时间
        var endInput = $("#end_input").val();//结束时间
        var bid = $("#bid").val();
        window.location.href = uri + "?email=" + email + "&type=" + type + "&startInput=" + startInput + "&endInput=" + endInput + "&bid=" + bid;
    });
}