<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>首页</title>
</head>
<body>
<input id="path" type="hidden" value="${ctx}" />
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<script type="text/javascript">
    var path = $("#path").val();
    $(function () {
        $.get(path + "/smsHomePage/homePageList.do", function (data) {
            var obj = data["data"];
            if (obj!= null) {
                $("#homePageStyle").html(obj.content);
            }
        });
    });
</script>




<div id="homePageStyle"></div>
</body>
</html>
