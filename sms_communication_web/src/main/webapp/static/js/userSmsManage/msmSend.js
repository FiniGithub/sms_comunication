// var ChineseDistricts_city = {};
$(function () {
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
        url: 'puserList.do?menuId=' + $("#menu_id").val(),
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
                sortable: "true",
                formatter: function(value,row,index){
               	 return '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="smsSendDetails(\'' +  row.id + '\');"><i class="fa fa-paste"></i>'+ row.id+'</a> ';
               }
            },
           
           
            {
                title: "发送类型",
                field: "sendType",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (!row.sendType || row.sendType==0) {
                    	return "立即发送";
                    }else if(row.state==1){
                    	return "定时发送";
                    }
                }
            },
            {
                title: "发送量",
                field: "sendNum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "计费量",
                field: "billingNum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "结算量",
                field: "actualNum",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            
            {
                title: "审核状态",
                field: "auditState",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	if(row.auditState==null){
                		return null;
                	}else if (row.auditState==0) {
                    	return "等待审核";
                    }else if(row.auditState==1){
                    	return "自动通过";
                    }else if(row.auditState==2){
                    	return "人工通过";
                    }else if(row.auditState==3){
                    	return "人工拒绝";
                    }else if(row.auditState==4){
                    	return "终止审核";
                    }
                }
            },
            
            {
                title: "发送状态",
                field: "state",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	
                	 if(index==0){
  	            		$("#i_sendNumCount").html(row.sendNumCount);
  	            		$("#i_billingNumCount").html(row.billingNumCount);
  	            		$("#i_actualNumCount").html(row.actualNumCount);
  	            	}
                	
                	if(row.state==null){
                		return null;
                	}else if (row.state==-1) {
                    	return "等待发送";
                    }else if(row.state==0){
                    	return "正在发送";
                    }else if(row.state==9){
                    	return "终止发送";
                    }else if(row.state==100){
                    	return "发送完成";
                    }else if(row.state==101){
                    	return "已结算";
                    }
                }
            },
            {
                title: "错误\\黑名单",
                field: "errorPhoneNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	var num1 = row.errorPhoneNum==null?"":row.errorPhoneNum; 
            		var num2 = row.blacklistPhoneNum==null?"":row.blacklistPhoneNum; 
                    return num1+"\\"+num2;
                }
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
           /* {
                title: "更新时间",
                field: "updateTime",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (!row.updateTime) {
                        return "";
                    }
                    return new Date(row.updateTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            },*/
            {
                title: "内容",
                field: "content",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	 if (!row.createTime) {
                         return row.content;
                     }
                 	return row.content+"<br/>提交时间："+new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                title: '操作',
                align: 'left',
                valign: "middle",
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id,row.state);
                }
            }
            ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}

//加载菜单对应的按钮
function loadBtn(btn_arr, id,state) {
    var html = '', edit_html = '', delete_html = '',add_html='';
    if (btn_arr.length > 0) {
        var user_path = '/userMsmSend/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            if(state==-1){
	            if (btn_obj["actionUrls"] == user_path + 'add.do') {
	                $("#add_button").show();
	                add_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="stopData(\'' + id + '\');"><i class="fa fa-paste"></i>终止发送</a> ';
	            }
            }
            if (btn_obj["actionUrls"] == user_path + 'edit.do') {
            	edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i>导出号码</a> ';
            	
            }
            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="smsSendDetails(\'' + id + '\');"><i class="fa fa-paste"></i>详情</a> ';
            }
        }
    }
    html = edit_html + delete_html +add_html;
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
	download(id)
   /* $(".fileinput-remove-button").trigger("click");
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
    });*/
}



function stopData(id) {
    ajaxCall({
        url: "/userMsmSend/stopSmsSend.do?id=" + id,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
            	alert("操作成功！");
            	  $('#tb_data').bootstrapTable('destroy');
	                load_data();
            }else{
            	alert("操作失败！");
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}

function smsSendDetails(ids) {
	$("#ids_dis").val("");
	$("#ids_dis").val(ids);
	$("#allotmerge_form").submit();
	
	
   /* ajaxCall({
        url: "/msmSend/smsSendDetails.do?ids="+ids+"&menuId="+$("#menu_id").val(),
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
            	
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });*/
}
