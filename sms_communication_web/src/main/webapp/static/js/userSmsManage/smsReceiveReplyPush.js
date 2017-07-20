var page_num = 1;
$(function() {
	$('#start_input').datetimepicker({
		lang : 'ch',
		format : "Y-m-d H:i",
		// timeFormat: 'hh:mm:ss',
		timepicker : true, // 关闭时间选项
		yearStart : 2000, // 设置最小年份
		yearEnd : 2050, // 设置最大年份
	// dateFormat:'yy-mm-dd',
	// showSecond: true, //显示秒
	// timeFormat: 'HH:mm:ss',//格式化时间
	// stepHour: 1,//设置步长
	// stepMinute: 1,
	// stepSecond: 1
	});// 设置中文;
	$('#end_input').datetimepicker({
		lang : 'ch',
		format : "Y-m-d H:i",
		// timeFormat: 'hh:mm:ss',
		timepicker : true, // 关闭时间选项
		yearStart : 2000, // 设置最小年份
		yearEnd : 2050, // 设置最大年份
	});// 设置中文;
	$.datetimepicker.setLocale('ch');//设置中文
	loadUser();

	$("#add_sysuser").bind("click", function() {
		
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

	$("#save_sysuser_btn").bind("click", function() {
		save_sysuser();
	});
	$("#search_btn").bind("click", function() {
		$('#tb_data').bootstrapTable( 'removeAll' );
		$('#tb_data').bootstrapTable('refresh', {
			url : 'blacklist.do?menuId=' + $("#menu_id").val(),
			queryParams : queryParamsVal
		});
	});

});

function loadUser() {
	$('#tb_data').bootstrapTable({
		url : 'pushlist.do?menuId=' + $("#menu_id").val(),
		dataType : "json",
		cache : false,
		striped : true,
		pagination : true,
		clickToSelect : true,
		sidePagination : "server", // 服务端处理分页
		queryParams : queryParamsVal, // 参数
		method : "post",
		columns : [ {
			title : "客户名称",
			field : "name",
			align : "left",
			valign : "middle",
			sortable : "true",
		}, {
			title : "账号",
			field : "email",
			align : "left",
			valign : "middle",
			sortable : "true",
		}, {
			title : "接收号码",
			field : "phone",
			align : "left",
			valign : "middle",
			sortable : "true",
		}, /*{
			title : "归属地",
			field : "region",
			align : "left",
			valign : "middle",
			sortable : "true"
		}, */
		{
			title : "通道名称",
			field : "aname",
			align : "left",
			valign : "middle",
			sortable : "true"
		}, 
		{
			title : "下发内容",
			field : "contents",
			align : "left",
			valign : "middle",
			sortable : "true"
		},{
			title : "推送状态",
			field : "state",
			align : "left",
			valign : "middle",
			sortable : "true",
            formatter: function (value, row, index) {
            	if(row.state==null){
            		return null;
            	}else if (row.state==1) {
                	return "未推送";
                }else if(row.state==2){
                	return "已推送";
                }
            }
		}, {
			title : "回复内容",
			field : "content",
			align : "left",
			valign : "middle",
			sortable : "true"
		}, {
			title : "接收时间",
			field : "createTime",
			align : "left",
			valign : "middle",
			sortable : "true"
		}, {
			title : '操作',
			align : 'left',
			valign : "middle",
			formatter : function(value, row, index) {

				return loadBtn(row.sysMenuBtns, row.id);
			}
		} ],
		formatNoMatches : function() {
			return '无符合条件的记录';
		}
	});

}

function queryParamsVal(params) { // 配置参数
	var temp = { // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
		pagesize : params.limit, // 页面大小
		pagenum : (params.offset / params.limit) + 1, // 页码
		// minSize: $("#leftLabel").val(),
		// maxSize: $("#rightLabel").val(),
		// minPrice: $("#priceleftLabel").val(),
		// maxPrice: $("#pricerightLabel").val(),
		// Cut: Cut,
		// Color: Color,
		// Clarity: Clarity,
		sort : params.sort, // 排序列名
		order : params.order,// 排位命令（desc，asc）
		emailInput : $("#email_input").val(),// 账号
		nameInput : $("#name_input").val(),// 客服名字
		sysUserName : $("#user_input").val(),// 搜索框 号码
		contentInput : $("#content_input").val(),// 回复内容
		startInput : $("#start_input").val(),// 开始时间
		endInput : $("#end_input").val()
	// 结束时间
	// 结束时间

	};
	return temp;
}

function searchParam(pageNum) {
	var param = {};
	pageInfo(param, pageNum, PAGE_SIZE);
	return param;
}

/* 加载数据 */
function loadData(param) {
	ajaxCall({
		url : '/userSmsReceiveReplyPush/blacklist.do',
		type : "post",
		data : JSON.stringify(param),
		success : function(data) {
			if (checkRes(data)) {
				var arr = data["data"]["dataList"];
				var total = data["data"]["total"];
				var btn_arr = data["data"]["data"];
				buildTable(arr, btn_arr);
				$("#paging ul").empty();
				paging(+total, param["pagenum"], param["pagesize"]);
			}

		},
		error : function() {

		},
		complete : function() {

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
	$("#btn_del").bind("click", function() {
		var param = {
			"smsReceiveReplyPushId" : id

		};
		ajaxCall({
			url : "/smsReceiveReplyPush/from/delete.do",
			type : "get",
			data : param,
			success : function(data) {
				if (checkRes(data)) {
					$("#del").modal("hide");
					$('#tb_data').bootstrapTable('refresh', {
						url : 'blacklist.do?menuId=' + $("#menu_id").val(),
						queryParams : queryParamsVal
					});
				} else {
					$('#delmodal').modal("show");
				}
			},
			error : function() {
			},
			complete : function() {
			}
		});
	});
}
