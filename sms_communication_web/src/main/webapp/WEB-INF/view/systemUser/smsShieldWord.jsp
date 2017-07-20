<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
    <link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
    <link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <link href="${ctx}/static/bower_components/bootstrap/dist/css/jquery.bootstrap.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
</head>
<body>

<div>
    <input type="hidden" value="${menuId}"/>

    <div class="com-content">

        <div class="com-menu inp-menu " style="text-align:center;">
            <div class="inp-box">
                <span>类型：</span>
                <select class="province select" id="typeId">
                    <option key="" value="">请选择</option>
                    <option key="0" value="0">通用敏感词</option>
                    <option key="1" value="1">特殊敏感词</option>
                </select>
            </div>
            <div class="inp-box">
                <span>等级：</span>
                <select class="province select" id="levelId">
                    <option key="" value="">请选择</option>
                    <option key="1" value="1">一级敏感词</option>
                    <option key="2" value="2">二级敏感词</option>
                </select>
            </div>
            <div class="inp-box">
                <span>屏蔽词：</span>
                <input class=' inp-box-inp' id="user_input" type="text" placeholder="">
            </div>
            <div class="inp-box">
                <input id="search_btn" class="f-btn" type="button" class="f-btn" value="查询">
                <a href="javaScript:void(0)" id="add_data">
                    <input id="add_button" type="button" class="f-btn" value="新增">
                </a>
                <a href="javaScript:void(0)" id="delete_selected" style="display: none;">
                    <input id="del_button" type="button" class="f-btn" value="删除">
                </a>
            </div>
            <input type="hidden" id="menu_id" value="${menuId}"/>


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
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="h4_title">屏蔽词管理</h4>
            </div>
            <div class="modal-body">
                <form id="upload_form" method="post" class="form-horizontal required-validate"
                      action="${ctx}/wordShielding/from/addShieldWord.do?menuId=${menuId}"
                      enctype="application/x-www-form-urlencoded">

                    <input type="hidden" class="form-control" id="i_id" name="id">

                    <div class="form-group">
                        <label class="col-sm-3 control-label">属性</label>
                        <div class="col-sm-5" style="position: relative;left: -60px;">
                            <select class="province select" id="i_type" name="type">
                                <option key="0" value="0">通用敏感词</option>
                                <option key="1" value="1">特殊敏感词</option>
                            </select><span style="color:#FF0000;">（必选）</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">等级</label>
                        <div class="col-sm-5" style="position: relative;left: -60px;">
                            <select class="province select" id="level" name="level">
                                <option key="1" value="1">一级敏感词</option>
                                <option key="2" value="2">二级敏感词</option>
                            </select><span style="color:#FF0000;">（必选）</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">屏蔽词</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" id="i_wordName" name="wordName"/>
                        </div>

                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label"></label>
                        <div class="col-sm-5" style="width: 420px;text-align: left;">
                            <span>注: 一级敏感词，强类型，敏感词中间不允许加符号或空格</span><br>
                            <span style="margin-left: 18px;">二级敏感词，弱类型，敏感词中间允许加符号或空格</span>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal" id="cancel_btn">取 消</button>
                        <button type="button" class="btn btn-primary" id="save_data_btn">确 定</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="del">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="del_h4_title">提示</h4>
            </div>
            <div class="modal-body">
                <p id="del_p_title">
                    删除后不可恢复,确认删除吗?
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取
                    消
                </button>
                <button type="button" class="btn btn-primary" id="btn_del">确
                    定
                </button>
            </div>
        </div>
    </div>
</div>

<!-- Custom Theme JavaScript -->
<input type="hidden" id="server_path" value="${ctx}"/>
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<script src="${ctx}/static/js/commons/common.js"></script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/systemUser/smsShieldWord.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>