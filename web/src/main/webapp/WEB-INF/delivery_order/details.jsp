<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md12">
            <h2 class="page-header">
                <i class="fa fa-shopping-bag"></i> OrderNo:${deliveryOrder.orderNo}
                Date: <lp:formatTime time="${deliveryOrder.createdTime }"
                                                               pattern="yyyy-MM-dd"/>
            </h2>
        </div>
        <!-- /.col -->
    </div>
    <!-- info row -->
    <div class="layui-row">
        <div class="layui-col-md4">
            From
            <address>
                Name:<strong>${deliveryOrder.senderInfo.senderName}</strong><br>
                Phone: ${deliveryOrder.senderInfo.senderPhone}<br>
                City: ${deliveryOrder.senderInfo.senderCity}<br>
                Address: ${deliveryOrder.senderInfo.senderAddress}<br>
            </address>
        </div>
        <!-- /.col -->
        <div class="layui-col-md4">
            To
            <address>
                Name:<strong>${deliveryOrder.receiverInfo.receiverName}</strong><br>
                Phone: ${deliveryOrder.receiverInfo.receiverPhone}<br>
                City: ${deliveryOrder.receiverInfo.receiverCity}<br>
                Address: ${deliveryOrder.receiverInfo.receiverAddress}<br>
            </address>
        </div>
        <!-- /.col -->
        <div class="layui-col-md4">
            ReferenceNo: ${deliveryOrder.referenceNo}<br>
            OrderType: ${deliveryOrder.orderType}<br>
            Order Amount: ${deliveryOrder.totalPrice}
        </div>
        <!-- /.col -->
    </div>
    <!-- /.row -->

    <div class="layui-row">
        <table class="layui-table">
            <colgroup>
                <col width="100">
                <col width="150">
                <col width="300">
                <col width="150">
                <col width="150">
            </colgroup>
            <thead>
            <tr>
                <th>ID</th>
                <th>SKU</th>
                <th>Description</th>
                <th>Qty</th>
                <th>Price</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${deliveryOrder.goodsInfoList}" var="goods" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${goods.goodsId}</td>
                    <td>${goods.goodsDesc}</td>
                    <td>${goods.qty}</td>
                    <td>${goods.unitPrice}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="layui-row">
        Trace:
        <hr>
        <c:forEach items="${orderRouteList}" var="route" varStatus="status">
            <span><lp:formatTime time="${route.createdTime }"
                           pattern="yyyy-MM-dd hh:mm:ss"/> ${route.opt}  ${route.optByName}</span><br>
        </c:forEach>
    </div>
</div>
