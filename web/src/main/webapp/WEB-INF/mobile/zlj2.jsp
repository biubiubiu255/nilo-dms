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
<link href=/mobile/"css/mps.css" type="text/css" rel="stylesheet" />
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
                    <li>
                        <%--<label>Logistics No</label>--%>
                        <input type="tel" placeholder="Logistics No" id="waybillNumber" name="waybillNumber" class="input_value" />
                        <span>scan</span>
                    </li>
                    <li>
                        <%--<label style="float: left;">Carrier site</label>--%>
                        <select required="required" class='input_value' name='carrierSite'>
                            <option value="0">Please enter Carrier site</option>
                            <option value="test1">test1</option>
                            <option value="test2">test2</option>
                            <option value="test3">test3</option>
                            <option value="test4">test4</option>
                            <option value="test5">test5</option>
                            <option value="test6">test6</option>
                        </select>
                    </li>
                    <li>
                        <%--<label>Stranded type</label>--%>
                        <select required="required" id="detainedType" class='input_value' name='detainedType'>
                            <option value="0">Stranded type</option>
                            <option value="test1">test1</option>
                            <option value="test2">test2</option>
                            <option value="Address error">Address error</option>
                            <option value="Other reasons">Other reasons</option>
                            <option value="test5">test5</option>
                            <option value="test6">test6</option>
                        </select>
                    </li>
                    <li>
                        <%--<label>Memo</label>--%>
                        <input type="tel" placeholder="Memo" name="remarks" class="input_value" /><br/>
                    </li>
                </ul>
                <div class="bottom_a_button"><a onclick="addTr2('tab', -1)">submit</a></div>
            </div>
        </form>
    </div>
    <div>
        <table cellpadding="0" id="tab" cellspacing="0" class="pf_div1">
            <tr>
                <td>Order number</td>
                <td>Detained type</td>
                <td><input type="checkbox"></td>
            </tr>
        </table>
    </div>

</div>

</body>
</html>
