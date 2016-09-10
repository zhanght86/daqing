<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);	
%>
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
    	<li><cite></cite><a href="burn/exportFileList.do" target="rightFrame">文件检索列表</a></li>
    	<li><cite></cite><a href="burn/exportFileTask.do" target="rightFrame">文件导出任务列表</a></li>
    	<!-- 
        <li class="active"><cite></cite><a href="to.do?file=index" target="rightFrame">首页模版</a><i></i></li> -->
        <li><cite></cite><a href="rawData/list.do" target="rightFrame">原始数据清单</a><i></i></li>
        <li><cite></cite><a href="tData/list.do" target="rightFrame">二维处理成果清单</a><i></i></li>
        <li><cite></cite><a href="dData/list.do" target="rightFrame">三维处理成果清单</a><i></i></li>
         
        <li><cite></cite><a href="mData/list.do" target="rightFrame">中间成果数据</a><i></i></li>
        <!-- -->
        <!-- <li><cite></cite><a href="download/list.do" target="rightFrame">数据下载查询</a><i></i></li>  -->
        <li><cite></cite><a href="rawData/applicationlist.do" target="rightFrame">数据申请</a><i></i></li>
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
    
     <dd>
    <div class="title">
    <span><img src="static/images/leftico01.png" /></span>系统管理
    </div>
    	<ul class="menuson">
        	<li><cite></cite><a href="user/query.do" target="rightFrame">用户列表</a><i></i></li>
        	<li><cite></cite><a href="role/query.do" target="rightFrame">角色列表</a><i></i></li>
        	<li><cite></cite><a href="permission/query.do" target="rightFrame">功能列表</a><i></i></li>
        	<li><cite></cite><a href="organization/into.do" target="rightFrame">机构列表</a><i></i></li>
        </ul>
    </dd>
    
    
	<div id="westDiv" class="easyui-accordion" data-options="multiple:true,animate:false,border:false" >
	<c:forEach items="${storageSysMenus}" var="modelItems">
		<shiro:hasPermission name="${modelItems.permission}">
			<div title="${modelItems.permissionName}" data-options="selected:true,iconCls:'icon-permission'" class="menu_type">
		       	<c:forEach items="${modelItems.menuItems}" var="menuItem">
		       		<shiro:hasPermission name="${menuItem.permission}">
		       			<span calss="menu_item">
		       			<a href="javascript:openTab('t_${menuItem.permissionCode}',
		       			 '${menuItem.permissionName}','<%=basePath%>${menuItem.permissionUrl}',
		       			 'icon-tabs')" class="easyui-linkbutton menu-btn" data-options="plain:true,iconCls:'icon-open'" >
		       			 ${menuItem.permissionName }</a></span>
		       		</shiro:hasPermission>
		       	</c:forEach>
			</div>
		</shiro:hasPermission>
	</c:forEach>
</div>

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
