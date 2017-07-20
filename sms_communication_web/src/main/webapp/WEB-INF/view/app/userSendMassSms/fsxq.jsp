<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,user-scalable=no">
    <title>全网信通-发送详情</title>
    <link rel="stylesheet"
          href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.css">
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
   
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
    <script src="${ctx}/static/js/commons/common.js"></script>
    <script src="${ctx}/static/js/commons/bootstrap.min.js"></script>
    <script src="${ctx}/static/js/bootstrap-table.js"></script>
    <script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
     <link rel="stylesheet" type="text/css"
          href="${ctx}/static/css/bootstrap-datetimepicker.min.css"/>
    <script src="${ctx}/static/js/bootstrap-datetimepicker.min.js"
            type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/js/bootstrap-datetimepicker.zh-CN.js"
            type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/js/app/page.js"></script>
</head>
<body>


<input id="menuId" type="hidden" value="${menuId}"/>
<input id="id_input" type="hidden" value="${id}"/>
<input type="hidden" id="superAdmin" value="${session_user.superAdmin}"/>
<input id="path" type="hidden" value="${ctx}">
<!--内容-->
<div class="content-box" style="text-align:center;">
    <div class="inp-box">
        <span>手机号码：</span><input id="phone" class="inp-box-inp" type="text" placeholder=""/>
    </div>
    <div class="inp-box">
        <span>发送状态：</span><select id="sendType">
        <option value="">全部</option>
        <option value="-2">发送中</option>
        <option value="-1">已发送</option>
        <option value="0">发送失败</option>
        <option value="1">发送成功</option>
    </select>
    </div>
    <div class="inp-box">
        <input id="search_btn" class="f-btn" type="button" value="查 询"/>
        <span id="exprot_span"></span>

        <a href="javascript:history.go(-1)" class="btn f-btn b-btn"
           style="line-height:22px!important;font-size:12px;position:relative;top: -2px;padding:0px;">
            返 回
        </a>
    </div>
    <div class="com-table-box" style="padding-left:10px;color:#4284DA;text-align:left;font-size:12px">
        提交短信：<span class="smsSum">0</span>条，
        发送中：<span class="fszsum">0</span>，
        已发送：<span class="yfssum">0</span>，
        发送成功：<span class="cgsum">0</span>，
        发送失败：<span class="sbsum">0</span>

    </div>
</div>

<div class="row">
<table id="com-table" class="container head-box pd0 com-table"></table>
<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>
</div>


<script src="${ctx}/static/js/app/send/sendDetail.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>

</body>
</html>