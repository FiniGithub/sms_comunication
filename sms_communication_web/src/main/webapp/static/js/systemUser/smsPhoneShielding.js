$(function() {

	load_data(); // 1.查询数据

})

//
function load_data() {
	ajaxCall({
		url : '/phoneShielding/getPhoneItems.do',
		type : "post",
		data : JSON.stringify(null),
		success : function(data) {
			if (checkRes(data)) {
				var arr = data["data"]["data"];
				var phone = arr["phone"];
				var id = arr["id"];
				$("#i_id").val(id);
				$("#i_phone").val(phone);
			}
		},
		error : function() {

		},
		complete : function() {

		}
	});
}