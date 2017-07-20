var page_num = 1;
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
    loadUser();
    
    $("#add_sysuser").bind("click", function () {
        $('#add_sysuser_div').modal("show");
        $("#password_div").show();
        $("#h4_title").html("新增系统用户");
        $("#id").val("");
        $("#phone").val("");
        $("#aisleId").val("");
        $("#aisleName").val("");
        var param_val = {};
        loadRoleData(param_val);
    });
    $("#save_sysuser_btn").bind("click", function () {
        save_sysuser();
    });
 /*   $("#search_btn").bind("click", function () {
    	bootstrapTable();
    });*/
    $("#search_btn").bind("click", function () {
    	var url = 'blacklist.do?menuId=' + $("#menu_id").val();
    	//$.post(url,queryParamsVal,function(data){
    	//	alert(data);
    	//});
    	//param = queryParamsVal;
    	//alert( param.sysUserName )
    	jQuery.ajax({
    		type: "POST",
    		url: url,
    		data: '{"pagesize":10,"pagenum":1,"order":"asc","sysUserName":"'+$("#user_input").val()+
    		'","startInput":"","endInput":""}',
    		contentType: "application/json; charset=utf-8",
    		dataType: "json",
    		success: function(msg){
    			//alert("msg="+msg); alert( msg.total );
    			if( msg.total == 0 ){
    				$('#tb_data').bootstrapTable( 'removeAll' );
    			}else{
    				$('#tb_data').bootstrapTable( 'load', msg );
    			}
    		}
    		});
    	
    	
    	//$('#tb_data').load( {"retCode":null,"retMsg":null,"data":null,"rows":null,"total":0} );
    	
    	
        //$('#tb_data').bootstrapTable('refresh', {url: 'blacklist.do?menuId=' + $("#menu_id").val(),queryParams: queryParamsVal});
    });
});



function text(){
	var url = 'blacklist.do?menuId=' + $("#menu_id").val();
	alert( "url2="+url);
	
	$.ajax({
		type: "POST",
		url: url,
		data: '{"pagesize":10,"pagenum":1,"order":"asc","sysUserName":"ss","startInput":"","endInput":""}',
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function(msg){
			alert(msg);
			}
		});
/*	
	$.ajax({
	    url:url,
	    type:'POST', //GET
	    async:true,    //或false,是否异步
	    data:{
	        name:'yang',age:25
	    },
	    timeout:5000,    	//超时时间
	    dataType:'application/json',    //返回的数据格式：json/xml/html/script/jsonp/text
	    success:function(data,textStatus,jqXHR){
	        console.log(data)
	        console.log(textStatus)
	        console.log(jqXHR)
	    }
	})*/
	
}


// 读文件
/*function readFile(filename) {
	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var f = fso.OpenTextFile(filename, 1);
	var s = "";
	while (!f.AtEndOfStream)
		s += f.ReadLine() + "\n";
	f.Close();
	return s;
}*/



function loadUser() {
    $('#tb_data').bootstrapTable({
        url: 'blacklist.do?menuId=' + $("#menu_id").val(),
       dataType: "json",
        cache: false,
        striped: true,
        pagination: true,
        clickToSelect: true,
        sidePagination: "server", //服务端处理分页
        queryParams: queryParamsVal, //参数
        method:"post",
        columns: [
           
            {
                title: "手机号码",
                field: "phone",
                align: "left",
                valign: "middle",
                sortable: "true",
            },
            {
                title: "加入时间",
                field: "createTime",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "归属通道",
                field: "aisleName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
         
            {
                title: '操作',
                align: 'left',
                valign: "middle",
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id,row.phone);
                }
            }],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });

}


function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset/params.limit) +1,  //页码
       
        sort: params.sort,  //排序列名
        order: params.order,//排位命令（desc，asc）
        sysUserName:$("#user_input").val(),//搜索框
        startInput: $("#start_input").val(), //开始时间
        endInput: $("#end_input").val()      //结束时间
        
    };
    return temp;
}

 


function searchParam(pageNum) {
    var param = {};
    pageInfo(param, pageNum, PAGE_SIZE);
    return param;
}

