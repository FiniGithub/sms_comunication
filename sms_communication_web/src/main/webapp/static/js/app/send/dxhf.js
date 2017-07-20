var path = $("#path").val();
$(function(){
	getRealTimeOrSmsNum();
	loadMsg();// 加载消息列表
	
	$("#search_btn").bind("click", function() {
		$('#com-table').bootstrapTable( 'removeAll' );
		$('#com-table').bootstrapTable('refresh', {
			url : 'pushlist.do',
			queryParams : queryParamsVal
		});
	});
});


/**
 * 加载消息列表
 */
function loadMsg() {
	$('#com-table').bootstrapTable({
		url : 'pushlist.do',
		dataType : "json",
		cache : false,
		striped : true,
		pagination : true,
		clickToSelect : true,
		sidePagination : "server", // 服务端处理分页
		queryParams : queryParamsVal, // 参数
		method : "post",
		columns : [ 
		   {
			title : "手机号码",
			field : "phone",
			align : "center",
			valign : "middle",
			formatter:function (value,row,index){
				var x = '<i> - </i><span class="phonum">' + row.phone + '</span>';
				return x;
			}
		},{
			title : "下发内容",
			field : "contents",
			align : "center",
			valign : "middle",
			formatter:function (value,row,index){
				var x = '<span class="xfcontent">' + row.contents + '</span>';
				return x;
			}
		},{
			title : "描述",
			field : "state",
			align : "center",
			valign : "middle",
            formatter: function (value, row, index) {
            	if(row.state==null){
            		return null;
            	}else if (row.state==1) {
                	return '<span class="status1">未推送</span>';
                }else if (row.state==2) {
                	return '<span class="status1">推送错误</span>';
                }else if (row.state==3){
                	return '<span class="status2">已推送</span>';
                }else if (row.state==4){
                	return '<span class="status1">URL为空</span>';
                }
            }
		}, {
			title : "回复内容",
			field : "content",
			align : "center",
			valign : "middle"
		}, {
			title : "接收时间",
			field : "createTime",
			align : "center",
			valign : "middle"
		}],
		formatNoMatches : function() {
			return '无符合条件的记录';
		}
	});

}

function queryParamsVal(params) { // 配置参数
	var temp = { // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
		pagesize : params.limit, // 页面大小
		pagenum : (params.offset / params.limit) + 1, // 页码
		sort : params.sort, // 排序列名
		order : params.order,// 排位命令（desc，asc）
		phone : $("#phone").val()
	};
	return temp;
}
