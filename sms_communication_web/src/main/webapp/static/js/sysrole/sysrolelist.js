var page_num = 1;
$(function () {
    $("#add_button").hide();
    // var param = {};
    // param["menuId"] = $("#menu_id").val();
    // pageInfo(param, 1, PAGE_SIZE);
    // loadData(param);
    // //分页事件
    // pagingEvent(function (_to) {
    //     page_num = _to;
    //     var param = searchParam(_to);
    //     loadData(param);
    // });

    //加载角色数据
    loadRole();


    $("#add_btn").bind("click", function () {
        $('#add_div').modal("show");
        $("#name").val("");
        $("#descr").val("");
        $("#id").val("");
        // $('#folder').tree({
        //     data: treeDataArr
        //     , folderIcon: 'glyphicon glyphicon-chevron-down'
        //     , onClick: function () {
        //         console.log(arguments)
        //     }
        // }).addClass('tree-arrow');

        //loadMenu();
        loadMenuData();
    });
    $("#save_btn").bind("click", function () {
        save();
    });
});


function loadRole() {
    $('#tb_data').bootstrapTable({
        url: 'rolelist.do?menuId=' + $("#menu_id").val(),
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
        columns: [{
            title: "编号",
            field: "id",
            align: "center",
            valign: "middle"

        }, {
            title: "名称",
            field: "roleName",
            align: "center",
            valign: "middle"

        }, {
            title: "创建时间",
            field: "createTime",
            align: "center",
            valign: "middle"

        }, {
            title: "修改时间",
            field: "updateTime",
            align: "center",
            valign: "middle"

        }, {
            title: "描述",
            field: "descr",
            align: "center",
            valign: "middle"

        }, {
            title: '操作',
            align: 'center',
            valign: "middle",
            formatter: function (value, row, index) {
                return loadBtn(row.sysMenuBtns, row.id);
            }
        }],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}
//加载菜单对应的按钮
function loadBtn(btn_arr, id) {
    var html = '', edit_html = '', delete_html = '';
    if (btn_arr.length > 0) {
        var role_path = '/sysRole/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            if (btn_obj["actionUrls"] == role_path + 'merge.do') {
                $("#add_button").show();
                edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
            }
            if (btn_obj["actionUrls"] == role_path + 'delete.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + id + '\');"><i class="fa fa-paste"></i> 删除</a> ';
            }
        }
    }
    html = edit_html + delete_html;
    return html;
}


var treeDataArr = [
    {
        id: "root"
        , text: "Root"
        , attr: {yourfield: "your value"}
        , nodes: [
        {
            id: "tool"
            , text: "Tool"
        }
        , {
            id: "users"
            , text: "Users"
            , active: true
            , nodes: [
                {text: "Kris"}
                , {text: "Tom"}
                , {text: "Jerry"}
                , {text: "Dna"}
            ]
        }
    ]
    }
    , {
        text: "Guest"
    }
    , {
        id: "admin"
        , text: "Admin"
    }
];


function bind_menu_checkbox() {
    $("input[type='checkbox']").bind("click", function () {
   
    	var inp_type = $(this).attr("inp_type");
    	var parent = $(this).attr("parent");    	
        var key = $(this).attr("key");

        if (inp_type == 'menu') {
            var checkVal = $(this).prop("checked");
            //点击选中
            if (checkVal) {
                //$(this).prop("checked", true);
                $("input[parent='" + key + "']").prop("checked", true);
            } else {
                //$(this).prop("checked", false);            
                $("input[parent='" + key + "']").prop("checked", false);
                $(this).parent().parent().parent().parent().siblings("a").children("input[parent='root']").prop("checked",true);
            }
        }
        if (inp_type == 'button') {
            var checkVal = $(this).prop("checked");
            //点击选中
            if (checkVal) {
                //上级选中
                $("input[key='" + parent + "']").prop("checked", true);
            } else {
                var childNum = $("input[parent='" + parent + "']:checked").length;
                if (childNum == 0) {
                	
                		 $("input[key='" + parent + "']").prop("checked", true);                	 
                    $(this).parent().parent().parent().parent().siblings("a").children("input[parent='root']").prop("checked",true);
                }
            }
        }
    });
}


