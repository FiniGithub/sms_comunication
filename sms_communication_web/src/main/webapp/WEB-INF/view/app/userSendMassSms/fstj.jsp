<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,user-scalable=no">
		<title>代理平台-发送统计</title>
		<link rel="stylesheet" href="${ctx}/static/css/app/send/fstj.css">
		<style>
			.com-table-box,#com-fstable{
				font-weight: normal!important;
			}
			th{
				border-right:1px #eaeaea solid;
			}
		</style>
	</head>

	<body>
		<!--顶部-->
 		<input id="path" type="hidden" value="${ctx}">
 		<input id="email" type="hidden" value="${session_user.email}"/>
		<!--内容-->
		<div class="content-box">
			<div class="container head-box pd0  indextilt">
				<div class="f-l flex-c">
					<i class="inborder">&nbsp;</i><span class="intilte">发送统计</span><img class="index-logo" src="../static/img/send/fastj.png" />
				</div>
				
				<div class="f-r flex-c back-btn-box">
					<a id="export" class="btn s-btn back-btn pd0 fstj-btn">
					导出上月统计
				</a>
				</div>
				
			</div>
			<div class="container head-box pd0 flex-c pho-search-box fstj-s-box">
				<ul class="flex-c pho-search-input f-r pho-search-input-r fstj-s-box">
						
						 <li ><span>时间：</span></li>
						 <li class="flex-c  pho-s-inp input-group date">
						 	<input id="start_input" type="text" placeholder="开始时间" />
						<a  class="input-group-addon pd6"> <b class="glyphicon-calendar caret"></b></a>
					</li>									
					<li><span>—</span></li>
					<li class="flex-c  pho-s-inp input-group date">
						<input id="end_input" type="text" placeholder="结束时间" />
						<a  class="input-group-addon pd6"> <b class="glyphicon-calendar caret"></b></a>
					</li>
					<li><input id="search_btn" class="btn s-btn" type="button" value="查询" /></li>	
					</ul>
			</div>
			<div class="com-table-box">			
				<div class="container head-box pd0 flex-c  tasksum-box">
					  

					号码数量：<span class="fssum">0</span>，
					发送成功：<span class="xfsum">0</span>，
					发送失败：<span class="jssum">0</span>，
					未知： <span class="unkwon">0</span>
				</div>
				
				<table id="com-fstable"></table>
			</div>
		</div>
		
	<script src="${ctx}/static/js/app/send/fstj.js"></script>
	</body>
	
</html>