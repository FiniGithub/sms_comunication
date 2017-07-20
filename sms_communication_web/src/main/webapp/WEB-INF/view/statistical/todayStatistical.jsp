<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset='utf-8'>
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>

<link
	href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css"	rel="stylesheet">	
<!-- Custom Fonts -->
<script src="${ctx}/static/js/commons/common.js"></script>
<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
<link href="${ctx}/static/css/jquery.datetimepicker.css"
	rel="stylesheet">
<script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
<!--[if IE]>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/dzdie8.css"/>
	<script src="${ctx}/static/js/appml5shiv.js"></script>
	<script src="${ctx}/static/js/applectivizr.js"></script>
	<![endif]-->
</head>
<body>

<input type="hidden" id="path" value="${ctx}">
<input type="hidden" id="logTime" value="today" />
	
	<div class="com-content">
		<div class="com-menu inp-menu" style="text-align:center;">
			<input type="hidden" id="menu_id" value="${menuId}" />

            
           <div class="inp-box ">
                 <span class="inp-title" >账号：</span>
                 		<input class="inp-box-inp" id="user_input" type="text" placeholder="">
           </div>  		
                
	            <span id="aisleSpan" style="display: none;">
	                <span  class="inp-title">通道类型：</span>	
	                <select id="aisle_select">
	                    <option value="">-全部-</option>
	                    <c:forEach var="item" items="${aisleNames}" varStatus="status">   
							<option key="${item}" value="${item}" >${item}</option>
						</c:forEach> 
	                </select>
                </span>
           
	            <span id="nickSpan" style="display: none;">
	                <span  class="inp-title">归属：</span>	
	                <select id="nickName_select">
	                    <option value="">-全部-</option>
	                    <c:forEach var="item" items="${nickNames}" varStatus="status">   
							<option key="${item}" value="${item}" >${item}</option>
						</c:forEach> 
	                </select>
            	</span>
            
            <div class="inp-box">               
	            <input class="f-btn" type="button" value="查询"  id="search_btn">
	            <input class="f-btn" type="button" value="刷新"  id="refresh_btn">
            </div>
        
        </div>
			<div id="data_div" ></div>
		</div>
                
		<div class="row">
			<table id="tb_data"></table>
			<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>



		</div>

<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/statistical/todayStatistical.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>