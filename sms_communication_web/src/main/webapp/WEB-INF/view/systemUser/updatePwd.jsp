<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>

    <title>找回密码</title>

    <link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/admin.css">
    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
    <script src="${ctx}/static/js/app/html5shiv.js"></script>
    <script src="${ctx}/static/js/app/selectivizr.js"></script>
    <style>
        input[type="text"] {
            position: relative;
            top: 4px;
        }

        #captchaImage {
            position: relative;
            top: 2px;
        }
    </style>
    <![endif]-->
    <style>
        .dxtype {
            position: static;
            border: none;
            width: 100%;
        }
        .f-btn{
	height: 22px!important;line-height: 20px!important;width: 62px;border:1px solid #4284DA!important;
	font-size:12px;background:##4284DA!important;
}
    </style>

<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<script src="${ctx}/static/js/commons/common.js"></script>


</head>

<body>

<input type="hidden" value="${ctx}" id="server_path" />
<input type="hidden" id="update_pwd_id"
			value="<c:if test="${not empty sessionScope.session_user}">${sessionScope.session_user.id}</c:if>" />
<div data-dxtype="zx">
        <div class="dxtype zx">

            <div class="tzcontent">
                <form id="zxForm">
                    <table class="helpbox">
                   
                   		<tr>
                            <td style="text-align: right;"><span class="cfd">*</span>原密码：</td>
                            <td style="text-align: left;">
                                <input id="oldPwd" readonly onfocus="this.removeAttribute('readonly');" type='password' class='inp-inp imp' placeholder="" maxlength="12"
                                       />

                            </td>
                        </tr>
						<tr>
                            <td style="text-align: right;"><span class="cfd">*</span>新密码：</td>
                            <td style="text-align: left;">
                                <input type="password" id="newPwd" readonly onfocus="this.removeAttribute('readonly');" type='password' class='inp-inp imp' placeholder="" maxlength="12"
                                       />

                            </td>
                        </tr>
                        <tr>
                            <td style="text-align: right;"><span class="cfd">*</span>确认密码：</td>
                            <td style="text-align: left;">
                                <input id="newPwd2" readonly onfocus="this.removeAttribute('readonly');" onblur="return checkPass();" type='password' class='inp-inp imp' maxlength="12"
                                       />

                            </td>
                        </tr>
                        
                    </table>
                    <p style="padding-top: 10px;">
					  <input type="button" class="f-btn" id="update_pwd_btn"  value="确 定">

                    <input type="reset" class="f-btn " id="upfilecenter"  value="取 消">
                      
                    </p>
                </form>
            </div>
        </div>
    </div>


		
<script src="${ctx}/static/js/systemUser/updatePwd.js"></script>

</body>
</html>