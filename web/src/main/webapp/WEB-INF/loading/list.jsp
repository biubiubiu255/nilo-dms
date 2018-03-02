<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>

<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
%>
<body>
<div class="box-body">
    <!-- /.box-header -->
    <div class="layui-row">

        <div class="layui-col-md5">
            <shiro:hasPermission name="400061">
                <button class="layui-btn layui-btn-normal loading-scan">Loading Scan</button>
            </shiro:hasPermission>
            <button class="layui-btn btn-search">Search</button>
        </div>

    </div>
    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content ">
                <div class="layui-form layui-row">
                    <div class="layui-col-md4 layui-col-lg3">
                        LoadingNo：
                        <div class="layui-inline">
                            <input class="layui-input" name="loadingNo" autocomplete="off">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label" style="width:110px">Status:</label>
                        <div class="layui-form-item layui-inline" style="margin: 0px">
                            <select lay-filter="loadingStatus" name="loadingStatus">
                                <option value="">Pls select Status...</option>
                                <option value=1>Create</option>
                                <option value=2>Loading</option>
                                <option value=3>Ship</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-col-md1">
                        <button class="layui-btn layui-btn-normal search">Search</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <table class="layui-table" lay-data="{ url:'/order/loading/list.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'loadingNo', width:200}">LoadingNo</th>
            <th lay-data="{field:'statusDesc', width:150}">Status</th>
            <th lay-data="{field:'nextStation', width:150}">NextStation</th>
            <%--
                        <th lay-data="{field:'carrier', width:150}">Carrier</th>
            --%>
            <th lay-data="{field:'riderName', width:150,templet: '<div>{{d.riderName}}</div>'}">Rider</th>
            <%--
                        <th lay-data="{field:'truckType', width:150}">TruckType</th>
            --%>
            <th lay-data="{field:'loadingName', width:150,templet: '<div>{{d.loadingName}}</div>'}">LoadingBy</th>
            <th lay-data="{field:'loadingFromTime', width:170, templet:'<div>{{ formatDate(d.loadingFromTime) }}</div>'}">
                LoadingFromTime
            </th>
            <th lay-data="{field:'loadingToTime', width:170, templet:'<div>{{ formatDate(d.loadingToTime) }}</div>'}">
                LoadingToTime
            </th>
            <th lay-data="{field:'remark', width:120}">Remark</th>

            <th lay-data="{fixed: 'right', width:200, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="400062">
            <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="detail-loading">Detail</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="400063">
            <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="ship-loading">Ship</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="400064">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete-loading">Delete</a>
        </shiro:hasPermission>
    </script>
</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use(['form', 'layer', 'element', 'laydate'], function () {
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
                var loadingNo = data.loadingNo;
                if (obj.event === 'detail-loading') {
                    toDetails(loadingNo);
                } else if (obj.event === 'ship-loading') {
                    ship(loadingNo);
                } else if (obj.event === 'delete-loading') {
                    deleteLoading(loadingNo);
                }

            });

            $(".search").on("click", function () {
                reloadTable();
            })
            $(".btn-search").on("click", function () {
                $(".layui-colla-content").toggleClass("layui-show");
                $(".btn-search").toggleClass("layui-btn-warm");
            })


        });

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    loadingNo: $("input[name='loadingNo']").val(),
                    loadingStatus: $("select[name='loadingStatus']").val(),
                }
            });
        };

        $(".loading-scan").on("click", function () {
            toLoadingScanPage("");
        })

        function toLoadingScanPage() {

            var url = "/order/loading/loadingScanPage.html";
            parent.addTabs({
                id: '40006001',
                title: 'Add Loading List',
                close: true,
                url: url
            });
        }

        function toDetails(loadingNo) {

            $.ajax({
                url: "/order/loading/detailsPage.html?loadingNo=" + loadingNo,
                type: 'GET',
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = parent.layer.open({
                        type: 1,
                        content: data,
                        area: ['800px', '600px'],
                        offset: ['100px', '250px'],
                        maxmin: true
                    });
                }
            });
        }

        function ship(loadingNo) {
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/order/loading/ship.html",
                dataType: "json",
                data: {
                    loadingNo: loadingNo,
                },
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
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

        function deleteLoading(loadingNo) {
            //询问框
            layer.confirm('Confirm to delete?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/loading/deleteLoading.html",
                    dataType: "json",
                    data: {
                        loadingNo: loadingNo,
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
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
            });
        }
    });

</script>
</body>
</html>