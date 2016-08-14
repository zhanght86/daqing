<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<div id="asset-license-grid-tb" >
    <form id="updateUser-form" method="post" novalidate="novalidate">
        <input type="hidden" name="userId" value="${user.userId }" />
        <input type="hidden" id="savetype" name="savetype" value="${savetype }"/>
        <table class="list">
        	<tr>
				<td style="text-align: right">登录名称</td>
				<td>
					<input type="text" name="loginName" maxlength="50" value="${user.loginName }"
					class="easyui-validatebox easyui-textbox" data-options="required:true,missingMessage:'请输入登录名称',validType:'length[1,32]'"
					<c:if test="${empty user.userId }">data-options="required:true,validType:{halfEnDigital:[],length:[1,32],remote:['<%=basePath%>/user/loginName/validation.do','loginName']}"</c:if>
					<c:if test="${not empty user.userId }">readonly="readonly"</c:if>
					/>
				</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td style="text-align: right">姓名<span class="red_star">*</span></td>
				<td><input type="text" name="userName" maxlength="50" value="${user.userName }"
					class="easyui-validatebox easyui-textbox" data-options="required:true,missingMessage:'请输入姓名',validType:'length[1,50]'" 
					<c:if test="${user.status eq '0'}">readonly="readonly"</c:if>
					/>
				</td>
				<td style="text-align: right">机构名称</td>
				<td>
					<select class="easyui-combobox" id="channelsId_combobox" name="dptCode">
						<option value=""></option>
						<c:forEach items="${orgList}" var="item">
							<option value="${item.dptCode}"  ${(user.dptCode eq item.dptCode ) ? "selected='selected'" : "" }>${item.dptName}</option>
						</c:forEach>
					</select>
			</tr>
			<tr>
				<td style="text-align: right">邮箱地址</td>
				<td><input type="text" name="email" maxlength="32" value="${user.email }"
					class="easyui-validatebox easyui-textbox" data-options="validType:'length[1,32]'" 
					<c:if test="${user.status eq '0'}">readonly="readonly"</c:if>
					/>
				</td>
				<td style="text-align: right">电话号码</td>
				<td><input type="text" name="phone" maxlength="20" value="${user.phone }"
					class="easyui-numberbox easyui-textbox" data-options="validType:'length[1,20]'" 
					<c:if test="${user.status eq '0'}">readonly="readonly"</c:if>
					/>
					</td>
			</tr>
			<tr>
				<td style="text-align: right">状态</td>
				<td>
					<select name="deleted" class="easyui-combobox">
						<option value="0" ${(user.deleted) ? "selected='selected'" : "" }>正常</option>
						<option value="1" ${(user.deleted) ? "selected='selected'" : "" }>撤销</option>
					</select>
				</td>
			</tr>
			<tr>
               <td style="text-align: center" colspan="4">
                   <a href="javascript:user_save('#updateUser-form')" class="easyui-linkbutton" iconCls="icon-add">保存</a>&nbsp;&nbsp;
                   <a href="javascript:user_reset('#updateUser-form')" class="easyui-linkbutton" iconCls="icon-clear">重置</a>
               </td>
           </tr>
        </table>
    </form>
</div>
