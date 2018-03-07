<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("merchantId", (String) session.getAttribute("merchantId"));
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
%>
<body>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md5">

            <%-- <shiro:hasPermission name="400011">
                <button class="layui-btn layui-btn-normal btn-add">Add</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="400012">

                <button type="button" class="layui-btn" id="importDeliveryOrder">
                    <i class="layui-icon">&#xe67c;</i>Import
                </button>

            </shiro:hasPermission>
            <shiro:hasPermission name="400013">
                <button class="layui-btn layui-btn-normal btn-export"><i class="fa fa-cloud-download"
                                                                         aria-hidden="true"></i>Template
                </button>
            </shiro:hasPermission> --%>
            <shiro:hasPermission name="400017">
                <button class="layui-btn layui-btn-normal btn-export">Export</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="400016">
                <button class="layui-btn btn-search">Search
                </button>
            </shiro:hasPermission>
        </div>
    </div>
    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content ">
                <div class="layui-form layui-row">
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OrderNo:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="orderNo" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">ReferenceNo:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="referenceNo" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-col-md8 layui-col-lg5">
                        <label class="layui-form-label">CreateTime:</label>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="fromCreatedTime" id="fromCreatedTime"
                                   placeholder="From">
                        </div>
                        -
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="toCreatedTime" id="toCreatedTime"
                                   placeholder="To">
                        </div>
                    </div>
                </div>

                <div class="layui-form layui-row">
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OrderType:</label>
                        <div class="layui-form-item layui-inline" style="margin: 0px">
                            <select lay-filter="orderType" multiple name="orderType">
                                <option value="">Pls select order type...</option>
                                <c:forEach items="${orderTypeList}" var="r">
                                    <option value=${r.code}>${r.code}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">Status:</label>
                        <div class="layui-form-item layui-inline">
                            <lp:deliveryStatus selectId="orderStatus" selectName="orderStatus" multiple="true"/>
                        </div>
                    </div>
                    <div class="layui-col-md3 layui-col-lg3">
                        <label class="layui-form-label">Platform:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="platform" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-col-md1">
                        <button class="layui-btn layui-btn-normal search">Search</button>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <table class="layui-table"
           lay-data="{ url:'/order/deliveryOrder/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}">OrderNo</th>
            <th lay-data="{field:'orderTypeDesc', width:100}">OrderType</th>
            <th lay-data="{field:'referenceNo', width:200}">ReferenceNo</th>
            <th lay-data="{field:'statusDesc', width:100}">Status</th>
            <th lay-data="{field:'orderTime', width:170, templet:'<div>{{ formatDate(d.orderTime) }}</div>'}">
                OrderTime
            </th>
            <th lay-data="{field:'weight', width:100}">Weight</th>
            <th lay-data="{field:'goodsType', width:120}">GoodsType</th>
            <th lay-data="{field:'orderPlatform', width:120}">Platform</th>
            <th lay-data="{field:'totalPrice', width:120}">Order Amount</th>
            <th lay-data="{field:'needPayAmount', width:120}">Need Pay</th>
            <th lay-data="{field:'receiverInfo', width:150,templet: '<div>{{d.receiverInfo.receiverName}}</div>' }">
                Receiver
                Name
            </th>
            <th lay-data="{field:'receiverInfo', width:150,templet: '<div>{{d.receiverInfo.receiverPhone}}</div>' }">
                Receiver Phone
            </th>
            <th lay-data="{field:'receiverInfo', width:150,templet: '<div>{{d.receiverInfo.receiverAddress}}</div>' }">
                Receiver
                Address
            </th>
            <th lay-data="{field:'orderTime', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                Create Time
            </th>

            <th lay-data="{title:'Image', width:100,templet:'<div>{{ showImageView(d.orderNo) }}</div>'}">Image</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="400014">
            <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="detail">Detail</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="400015">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="400017">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="print">Print</a>
        </shiro:hasPermission>
    </script>
    <%@ include file="../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {

            layui.use(['element', 'form', 'laydate'], function () {
                var layDate = layui.laydate;
                layDate.render({
                    elem: '#fromCreatedTime'
                    , lang: 'en'
                });
                layDate.render({
                    elem: '#toCreatedTime'
                    , lang: 'en'
                });
            });
            var table;
            layui.use('table', function () {
                table = layui.table;
                //监听工具条
                table.on('tool(demo)', function (obj) {
                    var data = obj.data;
                    var orderNo = data.orderNo;
                    if (obj.event === 'detail') {
                        orderDetails(orderNo);
                    } else if (obj.event === 'edit') {
                        editOrder(orderNo);
                    } else if (obj.event === 'print') {
                        print(orderNo);
                    }
                });

            });

            $(".search").on("click", function () {
                reloadTable();
            })

            function reloadTable() {
                table.reload("${id0}", {
                    where: {
                        orderNo: $("input[name='orderNo']").val(),
                        referenceNo: $("input[name='referenceNo']").val(),
                        orderTypes: $("select[name='orderType']").val(),
                        orderStatus: $("select[name='orderStatus']").val(),
                        fromCreatedTime: $("#fromCreatedTime").val(),
                        toCreatedTime: $("#toCreatedTime").val(),
                        platform: $("input[name='platform']").val(),

                    }
                });
            };


            $(".btn-export").on("click", function () {

                var orderNo = $("input[name='orderNo']").val(),
                        referenceNo = $("input[name='referenceNo']").val(),
                        orderTypes = $("select[name='orderType']").val(),
                        orderStatus = $("select[name='orderStatus']").val(),
                        fromCreatedTime = $("#fromCreatedTime").val(),
                        toCreatedTime = $("#toCreatedTime").val(),
                        platform = $("input[name='platform']").val();

                var url = "/order/deliveryOrder/export.html?orderNo="+orderNo+"&referenceNo="+referenceNo+"&orderTypes="+orderTypes+"&orderStatus="+orderStatus+"&fromCreatedTime="+fromCreatedTime+"&toCreatedTime="+toCreatedTime+"&platform="+platform;
                window.location.href = url;
            })

            $(".btn-search").on("click", function () {
                $(".layui-colla-content").toggleClass("layui-show");
                $(".btn-search").toggleClass("layui-btn-warm");
            })


            layui.use('upload', function () {
                var upload = layui.upload;
                var load;
                var uploadInst = upload.render({
                    elem: '#importDeliveryOrder'
                    , url: '/order/deliveryOrder/importOrderData.html'
                    , accept: 'file' //普通文件
                    , exts: 'xls|xlsx'
                    , before: function () {
                        load = layer.load(2);
                    }
                    , done: function (res) {
                        layer.close(load);
                        if (res.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 2000}, function () {
                                reloadTable()
                            });
                        } else {
                            layer.msg(res.msg, {icon: 2, time: 2000});
                        }
                    }
                });
            });

            function orderDetails(orderNo) {
                $.ajax({
                    url: "/order/deliveryOrder/" + orderNo + ".html",
                    type: 'GET',
                    dataType: 'text',
                    success: function (data) {
                        //弹出即全屏
                        var index = layer.open({
                            type: 1,
                            content: data,
                            area: ['800px', '600px'],
                            maxmin: true
                        });
                        layer.full(index);
                    }
                });
            }

            function print(orderNo) {
                parent.window.open("/order/deliveryOrder/print/" + orderNo + ".html");
            }


            $(".btn-add").on("click", function () {
                var url = "/order/deliveryOrder/add.html";
                var index = parent.layer.open({
                    type: 2,
                    title: 'Add DELIVERY',
                    shadeClose: true,
                    area: ['800px', '600px'],
                    content: url,
                    maxmin: true
                });
            })
            function editOrder(orderNo) {
                var index = parent.layer.open({
                    type: 2,
                    title: 'Edit DELIVERY',
                    shadeClose: true,
                    area: ['800px', '600px'],
                    content: "/order/deliveryOrder/edit/" + orderNo + ".html",
                    maxmin: true,
                    end: function () {
                        reloadCurrentPage();
                    }
                });
                layer.full(index);
            }
        });

    </script>
</body>
</html>