// var ChineseDistricts_city = {};
$(function () {
	var dateTime =new Date().Format("yyyy-MM-dd");
	$("#start_input").val(dateTime+" 00:00:00");
	$("#end_input").val(dateTime+" 23:59:59");
	
    $('#start_input').datetimepicker({
        lang: 'ch',
        format: "Y-m-d H:i",
        // timeFormat: 'hh:mm:ss',
        timepicker: true,    //关闭时间选项
        yearStart: 2000,     //设置最小年份
        yearEnd: 2050,        //设置最大年份
        // dateFormat:'yy-mm-dd',
        // showSecond: true, //显示秒
        // timeFormat: 'HH:mm:ss',//格式化时间
        // stepHour: 1,//设置步长
        // stepMinute: 1,
        // stepSecond: 1
    });//设置中文;
    $('#end_input').datetimepicker({
        lang: 'ch',
        format: "Y-m-d H:i",
        // timeFormat: 'hh:mm:ss',
        timepicker: true,    //关闭时间选项
        yearStart: 2000,     //设置最小年份
        yearEnd: 2050,        //设置最大年份
    });//设置中文;
    $.datetimepicker.setLocale('ch');//设置中文
    load_data();

    $("#search_btn").bind("click", function () {
    	refresh_data();
    });
    
});




//刷新数据
function refresh_data() {
    var start_input = $('#start_input').val();
    var end_input = $('#end_input').val();
    //开始时间不为空
    if (start_input!="" && end_input=="") {
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
        email: $("#user_input").val(), //账号信息
        type: $("#i_type").val(),      //类型
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}

function load_data() {
	$("#i_snum").html(0+"条");
	$("#i_fnum").html(0+"条");
	$("#i_unum").html(0+"条");
    $('#tb_data').bootstrapTable({
        url: 'puserBillList.do?menuId=' + $("#menu_id").val(),
        dataType: "json",
        cache: false,
        striped: true,
        pagination: true,
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: [
            {
                title: "账单编号",
                field: "id",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "客户名称",
                field: "name",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "账号",
                field: "email",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "交易前条数",
                field: "beforeNum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "变动条数",
                field: "operateNum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "交易后条数",
                field: "afterNum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
            	title: "交易类型",
                field: "type",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
            		if(index==0){
            			$("#i_snum").html(row.consumeNum+"条");
                		$("#i_fnum").html(row.topUpNum+"条");
                		$("#i_unum").html(row.refundNum+"条");
            		}
            		  if (!row.type || row.type==0) {
                      	return "充值";
                      }else if(row.type==1){
                      	return "消费";
                      }else if(row.type==2){
                      	return "返还";
                      }
                }
             
            },
            {
        	    title: "描述",
                field: "comment",
                align: "left",
                valign: "middle",
                sortable: "true",
            },
            {
                title: "创建时间",
                field: "createTime",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (!row.createTime) {
                        return "";
                    }
                    return new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}

//加载菜单对应的按钮
function loadBtn(btn_arr, id,name,money) {
    var html = '', edit_html = '', delete_html = '',add_html='';
    if (btn_arr.length > 0) {
        var user_path = '/msmuUser/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            
            if (btn_obj["actionUrls"] == user_path + 'add.do') {
                $("#add_button").show();
                add_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="editjeData(\'' + id + '\',\'' + name + '\',\'' + money + '\');"><i class="fa fa-paste"></i>充值</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'edit.do') {
            	edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + id + '\');"><i class="fa fa-paste"></i>分配通道组</a> ';
            }
        }
    }
    html = edit_html + delete_html+add_html;
    return html;
}