<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../common/header.jsp" %>
<div class="box-body">

        <div class="layui-form-item">

            <label class="layui-form-label" style="width:120px">Package By</label>
            <div class="layui-input-inline">
                <input type="text" value="${packageBy}" autocomplete="off"
                       class="layui-input" disabled>
            </div>
            <label class="layui-form-label" style="width:120px">Next Station</label>
            <div class="layui-input-inline">
                <input type="text" value="${packageInfo.nextNetworkDesc}" autocomplete="off"
                       class="layui-input " disabled>
            </div>
            <label class="layui-form-label" style="width:120px">Weight</label>
            <div class="layui-input-inline">
                <input type="text" name="weight" value="${packageInfo.weight}" autocomplete="off"
                       class="layui-input" disabled>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">Length</label>
            <div class="layui-input-inline">
                <input type="text" name="length" value="${packageInfo.len}" autocomplete="off"
                       class="layui-input" disabled>
            </div>
            <label class="layui-form-label" style="width:120px">Width</label>
            <div class="layui-input-inline">
                <input type="text" name="width" value="${packageInfo.width}" autocomplete="off"
                       class="layui-input" disabled>
            </div>
            <label class="layui-form-label" style="width:120px">Height</label>
            <div class="layui-input-inline">
                <input type="text" name="high" value="${packageInfo.high}" autocomplete="off"
                       class="layui-input" disabled>
            </div>
        </div>

    <hr>
    <div class="layui-row">
        <div class="layui-col-md6 layui-col-lg6">

            <table class="layui-table">
                <colgroup>
                    <col width="200">
                    <col width="150">
                    <col width="200">
                    <col width="150">
                </colgroup>
                <thead>
                <tr>
                    <th>OrderNo</th>
                    <th>Weight</th>
                    <th>ReferenceNo</th>
                    <th>OrderType</th>
                </tr>
                <c:forEach items="${list}" var="item">
                    <tr>
                        <td>${item.orderNo}</td>
                        <td>${item.weight}</td>
                        <td>${item.referenceNo}</td>
                        <td>${item.orderType}</td>
                    </tr>
                </c:forEach>
                </thead>
            </table>
        </div>
    </div>

</div>
<script type="text/javascript">
    $(function () {

        var form, table;
        layui.use(['form', 'layer', 'table'], function () {
            form = layui.form;
        });

    });
</script>
