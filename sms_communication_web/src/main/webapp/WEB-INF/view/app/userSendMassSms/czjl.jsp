<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,user-scalable=no">
    <title>全网信通-充值记录</title>
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
<input type="hidden" id="roleId" value="${roleId}"/>
<input id="menuId" type="hidden" value="${menuId}"/>
<input id="path" value="${ctx}" type="hidden"/>
<!--内容-->

<div class="content-box" style="text-align:center;">
    <div class="inp-box ">
        <span class="inp-title"> 账号：</span>
        <input class="inp-box-inp" id="email" type="text" placeholder="">
    </div>


    <div class="inp-box inpt-box-btn">
        <span class="inp-title"> 日期：</span>
        <input id="start_input" class="inp-box-inp" type="text" placeholder=""/>
        <span class="dataline"> 至 </span>
        <input id="end_input" class="inp-box-inp" type="text" placeholder=""/>
    </div>


    <div class="inp-box ">
        <span> 类型：</span>
        <select id="type">
            <option value="">全部</option>
            <option value="0">人工充值</option>
            <c:if test="${roleId!=58}">
                <option value="2">失败返还</option>
                <option value="3">快钱充值</option>
            </c:if>
            <option value="4">赠送</option>
            <option value="6">转入</option>
            <option value="7">退费</option>
            <c:if test="${roleId==58 || roleId== 48 || session_user.superAdmin == 1}">
                <option value="8">核减</option>
            </c:if>
        </select>
    </div>

    <c:if test="${session_user.superAdmin == 1}">
        <div class="inp-box " id="belongDiv" style="display: none;">
            <span> 归属：</span>
            <select id="bid">
                <option value="">请选择</option>
                <c:forEach var="s" items="${sysUserList}">
                    <option value="${s.sysUserId}">${s.email}</option>
                </c:forEach>
            </select>
        </div>
    </c:if>

    <div class="inp-box inpt-box-btn">
        <input class="f-btn" type="button" value="查询" id="search_btn">
        <input class="f-btn" type="button" value="导出" id="dc_btn">
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

<script src="${ctx}/static/js/app/send/czjl.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>

</html>