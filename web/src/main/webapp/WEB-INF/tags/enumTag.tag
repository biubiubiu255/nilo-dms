<%@ tag import="com.nilo.dms.common.enums.EnumMessage" %>
<%@ tag import="com.nilo.dms.common.utils.StringUtil" %>
<%@ tag import="java.util.EnumMap" %>
<%@ tag import="java.util.EnumSet" %>
<%@ tag import="com.nilo.dms.common.enums.CustomerTypeEnum" %>
<%@ tag import="java.util.Map" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ attribute name="selectId" type="java.lang.String" required="true" description="selectId" %>
<%@ attribute name="selectName" type="java.lang.String" required="true" description="selectName" %>
<%@ attribute name="code" type="java.lang.String" required="true" description="code" %>
<%@ attribute name="className" type="java.lang.String" required="true" description="className" %>
<%@ attribute name="disabled" type="java.lang.Boolean" required="true" description="disabled" %>
<%@ attribute name="showChoose" type="java.lang.Boolean" required="false" description="showChoose" %>
<%@ attribute name="exclude" type="java.lang.String" required="false" description="exclude" %>
<%@ attribute name="multi" type="java.lang.Boolean" required="false" description="multi" %>

<%
    out.print("<select name='" + selectName + "' id='" + selectId + "' " + (disabled != null && disabled ? "disabled " : "") + (multi != null && multi ? " multiple" : "") + ">");
%>
<%
    if (StringUtil.isEmpty(code) || showChoose == null) {
        out.print("<option value=''>choose or search....</option>");
    }
    try {
        Class c = Class.forName("com.nilo.dms.common.enums." + className);
    } catch (Exception e) {
        return;
    }

    Map map = EnumMessage.enumMaps;

    if (map.get(className) == null) {
        return;
    }

    Map<Object, EnumMessage> enumMessageMap = (Map<Object, EnumMessage>) map.get(className);
    for (Object key : enumMessageMap.keySet()) {

        String k = "";
        if (key instanceof java.lang.String) {
            k = (String) key;
        }
        if (key instanceof java.lang.Integer) {
            k = "" + (Integer) key;
        }
        if (StringUtil.equals(k, exclude)) {
            continue;
        }

        if (StringUtil.equals(k, code)) {
            out.print("<option value='" + k + "' selected >" + enumMessageMap.get(key).getDesc() + "</option>");
        } else {
            out.print("<option value='" + k + "'>" + enumMessageMap.get(key).getDesc() + "</option>");
        }

    }

%>
</select>

