var STATUS_CODE = {
    "success":"000000"
};

var PAGE_SIZE = 10;
var SERVER_PATH;

$(function () {
    SERVER_PATH = $("#server_path").val();

    var $alert = $("#pager_main .alert-success:first");

    if($alert.size() > 0) {
        setTimeout(function () {
            $alert.fadeOut(function () {
                $alert.remove();
            });
        }, 5000);
    }

    var menu_active = $("#menu_active").val();
    if(menu_active) {
        var _array = menu_active.split("-");
        if(_array) {
            for(var i = 0; i < _array.length; i++) {
                $("#side-menu li." + _array[i]).addClass("active");
            }
        }
    }
});

function pageInfo (obj, pagenum, pagesize) {
    if(!obj)
        obj = {};
    
    obj["pagenum"] = pagenum;
    obj["pagesize"] = pagesize;

    return obj;
}

function checkRes (data) {
    return data && data["retCode"] == STATUS_CODE.success;
}

function getRetCode (data) {
    return data["retCode"];
}

function ajaxCall(option) {
    var _url = SERVER_PATH + option["url"], ts = new Date().getTime();

    if (_url.indexOf("?") == -1) {
        _url += "?ts=" + ts;
    } else {
        _url += "&ts=" + ts;
    }

    $.ajax({
        url: _url,
        dataType: "json",
        data: option["data"],
        type: option["type"],
        timeout: option["timeout"],
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            option["success"](data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status == 301) {   //超时
                window.location.href = SERVER_PATH + "/loginview";

                return;
            }

            if(option["error"])
                option["error"]();
        },
        complete: function () {
            if(option["complete"])
                option["complete"]();
        }
    });
}

//type类型:success/info/warning/danger
function alertview (type, msg) {
    return '<div class="alert alert-'+type+'">'+msg+'</div>';
}

function trim_all (str) {
    return str.replace(/\s/g, "");
}

function imageOtherSize (src_, size) {
    if(!src_)
        return "";

    var ext = src_.substring(src_.lastIndexOf("."));

    return src_.replace(ext, "") + size + ext;
}

function lb_err (id, text) {
    $("#" + id).text(text);
}

function checkMobile (mobile) {
    var re = /^1\d{10}$/;   //1开头-11位数字
    return re.test(mobile);
}

function removeHtmlTag (str) {
    return str.replace(/<\/p>/gi, "\n")
        .replace(/<br\/?>/gi, "\n")
        .replace(/<\/?[^>]+(>|$)/g, "");
}

function scrollToTopOrBottom (toTop) {
    if(!toTop)
        $('html,body').animate({scrollTop:$('.footer').offset().top}, 300);
    else
        $('html,body').animate({scrollTop:$('.navbar-static-top').offset().top}, 300);
}

function toOtherView(url) {
    url = SERVER_PATH + url;

    window.location.href = url;
}

function dateFormat(date) {
    var month = (date.getMonth() + 1) + "", day = date.getDate() + "";
    if(month.length < 2) {
        month = "0" + month;
    }

    if(day.length < 2)
        day = "0" + day;

    return month + "-" + day;
}

function tableLoading (colspan) {
    return '<tr><td class="text-center" colspan="'+colspan+'"><img src="'+SERVER_PATH+'/static/img/loading.gif" /></td></tr>';
}

function tableErr (colspan) {
    return '<tr><td class="text-center" colspan="'+colspan+'"><span class="text-danger help-block m-b-none">加载失败...</span></td></tr>';
}

function tableNoData (colspan) {
    return '<tr><td class="text-center" colspan="'+colspan+'"><span class="text-danger help-block m-b-none">暂无数据...</span></td></tr>';
}

//下拉框选择
function selectMenu (fn) {
    $(".dropdown-menu li a").bind("click", function () {
        var text = $(this).text();

        var $btn = $(this).closest(".input-group-btn").find("button");
        $btn.find(".btntext").text(text);

        var value = $(this).parent().attr("value");

        var $inputGroup = $(this).closest(".input-group-btn");
        $inputGroup.find(".select_value").val(value);

        if(fn)
            fn($(this));
    });
}

