<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>

<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">


    <!-- /.box-header -->
    <div class="layui-row">

        <div class="layui-col-md5">
            <shiro:hasPermission name="400061">
                <button class="layui-btn layui-btn-normal loading-scan">Delivery Scan</button>
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

    <table class="layui-table"
           lay-data="{ url:'/waybill/third_express_delivery/list.html' ,method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'handleNo', width:140}">LoadingNo</th>
            <th lay-data="{field:'thirdExpressCode', width:150}">Third Express</th>
            <th lay-data="{field:'driver', width:150}">Rider</th>
            <th lay-data="{field:'handleName', width:150}">HandleName</th>
            <th lay-data="{field:'', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{field:'', width:120, templet:'<div>{{ parseStatus(d.status) }}</div>'}">Status</th>
            <th lay-data="{title:'Opt', width:230, align:'center', toolbar: '#barDemo', fixed: 'right'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="tool-print">Print</a>
        <a class="layui-btn layui-btn-primary  layui-btn-mini" lay-event="tool-detail">Detail</a>
        <a class="layui-btn layui-btn-danger  layui-btn-mini" lay-event="tool-ship">Ship</a>
    </script>
</div>
<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use(['layer', 'element', 'laydate'], function () {
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
                } else if (obj.event === 'tool-print') {
                    toPrint(handleNo);
                } else if (obj.event === 'tool-ship') {
                    if (data.status == 1) {
                        layer.msg("Has been shipped", {icon: 2, time: 2000});
                        return;
                    }
                    ship(handleNo);
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
                    handleNo: $("input[name='loadingNo']").val(),
                    status: $("select[name='status']").val(),
                }
            });
        };


        $(".loading-scan").on("click", function () {
            toLoadingScanPage("");
        })

        function toDetails(handleNo) {

            parent.layer.open({
                type: 2,
                title: 'Detail',
                shadeClose: true,
                shade: false,
                maxmin: true, //开启最大化最小化按钮
                area: ['900px', '600px'],
                offset: ['100px', '250px'],
                content: '/waybill/third_express_delivery/detail.html?loadingNo=' + handleNo
            });
        }

        function toPrint(handleNo) {
            parent.window.open("/waybill/third_express_delivery/print.html?loadingNo=" + handleNo);
            location.reload();
        }


        function toLoadingScanPage() {
            var url = "/waybill/third_express_delivery/addLoadingPage.html";
            parent.addTabs({
                id: '40008011',
                title: 'Third Express Delivery',
                close: true,
                url: url
            });
        }

        //暂时停用
        function ship(handleNo) {
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/waybill/third_express_delivery/ship.html",
                dataType: "json",
                data: {
                    handleNo: handleNo,
                    status: 1
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

    });

    function strainerValue(str) {
        if (typeof(str) == 'undefined') str = '';
        return str;
    }

    function parseStatus(str) {
        if (str == "0") {
            str = "Loading";
        } else if (str == "1") {
            str = "Ship";
        } else {
            str = "";
        }
        return str;
    }

</script>
</body>
</html>