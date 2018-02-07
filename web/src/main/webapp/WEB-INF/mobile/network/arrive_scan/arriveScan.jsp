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
<link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>
<script type="text/javascript">

    $(document).ready(function(){
        loadLanguage('en');

        var mobile = new MobileData({
            autoLoad:false
            ,formId:'arrive-form'
            ,model : 'customers'
        });
        var code_array = [];
        mobile.initSubmitForm({
            formId:'arrive-form'
            ,mbObject:mobile
            ,postUrl:'/mobile/arrive/submit.html'
            ,beforeSubmit:function () {
                var scaned_array = [];
                var checkboxs = $('#arrive-form').find('input:checked');
                for(var i=0;i<checkboxs.length;i++){
                    var value = $(checkboxs[i]).attr('value');
                    scaned_array.push(value);
                }
                console.dir(scaned_array);
                mobile.setFormFieldValue('logisticsNos',scaned_array.join(','));
                return true;
            }
            ,callback:function (data) {
                showError('Successful submission');
                del();
            }
        });

        var scan_callback = function (code) {
            if(!isEmpty(code_array[code])){
                warningTipMsg('This order already scanned');
                return;
            }
            code_array[code] = code;
            var append_html = "<li id='code"+code+"'><input type='checkbox' checked='checked' class='input_value' value='"+code+"' name='items' /><span>"+code+"</span></li>";
            $('#append_arrive_scan').prepend(append_html);
        }
        $.scanner(scan_callback);

        $('a.delete_button').click(function () {
            del();
        });
        function del(){
            var checkboxs = $('#arrive-form').find('input:checked');
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
    <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2>Arrive Scan</h2>
    </div>
    <div class="formula_modify">
        <form id="arrive-form">
            <div class="search_content" id="customers-search">
                <span class="scanner arrive_scan" id="scan">scan</span>
            </div>
            <div class="banner_content">
                <%--<input type="hidden" name="scaned_codes" />--%>
                <input type="hidden" name="logisticsNos" />
                <ul class="one_banner">
                    <%--<span class="scanner arrive_scan" id="scan">scan</span>--%>

                </ul>
                <div class="clear"></div>
                <ul id = 'append_arrive_scan'>

                </ul>
            </div>
            <div class="bottom_a_button11"><a href="javascript:void(0);" class="delete_button">delete</a></div>
            <div class="bottom_a_button22"><a href="javascript:void(0);" class="submit">submit</a></div>
        </form>
    </div>
</div>
</body>
</html>
