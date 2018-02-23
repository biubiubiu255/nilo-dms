<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

<link rel="stylesheet" href="/layui/css/layui.css" media="all">
<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css" />
<link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />




<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>

<script type="text/javascript">

    $(document).ready(function(){
    	
    	var isScanBigPack = false;
    	
        //loadLanguage('cn');

        var mobile = new MobileData({
            autoLoad:false
            ,formId:'unpackage-form'
            ,model : 'customers'
        });
        
        mobile.initSubmitForm({
           formId:'unpackage-form'
            ,mbObject:mobile
           ,postUrl:'/mobile/network/unpackage/save.html'
            ,callback:function (data) {

                if (data.result==true) {
                	showInfo("commit sussess");
				} else {
	                showError('commit error');
				}
            }
        });

        android.startScan();
        
        var scan_callback = function (code) {
        	
        	if(isEmpty(code)){
        		showError("scan error");
        	}
        	
        	//是否已扫描大包
            if (isScanBigPack===false && isEmpty(mobile.getFormFieldValue("logisticsNo"))) {
				mobile.setFormFieldValue("logisticsNo", code);
				getOrderList(code);
				isScanBigPack=true;
				updatePackage();
				//$(".scanner").first().hide();
				return;
			}
            
            updatePackage(code) ? showError("Scan success") : showError("unfound");
        }
        
        $.scanner(scan_callback);
        

        $("#test").click(function() {
			scan_callback('Kili201802000035');
		});
        $("#test2").click(function() {
			scan_callback('NILO18020596390');
		});
        
    });
    
    /*
     * Funcion updatePackage()
     * Params  String code     扫描后的小包编号
     * Return  Boolean         true找到并更新成功，false为未找到此小包裹
     */
     
     
    function updatePackage(code) {
    	
    	//alert(code);
    	
    	var isFound = false;
    	
    	var text = "";
    	
    	var iconSussess = '<i class="layui-icon" style="font-size: 30px; color: #1E9FFF;">&#xe618;</i>';
    	
		$("h3[iscomplete='false']").each(function(index){
			
			var elem = $(this);
			
			//alert(code + "  ----  " + elem.attr("value"))
	        if(code==elem.attr("value")){
	        	
	        	isFound = true;
	        	
	        	elem.attr("iscomplete", "true");
	        	
	        	elem.parent().parent().parent().find(".sign-icon").parent().html(iconSussess);
	        	
	        	if(text!=""){
	        		text += ',' + code;
	        	}else {
					text  = code; 
				}
	        	
	        	$("input[name='scanNos']").first().val(text);
	        	
	        }
	     
		});
		
		return isFound;
	}

    
    /*
     * Funcion getOrderList()  获取子包裹列表
     * Params  String orderNo  大包裹编号
     * Return  void               
     */
    function getOrderList(orderNo) {
    	
    	ajaxRequest('/mobile/network/unpackage/scanList.html', 'scanNo=&packageNo='+orderNo, false, function(result) {
    		//if (result.data.length=0) showWarning("No subparcels were found");
    		//alert(result.data.length);
    		//alert(result.data.length);
    		//alert(result.data[0].createdTime);
    		
    		if(result.data.length==0) {
    			showError("not found package");
    			setTimeout(function () {  window.history.go(-1); }, 2000);


    		}

    		var desc='';
    		var point="";
    		var tempRes=null;
    		var iconPoint=null;
    		
			for (var int = 0; int < result.data.length; int++) {
				tempRes = result.data[int];
				tempRes.arrived==false ? iconPoint='<i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop sign-icon">&#x1002;</i>' : iconPoint='<i class="layui-icon" style="font-size: 30px; color: #1E9FFF;">&#xe618;</i>';
				tempRes.nextNetworkDesc=='undefined' ? desc='' : desc=tempRes.nextNetworkDesc;
		        point += '<li><a href="javascript:void(0);" style="display:inline-block;width: 95%"><h3 iscomplete="' + tempRes.arrived + '" value="' + tempRes.orderNo + '">OrderNo：' + tempRes.orderNo + ' </h3><div class="banner_center"><span>dese:' + desc + '</span><p></p><span>OrderType:' + tempRes.orderType + '</span><span style=" float:right;">Weight: ' + tempRes.weight 
                point += '</span><p></p><span>ReferenceNo:0</span></div></a><div class="banner_bottom"><p align="right">' + iconPoint + '</p></div></li>';
			}
			
			$("#child-packages").append(point);
			
		});
	}
    
    
</script>


</head>
<body>

	<div class="wap_content">

		<div class="wap_top">
			<a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
			<h2 data-locale="unpack_title">unpackage</h2>
		</div>
		<div class="formula_modify">
			<form id="unpackage-form">
				<div class="banner_content">
					<input type="hidden" name="scanNos" value="" required="required" />
					<ul class="one_banner">
						<li><input type='text' placeholder="Logistics No" required="required" maxlength='100' property_name="all_logistics_no" set_attr="placeholder" class='input_value i18n-input'
							name='logisticsNo' /><!-- <span class="scanner">scan</span> -->
						</li>

					</ul>

					<ul id="child-packages">

					</ul>

				</div>

<!-- 				<div class="bottom_a_button">

  				a class="scan" style="margin-bottom: 100px" id="test2">模拟扫描小包</a> 
					<a class="scan" style="margin-bottom: 150px" id="test">扫描大包</a>   
					<span class="scanner"><a onclick="javascript:void(0);"style="margin-bottom: 50px" data-locale="all_scan">scan</a></span>

					<a onclick="javascript:void(0);" class="submit" data-locale="all_submit">submit</a>
					
				</div> -->
            <div class="bottom_a_button11"><a href="javascript:void(0);" class="submit"  data-locale="all_submit"></a></div>
            <div class="bottom_a_button22"><a href="javascript:void(0);"><span class="scanner">scan</span></a></div>
			</form>

		</div>

	</div>


</body>
</html>
