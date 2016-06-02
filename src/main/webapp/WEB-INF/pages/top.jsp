<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="static/css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="static/js/jquery.js"></script>
<script type="text/javascript">
$(function(){	
	//顶部导航切换
	$(".nav li a").click(function(){
		$(".nav li a.selected").removeClass("selected")
		$(this).addClass("selected");
	})
})	
</script>


</head>

<body style="background:url(static/images/topbg.gif) repeat-x;">

    <div class="topleft">
    <a href="to.do?file=index" target="rightFrame"><img src="static/images/logo.png" title="系统首页" /></a>
    </div>
        
    <ul class="nav">
    <!-- 
    <li><a href="default.html" target="rightFrame" class="selected"><img src="static/images/icon01.png" title="信息管理" /><h2>信息管理</h2></a></li>
     -->
    </ul>
            
    <div class="topright">    
    <ul>
    	<li><a href="logout.do" target="_parent"></a></li>
    </ul>
     
    <div class="user">
    <span>${userName }</span>
    </div>    
    </div>

</body>
</html>
