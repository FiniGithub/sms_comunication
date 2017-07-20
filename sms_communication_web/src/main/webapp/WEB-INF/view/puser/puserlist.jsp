<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset='utf-8'>
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<!-- Bootstrap Core CSS -->
<link
	href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- MetisMenu CSS -->
<link
	href="${ctx}/static/bower_components/metisMenu/dist/metisMenu.min.css"
	rel="stylesheet">
<!-- Custom CSS -->
<link href="${ctx}/static/dist/css/sb-admin-2.css" rel="stylesheet">
<!-- Custom Fonts -->
<link
	href="${ctx}/static/bower_components/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">

<script src="${ctx}/static/js/commons/common.js"></script>
<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/fileinput.css">
<link href="${ctx}/static/css/jquery.datetimepicker.css"
	rel="stylesheet">
<script src="${ctx}/static/js/jquery.datetimepicker.js"></script>

<style>
.select {
	background-color: #fff;
	background-position: right center;
	background-repeat: no-repeat;
	border: 1px solid #888;
	border-radius: 3px;
	box-sizing: border-box;
	font: 12px/1.5 Tahoma, Arial, sans-serif;
	height: 32px;
	padding: 0 0.5em;
}

.input_css_line {
	width: 30%;
	border: 1px solid #ccc;
	border-radius: 4px;
	padding: 6px 12px;
}

