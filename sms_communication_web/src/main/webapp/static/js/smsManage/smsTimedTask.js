// var ChineseDistricts_city = {};
var quuids ="";
$(function () {
	var startday = {//历史
	        elem: '#start_input',
	        format: 'YYYY-MM-DD',
	       min: laydate.now(), //设定最大日期为当前日期
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
	        min: laydate.now(), 
	        istime: false,
	        istoday: true,
	        choose: function(datas){ 
	        	
	        	startday.max = datas //将结束日的值设定为开始日的最大值  
	       }  
	}
    laydate(endtday);
    var timed = {
            elem: '#time_input',
            format: 'YYYY-MM-DD hh:mm',
            min: laydate.now(), //设定最小日期为当前日期
            istime: true,
            istoday: true

        };
	    laydate.skin('yahui');
	 laydate(timed);
	    laydate(startday);  

    load_data();
    
    $("#search_btn").unbind("click");
    $("#search_btn").bind("click", function () {
    	refresh_data();
    	$('#tb_data').bootstrapTable('hideColumn', 'aisleName');
    	$('#tb_data').bootstrapTable('hideColumn', 'operation');
    	$('#tb_data').bootstrapTable('hideColumn', 'select');
    });
    
    $("#reset").unbind("click");
    $("#reset").bind("click", function () {
       // $('#phone_input').val("");
        $('#start_input').val("");
        $('#end_input').val("");
    });
    
    var $table_stop = $('#tb_data'), $change_stop = $('#stop_btn');
    $change_stop.click(function () {
		typeChange($table_stop,2);
	});
	
	var $table_start = $('#tb_data'), $change_start = $('#start_btn');
	$change_start.click(function () {
		typeChange($table_start,1);
	});
	
	var $table_delete = $('#tb_data'), $delete = $('#delete_btn');
	$delete.click(function () {
    	deleteTimeTask($table_delete);
	});
	
	$("#save_btn").unbind("click");
	$("#save_btn").bind("click", function () {
		var checkTimeing = 10 * 60 * 1000;
		if(Date.parse(new Date($("#time_input").val())) < Date.parse(new Date())){
			alert("您预定的时间无效.");
			return;
		}else if((Date.parse(new Date($("#time_input").val())) - Date.parse(new Date())) < checkTimeing){
			alert("预定发送时间需设置为10分钟以后!\n" + "请重新设置发送时间.");
			return;
		}
        save();
    });
	
	var $table_export = $('#tb_data'), $export = $('#export_btn');
	$export.click(function () {
		orderExport($table_export);
	});
	
});

function orderExport($table){

    var taskIds = $.map($table.bootstrapTable('getSelections'), function (row) {
        if(row.superAdmin == 1){
            return null;
        }
        return row.id;
    });
    if(taskIds==""){
        alert("请选择定时任务！");
        return;
    }
    
    if(taskIds == null){
		taskIds = new Array();
	}
	
	var exportDate = {
            'taskIds' : taskIds.join(","),
        	};
	
	$.post(
			"querytimedtaskfororder.do",
			exportDate,
	 		function(data) {
				if(data["retCode"]=="000000"){
					window.location.href="orderExport.do?email=" + $("#smsUserEmail").val();
				}else{
					alert("没有可以导出数据");
				}
				
		});
}

function deleteTimeTask($table){

    var taskIds = $.map($table.bootstrapTable('getSelections'), function (row) {
        if(row.superAdmin == 1){
            return null;
        }
        return row.id;
    });
    if(taskIds==""){
        alert("请选择定时任务！");
        return;
    }
    
    if(taskIds == null){
		taskIds = new Array();
	}
    
    if(window.confirm("您确定删除选择的定时任务吗？")){
    	
     }else{
        return;
    }
    $('#delete_btn').attr("disabled","disabled");
	var deleteDate = {
            'taskIds' : taskIds.join(","),
        	};
	
	$.post("deletetimedtask.do",deleteDate,function(data){
			refresh_data();
			$('#tb_data').bootstrapTable('hideColumn', 'aisleName');
			$('#tb_data').bootstrapTable('hideColumn', 'operation');
			$('#tb_data').bootstrapTable('hideColumn', 'select');
			
	});
    

}

function save() {
    var signature = $("#signature_select").val();
    var content = $("#content").val();
    var taskId = $("#taskId").val();
    var timing = $("#time_input").val();
    var billingNnum = $("#i_num").text();
    
    var canCommit = true;
    if (!timing) {
        canCommit = false;
    }
    if (!content) {
        canCommit = false;
    }
    if (!signature) {
        canCommit = false;
    }
    if (!canCommit) {
    	alert("修改信息不允许为空，请检查");
        return;
    }
    if($("#canCommit").val() == 1){
    	alert("您的短信内容包含敏感词，请重新输入!");
        return;
    }
    
    updatetimedtask(taskId,null,null,null,content,timing,signature,billingNnum);
    
}


