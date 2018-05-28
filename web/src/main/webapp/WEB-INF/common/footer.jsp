<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<script src="${ctx}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
<!-- FastClick -->
<script src="${ctx}/plugins/fastclick/fastclick.js"></script>
<!-- layUI -->
<script src="${ctx}/layui/layui.js"></script>

<!-- AdminLTE App -->
<script src="${ctx}/dist/js/app.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="${ctx}/dist/js/demo.js"></script>
<!--tabs-->
<script src="${ctx}/dist/js/app_iframe.js"></script>
<script src="${ctx}/dist/js/components.js"></script>
<script src="${ctx}/dist/js/format.js"></script>

<script>
    var layer;
    layui.use('layer', function () {
        layer = layui.layer
    });

    $(function() {
        $.ajaxSetup({
            error: function (xhr, error, errorThrown) {
                if (xhr.status && xhr.status == 403) {
                    var url = xhr.getResponseHeader("Redirect");
                    layer.open({
                        title: false,
                        content: "Your session is inactive, please login again.",
                        closeBtn: 0,
                        btn: 'Login',
                        yes: function () {
                            top.location.href = url;
                        }
                    });
                }
            }
        });
    });
</script>