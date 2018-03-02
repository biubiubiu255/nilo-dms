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
    <!--<link rel="apple-touch-icon-precomposed" sizes="114x114" href="">
    <link rel="apple-touch-icon-precomposed" sizes="57x57" href="">-->
    <title></title>
    <link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
    <link href="/mobile/css/mp.css" type="text/css" rel="stylesheet" />
    <link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
    <script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/mobile/js/functions.js"></script>
    <script type="text/javascript"
            src="/mobile/js/jquery.i18n.properties-1.0.9.js"></script>
    <script src="/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript">
        //loadLanguage('en');
        layui.use(['upload', 'jquery'], function() {

            var $ = layui.jquery, upload = layui.upload;

            var isUpPic = false;
            upload.render({
                elem : '.xq',
                url : '/mobile/rider/Batch/submit.html',
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
                        totalOrders : $("input[name='totalOrders']").val(),
                        totalSum : $("input[name='totalSum']").val(),
                        serialNo : $("input[name='serialNo']").val()
                    };
                    //layui.upload.config.data = {logisticsNo:1,signer:2};
                },
                done : function(res) {
                    if (res.result) {
                        showInfo('submit success');
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
        $(document).ready(function() {
            $("#lypic").first().hide();
        });

    </script>
</head>
<body>
<div class="wap_content">
   
    <div class="wap_top"><a href="javascript:history.go(-1)" title="返回" class="wap_top_back"></a>
        <h2>Batch transfer</h2>
    </div>

    <div class="formula_modify">
        <div class="banner_content">
            <form id="batch-form">
                <ul class="one_banner">
                    <li><label>Total orders</label><input type="text" disabled="true" value="14" name="totalOrders" class="input_value33" /><br/></li>
                    <li><label>total sum</label><input type="text" disabled="true" value="141" name="totalSum" class="input_value33" /></li>
                    <li><label>Serial No</label><input type="text" value="777777" name="serialNo" class="input_value33" /><br/></li>
                    <li><label data-locale="sign_scan_Picture">Sign Picture</label><div class="xq"><img src="/mobile/images/2300.jpg" /></div></li>
                </ul>
                <center>
                    <div>
                        <img src="" style="width: 100px; height: 100px;" id="lypic" />
                    </div>
                </center>
                <div class="bottom_a_button">
                    <a id="commit" data-locale="all_submit">submit</a>
                </div>
            </form>
        </div>
    </div>
</div>
</div>
</body>
<html>
