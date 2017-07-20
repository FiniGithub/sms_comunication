var path = $("#ctx").val();
var STATUS_CODE = {
    "success": "000000"
};
function checkRes(data) {
    return data && data["retCode"] == STATUS_CODE.success;
}
$(function () {
    refreshControl();// 刷新
    generateUUID();// 生成唯一标识
    addPhoneDown();// 将输入的号码，添加到下方号码框
    btnFilterPhone();// 号码过滤
    getSignList();// 获取通道对应的签名
    rebind();//防止重复点击
    inpLength();// 短信内容输入的字数
//    jquploadFile();//插件  
    var startday = {
        elem: '#input_send_timing',
        format: 'YYYY-MM-DD hh:mm',
        min: laydate.now(), //设定最小日期为当前日期
        istime: true,
        istoday: true

    };
    laydate.skin('yahui');
    laydate(startday);


    $("#defalut_send_time").click(function () {
        $("#input_send_timing").val('');
    });
    $("#input_send_timing").click(function () {
        $("#defalut_send_time").prop("checked", false)
        $("#check_send_time").prop("checked", true);
    });
    $("#check_send_time").click(function () {
        $("#input_send_timing").click();

    });

    if ($("#forwardUuid").val() != "" && $("#forwardUuid").val() != undefined) {
        $("#uuid").val($("#forwardUuid").val());
        
        $("#bg").show(); $(".bg-d").show();
        loadFileConfig(-1, "load");
      
    }

});

//刷新控件
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
    generateUUID();
}


/**
 * 手动输入号码
 *
 * @returns
 */
function addPhoneDown() {
    $("#btn_addPhone").click(function () {

        var uuid = $("#uuid").val();
        var headPhone = $("#text_phoneList").val();

        var data = {
            "uuid": uuid,
            "phone": headPhone
        };
        var phoneStr = headPhone.split("\n");
        if (phoneStr.length > 50000) {
            alert("单次输入号码数量限5万个以内，可分批输入添加或选择导入号码文件。");
            $("#text_phoneList").val("");
            return;
        }

        $.post(path + "/smsUser/handAddPhone.do", data, function (data) {
            var obj = data["data"];
            if (obj["code"] == 0) {
                var fileName = obj["fileName"];
                loadFileConfig(1, "");// 加载上传的文件信息

                // 号码总数
                var leadCount = $("#font-leadCount").html();
                if (leadCount != null && leadCount != '' && leadCount != "0") {
                    leadCount = parseInt(leadCount) + parseInt(obj["phoneSize"]);
                } else {
                    leadCount = obj["phoneSize"];
                }
                $("#font-leadCount").html(leadCount);
                $("#lead_phone_div").hide();
                $("#filter_phone_div").hide();
                
            } else {
                alert(data["data"]["msg"]);
            }
        });
    });
}


/**
 * 加载上传的文件信息
 * * state:为了区分删除和刷新
 * uploadFileType:为了区分上传文件 ，不读取号码，删除文件读取号码
 */
