<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
			window.location.href = '<%=basePath%>/dData/excel.do?project_name='+$("#project_name").val()+'&filing_unit='+$("#filing_unit").val()+'&filing_date='+$("#filing_date").val();
		});
		
		$(".table").click(function(){
			window.location.href = '<%=basePath%>/doc/二维成果资料清单.xls';
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
										 var table="<table>"
									  $.each(data, function(i, item) {
										  table+="<tr> <td><input type='radio' name='dir' value='"+item+"' onchange='getDataPath1(this)'></input></td> <td><a href='#' class='aaa' name='button' value='"+item+"'>"+item+"</a></td></tr>";
										
								        });
								
									table+=("</table>");
									 
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
													 var table="<table>"
												  $.each(data, function(i, item) {
													  table+="<tr> <td><input type='radio' name='dir' value='"+item+"' onchange='getDataPath1(this)'></input></td> <td><a href='#' class='aaa' name='button' value='"+item+"' onclick='setCurrent(\""+item+"\")'>"+item+"</a></td></tr>";
													
											        });
											
												table+=("</table>");
												 
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
				

</script>
</head>
<!-- 三维数据 -->
<body>

	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="<%=basePath%>/to.do?file=index">首页</a></li>
			<li><a href="<%=basePath%>/dData/list.do">三维数据列表</a></li>
		</ul>
	</div>



	<div class="formbody">
		<div id="usual1" class="usual">
		
			<ul class="toolbar">
				<li class="refresh"><span><img src="<%=basePath%>/static/images/Refresh.gif" /></span>刷新</li>
			</ul>
		
			<ul class="toolbar">
				<li class="click"><span><img src="<%=basePath%>/static/images/t01.png" /></span>上传数据</li>
			</ul>
			
			<ul class="toolbar">
				<li class="excel"><span><img src="<%=basePath%>/static/images/Load.gif" /></span>导出Excel</li>
			</ul>
			
			<ul class="toolbar">
				<li class="table"><span><img src="<%=basePath%>/static/images/Table.gif" /></span>模板下载</li>
			</ul>
			
			<form action="<%=basePath%>/dData/list.do" method="post">
				<input name="volume_label" value="${volume_label }" type="hidden" />
			<ul class="seachform">
				<li>
					<label>&nbsp;&nbsp;&nbsp;&nbsp;项目名称</label>
					<input name="project_name" type="text" class="scinput" value="${project_name }" />
				</li>
				<li>
					<label>归档单位</label>
					<input name="filing_unit" type="text" class="scinput" value="${filing_unit }" />
				</li>
				
				<li>
					<label>归档日期</label>
					<input name="filing_date" type="text"class="scinput" value="${filing_date }" onclick="WdatePicker({dateFmt:'yyyy-MM', errDealMode:'0'})"/>
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
						<th width="60">序号</th>
						<th width="100">项目名称</th>
						<th width="100">记带内容</th>
						<th width="60">资料编号</th>
						<th width="90">INLINE范围</th>
						<th width="120">CROSSLINE范围</th>
						<th width="90">INLINE位置</th>
						<th width="120">CROSSLINE位置</th>
						<!-- 
						<th width="75">X坐标位置</th>
						<th width="75">Y坐标位置</th>
						 -->
						<th width="65">记录长度</th>
						<th width="65">采样间隔</th>
						<th width="65">处理单位</th>
						<th width="80">归档日期</th>
						<th width="90">数据量(GB)</th>
						<th width="100">备注</th>
						<th width="80">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="page" varStatus="status">
						<tr class="${status.count % 2 == 0 ? '' : 'brown'}">
							<td>${status.count }</td>
							<td>&nbsp;${page.project_name}</td>
							<td>&nbsp;${page.record_content}</td>
							<td>&nbsp;${page.tape_number}</td>
							<td>&nbsp;${page.inline_range}</td>
							<td>&nbsp;${page.crossline_range}</td>
							<td>&nbsp;${page.inline_position}</td>
							<td>&nbsp;${page.crossline_position}</td>
							<!-- 
							<td>&nbsp;${page.x_position}</td>
							<td>&nbsp;${page.y_position}</td>
							 -->
							<td>&nbsp;${page.record_length}</td>
							<td>&nbsp;${page.use_interval}</td>
							<td>&nbsp;${page.filing_unit}</td>
							<td>&nbsp;${page.filing_date}</td>
							<td>&nbsp;${page.data_quantity}</td>
							<td>&nbsp;${page.remarks }</td>
							<td>
							<a href="<%=basePath%>/data/dInsertInit.do?sid=${page.sid}&volume_label=${page.volume_label}">补录</a>
							<a href="<%=basePath%>/data/dUpdateInit.do?sid=${page.sid}">修改</a>
							<a href="<%=basePath%>/data/delete.do?sid=${page.sid}&dataType=D&url=dData/list.do">删除</a>
							<a href="<%=basePath%>/burn/list.do?volume_label=${page.volume_label}">详细</a></td>
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
			        <c:if test="${pageInfo.pages >= pageInfo.pageNum}">
			       	 	<li class="paginItem"><a href="<%=basePath%>/dData/list.do?pageNum=${pageInfo.pageNum + 1}&project_name=${project_name }&filing_unit=${filing_unit }&filing_date=${filing_date }"><span class="pagenxt"></span></a></li>
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
			<input name="dataType" value="D" type="hidden" />
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