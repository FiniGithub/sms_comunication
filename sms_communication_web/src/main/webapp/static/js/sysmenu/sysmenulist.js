$(function () {
    $("#rank").val("1");
    var param = {};
    loadData(param);
    $("#save_btn").bind("click", function () {
        save();
    });
    $("#add_btn").bind("click", function () {
        $('#add_div').modal("show");
        $("#name").val("");
        $("#url").val("");
        $("#rank").val("1");
        $("#id").val("");
        $("#parent_id").val("");
        $("#root_id").val("rootVal");
        $("#actions").val("");
        $("#btn_div").hide();
        btn_check();
    });

    $("#add").bind("click", function () {
        var html = '<tr> ' +
            '<td><input type="text" id="btnName"></td> ' +
            '<td><input type="text" id="btnType"></td> ' +
            '<td><input type="text" id="actionUrls"></td> ' +
            '<td><button type="button" class="btn btn-default" onclick="remove_tr(this)"> - </button></td>' +
            '</tr>';
        $("#tb_menu_btn tbody").append(html);
    });
    $("#add_default").bind("click", function () {
        var html = '<tr> ' +
            '<td><input type="text" value="新增" id="btnName"></td> ' +
            '<td><input type="text" value="add" id="btnType"></td> ' +
            '<td><input type="text"  id="actionUrls"></td> ' +
            '<td><button type="button" class="btn btn-default" onclick="remove_tr(this)"> - </button></td>' +
            '</tr>';
        html += '<td><input type="text" value="编辑" id="btnName"></td> ' +
            '<td><input type="text" value="edit" id="btnType"></td> ' +
            '<td><input type="text"  id="actionUrls"></td> ' +
            '<td><button type="button" class="btn btn-default" onclick="remove_tr(this)"> - </button></td>' +
            '</tr>';
        html += '<td><input type="text" value="删除" id="btnName"></td> ' +
            '<td><input type="text" value="remove" id="btnType"></td> ' +
            '<td><input type="text" id="actionUrls"></td> ' +
            '<td><button type="button" class="btn btn-default" onclick="remove_tr(this)"> - </button></td>' +
            '</tr>';
        $("#tb_menu_btn tbody").html(html);
    });
    $("#delete_all").bind("click", function () {
        $("#tb_menu_btn tbody").html("");
    });
});

function remove_tr(this_val) {
    $(this_val).parent().parent().remove();
}


function btn_check() {
    $("#add_btn_menu").prop("checked", false);
    $("#edit_btn_menu").prop("checked", false);
    $("#remove_btn_menu").prop("checked", false);
}


/*加载数据*/
function loadData(param) {
    ajaxCall({
        url: "/sysMenu/menulist.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                var total = data["data"]["total"];
                buildTable(arr);
                $("#paging ul").empty();
                paging(+total, param["pagenum"], param["pagesize"]);
            }
        },
        error: function () {

        },
        complete: function () {

        }
    });
}

function add(id) {
    $('#add_div').modal("show");
    $("#name").val("");
    $("#url").val("");
    $("#rank").val("1");
    $("#id").val("");
    $("#actions").val("");
    $("#parent_id").val(id);
    $("#root_id").val("child");
    $("#btn_div").show();
    btn_check();
    $("#url").attr("disabled", false);
    $("#tb_menu_btn tbody").html("");
}


