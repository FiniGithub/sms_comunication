$(function(){
	createUUID();
	
	$("#send_btn").unbind("click");
	$("#send_btn").bind("click", function() {
		sendMessage();
	});
	
	$("#export_btn").unbind("click");
	$("#export_btn").bind("click", function() {
		exportMessage();
	});
	
	
	
	pageControl();
	jquploadFile() 
});

function pageControl(){
	// 移动号码分页
	mobilePaging();
	// 联通号码分页
	unicomPaging();
	// 电信号码分页
	telecomPaging();
	// 无效号码分页
	invalidPaging();
}

function disabledContorl() {
	if ($("#mobileCurrentPage").val() == 1) {
		$("#mobilePreviousPage").attr("disabled", true);
	} else if ($("#mobileCurrentPage").val() == $("#mobileTotalPage").val()) {
		$("#mobileNextPage").attr("disabled", true);
	}
	
	if ($("#unicomCurrentPage").val() == 1) { 
		$("#unicomPreviousPage").attr("disabled", true);
	} else if ($("#unicomCurrentPage").val() == $("#unicomTotalPage").val()) {
		$("#unicomNextPage").attr("disabled", true);
	}
	
	if ($("#telecomCurrentPage").val() == 1) { 
		$("#telecomPreviousPage").attr("disabled", true);
	} else if ($("#telecomCurrentPage").val() == $("#telecomTotalPage").val()) {
		$("#telecomNextPage").attr("disabled", true);
	}
	
	if ($("#invalidCurrentPage").val() == 1) {
		$("#invalidPreviousPage").attr("disabled", true);
	} else if ($("#invalidCurrentPage").val() == $("#invalidTotalPage").val()) {
		$("#invalidNextPage").attr("disabled", true);
	}
}

