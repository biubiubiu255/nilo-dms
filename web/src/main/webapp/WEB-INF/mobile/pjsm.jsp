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


<link href="../mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
<link href="../mobile/css/mp.css" type="text/css" rel="stylesheet" />
<link href="../mobile/css/mps.css" type="text/css" rel="stylesheet" />
<script src="../mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../mobile/js/functions.js"></script>
<script type="text/javascript" src="../mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="../mobile/js/mobile.js"></script>
<script type="text/javascript">
	$(function(){
		var mobile = new MobileData({
			model : 'customers'
			//,viewModel:'view_customers'
			,templateId:'customers_tmplate'
			,appendId:'append_customers_id'
			,controller:'../controller/customer.php'
			,formId:'customers-form'
			,autoLoad:false
			,searchId:'customers-search'
		});
	});
</script>
</head>
<body>

<div class="wap_content"> 
   <div class="wap_top"><a href="/DemoController/toIndexPage.html" title="返回" class="wap_top_back"></a>
   <h2>Pie scan</h2>
   <a href="javascript:void(0)" class="top_button_add" id="customers-add-button"></a>   </div>
   <div class="search_banner">
        <div class="search_content" id="customers-search">
            <div class="search_input">
                <i></i>
                <input type="text" class="search_input_field keywords"/>
            </div>
            <div class="search_button"><input type="button" value="scanning" class="search_input_button submit"/></div>
        </div>
   </div>
   <div class="banner_content">
        <ul id = 'append_customers_id'>
		
		<li>
          <!--<em></em> -->
          <a href="javascript:void(0);">
          <div class="banner_center">
          	<i>Driver</i>
          	<h2>Waybill number</h2>
          	
             <i>cfbhfgh</i>
            <h2>46284589115715841288</h2>
           
          </div>
          </a>
          <div class="banner_bottom">
          <a href="javascript:void(0);" class="ckh_delete">delete</a> 
          
          </div>
         </li>
		
		</ul>
        <div class="append_more"></div> 
   </div>
   
    <div class="banner_content">
        <ul id = 'append_customers_id'>
		
		<li>
          <!--<em></em> -->
          <a href="javascript:void(0);">
          <div class="banner_center">
          	 <i>Driver</i>
          	<h2>Waybill number</h2>
           
             <i>cfbhfgh</i>
            <h2>46284589115715841288</h2>
           
          </div>
          </a>
          <div class="banner_bottom">
          <a href="javascript:void(0);" class="ckh_delete">delete</a> 
          
          </div>
         </li>
		
		</ul>
        <div class="append_more"></div> 
   </div>
   
   
</div>   

 
 
<form class="form_window" id="customers-form">
<div class="form_content">
		<div class="wap_top"><a href="javascript:void(0);"  onclick="$('#customers-form').hide();" class="wap_top_back"></a>
	   <h2>New confirmation</h2>
	   </div>
		<div class="banner_content">
		<input type="hidden" name="id" />
		<ul class="one_banner">
		
		<li><label>Waybill number</label><input type='text' maxlength='100' class='input_value' name='customers_address' /><span>scanning</span></li>
		<li>
            <label>site</label>
            <select required="required" class='input_value required' name='allow_exceed'>
				<option value="1">Please select the site</option>
				<option value="2">test2</option>
				<option value="3">nckjd</option>
				<option value="4">dvad</option>
				<option value="5">test5</option>
				<option value="6">test6</option>
			</select>
        </li>
		<li><label>Driver</label><input type='text' maxlength='100' class='input_value' name='customers_fax' /></li>
		<li><label>License plate number</label><input type='text' maxlength='100' class='input_value' name='customers_address' /></li>
		
		</ul> 
		<div class="clear"></div>
		</div>
		<div class="bottom_a_button"><a href="javascript:void(0);" class="submit">submit</a></div>
</div>
</form>  

</body>
</html>
