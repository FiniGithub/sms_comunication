<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
    String requestUri = request.getRequestURI();
    String contextPath = request.getContextPath();
    String url = requestUri.substring(contextPath.length() + 1);
%>
<c:set var="url_val" value="<%=url%>"/>
<!DOCTYPE html>
<html lang="en">

<head>


    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/style2.css">
    <script type="text/javascript" src="${ctx}/static/dzd/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/dzd/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/dzd/index2.js"></script>
    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/ie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <![endif]-->
</head>
<body class="easyui-layout">
<noscript>
    抱歉，请开启脚本支持
</noscript>
<input type="hidden" value="${ctx}" id="path"/>
<!--头部-->
<div region="north" border="false" class="h-box">
    <div class="head">
        <div class="logo">
        </div>
        		<div class="announcement-box">        	
						公告：<span class="announcement">${session_alise_group.notice}</span>				
        		</div>
				
        <ul class="u-box">
           
            <li style="position: relative;">
				
                <span class="ic ic-user"></span>
                <span class="u-name">${user.email}</span>
                <a class="exit-btn" href="${ctx}/logout.do">[退出]</a>
				<a href="${ctx}/announcement.do#consult" target="_blank" class="help-box">[咨询与反馈]</a>
            </li>


        </ul>

    </div>
</div>

<!-- menu -->
<div region="west" split="true" title="&nbsp" style="width:160px;overflow:hidden;margin-right: 20px;"
     class="center-box">
    <div id="left-m" class="easyui-accordion" fit="true" style="padding-right: 10px;padding-top: 40px;">

        <!-- 加载菜单 -->
        <c:forEach items="${sessionScope.menuList}" var="m" varStatus="status">
            <c:if test="${m.name != '短信管理'}">
                <div title="${m.name}" style="overflow:auto;padding-bottom:14px;">
                    <ul class="page-box">
                        <c:forEach items="${m.child}" var="child">
                            <li key="${child.url}">
                                <span class="ic ic-sanj"></span>
                                <a href="javascript:void(0);" data-url="${child.url}?id=${child.id}">
                                    <c:out value="${child.name}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
        </c:forEach>
       


    </div>
</div>
<!-- TAB -->
<div region="center" title="Main Title" style="overflow:hidden;">
    <div id="content-boxd" class="easyui-tabs" fit="true" border="false">

        <div id="hostweb" title="首页" style="padding:20px;overflow:hidden;">
            <iframe name="indextab" scrolling="auto" src="${ctx}/smsHomePage/index.do" frameborder="0" style="width:100%;height:100%;"></iframe>
        </div>

    </div>
</div>


</div>
</div>
</body>
</html>
