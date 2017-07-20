/**
 * 表单校验
 * @param val
 * @param obj
 * @param msg
 * @returns {Boolean}
 */
function checkInput(val,obj,msg){
	if(val==null || val=="" ){
//		obj.html(msg);
		return false;
    }
//    obj.html("");
    return true;
}

function checkInputMsg(val){
	if(val==null || val=="" ){
		return false;
    }
    return true;
}