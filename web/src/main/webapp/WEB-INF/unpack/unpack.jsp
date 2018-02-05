<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>

<div class="box-body">
    <input type="hidden" name="scanNo" value="${scanNo}" autocomplete="off"
           class="layui-input">
    <div class="layui-form-item">

        <label class="layui-form-label" style="width:120px">Unpack By</label>
        <div class="layui-input-inline">
            <input type="text" name="packageBy" value="${sessionScope.userName}" autocomplete="off"
                   class="layui-input layui-disabled" disabled>
        </div>
    </div>
    <hr>

    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">PackageNo:</label>
        <div class="layui-input-inline">
            <input type="text" id="packageNo" autocomplete="off" placeholder="Scan PackageNo." class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">OrderNo:</label>
        <div class="layui-input-inline">
            <input type="text" id="orderNo" autocomplete="off" placeholder="Scan OrderNo." class="layui-input">
        </div>
    </div>
    <div style="margin-left:120px;">
        <table class="layui-table" id="${id0}"
               lay-data="{ url:'/order/unpack/scanList.html', page:true,limit:10, id:'${id0}'}" lay-filter="${id0}">
            <thead>
            <tr>
                <th lay-data="{field:'arrived',width:100,templet: '#arrivedTpl'}">Arrived</th>
                <th lay-data="{field:'orderNo', width:200}">OrderNo</th>
                <th lay-data="{field:'weight', width:100}">Weight</th>
                <th lay-data="{field:'referenceNo', width:200}">ReferenceNo</th>
                <th lay-data="{field:'orderType', width:100}">OrderType</th>
            </tr>
            </thead>
        </table>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block" style="margin-left:120px;">
            <button class="layui-btn arrive">Submit</button>
        </div>
    </div>
    <script type="text/html" id="arrivedTpl">
        {{#  if(d.arrived){ }}
        <i class="layui-icon" style="font-size: 30px; color: #1E9FFF;">&#xe618;</i>
        {{#  } else { }}
        {{  }}
        {{#  } }}
    </script>
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
                scan($("#orderNo").val(), "order");
            }
        });

        $("#packageNo").keydown(function (event) {
            event = document.all ? window.event : event;
            if ((event.keyCode || event.which) == 13) {
                scan($("#packageNo").val(), "package");
            }
        });

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    scanNo: '${scanNo}',
                    packageNo: $("#packageNo").val()
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

        var scan = function (orderNo, type) {
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/order/unpack/scan.html",
                dataType: "json",
                data: {
                    orderNo: orderNo, scanNo: '${scanNo}', type: type
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
        $(".arrive").on("click", function () {

            var data = table.cache["${id0}"];
            var notArrived = false;
            for (var i = 0; i < data.length; i++) {
                if (!data[i].arrived) {
                    notArrived = true;
                    layer.confirm('Something is not arrived,Confirm Submit?', {
                                btn: ['OK', 'Cancel']
                                //按钮
                            }, function () {
                                arriveSubmit()
                            }
                    );
                }
            }
            if (!notArrived) {
                arriveSubmit();
            }
        })

        function arriveSubmit() {
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
        }
    });
</script>
</body>
</html>