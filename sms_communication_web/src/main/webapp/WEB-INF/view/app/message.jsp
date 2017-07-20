<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,user-scalable=no">
<title>消息</title>
<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
<link rel="stylesheet"
	href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/static/css/app/style.css">
<link rel="stylesheet" href="${ctx}/static/css/app/message.css">

</head>
<!--[if lt IE 7]>
    		亲，请升级您的浏览器!
   <![endif]-->
<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<![endif]-->
<!--[if lte IE 8]><script src="js/selectivizr.js"></script>
<link rel="stylesheet" type="text/css" href="css/ie8.css"/>
	
<![endif]-->
<body>
	<input id="path" type="hidden" value="${ctx}">

	<div class="content-box">
		<div class="container head-box pd0 flex-c indextilt">
			<i class="inborder">&nbsp;</i><span class="intilte">消息</span><img
				class="index-logo" src="../static/img/send/dxhf.png" />
		</div>
		<div class="container head-box pd0 flex-c pho-search-box new-box">

			<div style="width: 100%;">
				<table class="news-ul" id="msg-table"></table>
 

				<!-- <div class="container head-box pd0 flex-c pagetrun">
					首页 <a href="">上一页>&nbsp;</a> <a href="">下一页> &nbsp;</a> <a href="">
						尾页>></a>
				</div> -->
			</div>



		</div>
	</div>
 
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<script src="${ctx}/static/js/commons/bootstrap.min.js"></script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<%-- <script src="${ctx}/static/js/app/page.js"></script> --%>
<script src="${ctx}/static/js/app/send/message.js"></script>
<!--[if lte IE 8]><script src="js/ie8.js"></script>	<![endif]-->
</body>

</html>