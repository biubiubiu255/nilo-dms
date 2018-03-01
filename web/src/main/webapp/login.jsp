<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.nilo.dms.common.utils.WebUtil" %>
<%
    if (session.getAttribute("userId") != null) {
        //  已登陆用户回主面板
        response.sendRedirect("/dashboard.html");
        return;
    }
%>
<html background="public/img/bg.jpg">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>
        Nilo-DMS
    </title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="./plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <!-- Theme style -->
    <link rel="stylesheet" href="./dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="./dist/css/skins/all-skins.min.css">

</head>

<body class="hold-transition" background="/public/img/bg.jpg">
<div class="login-box">
    <div class="login-logo">
        DMS
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body">
        <p class="login-box-msg">Sign in to start your session</p>

        <form id="login-form" method="post">
            <div class="form-group has-feedback">
                <input class="form-control" placeholder="Account" name="username">
                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" class="form-control" placeholder="Password" name="password">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="form-group has-captcha" style="display: none;">
                <input type="text" name="randomCode" class="form-control" placeholder="Captcha" autocomplete="off">
            </div>
            <div class="row">

                <div class="col-xs-8" style="display: none;">
                    <img id="captcha_img" alt="refresh" src="captcha/image.html"/>
                </div>
                <div class="col-xs-4" style="float: right;">
                    <button type="submit" class="btn btn-primary btn-block btn-flat">Sign In</button>
                </div>
                <!-- /.col -->
            </div>
        </form>

    </div>
</div>
<script src="./plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="./bootstrap/js/bootstrap.min.js"></script>
<script>

        $(function () {
            $.get('account/verify.html', function (resp) {
                if (resp.result) {
                    setVerifyStyle(true);
                } else {
                    setVerifyStyle(false);
                }
            }, "json");
        });

        var loginNum = 0;
        $('#login-form').submit(function () {
            $('.login-box-msg').html("Waiting...");
            var params = $(this).serialize();
            $.post('account/login.html', params, function (resp) {
                if (resp.result) {
                    location.href = 'dashboard.html';
                } else {
                    $('.login-box-msg').html(resp.msg);
                    if(++loginNum >= 3){
                        setVerifyStyle(true);
                    }
                }
            }, "json");
            return false;
        });

        $('#captcha_img').click(function () {
            var src = "captcha/image.html?";
            $(this).attr('src', src + Math.random());
        }).trigger('click');

        function setVerifyStyle(bool) {
            if(bool===false){
                $(".has-captcha").first().attr("style", "display:none;");
                $(".col-xs-8").first().attr("style", "display:none;");
            }else {
                $(".has-captcha").first().attr("style", "display:block;");
                $(".col-xs-8").first().attr("style", "display:block;");
            }
        }

</script>
</body>
</html>