// var ChineseDistricts_city = {};
$(function () {
    check_unreg();// 退订格式
    load_data();// 加载数据
    loadAisleList();// 加载所有通道
    check_cancel();
//    $('#dredgeAM,#dredgePM').datetimepicker({
//        format: ' hh:ii',
//        initialDate: new Date(), //初始化当前日期
//        autoclose: true, //选中自动关闭
//        todayBtn: true, //显示今日按钮
//        language: 'zh-CN',
//        minView: 0,
//        minuteStep: 1
//    });

    $("#dredgeAM,#dredgePM").timepicki({
        show_meridian: false,
        min_hour_value: 0,
        max_hour_value: 23,
        reset: true
    });

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


    $("#search_btn").bind("click", function () {
        refresh_data();
    });

    $("#save_data_btn").bind("click", function () {
        var am = $("#dredgeAM").val();
        var pm = $("#dredgePM").val();
        if (am != null && am != "" && (pm == null || pm == "")) {
            alert("请填写通道结束时间!");
            return;
        }
        if (pm != null && pm != "" && (am == null || am == "")) {
            alert("请填写通道开始时间!");
            return;
        }
        if (!hourminTime(am, pm)) {
            alert("请正确填写通道开通时间!");
            return;
        }
        var i_id = $("#i_id").val();
        if ($('#checkbox_aisleType').is(':checked')) {
            var aisleType = $("#i_aisleType").val();
            var data = {
                "aisleId": i_id
            };
            ajaxCall({
                url: "/channel/queryAisleGroupDft.do?id=" + aisleType,
                type: "post",
                data: JSON.stringify(data),
                success: function (data) {

                    if (checkRes(data)) {
                        $("#upload_form").submit();
                    } else {
                        alert("该类型的默认通道已存在！");
                        return;
                    }
                },
                error: function () {
                    $(".ibox-content").prepend(alertview("danger", "操作失败."));
                },
                complete: function () {
                }
            });

        } else {
            $("#upload_form").submit();
        }

    });


    //$("#i_oneIntervalPrice").val(obj["oneIntervalPrice"]);
    //$("#i_oneIntervalStart").val(obj["oneIntervalStart"]);

    $("#i_oneIntervalEnd").blur(function () {
        var values = $("#i_oneIntervalEnd").val();
        if (values !== null && values != "") {
            $("#i_twoIntervalStart").val(values);
        }
    });

    $("#i_twoIntervalEnd").blur(function () {
        var values = $("#i_twoIntervalEnd").val();
        if (values !== null && values != "") {
            $("#i_threeIntervalStart").val(values);
        }
    });


    //$("#i_twoIntervalPrice").val(obj["twoIntervalPrice"]);
    //$("#i_twoIntervalStart").val(obj["twoIntervalStart"]);
    //$("#i_twoIntervalEnd").val(obj["twoIntervalEnd"]);

    //$("#i_threeIntervalPrice").val(obj["threeIntervalPrice"]);
    //$("#i_threeIntervalStart").val(obj["threeIntervalStart"]);
    //$("#i_threeIntervalEnd").val(obj["threeIntervalEnd"]);

    $('#i_aisleType').change(function () {
        var aisletypeid = $(this).children('option:selected').val();//这就是selected的值
        load_typedata(aisletypeid, null, null);
    })


    $("#reset").bind("click", function () {
        // $('#phone_input').val("");
        $('#start_input').val("");
        $('#end_input').val("");
    });

});


function addTdzDiv() {
    $("#i_id").val("");
    $("#i_name").val("");
    $("#i_state").val("");
    $("#i_aisleType").val("");
    $("#i_awardMoney").val("");
    $("#i_oneIntervalPrice").val("");
    $("#i_oneIntervalStart").val("");
    $("#i_oneIntervalEnd").val("");

    $("#i_twoIntervalPrice").val("");
    $("#i_twoIntervalStart").val("");
    $("#i_twoIntervalEnd").val("");

    $("#i_threeIntervalPrice").val("");
    $("#i_threeIntervalStart").val("");
    $("#i_threeIntervalEnd").val("");

    $("#i_succeedBilling").prop("checked", true);//不选中
    $("#i_failureBilling").prop("checked", false);//不选中
    $("#i_unknownBilling").prop("checked", true);//不选中

    $("#aisledata").html("");

    $("#dredgeAM").val("");
    $("#dredgePM").val("");
    $("#td_close").prop("checked", true);//选中
    $("#td_open").prop("checked", false);//不选中
    $("#mgc_ts").prop("checked", false);//不选中
    $("#notice").val("");
    $("#hint").val("");
    $("#smsLength").val("");
    $("#signature").val("");


    $("#add_data_div").modal("show");
}


