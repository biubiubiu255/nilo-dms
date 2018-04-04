<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>

<html>
<%@ include file="../../common/header.jsp" %>
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
                    <div class="layui-col-md4 layui-col-lg5">
                        <label class="layui-form-label" style="width:110px">Status:</label>
                        <div class="layui-form-item layui-inline" style="margin: 0px">
                            <select lay-filter="status" name="status">
                                <option value="">Pls select Status...</option>
                                <option value=0>Loading</option>
                                <option value=1>Ship</option>
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

    <table class="layui-table" lay-data="{ url:'/waybill/rider_delivery/list.html', method: 'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'handleNo', width:200}">LoadingNo</th>
            <th lay-data="{field:'rider', width:150}">Rider</th>
            <th lay-data="{field:'handleByName', width:150}">HandleByName</th>
            <th lay-data="{field:'handle_time', width:170, templet:'<div>{{ formatDate(d.handle_time) }}</div>'}">
                HandleTime
            </th>
            <th lay-data="{field:'', width:170, templet:'<div>{{ formatDate(d.created_time) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{field:'remark', width:120}">Remark</th>
            <th lay-data="{field:'', width:120, templet:'<div>{{ parseStatus(d.status) }}</div>'}">Status</th>
            <th lay-data="{title:'Opt', width:160, align:'center', toolbar: '#barDemo', fixed: 'right'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="tool-detail">Detail</a>
        <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="tool-print">Print</a>
        <%--
        <shiro:hasPermission name="400062">
            <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="detail-loading">Detail</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="400063">
            <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="ship-loading">Ship</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="400064">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete-loading">Delete</a>
        </shiro:hasPermission>--%>
    </script>
</div>
<%@ include file="../../common/footer.jsp" %>
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
                var handleNo = data.handleNo;
                if (obj.event === 'tool-detail') {
                    toDetails(handleNo);
                }else if (obj.event === 'tool-print') {
                    toPrint(handleNo);
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
                    status: $("select[name='status']").val(),
                }
            });
        };

        $(".loading-scan").on("click", function () {
            toLoadingScanPage("");
        })
        
        function toDetails(handleNo) {

            $.ajax({
                url: "/waybill/rider_delivery/detail.html?loadingNo=" + handleNo,
                type: 'GET',
                //dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = parent.layer.open({
                        type: 1,
                        content: data,
                        area: ['900px', '600px'],
                        offset: ['100px', '250px'],
                        maxmin: true
                    });
                }
            });
        }
        function toPrint(handleNo) {
            parent.window.open("/waybill/rider_delivery/print.html?loadingNo=" + handleNo);
            location.reload();
        }


        function toLoadingScanPage() {
            var url = "/waybill/rider_delivery/addLoadingPage.html";
            parent.addTabs({
                id: '40006001',
                title: 'Add Rider List',
                close: true,
                url: url
            });
        }

        function ship(loadingNo) {
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/waybill/rider_delivery/ship.html",
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
                    url: "/waybill/rider_delivery/deleteLoading.html",
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

    function strainerValue(str){
        if(typeof(str)=='undefined') str='';
        return str;
    }

    function parseStatus(str){
        if(str=="0"){
            str = "Loading";
        }else if(str=="1"){
            str = "Ship";
        }else {
            str = "";
        }
        return str;
    }

</script>
</body>
</html>