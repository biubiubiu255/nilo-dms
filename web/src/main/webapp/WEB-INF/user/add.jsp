<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <input type="hidden" name="userId" value="${user.userId}">

        <div class="layui-form-item">
            <label class="layui-form-label">User Name</label>
            <div class="layui-input-block">
                <input type="text" name="userName" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Real Name</label>
            <div class="layui-input-block">
                <input type="text" name="name" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Email</label>
            <div class="layui-input-block">
                <input type="text"  name="email" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Phone</label>
            <div class="layui-input-block">
                <input type="text" name="phone" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">User Roles</label>
            <div class="layui-input-block">
                <c:forEach items="${roleList}" var="role">
                    <input type="checkbox" name="roleIds" title="${role.roleName}" value="${role.roleId}"
                           lay-skin="primary" >
                </c:forEach>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Distribution Network</label>
            <div class="layui-input-block">
                <c:forEach items="${distributionList}" var="distribution">
                    <input type="checkbox" name="networks" title="${distribution.name}" value="${distribution.id}"
                           lay-skin="primary" >
                </c:forEach>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-user">Submit</button>
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
            form.on('submit(add-user)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/admin/user/addUserInfo.html",
                    data: $("#myForm").serialize(),
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS!', {icon: 1, time: 1000}, function () {
                                layer.closeAll();
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
