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
<title>业务员功能</title>


<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
<link href="/mobile/css/mp.css" type="text/css" rel="stylesheet" />
<link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
</head>
<body>


<div class="wap_content">

    <div class="wap_top"><a href="/mobile/DemoController/toIndexPage.html" title="返回" class="wap_top_back"></a>
        <h2>Stranded Parcel</h2>
    </div>

    <div class="formula_modify">
        <form id="myForm" class="layui-form" action="">
            <div class="banner_content">
                <ul class="one_banner">
                    <li><label>Logistics No</label><input type='text' maxlength='100' class='input_value' name='customers_address' /><span>Scan</span></li>
                    <li><label>Signer</label><input type='text' maxlength='13' class='input_value' name='customers_phone' /><span>Aquire</span></li>
                    <li><label>ID No</label><input type='text' maxlength='13' class='input_value' name='customers_fax' /></li>
                    <li><label>Pay Type</label><input type="radio" class="danxuan" name="danxuan"/>cash &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio" class="danxuan" name="danxuan"/>Mobile payment</li>
                    <li><label>Take a picture</label><div class="xq input_value"><img src="/mobile/images/2300.jpg" /></div></li>
                </ul>
                <div class="bottom_a_button"><a onclick="">submit</a></div>
            </div>
        </form>
    </div>
    <div>
        <table cellpadding="0" id="tab" cellspacing="0" class="pf_div1">
            <tr>
                <td>Logistics No</td>
                <td>Signer</td>
                <td><input type="checkbox"></td>
            </tr>
        </table>
    </div>
</div>

</body>
</html>
