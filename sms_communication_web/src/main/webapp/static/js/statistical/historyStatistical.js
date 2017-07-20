// var ChineseDistricts_city = {};
$(function () {
	var idss = $("#ids_input").val();
	if(idss==null || idss==""){
		var dateTime =laydate.now(-31);
		var enddateTime =laydate.now(-1);
		$("#start_input").val(dateTime+" ");
		$("#end_input").val(enddateTime+" ");
	}
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
		        max: laydate.now(-1), //设定最大日期为当前日期
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

    $("#search_btn").unbind("click");
    $("#search_btn").bind("click", function () {
    	refresh_data();
    });
    
    $("#refresh_btn").unbind("click");
    $("#refresh_btn").bind("click", function () {
    	refresh_data();
    });
    
    $("#reset").unbind("click");
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
        smsUser: $("#user_input").val(), //账号信息
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
    	url: 'statisticalList.do?menuId=' + $("#menu_id").val() + "&logTime=" + $("#logTime").val(),
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
                field: "email",
                align: "center",
                valign: "middle",
                width:100,
                formatter: function (value, row, index) {
                    if(index == 0){
                    	return "合计";
                    }else{
                    	return row.email;
                    }
                }
            },
            {
                title: "发送数量",
                field: "sendNum",
                align: "center",
                valign: "middle",
                width:80
            },
            {
                title: "成功数",
                field: "succeedNum",
                align: "center",
                valign: "middle",
                width:80
            },
            {
                title: "失败数",
                field: "failureNum",
                align: "center",
                valign: "middle",
                width:80
            },
            {
                title: "移动成功",
                field: "succeedNumUs",
                align: "center",
                valign: "middle",
                width:80
            },
            {
                title: "联通成功",
                field: "succeedNumMs",
                align: "center",
                valign: "middle",
                width:80
            },
            {
                title: "电信成功",
                field: "succeedNumTs",
                align: "center",
                valign: "middle",
                width:80
            },
            {
                title: "未知成功",
                field: "unknownSucceedNum",
                align: "center",
                valign: "middle",
                width:80
            },
            {
                title: "移动失败",
                field: "failureNumUs",
                align: "center",
                valign: "middle",
                width:80
            },
            {
                title: "联通失败",
                field: "failureNumMs",
                align: "center",
                valign: "middle",
                width:80
               
            },
            {
                title: "电信失败",
                field: "failureNumTs",
                align: "center",
                valign: "middle",
                width:80
                
            },
            {
                title: "未知失败",
                field: "unknownFailureNum",
                align: "center",
                valign: "middle",
                width:80
               
            },
            {
                title: "日期",
                field: "auditTime",
                align: "center",
                valign: "middle",
                width:80
               
            }
            ],
            
            responseHandler: function(a) {
                var btn_arr = a["data"];
                if (btn_arr != null && btn_arr.length > 0) {
                    var user_path = '/historyStatistical';
                    for (var k = 0; k < btn_arr.length; k++) {
                    	var btn_obj = btn_arr[k];
                        if (btn_obj["actionUrls"] == user_path + '/exportSpan.do') {
                            $("#exportSpan").show();// 
                        } 
                    }
                }
            	
                return a;
            },
    });
   
}

