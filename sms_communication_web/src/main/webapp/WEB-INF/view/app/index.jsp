<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,user-scalable=no">
<title>代理平台-主页</title>
</head>

<body>

	<input type="hidden" value="${ctx}" id="path" />
	<!--内容-->
	<div class="content-box">
		<div class="container head-box pd0 flex-c indextilt">
			<i class="inborder">&nbsp;</i><span class="intilte">首页</span><img
				class="index-logo" src="../static/img/send/zhuye.png" />
		</div>
		<div class="container head-box pd0 index-box">
			<div class="col-xs-6 com-box pd0">
				<div class="com-tielt">当前可发短信条数</div>
				<div class="flex-c com-cont-box twot">
					<span class="kytime">${session_user_blank}</span><span class="tiao">条</span>
					<a href="#" class="btn czbtn alwd-tc2" data-inp="cztc">充&nbsp;值</a>
					<a href="${ctx}/smsUser/puserBill.do?typeId=0" class="cl94">充值记录</a>
				</div>
			</div>
			<div class="col-xs-6 com-box pd0">
				<div class="com-tielt">今日消费短信条数</div>
				<div class="flex-c com-cont-box b-l-n">
					<ul class=" comul ">
						<li><span class="yytime"></span>条</li>
					</ul>
				</div>
			</div>

			<div class="col-xs-12 com-box pd0">
				<div class="com-tielt">快捷入口</div>
				<div class="flex-c com-cont-box fastrouter">

					<div class="col-xs-3 thumbnail  fasttn b-ra send">
						<a class="btn" href="#"> <img class="fastimg"
							src="../static/img/send/qunfa.png" />
							<p>短信群发</p>
						</a>
					</div>
					<div class="col-xs-3 thumbnail fasttn b-ra sendDetail">
						<a class="btn" href="#"> <img class="fastimg"
							src="../static/img/send/renwu.png" />
							<p>发送任务</p>
						</a>
					</div>
					<div class="col-xs-3 thumbnail fasttn b-ra sendStatistical">
						<a class="btn" href="#"> <img class="fastimg"
							src="../static/img/send/tongji.png" />
							<p>发送统计</p>
						</a>
					</div>
					<div class="col-xs-3 thumbnail fasttn puserBill">
						<a class="btn" href="#"> <img class="fastimg"
							src="../static/img/send/liushui.png" />
							<p>流水记录</p>
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>








	<!--首次进入弹窗-->
	<div class="alwindow-box sctc">
		<div class="alwd-content-box">
			<div class="alwd-bg alwd-t"></div>

			<div class=" flex-c alwd-bg alwd-b">
				<a id="enter-type" class="btn  back-btn chabtn mg0 pd0  flex-c"
					data-inp="zccg"><input type="hidden" id="dxdtype" value="" />确认</a>
			</div>
			<div class="flex-c alwd-content alwd-drhm">
				<div class="elastic-box-content hqkeytwo-content-box">
					<p class="sctitle">
						请选择发送类型，
						<label style="color:red;font-weight: normal;">选定后不可更改</label> 
					</p>
					<input id="atypeId" value="2" type="hidden" />
					
					
					<ul class="flex-c key-ul sc-ul">
						<li data-dxtype="yzm">验证码
						
							<div style="position: relative;top: 4px;left: -33px;color: #333333;">范例:</div>
							<div class="dxtype yzm" style="display: none;margin-top: 10px;">
								<span class="nowday">11-10 9:35</span>
								<div class="tzcontent">
									<span>【人脉云本】你的验证码是：2586</span>
								</div>
							</div>
						</li>
						<li data-dxtype="xxtz" class="key-on">消息通知
							<div class="dxtype xxtz" style="margin-top: 10px;">
								<span class="nowday">11-10 9:35</span>
								<div class="tzcontent">
									<span>【亲亲宝贝】亲,你的包裹已寄出,请注意查收！</span>
								</div>
							</div>
						</li>
						<li data-dxtype="ggyx">广告营销
							<div class="dxtype ggyx" style="display: none; margin-top: 10px;">
								<span class="nowday">11-10 9:35</span>
								<div class="tzcontent">
									<span>【智融财富】利息最低2厘,快速办理5万贷款请致电18888888888</span>
								</div>
							</div>
						</li>
					</ul>

				</div>
			</div>
		</div>
	</div>


	<!--注册成功弹窗-->
	<div class="alwindow-box zccg">
		<div class="alwd-content-box">

			<div class="alwd-bg alwd-t"></div>
			<i class="btn alwd-close"><img src="../static/img/send/tccha.png" /></i>
			<div class=" flex-c alwd-bg alwd-b">
				<a href="${ctx}/smsUser/sendView.do"
					class="btn s-btn back-btn mg0 pd0 flex-c ">立即试发</a>
			</div>
			<div class="flex-c alwd-content alwd-drhm">
				<div class="elastic-box-content">
					<p class="elastic-box-text text-center">
						恭喜你注册成功<br /> 系统赠送10条试发短信
					</p>

				</div>
			</div>
		</div>
	</div>










	<!--充值弹窗-->
	<div class="alwindow-box alwd-cz cztc">
		<div class="alwd-content-box">
			<div class="alwd-bg alwd-t"></div>
			<i class="btn alwd-close"><img src="../static/img/send/tccha.png" /></i>
			<div class=" flex-c alwd-bg alwd-b"></div>
			<div style="margin-top:100px;">
				<div style="margin: 0px auto;width: 200px;color: #333333;">
					<span>购买详情请联系右侧客服</span>
				</div>
				<div style="margin-top: 40px;margin-left: 60px;color: #333333;">
					<p style="font-weight: bold;">购买价格</p>
					
					<div id="money-div-i" style="margin-left: 30px;margin-top: 10px;">
						<ul>
							<li>
								<span id="smsMoney-i">0.025元/条</span> 
								<span id="smsNum-i">购买数量范围：0~10000条</span>
							</li>
							<li>
								<span id="smsMoney-i">0.025元/条</span> 
								<span id="smsNum-i">购买数量范围：0~10000条</span>
							</li>
							<li>
								<span id="smsMoney-i">0.025元/条</span> 
								<span id="smsNum-i">购买数量范围：0~10000条</span>
							</li>
						</ul>
					</div>
				</div>
				
				<div style="margin-top: 40px;margin-left: 60px;color: #333333;">
					<p style="font-weight: bold;">对公账号</p>
					
					<div style="margin-left: 30px;margin-top: 10px;">
						<ul>
							<li>
								<span>开户行：</span>
								民生银行深圳高新区支行 
							</li>
							<li>
								<span>收款人：</span>
								深圳市千讯数据股份有限公司 
							</li>
							<li>
								<span>账号：</span>
								&nbsp;1820 0141 7000 1447 
							</li>
						</ul>
					</div>
				</div>
				
				<div style="width: 100px;margin: 0px auto;margin-top: 55px;">
					<button class="btn  back-btn chabtn mg0 pd0 btn-alwd-btn-close" >关闭</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<!-- <div class="alwindow-box alwd-cz cztc">
		<div class="alwd-content-box">
			<div class="alwd-bg alwd-t"></div>
			<i class="btn alwd-close"><img src="../static/img/send/tccha.png" /></i>
			<div class=" flex-c alwd-bg alwd-b"></div>
			<div class="flex-c alwd-content alwd-cz-cont">
				<div class="elastic-box-content elastic-box-left">
					<p class="elastic-box-text ">充值账号：18677167879</p>
					<p class="elastic-box-text color333">请选择购买数量</p>
					<div class="flex-c fromTop">
						<button class="fileli-btn" onclick="btn_addSmsNum(1);">1万条</button>
						<button class="fileli-btn fromLeft" onclick="btn_addSmsNum(5);">5万条</button>
						<button class="fileli-btn fromLeft" onclick="btn_addSmsNum(10);">10万条</button>
						<button class="fileli-btn fromLeft" onclick="btn_addSmsNum(50);">50万条</button>
					</div>
					<div class="elastic-box-b">
						<p class="elastic-box-num">
							<input id="smsNum" type="text" maxlength="8" style="border: none;" placeholder="请输入购买的短信条数"/>
						</p>
						<p class="elastic-box-text color333 mgb30"style="margin-top: 14px">
							需支付：<span id="smsMoney" style="margin-right:5px;">0</span>元
						</p>
						<p class="flex-c">
							<a id="pay_index" class="btn s-btn back-btn mg0 pd0 flex-c alwd-btn-close">立即支付</a>
						</p>
						<p class="elastic-box-smallTexr">注：购买成功后不支持退款</p>
						<p class="elastic-box-text color333">购买价格</p>
						<div>
							<ul style="margin-top: 8px;">
								<li>
									<p class="elastic-box-smallTexr">0.025元/条 &nbsp; &nbsp;
										&nbsp; &nbsp; &nbsp;购买数量范围：0~10000条</p>
								</li>
								<li>
									<p class="elastic-box-smallTexr">0.024元/条 &nbsp; &nbsp;
										&nbsp; &nbsp; &nbsp;购买数量范围：10000~1000000条</p>
								</li>
								<li>
									<p class="elastic-box-smallTexr">0.023元/条 &nbsp; &nbsp;
										&nbsp; &nbsp; &nbsp;购买数量范围：1000000~10000000条</p>
								</li>
							</ul>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div> -->
	

	<!--支付提示弹窗-->
	<div class="alwindow-box zfts " style="display: none;">
		<div class="alwd-content-box">
			<div class="alwd-bg alwd-t"></div>
			<i class="btn alwd-close"><img src="../static/img/send/tccha.png" /></i>
			<div class=" flex-c alwd-bg alwd-b">
				<a class="btn s-btn back-btn mg0 pd0 flex-c ">支付失败</a> <a
					class="btn s-btn back-btn mg0 pd0 flex-c ">支付成功</a>
			</div>
			<div class="flex-c alwd-content alwd-drhm">
				<div class="elastic-box-content">
					<p class="elastic-box-text text-center">请在支付页面完成支付</p>
				</div>
			</div>
		</div>
	</div>
	<div class="alwindow-box cxzf " style="display: none;">
		<div class="alwd-content-box">
			<div class="alwd-bg alwd-t"></div>
			<i class="btn alwd-close"><img src="../static/img/send/tccha.png" /></i>
			<div class=" flex-c alwd-bg alwd-b">
				<a class="btn s-btn back-btn mg0 pd0 flex-c ">放弃</a> <a
					class="btn s-btn back-btn mg0 pd0 flex-c ">发起支付</a>
			</div>
			<div class="flex-c alwd-content alwd-drhm">
				<div class="elastic-box-content">
					<p class="elastic-box-text text-center">请重新发起支付</p>
				</div>
			</div>
		</div>
	</div>
	<div class="alwindow-box zfcg " style="display: none;">
		<div class="alwd-content-box">
			<div class="alwd-bg alwd-t"></div>
			<i class="btn alwd-close"><img src="../static/img/send/tccha.png" /></i>
			<div class=" flex-c alwd-bg alwd-b">
				<a class="btn s-btn back-btn mg0 pd0 flex-c ">关闭</a>

			</div>
			<div class="flex-c alwd-content alwd-drhm">
				<div class="elastic-box-content">
					<p class="elastic-box-text text-center">
						<span class="zftitle">支付成功</span><br />成功购买：<span>25500</span>条
					</p>
				</div>
			</div>
		</div>
	</div>
	<script src="${ctx}/static/js/app/send/index.js"></script>
</body>

</html>