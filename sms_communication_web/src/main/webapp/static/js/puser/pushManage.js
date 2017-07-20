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
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}


function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'pushManageList.do?menuId=' + $("#menu_id").val(),
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
                title: "消息标题",
                field: "title",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "消息内容",
                field: "content",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "推送范围",
                field: "type",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
	               if(row.type==0){
	            	   return "全部发推送";
	               }else if(row.type==1){
	            	   return "分类推送:"+row.smsUserTypeId;
	               }else if(row.type==2){
	            	   return "单独推送:"+row.smsUserEmail;
	               }
                }
            },
            {
                title: "操作人",
                field: "sysName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "创建时间",
                field: "created",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (!row.created) {
                        return "";
                    }
                    return new Date(row.created).Format("yyyy-MM-dd hh:mm:ss");
                }
            }
            ],
            responseHandler: function(a) {
            	var btn_arr = a["data"];
            	if (btn_arr!=null && btn_arr.length > 0) {
            	    var user_path = '/pushManage/from/';
                    for (var k = 0; k < btn_arr.length; k++) {
                        var btn_obj = btn_arr[k];
                        
                        if (btn_obj["actionUrls"] == user_path + 'add.do') {
                            add_html = '<a id="add_data" href="javaScript:void(0);" onclick="addDate()">'
            								+'<button id="add_button" type="button" class="btn btn-info">新增</button>'
            						  +'</a> ';
                            $("#addSmsUsers").html(add_html);
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


function  addDate() {
	$("#i_type").val("0");
	$("#i_pushIndex").prop("checked", false);//不选中
	$(".i_pushIndex").prop("checked", false);
	$("#div_smsUserType").hide();
	$("#i_title").val("");
	$("#i_content").val("");
	$("#i_smsUserEmail").val("");
	$("#div_smsUserEmail").hide();

    $("#add_data_div").modal("show");
};

 function btnChange(values) {
	 if (values == "0") {
		 $(".i_pushIndex").prop("checked", false);
		 $("#div_smsUserType").hide();
		 $("#i_smsUserEmail").val("");
		 $("#div_smsUserEmail").hide();
	 }
	 else if(values == "1"){
		 $("#i_smsUserEmail").val("");
		 $("#div_smsUserEmail").hide();
		 $("#div_smsUserType").show();
	 }if(values == "2"){
		 $(".i_pushIndex").prop("checked", false);
		 $("#div_smsUserType").hide();
		 $("#div_smsUserEmail").show();
	 }
 }
 
 
 $("#save_data_btn").bind("click", function () {
	 var title = $("#i_title").val();
	 var content = $("#i_content").val();
	 var types =$("#i_type").val();
	 var pushIndex =""; 
	 var smsUserType="";
	 var chk_value =[]; 
	 $('input[name="smsUserType"]:checked').each(function(){ 
		chk_value.push($(this).val()); 
	 }); 
	 smsUserType =chk_value.join(",");
	 $('input[id="i_pushIndex"]:checked').each(function(){ 
		 pushIndex=$(this).val(); 
	 }); 
	 
	 var smsUserEmail =$("#i_smsUserEmail").val();
	 
	 if(title==null || title==""){
		 alert("请填写消息标题！");
		 return;
	 }
	 if(content==null || content==""){
		 alert("请填写消息内容！");
		 return;
	 }
	 if(types==1 && (smsUserType==null || smsUserType=="")){
		 alert("请选择推送范围！");
		 return;
	 }
	 if(types==2 && (smsUserEmail==null || smsUserEmail=="")){
		 alert("请填写推送客户账号！");
		 return;
	 }
 	 var data = {
 		        "title": title,
 		        "content":content,
 		        "types":types,
 		        "pushIndex":pushIndex,
 		        "smsUserType":smsUserType,
 		       "smsUserEmail":smsUserEmail
 		};
	        ajaxCall({
	            url: "/puser/addPushManage.do?menuId="+$("#menu_id").val(),
	            type: "post",
	            data: JSON.stringify(data),
	            success: function (data) {
	                if (checkRes(data)) {
	                  $("#add_data_div").modal("hide");
	                  alert("新增成功！");
	                  refresh_data();
	                }else {
		                alert("添加失败");
	                }
	            },
	            error: function () {
	                $(".ibox-content").prepend(alertview("danger", "操作失败."));
	            },
	            complete: function () {
	            }
	        });
 });
 




