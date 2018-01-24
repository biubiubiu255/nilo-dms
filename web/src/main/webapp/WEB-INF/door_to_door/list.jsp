<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>

<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
%>
<body>
<div class="box-body">
    <div class="layui-form layui-row">
        <div class="layui-col-md4">
            <label class="layui-form-label">OrderNo:</label>
            <div class="layui-inline">
                <input class="layui-input" name="orderNo" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md4">
            <label class="layui-form-label">ReferenceNo:</label>
            <div class="layui-input-inline">
                <input type="text" name="referenceNo" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-col-md4">
            <label class="layui-form-label">OrderType:</label>
            <div class="layui-form-item layui-inline" style="margin: 0px">
                <select lay-filter="aaa" multiple name="aaa" lay-verify="required">
                    <option value="">Pls select order type...</option>
                    <c:forEach items="${orderTypeList}" var="r">
                        <option value=${r.code}>${r.code}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
    <div class="layui-row">
        <div class="layui-col-md8">
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To">
            </div>
        </div>

        <div class="layui-col-md1">
            <shiro:hasPermission name="400043">
            <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>
    <!-- /.box-header -->
    <div class="layui-btn-group demoTable">
        <shiro:hasPermission name="400041">
            <button class="layui-btn layui-btn-normal batchAllocate">Batch Allocate</button>
        </shiro:hasPermission>
    </div>

    <table class="layui-table" lay-data="{ url:'/order/doorToDoor/list.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{checkbox:true, fixed: true}"></th>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}">OrderNo</th>
            <th lay-data="{field:'orderType', width:100}">OrderType</th>
            <th lay-data="{field:'referenceNo', width:200}">ReferenceNo</th>
            <th lay-data="{field:'orderTime', width:170, templet:'<div>{{ formatDate(d.orderTime) }}</div>'}">
                OrderTime
            </th>
            <th lay-data="{field:'fetchTime', width:200,templet:'<div>{{ getDiffTime(d.fetchTime) }}</div>'}">Fetch
                Time
            </th>
            <th lay-data="{field:'fetchAddress', width:200}">Fetch Address</th>
            <th lay-data="{field:'weight', width:100}">Weight</th>
            <th lay-data="{field:'goodsType', width:120}">GoodsType</th>
            <th lay-data="{field:'statusDesc', width:100}">Status</th>
            <th lay-data="{field:'receiverInfo', width:150,templet: '<div>{{d.receiverInfo.receiverName}}</div>' }">
                Receiver
                Name
            </th>
            <th lay-data="{field:'receiverInfo', width:150,templet: '<div>{{d.receiverInfo.receiverPhone}}</div>' }">
                Receiver Phone
            </th>
            <th lay-data="{field:'receiverInfo', width:150,templet: '<div>{{d.receiverInfo.receiverAddress}}</div>' }">
                Receiver
                Address
            </th>

            <th lay-data="{field:'userdefine01', width:150}">UserDefine01</th>
            <th lay-data="{field:'userdefine02', width:150}">UserDefine02</th>
            <th lay-data="{field:'userdefine03', width:150}">UserDefine03</th>
            <th lay-data="{field:'userdefine04', width:150}">UserDefine04</th>
            <th lay-data="{field:'userdefine05', width:150}">UserDefine05</th>

            <th lay-data="{fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>
    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="detail">Detail</a>
        <shiro:hasPermission name="400042">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="allocate">Allocate</a>
        </shiro:hasPermission>
    </script>
</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use(['form', 'layer'], function () {
            var form = layui.form;
            form.render();
        })
        layui.use('laydate', function () {
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
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    var orderNo = data.orderNo;
                    layer.msg(orderNo);
                } else if (obj.event === 'allocate') {
                    var orderNo = data.orderNo;
                    var orderNos = new Array();
                    orderNos.push(orderNo)
                    toAllocatePage(orderNos);
                }
            });

        });

        $(".search").on("click", function () {
            reloadTable();
        })

        $('.batchAllocate').on('click', function () {
            var checkStatus = table.checkStatus('${id0}')
                    , data = checkStatus.data;
            if (data.length == 0) return;
            var arr = new Array();
            for (var i = 0; i < data.length; i++) {
                arr.push(data[i].orderNo);
            }
            toAllocatePage(arr);
        });

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    orderNo: $("input[name='orderNo']").val(),
                    referenceNo: $("input[name='referenceNo']").val(),
                    orderTypes: $("select[name='orderType']").val(),
                    orderStatusLimit: $("select[name='orderStatusLimit']").val(),
                    fromCreatedTime: $("#fromCreatedTime").val(),
                    toCreatedTime: $("#toCreatedTime").val(),
                }
            });
        };

        function toAllocatePage(orderNos) {

            $.ajax({
                url: "/order/doorToDoor/allocatePage.html",
                type: 'POST',
                data: {"orderNos": orderNos},
                dataType: 'text',
                success: function (data) {
                    layer.open({
                        type: 1,
                        title: "Allocate Rider",
                        area: ['600px','300px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }
    });

</script>
</body>
</html>