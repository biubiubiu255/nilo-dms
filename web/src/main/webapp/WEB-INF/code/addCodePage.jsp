<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<div style="margin: 15px;">
    <form id="myForm" class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">Type</label>
            <div class="layui-input-block">
                <input type="text" name="type" value="${type}" autocomplete="off" class="layui-input" readonly>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Code</label>
            <div class="layui-input-block">
                <input type="text" name="code" autocomplete="off" class="layui-input" lay-verify="required">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Value</label>
            <div class="layui-input-block">
                <input type="text" name="value" autocomplete="off" class="layui-input" lay-verify="required">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">ShowOrder</label>
            <div class="layui-input-block">
                <input type="text" name="showOrder" autocomplete="off" class="layui-input" lay-verify="required|number">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Remark</label>
            <div class="layui-input-block">
                <input type="text" name="remark" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-code-submit">Submit</button>
                <button type="reset" class="layui-btn layui-btn-primary reset">Reset</button>
            </div>
        </div>
    </form>
</div>

<script>
    $(function () {

        layui.use('form', function () {
            var form = layui.form;
            form.on('submit(add-code-submit)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/admin/code/addCode.html",
                    dataType: "json",
                    data: $("#myForm").serialize(),
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000});
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