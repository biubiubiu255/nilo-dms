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

<%@ include file="../../header.jsp" %>


<script type="text/javascript">
     
    $(function(){
    	
    	var mobile = new MobileData({
            autoLoad:false
            ,formId:'problem-form'
        });
    	
    	mobile.initSubmitForm({
    		formId: 'problem-form' ,
    		mbObject: mobile , 
    		postUrl : '/mobile/rider/problem/save.html' ,
    		callback: function (data) {
    			if (data.result) showInfo(null);
			}
    	});  
    	
    	//scan
        var scan_callback = function (code) {
    		// code 订单号
            mobile.setFormFieldValue('logisticsNo',code);
        }
    	
        $.scanner(scan_callback);                   //直接传一个空的回调函数
        $.scanner(scan_callback('这里是订单号'), 1); //测试模式，直接传订单号 

    	
    });
</script>


</head>
<body>


<div class="wap_content">
    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2>Problem</h2>
    </div>

    <div class="banner_content">
        <form id="problem-form">
            <div class="banner_content">
                <input type="hidden" name="id" />
                <ul class="one_banner">
                    <li><input type='text' placeholder="Logistics No" required="required" maxlength='100' class='input_value' name='logisticsNo' /><span>scan</span></li>
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
                <div class="clear"></div>
            </div>
            <div class="bottom_a_button">
				<a onclick="javascript:void(0);" class="submit">submit</a>
			</div>
        </form>

    </div>


 

<%-- 
    <div class="formula_modify">
        <form id="myForm" class="layui-form" action="">
            <div class="banner_content">
                <ul class="one_banner">
                    <li><input type='text' placeholder="Logistics No" maxlength='100' class='input_value' name='logisticsNo' /><span class="scanner">scan</span></li>
                    <li>
                        <label>Reason</label>
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
    </div> --%>

</div>



</body>
</html>
