<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	String requestUri = request.getRequestURI();
	String contextPath = request.getContextPath();
	String url = requestUri.substring(contextPath.length() + 1);
%>
<c:set var="url_val" value="<%=url%>" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title></title>

<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/easyui.css">	
	<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/icon.css">
	 <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>		
	<script type="text/javascript" src="${ctx}/static/dzd/jquery.easyui.min.js"></script>	
    <link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">   
    <script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
    <script src="${ctx}/static/js/bootstrap-table.js"></script>
	<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>	
<decorator:head/>
</head>

<body>
	
	<!--内容-->
	<div>
		<decorator:body />
	</div>
</body>
</html>
