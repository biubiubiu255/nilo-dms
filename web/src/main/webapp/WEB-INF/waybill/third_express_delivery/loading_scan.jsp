<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>


<body>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">

        <div class="layui-form-item">
            <label class="layui-form-label" style="width:120px">Carrier</label>
            <div class="layui-input-inline">
                <select name="carrier" lay-search="" lay-filter="carrier">
                    <option value="">choose or search....</option>
                    <c:forEach items="${expressList}" var="carrier">
                        <option value="${carrier.expressCode}">${carrier.expressName}</option>
                    </c:forEach>
                </select>
            </div>
            <label class="layui-form-label" style="width:120px">Drider</label>
            <div class="layui-input-inline">
                <select name="rider" id="rider" lay-search="">
                    <option value="">choose or search....</option>
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
        <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
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

        var tableData = new Array();
        view();
        var form, table;
        layui.use('form', function () {
            form = layui.form;
            form.on('select(carrier)', function (data) {
                getThirdDriver(data.value);
            });
        });

        var packList = new Array();

        //表单监控
        layui.use('table', function () {
            table = layui.table;
            table.on('tool(filterLab)', function (obj) {
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                var orderNo = data.orderNo;

                if (obj.event === 'delete') {
                    //deleteLoadingDetails(orderNo);
                    //alert(data.id);
                    tableData.splice(data.index - 1, 1);
                    for (var i = 0; i < tableData.length; i++) {
                        tableData[i].index = i + 1;
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
        function commit(type) {
            var printed = false;  //是否需要打印
            var status = 0;   //传输到后台的状态，0仅保存、1发运
            var carrier = $("select[name='carrier']").val();
            type = parseInt(type);
            switch (type) {
                case 0:
                    status = 0;
                    printed = false;
                    break;
                case 1:
                    status = 1;
                    printed = true;
                    break;
                default:
                    return;
            }

            var rider = $("select[name='rider']").val();
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
                url: "/waybill/third_express_delivery/addLoading.html",
                dataType: "json",
                data: {
                    smallPack: smallPack,
                    rider: rider,
                    status: status,
                    thirdExpressCode: carrier
                },
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            if (printed == true) {
                                //这里需要返回一个大包号，才能打印，因为是在后台创建时生成订单号
                                parent.window.open("/waybill/third_express_delivery/print.html?loadingNo=" + data.data.handleNo);
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
            //console.log(tableData);
            layui.use('table', function () {
                var table = layui.table;
                console.log("条目：" + tableData.length);
                //第一个实例
                table.render({
                    page: false //开启分页
                    , elem: '#${id0}'
                    , height: 315
                    , data: tableData
                    , width: 868
                    , even: true
                    , text: {
                        none: 'Please start scanning the order'
                    }
                    , page: {
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
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
                        //如果是异步请求数据方式，res即为你接口返回的信息。
                        //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                        console.log(res);
                    }
                });

            });
        }

        function getThirdDriver(code) {
            $.ajax({
                type: "POST",
                url: "/waybill/third_express_delivery/getThirdDriver.html",
                dataType: "json",
                data: {code: code},
                success: function (data) {
                    if (data.result) {
                        $("#rider").empty();
                        $("#rider").prepend("<option value='0'>choose or search....</option>");
                        var driver = data.data;
                        console.log(driver)
                        for (var i = 0; i < driver.length; i++) {
                            $("#rider").append("<option value='" + driver[i].name + "'>" + driver[i].name + "</option>");
                        }
                        form.render();
                    }
                }
            });
        }
    });


</script>
</body>
</html>