function loadFileConfig(state, loadType) {

    var uuid = $("#uuid").val();
    var data = {"uuid": uuid, "operators": $("#operators").val(), "loadType": loadType};
    $.post(path + "/smsUser/queryPhoneConfigUUID.do", data, function (data) {
        var obj = data["data"];
        var obj = data["data"];
        if (obj == null || obj.length == 0) {
            $("#phone_show").html("");
            $("#font-leadCount").html("0");
            return;
        }

        var htmlData = "";
        for (var i = 0; i < obj.length; i++) {
            if (i == 0 && state == -1) {
                $("#font-leadCount").html(obj[0].allPhoneNum);
            }
            // 文本提示内容
            var msg = "添加号码";
            if (obj[i].type == 1) {
                msg = "导入号码";
            }
            var addMsg = "<span style='margin-right:10px;width:80px'>" + msg + "</span>";
            var numMsg = "";
            if (obj[i].type == 0) {
                numMsg = "<span style='margin-right:10px;width:260px'>" + obj[i].phoneSize + "个</span>";
            } else {
                var showFName = obj[i].fileName;
                showFName = showFName.replace("/fileUpload/", "");// 原文件名
                var newShowFileName = "";
                if (showFName.lastIndexOf("dzdqw")!=-1) {
                    var docIndex = showFName.lastIndexOf(".");
                    var suffixName = showFName.substring(docIndex, showFName.length);// 后缀名字
                    var fileNames = showFName.substring(0, docIndex);
                    fileNames = fileNames.substring(0, fileNames.lastIndexOf("dzdqw"));
                    newShowFileName = fileNames + suffixName;// 去除中间系统生成随机数的文件名
                } else {
                    newShowFileName = showFName;
                }
                numMsg = "<span  style='margin-right:10px;width:260px'>" + newShowFileName + "</span>";
            }


            var phoneMsg = "<span  style='margin-right:10px'>" + obj[i].phone + "</span>";
            var x = "<a onclick='delPhone(this);' class='delt-btn'>X</a>";
            var f = "<span style='display:none;'>" + obj[i].fileName + "</span>";
            htmlData += "<div>" + addMsg + numMsg + phoneMsg + x + f + "</div>";
        }

        // 替换或填入内容
        $("#phone_show").html("");
        var phoneData = $("#phone_show").html();
        if (phoneData == null || phoneData == "") {
            $("#phone_show").html(htmlData);
        } else {

            $("#phone_show").html(phoneData + htmlData);
        }
        $("#text_phoneList").val("");
        $("#bg").fadeOut(); $(".bg-d").fadeOut();
    });
}

/**
 * 号码过滤
 */
