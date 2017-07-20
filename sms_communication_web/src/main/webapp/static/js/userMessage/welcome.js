// var ChineseDistricts_city = {};
$(function () {
	//load_data();  
	
	

/*	$("#xfdiv").mouseover(function(){
		$('#xfcopunt').modal("show");
	});*/

	
	
	$("#btn_del").bind("click", function() {
		var ugid = $("#ugid").val();
		var agstype =$("#ugid").find("option:selected").attr("key"); //获取选中的通道的类型
		if(smsAisleGroupType+"" != agstype+"" && agstype!=""){
			alert("通道类型不一致，请重新选择通道！");
			return;
		}
		var param = {
			"smsAisleGroupId" : smsAisleGroupId,
			"ugid":ugid
		};
		ajaxCall({
			url : "/userMessage/from/updateUserAg.do",
			type : "get",
			data : param,
			success : function(data) {
				if (checkRes(data)) {
					alert("加入成功！");
					load_data()
				} else {
					alert("加入失败！");
				}
			},
			error : function() {
			},
			complete : function() {
			}
		});
	});
	
	$("#btn_dquding").bind("click", function() {
		var uid = $("#i_uid").val();
		var param = {
				"uid" : uid
			};
		ajaxCall({
			url : "/userMessage/from/addUserAg.do",
			type : "get",
			data : param,
			success : function(data) {
				if (checkRes(data)) {
					var id = data["data"];
					
					var sehtml = $("#ugid").html();
					var sehtml2 = '<option key="" value="'+id+'" >'+id+'</option>'
					$("#ugid").html(sehtml+sehtml2);
					
					var htl = $("#tabediv").html();
					var htl2 = '<table id="" class="woetTables">'
								+'<tr>'
								+'<td class="titles"><span>通道格子编码：'+id+'</span></td>'
								+'</tr>'
								+'<tr>'
								+'<td class="count" style="background-color: #CBCBCB;color:#FFFFFF;text-align: center; ">'
								+'<div>请从通道市场中添加通道</div></td></tr></table>';
					htl+=htl2;
					$("#tabediv").html(htl)
				} else {
					alert("添加失败！");
				}
				$("#dquding").modal("hide");
			},
			error : function() {
			},
			complete : function() {
			}
		});
	});
	
	
});


//刷新页面
function load_data(){
    var path = $("#server_path").val();
	window.location.href = path+"/userMessage/welcome.do";
}

function addtabe(){
	var num = $("#tabediv table").length
	if(num>=20){
		alert("最多添加20个通道，数量已上限。");
		return;
	}
	$('#dquding').modal("show");
}

var smsAisleGroupId="";
var smsAisleGroupType="";
function deleteData(id,type) {
	$('#del').modal("show");
	smsAisleGroupId=id;
	smsAisleGroupType = type;
}