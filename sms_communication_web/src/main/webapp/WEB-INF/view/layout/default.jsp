<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	String requestUri = request.getRequestURI();
	String contextPath = request.getContextPath();
	String url = requestUri.substring(contextPath.length() + 1);
%>
<c:set var="url_val" value="<%=url%>" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title>系统后台<decorator:title/></title>
<!-- Bootstrap Core CSS -->
<link
	href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- MetisMenu CSS -->
<link
	href="${ctx}/static/bower_components/metisMenu/dist/metisMenu.min.css"
	rel="stylesheet">
<!-- Custom CSS -->
<link href="${ctx}/static/dist/css/sb-admin-2.css" rel="stylesheet">
<!-- Custom Fonts -->
<link
	href="${ctx}/static/bower_components/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<script src="${ctx}/static/js/commons/common.js"></script>
<decorator:head/>
</head>
<body>
	<input type="hidden" value="${ctx}" id="server_path" />
	<div id="wrapper">
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<c:if test="${sessionScope.smsUser!=null}">
					<a class="navbar-brand" href="${ctx}/userMessage/welcome.do">代理商后台</a>
				</c:if>
				<c:if test="${sessionScope.smsUser==null}">
					<a class="navbar-brand">系统后台</a>
				</c:if>
				
			</div>
			<ul class="nav navbar-top-links navbar-right">
				<li style="color: white;"><c:if test="${not empty sessionScope.session_user}">
						<h4>${sessionScope.session_user.email}</h4>
					</c:if></li>
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#"> <i class="fa fa-user fa-fw"></i>
						<i class="fa fa-caret-down"></i>
				</a>
					<ul class="dropdown-menu dropdown-user">
						<li><a href="#" id="update_pwd"><i
								class="fa fa-gear fa-fw"></i>修改密码</a></li>
						<li class="divider"></li>
						<li><a href="${ctx}/logout.do"><i
								class="fa fa-sign-out fa-fw"></i>退出</a></li>
					</ul></li>
			</ul>
			<div class="navbar-default sidebar" role="navigation">
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">
						<!-- 加载菜单 -->
						<c:forEach items="${sessionScope.menuList}" var="m"
							varStatus="status">
							<li
								<c:if test="${status.index eq 0 and url_val == 'welcome.do'}">class="active"</c:if>
								tag="${status.index}"><a href="#"><i
									class="fa fa-bar-chart-o fa-fw"></i> <c:out value="${m.name}" /><span
									class="fa arrow"></span></a>
								<ul class="nav nav-second-level">
									<c:forEach items="${m.child}" var="child">
										<li key="${child.url}"><a
											href="${ctx}${child.url}?id=${child.id}"><c:out
													value="${child.name}" /></a></li>
									</c:forEach>
								</ul></li>
						</c:forEach>
					</ul>
				</div>

			</div>
		</nav>
		<div id="page-wrapper">

			<decorator:body />

		</div>
	</div>


	<div class="modal fade bs-example-modal-lg" id="update_pwd_div">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">修改密码</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<input type="hidden" id="update_pwd_id"
							value="<c:if test="${not empty sessionScope.session_user}">${sessionScope.session_user.id}</c:if>" />
						<div class="form-group">
							<label for="oldPwd" class="col-sm-2 control-label">原密码</label>
							<div class="col-sm-5">
								<input type="password" class="form-control" id="oldPwd">
							</div>
						</div>
						<div class="form-group">
							<label for="newPwd" class="col-sm-2 control-label">新密码</label>
							<div class="col-sm-5">
								<input type="password" class="form-control" id="newPwd">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="update_pwd_btn">确定</button>
				</div>
			</div>
		</div>
	</div>

	<!-- jQuery -->
	<script
		src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
	<!-- Metis Menu Plugin JavaScript -->
	<script
		src="${ctx}/static/bower_components/metisMenu/dist/metisMenu.min.js"></script>
	<!-- Custom Theme JavaScript -->
	<script src="${ctx}/static/dist/js/sb-admin-2.js"></script>
	<script src="${ctx}/static/js/commons/welcome.js"></script>
</body>
</html>
