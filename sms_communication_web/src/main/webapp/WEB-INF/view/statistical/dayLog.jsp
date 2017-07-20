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
<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/laydate/need/laydate.css"/>
 <script src="${ctx}/static/dzd/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
<!--[if IE]>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/dzdie8.css"/>
	<script src="${ctx}/static/js/appml5shiv.js"></script>
	<script src="${ctx}/static/js/applectivizr.js"></script>
	<![endif]-->
</head>
<body>

	<input type="hidden" id="logTime" value="today" />
	<input type="hidden" id="path" value="${ctx}">
<div>
	
	<div class="com-content" >
		<div class="com-menu inp-menu" style="text-align:center;">
			<input type="hidden" id="menu_id" value="${menuId}" />
             
            
           <div class="inp-box ">
            	<span class="inp-title" >账号：</span>
                 	<input class="inp-box-inp"  id="user_input" type="text" placeholder="">
           </div>  		
            
           <div class="inp-box ">
                <span class="inp-title" >手机号码：</span>
                 	<input class="inp-box-inp"  id="phone_input"  type="text" placeholder="">
           </div> 
            
           <div class="inp-box ">
                <span class="inp-title" >内容：</span>
                 	<input class="inp-box-inp"  id="content_input" type="text" placeholder="">
           </div> 
             		
          <div class="inp-box">
                <span  class="inp-title">发送状态：</span>	
                <select id="state_select">
                    <option value="">-全部-</option>
                    <option value="-1">发送中</option>
                    <option value="0">已发送</option>
                    <option value="100">发送成功</option>
                    <option value="2">发送失败</option>
                </select>
            </div>
            
            <div class="inp-box">               
	           
	            <input class="f-btn" type="button" value="查询"  id="search_btn">
	            <input class="f-btn" type="button" value="刷新"  id="refresh_btn">
		            <span id="exportSpan" style="display: none;">
		           		<input class="f-btn" type="button" value="导出号码"  id="export_btn">
		            </span>
	            
            </div>
            
			<div id="data_div" ></div>
		</div>
		
		<div class="com-menu" style="padding-left:10px;color: #FD0000;font-size:12px;">
			<span>提交短信</span>
			<span id="i_tnum">0条：</span>
			<span>发送中</span>
			<span id="i_inum">0条、</span>
			<span>已发送</span>
			<span id="i_enum">0条、</span>
			<span>发送成功</span>
			<span id="i_snum">0条、</span>
			<span>发送失败</span>
			<span id="i_fnum">0条</span>
		</div>
		<div class="row">
			<table id="tb_data"></table>
			<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>
		</div>
	</div>
</div>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/statistical/dayAndHistoryLog.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>