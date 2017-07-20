<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>    
    <title>全网信通-登录日志</title>
    <link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
    <script src="${ctx}/static/js/commons/common.js"></script>
    
    <script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>   
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
     <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/laydate/need/laydate.css"/>
 <script src="${ctx}/static/dzd/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">   
    <script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>
    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <style>
    	.modal-dialog{
    		width:480px;
    	}
    </style>
    <![endif]-->
    
   
</head>
<body>

<input id="id_input" type="hidden" value="${id}" />
<input id="path" type="hidden" value="${ctx}">
<div class="content-box" style="text-align:center;">

        <div class="inp-box ">
            <span class="inp-title"> 账号：</span>
            <input  class=' inp-box-inp' id="email" type="text" placeholder="">
        </div>


    <div class="inp-box ">
        <span class="inp-title"> 日志内容：</span>
        <input  class=' inp-box-inp' id="loginState" type="text" placeholder="">
    </div>

    <div class="inp-box ">
        <span class="inp-title"> IP地址：</span>
        <input  class=' inp-box-inp' id="ip" type="text" placeholder="">
    </div>

    <div class="inp-box inpt-box-btn">
        <span class="inp-title"> 日期：</span> <input id="start_input"  class=' inp-box-inp'
                                                     type="text" placeholder=""/> <span
            class="dataline">至</span> <input  class=' inp-box-inp'
            id="end_input" type="text" placeholder=""/>

    </div>
    <div class="inp-box inpt-box-btn">

        <input class="f-btn" type="button" value="查询" id="search_btn">

    </div>
</div>
	<div class="com-table-box">

		<table id="com-table" class="container head-box pd0 com-table"></table>
		<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>
	</div>

<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>

<script src="${ctx}/static/js/log/loginLog.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>