td {
	height: 50px;
}
</style>
</head>
<body>

	<div>
		<div class="com-title">
			<h4>客户列表</h4>
			<div class="title-remark">查看代理相关的信息， 点击添加可以添加代理， 并对代理进行编辑等操作。</div>
			<hr />
		</div>
		<div class="com-content">
			<div class="com-menu inp-menu">
				<div class="inp-box" id="addSmsUsers"></div>
				<input type="hidden" value="${ctx}" id="server_path" /> <input
					type="hidden" id="menu_id" value="${menuId}" />

				<div class="inp-box ">
					<span> 状态：</span> <select id="state_select">
						<option value="">请选择</option>
						<option value="0">启用</option>
						<option value="1">禁用</option>
					</select>
				</div>
				<c:if test="${tdUserList!=null}">
					<div class="inp-box ">
						<span> 团队：</span> <select id="td_select">
							<option value="">请选择</option>
							<c:forEach var="item" items="${tdUserList}" varStatus="status">
								<option key="${item.id}" value="${item.id}">${item.nickName}</option>
							</c:forEach>
						</select>
					</div>
				</c:if>
				<div class="inp-box ">
					<span class="inp-title"> 代理账号：</span> <input class="btn_input"
						id="user_input" type="text" placeholder="请输入账号">
				</div>

				<c:if test="${num==1}">
					<input type="hidden" class="form-control" value="${sysUser.id}"
						id="s_bid" name="bid" />
					<input type="hidden" class="form-control"
						value="${sysUser.nickName}" id="s_userName" />
				</c:if>
				<div class="inp-box inpt-box-btn">
					<span class="inp-title"> 创建时间：</span> <input id="start_input"
						type="text" placeholder="年/月/日" /> <span class="dataline">—</span><input
						id="end_input" type="text" placeholder="年/月/日" />
					<button class="btn btn-warning" id="reset" type="button">重置</button>

					<button class="btn btn-info" type="button" id="search_btn">
						<span class="glyphicon glyphicon-search"></span>
					</button>
				</div>
				<div id="data_div"></div>
			</div>

			<div class="com-menu" style="color: #FD0000;">
				<span>合计剩余条数：</span> <span id="i_statistical">0</span>
			</div>
			<div class="row">
				<table id="tb_data"></table>
			</div>
		</div>
	</div>

	<form id="indexdl_merge_form" method="post"
		class="form-horizontal required-validate"
		action="${ctx}/puser/from/indexdl.do?menuId=${menuId}"
		enctype="application/x-www-form-urlencoded">
		<input type="hidden" class="form-control" id="index_id"
			name="indexUid" />

	</form>


	<div class="modal fade" id="add_data_div">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="h4_title">代理管理</h4>
				</div>
				<div class="modal-body">
					<form id="upload_form" method="post"
						class="form-horizontal required-validate"
						action="${ctx}/puser/from/merge.do?menuId=${menuId}"
						enctype="application/x-www-form-urlencoded">
						<div class="form-group">
							<label for="apk_description" class="col-sm-3 control-label">账号归属</label>
							<input type="hidden" id="i_id" name="id" /> <input type="hidden"
								id="sysUserId" name="sysUserId" /> <input type="hidden"
								id="smsUserBlankId" name="smsUserBlankId" />
							<div class="col-sm-5">
								<c:if test="${num==0}">
									<select class="province select" id="i_bid" name="bid">
										<option value="">请选择</option>
										<c:forEach var="item" items="${sysUserList}"
											varStatus="status">
											<option key="${item.id}" value="${item.id}">${item.nickName}</option>
										</c:forEach>
									</select>
								</c:if>
								<c:if test="${num==1}">
									<input type="hidden" class="form-control" value="${sysUser.id}"
										id="i_bid" name="bid">
									<input type="text" class="form-control" disabled="disabled"
										value="${sysUser.nickName}" id="i_userName">
								</c:if>
							</div>
						</div>

						<div class="form-group">
							<label for="apk_name_id" class="col-sm-3 control-label">客户名称</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="i_name" name="name">
							</div>
						</div>
						<div class="form-group">
							<%-- <label for="apk_name_id" class="col-sm-3 control-label">级别</label>
						<div class="col-sm-5">
							<select class="province select" id="i_level" name="level">
									<option value="">请选择</option>
									<c:forEach var="item" items="${levelList}" varStatus="status">   
	    								<option key="${item.id}" value="${item.id}" >${item.name}</option>
									</c:forEach> 
							</select> --%>

							<label for="apk_name_id" class="col-sm-3 control-label">状态</label>
							<div class="col-sm-5">
								<select class="province select" id="i_state" name="state">
									<option key="0" value="0">启用</option>
									<option key="2" value="2">停用</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="apk_version" class="col-sm-3 control-label">账户</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="i_userEmail"
									name="userEmail">
							</div>
						</div>
						<div class="form-group">
							<label for="apk_version" class="col-sm-3 control-label">账户类型</label>
							<div class="col-sm-5">
								<select class="province select" id="i_aisleType"
									name="aisleType">
									<option value="">请选择</option>
									<c:forEach var="item" items="${typeList}" varStatus="status">
										<option key="${item.id}" value="${item.id}">${item.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>


						<div class="form-group">
							<label for="apk_version" class="col-sm-3 control-label">所用通道组</label>
							<div class="col-sm-5">
								<select class="province select" id="i_aisleGroup"
									name="aisleGroup">
									<option value="">请选择</option>

								</select>
							</div>
						</div>

						<!-- 					<div class="form-group">
						<label for="apk_version" class="col-sm-3 control-label">密码</label>
						<div class="col-sm-5">
							<input type="password" class="form-control" id="i_userPwd" name="userPwd">
						</div>
					</div> -->

						<!-- 					<div class="form-group">
						<label for="apk_version" class="col-sm-3 control-label">预存金</label>
						<div class="col-sm-5">
							<input type="text"  style="width:100px; float: left;" class="form-control" id="i_money" name="money"/>
							
							
							<input type="text" style="width:100px;float: right;" class="form-control" id="i_awardMoney" name="awardMoney"/>
							<label for="apk_version" class="control-label" style="float: right;margin-right:20px;">授信额度</label>
						
						</div>
					</div> -->

						<div class="form-group">
							<label for="apk_version" class="col-sm-3 control-label">电话</label>
							<div class="col-sm-5">
								<input type="text" style="width: 180px;" class="form-control"
									id="i_phone" name="phone" />
							</div>
						</div>

						<div class="form-group">
							<label for="apk_description" class="col-sm-3 control-label">描述</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="i_describes"
									name="describes">
							</div>
						</div>
						<!-- 					<div class="form-group">
						<label for="apk_description" class="col-sm-3 control-label">签名</label>
						<div class="col-sm-5">
							<input type="textarea" style="float: left;" class="form-control" id="i_signature" name="signature">
							<input type='checkbox' style="float: left;" name='checkboxs' id="statetype" value="1"/>
							<span style="color: #F00000;">签名不校验</span>
							<span style="color: #2A00FF;">
							***多个签名时用","间隔
							</span>
						</div>
					</div> -->
						<div class="form-group">
							<label for="apk_description" class="col-sm-3 control-label">上行推送地址</label>
							<div class="col-sm-5">
								<input type="textarea" class="form-control" id="i_replyUrl"
									name="replyUrl"><span style="color: #2A00FF;"> </span>
							</div>
						</div>
						<div class="form-group">
							<label for="apk_description" class="col-sm-3 control-label">报告推送地址</label>
							<div class="col-sm-5">
								<input type="textarea" class="form-control" id="i_reportUrl"
									name="reportUrl"><span style="color: #2A00FF;">
								</span>
							</div>
						</div>
						<div class="form-group">
							<label for="apk_description" class="col-sm-3 control-label">协议支持</label>
							<div class="col-sm-5">
								<input name="httpProtocol" type="checkbox" id="i_httpProtocol"
									value="1" />http协议 <input name="cmppProtocol" type="checkbox"
									onclick="cmppCheckbox()" id="i_cmppProtocol" value="1" />cmpp协议
							</div>
						</div>
						<div id="aisledata" style="display: none;">
							<div class="form-group">
								<hr />
								<label for="apk_description" class="col-sm-3 control-label">通道接入码</label>
								<div class="col-sm-5">
									<input type="textarea" class="form-control" id="i_joinupCoding"
										name="joinupCoding">
								</div>
							</div>
							<div class="form-group">
								<label for="apk_description" class="col-sm-3 control-label">企业用户名</label>
								<div class="col-sm-5">
									<input type="textarea" style="width: 100px; float: left;"
										class="form-control" id="i_firmName" name="firmName">


									<input type="textarea" style="width: 150px; float: right;"
										class="form-control" id="i_firmPwd" name="firmPwd"> <label
										style="float: right; margin-right: 20px;"
										for="apk_description" class="control-label">密码</label>
								</div>
							</div>
							<div class="form-group">
								<label for="apk_description" class="col-sm-3 control-label">最大连接数</label>
								<div class="col-sm-5">
									<input type="textarea" class="form-control"
										onkeyup="value=value.replace(/[^-\d]/g,'')" id="i_joinuoMax"
										name="joinuoMax">
								</div>
							</div>
							<div class="form-group">
								<label for="apk_description" class="col-sm-3 control-label">企业IP</label>
								<div class="col-sm-5">
									<input type="textarea" class="form-control" id="i_firmIp"
										name="firmIp">
								</div>
							</div>
							<div class="form-group">
								<label for="apk_description" class="col-sm-3 control-label">默认通道组编号</label>
								<div class="col-sm-5">
									<input type="textarea" class="form-control" id="i_defaultAgid"
										name="defaultAgid">
								</div>
							</div>
						</div>

						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal" id="cancel_btn">取 消</button>
							<button type="button" class="btn btn-primary" id="save_data_btn">确
								定</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="je_data_div">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="h4_title">充值短信数量</h4>
				</div>
				<div class="modal-body">
					<form id="cz_merge_form" method="post"
						class="form-horizontal required-validate"
						action="${ctx}/puser/from/moneyMerge.do?menuId=${menuId}"
						enctype="application/x-www-form-urlencoded">
						<input type="hidden" style="width: 100px;" class="form-control"
							id="i_czid" name="czid" />
						<div class="form-group">
							<table style="margin-left: 10%">
								<label for="apk_name_id" class="col-sm-3 control-label">账户名称</label>
								<div class="col-sm-5">
									<input type="text" class="form-control" disabled="disabled"
										id="i_czname">
								</div>

							</table>
						</div>
						<div class="form-group">
							<table style="margin-left: 10%">
								<label for="apk_name_id" class="col-sm-3 control-label">剩余条数</label>
								<div class="col-sm-5">
									<input type="text" class="form-control" disabled="disabled"
										id="i_symoney" name="symoney">
								</div>

							</table>
						</div>
						<div class="form-group">
							<table style="margin-left: 10%">
								<label for="apk_name_id" class="col-sm-3 control-label">添加条数</label>
								<div class="col-sm-5">
									<input type="text" class="form-control"
										onkeyup="value=value.replace(/[^-\d]/g,'')" id="i_czmoney"
										name="czmoney">
								</div>

							</table>

						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal" id="cancel_btn">取 消</button>
							<button type="button" class="btn btn-primary" id="cz_allot_btn">确
								定</button>
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
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="del_h4_title">确定推荐</h4>
				</div>
				<div class="modal-body">
					<p id="del_p_title">确认批量推荐?</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取
						消</button>
					<button type="button" class="btn btn-primary" id="btn_del">确
						定</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Custom Theme JavaScript -->
		<script src="${ctx}/static/js/commons/jquery.bootstrap.js"></script>
	<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
	<script src="${ctx}/static/js/commons/welcome.js"></script>
	<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
	<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
	<script src="${ctx}/static/js/fileinput.js"></script>
	<script src="${ctx}/static/js/puser/puserlist.js"></script>
</body>
</html>