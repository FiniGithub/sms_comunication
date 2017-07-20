var path = $("#path").val();
$(function() {
	$.post(path + "/smsUserApiConfig/apiConfigList.do",function(data) {
			var obj = data["data"];
			var rightHtml = '';
			var leftHtml = '<ul id="api-ul">';

			var i = 0;
			for ( var key in obj) {
				if (i == 0) {
					leftHtml += '<li onclick="showli(this);" class="active"><a href="javascript:void(0);">'
								+ key + '</a></li>';
					var value = obj[key];
					rightHtml += "<div class='li-show'>" + value+ "</div>";
					i = 1;
				} else {
					leftHtml += '<li onclick="showli(this);"><a href="javascript:void(0);">'
										+ key + '</a></li>';
					var value = obj[key];
					rightHtml += "<div class='li-hide'>" + value+ "</div>";
				}
			}
			leftHtml += '</ul>';
			$(".leftMenu").html(leftHtml);
			$(".rightContent").html(rightHtml);
			initHighlighting();
	});
});

/**
 * 点击左侧标题，显示对应的内容
 * 
 * @param v
 */
function showli(obj) {
	var index = $(obj).index();
	var onShowDiv = $("#right").children("div").eq(index);
	$(onShowDiv).show();
	$(onShowDiv).siblings().hide();
	$(obj).siblings().removeClass('apiwd-active');
	$(obj).siblings().children("a").removeClass('apiwd-active');
	$(obj).addClass('apiwd-active');
	$(obj).children("a").addClass('apiwd-active');
}

var events = $("body");
var catalog = null;


/**
 * 初始化高亮插件
 */
function initHighlighting() {
	$('pre code').each(function(i, block) {
		hljs.highlightBlock(block);
	});
	hljs.initLineNumbersOnLoad();
}


events.on('article.open', function(event, url, init) {
	 
	if ('pushState' in history) {

		if (init == false) {
			history.replaceState({}, '', url);
			init = true;
		} else {
			history.pushState({}, '', url);
		}

	} else {
		location.hash = url;
	}
});