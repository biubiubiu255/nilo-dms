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
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
</head>
<body>

<div class="wap_content">

    <div class="wap_top"><a href="/mobile/DemoController/toIndexPage.html" title="Back" class="wap_top_back"></a>
        <h2>Stranded Parcel</h2>
    </div>

    <div class="formula_modify">
        <form id="myForm" class="layui-form" action="">
            <div class="banner_content">
                <ul class="one_banner">
                    <li><label>Logistics No</label><input type='text' maxlength='100' class='input_value' name='waybillNumber' /><span>scan</span></li>
                    <li>
                        <label>station</label>
                        <select required="required" class='input_value' name='site'>
                            <option value="1">Please select the site</option>
                            <option value="2">test2</option>
                            <option value="3">nckjd</option>
                            <option value="4">dvad</option>
                            <option value="5">test5</option>
                            <option value="6">test6</option>
                        </select>
                    </li>
                    <li><label>Driver</label><input type='text' maxlength='100' class='input_value' name='driver' /></li>
                    <li><label>Plate No</label><input type='text' maxlength='100' class='input_value' name='licensePlateNumber' /><span>save</span></li>

                </ul>
                <div class="bottom_a_button"><a onclick="">submit</a></div>
                <%--<div class="bottom_a_button"><a onclick="">delete</a></div>--%>
            </div>
        </form>
    </div>
    <div>
        <table cellpadding="0" id="tab" cellspacing="0" class="pf_div1">
            <tr>
                <td>Logistics No</td>
                <td>Driver</td>
                <td><input type="checkbox"></td>
            </tr>
        </table>
    </div>
</div>

</body>
</html>
