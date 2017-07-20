function paging(totalCount, pageNo, pageSize, container) {
    pageNo = parseInt(pageNo);

    if (totalCount == 0)
        return;

    var getTotalPages = function () {
        if (totalCount < 0)
            return -1;
        var count = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            count++;
        }
        return parseInt(count);
    };
    var isHasNext = function () {
        return ((pageNo + 1) <= getTotalPages());
    };
    var isHasPre = function () {
        return (pageNo - 1 >= 1);
    };

    var getPrePage = function () {
        if (this.isHasPre())
            return pageNo - 1;
        else
            return pageNo;
    };
    var getNextPage = function () {
        if (isHasNext())
            return pageNo + 1;
        else
            return pageNo;
    };
    var getPageNo = function () {
        return (totalCount == 0) ? 0 : pageNo;
    };

    var show = function () {
        var beforeDelay = 2;
        var afterDelay = 3;
        var before = "";
        var after = "";
        var result = "<li>";

        if (getTotalPages() > 1) {
            if ((getPageNo() - beforeDelay - 1) > 1) {
                before = "<li><span>...</span></li>";
            }

            if ((getPageNo() + afterDelay + 1) < getTotalPages()) {
                after = "<li><span>...</span></li>";
            }

            // <
            if (getPageNo() == 1) {
                result += "<span>&laquo;</span>";
            }
            else {
                result += "<a id='prePage' href='javascript:void(0);' _to='" + (pageNo - 1) + "'>&laquo;</a>";
            }

            result += "</li>";

            // 1
            if (getTotalPages() > 1 && getPageNo() > 1) {
                result += "<li><a id='firstPage' href='javascript:void(0)'>1</a></li>";
            }

            result += before;

            // ...34
            for (var i = 1; i <= beforeDelay; i++) {

                if ((pageNo - (beforeDelay - i + 1)) > 1) {
                    result += "<li><a id='prePage" + i + "'";
                    result += " href='javascript:void(0);'>";
                    result += pageNo - (beforeDelay - i + 1);
                    result += "</a></li>";
                }
            }

            // 5
            result += "<li class='active'><a href='javascript:void(0);' id='page_self'> " + getPageNo() + "</a></li>";

            // 678...
            for (var i = 1; i <= afterDelay; i++) {
                if ((getPageNo() + i) < getTotalPages()) {
                    result += "<li><a";
                    result += " href='javascript:void(0);'>";
                    result += pageNo + i;
                    result += "</a></li>";
                }
            }

            result += after;

            // 10
            if (getTotalPages() > 1 && getPageNo() < getTotalPages()) {
                result += "<li><a id='lastPage'";
                result += " href='javascript:void(0);'>";
                result += getTotalPages();
                result += "</a></li>";
            }

            // >
            result += "<li>";

            if (getPageNo() == getTotalPages()) {
                result += "<span> &raquo;</span>";
            }
            else {
                result += "<a id='nextPage' href='javascript:void(0);' _to='" + (pageNo + 1) + "'> &raquo;</a>";
            }

            result += "</li>";
        }
        else {
            result += "<span>&laquo;</span></li><li class='active'><a href='javascript:void(0);' id='page_self'>1</a></li><li><span>&raquo;</span></li>";
        }

        return result;
    };

    if(!container)
        $("#paging ul").html(show());
    else
        $("#" + container + " ul").html(show());
}

function pagingEvent(fn, container) {
    var $paging;

    if(!container)
        $paging = $("#paging");
    else
        $paging = $("#" + container);

    $paging.on("click","a", function () {
        var _id = $(this).attr("id");

        if (_id == "page_self")
            return;

        var _to = 0;

        if (_id == "nextPage" || _id == "prePage") {
            _to = $(this).attr("_to");
        } else {
            _to = $.trim($(this).text());
        }

        fn(_to);
    });
}



/**
 * 计算总页数
 * @param pageNum
 * @param pageSize
 * @param total
 * @param pageName
 * @param totalName
 */
function getTotalPageCount(pageNum,pageSize,totalCount,pageName,totalName){
	var totalPage;
	totalPage = (totalCount % pageSize ==0) ? (totalCount/pageSize) : (totalCount/pageSize + 1);
	$("#"+pageName).text(pageNum);
	$("#"+totalName).text(parseInt(totalPage));
}




