// var ChineseDistricts_city = {};
$(function () {
	
	var idss = $("#ids_input").val();
	if(idss==null || idss==""){
		var dateTime =laydate.now(-1);
		$("#time_input").val(dateTime+"");
	}
	var startday = {//历史
	        elem:'#time_input',
	        format: 'YYYY-MM-DD',
	        max: laydate.now(-1), //设定最大日期为当前日期
	        istime: false,
	        istoday: true,
	        issure: false
	    };
	 laydate.skin('yahui');
	 if($("#time_input").length==1){
		 laydate(startday);
	 }
	 
   
    load_data();
   
    $("#search_btn").unbind("click");
    $("#search_btn").bind("click", function () {
    	refresh_data();
    	$('#tb_data').bootstrapTable('hideColumn', 'fkState');
    	$('#tb_data').bootstrapTable('hideColumn', 'feedbackTime');
    });
    
    $("#refresh_btn").unbind("click");
    $("#refresh_btn").bind("click", function () {
    	refresh_data();
    	$('#tb_data').bootstrapTable('hideColumn', 'fkState');
    	$('#tb_data').bootstrapTable('hideColumn', 'feedbackTime');
    });
    
    $("#export_btn").unbind("click");
    $("#export_btn").bind("click", function () {
    	orderExport();
    });
    
    $("#reset").unbind("click");
    $("#reset").bind("click", function () {
        // $('#phone_input').val("");
         $('#time_input').val("");
     });
    
});

function orderExport() {
	var data = {
		"smsUser" : $("#user_input").val(), // 账号信息
		"phone" : $("#phone_input").val(), // 号码
		"content" : $("#content_input").val(),// 内容
		"state" : $("#state_select").val(),// 状态
		"startInput" : $("#time_input").val(),// 开始时间
		"endInput" : $("#time_input").val()
	// 结束时间
	};
	
	$.post(
			$("#path").val() + "/msmSend/queryOrderExport.do?logTime=" + $("#logTime").val(),
		data,
 		function(data) {
			if(data["retCode"]=="000000"){
				window.location.href=$("#path").val() + "/msmSend/orderExport.do?logTime=" + $("#logTime").val();
			}else{
				alert("没有可以导出数据");
			}
			
	});

	}

//刷新数据
function refresh_data() {
	$('#tb_data').bootstrapTable('hideColumn', 'fkState');
	$('#tb_data').bootstrapTable('hideColumn', 'feedbackTime');
	
	var time_input = $('#time_input').val();
    //开始时间不为空
    if (time_input=="") {
            alert("请选择时间！");
            return;
    }
    
    $('#tb_data').bootstrapTable('destroy');
    load_data();
}

function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        smsUser: $("#user_input").val(), //账号信息
        phone:$("#phone_input").val(), //号码
        content: $("#content_input").val(),//内容
        state:$("#state_select").val(),//状态
        startInput: $("#time_input").val(),//开始时间
        endInput: $("#time_input").val()//结束时间
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
    	url: 'log.do?menuId=' + $("#menu_id").val() + '&logTime=' + $("#logTime").val(),
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
        ccext:false,// 自定义样式,为true 跨行显示短信内容
        lines:13, //跨多少行
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: [
            {
                title: "账号",
                field: "smsUserEmail",
                align: "center",
                valign: "middle",
               width:100
            },
            {
                title: "手机号码",
                field: "receivePhone",
                align: "center",
                valign: "middle",
                width:100
            },
            {
                title: "发送时间",
                field: "sendTime",
                align: "center",
                valign: "middle",
                	width:130,
                	formatter: function (value, row, index) {
                		if(!!row.sendTime){
                			return row.sendTime.split(".")[0];
                		}
                		return "-";
                    }
            },
            {
                title: "短信内容",
                field: "content",
                align: "left",
                valign: "middle"
                
            },
            {
                title: "字数",
                field: "length",
                align: "center",
                valign: "middle",
               width:45,
                formatter: function (value, row, index) {
                    return row.content.length;
                }
            },
            {
                title: "发送状态",
                field: "state",
                align: "center",
                valign: "middle",
               width:70,
                formatter: function (value, row, index) {
                	if("today"==$("#logTime").val()){
                		if(row.state==null){
                    		return null;
                    	}else if (row.state==-1) {
                        	return "发送中";
                        }else if(row.state==0){
                        	return "已发送";
                        }else if(row.state==100 || row.state==99){
                        	return "发送成功";
                        }else{
                        	return "发送失败";
                        }
                	}else if("history"==$("#logTime").val()){
                		if(row.state==null){
                    		return null;
                    	}else if(row.state==100 || row.state==99){
                        	return "发送成功";
                        }else{
                        	return "发送失败";
                        }
                	}
                	
                }
            },
            {
                title: "状态报告",
                field: "fkState",
                align: "center",
                valign: "middle",
                width:80,
                formatter: function (value, row, index) {
                    if (row.fkState != null && row.receiveCode != null) {
                        return row.fkState + "-" + row.receiveCode;
                    } else if (row.fkState != null && row.receiveCode == null) {
                        return row.fkState
                    } else if (row.fkState == null && row.receiveCode != null) {
                        return row.receiveCode
                    }
                }
            }, 
            {
                title: "状态时间",
                field: "feedbackTime",
                align: "center",
                valign: "middle",
                width:140,
                formatter: function (value, row, index) {
                    if (!row.feedbackTime) {
                        return "-";
                    }
                   
                    return row.feedbackTime.split(".")[0];
                    
//                    return new Date(row.feedbackTime.split(".")[0]).Format("yyyy-MM-dd hh:mm:ss");
                }
            }
             
            ],
            responseHandler: function(a) {
            	var btn_arr = a["data"];
            	if (btn_arr!=null && btn_arr.length>0) {
            		$("#i_tnum").html(btn_arr[0]["grossNum"] + "条：");
            		$("#i_inum").html(btn_arr[0]["grossNum"] - (btn_arr[0]["succeedNum"] + btn_arr[0]["failureNum"]) + "条、");
            		$("#i_enum").html(btn_arr[0]["succeedNum"] + btn_arr[0]["failureNum"] + "条、");
            		$("#i_snum").html(btn_arr[0]["succeedNum"] + "条、");
            		$("#i_fnum").html(btn_arr[0]["failureNum"] + "条");
            	}
            	
            	$('#tb_data').bootstrapTable('hideColumn', 'fkState');
            	$('#tb_data').bootstrapTable('hideColumn', 'feedbackTime');
                var btn_arr1 = a["btn"];
                if (btn_arr1 != null && btn_arr1.length > 0) {
                    var user_path = '/dayAndHistoryLog';
                    for (var k = 0; k < btn_arr1.length; k++) {
                    	var btn_obj = btn_arr1[k];
                        if (btn_obj["actionUrls"] == user_path + '/exportSpan.do') {
                            $("#exportSpan").show();// 
                        } 
                        if (btn_obj["actionUrls"] == user_path + '/fkState.do') {
                        	$('#tb_data').bootstrapTable('showColumn', 'fkState');
                        } 
                        if (btn_obj["actionUrls"] == user_path + '/feedbackTime.do') {
                        	$('#tb_data').bootstrapTable('showColumn', 'feedbackTime');
                        } 
                    }
                }
            	
                return a;
            },
            formatNoMatches: function () {
            	
                return '无符合条件的记录';
            }           
    });

}

