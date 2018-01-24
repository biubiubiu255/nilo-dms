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
            OrderNo：
            <div class="layui-inline">
                <input class="layui-input" name="orderNo" autocomplete="off">
            </div>
        </div>

        <div class="layui-col-md7">
            ArrivedTime：
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toTime" placeholder="To">
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="400031">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>
    <!-- /.box-header -->
    <div class="layui-btn-group demoTable">
        <shiro:hasPermission name="400032">
            <button class="layui-btn layui-btn-normal arrive-scan">Arrive Scan</button>
        </shiro:hasPermission>
    </div>

    <table class="layui-table"
           lay-data="{ url:'/order/arriveScan/list.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:250}">OrderNo</th>
            <th lay-data="{field:'orderType', width:100,templet: '<div>{{d.deliveryOrder.orderType}}</div>' }">
                OrderType
            </th>
            <th lay-data="{field:'referenceNo', width:100,templet: '<div>{{d.deliveryOrder.referenceNo}}</div>' }">
                ReferenceNo
            </th>
            <th lay-data="{field:'country', width:100,templet: '<div>{{d.deliveryOrder.country}}</div>' }">
                Country
            </th>
            <th lay-data="{field:'weight', width:100,templet: '<div>{{d.deliveryOrder.weight}}</div>' }">
                Weight
            </th>
            <th lay-data="{field:'goodsType', width:100,templet: '<div>{{d.deliveryOrder.goodsType}}</div>' }">
                Weight
            </th>
            <th lay-data="{field:'createdTime', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                Arrive Time
            </th>

        </tr>
        </thead>
    </table>
    <%--
        <script type="text/html" id="barDemo">
            <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="detail">Detail</a>
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="arrive">Arrive</a>
        </script>--%>
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
                elem: '#fromTime'
                , lang: 'en'
            });
            layDate.render({
                elem: '#toTime'
                , lang: 'en'
            });
        });
        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                var orderNo = data.orderNo;
                if (obj.event === 'detail') {
                    layer.msg(orderNo);
                } else if (obj.event === 'arrive') {
                    var arr = new Array();
                    arr.push(orderNo);
                    arriveScan(arr);

                }
            });


            function arriveScan(arr) {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/arriveScan/arrive.html",
                    dataType: "json",
                    data: {orderNos: arr},
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                                reloadCurrentPage();
                            });
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
            }

            $('.batchArrive').on('click', function () {
                var data = table.checkStatus('${id0}').data;
                if (data.length == 0) return;
                var arr = new Array();
                for (var i = 0; i < data.length; i++) {
                    arr.push(data[i].orderNo);
                }
                arriveScan(arr);
            });

            var reloadTable = function (item) {
                table.reload("${id0}", {
                    where: {
                        orderNo: $("input[name=orderNo]").val(),
                        fromTime: $("#fromTime").val(),
                        toTime: $("#toTime").val(),
                    }
                });
            };

            $(".search").on("click", function () {
                reloadTable();
            })

        });

        $(".arrive-scan").on("click", function () {
            var url = "/order/arriveScan/arriveScanPage.html";
            parent.addTabs({
                id: 'arrive_scan_page',
                title: 'Arrive Scan',
                close: true,
                url: url
            });
        })

    });

</script>
</body>
</html>