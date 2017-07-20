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
	<!-- Bootstrap Core CSS -->
	<link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
	<!-- MetisMenu CSS -->
	<link href="${ctx}/static/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">
	<!-- Custom CSS -->
	<link href="${ctx}/static/dist/css/sb-admin-2.css" rel="stylesheet">
	<!-- Custom Fonts -->
	<link href="${ctx}/static/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href="${ctx}/static/bower_components/bootstrap/dist/css/jquery.bootstrap.css" rel="stylesheet">

    
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
		<h4>发送任务</h4>
		<div class="title-remark">查看发送任务相关的信息，点击导出可导出任务发送号码
			并查询发送详情等操作。111
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu inp-menu">
			<input type="hidden" id="menu_id" value="${menuId}" />
			<input type="hidden" id="i_path" value="${ctx}" />
					<form id="allotmerge_form" method="post" class="form-horizontal required-validate"
					  action="${ctx}/msmSend/smsSendDetails.do?menuId=${menuId}" enctype="application/x-www-form-urlencoded">
					  		<input class="ids_input"  id="ids_dis" name="ids" type="hidden"/>
					  </form>
				
			<div  class="inp-box">
           	 
                 <span class="inp-title">批次编号：</span>		<input class="ids_input"  id="ids_input" type="text" placeholder="请输入批次编号" />
              	
            </div>
            <div class="inp-box">
                <span  class="inp-title">发送状态：</span>	
                <select id="state_select">
                    <option value="">请选择</option>
                    <option value="-1">等待发送</option>
                    <option value="0">正在发送</option>
                    <option value="9">人工终止</option>
                    <option value="100">发送完成</option>
                    <option value="101">已结算</option>
                </select>
            </div>
            <div class="inp-box">
                	<span  class="inp-title">发送类型：</span>
                <select id="sendType_select">
                    <option value="">全部</option>
                    <option value="0">马上发送</option>
                    <option value="1">定时发送</option>
                </select>
            </div>
			
           	<div id="data_div" ></div>
            <div class="inp-box">
              <span  class="inp-title">发送内容：</span>
			                                <input class="btn_input"  id="content_input" type="text" placeholder="请数发送内容" />
               
            </div>
			 
            
            <div class="inp-box">
                  	<span  class="inp-title">代理账号：</span>
           	 
                 		<input class="smsUserName_input"  id="smsUser_input" type="text" placeholder="请输入账号" />
            		
                
            </div>
             <div class="inp-box inpt-box-btn">
                 <span  class="inp-title"> 提交时间：</span>
			                               <input id="start_input" type="text" placeholder="年/月/日" />
			                               <span class="dataline">—</span><input id="end_input" type="text" placeholder="年/月/日"/>
			                 <button class="btn btn-warning" id="reset" type="button">重置</button>
			          
			            <button class="btn btn-info" type="button" id="search_btn">
						<span class="glyphicon glyphicon-search"></span></button>               
            </div>
			<div id="data_div" style="width: 60%"></div>
		</div>
		
		<div class="com-menu" style="color: #FD0000;">
			<span>发送号码数：</span>
			<span id="i_sendNumCount" style="padding-right:20px;">0</span>
			<span>计费条数：</span>
			<span id="i_billingNumCount" style="padding-right:20px;">0</span>
			<span>结算条数：</span>
			<span id="i_actualNumCount">0</span>
		</div>
		<div class="row">
			<table id="tb_data" style="width:1300px;">
			
			</table>
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


<div class="modal fade" id="msmSendPackage">
	<div class="modal-dialog modal-sm">
		<div class="modal-content" style="width:800px;margin-left: -200px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="del_h4_title">子内容</h4>
			</div>

	 	<div class="com-content"> 
			<div class="com-menu">
				<input type="hidden" id="menu_id" value="${menuId}" />
				<input type="hidden" id="skid_id" value="${skid}" />
				
	            <span style="float:left;margin-top: 10px">
		           	 <div style="position: relative;">
		                 	通道：	<input class="smsUserName_input"  id="smsAisle_input" type="text" placeholder="" />
		            
		            	<button class="btn btn-info" type="button" id="package_btn">
							<span class="glyphicon glyphicon-search"></span>
					</button>
		             </div>  
	            </span>
	            
	              
							
				<div id="data_div" style="width: 60%"></div>
			</div>
			
			<div class="row">
				<table id="package_data">
				
				</table>
			</div>
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

<div class="modal fade" id="msmSendResend">
	<div class="modal-dialog modal-lg" style="width:600px;">
		<div class="modal-content" >
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="h4_title">任务重发</h4>
			</div>
			<div class="modal-body">
				<form id="uploadFile" class="form-horizontal" method="post" class="flex-c"  enctype="multipart/form-data" autocomplete="off">	
					<input type="hidden" id="sendResend_id"/>
					<div class="form-group">
						<label for="apk_description" class="col-sm-3 control-label">重发类型</label>
						<div class="col-sm-5">
							<select class="province select" style="width: 180px;" id="i_type" name="types" onchange='btnChange(this[selectedIndex].value);'>
								<option value="0">状态重发</option>
								<option value="1">号码重发</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label for="apk_name_id" class="col-sm-3 control-label">通道</label>
						<div class="col-sm-5">
							<select class="province select" style="width:180px;"  id="i_smsAisle" name="smsAisles">
								<option value="">请选择</option>
							</select>
						</div>
					</div>
					
					
					<div class="form-group" id="div_smsState">
						<label for="apk_name_id" class="col-sm-3 control-label">状态</label>
						<div class="col-sm-5">
							<select class="province select" style="width:180px;"  id="i_smsState" name="smsState">
								<option value="">请选择</option>
								<option value="0">待发送</option>
								<option value="1">发送失败</option>
							</select>
						</div>
					</div>
				
					<div class="form-group" id="div_sendPhong">
						<label for="apk_name_id" class="col-sm-3 control-label">发送号码</label>
						<div class="col-sm-5">
							<input type="file" id="btnFile" name="uploadFile" class="btnjy"/>  
					    	<input type="hidden" id="txtFoo" readonly="readonly" style="width: 300px" />
					    	<span style="color:#FF0000;">导入需要重发的号码txt文件且每行一个号码</span>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							取消
						</button>
						<button type="button" class="btn btn-primary" onclick="txtFoo.value=$('#btnFile').val();smsSendResend()" id="save_data_btn">
							确定
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>







<script type="text/javascript">
function download(id){
    var url="${ctx}/msmSend/export/csv.do?id="+id;
    window.open(url);
}
function phoneAllLoad(id){
    var url="${ctx}/msmSend/export/phoneAllLoad.do?id="+id;
    window.open(url);
}
</script>

<!-- Custom Theme JavaScript -->
<input type="hidden" id="server_path" value="${ctx}" />
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
<script src="${ctx}/static/js/commons/common.js"></script>
<script src="${ctx}/static/js/commons/welcome.js"></script>
<script src="${ctx}/static/js/commons/jquery.bootstrap.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<%-- <script src="${ctx}/static/js/fileinput.js"></script> --%>
<script src="${ctx}/static/js/smsManage/msmSend.js"></script>
</body>
</html>