function btnFilterPhone() {
    $("#btn_filter_phone").click(function () {
        var uuid = $("#uuid").val();
        var data = {"uuid": uuid, "operators": $("#operators").val()};
        $.ajax({
            url: path + "/smsUser/phoneVerify.do",
            type: "post",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (obj) {

                var data = obj["data"];
                if (data == null) {
                    return;
                }

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
            error: function () {
                alert("服务器故障，请稍后再试...");
            },
            complete: function () {

            }
        });
    });
}

/**
 * 选择文件，文本框赋值
 */
function readFileUpload() {

    var file = $("#input_uploadFile").val();
    if (file == null || file == '') {
        return;
    }
    $("#textfield").val(file);// 显示文件名称
}

/**
 * 点击确定，上传号码文件
 *
 */
//
function jquploadFile() {

    var datatype = "text";
    if (navigator.userAgent.indexOf("MSIE 9.0") > 0 && !window.innerWidth) {
        datatype = "iframe text";
    }
    var uuid = $("#uuid").val();
    $("#input_uploadFile").fileupload({
        url: path + '/smsUser/fileUpload.do?uuid=' + uuid,
        iframe: true,
        dataType: "iframe text",
        forceIframeTransport: true,
        add: function (e, data) {
//	    	console.log(data)


        },
        done: function (e, data) {
//	    	$('.up_progress').hide();
//	    	$('.upl').remove();
//	        var d = data.result;
//	        console.log(d)/
//	        if(d.status==0){
//	            alert("上传失败");
//	        }else{
//	        	var imgshow = '<div class="images_zone"><input type="hidden" name="imgs[]" value="'+d.msg+'" /><span><img src="'+d.msg+'"  /></span><a href="javascript:;">删除</a></div>';
//	        	jQuery('.files').append(imgshow);
//	        }
        },
        progressall: function (e, data) {
//	    	console.log(data);	    	
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('.progebfb').text(progress);
        }
    }).on('fileuploadadd', function (e, data) {
//		console.log(data);
        var d = data;
        $("#udatephobtn").bind("click", function () {
            var formData = new FormData($("#uploadFile")[0]);

            d.submit();
//			  $.ajax({
//		            url: path + '/smsUser/fileUpload.do?uuid=' + uuid,
//		            type: 'POST',
//		            data: {
//		            	"uploadFile":d.
//		            },		            
//		            async: false,
//		            cache: false,
//		            contentType: false,
//		            processData: false,           
//		            success: function (obj) {
//		            alert(obj);
//		            },
//		            error: function (data) {
//		                alert("失败");
//		            },
//		            complete: function () {
//		            
//		            }
//		        });
            $("#udatephobtn").unbind("click");
        });
        $('.lead-finish').show();
        $('.progebfb').text("0");
    }).on('fileuploaddone', function (e, data) {
        var d = data.result;
        var obj = JSON.parse(d);
        if (obj["retCode"] == "000000") {
            var data = obj["data"];
            setPhoneNum(data);// 展示号码个数
            loadFileConfig(0, "");
            $("#textfield").val("");// 清空文件文本框
            // 计算导入号码总数
            var lendCount = $("#font-leadCount").html();
            var onceNum = $("#font-allNum-once").html();
            lendCount = getSum(lendCount, onceNum);
            $("#font-leadCount").html(lendCount);
            $("#font-allNum-once").html("0");
            $("#input_uploadFile").val("");
            $("#lead_phone_div").show();
            $("#filter_phone_div").hide();
            $('#upload_file').modal("hide");
        } else {
            alert(obj["data"]["msg"]);
        }

    }).on('fileupalways', function (e, data) {
        console.log(data);
        $('.lead-finish').hide();
        $('.progebfb').text("0");
    }).on("fileuploadprogressall", function (e, data) {
        console.log(data);
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('.progebfb').text(progress);
    })
}
$("#upfilecenter").click(function () {
    $("#upload_file").modal("hide");
});
///////////////////////////////////////
function updatePhoneFile() {

    var file = $("#input_uploadFile").val();

    if (file == null || file == '') {
        alert("请先选择文件!");
        return;
    }
//    $.ajax();
    $("#udatephobtn,#upload_file .close,#upfilecenter").attr("disabled", "disabled");
    $(".proge-num-box img").show();   // 显示加载效果
//    var formData = new FormData($("#uploadFile")[0]);
//    console.log(formData)
    var uuid = $("#uuid").val();
    var timer = setTimeout(function () {
        var ajax_option = {
            url: path + '/smsUser/fileUpload.do?uuid=' + uuid,
//    	        contentType:"text/html;charset=utf-8",
//    	        type: 'POST',    	        
//    	        async: false,
//    	        cache: false,
//    	        dataType:"text",
//    	        iframe:true,
//    	        contentType: false,
//    	        processData: false,           
            success: function (obj) {
                //console.log(obj);

//    	        	var result = obj.replace(/<pre[^>]*>/, "").replace('</pre>', '');
//    	        	 var obj = JSON.parse(result);
                if (obj["retCode"] == "000000") {
                    var data = obj["data"];

                    setPhoneNum(data);// 展示号码个数
                    loadFileConfig(0, "");
                    $("#textfield").val("");// 清空文件文本框
                    // 计算导入号码总数
                    var lendCount = $("#font-leadCount").html();
                    var onceNum = $("#font-allNum-once").html();
                    lendCount = getSum(lendCount, onceNum);
                    $("#font-leadCount").html(lendCount);
                    $("#font-allNum-once").html("0");
                    $("#input_uploadFile").val("");
                    $("#lead_phone_div").show();
                    $("#filter_phone_div").hide();

                    $('#upload_file').modal("hide");
                } else {
                    alert(obj["data"]["msg"]);
                }
            },
            error: function (data) {
                alert("失败");
            },
            complete: function () {
                $("#udatephobtn,#upload_file .close,#upfilecenter").removeAttr("disabled")
                $(".proge-num-box img").hide();
                clearTimeout(timer);
            }
        }
        $("#uploadFile").ajaxSubmit(ajax_option);

    }, 200);

}


/**
 * 点击发送，进行基本校验
 */
function sendBeforeVerify() {
    var phoneCount = $("#font-leadCount").html();// 号码
    var content = $("#content").val();// 短信内容
    var sign = "";// 签名
    var inputSign = $("#input_sign").val();
    var selectSign = $("#qminp").val();
    if (inputSign != null && inputSign != "") {
        sign = "【" + inputSign + "】";
    } else if (selectSign != null && inputSign != "") {
        sign = selectSign;
    }


    if (phoneCount == 0 || phoneCount == '') {
        alert("手机号码不能为空!");
        return;
    }

    if (sign == "" || sign == null) {
        alert("请填写短信签名!");
        return;
    }


    if (content == '' || content == null) {
        alert("请填写短信内容!");
        return;
    }

    content = sign + content;

    if ($("#check_send_time").is(":checked")) {
        if ($("#input_send_timing").val() == "") {
            alert("请填写发送时间!");
            return;
        }
    }

    $("#sendsms-btn").attr("disabled", "disabled");

    var timing = $("#input_send_timing").val();
    var uuid = $("#uuid").val();
    var data = {
        "content": content,
        "timing": timing,
        "uuid": uuid,
        "repet": 0,
        "invalid": 0
    };
    $("#bg").show(); $(".bg-d").show();
    $.ajax({
        url: path + "/smsUser/checkSendBeforeVerify.do",
        type: "post",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var obj = data["data"];
            var msg = obj["msg"];
            var code = obj["code"];
            var phoneData = obj["phoneMap"];

            if (code == 0) {// 检验通过
                sendFilterBaseInfo(phoneData);// 设置值
                showFilterPhone_modal();// 显示号码详情框
            } else if (code == -300) {// 需要输入验证码
                sendFilterBaseInfo(phoneData);// 设置值
                showVerify_modal();// 显示输入验证码模态框
            } else {
                if (isContains(msg, "短信内容中含有敏感词")) {
                    var newmsg = msg.split(",");
                    var os = getOs();
                    if (os == 'FF' || os == 'SF') {  //FireFox、谷歌浏览器用这个
                        alert(newmsg[0] + "\n" + newmsg[1]);
                    } else {   //IE系列用这个
                        alert(newmsg[0] + "\r\n" + newmsg[1]);
                    }
                } else {
                    alert(msg);
                }
            }
        },
        error: function () {
            alert("处理发生异常,请稍后再试...");
        },
        complete: function () {
        	 $("#bg").fadeOut(); $(".bg-d").fadeOut();
            $("#sendsms-btn").removeAttr("disabled");
        }
    });
}
function isContains(str, substr) {//正则判断是否含有指定字符串
    return new RegExp(substr).test(str);
}
function getOs() {
    if (navigator.userAgent.indexOf("MSIE") > 0) {
        return "IE"; //InternetExplor  
    }
    else if (isFirefox = navigator.userAgent.indexOf("Firefox") > 0) {
        return "FF"; //firefox  
    }
    else if (isSafari = navigator.userAgent.indexOf("Safari") > 0) {
        return "SF"; //Safari  
    }
    else if (isCamino = navigator.userAgent.indexOf("Camino") > 0) {
        return "C"; //Camino  
    }
    else if (isMozilla = navigator.userAgent.indexOf("Gecko/") > 0) {
        return "G"; //Gecko  
    }
    else if (isMozilla = navigator.userAgent.indexOf("Opera") >= 0) {
        return "O"; //opera  
    } else {
        return 'Other';
    }

}
// 获取验证码
function getVerCode() {

    var phone = $("#phone").html();
    if (phone == "") {
        alert("手机号码为空!");
        return;
    }

    var data = {"phone": phone};
    $.post(path + "/smsVertifyCode/getSmsVertifyCode.do", data, function (data) {
        if (data["retCode"] == "000000") {
            countBtn("#pwd-getcode", 60);// 60s倒计时
        } else {
            var msg = data["data"];
            alert(msg);
        }
    });

}


