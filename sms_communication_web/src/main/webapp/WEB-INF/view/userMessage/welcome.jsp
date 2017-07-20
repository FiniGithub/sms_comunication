<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>welcome</title>

	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fileinput.css">
    <link href="${ctx}/static/css/jquery.datetimepicker.css" rel="stylesheet">
    <script src="${ctx}/static/js/jquery.datetimepicker.js"></script>
    
	<style>
	
	.onetable td{
			height: 30px;
			width: 150px;
			font-size: 18px;
		}
		
	.woetTable td{
		border:solid #A9A9A9;
		border-width:0px 1px 1px 0px; 
		padding:2px 0px;
		text-align:center;
		width: 150px;
	}
	.tabediv table{
		float: left;
		margin-right: 20px;
		margin-bottom: 10px;
	}
	 .woetTable{
		border:solid #A9A9A9;
		border-width:1px 0px 0px 1px;
		width:300px;
	}
	.woetTables td{
		border:solid #A9A9A9;
		border-width:0px 1px 1px 0px; 
		padding:2px 0px;
		width: 150px;
	}
	.woetTables{
		border:solid #A9A9A9;
		border-width:1px 0px 0px 1px;
		width:300px;
	}
	.titles span {
		padding-left:15px;
	}
	.titles{
		background-color:#CBCBCB;
		height: 40px;
	}
	.count{
		height: 270px;
		
	}
	.count div{
		margin-left: 15px;
		margin-right: 15px;
	}
	.contitle{
		font-weight: bold;
		font-size:22px;
	}
	.contitle2{
		float: right;
	}
	.xing{
		font-weight: bold;
		font-size:22px;
		color:#FF0000;
	}
	</style>
