<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<%@ include file="../common/header.jsp" %>

<script type="text/javascript">window.print();</script>
<style>
</style>
<body style="margin:2px;">
<div style="width: 240px;height: 680px">
    <table width="450" height="265px"  border="1" cellspacing="0" cellpadding="0">

        <tr style="height: 75px;width: 450px;" >
            <td width="90px"><img src="${ctx}/dist/img/logo.png" style="height: 75px;width: 90px">
            </td>
            <td width="270px" align="center">
                <div>${delivery.orderNo}</div>
                <div><img src="/barCode/${delivery.orderNo}.html" style="height: 45px;width: 250px"></div>
            </td>
            <td width="90px" align="center">
                <div>${delivery.orderType}</div>
                <div><c:if test="${delivery.channel=='Y'}">Sell Collect</c:if><c:if test="${delivery.channel != 'Y'}">Doorstep</c:if></div>
            </td>
        </tr>
        <tr style="height: 120px;width: 500px;padding-left: 10px">
            <td colspan="3" >
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md12 layui-col-xs12">
                        Order No.:${delivery.referenceNo}
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md6 layui-col-xs6">
                        Payment Method:Paid on Online
                    </div>
                    <div class="layui-col-md6 layui-col-xs6">
                        Amount Due:Ksh 0
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md6 layui-col-xs6">
                        MPESA Paybill No.:999888
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md6 layui-col-xs6">
                        Account No.:12345678
                    </div>
                </div>
            </td>
        </tr>
        <tr style="height: 75px;width: 500px;">
            <td style="padding-left: 10px" colspan="3">
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md12 layui-col-xs12">
                        Thank you for shopping at Kilimall.
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md12 layui-col-xs12">
                        Customer Service Number:+254(0) 700 717 000
                    </div>
                </div>
            </td>
        </tr>
    </table>
    <br>
    <table width="450" height="370" border="1" cellspacing="0" cellpadding="0" >

        <tr style="height: 85px;width: 450px;" >
            <td width="400px" align="center" style="padding-left: 10px">
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md12 layui-col-xs12">
                        Tracking No:${delivery.orderNo}
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md12 layui-col-xs12">
                        <img src="/barCode/${delivery.orderNo}.html" style="height: 45px;width: 300px">
                    </div>
                </div>

            </td>
            <td width="100px" align="center" style="padding-left: 10px">
                <div>${delivery.orderType}</div>
                <div><c:if test="${delivery.channel=='Y'}">Sell Collect</c:if><c:if test="${delivery.channel != 'Y'}">Doorstep</c:if></div>
            </td>
        </tr>
        <tr style="height: 130px;width: 450px;">
            <td colspan="2" style="padding-left: 10px">
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md12 layui-col-xs12">
                        To:${delivery.receiverInfo.receiverName}
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md12 layui-col-xs12">
                        Address:${delivery.receiverInfo.receiverAddress}
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md12 layui-col-xs12">
                        Tell:${delivery.receiverInfo.receiverPhone}
                    </div>
                </div>
            </td>
        </tr>
        <tr style="height: 60px;width: 450px;">
            <td colspan="2" style="padding-left: 10px">
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md6 layui-col-xs6">
                        From:Kilimall International Limited
                    </div>
                    <div class="layui-col-md6 layui-col-xs6">
                        Date: <lp:formatTime time="${delivery.createdTime }"
                                             pattern="yyyy-MM-dd"/>
                    </div>
                </div>

            </td>
        </tr>
        <tr style="height: 110px;width: 450px;">
            <td colspan="2" style="padding-left: 10px">
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md6 layui-col-xs6">
                        Order No.:${delivery.referenceNo}
                    </div>
                    <div class="layui-col-md6 layui-col-xs6">
                        Weight: ${delivery.weight}
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md6 layui-col-xs6">
                        Payment Method:Paid on Online
                    </div>
                    <div class="layui-col-md6 layui-col-xs6">
                        Amount Due:Ksh 0
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md6 layui-col-xs6">
                        MPESA Paybill No.:999888
                    </div>
                    <div class="layui-col-md6 layui-col-xs6">
                        Account No.:12345678
                    </div>
                </div>
            </td>
        </tr>
        <tr style="height: 75px;width: 450px;">
            <td colspan="2" style="padding-left: 10px">
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md6 layui-col-xs6">
                        Signature:
                    </div>
                    <div class="layui-col-md6 layui-col-xs6">
                        Date:
                    </div>
                </div>
                <div class="layui-row layui-col-space5">
                    <div class="layui-col-md12 layui-col-xs12">
                        Rider:
                    </div>
                </div>
            </td>
        </tr>
    </table>
</div>
</body>
</html>