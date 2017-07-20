// var ChineseDistricts_city = {};
$(function () {
	var idss = $("#ids_input").val();
	if(idss==null || idss==""){
		var dateTime =new Date().Format("yyyy-MM-dd");
		$("#start_input").val(dateTime+" 00:00:00");
		$("#end_input").val(dateTime+" 23:59:59");
	}
	
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
    
    
    $("#reset").bind("click", function () {
       // $('#phone_input').val("");
        $('#start_input').val("");
        $('#end_input').val("");
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
        content: $("#content_input").val(), 
        state: $("#state_select").val(), 
        ids: $("#ids_input").val(), 
        smsUser: $("#smsUser_input").val(), 
        sendType: $("#sendType_select").val(), 
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'puserDetailsList.do?menuId=' + $("#menu_id").val(),
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
                title: "批次编号",
                field: "id",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "接收号码",
                field: "receivePhone",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "归属地",
                field: "region",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
           /* {
                title: "运营商",
                field: "supplier",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	if(row.supplier==null){
                		return null;
                	}else if (row.supplier==0) {
                    	return "联通";
                    }else if(row.supplier==1){
                    	return "移动";
                    }else if(row.supplier==2){
                    	return "电信";
                    }
                }
            },*/
            {
                title: "发送状态",
                field: "state",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) { //状态:-1：待发送,0：已提交，2：发送失败，3未知，21; //网络错误，22; //空号错误，23; //发送上限，24; //黑名单错误，25：审核失败，26; //发送失败，27; 通道拒绝接收，31:通道返回失败，99; //状态报告为未知，100：成功
                	if(row.state==null){
                		return null;
                	}else if (row.state==-1) {
                    	return "待发送";
                    }else if(row.state==0){
                    	return "已发送";
                    }else if(row.state==2 || row.state==26){
                    	return "发送失败";
                    }else if(row.state==3){
                    	return "未知";
                    }else if(row.state==21){
                    	return "网络错误";
                    }else if(row.state==22){
                    	return "空号错误";
                    }else if(row.state==23){
                    	return "发送上限";
                    }else if(row.state==24){
                    	return "黑名单错误";
                    }else if(row.state==25){
                    	return "审核失败";
                    }else if(row.state==27){
                    	return "通道拒绝接收";
                    }else if(row.state==31){
                    	return "通道返回失败";
                    }else if(row.state==99){
                    	return "状态报告为未知";
                    }else if(row.state==100){
                    	return "发送成功";
                    }
                }
            },
            {
                title: "计费条数",
                field: "billingNnum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
           {
                title: "发送时间",
                field: "sendTime",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (!row.sendTime) {
                        return "";
                    }
                    return new Date(row.sendTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
           {
                title: "状态反馈时间",
                field: "feedbackTime",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (!row.feedbackTime) {
                        return "";
                    }
                    return new Date(row.feedbackTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            }
/*            {
                title: "内容",
                field: "content",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	 if (!row.createTime) {
                         return row.content;
                     }
                 	return row.content+"<br/>提交时间："+row.createTime;//new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            }*/
            ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}

//加载菜单对应的按钮
function loadBtn(btn_arr, id) {
    var html = '', edit_html = '', delete_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/msmSend/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            
            if (btn_obj["actionUrls"] == user_path + 'add.do') {
                $("#add_button").show();
              /*  edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 新增</a> ';*/
            }
            if (btn_obj["actionUrls"] == user_path + 'edit.do') {
            	edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i>导出号码</a> ';
            	
            }
            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + id + '\');"><i class="fa fa-paste"></i>详情</a> ';
            }
        }
    }
    html = edit_html + delete_html;
    return html;
}

function add0(m){return m<10?'0'+m:m }
function format(shijianchuo)
{
//shijianchuo是整数，否则要parseInt转换
var time = new Date(shijianchuo);
var y = time.getFullYear();
var m = time.getMonth()+1;
var d = time.getDate();
var h = time.getHours();
var mm = time.getMinutes();
var s = time.getSeconds();
return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}

function editData(id) {
    $(".fileinput-remove-button").trigger("click");
    $('#add_data_div').modal("show");
    ajaxCall({
        url: "/userMsmSend/export/csv.do?id=" + id,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
            	alert("导出成功！");
            }else{
            	alert("导出失败！");
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}
