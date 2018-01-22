<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="box-body">
    <form id="changePasswordForm" class="layui-form" action=""
          style="margin: 1.5em">

        <div class="layui-form-item">
            <label class="layui-form-label">Old Password</label>
            <div class="layui-input-block">
                <input type="text" name="oldPassword" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Password</label>
            <div class="layui-input-block">
                <input type="password" name="newPassword" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Password Again</label>
            <div class="layui-input-block">
                <input type="password" name="password2" autocomplete="off" class="layui-input">
            </div>
        </div>


        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-normal" lay-submit lay-filter="change-password-submit">Submit
                </button>
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
            form.on('submit(change-password-submit)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "admin/user/changePassword.html",
                    data: $("#changePasswordForm").serialize(),
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            window.parent.layer.close(window.parent.layer.index);
                            layer.msg('SUCCESS!', {icon: 1, time: 1000}, function () {
                                layer.closeAll();
                                var url = "account/logout.html";
                                window.location.href = url;
                            });
                        } else {
                            //失败，提交表单成功后，释放hold，如果不释放hold，就变成了只能提交一次的表单
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

