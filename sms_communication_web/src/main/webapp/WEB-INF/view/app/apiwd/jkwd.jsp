<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,user-scalable=no">
<title>千讯信通平台接口文档</title>

<link rel="stylesheet"
	href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/static/css/app/style.css">
<link rel="stylesheet" href="${ctx}/static/css/elastic-box.css">
<link rel="stylesheet" href="${ctx}/static/css/app/api/api.css">

<link rel="stylesheet" href="${ctx}/static/js/app/highlight/style/jstree.css" />
<link rel="stylesheet" href="${ctx}/static/js/app/highlight/style/default.css">
<link rel="stylesheet" href="${ctx}/static/js/app/highlight/style/zenburn.css">
<style type="text/css">
.li-show {
	display: block;
}

.li-hide {
	display: none;
}

.apiwd-active {
	background-color: #35c6ef!important;
	color: white!important;
}
.leftMenu ul li a:focus{
	color: white!important;
}
.leftMenu>ul>li>a{
    width:100%;
    height:100%;
    display: inline-block;
    padding-left: 9px;
    text-decoration:none;
    color: #000000;
}
.width-hundred-percent{
  width: 100%;
}
.leftMenu{
  position: absolute;
  left:0;
  top:69px;
  bottom:0;
  width:279px;
  border-right:1px solid #ddd;
  margin-top:0;
  z-index:301;
}
.cnblogs-markdown{
  position: absolute;
  left:280px;
  top:69px;
  z-index:300;
  bottom:0;
  right: 0;
  overflow-y: auto;
}
.cnblogs-markdown pre{
    background-color: #000;
    padding-left:0;
}
.cnblogs-markdown .hljs-line-numbers {
    text-align: center;
    border-right: 1px solid #ccc;
    color: #999;
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    width:2em;
}
.cnblogs-markdown{
	margin:0 auto;overflow-x:auto;
}
.rightContent{
	width:980px;
	margin:0 auto;
	font-size:14px;
}
.cnblogs-markdown table{
	overflow:visible;
 	word-break:normal!important;
}
.cnblogs-markdown table th:nth-child(3){
	min-width:90px;
}

</style>
</head>

	
<body style="min-width:auto">
	<input id="path" type="hidden" value="${ctx}">
	<div>
		<div class="head-box pd0 flex-c indextilt width-hundred-percent">
			<i class="inborder">&nbsp;</i><span class="intilte">千讯信通平台接口文档</span>
		</div>
		<div class="head-box pd0 flex-c width-hundred-percent">
			<div class="width-hundred-percent">
				<div class="leftMenu"></div>

				<div class="cnblogs-markdown" style="background-color: white;">
					<div id="right" class="rightContent">
					</div>
	</div>
	</div>
	</div>
	</div>

	<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
	<script src="${ctx}/static/js/commons/bootstrap.min.js"></script>
	<script src="${ctx}/static/js/bootstrap-datetimepicker.min.js"
		type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/static/js/bootstrap-datetimepicker.zh-CN.js"
		type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/static/js/app/page.js"></script>
	
    <!--代码高亮-->
    <script src="${ctx}/static/js/app/highlight/jstree.js"></script>
    <script src="${ctx}/static/js/app/highlight/highlight.js"></script>
    <script src="${ctx}/static/js/app/highlight/highlightjs-line-numbers.min.js"></script>
	<script src="${ctx}/static/js/app/send/api.js"></script>
</body>
</html>