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
            <input type="text" name="handleName" value="${pack.nextStation}" autocomplete="off"
                   class="layui-input layui-disabled" disabled>
        </div>
    </div>
    <form id="myForm" class="layui-form" action="">
        <input type="hidden" name="${pack.handleNo}">

        <div class="layui-form-item">
                <label class="layui-form-label" style="width:120px">Driver</label>
            <div class="layui-input-inline">
                <input type="text" name="driver" value="${pack.driver}" autocomplete="off"
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

    <div class="layui-form-item">
        <div class="layui-input-block shipDiv" style="margin-left:120px;">
            <button class="layui-btn layui-btn-danger commit" value="0">Ship</button>
        </div>
    </div>

<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
</script>

</div>
<script type="text/javascript">
    $(function () {
        //初始化开始
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
            tableData = tableData.data;

            //alert(tableData.length);
            table1 = layui.table;
            view();
            table1.on('tool', function (obj) {
                var data = obj.data;
                var orderNo = data.orderNo;
                if (obj.event === 'delete') {
                    if(isModify()===false){
                        layer.msg('Finished loading');
                        return ;
                    }
                    for(var i=0;i<tableData.length;i++){
                        if (tableData.splice(i, 1));
                        break;
                    }
                    view();
                }
            });
        });
        //int end

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

        function view() {

            $("input[name='quantity']").val(tableData.length);

            //方法级渲染
            table1.render({
                elem: '#${id2}'
                , data: tableData
                , cols: [[
                    {field: 'id', title: 'ID', width: 80}
                    , {field: 'orderNo', title: 'OrderNo', width: 190}
                    , {field: 'weight' , title: 'Weight' , width: 90}
                    , {title: 'CreateTime', width: 200, templet: '<div>{{formatDate(d.createdTime)}}</div>'}
                    //, {field: '', title: 'opt', width:80, toolbar: '#barDemo', fixed: 'right', align:'center'}
                ]]
                , id: '${id2}'
                , width: 565
            });
        }

        //Ship
        $('.commit').on('click', function (e) {
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/waybill/send_nextStation/updateStatus.html",
                dataType: "json",
                data: {
                    handleNo: "${pack.handleNo}",
                    status: 1
                },
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            //parent.layer.close(index);
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

        function initShow() {
            if('${pack.status}'=='1'){
                $('.commit').hide();
            }
        }

        //根据发车单状态，返回是否可以修改
        function isModify(){
            var bool = parseInt(${pack.status})==0 ? true : false;
            return bool;
        }
    });
</script>
