<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">

	<head>

		<title>全网信通</title>

		<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/admin.css">
		<!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
    <script src="${ctx}/static/js/app/html5shiv.js"></script>
    <script src="${ctx}/static/js/app/selectivizr.js"></script>
    <style>
        

        #captchaImage {
            position: relative;
            top: 2px;
        }
    </style>
    <![endif]-->
    <style type="text/css">
    	input[type="text"]:focus {
    border: 1px solid #4284DA;
    height: 20px;
    width: 161px;
    }
    .inp-inp {
   border: 1px solid #d4d4d4;
    height: 20px;
    width: 161px;
	}
    </style>
	</head>

	<body style="background-color: #FFFFFF;font-family:'微软雅黑，宋体'">
		<input type="hidden" value="${ctx}" id="server_path" />
		<input type="hidden" value="${ctx}" id="path" />
		<div class="pageheader">
			<div class="pageh-left">
				<div class="logo"></div>
				<div class="logotext">全网信通——即时通信平台：验证码短信，营销群发短信，短信通知，短信接口</div>
			</div>
			<div class="pageh-right">
				<ul class="pageh-menu">
					<li>
						<a href="logout.do">返回首页</a>
					</li>
					<li class="pagechange">
						<a href="#consult" id="consult" data-tar="consult">咨询与反馈</a>
					</li >
					<li class="pagechange">
						<a href="#forgetpsw" id="forgetpsw" data-tar="forgetpsw">找回密码</a>
					</li>
					<li class="pagechange" style="border:none">
						<a href="#notice" id="notice" data-tar="notice">用户须知</a>
					</li>
				</ul>
			</div>
		</div>
		<p style="border-width: 1px; border-color: #d4d4d4;hieght:1px;width:100%;border-top:1px solid #d4d4d4;margin-top: 5px" ></p>
		<div class="dxtype consult">
			<div class="dxtitle">咨询与反馈</div>
			<div class="tzcontent">
				<form id="zxForm1">
					<table class="helpbox">
						<tr>
							<td style="text-align: right;">账号名称：</td>
							<td style="text-align: left;">
								<input class="inp-inp" type="text" name="email" id="email" placeholder="" />无账号可不填写
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
								<input style="text-align: left;" class="inp-inp imp" onkeyup="value=value.replace(/[^\d]/g,'')" type="text" name="phone" id="phone" placeholder="" />
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
								<img id="captchaImage" src="captcha.do" style="vertical-align:top;height:22px;" title="点击图片刷新验证码">
							</td>
						</tr>
					</table>
					<p style="padding-top: 10px;padding-bottom: 20px;">

						<input type="button" style="margin-right: 20px;"  class="f-btn" id="subBtn" value="确  定"/>
						<input type="reset"   class="f-btn"  value="重  置"/>
					</p>
				</form>
			</div>
		</div>
		<!-- 找回密码 -->
		<div class="dxtype forgetpsw">
			<div class="dxtitle">找回密码</div>
			<div class="tzcontent">
				<form id="zxForm">
					<table class="helpbox">
						<tr>
							<td style="text-align: right;"><span class="cfd">*</span>账号名称：</td>
							<td style="text-align: left;">
								<input style="text-align: left;" class="inp-inp pimp" type="text" name="emailc" id="emailc" readonly onfocus="this.removeAttribute('readonly');" placeholder="" />
							</td>
						</tr>
						<tr>
							<td style="text-align: right;"><span class="cfd">*</span>手机号码：</td>
							<td style="text-align: left;">
								<input maxlength="11" style="text-align: left;" class="inp-inp pimp" type="text" name="phonec" id="phonec" onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="" />
							</td>
						</tr>
						<tr>
							<td style="text-align: right;"><span class="cfd">*</span>图文验证码：</td>
							<td style="text-align: left;">
								<input maxlength="5" class="inp-inp pimp" type="text" name="imgCode" id="imgCodec" readonly onfocus="this.removeAttribute('readonly');" placeholder="" />
								<img id="captchaImage2" src="captcha.do" style="vertical-align:top;height:22px;" title="点击图片刷新验证码">
							</td>
						</tr>
						<tr>
							<td style="text-align: right;"><span class="cfd">*</span>手机验证码：</td>
							<td style="text-align: left;">
								<input maxlength="8" class="inp-inp pimp reg-code" type="text" name="verifyCodec" id="verifyCodec" readonly onfocus="this.removeAttribute('readonly');" placeholder="" />
								<input id="pwd-getcodec" class="f-btn" type="button" value="获取验证码" />
							</td>
						</tr>
					</table>
					<p style="padding-top: 10px;padding-bottom: 20px;">

					<input type="button" style="margin-right: 20px;width:auto;"   class="f-btn"  id="updatePwdEnter" value="获取手机动态密码"/>
						<input type="reset"   class="f-btn"  value="重  置"/>
						
					</p>
					<div >
						<ul style="padding:0;text-align:left;">
							<li>提示：</li>
							<li>1、须准确输入账户名，勿含有空格或其他文字、符号；</li>
							<li>2、手机号码须为账户绑定号码，否则无法获取验证码短信；</li>
							<li>3、正确输入验证码，点击“确定”按钮后将会收到系统随机重置的</li>
							<li style="text-indent: 19px;">账户密码短信，请登录账户及时修改密码。</li>
						</ul>						
					</div>
				</form>
			</div>
		</div>

		<!-- 用户须知 -->
		<div class="dxtype notice">
			<div class="dxtitle">用户须知</div>
			<div class="tzcontent">
				<div align="left" style="margin-top:20px;;padding:10px;">

					<strong style="line-height:normal;">			
