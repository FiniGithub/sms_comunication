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
<input id="input_superAdmin" type="hidden" value="${session_user.superAdmin}">
<input type="hidden" value="${ctx}" id="server_path"/>
<input type="hidden" id="menu_id" value="${menuId}"/>

<div>
    <div class="com-content" style="text-align:center">

        <div class="com-menu inp-menu">
            <div class="inp-box" id="addSmsUsers"></div>
            <div class="inp-box inpt-box-btn">
                <span class="inp-title">账号:</span> <input type="text" class="inp-box-inp" id="email" placeholder=""/>
            </div>

            <div class="inp-box inpt-box-btn">
                <span class="inp-title">用户名:</span> <input type="text" class="inp-box-inp" id="name" placeholder=""/>
            </div>

            <div class="inp-box inpt-box-btn">
                <span class="inp-title">联系人:</span> <input type="text" class="inp-box-inp" id="contact" placeholder=""/>
            </div>

            <div class="inp-box inpt-box-btn">
                <span class="inp-title">手机号:</span> <input type="text" class="inp-box-inp" id="phone" placeholder=""/>
            </div>

            <div class="inp-box " style="display: none;" id="tdDiv">
                <span> 通道类型：</span>
                <select id="i_aisleType" name="aisleType">
                    <option value="">请选择</option>
                </select>
            </div>

            <div class="inp-box ">
                <span> 网充状态：</span> <select id="networkChargingState">
                <option value="">请选择</option>
                <option value="0">开通</option>
                <option value="1">关闭</option>
            </select>
            </div>
            <div class="inp-box" style="display: none;" id="userDiv">
                <span> 用户类型：</span>
                <select class="province select" id="i_userType"
                        name="userType">
                    <option value="">请选择</option>
                    <c:forEach var="item" items="${roleList}" varStatus="status">
                        <option key="${item.id}" value="${item.id}">${item.roleName}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="inp-box" style="display: none;" id="gsDiv">
                <span> 归属：</span>
                <select class="province select" id="nickName" name="nickName">
                    <option value="">请选择</option>
                    <c:forEach var="item" items="${sysUserList}"
                               varStatus="status">
                        <option key="${item.id}" value="${item.email}">${item.email}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="inp-box inpt-box-btn" id="btnMessage">
            </div>

            <div id="data_div"></div>
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

<div class="modal fade" id="zy_div">
    <div class="modal-dialog awl">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="h4_title">转移归属</h4>
            </div>
            <div class="modal-body">
                <form id="_form" method="post" class="form-horizontal required-validate">
                    <input type="hidden" id="zy_id"/>
			
                    <div class="inp-box" id="ygsDiv" style="padding-left:40px">
                        <span>原归属：</span>

                        
                            <span id="userType"></span>
                       
                    </div>

                    <div class="inp-box" style="padding-left:40px">
                        <span>归属：</span>                        
                            <select  id="zy_bid">
                                <%--  <option value="">请选择</option>
                                  <c:forEach var="item" items="${sysUserList}"
                                             varStatus="status">
                                      <option key="${item.sysUserId}" value="${item.sysUserId}">${item.email}</option>
                                  </c:forEach>--%>
                            </select>
                        </div>
                    </div>

                    <div class="modal-footer">
                      <input type="button" class="f-btn" id="save_data_btn"  value="确 定"/>
                <input type="button" class="f-btn"  id="cancel_data_btn" data-dismiss="modal" value="取 消">      
                       
                    </div>
                </form>
            </div>
        </div>
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
              <input type="button" class="f-btn"  value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">               
              
            </div>
        </div>
    </div>
</div>
<!-- Custom Theme JavaScript -->
<script src="${ctx}/static/js/commons/jquery.bootstrap.js"></script>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/account/manageMent.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>