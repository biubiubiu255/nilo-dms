<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">Opt Type:</label>
            <div class="layui-input-block">
                <lp:enumTag selectId="optTypeEdit" selectName="optTypeEdit" className="OptTypeEnum"
                            code="${orderHandleConfig.optType}" disabled="true" />
            </div>
        </div>
        <hr>
        <div class="layui-form-item">
            <label class="layui-form-label">Update Status:</label>
            <div class="layui-input-block">
                <lp:deliveryStatus selectId="updateStatus" showChoose="true" selectName="updateStatus" status="${orderHandleConfig.updateStatus}" multiple="false"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Allow Status:</label>
            <div class="layui-input-block">
                <lp:deliveryStatus selectId="allowStatus" selectName="allowStatus" multiple="true"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Not Allow Status:</label>
            <div class="layui-input-block">
                <lp:deliveryStatus selectId="notAllowStatus" selectName="notAllowStatus" multiple="true"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Class Name:</label>
            <div class="layui-input-block">
                <input type="text" value="" name="className" autocomplete="off"
                       class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-order-handle-config">Submit</button>
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
            form.on('submit(add-order-handle-config)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/systemConfig/orderHandle/edit.html",
                    data: {
                        optTypeEdit: $("select[name='optTypeEdit']").val(),
                        updateStatus: $("select[name='updateStatus']").val(),
                        allowStatus: $("select[name='allowStatus']").val(),
                        notAllowStatus: $("select[name='notAllowStatus']").val(),
                        className: $("input[name='className']").val(),
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
