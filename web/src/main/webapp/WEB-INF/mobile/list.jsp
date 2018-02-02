<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
    if (session.getAttribute("userId") != null) {
        //  已登陆用户回主面板
        response.sendRedirect("/dashboard.html");
        return;
    }
%>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta content="telephone=no" name="format-detection" />
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
    <meta name="keywords" content="#" />
    <meta name="description" content="#" />
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="">
    <link rel="apple-touch-icon-precomposed" sizes="57x57" href="">
    <title>登录</title>
    <link href="/mobile/css/cssdemo.css" type="text/css" rel="stylesheet" />
</head>
<body>
<div class="j_wap">
    <div class="j_dly">
        <div class="j_dly_logo"><img src="/mobile/images/logo.png"><br/></div>

        <form id="login-form" method="post">

            <div class="j_dly_1">
                <i class="j_dly_1i1"></i>
                <div class="j_dly_2">
                    <input type="text" name="username" placeholder="username"/>
                </div>
            </div>
            <div class="j_dly_1">
                <i class="j_dly_1i2"></i>
                <div class="j_dly_2">
                    <input type="password" name="password" placeholder="password"/>
                </div>
            </div>
            <div class="j_dly_5">
                <a href="" title="forget password?">forget password?</a>
            </div>
            <p class="login-box-msg">Sign in to start your session</p>
            <div class="j_dly_33">
                <input type="submit" value="log in" />
            </div>
        </form>
        </div>
    </div>
<script src="/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="/bootstrap/js/bootstrap.min.js"></script>
<!-- iCheck -->
<script src="/plugins/iCheck/icheck.min.js"></script>
<script>
    $('#login-form').submit(function () {
        $('.login-box-msg').html("Waiting...");
        var params = $(this).serialize();
        $.post('/mobile/test.html', params, function (resp) {
            if (resp.result) {

                // location.href = 'dashboard.html';
                location.href = '/mobile/toIndexPage.html';
            } else {
                $('.login-box-msg').html(resp.msg);
            }
        }, "json");
        return false;
    });
</script>
</body>

</html>