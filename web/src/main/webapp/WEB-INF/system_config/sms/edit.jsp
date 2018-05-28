<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">Message Type:</label>
            <div class="layui-input-block">
                <lp:enumTag selectId="optTypeEdit" selectName="optTypeEdit" className="OptTypeEnum"
                            code="${smsConfig.smsType}" disabled="true" />
            </div>
        </div>
        <hr>

        <div class="layui-form-item">
            <label class="layui-form-label">Message Content:</label>
            <div class="layui-input-block">
                <textarea name="content" placeholder="Message Content"
                          class="layui-textarea">${smsConfig.content}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Remark:</label>
            <div class="layui-input-block">
                <textarea name="remark" placeholder="Remark" class="layui-textarea">${smsConfig.remark}</textarea>

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
                    data: {
                        optTypeEdit: $("select[name='optTypeEdit']").val(),
                        content: $("textarea[name='content']").val(),
                        remark: $("textarea[name='remark']").val(),

                    },
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