$.fn.numeral = function (op) {
    $(this).css("ime-mode", "disabled");
    this.bind("keypress", function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);  //兼容火狐 IE
        if (!$.browser.msie && (e.keyCode == 0x8))  //火狐下 不能使用退格键
        {
            return;
        }
        return code >= 48 && code <= 57 || code == 46;

    });
    this.bind("blur", function () {

        if (this.value.lastIndexOf(".") == (this.value.length - 1)) {
            this.value = this.value.substr(0, this.value.length - 1);
        } else if (isNaN(this.value)) {
            this.value = " ";
        }
    });
    this.bind("paste", function () {
        var s = clipboardData.getData('text');
        if (!/\D/.test(s));
        value = s.replace(/^0*/, '');
        return false;
    });
    this.bind("dragenter", function () {
        return false;
    });
    this.bind("keyup", function () {
        if(op && op.float) {
            this.value = this.value.replace(/[^\d.]/g, "");
            //必须保证第一个为数字而不是.
            this.value = this.value.replace(/^\./g, "");
            //保证只有出现一个.而没有多个.
            this.value = this.value.replace(/\.{2,}/g, ".");
            //保证.只出现一次，而不能出现两次以上
            this.value = this.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        } else {
            this.value = this.value.replace(/[^\d]/g, '');
        }
    });
};

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format('yyyy-MM-dd hh:mm:ss.S') ==> 2006-07-02 08:09:04.423
// (new Date()).Format('yyyy-M-d h:m:s.S')      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        'M+': this.getMonth() + 1, //月份
        'd+': this.getDate(), //日
        'h+': this.getHours(), //小时
        'm+': this.getMinutes(), //分
        's+': this.getSeconds(), //秒
        'q+': Math.floor((this.getMonth() + 3) / 3), //季度
        'S': this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
    return fmt;
}
/**
 * 移除数组中指定下标的元素
 * dx下标值
 * **/
Array.prototype.remove=function(dx) {
    if(isNaN(dx)||dx>this.length){return false;}
    for(var i=0,n=0;i<this.length;i++)
    {
        if(this[i]!=this[dx])
        {
            this[n++]=this[i]
        }
    }
    this.length-=1
}
/**
 * 下拉框搜索
 * search   搜索框ID
 * select   下拉框ID
 * **/
function searchInput(search, select){
    $('#'+search).bind('input propertychange',function () {
        var val = $(this).val();
        if(val){
            $('#'+select).find('option').each(function(){
                var text = $(this).text();
                if(text.indexOf(val)==-1){
                    $(this).hide();
                }else{
                    $(this).show();
                }
            });
        }else{
            $('#'+select).find('option').show();
        }
    });
}
/**
 * table搜索
 * search   搜索框ID
 * select   下拉框ID
 * **/
function searchTable(search, select){
    $('#'+search).bind('input propertychange',function () {
        var val = $(this).val();
        if(val){
            $('#'+select+'>tbody>tr').each(function(){
                var text = $($(this).children()[0]).text();
                if(text.indexOf(val)==-1){
                    $(this).hide();
                }else{
                    $(this).show();
                }
            });
        }else{
            $('#'+select+'>tbody>tr').show();
        }
    });
}
/**
 * 产品搜索
 * search   搜索框ID
 * select   下拉框ID
 * **/
function searchProduct(search, select){
    $('#'+search).bind('input propertychange',function () {
        var val = $(this).val();
        if(val){
            $('#'+select+'>li').each(function(){
                var text = $(this).text();
                if(text.indexOf(val)==-1){
                    $(this).hide();
                }else{
                    $(this).show();
                }
            });
        }else{
            $('#'+select+'>li').show();
        }
    });
}
