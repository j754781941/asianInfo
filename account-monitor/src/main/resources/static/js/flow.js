var colorList;
var count;

$(function(){
  count= 4; 
  loadFlow(count);
  checkColor(colorList);
    
})
 

//页面跳转
function methodBtn(index, method, end) {
    var fFor;
    if (end != true) {
        if (method == "back") {
            if (index == 1) {
                fFor = ".for" + String.fromCharCode(index + 65);
            } else {
                fFor = ".for" + String.fromCharCode(index + 64);
            }
            $(fFor).removeClass("for-cur");
            loadFlowDiv(index-1);
            checkColor("default");
        } else if (method == "forward") {
            fFor = ".for" + String.fromCharCode(index + 65);
            $(fFor).addClass("for-cur");
            loadFlowDiv(index+1);
            checkColor(colorList);
        }
    } else if (end == true) {
       
    }

}
//确定流程颜色状态
function checkColor(color) {
	
    if (color != "default") {
        $(".flowList.for-cur").css({ "border": "2px solid #1ABB9C"});
        $(".flowList.for-cur,.flowListBox .for-cur em").css({ "background-color": "#1ABB9C"});
        $(".flowListBox .for-cur em").css({ "border": "0px none #000" }); 
    } else {
        $this = $('.flowList:not(.for-cur)');
        $this.css({ "border": "2px solid #ccc", "background-color": "#ccc" });
        $this.children("em").css({ "background-color": "#ccc" }); 
    }
    /*让当前选中步骤变为深蓝色*/
	 var obj=$('.for-cur:last');
	 obj.css({ "border": "2px solid #34495e", "background-color": "#34495e" });
	 obj.children("em").css({ "background-color": "#34495e" }); 
}
//确定步骤的宽度占比
function fixWidth(count) {
    var part = parseInt(100 / count) + "%";
    $(".flowListBox .flowList").css("width", part);
}
//加载步骤
function loadFlow(count){
  var flowFor;
  var flowVar="";
  for(var i=1;i<=count;i++){
    flowFor="for"+String.fromCharCode(i+64);
    if(i==1){
      flowVar += "<div class='flowList for-cur "+flowFor +"' style='position:relative'>\n";
      flowVar += "	<em style='position:absolute;left:35%'>"+i+"</em><br/><strong style='position:absolute;left:40%'>第"+i+"步</strong>\n";
      flowVar += "</div>\n";
    }else{
      flowVar += "<div class='flowList "+flowFor +"' style='position:relative'>\n";
      flowVar += "	<em style='position:absolute;left:35%'>"+i+"</em><br/><strong style='position:absolute;left:40%'>第"+i+"步</strong>\n";
      flowVar += "</div>\n";
    }

  }
  $(".flowListBox").html(flowVar);
  fixWidth(count);
  loadFlowDiv(1);
  checkBtn(1,count);
}
//加载内容详情
function loadFlowDiv(index){ 
 /* var strVar = "";
 if(index==1){strVar="aaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa"}
 if(index==2){strVar="bb<br>bbbbbb<br>22222222<br>22222222222<br>222222222<br>222222<br>2222222<br>222222<br>222222"}
 if(index==3){strVar="cc"}
 if(index==4){strVar="dd"}
 $("#iList").html(strVar);*/
 if(index==1){$("#contA").removeClass("contentList");$("#contA").siblings().addClass("contentList")}
 if(index==2){$("#contB").removeClass("contentList");$("#contB").siblings().addClass("contentList")}
 if(index==3){$("#contC").removeClass("contentList");$("#contC").siblings().addClass("contentList")}
 if(index==4){$("#contD").removeClass("contentList");$("#contD").siblings().addClass("contentList")}


}
//上一步下一步按钮点击事件
var maxstep=1;
function checkBtn(index, count) {
	
    $("#btnBack").addClass("disabled");
	 /*默认进来隐藏上一步按钮*/
	$("#btnBack").hide();	
	/*下一步点击事件*/ 
    $("#btnNext").click(function () {			 
        methodBtn(index++, 'forward', false);
		if(index>maxstep){
			maxstep=index;
			 
			}
        if (index != 1) {
			/*非第一步的时候，显示上一步*/
            $("#btnBack").removeClass("disabled");
			$("#btnBack").show();
        }
        if (index >= count) {  
		/*到最后一步时 去掉下一步 显示上一步和完成*/
            $("#btnNext").hide();
            $("#btnBack").show();
			$("#btnok").show();
        }
        refreshBack(index);
    });
	/*上一步点击事件*/
    $("#btnBack").click(function () {
        if (refreshBack(index) > 1) {
            methodBtn(index--, 'back', false);
			 $("#btnNext").show();
            if (index == 1) {
				/*如果当前为第一步 则给上一步添加disabled属性*/
                $("#btnBack").addClass("disabled");
            } 
			/* for(var num=1;num<=maxstep;num++){
				 if(num==maxstep+1){
					  $(".flowList.for-cur").css({ "border": "2px solid #1ABB9C"});
        			  $(".flowList.for-cur,.flowListBox .for-cur em").css({ "background-color": "#1ABB9C"});
					 }
				 }*/
        }
    });
}
/*上一步*/
function refreshBack(index) {
    return index;

}
