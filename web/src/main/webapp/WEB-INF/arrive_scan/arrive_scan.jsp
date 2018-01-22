<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>

<div class="box-body">
    <div class="box-header with-border">
        <h3 class="box-title">Arrive Scan</h3>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">Arrive By:</label>
        <div class="layui-input-inline">
            <label class="layui-form-label" style="width:120px">Ronny</label>
        </div>

    </div>

    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">OrderNo:</label>
        <div class="layui-input-inline">
            <input type="text" id="orderNoScan" autocomplete="off" placeholder="Scan" class="layui-input">
        </div>
        <div class="layui-input-inline">
            <button class="layui-btn layui-btn-normal print" ><i class="fa fa-print" aria-hidden="true"> </i>Print</button>
        </div>
    </div>

    <div style="margin-left:120px; width: 400px">

        <table class="layui-table" id="${id0}">
            <thead>
            <tr>
                <th lay-data="{fixed: 'left',field:'num', width:100}">ID</th>
                <th lay-data="{field:'orderNo', width:250}">OrderNo</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
    </script>

</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        var table;
        layui.use('table', function () {
            table = layui.table;
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                var orderNo = data.orderNo;
                if (obj.event === 'delete') {
                }
            });
        });

        var index = 0;
        $("#orderNoScan").focus();
        $("#orderNoScan").keydown(function (event) {
            event = document.all ? window.event : event;
            if ((event.keyCode || event.which) == 13) {

                var orderNo = $("#orderNoScan").val();
                var arr = new Array();
                arr.push(orderNo);

                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/arriveScan/arrive.html",
                    dataType: "json",
                    data: {
                        orderNos: arr
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 2000});
                            $("#orderNoScan").focus();
                            $("#orderNoScan").val('');
                            index = index + 1;
                            //添加数据
                            addList(orderNo, index);
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


        function addList(orderNo, index) {
            $("#${id0} tbody").prepend("'<tr><td>" + index + "</td>" +
                    "<td>" + orderNo + "</td>" +
                    "</tr>'");
        }
    });
</script>
</body>
</html>