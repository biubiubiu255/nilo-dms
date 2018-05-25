<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>

<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">
    <!-- /.box-header -->
    <div class="layui-row">

        <div class="layui-col-md5">
            <shiro:hasPermission name="400082">
                <button class="layui-btn layui-btn-normal loading-scan">Package Scan</button>
            </shiro:hasPermission>
            <button class="layui-btn btn-search">Search</button>
        </div>

    </div>
    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content ">
                <div class="layui-form layui-row">
                    <div class="layui-col-md4 layui-col-lg3">
                        PackageNo:
                        <div class="layui-inline">
                            <input class="layui-input" name="packageNo" autocomplete="off">
                        </div>
                    </div>
                    <div class="layui-col-md1">
                        <button class="layui-btn layui-btn-normal search">Search</button>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <table class="layui-table" lay-data="{ url:'/order/package/list.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}">PackageNo</th>
            <th lay-data="{field:'statusDesc', width:150}">Status</th>
            <th lay-data="{field:'nextNetworkDesc', width:150}">NextStation</th>
            <th lay-data="{field:'createdBy', width:120}">PackageBy</th>
            <th lay-data="{width:200, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">CreateTime</th>
            <th lay-data="{field:'weight', width:120}">Weight</th>
            <th lay-data="{field:'len', width:120}">Length</th>
            <th lay-data="{field:'width', width:120}">Width</th>
            <%--<th lay-data="{field:'height', width:120}">High</th>--%>
            <th lay-data="{title:'Opt',fixed: 'right', width:250, align:'center', toolbar: '#barDemo'}"></th>

        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="400083">
            <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="details">Details</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="400084">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="print">Print Packing</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="400085">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="print_delivery">Print Waybill</a>
        </shiro:hasPermission>
    </script>
</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        var table;
        layui.use(['form', 'element','laydate','table'], function () {
            var layDate = layui.laydate;
            layDate.render({
                elem: '#fromCreatedTime'
                , lang: 'en'
            });
            layDate.render({
                elem: '#toCreatedTime'
                , lang: 'en'
            });
            table = layui.table;
            table.on('tool(demo)', function (obj) {
                console.log(obj.data);
                var data = obj.data;
                var orderNo = data.orderNo;
                if (obj.event === 'details') {
                    detailsPackage(orderNo);
                }
                if (obj.event === 'print') {
                    if(data.status!='DELIVERY' && data.status!='ARRIVED'){
                        layer.msg("The package is not finish", {icon: 2, time: 2000});
                        return;
                    }
                    printPackage(orderNo);
                }
                if (obj.event === 'print_delivery') {
                    if(data.status!='DELIVERY' && data.status!='ARRIVED'){
                        layer.msg("The package is not finish", {icon: 2, time: 2000});
                        return;
                    }
                    print(orderNo);
                }
            });
        });


        function print(orderNo) {
            parent.window.open("/waybill/print/" + orderNo + ".html");
        }

        $(".search").on("click", function () {
            reloadTable();
        })
        $(".btn-search").on("click", function () {
            $(".layui-colla-content").toggleClass("layui-show");
            $(".btn-search").toggleClass("layui-btn-warm");
        })
        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    packageNo: $("input[name='packageNo']").val(),
                }
            });
        };

        $(".loading-scan").on("click", function () {
            var url = "/order/package/packagePage.html";
            parent.addTabs({
                id: '40008001',
                title: 'Package',
                close: true,
                url: url
            });
        });

        function detailsPackage( orderNo ) {
            $.ajax({
                url: "/order/package/" + orderNo + ".html",
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

        function printPackage( orderNo ) {
            parent.window.open("/order/package/print/"+orderNo+".html");
        }
    });

</script>
</body>
</html>