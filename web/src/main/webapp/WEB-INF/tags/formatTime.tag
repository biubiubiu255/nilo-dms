<%@ tag import="com.nilo.dms.common.utils.DateUtil" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ attribute name="time" type="java.lang.Long" required="true"  %>
<%@ attribute name="pattern" type="java.lang.String" required="true"  %>
<jsp:doBody/>
<%
    out.print(DateUtil.format(time,pattern));
%>
