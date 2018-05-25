<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ page import="com.nilo.dms.common.Constant" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("merchantId", (String) session.getAttribute("merchantId"));
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
//    request.setAttribute("carrierNames", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "report_carrier_name"));
%>
<body>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Waybill No:</label>
            <div class="layui-input-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Rider:</label>
            <div class="layui-form-item layui-inline">
                <input type="text" name="rider" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md8 layui-col-lg5">
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To">
            </div>
        </div>
    </div>
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">OrderType:</label>
            <div class="layui-form-item layui-inline" style="margin: 0px">
                <select lay-filter="orderType" multiple name="orderType">
                    <option value="">Pls select order type...</option>
                    <c:forEach items="${orderTypeList}" var="r">
                        <option value=${r.code}>${r.code}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Status:</label>
            <div class="layui-form-item layui-inline">
                <lp:deliveryStatus selectId="orderStatus" selectName="orderStatus" multiple="true"/>
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Express:</label>
            <div class="layui-form-item layui-inline" style="margin: 0px">
                <select lay-filter="express" multiple name="express">
                    <option value="">Pls select Express...</option>
                    <c:forEach items="${express}" var="e">
                        <option value=${e.expressName}>${e.expressName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
    <div class="layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Seller Customer:</label>
            <div class="layui-form-item layui-inline">
                <input type="text" name="sellerCustomer" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>

    <iframe scrolling="no" frameborder="0" src="/report/send/list.html" id="ifm" width="100%" height="100%" style="padding: 0px;"></iframe>

    <%@ include file="../../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {

            layui.use(['element', 'form', 'laydate'], function () {
                var layDate = layui.laydate;
                layDate.render({
                    elem: '#fromCreatedTime'
                    , lang: 'en'
                });
                layDate.render({
                    elem: '#toCreatedTime'
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
                        fromCreatedTime: $("#fromCreatedTime").val(),
                        toCreatedTime: $("#toCreatedTime").val(),
                        weight: $("input[name='weight']").val(),
                        rider: $("input[name='rider']").val(),
                        sellerCustomer: $("input[name='sellerCustomer']").val(),
                    };
                var url = "/report/send/list.html";
                param = jQuery.param( param )
                document.getElementById("ifm").src = url + "?" + param;

            };
        });

    </script>
</body>
</html>
