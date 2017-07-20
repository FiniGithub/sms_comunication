var path = $("#path").val();
$(function () {

    jumFromStatic();

    load_data();// 加载数据
    loadAisleGroup();

    $("#search_btn").bind("click", function () {
        refresh_data();
        $('#fs-table').bootstrapTable('hideColumn', 'groupName');
    });

    $("#reset").bind("click", function () {
        $('#start_input').val("");
        $('#end_input').val("");
    });

    $("#start_input").val(new Date(new Date).Format("yyyy-MM-dd"));
    $("#end_input").val(new Date(new Date).Format("yyyy-MM-dd"));

    $('#fs-table').bootstrapTable('hideColumn', 'groupName');
    bindDatetimepicker();// 时间控件
});

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
        issure: false,
        max: laydate.now(), //设定最大日期为当前日期
        istime: false,
        istoday: true,
        choose: function (datas) {

            startday.max = datas //将结束日的值设定为开始日的最大值
        }
    }
    laydate.skin('yahui');
    laydate(startday);
    laydate(endtday);
}

function load_data() {
    $('#fs-table').bootstrapTable({
        url: 'psendList.do?menuId=' + $("#menu_id").val(),
        dataType: "json",
        cache: false,
        striped: true,
        pageSize: 50,
        pageList: [50],
        pagination: true,
        ccext: false,// 自定义样式,为true 跨行显示短信内容
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
        ccext: false,// 自定义样式,为true 跨行显示短信内容
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: [
            {
                title: "账号",
                field: "smsUserEmail",
                align: "center",
                valign: "middle",
                width: 100
            },
            {
                title: "短信条数",
                field: "billingNum",
                align: "center",
                valign: "middle",
                width: 80,
                formatter: function (value, row, index) {
                    var s = "<span class='fsnum'>" + row.billingNum + "</span>";
                    return s;
                }
            },
            {
                title: "发送时间",
                field: "sendTime",
                align: "center",
                valign: "middle",
                width: 140,
                formatter: function (value, row, index) {
                    var time = ' - ';
                    if (row.sendTime != null) {
                        time = new Date(row.sendTime).Format("yyyy-MM-dd hh:mm:ss");
                    }
                    var s = '<span class="xfcontent">' + time + '</span>';
                    return s;
                }
            },
            {
                title: "短信内容",
                field: "content",
                align: "left",
                valign: "middle",

            },
            {
                title: "任务类型",
                field: "sendType",
                align: "center",
                valign: "middle",
                width: 45,
                formatter: function (value, row, index) {
                    if (row.sendType == 0) {
                        return "实时";
                    } else if (row.sendType == 1) {
                        return "定时";
                    }
                }
            },
            {
                title: "通道类型",
                field: "groupName",
                align: "center",
                valign: "middle",
                width: 150
            },
            {
                title: '操作',
                align: 'center',
                valign: "middle",
                width: 80,
                formatter: function (value, row, index) {
                    var q = '<a  style="color: #337ab7;" href="javaScript:void(0);" onclick="smsSendDetails(\'' + row.id + '\');"><span class="pcnum">详情</span></a>';
                    var f = '<a href="javaScript:void(0);" data-clipboard-action="copy" data-clipboard-target="#c-index' + index + '" id="copy-btn' + index + '" onclick=\'copyContent(this,\"' + index + '\");\'>复制</a><textarea style="filter:alpha(opacity=0);position:absolute;z-index:-100;opacity:0;width:0px;"  id="c-index' + index + '">' + row.content + '</textarea>';
                    return q + f;
                }
            }
        ],
        responseHandler: function (a) {
            $('#fs-table').bootstrapTable('hideColumn', 'groupName');
            var btn_arr = a["data"];
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/sendList';
                for (var k = 0; k < btn_arr.length; k++) {
                    var btn_obj = btn_arr[k];
                    if (btn_obj["actionUrls"] == user_path + '/aisleSelect.do') {
                        $("#aisleSpan").show();// 显示下拉框
                        $('#fs-table').bootstrapTable('showColumn', 'groupName');
                    }
                }
            }
            return a;
        },

        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });

//    getRealTimeOrSmsNum();// 获取用户消息和短信条数
}

/**
 * 复制
 * @param obj
 */
function copyContent(obj, arr) {
    if (!!window.ActiveXObject || "ActiveXObject" in window) {

        var t = $(obj).next()[0];
        t.select();
        window.clipboardData.setData('text', t.createTextRange().text);
        alert("复制内容：" + $(obj).next().val());
        return;
    }

    var clipboard = new Clipboard('#copy-btn' + arr);
    clipboard.on('success', function (e) {
        alert("复制内容：" + e.text);
    });
    clipboard.on('error', function (e) {

    });

}

/**
 * 加载通道组
 */
function loadAisleGroup() {
    $.post(path + "/smsUser/queryAisleGroup.do", function (data) {
        var listData = data["data"];

        $("#sel_groupId").empty();
        $("#sel_groupId").append("<option value=''>全部</option>");
        for (var i = 0; i < listData.length; i++) {
            // select添加option属性
            var key = listData[i].id;
            var data = listData[i].name;
            $("#sel_groupId").append("<option value=" + key + " >" + data + "</option>");

        }
    });

}

function jumFromStatic() {
    if ($("#account_jump").val() != "") {
        $("#account_input").val($("#account_jump").val());// 账号
    }
}

function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        account: $("#account_input").val(),// 账号
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val(),//结束时间
        sendType: $("#sendType").val(),// 1：定时任务
        groupId: $("#sel_groupId").val()// 通道类型id
    };
    return temp;
}

//刷新数据
function refresh_data() {
    $('#fs-table').bootstrapTable('hideColumn', 'groupName');
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
}

// 状态框
function addKey(obj, arr, key) {
    var id = $(obj).attr("data-inp");
    $(obj).parent().hide();
    $("#" + id).val(arr);
    $("#state_input").val(key);
}

// 终止操作
function endOption(id) {
    fixMidel(".zztc");
    $(".regiger-bg").show();
    $("#zz_id").val(id);
    $(".zztc").show();
}
// 终止发送
$(function () {
    $(".alwd-btn-close").click(function () {
        var id = $("#zz_id").val();
        $.ajax({
            url: path + "/smsUser/stopSmsSend.do?id=" + id,
            type: "get",
            success: function (data) {
                var regCode = data["retCode"];
                if (regCode == "000000") {
                    window.location.reload();//刷新当前页面
                } else {
                    var msg = data["data"];
                    alert(msg);
                    window.location.reload();//刷新当前页面
                }


            },
            error: function () {
                alert("操作失败");
            },
            complete: function () {
            }
        });
    });
});


/**
 * 发送详情
 * @param id
 */
function smsSendDetails(id) {
    var menu_id = $("#menu_id").val();
    var uri = path + '/smsUser/sendDetailsView.do?id=' + id + "&menuId=" + menu_id;
    window.location.href = uri;
}
function selectPage() {
    $('#fs-table').bootstrapTable('selectPage', 2)
}

Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        'M+': this.getMonth() + 1, //月份
        'd+': this.getDate(), //日
        'h+': this.getHours(), //小时
        'm+': this.getMinutes(), //分
        's+': this.getSeconds(), //秒
        'q+': Math.floor((this.getMonth() + 3) / 3), //季度
        'S': this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
    return fmt;
}