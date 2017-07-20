<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset='utf-8'>
<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>

<link
	href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css"	rel="stylesheet">	
<!-- Custom Fonts -->

<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/laydate/need/laydate.css"/>
<script src="${ctx}/static/dzd/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/static/js/commons/common.js"></script>
<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
<!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
    <script src="${ctx}/static/js/appml5shiv.js"></script>
    <script src="${ctx}/static/js/applectivizr.js"></script>
    <style>
    	.modal-dialog{
    		width:480px;
    	}
    	.inp-inp{
    		margin-left:auto;
    	}
    </style>
    <![endif]-->
</head>
<body>

<input type="hidden" id="shieldWord" value="${shieldWord}" />
<input type="hidden" id="canCommit" value=0 />
<input type="hidden" id="smsUserEmail"/>

<div>
	
	<div class="com-content">
		<div class="com-menu inp-menu" style="text-align:center;">
			<input type="hidden" id="menu_id" value="${menuId}" />
             
            
           <div class="inp-box ">
            	<span class="inp-title" >账号：</span>
                 	<input class="inp-box-inp"  id="user_input" type="text" placeholder="">
           </div>  		
            
           <div class="inp-box inpt-box-btn">
                 <span  class="inp-title"> 日期：</span>
			     <input id="start_input" class="inp-box-inp" type="text" placeholder="" />
			     <span class="dataline" style="display:inline-block;">至</span>
			     <input id="end_input" class="inp-box-inp" type="text" placeholder=""/>                         
            </div>
             		
          <div class="inp-box">
                <span  class="inp-title">状态：</span>	
                <select id="sendType_select">
                    <option value="" selected = "selected">-全部-</option>
                    <option value="1">启动</option>
                    <option value="2">停止</option>
                </select>
            </div>
            
	            <span id="aisleSpan" style="display: none;">
	                <span  class="inp-title">通道类型：</span>	
		                <select id="aisle_select">
		                    <option value="">-全部-</option>
		                    <c:forEach var="item" items="${aisleNames}" varStatus="status">   
								<option key="${item.id}" value="${item.id}" >${item.name}</option>
							</c:forEach> 
		                </select>
	            </span>
        
            <div class="inp-box">
           
					<input class="f-btn" type="button" value="查询"  id="search_btn">
					
	            		<span id="stopSpan" style="display: none;">
							<input class="f-btn" type="button" value="停止"  id="stop_btn">
						</span>
        			
	            		<span id="startSpan" style="display: none;">
	           		 		<input class="f-btn" type="button" value="启动"  id="start_btn">
	           		 	</span>
        			
	            		<span id="deleteSpan" style="display: none;">
		           			<input class="f-btn" type="button" value="删除"  id="delete_btn">	
		           		 </span>
        			   
	            		<span id="exportSpan" style="display: none;">                   
		          			<input class="f-btn" type="button" value="导出号码" id="export_btn">
		          		 </span>
            </div>
            
       <div class="modal fade bs-example-modal-lg" id="modify_div">
		<div class="modal-dialog ">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true"></span>
					</button>
					<h4 class="modal-title" id="btnOk_update">修改定时任务</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<input type="hidden" id="taskId" /> 
						<input  type="hidden" class="inp-box-inp" id="signature" />
						
					<table class="">
						<tr class="addNum-box">
				            <td class="tab-h">账号：</td>
				            <td class="tab-content">
								<span class="" id="email"></span>
							</td>	
						</tr>
						<tr class="addNum-box">
				            <td class="tab-h">签名：</td>	
							<td class="tab-content">
								<select id="signature_select">
				                    <option value=""></option>
				                    <c:forEach var="item" items="${signatures}" varStatus="status">   
										<option key="${item}" value="${item}" >${item}</option>
									</c:forEach> 
				                </select>
							</td>
						</tr>
						<tr class="addNum-box">
				            <td class="tab-h">内容：</td>	
							<td class="tab-content">
								<p style="padding-top:10px">
				                    <textarea id="content" class="dxcontent-xg-txt" maxlength="" class="chackTextarea" onkeyUp="sensitiveWordsAndcalcLangth()"></textarea>
				                </p>
								<br>
								<span class="inp-title">字数：</span>							
								<span class="" id="i_length"></span>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span class="inp-title">短信条数：</span>							
								<span class="" id="i_num"></span>
							</td>
						</tr>
						<tr class="addNum-box">
				            <td class="tab-h">定时：</td>	
							<td class="tab-content">
							<input id="time_input" class="inp-box-inp" type="text" placeholder="" readonly="readonly"/>
							</td>
						</tr>
					</table>	
					</form>
				</div>
				<div class="modal-footer">
				 <input type="button" class="f-btn" id="save_btn" value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">
					
				</div>
			</div>
		</div>
	</div>
            
			<div id="data_div" ></div>
		</div>
		
		<div class="row">
			<table id="tb_data"></table>
			
<div class="minlid">
				<a id="firstpage"  href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="sto">
    <div class="modal-dialog awl">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="btnOk_update">停止任务</h4>
            </div>
            <p class="aw-newcontent">
                <span class="aw-new">确认是否停止所选任务？</span>
            </p>

            <div class="modal-footer">
                <input type="button" class="f-btn" id="btn_stop" value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">

            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="del">
    <div class="modal-dialog awl">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
                <h4 class="modal-title" id="btnOk_update">删除任务</h4>
            </div>
            <p class="aw-newcontent">
                <span class="aw-new">确认是否删除所选任务？</span>
            </p>

            <div class="modal-footer">
                <input type="button" class="f-btn" id="btn_del" value="确 定"/>
                <input type="button" class="f-btn" data-dismiss="modal" value="取 消">

            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/smsManage/smsTimedTask.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>