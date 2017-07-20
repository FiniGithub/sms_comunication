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
		.control-label{
			text-align: right;
		}
		.form-group{
			margin-top: 10px;
		}
	</style>
</head>
<body>

<div>
	<div class="com-title">
		<h4>修改密码</h4>
		<div class="title-remark">
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu">
		<a id="logout_form" href="/sms-platform/logout.do"></a>
		<form id="upload_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/userMessage/from/merge.do?menuId=${menuId}" enctype="application/x-www-form-urlencoded">
				<input type="hidden" id="menu_id" value="${menuId}" />
				<div class="modal-body" style="width: 70%;">
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">客户名称:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" disabled="name" id="i_name" value="${smsUser.name}" name="name">
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">登录账号:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" disabled="disabled" id="i_email" value="${smsUser.email}" name="email" />
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">当前密码:</label>
						<div class="col-sm-5">
								<input type="password" class="form-control"  id="i_pwd" name="pwd" />
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">新密码:</label>
						<div class="col-sm-5">
							<input type="password" class="form-control"  id="i_pwd1" name="pwd1"/>
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">确认密码:</label>
						<div class="col-sm-5">
							<input type="password" class="form-control"  id="i_pwd2" name="pwd2" />
						</div>
					</div>
					</br>
					<div class="form-group">
						<div class="col-sm-5">
							<input type="button" style="margin-left:50%;" class="form-control" id="save_pwd_but"  value="修改密码" />
						</div>
					</div>
			</div>
			</form>
		</div>
	</div>
</div>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/userMessage/smsUserUpdatePwd.js"></script>
</body>
</html>