/**
 * 群发短信js
 *
 * @author:CHENCHAO
 * @date: 2017-3-27 9:30:00
 */
var path = $("#ctx").val();
$(function() {
	refreshControl();// 刷新
	generateUUID();// 生成唯一标识
	addPhoneDown();// 将输入的号码，添加到下方号码框
	btnFilterPhone();// 号码过滤
	getSignList();// 获取通道对应的签名
});

/**
 * 手动输入号码
 * 
 * @returns
 */
function addPhoneDown() {
	$("#btn_addPhone").click(function() {
		
		var uuid = $("#uuid").val();
		var headPhone = $("#text_phoneList").val();
		var data = {
				"uuid" : uuid,
				"phone" : headPhone
		};
		var phoneStr = headPhone.split("\n");
		if(phoneStr.length>10000){
			alert("限制输入号码最大为10000");
			return;
		}
		
		$.post(path + "/smsUser/handAddPhone.do",data,function(data) {
			var obj = data["data"];
			if (obj["code"] == 0) {
				var fileName = obj["fileName"];
				loadFileConfig();// 加载上传的文件信息
				
				// 号码总数
				var leadCount = $("#font-leadCount").html();
				if(leadCount !=null && leadCount!='' && leadCount!="0"){
					leadCount = parseInt(leadCount) + parseInt(obj["phoneSize"]);
				}else{
					leadCount = obj["phoneSize"];
				}
				$("#font-leadCount").html(leadCount);
			} else {
				alert(data["data"]["msg"]);
			}
		});
	});
}

/**
 * 删除单项号码
 */
function delPhone(obj) {
	var fileName = $(obj).next().html();
	var uuid = $("#uuid").val();
	var url = path + '/smsUser/deleteFile.do';
	$.post(url, {"uuid" : uuid,"fileName":fileName}, function(data) {
		loadFileConfig();
	});
}

/**
 * 加载上传的文件信息
 */
function loadFileConfig(){
	$("#phone_show").html("");
	var uuid = $("#uuid").val();
	$.post(path + "/smsUser/queryPhoneConfigUUID.do",{"uuid":uuid},function(data){
		var obj = data["data"];
		if(obj.length == 0){
			$("#phone_show").html("");
			$("#font-leadCount").html("0");
			return;
		}
		
		var htmlData = "";
		for(var i=0;i<obj.length;i++){
			if(i==0){
				$("#font-leadCount").html(obj[0].allPhoneNum);
			}
			// 文本提示内容
			var msg = "添加号码";
			if(obj[i].type == 1){
				msg = "导入号码";
			}
			var addMsg = "<span style='margin-right:10px'>"+msg+"</span>";
			var numMsg ="";
			if(obj[i].type==0){
				numMsg = "<span  style='margin-right:10px'>"+obj[i].phoneSize+ "个</span>";
			}else{
				var showFName = obj[i].fileName;
				showFName = showFName.replace("/fileUpload/","");
				showFName = showFName.replace(".txt","");
				numMsg = "<span  style='margin-right:10px'>"+showFName+ "</span>";
			}
			
			
			
			var phoneMsg = "<span  style='margin-right:10px'>"+ obj[i].phone+ "</span>";
			var x = "<span onclick='delPhone(this);' style='color:red'>删除</span>";
			var f = "<span style='display:none;'>"+ obj[i].fileName + "</span>";
			htmlData += "<div>" + addMsg+ numMsg + phoneMsg + x+ f + "</div>";
		}

		// 替换或填入内容
		var phoneData = $("#phone_show").html();
		if (phoneData == null|| phoneData == "") {
			$("#phone_show").html(htmlData);
		} else {
			
			$("#phone_show").html(phoneData+ htmlData);
		}
		$("#text_phoneList").val("");
	});
}



/**
 * 号码过滤
 */
