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
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <![endif]-->
    <style>
        select {
            margin-left: 6px;
        }

        input[type="radio"] {
            margin-left: 6px;
            position: relative;
            top: 2px;
        }

        td {
            height: 50px;
        }
    </style>
</head>
<body>
<input type="hidden" value="${ctx}" id="server_path"/>

<div style="text-align:center;">

    <div class="com-title">

    </div>
    <form id="_form" method="post"
          class="form-horizontal required-validate"
          action="${ctx}/management/from/merge.do"
          enctype="application/x-www-form-urlencoded"
          style="display:inline-block;">
        <input type="hidden" id="i_id" name="id" value="${id}"/>
        <input type="hidden" id="sysUserId" name="sysUserId" value="${sysUserId}"/>
        <input type="hidden" id="smsUserBlankId" name="smsUserBlankId"/>
        <input type="hidden" id="btnType" name="btnType" value="${btnType}"/>
        <input type="hidden" id="menu_id" name="menuId" value="${menuId}"/>
        <ul class="form-inp-ul">
            <li style="text-align:center;"><h4 class="counttitle">新建账户</h4></li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>账号：</span><input class="inp-inp imp" type="text" id="email"
                                                                     name="email" placeholder=""/>

                </div>
            </li>
            <li>
                <div class="form-inp-box uppas">
                    <span><span class="cfd">*</span>密码：</span><input class="inp-inp imp" type="password" id="pwd"
                                                                     name="pwd" placeholder=""/>
                </div>
            </li>
            <li>
                <div class="form-inp-box uppas">
                    <span><span class="cfd">*</span>确认密码：</span><input class="inp-inp imp" type="password" id="pwdOk"
                                                                       name="pwdOk" placeholder=""/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>用户类型：</span>
                    <select class="province select imp" id="roleName" name="roleName"
                            <c:if test="${roleId ==3 && btnType=='update'}">disabled="disabled" </c:if>>
                        <option value="">请选择</option>
                        <c:forEach var="item" items="${roleList}" varStatus="status">
                            <option key="${item.id}" value="${item.id}">${item.roleName}</option>
                        </c:forEach>
                    </select>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>账号归属：</span>
                    <select class="province select imp" id="bid" name="bid">
                        <option value="">请选择</option>
                        <c:forEach var="item" items="${sysUserList}"
                                   varStatus="status">
                            <option key="${item.sysUserId}" value="${item.sysUserId}">${item.email}</option>
                        </c:forEach>
                    </select>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>用户名称：</span><input class="inp-inp imp" type="text" id="name"
                                                                       name="name" placeholder=""/>
                </div>
                <div class="form-inp-box">
                    <span>地址：</span><input class="inp-inp" type="text" id="address" name="address"
                                           placeholder=""/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>联系人：</span><input class="inp-inp imp" type="text" id="contact"
                                                                      name="contact" placeholder=""/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>手机：</span><input class="inp-inp imp" type="text" id="phone"
                                                                     name="phone" placeholder=""/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span>电话：</span><input class="inp-inp" type="text" id="telphone" name="telphone"
                                           placeholder=""/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span>QQ：</span><input class="inp-inp" type="text" id="qq" name="qq" placeholder=""/>
                </div>
                <div class="form-inp-box">
                    <span>邮箱：</span><input class="inp-inp" type="text" id="userEmail" name="userEmail"
                                           placeholder=""/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>签名：</span>
                    <input type="radio" name="signatureType" value="0" checked="checked" id="signature_type1">绑定签名
                    <input type="text" class="inp-inp " id="signature"
                           name="signature">
                    <br/>
                    <input type="radio" name="signatureType" value="1" id="signature_type2">自定义签名
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>通道类型：</span>
                    <select class="province select imp" id="aisleGroupId"
                            name="aisleGroupId">
                    </select>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>网上充值：</span>
                    <input type="radio" name="networkChargingState" value="0" checked="checked"
                           id="networkChargingState1">开通
                    <input type="radio" name="networkChargingState" value="1" id="network_charging_state2">关闭
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span>短信回复：</span>
                    <input type="radio" name="smsReplyState" value="0" id="sms_reply1">开通
                    <input type="radio" name="smsReplyState" value="1" id="sms_reply2">关闭
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span>IP地址：</span><input class="inp-inp" type="text" name="firmIp" id="firmIp" placeholder=""/>
                </div>
            </li>


            <li style="text-align:center">
                <div class="inp-box inpt-box-btn">

                    <input class="f-btn" type="button" value="确定" id="save_data_btn">
                    <input style="margin-left:30px" class="f-btn" type="button" value="取消" id="cancel_data_btn">
                    <input style="margin-left:30px" class="f-btn r-btn" type="reset" value="重置">
                </div>
            </li>
        </ul>
    </form>

</div>

<!-- Custom Theme JavaScript -->
<script src="${ctx}/static/js/commons/jquery.bootstrap.js"></script>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/commons/welcome.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/account/operate.js"></script>
</body>
</html>