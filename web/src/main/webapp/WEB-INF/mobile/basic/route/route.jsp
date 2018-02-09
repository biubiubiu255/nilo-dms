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
<link href="/mobile/css/mp.css" type="text/css" rel="stylesheet" />
<link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript"
	src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>


<script type="text/javascript">
	$(document)
			.ready(
					function() {
						loadLanguage('en');

						var mobile = new MobileData({
							autoLoad : false,
							formId : 'arrive-form',
							model : 'customers'
						});
						var inputVal='';
						$("#query").click(function() {
							inputVal = $("input[name='logisticsNo']").val();
							ajaxRequest("/mobile/basic/route/query.html", {orderNo:inputVal}, true, function(res) {
								if (!res.result) {
									showError(res.msg);
								}
							})
						});
						
						var scan_callback = function(code) {
							mobile.setFormFieldValue('logisticsNo', code);
						}
						$.scanner(scan_callback);
                     
					});
</script>

</head>
<body>

<style>


ul li{
	overflow:hidden;
}

</style>
	
<div class="wap_content">

		<div class="wap_top">
			<a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
			<h2>unpackage</h2>
		</div>
		<div class="banner_content">
			<form id="unpackage-form">
				<div class="banner_content">
					<input type="hidden" name="scanNos" value=""/>
					<ul class="one_banner">
	                    <li>
	                        <input type='text' placeholder="Logistics No" required="required" maxlength='100' class='input_value' name='logisticsNo' style="width: 62%;" />
	                    	<span class="scanner" style="left: 66%;">scan</span>
	                    	<span id="query">query</span>
	                    </li>
					</ul>
					
					<ul>
					        <li>
					            <a href="javascript:void(0);" style="display:inline-block;width: 95%" class="bigpackage_item" id="bigpackage_item-button-10255">
					                <div class="banner_center">
					                    <span>2017/09/02 15:26:13</span>
					                    <span style=" float:right;">快件由wms扫描</span>
					                    <p></p><hr/>

					                    <span>2017/09/03 02:26:13</span>
					                    <span style=" float:right;">快件已到达站点，等待扫描</span>
					                    <p></p><hr/>
					                    
					                    <span>2017/09/03 02:26:13</span>
					                    <span style=" float:right;">正在派件，派件员【joke，13256226226】，该件为冷藏件，记录温度为【℃】，扫描人【admin】</span>
					                    <p></p><hr/>
					                </div>
					            </a>
					        </li>
					    </ul>
			</form>

		</div>

	</div>
	
</body>
</html>
