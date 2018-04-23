<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("merchantId", (String) session.getAttribute("merchantId"));
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Waybill No:</label>
            <div class="layui-input-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md8 layui-col-lg5">
            <label class="layui-form-label">handleTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromHandledTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toHandledTime" placeholder="To">
            </div>
        </div>
    </div>
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg5">
            <label class="layui-form-label">Rider:</label>
            <div class="layui-form-item layui-inline">
                <input type="text" name="rider" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>

    <iframe scrolling="no" frameborder="0" src="/report/sign/list.html" id="ifm" width="100%" height="100%" style="padding: 0px;"></iframe>

    <%@ include file="../../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {

            layui.use(['element', 'form', 'laydate'], function () {
                var layDate = layui.laydate;
                layDate.render({
                    elem: '#fromHandledTime'
                    , lang: 'en'
                });
                layDate.render({
                    elem: '#toHandledTime'
                    , lang: 'en'
                });
            });
            var table;
            layui.use('table', function () {
                table = layui.table;
                table.on('tool(demo)');
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            function reloadTable() {
                var param = {
                    orderNo: $("input[name='orderNo']").val(),
                    carrierNames: $("select[name='express']").val(),
                    orderTypes: $("select[name='orderType']").val(),
                    orderStatus: $("select[name='orderStatus']").val(),
                    fromHandledTime: $("#fromHandledTime").val(),
                    toHandledTime: $("#toHandledTime").val(),
                    weight: $("input[name='weight']").val(),
                    rider: $("input[name='rider']").val(),
                    sellerCustomer: $("input[name='sellerCustomer']").val()
                };
                var url = "/report/sign/list.html";
                param = jQuery.param( param )
                document.getElementById("ifm").src = url + "?" + param;
            };
        });

    </script>
</body>
</html>
