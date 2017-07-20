<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	String requestUri = request.getRequestURI();
	String contextPath = request.getContextPath();
	String url = requestUri.substring(contextPath.length() + 1);
%>
<c:set var="url_val" value="<%=url%>" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
<meta name="viewport"
	content="width=device-width,initial-scale=1,maximum-scale=1">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="blank">
<meta name="format-detection" content="telephone=no">
<title>代理平台</title>
<link rel="stylesheet"
	href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.css">
<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" 
	href="${ctx}/static/css/bootstrap-datetimepicker.min.css" />	
<link rel="stylesheet" href="${ctx}/static/css/app/style.css">
<link rel="stylesheet" href="${ctx}/static/css/app/backups.css">
<link rel="stylesheet" href="${ctx}/static/css/elastic-box.css">
    <style>
        #top_mag_label{
            color: #AAAAAA;
            font-size: 14px !important;
            font-weight: normal !important;
        }


    </style>


<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<script src="${ctx}/static/js/commons/common.js"></script>
<script src="${ctx}/static/js/commons/bootstrap.min.js"></script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/bootstrap-datetimepicker.min.js"
	type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/js/bootstrap-datetimepicker.zh-CN.js"
	type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/js/app/page.js"></script>
<script type="text/javascript">
	/* $(function(){
		$("#kefu_a").click(function(){
			window.open("http://wpa.qq.com/msgrd?v=3&uin=2674045640&site=qq&menu=yes");
		});
		
		$("#jishu_a").click(function(){
			window.open("http://wpa.qq.com/msgrd?v=3&uin=339813727&site=qq&menu=yes");
		});
	});
 */
</script>
<!--[if IE]>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/app/ie8.css"/>
	<script src="${ctx}/static/js/appml5shiv.js"></script>
	<script src="${ctx}/static/js/applectivizr.js"></script>
	<![endif]-->
