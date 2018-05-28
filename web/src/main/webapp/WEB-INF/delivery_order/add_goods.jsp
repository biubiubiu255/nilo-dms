<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
%>
<div class="box-body">
    <form id="goodsForm" class="layui-form" action="">
        <input type="hidden" name="orderNo" value="${orderNo}">
        <div class="layui-form-item">
            <label class="layui-form-label">OrderNo:</label>
            <div class="layui-input-block">
                <label class="layui-form-label">${orderNo}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">GoodsID:</label>
            <div class="layui-input-block">
                <input type="text" name="goodsId" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">GoodsDesc:</label>
            <div class="layui-input-block">
                <input type="text" name="goodsDesc" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Qty:</label>
            <div class="layui-input-block">
                <input type="text" name="qty" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Quality:</label>
            <div class="layui-input-block">
                <input type="text" name="quality" autocomplete="off"
                       class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn " lay-submit lay-filter="add-goods">Submit</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(function () {

        layui.use('form', function () {
            var form = layui.form;
            form.render();
            form.on('submit(add-goods)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/deliveryOrder/addGoods.html",
                    dataType: "json",
                    data: $("#goodsForm").serialize(),
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