<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%
    request.setAttribute("delayReasonList", SystemCodeUtil.getSystemCodeList((String)session.getAttribute("merchantId"),"delay_reason"));
%>
<style>
    .layui-upload-img{width: 92px; height: 92px; margin: 0 10px 10px 0;}
</style>
<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <input type="hidden" name="orderNo" value="${orderNo}">
        <input type="hidden" name="taskId" value="${taskId}">
        <div class="layui-form-item">
            <label class="layui-form-label">OrderNo:</label>
            <div class="layui-input-block">
                <label class="layui-form-label">${orderNo}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">ReferenceNo:</label>
            <div class="layui-input-block">
                <label class="layui-form-label">${referenceNo}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label layui-form-text">Type:</label>
            <div class="layui-input-block">
                <select name="reason" lay-verify="required" lay-search="" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${delayReasonList}" var="r">
                        <option value="${r.code}">${r.value}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">Remark</label>
            <div class="layui-input-block">
                <textarea placeholder="remark" class="layui-textarea" name="remark"></textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <input type="hidden" name="image">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="delay">Submit</button>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-upload-list" id="imageList">
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(function () {

        layui.use('form', function () {
            var form = layui.form;
            form.render();
            form.on('submit(delay)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/delay/delay.html",
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