function typeChange($table,sendType){
    var taskIds = $.map($table.bootstrapTable('getSelections'), function (row) {
        if(row.superAdmin == 1){
            return null;
        }
        return row.id;
    });
    var sendTypes =$.map($table.bootstrapTable('getSelections'), function (row) {  
        if(row.superAdmin == 1){
            return null;
        }
        if(sendType == row.sendType && sendType == 1){
        	return "start";
        }
        if(sendType == row.sendType && sendType == 2){
        	return "stop";
        }
        if(row.sendType == 1){
        	return 2;
        }
        if(row.sendType == 2){
        	return 1;
        }
        return null;
    });
    
    for (var i = 0; i < sendTypes.length; i++) {
    	if(sendTypes[i] == "start"){
        	alert("您选择修改的定时任务包含已启动部分，请重新选择！");
            return;
        }
        if(sendTypes[i] == "stop"){
        	alert("您选择修改的定时任务包含已停止部分，请重新选择！");
            return;
        }
	}
    
    if(taskIds == ""){
        alert("请选择定时任务！");
        return;
    }
    
    updatetimedtask(null,null,taskIds,sendTypes,null,null,null,null);
    
}

function editData(id) {
	$('#modify_div').modal("show");

	$.get("querytimedtaskformodify.do?taskId=" + id, function(data) {

		$("#taskId").val(data.id);
		$("#email").text(data.smsUserEmail);
		$("#signature_select option").eq(0).val(data.signature);
		$("#signature_select option").eq(0).html(data.signature);
		$("#content").val(data.content);
		$("#i_length").text(data.content.length);
		$("#i_num").text(data.billingNnum);
		$("#time_input").val(new Date(data.timing).Format("yyyy-MM-dd hh:mm"));
	});
}

function sensitiveWordsAndcalcLangth() {
	var content = $("#content").val();
	var shieldWord = $("#shieldWord").val().split(",");
	
	// 输入短信内容
    $("#content").keyup(function () {
        var content = $("#content").val();
        content = $.trim(content);// 去除两端空格
        content = content.replace(/(^\n*)|(\n*$)/g, "");//去掉两端换行
        var select_sign = $("#qminp").val();// 下拉框签名
        var input_sign = $("#signature_select").val();// 输入签名
        var signLength = 0;// 签名长度
        if (select_sign != undefined && select_sign != null && select_sign != '') {
            signLength = select_sign.length;
        } else {
            signLength = input_sign.length;
        }
        var allLength = content.length + signLength;

        for (var i = 0; i < shieldWord.length; i++) {
    		if (content == shieldWord[i]) {
    			$("#canCommit").val(1);
    			alert("您的短信内容包含敏感词，请重新输入!");
    		} else {
    			$("#canCommit").val(0);
    		}
    	}
    	$("#i_length").text(allLength);
    	
    	var singleSmsLength = 1;
    	if (allLength > 70) {
    		var smsTextLen = allLength - 70;
    		singleSmsLength = Math.ceil(smsTextLen / 67);
    		// 尾数加1条
    		if (smsTextLen % 67 >= 0) {
    			singleSmsLength += 1;
    		}
    	}
    	$("#i_num").text(singleSmsLength);
    	
    	$("textarea").attr("maxlength",300-signLength);
    });
}

// 刷新数据
function refresh_data() {
	$('#tb_data').bootstrapTable('hideColumn', 'aisleName');
	$('#tb_data').bootstrapTable('hideColumn', 'operation');
	$('#tb_data').bootstrapTable('hideColumn', 'select');
	
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
        email: $("#user_input").val(), 
        sendType: $("#sendType_select").val(), 
        aisleName: $("#aisle_select").val(),
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}

function updatetimedtask(taskId,sendType,taskIds,sendTypes,content,timing,signature,billingNnum){
	if(taskIds == null || sendTypes == null){
		taskIds = new Array();
		sendTypes = new Array();
	}
	
	var updateDate = {
            'taskId' : taskId,
            'sendType' : sendType,
            'taskIds' : taskIds.join(","),
            'sendTypes' : sendTypes.join(","),
            'content' : content,
            'timing' : timing,
            "signature" : signature,
            "billingNnum" : billingNnum
        	};
	
	$.post("updatetimedtask.do",updateDate,function(data){
			$('#modify_div').modal("hide")
			
			if(sendType == 2 || (!!sendTypes && sendTypes[0]==2)){
				alert("您选择的定时任务已停止！");
			}else if(sendType == 1 || (!!sendTypes && sendTypes[0]==1)){
				alert("您选择的定时任务已启动！");
			}
			
			if(!!content && !! data["msg"]){
				alert(data["msg"]);
			}
			
			refresh_data();
			$('#tb_data').bootstrapTable('hideColumn', 'aisleName');
	    	$('#tb_data').bootstrapTable('hideColumn', 'operation');
	    	$('#tb_data').bootstrapTable('hideColumn', 'select');
			console.log("数据更新成功");
	});
	
}


