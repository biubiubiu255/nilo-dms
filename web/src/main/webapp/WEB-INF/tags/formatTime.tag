<%@ tag import="com.nilo.dms.common.utils.DateUtil" %>
<%@ tag import="java.time.format.DateTimeFormatter" %>
<%@ tag import="java.time.Instant" %>
<%@ tag import="java.time.LocalDateTime" %>
<%@ tag import="java.time.ZoneId" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ attribute name="time" type="java.lang.Long" required="true"  %>
<%@ attribute name="pattern" type="java.lang.String" required="true"  %>
<jsp:doBody/>
<%
    //out.print(DateUtil.format(time,pattern));
    Instant instant = Instant.ofEpochMilli(new Long(time + "000"));
    LocalDateTime locaTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    String res = locaTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    out.print(res);
%>
