<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset='utf-8'>
	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
	<link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

	<link href="${ctx}/static/bower_components/bootstrap/dist/css/jquery.bootstrap.css" rel="stylesheet">
	  <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">  
</head>
<body>
	<div>
		
		<div class="com-content">
			<div class="com-menu inp-menu" style="text-align:center;">
			<div class="inp-box" >				
					<span >
						账号:
					</span>
						<input class="inp-box-inp" id="user_input" type="text" placeholder="">
				</div>
			<div class="inp-box">
				<a href="javaScript:void(0)" id="delete_selected">
					<input type="button" class="f-btn" value="批量删除" />
				</a>
				<a href="javaScript:void(0)" id="add_sysuser"  style="margin-left: 10px;">
				<input id="add_button" type="button" class="f-btn" value="新增" />	
							
				</a>
				<input id="search_btn"  type="button" class="f-btn" value="查询" />		
			</div>
				
				
			</div>
			<input type="hidden" id="menu_id" value="${menuId}" />
			<div class="row">
				<table id="tb_data"></table>
				<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>



			</div>
		</div>


	</div>

	<div class="modal fade" id="del">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true"></span>
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


	<div class="modal fade" id="reset">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">确定重置密码</h4>
				</div>
				<div class="modal-body">
					<p>确定重置密码?重置后密码初始化为123456</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取
						消</button>
					<button type="button" class="btn btn-primary" id="btn_reset">确
						定</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="add_sysuser_div">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="h4_title">新增系统用户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<input type="hidden" id="id" />
						<div class="form-group">
							<label for="email" class="col-sm-2 control-label">账号</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="email">
							</div>
						</div>
						<div class="form-group">
							<label for="nickName" class="col-sm-2 control-label">昵称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="nickName">
								<input type="hidden" class="form-control" id="i_sateType" value="${sateType}">
							</div>
						</div>
						<c:if test="${sateType==null}">
						<div class="form-group">
							<label for="nickName" class="col-sm-2 control-label">团队</label>
							<div class="col-sm-5">
								<select class="province select" id="i_superiorId" name="superiorId">
									<option value="">请选择</option>
									<c:forEach var="item" items="${sysUserList}" varStatus="status">   
	    								<option key="${item.id}" value="${item.id}" >${item.nickName}</option>
									</c:forEach> 
								</select>
							</div>
						</div>
						<div class="form-group" id="role_div">
							<label for="role_tab" class="col-sm-2 control-label">角色权限</label>
							<div class="col-sm-5">
								<table class="table table-striped" id="role_tab">
									<thead>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
						</c:if>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取
						消</button>
					<button type="button" class="btn btn-primary" id="save_sysuser_btn">确
						定</button>
				</div>
			</div>
		</div>
	</div>


	<input type="hidden" id="server_path" value="${ctx}" />
	<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
	<script src="${ctx}/static/js/commons/common.js"></script>	
	<script src="${ctx}/static/js/bootstrap-table.js"></script>
	<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
	<script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
	<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
	<script src="${ctx}/static/js/sysuser/sysuserlist.js"></script>
	<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>