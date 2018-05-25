<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta content="telephone=no" name="format-detection"/>
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0"/>
<meta name="viewport"
      content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
<meta name="keywords" content="#"/>
<meta name="description" content="#"/>
<title></title>


<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
<link href="/mobile/css/mp.css" type="text/css" rel="stylesheet"/>
<link href="/mobile/css/mps.css" type="text/css" rel="stylesheet"/>
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        //loadLanguage('cn');
        var mobile = new MobileData({
            autoLoad:false
            ,formId:'packing-form'
            ,model : 'customers'
        });
        var code_array = [];
        mobile.initSubmitForm({
            formId:'packing-form'
            ,mbObject:mobile
            ,showMsg : false
            ,postUrl:'/mobile/package/submit.html'
            ,beforeSubmit:function () {
                var scaned_array = [];
                var checkboxs = $('#packing-form').find('input:checked');
                for(var i=0;i<checkboxs.length;i++){
                    var value = $(checkboxs[i]).attr('value');
                    scaned_array.push(value);
                }
                console.dir(scaned_array);
                mobile.setFormFieldValue('scaned_codes',scaned_array.join(','));
                return true;
            }
            ,callback:function (data) {
                if (data.result) {
                    showError('Success');
                    del();
                    mobile.paginate();
                } else {
                    showError(data.msg)
                }
            }
        });

        var scan_callback = function (code) {
            mobile.setFormFieldValue('logisticsNo',code);
            if(!isEmpty(code_array[code])){
                warningTipMsg('This order already scanned');
                return;
            }
            code_array[code] = code;
            var append_html = "<li id='code"+code+"'><input type='checkbox' checked='checked' class='fuxuank' value='"+code+"' name='items' /><span class='suiyi'>"+code+"</span></li>";
            ajaxRequest('/mobile/package/check.html',{code: code},false,function(data){
                if(data.result){
                    $('#append_packing_id').prepend(append_html);
                }
                else{
                    showError(data.msg);
                }
            });


        }
        $.scanner(scan_callback);


        function del(){
            var checkboxs = $('#packing-form').find('input:checked');
            for(var i=0;i<checkboxs.length;i++){
                var value = $(checkboxs[i]).attr('value');
                delete code_array[value];
                $('#code'+value).remove();
            }
        }

    });
</script>
</head>
<%--<%@ include file="../../header.jsp" %>--%>
<body>

<div class="wap_content">

    <div class="wap_top">
        <a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2 data-locale="packing_title">Packing</h2>
    </div>

    <div class="formula_modify">
        <form id="packing-form">
            <div class="banner_content">
                <input type="hidden" name="scaned_codes" />
                <ul class="one_banner">
                    <li>
                        <select required="required" class='input_value' id='nextStation' name='nextStation'>
                            <option value="" data-locale="packing_nextStation">Please select the nextStation</option>
                            <c:forEach items="${nextStation}" var="station">
                                <option value="${station.code}" type="${station.type}">${station.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                    <li><input type='text' required="required" placeholder="Weight" id="weight" property_name="packing_weight" set_attr="placeholder" class='input_value i18n-input' name='weight'/></li>
                    <li><input type='text' required="required" placeholder="Length" id="length" property_name="packing_length" set_attr="placeholder" class='input_value i18n-input' name='length'/></li>
                    <li><input type='text' required="required" placeholder="Width" id="width" property_name="packing_width" set_attr="placeholder" class='input_value i18n-input' name='width'/></li>
                    <li><input type='text' required="required" placeholder="High" id="height" property_name="packing_high" set_attr="placeholder" class='input_value i18n-input' name='height'/></li>
                    <%--<li><label>Scan Logistics No</label><div class="xq">scan</div></li>--%>
                    <li><input type='text' placeholder="Logistics No" required="required" maxlength='100' property_name="all_logistics_no" set_attr="placeholder" class='input_value i18n-input' id="logisticsNo" name='logisticsNo'/><span class="scanner" id="scan" data-locale="all_scan">scan</span></li>
                </ul>
                <ul id = 'append_packing_id'>
                </ul>
            </div>
            <div class="bottom_a_button"><a href="javascript:void(0);" class="submit" data-locale="all_submit">submit</a></div>
        </form>
    </div>
</div>

</body>
</html>
