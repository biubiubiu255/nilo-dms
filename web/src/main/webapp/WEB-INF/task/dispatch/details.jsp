<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md12">
            <h2 class="page-header">
                <i class="fa fa-shopping-bag"></i> OrderNo:${deliveryOrder.orderNo}
                <small class="pull-right">Date: <lp:formatTime time="${deliveryOrder.createdTime }"
                                                               pattern="yyyy-MM-dd"/></small>
            </h2>
        </div>
        <!-- /.col -->
    </div>
    <!-- info row -->
    <div class="layui-row">
        <div class="layui-col-md4">
            From
            <address>
                Name:<strong>${deliveryOrder.senderInfo.name}</strong><br>
                Phone: ${deliveryOrder.senderInfo.contactNumber}<br>
                City: ${deliveryOrder.senderInfo.city}<br>
                Address: ${deliveryOrder.senderInfo.address}<br>
            </address>
        </div>
        <!-- /.col -->
        <div class="layui-col-md4">
            To
            <address>
                Name:<strong>${deliveryOrder.receiverInfo.name}</strong><br>
                Phone: ${deliveryOrder.receiverInfo.contactNumber}<br>
                City: ${deliveryOrder.receiverInfo.city}<br>
                Address: ${deliveryOrder.receiverInfo.address}<br>
            </address>
        </div>
        <!-- /.col -->
        <div class="layui-col-md4">
            <b>ReferenceNo:</b> ${deliveryOrder.referenceNo}<br>
            <b>OrderType:</b> ${deliveryOrder.orderType}<br>
            <b>ServiceType:</b> ${deliveryOrder.serviceType}<br>
            <b>Account:</b> ${deliveryOrder.totalPrice}
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
                    <td>${goods.totalPrice}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="layui-row">
        <button class="layui-btn layui-btn-normal receive">Receive</button>
        <button class="layui-btn layui-btn-danger abnormal">Abnormal</button>
    </div>
</div>
<script type="text/javascript">
    $(function () {

        var taskId = '${task.taskId}';
        var orderNo = '${deliveryOrder.orderNo}';
        var referenceNo = '${deliveryOrder.referenceNo}';

        $('.receive').on('click', function () {
            receive(taskId,orderNo,referenceNo);
        });
        $('.abnormal').on('click', function () {
            abnormal(taskId,orderNo,referenceNo);
        });
        function receive(taskId,orderNo,referenceNo) {
            $.ajax({
                url: "/task/dispatch/receivePage.html",
                type: 'GET',
                data: {"taskId": taskId,orderNo:orderNo,referenceNo:referenceNo},
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                        type: 1,
                        content: data,
                        area: ['800px', '600px'],
                        maxmin: true
                    });

                }
            });
        }
        function abnormal(taskId,orderNo,referenceNo) {
            $.ajax({
                url: "/task/dispatch/abnormalPage.html",
                type: 'GET',
                data: {"taskId": taskId,orderNo:orderNo,referenceNo:referenceNo},
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                        type: 1,
                        content: data,
                        area: ['700px', '500px'],
                        maxmin: true
                    });

                }
            });
        }
    });
</script>