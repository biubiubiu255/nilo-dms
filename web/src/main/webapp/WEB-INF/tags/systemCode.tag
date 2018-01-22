<%@ tag import="com.nilo.dms.common.utils.DateUtil" %>
<%@ tag import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ attribute name="merchantId" type="java.lang.String" required="true" %>
<%@ attribute name="type" type="java.lang.String" required="true" %>
<%@ attribute name="code" type="java.lang.String" required="true" %>
<jsp:doBody/>
<%
    out.print(SystemCodeUtil.getCodeVal(merchantId, type, code));
%>
