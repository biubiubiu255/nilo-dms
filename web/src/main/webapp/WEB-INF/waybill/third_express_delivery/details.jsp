<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">
    <%--第一排--%>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width:120px">LoadingNo</label>
        <div class="layui-input-inline">
            <input type="text" name="loadingBy" value="${pack.handleNo}" autocomplete="off"
                   class="layui-input layui-disabled" disabled>
        </div>
    </div>
    <form id="myForm" class="layui-form" action="">

        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">Carrier</label>
            <div class="layui-input-inline">
                <select name="carrier" lay-search="" lay-filter="carrier">
                    <option value="">choose or search....</option>
                    <c:forEach items="${expressList}" var="carrier">
                        <option value="${carrier.expressCode}" <c:if test="${pack.thirdExpressCode == carrier.expressCode}">selected </c:if> >${carrier.expressName}</option>
                    </c:forEach>
                </select>
            </div>
            <label class="layui-form-label" style="width:120px">Rider</label>
            <div class="layui-input-inline">
                <select name="rider" id="rider" lay-search="">
                    <option value="">choose or search....</option>
                    <c:forEach items="${driverList}" var="driver">
                        <option value="${driver.driverName}"
                                <c:if test="${pack.driver == driver.driverName}">selected</c:if>>${driver.driverName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">OrderNo:</label>
            <div class="layui-input-inline">
                <input type="text" id="orderNo" autocomplete="off" placeholder="Scan" class="layui-input">
            </div>
        </div>
    </form>
    <hr>

    <div style="margin-left:120px;">
        <table id='${id0}' lay-filter="filterLab"></table>
    </div>


    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-danger layui-btn-mini btn-delete" lay-event="delete">Delete</a>
    </script>


    <div class="layui-form-item">
        <div class="layui-input-block shipDiv" style="margin-left:120px;">
            <button class="layui-btn commit" value="0">Save</button>
            <button class="layui-btn commit" value="1">Ship</button>
        </div>
    </div>

</div>
<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        var tableData = JSON.parse('${list}');
        tableData = tableData.data;
        for (var i = 0; i < tableData.length; i++) {
            tableData[i].index = i + 1;
        }
        view();
        initShow();

        var form, table;

        //表单监控
        layui.use(['table', 'form'], function () {
            form = layui.form;
            table = layui.table;
            table.on('tool(filterLab)', function (obj) {
                var data = obj.data; //获得当前行数据

                if (obj.event === 'delete') {
                    if (isModify() === false) {
                        layer.msg('Finished loading');
                        return;
                    }
                    for (var i = 0; i < tableData.length; i++) {
                        if (tableData.splice(i, 1));
                        break;
                    }
                    view();
                }
            });
        });

        //小包扫描
        $("#orderNo").keydown(function (event) {
            event = document.all ? window.event : event;
            if ((event.keyCode || event.which) == 13) {
                var loadingNo = $("#loadingNo").val();
                if (loadingNo == '') {
                    return false;
                }
                var orderNo = $("#orderNo").val();
                if (loadingNo == '') {
                    return false;
                }
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/waybill/third_express_delivery/scanSmallPack.html",
                    dataType: "json",
                    data: {
                        orderNo: orderNo,
                    },
                    success: function (data) {
                        if (data.result) {
                            $("#orderNo").focus();
                            $("#orderNo").val('');
                            var res = JSON.parse(data.data).data
                            for (var i = 0; i < tableData.length; i++) {
                                if (res.orderNo == tableData[i].orderNo) {
                                    layer.msg("Order already exists", {icon: 2, time: 2000});
                                    return;
                                }
                            }
                            res.index = tableData.length + 1;
                            tableData.push(res);
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                        view();
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
            }
        });

        //表单提交
        $('.commit').on('click', function (e) {
            //获取点击btn的value值
            var type = e.currentTarget.value;
            commit(type);
        });

        //提交数据
        function commit(isPrint) {
            var printed = false;  //是否需要打印
            var saveStatus = 0;   //传输到后台的状态，0仅保存、1发运
            isPrint = parseInt(isPrint);

            var rider = $("input[name='rider']").first().val();
            if (rider == "") {
                layer.msg("Please select the Rider", {icon: 2, time: 2000});
                return;
            }
            var smallPack = "";
            for (var i = 0; i < tableData.length; i++) {
                smallPack += tableData[i].orderNo + ",";
            }
            smallPack = smallPack.substring(0, smallPack.length - 1);
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/waybill/third_express_delivery/edit.html",
                dataType: "json",
                data: {
                    smallPack: smallPack,
                    saveStatus: saveStatus,
                    handleNo: '${riderDelivery.handleNo}',
                    rider: $("input[name='rider']").val()
                },
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            parent.layer.closeAll();
                            if (isPrint == 1) {
                                //这里需要返回一个大包号，才能打印，因为是在后台创建时生成订单号
                                parent.window.open("/waybill/rider_delivery/print.html?loadingNo=" + data.data.handleNo);
                                location.reload();
                            }
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

        //视图渲染
        function view() {

            $("input[name='quantity']").val(tableData.length);

            layui.use('table', function () {
                var table = layui.table;
                console.log("条目：" + tableData.length);
                //第一个实例
                table.render({
                    elem: '#${id0}',
                    height: 400
                    , data: tableData
                    , page: false //开启分页
                    , width: 737
                    , even: true
                    , text: {
                        none: 'Please start scanning the order'
                    }
                    , limit: 3000
                    , cellMinWidth: 80

                    , cols: [[ //表头 //layui-btn layui-btn-primary layui-btn-mini
                        {
                            title: 'id',
                            align: 'center',
                            width: 80,
                            sort: true,
                            fixed: 'left',
                            templet: '<div>{{d.index }}</div>'
                        }
                        , {field: 'orderNo', title: 'OrderNo', width: 160, align: 'center'}
                        , {field: 'orderType', title: 'OrderType', width: 150, align: 'center'}
                        , {field: 'referenceNo', title: 'ReferenceNo', width: 130, align: 'center'}
                        , {field: 'weight', title: 'Weight(KG)', width: 130, align: 'center'}
                        , {field: '', title: 'opt', width: 80, toolbar: '#barDemo', fixed: 'right', align: 'center'}
                    ]]

                    , done: function (res, curr, count) {

                    }
                });

            });
        }

        //根据isModify的值，来决定是否显示和不显示储存按钮
        function initShow() {
            if (isModify() === false) {
                $('.commit').hide();
            }
        }

        //根据发车单状态，返回是否可以修改
        function isModify() {
            var bool = parseInt(${riderDelivery.status}) == 0 ? true : false;
            return bool;
        }

    });


</script>
