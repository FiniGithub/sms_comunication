<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	
	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
	<link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

	<link href="${ctx}/static/bower_components/bootstrap/dist/css/jquery.bootstrap.css" rel="stylesheet">
	  <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">  
</head>
<body>
	<input type="hidden" id="server_path" value="${ctx}" />
	<div>
	
		<div class="com-content">
			<div class="com-menu inp-menu">
			<div class="inp-box">
				<a href="javaScript:void(0)" id="add_btn">
					<input class="f-btn" value="新增" type="button" style="margin-left: 10px" />				
				</a>
			</div>				
			</div>
			<div class="row">
				<table class="table table-hover table-striped" id="tb_data">
					<thead>
						<tr>
							<th>名称</th>
							<th>序号</th>
							<th>URL</th>
							<th>创建时间</th>
							<th>修改时间</th>
							<th class="text-center">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>

		<div id="paging" class="row">
			<ul class="pagination"></ul>
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


	<div class="modal fade bs-example-modal-lg" id="add_div">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true"></span>
					</button>
					<h4 class="modal-title">新增菜单</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<input type="hidden" id="id" /> <input type="hidden"
							id="parent_id" /> <input type="hidden" id="root_id"
							value="rootVal" />
						<div class="form-group">
							<label for="name" class="col-sm-2 control-label">名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="name">
							</div>
						</div>
						<div class="form-group">
							<label for="url" class="col-sm-2 control-label">URL</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="url">
							</div>
						</div>
						<div class="form-group">
							<label for="rank" class="col-sm-2 control-label">Rank</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="rank">
							</div>
						</div>
						<div class="form-group">
							<label for="actions" class="col-sm-2 control-label">Actions</label>
							<div class="col-sm-7">
								<input type="text" class="form-control" id="actions">
							</div>
							<label for="actions" class="col-sm-2 control-label">(用"|"分格)</label>
						</div>
						<div class="form-group" id="btn_div">
							<label for="btn_url" class="col-sm-2 control-label">按钮类型</label>
							<div class="col-sm-8">
								<input type="hidden" class="form-control" id="btn_url">
								<button id="add" type="button" class="btn btn-info">add</button>
								&nbsp;&nbsp;&nbsp;
								<button id="add_default" type="button" class="btn btn-info">addDefault</button>
								&nbsp;&nbsp;&nbsp;
								<button id="delete_all" type="button" class="btn btn-info">Delete
									All</button>
								<table class="table table-striped" id="tb_menu_btn">
									<thead>
										<tr>
											<th>按钮名称</th>
											<th>按钮类型</th>
											<th>注册Action</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="save_btn">确定</button>
				</div>
			</div>
		</div>
	</div>

	<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
	<script src="${ctx}/static/js/commons/common.js"></script>	
	<script src="${ctx}/static/js/bootstrap-table.js"></script>
	<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
	<script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
	<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
	<script src="${ctx}/static/js/sysmenu/paging.js"></script>
	<script src="${ctx}/static/js/sysmenu/sysmenulist.js"></script>
</body>
</html>