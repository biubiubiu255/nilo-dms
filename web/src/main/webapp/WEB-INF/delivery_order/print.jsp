<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<%@ include file="../common/header.jsp" %>
<script type="text/javascript">window.print();</script>
<style media="all">
    * {
        font-size: small;
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

    .logo-print {
        height: 1.53cm;
        width: 1.53cm;
    }

    .barcode-print {
        height: 1.53cm;
        width: 5cm;
    }
    .print-font-large{
        font-size: large;
    }
</style>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">window.print();</script>
<body>
<div style="width:10cm; height:10cm;">
    <div class="row" style="margin: 2px">
        <table class="table table-bordered">
            <tr style="height: 1.53cm;width: 10cm;">
                <td colspan="1" align="center">
                    <div class="row">
                        <div class="col-xs-12"><img src="${ctx}/dist/img/logo.png" class="logo-print"></div>
                    </div>
                </td>
                <td colspan="2" align="center">
                    <div>${delivery.orderNo}</div>
                    <div><img src="/barCode/${delivery.orderNo}.html" class="barcode-print">
                    </div>
                </td>
                <td colspan="1" align="center">
                    <div>${delivery.orderType}</div>
                    <div>Doorstep</div>
                </td>
            </tr>
            <tr style="height: 2.3cm;width: 10cm;">
                <td colspan="4" style="padding-left: 10px">
                    <div class="row">
                        <div class="col-xs-12">
                            To:${delivery.receiverInfo.receiverName}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            Address:${delivery.receiverInfo.receiverAddress}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <span class="print-font-large">Tell:${delivery.receiverInfo.receiverPhone}</span>
                        </div>
                    </div>
                </td>
            </tr>
            <tr style="height: 2.3cm;width: 10cm;">
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
            </tr>
            <tr style="height: 1.53cm;width: 10cm;">
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
            </tr>
            <tr style="height: 1.53cm;width: 10cm;">
                <td colspan="4" style="padding-left: 10px">
                    <div class="row">
                        <div class="col-xs-6">
                            Signature:
                        </div>
                        <div class="col-xs-6">
                            Date:
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-6">
                            Rider:
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>