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
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To">
            </div>
        </div>
    </div>
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Status:</label>
            <div class="layui-form-item layui-inline">
                <lp:taskStatus selectId="taskStatus" selectName="taskStatus" multiple="true"/>
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
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
           lay-data="{ url:'/report/deliver/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}">orderNo</th>
            <th lay-data="{field:'referenceNo', width:200}">referenceNo</th>
            <th lay-data="{field:'statusDesc', width:100}">status</th>
            <th lay-data="{field:'networkDesc', width:100}">network</th>
            <%--<th lay-data="{field:'handledBy', width:100}">handledBy</th>--%>
            <th lay-data="{field:'createdTime',width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">createdTime</th>
            <th lay-data="{field:'handledTime', width:170, templet:'<div>{{ formatDate(d.handledTime) }}</div>'}">handledTime</th>
            <th lay-data="{field:'name', width:100}">name</th>
            <th lay-data="{field:'contactNumber', width:120}">contactNumber</th>
            <th lay-data="{field:'address', width:150}">address</th>
            <th lay-data="{field:'needPayAmount', width:100}">needPayAmount</th>
            <th lay-data="{field:'deliveryFee', width:100}">deliveryFee</th>
            <th lay-data="{field:'remark', width:170}">remark</th>
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
                table.reload("${id0}", {
                    where: {
                        orderNo: $("input[name='orderNo']").val(),
                        taskStatus: $("select[name='taskStatus']").val(),
                        fromCreatedTime: $("#fromCreatedTime").val(),
                        toCreatedTime: $("#toCreatedTime").val(),
                        rider: $("input[name='rider']").val(),
                    }
                });
            };
        });

    </script>
</body>
</html>
