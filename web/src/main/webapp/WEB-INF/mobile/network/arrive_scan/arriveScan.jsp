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

						mobile.initSubmitForm({
							formId : 'arrive-form',
							mbObject : mobile,
							postUrl : '/mobile/arrive/submit.html',
							beforeSubmit : function() {
								var scaned_array = [];
								var checkboxs = $('#arrive-form').find(
										'input:checked');
								for (var i = 0; i < checkboxs.length; i++) {
									var value = $(checkboxs[i]).attr('value');
									scaned_array.push(value);
								}
								console.dir(scaned_array);
								mobile.setFormFieldValue('scanedCodes',
										scaned_array.join(','));
								return true;
							},
							callback : function(data) {
								console.log(data);
								if (data.result) {
									showError('submit success')
									del();
								} else {
									showError(data.msg);

								}

							}
						});

						var code_array = [];
						var scan_callback = function(code) {
							mobile.setFormFieldValue('logisticsNo', code);
							if (!isEmpty(code_array[code])) {
								warningTipMsg('This order already scanned');
								return;
							}
							code_array[code] = code;
							var append_html = "<li id='code"+code+"'><input type='checkbox' checked='checked' class='input_value' value=\""+code+"\" name='items[]' /><span>"
									+ code + "</span></li>";
							$('#append_order_items_id').prepend(append_html);
						}
						$.scanner(scan_callback);

						$('a.delete_button').click(function () {
				            del();
				        });

						function del() {
							var checkboxs = $('#arrive-form').find(
									'input:checked');
							for (var i = 0; i < checkboxs.length; i++) {
								var value = $(checkboxs[i]).attr('value');
								delete code_array[value];
								$('#code' + value).remove();
							}
						}
					});
</script>

</head>
<body>

	<div class="wap_content">
		<div class="wap_top">
			<a href="javascript:history.go(-1)" title="Back" class="wap_top_back"></a>
			<h2 data-locale="arrive_scan_title">Arrive Scan</h2>
		</div>

		<div class="banner_content">
			<form id="arrive-form">
				<div class="banner_content">
					<input type="hidden" name="scanedCodes" />
					<ul class="one_banner">
						<li><input type='text' placeholder="Logistics No"
							required="required" maxlength='100' class='input_value'
							id="logisticsNo" name='logisticsNo' /><span class="scanner"
							id="scan">scan</span></li>

					</ul>
				</div>

				<ul id='append_order_items_id'>
					<%--<li id="code123"><input type='checkbox' class='input_value' value="123" name='items' /><span>code 12123123</span></li>--%>
					<%--<li id="code456"><input type='checkbox' class='input_value' value="456" name='items' /><span>code 12123123</span></li>--%>
				</ul>

				<div class="bottom_a_button11">
					<a href="javascript:void(0);" class="delete_button">delete</a>
				</div>
				<div class="bottom_a_button22">
					<a href="javascript:void(0);" class="submit">submit</a>
				</div>

				<!-- <div class="bottom_a_button11">
					<a onclick="delTr2()">delete</a>
				</div>
				<div class="bottom_a_button22">
					<a onclick="Judge('fuxuan')">submit</a>
				</div> -->

				<!-- <table cellpadding="0" id="tab" cellspacing="0" class="pf_div1">
					<tr>
						<td>Logistics No</td>
						<td><input type="checkbox" id="allFuxuan" checked="checked"
							onclick="sel('fuxuan')"></td>
					</tr>
				</table> -->
			</form>
		</div>

	</div>

	<!-- <script>
		document.getElementById('scan').onclick = function() {
			android.startScan()
		};

		function doScan() {
			android.startScan();
		}
		function afterScan(scanResult) {
			//document.getElementById("logisticsNo").value = scanResult;
			addTr2('tab', 0, scanResult);
		}

		/* $("#logisticsNo").focus();
		$("#logisticsNo").keydown(function (event) {
		    event = document.all ? window.event : event;
		    if ((event.keyCode || event.which) == 13) {
		         var scanResult = document.getElementById("logisticsNo").value
		        addTr2('tab', 0,scanResult);
		    }
		}); */

		function sel(a) {
			var o = document.getElementsByName(a)
			for (var i = 0; i < o.length; i++)
				o[i].checked = event.srcElement.checked
		}

		function delTr2() {
			// android.startScan(afterScan);
			delTr('fuxuan');
		}
		function delTr(fuxuan) {
			//获取选中的复选框，然后循环遍历删除
			var fuxuans = $("input[name=" + fuxuan + "]:checked");
			if (fuxuans.size() == 0) {
				alert("You did not select the required action！");
				return;
			}
			fuxuans.each(function() {
				$(this).parent().parent().remove();
			});
		}

		function addTr2(tab, row, scanResult) {
			var trHtml = "<tr align='center'><td>"
					+ scanResult
					+ "</td><td><input type=\"checkbox\" checked=\"checked\" name=\"fuxuan\" value=\""+scanResult+"\"></td></tr>";
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
			//$("#logisticsNo").val("");
		}

		function Judge(fuxuan) {
			//获取选中的复选框，然后循环遍历删除
			var fuxuans = $("input[name=" + fuxuan + "]:checked");
			if (fuxuans.size() == 0) {
				alert("You did not select the required action！");
				return;
			} else {
				suiyi(fuxuan);
			}
		}

		function suiyi(fuxuan) {
			var arrWaybillNo = new Array();
			$("input[name=" + fuxuan + "]:checked").each(function(i, n) {
				arrWaybillNo.push($(this).val());
			});
			$.ajax({
				cache : false,
				type : "POST",
				traditional : true,
				url : "/mobile/arrive/submit.html",
				data : {
					arrWaybillNo : arrWaybillNo
				},
				async : false,
				error : function() {
					alert("submit error！");
				},
				success : function() {
					// addTr2('tab', -1);
					alert("submit success！");
					delTr(fuxuan);
				}
			});
		}
	</script> -->
</body>
</html>
