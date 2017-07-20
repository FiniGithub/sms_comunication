<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,user-scalable=no">
		<title>代理平台-API配置</title>
	</head>


	<body>
		<input id="path" type="hidden" value="${ctx}">
		
		<!--内容-->
		<div class="content-box ">
			<div class="container head-box pd0 flex-c indextilt">
				<i class="inborder">&nbsp;</i><span class="intilte">API配置</span><img class="index-logo" src="${ctx}/static/img/send/peizhi.png" />
			</div>
			<div class="com-table-box api-content-box">
				<div class="container head-box pd0 com-table  text-left">
					<ul class="api-content">
						<li>账号企业ID：<span class="qyid">${session_sms_user.id}</span></li>
						<li>KEY：
							<span id="span_key">${key}</span>
							<a class="cl29 alwd-inp alwd-tc1" data-inp="hqkey">获取</a>
							<a class="cl29 alwd-inp alwd-tc1" data-inp="czkey">重置</a>
						</li>
						<li class="flex-js api-title">推送地址 
							<input id="upd_btn" class="fileli-btn jkwd-btn-ck alwd-inp alwd-tc1" data-inp="tsdz" type="button" />
						</li>
						<li>上行推送地址：<span class="sxdz">${replyUrl}</span></li>
						<li>报告推送地址：<span class="xzdz">${reportUrl}</span></li>
						<li class="api-title">SDK下载</li>
						<ul class="sdk-btn-box">
							<li id="java_sdk" class="btn flex-c sdkxz-btn">
								<p><span class="cl29">JAVA版</span><br />点击下载</p>
							</li>
							<!-- <li class="btn flex-c sdkxz-btn">
								<p><span class="cl29">NET版</span><br />点击下载</p>
							</li>
							<li class="btn flex-c sdkxz-btn">
								<p><span class="cl29">PHP版</span><br />点击下载</p>
							</li>
							<li class="btn flex-c sdkxz-btn">
								<p><span class="cl29">Python版</span><br />点击下载</p>
							</li>
							<li class="btn flex-c sdkxz-btn">
								<p><span class="cl29">Metadata版</span><br />点击下载</p>
							</li>
							<li class="btn flex-c sdkxz-btn">
								<p><span class="cl29">C/C++版</span><br />点击下载</p>
							</li>
							<li class="btn flex-c sdkxz-btn">
								<p><span class="cl29">NodeJS版</span><br />点击下载</p>
							</li> -->

						</ul>
						<li class="flex-js api-title">接口文档
							 <input id="online_read" class="fileli-btn jkwd-btn-ck" type="button" value="在线查看" /> 
							 <input id="down_wd" class="fileli-btn jkwd-btn-xz" type="button" value="下载" />
						</li>
					</ul>
				</div>
			</div>

		</div>
		<!--KEY弹窗-->
		
		<!-- 重置 -->
		<div class="alwindow-box czkey">
			<div class="alwd-content-box">
				<div class="alwd-bg alwd-t"></div>
				<i class="btn alwd-close"><img src="${ctx}/static/img/send/tccha.png"/></i>
				<div class=" flex-c alwd-bg alwd-b">
					<a id="getkey_next" class="btn s-btn back-btn mg0 pd0  flex-c">下一步</a>
				</div>
				<div class="flex-c alwd-content alwd-drhm">
					<div class="elastic-box-content">
						<ul class="flex-c key-ul">
							<li class="key-on">第一步：手机验证</li>
							<li>第二步：获取KEY</li>
						</ul>
						<p>
							接收验证码手机号：<span class="session_phone">${session_sms_user.phone}</span>
						</p>
						<p>
							验证码
						</p>
						<div class="flex-js">
							<input id="reset_code" type='text'  class='' maxlength="6" placeholder="请输入验证码" /> 
							<input id="getcode_reset" class="fileli-btn" type='button' class='btn' value='获取验证码' style="width: 80px;" />
						</div>
						<div class="flex-js">
							<input id="reset_checkbox" type="checkbox" class="czcheck" style="margin-right:5px;"/>
							重置后原KEY失效
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 获取key -->
		<div class="alwindow-box hqkey">
			<div class="alwd-content-box">
				<div class="alwd-bg alwd-t"></div>
				<i class="btn alwd-close"><img src="${ctx}/static/img/send/tccha.png"/></i>
				<div class=" flex-c alwd-bg alwd-b">
					<a id="gion_next" class="btn s-btn back-btn mg0 pd0  flex-c  "  >下一步</a>
				</div>
				<div class="flex-c alwd-content alwd-drhm">
					<div class="elastic-box-content">
						<ul class="flex-c key-ul">
							<li class="key-on">第一步：手机验证</li>
							<li>第二步：获取KEY</li>
						</ul>
						<p>
							接收验证码手机号：<span>${session_sms_user.phone}</span>
						</p>
						<p>
							验证码
						</p>
						<div class="flex-js key-inp-box">
							<input id="getkey-code" type='text'  class='' maxlength="6" placeholder="请输入验证码"/> 
							<input id="gaincode_next" class="fileli-btn" type='button' class='btn' value='获取验证码' style="width: 80px;" />
						</div>
						
					</div>
				</div>
			</div>
		</div>
		<div class="alwindow-box hqkeytwo">
			<div class="alwd-content-box">
				<div class="alwd-bg alwd-t"></div>
				<i class="btn alwd-close"><img src="${ctx}/static/img/send/tccha.png"/></i>
				<div class=" flex-c alwd-bg alwd-b">
					<a class="btn s-btn back-btn mg0 pd0  flex-c alwd-btn-close">确认</a>
				</div>
				<div class="flex-c alwd-content alwd-drhm">
					<div class="elastic-box-content hqkeytwo-content-box">
						<ul class="flex-c key-ul">
							<li >第一步：手机验证</li>
							<li class="key-on">第二步：获取KEY</li>
						</ul>
						
						<p>&nbsp;
							KEY
						</p>
						<div class="flex-c keyhq-inpt-box">
							<input id="input-key"  type='text'  class='key-text' /> 
							<input id="copy_btn" class="fileli-btn " type='button' class='btn' value='复制'  style="margin-left: 4px" />
						</div>
						<!-- <div >
							<span>重置后原KEY将失效KEY已生成，本页面关闭后，平台将不再储存和显示KEY，请妥善保存。</span>
						</div> -->
					</div>
				</div>
			</div>
		</div>
		
		
		
		
		
		
		
		<!--推送地址弹窗-->
		<div class="alwindow-box tsdz">
			<div class="alwd-content-box">
				<div class="alwd-bg alwd-t"></div>
				<i class="btn alwd-close"><img src="${ctx}/static/img/send/tccha.png"/></i>
				
				<div class=" flex-c alwd-bg alwd-b">
					<a id="commit_apiurl" class="btn s-btn back-btn mg0 pd0  flex-c" >提交</a>
				</div>
				
				<form id="cz_merge_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/puser/from/moneyMerge.do?menuId=${menuId}" enctype="application/x-www-form-urlencoded">
					<div class="flex-c alwd-content alwd-drhm">
						<div class="elastic-box-content">					
							<p>
								推送上行地址
							</p>
							<div class="flex-js key-inp-box">							
								<input id="replyUrl" name="replyUrl"   type='text'  class='sxdztxt'  placeholder="请输入推送上行地址"  /> 
							</div>					
							<p>
								推送报告地址
							</p>
							<div class="flex-js key-inp-box">							
								<input id="reportUrl" name="reportUrl"  type='text'  class='xsdztxt' placeholder="请输入推送报告地址" /> 
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<div class="regiger-bg" style="display: none;">
			
		</div>
	<script src="${ctx}/static/js/app/send/apiwd.js"></script>
	</body>
	
	
	