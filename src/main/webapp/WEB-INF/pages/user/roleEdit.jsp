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
    <form id="update_form" method="post" novalidate="novalidate">
        <input type="hidden" name="roleId" value="${role.roleId }" />
         <input type="hidden" id="savetype" name="savetype" value="${savetype }"/> 
        <table class="list">
			 <tr>
					<td>角色编码<span class="red_star">*</span></td>
					<td>
						<input type="text" name="roleCode" class="easyui-validatebox easyui-textbox" value="${role.roleCode }" id="roleCode"
						data-options="required:true,missingMessage:'请输入角色编码',validType:'length[1,32]'" /></td>
					</td>
					<td>角色名称<span class="red_star">*</span></td><%-- validType:{length:[1,50],remote:['<%=basePath%>/role/roleName/validation.do?roleId=${role.roleId }' --%>
					<td><input type="text" name="roleName" maxlength="50" class="easyui-validatebox easyui-textbox" value="${role.roleName }"
						data-options="required:true,missingMessage:'请输入角色名称',validType:'length[1,50]'" onBlur="<%=basePath%>/role/roleName/validation.do?roleId=${role.roleId }" /></td>
				</tr>
				<tr>
					<td>状态</td>
					<td>
						<select name="deleted" class="easyui-combobox" id="channelsId_combobox" editable="false">
							<option value="0" ${(role.deleted eq '0' ) ? "selected='selected'" : "" }>正常</option>
							<option value="1" ${(role.deleted eq '1' ) ? "selected='selected'" : "" }>撤销</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>备注</td>
					<td colspan="3"><input type="text" name="remark" maxlength="400" style="width:250px;" class="easyui-validatebox easyui-textbox" value="${role.remark }" data-options="validType:'length[0,400]'" /></td>
				</tr>
			<tr>
               <td style="text-align: center"" colspan="4">
                 <a href="javascript:role_save('#update_form')" class="easyui-linkbutton" iconCls="icon-add">保存</a>&nbsp;&nbsp;
                   <a href="javascript:role_reset('#update_form')" class="easyui-linkbutton" iconCls="icon-clear">重置</a>
               </td>
           </tr>
        </table>
    </form>
</div>
