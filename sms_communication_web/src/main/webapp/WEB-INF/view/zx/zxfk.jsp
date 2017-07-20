<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>    
    <title>咨询与反馈</title>
    <link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
    <link rel="stylesheet" type="text/css"
          href="${ctx}/static/css/bootstrap-datetimepicker.min.css"/>
    <script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>  
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
     <script src="${ctx}/static/js/bootstrap-datetimepicker.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/js/bootstrap-datetimepicker.zh-CN.js" type="text/javascript" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">   
    <script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>
    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <style>
    	.modal-dialog{
    		width:480px;
    	}
    </style>
    <![endif]-->
    
   
</head>
<body>

<input id="id_input" type="hidden" value="${id}" />
<input id="path" type="hidden" value="${ctx}">
<input type="hidden" value="${ctx}" id="server_path"/>
 <div class="inp-box inpt-box-btn" style="padding-left:6px;">

        <input class="f-btn" type="button" value="删除" id="del_btn">

    </div>

	<div class="com-table-box">

		<table id="com-table" class="container head-box pd0 com-table"></table>
		<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>



	</div>
<div class="modal fade" id="del">
    <div class="modal-dialog awl">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="del_h4_title">确定删除</h4>
            </div>
            <p class="aw-newcontent">
                <span class="aw-new">确认是否删除账号？</span>
            </p>

            <div class="modal-footer">
                <input type="button" class="f-btn" id="btn_del" value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">

            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/commons/common.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/zx/zxfk.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>