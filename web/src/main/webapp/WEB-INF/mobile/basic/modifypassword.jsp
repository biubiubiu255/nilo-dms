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
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        loadLanguage('cn');
        var mobile = new MobileData({
            autoLoad: false
            , formId: 'password-form'
            , model: 'customers'
        });
        mobile.initSubmitForm({
            formId:'password-form'
            ,mbObject:mobile
            ,postUrl:'/mobile/password/submit.html'
            ,beforeSubmit:function () {
                var newPassword = mobile.getFormField('newPassword');
                var againPassword = mobile.getFormField('againPassword');
                if(newPassword.val() == againPassword.val()){
                    return true;
                }else{
                    showError("Two password entries are inconsistent");
                }
            }
            ,callback:function (data) {
                if (data.result) {
                    showInfo('Success');
                    // var url = "http://127.0.0.1:8080/mobile/login.html";
                    // window.location.href = url;
                } else {
                    showError(data.msg)
                }
            }
        });

    });
</script>
</head>
<body>
<div class="wap_content">
    <div class="wap_top"><a href="javascript:history.go(-1)" title="返回" class="wap_top_back"></a>
        <h2 data-locale="password_title">Modify password</h2>
    </div>

    <div class="formula_modify">
        <form id="password-form">
            <div class="banner_content">
                <ul class="one_banner">
                    <li>
                        <%--<label>Old password</label>--%>
                        <input type="text" required="required" placeholder="Old password"  name="oldPassword" property_name="password_oldPassword" set_attr="placeholder" class='input_value22 i18n-input' /><br/>
                    </li>
                    <li>
                        <%--<label style="float: left;">New password</label>--%>
                        <input type="password" required="required"  placeholder="New password" name="newPassword" property_name="password_newPassword" set_attr="placeholder" class='input_value22 i18n-input' />
                    </li>
                    <li>
                        <%--<label>Repeat the new password</label>--%>
                        <input type="password" required="required" placeholder="Repeat the new password"  name="againPassword" property_name="password_againPassword" set_attr="placeholder" class='input_value22 i18n-input' /><br/>
                    </li>

                </ul>
            </div>
            <div class="bottom_a_button"><a href="javascript:void(0);" class="submit" data-locale="all_submit">submit</a></div>
        </form>
    </div>
</div>
</body>
</html>