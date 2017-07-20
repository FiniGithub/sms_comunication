<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	String requestUri = request.getRequestURI();
	String contextPath = request.getContextPath();
	String url = requestUri.substring(contextPath.length() + 1);
%>
<c:set var="url_val" value="<%=url%>" />
<!DOCTYPE html>
<html lang="en">
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="blank">
		<meta name="format-detection" content="telephone=no">
		<title>代理平台后台-首页</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/static/new/css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/new/css/icon.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/new/css/webmasterStyle.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/static/new/css/common.css" />
		<!--[if IE]>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/new/css/ie8.css"/>
	<script src="${ctx}/static/js/appml5shiv.js"></script>
	<script src="${ctx}/static/js/applectivizr.js"></script>
	<![endif]-->
		<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
		<script type="text/javascript" src="${ctx}/static/new/js/aws_admin.js"></script>
		<script type="text/javascript" src="${ctx}/static/new/js/jquery.form.js"></script>
		<script type="text/javascript" src="${ctx}/static/new/js/framework.js"></script>
		<script type="text/javascript" src="${ctx}/static/new/js/global.js"></script>
		    <script src="${ctx}/static/js/commons/common.js"></script>
		  <script>
		  		$(function(){
                cookieSet();
                 cookieGet();
                console.log();
                 $(".collapse li a").each(function(){  
			        var $this = $(this);  
			        var $title=$(".com-title h4").text();
			        var $history=$("#history");
			        if($this[0].href==String(window.location)){
			            // $(".collapse").removeClass("active");
			            if($this[0].hash!="#history"){
			            	$history.hide();
			            	$history.parent().removeClass("active")
			            }
			           	if($title==""||$title=="undefined"){
			            	$history.show();
			            	$history.parent().addClass("active");
			           	}
			            $this.eq(0).parent().parent().parent().addClass("active");
			             $this.eq(0).parent().parent().show();
			            $this.eq(0).addClass("active");  			            
			            return;
			        }  
			    }); 

            })
                    function cookieSet(){
                         var path=$("#server_path").val()
                 var art_title = $(".com-title h4").text();
                 var art_url = window.location.href;
                
                 if(art_title==""||art_title=="undefined"){
                    return;
                 }
                 var history;
                 var json="[";
                 //json1是第一次注入cookie以后的第一个json，"此时还不是数组" 以点带面的处理
                 var json1;
                 var canAdd= true;
                  var web ={"title":art_title,"url":art_url};
                  var sweb=JSON.stringify(web);
                
                 if(!$.cookie("history")){
                 //第一次的时候需要初始化
                 history = $.cookie("history",sweb,{expires:7,path:path});
                 }else {
                 //已经存在
                 history = $.cookie("history");
                 console.log(history);
                 json1 = JSON.parse(history);
                  if(json1.length>5){
                     json1=json1.slice(-5);
                     console.log(json1.length);
                }
                 $(json1).each(function(){
                  if(this.title==art_title){
                      canAdd=false;
                      return false;
                  }
                 })
                 if(canAdd){
                  $(json1).each(function(){
                  json = json + "{\"title\":\""+this.title+"\",\"url\":\""+this.url+"\"},";
                  });
                  json = json + "{\"title\":\""+art_title+"\",\"url\":\""+art_url+"\"}]"; 

                  $.cookie("history",json,{expires:7,path:path});
                 }
                 }
                }
                function cookieGet(){
			       if($.cookie("history")){
			    var data =JSON.parse($.cookie("history")); 
			  
			   if(data.length>5){
			                     data=data.slice(-5);            
			  }
			   
			   var list =""; 
			   if(data.length>1){
			    data=data.reverse();
			   }
			   $(data).each(function(){
			    list = list + "<li><a href='"+this.url+"#history' >"+this.title+"</a></li>";
			   
			   })
			  
			   $("#history").append(list);;
			   // console.log(list);
			   } 
			}
		  </script>
<decorator:head/>

