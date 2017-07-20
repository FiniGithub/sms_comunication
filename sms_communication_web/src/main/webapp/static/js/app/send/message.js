var path = $("#path").val();
$(function() {
	load_data();
	var msgCount = $(".newsnum").html();
	if(msgCount == "0"){
		$(".inborder").addClass("msg_style");
	}else{
		$(".inborder").removeClass("msg_style");
	}
});


// 加载消息数据
function load_data() {
	var path = $("#path").val();
	$('#msg-table').bootstrapTable({
		url : path + '/smsUser/messageList.do',
		dataType : "json",
		cache : false,
		striped : false,
		pagination : true,
		clickToSelect : true,
		sidePagination : "server", // 服务端处理分页
		method : "post",
		queryParams : queryParamsVal, // 参数
		columns : [ {
			formatter :function(value,row,index){
				var created = new Date(row.created).Format("MM月dd日");
				var state = row.state;
				var htmlData = '';
				htmlData += '<ul class="news-ul" onclick="newsBtn(this,'+ row.id +')" >';
				htmlData += '<li class="ovh">';
				if(state == 1){
					htmlData += '<p class="newstitle f-l" style="color:#999999 !important;">';
					htmlData += row.title + '</p>';
					htmlData += '<p class="newstime f-r" style="color:#999999 !important;">' + created + '</p></li>';
					htmlData += '<li><p class="newscontent" style="color:#999999 !important;">';
					htmlData += row.content + '</p>';
				}else{
					htmlData += '<p class="newstitle f-l">';
					htmlData += '<img src="../static/img/send/dian.png" alt="">'
					htmlData += row.title + '</p>';
					htmlData += '<p class="newstime f-r">' + created + '</p></li>';
					htmlData += '<li><p class="newscontent">';
					htmlData += row.content + '</p>';
				}
				htmlData += '</li></ul>';
				return htmlData;
			}
		} ],
		formatNoMatches : function() {
			return '暂时无消息';
		}
	});
}

function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order//排位命令（desc，asc）
    };
    return temp;
}

