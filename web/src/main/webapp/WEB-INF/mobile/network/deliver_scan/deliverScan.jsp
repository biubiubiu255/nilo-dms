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
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>
<script type="text/javascript">

    $(document).ready(function(){
        loadLanguage('cn');

        var mobile = new MobileData({
            autoLoad:false
            ,formId:'delivery-form'
            ,model : 'customers'
        });

        var station = mobile.getFormField('station');
        var deliverDriver = mobile.getFormField('deliverDriver');

        if(station.length > 0){
            station.bind('change',function(){
                var station_value = $(this).val();
                ajaxRequest('/mobile/deliver/getDriver.html',{code: station_value},false,function(data){
                    if(deliverDriver.length > 0)
                        deliverDriver.empty();
                        deliverDriver.prepend("<option value='0'>Please select a driver</option>");
                        var driver = data.data;
                        for (var i = 0; i < driver.length; i++) {
                            deliverDriver.append("<option value='" + driver[i].code + "'>" + driver[i].name + "</option>");
                        }
                });
            });
        }

        mobile.initSubmitForm({
           formId:'delivery-form'
            ,mbObject:mobile
           ,postUrl:'/mobile/deliver/test.html'
            ,callback:function (data) {
                showError('dddd');
            }
        });

        var code_array = [];
        var scan_callback = function (code) {
            mobile.setFormFieldValue('logisticsNo',code);
            if(!isEmpty(code_array[code])){
                warningTipMsg('This order already scanned');
                return;
            }
            code_array[code] = code;
            var append_html = "<li id='code"+code+"'><input type='checkbox' checked='checked' class='input_value' value=\""+code+"\" name='items[]' /><span>"+code+"</span></li>";
            $('#append_order_items_id').prepend(append_html);
        }
        $.scanner(scan_callback);



        $('a.delete_button').click(function(){
            var checkboxs = $('#delivery-form').find('input:checked');
            for(var i=0;i<checkboxs.length;i++){
                var value = $(checkboxs[i]).attr('value');
                delete code_array[value];
                $('#code'+value).remove();
            }
        });



    });

</script>


</head>
<body>

<div class="wap_content">

    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2 data-locale="delivery_scan_title">Deliver Scan</h2>
    </div>
    <div class="formula_modify">
    <%--<div class="banner_content">--%>
        <form id="delivery-form">
            <div class="banner_content">
                <input type="hidden" name="id" />
                <ul class="one_banner">
                    <li>
                        <%--<label>station</label>--%>
                        <select required="required" class='input_value' name='station'>
                            <option value="">Please select the site</option>
                            <c:forEach items="${station}" var="station">
                                <option value="${station.code}" type="${station.type}">${station.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                    <li>
                        <select required="required" class='input_value' name="deliverDriver">
                            <option value="">Please select a driver</option>
                        </select>
                    </li>
                    <li><input type='text' placeholder="Plate No" maxlength='100' class='input_value' id='plateNo' name='plateNo' /></li>
                    <li><input type='text' placeholder="Logistics No" required="required" maxlength='100' class='input_value' id="logisticsNo" name='logisticsNo' /><span class="scanner" id="scan">scan</span></li>

                </ul>
                <div class="clear"></div>
                <ul id = 'append_order_items_id'>
                    <%--<li id="code123"><input type='checkbox' class='input_value' value="123" name='items' /><span>code 12123123</span></li>--%>
                    <%--<li id="code456"><input type='checkbox' class='input_value' value="456" name='items' /><span>code 12123123</span></li>--%>
                </ul>
            </div>
            <div class="bottom_a_button11"><a href="javascript:void(0);" class="delete_button">delete</a></div>
            <div class="bottom_a_button22"><a href="javascript:void(0);" class="submit">submit</a></div>
        </form>

    </div>

</div>


</body>
</html>
