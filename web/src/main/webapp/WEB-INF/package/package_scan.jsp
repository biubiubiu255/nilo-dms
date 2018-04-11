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
               class="layui-input">

        <div class="layui-form-item">

            <label class="layui-form-label" style="width:120px">Package By</label>
            <div class="layui-input-inline">
                <input type="text" name="packageBy" value="${sessionScope.userName}" autocomplete="off"
                       class="layui-input layui-disabled" disabled>
            </div>
            <label class="layui-form-label" style="width:120px">Next Station</label>
            <div class="layui-input-inline">
                <select name="nextNetworkId" lay-search="" lay-filter="nextNetworkId" lay-verify="required">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStation}" var="station">
                        <option value="${station.code}" optionSeq="${station.code}" type="${station.type}">${station.name}</option>
                    </c:forEach>
                </select>
                <input type="hidden" name="network_id" value="">
                <input type="hidden" name="nextStation" value="">
            </div>
            <label class="layui-form-label" style="width:120px">Weight</label>
            <div class="layui-input-inline">
                <input type="number" name="weight" value="" autocomplete="off"
                       class="layui-input" lay-verify="required">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">Length</label>
            <div class="layui-input-inline">
                <input type="number" name="length" value="" autocomplete="off"
                       class="layui-input">
            </div>
            <label class="layui-form-label" style="width:120px">Width</label>
            <div class="layui-input-inline">
                <input type="number" name="width" value="" autocomplete="off"
                       class="layui-input">
            </div>
            <label class="layui-form-label" style="width:120px">Height</label>
            <div class="layui-input-inline">
                <input type="number" name="height" value="" autocomplete="off"
                       class="layui-input">
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
            <button class="layui-btn package" value="submit">Submit</button>
            <button class="layui-btn package" value="submitShip">Submit Ship</button>
        </div>
    </div>

</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        var form, table;
        layui.use(['form', 'layer'], function () {
            form = layui.form;

            form.on('select(nextNetworkId)', function (data) {
                var seq = data.value.toString();
                $("input[name='network_id']").val(data.value);

                $("input[name='nextStation']").val($("option[optionSeq='"+seq+"']").html());
            });

        });

        layui.use('table', function () {
            table = layui.table;
            table.on('tool(${id0})', function (obj) {
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
                        orderNo: orderNo, scanNo: '${scanNo}'
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

        $('.package').on('click', function (e) {
            var param = '?tempScanNo=249907893388840960&network_id='+$("input[name='network_id']").val()+'&nextStation='+$("input[name='nextStation']").val()
            layer.open({
                type: 2,
                title: 'Edit',
                shadeClose: true,
                shade: false,
                maxmin: true, //开启最大化最小化按钮
                area: ['900px', '600px'],
                offset: ['100px', '250px'],
                content: '/waybill/send_nextStation/editPage.html' + param
            });

            return;


            var subtype = e.currentTarget.value;
            var nextStation = $("select[name='nextNetworkId']").val();
            var weight = $("input[name='weight']").val();
            if (nextStation=='') {
                layer.msg("Pls select Next Station", {icon: 2, time: 2000});
                return;
            }
            if (weight=='') {
                layer.msg("Pls input Weight", {icon: 2, time: 2000});
                return;
            }
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/order/package/addPackage.html",
                dataType: "json",
                data: $('#myForm').serialize(),
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            if(subtype=="submitShip"){
                                $.get("/waybill/send_nextStation/editPage.html?tempScanNo=${scanNo}", function (data) {
                                    var index = parent.layer.open({
                                        type: 1,
                                        content: data,
                                        area: ['900px', '600px'],
                                        offset: ['100px', '250px'],
                                        maxmin: true
                                    });
                                });
                            }
                            //location.reload();
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