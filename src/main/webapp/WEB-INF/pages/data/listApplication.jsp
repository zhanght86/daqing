<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<script type="text/javascript" src="<%=basePath%>/static/js/common.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
		$(".refresh").click(function() {
			window.location.reload(true);
		});
		
		$(".click").click(function() {
			$(".tipnew").fadeIn(200);
		});

		$(".tiptop a").click(function() {
			$(".tipnew").fadeOut(200);
		});

		$(".sure").click(function() {
			$(".tipnew").fadeOut(100);
		});

		$(".cancel").click(function() {
			$(".tipnew").fadeOut(100);
		});
		$(".select1").uedSelect({
			width : 100			  
		});
		
		$(".excel").click(function(){
			window.location.href = '<%=basePath%>/rawData/excel.do?work_area='+$("#work_area").val()+'&construction_year='+$("#construction_year").val();
		});
		
		$(".table").click(function(){
			window.location.href = '<%=basePath%>/doc/原始资料清单.xls';
		});
		
		
		$(".applicationSubmit").click(function() {
				
			    $(".tipnew").fadeIn(200);
				var checkFiles=getallcheckedvalue('box');
				document.getElementById('volLabel').value=checkFiles;
				alert(document.getElementById('volLabel').value);
				//window.location.href = '<%=basePath%>/data/ApplicationData.do?volLabel=' +checkFiles;			
			
			});
		
		$(document).on('click','.button',function(){ 
			/* 		$(".aaa").click(function(){ */
			 
				  var currentPath=$("#currentPath").val() ;
				 // alert($("#currentPath").val());
				/*   var currentPath1= currentPath.substring(0, currentPath.lastIndexOf("/"));
				  alert(currentPath.split('/')[3]); */
				  var projectName=currentPath.substring(0,currentPath.substr(1).lastIndexOf('/')+1);
				 // alert(projectName);
				    var arg = {};
				    var arg1="path";
				    arg[arg1]=projectName;
				  
				  
									  $.post("<%=basePath%>/data/openDir.do", arg,function(data,status){
									  
									  var json =eval(data); 
								      
									  $("#info").html(""); 
										 var table="<div> <table>"
									  $.each(data, function(i, item) {
										  table+="<tr> <td><input type='radio' name='dir' value='"+item+"' onchange='getDataPath1(this)'></input></td> <td><a href='#' class='aaa' name='button' value='"+item+"'>"+item+"</a></td></tr>";
										
								        });
								
									table+=("</table> </div>");
									 
									  /*  $("#info").append("<table> <tr> <td> "+item+"</td></tr></table>");*/
									  document.getElementById("info").innerHTML = table;
									
									/*  alert("Data: " + data + "\nStatus: " + status); */ 
								  });
						});
						
						
					$(document).on('click','.aaa',function(){ 
						/* 		$(".aaa").click(function(){ */
						 
							 
												  $.post("<%=basePath%>/data/openDir.do", {path:$(this).attr("value")},function(data,status){
												  
												  var json =eval(data); 
									 
												  $("#info").html(""); 
													 var table="<div><table>"
												  $.each(data, function(i, item) {
													  table+="<tr> <td><input type='radio' name='dir' value='"+item+"' onchange='getDataPath1(this)'></input></td> <td><a href='#' class='aaa' name='button' value='"+item+"' onclick='setCurrent(\""+item+"\")'>"+item+"</a></td></tr>";
													
											        });
											
												table+=("</table></div>");
												 
												  /*  $("#info").append("<table> <tr> <td> "+item+"</td></tr></table>");*/
												  document.getElementById("info").innerHTML = table;
												
												/*  alert("Data: " + data + "\nStatus: " + status); */ 
											  });
									});
				});
				
				/*  */
				
				function getDataPath1(obj)
				{
				 document.getElementById("exportPath").value =obj.value;  //显示获取结果
				}

				
				 function setCurrent(obj){
					 document.getElementById("currentPath").value=obj;
				 }
				


	function linkSubmit(linkObject) {
		var formObject = document.createElement('form');
		document.body.appendChild(formObject);
		formObject.setAttribute('method', 'post');
		formObject.action = linkObject;
		formObject.submit();
	}
	
	
	

