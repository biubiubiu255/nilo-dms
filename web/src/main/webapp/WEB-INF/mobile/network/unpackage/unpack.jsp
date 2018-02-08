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
<link href="/mobile/css/mp.css" type="text/css" rel="stylesheet" />




<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript"
	src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>

<script type="text/javascript">

    $(document).ready(function(){
    	
    	var isScanBigPack = false;
    	
        loadLanguage('cn');

        var mobile = new MobileData({
            autoLoad:false
            ,formId:'unpackage-form'
            ,model : 'customers'
        });
        
        mobile.initSubmitForm({
           formId:'unpackage-form'
            ,mbObject:mobile
           ,postUrl:'/mobile/deliver/test.html'
            ,callback:function (data) {
                showError('dddd');
            }
        });


        var scan_callback = function (code) {
        	
        	if(isEmpty(code)){
        		showError("scan error");
        	}
        	
        	//是否已扫描大包
            if (isScanBigPack===false && isEmpty(mobile.getFormFieldValue("logisticsNo"))) {
				mobile.setFormFieldValue("logisticsNo", code);
				getOrderList(code);
				isScanBigPack=true;
				return;
			}
            
            updatePackage(code) ? showError("Scan success") : showError("unfound");
        }
        
        //$.scanner(scan_callback);
        
        $("#test").click(function() {
			//test();
			scan_callback('Kili201802000015');
		});

    });
    
    /*
     * Funcion updatePackage()
     * Params  String code     扫描后的小包编号
     * Return  Boolean         true找到并更新成功，false为未找到此小包裹
     */
     
     
    function updatePackage(code) {
    	
    	var isFound = false;
    	
    	var iconSussess = '<i class="layui-icon" style="font-size: 30px; color: #1E9FFF;">&#xe618;</i>';
    	
		$("span[iscomplete='false']").each(function(index){
			
			var elem = $(this);
			
	        if(code==elem.attr("value")){
	        	
	        	isFound = true;
	        	
	        	elem.attr("iscomplete", "true");
	        	
	        	elem.parent().parent().parent().find(".sign-icon").parent().html(iconSussess);	
	  
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
    	
    	ajaxRequest('/mobile/network/unpackage/scanList.html', 'scanNo=227185144014966784&packageNo='+orderNo, false, function(result) {
    		//if (result.data.length=0) showWarning("No subparcels were found");
    		//alert(result.data.length);
    		//alert(result.data.length);
    		//alert(result.data[0].createdTime);
    		
    		var point="";
    		var tempRes=null;

			for (var int = 0; int < result.data.length; int++) {
				tempRes = result.data[int];
		        point += '<li><a href="javascript:void(0);" style="display:inline-block;width: 95%"><h3>Arrived：' + (int+1) + ' </h3><div class="banner_center"><span iscomplete="false" value="' + tempRes.orderNo + '">' + tempRes.orderNo + '</span><p></p><span>OrderType:' + tempRes.orderType + '</span><span style=" float:right;">Weight: ' + tempRes.weight 
                point += '</span><p></p><span>ReferenceNo:0</span></div></a><div class="banner_bottom"><p align="right"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop sign-icon">&#x1002;</i></p></div></li>';
			}
			
			$("#child-packages").append(point);
			
		});
	}
    
    function test() {
    	alert("sssghg");
	}
    
</script>


</head>
<body>

	<div class="wap_content">

		<div class="wap_top">
			<a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
			<h2 data-locale="delivery_scan_title">Deliver Scan</h2>
		</div>
		<div class="banner_content">
			<form id="unpackage-form">
				<div class="banner_content">
					<input type="hidden" name="id" />
					<ul class="one_banner">

						<li><input type='text' placeholder="Logistics No"
							required="required" maxlength='100' class='input_value'
							name='logisticsNo' /><span class="scanner">scan</span></li>


					</ul>

					<ul id="child-packages">

					</ul>

				</div>

				<div class="bottom_a_button">
					<a class="scan" style="margin-bottom: 100px" id="test">test
						scan</a> <a onclick="javascript:void(0);" class="scan"
						style="margin-bottom: 50px" class="input_value">scan</a> <a
						onclick="javascript:void(0);" class="submit">submit</a>
				</div>
			</form>

		</div>

	</div>


</body>
</html>
