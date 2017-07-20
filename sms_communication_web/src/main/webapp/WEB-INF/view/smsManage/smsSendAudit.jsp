<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset='utf-8'>
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
	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fileinput.css">
    <link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
	<script src="${ctx}/static/js/commons/common.js"></script>
	<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
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
		<h4>短信审核</h4>
		<div class="title-remark">查看短信审核相关的信息，点击导出可导出任务发送号码
			并审核当前消息等操作。
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu inp-menu">
			<input type="hidden" id="menu_id" value="${menuId}" />
			 	
			<div  class="inp-box">
           	 
                 
             	<a href="#" id="delete_selected">
					<button type="button" class="btn btn-info">批量审核</button>
				</a> 
			</div>
				
			<div  class="inp-box">
           	 
                 <span class="inp-title">批次编号：</span>
                 		<input class="ids_input"  id="ids_input" type="text" value="${ids}" placeholder="请输入编号" />
               </div>  		
            

             <div  class="inp-box">
           	 
                 <span class="inp-title">审核状态：</span>
                	
                <select id="state_select">
                    <option value="0" selected = "selected">等待审核</option>
                    <option value="1">自动通过</option>
                    <option value="2">人工通过</option>
                    <option value="3">人工拒绝</option>
                    <option value="4">终止审核</option>
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
			<div id="data_div" ></div>
		</div>
		<div class="row">
			<table id="tb_data" style="width:1500px;">
			
			</table>
		</div>
	</div>
</div>


<div class="modal fade" id="del">
	<div class="modal-dialog modal-sm">
		<div class="modal-content" id="i_modal-content" style="width:500px;height:400px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="del_h4_title">确定审核</h4>
			</div>
			<div class="modal-body" >
				<div class="form-group">
					<label for="aisleId" class="col-sm-3 control-label">审核：</label>
					<div class="col-sm-4">
						<select id="is_type" style="width: 200px;">
							<option value="1">审核通过</option>
							<option value="0">不通过</option>
						</select>
					</div>
				</div>
				</br>
				<div id="shDiv">
					<div class="form-group">
						<label for="" class="col-sm-3 control-label">通道组：</label>
						<div class="col-sm-4">
							<select id="i_aisleGroup" style="width: 200px;">
								
							</select>
						</div>
					</div>
					</br>
			</br>
					<div class="form-group" style="height: 120px;">
						<label for="aisleId" class="col-sm-3 control-label">内容：</label>
						<div class="col-sm-4">
							<textarea class="form-control" id="s_count" style="font-weight: normal;width:300px;"
							  name="phones" rows="6">
							</textarea>
						</div>
					</div>
					</br>
					<div class="form-group">
						<input id="i_selectId" type="hidden"/>
						<div class="col-sm-3" style="width: 170px;">
							<input  name="succeedBilling" type="checkbox" id="i_qmbb" value="1" /><span style="padding-right: 20px;">添加签名报备</span>
						</div>
						<div class="col-sm-6"> 
								<input name="failureBilling" type="checkbox" id="i_mrms" value="1" /><span style="padding-right: 20px;">添加内天免审</span>
						</div>
					</div>
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


<div class="modal fade" id="del2">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="del_h4_title2">确定审核</h4>
			</div>
			<div class="modal-body" >
				<p id="del_p_title2">
					确认批量审核?
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取
					消
				</button>
				<button type="button" class="btn btn-primary" id="btn_del2">确
					定
				</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
function download(id){
    var url="${ctx}/msmSend/export/csv.do?id="+id;
    window.open(url);
}
</script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/smsManage/smsSendAudit.js"></script>
</body>
</html>