function load_data() {
	
	var sendType = $("#sendType_select").val();
    $('#tb_data').bootstrapTable({
        url: 'querytimedtask.do?menuId=' + $("#menu_id").val(),
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
        ccext:false,// 自定义样式,为true 跨行显示短信内容
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
                  width:30,
                  checkbox: sendType==""?true:false
              },
              {
                  title: "账号",
                  field: "smsUserEmail",
                  align: "center",
                  width:100,
                  valign: "middle",
                  formatter: function (value, row, index) {
                      $("#smsUserEmail").val(row.smsUserEmail);
                      return row.smsUserEmail;
                  }
              },
              {
                  title: "短信数",
                  field: "sendNum",
                  align: "center",
                  valign: "middle",
                  width:80
              },
              {
                  title: "预定发送时间",
                  field: "timing",
                  align: "center",
                  valign: "middle",
                  width:115,
                  formatter: function (value, row, index) {
                      if (!row.timing) {
                          return "";
                      }
                      return new Date(row.timing).Format("yyyy-MM-dd hh:mm");
                  }
              },
              {
                  title: "短信内容",
                  field: "content",
                  align: "left",
                  valign: "middle"
                 
              },
              {
                  title: "状态",
                  field: "sendType",
                  align: "center",
                  valign: "middle",
                  width:45,
                  formatter: function (value, row, index) {
                      if (row.sendType==1) {
                      	return "启动";
                      }else if(row.sendType==2){
                      	return "停止";
                      }
                  }
              },
              {
                  title: "通道类型",
                  field: "aisleName",
                  align: "center",
                  valign: "middle",
                width:150
              },
              {
            	  title: '操作',
            	  field: "operation",
                  align: 'center',
                  valign: "middle",
                  width:80,
                  formatter: function(value,row,index){
                	  if (row.sendType==1) {
                		  var j ='<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="updatetimedtask(\''+row.id +'\' , \'' +2+'\',null,null,null,null,null,null);"><i class="fa fa-paste"></i>'+ "停止" +'</a> ';
                        }else if(row.sendType==2){
                          var j ='<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="updatetimedtask(\''+row.id +'\' , \'' +1+'\',null,null,null,null,null,null);"><i class="fa fa-paste"></i>'+ "启动" +'</a> ';
                        }
                	  
                	  var i ='<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + row.id + '\');"><i class="fa fa-paste"></i>'+ "修改" +'</a> ';
                      	return j + '&nbsp;' + i;
                  }
              }
            ],
            
            responseHandler: function (a) {
                $('#tb_data').bootstrapTable('hideColumn', 'aisleName');
                $('#tb_data').bootstrapTable('hideColumn', 'operation');
                $('#tb_data').bootstrapTable('hideColumn', 'select');
                var btn_arr = a["data"];
                if (btn_arr != null && btn_arr.length > 0) {
                    var user_path = '/smsTimedTask';
                    for (var k = 0; k < btn_arr.length; k++) {
                    	var btn_obj = btn_arr[k];
                        if (btn_obj["actionUrls"] == user_path + '/aisleSelect.do') {
                            $("#aisleSpan").show();// 
                            $('#tb_data').bootstrapTable('showColumn', 'aisleName');
                        } 
                       if (btn_obj["actionUrls"] == user_path + '/stopSpan.do') {    
                            $("#stopSpan").show();// 
                       }
                       if (btn_obj["actionUrls"] == user_path + '/startSpan.do') {     
                            $("#startSpan").show();// 
                       }
                       if (btn_obj["actionUrls"] == user_path + '/deleteSpan.do') {
                            $("#deleteSpan").show();// 
                       }
                       if (btn_obj["actionUrls"] == user_path + '/exportSpan.do') {
                            $("#exportSpan").show();// 
                       }
                       if (btn_obj["actionUrls"] == user_path + '/operation.do') {
                            $('#tb_data').bootstrapTable('showColumn', 'operation');
                       }
                       if (btn_obj["actionUrls"] == user_path + '/select.do') {
                    	   $('#tb_data').bootstrapTable('showColumn', 'select');
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

