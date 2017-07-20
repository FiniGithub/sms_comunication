$(function(){
	load_data();// 加载数据
	$("#search_btn").bind("click", function () {
		refresh_data();
	});
});


//刷新数据
function refresh_data() {
   
    var start_input = $('#start_input').val();
    var end_input = $('#end_input').val();
    //开始时间不为空
    if (start_input != "" && end_input == "") {
        if (!end_input) {
            alert("请选择结束时间！");
            return;
        }
    }
    var startNum = parseInt(start_input.replace(/-/g, ''), 10);
    var endNum = parseInt(end_input.replace(/-/g, ''), 10);
    var nowDate = new Date(new Date()).Format("yyyy-MM-dd");
    var nowNum = parseInt(nowDate.replace(/-/g, ''), 10);
    if (startNum > endNum) {
        alert("结束时间不能在开始时间之前！");
        return;
    }
    
    $('#com-table').bootstrapTable('destroy');
    load_data();
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
/**
 * 加载数据
 */
function load_data() {
    $('#com-table').bootstrapTable({
        url: 'logList.do',
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
                title: "操作用户账号",
                field: "userName",
                align: "center",
                valign: "middle"
            },
            {
                title: "操作内容",
                field: "content",
                align: "left",
                valign: "middle"
            },
           /* {
                title: "备注",
                field: "remark",
                align: "center",
                valign: "middle"
            },*/
            {
                title: "操作时间",
                field: "createTime",
                align: "center",
                valign: "middle",
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
        menuId: $("#id_input").val(),
        email:$("#email").val(),
        startInput: $("#start_input").val(),
        endInput: $("#end_input").val()
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
