// var ChineseDistricts_city = {};
$(function () {
    $('#start_input').datetimepicker({
        lang: 'ch',
        format: "Y-m-d H:i",
        // timeFormat: 'hh:mm:ss',
        timepicker: true,    //关闭时间选项
        yearStart: 2000,     //设置最小年份
        yearEnd: 2050       //设置最大年份
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
        yearEnd: 2050        //设置最大年份
    });//设置中文;
    $.datetimepicker.setLocale('ch');//设置中文
    load_data();

    $("#search_btn").bind("click", function () {
    	refresh_data();
    });
    
    $("#cz_allot_btn").bind("click", function () {
    	$("#cz_merge_form").submit();
    });

    
    $("#save_data_btn").bind("click", function () {
    	var id=$("#i_id").val();
    	var fname=$("#i_firmName").val();
    	//if(id==null || id==""){
	    	var userEmal = $("#i_userEmail").val();
    	    var data = {
    		        "userEmal": userEmal,
    		        "fname":fname
    		};
	        ajaxCall({
	            url: "/puser/querySysUserbyMsmuser.do?id="+id,
	            type: "post",
	            data: JSON.stringify(data),
	            success: function (data) {
	            	
	                if (checkRes(data)) {
	                   $("#upload_form").submit();
	                }else if(data["retCode"] == "000001"){
	                	alert("系统账号已分配，请更换账号名称！");
		                return;
	                } else if(data["retCode"] == "000002"){
	                	alert("企业用户名已分配，请更换企业用户名！");
		                return;
	                }
	            },
	            error: function () {
	                $(".ibox-content").prepend(alertview("danger", "操作失败."));
	            },
	            complete: function () {
	            }
	        });
    	
    	//}else{
    	//	 $("#upload_form").submit();
    	//}
    });
    
    
    
    $('#i_instruct').change(function(){ 
    	var p1=$(this).children('option:selected').val();//这就是selected的值 
    	if(p1=="install" || p1=="uninstall"){
    		  $("#i_content").hide();
    		  $("#i_claim").show();
    		  
    	}else{
    		  $("#i_claim").hide();
    		  $("#i_content").show();
    	}
    }) 
    
     $('#i_claim').change(function(){ 
    	 var p1=$(this).children('option:selected').val();//这就是selected的值 
         $("#i_content").val(p1);
     })
     
     
      $('#i_aisleType').change(function(){ 
    	
    	 var p1=$(this).children('option:selected').val();//这就是selected的值 
    	 querySmsGroup(p1,null);
     })
     
     
    
    
    $("#reset").bind("click", function () {
       // $('#phone_input').val("");
        $('#start_input').val("");
        $('#end_input').val("");
    });
    
});


function querySmsGroup(p1,type){
	
	 var data={};
     ajaxCall({
         url: "/puser/querySmsGroup.do?gid="+p1,
         type: "post",
         data: JSON.stringify(data),
         success: function (data) {
             if (checkRes(data)) {
             	var obj = data["data"]
             	var aisleGroup = $("#i_aisleGroup");
             	var htl = "<option value=\"\">请选择</option>";
             	for (var i = 0; i < obj.length; i++) {
						var ss = obj[i];
						htl+="<option value=\""+ss["id"]+"\">"+ss["name"]+"</option>";
					}
             	aisleGroup.html(htl);
             }
             
             if(type!=null){
            	 $("#i_aisleGroup").val(type);
             }
         },
         error: function () {
             $(".ibox-content").prepend(alertview("danger", "操作失败."));
         },
         complete: function () {
         }
     });
}


function handle(name1,name2)   
{
	var titles = name1;
	if(name1==null){
		titles = name2;
	}
	var money = $("#"+titles+"").val();
	if(money<0.027){
		alert("通道组价格不得少于0.027元");
		$("#"+titles+"").val("");
	}
}  

//获取长度为len的随机字符串  
function _getRandomString(len) {  
    len = len || 32;  
    var $chars = '123456789ABCDEFGHJKMNPQRSTWXYZVULQI'; // 默认去掉了容易混淆的字符oO
    var maxPos = $chars.length;  
    var pwd = '';  
    for (i = 0; i < len; i++) {  
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));  
    }  
    return pwd;  
} 


