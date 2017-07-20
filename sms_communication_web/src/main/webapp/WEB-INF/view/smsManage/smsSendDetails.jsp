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
		<h4>发送详情</h4>
		<div class="title-remark">
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu inp-menu">
			<input type="hidden" id="menu_id" value="${menuId}" />
			
			<div  class="inp-box">
           	 
                 <span class="inp-title">批次编号：</span>	
                 		<input class="ids_input"  id="ids_input" type="text" value="${ids}" placeholder="请输入编号" />
               </div>  		
           
          <div  class="inp-box">
           	 
                 <span class="inp-title">客户账号：</span>	
	                 		<input class="smsUserName_input"  id="smsUser_input" type="text" placeholder="请输入账号" />
	             </div>  		
           
            
			 
            
            <div  class="inp-box">
           	 
                 <span class="inp-title">发送内容：</span>	
			                                <input class="btn_input"  id="content_input" type="text" placeholder="请输入发送内容" />
                </div>
           
            <div  class="inp-box">
           	 
                 <span class="inp-title">发送状态：</span>	
                	
                <select id="state_select">
                    <option value="">请选择</option>
                    <option value="-1">待发送</option>
                    <option value="0">已发送</option>
                    <option value="2">发送失败</option>
                     <option value="99">状态报告未知</option>
                     <option value="100">发送成功</option>
                </select>
            </div>
            
               <div  class="inp-box">
           	 
                 <span class="inp-title">下发号码：</span>
			                                <input class="btn_input"  id="phone_input" type="text" placeholder="请输入号码" />
                </div>
             <div  class="inp-box">
           	 
                 <span> 是否返回转态报告：</span>
	                                   
	            <select id="bgzt_select">
	                    <option value="">请选择</option>
	                    <option value="0">已返回</option>
	                    <option value="1">未返回</option>
	                </select>
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
		<div class="com-menu" style="color: #FD0000;">
			<span>号码数量：</span>
			<span id="i_grossNum" style="padding-right:20px;">0</span>
			<span>发送成功：</span>
			<span id="i_snum">0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>发送失败：</span>
			<span id="i_fnum">0</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>未知：</span>
			<span id="i_unum">0</span>
		</div>
		<div class="row">
			<table id="tb_data" style="width:1300px;"></table>
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
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/smsManage/smsSendDetails.js"></script>
</body>
</html>