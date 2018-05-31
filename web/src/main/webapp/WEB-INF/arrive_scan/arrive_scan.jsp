<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("userName", session.getAttribute("userName"));
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<input type="hidden" id="scanNo" autocomplete="off" value="${scanNo}">
<div class="box-body">
    <div class="box-header with-border">
        <h3 class="box-title">Arrive Scan</h3>
    </div>
    <br>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">OrderNo:</label>
        <div class="layui-input-inline">
            <input type="text" id="orderNoScan" autocomplete="off" placeholder="Scan" class="layui-input">
        </div>

    </div>

    <div style="margin-left:120px;">
        <table class="layui-table" id="${id0}"
               lay-data="{ url:'/order/arriveScan/scanList.html', page:true,limit:10, id:'${id0}'}" lay-filter="${id0}">
            <thead>
            <tr>
                <th lay-data="{field:'orderNo', width:200}">OrderNo</th>
                <th lay-data="{field:'weight', width:130,edit:'text'}">Weight(KG)</th>
                <th lay-data="{field:'referenceNo', width:200}">ReferenceNo</th>
                <th lay-data="{field:'orderType', width:100}">OrderType</th>
                <th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
            </tr>
            </thead>
        </table>
        <div class="layui-form-item">
            <div class="layui-input-inline">
                <button class="layui-btn layui-btn-normal arrive">Submit</button>
            </div>
        </div>
        <script type="text/html" id="barDemo">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="print">Print</a>
        </script>
    </div>
</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        var table;
        layui.use('table', function () {
            table = layui.table;
            table.on('tool(${id0})', function (obj) {
                var data = obj.data;
                var orderNo = data.orderNo;
                if (obj.event === 'delete') {
                    deleteDetails(orderNo);
                    obj.del();
                }
                if (obj.event === 'print') {
                    printOrder(orderNo);
                }
            });
            //监听单元格编辑
            table.on('edit(${id0})', function (obj) {
                var number = parseFloat(obj.value);
                number = number.toFixed(2);
                $("cdsf").change()
                var value = obj.value //得到修改后的值
                        , data = obj.data //得到所在行所有键值
                if (value.length > 10){
                    layer.msg("sorry! too heavy");
                    reloadTable();
                    return ;
                }
                layer.msg("sussess");
                updateWeight(data.orderNo, value);
                setTimeout(function () {
                    reloadTable();
                }, 500)

            });

        });

        $("#orderNoScan").focus();
        $("#orderNoScan").keydown(function (event) {
            event = document.all ? window.event : event;
            if ((event.keyCode || event.which) == 13) {

                var orderNo = $("#orderNoScan").val();
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/arriveScan/scan.html",
                    dataType: "json",
                    data: {orderNo: orderNo, scanNo: '${scanNo}'},
                    success: function (data) {
                        if (data.result) {
                            $("#orderNoScan").focus();
                            $("#orderNoScan").val('');
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

        var reloadTable = function (item) {
            table.reload("${id0}", {
                where: {
                    scanNo: $("#scanNo").val(),
                }
            });
        };
        $(".arrive").on("click", function () {

            var check = true;
            $("td[data-field='weight'] div").each(function (key, value) {
                if (!isMoreThan0(this.innerHTML)) {
                    check = false;
                    layer.msg("Weight needs more than 0", {icon: 2, time: 2000});
                }
            });

            if (!check) return;

            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/order/arriveScan/arrive.html",
                dataType: "json",
                data: {scanNo: '${scanNo}'},
                success: function (data) {
                    if (data.result) {
                        layer.msg('SUCCESS', {icon: 1, time: 2000}, function () {
                            window.location.reload();
                        });
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    }
                },
                complete: function () {
                    layer.close(load);
                }
            });
        })
        function isMoreThan0(obj) {
            reg = /^\d+(\.\d+)?$/;
            if (!reg.test(obj) || obj <= 0) {
                return false;
            } else {
                return true;
            }
        }

        var updateWeight = function (orderNo, weight) {
            $.ajax({
                type: "POST",
                url: "/order/arriveScan/updateWeight.html",
                dataType: "json",
                data: {orderNo: orderNo, weight: weight, scanNo: '${scanNo}'},
                success: function (data) {
                    if (!data.result) {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    }
                },
                complete: function () {
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
        
        var printOrder = function (orderNo) {
            parent.window.open("/waybill/arriveScanPrint/" + orderNo + ".html");
        }
    });
</script>
</body>
</html>