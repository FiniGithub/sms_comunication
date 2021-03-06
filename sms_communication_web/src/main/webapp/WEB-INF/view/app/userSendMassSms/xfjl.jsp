<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,user-scalable=no">
    <title>全网信通-消费记录</title>
    <link
            href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
    <script src="${ctx}/static/js/commons/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/laydate/need/laydate.css"/>
       <script src="${ctx}/static/dzd/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
   
    <script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>
</head>
<body>
<input id="menuId" type="hidden" value="${menuId}"/>
<input id="input_superAdmin" type="hidden" value="${session_user.superAdmin}">
<input id="path" value="${ctx}" type="hidden"/>
<!--内容-->

<div class="content-box" style="text-align:center;">

    <div class="inp-box ">
        <span class="inp-title"> 账号：</span>
        <input id="email" class="inp-box-inp" type="text" placeholder="">
    </div>


    <div class="inp-box inpt-box-btn">
        <span class="inp-title"> 创建时间：</span>
        <input id="start_input" class="inp-box-inp" type="text"/>
        <span class="dataline">至</span>
        <input id="end_input" type="text" class="inp-box-inp"/>
    </div>
    <div class="inp-box inpt-box-btn">

        <input class="f-btn" type="button" value="查询" id="search_btn">

    </div>
</div>
<div class="com-table-box">
    <table rules="none" id="fs-table" class="container head-box pd0 com-table fs-table"></table>
    <div class="minlid">
        <a id="firstpage" href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input
            class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
    </div>
</div>


<script src="${ctx}/static/js/commons/common.js"></script>

<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>

<script src="${ctx}/static/js/app/send/xfjl.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>

</html>