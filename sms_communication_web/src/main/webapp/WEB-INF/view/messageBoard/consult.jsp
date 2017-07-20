<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>

<title>咨询与反馈</title>

<link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/admin.css">
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
<script src="${ctx}/static/js/app/html5shiv.js"></script>
<script src="${ctx}/static/js/app/selectivizr.js"></script>
<style>
	input[type="text"]{
		position:relative;top:4px;
	}
	#captchaImage{
	position:relative;top:2px;
	}
</style>
<![endif]-->
<style>
	.dxtype{
		position:static;border:none;width:100%;
	}
</style>
</head>
<body>
	
		<input type="hidden" value="${ctx}" id="server_path" />
		<input type="hidden" value="${ctx}" id="path" />	
		<div>
			<div data-dxtype="zx" >			
										<div class="dxtype zx" >
	
											<div class="tzcontent">
											<form id="zxForm">
												<table class="helpbox">
													<tr>
														<td style="text-align: right;">账号名称：</td>
														<td style="text-align: left;">
															<input class="inp-inp" type="text" name="email" id="email" placeholder="" value="${session_user.email}"/>无账号可不填写
														</td>

													</tr>
													<tr>
														<td style="text-align: right;"><span class="cfd">*</span>填写内容：</td>
														<td style="text-align: left;padding-top: 0;">
															<br />
															<textarea id="content" name="content" class="newcontent imp" maxlength="300"></textarea>
															<p>
																当前字数：<span style="color: #FD0000;" class="dxlength">0</span>个，剩余字数：<span style="color: #FD0000;" class="s-dxlength">0</span>个
															</p>
														</td>
													</tr>
													<tr>
														<td style="text-align: right;"><span class="cfd">*</span>联系人：</td>
														<td style="text-align: left;">
															<input style="text-align: left;" class="inp-inp imp" type="text" name="contact" id="contact" placeholder="" />
														</td>
													</tr>

													<tr>

														<td style="text-align: right;"><span class="cfd">*</span>手机号码：</td>
														<td style="text-align: left;">
															<input style="text-align: left;" class="inp-inp imp" type="text" name="phone" id="phone" placeholder="" />
															<input id="pwd-getcode" class="f-btn" type="button" value="获取验证码" />
														</td>

													</tr>

													<tr>
														<td style="text-align: right;"><span class="cfd">*</span>手机验证码：</td>
														<td style="text-align: left;">
															<input class="inp-inp imp reg-code" type="text" name="verifyCode" id="verifyCode" placeholder="" />
														</td>
													</tr>

													<tr>
														<td style="text-align: right;"><span class="cfd">*</span>图文验证码：</td>
														<td style="text-align: left;">
															<input class="inp-inp imp" type="text" name="imgCode" id="imgCode" placeholder="" />
															<img id="captchaImage2" src="captcha.do" style="vertical-align:top;height:22px;" title="点击图片刷新验证码">
														</td>
													</tr>
												</table>
												<p style="padding-top: 10px;">
						
													<button type="button" style="margin-right: 20px;" class="btn btn-primary" id="subBtn">确
						定</button>
													<button type="reset" class="btn btn-default" data-dismiss="modal">重
						置</button>
												</p>
												</form>
											</div>
										</div>
									</div>
		</div>
		
			

		
	
			<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
	 <script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>
	
	<script type="text/javascript">
	  
	  	$('#captchaImage2').click(function() {
            $('#captchaImage2').attr("src", "captcha.do?timestamp=" + (new Date()).valueOf());
   }); 
	</script>
	
<script src="${ctx}/static/js/consult/consult.js"></script>
		</body>

</html>