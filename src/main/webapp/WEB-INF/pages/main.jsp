<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<html>
<head> 
<c:import url="include/headers.jsp"></c:import>
<script type="text/javascript">
$(function(){
	//eLoading();
});

function reportDownload(){
	sLoading();
	window.location.href =  '<%=basePath%>/report/reportDownload.do';
}

function showReportType(){
	window.location.href =  '<%=basePath%>/report/querytype.do';
}
</script>
</head>
<body class="easyui-layout">
    <div region="north" style="height:110px;width: 100% "><c:import url="include/north_include.jsp"></c:import></div>  
    <div region="south" style="height:40px;padding-top: 5px;"><c:import url="include/south_include.jsp"></c:import></div>    
    <div region="west" split="true" title="&nbsp;导航菜单"  style="width:220px;padding: 3px;"><c:import url="include/west_include.jsp"></c:import></div>  
    <div region="center" >
    	<div class="easyui-tabs" id="centerTab" data-options="fit:true,border:false">
    		<div title="主页" data-options="iconCls:'icon-home'" style="padding: 2px;overflow: hidden;">
    			<div style="margin: 20px;">
    				<h2>欢迎来到存储平台管理系统</h2>
    			</div>   			
    			
    			<div class="easyui-panel" title="快捷操作" data-options="iconCls:'icon-large-clipart',fit:true" style="padding: 2px;">
				</div>
    		</div>
    	</div>
    </div> 
</body>
</html>