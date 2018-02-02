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
        <h2>Porblem</h2>
    </div>

    <div class="formula_modify">
        <form id="myForm" class="layui-form" action="">
            <div class="banner_content">
                <ul class="one_banner">
                    <li><input type='text' placeholder="Logistics No" maxlength='100' class='input_value' id='logisticsNo' name='logisticsNo' /><span>scan</span></li>
                    <li>
                        <%--<label>Reason</label>--%>
                        <select required="required" class='input_value' id="reason" name='reason'>
                            <option value="0">Please select a return reason</option>
                            <option value="Over Scope Of Deliver">Over Scope Of Deliver</option>
                            <option value="Unable to contact customers">Unable to contact customers</option>
                            <option value="Return">Return</option>
                            <option value="test">test</option>
                            <option value="Rejection">Rejection</option>
                        </select><span onclick="addTr2('tab', -1);">Save</span>
                    </li>
                    <li><input type='text' placeholder="Memo" maxlength='100' class='input_value' id="memo" name='memo' /></li>
                </ul>
                <div class="bottom_a_button11"><a onclick="delTr2()">delete</a></div>
                <div class="bottom_a_button22"><a onclick="suiyi('fuxuan')">submit</a></div>
            </div>
        </form>
    </div>
    <div>
        <table cellpadding="0" id="tab" cellspacing="0" class="pf_div1">
            <tr>
                <td>Logistics No</td>
                <td>Reason</td>
                <td><input type="checkbox" id="allFuxuan" onclick="sel('fuxuan')"></td>
            </tr>
        </table>
    </div>
</div>

<script>
    function addTr2(tab, row) {
        var kuang1 = document.getElementById("logisticsNo")
        var kuang2 = document.getElementById("reason")
        var trHtml = "<tr align='center'><td>" +kuang1.value+ "</td><td>" +kuang2.value+ "</td><td><input type=\"checkbox\" id=\"fuxuan\" name=\"fuxuan\"  value=\""+kuang1.value+"\"></td></tr>";
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
        $("#logisticsNo").val("").focus();
        $("#reason").val("0");
        $("#memo").val("");
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
            alert("要删除指定行，需选中要删除的行！");
            return;
        }
        fuxuans.each(function(){
            $(this).parent().parent().remove();
        });
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
            url: "/mobile/PorblemController/test.html",
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
