<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	
	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
	<link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

	 <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/laydate/need/laydate.css"/>
	  <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">  
</head>
<body>

<input type="hidden" id="path" value="${ctx}">

<div>
	
	<div class="com-content">
		<div  style="text-align:center;">
			<input type="hidden" id="menu_id" value="${menuId}" />
             
          
           	 	<div class="inp-box">
                 <span   class="inp-title"> 账号：</span>
                 		<input  class=' inp-box-inp'  id="user_input" type="text" placeholder="">
                </div> 
                
                <div class="inp-box">
                <span  class="inp-title">通道名称：</span>	
                <select id="aisle_select">
                    <option value="">-全部-</option>
                    <c:forEach var="item" items="${aisleNames}" varStatus="status">   
						<option key="${item}" value="${item}" >${item}</option>
					</c:forEach> 
                </select>
            </div> 		
            
          	<div class="inp-box inpt-box-btn">
                 <span  class="inp-title"> 时间：</span>
			                               <input class=' inp-box-inp' id="start_input" type="text" placeholder="" />
			                               <span class="dataline"> 至 </span>
			                               <input  class=' inp-box-inp' id="end_input" type="text" placeholder=""/> 
			                    
          </div>
          <div class="inp-box">
          	<input class="f-btn" value="查询" type="button" id="search_btn" style="margin-left: 10px" />
          </div>
          <div class="inp-box">
          	<input class="f-btn" value="刷新" type="button" id="refrash_btn" style="margin-left: 10px" />
          </div>
          <div class="inp-box">
          	<input class="f-btn" value="导出" type="button" id="export_btn" style="margin-left: 10px" />
          </div>
			<div id="data_div" ></div>
		</div>
			
			
		
	</div>
	<div class="row">
			<table id="tb_data"></table>
			<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>



		</div>
</div>
	<input type="hidden" id="server_path" value="${ctx}" />
	<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
	<script src="${ctx}/static/js/commons/common.js"></script>	
	<script src="${ctx}/static/js/bootstrap-table.js"></script>
	<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
	<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
	 <script src="${ctx}/static/dzd/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/js/puser/channelStataical.js"></script>
<script src="${ctx}/static/js/puser/orderExport.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>

</body>
</html>