/*加载数据*/
function loadData(param) {
    ajaxCall({
        url: '/smsBlacklist/blacklist.do',
        type: "post",
        data: JSON.stringify(param),
        success: function (data) {
            if (checkRes(data)) {
                var arr = data["data"]["dataList"];
                var total = data["data"]["total"];
                var btn_arr = data["data"]["data"];
                buildTable(arr, btn_arr);
                $("#paging ul").empty();
                paging(+total, param["pagenum"], param["pagesize"]);
            }
            
	             
            
        },
        error: function () {
         
        },
        complete: function () {

        }
    });
}



function buildRoleTable(arr) {
    var html = '';
    $("#role_tab").html("");
    if (arr.length === 0) {
        $("#role_tab").html(tableNoData(7));
        return;
    }
    for (var i = 0; i < arr.length; i++) {
        var obj = arr[i];
        html += '<tr> ' + '<td><input type="checkbox"  key=' + obj["id"] + '>&nbsp;&nbsp;' + obj["roleName"] + '</td> ' + '</td> ' + '</tr>';
    }
    $("#role_tab").html(html);
}


function loadBtn(btn_arr, id,phone) {
    var html = '', edit_html = '', delete_html = '';
    if (btn_arr.length > 0) {
        var user_path = '/smsBlacklist/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
/*            if (btn_obj["actionUrls"] == user_path + 'merge.do') {
                $("#add_button").show();
                edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);"  onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
            }*/
            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="deleteData(\'' + id + '\',\'' + phone + '\');"><i class="fa fa-paste"></i> 移除黑名单</a> ';
            }
        }
    }
    html = edit_html + delete_html;
    return html;
}



function deleteData(id,phone) {
   $('#del').modal("show");
    del(id,phone)
}
function del(id,phone) {
    $("#btn_del").bind("click", function () {
        var param = {"smsBlacklistId": id,"phone":phone};
        ajaxCall({
            url: "/smsBlacklist/from/delete.do",
            type: "get",
            data: param,
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    $('#tb_data').bootstrapTable('refresh', {url: 'blacklist.do?menuId=' + $("#menu_id").val(),queryParams: queryParamsVal});
                } else {
                    $('#delmodal').modal("show");
                }
            },
            error: function () {
            },
            complete: function () {
            }
        });
    });
}


//新增 与修改
function save_sysuser() {
	
    var id = $("#id").val();
    var phone = $("#phone").val();
    var aisleId = $("#aisleId").val();
    var aisleName = $("#aisleName").val();
    var canCommit = true;
    if (!phone) {
        //$("#lb_err_name").text("员工名称不能为空");
        //alert(1);
        canCommit = false;
    }
    if (!aisleId) {
        //$("#lb_err_posts").text("岗位不能为空");
        //alert(2);
        canCommit = false;
    }
    if (!aisleName) {
        //$("#lb_err_posts").text("岗位不能为空");
        //alert(2);
        canCommit = false;
    }
   /* if (!id) {
        if (!password) {
            //$("#lb_err_posts").text("岗位不能为空");
            //alert(2);
            canCommit = false;
        }
    }*/
    if (!canCommit) {
        return;
    }

    var data = {
        "phone": phone,
        "aisleId": aisleId,
        "aisleName": aisleName
    };

    if (id)
        data["id"] = id;
    var role_arr = [];
    $("input[type='checkbox']:checked").each(function () {
        role_arr.push($(this).attr("key"));
    });
    data["roleIds"] = role_arr;
    ajaxCall({
        url: "/smsBlacklist/from/merge.do",
        type: "post",
        data: JSON.stringify(data),
        success: function (data) {
            if (checkRes(data)) {
                $('#add_sysuser_div').modal("hide");
                $('#tb_data').bootstrapTable('refresh', {url: 'blacklist.do?menuId=' + $("#menu_id").val(),queryParams: queryParamsVal});
            } else {
                $(".ibox-content").prepend(alertview("danger", "操作失败."));
            }
        },
        error: function () {
            $(".ibox-content").prepend(alertview("danger", "操作失败."));
        },
        complete: function () {
        }
    });
}

