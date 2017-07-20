var page_num = 1;
$(function () {
    loadUser();
    $("#add_sysuser").bind("click", function () {
        $('#add_sysuser_div').modal("show");
        $("#password_div").show();
        $("#h4_title").html("新增系统用户");
        $("#id").val("");
        $("#email").val("");
        $("#nickName").val("");
        $("#password").val("");
        $("#i_superiorId").val("");
        var param_val = {};
        loadRoleData(param_val);
    });
    $("#save_sysuser_btn").bind("click", function () {
        save_sysuser();
    });
    $("#search_btn").bind("click", function () {
        $('#tb_data').bootstrapTable('refresh', {url: 'userlist.do?menuId=' + $("#menu_id").val(),queryParams: queryParamsVal});
    });
});

function loadUser() {
    $('#tb_data').bootstrapTable({
        url: 'userlist.do?menuId=' + $("#menu_id").val(),
        dataType: "json",
        cache: false,
        striped: true,
    	pageSize:50,
        pageList:[50],
        pagination: true,
        paginationPreText:"上一页",
        paginationNextText:"下一页",
        paginationHAlign:"left",
formatShowingRows: function (pageFrom, pageTo, totalRows) {
        	 if(totalRows>50){
        		 $(".minlid").show();			
        		}else{
        			$(".minlid").hide(); 
        			
        		}
		 return '总共 ' + totalRows + ' 条记录';
        	 },
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        queryParams: queryParamsVal, //参数
        method:"post",
        columns: [
            {
                title: "全选",
                field: "select",
                align: "center",
                valign: "middle",
                checkbox: true
            }, {
                title: "编号",
                field: "id",
                align: "center",
                valign: "middle"
               
            },
            {
                title: "账号",
                field: "email",
                align: "center",
                valign: "middle"
               
            },
            {
                title: "昵称",
                field: "nickName",
                align: "center",
                valign: "middle"
               
            },
            {
                title: "团队",
                field: "tdName",
                align: "center",
                valign: "middle"
               
            },
            {
                title: "角色",
                field: "roleName",
                align: "center",
                valign: "middle"
              
            },
            {
                title: "超级管理员",
                field: "superAdmin",
                align: "center",
                valign: "middle",
              
                formatter: function (value, row, index) {
                    var is_super = "是";
                    if (value == 0) {
                        is_super = "否";
                    }
                    return is_super;
                }
            },
            {
                title: "创建时间",
                field: "createTime",
                align: "center",
                valign: "middle"
               
            },
            {
                title: "登录次数",
                field: "loginCount",
                align: "center",
                valign: "middle"
              
            },
            {
                title: "登录时间",
                field: "loginTime",
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
            }],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}


function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset/params.limit) +1,  //页码
        // minSize: $("#leftLabel").val(),
        // maxSize: $("#rightLabel").val(),
        // minPrice: $("#priceleftLabel").val(),
        // maxPrice: $("#pricerightLabel").val(),
        // Cut: Cut,
        // Color: Color,
        // Clarity: Clarity,
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        sysUserName:$("#user_input").val()//搜索框
    };
    return temp;
}

/**
 * 全选删除
 */
var $table = $('#tb_data'), $remove = $('#delete_selected');
$(function () {
    $remove.click(function () {
        var uids = $.map($table.bootstrapTable('getSelections'), function (row) {
            if(row.superAdmin == 1){
                return null;
            }
            return row.id;
        });
        if(uids==""){
            alert("请先选择需要删除的项!");
            return;
        }
        deleteUsers(uids);
    });
});

