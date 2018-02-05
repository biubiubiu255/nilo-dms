<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta content="telephone=no" name="format-detection" />
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
    <meta name="keywords" content="#" />
    <meta name="description" content="#" />
    <title></title>
    <link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
    <link href="/mobile/css/mp.css" type="text/css" rel="stylesheet" />
    <link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
    <script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/mobile/js/functions.js"></script>
</head>
<body>

<div class="wap_content">
    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2>Stocktaking</h2>
    </div>
    <div class="search_banner">
        <div class="search_content" id="customers-search">
            <div class="search_input">
                <i></i>
                <input type="text" placeholder="Logistics No" id="logisticsNo" class="search_input_field keywords"/>
            </div>
            <div class="search_button"><input type="button" value="submit" class="search_input_button submit"/></div>
        </div>
    </div>
</div>
<div>
    <table cellpadding="0" id="tab" cellspacing="0" class="pf_div1">
        <tr>
            <td>Logistics No</td>
        </tr>
    </table>
</div>

<script>
    $("#logisticsNo").focus();
    $("#logisticsNo").keydown(function (event) {
        event = document.all ? window.event : event;
        if ((event.keyCode || event.which) == 13) {
            addTr2('tab', -1);
        }
    });
</script>
</body>
</html>
