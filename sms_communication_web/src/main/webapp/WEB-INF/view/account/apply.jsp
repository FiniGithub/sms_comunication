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

    <style>

        select {
            margin-left: 6px;
        }

        input[type="radio"] {
            margin-left: 6px;
            position: relative;
            top: 2px;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <![endif]-->
</head>

<body>

<div style="text-align:center;">
    <input type="hidden" value="${ctx}" id="server_path"/>
    <input type="hidden" value="${menuId}" id="menuId"/>

    <div class="com-title">
        <h4 class="counttitle">申请账户页面</h4>
    </div>
    <form id="add_form" method="post" style="display:inline-block;"
          class="form-horizontal required-validate"
          action="${ctx}/applyaccount/from/merge.do?menuId=${menuId}"
          enctype="application/x-www-form-urlencoded">
        <input type="hidden" id="i_id" name="id" value="${smsUser.id}"/>
        <ul class="form-inp-ul">
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>用户名称：</span><input class="inp-inp imp" type="text" id="name"
                                                                       name="name"
                                                                       value="${smsUser.name}"/>

                </div>
            </li>


            <li>
                <div class="form-inp-box">
                    <span>地址：</span><input class="inp-inp" type="text" id="address" name="address"
                                           value="${smsUser.address}"/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>联系人：</span><input class="inp-inp imp" type="text" id="contact"
                                                                      name="contact" value="${smsUser.contact}"/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span><span class="cfd">*</span>手机：</span><input class="inp-inp imp" type="text" id="phone"
                                                                     name="phone" value="${smsUser.phone}"/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span>电话：</span><input class="inp-inp" type="text" id="telphone" name="telphone"
                                           value="${smsUser.telphone}"/>
                </div>
            </li>

            <li>
                <div class="form-inp-box">
                    <span>QQ：</span><input class="inp-inp" type="text" id="qq" name="qq" value="${smsUser.qq}"/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span>邮箱：</span><input class="inp-inp" type="text" id="userEmail" name="userEmail" data-alert="邮箱"
                                           value="${smsUser.userEmail}"/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span>签名：</span><input class="inp-inp" type="text" id="signature"
                                           name="signature" value="${smsUser.signature}"/>
                </div>
            </li>
            <li>
                <div class="form-inp-box">
                    <span>备注：</span><input class="inp-inp" type="text" id="describes"
                                           name="describes" value="${smsUser.describes}"/>
                </div>
            </li>

            <li style="text-align:center">
                <div class="inp-box inpt-box-btn">
                    <input class="f-btn" type="button" value="确定" id="save_data_btn">
                    <input class="f-btn" type="button" value="取消" id="cancel_data_btn">
                </div>
            </li>
        </ul>
    </form>
</div>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/account/apply.js"></script>
</body>
</html>