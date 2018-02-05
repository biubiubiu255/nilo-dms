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
    <!--<link rel="apple-touch-icon-precomposed" sizes="114x114" href="">
    <link rel="apple-touch-icon-precomposed" sizes="57x57" href="">-->
    <title></title>
    <link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
    <link href="/mobile/css/mp.css" type="text/css" rel="stylesheet" />
    <link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
    <script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/mobile/js/functions.js"></script>
</head>
<body>
<div class="wap_content">
    <div class="wap_top"><a href="javascript:history.go(-1)" title="返回" class="wap_top_back"></a>
        <h2>Modify password</h2>

    </div>


    <div class="formula_modify">
        <div></div><br/>
        <div class="banner_content">
        <ul class="one_banner">
            <li id="group_customers_id">
                <%--<label>Old password</label>--%>
                <input type="tel" placeholder="Old password"  name="old" class="input_value22" /><br/>
            </li>
            <li>
                <%--<label style="float: left;">New password</label>--%>
                <input type="tel"  placeholder="New password" name="new" class="input_value22" />
            </li>
            <li>
                <%--<label>Repeat the new password</label>--%>
                <input type="tel" placeholder="Repeat the new password"  name="repeat" class="input_value22" /><br/>
            </li>

        </ul>
        </div>
        <div class="bottom_a_button"><a onclick="">submit</a></div>
    </div>

</div>
</body>
</html>