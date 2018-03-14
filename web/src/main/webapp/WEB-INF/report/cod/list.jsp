<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("merchantId", (String) session.getAttribute("merchantId"));
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
%>
<body>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">OrderNo:</label>
            <div class="layui-input-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">CreatedTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From" name="createdTime_s">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To" name="createdTime_e">
            </div>
        </div>


    </div>
    <!-- 搜索栏的第二行 -->
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">ScanNetwork:</label>
            <div class="layui-form-item layui-inline">
                <input type="text" name="scanNetwork" autocomplete="off" class="layui-input">
            </div>
        </div>
        <!-- 搜索按钮 -->
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>
    <iframe scrolling="no" frameborder="0" src="/report/cod/list.html" id="ifm" width="100%" height="100%" style="padding: 0px;"></iframe>

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

            $(".search").on("click", function () {
                reloadTable();
            })

            function reloadTable() {
                var sTime_creat = $("input[name='createdTime_s']").val() == "" ? "" : Date.parse(new Date($("input[name='createdTime_s']").val())) / 1000;
                var eTime_creat = $("input[name='createdTime_e']").val() == "" ? "" : Date.parse(new Date($("input[name='createdTime_e']").val())) / 1000 + 86400;
                if (sTime_creat != "" && eTime_creat == "" || eTime_creat != "" && sTime_creat == "") {
                    layui.use('layer', function () {
                        var layer = layui.layer;
                        layer.msg('Please select the full date', {icon: 0, time: 2000});
                    });
                    return;
                }
                var url = "/report/cod/list.html";
                var param = "?orderNo=" + $("input[name='orderNo']").val()+"&scanNetwork=" + $("input[name='scanNetwork']").val();
                document.getElementById("ifm").src = url + param;
                /*
                parent.addTabs({
                    id: '400099001',
                    title: 'COD Report',
                    close: true,
                    url: url
                });
                */

            };
        });

    </script>
</body>
</html>