function  addDate() {
	$("#i_id").val("");
	
	var bid = $("#s_bid").val();
	if(bid!=null && bid!=""){
		var userName = $("#s_userName").val();
		$("#i_bid").val(bid);
		$("#i_userName").val(userName);
	}else{
		$("#i_bid").val("");
	}
	$("#sysUserId").val("");
	$("#smsUserBlankId").val("");
	
	$("#i_name").val("");
	$("#i_userEmail").val("");
	$("#i_state").val(0);
	$("#i_replyUrl").val("");
	$("#i_reportUrl").val("");
	$("#i_aisleType").val("");
	$("#i_describes").val("");
	$("#i_phone").val("");
	
	$("#aisledata").hide();//表示display:none; 
	$("#i_joinupCoding").val("");
	$("#i_firmName").val("");
	$("#i_firmPwd").val("");
	$("#i_joinuoMax").val("");
	$("#i_firmIp").val("");
	$("#i_httpProtocol").prop("checked", false);//不选中
	$("#i_cmppProtocol").prop("checked", false);//不选中
	$("#i_defaultAgid").val("");
	var aisleGroup = $("#i_aisleGroup");
	var htl = "<option value=\"\">请选择</option>";
	aisleGroup.html(htl);
	
    $("#add_data_div").modal("show");
};



function cmppCheckbox(){
	if ($('#i_cmppProtocol').is(':checked')) {
		$("#aisledata").show();//表示display:block, 
		
		var time_end = new Date().getTime(); //设定目标时间
		var timeString = time_end+"";
		var i_firmName = timeString.substr(timeString.length-8);
		$("#i_firmName").val(i_firmName);
		var i_firmPwd = _getRandomString(12);
		$("#i_firmPwd").val(i_firmPwd);
	}else{
		$("#aisledata").hide();//表示display:none; 
		$("#i_joinupCoding").val("");
		$("#i_firmName").val("");
		$("#i_firmPwd").val("");
		$("#i_joinuoMax").val("");
		$("#i_firmIp").val("");
	}
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
        state: $("#state_select").val(), //状态
        tdState: $("#td_select").val(), //状态
        ptype: $("#ptype_id").val(), //
        email: $("#user_input").val(), //账号信息
        bid: $("#s_bid").val(), //账号信息
        startInput: $("#start_input").val(),//开始时间
        endInput: $("#end_input").val()//结束时间
    };
    return temp;
}

function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'puserList.do?menuId=' + $("#menu_id").val(),
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
                title: "客户名称",
                field: "name",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "客户账号",
                field: "email",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "剩余条数",
                field: "surplusNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
	                if(index==0){
	            		$("#i_statistical").html(row.statistical);
	            	}
	                return row.surplusNum;
                }
            },
            {
                title: "状态",
                field: "state",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if (!row.state || row.state==0) {
                    	return "启用";
                    }else if(row.state==1){
                    	return "禁用";
                    }
                }
            },
            {
                title: "账号类型",
                field: "aisleGroupType",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "所用通道组",
                field: "aisleGroup",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
        	    title: "免审短信数",
                field: "blankNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                   if(row.blankNum!=null && row.blankNum>0){
                	   return "<a href='"+$("#server_path").val()+"/userFreeTrial/listview2.do?id=141&email="+row.email+"'>"+row.blankNum+"</a>";
                   }else{
                	   return row.blankNum; 
                   }
                }
            },
            {
        	    title: "签名数",
                field: "signatureNum",
                align: "left",
                valign: "middle",
                sortable: "true",
                formatter: function (value, row, index) {
                    if(row.blankNum!=null && row.blankNum>0){
                 	   return "<a href='"+$("#server_path").val()+"/userFreeTrial/sysSignatureView.do?id=151&email="+row.email+"'>"+row.signatureNum+"</a>";
                    }else{
                 	   return row.blankNum; 
                    }
                 }
            },
            
            {
        	    title: "账号归属",
                field: "nickName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
        	    title: "团队",
                field: "teamName",
                align: "left",
                valign: "middle",
                sortable: "true"
            },
            {
                title: "创建时间",
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
                title: '操作',
                align: 'left',
                valign: "middle",
                formatter: function (value, row, index) {
                    return loadBtn(row.sysMenuBtns, row.id,row.name,row.surplusNum);
                }
            }
            ],
            responseHandler: function(a) {
            	var btn_arr = a["data"];
            	if (btn_arr!=null && btn_arr.length > 0) {
            	    var user_path = '/msmuUser/from/';
                    for (var k = 0; k < btn_arr.length; k++) {
                        var btn_obj = btn_arr[k];
                        
                        if (btn_obj["actionUrls"] == user_path + 'add.do') {
                            add_html = '<a id="add_data" href="javaScript:void(0);" onclick="addDate()">'
            								+'<button id="add_button" type="button" class="btn btn-info">新增</button>'
            						  +'</a> ';
                            $("#addSmsUsers").html(add_html);
        	            }
        	        }
            	}
            	
                return a;
            },
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
}

