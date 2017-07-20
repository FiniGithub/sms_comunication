<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">

    <link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">


    <link href="${ctx}/static/bower_components/bootstrap/dist/css/jquery.bootstrap.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/laydate/need/laydate.css"/>
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


            <div class="inp-box">
                <span> 通道：</span> <input class='inp-box-inp' id="user_input"
                                         type="text">
            </div>


            <c:if test="${num==1}">
                <input type="hidden" class="form-control" value="${sysUser.id}"
                       id="s_bid" name="bid"/>
                <input type="hidden" class="form-control"
                       value="${sysUser.nickName}" id="s_userName"/>
            </c:if>
            <div class="inp-box inpt-box-btn">
                <span class="inp-title"> 时间：</span> <input id="start_input" class='inp-box-inp'
                                                           type="text" placeholder=""/><span class="dataline"> 至 </span><input
                    class='inp-box-inp'
                    id="end_input" type="text" placeholder=""/>
            </div>
            <div class="inp-box ">
                <span> 状态：</span> <select id="state_select">
                <option value="">请选择</option>
                <option key="1" value="1">启用</option>
                <option key="2" value="2">停用</option>
                <option key="3" value="3">自动停用</option>
            </select>
            </div>
            <div class="inp-box ">
                <input class="f-btn" value="查询" type="button" id="search_btn" style="margin-left: 10px"/>
            </div>

            <!-- 新增按钮 -->
            <div class="inp-box " id="addTdDiv"></div>

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
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="h4_title">通道管理</h4>
            </div>
            <div class="modal-body">
                <form id="upload_form" method="post"
                      class="form-horizontal required-validate"
                      action="${ctx}/channel/from/merge.do?menuId=${menuId}"
                      enctype="application/x-www-form-urlencoded">
                    <div class="model-cont">


                        <input type="hidden" class="form-control" id="i_id" name="id">
                        <div class="inp-box">
                            <span> 通道名称：</span> <input class='inp-box-inp model-inp' id="i_name" name="name"
                                                       type="text">
                            <span>通道状态：</span> <select class="province select" id="i_state" name="state">
                            <option value="">请选择</option>
                            <option key="1" value="1">启用</option>
                            <option key="2" value="2">停用</option>
                            <option key="3" value="3">自动停用</option>
                        </select>
                            <span>通道类型：</span>
                            <select class="province select" id="i_aisleType"
                                    name="aisleType">
                                <option value="">请选择</option>
                                <c:forEach var="item" items="${typeList}" varStatus="status">
                                    <option key="${item.id}" value="${item.id}">${item.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="inp-box">
                            <span>每日短信上限数：</span> <input class='inp-box-inp model-inp' id="i_maxNum" name="maxNum"
                                                         onkeyup="value=value.replace(/[^\d]/g,'')"
                                                         type="text"/>
                            <span>分包数：</span> <input class='inp-box-inp model-inp' id="i_singleNum" name="singleNum"
                                                        onkeyup="value=value.replace(/[^\d]/g,'')"
                                                        type="text"/>
                            <span>起发量：</span> <input class='inp-box-inp model-inp' id="startCount" name="startCount"
                                                     onkeyup="value=value.replace(/[^\d]/g,'')"
                                                     type="text"/>
                        </div>
                        <br class="iebr"/>
                        <div class="inp-box">
                            <span>描述：</span> <input class='inp-box-inp model-inp' id="i_awardMoney" name="awardMoney"
                                                    type="text">
                        </div>
                        <br class="iebr"/>


                        <div class="inp-box">
                            <span>通道成本：</span>

                            <input type="text" id="money" name="money"/>元/条
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


                        <!-- 	<div class="form-group">
                        <label for="apk_version" class="col-sm-3 control-label">通道成本价</label>
                        <div class="col-sm-5">
                            <input type="text" style="width:130px;" class="form-control" id="i_money" name="money"/>
                        </div>
                    </div> -->


                        <div class="inp-box">
                            <span>通道公司名称：</span>
                            <select class="province select" id="i_smsAisleKey"
                                    name="smsAisleKey">
                                <option value="">请选择</option>
                                <c:forEach var="item" items="${sasList}" varStatus="status">
                                    <option key="${item.smsAisleKey}"
                                            value="${item.smsAisleKey}">${item.smsAisleName}</option>
                                </c:forEach>
                            </select><span style="color: #FF0000;">（必选）</span>
                        </div>
                        <br class="iebr"/>
                        <div class="inp-box">
                            <span>通道可发省份：</span> <select class="province select" id="i_smsRegion"
                                                         name="smsRegion">
                            <option value="-1">全国</option>
                            <c:forEach var="item" items="${smsRegionList}"
                                       varStatus="status">
                                <option key="${item.id}" value="${item.id}">${item.districtName}</option>
                            </c:forEach>
                        </select>
                            <span>签名：</span>
                            <select class="province select" id="i_signatureState"
                                    name="signatureState">
                                <option value="">请选择</option>
                                <option key="1" value="1">前置</option>
                                <option key="2" value="2">后置</option>
                            </select>
                        </div>
                    </div>


                    <hr/>
                    <div class="model-cont">
                        <div id="aisledata">
                            <!-- <div class="form-group">
                        <label for="apk_version" class="col-sm-3 control-label">通道成本价</label>
                        <div class="col-sm-5">
                            <input type="text" style="width:130px;" class="form-control" id="i_money" name="money"/>
                        </div>
                        </div> -->
                        </div>
                        <input type="hidden" name="extra" id="extra" value=""/>
                    </div>
                    <div class="modal-footer" style="margin-top:10px">
                      <input type="button" class="f-btn" id="save_data_btn" value="确 定"/>
               		 <input type="button" class="f-btn" id="cancel_btn" data-dismiss="modal" value="取 消">
                       
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

<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>

<script src="${ctx}/static/dzd/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/js/channel/channellist.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>