var path = $("#server_path").val();
$(function(){
	load_data();// 加载数据
	$("#search_btn").bind("click", function () {
		refresh_data();
	});

    $("#del_btn").on("click",function(){
        del();
    });

    $("#btn_del").on("click",function(){
        delOk();
    });
});
//删除
function del(){
    var a = $('#com-table').bootstrapTable('getSelections');
    if (a.length > 0) {
        $("#del").modal("show");
    } else {
        alert("请选择要删除的数据");
        return;
    }
}
//确认删除
function delOk(){
    var a = $('#com-table').bootstrapTable('getSelections');
    var ids = new Array();
    for (var i = 0; i < a.length; i++) {
        ids.push(a[i].id);
    }
    $.ajax({
        url:path+ "/zixun/delete.do",
        type: "POST",
        dataType: "json",
        data: {"ids": ids},
        success: function (data) {
            if (checkRes(data)) {
                $("#del").modal("hide");
                refresh_data();
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}
//刷新数据
function refresh_data() {
    $('#com-table').bootstrapTable('destroy');
    load_data();
}

/**
 * 加载数据
 */
function load_data() {
    $('#com-table').bootstrapTable({
        url: 'zixunList.do',
        dataType: "json",
        cache: false,
        striped: true,
        ccext:false,
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
                field: "",
                checkbox: true,
                align: "center",
                valign: "middle",
                width:30
            },
            {
                title: "账号",
                field: "email",
                align: "center",
                valign: "middle",
                width:100
            },
            {
                title: "联系人",
                field: "contact",
                align: "center",
                valign: "middle",
                width:80
            },
            {
                title: "手机号",
                field: "phone",
                align: "center",
                valign: "middle",
                width:100
            },
            {
                title: "内容",
                field: "content",
                align: "left",
                valign: "middle"
            },
            {
                title: "时间",
                field: "createTime",
                align: "center",
                valign: "middle",
                width:140,
                formatter: function (value, row, index) {
                    return new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            }
            ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}



function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        menuId: $("#id_input").val(),
        u_id:$("#u_id").val(),
        time: $("#time").val()//结束时间
    };
    return temp;
}
function format(shijianchuo) {
//shijianchuo是整数，否则要parseInt转换
    var time = new Date(shijianchuo);
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
}
function add0(m) {
    return m < 10 ? '0' + m : m
}
