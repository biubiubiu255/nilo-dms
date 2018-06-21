<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>

<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md12">
            <h2 class="page-header">
                <i class="fa fa-shopping-bag"></i> OrderNo:${deliveryOrder.orderNo}
                Date: <lp:formatTime time="${deliveryOrder.createdTime }" pattern="yyyy-MM-dd"/>
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
        <jsp:useBean id="dateValue" class="java.util.Date"/>
        <c:forEach items="${orderRouteList}" var="route" varStatus="status">
            <span>
            <span class="unformatted">${route.createdTime}</span>
            【${route.opt}】${route.optNetwork}(${route.optByName})
            <c:if test="${route.nextStation!=null}">，[NextNetwork] ${route.nextStation}</c:if>
            <c:if test="${route.expressName!=null}">，[ExpressName] ${route.expressName}</c:if>
            <c:if test="${route.driver!=null}">，[Driver] ${route.driver}</c:if>
            <c:if test="${route.rider!=null}">，[Rider] ${route.rider}</c:if>
            <c:if test="${route.riderPhone!=null}">，[RiderPhone] ${route.riderPhone}</c:if>
            </span><br>
        </c:forEach>
    </div>
</div>

<script>
    $(".unformatted").each(function(index, elem){
        $(this).text(GetCurrentTime('YYYY-MM-DD hh:mm:ss', $(this).text()));
    });
</script>
