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
        aisleName:$("#aisleName_input").val(), //账号信息
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'puserStatisticalList.do?menuId=' + $("#menu_id").val(),
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
                title: "通道",
                field: "smsAisleName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "通道类型",
                field: "aisleGroupType",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "发送号码数",
                field: "sendNum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "消费计费条数",
                field: "billingNum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "结算计费条数",
                field: "actualNum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
        	    title: "移动发送成功\\失败\\未知",
                field: "succeedNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
            		/*if(index==0){
            			$("#i_snum").html(row.sumSendNum);
            			$("#i_bnum").html(row.sumBillingNum);
            			$("#i_anum").html(row.sumActualNum);
                		$("#i_fnumUs").html(row.sumSucceedNumUs+"/"+row.sumFailureNumUs+"/"+row.sumUnknownNumUs);
                		$("#i_fnumMs").html(row.sumSucceedNumMs+"/"+row.sumFailureNumMs+"/"+row.sumUnknownNumMs);
                		$("#i_fnumTs").html(row.sumSucceedNumTs+"/"+row.sumFailureNumTs+"/"+row.sumUnknownNumTs);
            		}*/
                    return row.succeedNumUs+"\\"+row.failureNumUs+"\\"+row.unknownNumUs;
                }
             
            },
            {
        	    title: "联通发送成功\\失败\\未知",
                field: "succeedNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    return row.succeedNumMs+"\\"+row.failureNumMs+"\\"+row.unknownNumMs;
                }
             
            },
            {
        	    title: "电信发送成功\\失败\\未知",
                field: "succeedNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    return row.succeedNumTs+"\\"+row.failureNumTs+"\\"+row.unknownNumTs;
                }
             
            }
            ],
        
            responseHandler: function(a) {
            	var btn_arr = a["data"];
            	if (btn_arr!=null) {
	            		$("#i_snum").html(btn_arr["sumSendNum"]);
            			$("#i_bnum").html(btn_arr["sumBillingNum"]);
            			$("#i_anum").html(btn_arr["sumActualNum"]);
                		$("#i_fnumUs").html(btn_arr["sumSucceedNumUs"]+"/"+btn_arr["sumFailureNumUs"]+"/"+btn_arr["sumUnknownNumUs"]);
                		$("#i_fnumMs").html(btn_arr["sumSucceedNumMs"]+"/"+btn_arr["sumFailureNumMs"]+"/"+btn_arr["sumUnknownNumMs"]);
                		$("#i_fnumTs").html(btn_arr["sumSucceedNumTs"]+"/"+btn_arr["sumFailureNumTs"]+"/"+btn_arr["sumUnknownNumTs"]);
            	}else{
            		$("#i_snum").html(0);
        			$("#i_bnum").html(0);
        			$("#i_anum").html(0);
            		$("#i_fnumUs").html(0+"/"+0+"/"+0);
            		$("#i_fnumMs").html(0+"/"+0+"/"+0);
            		$("#i_fnumTs").html(0+"/"+0+"/"+0);
            	}
                return a;
            },
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

