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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>


<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css" />
<link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="/layui/css/multselect.css" media="all">
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript"
	src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>
<script src="/layui/layui.js" charset="utf-8"></script>
<script type="text/javascript">
	//loadLanguage('en');
	var payType = '0';
	var payForm = new MobileData({
		autoLoad : false,
		formId : 'pay-form'
	});

	payForm.initSubmitForm({
		formId : 'pay-form',
		mbObject : payForm,
		postUrl : 'http://sandbox.lipapay.com/api/excashier.html'
	});

	layui.use([ 'upload', 'jquery' ], function() {

		var $ = layui.jquery, upload = layui.upload;

		var isUpPic = false;
		upload.render({
			elem : '.xq',
			url : '/mobile/rider/COD/onlineProblemSave.html',
			auto : false, //选择文件后不自动上传
			data : {},
			bindAction : '#commit',
			choose : function(obj) {
				obj.preview(function(index, file, result) {
					//$('#demo2').append('<img name = "s_pmt_dw" style="width: 120px; height: 150px; margin-left: 16px;" src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img">')
					$("#lypic").first().show();
					$('#lypic').attr('src', result); //图片链接（base64）
				});
				isUpPic = true;
			},
			before : function(res) {
				if (payType == '1') {
					ajaxRequest('/mobile/rider/COD/redyToPay.html', {
						orderNo : $("#logisticsNo").val()
					}, false, function(res) {
						payForm.setFormFieldValue("version", "1.4");
						payForm.setFormFieldValue("sign", res.data.sign);
						payForm.setFormFieldValue("signType", res.data.signType);
						payForm.setFormFieldValue("merchantId", res.data.merchantId);
						payForm.setFormFieldValue("notifyUrl", res.data.notifyUrl);
						payForm.setFormFieldValue("returnUrl", res.data.returnUrl);
						payForm.setFormFieldValue("merchantOrderNo", res.data.merchantOrderNo);
						payForm.setFormFieldValue("amount", res.data.amount);
						payForm.setFormFieldValue("goods[0].goodsName", res.data['goods[0].goodsName']);
						payForm.setFormFieldValue("goods[0].goodsQuantity", res.data['goods[0].goodsQuantity']);
						payForm.setFormFieldValue("goods[0].goodsPrice", res.data['goods[0].goodsPrice']);
						payForm.setFormFieldValue("goods[0].goodsType", res.data['goods[0].goodsType']); 
						payForm.setFormFieldValue("expirationTime", res.data.expirationTime);
						payForm.setFormFieldValue("sourceType", res.data.sourceType);
						payForm.setFormFieldValue("currency", res.data.currency);
						
						document.getElementById("pay-form").submit();
					});
					return;
				} else if (payType == '2') {
					if (isUpPic === false) {
						showWarning("plase chose pic");
						return;
					}
				} else if (payType == '0') {
					var logisticsNo = $("#logisticsNo").val();
					ajaxRequest('/mobile/rider/COD/cashSave.html', {
						logisticsNo : logisticsNo,
						paidType : 0
					}, false, function(res) {
						if(res.result){
							// alert('success,plase to sign logistics');
							showInfo("success,will foward to sign");
							//alert(logisticsNo);
                            setTimeout(function(){
                                window.location.href="/mobile/rider/sign/toSign.html?logisticsNo="+logisticsNo;
                            }, 2000);
						}else{
							showWarning("Sorry,cash pay error!");
						}
						
					});
					return;
				}

				this.data = {
					logisticsNo : $("#logisticsNo").val(),
					paidType : payType
				};
				//layui.upload.config.data = {logisticsNo:1,signer:2};
			},
			done : function(res) {
				if (res.result) {
					//showWarning("success,plase to sign logistics");
					alert('success,plase to sign logistics');
					javascript: history.go(-1);
				} else {
					showError(res.msg);

				}
			}
		});

		var inputFile = $("input[type='file']");

		inputFile.addClass("layui-hide");

		inputFile.css("display", "none");

	});

	//android.startScan();

	$(document).ready(function() {
		var mobile = new MobileData({
			autoLoad : false,
			formId : 'myForm',
			model : 'customers'
		});
		var scan_callback = function(code) {
			mobile.setFormFieldValue("logisticsNo", code)
			ajaxRequest('/mobile/rider/COD/getDetail.html', {
				orderNo : code
			}, false, function(res) {
				mobile.setFormFieldValue("amont", res.data)
			});
		}
		$.scanner(scan_callback);

		$("#lypic").first().hide();

		$(":radio").click(function() {
			payType = $(this).val();
			if (payType == '2') {
				$('#picture').show();
				$('#Memo').show();

			} else {
				$('#picture').hide();
				$('#Memo').hide();
			}
		});
	});