/**
 * 校验验证码是否正确
 */
function checkVerifyCode() {
    var phone = $("#phone").html();
    var code = $("#verifyCodeInput").val();
    if (code == null || code == "") {
        alert("请输入验证码!");
        $("#sendsmsyz-btn").removeAttr("disabled");
        return;
    }
    var data = {"phone": phone, "verifyCode": code};
    $.ajax({
        url: path + "/smsVertifyCode/checkVerifyCodeBySendSms.do",
        type: "post",
        data: data,
        success: function (data) {
            if (data["retCode"] == "000000") {
                showFilterPhone_modal();// 显示号码详情框
            } else {
                var msg = data["retMsg"];
                alert(msg);
            }

        },
        error: function () {
            alert("错误");
        },
        complete: function () {
            $("#sendsmsyz-btn").removeAttr("disabled");
        }
    });


}


/**
 * 发送号码详细
 */
function sendFilterBaseInfo(data) {
    var allNumber = data["allNumber"];// 所有号码数量
    var validNum = data["validPhoneNum"];// 有效号码数量
    var repeatNum = data["repeatPhoneNum"];// 重复号码数量
    var invalidNum = data["invalidPhoneNum"];// 错误号码数量

    var smsLength = $(".smslength").html();// 短信长度
    var smsNum = smsLength * validNum;// 短信条数

    $("#smsWords").html($(".dxlength").html());// 短信字数
    $("#smsTiao").html(smsLength);// 单条计费条数
    $("#send_smsLength").html(smsNum);// 共计费条数
    $("#validPhoneNum").html(validNum);// 有效号码

    $("#allPhoneNum").html(allNumber);
    $("#repeatPhoneNum").html(repeatNum);
    $("#invalidPhoneNum").html(invalidNum);
}


