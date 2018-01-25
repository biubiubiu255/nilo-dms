<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("id2", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("id1", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("nextStation", SystemCodeUtil.getSystemCodeList((String)session.getAttribute("merchantId"),"next_station"));
%>

<div class="box-body">

    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">Loading No.</label>
        <div class="layui-input-inline">
            <input type="text" id="loadingNo" value="${loading.loadingNo}" autocomplete="off" disabled
                   class="layui-input layui-disabled">
        </div>
    </div>
    <form id="myForm" class="layui-form" action="">
        <input type="hidden" name="${loading.loadingNo}">
        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">Loading By</label>
            <div class="layui-input-inline">
                <input type="text" name="loadingBy" value="${loading.loadingName}" autocomplete="off"
                       class="layui-input layui-disabled" disabled>
            </div>

            <label class="layui-form-label" style="width:120px">Next Station</label>
            <div class="layui-input-inline">
                <select name="nextStation" lay-verify="required" lay-search="" disabled style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStation}" var="station">
                        <option value="${station.code}"
                                <c:if test="${station.code == loading.nextStation}">selected</c:if>>${station.value}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">

            <label class="layui-form-label" style="width:120px">Rider</label>
            <div class="layui-input-inline">
                <select name="rider" id="rider" lay-verify="required" lay-search="" disabled style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${riderList}" var="rider">
                        <option value="${rider.userId}"
                                <c:if test="${rider.userId == loading.rider}">selected</c:if>> ${rider.staffId}</option>
                    </c:forEach>
                </select></div>

            <label class="layui-form-label" style="width:120px">TruckNo</label>
            <div class="layui-input-inline">
                <input type="text" name="truckNo" value="${loading.truckNo}" autocomplete="off" class="layui-input">
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
        <table class="layui-hide" id="${id2}" style="width:600px;"></table>
    </div>
    <script type="text/html" id="${id1}">
        <c:if test="${loading.statusDesc != 'Ship'}">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
        </c:if>
    </script>

    <div class="layui-form-item">
        <div class="layui-input-block" style="margin-left:120px;">
            <c:if test="${loading.statusDesc != 'Ship'}">
                <button class="layui-btn ship">Ship</button>
            </c:if>
            <button class="layui-btn layui-btn-normal print">Print</button>
        </div>
    </div>

</div>
<script type="text/javascript">
    $(function () {

        if ($("#loadingNo").val() != '') {
            $("#orderNo").removeClass("layui-disabled");
            $("#orderNo").attr("disabled", false);
            $("#orderNo").focus();
        }

        var form, table1;
        layui.use(['form', 'table'], function () {
            form = layui.form;
            form.render();

            table1 = layui.table;
            //方法级渲染
            table1.render({
                elem: '#${id2}'
                , url: '/order/loading/loadingDetails.html?loadingNo=${loading.loadingNo}'
                , cols: [[
                    {field: 'num', title: 'ID', width: 80,}
                    , {field: 'orderNo', title: 'OrderNo', width: 250}
                    , {
                        field: 'orderType',
                        title: 'OrderTyp',
                        width: 200,
                        templet: '<div>{{d.deliveryOrder.orderType}}</div>'
                    }
                    , {field: 'weight', title: 'Weight', width: 200, templet: '<div>{{d.deliveryOrder.weight}}</div>'}
                    , {title: "Operation", width: 100, align: 'center', toolbar: '#${id1}'}
                ]]
                , id: '${id2}'
            });

            table1.on('tool', function (obj) {
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
                                reloadTable1();
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

        function reloadTable1(item) {
            table1.reload("${id2}");
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
                        reloadTable1();
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
        
        $('.print').on('click', function () {
        	window.open("/order/loading/print.html?loadingNo=" + $("#loadingNo").val());
        });
    });
</script>
