<%@ page import="com.nilo.dms.service.impl.SessionLocal" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<% boolean isRider = SessionLocal.getPrincipal().isRider(); %>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta content="telephone=no" name="format-detection"/>
    <meta name="viewport"
          content="width=device-width, minimum-scale=1.0, maximum-scale=2.0"/>
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
    <meta name="keywords" content="#"/>
    <meta name="description" content="#"/>
    <!--<link rel="apple-touch-icon-precomposed" sizes="114x114" href="">
    <link rel="apple-touch-icon-precomposed" sizes="57x57" href=""> -->
    <title></title>
    <link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
    <link href="/mobile/css/mp.css" type="text/css" rel="stylesheet"/>
    <link href="/mobile/css/jxj_css.css" type="text/css" rel="stylesheet"/>
    <link href="/mobile/css/mps.css" type="text/css" rel="stylesheet"/>
    <script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/mobile/js/functions.js"></script>
    <script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
    <script type="text/javascript" src="/mobile/js/mobile.js"></script>
    <script type="text/javascript" src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>
</head>
<body>
<div class="wap_content">
    <div class="wap_top">
        <h2>KiliExpress</h2>
    </div>

    <%--<c:if test="${isRider }">--%>
    <div class="j_sy_n1">
        <div class="model_banner_title">
            <i class="model_banner_title"></i>
            <div>Handle function</div>
        </div>
        <ul class="model_banner_ul"
            style="width: 100%; overflow: hidden; margin: 0px;">
            <c:if test="${isRider==false}">
                <a href="/mobile/arrive/scan.html" title="">
                    <li><img src="/mobile/images/icon_3.png"/><br/><label data-locale="arrive_scan">Arrive Scan</label>
                    </li>
                </a>
            </c:if>
            <a href="/mobile/rider/sign/toSign.html" title="">
                <li><img src="/mobile/images/icon_1.png"/><br/><label data-locale="sign_scan">Sign</label></li>
            </a>
            <c:if test="${isRider==true}">
                <a href="/mobile/rider/stranded/scan.html" title="">
                    <li><img src="/mobile/images/icon_7.png"/><br/><label data-locale="delay">Delay</label></li>
                </a>
            </c:if>
            <%--<a href="/mobile/rider/problem/scan.html" title="">
                <li><img src="/mobile/images/icon_7.png" /><br /><label data-locale="problem">problem</label></li>
            </a>--%>
            <a href="/mobile/rider/refuse/scan.html" title="">
                <li><img src="/mobile/images/icon_7.png"/><br/><label data-locale="refuse">refuse</label></li>
            </a>
            <!-- <a href="/mobile/rider/COD/sign.html" title="">
                <li><img src="/mobile/images/icon_2.png" /><br /><label >Payment</label></li>
            </a>
            <a href="/mobile/rider/Batch/toPay.html" title="">
                <li><img src="/mobile/images/icon_5.png" /><br /><label>Batch Payment</label></li>
            </a> -->
            <!-- <a href="/mobile/rider/deliver/toScan.html" title="">
                <li><img src="/mobile/images/icon_5.png" /><br /><label>Rider Delivery</label></li>
            </a> -->
            <!-- <a href="/mobile/network/unpackage/unpack.html" title="">
                <li><img src="/mobile/images/icon_3.png" /><br /><label data-locale="unPack">UnPack</label></li>
            </a> -->
        </ul>
        <div class="clear"></div>
    </div>
    <%--</c:if>--%>
    <%-- <c:if test="${isRider==false}">
        <div class="model_banner">
            <div class="model_banner_title">
                <i class="model_banner_title"></i><div data-locale="home_network">Network</div>
            </div>
            <ul class="model_banner_ul">
                <a href="/mobile/arrive/scan.html" title="">
                    <li><img src="/mobile/images/icon_3.png" /><br /><label data-locale="arrive_scan">Arrive Scan</label></li>
                </a>
                <a href="/mobile/deliver/scan.html" title="">
                    <li><img src="/mobile/images/icon_5.png" /><br /><label data-locale="deliver_scan">Deliver Scan</label></li>
                </a>
                <a href="/mobile/send/scan.html" title="">
                    <li><img src="/mobile/images/icon_4.png" /><br /><label data-locale="send_scan">Send Scan</label></li>
                </a>
                <a href="/mobile/package/packing.html" title="">
                    <li><img src="/mobile/images/icon_3.png" /><br /><label data-locale="packing">Packing</label></li>
                </a>
                <a href="/mobile/package/list.html" title="">
                    <li><img src="/mobile/images/icon_3.png" /><br /><label data-locale="pack_list">Pack List</label></li>
                </a>
                <a href="/mobile/network/unpackage/unpack.html" title="">
                    <li><img src="/mobile/images/icon_3.png" /><br /><label data-locale="unPack">UnPack</label></li>
                </a>
                <a href="/mobile/rider/problem/scan.html" title="">
                    <li><img src="/mobile/images/icon_7.png" /><br /><label data-locale="problem">Problem</label></li>
                </a>
                <a href="/mobile/rider/stranded/scan.html" title="">
                    <li><img src="/mobile/images/icon_7.png" /><br /><label data-locale="stranded_parcel">Stranded Parcel</label></li>
                </a>
            </ul>
            <div class="clear"></div>
        </div>
    </c:if> --%>
    <div class="model_banner">
        <div class="model_banner_title">
            <i class="model_banner_title"></i>
            <div data-locale="home_basic">Basic function</div>
        </div>
        <ul class="model_banner_ul">
            <a href="/mobile/basic/route/route.html">
                <li><img src="/mobile/images/scheduler_info.png"/><br/><label data-locale="tracking_query">Routing
                    Query</label></li>
            </a>
            <a href="/mobile/password/toPage.html" title="">
                <li><img src="/mobile/images/index_icon_1.png"/><br/><label data-locale="modify_password">Modify
                    password</label></li>
            </a>
        </ul>
        <div class="clear"></div>
    </div>

