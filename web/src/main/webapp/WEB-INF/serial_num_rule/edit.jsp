<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">Serial Type:</label>
            <div class="layui-input-block">
                <input type="hidden" value="${rule.serialType}" name="serialType" autocomplete="off" >
                <label class="layui-form-label">${rule.serialType}</label>

            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Name:</label>
            <div class="layui-input-block">
                <input type="text" value="${rule.name}" name="name" autocomplete="off" class="layui-input"
                       lay-verify="required">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Prefix:</label>
            <div class="layui-input-block">
                <input type="text" name="prefix" value="${rule.prefix}" autocomplete="off" class="layui-input"
                       lay-verify="required">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Format:</label>
            <div class="layui-input-block">
                <select name="format" id="format"  lay-search="" style="display: none">
                    <option value="">choose or search....</option>
                    <option value="yy">yy</option>
                    <option value="yyMM">yyMM</option>
                    <option value="yyMMdd">yyMMdd</option>
                    <option value="yyyy">yyyy</option>
                    <option value="yyyyMM">yyyyMM</option>
                    <option value="yyyyMMdd">yyyyMMdd</option>
                </select></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Length:</label>
            <div class="layui-input-block">
                <input type="text" value="${rule.serialLength}" name="serialLength" autocomplete="off"
                       lay-verify="required"
                       class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="edit-serial-rule">Submit</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            $("#format option").each(function () {
                if ($(this).val() == '${rule.format}') {
                    $(this).attr("selected", true)
                }
            });
            form.render();
            //监听提交
            form.on('submit(edit-serial-rule)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/admin/serialNumRule/edit.html",
                    data: $("#myForm").serialize(),
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.result) {
                            window.parent.layer.close(window.parent.layer.index);
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
