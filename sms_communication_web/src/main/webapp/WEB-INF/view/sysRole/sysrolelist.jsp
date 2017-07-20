<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
	<link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

	<link href="${ctx}/static/bower_components/bootstrap/dist/css/jquery.bootstrap.css" rel="stylesheet">
	  <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">  
	<style>
		a:link{
			text-decoration:none;
		}
		a:visited{
			text-decoration:none;
		}
		a:hover{
			text-decoration:none;
		}
		a:active{
			text-decoration:none;
		}
	</style>
</head>
<body>
	<div>
		
		<div class="com-content">
			<div class="com-menu inp-menu" style=""text-align:center;>
			
				<div class="inp-box" style="padding-left:6px;">
				<a href="javaScript:void(0)" id="add_btn">
					<input class="f-btn" value="新增" type="button"  />					
				</a>
				</div>
				
			</div>

			<input type="hidden" id="menu_id" value="${menuId}" />
			
		</div>
		<div class="row">
				<table id="tb_data"></table>
					<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>
			</div>
	</div>

	<div class="modal fade"  id="del">
    <div class="modal-dialog awl">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="del_h4_title">确定删除</h4>
            </div>
            <p class="aw-newcontent">
                <span class="aw-new">确认是否删除？</span>
            </p>

            <div class="modal-footer">
                <input type="button" class="f-btn" id="btn_del" value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">

            </div>
        </div>
    </div>
</div>


	<div class="modal fade" id="add_div">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true"></span>
					</button>
					<h4 class="modal-title">新增角色</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<input type="hidden" id="id" />
						<div class="form-group">
							<label for="name" class="col-sm-2 control-label">名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="name">
							</div>
						</div>
						<div class="form-group">
							<label for="descr" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="descr">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">菜单权限</label>
						</div>
						<div class="form-group">
							<div id="folder"></div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
				 <input type="button" class="f-btn" id="save_btn" value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">					
				</div>
			</div>
		</div>
	</div>
<input type="hidden" id="server_path" value="${ctx}" />
	<script src="${ctx}/static/js/commons/common.js"></script>
	<script
		src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
	<!-- Custom Theme JavaScript -->
	<%-- <script src="${ctx}/static/dist/js/sb-admin-2.js"></script> --%>
	<script src="${ctx}/static/js/commons/welcome.js"></script>
	<script src="${ctx}/static/js/commons/jquery.bootstrap.js"></script>
	<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
	<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
	<script src="${ctx}/static/js/sysrole/sysrolelist.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>