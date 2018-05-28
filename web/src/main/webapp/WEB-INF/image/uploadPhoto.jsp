<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE>
<html lang="en">
<head>
    <title>Photo Cut</title>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>

    <link rel="stylesheet" href="${ctx}/jcrop/jquery.Jcrop.min.css">
    <link rel="stylesheet" href="${ctx}/jcrop/photo.css">

    <script src="${ctx}/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${ctx}/jcrop/jquery.Jcrop.min.js"></script>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
</head>
<body>

<div class="container">
    <div class="row">
        <div class="span12">
            <div class="jc-demo-box">
                <div class="page-header">
                    <h1>Photo Cut</h1>
                </div>
                <img src="${ctx}/jcrop/test.png" width="500" height="500" id="target"/>
                <div id="preview-pane">
                    <div class="preview-container">
                        <img src="${ctx}/jcrop/test.png" class="jcrop-preview"/>
                    </div>
                    <div style='float:left;display:inline;'>
                        <a class='btn_addPic' href="javascript:void(0);">
                            <span><EM>+</EM>Add Photo</span>
                            <input id="upload_image" type="file" name="upimage" class="filePrew"/>
                        </a>
                    </div>
                    <div style='float:right;display:inline;'>
                        <a class='btn_addPic' href="javascript:void(0);" onclick='submit()'>
                            <span>Submit</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
<script type="text/javascript">
    var global_api = "";
    var boundx = "";//页面图片宽度
    var boundy = "";//页面图片高度
    var tag = false;
    $(function () {

        // Create variables (in this scope) to hold the API and image size
        //创建变量(在这个范围)的API和图像大小
        var jcrop_api,
        // Grab some information about the preview pane
        //获取一些信息预览窗格
                $preview = $('#preview-pane'),
                $pcnt = $('#preview-pane .preview-container'),
                $pimg = $('#preview-pane .preview-container img'),

                xsize = $pcnt.width(),
                ysize = $pcnt.height();

        $('#target').Jcrop({
            onChange: updatePreview,
            onSelect: updatePreview,
            aspectRatio: xsize / ysize
        }, function () {
            // Use the API to get the real image size
            //使用API来获得真实的图像大小
            var bounds = this.getBounds();
            boundx = bounds[0];
            boundy = bounds[1];
            // Store the API in the jcrop_api variable
            //jcrop_api变量中存储API
            jcrop_api = this;
            // Move the preview into the jcrop container for css positioning
            //预览进入jcrop容器css定位
            $preview.appendTo(jcrop_api.ui.holder);
        });

        function updatePreview(c) {
            if (parseInt(c.w) > 0)
                global_api = c;
            {
                var rx = xsize / c.w;
                var ry = ysize / c.h;

                $pimg.css({
                    width: Math.round(rx * boundx) + 'px',
                    height: Math.round(ry * boundy) + 'px',
                    marginLeft: '-' + Math.round(rx * c.x) + 'px',
                    marginTop: '-' + Math.round(ry * c.y) + 'px'
                });
            }
        };


        //=======选择图片回显===============
        $('#upload_image').change(function (event) {
            // 根据这个 <input> 获取文件的 HTML5 js 对象
            var files = event.target.files, file;
            if (files && files.length > 0) {
                alert();
                // 获取目前上传的文件
                file = files[0];
                // 下面是关键的关键，通过这个 file 对象生成一个可用的图像 URL
                // 获取 window 的 URL 工具
                var URL = window.URL || window.webkitURL;
                // 通过 file 生成目标 url
                var imgURL = URL.createObjectURL(file);
                // 用这个 URL 产生一个 <img> 将其显示出来
                $('.jcrop-holder img').attr('src', imgURL);
                $('.preview-container img').attr('src', imgURL);
            }
        });
        //=============是否选择了本地图片==================
        $("#upload_image").change(function () {
            tag = true;
        });
    });
    //========================================================
    //======图片保存===========
    function submit() {
        if (tag && global_api != "") {
            ajaxFileUpload();
        } else {
            alert('你是不是什么事情都没干？');
        }

    }

    //ajax文件上传
    function ajaxFileUpload() {
        $.ajaxFileUpload({
                    url: '/image/uploadPhoto.html', //用于文件上传的服务器端请求地址
                    secureuri: false, //一般设置为false
                    fileElementId: 'upload_image', //文件上传空间的id属性
                    dataType: 'json', //返回值类型 一般设置为json
                    data: {x: global_api.x, y: global_api.y, w: global_api.w, h: global_api.h, pw: boundx, ph: boundy},
                    success: function (data) {
                        if (data.result) {
                            alert('成功');
                        } else {
                            alert('失败');
                        }
                    }
                }
        );
    }
</script>
</html>