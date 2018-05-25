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
						var mobile = new MobileData({
							autoLoad : false,
							formId : 'arrive-form',
							model : 'customers'
						});
						var inputVal='';
						var content ='';
						$("#query").click(function() {
							inputVal = $("input[name='logisticsNo']").val();
							ajaxRequest("/mobile/basic/route/query.html", {orderNo:inputVal}, true, function(res) {
								if (!res.result) {
									showError(res.msg);
								}else{
				
									content = '';
				                    for (var int = 0; int < res.data.length; int++) {
				                    	content += getResult(res.data[int]) + ' ';
									}
				                    if(content=='') content='<span>' + getI18nAttr('title_not_found_order') + '</span>';
				                    $(".banner_center").first().html(content);
								}
							})
						});
						
						var scan_callback = function(code) {
							$("input[name='logisticsNo']").val(code);
							$("#query").trigger("click");
						}
						$.scanner(scan_callback);
						
						//android.startScan();
					});

    var map = {
        route_const_parcel: getI18nAttr('route_const_parcel'),
        all_scan: getI18nAttr('all_scan'),
        home_network: getI18nAttr('home_network'),
        route_const_delivering: getI18nAttr('route_const_delivering'),
        home_rider: getI18nAttr('home_rider'),
        route_const_signed: getI18nAttr('route_const_signed'),
        sign_scan_signer: getI18nAttr('sign_scan_signer'),
        arrive_scan: getI18nAttr('arrive_scan'),
        send_scan: getI18nAttr('send_scan')
    };


	function getResult(d) {

		var dataStr = GetCurrentTime('YYYY-MM-DD hh:mm:ss', d.optTime);
		
		var point = '';
	
    	//alert(d.opt);
    	//alert(dataStr);

    	
		switch (d.opt) {

		case 'arrive_scan':
			point = map.arrive_scan + '：' + d.optByName + '<br/>' + map.home_network + '：' + d.networkDesc;
			//alert(point);
			break;

            case 'send':
                point = map.send_scan + '，' + map.home_network + '：' + d.optByName;
                break;

		case 'delivery':
			point = map.route_const_delivering  + '，' + map.home_rider + '【' + d.optByName + '，' + d.phone+'】';
			break;

		case 'receive':
			point = map.route_const_signed + '，' + map.sign_scan_signer + '：' + d.optByName;
			break;

		default:
			return '';
			break;
		}
		
		point = '<span>' + dataStr + '<br/></span><span>' + point + '</span><br/><hr/><br/>';
    	//alert(point);
		return point;
	}

	


</script>

</head>
<body>

<style>


ul li{
	overflow:hidden;
}

</style>
	
<div class="formula_modify">

		<div class="wap_top">
			<a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
			<h2 data-locale="route_title">Track</h2>
		</div>
		<div class="banner_content">
			<form id="unpackage-form">
				<div class="banner_content">
					<input type="hidden" name="scanNos" value=""/>
					<ul class="one_banner">
	                    <li>
	                        <input type='text' placeholder="Logistics No" required="required" maxlength='100' property_name="all_logistics_no" set_attr="placeholder" class='input_value i18n-input' name='logisticsNo' style="width: 62%;" />
	                    	<span class="scanner" style="left: 66%;" data-locale="all_scan">scan</span>
	                    	<span id="query" data-locale="route_title">Query</span>
	                    </li>
					</ul>
					
					<ul>
					        <li>
					            <a href="javascript:void(0);" style="display:inline-block;width: 95%" class="bigpackage_item" id="bigpackage_item-button-10255">
					                <div class="banner_center">
					                </div>
					            </a>
					        </li>
					    </ul>
				</div>
			</form>

		</div>

	</div>
	
</body>
</html>
