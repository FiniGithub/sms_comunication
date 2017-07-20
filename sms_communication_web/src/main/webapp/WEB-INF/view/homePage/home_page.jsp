<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>首页样式 - 编辑</title>
</head>
<body>
<input id="path" type="hidden" value="${ctx}" />
<script type="text/javascript" src="${ctx}/static/dzd/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${ctx}/static/js/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${ctx}/static/js/homePage/homePage.js"></script>
 <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/icon.css">
    <script type="text/javascript" src="${ctx}/static/dzd/jquery.easyui.min.js"></script>
<style>
	#btn_enter{
	margin-top:10px;
	border: none;width:62px;border-radius: 4px;background: #4284DA;color: #FFFFFF;height: 20px;line-height: 20px;padding: 0 8px;text-align: center;
	}

</style>

<textarea id="content"  name="content.fn"></textarea>
<script type="text/javascript">var myeditor=CKEDITOR.replace('content.fn');


</script>
<div style="text-align:center;">
	<button  id="btn_enter">确   定</button>
</div>

</body>
</html>
