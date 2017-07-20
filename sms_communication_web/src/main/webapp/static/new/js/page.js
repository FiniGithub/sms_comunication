/*//index
$(function() {
	
	$(".footer-box").load("footer.html");
//	$(".top-box").load("header.html");
	uploadAnimate();//上传文件进度条动画
	menuOpen(); //菜单变化
	pageInp();//页码框
	paswInp();//密码框	
	fixMidel(".regiger-box"); //动态固定注册框的位置	
	regBox(); //注册弹窗
	tsAnimate(); //提示动画	
	slideAnimate(".ptsbtn", ".ptsbox"); //点击滑动动画;
	moveAnimate(".uer-name-box", ".usermenu");//移入显示
	alwdInp(); //弹窗按钮
	clearbtn(); //清除按钮
	selebox(); //选择框
	alwdClose(); //弹窗关闭
	selectDay(); //日期选择插件	
	alone();//弹窗类型一	
	inpLength();//输入字符数
	test();
	tsBtn();//推送按钮
	newsBtn();//消息按钮
	
})
function newsBtn(){//消息按钮
	$(document).on('click',function(){ 
    $(".newscontent").hide() 
}); 
	$(".news-ul").on("click",function(e){	
		 stopPropagation(e); 		
		if(!$(this).children().children(".newscontent").is(":animated")) {
			$(this).children().children(".newscontent").is(":hidden") ? $(this).children().children(".newscontent").slideDown() : $(this).children().children(".newscontent").slideUp();
		}
	})
}

function tsBtn(){//推送按钮
	$(".sc-ul li").on("mouseover",tmoveIn);
	$(".sc-ul li").on("click",function(){
		$(".sc-ul li").off("mouseover");
		var type=$(this).attr("data-dxtype");
		$("#dxdtype").val(type);
		$(".sc-ul li").off("click")
	})
}
function tmoveIn(){
	$(this).addClass("key-on").siblings().removeClass("key-on")
	$(this).children(".dxtype").show()
	$(this).siblings().children(".dxtype").hide();
}
function test(){
	$(".nrsm-inp").on("input",function(){
		console.log(event)
	})
}
function pageInp(){
	$(".pagenum").on("keydown",onlyNum)
}
function addtitle(obj,arr){//菜单按钮
	var titl=$(obj).attr("date-tit");	
	 $("."+titl).text(arr);
	 $(".head-btn").removeClass("head-onactive");
	 $("."+titl).parent().addClass("head-onactive");
}

function pageTurn(){//页面跳转
	$(".fsxq-btn").on("click",function(){
		var pcnum=$(this).attr("data-pcnum");
		$.ajax({
		type: "POST",
		url: "",
		data:{},			
		success: function(data, textStatus) {
				window.location.href ="/dlpt328/fsxq.html"
		}		
	});
	})
}

function onlyNumZ(){//正则限制输入字母和数字
	var reg=/^[0-9a-zA-Z]*$/g;	
	 if(!reg.test(event.key)){
	 event.returnValue=false; 	 	
	 }

}
 
 function paswInp(){
 $(":input[type=password]").on("keydown",onlyNumZ) ;
 }
 
function moveAnimate(btn,box){
	$(btn).hover(function(){
		$(box).show()
	},function(){
		$(box).hide()
	})
}

function inpLength(){
	$(".dxcontent-xg-txt").on("input",function(){
		var l=$(this).val().length;
		$(".dxlength").text(l);
	})
};


function uploadAnimate(){
	var bfb=0;	
	var t=setInterval(function(){
		var sum=parseInt($("#jdall").text());
		var cgnum=parseInt($("#jdcghm").text());
		var jerror=parseInt($("#jderror").text());
		var repet=parseInt($("#jdrepet").text());
		bfb=parseInt((cgnum+jerror+repet)/sum*100);
		$(".progebfb").text(bfb);
		$(".proge").css("width",bfb+"%");
		if((cgnum+jerror+repet)==sum){
			clearInterval(t);
		}		
	},1000)
	
}	

function menuOpen() { //菜单变化
	$(".api-box").on("click",function(){
		
		$(".head-btn").removeClass("head-onactive");
		$(this).addClass("head-onactive");
		
	})
	$(".head-on").on("mousemove", moveIn);
	$(".d").on("mouseleave", moveOut)
}

function moveIn() {

	$(".head-cha,.subbtn").hide();
	$(".head-on").show();
	$(this).hide().siblings(".d").children("ul,.head-cha").css("display", "block");

}
 
function moveOut() {
	$(".head-cha,.subbtn").hide().parent().siblings(".head-on").show();
}

function fixMidel(obj) { //obj为JQ选择器
	var w = $(obj).width() //注册框宽度;
	var h = $(obj).height() //注册高度；
	$(obj).css({
		position: "absolute",
		top: "50%",
		left: "50%",
		"margin-left": -0.5 * w + "px",
		"margin-top": -0.5 * h + "px",
		display: "block"
	})

}
function onlyNum() 
{//输入数字类型 	
if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105)||(event.keyCode==8))) 
event.returnValue=false; 
}
function alwdInp() { //弹窗按钮
	$(".alwd-inp").on("click", function() {
		fixMidel(".alwindow-box"); //弹窗动态固定		
		$(".alwindow-box").hide();
		$(".regiger-bg").show();		
	})
}

function alwdClose() { //弹窗关闭
	$(".alwd-close,.alwd-btn-close").on("click", function() {
		$(".regiger-bg,.alwindow-box").hide();
	})
}
function countBtn(obj,time){//倒计时
	var time=time;
	$(obj).on("click",function(){
		$(this).attr("disabled","disabled");
		var timer=$(this);
		var cleart=setInterval(function(){
			time--;
			timer.val(time);				
			if(time==0){
				time=60;
				clearInterval(cleart);
				timer.val("重新获取")
				timer.removeAttr("disabled");				
			}
		},1000);
	})
	};
function regBox() { //注册弹窗
		var time=60;
	$(".yz-btn,.key-inp-yzbtn").on("click",function(){
		$(this).attr("disabled","disabled");
		var timer=$(this);
		var cleart=setInterval(function(){
			time--;
			timer.val(time);				
			if(time==0){
				time=60;
				clearInterval(cleart);
				timer.val("重新获取")
				timer.removeAttr("disabled");				
			}
		},1000);
	})
	$(".regiger-btn").on("click", function() {
		$(this).addClass("regiger-active").siblings(".enty-btn").removeClass("regiger-active");
		$(".r-box").show().siblings(".e-box").hide();
	})
	$(".enty-btn").on("click", function() {
		$(this).addClass("regiger-active").siblings(".regiger-btn").removeClass("regiger-active");
		$(".e-box").show().siblings(".r-box").hide();
	})
}

function clearbtn() { //清除按钮
	$(".clearbtn").on("click", function() {
		$(".pho-load-result").val("");
	})
}
function otherHide(obj){
	
}
function stopPropagation(e) { 
　　if (e.stopPropagation) 
　　　　e.stopPropagation(); 
　　else 
　　　　e.cancelBubble = true; 
} 
function selebox() { //选择框
	$(document).on('click',function(){ 
    $(".xlbox").hide() 
}); 
	$(".xlbtn").on("click", function(e) {
		 stopPropagation(e); 
		if(!$(this).siblings(".xlbox").is(":animated")) {
			$(this).siblings(".xlbox").is(":hidden") ? $(this).siblings(".xlbox").show() : $(this).siblings(".xlbox").hide();
		}
	});
		
}

function alone(){
	$(".alwd-tc2").on("click",function(){
		fixMidel(".cztc"); 
		$(".alwindow-box").hide();
		$(".regiger-bg").show();	
		var taget=$(this).attr("data-inp");
		console.log(taget)
		$("."+taget).show();
	})
	$(".alwd-tc1").on("click",function(){
		
		var taget=$(this).attr("data-inp");
		
		$("."+taget).show();
	})
}

function addKey(obj, arr) { //下拉框内容按钮	
	var id = $(obj).attr("data-inp");
	
	$("#" + id).val(arr);
	$(obj).parent().hide();
}

function sendAjax(arr) { //发送请求
	console.log(arr)
	$.ajax({
		type: "get",
		url: arr,
//		data: ""
//		timeout: 20000,

//		beforeSend: function(XMLHttpRequest) {},
//		success: function(data, textStatus) {
		success: function(data){
			console.log(data)
		},
//		complete: function(XMLHttpRequest, textStatus) {
//			
//		},
		error: function(msg) {
			console.log(msg)
		}
	});
}

function slideAnimate(obj, hd) { //obj为JQ选择器 //滑动动画
	$(obj).on("click", function() {
		if(!$(hd).is(":animated")) {
			$(hd).is(":hidden") ? $(hd).slideDown() : $(hd).slideUp();
		}
	})

}

function tsAnimate() { //提示动画
$('.dx-tishi-img').mousemove(function(){
	$(this).hide()
})
	setTimeout(function() {
		$('.dx-tishi-img').fadeOut(1000);		
	}, 2000)

}

function selectDay() { //日期选择插件
	if($('#stardate').length==0&&$("#seleday").length==0) {
		return;
	}
	$('#stardate').datetimepicker({

		format: 'yyyy-mm-dd ',

		minView: "month",
		initialDate: new Date(), //初始化当前日期
		autoclose: true, //选中自动关闭
		todayBtn: true, //显示今日按钮    
		language: 'zh-CN'
	});
	$('#enddate').datetimepicker({
		format: 'yyyy-mm-dd ',
		minView: "month", //设置只显示到月份
		initialDate: new Date(), //初始化当前日期
		autoclose: true, //选中自动关闭
		todayBtn: true, //显示今日按钮    
		language: 'zh-CN'
	});
	$('#seleday').datetimepicker({

		format: 'yyyy-mm-dd hh:ii',

		initialDate: new Date(), //初始化当前日期
		autoclose: true, //选中自动关闭
		todayBtn: true, //显示今日按钮    
		language: 'zh-CN',
		pickerPosition: "top-right"
	});
}

function adrouter(obj) {
	$("#textfield").val($(obj).val());
	console.log($("#upload-form").serialize())
	$(".proge").animate({
		width: "100%",
	}, 2000)

	//	ajax{(
	//		type : "POST",  
	//       url : "",  
	//       data : $(obj).serialize(),
	//	)}
}

*/