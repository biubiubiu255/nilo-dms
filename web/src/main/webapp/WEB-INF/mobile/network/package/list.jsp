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
<title>Pack List</title>


<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
<link href="/mobile/css/mp.css" type="text/css" rel="stylesheet"/>
<link href="/mobile/css/mps.css" type="text/css" rel="stylesheet"/>
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>

<script type="text/template" id="customers_tmplate">
	<li id="customers_li_{id}" is_audit="{is_audit}">
		<a href="javascript:void(0);">
			<div class="banner_center"><h2>{orderNo}</h2>
				<p>{orderType}  <span style=" float:right;">{nextNetworkDesc}</span></p>
			</div>
		</a>
		<div class="banner_bottom">
			 <p><a href="javascript:void(0);" id="customers_edit_{id}" class="ckh_edit">Detail</a></p>
		</div>
	</li>
</script>
<script type="text/javascript">
	$(function(){
        //loadLanguage('cn');
		var mobile = new MobileData({
			model : 'customers'
			//,viewModel:'view_customers'
			,templateId:'customers_tmplate'
			,appendId:'append_customers_id'
			,controller:'/mobile/package/pageList.html'
			,formId:'customers-form'
			,searchId:'customers-search'
			,findUrl:'/mobile/package/pageList.html'
			,searchParam:'packageNo'
		});
	});
</script>
</head>
<body>

<div class="wap_content"> 
   <div class="wap_top"><a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
        <h2 data-locale="pack_list_title">Pack List</h2>
   </div>
   <div class="search_banner">
        <div class="search_content" id="customers-search">
            <div class="search_input">
                <i></i>
                <input type="text" placeholder="enter number" searchParam="{keywords}"
                       property_name="all_logistics_no" set_attr="placeholder"
					class="search_input_field keywords i18n-input"/>
            </div>
            <div class="search_button"><input type="button" property_name="all_search" set_attr="value" value="search" class="search_input_button submit i18n-input"/></div>
        </div>
   </div>
   <div class="banner_content">
        <ul id = 'append_customers_id'>

        </ul>
        <div class="append_more"></div> 
   </div>
</div>

</body>
</html>
