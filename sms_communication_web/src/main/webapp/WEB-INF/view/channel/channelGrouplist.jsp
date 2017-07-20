<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>

    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/laydate/need/laydate.css"/>
    <link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/static/dzd/themes/timepicki.css" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
</head>
<style>
    #add_button {
          border: none;
    border-radius: 4px;
    position: relative;
    top: -2px;
    background: #4284DA;
    color: #FFFFFF;
    width: 62px;
    height: 22px;
    font-size: 12px;
    line-height: 20px;
    padding: 0 8px;
    text-align: center;
    }
</style>
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
<script src="${ctx}/static/js/appml5shiv.js"></script>
<script src="${ctx}/static/js/applectivizr.js"></script>
<style>
    .modal-dialog {
        width: 686px;
    }
</style>
<![endif]-->
<body>

<div>
    <div class="com-content">
        <div class="com-menu inp-menu" style="text-align:center;">

            <input type="hidden" id="menu_id" value="${menuId}"/>


            <div class="inp-box ">
                <span>通道组： </span> <input class='inp-box-inp' id="user_input"
                                          type="text" placeholder=""/>
            </div>


            <c:if test="${num==1}">
                <input type="hidden" class="form-control" value="${sysUser.id}"
                       id="s_bid" name="bid"/>
                <input type="hidden" class="form-control"
                       value="${sysUser.nickName}" id="s_userName"/>
            </c:if>
            <div class="inp-box inpt-box-btn">
                <span class="inp-title"> 时间：</span> <input id="start_input" class='inp-box-inp'
                                                           type="text" placeholder=""/> <span class="dataline">至 </span>
                <input class='inp-box-inp'
                       id="end_input" type="text" placeholder=""/>

            </div>
            <div class="inp-box ">
                <span> 状态：</span> <select id="state_select">
                <option value="">请选择</option>
                <option key="1" value="1">启用</option>
                <option key="2" value="2">停用</option>
            </select>
            </div>
            <div class="inp-box ">
                <input class="f-btn" value="查询" type="button" id="search_btn" style="margin-left: 10px"/>
            </div>
            <div class="inp-box" id="addTdzDiv"></div>
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

