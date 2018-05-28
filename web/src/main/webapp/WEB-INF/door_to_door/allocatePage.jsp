<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div style="margin: 15px;">
    <form class="layui-form" id="myForm" action="">
        <input type="hidden" name="orderNo" value="${orderNo}">

        <div class="layui-form-item">
            <label class="layui-form-label">Rider:</label>
            <div class="layui-input-block">

                <select name="userId" lay-verify="required" lay-search="" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${list}" var="rider">
                        <option value="${rider.userId}">${rider.staffId}-${rider.nickName}</option>
                    </c:forEach>
                </select>

            </div>
        </div>


        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="submit">Submit</button>
                <button type="reset" class="layui-btn layui-btn-primary reset">Reset</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            form.render();
            form.on('submit(submit)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/doorToDoor/allocate.html",
                    dataType: "json",
                    data: $("#myForm").serialize(),
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                                layer.closeAll();
                            });
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
                return false;
            });
        });
    });

</script>
