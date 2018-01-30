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
                    <li>
                        <label>Logistics No</label>
                        <input type="tel" id="waybillNumber" name="waybillNumber" class="input_value" />
                        <span>scan</span>
                    </li>
                    <li><label>Signer</label><input type='text' id="recipient" class='input_value' name='recipient' /><span>Aquire</span></li>
                    <li><label>ID No</label><input type='text' class='input_value' name='idNumber' /></li>
                    <li>
                        <label>Take a picture</label>
                        <div class="xq input_value"><img src="/mobile/images/2300.jpg" /></div>
                    </li>
                </ul>
                <div class="bottom_a_button"><a onclick="doFind()">submit</a></div>
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


<script type="text/javascript">
    function doFind() {
        $.ajax({
            cache: false,
            type: "POST",
            url: "/mobile/SignScanController/test.html",
            data: $('#myForm').serialize(),
            async: false,
            error: function () {
                alert("发送请求失败！");
            },
            success: function () {
                addTr2('tab', -1);
            }
        });
    }

    function addTr2(tab, row) {
        var kuang1 = document.getElementById("waybillNumber")
        var kuang2 = document.getElementById("recipient")
        var trHtml = "<tr align='center'><td>" +kuang1.value+ "</td><td>" +kuang2.value+ "</td><td><input type=\"checkbox\"></td></tr>";
        addTr(tab, row, trHtml);
    }


    function addTr(tab, row, trHtml){
        //获取table最后一行 $("#tab tr:last")
        //获取table第一行 $("#tab tr").eq(0)
        //获取table倒数第二行 $("#tab tr").eq(-2)
        var $tr=$("#"+tab+" tr").eq(row);
        if($tr.size()==0){
            alert("指定的table id或行数不存在！");
            return;
        }
        $tr.after(trHtml);
    }
</script>
</body>
</html>
