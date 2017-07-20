<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>

    <link
            href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <script src="${ctx}/static/js/commons/common.js"></script>
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
    <link href="${ctx}/static/css/jquery.datetimepicker.css"
          rel="stylesheet">
    <script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">   
    <script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>

    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <![endif]-->
</head>
<body>

<input id="id_input" type="hidden" value="${id}" />
<input id="path" type="hidden" value="${ctx}">
<!--内容-->
<div class="content-box" style="text-align:center;">
			 <div class="inp-box inpt-box-btn">
                <span class="inp-title">账号：</span><input id="email" class="inp-box-inp" type="text" placeholder="" />
            </div>
             <div class="inp-box inpt-box-btn">
              
			<input id="search_btn" class="f-btn" type="button" value="查 询" />
            </div>
	</div>
		<div class="row">
			<table id="com-table" class="container head-box pd0 com-table"></table>
			<div class="minlid">
					<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
				</div>
		</div>
	
	
<script src="${ctx}/static/js/commons/jquery.bootstrap.js"></script>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/app/send/filter.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>