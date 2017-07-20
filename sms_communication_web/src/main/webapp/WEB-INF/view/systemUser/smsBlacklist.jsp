<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fileinput.css">
<link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
<script src="${ctx}/static/js/jquery.datetimepicker.js"></script>

</head>
<body>
	<div>	
		<div class="com-title">
			<h4>黑名单管理</h4>
		</div>
		<hr/>
		<!-- id="upload_form"  -->
		<div>
				
		  <%--  <form method="post"
				class="form-horizontal required-validate"
				action="${ctx}/smsBlacklist/from/merge.do?menuId=${menuId}"
				enctype="application/x-www-form-urlencoded" >
				<input type="file" name="file" id="file"/>
				<input type="submit"value="提交" id="button"/>(导入黑名单号码使用英文",")
			</form> --%>
			<form id="form1" name="form1" method="post" action="${ctx}/smsBlacklist/from/merge.do?menuId=${menuId}" enctype="multipart/form-data">
				 <div class="inp-box">
                  	<span  class="inp-title">上传文件：</span>
							<input type="button" class="btn btn-info" value="上传" style="width: 64px;margin-left: 10px" onclick="$('#file-btn').click()">
							<input name="file" type="file"  style="display: none;" id="file-btn" />
							<input style="width:64px;margin-left: 10px" type="reset" name="reset" value="重置" class="btn btn-warning" />
							<input style="width: 64px;margin-left: 10px" type="submit" class="btn btn-info" value="提交" />
						
				</div>
			</form>
		</div>
		<div class="com-content">
			<div class="com-menu inp-menu"> 
			<!-- 	<a href="#" id="add_sysuser">
					<button id="add_button" type="button" class="btn btn-info">新增</button>
				</a>  --> <div class="inp-box">
                  	<span  class="inp-title">手机号码:</span>
						<input class="btn_input" id="user_input" type="text" placeholder="请输入手机号码">
							<span style="margin-left: 10px"  class="inp-title">加入时间:</span>
							 <input id="start_input" type="text" placeholder="年/月/日" />
			                               <span class="dataline">—</span><input id="end_input" type="text" placeholder="年/月/日"/>
			                
			            <button class="btn btn-info" type="button" id="search_btn">
						<span class="glyphicon glyphicon-search"></span></button>  
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


	<div class="modal fade" id="add_sysuser_div">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="h4_title">新增黑名单</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<input type="hidden" id="id" />
						<div class="form-group">
							<label for="phone" class="col-sm-2 control-label">黑名单手机号</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="phone">
							</div>
						</div>
						<div class="form-group" id="aisleId_div">
							<label for="aisleId" class="col-sm-2 control-label">通道id</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="aisleId">
							</div>
						</div>
						<div class="form-group">
							<label for="aisleName" class="col-sm-2 control-label">通道名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="aisleName">
							</div>
						</div>
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


     <script src="${ctx}/static/js/bootstrap-table.js"></script>
     <script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
     <script src="${ctx}/static/js/fileinput.js"></script>
	<script  src="${ctx}/static/js/systemUser/smsBlacklist.js"></script>
</body>
</html>