/**
 * 发送短信
 */
function sendSms() {
    var uuid = $("#uuid").val();// 唯一标识
    // 1.短信内容
    var sign = $("#qminp").val();
    if (sign == null || sign == "") {
        sign = "【" + $("#input_sign").val() + "】";
    }
    var sms_content = $("#content").val();
    var content = sign + sms_content;

    // 2.定时发送时间
    var timing = $("#input_send_timing").val();

    // 3.重复、错误号码数量
    var repetNum = $("#repeatPhoneNum").html();
    var invalidNum = $("#invalidPhoneNum").html();

    var data = {
        "content": content,
        "timing": timing,
        "uuid": uuid,
        "repet": repetNum,
        "invalid": invalidNum
    };
    $.ajax({
        url: path + "/smsUser/send.do",
        type: "post",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"];
                var isAudit = obj["audit"];
                var msg = obj["msg"];
                var code = obj["code"];
                var sendType = obj["sendType"];
                var surplusNum = obj["surplusNum"];
                var htmlMsg = '';
                if (code == 0 && sendType == 0) {// 发送成功
                    htmlMsg = '发送成功!';
                } else if (code == 0 && sendType == 1) {// 定时发送
                    htmlMsg = '定时任务成功！';
                } else {
                    htmlMsg = msg;
                }
                alert(htmlMsg);

                if (code == 0) {
                    $('#filter_modal').modal("hide");
                    $(".u-num").html(surplusNum);
                    window.location.reload();//刷新当前页面
                    generateUUID();// 成功，重新生成UUID唯一标识
                }
            } else {
                alert("服务器异常,请稍后再试!");
            }
        },
        error: function () {
            alert("错误");
        },
        complete: function () {
            $("#smssendBtn").removeAttr("disabled");
        }
    });

}


/**
 * 删除单项号码
 */
function delPhone(obj) {
    var fileName = $(obj).next().html();
    $(".result-num-box").hide();
    var uuid = $("#uuid").val();
    var url = path + '/smsUser/deleteFile.do';
    $.post(url, {"uuid": uuid, "fileName": fileName}, function (data) {
        loadFileConfig(-1, "load");
    });
}

/**
 * 清空文件
 */
function clearFile() {
    var uuid = $("#uuid").val();
    var url = path + '/smsUser/deleteFile.do';
    $.post(url, {"uuid": uuid}, function (data) {
        if (checkRes(data)) {
            // 导入号码数清空
            $(".result-num-box").hide();
            $(".result-num-box span").text("0");
            $("#font-leadCount").html("0");
            $(".li_hide").hide();

            $("#phone_show").html("");

            // 手动的号码为0
            $(".pho-count").html("0");
            $(".pho-load-result").val("");
            generateUUID();// 清空成功，重新生成UUID唯一标识
        }
    });
}


/*********************初始化数据;短信字数显示******************************/

/**
 * 输入字符数
 */
function inpLength() {
    // 输入短信内容
    $("#content").keyup(function () {
        var content = $("#content").val();
        content = $.trim(content);// 去除两端空格
        content = content.replace(/(^\n*)|(\n*$)/g, "");//去掉两端换行
        var select_sign = $("#qminp").val();// 下拉框签名
        var input_sign = $("#input_sign").val();// 输入签名
        var signLength = 0;// 签名长度
        if (select_sign != undefined && select_sign != null && select_sign != '') {
            signLength = select_sign.length;
        } else {
            signLength = input_sign.length;
        }
        var allLength = content.length + signLength;

        getSmsNumber(allLength, content, signLength);// 短信字数
        getContentLength(allLength);// 短信条数
        getSmsSurplusNumber(allLength);// 剩余输入字数
    });


    // 输入签名
    $("#input_sign").keyup(function () {
        var input_sign = $("#input_sign").val();
        var content = $("#content").val();
        var signLength = input_sign.length;
        var contentLength = content.length;
        var allLength = contentLength + signLength;
        getSmsNumber(allLength, content, signLength);// 短信字数
        getContentLength(allLength);// 短信条数
        getSmsSurplusNumber(allLength);// 剩余输入字数
    });

};

