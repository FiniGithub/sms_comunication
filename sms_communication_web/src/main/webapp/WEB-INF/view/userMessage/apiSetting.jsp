<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>welcome</title>
	<style>
	
	.onetable td{
			height: 50px;
			width: 150px;
			font-size: 18px;
		}
		
	.woetTable td{border:solid #A9A9A9;
		border-width:0px 1px 1px 0px; 
		padding:10px 0px;
		text-align:center;
		width: 150px;
		height: 50px;
	}
	 .woetTable{
		border:solid #A9A9A9;
		border-width:1px 0px 0px 1px;
	}
	</style>
</head>
<body>
	<div class="com-title">
			<h4>AIP配置</h4>
			<div class="title-remark"></div>
			<hr/>
	</div>


	<div class="com-content">
			<table style="margin-top:50px;" class="onetable" align="center" >
				<tr>
					<td >
						企业ID:
					</td>
					<td>${smsUsers.id}
						<input type="hidden" value="${smsUsers.id}" id="smsUserId"/>
					</td>
					
				</tr>
				<tr>
					<td >
						KEY:
					</td>
					<td>
						<span id="smsUserKey">${smsUsers.key}</span>
					</br>
					<c:if test="${smsUsers.key==null || smsUsers.key==''}">
						<span id="a_key"><a href="javascript:sbmitQueryKey(1);"  style="color: blue;">获取KEY</a></span>
					</c:if>
					<c:if test="${smsUsers.key!=null && smsUsers.key!=''}">
						<a href="javascript:czDataDiv();"  style="color: blue;">重置KEY</a>
					</c:if>
					</td>
				</tr>
				<tr>
					<td >
						推送地址:
					</td>
					<td>
						<input type="button" class="form-control"  onclick="tsDateDiv()" value="填写/修改" />
					</td>
				</tr>
				<tr>
					<td >
						上行推送地址:
					</td>
					<td>
						<span id="td_replyUrl">
							<c:if test="${smsUsers.replyUrl == null || smsUsers.replyUrl==''}">未填写</c:if>
							<c:if test="${smsUsers.replyUrl !=null && smsUsers.replyUrl!=''}">${smsUsers.replyUrl}</c:if>
						</span>
						</td>
				</tr>
				<tr>
					<td >
						报告推送地址:
					</td>
					<td>
						<span id="td_reportUrl">
							<c:if test="${smsUsers.reportUrl ==null || smsUsers.reportUrl==''}">未填写</c:if>
							<c:if test="${smsUsers.reportUrl !=null && smsUsers.reportUrl!=''}">${smsUsers.reportUrl}</c:if>
						</span>
					</td>
				</tr>
				<tr>
					<td>
						接口文档:
					</td>
					<td>
					<a class="form-control" href="${pluginPath}/calim/api_manual.docx" style="text-align: center;text-decoration: none;">下载</a>
					</td>
				</tr>
			</table>
	</div>
	



<div class="modal fade" id="cz_data_div">
	<div class="modal-dialog modal-sm">
		<div class="modal-content" style="width: 520px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="del_h4_title">手机验证码</h4>
			</div>
			<div class="modal-body">
				<div class="modal-body">
				<form id="cz_merge_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/puser/from/moneyMerge.do?menuId=${menuId}" enctype="application/x-www-form-urlencoded">
					<input type="hidden" style="width:100px;" class="form-control" id="i_czid" name="czid"/>
					<div class="form-group">
						<table style="margin-left: 10%">
							<label for="apk_name_id" style="width: 150px;" class="col-sm-3 control-label">接收验证码手机号</label>
							<div class="col-sm-5">
								<span  class="form-control" style="width: 150px;">${smsUsers.phone}</span>
							</div>
							
						</table>
					</div>
					<div class="form-group">
						<table style="margin-left: 10%">
							<label for="apk_name_id" style="width: 150px;" class="col-sm-3 control-label">验证码</label>
							<div class="col-sm-5" style="width: 300px;">
								<input type="hidden"  id="i_phone" value="${smsUsers.phone}" >
								<input type="text" style="width: 150px;float: left;" class="form-control"   id="i_key">
								<input type="button" class="btn" style="float: left;" id="key_btn" value="获取验证码" onclick="settime(this)" /> 
							</div>
							
						</table>
					</div>
					<!-- <div class="form-group">
						<table >
							<label for="apk_name_id" style="width:250px;" class="col-sm-4 control-label">
								<input name="mobileSate" type="checkbox" id="i_checkbox" value="1" /><span style="color: red;">重置后原KEY将失效</span>
							</label>
						</table>
					</div> -->
				</form>
			</div>
			</div>
			<div class="modal-footer">
				<input name="mobileSate" type="checkbox" id="i_checkbox" value="1" /><span style="color: red;">重置后原KEY将失效</span>
				<button type="button" class="btn btn-default" data-dismiss="modal">取
					消
				</button>
				<button type="button" class="btn btn-primary" id="i_btn">确
					定
				</button>
			</div>
		</div>
	</div>
