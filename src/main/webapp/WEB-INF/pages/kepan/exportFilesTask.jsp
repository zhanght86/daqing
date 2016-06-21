<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path;
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link href="<%=basePath%>/static/css/style.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath%>/static/css/select.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/js/jquery.idTabs.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/js/select-ui.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/editor/kindeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/common.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$(".click").click(function() {
		window.location.reload(true);
	});
	$(".history").click(function() {
		window.location.href = history.go(-1);
	})
	$(".select1").uedSelect({
			width : 150			  
	});
	$(".tiptop a").click(function() {
		$(".tip").fadeOut(200);
	});
	$(".sure").click(function() {
		$(".tip").fadeOut(100);
	});
	$(".exportHistoryClick").click(function() {
		window.location.href = '<%=basePath%>/burn/mergeList.do?volLabel=' + $("#volLabel").val();
	});
	$(".exportClick").click(function() {
		$(".tip").fadeIn(200);
		var checkFiles=getallcheckedvalue('box');
		var sourcePath = document.getElementById('sourcePath');
		sourcePath.value=checkFiles;
	
	
	})
	});
</script>
</head>
<!-- 电子标签位置查看 -->
<body>
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>
			<li><a
				href="<%=basePath%>/burn/mergeList.do?volLabel=${volLabel}">导出管理</a></li>
		</ul>
	</div>

	<div class="formbody">
		<div id="usual1" class="usual">

			<ul class="toolbar">
				<li class="click"><span><img
						src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>

				<li class="history"><span><img
						src="<%=basePath%>/static/images/left.png" /></span>返回</li>
			
						
			</ul>

			<form action="<%=basePath%>/burn/exportFileList.do">
				<ul class="seachform">
				<!-- 	<li><label>&nbsp;&nbsp;&nbsp;&nbsp;文件名</label> <input
						id="fileName" name="fileName" type="text" class="scinput"
						style="width: 100px" /></li>

					<li><label>&nbsp;</label> <input name="" type="submit"
						class="scbtn" value="文件查询" /></li> -->
					<%-- 	<li><label>&nbsp;</label> <input name="" type="submit"
						class="scbtn" value="卷标查询"  onclick="javascript:this.form.action='<%=basePath%>/burn/exportVolList.do';"/></li> --%>
				</ul>
			</form>

			<table class="tablelist" >
				<thead>
					<tr>
					    <th width="50px">序号</th>
						<th width="900px">导出文件</th>
						<th width="160px">导出路径</th>
						<th width="150px">创建时间</th>	
						<th width="150px" >修改时间</th>
						<!-- 
						<th>操作用户</th>
						 -->
						<th width="70px">状态</th>
						<th width="50px">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="map" varStatus="status">
						<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
							<td>${status.count }</td>
							<td >${map.filelist}</td>						
							<td >${map.export_path}</td>
							<td >${map.create_time }</td>
							<td >${map.update_time}</td>
							<td>							
						    	<c:if test="${map.export_state == 0}">等待执行</c:if>
								<c:if test="${map.export_state == 1}">下载数据中</c:if>
								<c:if test="${map.export_state == 2}">下载成功</c:if>
								<c:if test="${map.export_state == 3}">下载失败</c:if>
								<c:if test="${map.export_state == 4}">导出成功</c:if>
								<c:if test="${map.export_state == 5}">导出失败</c:if>
								<c:if test="${map.export_state == 6}">关闭</c:if>
							</td> 
							
							<!-- <td>${map.c_user}</td>  -->
							<td><a onclick="return confirm('确定要删除么？');" href="<%=basePath %>/burn/deleteExportFile.do?eid=${map.eid}">删除</a></td>
						</tr>
					</c:forEach>
				</tbody>

			</table class="tablelist">
			
			<div class="pagin">
		    	<div class="message">共<i class="blue">${pageInfo.total }</i>条记录，当前显示第&nbsp;<i class="blue">${pageInfo.pageNum }&nbsp;</i>页</div>
		        <ul class="paginList">
			        <c:if test="${pageInfo.pageNum == 1 }">
			        	<li class="paginItem"><a href="javascript:alert('没有上一页');"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pageNum != 1 }">
			        	<li class="paginItem"><a href="<%=basePath%>/burn/exportFileTask.do?pageNum=${pageInfo.pageNum - 1}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:forEach items="${pageInfo.navigatepageNums }" var="num" varStatus="status">
			        	<c:if test="${status.count < 5 }">
			        		<li class="paginItem"><a charset="utf-8" href="<%=basePath%>/burn/exportFileTask.do?pageNum=${num}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }">${num }</a></li>
			        	</c:if>
			        </c:forEach>
			        <c:if test="${pageInfo.pages == pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="javascript:alert('没有下一页');"><span class="pagenxt"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pages > pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="<%=basePath%>/burn/exportFileTask.do?pageNum=${pageInfo.pageNum + 1}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }"><span class="pagenxt"></span></a></li>
			        </c:if>
		        </ul>
		    </div>
		
			</td>


		</div>
	</div>
	
</body>
</html>