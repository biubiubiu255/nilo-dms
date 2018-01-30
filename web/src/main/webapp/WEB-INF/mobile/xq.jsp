<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
	<link href="../mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
	<link href="../mobile/css/mp.css" type="text/css" rel="stylesheet" />
	<link href="../mobile/css/mps.css" type="text/css" rel="stylesheet" />
	<script src="../mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../mobile/js/functions.js"></script>
	<script type="text/javascript" src="../mobile/js/mobile_valid.js"></script>
	<script type="text/javascript" src="../mobile/js/mobile.js"></script>
	</head>
	
	<body>
		<div class="wap_content">
    <div class="wap_top"><a href="/DemoController/toIndexPage.html" title="返回" class="wap_top_back"></a>
        <h2></h2>
    </div>
    <div class="formula_modify">
        <div></div><br/>
        <div class="banner_content">
        <ul class="one_banner">
            <li id="group_customers_id">
                <label>订单号</label>
                <input type="tel"   name="phone" class="input_value" /><br/>
            </li>
            <li>
                <label style="float: left;">运单号</label>
                <input type="tel"   name="phone" class="input_value" />
            </li>
            <li>
                <label>金额</label>
                <input type="tel"   name="phone" class="input_value" /><br/>
            </li>
        	<li>
        		<label>拍照</label>
                <div class="xq input_value"><img src="images/2300.jpg" /></div>
        	</li>
        </ul>
        </div>
        <div class="clear"></div>

        <div class="bottom_a_button"><a href="javascript:void(0);" class="submit">提交</a></div>
    </div>

</div>
	</body>
</html>