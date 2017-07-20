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
			margin-top: 30px;
		}
	</style>
</head>
<body>

<div>
	<div class="com-title">
		<h4>基本信息</h4>
		<div class="title-remark">
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu">
			<input type="hidden" id="menu_id" value="${menuId}" />
				<div class="modal-body" style="width: 70%;">
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">客户名称:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" disabled="name" id="i_name" value="${smsUser.name}">
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">账户余额:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" disabled="disabled" id="i_money" value="${smsUser.money}">
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">授信额度:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" disabled="disabled" id="i_awardMoney" value="${smsUser.awardMoney}">
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">联系人:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" disabled="disabled" id="i_contact" value="${smsUser.contact}">
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">联系电话:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" disabled="disabled" id="i_phone" value="${smsUser.phone}">
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">登录账号:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" disabled="disabled" id="i_email" value="${smsUser.email}">
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">状态:</label>
						<div class="col-sm-5">
							 <c:if test="${smsUser.state==0}">	
								<input type="text" class="form-control" disabled="disabled" id="i_state" value="启用">
							</c:if>
							<c:if test="${smsUser.state==1}">	
								<input type="text" class="form-control" disabled="disabled" id="i_state" value="禁用">
							</c:if>
						</div>
					</div>
					</br>
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">签名:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" disabled="disabled" id="i_signature"value="${smsUser.signature}">
						</div>
					</div>
					</br>
			</div>
		</div>
	</div>
</div>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script type="text/javascript">
$(function () {
    $("#save_manage").bind("click", function () {
    	editData();
    });
});
function editData() {
	var i_reportUrl = $("#i_reportUrl").val();
	var i_replyUrl = $("#i_replyUrl").val();
	var i_id = $("#i_id").val();
	
        var param = {"reportUrl": i_reportUrl,"replyUrl": i_replyUrl,"id":i_id};
        ajaxCall({
            url: "/userMessage/from/userMerge.do",
            type: "get",
            data: param,
            success: function (data) {
            	if (checkRes(data)) {
                    alert("地址修改成功！");
                } else {
                	alert("操作失败");
                }
            },
            error: function () {
            	alert("操作失败");
            },
            complete: function () {
            }
        });
}
</script>
</body>
</html>