</div>	


<div class="modal fade" id="key_data_div" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog modal-sm">
		<div class="modal-content" style="width: 520px;">
			<div class="modal-header">
				<h4 class="modal-title" id="del_h4_title">获取KEY</h4>
			</div>
			<div class="modal-body">
				<div class="modal-body">
					<div class="form-group">
						<table >
							<label for="apk_name_id" style="width: 150px;margin-left:50px;">KEY</label>
						</table>
					</div>
					<div class="form-group">
						<table style="margin-left:50px;">
							<label for="apk_name_id" style="width: 500px;margin-left:50px;">
								<input type="text" readOnly="true"   style="width: 310px;float: left;" class="form-control col-sm-3"   id="u_key">
								<input type="button" class="btn" style="float: left;" id="u_key_btn" value="复制" onclick="copyUrl2()" /> 
							</label>
						</table>
					</div>
					<div class="form-group">
						<table >
							<label for="apk_name_id" style="width:300px;margin-left:50px;">
								<input name="mobileSate" type="checkbox" id="u_checkbox" value="1" /><span style="color: red;">
									KEY已生成，本页面关闭后，平台将不再显示完整KEY，请妥善保存。
								</span>
							</label>
						</table>
					</div>
			</div>
			</div>
			<div class="modal-footer" style="text-align: center;">
				<button type="button" class="btn btn-primary" id="u_btn" style="width:300px;">
					确定并关闭
				</button>
			</div>
		</div>
	</div>
</div>	


<div class="modal fade" id="sx_data_div">
	<div class="modal-dialog modal-sm">
		<div class="modal-content" style="width: 520px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="del_h4_title"></h4>
			</div>
			<div class="modal-body">
								<div class="modal-body">
				<form id="cz_merge_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/puser/from/moneyMerge.do?menuId=${menuId}" enctype="application/x-www-form-urlencoded">
					<input type="hidden" style="width:100px;" class="form-control" id="i_czid" name="czid"/>
					<div class="form-group">
						<table style="margin-left: 10%">
							<label for="apk_name_id" style="width: 150px;" class="col-sm-3 control-label">上行推送地址</label>
							<div class="col-sm-5">
								<input type="text"  class="form-control" style="width: 250px;" value="${smsUsers.replyUrl}" name="replyUrl"  id="s_replyUrl"/>
							</div>
							
						</table>
					</div>
					<div class="form-group">
						<table style="margin-left: 10%">
							<label for="apk_name_id" style="width: 150px;" class="col-sm-3 control-label">报告推送地址</label>
							<div class="col-sm-5" style="width: 300px;">
								<input type="text" style="width: 250px;float: left;" class="form-control" value="${smsUsers.reportUrl}" name="reportUrl"   id="s_reportUrl">
							</div>
							
						</table>
					</div>
				</form>
			</div>
			</div>
			<div class="modal-footer" style="text-align: center;">
				<button type="button" class="btn btn-primary" id="s_btn" style="width:300px;">
					提交
				</button>
			</div>
		</div>
	</div>
</div>	

<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/userMessage/apiSetting.js"></script>
</body>
</html>