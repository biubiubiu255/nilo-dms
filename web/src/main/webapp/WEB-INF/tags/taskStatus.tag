<%@ tag import="com.nilo.dms.common.utils.StringUtil" %>
<%@ tag import="com.nilo.dms.common.enums.TaskStatusEnum" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ attribute name="selectId" type="java.lang.String" required="true" description="selectId" %>
<%@ attribute name="selectName" type="java.lang.String" required="true" description="selectName" %>
<%@ attribute name="status" type="java.lang.String" description="status" %>
<%@ attribute name="multiple" type="java.lang.Boolean" required="true" description="multiple" %>
<%@ attribute name="showChoose" type="java.lang.Boolean"  required="false" description="showChoose" %>

<%
    if (multiple) {
        out.print("<select name='" + selectName + "' id='" + selectId + "' lay-search='' multiple>");
    } else {
        out.print("<select name='" + selectName + "' id='" + selectId + "' lay-search=''>");
    }
%>
<%
    if (StringUtil.isEmpty(status) || showChoose) {
        out.print("<option value=''>choose or search....</option>");
    }
    for (TaskStatusEnum e : TaskStatusEnum.values()) {
        if (StringUtil.equals(status, "" + e.getCode())) {
            out.print("<option value='" + e.getCode() + "' selected >" + e.getDesc() + "</option>");
        } else {
            out.print("<option value='" + e.getCode() + "'>" + e.getDesc() + "</option>");
        }
    }
%>
</select>

