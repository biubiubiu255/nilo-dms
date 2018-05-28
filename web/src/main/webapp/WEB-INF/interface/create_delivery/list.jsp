<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="shiro" uri="/shiro.tag" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<html>
<%@ include file="../../common/header.jsp" %>
<div class="box-body">

    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">OrderNo:</label>
            <div class="layui-inline">
                <input class="layui-input" id="orderNo" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Data:</label>
            <div class="layui-inline">
                <input class="layui-input" id="data" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Sign:</label>
            <div class="layui-inline">
                <input class="layui-input" id="sign" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md1 layui-col-lg1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>

    <table class="layui-table"
           lay-data="{ url:'/interface/createDelivery/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:250}">OrderNo</th>
            <th lay-data="{field:'createStatus', width:150}">CreateStatus</th>
            <th lay-data="{field:'createMsg', width:150}">CreateMsg</th>
            <th lay-data="{field:'createdTime', width:200, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{field:'notifyStatus', width:200}">NotifyStatus</th>
            <th lay-data="{field:'notifyMsg', width:200}">NotifyMsg</th>
            <th lay-data="{field:'notifyTime', width:200, templet:'<div>{{ formatDate(d.notifyTime) }}</div>'}">
                NotifyTime
            </th>
            <th lay-data="{field:'data', width:300}">Data</th>
            <th lay-data="{field:'sign', width:300}">Sign</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:100, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="300012">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="retry">Retry</a>
        </shiro:hasPermission>
    </script>

</div>
<%@ include file="../../common/footer.jsp" %>

<script type="text/javascript">

    $(function () {

        var table;
        layui.use('table', function () {
            table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                var notifyId = data.notifyId;
                if (obj.event === 'retry') {
                    retryNotify(notifyId);
                }
            });

        });
        $(".search").on("click", function () {
            reloadTable();
        })

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    orderNo: $("#orderNo").val(),
                    data: $("#data").val(),
                    sign: $("#sign").val(),
                }
            });
        };
        function retryNotify(notifyId) {
            //询问框
            layer.confirm('Confirm to retry?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/interface/notify/retryNotify.html",
                    dataType: "json",
                    data: {
                        notifyId: notifyId
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 1000}, function () {
                                reloadTable();
                            });
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                    }
                });
            });
        }


    });
</script>

</body>
</html>