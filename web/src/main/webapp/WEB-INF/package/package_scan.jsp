<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <input type="hidden" name="scanNo" value="${scanNo}" autocomplete="off"
               class="layui-input" >

        <div class="layui-form-item">

            <label class="layui-form-label" style="width:120px">Package By</label>
            <div class="layui-input-inline">
                <input type="text" name="packageBy" value="${sessionScope.userName}" autocomplete="off"
                       class="layui-input layui-disabled" disabled>
            </div>
            <label class="layui-form-label" style="width:120px">Next Station</label>
            <div class="layui-input-inline">
                <select name="nextNetworkId" lay-search="" lay-filter="nextNetworkId">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStation}" var="station">
                        <option value="${station.code}" type="${station.type}">${station.name}</option>
                    </c:forEach>
                </select>
            </div>
            <label class="layui-form-label" style="width:120px">Weight</label>
            <div class="layui-input-inline">
                <input type="text" name="weight" value="" autocomplete="off"
                       class="layui-input" >
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">Length</label>
            <div class="layui-input-inline">
                <input type="text" name="length" value="" autocomplete="off"
                       class="layui-input" >
            </div>
            <label class="layui-form-label" style="width:120px">Width</label>
            <div class="layui-input-inline">
                <input type="text" name="width" value="" autocomplete="off"
                       class="layui-input" >
            </div>
            <label class="layui-form-label" style="width:120px">Height</label>
            <div class="layui-input-inline">
                <input type="text" name="high" value="" autocomplete="off"
                       class="layui-input" >
            </div>
        </div>

    </form>
    <hr>

    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">OrderNo:</label>
        <div class="layui-input-inline">
            <input type="text" id="orderNo" autocomplete="off" placeholder="Scan" class="layui-input">
        </div>
    </div>

    <div style="margin-left:120px;">
        <table class="layui-table" id="${id0}"
               lay-data="{ url:'/order/arriveScan/scanList.html', page:true,limit:10, id:'${id0}'}" lay-filter="${id0}">
            <thead>
            <tr>
                <th lay-data="{field:'orderNo', width:200}">OrderNo</th>
                <th lay-data="{field:'weight', width:100}">Weight</th>
                <th lay-data="{field:'referenceNo', width:200}">ReferenceNo</th>
                <th lay-data="{field:'orderType', width:100}">OrderType</th>
                <th lay-data="{field:'country', width:100}">Country</th>
                <th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
            </tr>
            </thead>
        </table>
    </div>

    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
    </script>

    <div class="layui-form-item">
        <div class="layui-input-block" style="margin-left:120px;">
            <button class="layui-btn package">Submit</button>
        </div>
    </div>

</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        var form, table;
        layui.use(['form', 'layer'], function () {
            form = layui.form;
        });

        layui.use('table', function () {
            table = layui.table;
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                var orderNo = data.orderNo;
                if (obj.event === 'delete') {
                    deleteDetails(orderNo);
                    obj.del();
                }
            });
        });

        $("#orderNo").keydown(function (event) {
            event = document.all ? window.event : event;
            if ((event.keyCode || event.which) == 13) {

                var orderNo = $("#orderNo").val();
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/package/packageScan.html",
                    dataType: "json",
                    data: {
                        orderNo: orderNo,scanNo:'${scanNo}'
                    },
                    success: function (data) {
                        if (data.result) {
                            $("#orderNo").focus();
                            $("#orderNo").val('');
                            //刷新数据
                            reloadTable();
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
            }
        });

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    scanNo: '${scanNo}',
                }
            });
        };

        var deleteDetails = function (orderNo) {
            $.ajax({
                type: "POST",
                url: "/order/arriveScan/deleteDetails.html",
                dataType: "json",
                data: {orderNo: orderNo, scanNo: '${scanNo}'},
                complete: function () {
                }
            });
        };

        $('.package').on('click', function () {

            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/order/package/addPackage.html",
                dataType: "json",
                data: $('#myForm').serialize(),
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            location.reload();
                        });
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    }
                },
                complete: function () {
                    layer.close(load);
                }
            });

        });

    });
</script>
</body>
</html>