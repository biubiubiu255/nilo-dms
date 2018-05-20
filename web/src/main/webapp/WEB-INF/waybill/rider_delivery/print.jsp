<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<%
    request.setAttribute("nowDate", DateUtil.formatCurrent("yyyy-MM-dd HH:mm:ss"));
%>
<%@ include file="../../common/header.jsp" %>
<script type="text/javascript">window.print();</script>
<style>
    *{
        font-size: 10px;
    }
    html {
        -webkit-text-size-adjust: none;
    }

    td {
        padding: 5px;
    }
    tr {
        height: 150%;
    }
</style>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
<div class="container">
    <div class="row">
        <div class="center-block" style="width:400px;">

            <h4>Shipment List</h4>

            <h5 style="position:absolute;right: 10px;top: 20px;"> Date ${nowDate}</h5>
        </div>

        <br><br>
    </div>


    <div class="row">
        <div class="col-xs-3">
            Loading NO: ${pack.handleNo}
        </div>
        <div class="col-xs-3">
            Name: ${pack.carrier}
        </div>
        <div class="col-xs-3">
            Rider :${pack.riderName}
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
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${smalls}" var="item">
	                   <tr>
	                        <td>${item.orderNo}</td>
	                        <td>${item.receiverInfo.receiverName}</td>
	                        <td>${item.receiverInfo.receiverPhone}</td>
	                        <td>${item.receiverInfo.receiverAddress}</td>
                       </tr>
                   </c:forEach> 
                </tbody>
            </table>
        </div>
        <br><br>

    </div>
    <div class="row">
        <div class="col-xs-6">
            Total Order :${fn:length(smalls)}
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