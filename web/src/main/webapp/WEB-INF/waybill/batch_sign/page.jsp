<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md3">
            <button type="button" class="layui-btn" id="importSignData">
                <i class="layui-icon">&#xe67c;</i>Import
            </button>
        </div>
        </br>
        <hr>
        </br>

        <div id="batch_sign_msg">
        </div>

    </div>
</div>
<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">

    $(function () {
        layui.use(['element'], function () {
        });
        layui.use('upload', function () {
            var upload = layui.upload;
            var load;
            var uploadInst = upload.render({
                elem: '#importSignData'
                , url: '/waybill/batch_sign/importSignData.html'
                , accept: 'file' //普通文件
                , exts: 'xls|xlsx'
                , before: function () {
                    load = layer.load(2);
                }
                , done: function (res) {
                    layer.close(load);
                    if (res.result) {
                        var message = "";
                        for (j = 0, len = res.data.length; j < len; j++) {
                            message = message + res.data[j] + "</br>";
                        }
                        $("#batch_sign_msg").html(message);
                    } else {
                        layer.msg(res.msg, {icon: 2, time: 2000});
                    }
                }
            });
        });
    });

</script>
</body>
</html>