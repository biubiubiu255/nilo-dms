<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%
    request.setAttribute("abnormalTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "abnormal_order_type"));
%>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <input type="hidden" name="abnormalNo" value="${abnormalNo}">
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
            <div class="layui-input-inline">
                <select name="handleType" lay-filter="typeSelect" lay-verify="required" lay-search=""
                        style="display: none">
                    <option value="">Select type....</option>
                    <c:forEach items="${abnormalTypeList}" var="r">
                        <option value=${r.code}>${r.value}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Remark:</label>
            <div class="layui-input-block">
                <textarea name="remark" placeholder="Remark" class="layui-textarea"></textarea>

            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label"></label>
            <input type="checkbox" name="returnToMerchantFlag" title="Return To Merchant" value="Y"
                   lay-skin="primary">
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="submit">Submit</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">

    $(function () {

        layui.use('form', function () {
            var form = layui.form;
            form.render();
            form.on('submit(submit)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/abnormalOrder/handleAbnormal.html",
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