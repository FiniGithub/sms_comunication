// var ChineseDistricts_city = {};
var package_id;
$(function () {
	path = $("i_path").val();
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
    
    $("#package_btn").bind("click", function () {
    	$('#package_data').bootstrapTable('destroy');
    	package_data(package_id);
    })
    
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
        ccext:true,// 自定义样式,为true 跨行显示短信内容
        lines:12, //跨多少行
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
                	var i ='<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="smsSendDetails(\'' +  row.id + '\');"><i class="fa fa-paste"></i>'+ row.id+'</a> ';
                	var c="";
                	 if (!row.createTime) {
                         c= '!' +row.content +"&nbsp;&nbsp;&nbsp;&nbsp;(共"+row.content.length+"个字)";
                     }else{
                    	 c= '!' +row.content+"<br/>提交时间："+new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss")+"&nbsp;&nbsp;&nbsp;&nbsp;(共"+row.content.length+"个字)";;
                     }
                	return i + c;
                }
            },
            {
                title: "团队",
                field: "temaName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "客户名称",
                field: "smsUserName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "账户",
                field: "smsUserEmail",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "发送量",
                field: "sendNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	var num1 = row.sendNum==null?"":row.sendNum; 
                    return num1;
                }
            },
            {
                title: "计费量",
                field: "sendNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	var num2 = row.billingNum==null?"":row.billingNum; 
                    return num2;
                }
            },
            {
                title: "结算条数",
                field: "actualNum",
                align: "left",
                valign: "middle",
                sortable: "true"
               
            },
            {
                title: "发送状态",
                field: "state",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	if(row.state==null){
                		return null;
                	}else if (row.state==-1) {
                    	return "等待发送";
                    }else if(row.state==0){
                    	return "正在发送";
                    }else if(row.state==9){
                    	return "人工终止";
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
           /* {
                title: "通道组",
                field: "groupName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },*/
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
                title: '操作',
                align: 'left',
                valign: "middle",
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id,row.state,row.sendTime,row.tid,row.sendResendState);
                }
            }
            ],
            responseHandler: function(a) {
            	var btn_arr = a["data"];
            	if (btn_arr!=null) {
            			$("#i_sendNumCount").html(btn_arr["sendNumCount"]);
	            		$("#i_billingNumCount").html(btn_arr["billingNumCount"]);
	            		$("#i_actualNumCount").html(btn_arr["actualNumCount"]);
            	}else{
            		$("#i_sendNumCount").html(0);
            		$("#i_billingNumCount").html(0);
            		$("#i_actualNumCount").html(0);
            	}
                return a;
            },
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}

//加载菜单对应的按钮
function loadBtn(btn_arr,id,state,sendTime,tid,sendResendState) {
    var html = '', edit_html = '', delete_html = '',subtask_html = '',resend_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/msmSend/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            
            if (btn_obj["actionUrls"] == user_path + 'add.do') {
               /* $("#add_button").show();*/
            	 if(sendTime!=null){
                 	var newTimes =   new Date()  //得到当前时间
                 	var m=parseInt(Math.abs(newTimes-sendTime)/1000/60);
                     if(state<100 && m >10 && (m/60)<12 && (tid==2 || tid==3) && sendResendState==0){
                    	 resend_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="resendData(\'' + id + '\',\''+tid+'\');"><i class="fa fa-paste"></i>重发</a> ';
                     }
                 }
              
            }
            if (btn_obj["actionUrls"] == user_path + 'edit.do') {
            	edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i>导出号码</a> ';
            	
            }
            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="smsSendDetails(\'' + id + '\');"><i class="fa fa-paste"></i>详情</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'subtask.do') {
            	subtask_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="subtaskData(\'' + id + '\');"><i class="fa fa-paste"></i> 子任务</a> ';
            }
            
        }
    }
    html = subtask_html+edit_html + delete_html+resend_html;
    return html;
}


function resendData(id,tid){
	
	 var data = {};
    ajaxCall({
        url: "/msmSend/querySmsAisle.do?tid="+tid,
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
            	var list =data["data"];
            	var htl = '<option value="">请选择</option>';
            	for (var i = 0; i < list.length; i++) {
            		
            		htl+='<option value="'+list[i]["id"]+'">'+list[i]["name"]+'</option>';
				}
            	$("#i_smsAisle").html(htl);
            }
        },
        error: function () {
            $(".ibox-content").prepend(alertview("danger", "操作失败."));
        },
        complete: function () {
        }
    });
    
    $("#i_type").val(0);
    $("#i_smsState").val("");
    $("#div_sendPhong").hide();
    $("#div_smsState").show();
    $("#btnFile").val("");
    $("#sendResend_id").val(id);
    $("#msmSendResend").modal("show");
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