/**
 * 设置号码个数
 *
 * @param data
 */
function setPhoneNum(data) {

    var invalidNum = data["invalidPhoneNum"];// 错误号码数量
    var repeatNum = data["repeatPhoneNum"];// 重复号码数量
    var validNum = data["validPhoneNum"];// 有效号码数量

    $("#font-allNum-once").html(validNum);
    $("#jdrepet").html(repeatNum);// 重复号码
    $("#jderror").html(invalidNum);// 错误号码
    $(".cghm").html(validNum);// 有效号码
}

/**
 *
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
        if (i == 0) {
            $("#qminp").val("【" + arr[i] + "】");
        }
        if (state == 0) {
            key = arr[i];
            data = arr[i];
            $(tag).append("<p data-inp='qminp' onclick='addKey(this,\"" + key + "\",\"" + key + "\",0)'>" + data + "</p>");
        }
    }
    initSelectSmsNumber();// 下拉框设置短信条数、字数、剩余字数
}
//下拉框选中事件，直接赋值
function addKey(obj, arr, key, state) {
    var id = $(obj).attr("data-inp");
    $(obj).parent().hide();
    var content = $("#content").val();

    // 只替换签名
    if (state == 0) {
        $("#" + id).val("【" + arr + "】");
    }
    var signLength = $("#" + id).val().length;
    var length = $("#content").val().length;
    var allLength = length + signLength;
    getSmsNumber(allLength, content, signLength);// 短信字数
    getContentLength(allLength);// 短信条数
    getSmsSurplusNumber(allLength);// 剩余输入字数
}

/**
 * 获取通道对应的签名
 */
function getSignList() {
    var url = path + "/smsUser/signatureList.do";
    $.post(url, function (data) {
        var rowData = data["data"];
        var listData = rowData["signList"];
        if (listData != null && listData != '') {
            bindSelectList("#sign_xlbox", listData);
        }
        var notice = rowData["notice"];
        var hint = rowData["hint"];
        $("#aisleMsg_div").html(notice);
        // alert("提示：" + hint);
    });
}

/**
 * 获取短信字数
 * @param allLength
 * @param content
 * @param signLength
 */
function getSmsNumber(allLength, content, signLength) {
    // 内容长度超过300，则截取前300位
    if (allLength > 300) {
        content = content.substring(0, 300 - signLength);
        $("#content").val(content);
    }
    allLength = content.length + signLength;
    $(".dxlength").text(allLength);// 短信字数
}

/**
 * 获取短信条数
 * @param allLength
 */
function getContentLength(allLength) {
    // 短信条数
    var sigleSmsLength = 1;
    if (allLength > 70) {
        var smsTextLen = allLength - 70;
        sigleSmsLength = parseInt((smsTextLen / 66) + 1);
        if (smsTextLen % 66 > 0) {
            sigleSmsLength += 1;
        }
    } else {
        sigleSmsLength = 1;
    }
    $(".smslength").html(sigleSmsLength);
}

/**
 * 剩余输入字数
 * @param allLength
 */
function getSmsSurplusNumber(allLength) {
    var surplusNum;
    if (allLength >= 300) {
        alert("短信内容字数不能超过300字！");
        surplusNum = 0;
    } else {
        surplusNum = 300 - allLength;
    }
    $(".s-dxlength").html(surplusNum);
}

/**
 * 下拉框设置短信条数、字数、剩余字数
 */
function initSelectSmsNumber() {
    var select_sign = $("#qminp").val();// 下拉框签名
    var signLength = 0;
    if (select_sign != undefined && select_sign != null && select_sign != '') {
        signLength = select_sign.length;
    }
    $(".dxlength").text(signLength);// 短信字数
    $(".s-dxlength").html(300 - signLength);// 剩余字数
    if (signLength > 0) {
        $(".smslength").html(1);
    }
}


