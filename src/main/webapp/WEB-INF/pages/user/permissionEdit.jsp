<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
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
    <form id="updatesuspectinfo-form" method="post" novalidate="novalidate">
        <input type="hidden" name="permissionId" value="${permission.permissionId }" />
        <input type="hidden" id="savetype" name="savetype" value="${savetype }"/> 
        <table class="list">
			 <tr>
				<td style="text-align: right">权限编码</td>
				<td><input type="text" name="permissionCode" maxlength="200" value="${permission.permissionCode }"
					id="permissionCode" class="easyui-validatebox easyui-textbox" data-options="required:true,missingMessage:'请输入权限编码',validType:'length[1,256]'"/>
					
				</td>
				<td style="text-align: right">权限名称</td>
				<td><input type="text" name="permissionName" value="${permission.permissionName }" id="permissionName" 
				class="easyui-validatebox easyui-textbox" data-options="required:true,missingMessage:'请输入权限名称',validType:'length[1,256]'"/> </td>
		
			<tr>
				<td style="text-align: right">权限路径</td>
				<td><input type="text" name="permissionUrl" value="${permission.permissionUrl }" id="permissionUrl" 
				class="easyui-validatebox easyui-textbox" data-options="required:true,missingMessage:'请输入权限路径',validType:'length[1,256]'"/> </td>
				
				<td style="text-align: right">权限描述符</td>
				<td><input type="text" name="permission" value="${permission.permission }" id="permission" 
				class="easyui-validatebox easyui-textbox" data-options="required:true,missingMessage:'请输入权限描述符',validType:'length[1,256]'"/> </td>
			</tr>  
			
			<tr>
				<td style="text-align: right">父级菜单</td>
				<td>
					<select class="easyui-combobox" id="channelsId_combobox" name="parentId" editable="false">
						<option value=""></option>
						<c:forEach items="${per}" var="item">
							<option value="${item.permissionId}"  ${(permission.parentId eq item.permissionId ) ? "selected='selected'" : "" }>${item.permissionName}</option>
						</c:forEach>
					</select>
				</td>
				
				<td style="text-align: right">权限类型</td>
				<td>
					<select name="permissionType" class="easyui-combobox" id="channelsId_combobox" editable="false">
						<option value="1" ${(permission.permissionType eq '1') ? "selected='selected'" : "" }>权限(菜单)</option>
						<option value="2" ${(permission.permissionType eq '2') ? "selected='selected'" : "" }>权限(功能)</option>
					</select>
				</td>
			</tr>  
			
			<tr>
               <td style="text-align: center" colspan="4">
                   <a href="javascript:permissioninfo_save('#updatesuspectinfo-form')" class="easyui-linkbutton" iconCls="icon-add">保存</a>&nbsp;&nbsp;
                   <a href="javascript:permissioninfo_reset('#updatesuspectinfo-form')" class="easyui-linkbutton" iconCls="icon-clear">重置</a>
               </td>
           </tr>
        </table>
    </form>
</div>

