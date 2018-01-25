<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ page import="com.nilo.dms.service.model.User" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("nextStation", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "next_station"));
%>
<body>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">Loading No.</label>
            <div class="layui-input-inline">
                <input type="text" id="loadingNo" value="" autocomplete="off" disabled class="layui-input">
            </div>
            <div class="layui-input-inline">
                <button class="layui-btn layui-btn-normal" lay-submit lay-filter="createSubmit">Create</button>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">Loading By</label>
            <div class="layui-input-inline">
                <input type="text" name="loadingBy" value="${sessionScope.userName}" autocomplete="off"
                       class="layui-input layui-disabled" disabled>
            </div>

            <label class="layui-form-label" style="width:120px">Next Station</label>
            <div class="layui-input-inline">
                <select name="nextStation" lay-verify="required" lay-search="" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStation}" var="station">
                        <option value="${station.code}">${station.value}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">

            <label class="layui-form-label" style="width:120px">Rider</label>
            <div class="layui-input-inline">
                <select name="rider" id="rider" lay-verify="required" lay-search=""
                        <c:if test="${ not empty loading.rider}">disabled</c:if> style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${riderList}" var="rider">
                        <option value="${rider.userId}"> ${rider.staffId}</option>
                    </c:forEach>
                </select></div>

            <label class="layui-form-label" style="width:120px">TruckNo</label>
            <div class="layui-input-inline">
                <input type="text" name="truckNo" value="" autocomplete="off" class="layui-input">
            </div>

            <label class="layui-form-label" style="width:120px">Quantity</label>
            <div class="layui-input-inline">
                <input type="text" name="quantity" value="${loading.detailsList.size()}" autocomplete="off"
                       class="layui-input layui-disabled"
                       disabled>
            </div>
        </div>
    </form>
    <hr>

    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">OrderNo:</label>
        <div class="layui-input-inline">
            <input type="text" id="orderNo" autocomplete="off" placeholder="Scan" class="layui-input layui-disabled"
                   disabled>
        </div>
    </div>

    <div style="margin-left:120px;">

        <table class="layui-table"
               lay-data="{ url:'/order/loading/loadingDetails.html',where:{loadingNo:'${loading.loadingNo}'}, page:true,limit:10, id:'${id0}'}"
               lay-filter="demo" style="margin-left:120px;">
            <thead>
            <tr>
                <th lay-data="{fixed: 'left',field:'num', width:100}">ID</th>
                <th lay-data="{field:'orderNo', width:250}">OrderNo</th>
                <th lay-data="{field:'orderType', width:100,templet: '<div>{{d.deliveryOrder.orderType}}</div>'}">
                    OrderTyp
                </th>
                <th lay-data="{field:'weight', width:200,templet: '<div>{{d.deliveryOrder.weight}}</div>'}">Weight</th>
                <th lay-data="{title:'opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
            </tr>
            </thead>
        </table>
    </div>
    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
    </script>

    <div class="layui-form-item">
        <div class="layui-input-block" style="margin-left:120px;">
            <button class="layui-btn ship">Ship</button>
            <button class="layui-btn layui-btn-normal print"><i class="fa fa-print" aria-hidden="true"> </i>Print
            </button>
        </div>

    </div>

</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        var form, table;
        layui.use(['form', 'layer'], function () {
            form = layui.form;
            form.on('submit(createSubmit)', function (data) {

                var loadingNo = $("#loadingNo").val();
                if (loadingNo != '') {
                    return false;
                }
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/loading/addLoading.html",
                    dataType: "json",
                    data: $("#myForm").serialize(),
                    success: function (data) {
                        if (data.result) {
                            $("#orderNo").removeClass("layui-disabled");
                            $("#orderNo").attr("disabled", false);
                            $("#orderNo").focus();
                            $("#loadingNo").val(data.data);
                            $("#loadingNo").attr("disabled", true);
                            layer.closeAll();
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
                return false;
            });

        });

        layui.use('table', function () {
            table = layui.table;
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                var orderNo = data.orderNo;
                if (obj.event === 'delete') {
                    deleteLoadingDetails(orderNo);
                }
            });
        });

        $("#orderNo").keydown(function (event) {
            event = document.all ? window.event : event;
            if ((event.keyCode || event.which) == 13) {
                var loadingNo = $("#loadingNo").val();
                if (loadingNo == '') {
                    return false;
                }
                var orderNo = $("#orderNo").val();
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/loading/loadingScan.html",
                    dataType: "json",
                    data: {
                        orderNo: orderNo,
                        loadingNo: loadingNo,
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 1000}, function () {
                                $("#orderNo").focus();
                                $("#orderNo").val('');
                                //刷新数据
                                reloadTable();
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

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    loadingNo: $("#loadingNo").val(),
                }
            });
        };

        function deleteLoadingDetails(orderNo) {

            var loadingNo = $("#loadingNo").val();
            if (loadingNo == '') {
                return false;
            }
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/order/loading/deleteDetails.html",
                dataType: "json",
                data: {
                    loadingNo: loadingNo,
                    orderNo: orderNo,
                },
                success: function (data) {
                    if (data.result) {
                        $("#orderNo").focus();
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

        $('.ship').on('click', function () {

            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/order/loading/ship.html",
                dataType: "json",
                data: {
                    loadingNo: $("#loadingNo").val(),
                },
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