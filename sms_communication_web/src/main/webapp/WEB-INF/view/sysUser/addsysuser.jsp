<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<div class="row">
		<div class="col-xs-10">
			<h4>
				客户管理 &nbsp;&nbsp;&nbsp;&nbsp; <a href="${ctx}/sysUser/listview.do"
					class="text-right">返回</a>
			</h4>
			<hr/>
		</div>

	</div>
	<form class="form-horizontal">
		<input type="hidden" id="id" value="${sysUser.id}" />
		<div class="form-group">
			<label for="email" class="col-sm-2 control-label">邮箱</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" id="email"
					value="${sysUser.email}">
			</div>
		</div>
		<div class="form-group">
			<label for="nickName" class="col-sm-2 control-label">昵称</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" id="nickName"
					value="${sysUser.nickName}">
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button class="btn btn-primary" type="button" id="btn_sub">确
					认</button>
			</div>
		</div>
	</form>
	<script src="${ctx}/static/js/sysuser/addsysuser.js"></script>
</body>
</html> 