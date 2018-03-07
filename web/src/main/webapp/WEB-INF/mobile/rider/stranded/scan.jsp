<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ page import="com.nilo.dms.common.Constant" %>

<%
    request.setAttribute("refuse_reason", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), Constant.REFUSE_REASON));
%>


<!DOCTYPE HTML>

<%@ include file="../../header.jsp" %>
<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>

<body>

<div class="wap_content">

    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2 data-locale="Stranded_title">Stranded</h2>
    </div>

    <div class="banner_content formula_modify">
        <form id="stranded-form">
            <div class="banner_content">
                <input type="hidden" name="id" />
               <ul class="one_banner">
                    <li><input type='text' placeholder="Logistics No" required="required" property_name="all_logistics_no" set_attr="placeholder" maxlength='100' class='input_value i18n-input' name='orderNo' /><span class="scanner" data-locale="all_scan">scan</span></li>
                    <li>
                        <%--<label>Reason</label>--%>
                        <select required="required" class='input_value' name='reason'>
                            <option value="">Please enter a reason</option>
	                       <c:forEach var="values" items="${abnormalTypeList}" >
	                           <option value="${values.code}">${values.value }</option>
		                   </c:forEach>
                        </select>
                    </li>
                    <li><input type='text' placeholder="Remark" property_name="stranded_remark" set_attr="placeholder" maxlength='100' class='input_value i18n-input' name='remark' /></li>
                    <a ></a>
                </ul>
                <div class="clear"></div>
            </div>
            <div class="bottom_a_button">
				<a onclick="javascript:void(0);" class="submit" data-locale="all_submit">submit</a>
			</div>
        </form>

    </div>


</div>

<script type="text/javascript">
     
    $(function(){
        //loadLanguage('cn');
    	var mobile = new MobileData({
            autoLoad:false
            ,formId:'stranded-form'
        });
    	
    	mobile.initSubmitForm({
    		formId: 'stranded-form' ,
    		mbObject: mobile ,
            showMsg : false,
    		postUrl : '/mobile/rider/stranded/save.html' ,
    		callback: function (data) {
                if (data.result) {
                    showInfo('submit success');
                    mobile.paginate();
                } else {
                    showError(data.msg);;
                }
			}
    	});


    	
    	//scan
        var scan_callback = function (code) {
    		// code 订单号
            mobile.setFormFieldValue('orderNo',code);
        }
    	
    	
        $.scanner(scan_callback);                     //直接传一个空的回调函数
        //$.scanner(scan_callback('这里是订单号'), 1); //测试模式，直接传订单号 
        
    });
</script>

</body>
</html>
