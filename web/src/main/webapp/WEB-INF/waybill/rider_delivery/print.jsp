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
        font-size: 14px;
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
    table, tr, td, th{
        border: 2px solid black !important;
    }

    .printer {
        padding: 0;
    }

    .printer li {
        border-bottom: 1px dashed #ddd;
        padding-bottom: 1em;
    }

    .printer li.r3 {
        border-bottom: none;
    }
    .lister tr td, .lister tr th {
        border: 1px solid #333;
        padding: 0 2px;
        color: #000;
    }

    .lister tr.title td {
        font-size: 18px;
        border: none;
    }

    .lister tr.total th {
        font-size: 14px;
    }

    .lister tr.total td {
        padding-right: 12px;
    }

    .lister tr th span.grey {
        font-weight: lighter;
    }

    .intro td {
        font-size: 15px;
        font-weight: bold;
        text-align: center;
        color: #000;
    }

    .intro2 td {
        font-size: 13px;
        text-align: left;
        color: #000;
    }

    .intro2 {
        margin: 5px 0;
    }

</style>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
<div class="container">
    <div class="row" style="text-align: center;">
        <div class="center-block" style="width:400px;">

            <h4><b><span style="font-weight: bolder; font-size: larger;">Shipment List</span></b></h4>
			<div
				style="position: absolute; left: 2px; top: 10px; height: 1.53cm; width: 5cm;">
				<img src="/barCode/${pack.handleNo}.html">
			</div>
            <h5 style="position:absolute;right: 10px;top: 20px;"> Date ${nowDate}</h5>
        </div>

    <br><br><br>
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

    <div class="row">
        <div class="col-xs-12">
            <table class="lister table">
                <thead>
                <tr>
                    <th>No</th>
                    <th>Order No</th>
                    <th>Customer Name</th>
                    <th>Contact No</th>
                    <th>Address</th>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${smalls}" var="item" varStatus="status">
	                   <tr>
                            <td>${status.index + 1}</td>
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