function subtaskData(id){
	if(id!=null){
		package_id = id;
		$('#package_data').bootstrapTable('destroy');
		package_data(id);
		plimit=1;
		$("#msmSendPackage").modal("show");
	}
}

function queryParamsVal2(params) {  //配置参数
	 var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
		        pagesize: params.limit,   //页面大小
		        pagenum: (params.offset / params.limit) + 1,  //页码
		        sort: params.sort,  //排序列名
		        order: params.order,//排位命令（desc，asc）
		        smsAisleName: $("#smsAisle_input").val()
		    };
	 return temp;
}

function package_data(id) {
    $('#package_data').bootstrapTable({
        url: 'sendPackageList.do?menuId=' + $("#menu_id").val()+'&skid='+id,
        dataType: "json",
        cache: false,
        striped: true,
        pagination: true,
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal2, //参数
        columns: [
            {
                title: "编号",
                field: "id",
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
                title: "发送号码数",
                field: "phoneNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function(value,row,index){
                	if(row.phoneNum!=null && row.phoneNum>0){
                		return '<a class="btn btn-sm btn-white" style="color: #2A00FF" href="javaScript:void(0);" onclick="phoneAll('+row.id+')">'+ row.phoneNum+'</a> ';
                	}else{
                		return 0;
                	}
               	 
               }
            },
            {
                title: "内容",
                field: "content",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	 if (!row.createDate) {
                         return row.content;
                     }
                 	return row.content+"<br/>提交时间："+new Date(row.createDate).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                title: "状态",
                field: "state",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
               	 if (row.state!=null && row.state==0) {
                        return "发送成功";
                    }else if(row.state==1){
                    	 return "发送失败";
                    }
               }
            },
            {
                title: "描述",
                field: "describe",
                align: "left",
                valign: "middle",
                sortable: "true"
            }
            ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}

function phoneAll(id){
	phoneAllLoad(id);
}


function editData(id) {
	download(id);
  /* $(".fileinput-remove-button").trigger("click");
    $('#add_data_div').modal("show");
    ajaxCall({
        url: "/msmSend/export/csv.do?id=" + id,
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


function smsSendDetails(ids) {
	$("#ids_dis").val("");
	$("#ids_dis").val(ids);
	$("#allotmerge_form").submit();
}


function btnChange(values){
	if(values==0){
	  $("#i_smsState").val("");
	  $("#div_smsState").show();
	  $("#div_sendPhong").hide();
	  $("#btnFile").val("");
	}else if(values==1){
	  $("#i_smsState").val("");
	  $("#div_smsState").hide();
	  $("#btnFile").val("");
	  $("#div_sendPhong").show();
	}
}


//上传文件  
function smsSendResend(){  
	var  sendId= $("#sendResend_id").val();
	if(sendId==null || sendId==""){
		return;
	}
	var smsAisle = $("#i_smsAisle").val();
	var type =$("#i_type").val();
	if(smsAisle==null || smsAisle==""){
		alert("请选择发送通道！");
		return;
	}
	
	if(type==1){
		var formData = new FormData($("#uploadFile")[0]);
		var file = $("#txtFoo").val();
		if (file == null || file == '') {
			alert("请选择上传文件！");
			return;
		}
		$.ajax({
			url : '../msmSend/smsSendResend.do',
			type : 'POST',
			data : formData,
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(obj) {
				if (checkRes(obj)) {
					saveSendResend(sendId,type,smsAisle,null);
				} else if(obj["retCode"] == "000002"){
					alert("无上传号码！");
				}else{
					alert("上传失败！");
				}
			},
			error : function(data) {
				alert("上传失败");
			}
		});
	}else{
		var smsState =$("#i_smsState").val();
		if(smsState==null || smsState==""){
			alert("请选择转发任务状态！");
			return;
		}
		saveSendResend(sendId,type,smsAisle,smsState);
	}
}; 

function saveSendResend(sendId,type,smsAisle,smsState){
	var data = {
		"sendId":sendId,
		"type":type,
		"smsAisle":smsAisle
	}
	if(type==0){
		data["smsState"] = smsState;
	}
    ajaxCall({
        url: "/msmSend/saveSendResend.do?menuId="+$("#menu_id").val(),
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
              $("#msmSendResend").modal("hide");
              alert("重发成功！");
              $('#tb_data').bootstrapTable('destroy');
              load_data();
            }else if(data["retCode"]=="000002"){
                alert("任务已经重发，无法再次发送！");
            }else{
            	alert("重发失败！");
            }
        },
        error: function () {
            $(".ibox-content").prepend(alertview("danger", "操作失败."));
        },
        complete: function () {
        }
    });
	
}

