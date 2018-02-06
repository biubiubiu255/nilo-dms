<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ page import="com.nilo.dms.common.Constant" %>

<%
    request.setAttribute("abnormalTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), Constant.ABNORMAL_ORDER_TYPE));
%>




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
        <h2>Problem</h2>
    </div>

    <div class="formula_modify">
        <form id="myForm" class="layui-form" action="">
            <div class="banner_content">
                <ul class="one_banner">
                    <li><input type='text' placeholder="Logistics No" maxlength='100' class='input_value' name='logisticsNo' /><span>scan</span></li>
                    <li>
                        <%--<label>Reason</label>--%>
                        <select required="required" class='input_value' name='reason'>  
	                       <c:forEach var="values" items="${abnormalTypeList}" varStatus="status">
	                           <option value="${values.code}">${values.value }</option>
		                   </c:forEach>
                        </select>
                    </li>
                    <li><input type='text' placeholder="Memo" maxlength='100' class='input_value' name='memo' /></li>
                </ul>
				<div class="bottom_a_button">
					<a onclick="save()">submit</a>
				</div>
            </div>
        </form>
    </div>

</div>

<script>

    function save() {

       var param = $("#myForm").serialize();
       ajaxRequest("/mobile/rider/problem/save.html", param, true, function(result){
    	   
       });
        
    }
    
</script>

</body>
</html>