</head>
<body>
	<input type="hidden" value="${ctx}" id="server_path" />
	<div class="com-title">
			<h4>代理商主页面</h4>
			<div class="title-remark">欢迎来到运营系统， 下面列出常用功能菜单， 方便您快速操作。</div>
			<hr/>
	</div>
	<input type="hidden" id="menu_id" value="${menuId}" />
	<input type="hidden" id="i_uid" value="${uid}" />
	
	<div class="com-content" style="height:1200px;">
			<table style="margin-top:50px;" class="onetable" align="right" >
				<tr>
					<td style="font-weight: bold;">
						当前剩余金额:
					</td>
					<td>${smsUser.money}元</td>
					<td></td>
				</tr>
			</table>
			</br>
			 <table id="tb_data" align="center" class="woetTable"  style="margin-top:80px;width: 500px;">
            	 <thead>
	                  <tr>
	                      <td colspan="2" style="background-color:#CBCBCB;"><h4>今日数据</h4></td>
	                  </tr>
	                  <tr>
	                      <td><h4>${tjsmsUser.sumSendNum}</h4></br>计费条数</td>
	                      <td><h4>${tjsmsUser.sumExpectMoney}</h4></br>预扣金额</td>
	                  </tr>
                  </thead>
            </table>
            
			</br>
			<div>
				<div style="height: 30px;">
				<span><span style="font-weight: bold;font-size:22px;  float: left;">我的通道</span>
				<c:if test="${userLevel>1}">
					<a style="margin-left: 20px; color:#27AFF2;" href="javascript:addtabe();"><span style="font-size:22px;">⊕</span>创建格子</a>
					<a style="font-size:16px; color:#989898; " title="~&#10;通道格子用于存放通道，在接口调用&#10;时需传输固定的通道格子编码。创建&#10;通道格子后编码既固定，且不可删除&#10;~" id="xfdiv"><span>【?】</span></a>
				</c:if>
				</span>
				</div>
				</br>
				<div id="tabediv" class="tabediv">
				<c:forEach var="item" items="${saList}" varStatus="status">  
					<c:if test="${not empty item.smsAisleGroupId}">
						<table  class="woetTables">
							<tr>
								 <td class="titles"><span>通道格子编码：${item.id}</span></td>
							</tr>
							<tr>
								<td class="count">
								<div>
									<span class="contitle">
										<c:if test="${item.addType==1}">默认通道</c:if>
										<c:if test="${item.addType>1}">${item.sagName}</c:if>
									</span>
									<span class="contitle2">${item.typeName}</span>
									</br>
									<span class="xing">${item.lvName}</span>
									</br>
									</br>
									<span>移动：
										<c:if test="${item.userLevel==1}">${item.uprice}</c:if>
										<c:if test="${item.userLevel>1}">${item.guprice}</c:if>
									元/条</span>
									</br>
									<span>联通：
										<c:if test="${item.userLevel==1}">${item.mprice}</c:if>
										<c:if test="${item.userLevel>1}">${item.gmprice}</c:if>
									元/条</span>
									</br>
									<span>电信：
										<c:if test="${item.userLevel==1}">${item.tprice}</c:if>
										<c:if test="${item.userLevel>1}">${item.gtprice}</c:if>
									元/条</span> <span class="contitle2"><a>关联屏蔽词>></a></span>
									</br>
									</br>
									<span>${item.describes}</span>
									</br>
									</div>
								</td>
								
							</tr>
						</table>
					</c:if>
					<c:if test="${empty item.sagName}">
						<table id="" class="woetTables">
							<tr>
								<td class="titles"><span>通道格子编码：${item.id}</span></td>
							</tr>
							<tr>
								<td class="count" style="background-color: #CBCBCB;color:#FFFFFF;text-align: center; ">
								<div>请从通道市场中添加通道</div></td>
							</tr>
						</table>
					</c:if> 
				</c:forEach>
				</div>
			</div>
			
			<div style="width:100%; float: left;">	
				</br>
				</br>
				<span style="font-weight: bold;font-size:22px;  float: left;">通道市场</span>
				</br>
				</br>
				</br>
			<div class="tabediv">
				<c:forEach var="item" items="${vipList}" varStatus="status">  
					<table  class="woetTables">
						<tr>
							<td class="count">
							<div>
								<span class="contitle">
									${item.sagName}
								</span>
								<span class="contitle2">${item.typeName}</span>
								</br>
								<span class="xing">${item.lvName}</span>
								</br>
								</br>
								<span>移动：${item.guprice}元/条</span>
								</br>
								<span>联通：${item.gmprice}元/条</span>
								</br>
								<span>电信：${item.gtprice}元/条</span> <span class="contitle2"><a>关联屏蔽词>></a></span>
								</br>
								</br>
								<span>${item.describes}</span>
								</br>
								</br>
								<button type="button" onclick="deleteData(${item.smsAisleGroupId},${item.aisleTypeId})" style="width: 100%;background-color: #4EBEEB;height: 30px;" class="btn btn-primary" id="cz_allot_btn">加入通道</button>
								</div>
							</td>
							
						</tr>
					</table>
				</c:forEach>
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
				<h4 class="modal-title" id="del_h4_title">确定加入通道</h4>
			</div>
			<div class="modal-body">
				<p id="del_p_title">
					请选择格子编号：
					<select style="width: 150px;" id="ugid">
					<c:forEach var="item" items="${saList}" varStatus="status">   
							<c:if test="${(item.userLevel>1 && item.addType>1) || empty item.smsAisleGroupId}">
								<option key="${item.aisleTypeId}" value="${item.id}" >${item.id}</option>
							</c:if>
					</c:forEach> 
				</select>
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

<div class="modal fade" id="dquding">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="del_h4_title">确认</h4>
			</div>
			<div class="modal-body">
				<p id="del_p_title">
					确定添加通道格子？
					</br><span style="color: #989898;">注：创建后不可删除</span>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取
					消
				</button>
				<button type="button" class="btn btn-primary" id="btn_dquding">确
					定
				</button>
			</div>
		</div>
	</div>
</div>	
	
<script src="${ctx}/static/js/bootstrap-table.js"></script>
<script src="${ctx}/static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/fileinput.js"></script>
<script src="${ctx}/static/js/userMessage/welcome.js"></script>
</body>
</html>