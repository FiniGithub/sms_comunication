<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
    <title>全网信通-短信发送</title>
    <link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/laydate/need/laydate.css"/>
    <script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
    <script src="${ctx}/static/dzd/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
	 <script type="text/javascript" src="${ctx}/static/dzd/jquery.ui.widget.js"></script>	 
	 <script type="text/javascript" src="${ctx}/static/dzd/jquery.iframe-transport.js"></script>
	  <script type="text/javascript" src="${ctx}/static/dzd/jquery.fileupload.js"></script>
    <script type="text/javascript" src="${ctx}/static/dzd/index.js"></script>
    <script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>
    <!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <style>
        .modal-dialog {
            width: 480px;
        }
    </style>
    <![endif]-->
    <style>
        .laydatesecend {
            display: none;
        }
        .modal-backdrop.bg-d{
        	    filter: alpha(opacity = 70);
   				 opacity: .7;
        }
    </style>
</head>

<body>
<input id="ctx" type="hidden" value="${ctx}">
<input id="uuid" type="hidden">
<input id="forwardUuid" type="hidden" value="${forwardUuid}">
<input id="operators" type="hidden" value="${operators}">
<div class="h-title" style="width:910px; margin-left:21px;">
    账号：<span class="u-n-box">${user.nickName}</span> &nbsp;&nbsp;短信剩余：<span class="u-num">${surplusNum}</span>条
</div>
<div class="tab-box">
    <table class="dzd-table">
        <tr class="addNum-box">
            <td class="tab-h">添加号码</td>
            <td class="tab-content">
                <textarea id="text_phoneList" class="addNum-text" name="addNum" rows="" cols=""
                          onkeyup="value=value.replace(/[^\d\,\n]/g,'')"></textarea>
                <button class="addNum-btn" type="button" id="btn_addPhone"><p style="margin-top:-6px;">添加</p></button>


            </td>
            <td class="tab-exp">
                <ul>
                    <li>&middot;每行一个手机号码，可手动输入或复制粘贴号码；</li>
                    <li>&middot;点击“添加”按钮，将号码添加到号码列表中。</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td>号码列表</td>
            <td class="addPhon-box">
                <div id="phone_show" class="addPhon-text">

                </div>
                <p style="margin-top: 4px;">已添加号码：<span class="pho-count" id="font-leadCount"></span>个</p>
                <p class="addphon-btn-box">
                    <input type="button" name="" value="导入号码" class="f-btn f-l"
                           data-toggle="modal" data-target="#upload_file"/>

                    <input type="button" name="" id="btn_filter_phone" value="二次过滤 " class="f-btn f-r"/>
                    <input type="button" name="" id="" value="清 空" class="clear-btn f-btn f-r" onclick="clearFile()"/>
                </p>
            </td>
            <td style="vertical-align:top;">
                <ul style="margin-bottom: 55px;">
                    <li>&middot;系统会自动过滤当前添加或导入号码中的重号和错误号码；</li>
                    <li>&middot;多批次的号码，请点击“二次过滤”按钮进行合并过滤。</li>


                </ul>
                <p id="lead_phone_div" class=" result-num-box " style="display: none;">当前导入：有效号码 <span
                        class="cghm">0</span>个、重复号码<span
                        class="r-num" id="jdrepet">0</span>个、错误号码<span class="e-num" id="jderror">0</span>个</p>
                <p id="filter_phone_div" class="result-num-box" style="display: none;">二次过滤：有效号码 <span
                        class="filter_cghm">0</span>个、重复号码<span
                        class="r-num" id="filter_jdrepet">0</span>个、错误号码<span class="e-num" id="filter_jderror">0</span>个
                </p>
            </td>
        </tr>
        <tr>
            <td>短信内容</td>
            <td style="text-align:left;">
                <div class="pho-s-inp-box">
                    <c:if test="${signType==0}">
                        <div class="flex-c  pho-s-inp mg0 xialbtn f-l">
                            <div id="sign_xlbox" class="xlbox" style="color: #000000;"></div>
                            <input class="cl0 xlbtn " data-inp="qminp" id="qminp" type="button" value=""
                                   style="text-align:left;width:100%;color:#585959;"/>
                            <a class="xlbtn" data-inp="qminp">
                                <span class="xia-bg"></span>
                            </a>

                        </div>
                    </c:if>
                    <c:if test="${signType!=0}">
                        <div class="flex-c   mg0 xialbtn f-l">
                            【<input type="text" id="input_sign"/>】
                        </div>
                    </c:if>
                    <span style="padding-top:2px;padding-left:10px;color:#585959" class="f-l">短信签名</span>
                </div>


                <p style="padding-top:10px">
                    <textarea id="content" class="dxcontent-xg-txt" maxlength="300"></textarea>
                </p>
                <p class="dxnum">当前字数：<span class="dxlength">0</span>个，剩余字数：<span
                        class="s-dxlength">300</span>个，短信条数：<span
                        class="smslength">0</span>条</p>
            </td>
            <td rowspan="2" style="vertical-align:top;position:relative;top:1px;">
                <ul>
                    <li>&middot;短信内容须合法、真实、健康，严格遵守 <a class="nocie" href="${ctx}/announcement.do" target="_blank">《全网信通使用规定》</a>
                        ；
                    </li>
                    <li>&middot;一条短信70个字，字母、符号以及内容中空格均计字数；</li>
                    <li>&middot;70字以上长短信按每66个字1条计费，最多300字；</li>
                    <li>&middot;复制短信内容，请使用键盘Ctrl+V粘贴。</li>
                    <li>&middot;发送失败的短信数量，系统于次日凌晨自动返还。</li>
                </ul>
                <p class="snotice">特别提示：${session_alise_group.hint}</p>
            </td>
        </tr>
        <tr>
            <td>发送方式</td>
            <td>
                <div class="send_box">
                    <div class="send_time_box type-box">
                        <input id="defalut_send_time" type="radio" name="type" checked="checked"/>实时发送
                        <input id="check_send_time" type="radio" name="type"/>定时发送
                    </div>
                    <div class="send_time_box day-box">
                        <div id='seleday'>
                            <input id="input_send_timing" type="text" readonly="readonly"/>

                        </div>
                    </div>
                </div>
                <div class="sendbtn-box">
                    <input type="button" class="f-btn" value="发送" id="sendsms-btn">
                    <input type="button" class="f-btn" value="重置" style="margin-left:10px"
                           onclick="textclear()">
                </div>

            </td>


        </tr>
    </table>