<div class="modal fade" id="add_data_div">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="h4_title">通道组管理</h4>
            </div>
            <div class="modal-body">
                <form id="upload_form" method="post"
                      class="form-horizontal required-validate"
                      action="${ctx}/channel/from/groupmerge.do?menuId=${menuId}"
                      enctype="application/x-www-form-urlencoded">

                    <input type="hidden" class="form-control" id="i_id" name="id">
                    <div class="model-cont channegroup">
                        <div class="inp-box">
                            <span> 通道名称：</span> <input class='inp-box-inp model-inp' id="i_name" name="name"
                                                       type="text">

                            <span>长短信计费：</span><input class='inp-box-inp model-inp' id="smsLength" name="smsLength"
                                                      onkeyup="value=value.replace(/[^\d]/g,'')"
                                                      type="text"/> 字/条

                        </div>

                        <div class="inp-box">
                            <span>开通时段：</span> <input class='inp-box-inp model-inp' id="dredgeAM" name="dredgeAM"
                                                      type="text"/>
                            <input class='inp-box-inp model-inp' id="dredgePM" name="dredgePM"
                                   type="text"/><span style="color:#fd0000">注:不填写时间则为24小时开通</span>

                        </div>
                        <div class="inp-box">
                            <span>敏感词类型：</span>
                            <input type="hidden" id="shieldingFieldId" value="0"
                                   name="shieldingFieldId"/> <input class="model-check" type="checkbox" id="mgc_ty"
                                                                    name="td_close" checked="checked"
                                                                    disabled="disabled">通用敏感词
                            <input class="model-check" type="checkbox" id="mgc_ts" name="td_open">特殊敏感词
                            <span>退订格式：</span>
                            <input type="hidden" id="unregTypeId" name="unregTypeId" value="0">
                            <input class="model-check" type="checkbox" id="td_close" name="td_close" checked="checked">关
                            <input class="model-check" type="checkbox" id="td_open" name="td_close">开
                        </div>


                        <div class="inp-box">
                            <span>通道状态：</span>
                            <select id="i_state" name="state">
                                <option key="1" value="1">启用</option>
                                <option key="2" value="2">停用</option>
                            </select>
                            <span>通道类型：</span>
                            <select id="i_aisleType"
                                    name="aisleType">
                                <option value="">请选择</option>
                                <c:forEach var="item" items="${typeList}" varStatus="status">
                                    <option key="${item.id}" value="${item.id}">${item.name}</option>
                                </c:forEach>
                            </select>

                        </div>


                        <div class="inp-box" style="text-align:right;width:468px">
                            <span>售价设定：</span>
                            <input class='inp-box-inp model-inp'
                                   onkeyup="value=value.replace(/[^\d(\.{0,1}\d{0,4})]/g,'')"
                                   id="i_oneIntervalPrice" name="oneIntervalPrice"
                                   type="text"/>元/条
                            <input class='inp-box-inp model-inp'
                                   onkeyup="value=value.replace(/[^\d(\.{0,1}\d{0,4})]/g,'')"
                                   id="i_oneIntervalStart" name="oneIntervalStart"
                                   type="text"/>&lt;=购买条数&lt;
                            <input class='inp-box-inp model-inp'
                                   onkeyup="value=value.replace(/[^\d(\.{0,1}\d{0,4})]/g,'')"
                                   id="i_oneIntervalEnd" name="oneIntervalEnd"
                                   type="text"/>
                            <br/>
                            <input class='inp-box-inp model-inp'
                                   onkeyup="value=value.replace(/[^\d(\.{0,1}\d{0,4})]/g,'')"
                                   id="i_twoIntervalPrice" name="twoIntervalPrice"
                                   type="text"/>元/条
                            <input class='inp-box-inp model-inp'
                                   onkeyup="value=value.replace(/[^\d(\.{0,1}\d{0,4})]/g,'')"
                                   id="i_twoIntervalStart" name="twoIntervalStart"
                                   type="text"/>&lt;=购买条数&lt;
                            <input class='inp-box-inp model-inp'
                                   onkeyup="value=value.replace(/[^\d(\.{0,1}\d{0,4})]/g,'')"
                                   id="i_twoIntervalEnd" name="twoIntervalEnd"
                                   type="text"/>
                            <br/>
                            <input class='inp-box-inp model-inp'
                                   onkeyup="value=value.replace(/[^\d(\.{0,1}\d{0,4})]/g,'')"
                                   id="i_threeIntervalPrice" name="threeIntervalPrice"
                                   type="text"/>元/条
                            <input class='inp-box-inp model-inp'
                                   onkeyup="value=value.replace(/[^\d(\.{0,1}\d{0,4})]/g,'')"
                                   id="i_threeIntervalStart" name="threeIntervalStart"
                                   type="text"/>&lt;=购买条数&lt;
                            <input class='inp-box-inp model-inp'
                                   onkeyup="value=value.replace(/[^\d(\.{0,1}\d{0,4})]/g,'')"
                                   id="i_threeIntervalEnd" name="threeIntervalEnd"
                                   type="text"/>
                            <br/>
                        </div>
                        <div class="inp-box">
                            <span>计费模式：</span>
                            <input name="succeedBilling" type="checkbox"
                                   id="i_succeedBilling" value="1"/><span
                                style="padding-right: 20px;">成功计费</span> <input
                                name="failureBilling" type="checkbox" id="i_failureBilling"
                                value="1"/><span style="padding-right: 20px;">失败计费</span> <input
                                name="unknownBilling" type="checkbox" id="i_unknownBilling"
                                value="1"/><span>未知计费</span>
                        </div>
                        <br class="iebr"/>

                        <div class="inp-box">
                            <span>描述：</span> <input class='inp-box-inp model-inp' id="i_awardMoney" name="awardMoney"
                                                    type="text">
                        </div>
                        <br class="iebr"/>
                        <div class="inp-box">
                            <span>公告：</span> <input class='inp-box-inp model-inp' id="notice" name="notice"
                                                    type="text">
                        </div>
                        <br class="iebr"/>
                        <div class="inp-box">
                            <span>提示：</span> <input class='inp-box-inp model-inp' id="hint" name="hint"
                                                    type="text">
                        </div>
                        <br class="iebr"/>
                        <div class="inp-box">
                            <span>签名：</span> <input class='inp-box-inp model-inp' id="signature" name="signature"
                                                    type="text">
                        </div>
                    </div>


                    <hr/>
                    <div class="model-cont channegroup">
                        <div id="smsAisleInfoDiv">
                            <table style="margin-left: 10%">
                                <tr>
                                    <td style="width: 112px; font-weight: bold;">运营商</td>
                                    <td style="width: 112px; font-weight: bold;">通道名称</td>
                                </tr>
                                <tr>
                                    <td style="width: 112px; ">
                                        <input type="checkbox" name="operatorId" value="0"> 联通
                                    </td>
                                    <td style="width: 112px; ">
                                        <select class="aisleSelect" name="statetype"></select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 112px;">
                                        <input type="checkbox" name="operatorId" value="1"> 移动
                                    </td>
                                    <td style="width: 112px; ">
                                        <select class="aisleSelect" name="statetype"></select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 112px; ">
                                        <input type="checkbox" name="operatorId" value="2"> 电信
                                    </td>
                                    <td style="width: 112px; ">
                                        <select class="aisleSelect" name="statetype"></select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 112px; ">
                                        <input type="checkbox" name="operatorId" value="-1"> 未知
                                    </td>
                                    <td style="width: 112px; ">
                                        <select class="aisleSelect" name="statetype"></select>
                                    </td>
                                </tr>
                            </table>
                            <table style="margin-left: 10%" id="aisledata">

                            </table>
                        </div>
                    </div>
                    <div class="modal-footer" style="margin-top:10px">
                     <input type="button" class="f-btn" id="save_data_btn" value="确 定">

                    <input type="button" class="f-btn "  data-dismiss="modal" id="cancel_btn"  value="取 消">
                       
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade"  id="del">
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
                <span class="aw-new">确认是否删除？</span>
            </p>

            <div class="modal-footer">
                <input type="button" class="f-btn" id="btn_del" value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">

            </div>
        </div>
    </div>
</div>

<input type="hidden" id="server_path" value="${ctx}"/>
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<script src="${ctx}/static/js/commons/common.js"></script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/dzd/timepicki.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/dzd/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>

<script src="${ctx}/static/js/channel/channelGrouplist.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>