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

        <div class="layui-form layui-row">
            <div class="layui-col-md4 layui-col-lg3">
                OrderNo：
                <div class="layui-inline">
                    <input class="layui-input" name="loadingNo" autocomplete="off">
                </div>
            </div>

            <div class="layui-col-md1">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </div>
        </div>

    </div>


    <table class="layui-table"
           lay-data="{ url:'/waybill/third_express_delivery/bindList.html' ,method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:140}">OrderNo</th>
            <th lay-data="{field:'expressNo', width:150, edit:'text'}">ExpressNo</th>
            <th lay-data="{field:'driver', width:170, templet:'<div>Waiting supplement</div>'}">Status</th>
        </tr>
        </thead>
    </table>

</div>
<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        var layerM;

        var table;

        layui.use(['layer', 'element', 'laydate', 'table'], function () {

            var layerM = layui.layer;

            table = layui.table;
            table.on('edit(demo)', function(obj){
                var value = obj.value //得到修改后的值
                    ,data = obj.data //得到所在行所有键值
                    ,field = obj.field; //得到字段
                layer.msg('[ID: '+ data.orderNo +'] ' + field + ' 字段更改为：'+ value);
                $.post('/waybill/third_express_delivery/bindOrderNo.html', {orderNo: data.orderNo, expressNo: value}, function (data) {
                    console.log(data);
                }, "json");
            });

        });


        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    handleNo: $("input[name='loadingNo']").val(),
                    status: $("select[name='status']").val(),
                }
            });
        };


        $(".search").on("click", function () {
            reloadTable();
        });

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