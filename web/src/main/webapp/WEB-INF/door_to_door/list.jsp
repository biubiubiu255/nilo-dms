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
    <div class="layui-row">
        <div class="layui-col-md5 layui-col-lg3">
            <shiro:hasPermission name="400041">
                <button class="layui-btn layui-btn-normal batch">Batch</button>
            </shiro:hasPermission>

            <shiro:hasPermission name="400046">
                <button class="layui-btn layui-btn-normal print">Print</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="400047">
                <button class="layui-btn layui-btn-normal printAgain">Print No Limit</button>
            </shiro:hasPermission>
            <button class="layui-btn btn-search">Search
            </button>
        </div>
    </div>
    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content ">
                <div class="layui-form layui-row">
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OrderNo:</label>
                        <div class="layui-inline">
                            <input class="layui-input" name="orderNo" autocomplete="off">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">ReferenceNo:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="referenceNo" autocomplete="off" class="layui-input">
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

                    <div class="layui-col-md1 layui-col-lg1">
                        <shiro:hasPermission name="400043">
                            <button class="layui-btn layui-btn-normal search">Search</button>
                        </shiro:hasPermission>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <hr>

    <table class="layui-table" lay-data="{ url:'/order/doorToDoor/list.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{checkbox:true, fixed: true}"></th>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}">OrderNo</th>
            <th lay-data="{field:'referenceNo', width:200}">ReferenceNo</th>
            <th lay-data="{field:'orderTime', width:170, templet:'<div>{{ formatDate(d.orderTime) }}</div>'}">
                OrderTime
            </th>
            <th lay-data="{field:'statusDesc', width:100}">Status</th>
            <th lay-data="{field:'allocatedRider', width:100}">Rider</th>
            <th lay-data="{field:'printTimes', width:100,templet: '<div>{{d.printTimes}}</div>'}">PrintTimes</th>
            <th lay-data="{field:'senderName', width:100,templet: '<div>{{d.senderInfo.senderName}}</div>'}">Name</th>
            <th lay-data="{field:'senderPhone', width:150,templet: '<div>{{d.senderInfo.senderPhone}}</div>'}">Phone
            </th>
            <th lay-data="{field:'senderAddress', width:200,templet: '<div>{{d.senderInfo.senderAddress}}</div>'}">Fetch
                Address
            </th>
            <th lay-data="{field:'weight', width:100}">Weight</th>
            <th lay-data="{field:'goodsType', width:120}">GoodsType</th>
            <th lay-data="{field:'createdTime', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{field:'updatedTime', width:170, templet:'<div>{{ formatDate(d.updatedTime) }}</div>'}">
                UpdatedTime
            </th>
            <th lay-data="{fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>
    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="400042">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="allocate">Allocate</a>
        </shiro:hasPermission>
    </script>
</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        layui.use(['form', 'layer', 'element', 'laydate'], function () {
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
        $(".btn-search").on("click", function () {
            $(".layui-colla-content").toggleClass("layui-show");
            $(".btn-search").toggleClass("layui-btn-warm");
        })
        $(".search").on("click", function () {
            reloadTable();
        })

        $('.batch').on('click', function () {
            var checkStatus = table.checkStatus('${id0}')
                    , data = checkStatus.data;
            if (data.length == 0) {
                layer.msg("Pls select...");
                return;
            }
            ;
            var arr = new Array();
            for (var i = 0; i < data.length; i++) {
                arr.push(data[i].orderNo);
            }
            toAllocatePage(arr);
        });

        $('.print').on('click', function () {
            var checkStatus = table.checkStatus('${id0}')
                    , data = checkStatus.data;
            if (data.length == 0) {
                layer.msg("Pls select...");
                return;
            }
            ;
            var arr = new Array();
            for (var i = 0; i < data.length; i++) {
                arr.push(data[i].orderNo);
            }
            parent.window.open("/order/doorToDoor/print.html?orderNos="+arr);
        });

        $('.printAgain').on('click', function () {
            var checkStatus = table.checkStatus('${id0}')
                    , data = checkStatus.data;
            if (data.length == 0) {
                layer.msg("Pls select...");
                return;
            }
            var arr = new Array();
            for (var i = 0; i < data.length; i++) {
                arr.push(data[i].orderNo);
            }
            parent.window.open("/order/doorToDoor/printAgain.html?orderNos="+arr);
        });

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    orderNo: $("input[name='orderNo']").val(),
                    referenceNo: $("input[name='referenceNo']").val(),
                    orderTypes: $("select[name='orderType']").val(),
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
                    parent.layer.open({
                        type: 1,
                        title: "Allocate PdaRider",
                        area: ['800px', '600px'],
                        offset: ['100px', '250px'],
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