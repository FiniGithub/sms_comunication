<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,user-scalable=no">
    <title>全网信通-发送记录</title>
    <link rel="stylesheet" href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.css">
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
       <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/laydate/need/laydate.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
    <script src="${ctx}/static/js/commons/bootstrap.min.js"></script>
    <script src="${ctx}/static/js/bootstrap-table.js"></script>
    <script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>

<script src="${ctx}/static/dzd/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
    <style>

    </style>
    <!--[if !IE]><!-->
    <script type="text/javascript" src="${ctx}/static/dzd/copy.js"></script> <!--<![endif]-->

    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <style>
        .modal-dialog {
            width: 480px;
        }
    </style>
    <![endif]-->
</head>
<body>

<!--顶部-->
<input type="hidden" id="menu_id" value="${menuId}"/>
<input id="path" type="hidden" value="${ctx}">
<!--内容-->
<input id="state_input" type="hidden"/>
<input type="hidden" id="superAdmin" value="${session_user.superAdmin}"/>

<input type="hidden" id="account_jump" value="${account}"/>

<div class="content-box" style="text-align:center;">

    <div class="inp-box">
        <span>账号：</span>
        <input id="account_input" type="text" class="inp-box-inp"/>
    </div>


    <div class="inp-box">
        <span>日期：</span>


        <input class="inp-box-inp" id="start_input" type="text"/>

        <span>至</span>

        <input class="inp-box-inp"  id="end_input" type="text"/>


    </div>

    <div class="inp-box">
        <span>任务类型：</span>
        <select id="sendType">
            <option value="">-全部-</option>
            <option value="0">实时</option>
            <option value="1">定时</option>
        </select>
    </div>
    <div class="inp-box">
        <!-- 通道类型 -->
        <c:if test="${session_user.superAdmin == 1}">
            <span id="aisleSpan" style="display: none;">
                <span>通道类型：</span>
                <select id="sel_groupId"></select>
            </span>
        </c:if>


        <input id="search_btn" class="f-btn" type="button"
               value="查询"/>
        <input id="refresh" class="f-btn" type="button"
               value="刷新" onclick="selectPage()"/>
        <c:if test="${back != null}">
	        <a href="javascript:history.go(-1)" class="btn f-btn b-btn"
	           style="line-height:22px!important;font-size:12px;position:relative;top: -2px;padding:0px;">
	            		返 回
	        </a>
        </c:if>
               
    </div>
</div>


<div class="com-table-box">
    <%--div class="container head-box pd0 flex-c  tasksum-box">
        发送号码数：<span class="fssum">0</span>；
        消费计费条数：<span class="xfsum">0</span>；
        结算计费条数：<span class="jssum">0</span>
    </div>--%>
</div>
<div class="row">
    <table id="fs-table"  class="com-tbale"></table>
    <div class="minlid">
        <a id="firstpage" href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input
            class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
    </div>
</div>

<script src="${ctx}/static/js/app/send/sendList.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>

</html>
