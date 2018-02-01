<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
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

<script type="text/javascript">
</script>
</head>
<body>
<div class="wap_content">

    <div class="wap_top"><a href="/mobile/DemoController/toIndexPage.html" title="Back" class="wap_top_back"></a>
        <h2>Stranded Parcel</h2>
    </div>

    <div class="formula_modify">
        <form id="myForm" class="layui-form">
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
                <div class="bottom_a_button"><a onclick="doFind()">submit</a></div>
            </div>
        </form>
    </div>
    <div>
        <table cellpadding="0" id="tab" cellspacing="0" class="pf_div1">
            <tr>
                <td>Logistics No</td>
                <td>Stranded type</td>
            </tr>
        </table>
    </div>

</div>

<script type="text/javascript">
    function doFind() {
        $.ajax({
            cache: false,
            type: "POST",
            url: "/mobile/StrandedParcelController/test.html",
            data: $('#myForm').serialize(),
            async: false,
            error: function () {
                alert("发送请求失败！");
            },
            success: function () {
                console.log("tttttttttttttt");
                addTr2('tab', -1);
            }
        });
    }

    function addTr2(tab, row) {
        var kuang1 = document.getElementById("waybillNumber")
        var kuang2 = document.getElementById("detainedType")
        console.log("``````````````````````")
        console.log(kuang1.value)
        console.log(kuang2.value)
        var trHtml = "<tr align='center'><td>" +kuang1.value+ "</td><td>" +kuang2.value+ "</td></tr>";
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