//判断多选是否选中
function checkbox(thiss) {
    var values = $("#" + thiss + "").val();
    var checkID = "i_statetype" + values;
    if ($("#" + thiss + "").prop('checked')) {
        $("#" + checkID + "").val(values);
    } else {
        $("#" + checkID + "").val("");
    }
}


function load_typedata(aisletypeid, aslist, i_sorts) {
    var html = "";
    ajaxCall({
        url: "/channel/formAisleByaisle.do?id=" + aisletypeid,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                var aisledata = $("#aisledata");


                /* for (var i = 0; i < obj.length; i++) {
                 var aislebyaisle = obj[i];
                 var id = aislebyaisle["id"];
                 var money = aislebyaisle["money"];
                 var minSendNum = aislebyaisle["minSendNum"];
                 var smsRegionId = aislebyaisle["smsRegionId"];
                 var regionName = aislebyaisle["regionName"];
                 var name = aislebyaisle["name"]
                 if (smsRegionId == -1) {
                 regionName = "全国";
                 }
                 var mobileSate = aislebyaisle["mobileSate"];
                 var unicomSate = aislebyaisle["unicomSate"];
                 var telecomState = aislebyaisle["telecomState"];

                 var mobileMoney = aislebyaisle["mobileMoney"] == null ? 0 : aislebyaisle["mobileMoney"];
                 var unicomMoney = aislebyaisle["unicomMoney"] == null ? 0 : aislebyaisle["unicomMoney"];
                 var telecomMoney = aislebyaisle["telecomMoney"] == null ? 0 : aislebyaisle["telecomMoney"];

                 var sateString = "";
                 if (1 == mobileSate) {
                 sateString = "<div style='width:100px;float: left;'>移动:" + mobileMoney;
                 }
                 if (1 == unicomSate) {
                 if (sateString != "") {
                 sateString += "</div>"
                 }
                 sateString += "<div style='width:100px;float: left;'>联通:" + unicomMoney;
                 }
                 if (1 == telecomState) {
                 if (sateString != "") {
                 sateString += "</div>"
                 }
                 sateString += "<div style='width:100px;float: left;'>电信:" + telecomMoney + "</div>";
                 }

                 var names = '"' + 'statetype' + (i + 1) + '"';
                 html += "<tr>"
                 + "<td style='width:112px;'>"
                 + "<input type='hidden'  id='i_statetype" + id + "'  name='statetype'>"
                 + "<input type='checkbox' name='checkboxs' onclick='checkbox(" + names + ")' id='statetype" + (i + 1) + "' value='" + id + "' />" + name + ""
                 + "</td>"
                 + "<td style='width:125px;'>"
                 + "<input type='text' style='width: 115px;'  id='minSendNum" + id + "' name='minSendNum' class='form-control'/>"
                 + "</td>"
                 + "<td>"
                 + regionName
                 + "</td>"
                 + "<td style='width:300px;'>"
                 + sateString
                 + "</td>"
                 + "</tr>";

                 }
                 aisledata.html(html);


                 if (aslist != null) {
                 for (var i = 0; i < aslist.length; i++) {
                 var objs = aslist[i];
                 var smsAisleId = objs["smsAisleId"];
                 var statename = "i_statetype" + smsAisleId;
                 $("input[name=checkboxs][value=" + smsAisleId + "]").attr("checked", 'checked');
                 $("#" + statename + "").val(smsAisleId);

                 $("#minSendNum" + smsAisleId + "").val(objs["minSendNum"]);
                 }
                 }*/
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

function handle(name) {
    var money = $("#" + name + "").val();
    if (money < 0.027) {
        alert("通道组价格不得少于0.027元");
        $("#" + name + "").val("");
    }
}

function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'channelGroupList.do?menuId=' + $("#menu_id").val(),
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
                title: "通道类型",
                field: "name",
                align: "center",
                valign: "middle",
                width: 150

            },
            {
                title: "长短信计费",
                field: "smsLength",
                align: "center",
                valign: "middle"

            },
            {
                title: "开通时段",
                field: "smsLength",
                align: "center",
                valign: "middle",
                width: 80,
                formatter: function (value, row, index) {
                    var am = row.dredgeAM;
                    var pm = row.dredgePM;
                    if (am == null || am == "" || pm == null || pm == "") {
                        return "24小时";
                    }
                    return am + "-" + pm;
                }
            }, {
                title: "退订格式",
                field: "unregTypeId",
                align: "center",
                valign: "middle",
                width: 45,
                formatter: function (value, row, index) {
                    if (row.unregTypeId == 0) {
                        return "关闭";
                    } else if (row.unregTypeId == 1) {
                        return "开启";
                    }
                }
            },
            {
                title: "状态",
                field: "state",
                align: "center",
                valign: "middle",
                width: 45,
                formatter: function (value, row, index) {
                    if (!row.state || row.state == 1) {
                        return "启用";
                    } else if (row.state == 2) {
                        return "停用";
                    }
                }
            },
            {
                title: "公告",
                field: "notice",
                align: "left",
                valign: "middle"

            },
            {
                title: "提示",
                field: "hint",
                align: "left",
                valign: "middle"

            },
            {
                title: "签名",
                field: "signature",
                align: "left",
                valign: "middle"

            },
            {
                title: "敏感词类型",
                field: "shieldingFieldId",
                align: "center",
                valign: "middle",
                width: 70,
                formatter: function (value, row, index) {
                    if (row.shieldingFieldId.indexOf(",") != -1) {
                        var txt = "";
                        var result = row.shieldingFieldId.split(",");
                        for (var i = 0; i < result.length; i++) {
                            if (result[i] == 0) {
                                txt += "通用+";
                            } else if (result[i] == 1) {
                                txt += "特殊";
                            }
                        }
                        return txt;
                    }

                    if (!row.shieldingFieldId || row.shieldingFieldId == 0) {
                        return "通用";
                    } else if (row.shieldingFieldId == 1) {
                        return "特殊";
                    }
                }
            },
            {
                title: "备注",
                field: "describes",
                align: "left",
                valign: "middle"

            },
            {
                title: '操作',
                align: 'center',
                valign: "middle",
                width: 70,
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id);
                }
            }
        ],
        responseHandler: function (a) {
            var btn_arr = a["data"];
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/channelGroup/from/';
                for (var k = 0; k < btn_arr.length; k++) {
                    var btn_obj = btn_arr[k];

                    if (btn_obj["actionUrls"] == user_path + 'add.do') {
                        add_html = '<a id="add_data" href="javaScript:void(0);" onclick="addTdzDiv()">'
                            + '<button id="add_button" type="button" class="btn btn-info">新增</button>'
                            + '</a> ';
                        $("#addTdzDiv").html(add_html);
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
        var user_path = '/channelGroup/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];

            /* if (btn_obj["actionUrls"] == user_path + 'add.do') {
             $("#add_button").show();
             edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 新增</a> ';
             }*/
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
//时间戳是整数，否则要parseInt转换
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
    var parentObj = $("#smsAisleInfoDiv");
    var selectAisle = parentObj.find("select[name=statetype]");
    var checkBox = parentObj.find("input[name=operatorId]");
    checkBox.prop("checked", false);
    selectAisle.val("");

    $(".fileinput-remove-button").trigger("click");
    $('#add_data_div').modal("show");
    ajaxCall({
        url: "/channel/formGroupEdit.do?id=" + id,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                $("#dredgeAM").val(obj["dredgeAM"]);
                $("#dredgePM").val(obj["dredgePM"]);


                $("#i_id").val(obj["id"]);
                $("#i_name").val(obj["name"]);
                $("#i_state").val(obj["state"]);
                $("#i_aisleType").val(obj["tid"]);
                $("#i_awardMoney").val(obj["describes"]);

                $("#i_oneIntervalPrice").val(obj["oneIntervalPrice"]);
                $("#i_oneIntervalStart").val(obj["oneIntervalStart"]);
                $("#i_oneIntervalEnd").val(obj["oneIntervalEnd"]);

                $("#i_twoIntervalPrice").val(obj["twoIntervalPrice"]);
                $("#i_twoIntervalStart").val(obj["twoIntervalStart"]);
                $("#i_twoIntervalEnd").val(obj["twoIntervalEnd"]);

                $("#i_threeIntervalPrice").val(obj["threeIntervalPrice"]);
                $("#i_threeIntervalStart").val(obj["threeIntervalStart"]);
                $("#i_threeIntervalEnd").val(obj["threeIntervalEnd"]);

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

                var type = obj["type"];
                if (type != null && type == 1) {
                    $("#checkbox_aisleType").prop("checked", true); //选中
                } else {
                    $("#checkbox_aisleType").prop("checked", false);//不选中
                }

                $("#smsLength").val(obj["smsLength"]);
                $("#notice").val(obj["notice"]);
                $("#hint").val(obj["hint"]);
                $("#signature").val(obj["signature"]);

                var unregTypeId = obj["unregTypeId"];
                $("#unregTypeId").val(unregTypeId);
                if (unregTypeId == 0) {
                    $("#td_open").prop("checked", false);
                    $("#td_close").prop("checked", true);
                } else {
                    $("#td_close").prop("checked", false);
                    $("#td_open").prop("checked", true);
                }

                var sfId = obj["shieldingFieldId"];

                if (sfId != null && sfId.indexOf(",") != -1) {
                    $("#shieldingFieldId").val(sfId);
                    $("#mgc_ts").prop("checked", true);
                } else {
                    $("#shieldingFieldId").val(sfId);
                    $("#mgc_ts").prop("checked", false);
                }

                var operatorId = obj["operatorId"];
                checkOperatorIdBox(obj);

                /*i_sorts = obj["sorts"];
                 var aslist = obj["aslist"];
                 load_typedata(obj["tid"], aslist, i_sorts);*/
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}

/**
 * 设置选中的运营商复选框，同时设置通道下拉框
 * @param obj
 */
function checkOperatorIdBox(obj) {
    var aslist = obj["aslist"];
    var parentObj = $("#smsAisleInfoDiv");
    var checkBoxOper = parentObj.find("input[name=operatorId]");
    var selectAisle = parentObj.find("select[name=statetype]");

    for (var i in aslist) {
        var objData = aslist[i];
        var operatorId = objData.operatorId;
        var smsAisleId = objData.smsAisleId;
        for (var k = 0; k < checkBoxOper.length; k++) {
            var checkOper = checkBoxOper.eq(k).val();
            if (checkOper == operatorId) {
                checkBoxOper.eq(k).prop("checked", true);
                selectAisle.eq(k).val(smsAisleId);
            }
        }
    }


}


/**
 * 取消运营商的通道
 */
function check_cancel() {
    var parentObj = $("#smsAisleInfoDiv");
    var selectAisle = parentObj.find("select[name=statetype]");
    var checkBox = parentObj.find("input[name=operatorId]");

    $("input[name=operatorId]").click(function () {
        var obj = $(this).parent().next().find(selectAisle);
        if ($(this).prop('checked')) {
            $(obj).find("option").eq(1).prop("selected", true);
        } else {
            obj.val("");
        }
    });


    // checkBox.prop("checked", false);
    // selectAisle.val("");

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
            url: "/channel/from/deleteGroup.do",
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


/**
 * 开通、关闭
 */
function check_unreg() {
    $("#td_close").click(function () {
        if ($("#td_close").is(':checked')) {
            $("#td_open").attr("checked", false);
            $("#unregTypeId").val("0");
        } else {
            $("#unregTypeId").val("");
        }
    });

    $("#td_open").click(function () {
        if ($("#td_open").is(':checked')) {
            $("#td_close").attr("checked", false);
            $("#unregTypeId").val("1");
        } else {
            $("#unregTypeId").val("");
        }
    });

    $("#mgc_ts").click(function () {
        if ($("#mgc_ts").is(':checked')) {
            $("#shieldingFieldId").val("0,1");
        } else {
            $("#shieldingFieldId").val("0");
        }
    });


    $("select[name=statetype]").change(function () {
        var parentObj = $("#smsAisleInfoDiv");
        var checkBox = parentObj.find("input[name=operatorId]");
        var obj = $(this).parent().prev().find(checkBox);

        var value = $(this).val();
        if (value != null && value != "") {
            $(obj).prop("checked", true);
        } else {
            $(obj).prop("checked", false);
        }
    });
}


/**
 * 加载所有通道
 */
function loadAisleList() {
    ajaxCall({
        url: "/channel/queryAisleList.do",
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var listData = data["data"];
                $(".aisleSelect").empty();
                $(".aisleSelect").append("<option value=''>请选择通道</option>");
                for (var i in listData) {
                    // select添加option属性
                    var id = listData[i].id;
                    var name = listData[i].name;
                    if (name != null && name != "") {
                        $(".aisleSelect").append("<option value=" + id + " >" + name + "</option>");
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
function hourminTime(star, end) {
    var start = star;
    var end = end;
    var t = false;
    if (start == '' && end == "") {
        t = true;
    } else {
        var sta = start.split(":");
        var nd = end.split(":");
        if (sta[0] < nd[0]) {
            t = true;
            
        }else if(sta[0] == nd[0]) {
        	if (sta[1] < nd[1]||sta[1] ==nd[1]) {
                t = true;
            } else {
                t = false;
            }
        }
    }   
    return t;
}