用户使用全网信通平台视为同意并遵守以下条款：
</strong>
					<pre style="font-size:12px;">
一、根据《互联网信息服务管理办法》不准利用本平台制作、复制、发布、传播含有下列内容的信息：
　　（一）反对宪法所确定的基本原则的；
　　（二）危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；
　　（三）损坏国家荣誉和利益的；
　　（四）煽动民族仇恨、民族歧视，破坏民族团结的；
　　（五）破坏国家民族宗教政策，宣扬邪教和封建迷信的；
　　（六）散布谣言，扰乱社会秩序，破坏社会稳定的；
　　（七）散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；
　　（八）侮辱或者诽谤他人，侵害他人合法权益的；
　　（九）含有法律、行政法规禁止的其他内容的。

二、根据《中华人民共和国广告法》第七条规定，信息发布者不得制作、复制、发布、传播含有下列内容的信息：
　　（一）使用中华人民共和国国旗、国徽、国歌；
　　（二）使用国家机关和国家机关工作人员的名义；
　　（三）使用国家级、最高级、最佳等用语；
　　（四）妨碍社会安定和危害人身、财产安全，损害社会公共利益；
　　（五）妨碍社会公共秩序和违背社会良好风尚； 
　　（六）含有淫秽、迷信、恐怖、暴力、丑恶的内容；
　　（七）含有民族、种族、宗教、性别歧视的内容；
　　（八）妨碍环境和自然资源保护；
　　（九）法律、行政法规规定禁止的其他情形。

三、严禁发送下列类型的信息。
　　1、不良SP、app信息，如诱骗定制等 ；
　　2、所有与政治相关的信息（政府部门除外）；
　　3、反对宪法所确定的基本原则的； 
　　4、危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；
　　5、损坏国家荣誉和利益的；
　　6、煽动民族仇恨、民族歧视，破坏民族团结的；
　　7、破坏国家民族宗教政策，宣扬邪教和封建迷信的；
　　8、散布谣言，扰乱社会秩序，破坏社会稳定的；
　　9、散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；
　　10、侮辱或者诽谤他人，侵害他人合法权益的；
　　11、垃圾信息；
　　12、有悖公序良俗的；
　　13、信息表面文字合法，但所涉及的企业、网站、产品、服务等违反国家法律法规的；
　　14、含有法律、行政法规禁止的其他内容的。

