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
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <![endif]-->
    <style>
        .btn-group-sm > .btn, .btn-sm, .btn {
            padding: 0;
            line-hegiht: normal;
            margin-left: 5px;
        }
    
    </style>
</head>
<body>

<div>
    <div class="com-content " style="text-align:center;">
        <div class="com-menu inp-menu">
            <div class="inp-box" id="addSmsUsers"></div>
            <input type="hidden" value="${ctx}" id="server_path"/> <input
                type="hidden" id="menu_id" value="${menuId}"/>

            <div class="inp-box ">
                <span class="inp-title"> 账号：</span> <input class="inp-box-inp"
                                                           id="email" type="text" placeholder="">
            </div>
            <div class="inp-box ">
                <span> 状态：</span> <select id="state">
                <option value="">请选择</option>
                <option value="0">正常</option>
                <option value="1">关闭</option>
            </select>
            </div>
            <div class="inp-box" style="display: none;" id="tdDiv">
                <span> 通道类型：</span>
                <select id="i_aisleType" name="aisleType">
                    <option value="">请选择</option>
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

            <div class="inp-box inpt-box-btn">
                <input class="f-btn" type="button" value="查询" id="search_btn">
            </div>
            <div id="data_div"></div>
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

<div class="modal fade" id="je_data_div">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="h4_title">配置短信</h4>
            </div>
            <div class="modal-body">
                <form id="cz_merge_form" method="post"
                      class="form-horizontal required-validate">
                    <input type="hidden" style="width: 100px;" id="czid" name="czid"/>
                    <input type="hidden" style="width: 100px;" id="sysUserId" name="sysUserId"/>

                    <div class="awcont">


                        <div class="inp-box ">
                            <span>充值账号： </span>

                            <input class='inp-box-inp' disabled="disabled" id="czname"
                                   type="text" placeholder=""/>
                        </div>
                        <div class="inp-box ">
                            <span>剩余数量： </span>

                            <input class='inp-box-inp' disabled="disabled" id="symoney"
                                   type="text" placeholder=""/>
                        </div>
                        <div class="inp-box ">
                            <span>操作类型： </span>
                            <select id="type" name="type">
                                <option value="">请选择</option>
                                <option value="0">人工充值</option>
                                <c:if test="${roleType !=58}">
                                    <option value="1">发送扣费</option>
                                    <option value="2">返还扣费</option>
                                </c:if>
                                <!---客服   一级管理员  超级管理员-->
                                <c:if test="${roleType ==48 || roleType ==58 || session_user.superAdmin==1}">
                                    <option value="8">核减</option>
                                </c:if>
                                <%--   <option value="3">快钱充值</option>--%>
                                <option value="4">赠送</option>
                                <option value="5">转出</option>
                                <option value="6">转入</option>
                                <option value="7">退费</option>
                            </select>
                        </div>
                        <div class="inp-box " id="moneyDiv">
                            <span>充值金额： </span>
                            <input class='inp-box-inp' onkeyup="value=value.replace(/[^-\d]/g,'')" id="money"
                                   name="money"/>元
                        </div>
                        <div class="inp-box ">
                            <span>添加条数： </span>

                            <input class='inp-box-inp' onkeyup="value=value.replace(/[^-\d]/g,'')" id="i_czmoney"
                                   name="czmoney"/>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="button" class="f-btn " id="cz_allot_btn" value="提 交"/>

                        <input type="button" class=" f-btn"
                               data-dismiss="modal" id="cancel_btn" value="取 消"/>


                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="sureModel">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="del_h4_title">确定充值</h4>
            </div>
            <ul class="czmessge">
                <li> 请确认充值信息：
                </li>
                <li> 账号：<span id="zh"></span>
                </li>
                <li> 充值类型：<span id="lx"></span>
                </li>
                <li> 充值金额：<span id="je"></span>
                </li>
                <li> 短信数量：<span id="sl"></span>
                </li>
            </ul>
            <div class="modal-footer">
                <input type="button" class="f-btn" id="btn_ok" value="确 定"/>
                <input type="button" class="f-btn" id="btn_cencel" data-dismiss="modal" value="取 消">
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="updateState">
    <div class="modal-dialog awl">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="btnOk_update">确定修改</h4>
            </div>
            <p class="aw-newcontent">
                <span class="aw-new">确认是否关闭账号？</span>
            </p>

            <div class="modal-footer">
                <input type="button" class="f-btn" id="btn_update" value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">

            </div>
        </div>
    </div>
</div>
<!-- Custom Theme JavaScript -->
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/account/account.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>