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
<link rel="stylesheet" href="/layui/css/multselect.css" media="all">
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>
<script type="text/javascript" src="/mobile/js/jquery.scanner.js"></script>
<script type="text/javascript"
	src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>


</head>
<body>

	<div class="wap_content">

		<div class="wap_top">
			<a href="/mobile/home.html" title="Back" class="wap_top_back"></a>
			<h2 data-locale="sign_scan_title">Sign Scan</h2>
		</div>

		<div class="formula_modify">
			<form id="myForm" class="layui-form" action="">
				<div class="banner_content">
					<ul class="one_banner">

						<li><input type="text" placeholder="Logistics No" property_name="all_logistics_no" set_attr="placeholder"
							id="logisticsNo" name="logisticsNo" class="input_value i18n-input" /><span class="scanner" data-locale="all_scan"></span></li>
						<li><input type='text' placeholder="Signer" id="signer" property_name="sign_scan_signer" set_attr="placeholder"
							class='input_value i18n-input' name='signer' required="required" />
						<!-- <span>Aquire</span> --></li>
						<li><input type='text' placeholder="Remark" id="remark" property_name="sign_scan_remark" set_attr="placeholder"
							class='input_value i18n-input' name='remark' /></li>
						<li><label data-locale="sign_scan_Picture">Sign Picture</label>
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
		</div>
	</div>
	<script src="/layui/layui.js" charset="utf-8"></script>
	<script type="text/javascript">
        //loadLanguage('en');
		layui.use(['upload', 'jquery'], function() {

			var $ = layui.jquery, upload = layui.upload;

			var isUpPic = false;

			upload.render({
				elem : '.xq',
				url : '/mobile/rider/sign/save.html',
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
                    if(isUpPic===false) {
                    	showWarning("plase chose pic");
                    	return;
                    }
					this.data = {
						logisticsNo : $("#logisticsNo").val(),
						signer : $("#signer").val(),
						remark : $("#remark").val()
					};
                    showMask();
                    //showError('submit success');

					//layui.upload.config.data = {logisticsNo:1,signer:2};
				},
				done : function(res) {
					if (res.result) {
                        $("#remark").val();
					} else {
						showError(res.msg);
						$("#remark").val();
					}
				}
			});

			var inputFile = $("input[type='file']");

			inputFile.addClass("layui-hide");

			inputFile.css("display", "none");

		});

		//android.startScan();

		$(document)
		.ready(
				function() {
					var mobile = new MobileData({
						autoLoad : false,
						formId : 'myForm',
						model : 'customers'
					});
					var scan_callback = function(code) {
						mobile.setFormFieldValue("logisticsNo", code);
						ajaxRequest('/mobile/rider/sign/getDetail.html',{orderNo: code},false,function(data){
                            if(!(data.msg==null)){
                                showError(data.msg)
                            }
                            mobile.setFormFieldValue("signer", data.data)
					       // mobile.setFormFieldValue("signer", code)
					    });
					}
					$.scanner(scan_callback);

					$("#lypic").first().hide();

				});


	</script>
</body>
</html>
