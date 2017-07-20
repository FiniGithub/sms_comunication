
$(function(){
			var path=$("#path").val();	
			scrollTab();
			

			

			$(".page-box li").click(function(){//左侧菜单点击
				
				$(this).addClass("slec").siblings().removeClass("slec");
					var title=$(this).children("a").html();
					var url=path+$(this).children("a").attr("data-url");
					openTab(title,url);//添加TABS
					tabIndex();//菜单选项卡效果
					
					
					// console.log(2);
			})
//			indexBtn();
		});
	function tabIndex(){
   		
		
		$(".tabs li").each(function(){
			var index=$(this).index()+1; 
			$(this).css("z-index","-"+index);
		});

		
	}
	
function addResh(){
	$(".tabs-inner .icon-reload").click(function(){
   		var path=$("#path").val();
   		var index=$(this).parent().parent().index()-1; 
   		console.log(index);
   		var rt=$(this).siblings("span").text();
   		var rh='';   		
   		$(".page-box a").each(function() {
   			if($(this).text()==rt){
   				rh=path+$(this).attr("data-url");
   				refreshBtn({tabTitle:rt,url:rh,index:index});//刷新按钮事件
   				return false;
   			}   			
   		});       
	})
}
	function openTab(title,href){
		var t=title;
		var h=href;
	
    var e = $('#content-boxd').tabs('exists',t);
    if(e){
        $("#content-boxd").tabs('select',t);
        var tab = $("#content-boxd").tabs('getSelected');
     
        $('#content-boxd').tabs('update',{
             tab:tab,
             options : {
                  content : '<iframe scrolling="auto" frameborder="0"  src="'+h+'" style="width:100%;height:100%;"></iframe>',
             
             }
        });

        
    }else{
        $('#content-boxd').tabs('add',{
            title:t,
            content:'<iframe name="indextab" scrolling="auto" src="'+h+'" frameborder="0" style="width:100%;height:100%;"></iframe>',
            idSeed:"0",
           	
            closable:true            
        });
    }
}
	function scrollTab(){
//		$(".panel").click(function(){
//			alert()
//			console.log($(this).is(":animated")) 
//		})

				var w=$(".tabs li").eq(1).width()+4;
				$("#left-m").accordion({
					 animate:false
				})
			
//				  $("#left-m").accordion('panels').panel("resize",{top:28})
				  
//				
//			$("#content-boxd").tabs({scrollIncrement:w,id:1})
			
			
			}
	function indexBtn(){
			
			$("#content-boxd").tabs('resize',{width:0})
		
	}
function refreshTab(cfg){    
        var refresh_tab = cfg.tabTitle?$('#content-boxd').tabs('getTab',cfg.tabTitle):$('#content-boxd').tabs('getSelected');
        console.log(refresh_tab)
        if(refresh_tab && refresh_tab.find('iframe').length > 0){    
        var _refresh_ifram = refresh_tab.find('iframe'); 
        for(var i=0;i<_refresh_ifram.length;i++){
        	 var refresh_url = cfg.url?cfg.url:_refresh_ifram[i].src;    
        //_refresh_ifram.src = refresh_url;    
        _refresh_ifram[i].contentWindow.location.href=refresh_url; 
        
        }    
        }      
    }    
    
 function refreshBtn(cfg){//刷新按钮事件
 	var refresh_tab = cfg.tabTitle?$('#content-boxd').tabs('getTab',cfg.tabTitle):$('#content-boxd').tabs('getSelected');
        if(refresh_tab && refresh_tab.find('iframe').length > 0){    
        var _refresh_ifram = refresh_tab.find('#gen-tabs-panel'+cfg.index).children("iframe")[0];//初始TAB数量不为1则需要判断
        console.log(_refresh_ifram);
        var refresh_url = cfg.url?cfg.url:_refresh_ifram.src;    
        //_refresh_ifram.src = refresh_url;    
        _refresh_ifram.contentWindow.location.href=refresh_url;    
            alert();
        }    
 }
  