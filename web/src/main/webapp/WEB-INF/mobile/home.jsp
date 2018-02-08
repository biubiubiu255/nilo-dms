<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<link rel="apple-touch-icon-precomposed" sizes="57x57" href=""> -->
<title></title>
<link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css" />
<link href="/mobile/css/mp.css" type="text/css" rel="stylesheet" />
<link href="/mobile/css/jxj_css.css" type="text/css" rel="stylesheet" />
<link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
</head>
<body>
	<div class="wap_content">
		<div class="wap_top">
			<h2>KiliExpress</h2>
		</div>

		<c:if test="${isRider }">
			<div class="j_sy_n1">
				<div class="model_banner_title">
					<i></i>Rider
				</div>
				<ul class="model_banner_ul"
					style="width: 100%; overflow: hidden; margin: 0px;">
					<a href="/mobile/rider/sign/sign.html" title="">
						<li><img src="/mobile/images/icon_1.png" /><br />Sign Scan</li>
					</a>
					<!-- <a href="/mobile/tiaozhuangController/dshkqs.html" title="">
         <li><img src="/mobile/images/icon_2.png" /><br/>COD Sign</li>
       </a> -->
					<!-- <a href="/mobile/rider/self/self.html" title="">
						<li><img src="/mobile/images/icon_6.png" /><br />Self-Collect
							Sign</li>
					</a> -->
					<a href="/mobile/rider/problem/scan.html" title="">
						<li><img src="/mobile/images/icon_7.png" /><br />Problem</li>
					</a>
					<a href="/mobile/rider/stranded/scan.html" title="">
						<li><img src="/mobile/images/icon_7.png" /><br />Stranded
							Parcel</li>
					</a>
					<!-- <a href="/mobile/tiaozhuangController/pjqr.html" title="">
         <li><img src="/mobile/images/icon_3.png" /><br/>Parcel Confirmation</li>
       </a>
       <a href="/mobile/tiaozhuangController/plzz.html" title="">
         <li><img src="/mobile/images/icon_5.png" /><br/>Batch transfer</li>
       </a> -->
				</ul>
				<div class="clear"></div>
			</div>
		</c:if>
		<c:if test="${isRider==false}">
			<div class="model_banner">
				<div class="model_banner_title">
					<i class="model_banner_title"></i>Network
				</div>
				<ul class="model_banner_ul">
					<a href="/mobile/arrive/scan.html" title="">
						<li><img src="/mobile/images/icon_3.png" /><br />Arrive Scan</li>
					</a>
					<a href="/mobile/deliver/scan.html" title="">
						<li><img src="/mobile/images/icon_5.png" /><br />Deliver Scan</li>
					</a>
					<a href="/mobile/send/scan.html" title="">
						<li><img src="/mobile/images/icon_4.png" /><br />Send Scan</li>
					</a>
					<a href="/mobile/send/list.html" title="">
						<li><img src="/mobile/images/icon_4.png" /><br />Send List</li>
					</a>

					<a href="/mobile/package/packing.html" title="">
						<li><img src="/mobile/images/icon_3.png" /><br />Packing</li>
					</a>
					
					<a href="/mobile/network/unpackage/unpack.html" title="">
						<li><img src="/mobile/images/icon_3.png" /><br />UnPack</li>
					</a>

					<a href="/mobile/rider/problem/scan.html" title="">
						<li><img src="/mobile/images/icon_7.png" /><br />Problem</li>
					</a>

					<a href="/mobile/rider/stranded/scan.html" title="">
						<li><img src="/mobile/images/icon_7.png" /><br />Stranded
							Parcel</li>
					</a>

				</ul>
				<div class="clear"></div>
			</div>
		</c:if>
		<div class="model_banner">
			<div class="model_banner_title">
				<i class="model_banner_title"></i>Basic function
			</div>
			<ul class="model_banner_ul">
				<a href="/mobile/tiaozhuangController/lycx.html">
					<li><img src="/mobile/images/scheduler_info.png" /><br />Routing
						Query</li>
				</a>
				<a href="/mobile/password/toPage.html" title="">
					<li><img src="/mobile/images/index_icon_1.png" /><br />Modify
						password</li>
				</a>
			</ul>
			<div class="clear"></div>
		</div>
		<div class="j_bootm"></div>

	</div>
</body>
</html>
