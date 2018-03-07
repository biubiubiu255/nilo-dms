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

        <div class="layui-col-md8 layui-col-lg5">
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From" name="sTime">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To" name="eTime">
            </div>
        </div>
    </div>

    <!-- 搜索栏的第二行 -->

    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Status:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toMonth" placeholder="To" name="month">
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Client Name:</label>
            <div class="layui-form-item layui-inline">
                <input type="text" name="clientName" autocomplete="off" class="layui-input">
            </div>
        </div>

        <!-- 搜索按钮 -->
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>

    <table class="layui-table"
           lay-data="{ url:'/report/receive/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}"><O></O>rderNo</th>
            <th lay-data="{field:'order_type', width:200}">OrderType</th>
            <th lay-data="{field:'order_platform', width:200}">OrderPlatform</th>
            <th lay-data="{field:'name', width:200}">Signer</th>
            <th lay-data="{field:'created_time', width:200, templet:'<div>{{ formatDate(d.created_time) }}</div>'}">CreatedTime</th>
            <th lay-data="{field:'receive_time', width:200, templet:'<div>{{ formatDate(d.receive_time) }}</div>'}">ReceiveTime</th>
        </tr>
        </thead>
    </table>
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

                layDate.render({
                    elem: '#toMonth'
                    , lang: 'en'
                    , type: 'month'
                    , format: 'yyyyMM'
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
               var sTime = $("input[name='sTime']").val()=="" ? "" : Date.parse(new Date($("input[name='sTime']").val()))/1000;
               var eTime = $("input[name='eTime']").val()=="" ? "" : Date.parse(new Date($("input[name='eTime']").val()))/1000;


                table.reload("${id0}", {
                    where: {
                        orderNo: $("input[name='orderNo']").val(),
                        sTime: sTime,
                        eTime: eTime,
                        mother: $("input[name='month']").val(),
                        clientName: $("input[name='clientName']").val()
                    }
                });
            };
        });

    </script>
</body>
</html>
