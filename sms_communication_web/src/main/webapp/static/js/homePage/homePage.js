var path = $("#path").val();

$(function () {
    loadHomePage();
    margeHomePage();
});

/**
 * 加载数据库保存的首页样式
 */
function loadHomePage() {
    $.post(path + "/smsHomePage/homePageList.do", function (data) {
        var obj = data["data"];
        if (obj != null) {
            myeditor.setData(obj.content);
        }
    });
}

/**
 * 新增修改
 */
function margeHomePage() {
    $("#btn_enter").click(function () {
        var content = myeditor.document.getBody().getHtml();// 文本编辑器中的文本
        var data = {"content": content};

        $.ajax({
            url: path + "/smsHomePage/from/merge.do",
            type: "post",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
            	
                if(data["retCode"]=="000000"){
                    alert("操作成功!");
                    var obj = data["data"];
                    if (obj != null) {
                        myeditor.setData(obj.content);
                    }
                    parent.$('#content-boxd').tabs('select',"首页");
                    var tab =parent.$("#content-boxd").tabs('getSelected');
                    parent.$('#content-boxd').tabs('update',{
                        tab:tab,
                        options : {
                        content : '<iframe scrolling="auto" frameborder="0"  src="'+path+'/smsHomePage/index.do" style="width:100%;height:100%;"></iframe>',
                        
                     }
                  });
                }else{
                    alert("操作失败!");
                }
            }, error: function () {
                alert("系统错误!");
            },
            complete: function () {
            }
        });
    });

}
