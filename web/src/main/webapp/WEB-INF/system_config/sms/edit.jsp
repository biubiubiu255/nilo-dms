<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <input type="hidden" name="smsType" value="${smsConfig.smsType}">
        <div class="layui-form-item">
            <label class="layui-form-label">Message Type:</label>
            <div class="layui-input-block">
                <label class="layui-form-label">${smsConfig.smsType}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Name:</label>
            <div class="layui-input-inline">
                <input class="layui-input" name="name" autocomplete="off" value="${smsConfig.name}">
            </div>
            <label class="layui-form-label">是否启用:</label>
            <div class="layui-input-inline">
                <select name="status" lay-filter="status" lay-verify="required" lay-search="" style="display: none">
                    <option value="1" <c:if test="${smsConfig.status == 1}"> selected</c:if>>是</option>
                    <option value="0" <c:if test="${smsConfig.status == 0}"> selected</c:if>>否</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Param:</label>
            <label class="layui-label">${smsConfig.param}</label>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Message Content:</label>
            <div class="layui-input-block">
                <textarea name="content" maxlength="250" placeholder="Message Content"
                          class="layui-textarea">${smsConfig.content}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-route-config">Submit</button>
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
            form.on('submit(add-route-config)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/systemConfig/sms/edit.html",
                    data: data.field,
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 1000});
                            layer.closeAll();
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
