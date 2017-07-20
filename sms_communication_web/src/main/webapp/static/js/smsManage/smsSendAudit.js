// var ChineseDistricts_city = {};
var quuids ="";
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
   
    $("#btn_del").bind("click", function () {
    	var type = $("#is_type").val();
    	 $("#del_h4_title2").html("确定审核");
    	if(type==1){
    		$("#del_p_title2").html("确认审核通过?");
    	}else{
    		$("#del_p_title2").html("确认审核不通过?");
    	}
    	 $('#del2').modal("show");
    	 $("#del").modal("hide");
    });
    
    
    $("#btn_del2").bind("click", function () {
    	var type = $("#is_type").val();
    	var datas = {
    	        "id": quuids+"",
    	        "type":type
    	    };
    	var sCount = $("#s_count").val();
    	var iAisleGroup = $("#i_aisleGroup").val();
    	
    	var  selectId= $("#i_selectId").val();
    	
    	if(sCount!=null && sCount!=""){
    		datas["sCount"]=sCount;
    		datas["iAisleGroup"]=iAisleGroup;
    		datas["smsUserId"]=selectId;
    		if($('#i_qmbb').is(':checked')) {
    			var qm =sCount.substring(sCount.indexOf("【")+1,sCount.indexOf("】"));
    			datas["qmbb"]=qm;
    		}
    		if($('#i_mrms').is(':checked')) {
    			var qmnr =sCount.substring(sCount.indexOf("】")+1,sCount.length);
    			datas["qmnr"]=qmnr;
    		}
    	}
    	ajaxCall({
    	       url: "/smsAudit/auditSms.do",
    	       type: "post",
    	       data: JSON.stringify(datas),
    	       success: function (data) {
    	            if (checkRes(data)) {
    	                $('#tb_data').bootstrapTable('destroy');
    	                load_data();
    	            }else{
    	            	alert("审核失败！");
    	            }
    	        },
    	        error: function () {
    	        },
    	        complete: function () {
    	        }
    	    });
    	$("#del2").modal("hide");
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
	
	var state = $("#state_select").val();
    $('#tb_data').bootstrapTable({
        url: 'puserList.do?menuId=' + $("#menu_id").val(),
        dataType: "json",
        cache: false,
        striped: true,  
        pagination: true,
        ccext:true,// 自定义样式,为true 跨行显示短信内容
        lines:13, //跨多少行
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: [
              {
                  title: "全选",
                  field: "select",
                  align: "center",
                  valign: "middle",
                  checkbox: state==0?true:false
              }, 
            {
                title: "批次编号",
                field: "id",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function(value,row,index){
                	var i =row.id;
                	var c="";
                	 if (!row.createTime) {
                         c= '!' +row.content+"&nbsp;&nbsp;&nbsp;&nbsp;(共"+row.content.length+"个字)";
                     }else{
                    	 c= '!' +row.content+"<br/>提交时间："+new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss")+"&nbsp;&nbsp;&nbsp;&nbsp;(共"+row.content.length+"个字)";
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
           /* {
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
            },*/
            {
                title: "账户",
                field: "smsUserEmail",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "通道组",
                field: "groupName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "发送类型",
                field: "sendType",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (!row.sendType || row.sendType==0) {
                    	return "马上发送";
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
                title: "审核时间",
                field: "auditTime",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (!row.auditTime) {
                        return "";
                    }
                    return new Date(row.auditTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                title: '操作',
                align: 'left',
                valign: "middle",
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id,row.auditState);
                }
            }
            ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}


//加载菜单对应的按钮
function loadBtn(btn_arr, id,auditState) {
    var html = '', edit_html = '', delete_html = '',add_html="";
    if (btn_arr.length > 0) {
        var user_path = '/smsAudit/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            
           
            if (btn_obj["actionUrls"] == user_path + 'edit.do') {
            	edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i>导出号码</a> ';
            	
            }
          /*  if(auditState==0){
	            if (btn_obj["actionUrls"] == user_path + 'add.do') {
	                $("#add_button").show();
	                add_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="auditData(\'' + id + '\',\'0\');"><i class="fa fa-paste"></i>不通过</a> ';
	            }
	            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
	                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="auditData(\'' + id + '\',\'1\');"><i class="fa fa-paste"></i>审核通过</a> ';
	            }
            }*/
        }
    }
    html = edit_html + delete_html+add_html;
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


function auditData(ids,type){
	    ajaxCall({
	        url: "/smsAudit/auditSms.do?id=" + ids+"&type="+type,
	        type: "get",
	        success: function (data) {
	            if (checkRes(data)) {
	            	alert("审核成功！");
	                $('#tb_data').bootstrapTable('destroy');
	                load_data();
	            }else{
	            	alert("审核失败！");
	            }
	        },
	        error: function () {
	        },
	        complete: function () {
	        }
	    });
}


/**
 * 全选审核
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
        var tids = $.map($table.bootstrapTable('getSelections'), function (row) {  //通道组类型ID
            if(row.superAdmin == 1){
                return null;
            }
            return row.tid;
        });
        var groupIds = $.map($table.bootstrapTable('getSelections'), function (row) {  //通道组ID
            if(row.superAdmin == 1){
                return null;
            }
            return row.groupId;
        });
        var contents = $.map($table.bootstrapTable('getSelections'), function (row) {  //通道组ID
            if(row.superAdmin == 1){
                return null;
            }
            return row.content;
        });
        var smsUserIds =$.map($table.bootstrapTable('getSelections'), function (row) {  //通道组ID
            if(row.superAdmin == 1){
                return null;
            }
            return row.smsUserId;
        });
        if(uids==""){
            alert("请先选择需要审核的项!");
            return;
        }
        deleteUsers(uids,tids,groupIds,contents,smsUserIds);
    });
});


//批量删除系统用户
function deleteUsers(uids,tids,groupIds,contents,smsUserIds){
	if(uids.length==1){
		$("#i_modal-content").css("height", "420px");
		$("#s_count").val(contents);
		querySmsGroup(tids,groupIds)
		$("#i_qmbb").prop("checked", false);//不选中
    	$("#i_mrms").prop("checked", false);//不选中
    	$("#i_selectId").val(smsUserIds);
		$("#shDiv").show();
	}else{
		$("#i_modal-content").css("height", "200px");
		$("#s_count").val("");
		var aisleGroup = $("#i_aisleGroup");
		aisleGroup.html("");
		$("#i_selectId").val("");
    	$("#i_qmbb").prop("checked", false);//不选中
    	$("#i_mrms").prop("checked", false);//不选中
		$("#shDiv").hide();
	}
	 $('#del').modal("show");
    quuids=uids;
    
}


function querySmsGroup(p1,type){
	
	 var data={};
    ajaxCall({
        url: "/puser/querySmsGroup.do?gid="+p1,
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
            	var obj = data["data"]
            	var aisleGroup = $("#i_aisleGroup");
            	var htl = "";
            	for (var i = 0; i < obj.length; i++) {
						var ss = obj[i];
						htl+="<option value=\""+ss["id"]+"\">"+ss["name"]+"</option>";
					}
            	aisleGroup.html(htl);
            }
            
            if(type!=null){
           	 $("#i_aisleGroup").val(type);
            }
        },
        error: function () {
            $(".ibox-content").prepend(alertview("danger", "操作失败."));
        },
        complete: function () {
        }
    });
}