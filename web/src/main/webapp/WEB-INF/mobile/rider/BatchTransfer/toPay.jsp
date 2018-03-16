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
<!--<link rel="apple-touch-icon-precomposed" sizes="114x114" href="">
    <link rel="apple-touch-icon-precomposed" sizes="57x57" href="">-->
<title></title>
<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css" />
<link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
<script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/mobile/js/functions.js"></script>
<script type="text/javascript" src="/mobile/js/mobile_valid.js"></script>
<script type="text/javascript" src="/mobile/js/mobile.js"></script>

<script>
	var payForm = new MobileData({
		autoLoad : false,
		formId : 'pay-form'
	});

	payForm.initSubmitForm({
		formId : 'pay-form',
		mbObject : payForm,
		postUrl : 'http://sandbox.lipapay.com/api/excashier.html'
	});

	$(document).ready(function() {
		$("#all").click(function() {
			var o;
			if (this.checked) {
				o = document.getElementsByName("fuxuan");
				for (var i = 0; i < o.length; i++) {
					o[i].checked = event.srcElement.checked
				}
				;
			} else {
				$("[name=fuxuan]").attr("checked", false);
			}
		});
		$("[name=fuxuan]").click(function() {
			var o = document.getElementsByName("fuxuan");
			for (var i = 0; i < o.length; i++) {
				if (!o[i].checked) {
					$("#all").prop("checked", false);
					return;
				} else {
					$("#all").prop("checked", true);
				}
			}
			;
		});
	});

	function toPay() {
		var submit_array = [];
		$("input:checkbox[name='fuxuan']:checked").each(function() { // 遍历name=fuxuan的多选框
			submit_array.push($(this).val());// 每一个被选中项的值
		});
		if(submit_array.length==0){
			showError("please select an order");
			return ;
		}

		/* var checkboxs = $("[name=fuxuan]").find(
				'input:checked');
		var checkboxs = $('#arrive-form').find(
		'input:checked');
		alert('length='+checkboxs.length);
		for (var i = 0; i < checkboxs.length; i++) {
			
			var value = $(checkboxs[i]).attr('value');
			submit_array.push(value);
		} */
		ajaxRequest('/mobile/rider/Batch/readyToPay.html', {
			orderNos : submit_array.join(',')
		}, false, function(res) {
			payForm.setFormFieldValue("version", "1.4");
			payForm.setFormFieldValue("sign", res.data.sign);
			payForm.setFormFieldValue("signType", res.data.signType);
			payForm.setFormFieldValue("merchantId", res.data.merchantId);
			payForm.setFormFieldValue("notifyUrl", res.data.notifyUrl);
			payForm.setFormFieldValue("returnUrl", res.data.returnUrl);
			payForm.setFormFieldValue("merchantOrderNo",
					res.data.merchantOrderNo);
			payForm.setFormFieldValue("amount", res.data.amount);
			payForm.setFormFieldValue("goods[0].goodsName",
					res.data['goods[0].goodsName']);
			payForm.setFormFieldValue("goods[0].goodsQuantity",
					res.data['goods[0].goodsQuantity']);
			payForm.setFormFieldValue("goods[0].goodsPrice",
					res.data['goods[0].goodsPrice']);
			payForm.setFormFieldValue("goods[0].goodsType",
					res.data['goods[0].goodsType']);
			payForm
					.setFormFieldValue("expirationTime",
							res.data.expirationTime);
			payForm.setFormFieldValue("sourceType", res.data.sourceType);
			payForm.setFormFieldValue("currency", res.data.currency);
			document.getElementById("pay-form").submit();
		})
	}
</script>
</head>

<body>
	<div class="wap_content">

		<div class="wap_top">
			<a href="javascript:history.go(-1)" title="返回" class="wap_top_back"></a>
			<h2>Batch transfer</h2>
		</div>
		<div class="formula_modify">
			<div class="banner_content">
				<ul class="ni">
					<li>运单号</li>
					<li>金额</li>
					<li>签收时间</li>
					<li>全选&nbsp;<input type="checkbox" class="fuxuank" id="all"></li>
				</ul>
				<c:forEach items="${list}" var="m">
					<ul class="ni">
						<li>${m.orderNo}</li>
						<li>${m.needPayAmount}</li>
						<li>${m.handledTime}</li>
						<li><input type="checkbox" class="fuxuank" name="fuxuan"
							value="${m.orderNo}"></li>
					</ul>
				</c:forEach>
				<div class="bottom_a_button">
					<a href="javascript:void(0);" onclick="toPay()">submit</a>
				</div>
			</div>
		</div>
	</div>

	<form id="pay-form"
		action="http://sandbox.lipapay.com/api/excashier.html" hidden="true"
		enctype="application/x-www-form-urlencoded" method="post">
		<input type="hidden" name="version" value="1.4" /> <input
			type="hidden" name="merchantId" value="test" /> <input type="hidden"
			name="signType" value="MD5" /> <input type="hidden" name="sign"
			value="24ecaefadadbc53d2f2299e272af9390" /> <input type="hidden"
			name="notifyUrl" value="http://47.89.177.73:8080" /> <input
			type="hidden" name="returnUrl" value="http://47.89.177.73:8080" /> <input
			type="hidden" name="merchantOrderNo" value="test" /> <input
			type="number" name="amount" value="2" hidden="true" /> <input
			type="hidden" name="goods[0].goodsName" value="test1" /> <input
			type="hidden" name="goods[0].goodsQuantity" value="1" /> <input
			type="hidden" name="goods[0].goodsPrice" value="22" /> <input
			type="hidden" name="goods[0].goodsType" value="2" /> <input
			type="hidden" name="expirationTime" value="1000000" /> <input
			type="hidden" name="sourceType" value="B" /> <input type="hidden"
			name="currency" value="KES" />
	</form>
</body>
<html>