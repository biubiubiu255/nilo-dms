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


</head>
<body>

	<div class="wap_content">

		<div class="wap_top">
			<a href="/mobile/home.html" title="Back"
				class="wap_top_back"></a>
			<h2>Sign Scan</h2>
		</div>

		<div class="formula_modify">
			<form id="myForm" class="layui-form" action="">
				<div class="banner_content">
					<ul class="one_banner">
						<li>
							<%--<label>Logistics No</label>--%> <input type="tel"
							placeholder="Logistics No" id="logisticsNo" name="logisticsNo"
							class="input_value" /> <span id="scan">scan</span>
						</li>
						<li><input type='text' placeholder="Signer" id="signer"
							class='input_value' name='signer' /><span>Aquire</span></li>
						<li><input type='text' placeholder="Remark" id="remark"
							class='input_value' name='remark' /></li>
						<li><label>Take a picture</label>
							<div class="xq">
								<img src="/mobile/images/2300.jpg" />
							</div></li>
					</ul>
					<center>



						<div>
							<img src=""
								style="width: 100px; height: 100px;" id="lypic" /> 
						</div>
					</center>





					<div class="bottom_a_button">
						<!-- <a onclick="doFind()" id="commit">submit</a> -->
						<a id="commit">submit</a>
					</div>
				</div>
			</form>
		</div>

	</div>

	<script src="/layui/layui.js" charset="utf-8"></script>
	<script type="text/javascript">
		layui.use('upload', function() {
			var $ = layui.jquery, upload = layui.upload;
			
			upload.render({
				elem : '.xq',
				url : '/mobile/sign/save.html',
				auto : false , //选择文件后不自动上传
                data:{} ,
				bindAction : '#commit',
				choose : function(obj) {
					obj.preview(function(index, file, result) {
						//$('#demo2').append('<img name = "s_pmt_dw" style="width: 120px; height: 150px; margin-left: 16px;" src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img">')
						$('#lypic').attr('src', result); //图片链接（base64）  
					});
				},
				before : function(res) {
					this.data = {logisticsNo: $("#logisticsNo").val(), signer: $("#signer").val(), remark: $("#remark").val()};
					//layui.upload.config.data = {logisticsNo:1,signer:2};
				}, 

				done : function(res) {
					console.log(res)
				}
			});
			
			var inputFile = $("input[type='file']");

			inputFile.addClass("layui-hide");

			inputFile.css("display", "none");

		});

		document.getElementById('scan').onclick = function() {
			//android.startScan()
			var orderNo = $("#logisticsNo").val();
			drawTab(orderNo);
		};
		android.startScan();
		function doScan() {
			android.startScan();
		}
		function afterScan(scanResult) {
			alert(scanResult);
			document.getElementById("logisticsNo").value = scanResult;
			
		}

		function drawTab(orderNo){
			
            // var result;
			// url  param  isShowMsg callback isShowLoadMsgBox
            ajaxRequest("/mobile/rider/sign/getDetail.html", {orderNo : orderNo}, true, function(response){
            	if(response){
            		$("#signer").val(response.data.receiverInfo.receiverName);
            		$("#remark").val(response.data.remark);
                	//alert(response.data.receiverInfo.receiverName);
                	//alert(response.data.remark);
            	}

            }, true);

		}
		
        
		
		function doFind() {
			//load
		}

		function addTr2(tab, row) {
			var kuang1 = document.getElementById("logisticsNo")
			var kuang2 = document.getElementById("signer")
			var trHtml = "<tr align='center'><td>" + kuang1.value + "</td><td>"
					+ kuang2.value + "</td></tr>";
			addTr(tab, row, trHtml);
		}

		function addTr(tab, row, trHtml) {
			//获取table最后一行 $("#tab tr:last")
			//获取table第一行 $("#tab tr").eq(0)
			//获取table倒数第二行 $("#tab tr").eq(-2)
			var $tr = $("#" + tab + " tr").eq(row);
			if ($tr.size() == 0) {
				alert("指定的table id或行数不存在！");
				return;
			}
			$tr.after(trHtml);
			// $("#logisticsNo").val("").s();
			// $("#signer").val("");
			// $("#remarkx").val("");
		}
	</script>
</body>
</html>
