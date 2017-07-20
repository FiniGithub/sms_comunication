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
  
     $("#save_data_btn").bind("click", function () {
    	 
    	var name = $("#i_name").val();
     	var aisleType = $("#i_aisleType").val();
     	var awardMoney = $("#i_awardMoney").val();
     	if(name=="" || aisleType=="" || awardMoney==""){
     		alert("请将数据填写完整！");
     		return;
     	}
	    $("#upload_form").submit();
    });
    
   $("#add_data").bind("click", function () {
    	
    	$("#i_id").val("");
    	$("#i_name").val(""); 
    	$("#i_aisleType").val("");
    	$("#aisledata").html("");
    	$("#i_awardMoney").val("");
    	$("#i_smsUserId").val("");
        $("#add_data_div").modal("show");
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
        freeTrialState: $("#i_freeTrialState").val(),
        smsUser: $("#smsUser_input").val(), 
        sendType: $("#sendType_select").val(), 
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'sysFreeTrialList.do?urltypes=0&menuId=' + $("#menu_id").val(),
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
			    title: "名称",
				field: "name",
				align: "left",
				valign: "middle",
				sortable: "true"
			},
            {
                title: "内容",
                field: "content",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "类型",
                field: "typeName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "状态",
                field: "freeTrialState",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                	if(row.freeTrialState==null){
                		return null;
                	}else if (row.freeTrialState==0) {
                    	return "启用";
                    }else if(row.freeTrialState==1){
                    	return "停用";
                    }
                }
            },
            
            {
                title: "提交时间",
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
            {
    			title : '操作',
    			align : 'left',
    			valign : "middle",
    			formatter : function(value, row, index) {

    				return loadBtn(row.sysMenuBtns, row.id,row.freeTrialState);
    			}
    		} 
            ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}

//加载菜单对应的按钮
function loadBtn(btn_arr, id,freeTrialState) {
    var html = '', edit_html = '', delete_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/sysFreeTrial/from/';
        for (var k = 0; k < btn_arr.length; k++) {
        	var btn_obj = btn_arr[k];
        	if(freeTrialState=="0"){
        		 if (btn_obj["actionUrls"] == user_path + 'onThrough.do') {
                     delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="auditDate(\'' + id + '\',1);"><i class="fa fa-paste"></i>停用</a> ';
                 }
        	}else {
        		if (btn_obj["actionUrls"] == user_path + 'through.do') {
                	edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="auditDate(\'' + id + '\',0);"><i class="fa fa-paste"></i>启用</a> ';
                }
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

function auditDate(id,num) {
    $("#del_h4_title").html("确定操作");
    $("#del_p_title").html("确认操作?");
    $('#del').modal("show");
    del(id,num)
}

function del(id,num) {
	$("#btn_del").bind("click", function () {
	    ajaxCall({
	        url: "/userFreeTrial/updateFreeTrialById.do?id=" + id+"&nums="+num,
	        type: "get",
	        success: function (data) {
	        	 if (checkRes(data)) {
	        		 $("#del").modal("hide");
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
	});
}

function editData(id) {
    $(".fileinput-remove-button").trigger("click");
    $('#add_data_div').modal("show");
    ajaxCall({
        url: "/userFreeTrial/queryUserFreeTrialById.do?id=" + id,
        type: "get",
        success: function (data) {
        	 if (checkRes(data)) {
                 var obj = data["data"]["data"];
             	$("#i_id").val(obj["id"]);
            	$("#i_name").val(obj["name"]);
            	$("#i_aisleType").val(obj["freeTrialType"]);
            	$("#i_smsUserId").val(obj["smsUserId"]);
            	$("#i_awardMoney").val(obj["content"]);
            	load_typedata(obj["freeTrialType"]);
        	 }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}


function countvalue(count){
	$("#i_awardMoney").val(count);
}
