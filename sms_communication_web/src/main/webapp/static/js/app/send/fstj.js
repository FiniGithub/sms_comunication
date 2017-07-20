var path = $("#path").val();
$(function(){
	getRealTimeOrSmsNum();
	smsOrderExport();// 导出报表
	load_data();// 加载数据
	bindDatetimepicker();// 日期控件
	
    $("#search_btn").bind("click", function () {
    	refresh_data();
    });
    
});




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
	$('#com-fstable').bootstrapTable({
        url: 'puserStatisticalList.do',
        dataType: "json",
        cache: false,
        striped: true,
        pagination: true,
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: 
        [
			[
				 {
				     field: 'auditTime',
				     title: "时间",
				     valign:"middle",
				     align:"center",
				     rowspan: 2,
				     width:110,
				     cellStyle:formatTableUnit,
				     formatter:function(value, row, index){
				    	 if (!row.auditTime) {
		                      return "";
		                  }
		                 return new Date(row.auditTime).Format("yyyy-MM-dd");
		             }
				 },{
				     field: 'sendNum',
				     title: "发送号码数",
				     valign:"middle",
				     align:"center",
				     width:110,
				     rowspan: 2
				 },{
				     field: 'billingNum',
				     title: "消费计费条数",
				     valign:"middle",
				     align:"center",
				     width:110,
				     rowspan: 2,
				     formatter: function (value, row, index) {
		            		if(index==0){
		            			var phoneNum = row.sumSendNum;
		            	    	var successSum = row.sumSucceedNumUs + row.sumSucceedNumMs + row.sumSucceedNumTs;
		            	    	var failureSum = row.sumFailureNumUs + row.sumFailureNumMs + row.sumFailureNumTs;
		            	    	var unknownSum = row.sumUnknownNumUs + row.sumUnknownNumMs + row.sumUnknownNumTs;

		            	    	$(".fssum").html(phoneNum);
		            	    	$(".xfsum").html(successSum);
		            	    	$(".jssum").html(failureSum);
		            	    	$(".unkwon").html(unknownSum);
		            		}
		                    return row.billingNum;
		             }
				 },{
				     field: 'actualNum',
				     title: "结算计费条数",
				     valign:"middle",
				     align:"center",
				     width:110,
				     rowspan: 2
				 },{
				     title: "移动",
				     valign:"middle",
				     align:"center",
				     width:180,
				     colspan: 3,
				     rowspan: 1
				 },{
				     title: "联通",
				     valign:"middle",
				     align:"center",
				     width:180,
				     colspan: 3,
				     rowspan: 1
				 },{
				     title: "电信",
				     valign:"middle",
				     align:"center",
				     width:180,
				     colspan: 3,
				     rowspan: 1
				 }
			],
			[
				 {
				     field: 'succeedNumUs',// 移动
				     title: '成功',
				     valign:"middle",
				     align:"center",
				     width: 60
				 },
				 {
				     field: 'failureNumUs',
				     title: '失败',
				     valign:"middle",
				     align:"center",
				     width: 60
				 },{
				     field: 'unknownNumUs',
				     title: '未知',
				     valign:"middle",
				     align:"center",
				     width: 60
				 },{
				     field: 'succeedNumMs',// 联通
				     title: '成功',
				     valign:"middle",
				     align:"center",
				     width: 60
				 },
				 {
				     field: 'failureNumMs',
				     title: '失败',
				     valign:"middle",
				     align:"center",
				     width: 60
				 },{
				     field: 'unknownNumMs',
				     title: '未知',
				     valign:"middle",
				     align:"center",
				     width: 60
				 },{
				     field: 'succeedNumTs',// 电信
				     title: '成功',
				     valign:"middle",
				     align:"center",
				     width: 60
				 },
				 {
				     field: 'failureNumTs',
				     title: '失败',
				     valign: "middle",
				     align: "center",
				     width: 60
				 },{
				     field: 'unknownNumTs',
				     title: '未知',
				     valign: "middle",
				     align: "center",
				     width: 60
				 }
			]
		],
	    formatNoMatches: function () {
	        return '无符合条件的记录';
	    }
    });
}

function formatTableUnit(value, row, index) {
    return {
        css: {
//            "border-right":"1px red solid;"
        }
    }
}


/**
 * 导出上月报表
 */
function smsOrderExport(){
	$("#export").click(function(){
		var email = $("#email").val();
		$.get(path + "/export/querySmsData.do?email=" + email,function(data){
			if (checkRes(data)) {
				window.location.href=path + '/export/orderExport.do?email=' + email,
				'height=100,width=400,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no';
		    }else{
		    	alert("上月没有可以导出数据");
		    }
		});
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
    $('#com-fstable').bootstrapTable('destroy');
    load_data();
}

function bindDatetimepicker(){
 	$('#start_input').datetimepicker({
		format: 'yyyy-mm-dd hh:ii',
		initialDate: new Date(), //初始化当前日期
		autoclose: true, //选中自动关闭
		todayBtn: true, //显示今日按钮    
		language: 'zh-CN'
	});
	    
    $('#end_input').datetimepicker({
		format: 'yyyy-mm-dd hh:ii',
		initialDate: new Date(), //初始化当前日期
		autoclose: true, //选中自动关闭
		todayBtn: true, //显示今日按钮    
		language: 'zh-CN'
	});
}
