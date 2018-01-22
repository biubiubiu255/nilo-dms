<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Pick Up List</title>
    <link rel="stylesheet" href="${ctx}/layui/css/layui.css" media="all">
</head>

<style media="all">
    .PageNext {
        page-break-toStatus: always;
    }
</style>

<body>
<div style="width: 700px">

    <c:forEach items="${list}" var="order">

        <table class="layui-table">
            <colgroup>
                <col width="150">
                <col width="200">
                <col width="300">
            </colgroup>
            <thead>
            <tr>
                <th>OrderNo</th>
                <th>CreatedTime</th>
                <th>Barcode</th>
            </tr>
            <tr>
                <th>${order.orderNo}</th>
                <th>${order.createdTime}</th>
                <th><img src="/barCode/createCode128.html?content="${order.orderNo}></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        <div class="PageNext"></div>
    </c:forEach>

</div>

</body>
</html>