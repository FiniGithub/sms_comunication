<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>

<title>登录</title>

<link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/admin.css">
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
<script src="${ctx}/static/js/app/html5shiv.js"></script>
<script src="${ctx}/static/js/app/selectivizr.js"></script>
<style>
	
	
</style>
<![endif]-->
<style>
	#captcha:focus,#captcha{
	width:96px;
}
</style>
</head>
	<body>
		<input type="hidden" value="${ctx}" id="server_path" />
		<input type="hidden" value="${ctx}" id="path" />

		</head>

		<body class="new_index_bg">
			<input type="hidden" value="${ctx}" id="server_path" />
		<input type="hidden" value="${ctx}" id="path" />

		</head>

		<body class="new_index_bg">
			<table height="100%" cellspacing="0" cellpadding="0" width="100%" border="0" style="position:absolute;">
				<tbody>
					<tr>
						<td align="middle">
							<table cellspacing="0" cellpadding="0" width="468" border="0">
								<tbody>
									<tr>
										<td>

										</td>
									</tr>
									<tr>
										<td>

										</td>
									</tr>
								</tbody>
							</table>
							<table cellspacing="0" cellpadding="0" width="445" height="450" border="0">
								<tbody>
									<tr>
										<td width="16">

										</td>
										<td align="middle">
											<form id="loginUsers" name="loginUsers" action="http://sms.dzd.com/loginUsers.action" method="post">
												<table cellspacing="0" cellpadding="0" width="440" height="363" id="logo-table" style="background-position:2px 0;"border="0">

													<tbody>
														<tr height="40" align="center">

															<td>

															</td>
															<td style="vertical-align: middle;text-align: right;"><br>
																<a href="${ctx}/announcement.do#consult" target="_blank" class="awbtn" data-tar="zx" id="zxfk"><u>咨询与反馈 &gt;</u></a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
														</tr>
														<tr height="28" align="center">
															<td>
																&nbsp;
															</td>
															<td align="right">
																<br>
																<!--<font style="font-size: 13px">全网短信发送平台</font> 
											-->
															</td>

														</tr>
														<tr height="36" align="left">
															<td>

															</td>
															<td>
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;账户名
																<input type="text" id="account" name="account" style="BORDER-RIGHT: #000000 1px solid; BORDER-TOP: #000000 1px solid; BORDER-LEFT: #000000 1px solid; BORDER-BOTTOM: #000000 1px solid; width:165px;">
															</td>
														</tr>
														<tr height="36" align="left">
															<td>

															</td>
															<td>
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;&nbsp;码
																<input type="password" name="password" id="password" style="BORDER-RIGHT: #000000 1px solid; BORDER-TOP: #000000 1px solid; BORDER-LEFT: #000000 1px solid; BORDER-BOTTOM: #000000 1px solid; width:165px;">
																
															</td>
														</tr>
														<tr height="36" align="left">
															<td>

															</td>
															<td>
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;验证码
																<input type="text" id="captcha" name="captcha" size="11" maxlength="4" value="">

																<img id="captchaImage" src="captcha.do" style="vertical-align:top;height:24px;" title="点击图片刷新验证码">
															
																<a href="${ctx}/announcement.do#forgetpsw" target="_blank" class="awbtn psad" data-tar="mm"> <u>找回密码</u></a>
															</td>
														</tr>

														<tr height="36">
															<td colspan="2" align="left">
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																<input type="button" id="loginbtn" value="登录" style="height:25px; width:164px;"> &nbsp;&nbsp;&nbsp;
																<!--<input type="reset" value="&#37325;&#32622;" style="height:22px;width:70px"/>

												&nbsp;&nbsp;<a href="openApplyUsers">订户注册 </a>
											--></td>
														</tr>
														<tr height="36" align="left">
															<td>
															</td>
															<td style="padding-top:10px;">

																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																<input type="checkbox" id="cbLisence" checked="">
																<a class="aQues" href="${ctx}/announcement.do#notice" target="_blank" data-tar="xy">同意并遵守全网信通助理平台使用规定</a>
															</td>
														</tr>
														<tr>
															<td colspan="2">
																&nbsp;
															</td>
														</tr>

													</tbody>
												</table>
											</form>

										</td>
										<td width="16">

										</td>
									</tr>
								</tbody>
							</table>
							<table cellspacing="0" cellpadding="0" width="468" border="0">
								<tbody>
									<tr>
										<td>

										</td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>

			<!--  -->

			

	
			<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
	<script src="${ctx}/static/js/commons/bootstrap.min.js"></script>
	<script src="${ctx}/static/js/commons/common.js"></script>
	<script src="${ctx}/static/js/checkinput.js"></script>
	
	<script type="text/javascript">
	  	$('#captchaImage').click(function() {
                 $('#captchaImage').attr("src", "captcha.do?timestamp=" + (new Date()).valueOf());
        }); 
	  	
	</script>
	<script type="text/javascript" src="${ctx}/static/js/login/login.js"></script>
	 <script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>
		</body>

</html>