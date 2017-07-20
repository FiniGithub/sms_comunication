<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset='utf-8'>
	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fileinput.css">
    <link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
    <script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
    
	<style>
		.select {
			background-color: #fff;
			background-position: right center;
			background-repeat: no-repeat;
			border: 1px solid #888;
			border-radius: 3px;
			box-sizing: border-box;
			font: 12px/1.5 Tahoma,Arial,sans-serif;
			height: 32px;
			padding: 0 0.5em;
		}
		.input_css_line{
			width: 30%;border:1px solid #ccc;border-radius:4px;padding:6px 12px;
		}
		td{
			height: 50px;
		}
	</style>
</head>
<body>

<div>
	<div class="com-title">
		<h4>代理统计</h4>
		<div class="title-remark">
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu">
			<input type="hidden" id="menu_id" value="${menuId}" />
             <span style="float:right;margin-right:25px;margin-top: 1.5px">
                  <div style="position: relative;">
			                               时间： <input id="start_input" type="text" />
			                               -<input id="end_input" type="text" />
			                 <button class="btn btn-warning" id="reset" type="button">重置</button>
			          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			            <button class="btn btn-info" type="button" id="search_btn">
						<span class="glyphicon glyphicon-search"></span></button>
                </div>
				
            </span>
            
			<div id="data_div" style="width: 60%"></div>
		</div>
		</br>
		</br>
		</br>
		<div class="com-menu" style="color: #FD0000;">
			<span>计费量：</span>
			<span id="i_snum">0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>移动发送成功/失败/未知：</span>
			<span id="i_fnumUs">0/0/0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>联通发送成功/失败/未知：</span>
			<span id="i_fnumMs">0/0/0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>电信发送成功/失败/未知：</span>
			<span id="i_fnumTs">0/0/0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>消费金额：</span>
			<span id="i_xfnum">0元</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>结算金额：</span>
			<span id="i_unum">0元</span>
		</div>
		<div class="row">
			<table id="tb_data"></table>
		</div>
	</div>
</div>

<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/userMessage/puserStatistical.js"></script>
</body>
</html>