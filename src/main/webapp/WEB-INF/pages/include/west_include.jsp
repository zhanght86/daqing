<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);	
%>

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