function buildTable(arr) {
    var html = '', editPath = SERVER_PATH + "/sysUser/form/";
    if (arr.length === 0) {
        $("#tb_data tbody").html(tableNoData(7));
        return;
    }
    for (var i = 0; i < arr.length; i++) {
        var obj = arr[i];
        var inden = "";
        var add_html = "";
        var edit_html = "";
        var remove_html = "";
        var flag = "root_menu"
        var updated = obj["updateTime"];// 修改时间
        if(updated!=null){
            updated =  new Date(obj["updateTime"]).Format("yyyy-MM-dd hh:mm:ss");
        }else{
            updated = " ";
        }
        if (obj["parentId"]) {
            inden += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            flag = "child_menu";
        } else {
            add_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="add(\'' + obj["id"] + '\');"><i class="fa fa-paste"></i> 新增</a>';
        }
        edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"   onclick="editData(\'' + obj["id"] + '\',\'' + flag + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
        remove_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + obj["id"] + '\');"><i class="fa fa-paste"></i> 删除</a> ';
        html += '<tr> ' +
            '<td>' + inden + obj["name"] + '</td> ' +
            '<td>' + obj["rank"] + '</td> ' +
            '<td>' + obj["url"] + '</td> ' +
            '<td>' + new Date(obj["createTime"]).Format("yyyy-MM-dd hh:mm:ss") + '</td> ' +
            '<td>' + updated + '</td> ' +
            '<td class="text-center"> ' +
            add_html +
            edit_html +
            remove_html +
            '</td> ' +
            '</tr>';
    }
    $("#tb_data tbody").html(html);
}

function deleteData(id) {
    $('#del').modal("show");
    del(id)
}
function del(id) {
    $("#btn_del").bind("click", function () {
        var param = {"sysMenuId": id};
        ajaxCall({
            url: "/sysMenu/from/delete.do",
            type: "get",
            data: param,
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    var param = {};
                    loadData(param);
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


//新增
function save() {
    var name = $("#name").val();
    var url = $("#url").val();
    var rank = $("#rank").val();
    var id = $("#id").val();
    var actions = $("#actions").val();
    var canCommit = true;

    var reg = /^[0-9]$/;
    if (!reg.test(rank)) {
        canCommit = false;
    }
    if (!name) {
        //$("#lb_err_name").text("员工名称不能为空");
        //alert(1);
        canCommit = false;
    }
    //if (!url) {
    //$("#lb_err_posts").text("岗位不能为空");
    //alert(2);
    //    canCommit = false;
    //}
    if (!rank) {
        //$("#lb_err_posts").text("岗位不能为空");
        //alert(2);
        canCommit = false;
    }
    if (!canCommit) {
        return;
    }

    var data = {
        "name": name,
        "rank": rank
    };
    if(url){
        data["url"] = url;
    }
    if(actions){
        data["actions"] = actions;
    }
    var root_id = $("#root_id").val();

    var btn_arr = [];
    //不为根节点
    if (root_id == 'child' || root_id == 'update_menu') {
        // var url_val = '';
        // if(url){
        //     url_val=url.substr(0,url.lastIndexOf('/'));
        // }
        var parentId_val= $("#parent_id").val();
        if(parentId_val){
            data["parentId"] = parentId_val;
        }
        $("#tb_menu_btn tbody").find("tr").each(function () {
            var btn_data = {};
            $(this).find(":input").each(function () {
                if ($(this).attr("id") == 'btnName') {
                    btn_data["btnName"] = $(this).val();
                }
                if ($(this).attr("id") == 'btnType') {
                    btn_data["btnType"] = $(this).val();
                }
                if ($(this).attr("id") == 'actionUrls') {
                    btn_data["actionUrls"] = $(this).val();
                }
            });
            btn_arr.push(btn_data);
        });
    }
    data["btns"] = btn_arr;
    if (root_id == 'update_menu') {
        if (id)
            data["id"] = id;
    }
    //$("input[name='btn_menu']").each(function(){
    //    alert($(this).prop("checked"));
    //    if($(this).prop("checked")){
    //        alert(1);
    //    }
    //});
    //return ;

    // if ($("#add_btn_menu").prop("checked")) {
    //     btn_arr.push({
    //         "btnName": "新增",
    //         "btnType": "add",
    //     });
    // }
    // if ($("#edit_btn_menu").prop("checked")) {
    //     btn_arr.push({
    //         "btnName": "修改",
    //         "btnType": "edit",
    //     });
    // }
    // if ($("#remove_btn_menu").prop("checked")) {
    //     btn_arr.push({
    //         "btnName": "删除",
    //         "btnType": "remove",
    //     });
    // }


    ajaxCall({
        url: "/sysMenu/from/merge.do",
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
                $('#add_div').modal("hide");
                var param = {};
                loadData(param);
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

function editData(id, flag) {
    $("#root_id").val("update_menu");
    $('#add_div').modal("show");
    $("#btn_div").show();
    btn_check();
    if (flag == "root_menu") {
        $("#url").val("");
        $("#parent_id").val("");
        $("#url").attr("disabled", true);
        btn_check();
        $("#btn_div").hide();
    }
    if (flag == "child_menu") {
        $("#parent_id").val(id);
        $("#url").attr("disabled", false);
    }
    ajaxCall({
        url: "/sysMenu/formEdit.do?id=" + id,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                $("#id").val(obj["id"]);
                $("#name").val(obj["name"]);
                $("#url").val(obj["url"]);
                $("#rank").val(obj["rank"]);
                $("#parent_id").val(obj["parentId"]);
                $("#actions").val(obj["actions"]);
                var url = obj["url"];
                if(url){
                    if(url.indexOf("/menu.do")!=-1){
                        $("#tb_menu_btn tbody").html("");
                        $("#btn_div").hide();
                    }else{
                        var btn_arr = obj["btns"];
                        $("#tb_menu_btn tbody").html("");
                        $("#btn_div").show();
                        btn_arr.forEach(function (e) {
                            var html = '<tr> ' +
                                '<td><input type="text" id="btnName" value="'+e["btnName"]+'"></td> ' +
                                '<td><input type="text" id="btnType" value="'+e["btnType"]+'"></td> ' +
                                '<td><input type="text" id="actionUrls" value="'+e["actionUrls"]+'"></td> ' +
                                '<td><button type="button" class="btn btn-default" onclick="remove_tr(this)"> - </button></td>' +
                                '</tr>';
                            $("#tb_menu_btn tbody").append(html);
                        });
                    }
                }
            } else {
                $('#add_div').modal("show");
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}