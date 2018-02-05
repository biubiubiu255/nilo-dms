<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<%@ include file="../../header.jsp" %>
<body>

<div class="wap_content">

    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2>Packing</h2>
    </div>

    <div class="formula_modify">
        <form id="myForm" class="layui-form" action="">
            <div class="banner_content">
                <ul class="one_banner">
                    <li>
                        <%--<label>Next Station</label>--%>
                        <select required="required" class='input_value' id='nextStation' name='nextStation'>
                            <option value="0">Please select the nextStation</option>
                            <c:forEach items="${nextStation}" var="station">
		                        <option value="${station.code}" type="${station.type}">${station.name}</option>
		                    </c:forEach>
                        </select>
                    </li>
                    <li><input type='text' placeholder="Weight" id="Weight" class='input_value' name='Weight' /></li>
                    <li><input type='text' placeholder="Length" id="Length" class='input_value' name='Length' /></li>
                    <li><input type='text' placeholder="Width" id="Width" class='input_value' name='Width' /></li>
                    <li><input type='text' placeholder="High" id="High" class='input_value' name='High' /></li>
                    <li>
                        <label>Scan Logistics No</label>
                        <div class="xq">scan</div>
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
                 <td><input type="checkbox" id="allFuxuan" onclick="sel('fuxuan')"></td>
            </tr>
        </table>
    </div>
</div>


<script type="text/javascript">

document.getElementById('scan').onclick = function(){android.startScan()};
	
	function doScan(){
		android.startScan(afterScan);
	}
	function afterScan(scanResult){
		document.getElementById("logisticsNo").value = scanResult;
	}
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
        var kuang1 = document.getElementById("logisticsNo")
        var kuang2 = document.getElementById("signer")
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
        // $("#logisticsNo").val("").s();
        // $("#signer").val("");
        // $("#remarkx").val("");
    }
</script>
</body>
</html>