</script>
</head>
<body>

	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>

		</ul>
	</div>



	<div class="formbody">
		<div id="usual1" class="usual">
			
			<ul class="toolbar">
				<li class="refresh"><span><img src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>				
				
				
			</ul>
			<form action="<%=basePath%>/data/applicationQuery.do" method="post">
				<input name="volume_label" value="${volume_label }" type="hidden" />
			<ul class="seachform">
				<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;工区</label>
					<input name="project_name" id="project_name" type="text"class="scinput" value="${project_name }" />
				</li>
				<li>
					<label>卷标号</label>
					<input name="vol_label" id="vol_label" type="text" class="scinput" value="${vol_label }" /><!-- onclick="WdatePicker({dateFmt:'yyyy-MM', errDealMode:'0'})" -->
				</li>
				
				<li>
					<label>申请人</label>
					<input name="application_user" id="application_user" type="text" class="scinput" value="${application_user }" /><!-- onclick="WdatePicker({dateFmt:'yyyy-MM', errDealMode:'0'})" -->
				</li>

				<li>
					<label>&nbsp;</label>
					<input name="" type="submit" class="scbtn" value="查询" />
				</li>
			</ul>
			</form>

			<table class="tablelist">
				<thead>
					<tr>
						<th width="50px">序号</th>
						<th width="120px">卷标号</th>
						<th width="300px">工区</th>
						<th width="100px">用户</th>
						<th width="100px"> 申请人</th>
						<th width="100px">联系电话(s)</th>
						<th width="100px">邮件</th>
						<th width="200px">备注说明</th>
						<th width="150px">申请时间</th>
						<th width="100px">状态</th>
						
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${pageInfo.list }" var="page" varStatus="status">
					<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
							<td>${status.count }</td>
							<td>&nbsp;${page.vol_label }</td>
							<td>&nbsp;${page.project_name }</td>
							<td>&nbsp;${page.user }</td>
							<td>&nbsp;${page.application_user }</td>
							<td>&nbsp;${page.phone }</td>
							<td>&nbsp;${page.email }</td>
							
							
							<td title="${page.remark}">
							<c:if test="${fn:length( page.remark)>20 }">
							${ fn:substring( page.remark ,0,20)}. . . . . . </c:if> 
							<c:if test="${fn:length( page.remark)<=20 }">
							${page.remark}</c:if> 
							</td>
							<td>&nbsp;${page.create_date }</td>
								
							
							<td>
								<c:if test="${page.application_status == 0}">未处理</c:if>
								<c:if test="${page.application_status == 1}">审核通过</c:if>
								
							</td>		
							<td>		
							 <shiro:hasPermission name="appdata:all">
							<c:if test="${page.application_status == 0}">
								<a onclick="return confirm('确定要通过吗？');"  href="<%=basePath %>/data/ApplicationUpdate.do?id=${page.id}&application_status=1">审核</a>&nbsp;
								</c:if>
								
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
			        	<li class="paginItem"><a href="<%=basePath%>/data/applicationQuery.do?pageNum=${pageInfo.pageNum - 1}&work_area=${work_area}&construction_year=${construction_year}"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:forEach items="${pageInfo.navigatepageNums }" var="num" varStatus="status">
			        	<c:if test="${status.count < 5 }">
			        		<li class="paginItem"><a charset="utf-8" href="<%=basePath%>/data/applicationQuery.do?pageNum=${num}&work_area=${work_area}&construction_year=${construction_year}">${num }</a></li>
			        	</c:if>
			        </c:forEach>
			        <c:if test="${pageInfo.pages == pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="javascript:alert('没有下一页');"><span class="pagenxt"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pages > pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="<%=basePath%>/data/applicationQuery.do?pageNum=${pageInfo.pageNum + 1}&work_area=${work_area}&construction_year=${construction_year}"><span class="pagenxt"></span></a></li>
			        </c:if>
		        </ul>
		    </div>
		</div>
	</div>

	<div class="tipnew">
		<div class="tiptop">
			<span>上传数据</span><a></a>
		</div>
		<form action="<%=basePath%>/data/ApplicationData.do" method="post" enctype="multipart/form-data">
			<input name="volLabel"  type="hidden" />
			<div class="tipinfo">
				<span><img src="<%=basePath%>/static/images/ticon.png" /></span>
				<table> <tr><td width="50%">
				
				<div class="tipleft">
					<p>数据申请备注说明:<p>
					<cite> 
					<textarea id="reMark"  name="reMark" rows="" cols="" style="width: 400px;height: 160px"></textarea>
					<p>联系电话<p>
					 <input type="text" id="phone" name="phone" class="scinput" value="请在此输入的的备注信息"> 
					 
					 <p>数据使用时间<p>
					 <input type="text" id="date" name="date" class="scinput" value="请在此输入的的备注信息"> 
						
				
						
					
				
					
				</div>
				</td>
				<td> </td>
				
				
				</tr>
				
				<tr>
				<td colspan= 2>
				
				
				
				</td>
				</tr>
				</table>
				
				
				<div class="tipbtn">
				<input type="submit" class="sure" value="确定" /> <input type="reset" class="cancel" value="取消" />
			</div>	
				
			</div>
			
		</form>
	</div>

</body>
</html>