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
		<h4>代理管理</h4>
		<div class="title-remark">
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu">
			<input type="hidden" id="menu_id" value="${menuId}" />
             <span style="float:right;margin-right:25px;margin-top: 1.5px">
                  <div style="position: relative;">
			                               创建时间： <input id="start_input" type="text" />
			                               -<input id="end_input" type="text" />
			                 <button class="btn btn-warning" id="reset" type="button">重置</button>
			          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			            <button class="btn btn-info" type="button" id="search_btn">
						<span class="glyphicon glyphicon-search"></span></button>
                </div>
				
            </span>
            
            <span style="float:right;margin-right:15px;margin-top: 1.5px">
           	 	<div style="position: relative;">
                 	类型：	
                 	 <select id="i_type">
                    <option value="">请选择</option>
                    <option value="0">充值</option>
                    <option value="1">消费</option>
                    <option value="2">退款</option>
                </select>
                </div>  		
            </span>
          
			<div id="data_div" style="width: 60%"></div>
		</div>
		</br>
		</br>
		</br>
		<div class="com-menu" style="color: #FD0000;">
			<span>消费合计：</span>
			<span id="i_snum">0元</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>充值合计：</span>
			<span id="i_fnum">0元</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>退款合计：</span>
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
<script src="${ctx}/static/js/userMessage/puserBill.js"></script>
</body>
</html>