//新增
function save() {
    var name = $("#name").val();
    var id = $("#id").val();
    var descr = $("#descr").val();
    var canCommit = true;
    if (!name) {
        //$("#lb_err_name").text("员工名称不能为空");
        //alert(1);
        canCommit = false;
    }
    if (!canCommit) {
        return;
    }

    var data = {
        "roleName": name,
        "descr": descr
    };
    if (id)
        data["id"] = id;

    var menu_arr = [];
    $("input[type='checkbox']:checked").each(function () {
        menu_arr.push($(this).attr("key"));
    });
    data["menuIds"] = menu_arr;
    
    var top_arr = [];
    $("input[type='checkbox']:checked").each(function () {
        top_arr.push($(this).attr("tops"));
    });
    data["tops"] = top_arr;
    ajaxCall({
        url: "/sysRole/from/merge.do",
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
                $('#add_div').modal("hide");
                // var param = {};
                // param["menuId"] = $("#menu_id").val();
                // loadData(param);
                $('#tb_data').bootstrapTable('refresh', {url: 'rolelist.do?menuId=' + $("#menu_id").val()});
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


function loadMenu() {
    //var html = "";
    //html += "<option value='blueberry' data-section='Smoothies'>Blueberry</option>";
    //html += "<option value='strawberry' data-section='Smoothies'>Strawberries</option>";
    //    <option value="peach" data-section="Smoothies">Peach</option>
    //    <option value="milk tea" data-section="Smoothies/Bubble Tea">Milk Tea</option>
    //<option value="green apple" data-section="Smoothies/Bubble Tea">Green Apple</option>
    //<option value="passion fruit" data-section="Smoothies/Bubble Tea" data-description="The greatest flavor" selected="selected">Passion Fruit</option>
    //$("#test-select").html(html);
    $("#test-select").treeMultiselect({enableSelectAll: true, sortable: true});

    var param = {};

    ajaxCall({
        url: "/sysMenu/queryMenuList.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                buildMenu(arr);
            }
        },
        error: function () {

        },
        complete: function () {

        }
    });
}

function buildMenu(arr) {
    $("#test-select").html("");
    var html = '';
    if (arr.length === 0) {
        $("#tb_data tbody").html(tableNoData(6));
        return;
    }
    for (var i = 0; i < arr.length; i++) {
        var obj = arr[i];
        if (obj["child"] != null && obj["child"].length > 0) {
            for (var k = 0; k < obj["child"].length; k++) {
                var child_obj = obj["child"][k];
                html += '<option value=' + child_obj["parentId"] + ' data-section=root/' + obj["name"] + '>' + child_obj["name"] + '</option>';
            }
        } else {
            html += '<option data-section="root">' + obj["name"] + '</option>';
        }
    }
    $("#test-select").html(html);
    $("#test-select").treeMultiselect({enableSelectAll: true, sortable: true});
}


function searchParam(pageNum) {
    var param = {};
    pageInfo(param, pageNum, PAGE_SIZE);
    return param;
}

/*加载数据*/
function loadData(param) {
    ajaxCall({
        url: "/sysRole/rolelist.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                var btn_arr = data["data"]["data"];
                var total = data["data"]["total"];
                buildTable(arr, btn_arr);
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


function buildTable(arr, btn_arr) {
    var html = '', editPath = SERVER_PATH + "/sysUser/form/";
    if (arr.length == 0) {
        $("#tb_data tbody").html(tableNoData(6));
        return;
    }
    var edit_bl = false;
    var remove_bl = false;
    if (btn_arr.length > 0) {
        var role_path = '/sysRole/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            if (role_path + btn_obj["actionUrls"] == role_path + 'merge.do') {
                $("#add_button").show();
            }
            if (role_path + btn_obj["actionUrls"] == role_path + 'merge.do') {
                edit_bl = true;
            }
            if (role_path + btn_obj["actionUrls"] == role_path + 'delete.do') {
                remove_bl = true;
            }
        }
    }
    var edit_html = "";
    var remove_html = "";
    for (var i = 0; i < arr.length; i++) {
        var obj = arr[i];
        if (edit_bl) {
            edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="editData(\'' + obj["id"] + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
        }
        if (remove_bl) {
            remove_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + obj["id"] + '\');"><i class="fa fa-paste"></i> 删除</a> ';
        }
        html += '<tr> ' +
            '<td>' + obj["roleName"] + '</td> ' +
            '<td>' + new Date(obj["createTime"]).Format("yyyy-MM-dd hh:mm:ss") + '</td> ' +
            '<td>' + new Date(obj["updateTime"]).Format("yyyy-MM-dd hh:mm:ss") + '</td> ' +
            '<td>' + obj["descr"] + '</td> ' +
            '<td class="text-center"> ' +
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
        var param = {"sysRoleId": id};
        ajaxCall({
            url: "/sysRole/from/delete.do",
            type: "get",
            data: param,
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    var param = searchParam(page_num);
                    param["menuId"] = $("#menu_id").val();
                    $('#tb_data').bootstrapTable('destroy');
                    loadRole();
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


//新增 与修改
function save_sysuser() {
    var id = $("#id").val();
    var email = $("#email").val();
    var nickName = $("#nickName").val();
    var canCommit = true;
    if (!email) {
        //$("#lb_err_name").text("员工名称不能为空");
        //alert(1);
        canCommit = false;
    }
    if (!nickName) {
        //$("#lb_err_posts").text("岗位不能为空");
        //alert(2);
        canCommit = false;
    }
    if (!canCommit) {
        return;
    }

    var data = {
        "email": email,
        "nickName": nickName
    };

    if (id)
        data["id"] = id;
    //$btn.attr("disabled", "disabled");
    //$btn.text("正在提交...");

    ajaxCall({
        url: "/sysUser/from/merge.do",
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
                $('#add_sysuser_div').modal("hide");
                var param = searchParam(page_num);
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

function editData(id) {
    $('#add_div').modal("show");
    $('#folder').html('');
    ajaxCall({
        url: "/sysRole/formEdit.do?id=" + id + "",
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                $("#id").val(obj["id"]);
                $("#name").val(obj["roleName"]);
                $("#descr").val(obj["descr"]);
                var arr = data["data"]["dataList"];
                // loadMenuData(arr);
                buildMenuData(arr);
                if (obj["sysRoleRels"]) {
                    for (var i = 0; i < obj["sysRoleRels"].length; i++) {
                        var menu_obj = obj["sysRoleRels"][i];
                        var objId = menu_obj["objId"];
                        $("input[key='" + objId + "']").prop("checked", true);
                    }
                }
                //绑定复选框事件
                bind_menu_checkbox();
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}


function loadMenuData() {
    $('#folder').html('');
    var param = {};
    ajaxCall({
        url: "/sysMenu/queryMenuList.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                buildMenuData(arr);
                //绑定复选框点击事件
                bind_menu_checkbox();
            }
        },
        error: function () {

        },
        complete: function () {

        }
    });
}

function buildMenuData(arr) {
    // $("#tb_menu").html("");
    // var html = '';
    // if (arr.length === 0) {
    //     $("#tb_menu").html(tableNoData(6));
    //     return;
    // }
    // for (var i = 0; i < arr.length; i++) {
    //     var obj = arr[i];
    //     html += '<tr> ' + '<td><input type="checkbox" flag="root" key=' + obj["id"] + '>' + obj["name"] + '</td> ' + '</td> ' + '</tr>';
    //     if (obj["child"] != null && obj["child"].length > 0) {
    //         for (var k = 0; k < obj["child"].length; k++) {
    //             var child_obj = obj["child"][k];
    //             html += '<tr> ' + '<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" flag="child" key=' + child_obj["id"] + ' parentIdVal=' + child_obj["parentId"] + '>' + child_obj["name"] + '</td> ' + '</td> ' + '</tr>';
    //             if (child_obj["btns"] != null && child_obj["btns"].length > 0) {
    //                 for (var j = 0; j < child_obj["btns"].length; j++) {
    //                     var btn_val = child_obj["btns"][j];
    //                     html += '<tr> ' + '<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" flag="child" key=' + btn_val["id"] + ' parentIdVal=' + child_obj["id"] + '>' + btn_val["btnName"] + '</td> ' + '</td> ' + '</tr>';
    //                 }
    //             }
    //         }
    //     }
    // }
    // $("#tb_menu").html(html);
    // $('#folder').html('');
    $('#folder').tree({
        data: arr
        , folderIcon: 'glyphicon glyphicon-chevron-down'
        , onClick: function () {
            console.log(arguments)
        }
    }).addClass('tree-arrow');
}