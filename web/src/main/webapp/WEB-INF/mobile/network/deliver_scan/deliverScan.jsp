<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2>Deliver Scan</h2>
    </div>

    <div class="formula_modify">
        <form id="myForm" class="layui-form" action="">
            <div class="banner_content">
                <ul class="one_banner">
                    <li>
                        <%--<label>station</label>--%>
                        <select required="required" class='input_value' id="station" name='station'>
                            <option value="0">Please select the site</option>
                            <c:forEach items="${station}" var="station">
                                <option value="${station.code}" id="hhh" type="${station.type}">${station.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                    <li>
                        <select class='input_value' name="deliverDriver" id="deliverDriver" lay-search="">
                            <option value="0">Please select a driver</option>
                        </select>
                    </li>
                    <li><input type='text' placeholder="Logistics No" maxlength='100' class='input_value' id="logisticsNo" name='logisticsNo' /><span id="scan">scan</span></li>
                    <li><input type='text' placeholder="Plate No" maxlength='100' class='input_value' id='plateNo' name='plateNo' /><span onclick="addTr2('tab');">save</span></li>

                </ul>
                <div class="bottom_a_button11"><a onclick="delTr2()">delete</a></div>
                <div class="bottom_a_button22"><a onclick="Judge('fuxuan')">submit</a></div>
            </div>
        </form>
    </div>
    <div>
        <table cellpadding="0" id="tab" cellspacing="0" class="pf_div1">
            <tr>
                <td>Logistics No</td>
                <td><input type="checkbox" id="allFuxuan" checked="checked" onclick="sel('fuxuan')"></td>
            </tr>
        </table>
    </div>
</div>

<script>
    document.getElementById('scan').onclick = function(){android.startScan()};

    function doScan(){
        android.startScan();
    }
    function afterScan(scanResult){
        document.getElementById("logisticsNo").value = scanResult;
    }

    $(document).ready(function(){
        var kuang3 = document.getElementById("station")
        $("#station").change(function(){
            var code = kuang3.value;
            getNextStationDriver(code)
            // alert(kuang3.value)
        });
    });
    function addTr2(tab) {
        var kuang1 = document.getElementById("logisticsNo")
        var kuang2 = document.getElementById("station")
        var kuang3 = document.getElementById("deliverDriver")
        var kuang4 = document.getElementById("plateNo")
        if(kuang1.value == ""){
            alert("Logistics no cannot be empty")
        }else if(kuang2.value == 0){
            alert("Please select a site")
        }else if(kuang3.value == 0){
            alert("driver no cannot be empty")
        }else{
            var trHtml = "<tr align='center'><td>" +kuang1.value+ "</td><td><input type=\"checkbox\" checked=\"checked\" name=\"fuxuan\" value=\"" +kuang1.value+ "\"></td></tr>";
            addTr(tab,  trHtml);
        }

    }
    function addTr(tab, trHtml){
        //获取table最后一行 $("#tab tr:last")
        //获取table第一行 $("#tab tr").eq(0)
        //获取table倒数第二行 $("#tab tr").eq(-2)
        var $tr=$("#"+tab+" tr").eq(0);
        if($tr.size()==0){
            alert("指定的table id或行数不存在！");
            return;
        }
        $tr.after(trHtml);
        $("#logisticsNo").val("");
        // $("#station").val("0");
        // $("#deliverDriver").val("0");
        $("#plateNo").val("");
    }
    function sel(a){
        var o=document.getElementsByName(a)
        for(var i=0;i<o.length;i++)
            o[i].checked=event.srcElement.checked
    }

    function delTr2(){
        delTr('fuxuan');
    }

    function delTr(fuxuan){
        //获取选中的复选框，然后循环遍历删除
        var fuxuans=$("input[name="+fuxuan+"]:checked");
        if(fuxuans.size()==0){
            alert("You did not select the required action！");
            return;
        }
        fuxuans.each(function(){
            $(this).parent().parent().remove();
        });
    }
    function Judge(fuxuan){
        //获取选中的复选框，然后循环遍历删除
        var fuxuans=$("input[name="+fuxuan+"]:checked");
        if(fuxuans.size()==0){
            alert("You did not select the required action！");
            return;
        }else {
            suiyi(fuxuan);
        }
    }
    function suiyi(fuxuan) {
        var arr = new Array();
        $("input[name="+fuxuan+"]:checked").each(function (i, n) {
            arr.push($(this).val());
        });
        $.ajax({
            cache: false,
            type: "POST",
            traditional: true,
            url: "/mobile/deliver/test.html",
            data : {arr : arr},
            async: false,
            error: function () {
                alert("发送请求失败！");
            },
            success: function () {
                console.log("zzzzzzzzzzzzzzzzzzzzzz")
                // addTr2('tab', -1);
                delTr(fuxuan);
            }
        });
    }
    function getNextStationDriver(code) {
        $.ajax({
            type: "POST",
            url: "/mobile/deliver/getDriver.html",
            dataType: "json",
            data: {code: code},
            success: function (data) {
                if (data.result) {
                    $("#deliverDriver").empty();
                    $("#deliverDriver").prepend("<option value='0'>Please select a driver</option>");
                    var driver = data.data;
                    for (var i = 0; i < driver.length; i++) {
                        $("#deliverDriver").append("<option value='" + driver[i].code + "'>" + driver[i].name + "</option>");
                    }
                    form.render();
                }
            }
        });
    }
</script>
</body>
</html>
