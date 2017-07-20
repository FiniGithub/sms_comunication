<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
    <script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>  
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">   
    <script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>

</head>

<body>

	<div>
		<div class="com-title">
			<h4>运营商号段</h4>
			<hr/>
		</div>
		<span>移动号段：</span>
		<textarea id="mobile" name="mobileOperator" style="height:200px;width:1000px;font-size:15px;"></textarea>
		<br>
		<span>联通号段：</span>
		<textarea id="unicom" name="unicomOperator" style="height:200px;width:1000px;font-size:15px;" ></textarea>
		<br>
		<span>电信号段：</span>
		<textarea id="telecom" name="telecomOperator" style="height:200px;width:1000px;font-size:15px;" ></textarea>
		 
		<div class="modal-footer">
			<button type="button" class="btn btn-primary" id="btn_submit">确
				定</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">取
				消</button>
		</div>       		
	</div>

<script src="${ctx}/static/js/systemUser/operatorSectionNo.js"></script>

</body>
</html>