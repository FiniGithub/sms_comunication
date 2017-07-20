<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<link href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
    <script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>  
    <link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
    <script type="text/javascript" src="${ctx}/static/dzd/jquery.ui.widget.js"></script>	 
	 <script type="text/javascript" src="${ctx}/static/dzd/jquery.iframe-transport.js"></script>
	  <script type="text/javascript" src="${ctx}/static/dzd/jquery.fileupload.js"></script>   
    <script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>

</head>
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
<script src="${ctx}/static/js/appml5shiv.js"></script>
<script src="${ctx}/static/js/applectivizr.js"></script>
<style>
    .modal-dialog {
        width: 300px;
    }
</style>
<![endif]-->
<body>

<input type="hidden" id="path" value="${ctx}" />
<input id="forwardUuid" type="hidden" value="" />

<input type="hidden" id="mobileTotalPage" />
<input type="hidden" id="unicomTotalPage" />
<input type="hidden" id="telecomTotalPage" />
<input type="hidden" id="invalidTotalPage" />

<input type="hidden" id="mobileCurrentPage" value="1"/>
<input type="hidden" id="unicomCurrentPage"  value="1"/>
<input type="hidden" id="telecomCurrentPage"  value="1"/>
<input type="hidden" id="invalidCurrentPage"  value="1"/>

	<div style="padding:2px" >
	<table class="qftab">
		<tr>
		<td style="text-align:center;">
		<div style="display:inline-block;">
			<div style="padding-left:24px;text-align:left;">
			
				<div class="inp-box">
				<input type="button" id="start-upload"  class="f-btn" value="导入号码" data-toggle="modal" data-target="#upload_file"/>
				<input type="button" id="send_btn"  class="f-btn" value="发送短信" />
				
	            
	           
	           	<input type="button" id="export_btn"  class="f-btn"  value="导出号码" />	           
        </div>
       
		</div>
		
        <table class="filter-pho-table">
        	<tr>
        		<td>
        		<div class="mobileCheckbtnbox" >
	        		<input id="mobileCheckbox" type=checkbox >
					<span>移动号码：</span>
					<span class="phnum" id="i_mobile">0</span>个
        		</div>
        			
        		</td>
        		<td>
        		<div class="mobileCheckbtnbox" >
        		<input id="unicomCheckbox" type=checkbox  >
				<span>联通号码：</span>
				<span class="phnum" id="i_unicom">0</span>个
        		</div>
        			
        		</td>
        		<td>
        		<div class="mobileCheckbtnbox" >
        		<input id="telecomCheckbox" type=checkbox  >
				<span>电信号码：</span>
				<span class="phnum" id="i_telecom">0</span>个
        		</div>
        			
        		</td>
        		<td>
        		<div class="mobileCheckbtnbox" >
        		<input id="invalidCheckbox" type=checkbox>
				<span>未知号码：</span>
				<span class="phnum" id="i_invalid">0</span>个
        		</div>
        		
        			
        		</td>
        	</tr>
        	<tr>
        		<td>
        		<textarea class="filtertextear" id="mobile" name="mobileOperator" readOnly></textarea>
        		</td>
        		<td>
        		<textarea class="filtertextear" id="unicom" name="unicomOperator"  readOnly ></textarea>
        		</td>
        		<td>
        		<textarea class="filtertextear" id="telecom" name="telecomOperator"  readOnly ></textarea>
        		</td>
        		<td>
        		<textarea class="filtertextear" id="invalid" name="invalidNum"  readOnly ></textarea>
        		</td>
        	</tr>
        	<tr>
        		<td>
        			<button id="mobileHomePage" disabled>首页</button>
        			<button id="mobilePreviousPage" disabled>上一页</button>
        			<button id="mobileNextPage" disabled>下一页</button>
        			<button id="mobileLastPage" disabled>尾页</button>
        		</td>
        		<td>
        			<button id="unicomHomePage" disabled>首页</button>
        			<button id="unicomPreviousPage" disabled>上一页</button>
        			<button id="unicomNextPage" disabled>下一页</button>
        			<button id="unicomLastPage" disabled>尾页</button>
        		</td>
        		<td>
        			<button id="telecomHomePage" disabled>首页</button>
        			<button id="telecomPreviousPage" disabled>上一页</button>
        			<button id="telecomNextPage" disabled>下一页</button>
        			<button id="telecomLastPage" disabled>尾页</button>
        		</td>
        		<td>
        			<button id="invalidHomePage" disabled>首页</button>
        			<button id="invalidPreviousPage" disabled>上一页</button>
        			<button id="invalidNextPage" disabled>下一页</button>
        			<button id="invalidLastPage" disabled>尾页</button>
        		</td>
        	</tr>
        </table>
		</div>
			
		</td>
		</tr>
	</table>       
	</div>

<div class="modal fade" id="upload_file">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
				<h4 class="modal-title" >上传文件</h4>
            </div>
            <div class="modal-body">
                <p id="del_p_title">
                <ul class="alwd-upload-inp">
                    <li>
                        <form id="uploadFile" method="post" class="flex-c" enctype="multipart/form-data"
                              autocomplete="off">
                            <span id="font-allNum-once" style="display: none">0</span>
                            <span class="upl-title">号码文件</span>
                            <input type='text' name='filerout' id='textfield' class='fileli' readonly="readonly"/>

                            <input id="file_uploadFile" class="fileli-btn" type='button' class='btn' value='浏览...'
                                   onclick="$('#input_uploadFile').click()"/>

                            <input type="file" name="uploadFile" id='input_uploadFile' style="display:none;"
                                   class='fileli' onchange="readFileUpload();"/>
                        </form>
                    </li>

                    <li class="flex-c proge_li" style="font-size: 10px;">
                        注:文件内每行一个号码
                    </li>
                    <li class="proge-num-box proge_li">
                        <label class="lead-finish" style="display: none">
                            已完成：<span class="progebfb">0</span>%
                        </label>
                        <img class="flex-hint" src="../static/img/send/u4.gif">
                    </li>
                </ul>
                </p>
            </div>
            <div class="modal-footer">
            <input type="button" class="f-btn" id="udatephobtn"    value="确 定">
                    <input type="button" class="f-btn " id="upfilecenter"  value="取 消">          

            </div>
        </div>
    </div>
</div>


<script src="${ctx}/static/js/systemUser/distinguishOperator.js"></script>

</body>
</html>