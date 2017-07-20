<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset='utf-8'>
	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fileinput.css">
    <link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
    <script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
</head>
<body>
	<div>
		<div class="title-remark"></div>
		<div class="com-title">
			<h4>短信回复</h4>
			<hr/>
		</div>
		<div class="com-content">
			<div class="com-menu">

				<a href="#" id="add_sysuser">
					<!-- <button id="add_button" type="button" class="btn btn-info">新增</button> -->
				</a> 
				<span style="float:left; margin-right: 25px; margin-top: 1.5px">
					<div style="position: relative;">
						客户名称：<input id="name_input" type="text" /> 
						
					         手机号码:<input class="btn_input"id="user_input" type="text" placeholder="手机号码">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						加入时间:<input id="start_input" type="text" placeholder="年/月/日">~<input id="end_input" type="text" placeholder="年/月/日">
						<button class="btn btn-info" type="button" id="search_btn">
								<span class="glyphicon glyphicon-search"></span>
							</button>
						</br>
						
					<!-- 	账号：<input id="email_input" type="text" /> -->
						回复内容：<input id="content_input" type="text"/> 
							
					</div>
				</span>
						
			</div>
		</div>
			<input type="hidden" id="menu_id" value="${menuId}" />
			<div class="row">
				<table id="tb_data"></table>
			</div>
		</div>


	</div>

	<div class="modal fade" id="del">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">确定删除</h4>
				</div>
				<div class="modal-body">
					<p>确认删除?删除后将无法复原</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取
						消</button>
					<button type="button" class="btn btn-primary" id="btn_del">确
						定</button>
				</div>
			</div>
		</div>
	</div>


    <script src="${ctx}/static/js/bootstrap-table.js"></script>
     <script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
     <script src="${ctx}/static/js/fileinput.js"></script>
	<script src="${ctx}/static/js/userSmsManage/smsReceiveReplyPush.js"></script>
</body>
</html>