<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>api下载</title>
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
</head>
<body>
<input id="path" type="hidden" value="${ctx}">
<a href="${ctx}/static/sdk/qxxt.zip">JAVA版 点击下载</a>
</body>
</html>
