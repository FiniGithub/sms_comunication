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
		<div style="text-align:right">
				<input type="button" class="btn btn-info searchbtns search" value="导出上月统计表"
				onclick="orderExport12('${ctx}')">
			</div>
	</div>
	<div class="com-content">
		<div class="com-menu inp-menu">
			<input type="hidden" id="menu_id" value="${menuId}" />
             
            
           <div class="inp-box ">
                 <span class="inp-title" >代理账号：</span>
                 		<input class="btn_input"  id="user_input" type="text" placeholder="请输入账号">
                </div>  		
            
            
                
           <div class="inp-box ">
                 <span class="inp-title" >通道名称：</span>
                 		<input class="btn_input"  id="aisleName_input" type="text" placeholder="请输入通道组名称">
                </div>  		
          <div class="inp-box inpt-box-btn">
                 <span  class="inp-title"> 创建时间：</span>
			                               <input id="start_input" type="text" placeholder="年/月/日" />
			                               <span class="dataline">—</span><input id="end_input" type="text" placeholder="年/月/日"/>
			                 <button class="btn btn-warning" id="reset" type="button">重置</button>
			          
			            <button class="btn btn-info" type="button" id="search_btn">
						<span class="glyphicon glyphicon-search"></span></button>               
          </div>
			<div id="data_div" ></div>
		</div>
		
		<div class="com-menu" style="color: #FD0000;">
			<span>发送号码数：</span>
			<span id="i_snum">0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>消费计费条数：</span>
			<span id="i_bnum">0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>结算计费条数：</span>
			<span id="i_anum">0</span><br>
			<span>移动发送成功/失败/未知：</span>
			<span id="i_fnumUs">0/0/0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>联通发送成功/失败/未知：</span>
			<span id="i_fnumMs">0/0/0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>电信发送成功/失败/未知：</span>
			<span id="i_fnumTs">0/0/0</span>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div class="row">
			<table id="tb_data"></table>
		</div>
	</div>
</div>

<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/puser/puserStatistical.js"></script>
<script src="${ctx}/static/js/puser/orderExport.js"></script>
</body>
</html>