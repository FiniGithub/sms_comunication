var path = $("#path").val();
$(function () {
    load_data();// 加载数据

    hideColumn();
    $("#search_btn").bind("click", function () {
        refresh_data();
        hideColumn();
    });
});


//刷新数据
function refresh_data() {
    $('#com-table').bootstrapTable('destroy');
    load_data();
}

function hideColumn() {
    $('#com-table').bootstrapTable('hideColumn', 'fkState');
    $('#com-table').bootstrapTable('hideColumn', 'feedbackTime');
}


/**
 * 加载数据
 */
function load_data() {
    $(".smsSum").html("0");// 提交短信
    $(".fszsum").html("0");// 发送中
    $(".yfssum").html("0");// 已发送
    $(".cgsum").html("0");// 发送成功
    $(".sbsum").html("0");// 发送失败
    $('#com-table').bootstrapTable({
        url: 'sendSmsDetailList.do',
        dataType: "json",
        cache: false,
        striped: true,
        ccext: false,
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
                field: "smsUserEmail",
                align: "center",
                valign: "middle",
                width: 100,
                formatter: function (value, row, index) {
                    if (index == 0) {
                        $(".smsSum").html(row.billingNnum);// 提交短信
                        $(".fszsum").html(row.sendingNum);// 发送中
                        $(".yfssum").html(row.sendFinishNum);// 已发送
                        $(".cgsum").html(row.succeedNum);// 发送成功
                        $(".sbsum").html(row.failureNum);// 发送失败
                    }
                    return row.smsUserEmail;
                }
            },
            {
                title: "手机号码",
                field: "receivePhone",
                align: "center",
                valign: "middle",
                width: 100,
                formatter: function (value, row, index) {
                    var p = '<span class="phonum">' + row.receivePhone + '</span>';
                    return p;
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
                        time = row.sendTime.split(".")[0];
                    }
                    return time;
                }
            },
            {
                title: "短信内容",
                field: "content",
                align: "left",
                valign: "middle",
                formatter: function (value, row, index) {
                    var p = '<span class="phonum">' + row.content + '</span>';
                    return p;
                }
            },
            {
                title: "字数",
                field: "content",
                align: "center",
                valign: "middle",
                width: 45,
                formatter: function (value, row, index) {
                    var p = '<span class="phonum">' + row.content.length + '</span>';
                    return p;
                }
            },
            {
                title: "发送状态",
                field: "state",
                align: "center",
                valign: "middle",
                width: 70,
                formatter: function (value, row, index) { //状态:-1：待发送,0：已提交，2：发送失败，3未知，21; //网络错误，22; //空号错误，23; //发送上限，24; //黑名单错误，25：审核失败，26; //发送失败，27; 通道拒绝接收，31:通道返回失败，99; //状态报告为未知，100：成功
                    if (row.state == null) {
                        return null;
                    } else if (row.state == -1) {
                        return "发送中";
                    } else if (row.state == 0) {
                        return "已发送";
                    } else if (row.state == 2 || row.state == 26 || row.state == 3 || row.state == 21 || row.state == 22 || row.state == 23 || row.state == 24 || row.state == 27 || row.state == 31 || row.state == 25) {
                        return "发送失败";
                    }
                    else if (row.state == 100) {
                        return '<span class="status2">发送成功</span>';
                    } else if (row.state == 99) {
                        return '<span class="status2">已发送</span>';
                    }

                }
            },
            {
                title: "状态报告",
                field: "fkState",
                align: "center",
                valign: "middle",
                width: 80,
                formatter: function (value, row, index) {
                    if (row.fkState != null && row.receiveCode != null) {
                        return row.fkState + "-" + row.receiveCode;
                    } else if (row.fkState != null && row.receiveCode == null) {
                        return row.fkState
                    } else if (row.fkState == null && row.receiveCode != null) {
                        return row.receiveCode
                    }
                }
            }, {
                title: "状态时间",
                field: "feedbackTime",
                align: "center",
                valign: "middle",
                width: 140,
                formatter: function (value, row, index) {
                    var feedbackTime = '-';
                    if (row.feedbackTime != null) {
                        feedbackTime = row.sendTime.split(".")[0];
                    }
                    return feedbackTime;
                }

            }
        ],
        responseHandler: function (obj) {
            hideColumn();


            var btn_arr = obj["sysMenuBtns"];
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/sendList';
                for (var k = 0; k < btn_arr.length; k++) {
                    var btn_obj = btn_arr[k];
                    if (btn_obj["actionUrls"] == user_path + '/exportPhone.do') {// 导出
                        var htmlData = '<input id="export_btn" class="f-btn" type="button" value="导 出" onclick="export_phone();" />';
                        $("#exprot_span").html(htmlData);
                    }
                    if (btn_obj["actionUrls"] == user_path + '/statusReport.do') {// 状态报告
                        $('#com-table').bootstrapTable('showColumn', 'fkState');
                    }
                    if (btn_obj["actionUrls"] == user_path + '/statusTime.do') {// 状态时间
                        $('#com-table').bootstrapTable('showColumn', 'feedbackTime');
                    }
                }
            }
            return obj;
        },
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    })
    ;
}


function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        id: $("#id_input").val(),
        menuId: $("#menuId").val(),
        phone: $("#phone").val(),// 发送号码
        type: $("#sendType").val()// 发送状态

    };
    return temp;
}


function export_phone() {

    var id = $("#id_input").val();
    var uri = path + "/smsUser/export/csv.do?id=" + id;
    window.location.href = uri;


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