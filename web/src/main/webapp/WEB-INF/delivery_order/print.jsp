<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.nilo.dms.dto.order.Waybill" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Waybill deliveryOrder = (Waybill) request.getAttribute("delivery");
    String createdTimeStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.valueOf(deliveryOrder.getCreatedTime())*1000));
%>

<html>
<link href="${ctx}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript">window.print();</script>
<style media="all">
    * {
        font-size: small;
        font-weight: 500;
    }

    html {
        -webkit-text-size-adjust: none;
    }

    td {
        border:none;
        padding: 5px;
    }

    tr {
        height: 200%;
    }

    table{border-collapse:collapse;}
    td{width:100px;height:40px;}
    tr td{
        border-right:none !important;
        border-left: none !important;
        border-style:none;
        vertical-align: middle !important;
    }

    .td-md {
        vertical-align: middle;
    }

    .logo-print {
        height: 1.53cm;
        width: 1.53cm;
    }

    .barcode-print {
        height: 1.53cm;
        width: 5cm;
    }

    .print-font-large {
        font-size: large;
    }


</style>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">window.print();</script>
<body>
<div style="width:10cm; height:15cm;">
    <div class="row" style="margin: 2px">
        <table class="table table-bordered">
            <tr style="height: 1.53cm;width: 10cm;">
                <td colspan="1" align="center">
                    <div class="row">
                        <div class="col-xs-12"><img src="${ctx}/dist/img/logo.png" class="logo-print"></div>
                    </div>
                </td>
                <td colspan="2" align="center">
                    <div><b>${delivery.orderNo}</b></div>
                    <div><img src="/barCode/${delivery.orderNo}.html" class="barcode-print">
                    </div>
                </td>
                <td colspan="1" align="center">
                    <div>${delivery.orderType}</div>
                    <div>
                        <c:if test="${delivery.channel != null and delivery.channel=='0'}"><b>Doorstep</b></c:if>
                        <c:if test="${delivery.channel != null and delivery.channel=='1'}"><b>Self Collect</b></c:if>
                    </div>
                </td>
            </tr>
            <tr style="height: 1cm;width: 10cm;">
                <td colspan="4" style="padding-left: 10px;vertical-align: middle;">
                    <div class="row">
                        <div class="col-xs-12" style="font-weight:bolder;">
                            Order No:<b>${delivery.referenceNo}
                        </b>
                        
                        </div>
                    </div>
                    <div class="row">
                    	<div class="col-xs-7" >
                            <b>  &nbsp;
                            </b>
                        </div>
                    </div>
                    
                </td>
            </tr>
            <%--<tr style="height: 2.3cm;width: 10cm;">
                <td colspan="4" style="padding-left: 10px">
                    <div class="row">
                        <div class="col-xs-12">
                            From:${delivery.senderInfo.senderName}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            Address:${delivery.senderInfo.senderAddress}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <span class="print-font-large">Tell:${delivery.senderInfo.senderPhone}</span>
                        </div>
                    </div>
                </td>
            </tr>--%>
            <%--<tr style="height: 1.53cm;width: 10cm;">
                <td colspan="4" style="padding-left: 10px">
                    <div class="row">
                        <div class="col-xs-8">
                            OrderNo:${delivery.referenceNo}
                        </div>
                        <div class="col-xs-4">
                            Weight:${delivery.weight}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-8">
                            Payment Method:Pay on Delivery
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-8">
                            Amount:${delivery.needPayAmount} KSH
                        </div>

                    </div>
                </td>
            </tr>--%>
            <tr style="height: 1.7cm;width: 10cm;">
                <td colspan="4" style="padding-left: 10px">
                    <div class="row">
                        <div class="col-xs-12">
                            Thank you for shopping at Killmall<br/>
                            Customer Service Number：<b>+254(0) 799 717 001</b>
                        </div>
                    </div>
                    <div class="row">
                    	<div class="col-xs-7" >
                            <b>  &nbsp;
                            </b>
                        </div>
                    </div>
                </td>
                
            </tr>
        </table>
        <hr/>
        <table class="table table-bordered">
            <tr style="height: 1.53cm;width: 10cm;">
                <td colspan="3" align="center">
                    <div>Tracking Number：<b>${delivery.orderNo}</b></div>
                    <div><img src="/barCode/${delivery.orderNo}.html" class="barcode-print">
                    </div>
                </td>
                <td colspan="1" align="center">
                    <div>${delivery.orderType}</div>
                    <div>
                        <c:if test="${delivery.channel != null and delivery.channel=='0'}"><b>Doorstep</b></c:if>
                        <c:if test="${delivery.channel != null and delivery.channel=='1'}"><b>Self Collect</b></c:if>
                    </div>
                </td>
            </tr>



            <tr style="height: 2.3cm;width: 10cm;">
                <td colspan="4" style="padding-left: 10px">
                    <div class="row">
                        <div class="col-xs-12">
                            To:<b>${delivery.receiverInfo.receiverName}</b>
                        </div>
                        <div class="row">
                    	<div class="col-xs-7" >
                              &nbsp;
                        </div>
                    </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            Address:<b>${delivery.receiverInfo.receiverAddress}</b>
                        </div>
                         
                    </div>
                    
                    <div class="row">
                        <div class="col-xs-12">
                            Tell:<b>${delivery.receiverInfo.receiverPhone}</b>
                        </div>
                    </div>
                </td>
            </tr>  <!--第二个表格收货信息-->


            <tr style="height: 1.53cm;width: 16cm;">
                <td colspan="4" style="padding-left: 10px;vertical-align: middle;">
                    <div class="row">
                        <div class="col-xs-7" >
                            
                                Order No：<b>${delivery.referenceNo}
                            </b>
                        </div>
                        <div class="col-xs-5" >
                            
                                Weight：<b>${delivery.weight}
                            </b>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-7" >
                                From：Killmall
                        </div>
                        <div class="col-xs-5" >
                                Date：<%= createdTimeStr %>
                            </b>
                        </div>
                    </div>
                </td>
            </tr>

            <tr style="height: 1.9cm;width: 10cm;">
                <td colspan="4" style="padding-left: 10px;vertical-align: middle;">
                    <div class="row">
                        <div class="col-xs-7" >
                            <b>
                                Signature：
                            </b>
                        </div>
                         
                        <div class="col-xs-5" >
                            <b>
                                Date：
                            </b>
                        </div>
                    </div>
                    <div class="row">
                    	<div class="col-xs-7" >
                            <b>  &nbsp;
                            </b>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-7" >
                            <b>
                                Rider：
                            </b>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>