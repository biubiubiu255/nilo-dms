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
            <label class="layui-form-label">OrderNo:</label>
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

    <table class="layui-table"
           lay-data="{ url:'/report/sign/list.html?exportType=2',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}">orderNo</th>
            <th lay-data="{field:'referenceNo', width:200}">referenceNo</th>
            <th lay-data="{field:'weight', width:100}">weight</th>
            <th lay-data="{field:'needPayAmount', width:100}">needPayAmount</th>
            <th lay-data="{field:'alreadyPaid', width:100}">alreadyPaid</th>
            <th lay-data="{field:'handleBy', width:100}">handleBy</th>
            <th lay-data="{field:'handleTime', width:170, templet:'<div>{{ formatDate(d.handleTime) }}</div>'}">handleTime</th>
            <th lay-data="{field:'sName', width:100}">sName</th>
            <th lay-data="{field:'rName', width:100}">rName</th>
            <th lay-data="{field:'contactNumber', width:170}">contactNumber</th>
            <th lay-data="{field:'address', width:100}">address</th>
            <th lay-data="{field:'remark', width:150}">remark</th>
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
                $.get()
                reloadTable();
            })

            function reloadTable() {
                table.reload("${id0}", {
                    where: {
                        orderNo: $("input[name='orderNo']").val(),
                        fromHandledTime: $("#fromHandledTime").val(),
                        toHandledTime: $("#toHandledTime").val(),
                        rider: $("input[name='rider']").val(),
                    }
                });
            };
        });

    </script>
</body>
</html>
