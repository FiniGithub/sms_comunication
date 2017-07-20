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
		<h4>代理账单流水</h4>
		<div class="title-remark">
		</div>
		<hr/>
	</div>
	<div class="com-content">
		<div class="com-menu inp-menu" >
			<input type="hidden" id="menu_id" value="${menuId}" />
           
           <div class="inp-box ">
                 <span  >	类型： </span>
                 	
                 	 <select id="i_type">
                    <option value="">请选择</option>
                    <c:if test="${type!=null}">
                    	<option value="0" selected="selected">充值</option>
                    </c:if>
                    <c:if test="${type==null}">
                    	<option value="0">充值</option>
                    </c:if>
                     <option value="1">消费</option>
                    
                  
                    <option value="2">返还</option>
                </select>
                </div>  		
            
              <div class="inp-box ">
                 <span class="inp-title"  >客户账号： </span>
                 		<input class="btn_input"  id="user_input" type="text" placeholder="请输入账号">
                </div>  		
          
           <div class="inp-box inpt-box-btn">
                 <span  class="inp-title"> 创建时间：</span>
			                               <input id="start_input" type="text" placeholder="年/月/日" />
			                               <span class="dataline">—</span><input id="end_input" type="text" placeholder="年/月/日"/>
			                 <button class="btn btn-warning" id="reset" type="button">重置</button>
			          
			            <button class="btn btn-info" type="button" id="search_btn">
						<span class="glyphicon glyphicon-search"></span></button>               
          </div>
			<div id="data_div" ></div>
		</div>
		
		<div class="com-menu" style="color: #FD0000;">
			<span>消费合计：</span>
			<span id="i_snum">0条</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>充值合计：</span>
			<span id="i_fnum">0条</span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span>返还合计：</span>
			<span id="i_unum">0条</span>
		</div>
		<div class="row">
			<table id="tb_data"></table>
		</div>
	</div>
</div>

<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/puser/puserBill.js"></script>
</body>
</html>