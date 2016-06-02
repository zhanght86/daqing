<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="refresh" content="60">

<link href="<%=basePath%>/static/css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/static/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.idTabs.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/select-ui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/editor/kindeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/common.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".click").click(function() {
			window.location.reload(true);
		});
		$(".select1").uedSelect({
				width : 150			  
		});
	});
	
	function showBurn(burninfo){
		alert(burninfo);
		
	}
	
	
</script>
<style>
/* 收缩展开效果 */
.text{line-height:22px;padding:0 6px;color:#666;}
.box h1{padding-left:10px;height:22px;line-height:22px;background:#f1f1f1;font-weight:bold;overflow:auto}
.box{position:relative;border:1px solid #e7e7e7;;overflow:auto;height:22px;}
</style>
</head>
<!-- 刻盘管理数据 -->
<body>

	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>
			<li><a href="<%=basePath%>/download/list.do">刻录管理</a></li>
		</ul>
	</div>
	
	<div class="formbody">
		<div id="usual1" class="usual">
			
			<ul class="toolbar">
				<li class="click"><span><img src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>
			</ul>
			<form action="<%=basePath %>/burn/list.do" method="post">
			<ul class="seachform">
				<li><label>&nbsp;&nbsp;&nbsp;&nbsp;刻录状态</label>
					<div class="vocation">
						<select name="burning_state" class="select1">
							<option value="" <c:if test="${burning_state == ''}">selected="selected"</c:if> >全部</option>
							<option value="1" <c:if test="${burning_state == 1}">selected="selected"</c:if> >初始化</option>
							<option value="2" <c:if test="${burning_state == 2}">selected="selected"</c:if> >下载数据</option>
							<option value="3" <c:if test="${burning_state == 3}">selected="selected"</c:if> >下载成功</option>
							<option value="4" <c:if test="${burning_state == 4}">selected="selected"</c:if> >下载失败</option>
							<option value="5" <c:if test="${burning_state == 5}">selected="selected"</c:if> >指令失败</option>
							<option value="6" <c:if test="${burning_state == 6}">selected="selected"</c:if> >刻录中</option>
							<option value="7" <c:if test="${burning_state == 7}">selected="selected"</c:if> >延迟刻录</option>
							<option value="8" <c:if test="${burning_state == 8}">selected="selected"</c:if> >刻录成功</option>
							<option value="9" <c:if test="${burning_state == 9}">selected="selected"</c:if> >刻录失败</option>
							<option value="10" <c:if test="${burning_state == 10}">selected="selected"</c:if> >其他</option>
						</select>
					</div>
				</li>
				<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;任务名称</label>
					<input name="task_name" id="task_name" type="text"class="scinput" value="${work_area }" />
				</li>
				<li>
					<label>&nbsp;</label>
					<input type="submit" class="scbtn" value="查询" />
				</li>
			</ul>
			</form>

			<table class="tablelist">
				<thead>
					<tr>
						<th>序号</th>
						<th>卷标号</th>
						<th>任务名称</th>
						<th>光盘类型</th>
						<!-- <th>数据来源</th>  -->
						<th>数据类型</th>
						<th>刻录机器</th>
						<th>刻录状态</th>
						<th>刻录数量</th>
						<th>刻录描述</th>
						<th>刻录时间</th>
						<!-- <th>更新时间</th> -->
					<!-- 	<th>刻录进度</th> -->
						<th>操作</th> 
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="page" varStatus="status">
						<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
							<td>${status.count }</td>
							<td>${page.volume_label}</td>
							<td>${page.task_name}</td>
							<td>${page.disc_type}</td>
							<!-- <td>
								<c:if test="${page.data_source == 1}">移动硬盘</c:if>
								<c:if test="${page.data_source == 2}">网络共享</c:if>
							</td> -->
							<td>
								<c:if test="${page.data_type == 'R'}">原始数据</c:if>
								<c:if test="${page.data_type == 'T'}">二维数据</c:if>
								<c:if test="${page.data_type == 'D'}">三维数据</c:if>
								<c:if test="${page.data_type == 'M'}">中间数据</c:if>
							</td>
							<td>${page.sp_name}</td>
							<td>
								<c:if test="${page.burning_state == 1}">初始化</c:if>
								<c:if test="${page.burning_state == 2}">下载数据</c:if>
								<c:if test="${page.burning_state == 3}">下载成功</c:if>
								<c:if test="${page.burning_state == 4}">下载失败</c:if>
								<c:if test="${page.burning_state == 5}">指令失败</c:if>
								<c:if test="${page.burning_state == 6}">刻录中</c:if>
								<c:if test="${page.burning_state == 7}">刻录延迟</c:if>
								<c:if test="${page.burning_state == 8}">刻录成功</c:if>
								<c:if test="${page.burning_state == 9}">刻录失败</c:if>
								<c:if test="${page.burning_state == 11}">手动取消</c:if>
								<c:if test="${page.burning_state == 10}">其他</c:if>
							</td>
							<td>${page.disc_number}</td>
							<td>${page.burn_desc}</td>
							<td><fmt:formatDate value="${page.burn_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<!-- <td><fmt:formatDate value="${page.update_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td> -->
						 </td>
						<%--  <td>
						 <div class="box" >${fn:replace(page.burn_progress, ",", "<br>")}
						
								</div>
						 </td> --%>
							<td>
								<a onclick="return confirm('确定要删除么？');" href="<%=basePath %>/burn/delete.do?volLabel=${page.volume_label}&dataType=${page.data_type }">删除</a>
								<c:if test="${page.burning_state != 8 and page.burning_state != 11}">
									<a onclick="return confirm('确定要取消么？');" href="<%=basePath %>/burn/cancel.do?mid=${page.mid}&volLabel=${page.volume_label}">取消任务</a>
								</c:if>
								<a href="<%=basePath %>/burn/detailData.do?dataType=${page.data_type}&volLabel=${page.volume_label}">详细数据</a>&nbsp;
								<c:if test="${page.burning_state == 8 or page.burning_state == 7 or page.burning_state == 6}">
									<a href="<%=basePath %>/burn/position.do?volLabel=${page.volume_label}">刻录位置</a>
								<%-- 	<a href="<%=basePath %>/burn/downloadfile.do?volLabel=${page.volume_label}&server=${page.burning_machine}">刻录文件</a> --%>
								</c:if>

								<input id="box" name="box" type="checkbox" value="id|username" onclick="checkonebox('checkall','box')"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<input id="checkall"     type="checkbox" value="" onclick="checkboxall('checkall','box')"/> 全选/全不选  
			
			<div class="pagin">
		    	<div class="message">共<i class="blue">${pageInfo.total }</i>条记录，当前显示第&nbsp;<i class="blue">${pageInfo.pageNum }&nbsp;</i>页</div>
		        <ul class="paginList">
			        <c:if test="${pageInfo.pageNum == 1 }">
			        	<li class="paginItem"><a href="javascript:alert('没有上一页');"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pageNum != 1 }">
			        	<li class="paginItem"><a href="<%=basePath%>/burn/list.do?pageNum=${pageInfo.pageNum - 1}&burning_state=${burning_state}"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:forEach items="${pageInfo.navigatepageNums }" var="num" varStatus="status">
			        	<c:if test="${status.count < 5 }">
			        		<li class="paginItem"><a charset="utf-8" href="<%=basePath%>/burn/list.do?pageNum=${num}&burning_state=${burning_state}">${num }</a></li>
			        	</c:if>
			        </c:forEach>
			        <c:if test="${pageInfo.pages == pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="javascript:alert('没有下一页');"><span class="pagenxt"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pages > pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="<%=basePath%>/burn/list.do?pageNum=${pageInfo.pageNum + 1}&burning_state=${burning_state}"><span class="pagenxt"></span></a></li>
			        </c:if>
		        </ul>
		    </div>
		    
		</div>
		

		
	</div>
	

</body>
</html>