<decorator:head />
</head>
<body>
	<input type="hidden" value="${ctx}" id="path" />
	<!--头部-->
	<div class="top-box">
		<div class="container head-box pd0 flex-c">
			<div class="h-logo">
				<a href="${ctx}/smsUser/index.do" class="logo"> </a>
			</div>
			<!--菜单-->
			<div class="flex-c head-btn-box">
				<div class="btn-group">
					<button type="button"
						class="btn btn-default  head-btn head-on">
						<span class="dxqft">短信管理</span>
					</button>
					<div class="d">
						<button style="display: none;" type="button"
							class="btn btn-default  head-btn head-cha head-onactive">
							<img src="${ctx}/static/img/send/cha.png" />
						</button>
						<ul class="dropdown-menu mg0 subbtn">
							<li><a href="${ctx}/smsUser/sendView.do"
								onclick="addtitle(this,'短信群发')" date-tit="dxqft">短信群发</a></li>
							<li><a href="${ctx}/smsUser/sendListview.do"
								onclick="addtitle(this,'发送任务')" date-tit="dxqft">发送任务</a></li>
							<li><a href="${ctx}/smsUser/pushSmsListview.do"
								onclick="addtitle(this,'短信回复')" date-tit="dxqft">短信回复</a></li>
							<li class="divider"><img
								src="${ctx}/static/img/send/menuline.png" /></li>
							<li class="flex-c fmenu">短信管理</li>
						</ul>
					</div>

				</div>
				<div class="btn-group  ">
					<button type="button" class="btn btn-default  head-btn head-on">
						<span class="cwtjt">财务统计</span>
					</button>
					<div class="d">
						<button style="display: none;" type="button"
							class="btn btn-default  head-btn head-cha head-onactive">
							<img src="${ctx}/static/img/send/cha.png" />
						</button>
						<ul class="dropdown-menu mg0 subbtn">
							<li><a href="${ctx}/smsUser/statisticalList.do"
								onclick="addtitle(this,'发送统计')" date-tit="cwtjt">发送统计</a></li>
							<li><a href="${ctx}/smsUser/puserBill.do"
								onclick="addtitle(this,'账单流水')" date-tit="cwtjt">账单流水</a></li>

							<li class="divider"><img
								src="${ctx}/static/img/send/menuline.png" /></li>
							<li class="flex-c fmenu">财务统计</li>
						</ul>
					</div>
				</div>
				<div class="btn-group  ">
					<button type="button" class="btn btn-default  head-btn head-on">
						<span class="xxbbt">信息报备</span>
					</button>
					<div class="d">
						<button style="display: none;" type="button"
							class="btn btn-default  head-btn head-cha head-onactive">
							<img src="${ctx}/static/img/send/cha.png" />
						</button>
						<ul class="dropdown-menu mg0 subbtn ">
							<li><a href="${ctx}/smsUser/signatureView.do"
								onclick="addtitle(this,'签名报免')" date-tit="xxbbt">签名报免</a></li>
							<li><a href="${ctx}/smsUser/freeTrialList.do"
								onclick="addtitle(this,'内容免审')" date-tit="xxbbt">内容免审</a></li>
							<li class="divider"><img
								src="${ctx}/static/img/send/menuline.png" /></li>
							<li class="flex-c fmenu">信息报备</li>
						</ul>
					</div>
				</div>
				<div class="btn-group apiWord">
					<button type="button" class="btn btn-default  head-btn  api-box ">
						<span class="apit">API配置&文档</span>
					</button>

				</div>

			</div>
			<!--用户-->
			<div class="flex-c user-box">
				<ul class="user-btn-box pd0 mg0">
					<li class="flex-c"><b class="uer-name-box">欢迎,<span
							class="uphone">${user.nickName}</span><img class="sanjiao"
							src="${ctx}/static/img/send/sanjiao.png" />
							<ul class="usermenu" style="display: none;">
								<li class="alwd-inp userInfo">账号信息<img class="usermenuline" src="${ctx}/static/img/send/shangla.png" /></li>
								<li class="alwd-inp updatePwd" data-inp="xgmm">修改密码</li>
								<li class="logout">退 &nbsp; 出</li>
							</ul>
					</b> <span class="btn pd0 yzbtn"> ${session_smsUser_alise.name} </span></li>
					<li>
                        <label id="top_mag_label">剩余： <sapn class="utime">${session_user_blank}</sapn> 条 </label>
						<span class="newsnum" style="cursor: pointer;">${session_message_count}</span>
					</li>
				</ul>
			</div>
		</div>
	</div>
	





	<!--内容-->
	<div>
		<decorator:body />
	</div>





	<!--底部-->
	<div class="regiger-bg" style="display: none;"></div>

	<div class="footer-box text-center">
		<div class="container head-box pd0 flex-c footer-cont">
			<span>ICP证号：<a href="http://www.miitbeian.gov.cn/"
				target="_blank" class="ftlink">粤ICP备09100815号-7</a></span>
		</div>
		<div class="ui-box">
			<ul class="ui-ul">
				<li class="kf">
					<a id="kefu_a" href="http://wpa.qq.com/msgrd?v=3&uin=2674045640&site=qq&menu=yes" target="_blank">
						<img src="../static/img/send/zaixiankefu.png" />
						<p>在线客服</p>
					</a>
				</li>
				<li class="js">
					<a id="jishu_a" href="http://wpa.qq.com/msgrd?v=3&uin=339813727&site=qq&menu=yes"  target="_blank">
						<img src="../static/img/send/jishuzhichi.png" />
						<p>技术支持</p>
					</a>
				</li>
			</ul>
		</div>
	</div>





	<!--密码修改弹窗-->
	<div class="alwindow-box  xgmm">
		<div class="alwd-content-box">
			<div class="alwd-bg alwd-t"></div>
			<i class="btn alwd-close"><img
				src="${ctx}/static/img/send/tccha.png" /></i>
			<div class=" flex-c alwd-bg alwd-b">
				<a id="updatePwd" class="btn s-btn back-btn mg0 pd0  flex-c">确认修改</a>
			</div>
			<div class="flex-c alwd-content alwd-drhm">
				<div class="elastic-box-content">

					<p>
						接收验证码手机号：<span id="sp-phone">${session_sms_user.phone}</span>
					</p>
					<p>验证码</p>
					<div class="flex-js key-inp-box mg10">
						<input id="input-code" type='text' class='key-inp-text'  placeholder="请输入验证码"/> <input
							id="input-getcode" class="fileli-btn" type='button'
							class='btn' value='获取验证码' style="width: 82px;" />
					</div>
					<p>密码修改</p>
					<div class="flex-js key-inp-box mg10">
						<input id="input-pwd" type='password' class='pwztxt'
							placeholder="请输入6~12位数字及英文组合密码" maxlength="12"
							onkeydown="onlyNumZ()" />
					</div>
					<p>密码确认</p>
					<div class="flex-js key-inp-box mg10">
						<input id="input-pwdenter" type='password' class='repwtxt'
							placeholder="请输入重新输入密码" maxlength="12" onkeydown="onlyNumZ()" />
					</div>

					<div id="check-msg" class="flex-js mg10"
						style="color: red; height: 10px;"></div>
				</div>
			</div>
		</div>
	</div>
	
	
	
	<!-- 个人中心弹框 -->
	 <div id="userInfo_div" class="alwindow-box alwd-cz" style="display: none;">
		<div class="alwd-content-box">
			<div class="alwd-bg alwd-t"></div>
			<i class="btn alwd-close"><img src="../static/img/send/tccha.png" /></i>
			<div class=" flex-c alwd-bg alwd-b"></div>
			<div style="margin-top: 130px; color: #333333;">
				<div style="margin-left:110px;">
					<div style="margin-bottom: 15px;">
						名称:<span style="margin-left:20px"> ${ user.nickName } </span>
					</div>
					<div style="margin-bottom: 15px;">
						账号:<span style="margin-left:20px"> ${ user.email } </span>
					</div>
					<div style="margin-bottom: 15px;">
						类型:<span style="margin-left:20px"> ${ session_smsUser_alise.name } </span>
					</div>
				</div>
				
				
				<div style="margin-left:80px;margin-top: 40px;">
					<p style="font-weight: bold;">购买价格</p>
					
					<ul style="margin-top: 20px;margin-left: 30px;">
						<li>
							<p class="elastic-box-smallTexr" style="color:#333333;margin-bottom: 4px;font-size:12px">
								<label id="lb_one_IntervalPrice"></label> 元/条 
								<label style="font-weight: normal;">
									购买数量范围：<span id="sp_oneIntervalStart"></span>
												~ <span id="sp_oneIntervalEnd"></span>条
								</label>
							</p>
						</li>
						<li>
							<p class="elastic-box-smallTexr" style="color:#333333;margin-bottom: 4px;font-size:12px">
								<label id="lb_two_IntervalPrice"></label> 元/条 
								<label style="font-weight: normal;">
									购买数量范围：<span id="sp_twoIntervalStart"></span>
												~ <span  id="sp_twoIntervalEnd"></span>条
								</label>
							</p>
						</li>
						<li>
							<p class="elastic-box-smallTexr" style="color:#333333;margin-bottom: 4px;font-size:12px">
								<label id="lb_three_IntervalPrice"></label> 元/条 
								<label style="font-weight: normal;">
									购买数量范围：<span  id="sp_threeIntervalStart"></span>
												~ <span  id="sp_threeIntervalEnd"></span>条
								</label>
							</p>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div> 
	<!--[if lte IE 8]><script src="${ctx}/static/js/app/ie8.js"></script>	<![endif]-->
</body>
</html>
