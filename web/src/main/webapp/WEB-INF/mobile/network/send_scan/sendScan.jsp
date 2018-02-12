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

        // var station = mobile.getFormField('nextStation');
        var carrier = mobile.getFormField('carrier');
        var deliverDriver = mobile.getFormField('sendDriver');

        if(carrier.length > 0){
            carrier.bind('change',function(){
                var carrier_value = $(this).val();
                ajaxRequest('/mobile/send/getDriver.html',{code: carrier_value},false,function(data){
                    if(deliverDriver.length > 0)
                        deliverDriver.empty();
                    deliverDriver.prepend("<option value=''>Please select a driver</option>");
                    var driver = data.data;
                    for (var i = 0; i < driver.length; i++) {
                        deliverDriver.append("<option value='" + driver[i].code + "'>" + driver[i].name + "</option>");
                    }
                });
            });
        }

        var code_array = [];
        mobile.initSubmitForm({
            formId:'delivery-form'
            ,mbObject:mobile
            ,postUrl:'/mobile/send/submit.html'
            ,beforeSubmit:function () {
                var scaned_array = [];
                var checkboxs = $('#delivery-form').find('input:checked');
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
                    showInfo('Success');
                    del();
                } else {
                    showError(data.msg);
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
            ajaxRequest('/mobile/send/check.html',{code: code},false,function(data){
                if(data.result){
                    $('#append_order_items_id').prepend(append_html);
                }
                else{
                    showError(data.msg);
                }
            });
        }
        $.scanner(scan_callback);
        // $.scanner(scan_callback('11111111213123'),1,true);
        // $.scanner(scan_callback('333'),1,true);



        $('a.delete_button').click(function () {
            del();
        });
        function del(){
            var checkboxs = $('#delivery-form').find('input:checked');
            for(var i=0;i<checkboxs.length;i++){
                var value = $(checkboxs[i]).attr('value');
                delete code_array[value];
                $('#code'+value).remove();
            }
        }

    });

</script>
</head>
<body>
<div class="wap_content">
    <%--logisticsNo nextStation sendDriver plateNo--%>
    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2 data-locale="send_scan_title">Send Scan</h2>
    </div>

    <div class="formula_modify">
        <%--<div class="banner_content">--%>
        <form id="delivery-form">
            <div class="banner_content">
                <input type="hidden" name="scaned_codes" />
                <ul class="one_banner">
                    <li>
                        <%--<label>station</label>--%>
                        <select required="required" class='input_value' name='nextStation'>
                            <option value="" data-locale="send_scan_nextStation">Please select the nextStation</option>
                            <c:forEach items="${nextStation}" var="station">
                                <option value="${station.code}" type="${station.type}">${station.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                    <li>
                        <select required="required" class='input_value' name="carrier">
                            <option value="" data-locale="send_scan_carrier">Please select the carrier</option>
                            <c:forEach items="${thirdCarrier}" var="carrier">
                                <option value="${carrier.expressCode}" >${carrier.expressName}</option>
                            </c:forEach>
                        </select>
                    </li>
                    <li>
                        <select required="required" class='input_value' name="sendDriver">
                            <option value="" data-locale="send_scan_driver">Please select a driver</option>
                        </select>
                    </li>
                    <li><input type='text' placeholder="Plate No" maxlength='100' property_name="send_scan_plateNo" set_attr="placeholder" class='input_value i18n-input' id='plateNo' name='plateNo' /></li>
                    <li><input type='text' placeholder="Logistics No" required="required" maxlength='100' property_name="all_logistics_no" set_attr="placeholder" class='input_value i18n-input' id="logisticsNo" name='logisticsNo' /><span data-locale="all_scan" class="scanner" id="scan">scan</span></li>

                </ul>
                <div class="clear"></div>
                <ul id = 'append_order_items_id'>
                    <%--<li id="code123"><input type='checkbox' class='input_value' value="123" name='items' /><span>code 12123123</span></li>--%>
                    <%--<li id="code456"><input type='checkbox' class='input_value' value="456" name='items' /><span>code 12123123</span></li>--%>
                </ul>
            </div>
            <div class="bottom_a_button11"><a href="javascript:void(0);" class="delete_button" data-locale="all_delete">delete</a></div>
            <div class="bottom_a_button22"><a href="javascript:void(0);" class="submit" data-locale="all_submit">submit</a></div>
        </form>

    </div>
</div>
</body>
</html>
