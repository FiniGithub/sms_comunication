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
		<h4>消息推送</h4>
		<div class="title-remark">
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu inp-menu">
			<div class="inp-box" id="addSmsUsers">
				
			</div>
			<input type="hidden" value="${ctx}" id="server_path" />
			<input type="hidden" id="menu_id" value="${menuId}" />
              <div class="inp-box inpt-box-btn">
                 <span  class="inp-title"> 创建时间：</span>
			                               <input id="start_input" type="text" placeholder="年/月/日" />
			                               <span class="dataline">—</span><input id="end_input" type="text" placeholder="年/月/日"/>
			                 <button class="btn btn-warning" id="reset" type="button">重置</button>
			          
			            <button class="btn btn-info" type="button" id="search_btn">
						<span class="glyphicon glyphicon-search"></span></button>               
          </div>
            
			<div id="data_div" ></div>
		</div>
		
		<div class="row">
			<table id="tb_data"></table>
		</div>
	</div>
</div>


<div class="modal fade" id="add_data_div">
	<div class="modal-dialog modal-lg" style="width:800px;">
		<div class="modal-content" >
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="h4_title">推送管理</h4>
			</div>
			<div class="modal-body">
				<form id="upload_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/puser/from/merge.do?menuId=${menuId}" enctype="application/x-www-form-urlencoded">
					<div class="form-group">
						<label for="apk_description" class="col-sm-3 control-label">推送类型</label>
						<div class="col-sm-5">
							<select class="province select" style="width: 150px;" id="i_type" name="types" onchange='btnChange(this[selectedIndex].value);'>
								<option value="0">全部推送</option>
								<option value="1">分类推送</option>
								<option value="2">单独推送</option>
							</select>
							<input name="pushIndex" type="checkbox" id="i_pushIndex" value="1" />推送至首页
						</div>
					</div>
					
					<div class="form-group" id="div_smsUserType">
						<label for="apk_name_id" class="col-sm-3 control-label">推送范围</label>
						<div class="col-sm-5">
							<c:forEach var="item" items="${typeList}" varStatus="status">   
	 							<input name="smsUserType" type="checkbox" class="i_pushIndex" value="${item.id}" />${item.name}
							</c:forEach>
						</div>
					</div>
					<div class="form-group" id="div_smsUserEmail">
						<label for="apk_name_id" class="col-sm-3 control-label">客户账号</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" id="i_smsUserEmail" name="smsUserEmail"/>
						</div>
					</div>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">消息标题</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" id="i_title" name="title"/>
						</div>
					</div>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">消息内容</label>
						<div class="col-sm-5">
							<textarea class="form-control" id="i_content" style="font-weight: normal;width:300px;"
							  name="content" rows="6">
							</textarea>
						</div>
					</div>
					
					<div class="modal-footer" style="text-align: center;">
						<button type="button" class="btn btn-primary" id="save_data_btn">推送</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/puser/pushManage.js"></script>
</body>
</html>