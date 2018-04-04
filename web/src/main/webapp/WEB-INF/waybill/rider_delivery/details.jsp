<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("id2", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("id1", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("nextStation", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "next_station"));
%>

<div class="box-body">

    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">Loading No.</label>
        <div class="layui-input-inline">
            <input type="text" id="loadingNo" value="${pack.handleNo}" autocomplete="off" disabled
                   class="layui-input layui-disabled">
        </div>
        <label class="layui-form-label" style="width:120px">Loading By</label>
        <div class="layui-input-inline">
            <input type="text" name="handleByName" value="${pack.handleByName}" autocomplete="off"
                   class="layui-input layui-disabled" disabled>
        </div>
    </div>
    <form id="myForm" class="layui-form" action="">
        <input type="hidden" name="${pack.handleNo}">


        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">Rider</label>
            <div class="layui-input-inline">
                <input type="text" name="quantity" value="${pack.rider}" autocomplete="off"
                       class="layui-input layui-disabled"
                       disabled>
            </div>

            <label class="layui-form-label" style="width:120px">Quantity</label>
            <div class="layui-input-inline">
                <input type="text" name="quantity" value="" autocomplete="off"
                       class="layui-input layui-disabled"
                       disabled>
            </div>
        </div>
    </form>
    <hr>


    <div style="margin-left:120px;">
        <table class="layui-hide" id="${id2}" style="width:600px;"></table>
    </div>

<%--    <div class="layui-form-item">
        <div class="layui-input-block" style="margin-left:120px;">
            <c:if test="${loading.statusDesc != 'Ship'}">
                <button class="layui-btn ship">Ship</button>
            </c:if>
            <button class="layui-btn layui-btn-normal print">Print</button>
        </div>
    </div>--%>

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

            tableData = JSON.parse('${smallsJson}');
            $("input[name='quantity']").val(tableData.data.length);

            //alert(tableData.length);

            table1 = layui.table;
            //方法级渲染
            table1.render({
                elem: '#${id2}'
                , data: tableData.data
                , cols: [[
                    {field: 'id', title: 'ID', width: 80}
                    , {field: 'orderNo', title: 'OrderNo', width: 250}
                    , {field: 'weight' , title: 'Weight' , width: 130}
                    , {field: 'weight', title: 'Weight', width: 200, templet: '<div>{{formatDate(d.created_time)}}</div>'}
                ]]
                , id: '${id2}'
                , width: 735
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
