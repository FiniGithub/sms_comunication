<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="${ctx}/static/js/systemUser/smsPhoneShielding.js"></script>
</head>
<body>


	<!-- <div style="padding-top:300px;padding-left: 300px;width: 1000px;height: 800px;">

	<div style="width:100px;">屏蔽段号&nbsp;</div><textarea id="word" rows="8" cols="50" ></textarea>
	
	<div style="text-align: right;width:300px;height:40px;">多个号段用分隔符“,”或换行
	</div>
	<div style="width:300px;"><input id="submitData" type="button" value="提交" style="height:30px;width:200px;margin-left: 70px;"/></div>

</div> -->

	<div>
		<div class="com-title">
			<h4>屏蔽号段管理</h4>
			<div class="title-remark">查看屏屏号段相关的信息， 可以添加屏蔽号段， 并对屏蔽号段进行编辑等操作。</div>
			<hr/>
		</div>
		
		<div class="com-content">
			<div class="com-menu inp-menu">

				<div id="data_div" ></div>
			</div>

			<div class="row">
				<div id="tb_data"
					style="text-align: center; padding-left: 300px; width: 800px; height: 800px;">
					<form id="upload_form" method="post"
						class="form-horizontal required-validate"
						action="${ctx}/phoneShielding/smsPhoneShieldinglist.do?menuId=${menuId}"
						enctype="application/x-www-form-urlencoded">
						<div>
							屏蔽号段：<input type="text" id="i_phone" name="phone" height="200px"
								style="width: 300px; height: 200px;" /> <input type="hidden"
								id="i_id" name="id" />
						</div>
						<div>
							<input type="submit" value="提交" id="button" />
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>