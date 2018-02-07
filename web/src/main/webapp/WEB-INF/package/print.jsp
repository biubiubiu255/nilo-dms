<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<html>
<%@ include file="../common/header.jsp" %>

<script type="text/javascript">window.print();</script>
<style>
    * {
        font-size: large;
    }

    html {
        -webkit-text-size-adjust: none;
    }

    td {
        padding: 5px;
    }

    .col-center-block {
        float: none;
        display: block;
        margin-left: auto;
        margin-right: auto;
    }

    tr {
        height: 200%;
    }

</style>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="/js/bootstrap.min.js"></script>
<div class="container">
    <div class="row">
        <div class="center-block" style="width:400px;">
            <h4>Package List</h4>
        </div>
        <br><br>
    </div>


    <div class="row">
        <div class="col-xs-4">
            Package NO: ${packageInfo.orderNo}
        </div>
        <div class="col-xs-3">
            NextStation: ${packageInfo.nextNetworkDesc}
        </div>
        <div class="col-xs-3">
            Date: <lp:formatTime time="${packageInfo.createdTime }"
                                 pattern="yyyy-MM-dd"/>
        </div>
        <div class="col-xs-2">
            Weight: ${packageInfo.weight}
        </div>
        <br><br>
    </div>

    <br><br>
    <div class="layui-row">
        <div class="layui-col-md12 layui-col-lg12">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Order No</th>
                    <th>Order Type</th>
                    <th>Price</th>
                    <th>Weight</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="item">
                    <tr>
                        <td>${item.orderNo}</td>
                        <td>${item.orderType}</td>
                        <td>${item.totalPrice}</td>
                        <td>${item.weight}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <br><br>

    </div>

    <br><br>

    <br><br>
</div>

</html>