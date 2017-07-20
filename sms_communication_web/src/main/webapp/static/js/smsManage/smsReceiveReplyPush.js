var page_num = 1;
$(function () {
	$("#end_input").val(laydate.now());
	$("#start_input").val(laydate.now(-6));
	
	var startday = {//历史
	        elem: '#start_input',
	        format: 'YYYY-MM-DD',
	        max: laydate.now(), //设定最大日期为当前日期
	        istime: false,
	        istoday: true,
	        issure: false,
	        choose: function(datas){  
	        	endtday.min = datas; //开始日选好后，重置结束日的最小日期  
	        	endtday.start = datas //将结束日的初始值设定为开始日  
	        	
	        	
	       }  
	    };
	var endtday={
		 elem: '#end_input',
	        format: 'YYYY-MM-DD',
	        issure: false,
	        max: laydate.now(), //设定最大日期为当前日期
	        istime: false,
	        istoday: true,
	        choose: function(datas){ 
	        	
	        	startday.max = datas //将结束日的值设定为开始日的最大值  
	       }  
	}
	    laydate.skin('yahui');
	    laydate(startday);
	    laydate(endtday);
	    laydate.now();
	    
    loadUser();
    hideColumn();

    $("#add_sysuser").bind("click", function () {

        $("#password_div").show();
        $("#h4_title").html("新增系统用户");
        $("#id").val("");
        $("#phone").val("");
        $("#aisleId").val("");
        $("#aisleName").val("");
        var param_val = {};
        loadRoleData(param_val);
        $("#add_sysuser_div").modal("show");
    });

    $("#save_sysuser_btn").bind("click", function () {
        save_sysuser();
    });
    $("#search_btn").bind("click", function () {
        $('#tb_data').bootstrapTable('removeAll');
        $('#tb_data').bootstrapTable('refresh', {
            url: 'blacklist.do?menuId=' + $("#menu_id").val(),
            queryParams: queryParamsVal
        });
    });

});

function loadUser() {
    $('#tb_data').bootstrapTable({
        url: 'blacklist.do?menuId=' + $("#menu_id").val(),
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
        sidePagination: "server", // 服务端处理分页
        queryParams: queryParamsVal, // 参数
        method: "post",
        columns: [
            /*{
             title : "团队",
             field : "teamName",
             align : "left",
             valign : "middle",
             sortable : "true",
             },
             {
             title : "业务员",
             field : "ywName",
             align : "left",
             valign : "middle",
             sortable : "true",
             },
             {
             title : "客户名称",
             field : "name",
             align : "left",
             valign : "middle",
             sortable : "true",
             },*/ {
                title: "账号",
                field: "email",
                align: "center",
                valign: "middle",
                width:100
            }, {
                title: "发送短信",
                field: "contents",
                align: "left",
                valign: "middle",
                width:250,
                formatter: function (value, row, index) {
                    var c='<div class="fitle" title="'+row.contents+'">'+row.contents+'</div>';
                     return c;                     
                  }
            }, {
                title: "手机号码",
                field: "phone",
                align: "center",
                valign: "middle",
                width:100
            }, {
                title: "回复内容",
                field: "content",
                align: "left",
                valign: "middle"
            }, {
                title: "接收时间",
                field: "createTime",
                align: "center",
                valign: "middle",
                width:140,
                formatter: function (value, row, index) {
                    var c = "-";
                    if (!row.createTime) {
                        c = new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
                    }
                    return c;
                }
            },
            {
                title: "通道名称",
                field: "aname",
                align: "center",
                valign: "middle",
                width:150
            }/*, {
             title: "推送状态",
             field: "state",
             align: "left",
             valign: "middle",
             sortable: "true",
             formatter: function (value, row, index) {
             if (row.state == null) {
             return null;
             } else if (row.state == 1) {
             return "未推送";
             } else if (row.state == 2) {
             return "已推送";
             }
             }
             }*/],
        responseHandler: function (obj) {
            hideColumn();// 控制列的隐藏
            var btn_arr = obj["data"];
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/receiveReplyPush';
                for (var k = 0; k < btn_arr.length; k++) {
                    var btn_obj = btn_arr[k];
                    if (btn_obj["actionUrls"] == user_path + '/aisleName.do') {// 通道
                        $('#tb_data').bootstrapTable('showColumn', 'aname');
                        $("#aisleSpan").show();
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

function queryParamsVal(params) { // 配置参数
    var temp = { // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit, // 页面大小
        pagenum: (params.offset / params.limit) + 1, // 页码
        sort: params.sort, // 排序列名
        order: params.order,// 排位命令（desc，asc）
        emailInput: $("#email_input").val(),// 账号
        nameInput: $("#name_input").val(),// 客服名字
        sysUserName: $("#user_input").val(),// 搜索框 号码
        contentInput: $("#content_input").val(),// 回复内容
        startInput: $("#start_input").val(),// 开始时间
        endInput: $("#end_input").val(),// 结束时间
        smsAisleId: $("#aisleNameSelect").val()// 通道


    };
    return temp;
}

function hideColumn() {
    $('#tb_data').bootstrapTable('hideColumn', 'aname');
}

function searchParam(pageNum) {
    var param = {};
    pageInfo(param, pageNum, PAGE_SIZE);
    return param;
}

/* 加载数据 */
function loadData(param) {
    ajaxCall({
        url: '/smsReceiveReplyPush/blacklist.do',
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

function buildRoleTable(arr) {
    var html = '';
    $("#role_tab").html("");
    if (arr.length === 0) {
        $("#role_tab").html(tableNoData(7));
        return;
    }
    for (var i = 0; i < arr.length; i++) {
        var obj = arr[i];
        html += '<tr> ' + '<td><input type="checkbox"  key=' + obj["id"]
            + '>&nbsp;&nbsp;' + obj["roleName"] + '</td> ' + '</td> '
            + '</tr>';
    }
    $("#role_tab").html(html);
}

function loadBtn(btn_arr, id) {

    var html = '', edit_html = '', delete_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/smsReceiveReplyPush/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];

            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\''
                    + id + '\');"><i class="fa fa-paste"></i> 移除黑名单</a> ';
            }
        }
    }
    html = edit_html + delete_html;
    return html;
}
function deleteData(id) {
    $('#del').modal("show");
    alert(id);
}
function del(id) {
    $("#btn_del").bind("click", function () {
        var param = {
            "smsReceiveReplyPushId": id

        };
        ajaxCall({
            url: "/smsReceiveReplyPush/from/delete.do",
            type: "get",
            data: param,
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    $('#tb_data').bootstrapTable('refresh', {
                        url: 'blacklist.do?menuId=' + $("#menu_id").val(),
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