</script>
</head>
<body>
	<div class="wap_content">

		<div class="wap_top">
			<a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
			<h2>Payment</h2>
		</div>

		<div class="formula_modify">
			<form id="myForm" class="layui-form" action="">
				<div class="banner_content">
					<ul class="one_banner">
						<li><input type="text" placeholder="Logistics No"
							property_name="all_logistics_no" set_attr="placeholder"
							id="logisticsNo" name="logisticsNo"
							class="input_value i18n-input" /><span class="scanner"
							data-locale="all_scan"></span></li>
						<li><input type='text' id="amont" placeholder="Amont"
							property_name="amont" set_attr="placeholder"
							class='input_value i18n-input' name='amont' required="required" />
						<li><label>Pay Type</label>
							<div style="position: absolute; left: 25%">
								<input type="radio" name="danxuan" value="0" />Cash
								&nbsp;&nbsp; <input type="radio" name="danxuan" value="1" />Online
								&nbsp;&nbsp; <input type="radio" name="danxuan" value="2" />Online
								Problem
							</div></li>
						<!-- <li><label>toPay</label><div style="position:absolute; left: 30%">
                        <button id="toPay">Online payment</button>
                        </div>
                    </li> -->
						<li id="Memo" hidden="true"><input type='text'
							placeholder="Memo" required="required"
							property_name="problem_memo" set_attr="placeholder"
							maxlength='100' class='input_value i18n-input' name='memo' /></li>
						<li hidden="ture" id="picture"><label> picture</label>
							<div class="xq">
								<img src="/mobile/images/2300.jpg" />
							</div></li>
					</ul>
					<center>
						<div>
							<img src="" style="width: 100px; height: 100px;" id="lypic" />
						</div>
					</center>
					<div class="bottom_a_button">
						<a id="commit" data-locale="all_submit">submit</a>
					</div>
				</div>
			</form>

			<form id="pay-form"
				action="http://sandbox.lipapay.com/api/excashier.html" hidden="true"
				enctype="application/x-www-form-urlencoded" method="post">
				<input type="hidden" name="version" value="1.4" /> <input
					type="hidden" name="merchantId" value="test" /> <input
					type="hidden" name="signType" value="MD5" /> <input type="hidden"
					name="sign" value="24ecaefadadbc53d2f2299e272af9390" /> <input
					type="hidden" name="notifyUrl" value="http://47.89.177.73:8080" />
				<input type="hidden" name="returnUrl"
					value="http://47.89.177.73:8080" /> <input type="hidden"
					name="merchantOrderNo" value="test" /> <input type="number"
					name="amount" value="2" hidden="true" /> <input type="hidden"
					name="goods[0].goodsName" value="test1" /> <input type="hidden"
					name="goods[0].goodsQuantity" value="1" /> <input type="hidden"
					name="goods[0].goodsPrice" value="22" /> <input type="hidden"
					name="goods[0].goodsType" value="2" /> <input type="hidden"
					name="expirationTime" value="1000000" /> <input type="hidden"
					name="sourceType" value="B" /> <input type="hidden"
					name="currency" value="KES" />
			</form>
		</div>
	</div>
</body>
</html>

