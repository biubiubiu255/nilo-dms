<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
    
        <input type="hidden" name="id" value="${resultDate.id}">

        <div class="layui-form-item">
            <label class="layui-form-label">expressName</label>
            <div class="layui-input-block">
                <input type="text" name="expressName" value="${resultDate.expressName}" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">expressCode</label>
            <div class="layui-input-block">
                <input type="text" name="expressCode" value="${resultDate.expressCode}" autocomplete="off" class="layui-input">
            </div>
        </div>


        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-express">Submit</button>
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
            //监听提交
            form.on('submit(add-express)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/admin/express/editExpressInfo.html",
                    data: $("#myForm").serialize(),
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS!', {icon: 1, time: 1000}, function () {
                                layer.closeAll();
                                reloadCurrentPage();
                            });
                        } else {
                            //失败，提交表单成功后，释放hold，如果不释放hold，就变成了只能提交一次的表单
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                        reloadCurrentPage();
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
