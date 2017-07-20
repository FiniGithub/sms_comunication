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
			font-size: 16px;
			font-weight: bold;
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
			<h4>短信群发</h4>
			<div class="title-remark"></div>
			<hr/>
	</div>

	<input type="hidden" value="${ctx}"  id="i_ctx"/>
	<input type="hidden" value="${menuId}"  id="menuId"/>
	
	<div >
			<table style="margin-top:50px;margin-left: 15%;" class="onetable" >
				<tr>
					<td colspan="2" style="text-align: center;">
						<span style="width:250px;height:30px;margin-left:20%;color:#0035BA;">☆短信发送☆</span>
					</br>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td style="float: right;text-align: right;">
						<span style="margin-right:30px;">电话号码:</span>
					</td>
					<td style="width: 500px;">
						<textarea class="form-control" style="font-weight: normal;"
						 placeholder="每个号码间用英文逗号字符间隔如（18697646,54654616.....）"
						 id="text_phone" name="phones" onkeyup="value=value.replace(/[^\d\,]/g,'')" rows="6">
						
						</textarea>
						<button type="button" onclick="drDataDiv();" class="btn" style="background-color:#B5EB94;width:120px;" id="i_btn">txt导入</button>
						<button type="button" class="btn" id="i_empty" style="background-color:#B5EB94;width:120px;">清空</button>
						<span style="float: right;font-size: 14px;">号码数：<span id="number_num" ></span></span>
						</br>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td style="float: right;text-align: right;">
						<span style="margin-right:30px;">短信内容:
					</td>
					<td>
						<textarea class="form-control" style="font-weight: normal;"
						 placeholder="请输入发送内容”【签名】+内容+退订回T“，内容需合法、真实、健康。"
						 id="text_count" name="counts" onkeyup="countFrom(this.value)" rows="4" >
							
						</textarea>
						<span style="float: left;font-weight: normal;font-size: 14px;"><span id="shuzi">0</span>/300</span>
						<span style="float: right;font-weight: normal;font-size: 14px;color: #999999">温馨提示：前70个字计1条，超出70个字每67个字计1条</span>
						</br>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td style="float: right;text-align: right;">
						<span style="margin-right:30px;">通道:
					</td>
					<td>
						<select  style="font-size: 16px;width:200px;" name="gid" id="text_gid">
							<option value="">请选择</option>
							<c:forEach var="item" items="${groupList}" varStatus="status">  
								<option key="${item.id}" value="${item.id}">${item.name}</option>	
							</c:forEach>
						</select>
						</br>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;">
						<button type="button" class="btn" style="width:250px;height:30px;margin-left:20%; background-color:#66AFE9;color:#FFFFFF;" id="i_dxfas">发送短信</button>
					
					</td>
					
				</tr>
			</table>
	</div>
	



<div class="modal fade" id="dr_data_div" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog modal-sm">
		<div class="modal-content" style="width: 520px;" id="dr_modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="del_h4_title">导入号码</h4>
			</div>
			<div class="modal-body">
				<div class="modal-body">
				<form id="fr_merge_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/userMsmSend/from/importPhone.do?menuId=${menuId}" enctype="multipart/form-data">
					<input type="hidden" style="width:100px;" class="form-control" id="i_czid" name="czid"/>
					<div class="form-group">
						<table style="margin-left: 10%">
							<label for="apk_name_id" style="width: 150px;" class="col-sm-3 control-label">导入号码文件：</label>
							<div class="col-sm-5">
							<input type="file" id="btnFile" name="btnFile" class="btnjy"/>  
					    	<input type="hidden" id="txtFoo" readonly="readonly" style="width: 300px" />           
							</div>
							
						</table>
					</div>
					
					<div class="form-group">
						<table style="margin-left: 10%">
							<!-- <tr>
								<td>
									<input type="button" value="上传" id="uploadbtn" onclick="upload()" style="cursor:pointer;padding:2px 2px 2px 2px;" />&nbsp;&nbsp;
									<input type="button" value="取消" onclick="closeWin()" style="cursor:pointer;padding:2px 2px 2px 2px;"  />
								</td>	
							</tr> -->
							<tr>
								<td>
								<span style="color: red;">提示： 仅支持txt格式号码文件，文件中手机号码须纵向排列，每行一个号码。</span>
								</td>
							</tr>
						</table>
					</div>
				</form>
			</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default btnjy" data-dismiss="modal">取
					消
				</button>
				<button type="button" class="btn btn-primary btnjy" onclick="txtFoo.value=$('#btnFile').val();com.company.project.services.newCase.fileUpLoad()" id="i_btn">
					上传
				</button>
			</div>
		</div>
	</div>
</div>	
<div id="loading" class="loading" style="display:none;">正在加载...</div>  

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

<style type="text/css">  
.loading{  
    width:160px;  
    height:56px;  
    position: absolute;  
    top:50%;  
    left:50%;  
    line-height:56px;  
    color:#fff;  
    padding-left:60px;  
    font-size:15px;  
    background: #000 url(../static/img/loader.gif) no-repeat 10px 50%;  
    opacity: 0.7;  
    z-index:9999;  
    -moz-border-radius:20px;  
    -webkit-border-radius:20px;  
    border-radius:20px;  
    filter:progid:DXImageTransform.Microsoft.Alpha(opacity=70);  
}  
</style> 
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/userSmsManage/smsSendGroup.js"></script>
<script src="${ctx}/static/bower_components/jquery/dist/ajaxfileupload.js"></script>
</body>
</html>