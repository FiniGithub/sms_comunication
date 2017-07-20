var path = $("#path").val();
$(function () {
    showState();

    bindDatetimepicker();
    $("#search_btn").bind("click", function () {
        refresh_data();
    });
    hidemColumn();// 隐藏列
    load_data();// 加载数据
});

/**
 * 控制列的隐藏
 */
function hidemColumn() {
    $('#fs-table').bootstrapTable('hideColumn', 'bname');
}


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

    if (startNum == nowNum || endNum == nowNum) {
        alert("当天的发送状态在统计中，请于次日查询结果。");
        return;
    }


    $('#fs-table').bootstrapTable('destroy');
    hidemColumn();
    load_data();
}
//配置参数
function queryParamsVal(params) {
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        email: $("#email").val(),// 账户
        type: $("#type").val(),      //类型
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}

function load_data() {
    $('#fs-table').bootstrapTable({
        url: 'puserConsumeBillList.do?menuId=' + $("#menuId").val(),
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
                title: "日期",
                field: "createTime",
                align: "center",
                valign: "middle",
                width:140,
                formatter: function (value, row, index) {
                    if (!row.createTime) {
                        return "";
                    }
                    return new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                title: "计费短信",
                field: "operateNum",
                align: "center",
                valign: "middle"
            },
            {
                title: "发送前",
                field: "beforeNum",
                align: "center",
                valign: "middle"
            },
            {
                title: "发送后",
                field: "afterNum",
                align: "center",
                valign: "middle"
            },
            {
                title: "归属",
                field: "bname",
                align: "center",
                valign: "middle",
                width:100
            }
        ],
        responseHandler: function (obj) {
            hidemColumn();// 控制列的隐藏

            var btn_arr = obj["data"];
            if (btn_arr != null && btn_arr.length > 0) {
                var user_path = '/puserConsume';
                for (var k = 0; k < btn_arr.length; k++) {
                    var btn_obj = btn_arr[k];
                    if (btn_obj["actionUrls"] == user_path + '/belong.do') {// 归属
                        $('#fs-table').bootstrapTable('showColumn', 'bname');
                    }
                }
            }
            return obj;
        },
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}


function bindDatetimepicker() {
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
	        max: laydate.now(), //设定最大日期为当前日期
	        istime: false,
	        istoday: true,
	        issure: false,
	        choose: function(datas){  		        	 
	        	startday.max = datas //将结束日的值设定为开始日的最大值  
	       }  
 }
	    laydate.skin('yahui');
	    laydate(startday);
	    laydate(endtday);
}

function addKey(obj, arr, key) {
    var id = $(obj).attr("data-inp");
    $(obj).parent().hide();
    $("#" + id).val(arr);
    $("#i_type").val(key);
}

function showState() {
    var type = $("#i_type").val();
    if (type == 0 && type != null && type != '') {
        var id = $("#cz").attr("data-inp");
        $("#cz").parent().hide();
        $("#qminp").val("充值");
    }
}