</head>
<body>
	<input type="hidden" value="${ctx}" id="server_path" />
	<!--头部-->
		<div class="aw-header">
			<button class="btn btn-sm mod-head-btn pull-left " style="display: none;">
         <ul class="brline">
         	<li></li>

         	<li></li>

         	<li></li>
         </ul>
        
        
    </button>
			
			<div class="mod-header-user">
				<div class="flex-c m-logo pull-left">
				<a href="${ctx}/welcome.do"><img src="${ctx}/static/new/img/logoht.png"/></a>

					
				</div>

				<ul class="pull-right">

					

					<li class="dropdown username">
						<a href="javascript:void" class="dropdown-toggle" data-toggle="dropdown">
							<img src="${ctx}/static/new/img/avatar-mid-img.png" class="img-circle" width="30">${sessionScope.session_user.email} <span class="caret"></span>
						</a>
						<ul class="dropdown-menu pull-right mod-user " style="left:0px;min-width: 100px;top:44px">
							<li>
								<a href="#" id="update_pwd">修改密码</a>
							</li>

							<li>
								<a href="${ctx}/logout.do">退出</a>
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
			<!--菜单-->
		<div class="aw-side ps-container" id="aw-side" style="background: #3f3f3f;">
			<div class="mod">				

				<div class="mod-message">
					<div class="message">
						 

						
					</div>
				</div>

				<ul class="mod-bar" id="side-menu">
					<input type="hidden" id="hide_values" val="0">
					<li class="hostbtn-box">
					   <a class="hostbtn" href="${ctx}/welcome.do"  title="首页">
                  			<i class="glyphicon glyphicon-home"></i><span class="hbtn">首  &nbsp;页</span>
               			 </a>
               			 </li>
					<li class="active">
						<a href="javascript:;" class=""  >
							<i class="glyphicon glyphicon-time"></i>
							<span>最近浏览</span>
							<img src="${ctx}/static/new/img/xiala.png" style="position: absolute;top: 10px;right: 15px ;"/>
							
						</a>
						<ul  class="collapse" id="history">							
						</ul>
					</li>
					<!-- 加载菜单 -->
						<c:forEach items="${sessionScope.menuList}" var="m" varStatus="status">
							<li>
								<a  href="javascript:;" class="" >
									<img src="${ctx}/static/new/img/duanxinguanli.png"/>
									<span>${m.name}</span>
									<img src="${ctx}/static/new/img/xiala.png" style="position: absolute;top: 10px;right: 15px ;"/>
								</a>
								<ul class="collapse">
									<c:forEach items="${m.child}" var="child">
										<li key="${child.url}">
											<a href="${ctx}${child.url}?id=${child.id}">
												<span><c:out value="${child.name}" /></span>
											</a>
										</li>
									</c:forEach>
								</ul>
							</li>
						</c:forEach>
				</ul>
			</div>
			
		</div>
		
		<!--内容-->
		<div class="aw-content-wrap">
			<div id="page-wrapper">

			<decorator:body />

			</div>	
		</div>


	<div class="modal fade bs-example-modal-lg" id="update_pwd_div">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">修改密码</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<input type="hidden" id="update_pwd_id"
							value="<c:if test="${not empty sessionScope.session_user}">${sessionScope.session_user.id}</c:if>" />
						<div class="form-group">
							<label for="oldPwd" class="col-sm-2 control-label">原密码</label>
							<div class="col-sm-5">
								<input type="password" class="form-control" id="oldPwd">
							</div>
						</div>
						<div class="form-group">
							<label for="newPwd" class="col-sm-2 control-label">新密码</label>
							<div class="col-sm-5">
								<input type="password" class="form-control" id="newPwd">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="update_pwd_btn">确定</button>
				</div>
			</div>
		</div>
	</div>


	<!--底部-->
	<div class="aw-footer">
		<span class="hidden-xs" style="width: 190px;display: inline-block">&nbsp;</span>
		<span>ICP证号：粤ICP备09100815号-7
			
		</span>
	</div>


	<div class="modal fade bs-example-modal-lg" id="update_pwd_div">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">修改密码</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<input type="hidden" id="update_pwd_id"
							value="<c:if test="${not empty sessionScope.session_user}">${sessionScope.session_user.id}</c:if>" />
						<div class="form-group">
							<label for="oldPwd" class="col-sm-2 control-label">原密码</label>
							<div class="col-sm-5">
								<input type="password" class="form-control" id="oldPwd">
							</div>
						</div>
						<div class="form-group">
							<label for="newPwd" class="col-sm-2 control-label">新密码</label>
							<div class="col-sm-5">
								<input type="password" class="form-control" id="newPwd">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="update_pwd_btn">确定</button>
				</div>
			</div>
		</div>
	</div>
	
	
    
 <div id="adWindow" style="position: fixed; right:0px;width:300px; height:200px; bottom:0px;display: none; z-index: 99999;">
	<div class="modal-dialog modal-sm">
		<div class="modal-content" style="width:300px">
			<div class="modal-header">
				<button type="button" class="close cencalcolose" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="del_h4_title">审核提示</h4>
			</div>
			<div class="modal-body">
				<p id="del_p_title">
					<a href="${ctx}/smsAudit/listview.do?id=120">有<span id="ardValue"></span>条待审核信息！</a>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default cencalcolose" data-dismiss="modal">
					确定
				</button>
			</div>
		</div>
	</div>
</div>
	
	<%--  <!-- jQuery -->
	<script
		src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
	<!-- Metis Menu Plugin JavaScript -->
	<script
		src="${ctx}/static/bower_components/metisMenu/dist/metisMenu.min.js"></script>
	<!-- Custom Theme JavaScript -->
	<script src="${ctx}/static/dist/js/sb-admin-2.js"></script>
	<script src="${ctx}/static/js/commons/welcome.js"></script> --%>
	<script
		src="${ctx}/static/bower_components/metisMenu/dist/metisMenu.min.js"></script>
	<script src="${ctx}/static/dist/js/sb-admin-2.js"></script>
	<script src="${ctx}/static/js/commons/welcome.js"></script>
	<script src="${ctx}/static/js/new.js"></script>
</body>
</html>