// 显示验证码框
function showVerify_modal() {
    $("#verifyAndDetailModal").modal("show");
    $("#verifyModal").show();
    $("#filterPhoneModal").hide();
}

function showFilterPhone_modal() {
    $("#verifyAndDetailModal").modal("show");
    $("#verifyModal").hide();
    $("#filterPhoneModal").show();
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


/**
 * 求和
 * @param a
 * @param b
 * @returns {*}
 */
function getSum(a, b) {
    if (isEmpty(a)) {
        a = 0;
    }
    if (isEmpty(b)) {
        b = 0;
    }
    a = parseInt(a);
    b = parseInt(b);
    return a + b;
}
// 非空验证
function isEmpty(str) {
    if (str == null || str == '') {
        return true;
    }
    return false;
}

/**
 * 确定按钮之后刷新窗口
 */
function fixedAw(obj) {
    $(obj).on('show.bs.modal', function (e) {
        $(this).css('display', 'block');
        var modalHeight = $(window).height() / 2 - $(obj + ' .modal-dialog').height() / 2;
        var modalWidth = $(window).width() / 2 - $(obj + '.modal-dialog').width() / 2;
        $(this).find('.modal-dialog').css({
            "margin-left": modalWidth,
            'margin-top': modalHeight
        });
    });
}
$('#upload_file').on('show.bs.modal', function (e) {
    $(this).css('display', 'block');
    var modalHeight = $(window).height() / 2 - $('#upload_file .modal-dialog').height() / 2;
    var modalWidth = $(window).width() / 2 - $('#upload_file .modal-dialog').width() / 2;
    $(this).find('.modal-dialog').css({      
        'margin-top': modalHeight
    });
});

$('#upload_file').on('hidden.bs.modal', function (e) {
    $(".proge-num-box img").hide();
    $("#textfield").val('');

})
function rebind() {
    $("#pwd-getcode").bind("click", function () {
        $(this).attr("disabled", "disabled");
        Button_Click(getVerCode, 200);
    });//验证按钮防止重复点击
    $("#smssendBtn").bind("click", function () {
        $(this).attr("disabled", "disabled");
        Button_Click(sendSms, 200);//确认发送按钮
    });

    $("#sendsms-btn").bind("click", function () {

        Button_Click(sendBeforeVerify, 200);//发送按钮
    });
    $("#sendsmsyz-btn").bind("click", function () {
        $(this).attr("disabled", "disabled");
        Button_Click(checkVerifyCode, 200);//发送验证按钮
    });

    var i = 0;  //判断点击次数寄存
    var closetimer = null;  //延时函数寄存
    function Button_Click(fn, time)//botton点击事件
    {
        i++;  //记录点击次数
        var action = fn;
        closetimer = window.setTimeout(function () {
            setout(action)
        }, time); //后执行事件 	提交
    }

    function setout(fn) {  //点击执行事件
        if (i > 1)   //如果点击次数超过1
        {
            alert("请勿频繁点击按钮!");
            window.clearTimeout(closetimer);  //清除延时函数
            closetimer = null;  //设置延时寄存为null
            i = 0;  //重置点击次数为0
        } else if (i == 1) {  //如果点击次数为1
            fn();// 咨询提交
            i = 0;  //重置点击次数为0
            //添加执行操作的代码
        }
    }
}
function textclear() {
    $("#content").val('');
    var content = $("#content").val();
    var select_sign = $("#qminp").val();// 下拉框签名
    var input_sign = $("#input_sign").val();// 输入签名
    var signLength = 0;// 签名长度
    if (select_sign != undefined && select_sign != null && select_sign != '') {
        signLength = select_sign.length;
    } else {
        signLength = input_sign.length;
    }
    var allLength = content.length + signLength;

    getSmsNumber(allLength, content, signLength);// 短信字数
    getContentLength(allLength);// 短信条数
    getSmsSurplusNumber(allLength);// 剩余输入字数
    $("#content").focus();
}
$('.modal').modal({backdrop: 'static', keyboard: false, show: false});