</div>

<div
<%--		<c:if test="${isRider == false}">
			style="display: none;"
		</c:if>--%>
>
    <div class="model_banner_title">
        <i class="model_banner_title"></i>
        <div data-locale="today_task"></div>
    </div>

    <div class="list card">
        <!-- <a href="#" class="item item-icon-left">
            <i class="icon ion-home"></i>
            Month had been signed：<span id="deliveredMonthNum"></span>
        </a> -->
        <%--			<a href="#" class="item item-icon-left">
                        <i class="icon ion-ios-telephone"></i>
                        Task / delay / signed
                    </a>--%>
        <a href="#" class="item">
            <span id="day"></span>
        </a>
        <a href="#" class="item">
            <span id="yesterday"></span>
        </a>
        <a href="#" class="item">
            <span id="beforeYesterday"></span>
        </a>
    </div>
</div>

<script>
    $(function () {
        getReport();
        setInterval(getReport, 2*60*1000);
    });

    function getReport() {
        $.post('/mobile/getTaskReport.html', function (data) {
            var date = new Date();
            var today = date.Format("MM-dd");
            date.setDate(date.getDate() - 1);
            var yesterday = date.Format("MM-dd");
            date.setDate(date.getDate() - 1);
            var bfYesterday = date.Format("MM-dd");

            $("#day").html(today + ' task:' + data.data.day.taskDayNum + ",delay:" + data.data.day.delayDayNum + ",singed:" + data.data.day.deliveredDayNum);
            $("#yesterday").html(yesterday + ' task:' + data.data.yesterday.taskDayNum + ",delay:" + data.data.yesterday.delayDayNum + ",singed:" + data.data.yesterday.deliveredDayNum);
            $("#beforeYesterday").html(bfYesterday + ' task:' + data.data.beforeYesterday.taskDayNum + ",delay:" + data.data.beforeYesterday.delayDayNum + ",singed:" + data.data.beforeYesterday.deliveredDayNum);
        }, "json")
    }


    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

</script>
</body>
</html>
