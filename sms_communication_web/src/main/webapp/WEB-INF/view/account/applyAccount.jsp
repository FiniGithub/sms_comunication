<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>

    <link
            href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <script src="${ctx}/static/js/commons/common.js"></script>
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
    <link href="${ctx}/static/css/jquery.datetimepicker.css"
          rel="stylesheet">
    <script src="${ctx}/static/js/jquery.datetimepicker.js"></script>


    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <![endif]-->
</head>
<body>
<div>
    <div class="com-content" style="text-align:center;">
        <input id="input_superAdmin" type="hidden" value="${session_user.superAdmin}">

        <div class="com-menu inp-menu">
            <div class="inp-box" id="addSmsUsers"></div>
            <input type="hidden" value="${ctx}" id="server_path"/> <input
                type="hidden" id="menu_id" value="${menuId}"/>

            <div class="inp-box ">
                <span class="inp-title"> 手机号:</span> <input class="inp-box-inp"
                                                            id="phone" type="text" placeholder="">
            </div>
            <div class="inp-box inpt-box-btn">
                <input class="f-btn" type="button" value="查询" id="search_btn">
                <input class="f-btn" type="button" value="申请" id="addBtn">
                <input class="f-btn" type="button" value="修改" id="updateBtn">
                <input class="f-btn" type="button" value="删除" id="delBtn">
            </div>
        </div>
        <div id="data_div"></div>
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
              <input type="button" class="f-btn"  value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">               
              
            </div>
        </div>
    </div>
</div>

    <div class="row">
        <table id="tb_data"></table>
        <div class="minlid">
            <a id="firstpage" href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input
                class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
        </div>


    </div>

</div>


<!-- Custom Theme JavaScript -->
<script src="${ctx}/static/js/commons/jquery.bootstrap.js"></script>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/account/applyAccount.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>