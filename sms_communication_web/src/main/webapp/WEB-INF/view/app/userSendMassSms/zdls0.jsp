<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,user-scalable=no">
<title>代理平台-账单流水</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fileinput.css" />
	<link rel="stylesheet"
		  href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.css">
	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css"
		  href="${ctx}/static/css/bootstrap-datetimepicker.min.css" />
	<link rel="stylesheet" href="${ctx}/static/css/app/style.css">
	<link rel="stylesheet" href="${ctx}/static/css/app/backups.css">
	<link rel="stylesheet" href="${ctx}/static/css/elastic-box.css">
<style>
	.com-table-box{
		font-weight: normal;		
	}
	.com-table-box tr th{
		border: 1px solid #dddddd!important;
	}
</style>
</head>
	<body>
		
		<input id="path" value="${ctx}" type="hidden" />
		<!--内容-->
		<div class="content-box">
			<div class="container head-box pd0 flex-c indextilt">
				<i class="inborder">&nbsp;</i><span class="intilte">账单流水</span><img class="index-logo" src="../static/img/send/zdls.png" />
			</div>
			<div class="container head-box pd0 flex-c pho-search-box fstask-s-box">
				<input id="i_type" type="hidden" value="${typeId}">
				<ul class="head-box pd0 mg0 pho-search-btn fstask-s-btn">
					<ul class="pho-search-input f-l flex-c zdls-box">	
						<li><span>状态：</span></li>	
						<li><div class="flex-c  pho-s-inp mg0 xialbtn">
								<div class="xlbox">
									<p onclick="addKey(this,'全部',null)" data-inp="qminp">全部</p>
									<p id="cz" onclick="addKey(this,'充值',0)" data-inp="qminp">充值</p>
									<p onclick="addKey(this,'消费',1)" data-inp="qminp">消费</p>
									<p onclick="addKey(this,'退款',2)" data-inp="qminp">退款</p>
								</div>
								<input  class="cl0 xlbtn " data-inp="qminp" id="qminp" type="button" value=" " />								
								<a class="xlbtn" data-inp="qminp"> <b class="caret"></b></a>
							</div></li>
						
					</ul>					
					<ul class="flex-c pho-search-input f-r pho-search-input-r zdls-s-box">
							
						 <li ><span>时间：</span></li>
						 <li class="flex-c  pho-s-inp input-group date">
						 	<input class="focusinp" id="start_input" type="text" placeholder="开始时间" />
						<a  class="input-group-addon pd6"> <b class="glyphicon-calendar caret"></b></a>
					</li>									
					<li><span>—</span></li>
					<li class="flex-c pho-s-inp input-group date" id='enddate'>
						<input class="focusinp" id="end_input" type="text" placeholder="结束时间" />
						<a  class="input-group-addon pd6"> <b class="glyphicon-calendar caret"></b></a>
					</li>
					<li><input id="search_btn" class="btn s-btn" type="button" value="查询" /></li>	
					</ul>
										
				</ul>
			</div>
			<div class="com-table-box">			
				<table rules="none" id="fs-table" class="container head-box pd0 com-table fs-table"></table>
			</div>
		</div>


		<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
		<script src="${ctx}/static/js/commons/common.js"></script>
		<script src="${ctx}/static/js/commons/bootstrap.min.js"></script>
		<script src="${ctx}/static/js/bootstrap-table.js"></script>
		<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
		<script src="${ctx}/static/js/bootstrap-datetimepicker.min.js"
				type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/static/js/bootstrap-datetimepicker.zh-CN.js"
				type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/static/js/app/send/czjl.js"></script>
	</body>

</html>