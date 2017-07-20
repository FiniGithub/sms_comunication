<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>welcome</title>
	<style>
	
	.onetable td{
			height: 30px;
			width: 150px;
			font-size: 18px;
		}
		
	.woetTable td{border:solid #A9A9A9;
		border-width:0px 1px 1px 0px; 
		padding:10px 0px;
		text-align:center;
		width: 150px;
		height: 50px;
	}
	 .woetTable{
		border:solid #A9A9A9;
		border-width:1px 0px 0px 1px;
	}
	</style>
</head>
<body>
<!-- 	<div class="com-title">
			<h4>欢迎页面</h4>
			<div class="title-remark">欢迎来到运营系统， 下面列出常用功能菜单， 方便您快速操作。</div>
			<hr/>
	</div> -->


	<!--内容-->
<c:if test="${userSmsNum!=null}">
	<div class="">

		<div class="aw-content-wrap-top">
			<div class="index-btn-box  flex-c">
				<c:if test="${newSmsUserNum!=null}">
						<div class="index-btn flex-c">
						   <c:if test="${newSmsUserNum!=0}">
								<a href="${ctx}/puser/listview.do?id=105&ptype=1">
								<div class="index-btn-imgbox pull-left">
									<img style="width: 80%;" src="${ctx}/static/new/img/tianjia.png"/>
								</div>
									<div class="index-btn-ifo flex-c pull-right">
										<p><span style="color: #27a9e3;"><c:out value="${newSmsUserNum}"/></span>个</p>
										<p>新增注册会员</p>
									</div>
								</a>
							</c:if>
							<c:if test="${newSmsUserNum==0}">
								<div class="index-btn-imgbox pull-left">
									<img style="width: 80%;" src="${ctx}/static/new/img/tianjia.png"/>
								</div>
									<div class="index-btn-ifo flex-c pull-right">
										<p><span style="color: #27a9e3;"><c:out value="${newSmsUserNum}"/></span>个</p>
										<p>新增注册会员</p>
									</div>
							</c:if>
						</div>
					
				</c:if>
				<c:if test="${newSmsUserNum==null}">
					<div class="index-btn flex-c">
							<div class="index-btn-imgbox pull-left">
								<img style="width: 80%;" src="${ctx}/static/new/img/tianjia.png"/>
							</div>
								<div class="index-btn-ifo flex-c pull-right">
									<p><span style="color: #27a9e3;">-</span></p>
									<p>新增注册会员</p>
								</div>
						</div>
				</c:if>
				
				
				
				
					<div class="index-btn flex-c">
						<a href="${ctx}/puser/listview.do?id=105">
						<div class="index-btn-imgbox pull-left">
							<img style="width: 80%;" src="${ctx}/static/new/img/dxts.png"/>
						</div>
						<div class="index-btn-ifo flex-c pull-right">
							<p><span style="color: #28b779;"><c:out value="${userSmsNum}"/></span>条</p>
							<p>客户剩余条数</p>
						</div>
						</a>
					</div>
				
				
				<div class="index-btn flex-c">
					<div class="index-btn-imgbox pull-left">
						<img style="width: 80%;" src="${ctx}/static/new/img/jinzhang.png"/>
					</div>
					<div class="index-btn-ifo flex-c pull-right">
						<p><span style="color: #852b99;">0</span>元</p>
						<p>今日在线充值</p>
					</div>
				</div>
				
				<c:if test="${todaySmsNum!=null}">
					<div class="index-btn flex-c">
						<a href="${ctx}/puser/puserBill.do?id=139&type=0">
						<div class="index-btn-imgbox pull-left">
							<img style="width: 80%;" src="${ctx}/static/new/img/goumai.png"/>
						</div>
						<div class="index-btn-ifo flex-c pull-right" style="margin-left: 10px;">
							<p><span style="color: #ffb848;">
									<c:out value="${todaySmsNum}"/>
								</span>
							条</p>
							<p style="font-size: 12px;">今日客户购买条数</p>
						</div>
						</a>
					</div>
				</c:if>
				<c:if test="${todaySmsNum==null}">
					<div class="index-btn flex-c">
						<div class="index-btn-imgbox pull-left">
							<img style="width: 80%;" src="${ctx}/static/new/img/goumai.png"/>
						</div>
						<div class="index-btn-ifo flex-c pull-right" style="margin-left: 10px;">
							<p><span style="color: #ffb848;">
									0
								</span>
							条</p>
							<p style="font-size: 12px;">今日客户购买条数</p>
						</div>
					</div>		
				</c:if>
				
			</div>
		</div>
		<div class="aw-content-wrap-title-box">
			<span class="aw-content-wrap-title pull-left">发送统计</span> <div class="aw-content-wrap-title-link pull-right">
				<a href="${ctx}/puser/statistical.do?id=138">客户统计详情>></a><a href="${ctx}/puser/channelstataical.do?id=154">通道统计详情>></a>
			</div>
		</div>
		<div class="aw-content-wrap-table-box flex-c">
			<table class="aw-content-wrap-com-table">	
				<tbody>
					<tr class="aw-content-wrap-com-th">
						<th rowspan="2" style="width: 110px;height: 25px;">时间</th>
						<th rowspan="2" style="width: 110px;height: 25px;">发送号码数</th>
						<th rowspan="2" style="width: 110px;height: 25px;">消费计费条数</th>
						<th rowspan="2" style="width: 110px;height: 25px;">结算计费条数</th>
						<th colspan="3" style="width: 180px;height: 25px;">移动</th>
						<th colspan="3" style="width: 180px;height: 25px;">联通</th>
						<th colspan="3" style="width: 180px;height: 25px;">电信</th>
						
						
					</tr>
					<tr class="aw-content-wrap-com-th">
						
						<th style="width: 60px;height: 25px;">成功</th>
						<th style="width: 60px;height: 25px;">失败</th>
						<th style="width: 60px;height: 25px;">未知</th>
						<th style="width: 60px;height: 25px;">成功</th>
						<th style="width: 60px;height: 25px;">失败</th>
						<th style="width: 60px;height: 25px;">未知</th>
						<th style="width: 60px;height: 25px;">成功</th>
						<th style="width: 60px;height: 25px;">失败</th>
						<th style="width: 60px;height: 25px;">未知</th>
					</tr>
					
					
					
					<c:forEach var="item" items="${smsAgentlist}" varStatus="status">   
		 				<tr  class="com-t-btn">
							<td ><span ><c:out value="${item.auditTime}"/></span></td>
							<td ><c:out value="${item.sumSendNum}"/></td>
							<td ><c:out value="${item.sumBillingNum}"/></td>
							<td ><c:out value="${item.sumActualNum}"/></td>
							<td ><c:out value="${item.sumSucceedNumUs}"/></td>
							<td ><c:out value="${item.sumFailureNumUs}"/></td>
							<td ><c:out value="${item.sumUnknownNumUs}"/></td>
							<td ><c:out value="${item.sumSucceedNumMs}"/></td>
							<td ><c:out value="${item.sumFailureNumMs}"/></td>
							<td ><c:out value="${item.sumUnknownNumMs}"/></td>
							<td ><c:out value="${item.sumSucceedNumTs}"/></td>
							<td ><c:out value="${item.sumFailureNumTs}"/></td>
							<td ><c:out value="${item.sumUnknownNumTs}"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</c:if>
<c:if test="${userSmsNum==null}">
	<div style="width: 100%;text-align: center;" >
		<img style="" src="${ctx}/static/new/img/bangong.png"/>
	</div>
</c:if>
</body>
</html>