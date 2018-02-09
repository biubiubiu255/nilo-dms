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
						var content ='';
						$("#query").click(function() {
							inputVal = $("input[name='logisticsNo']").val();
							ajaxRequest("/mobile/basic/route/query.html", {orderNo:inputVal}, true, function(res) {
								if (!res.result) {
									showError(res.msg);
								}else{
				
									content = '';
				                    for (var int = 0; int < res.data.length; int++) {
				                    	content += getResult(res.data[int]);
									}
				                    $(".banner_center").first().html(content);
								}
							})
						});
						
						var scan_callback = function(code) {
							mobile.setFormFieldValue('logisticsNo', code);
						}
						$.scanner(scan_callback);
                     
					});
	
	function getResult(d) {
		//alert("test"+d.opt);
		//var dataStr = GetCurrentTime('YYYY-MM-DD hh:mm:ss', parseint(d.optTime));
		var dataStr = UnixToDate(d.optTime, true);

		
		var point = '';
	
    	//alert(d.opt);
    	//alert(dataStr);
		switch (d.opt) {
		
		case 'arrive_scan':
			point = '快件由' + d.optByName + '扫描，网点：' + d.networkDesc;
							break;

		case 'delivery':
			point = '正在派件，派件员【'+d.optByName+'，'+d.phone+'】';
			break;
			
		case 'receive':
			point = '快件已签收，签收人：' + d.optByName;
			break;
			
		default:
			break;
		}
		
		point = '<span>' + dataStr + '<br/></span><span>' + point + '</span><br/><hr/><br/>';
    	//alert(point);
		return point;
	}
	function UnixToDate(unixTime, isFull, timeZone) {  
	    if (typeof (timeZone) == 'number'){  
	        unixTime = parseInt(unixTime) + parseInt(timeZone) * 60 * 60;  
	    }  
	    var time = new Date(unixTime * 1000);  
	    var ymdhis = "";  
	    ymdhis += time.getUTCFullYear() + "-";  
	    ymdhis += (time.getUTCMonth()+1) + "-";  
	    ymdhis += time.getUTCDate();  
	    if (isFull === true){  
	        ymdhis += " " + time.getUTCHours() + ":";  
	        ymdhis += time.getUTCMinutes() + ":";  
	        ymdhis += time.getUTCSeconds();  
	    }  
	    return ymdhis;  
	}  
	  
	
	

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
					                </div>
					            </a>
					        </li>
					    </ul>
			</form>

		</div>

	</div>
	
</body>
</html>