function btnFilterPhone() {
	$("#btn_filter_phone").click(function() {
		var uuid = $("#uuid").val();
		var data={"uuid":uuid};
		$.ajax({
			url : path + "/smsUser/phoneVerify.do",
			type : "post",
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8",
			success : function(obj) {
				
				var data = obj["data"];
				var allNumber = data["allNumber"];// 所有号码数量
				var validNum = data["validPhoneNum"];// 有效号码数量
				var repeatNum = data["repeatPhoneNum"];// 重复号码数量
				var invalidNum = data["invalidPhoneNum"];// 错误号码数量
				var validDataList = data["validList"];// 有效号码集合

				$("#filter_jdall").html(allNumber);
				$(".filter_cghm").html(validNum);
				$("#filter_jdrepet").html(repeatNum);
				$("#filter_jderror").html(invalidNum);
				
				$("#font-leadCount").html(validNum);
				$("#lead_phone_div").hide();
				$("#filter_phone_div").show();
			},
			error : function() {
				$(".p-manage-msg").html("处理发生异常,请重试...");
				alert("错误");
			},
			complete : function() {

			}
		});
	});
}


/**
 * 获取通道对应的签名
 */
function getSignList() {
	var url = path + "/smsUser/signatureList.do";
	$.post(url, function(data) {
		var rowData = data["data"];
		var listData = rowData["signList"];
		if(listData!=null && listData !=''){
			bindSelectList("#sign_xlbox", listData);
		}
		var notice = rowData["notice"];
		var hint = rowData["hint"];
		$("#aisleMsg_div").html(notice);
		// alert("提示：" + hint);
	});
}

/**
 * 下拉框赋值
 * @param tag
 * @param arr
 */
function bindSelectList(tag, arr) {
	var state = 0;
	$(tag).empty();
	// select 添加提示
	if (tag == "#sign_xlbox") {
		state = 0;
	} 
	// select添加option属性
	for (var i = 0; i < arr.length; i++) {
		var key = '';
		var data = '';
		if (state == 0) {
			key = arr[i];
			data = arr[i];
			$(tag).append( "<p data-inp='qminp' onclick='addKey(this,\"" + key + "\",\"" + key + "\",0)'>" + data + "</p>");
		}
	}
}


//下拉框选中事件，直接赋值
function addKey(obj, arr, key, state) {
	var id = $(obj).attr("data-inp");
	$(obj).parent().hide();
	var content = $("#content").val();

	// 只替换签名
	if (state == 0) {
		$("#" + id).val("【"+arr+"】");
	}
	var signLength = $("#" + id).val().length;
	var length = $("#content").val().length;

	// 内容长度超过300，则截取前300位
	if (length + signLength > 300) {
		content = content.substring(0, 300-signLength);
		length = content.length;
		$("#content").val(content);
	}
	$(".dxlength").text(length + signLength);
}





// 刷新控件
function refreshControl() {
	$("#text_phone").val("");
	$("#text_phone-w").val("");
	$("#sign_hidden").val("");
	$("#tria_hidden").val("");
	$("#content").val("");
	$("#text_phone_list").val("");
	$("#font-leadCount").html("0");
	$(".li_hide").hide();
	$(".dxlength").html("0");
	setTextareaContent();// 用户没有过发送记录,则默认短信内容,手势显示
	generateUUID();
}

/**
 * 显示手势
 */
function setTextareaContent(){
	var textarea_content = $("#textarea_content").val();
	if(textarea_content!=null && textarea_content!=''){
		var phone = $("#text_phone_input").val();
		$("#content").val(textarea_content);
		$("#text_phone-w").val(phone);
	}else{
		$("#content").val("");
		$(".dx-tishi-img").hide();
	}
}




/**
 * 生成唯一标识，上传文件
 * 
 * @returns
 */
function generateUUID() {
	var s = [];
	var hexDigits = "0123456789abcdef";
	for (var i = 0; i < 36; i++) {
		s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
	}
	s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
	s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the
	// clock_seq_hi_and_reserved
	// to 01
	s[8] = s[13] = s[18] = s[23] = "-";

	var uuid = s.join("");
	$("#uuid").val(uuid);
}