<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta content="telephone=no" name="format-detection" />
<meta name="viewport"
	content="width=device-width, minimum-scale=1.0, maximum-scale=2.0" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
<meta name="keywords" content="#" />
<meta name="description" content="#" />
<title></title>


<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css" />
<link href="/mobile/css/mps.css?v=1" type="text/css" rel="stylesheet" />
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js?v=4"></script>
<script type="text/javascript"
	src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>


<script type="text/javascript">
	$(document).ready(
		function() {
			//loadLanguage('cn');

			var mobile = new MobileData({
				autoLoad : false,
				formId : 'arrive-form',
				model : 'customers'
			});

			mobile.initSubmitForm({
				formId : 'arrive-form',
				mbObject : mobile,
                showMsg : false,
				postUrl : '/mobile/arrive/submit.html',
				beforeSubmit : function() {
					var scaned_array = [];
					var checkboxs = $('#arrive-form').find(
							'input:checked');
					for (var i = 0; i < checkboxs.length; i++) {
						var value = $(checkboxs[i]).attr('value');
						scaned_array.push(value);
					}
					console.dir(scaned_array);
					mobile.setFormFieldValue('scanedCodes',
							scaned_array.join(','));
					return true;
				},
				callback : function(data) {
					if (data.result) {
						showInfo('submit success')
						del();
					} else {
						showError(data.msg);
						// window .history.go(-1);
					}

				}
			});

			var code_array = [];
			var scan_callback = function(code) {
				//mobile.setFormFieldValue('logisticsNo', code);
				if (!isEmpty(code_array[code])) {
					warningTipMsg('This logisticsNo already scanned');
					return;
				}
				code_array[code] = code;
				var append_html = "<li id='code"+code+"'><input type='checkbox' checked='checked' class=\'fuxuank\' value=\""+code+"\" name='items[]' /><span class='suiyi'>" + code + "</span></li>";
                ajaxRequest('/mobile/arrive/check.html',{code: code},false,function(data){
					if(data.result){
                        $('#append_order_items_id').prepend(append_html);
					}
					else{
                        showError(data.msg);
					}
				});

			}
			$.scanner(scan_callback);

			$('a.delete_button').click(function () {
				del();
			});
			

			function del() {
				var checkboxs = $('#arrive-form').find(
						'input:checked');
				for (var i = 0; i < checkboxs.length; i++) {
					var value = $(checkboxs[i]).attr('value');
					delete code_array[value];
					$('#code' + value).remove();
				}
			}
			
			//android.startScan();
			
		});
	/* function scan(){
		android.startScan();
	} */
</script>

</head>
<body>

	<div class="formula_modify">
		<div class="wap_top">
			<a href="/mobile/home.html" title="Back" class="wap_top_back"></a>
			<h2 data-locale="arrive_scan_title">Arrive Scan</h2>
		</div>

		<div class="banner_content">
			<form id="arrive-form">
				<div class="banner_content">
					 <input type="hidden" name="scanedCodes" />
					 <ul class="scanner_banner">
						<img src="/mobile/images/scannner.png" class="scanner"><input type='text'  placeholder="Logistics No" property_name="all_logistics_no" set_attr="placeholder" class='input_value i18n-input' id="logisticsNo" name='logisticsNo' /><span >ok</span>
					</ul>  
					<!-- <div class="scanner"><img src="/mobile/images/scannner.png" id="scan"><input type="text" placeholder="Input code or scan code"><span>OK</span></div> -->
				</div>
				</br>

				<ul id='append_order_items_id'>
					<%--<li id="code123"><input type='checkbox' class='fuxuank' value="123" name='items' /><span class="suiyi">code 12123123</span></li>--%>
					<%--<li id="code456"><input type='checkbox' class='input_value' value="456" name='items' /><span>code 12123123</span></li>--%>
				</ul>

				<div class="bottom_a_button11"><a href="javascript:void(0);" class="delete_button" data-locale="all_delete">delete</a></div>
				<div class="bottom_a_button22"><a href="javascript:void(0);" class="submit" data-locale="all_submit">submit</a></div>
			</form>
		</div>

	</div>
</body>
</html>
