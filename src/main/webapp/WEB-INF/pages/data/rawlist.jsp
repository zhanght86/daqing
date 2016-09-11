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
			<li><a href="<%=basePath%>/rawData/list.do">原始数据列表</a></li>
		</ul>
	</div>



	<div class="formbody">
		<div id="usual1" class="usual">
			
			<ul class="toolbar">
				<li class="refresh"><span><img src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>
				<li class="click"><span><img src="<%=basePath%>/static/images/t01.png" /></span>上传数据</li>
				<li class="excel"><span><img src="<%=basePath%>/static/images/Load.gif" /></span>导出Excel</li>
				<li class="table"><span><img src="<%=basePath%>/static/images/Table.gif" /></span>模板下载</li>
			</ul>
			<form action="<%=basePath%>/rawData/list.do" method="post">
				<input name="volume_label" value="${volume_label }" type="hidden" />
			<ul class="seachform">
				<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;工区</label>
					<input name="work_area" id="work_area" type="text"class="scinput" value="${work_area }" />
				</li>
				<li>
					<label>施工年度</label>
					<input name="construction_year" id="construction_year" type="text" class="scinput" value="${construction_year }" /><!-- onclick="WdatePicker({dateFmt:'yyyy-MM', errDealMode:'0'})" -->
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
						<th>序号</th>
						<th width="150">工区</th>
						<th>测线（束）号</th>
						<th>起止序号</th>
						<th>记录长度(s)</th>
						<th>采样间隔(ms)</th>
						<th>磁带编号</th>
						<th>磁带盘数</th>
						<th>施工年度</th>
						<th>施工单位</th>
						<th>数据量量（GB）</th>
						<th width="150">备注</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${pageInfo.list }" var="page" varStatus="status">
					<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
							<td>${status.count }</td>
							<td>&nbsp;${page.work_area }</td>
							<td>&nbsp;${page.test_line_number }</td>
							<td>&nbsp;${page.se_number }</td>
							<td>&nbsp;${page.record_length }</td>
							<td>&nbsp;${page.use_interval }</td>
							
							<td>&nbsp;${page.tape_number }</td>
							<td>&nbsp;${page.tape_size }</td>
							<td>&nbsp;${page.construction_year }</td>
							<td>&nbsp;${page.construction_unit }</td>
							<td>&nbsp;${page.data_quantity }</td>
							<td>&nbsp;${page.remarks }</td>
							<td>
							
							 <shiro:hasPermission name="burn:raw">
							<a href="<%=basePath%>/data/rawInsertInit.do?sid=${page.sid}&volume_label=${page.volume_label}">补录</a>
							<a href="<%=basePath%>/data/rawUpdateInit.do?sid=${page.sid}">修改</a>
							<a onclick="return confirm('确定要删除么？');" href="<%=basePath%>/data/delete.do?sid=${page.sid}&dataType=R&url=rawData/list.do">删除</a>
							<a href="<%=basePath%>/burn/list.do?volume_label=${page.volume_label}">刻录详细</a>
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
			        	<li class="paginItem"><a href="<%=basePath%>/rawData/list.do?pageNum=${pageInfo.pageNum - 1}&work_area=${work_area}&construction_year=${construction_year}"><span class="pagepre"></span></a></li>
			        </c:if>
			        <c:forEach items="${pageInfo.navigatepageNums }" var="num" varStatus="status">
			        	<c:if test="${status.count < 5 }">
			        		<li class="paginItem"><a charset="utf-8" href="<%=basePath%>/rawData/list.do?pageNum=${num}&work_area=${work_area}&construction_year=${construction_year}">${num }</a></li>
			        	</c:if>
			        </c:forEach>
			        <c:if test="${pageInfo.pages == pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="javascript:alert('没有下一页');"><span class="pagenxt"></span></a></li>
			        </c:if>
			        <c:if test="${pageInfo.pages > pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="<%=basePath%>/rawData/list.do?pageNum=${pageInfo.pageNum + 1}&work_area=${work_area}&construction_year=${construction_year}"><span class="pagenxt"></span></a></li>
			        </c:if>
		        </ul>
		    </div>
		</div>
	</div>

	<div class="tipnew">
		<div class="tiptop">
			<span>上传数据</span><a></a>
		</div>
		<form action="<%=basePath%>/data/upload.do" method="post" enctype="multipart/form-data">
			<input name="dataType" value="R" type="hidden" />
			<div class="tipinfo">
				<span><img src="<%=basePath%>/static/images/ticon.png" /></span>
				<table> <tr><td width="50%">
				
				<div class="tipleft">
					<p>选择上传的数据结构和数据体的位置 :</p>
					<cite><input name="dataSource" type="radio" value="1"
						 /> <input type="text" id="exportPath" name="exportPath" class="scinput" value="/jukebox/harddisk">
						<input
						name="dataSource" type="radio" value="2" checked="checked" />&nbsp;龙存目录</cite>
				
						
					<p>结构文档:</p>
					<cite>
						<li><input name="file" type="file" class="fileinput" /></li>
					</cite>
					<p>刻盘光盘类型 :</p>
					<cite>
						<li>
						    <select class="select1" name="discType">
								<c:forEach items="${discType }" var="disc">
									<option value="${disc.sp_value3 }" <c:if test="${disc.sp_value2 == 1 }" >selected='selected'</c:if> >${disc.sp_name }</option>
								</c:forEach>
							</select>
						</li>
					</cite>
					<p>请选择刻录机器 :</p>
					<cite>
							<select class="select1" name="server">
									<option value=""></option>
									<option value="SERVER1">XSERVER-1(106)</option>
									<option value="SERVER2">XSERVER-2(107)</option>
							</select>
					 </cite>
				</div>
				</td>
				<td> </td>
				<td width="50%" >
				<div class="tipright" style="height:150px; overflow:auto">
				<input type="hidden" name="currentPath" id="currentPath"   value="">
				<input type="button"  name="goback" value="返回根目录" class="button"> 
				<table class="info" id="info">
				
							<c:forEach var="dirList" items="${dirLists}">
								<tr  >
									<td><input type="radio" name="dir" value="${dirList}" onchange="getDataPath1(this)"></input></td>
										 
									<td><a href="#" class="aaa" name="button" value="${dirList}"  onclick="setCurrent('${dirList}')">${dirList}</a></td>
								</tr>
							</c:forEach>
						</table>
				</div>
				</td>
				
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