四、违法、违规、不良信息的举列：
　　1、各种类型的诈骗信息，即使文字表面语义合法；
　　2、字面内容中包含各类低俗、谩骂、污辱、恐吓类的信息；
　　3、发布所谓的中奖信息、特码等；
　　4、涉及非法药品、保健品类推广；
　　5、性保健品、成人用品、色情服务等；
　　6、各类非法贷款，信用卡套现等；
　　7、非法开据发票，出售假发票，假票据等；
　　8、散布、出售考试考卷或答案的；
　　9、招聘男女公关等不良招聘信息；
　　10、赌球、六合彩；
　　11、倒卖二手车、假文凭、武器、针孔摄像头、安装卫星天线等；
　　12、宣传倒卖各类个人信息、客户资料等；
　　13、涉及非法培训等；
　　14、涉及非法传销等。
	
五、根据《中华人民共和国广告法》第十四条的有关规定：在宣传药品、医疗器械中不得有下列内容：
　　（一）含有不科学的表示功效的断言或者保证的；
　　（二）说明治愈率或者有效率的；
　　（三）与其他药品、医疗器械的功效和安全性比较的；
　　（四）利用医药科研单位、学术机构、医疗机构或者专家、医生、患者的名义和形象作证明的；
　　（五）法律、行政法规规定禁止的其他内容。

六、根据《中华人民共和国广告法》第十六条的有关规定：
　　麻醉药品、精神药品、毒性药品、放射性药品等特殊药品，不得做广告。

七、根据《中华人民共和国广告法》第十七条的有关规定：
　　农药信息发布不得有下列内容：
　　（一）使用无毒、无害等表明安全性的绝对化断言的；
　　（二）含有不科学的表示功效的断言或者保证的；
　　（三）含有违反农药安全使用规程的文字、语言或者画面的；
　　（四）法律、行政法规规定禁止的其他内容。

八、根据《中华人民共和国广告法》第十八条的有关规定：
　　信息提供者禁止发布烟草广告。

九、根据《中华人民共和国广告法》第十九条的有关规定：
　　食品、酒类、化妆品信息的内容必须符合卫生许可的事项，并不得使用医疗用语或者易与药品混淆的用语。

十、本平台使用相关管理办法：
　　1、如用户发送违反上述规定的信息，将封停账户，冻结账户中余额。还将根据情况的轻重追究法律责任和要求赔偿损失。
　　2、平台用户应严格针对本企业应用的相关业务，明确客户群和客户范围。向会员客户发送信息，应当建立信息退订制度，
　　　　当客户不愿继续收到企业所发布的信息时，须将该用户手机号码从号码库里删除。
　　3、严禁向通过买卖等非法途径获得的客户资料发送信息；
　　4、使用行业应用通道的账户严禁发送营销广告类信息,否则将封停账户，冻结账户中余额。

十一、重要声明
　　1、合法使用：
　　　　严禁使用我公司平台发送违法信息以及任何与违法活动相关的信息。
　　2、有限责任：
　　　　我公司仅对平台产品本身负责，即我公司仅以用户支付的费用为最大责任范围。就用户的利润损失、业务损失、合同
　　　　损失、预期收入、预期节约的减少、费用或开支的增加或任何间接的、继发的或特殊的损失或损害，我公司不承担任
　　　　何责任。用户对发送信息的行为承担完全责任。
　　3、平台账户：
　　　　账户名称和密码为识别用户的最终依据，请用户务必妥善保管账户名和密码，由于账户名遗失、被他人盗用等造成的
　　　　损失由用户自行承担。
　　4、通道管理：
　　　　我公司有权根据运营商的政策变化，对于通道的价格和使用做出调整。
　　5、账户余额：
　　　　用户连续6个月未付费充值的，需在顺延的12个月内将账户短信余额使用完毕。连续12个月未登录使用账户的，视为
　　　　账户失效，将被注销。
			</pre>
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

		<script type="text/javascript">
			$('#captchaImage2,#captchaImage').click(function() {
				$(this).attr("src", "captcha.do?timestamp=" + (new Date()).valueOf());
			});
			
		</script>
	</body>

</html>