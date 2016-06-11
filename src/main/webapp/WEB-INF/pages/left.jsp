<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="static/css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="static/js/jquery.js"></script>

<script type="text/javascript">
$(function(){	
	//导航切换
	$(".menuson li").click(function(){
		$(".menuson li.active").removeClass("active")
		$(this).addClass("active");
	});
	
	$('.title').click(function(){
		var $ul = $(this).next('ul');
		$('dd').find('ul').slideUp();
		if($ul.is(':visible')){
			$(this).next('ul').slideUp();
		}else{
			$(this).next('ul').slideDown();
		}
	});
})	
</script>


</head>

<body style="background:#f0f9fd;">
	<div class="lefttop"><span></span>数据</div>
    
    <dl class="leftmenu">
    <dd>
    <div class="title">
    <span><img src="static/images/leftico01.png" /></span>管理信息
    </div>
    	<ul class="menuson">
    	<li class="active"><cite></cite><a href="burn/list.do" target="rightFrame">刻录状态</a><i></i></li>
    	<li><cite></cite><a href="burn/mergeList.do" target="rightFrame">导出列表</a></li>
    	<li><cite></cite><a href="burn/exportFileList.do" target="rightFrame">文件导出列表</a></li>
    	<li><cite></cite><a href="burn/exportFileTask.do" target="rightFrame">文件导出任务列表</a></li>
    	<!-- 
        <li class="active"><cite></cite><a href="to.do?file=index" target="rightFrame">首页模版</a><i></i></li> -->
        <li><cite></cite><a href="rawData/list.do" target="rightFrame">原始数据清单</a><i></i></li>
        <li><cite></cite><a href="tData/list.do" target="rightFrame">二维处理成果清单</a><i></i></li>
        <li><cite></cite><a href="dData/list.do" target="rightFrame">三维处理成果清单</a><i></i></li>
         
        <li><cite></cite><a href="mData/list.do" target="rightFrame">中间成果数据</a><i></i></li>
        <!-- -->
        <!-- <li><cite></cite><a href="download/list.do" target="rightFrame">数据下载查询</a><i></i></li>  -->
        
        </ul>
    </dd>
    
    
    <dd>
    <div class="title">
    <span><img src="static/images/leftico01.png" /></span>台账管理
    </div>
    	<ul class="menuson">
        	<li><cite></cite><a href="standingbook/rawData.do" target="rightFrame">原始数据台账</a><i></i></li>
        	<li><cite></cite><a href="standingbook/tData.do" target="rightFrame">二维数据台账</a><i></i></li>
        	<li><cite></cite><a href="standingbook/dData.do" target="rightFrame">三维数据台账</a><i></i></li>
        	<li><cite></cite><a href="standingbook/mData.do" target="rightFrame">中间数据台账</a><i></i></li>
        </ul>
    </dd>
    
    <!-- 
    <dd>
    <div class="title">
    <span><img src="static/images/leftico03.png" /></span>系统设置
    </div>
    	<ul class="menuson">
        <li><cite></cite><a href="user/list.do" target="rightFrame">用户管理</a><i></i></li>
         <li><cite></cite><a href="user/addInit.do" target="rightFrame">添加用户</a><i></i></li>
        </ul>
    </dd>
     -->
    </dl>
    
</body>
</html>
