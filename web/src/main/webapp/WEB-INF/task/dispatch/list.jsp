<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">OrderNo:</label>
            <div class="layui-inline">
                <input class="layui-input" name="orderNo" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Status:</label>
            <div class="layui-form-item layui-inline">
                <select lay-filter="taskStatus" multiple name="taskStatus">
                    <option value="">Pls select Status...</option>
                    <option value=0>Create</option>
                    <option value=10>Process</option>
                    <option value=20>Complete</option>
                </select>
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>
    <div class="layui-row">
        <div class="layui-col-md3">
            <button class="layui-btn layui-btn-normal print"><i class="fa fa-print" aria-hidden="true"> </i>Print
            </button>
            <button type="button" class="layui-btn" id="importSignData">
                <i class="layui-icon">&#xe67c;</i>Import
            </button>
        </div>
    </div>

    <table class="layui-table" lay-data="{ url:'/task/dispatch/list.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{checkbox:true, fixed: true}"></th>
            <th lay-data="{fixed:'left',field:'orderNo', width:250}">OrderNo</th>
            <th lay-data="{fixed:'left',field:'referenceNo', width:200,templet: '<div>{{d.deliveryOrder.referenceNo}}</div>'}">
                ReferenceNo
            </th>
            <th lay-data="{field:'statusDesc', width:150}">Task Status</th>
            <th lay-data="{field:'statusDesc', width:150,templet: '<div>{{d.deliveryOrder.statusDesc}}</div>'}">Order
                Status
            </th>
            <th lay-data="{field:'country', width:100,templet: '<div>{{d.deliveryOrder.country}}</div>'}">Country</th>
            <th lay-data="{field:'weight', width:100,templet: '<div>{{d.deliveryOrder.weight}}</div>'}">Weight</th>
            <th lay-data="{field:'goodsType', width:120,templet: '<div>{{d.deliveryOrder.goodsType}}</div>'}">
                GoodsType
            </th>
            <th lay-data="{field:'remark', width:100}">Remark</th>
            <th lay-data="{field:'name', width:150,templet: '<div>{{d.deliveryOrder.receiverInfo.name}}</div>' }">
                Receiver
                Name
            </th>
            <th lay-data="{field:'contactNumber', width:150,templet: '<div>{{d.deliveryOrder.receiverInfo.contactNumber}}</div>' }">
                Receiver Phone
            </th>
            <th lay-data="{field:'address', width:150,templet: '<div>{{d.deliveryOrder.receiverInfo.address}}</div>' }">
                Receiver
                Address
            </th>
            <th lay-data="{title:'Image', width:100,templet:'<div>{{ showImageView(d.orderNo) }}</div>'}">Image</th>
            <th lay-data="{field:'userdefine01', width:150}">UserDefine01</th>
            <th lay-data="{field:'userdefine02', width:150}">UserDefine02</th>
            <th lay-data="{field:'userdefine03', width:150}">UserDefine03</th>
            <th lay-data="{field:'userdefine04', width:150}">UserDefine04</th>
            <th lay-data="{field:'userdefine05', width:150}">UserDefine05</th>

            <th lay-data="{title:'Opt',fixed: 'right', width:300, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>
</div>

<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="transfer">Transfer</a>
    <a class="layui-btn layui-btn-warm layui-btn-mini" lay-event="delay">Delay</a>
    <a class="layui-btn layui-btn-warm layui-btn-mini" lay-event="receive">Receive</a>
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="abnormal">Abnormal</a>
</script>

<%@ include file="../../common/footer.jsp" %>
<script src="${ctx}/dist/js/jQuery.print.js"></script>
<script type="text/javascript">


    function formatDispatchTaskStatus(status) {
        if (status == 10) {
            return "Created";
        }
        if (status == 20) {
            return "Process";
        }
        if (status == 30) {
            return "<Font color='1E9FFF'>Complete</Font>";
        }
    }

    $(function () {

        layui.use('upload', function () {
            var upload = layui.upload;
            var load;
            var uploadInst = upload.render({
                elem: '#importSignData'
                , url: '/task/dispatch/importSignData.html'
                , accept: 'file' //普通文件
                , exts: 'xls|xlsx'
                , before: function () {
                    load = layer.load(2);
                }
                , done: function (res) {
                    layer.close(load);
                    if (res.result) {
                        layer.msg('SUCCESS', {icon: 1, time: 2000});
                    } else {
                        layer.msg(res.msg, {icon: 2, time: 2000});
                    }
                }
            });
        });

        var table;
        layui.use('table', function () {
            table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                var taskId = data.taskId;
                var orderNo = data.orderNo;
                var referenceNo = data.deliveryOrder.referenceNo;
                if (obj.event === 'detail') {
                    toDetails(taskId, orderNo);
                } else if (obj.event === 'transfer') {
                    toTransferPage(taskId);
                } else if (obj.event === 'receive') {
                    receive(taskId, orderNo, referenceNo);
                } else if (obj.event === 'abnormal') {
                    abnormal(taskId, orderNo, referenceNo);
                }else if(obj.event ==='delay'){
                    delay(taskId, orderNo, referenceNo);
                }
            });

            $('.print').on('click', function () {
                var data = table.checkStatus('${id0}').data;
                var orderNos = new Array();
                for (var i = 0; i < data.length; i++) {
                    orderNos.push(data[i].orderNo);
                }
                window.open("/task/pickup/print.html?orderNos=" + orderNos, "Print Pick up List");
            });

            $('.search').on('click', function () {
                reloadTable();
            });

        });
        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    orderNo: $("input[name='orderNo']").val(),
                    taskStatus: $("select[name='taskStatus']").val(),
                }
            });
        };

        function toTransferPage(taskId) {
            $.ajax({
                url: "/task/dispatch/transferPage.html",
                type: 'POST',
                data: {"taskId": taskId},
                dataType: 'text',
                success: function (data) {
                    layer.open({
                        type: 1,
                        title: "Transfer Task",
                        area: ['600px', '600px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }

        function toDetails(taskId, orderNo) {
            $.ajax({
                url: "/task/dispatch/detailsPage.html",
                type: 'GET',
                data: {"taskId": taskId, orderNo: orderNo},
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                        type: 1,
                        content: data,
                        area: ['800px', '600px'],
                        maxmin: true,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                    layer.full(index);

                }
            });
        }

        function receive(taskId, orderNo, referenceNo) {
            $.ajax({
                url: "/task/dispatch/receivePage.html",
                type: 'GET',
                data: {"taskId": taskId, orderNo: orderNo, referenceNo: referenceNo},
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                        type: 1,
                        content: data,
                        area: ['700px', '500px'],
                        maxmin: true,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                    layer.full(index);

                }
            });
        }

        function abnormal(taskId, orderNo, referenceNo) {
            $.ajax({
                url: "/task/dispatch/abnormalPage.html",
                type: 'GET',
                data: {"taskId": taskId, orderNo: orderNo, referenceNo: referenceNo},
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                        type: 1,
                        content: data,
                        area: ['700px', '500px'],
                        maxmin: true,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                    layer.full(index);
                }
            });
        }

        function delay(taskId, orderNo, referenceNo) {
            $.ajax({
                url: "/task/dispatch/delayPage.html",
                type: 'GET',
                data: {"taskId": taskId, orderNo: orderNo, referenceNo: referenceNo},
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                        type: 1,
                        title:"Delay",
                        content: data,
                        area: ['700px', '500px'],
                        offset:['150px','300px'],
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }
    });

</script>
</body>
</html>