//加载菜单对应的按钮
function loadBtn(btn_arr, id,name,surplusNum) {
    var html = '', edit_html = '', delete_html = '',addNum = '';add_html='';
    if (btn_arr.length > 0) {
        var user_path = '/msmuUser/from/';
        for (var k = 0; k < btn_arr.length; k++) {
            var btn_obj = btn_arr[k];
            
           /* if (btn_obj["actionUrls"] == user_path + 'add.do') {
                add_html = '<a id="add_data" href="javaScript:void(0);" onclick="addDate()">'
								+'<button id="add_button" type="button" class="btn btn-info">新增</button>'
						  +'</a> ';
            }*/
            if(btn_obj["actionUrls"] == user_path + 'addNum.do'){
            	addNum = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editjeData(\'' + id + '\',\''+name+'\',\''+surplusNum+'\');"><i class="fa fa-paste"></i>添加条数</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'edit.do') {
            	edit_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + id + '\');"><i class="fa fa-paste"></i> 编辑</a> ';
            }
            if (btn_obj["actionUrls"] == user_path + 'remove.do') {
                delete_html = '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="indexdl(\'' + id + '\');"><i class="fa fa-paste"></i>登陆</a> ';
            }
        }
    }
    html = edit_html + delete_html +addNum;
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

function editData(id) {
    $(".fileinput-remove-button").trigger("click");
    $('#add_data_div').modal("show");
    ajaxCall({
        url: "/puser/formEdit.do?id=" + id,
        type: "get",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]["data"];
                $("#i_id").val(obj["id"]);
                $("#i_name").val(obj["name"]);
              
                $("#i_userEmail").val(obj["email"]);
                
                $("#i_bid").val(obj["bid"]);
                $("#i_userName").val(obj["nickName"]);
                
                $("#sysUserId").val(obj["sysUserId"]);
                
                $("#smsUserBlankId").val(obj["smsUserBlankId"]);
                
                $("#i_state").val(obj["state"]);
                
            	$("#i_replyUrl").val(obj["replyUrl"]);
            	$("#i_reportUrl").val(obj["reportUrl"]);
                
                $("#i_defaultAgid").val(obj["defaultAgid"]);
                
                $("#i_describes").val(obj["describes"]);
                
                $("#i_phone").val(obj["phone"]);
                
                
                var groupTypeId = obj["groupTypeId"];
                $("#i_aisleType").val(groupTypeId);
                
                if(groupTypeId!=null){
                	  var gid = obj["aisleGroupId"];
                      querySmsGroup(groupTypeId,gid);
                }
              
                
                var httpProtocol = obj["httpProtocol"];
                var cmppProtocol = obj["cmppProtocol"];
                
                if(httpProtocol==1){
               	   $("#i_httpProtocol").prop("checked",true); //选中
               }else{
               	  $("#i_httpProtocol").prop("checked", false);//不选中
               }
               if(cmppProtocol==1){
              	 $("#i_cmppProtocol").prop("checked",true); //选中
              	$("#aisledata").show();
              	$("#i_joinupCoding").val(obj["joinupCoding"]);
        		$("#i_firmName").val(obj["firmName"]);
        		$("#i_firmPwd").val(obj["firmPwd"]);
        		$("#i_joinuoMax").val(obj["joinuoMax"]);
        		$("#i_firmIp").val(obj["firmIp"]);
               }else{
              	 $("#i_cmppProtocol").prop("checked", false);//不选中
              	$("#aisledata").hide();//表示display:none; 
        		$("#i_joinupCoding").val("");
        		$("#i_firmName").val("");
        		$("#i_firmPwd").val("");
        		$("#i_joinuoMax").val("");
        		$("#i_firmIp").val("");
               }
     
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}


function indexdl(uid){
	$("#index_id").val(uid);
	$("#indexdl_merge_form").submit();
}

function del(id) {
    $("#btn_del").bind("click", function () {
        var param = {"id": id};
        ajaxCall({
            url: "/instruct/from/delete.do",
            type: "get",
            data: param,
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
    });
}


function editjeData(id,name,surplusNum){
	
	$("#i_czname").val("");
	$("#i_czid").val("");
	$("#i_symoney").val("");
	
	$("#i_czname").val(name);
	$("#i_czid").val(id);
	//$("#i_symoney").val(money);
	$('#je_data_div').modal("show");
    var param = {"id": id};
        ajaxCall({
        url: "/puser/from/querySurplusNum.do",
        type: "get",
        data: param,
        success: function (data) {
            if (checkRes(data)) {
            	$("#i_symoney").val(data["data"]["data"]);
            }
        },
        error: function () {
        },
        complete: function () {
        }
    });
}
