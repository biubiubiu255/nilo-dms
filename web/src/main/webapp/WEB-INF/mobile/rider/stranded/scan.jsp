<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ page import="com.nilo.dms.common.Constant" %>

<%
    request.setAttribute("abnormalTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), Constant.ABNORMAL_ORDER_TYPE));
%>


<!DOCTYPE HTML>

<%@ include file="../../header.jsp" %>
<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css" />


<body>

<div class="wap_content">

    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2>Stranded</h2>
    </div>

    <div class="banner_content formula_modify">
        <form id="stranded-form">
            <div class="banner_content">
                <input type="hidden" name="id" />
               <ul class="one_banner">
                    <li><input type='text' placeholder="Logistics No" required="required" maxlength='100' class='input_value' name='orderNo' /><span class="scanner">scan</span></li>
                    <li>
                        <%--<label>Reason</label>--%>
                        <select required="required" class='input_value' name='reason'>  
	                       <c:forEach var="values" items="${abnormalTypeList}" varStatus="status">
	                           <option value="${values.code}">${values.value }</option>
		                   </c:forEach>
                        </select>
                    </li>
                    <li><input type='text' placeholder="Memo" maxlength='100' class='input_value' name='remark' /></li>
                    <a ></a>
                </ul>
                <div class="clear"></div>
            </div>
            <div class="bottom_a_button">
				<a onclick="javascript:void(0);" class="submit">submit</a>
			</div>
        </form>

    </div>


</div>

<script type="text/javascript">
     
    $(function(){
    	
    	var mobile = new MobileData({
            autoLoad:false
            ,formId:'stranded-form'
        });
    	
    	mobile.initSubmitForm({
    		formId: 'stranded-form' ,
    		mbObject: mobile , 
    		postUrl : '/mobile/rider/stranded/save.html' ,
    		callback: function (data) {
    			if (data.result) showInfo(null);
			}
    	});
    	
    	//scan
        var scan_callback = function (code) {
    		// code 订单号
            mobile.setFormFieldValue('orderNo',code);
        }
    	
    	
        $.scanner(scan_callback, 1);                     //直接传一个空的回调函数
        //$.scanner(scan_callback('这里是订单号'), 1); //测试模式，直接传订单号 
        
    });
</script>

</body>
</html>
