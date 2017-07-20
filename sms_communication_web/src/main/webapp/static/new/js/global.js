$(function () {



    $('#captcha').click();

    // bs自带方法-气泡提示
   // $('.aw-content-wrap .md-tip').tooltip('hide');

    // 顶部按钮收缩左侧菜单
    $('.aw-header .mod-head-btn').click(function ()
    {
        if ($('#aw-side').is(':visible'))
        {
            $('#aw-side').hide();

            $('.aw-content-wrap, .aw-footer').addClass('active');
        }
        else
        {
            $('#aw-side').show();

            $('.aw-content-wrap, .aw-footer').removeClass('active');
        }
    });

    // 左侧导航模拟滚动条
    $("#aw-side").perfectScrollbar({
        wheelSpeed: 20,
        wheelPropagation: true,
        minScrollbarLength: 20
    });

    // 左侧导航菜单的折叠与展开
     $('.mod-bar > li > a').click(function ()
    {
        if ($(this).next().is(':visible'))
        {
            $(this).next().hide();
             $(this).removeClass('active');
           
        }
        else
        {
            $('#aw-side').find('li').children('ul').hide();
           

            $(this).next().show();
        }

        $("#aw-side").perfectScrollbar('update');
    });  

    // 日期选择
    if (typeof (DateInput) != 'undefined')
    {
        $('input.mod-data').date_input();
    }

//    // 单选框 input checked radio 初始化
//    $('.aw-content-wrap').find("input").iCheck({
//        checkboxClass: 'icheckbox_square-blue',
//        radioClass: 'iradio_square-blue',
//        increaseArea: '20%'
//    });

    // input 菜单折叠，展开、拖动
//    $('.aw-nav-menu li .mod-set-head').click(function ()
//    {
//        if ($(this).parents('li').find('.mod-set-body').is(':visible'))
//        {
//            $(this).parents('li').find('.mod-set-body').slideUp();
//        }
//        else
//        {
//            $(this).parents('li').find('.mod-set-body').slideDown();
//
//            $(this).parents('li').siblings('li').find('.mod-set-body').slideUp();
//        }
//    });

    $(".aw-nav-menu").find('ul:first').dragsort({
        dragEnd: function () {
            var arr = [];
            $.each($('.aw-nav-menu ul li'), function (i, e) {
                arr.push($(this).attr('data-sort'));
            });
            $('#nav_sort').val(arr.join(','));

        }
    });


//    // input 单选框全选or 全取消
//    $('.aw-content-wrap .table').find(".check-all").on('ifChecked', function (e)
//    {
//        e.preventDefault();
//
//        $(this).parents('table').find(".icheckbox_square-blue").iCheck('check');
//    });
//
//    $('.aw-content-wrap .table').find(".check-all").on('ifUnchecked', function (e)
//    {
//        e.preventDefault();
//
//        $(this).parents('table').find(".icheckbox_square-blue").iCheck('uncheck');
//    });


    //微博发布用户
    $('.aw-admin-weibo-answer .search-input').bind("keydown", function()
    {
        if (window.event && window.event.keyCode == 13)
        {
            return false;
        }
    });

    // 微博提问用户删除
    $(document).on('click', '.aw-admin-weibo-publish .delete', function()
    {
        $('.aw-admin-weibo-publish').find('.search-input').val('').show();

        $(this).parents('li').detach();
    });

    // 微博接收用户删除
    $(document).on('click', '.aw-admin-weibo-answer li .delete', function()
    {
        $(this).parent().detach();

        weiboPost($(this));
    });


    // 概述页面，新增话题数，点击排序
    $('#sorttable thead').delegate("td","click",function()
    {
        if($(this).index()==0)
        {
            return false;
        }
        else
        {
            $(this).find('i').addClass('icon-down').show();

            $(this).siblings('td').find('i').removeClass('icon-down').hide();

            if($(this).index()==1)
            {
                subjectData('week');
            }
            else if ($(this).index()==2)
            {
                subjectData('month');
            }
            else if ($(this).index()==3)
            {
                subjectData('all');
            }
        }
    });

    $('#sorttable thead td:eq(2)').click();

});

