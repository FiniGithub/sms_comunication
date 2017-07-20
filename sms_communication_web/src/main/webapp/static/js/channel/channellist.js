// var ChineseDistricts_city = {};
$(function () {
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
    load_data();

    $("#search_btn").bind("click", function () {
        refresh_data();
    });

    /*    $("#add_data").bind("click", function () {


     });*/


    $("#save_data_btn").bind("click", function () {
        var extra = '';
        $(".ext_1").each(function (k, obj) {
            extra += $(obj).val();
        });
        $('#extra').val(extra);
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

    $('#i_smsAisleKey').change(function () {
        var aisletypeid = $(this).children('option:selected').val();//这就是selected的值
        load_typedata(aisletypeid, null);
    })


});


function addTdDiv() {
    $("#i_id").val("");
    $("#i_aisleType").val("");
    $("#i_name").val("");
    $("#i_state").val("");
    $("#i_signatureState").val("");
    $("#i_maxNum").val("");
    $("#i_smsAisleKey").val("");
    $("#i_singleNum").val("");
    $("#i_shieldingField").val("");
    $("#i_awardMoney").val("");
    $("#i_smsRegion").val("-1");
    $("#money").val("");
    $("#startCount").val("");
    $("#i_succeedBilling").prop("checked", true);//不选中
    $("#i_failureBilling").prop("checked", false);//不选中
    $("#i_unknownBilling").prop("checked", true);//不选中
    $("#aisledata").html("");

    $("#add_data_div").modal("show");
}

function load_typedata(aisletypeid, map) {
    var html = "";
    ajaxCall({
        url: "/channel/queryPluginConfig.do?className=" + aisletypeid,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                var aisledata = $("#aisledata");


                for (var key in obj) {
                    var values = obj[key];
                    //alert(key+","+values["label"]+","+values["type"]);
                    html += '<div class="inp-box">'
                        + '<span class="inp-title">' + values["label"] + '：</span>'

                        + '<input type="text" class="inp-box-inp ext_' + values["extra"] + '" id="u_' + key + '" name="options" />'
                        + '<input type="hidden"  name="optionsKey" value="' + key + '"/>'
                        + '</div></br>';
                }

                aisledata.html(html);

                if (map != null && map != "") {
                    for (var key in map) {
                        var values = map[key];
                        $("#u_" + key + "").val(values);
                    }
                }
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
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
    $('#tb_data').bootstrapTable('destroy');
    load_data();
}

function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        state: $("#state_select").val(),     //状态
        name: $("#user_input").val(),       //通道名称
        startInput: $("#start_input").val(), //开始时间
        endInput: $("#end_input").val()      //结束时间
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'channelList.do?menuId=' + $("#menu_id").val(),
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
                title: "通道名称",
                field: "name",
                align: "center",
                valign: "middle"

            },
            {
                title: "通道余额",
                field: "remainingNum",
                align: "center",
                valign: "middle",

                formatter: function (value, row, index) {
                    if (row.remainingNum != null) {
                        return row.remainingNum + "条";
                    } else {
                        return null;
                    }
                }

            },
            {
                title: "通道单价",
                field: "money",
                align: "center",
                valign: "middle",

                formatter: function (value, row, index) {
                    if (row.money != null) {
                        return row.money + "元/条";
                    } else {
                        return null;
                    }
                }

            },
            {
                title: "分包数",
                field: "singleNum",
                align: "center",
                valign: "middle"

            },

            {
                title: "起发量",
                field: "startCount",
                align: "center",
                valign: "middle"

            },
            {
                title: "状态",
                field: "state",
                align: "center",
                valign: "middle",

                formatter: function (value, row, index) {
                    if (!row.state || row.state == 1) {
                        return "启用";
                    } else if (row.state == 2) {
                        return "停用";
                    } else if (row.state == 3) {
                        return "自动停用";
                    }
                }
            },
            {
                title: "类型",
                field: "typeName",
                align: "center",
                valign: "middle"


            },
            {
                title: '操作',
                align: 'center',
                valign: "middle",
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id);
                }
            }
        ],
        responseHandler: function (a) {
            var btn_arr = a["data"];
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/msmuUser/from/';
                for (var k = 0; k < btn_arr.length; k++) {
                    var btn_obj = btn_arr[k];
                    if (btn_obj["actionUrls"] == user_path + 'add.do') {
                        var add_html = '<a id="add_data" href="javaScript:void(0);" onclick="addTdDiv()">'
                            + '<button id="add_button" type="button" class="btn btn-info">新增</button>'
                            + '</a> ';
                        $("#addTdDiv").html(add_html);
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
function loadBtn(btn_arr, id) {
    var html = '', edit_html = '', delete_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/msmuUser/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];

            if (btn_obj["actionUrls"] == user_path + 'edit.do') {
                edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + id + '\');"><i class="fa fa-paste"></i> 删除</a> ';
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
        url: "/channel/formEdit.do?id=" + id,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                $("#i_id").val(obj["id"]);
                $("#i_aisleType").val(obj["smsAisleTypeId"]);
                $("#i_name").val(obj["name"]);
                $("#i_state").val(obj["state"]);
                $("#i_signatureState").val(obj["signatureState"]);
                $("#i_maxNum").val(obj["maxNum"]);
                $("#i_singleNum").val(obj["singleNum"]);
                $("#i_shieldingField").val(obj["shieldingFieldId"]);
                $("#startCount").val(obj["startCount"]);

                $("#i_awardMoney").val(obj["comment"]);
                $("#i_smsRegion").val(obj["smsRegionId"]);
                var className = obj["className"];
                $("#i_smsAisleKey").val(className);
                var mapobj = data["data"]["dataList"];
                load_typedata(className, mapobj);

                $("#money").val(obj["money"]);


                var succeedBilling = obj["succeedBilling"];
                if (succeedBilling != null && succeedBilling == 1) {
                    $("#i_succeedBilling").prop("checked", true); //选中
                } else {
                    $("#i_succeedBilling").prop("checked", false);//不选中
                }

                var failureBilling = obj["failureBilling"];
                if (failureBilling != null && failureBilling == 1) {
                    $("#i_failureBilling").prop("checked", true); //选中
                } else {
                    $("#i_failureBilling").prop("checked", false);//不选中
                }

                var unknownBilling = obj["unknownBilling"];
                if (unknownBilling != null && unknownBilling == 1) {
                    $("#i_unknownBilling").prop("checked", true); //选中
                } else {
                    $("#i_unknownBilling").prop("checked", false);//不选中
                }
            }
        },
        error: function () {
        },
        complete: function () {
        }
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
            url: "/channel/from/delete.do",
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
