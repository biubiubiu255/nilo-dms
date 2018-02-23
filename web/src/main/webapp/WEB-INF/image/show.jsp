<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .layui-show-img {
        width: 200px;
        height: 200px;
        margin: 0 10px 10px 0;
    }
</style>
<div class="box-body">
    <c:forEach items="${list}" var="image" varStatus="status">
        <img src="${image.imageName}" alt="${image.imageName}" class="layui-show-img">
    </c:forEach>

</div>
