<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<%
    request.setAttribute("now_time", DateUtil.formatCurrent("yyyy-MM-dd HH:mm"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge;chrome=1">
    <title>Print</title>
    <script src="${ctx}/plugins/jQuery/jquery-2.2.3.min.js"></script>
</head>
<body>
<style media=print>
    .Noprint {
        display: none;
    }

    .PageNext {
        page-break-after: always;
    }
</style>
<script type="text/javascript">
    $(function () {
        window.print();
    })
</script>
<style type="text/css">
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


<ul id="printer" class="printer">
    <c:forEach items="${list}" var="delivery">

        <li class="r1">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="lister">
                <tbody>
                <tr class="title">
                    <td colspan="2" style="padding-bottom:5px;"><img src="${ctx}/dist/img/logo.png" height="30">
                    </td>
                    <td class="cc" colspan="2" style="padding-bottom:5px;">Pickup list</td>
                    <td  style="font-size:14px; text-align:right;">PrintTimes:${delivery.printTimes}</td>
                    <td style="font-size:14px; text-align:right;">No.${delivery.orderNo}</td>
                </tr>
                <tr>
                    <th><strong>Supplier:</strong></th>
                    <td>${delivery.senderInfo.senderName}</td>
                    <th><strong>Receive company:</strong></th>
                    <td>Kilimall</td>
                    <th><strong>Date:</strong></th>
                    <td>${now_time}</td>
                </tr>
                <tr>
                    <th><strong>contact name:</strong></th>
                    <td>${delivery.senderInfo.senderName}</td>
                    <th><strong>contact name:</strong></th>
                    <td>everlyne</td>
                    <th></th>
                    <td></td>
                </tr>
                <tr style="background: rgb(255, 255, 255);">
                    <th><strong>contact:</strong></th>
                    <td>0734550177</td>
                    <th><strong>contact:</strong></th>
                    <td>0707624326</td>
                    <th><strong>WareHouse:</strong></th>
                    <td>Narobi01</td>
                </tr>
                <tr style="background: rgb(255, 255, 255);">
                    <th><strong>Address:</strong></th>
                    <td>${delivery.senderInfo.senderAddress}</td>
                    <th><strong>Address:</strong></th>
                    <td>Athi55</td>
                    <td></td>
                    <td></td>
                </tr>
                </tbody>
            </table>
            <div style="color:#000; padding:5px 0px;"><strong>Receive Type</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                PO.: <span
                        style="font-size:18px">□</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sample: <span
                        style="font-size:18px">□</span></div>

            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="lister">
                <tbody>
                <tr style="background: rgb(255, 255, 255);">
                    <th><strong>SKU No.</strong></th>
                    <th><strong>Description</strong></th>
                    <th><strong>Qty</strong></th>
                    <th><strong>Specifications</strong></th>
                    <th><strong>Kilimall Order</strong></th>
                    <th><strong>Subtotal</strong></th>

                </tr>
                <c:forEach items="${delivery.goodsInfoList}" var="item">
                    <tr>
                        <td>${item.goodsId}</td>
                        <td>${item.goodsDesc}</td>
                        <td>${item.qty}</td>
                        <td></td>
                        <td>${delivery.referenceNo}</td>
                        <td>KSh ${item.unitPrice}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <p>&nbsp;</p>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="intro2">
                <tbody>
                <tr style="background: rgb(255, 255, 255);">
                    <td style="font-size:14px">Remark:</td>
                </tr>
                <tr style="background: rgb(255, 255, 255);">
                    <td style="padding-top:8px;">1.&nbsp;Kilimall Drop Shipping SOP shall be fully complied to increase
                        the
                        pickup efficiency and therefore increase the customer satisfaction.
                    </td>
                </tr>
                <tr style="background: rgb(255, 255, 255);">
                    <td style="padding-top:8px;">2.&nbsp;Sellers are required to handover the right and complete
                        products in
                        due time to Kilimall or Kilimall authorised partners according to order information.
                    </td>
                </tr>
                <tr style="background: rgb(255, 255, 255);">
                    <td style="padding-top:8px;">3.&nbsp;In case of customer rejecting the order with due reason or
                        unjustified reason, Kilimall After Sale Policy will be applied
                    </td>
                </tr>
                <tr style="background: rgb(255, 255, 255);">
                    <td style="padding-top:8px;">4. This Pickup Note will not form any purchase relationship between
                        Kilimall and Seller.
                    </td>
                </tr>
                <tr style="background: rgb(255, 255, 255);">
                    <td style="padding-top:8px;">5. The document does not serve as settlement document.</td>
                </tr>
                <tr style="background: rgb(255, 255, 255);">
                    <td style="padding-top:8px;">6. In case of any problem with Kilimall logistic team or Kilimall
                        authorised partner, please Call 0709717000 or email sellercenter@Kilimall.com. Kilimall seller
                        center team will provide sufficient support to you in real time. Seller Centre working hours:
                        8:00am
                        - 5pm, from Monday to Friday and 8 am - 12pm on Saturday.
                    </td>
                </tr>
                </tbody>
            </table>
            <p>&nbsp;</p>

            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="intro2">
                <tbody>
                <tr style="background: rgb(255, 255, 255);">
                    <td style="padding-bottom:5px;">Purchase sign:<span
                            style="border-bottom:1px solid #333;padding:0px 55px;">&nbsp;</span></td>
                    <td style="padding-bottom:5px;">Approve sign:<span
                            style="border-bottom:1px solid #333; padding:0px 55px;">&nbsp;</span></td>

                </tr>
                <tr>
                    <td style="padding-bottom:5px;">Date:<span style="border-bottom:1px solid #333;  padding:0px 55px;">&nbsp;</span>
                    </td>
                    <td style="padding-bottom:5px;">Date:<span style="border-bottom:1px solid #333; padding:0px 55px;">&nbsp;</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </li>

    </c:forEach>
</ul>

</body>
</html>