</div>

<div class="modal fade in" id="bg" style="display: none;">
<div class="loadding-img">
<img class="flex-hint" src="../static/img/send/u4.gif">
</div>
	
</div>
<div class="modal-backdrop fade in bg-d" style="display: none;">

</div>
<!-- 上传文件模态框 -->
<div class="modal fade" id="upload_file">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
				<h4 class="modal-title" >上传文件</h4>
            </div>
            <div class="modal-body">
                <p id="del_p_title">
                <ul class="alwd-upload-inp">
                    <li>
                        <form id="uploadFile" method="post" class="flex-c" enctype="multipart/form-data"
                              autocomplete="off">
                            <span id="font-allNum-once" style="display: none">0</span>
                            <span class="upl-title">号码文件</span>
                            <input type='text' name='filerout' id='textfield' class='fileli inp-box-inp'
                                   readonly="readonly"/>

                            <input type='button' class="f-btn" value='浏览...' 
                                   onclick="$('#input_uploadFile').click()"/>

                            <input type="file" name="uploadFile" id='input_uploadFile' style="dispaly:none;"
                                   class='fileli' onchange="readFileUpload();"/>
                        </form>
                    </li>

                    <li class="flex-c proge_li" style="font-size: 10px;">
                        注:文件内每行一个号码
                    </li>
                    <li class="proge-num-box proge_li">
                        <label class="lead-finish" style="display: none">
                            已完成：<span class="progebfb">0</span>%
                        </label>
                        <img class="flex-hint" src="../static/img/send/u4.gif">
                    </li>
                </ul>
                </p>
            </div>
            <div class="modal-footer">
              <input type="button" class="f-btn" id="udatephobtn" onclick="updatePhoneFile()"   value="确 定">

                    <input type="button" class="f-btn " id="upfilecenter"  value="取 消">
        
            </div>
        </div>
    </div>
</div>


<!-- 输入验证码、号码详情模态框 -->
<div class="modal fade" id="verifyAndDetailModal">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
				<h4 class="modal-title" >确认发送</h4>
            </div>


            <!-- 验证码 -->
            <div id="verifyModal" style="display: none;">
                <div class="modal-body" style="text-align:center;">
                    <div id="vertify_title" style="display:block;" >
                    <ul class="alwd-upload-inp yzmdb" style="text-align:left;width:220px;margin:0 auto;">
                        <li>
                       		 <span style="display:inline-block;width:60px">手机号码：</span>
                            <span style="display:inline-block;width:106px;" id="phone">${phone}</span>
                            <input id="pwd-getcode" class="f-btn" type="button" value="获取验证码" style="width: 75px;"/>
                            <br/>
                            <br/>
                            <span style="display:inline-block;width:60px;text-align:right;">验 证 码：</span>
                            
                            <input id="verifyCodeInput" style="text-align: left;width:106px;margin:0" class="inp-box-inp"
                                   type="text"
                                   maxlength="5"/>
                        </li>
                      
                    </ul>
                    </div>
                </div>
                <div class="modal-footer">
                 <input type="button" class="f-btn" id="sendsmsyz-btn" value="确 定">

                    <input type="button" class="f-btn" data-dismiss="modal" value="取 消">
                    
                </div>
            </div>






            <!-- 号码详情 -->
            <div id="filterPhoneModal" style="display: none;">
                <div class="modal-body">
                    <span id="span_smsContent" style="display: none;"></span>
                    <ul class="alwd-filter">
                        <li>有效号码: <span id="validPhoneNum">0</span> 个</li>
                        <li>短信字数: <span id="smsWords">0</span> 个，计<span id="smsTiao"></span>条</li>
                        <li>短信数量: <span id="send_smsLength">0</span> 条</li>

                        <li style="display: none;">所有号码: <span id="allPhoneNum">0</span> 个</li>
                        <li style="display: none;">重复号码: <span id="repeatPhoneNum">0</span> 个</li>
                        <li style="display: none;">错误号码: <span id="invalidPhoneNum">0</span> 个</li>
                    </ul>
                    </p>
                </div>
                <div class="modal-footer">

                    <input type="button" class="f-btn" id="smssendBtn" value="确  定">

                    <input type="button" class="f-btn" data-dismiss="modal" value="取 消">

                </div>
            </div>


        </div>
    </div>
</div>


<script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>
<script src="${ctx}/static/js/app/send/send.js"></script>

</body>
</html>