//批量删除系统用户
function deleteUsers(uids){
    $('#del').modal("show");
    var data = {
        "uids": uids
    };
    $("#btn_del").bind("click", function () {
        ajaxCall({
            url: "/sysUser/formDelete.do",
            type: "post",
            data: JSON.stringify(data),
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    $('#tb_data').bootstrapTable('refresh', {url: 'userlist.do?menuId=' + $("#menu_id").val(),queryParams: queryParamsVal});
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

//加载菜单对应的按钮
function loadBtn(btn_arr, id) {
    var html = '', edit_html = '', delete_html = ''; reset_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/sysUser/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            if ( btn_obj["actionUrls"] == user_path + 'merge.do') {
                $("#add_button").show();
                edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'reset.do') {
            	reset_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="resetData(\'' + id + '\');"><i class="fa fa-paste"></i>重置密码</a> ';
            }
            
            if (btn_obj["actionUrls"] == user_path + 'delete.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + id + '\');"><i class="fa fa-paste"></i> 删除</a> ';
            }
           
           
        }
    }
    html = edit_html +reset_html+ delete_html;
    return html;
}


function searchParam(pageNum) {
    var param = {};
    pageInfo(param, pageNum, PAGE_SIZE);
    return param;
}

/*加载数据*/
function loadData(param) {
    ajaxCall({
        url: "/sysUser/userlist.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                var total = data["data"]["total"];
                var btn_arr = data["data"]["data"];
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


/*加载数据*/
function loadRoleData(param) {
    ajaxCall({
        url: "/sysRole/roleListInfo.do",
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                buildRoleTable(arr);
            }
        },
        error: function () {

        },
        complete: function () {

        }
    });
}

function buildRoleTable(arr) {
    var html = '';
    $("#role_tab").html("");
    if (arr.length === 0) {
        $("#role_tab").html(tableNoData(7));
        return;
    }
    for (var i = 0; i < arr.length; i++) {
        var obj = arr[i];
        html += '<tr> ' + '<td><input type="checkbox"  key=' + obj["id"] + '>&nbsp;&nbsp;' + obj["roleName"] + '</td> ' + '</td> ' + '</tr>';
    }
    $("#role_tab").html(html);
}


function buildTable(arr, btn_arr) {
    var html = '', editPath = SERVER_PATH + "/sysUser/form/";
    if (arr.length === 0) {
        $("#tb_data tbody").html(tableNoData(7));
        return;
    }

    var edit_bl = false;
    var remove_bl = false;
    if (btn_arr.length > 0) {
        var user_path = '/sysUser/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            if (user_path + btn_obj["actionUrls"] == user_path + 'merge.do') {
                $("#add_button").show();
            }
            if (user_path + btn_obj["actionUrls"] == user_path + 'merge.do') {
                edit_bl = true;
            }
            if (user_path + btn_obj["actionUrls"] == user_path + 'delete.do') {
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
        var is_super = "是";
        if (obj["superAdmin"] == 0) {
            is_super = "否";
        }
        html += '<tr> ' +
            '<td>' + obj["email"] + '</td> ' +
            '<td>' + obj["nickName"] + '</td> ' +
            '<td>' + obj["state"] + '</td> ' +
            '<td>' + is_super + '</td> ' +
            '<td>' + new Date(obj["createTime"]).Format("yyyy-MM-dd hh:mm:ss") + '</td> ' +
            '<td>' + obj["loginCount"] + '</td> ' +
            '<td>' + new Date(obj["loginTime"]).Format("yyyy-MM-dd hh:mm:ss") + '</td> ' +
            '<td class="text-center"> ' +
            edit_html +
            remove_html +
            '</td> ' +
            '</tr>';
    }
    $("#tb_data tbody").html(html);
}


function resetData(id){
	$('#reset').modal("show");
	reset(id)
}
function reset(id) {
    $("#btn_reset").bind("click", function () {
        var param = {"sysUserId": id};
        ajaxCall({
            url: "/sysUser/from/resetPwd.do",
            type: "get",
            data: param,
            success: function (data) {
                if (checkRes(data)) {
                    $("#reset").modal("hide");
                    $('#tb_data').bootstrapTable('refresh', {url: 'userlist.do?menuId=' + $("#menu_id").val(),queryParams: queryParamsVal});
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
    $('#del').modal("show");
    del(id)
}
function del(id) {
    $("#btn_del").bind("click", function () {
        var param = {"sysUserId": id};
        ajaxCall({
            url: "/sysUser/from/delete.do",
            type: "get",
            data: param,
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    
                    var list = data["data"];
                    var htl = '<option value="">请选择</option>';
                    var selectDIv = $("#i_superiorId");
                    for (var i = 0; i < list.length; i++) {
                    	htl+='<option key="'+list[i]["id"]+'" value="'+list[i]["id"]+'" >'+list[i]["nickName"]+'</option>';
    				}
                    selectDIv.html(htl);
                    
                    $('#tb_data').bootstrapTable('refresh', {url: 'userlist.do?menuId=' + $("#menu_id").val(),queryParams: queryParamsVal});
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
    var password = $("#password").val();
    var nickName = $("#nickName").val();
    var superiorId = $("#i_superiorId").val();
    var sateType = $("#i_sateType").val();
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
        "nickName": nickName,
        "pwd": password
       
    };

    if(superiorId!=null && superiorId!=""){
        data["superiorId"] = superiorId;
    }
    if(sateType!=null && sateType!=""){
        data["sateType"] = sateType;
    }
    
    if (id)
        data["id"] = id;
    var role_arr = [];
    $("input[type='checkbox']:checked").each(function () {
        role_arr.push($(this).attr("key"));
    });
    data["roleIds"] = role_arr;
    ajaxCall({
        url: "/sysUser/from/merge.do",
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
                $('#add_sysuser_div').modal("hide");
                
                var list = data["data"];
                var htl = '<option value="">请选择</option>';
                var selectDIv = $("#i_superiorId");
                for (var i = 0; i < list.length; i++) {
                	htl+='<option key="'+list[i]["id"]+'" value="'+list[i]["id"]+'" >'+list[i]["nickName"]+'</option>';
				}
                selectDIv.html(htl);
                $('#tb_data').bootstrapTable('refresh', {url: 'userlist.do?menuId=' + $("#menu_id").val(),queryParams: queryParamsVal});
            } else {
                $(".ibox-content").prepend(alertview("danger", "操作失败."));
            }
        },
        error: function () {
            $(".ibox-content").prepend(alertview("danger", "操作失败."));
        },
        complete: function () {
        }
    });
}

function editData(id) {
    $('#add_sysuser_div').modal("show");
    $("#password_div").hide();
    $("#h4_title").html("修改系统用户");
    ajaxCall({
        url: "/sysUser/formEdit.do?id=" + id,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                $("#id").val(obj["id"]);
                $("#email").val(obj["email"]);
                $("#nickName").val(obj["nickName"]);
                $("#i_superiorId").val(obj["superiorId"]);
                var role_arr = data["data"]["dataList"];
                if (obj["superAdmin"] == 0) {
                    $("#role_div").show();
                    buildRoleTable(role_arr);
                    if (obj["sysRoleRels"]) {
                        for (var i = 0; i < obj["sysRoleRels"].length; i++) {
                            var role_obj = obj["sysRoleRels"][i];
                            var roleId = role_obj["roleId"];
                            if(roleId==48){
                            	 $("input[key='" + roleId + "']").prop("checked", true);
                            	 $("input[key='" + roleId + "']").prop("disabled", true);
                            }else{
                            	 $("input[key='" + roleId + "']").prop("checked", true);
                            }
                        }
                    }
                }
                if (obj["superAdmin"] == 1) {
                    $("#role_div").hide();
                }
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}