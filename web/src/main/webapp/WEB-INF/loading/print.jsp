<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<%@ include file="../common/header.jsp" %>
<script type="text/javascript">window.print();</script>
<style>
    *{
        font-size: large;
    }
    html {
        -webkit-text-size-adjust: none;
    }

    td {
        padding: 5px;
    }
    tr {
        height: 200%;
    }

</style>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
<div class="container">
    <div class="row">
        <div class="center-block" style="width:400px;">

            <h4>Shipment List</h4>

            <h5 style="position:absolute;right: 10px;top: 20px;"> Date ${date_str}</h5>
        </div>

        <br><br>
    </div>


    <div class="row">
        <div class="col-xs-3">
            Loading NO: ${loading.loadingNo}
        </div>
        <div class="col-xs-3">
            Carrier Name: ${loading.carrier}
        </div>
        <div class="col-xs-3">
            Rider :${loading.riderName}
        </div>
        <div class="col-xs-3">
            Operator :
            ${sessionScope.userName}
        </div>
        <br><br>
    </div>
    <br><br>
    <div class="row">
        <div class="col-xs-12">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Order No</th>
                    <th>Customer Name</th>
                    <th>Contact No</th>
                    <th>Address</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${loading.detailsList}" var="item">
	                   <tr>
	                        <td>${item.orderNo}</td>
	                        <td>${item.deliveryOrder.receiverInfo.receiverName}</td>
	                        <td>${item.deliveryOrder.receiverInfo.receiverPhone}</td>
	                        <td>${item.deliveryOrder.receiverInfo.receiverAddress}</td>
	                        <td>0</td>
	                   </tr>
                   </c:forEach> 
                </tbody>
            </table>
        </div>
        <br><br>

    </div>
    <div class="row">
        <div class="col-xs-6">
            Total Order Amount:${fn:length(loading.detailsList)}
        </div>

        <div class="col-xs-6">
            Pending payment :0
        </div>
        <br><br>
    </div>
    
<br><br>
        <div class="col-xs-12" style="float: right" >
            Rider's Name:____________________________
        </div>
        <br><br>
    </div>
    
</html>