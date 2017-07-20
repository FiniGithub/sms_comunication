<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
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

<div>
	
	<div class="com-content">
		<div class="com-menu inp-menu">		
		<div class="inp-box" style="padding-left:6px;">
			<a href="#" id="add_data">
			<input class="f-btn" value="新增" type="button"  />				
			</a>
		</div>
			
			<div id="data_div" ></div>
		</div>
		<input type="hidden" id="menu_id" value="${menuId}"/>
		<div class="row">
			<table id="tb_data"></table>
			<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>



		</div>
	</div>
</div>

<div class="modal fade" id="add_data_div">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="h4_title">jar包信息管理</h4>
			</div>
			<div class="modal-body">
				<form id="upload_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/plugin/from/merge.do?menuId=${menuId}" enctype="multipart/form-data" >
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">名称</label>
						<div class="col-sm-5">
							<input type="hidden" id="id_input" name="id_name">
							<input type="text" class="form-control" id="apk_name_id" name="apk_name">
						</div>
					</div>

					<div class="form-group">
						<label for="apk_description" class="col-sm-3 control-label">描述</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" id="apk_description" name="apk_description_name">
						</div>
					</div>
					
					<div class="form-group" style="height: 230px;">
						<label for="file_upload" class="col-sm-3 control-label">文件</label>
						<div class="col-sm-8">
							<%--<img id="image_div" class="kv-preview-data file-preview-image" style="width:160px;height:160px;"/>--%>
							<input type="text" class="form-control" id="apk_upload_value" disabled="disabled">
							<input id="file_upload" type="file" name="image" class="file"/>
							<p class="help-block">jar文件</p>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal" id="cancel_btn">取 消</button>
						<button type="button" class="btn btn-primary" id="save_data_btn">确 定</button>
					</div>
				</form>
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
				<h4 class="modal-title" id="del_h4_title">确定推荐</h4>
			</div>
			<div class="modal-body">
				<p id="del_p_title">
					确认批量推荐?
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取
					消
				</button>
				<button type="button" class="btn btn-primary" id="btn_del">确
					定
				</button>
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
	<script src="${ctx}/static/js/plugin/pluginlist.js"></script>
	<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>