// var ChineseDistricts_city = {};
$(function () {
	var dateTime =new Date().Format("yyyy-MM-dd");
	$("#start_input").val(dateTime+" ");
	$("#end_input").val(dateTime+" ");
	
	var startday = {//历史
	        elem: '#start_input',
	        format: 'YYYY-MM-DD',
	        max: laydate.now(), //设定最大日期为当前日期
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
	        max: laydate.now(), //设定最大日期为当前日期
	        istime: false,
	        istoday: true,
	        choose: function(datas){ 
	        	
	        	startday.max = datas //将结束日的值设定为开始日的最大值  
	       }  
	}
	    laydate.skin('yahui');
	    laydate(startday);
	    laydate(endtday);
   
    load_data();

    $("#search_btn").bind("click", function () {
    	refresh_data();
    });
    
    $("#refrash_btn").bind("click", function () {
    	refresh_data();
    });
    
    $("#export_btn").bind("click", function () {
    	orderExport($("#path").val());
    });
    
});

function orderExport(path) {
	var data = {
			date_export: "date_export",
			email: $("#user_input").val(), //账号信息
	        aisleName: $("#aisle_select").val(), //账号信息
	        startInput: $("#start_input").val(),//开始时间
	        endInput: $("#end_input").val()//结束时间
	    };
	    ajaxCall({
	        url: '/puser/puserStatisticalList.do?menuId=' + $("#menu_id").val(),
	        type: "post",
	        data: JSON.stringify(data),
	        success: function (data) {
	        	window.open('orderExport.do',
    					'height=100,width=400,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'
    				); 	
	        },
	        error: function () {
	        	alert("导出失败！");
	        },
	        complete: function () {
	            //$btn.removeAttr("disabled");
	            //$btn.text("确 认");
	        }
	    });
}
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
        aisleName: $("#aisle_select").val(), //账号信息
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
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: [
            {
            	title: "账号",
                field: "email",
                align: "center",
                valign: "middle",
                width:100
            },
            {
                title: "通道名称",
                field: "smsAisleName",
                align: "center",
                valign: "middle",
                width:150,
                formatter: function (value, row, index) {
                    if(index == 0){
                    	return "合计";
                    }else{
                    	return row.smsAisleName;
                    }
                }
            },
            {
                title: "发送数",
                field: "totalNum",
                align: "center",
                valign: "middle",
                formatter: function (value, row, index) {
                	return row.succeedNum + row.failureNum + row.unknownFailureNum
                }
            },
            {
        	title: "成功数",
                field: "succeedNum",
                align: "center",
                valign: "middle"
            },
            {
            	title: "失败数",
                field: "failureNum",
                align: "center",
                valign: "middle"
            },
            {
            	title: "未知数",
                field: "unknownFailureNum",
                align: "center",
                valign: "middle"
            },
            {
        	title: "成功率",
                field: "succeedRate",
                align: "center",
                valign: "middle",
                width:100,
                formatter: function (value, row, index) {
                	if((row.succeedNum + row.failureNum + row.unknownFailureNum) == 0){
                		return 0 + "%";
                	}
                	return Math.round(row.succeedNum / (row.succeedNum + row.failureNum + row.unknownFailureNum) * 10000) / 100.00 + "%";
                }
            },
            {
            	title: "未知率",
                field: "unknownRate",
                align: "center",
                valign: "middle",
                width:100,
                formatter: function (value, row, index) {
                	if((row.succeedNum + row.failureNum + row.unknownFailureNum) == 0){
                		return 0 + "%";
                	}
                	return Math.round(row.unknownFailureNum / (row.succeedNum + row.failureNum + row.unknownFailureNum) * 10000) / 100.00 + "%";
                }
             
            },
            {
                title: "日期",
                field: "auditTime",
                align: "center",
                valign: "middle",
                width:80,
                formatter: function (value, row, index) {
                	if(index==0){
                		return "";
                	}
                	return row.auditTime;
                }
             
            }
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