function invalidPaging() {// 1-未知号码分页--首页
	$("#invalidHomePage").unbind("click");
	$("#invalidHomePage").bind("click", function() {
		$("#invalidCurrentPage").val(1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 2-未知号码分页--上一页
	$("#invalidPreviousPage").unbind("click");
	$("#invalidPreviousPage").bind("click", function() {
		$("#invalidCurrentPage").val(Number($("#invalidCurrentPage").val()) - 1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 3-未知号码分页--下一页
	$("#invalidNextPage").unbind("click");
	$("#invalidNextPage").bind("click", function() {
		$("#invalidCurrentPage").val(Number($("#invalidCurrentPage").val()) + 1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 4-未知号码分页--末页
	$("#invalidLastPage").unbind("click");
	$("#invalidLastPage").bind("click", function() {
		$("#invalidCurrentPage").val($("#invalidTotalPage").val());
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
}

function telecomPaging() {// 1-电信号码分页--首页
	$("#telecomHomePage").unbind("click");
	$("#telecomHomePage").bind("click", function() {
		$("#telecomCurrentPage").val(1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 2-电信号码分页--上一页 
	$("#telecomPreviousPage").unbind("click");
	$("#telecomPreviousPage").bind("click", function() {
		$("#telecomCurrentPage").val(Number($("#telecomCurrentPage").val()) - 1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 3-电信号码分页--下一页
	$("#telecomNextPage").unbind("click");
	$("#telecomNextPage").bind("click", function() {
		$("#telecomCurrentPage").val(Number($("#telecomCurrentPage").val()) + 1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 4-电信号码分页--末页
	$("#telecomLastPage").unbind("click");
	$("#telecomLastPage").bind("click", function() {
		$("#telecomCurrentPage").val($("#telecomTotalPage").val());
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
}

function unicomPaging() {// 1-联通号码分页--首页
	$("#unicomHomePage").unbind("click");
	$("#unicomHomePage").bind("click", function() {
		$("#unicomCurrentPage").val(1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 2-联通号码分页--上一页
	$("#unicomPreviousPage").unbind("click");
	$("#unicomPreviousPage").bind("click", function() {
		$("#unicomCurrentPage").val(Number($("#unicomCurrentPage").val()) - 1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 3-联通号码分页--下一页
	$("#unicomNextPage").unbind("click");
	$("#unicomNextPage").bind("click", function() {
		$("#unicomCurrentPage").val(Number($("#unicomCurrentPage").val()) + 1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 4-联通号码分页--末页
	$("#unicomLastPage").unbind("click");
	$("#unicomLastPage").bind("click", function() {
		$("#unicomCurrentPage").val($("#unicomTotalPage").val());
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
}

function mobilePaging() {// 1-移动号码分页--首页
	$("#mobileHomePage").unbind("click");
	$("#mobileHomePage").bind("click", function() {
		$("#mobileCurrentPage").val(1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 2-移动号码分页--上一页
	$("#mobilePreviousPage").unbind("click");
	$("#mobilePreviousPage").bind("click", function() {
		$("#mobileCurrentPage").val(Number($("#mobileCurrentPage").val()) - 1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 3-移动号码分页--下一页
	$("#mobileNextPage").unbind("click");
	$("#mobileNextPage").bind("click", function() {
		$("#mobileCurrentPage").val(Number($("#mobileCurrentPage").val()) + 1);
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
	// 4-移动号码分页--末页
	$("#mobileLastPage").unbind("click");
	$("#mobileLastPage").bind("click", function() {
		$("#mobileCurrentPage").val($("#mobileTotalPage").val());
		distinguishPhoneFile($("#mobileCurrentPage").val(),$("#unicomCurrentPage").val(),$("#telecomCurrentPage").val(),$("#invalidCurrentPage").val());
	});
}

function sendMessage() {
	var isSend = false;
	var isCheckBox = false;
	var isEmpty= false;
	var isInvalid = false;
	// 0：联通，1：移动，2：电信
	var operators = "";
	
	if ($("#mobileCheckbox").get(0).checked && $("#mobile").val() != "") {
		isSend = true;
		operators += ",1";
	}
	if ($("#unicomCheckbox").get(0).checked && $("#unicom").val() != "") {
		isSend = true;
		operators += ",0";
	}
	if ($("#telecomCheckbox").get(0).checked && $("#telecom").val() != "") {
		isSend = true;
		operators += ",2";
	}
	if ($("#invalidCheckbox").get(0).checked && $("#invalid").val() != "") {
		isInvalid = true;
	}
	
	if ($("#mobileCheckbox").get(0).checked || $("#unicomCheckbox").get(0).checked || 
			$("#unicomCheckbox").get(0).checked || $("#invalidCheckbox").get(0).checked) {
		isCheckBox = true;
	}
	
	if($("#invalidCheckbox").get(0).checked){
		isInvalid = true;
	}
	
	if ($("#mobile").val() == "" && $("#unicom").val() == "" && $("#telecom").val() == "" && $("#telecom").val() == "" && $("#invalid").val() == "") {
		isEmpty = true;
	}
	
	if(isEmpty){
		alert("号码为空，无法发送，请先上传号码！");
		return;
	}else if(!isCheckBox){
		alert("请选择需要发送的号码！");
		return;
	}else if(isInvalid){
		alert("未知号码无法发送！");
		return;
	}else if(isSend){
		window.location.href = $("#path").val() + '/smsUser/sendView.do?forwardUuid=' + $("#forwardUuid").val() + "&operators=" + operators.toString().substring(1);
	}
}

function exportMessage() {
	var mobileOperator = 0;
	var unicomOperator = 0;
	var telecomOperator = 0;
	var invalidOperator = 0;
	
	if ($("#mobileCheckbox").get(0).checked && $("#mobile").val() != "") {
		mobileOperator = 1;
	}
	if ($("#unicomCheckbox").get(0).checked && $("#unicom").val() != "") {
		unicomOperator = 1;
	}
	if ($("#telecomCheckbox").get(0).checked && $("#telecom").val() != "") {
		telecomOperator = 1;
	}
	if ($("#invalidCheckbox").get(0).checked && $("#invalid").val() != "") {
		invalidOperator = 1;
	}
	
	if ($("#mobile").val() == "" && $("#unicom").val() == "" && $("#telecom").val() == "" && $("#telecom").val() == "" && $("#invalid").val() == "") {
		alert("号码为空，请先导入号码！");
		return;
	}
	
	if((mobileOperator + unicomOperator + telecomOperator + invalidOperator) == 0){
		alert("请选择需要导出的号码");
		return;
	}
	
	window.location.href='exportfilternumber.do?forwardUuid='+$("#forwardUuid").val()
		+'&mobileOperator='+mobileOperator+'&unicomOperator='+unicomOperator+'&telecomOperator='+telecomOperator+'&invalidOperator='+invalidOperator;
}

/**
 * 生成唯一标识，上传文件
 *
 * @returns
 */
function createUUID() {
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
    $("#forwardUuid").val(uuid);
}

function readFileUpload() {
	
   
    var file = $("#input_uploadFile").val();

    if (file == null || file == '') {
        alert("请先选择文件!");
        return;
    }

    $("#textfield").val(file);// 显示文件名称
}
function jquploadFile() {

    var datatype = "text";
    if (navigator.userAgent.indexOf("MSIE 9.0") > 0 && !window.innerWidth) {
        datatype = "iframe text";
    }
    var uuid = $("#uuid").val();
    $("#input_uploadFile").fileupload({
        url:'saveuploadfile.do?forwardUuid=' + $("#forwardUuid").val(),
        iframe: true,
        dataType: "iframe text",
        forceIframeTransport: true,  
        add: function (e, data) {
//	    	console.log(data)


        },
    }).on('fileuploadadd', function (e, data) {
//		console.log(data);
        var d = data;
        $("#udatephobtn").bind("click", function () {
        	 $(".proge-num-box img").show();
        	d.submit();
            $("#udatephobtn").unbind("click");
        });
     
    }).on('fileuploaddone', function (e, data) {   
    	 distinguishPhoneFile(1,1,1,1);
    	
    }).on("fileuploadprogressall", function (e, data) {
        console.log(data);
       
    })
}
/**
 * 点击确定，上传号码文件
 */
function updatePhoneFile() {

	$("#mobile").val("");
	$("#unicom").val("");
	$("#telecom").val("");
    $("#invalid").val("");
	
    var file = $("#input_uploadFile").val();
    if (file == null || file == '') {
        alert("请先选择文件!");
        return;
    }
    var formData = new FormData($("#uploadFile")[0]);
    
    $.ajax({
        url: 'saveuploadfile.do?forwardUuid=' + $("#forwardUuid").val(),
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (date) {
        	
        },
        error: function (data) {
        	
        }
    });
    
    // 运营商号码区分
    distinguishPhoneFile(1,1,1,1);
}

/**
 * 进行号码分运营商
 */
function distinguishPhoneFile(mobileCurrentPage,unicomCurrentPage,telecomCurrentPage,invalidCurrentPage) {
	$("#mobile").val("");
	$("#unicom").val("");
	$("#telecom").val("");
	$("#invalid").val("");
	
	var data = {
		forwardUuid : $("#forwardUuid").val(),
		mobileCurrentPage : mobileCurrentPage,
		unicomCurrentPage : unicomCurrentPage,
		telecomCurrentPage :  telecomCurrentPage,
		invalidCurrentPage : invalidCurrentPage
	};
	
    $.ajax({
        url: 'filterDistinguishOperator.do',
        type : 'POST',  
        data:data,
        success: function (date) {
        	var map = date["data"];
        	if(date["retCode"] == "000000"){
        		for(var i=0;i<map["mobileOperator"].length;i++){
        			$("#mobile").val($("#mobile").val() + map["mobileOperator"][i] + '\r\n');
        		}
        		for(var i=0;i<map["unicomOperator"].length;i++){
        			$("#unicom").val($("#unicom").val() + map["unicomOperator"][i] + '\r\n');
        		}
        		for(var i=0;i<map["telecomOperator"].length;i++){
        			$("#telecom").val($("#telecom").val() + map["telecomOperator"][i] + '\r\n');
        		}
        		for(var i=0;i<map["invalid"].length;i++){
        			$("#invalid").val($("#invalid").val() + map["invalid"][i] + '\r\n');
        		}
        		
        		 $('#upload_file').modal("hide");
        		
        		$("#mobileTotalPage").val(map["mobileTotalPage"]);
        		$("#unicomTotalPage").val(map["unicomTotalPage"]);
        		$("#telecomTotalPage").val(map["telecomTotalPage"]);
        		$("#invalidTotalPage").val(map["invalidTotalPage"]);
        		
            	$("#i_mobile").text(map["mobileLength"]);	
            	$("#i_unicom").text(map["unicomLength"]);	
            	$("#i_telecom").text(map["telecomLength"]);	
            	$("#i_invalid").text(map["invalidLength"]);	
            	
            	// 空数据无法翻页
                disablePageContorl();
                
                // 控制翻页不能点击
            	disabledContorl();
            	
            	$("#textfield").val("");
        	}else{
        		alert("分号失败");
        	}
        	
        },
        error: function (data) {
            alert("分号失败");
        },
        complete: function () {
        	 $(".proge-num-box img").hide();
        	 $('#upload_file').modal("hide");
        	 $("#udatephobtn,#upload_file .close,#upfilecenter").removeAttr("disabled");    	
            
        }
       
    });
    
}

function disablePageContorl() {
	var numberContorl = 20;
	// 没有数据不能点击翻页
	if ($("#mobile").val() != "" && Number($("#i_mobile").text()) > numberContorl) {
		$("#mobileHomePage").removeAttr("disabled");
		$("#mobilePreviousPage").removeAttr("disabled");
		$("#mobileNextPage").removeAttr("disabled");
		$("#mobileLastPage").removeAttr("disabled");
	}
	if ($("#unicom").val() != "" && Number($("#i_unicom").text()) > numberContorl) {
		$("#unicomHomePage").removeAttr("disabled");
		$("#unicomPreviousPage").removeAttr("disabled");
		$("#unicomNextPage").removeAttr("disabled");
		$("#unicomLastPage").removeAttr("disabled");
	}
	if ($("#telecom").val() != "" && Number($("#i_telecom").text()) > numberContorl) {
		$("#telecomHomePage").removeAttr("disabled");
		$("#telecomPreviousPage").removeAttr("disabled");
		$("#telecomNextPage").removeAttr("disabled");
		$("#telecomLastPage").removeAttr("disabled");
	}
	if ($("#invalid").val() != "" && Number($("#i_invalid").text()) > numberContorl) {
		$("#invalidHomePage").removeAttr("disabled");
		$("#invalidPreviousPage").removeAttr("disabled");
		$("#invalidNextPage").removeAttr("disabled");
		$("#invalidLastPage").removeAttr("disabled");
	}
}
$("#upfilecenter").click(function () {
    $("#upload_file").modal("hide");
});
$('#upload_file').on('show.bs.modal', function (e) {
    $(this).css('display', 'block');
    var modalHeight = $(window).height() / 2 - $('#upload_file .modal-dialog').height() / 2;
    var modalWidth = $(window).width() / 2 - $('#upload_file .modal-dialog').width() / 2;
    $(this).find('.modal-dialog').css({      
        'margin-top': modalHeight
    });
});
$('.modal').modal({backdrop: 'static', keyboard: false, show: false});