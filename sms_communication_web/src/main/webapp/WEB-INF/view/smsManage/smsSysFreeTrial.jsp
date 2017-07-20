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
		<h4>通用免审报备</h4>
		<div class="title-remark">查看通用免审模板相关的信息，
			并查询通用免审模板等操作。
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu inp-menu">
			<input type="hidden" id="menu_id" value="${menuId}" />
				   <div  class="inp-box">
           	 
              
					 <a href="#" id="add_data">
						<button id="add_button" type="button" class="btn btn-info">新增</button>
					</a>
				</div>
              <div  class="inp-box">
           	 
                 <span class="inp-title">审核状态：</span>
                	
                <select name="freeTrialState" id="i_freeTrialState" style="width:180px;">
                    <option value="">请选择</option>
                    <option value="0" >启用</option>
                    <option value="1">停用</option>
                </select>
                  <button class="btn btn-info" type="button" id="search_btn" style="margin-left:20px;">
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
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="h4_title">免审模板管理</h4>
			</div>
			<div class="modal-body">
				<form id="upload_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/userFreeTrial/from/sysAddFreeTrial.do?urltypes=0&menuId=${menuId}" enctype="application/x-www-form-urlencoded">
					<input type="hidden" class="form-control" id="i_id" name="id">

					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">名称</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" id="i_name" name="name">
						</div>
					</div>
					
					<div class="form-group">
						<label for="apk_description" class="col-sm-3 control-label">类型</label>
						<div class="col-sm-5">
							<select class="province select" id="i_aisleType" name="aisleType">
								<option value="">请选择</option>
							 	<c:forEach var="item" items="${typeList}" varStatus="status">   
		 							<option key="${item.id}" value="${item.id}" >${item.name}</option>
								</c:forEach>
							</select>
						</div>
					</div>

				  
					<div class="form-group">
						<label for="apk_version" class="col-sm-3 control-label">内容</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" id="i_awardMoney" name="awardMoney"/>
							<input type="hidden" class="form-control" id="i_awardMoney_val" name="awardMoneyVal"/>
							<span style="color:#FF0000;">注：不包含签名,可以用@符号代替2到8个中文或数字</span>
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
					<span aria-hidden="true">&times;</span>
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

<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/smsManage/smsSysFreeTrial.js"></script>
</body>
</html>