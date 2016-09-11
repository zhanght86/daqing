<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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

<link href="<%=basePath%>/static/css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/static/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.idTabs.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/select-ui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/editor/kindeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".refresh").click(function() {
			window.location.reload(true);
		});
		$(".excel").click(function(){
			window.location.href = '<%=basePath%>/standingbook/tExcel.do?work_area='+$("#work_area").val()+'&construction_year='+$("#construction_year").val()+'&startDate='+$("#startDate").val()+'&endDate='+$("#endDate").val()+'&type='+$("#type").val();
		});
		$(".scbtn").click(function(){
			window.location.href = '<%=basePath%>/standingbook/forInsert.do';
		});
		$(".select1").uedSelect({
			width : 80			  
		});
	});
	
 
</script>
</head>
<!-- 二维数据台账信息 -->
<body>

	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>
			<li><a href="<%=basePath%>/standingbook/tData.do">原始数据台账信息</a></li>
		</ul>
	</div>

	<div class="formbody">
		<div id="usual1" class="usual">
		
			<ul class="toolbar">
				<li class="refresh"><span><img src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>
					<li class="excel"><span><img src="<%=basePath%>/static/images/Load.gif" /></span>导出Excel</li>
			</ul>
			<form action="<%=basePath%>/standingbook/rawData.do" method="post">
			<ul class="seachform">
				<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;项目名</label>
					<input name="work_area" type="text" class="scinput" value="${work_area }" />
				</li>
						<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;归档年度</label>
					<input name="construction_year" type="text" class="scinput" value="${construction_year }" />
				</li>
				<li>
					<label>刻录时间-开始</label>
					<input name="startDate" type="text" class="scinput" value="${startDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd', errDealMode:'0'})" />
				</li>
				<li>
					<label>结束</label>
					<input name="endDate" type="text"class="scinput" value="${endDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd', errDealMode:'0'})"/>
				</li>
					<li>
					<label>类型</label>
					<div class="vocation">
						<select id="type" name="type" class="select1">
							<option value="" <c:if test="${type == ''}">selected="selected"</c:if>>全部</option>
							 <option value="1" <c:if test="${type == '1'}">selected="selected"</c:if>>刻录</option>
								<option value="2" <c:if test="${type == '2'}">selected="selected"</c:if>>导出</option>
						</select>
					</div>
				</li>
				<li>
					<label>&nbsp;</label>
					<input type="submit" class="scbtn" value="查询" />
				</li>
				<shiro:hasPermission name="burn:tData">
				<li>
					<label>&nbsp;</label>
					<input type="button" class="scbtn"  value="补录数据"  />
				</li>
				</shiro:hasPermission>
					
			</ul>
			</form>

			<table class="tablelist">
				<thead>
					<tr>
						<th width="60">序号</th>
						<th width="200">项目名</th>
						<th width="100">处理单位</th>
						<th width="100">归档日期</th>
						<th width="100">数据大小(MB)</th>
						<th width="100">数据大小(GB)</th>
						<th width="100">刻盘数量</th>
						<th width="100">开始刻录时间</th>
						<th width="100">刻录结束时间</th>
						<th width="100">刻录用时(小时)</th>
							<th width="60">类型</th>
						<th width="60">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="page" varStatus="status">
						<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
							<td>${status.count }</td>
							<td>${page.work_area}</td>
							<td>${page.construction_unit}</td>
							<td>${page.construction_year}</td>
							<td> <fmt:formatNumber pattern="##.##">${page.data_quantity/1024/1024}</fmt:formatNumber></td>
							<td> <fmt:formatNumber pattern="##.####">${page.data_quantity/1024/1024/1024}</fmt:formatNumber></td>
							<td>${page.burn_count}</td>
							<td><fmt:formatDate value="${page.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${page.update_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatNumber pattern="##.###" >${(page.update_time.time - page.create_time.time)/1000/60/60}</fmt:formatNumber></td>
							<td>${page.type=='1'?'刻录':'导出'}</td>
							<td>
							<shiro:hasPermission name="burn:tData">
							<a href="<%=basePath%>/standingbook/delete.do?sid=${page.sid}&dataType=R&url=standingbook/tData.do">删除</a>
						
						</shiro:hasPermission>
						</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<div class="pagin">
		    	<div class="message">共<i class="blue">${pageInfo.total }</i>条记录，当前显示第&nbsp;<i class="blue">${pageInfo.pageNum }&nbsp;</i>页</div>
		        <ul class="paginList">
			        <c:if test="${pageInfo.pageNum == 1 }">
			        	<li class="paginItem"><a href="javascript:alert('没有上一页');"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pageNum != 1 }">
			        	<li class="paginItem"><a href="<%=basePath%>/dData/list.do?pageNum=${pageInfo.pageNum - 1}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:forEach items="${pageInfo.navigatepageNums }" var="num" varStatus="status">
			        	<c:if test="${status.count < 5 }">
			        		<li class="paginItem"><a charset="utf-8" href="<%=basePath%>/dData/list.do?pageNum=${num}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }">${num }</a></li>
			        	</c:if>
			        </c:forEach>
			        <c:if test="${pageInfo.pages == pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="javascript:alert('没有下一页');"><span class="pagenxt"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pages > pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="<%=basePath%>/dData/list.do?pageNum=${pageInfo.pageNum + 1}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }"><span class="pagenxt"></span></a></li>
			        </c:if>
		        </ul>
		    </div>
		</div>
	</div>

</body>
</html>