function subjectData(type)
{
    AWS.loading('show');

    var tempTop = $('#sorttable').offset().top + $('#sorttable').height()/2 - 50;

    var tempLeft = $('#sorttable').offset().left + $('#sorttable').width()/2;

    $('#aw-loading').css({top:tempTop+'px',left:tempLeft+'px',position:'absolute'});

    $.get(G_BASE_URL + '/admin/ajax/topic_statistic/tag-'+ type +'__limit-10', function (result)
    {
        var tempLyout = '' ;

        for (var i = result.length - 1; i >= 0; i--) {
            tempLyout += '<tr><td></td><td></td><td></td><td></td></tr>';
        };
        $('#sorttbody').html(tempLyout);

        AWS.loading('hide');

        if(result == '')
        {
            $('.sorttable-mask').show();
        }
        else
        {
            $('.sorttable-mask').hide();

            $.each(result, function(key,value)
            {
                var tempObj = $('#sorttable tbody tr:eq('+ key +')');
                tempObj.find('td:eq(3)').text(value.all);
                tempObj.find('td:eq(2)').text(value.month);
                tempObj.find('td:eq(1)').text(value.week);
                tempObj.find('td:eq(0)').text(value.title);
            });
        }
    }, 'json');
}

function weiboPost(obj)
{
    $.post(G_BASE_URL + '/admin/ajax/weibo_batch/', {'uid': obj.attr('data-id'), 'action':obj.attr('data-actions')}, function (result)
    {
        if (result.errno == -1)
        {
            AWS.alert(result.err);

             $('.mod-weibo-reply li:last').detach();
        }
        else if (result.errno == 1)
        {
            if(result.rsm != null)
            {
                if (result.rsm.staus == 'bound')
                {
                    $('.mod-weibo-reply li:last .btn-primary').text('更新 Access Token');
                }
                else
                {
                   $('.mod-weibo-reply li:last .btn-primary').text('绑定微博');
                }
            }

            $(".alert-box").modal('hide');
        }
    }, 'json');
};


(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as anonymous module.
        define(['jquery'], factory);
    } else {
        // Browser globals.
        factory(jQuery);
    }
}(function ($) {

    var pluses = /\+/g;

    function encode(s) {
        return config.raw ? s : encodeURIComponent(s);
    }

    function decode(s) {
        return config.raw ? s : decodeURIComponent(s);
    }

    function stringifyCookieValue(value) {
        return encode(config.json ? JSON.stringify(value) : String(value));
    }

    function parseCookieValue(s) {
        if (s.indexOf('"') === 0) {
            // This is a quoted cookie as according to RFC2068, unescape...
            s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
        }

        try {
            // Replace server-side written pluses with spaces.
            // If we can't decode the cookie, ignore it, it's unusable.
            s = decodeURIComponent(s.replace(pluses, ' '));
        } catch(e) {
            return;
        }

        try {
            // If we can't parse the cookie, ignore it, it's unusable.
            return config.json ? JSON.parse(s) : s;
        } catch(e) {}
    }

    function read(s, converter) {
        var value = config.raw ? s : parseCookieValue(s);
        return $.isFunction(converter) ? converter(value) : value;
    }

    var config = $.cookie = function (key, value, options) {

        // Write
        if (value !== undefined && !$.isFunction(value)) {
            options = $.extend({}, config.defaults, options);

            if (typeof options.expires === 'number') {
                var days = options.expires, t = options.expires = new Date();
                t.setDate(t.getDate() + days);
            }

            return (document.cookie = [
                encode(key), '=', stringifyCookieValue(value),
                options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
                options.path    ? '; path=' + options.path : '',
                options.domain  ? '; domain=' + options.domain : '',
                options.secure  ? '; secure' : ''
            ].join(''));
        }

        // Read

        var result = key ? undefined : {};

        // To prevent the for loop in the first place assign an empty array
        // in case there are no cookies at all. Also prevents odd result when
        // calling $.cookie().
        var cookies = document.cookie ? document.cookie.split('; ') : [];

        for (var i = 0, l = cookies.length; i < l; i++) {
            var parts = cookies[i].split('=');
            var name = decode(parts.shift());
            var cookie = parts.join('=');

            if (key && key === name) {
                // If second argument (value) is a function it's a converter...
                result = read(cookie, value);
                break;
            }

            // Prevent storing a cookie that we couldn't decode.
            if (!key && (cookie = read(cookie)) !== undefined) {
                result[name] = cookie;
            }
        }

        return result;
    };

    config.defaults = {};

    $.removeCookie = function (key, options) {
        if ($.cookie(key) !== undefined) {
            // Must not alter options, thus extending a fresh object...
            $.cookie(key, '', $.extend({}, options, { expires: -1 }));
            return true;
        }
        return false;
    };

}));




