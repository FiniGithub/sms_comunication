<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset='utf-8'>
	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fileinput.css">
    <link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">

    <script src="${ctx}/static/js/jquery.datetimepicker.js"></script>

	<style>
		.select {
			background-color: #fff;
			background-position: right center;
			background-repeat: no-repeat;
			border: 1px solid #888;
			border-radius: 3px;
			box-sizing: border-box;
			font: 12px/1.5 Tahoma,Arial,sans-serif;
			height: 32px;
			padding: 0 0.5em;
		}
		.input_css_line{
			width: 30%;border:1px solid #ccc;border-radius:4px;padding:6px 12px;
		}
		td{
			height: 50px;
		}
	</style>
</head>
<body>

<div>
	<div class="com-title">
		<h4>用户发送任务管理</h4>
		<div class="title-remark">查看发送任务相关的信息，点击导出可导出任务发送号码
			并查询发送详情等操作。
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu">
			<input type="hidden" id="menu_id" value="${menuId}" />
					<form id="allotmerge_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/userMsmSend/smsSendDetails.do?menuId=${menuId}" enctype="application/x-www-form-urlencoded">
					  		<input class="ids_input"  id="ids_dis" name="ids" type="hidden"/>
					  </form>
			
			 <span style="float:left;margin-right:25px;margin-top: 1.5px">
                  <div style="position: relative;">
			                               提交时间： <input id="start_input" type="text" />
			                               -<input id="end_input" type="text" />
			                 <button class="btn btn-warning" id="reset" type="button">重置</button>
                </div>
            </span>
            <span style="float:left;margin-right:25px;margin-top: 1.5px">
           	 	<div style="position: relative;">
                 	批次编号：	<input class="ids_input"  id="ids_input" type="text" placeholder="" />
               </div>  		
            </span>
               <span style="float:left;margin-right:25px;margin-top: 1.5px">
                <div style="position: relative;">
			                               发送内容： <input class="btn_input"  id="content_input" type="text" placeholder="" />
               		   <button class="btn btn-info" type="button" id="search_btn" style="margin-left: 50px;">
						<span class="glyphicon glyphicon-search"></span></button>
                </div>
            </span>
             <span style="float:left;margin-right:15px;margin-top: 1.5px">
                	发送状态：
                <select id="state_select">
                    <option value="">请选择</option>
                    <option value="-1">等待发送</option>
                    <option value="0">正在发送</option>
                    <option value="9">终止发送</option>
                    <option value="100">发送完成</option>
                    <option value="101">已结算</option>
                </select>
            </span>
			<div id="data_div" style="width: 60%"></div>
			</br>
			</br>
			</br>
			<div class="com-menu" style="color: #FD0000;">
				<span>发送号码数：</span>
				<span id="i_sendNumCount" style="padding-right:20px;">0</span>
				<span>计费条数：</span>
				<span id="i_billingNumCount" style="padding-right:20px;">0</span>
				<span>结算条数：</span>
				<span id="i_actualNumCount">0</span>
			</div>
		</div>
		<div class="row">
			<table id="tb_data"></table>
		</div>
	</div>
</div>


<div class="modal fade" id="del">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="del_h4_title">确定推荐</h4>
			</div>
			<div class="modal-body">
				<p id="del_p_title">
					确认批量推荐?
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
<script type="text/javascript">
function download(id){
    var url="${ctx}/userMsmSend/export/csv.do?id="+id;
    window.open(url);
}
</script>
<script type="text/javascript" src="${ctx}/static/dzd/jquery-1.4.2.min.js"></script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/userSmsManage/msmSend.js"></script>
</body>
</html>