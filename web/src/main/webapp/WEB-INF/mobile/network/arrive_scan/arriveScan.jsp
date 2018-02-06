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
    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2>Arrive Scan</h2>
    </div>
    <div class="search_banner">
        <div class="search_content" id="customers-search">
            <div class="search_input">
                <i></i>
                <input type="text" id="logisticsNo" name="logisticsNo" placeholder="Logistics No" class="search_input_field keywords"/>
            </div>
            <div class="search_button"><input type="button" value="scan" id="scan" class="search_input_button"/></div>
        </div>
        <div class="bottom_a_button11"><a onclick="delTr2()">delete</a></div>
        <div class="bottom_a_button22"><a onclick="Judge('fuxuan')">submit</a></div>
    </div>
</div>
<div>
    <table cellpadding="0" id="tab" cellspacing="0" class="pf_div1">
        <tr>
            <td>Logistics No</td>
            <td><input type="checkbox" id="allFuxuan" checked="checked" onclick="sel('fuxuan')"></td>
        </tr>
    </table>
</div>

<script>

    document.getElementById('scan').onclick = function(){android.startScan()};

    function doScan(){
        android.startScan();
    }
    function afterScan(scanResult){
        document.getElementById("logisticsNo").value = scanResult;
        addTr2('tab', 0,scanResult);
    }

    $("#logisticsNo").focus();
    $("#logisticsNo").keydown(function (event) {
        event = document.all ? window.event : event;
        if ((event.keyCode || event.which) == 13) {
            addTr2('tab', 0);
        }
    });

    function sel(a){
        var o=document.getElementsByName(a)
        for(var i=0;i<o.length;i++)
            o[i].checked=event.srcElement.checked
    }

    function delTr2(){
    	// android.startScan(afterScan);
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

    function addTr2(tab, row,scanResult) {
        var trHtml = "<tr align='center'><td>" +scanResult+ "</td><td><input type=\"checkbox\" checked=\"checked\" name=\"fuxuan\" value=\""+scanResult+"\"></td></tr>";
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
        $("#logisticsNo").val("");
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
            url: "/mobile/MobileArriveScanController/submit.html",
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
</script>
</body>
</html>
