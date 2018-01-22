<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
            <label class="layui-form-label">BizType:</label>
            <div class="layui-form-item layui-inline" style="margin: 0px">
                <select lay-filter="bizType" id="bizType">
                    <option value="">Pls select type...</option>
                    <option value="update_status">update_status</option>
                    <option value="create_order_notify">create_order_notify</option>

                </select>
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Status:</label>
            <div class="layui-form-item layui-inline" style="margin: 0px">
                <select lay-filter="status" id="status">
                    <option value="">Pls select status...</option>
                    <option value=0>Created</option>
                    <option value=1>Success</option>
                    <option value=2>Failed</option>
                    <option value=3>Retry</option>
                </select>
            </div>
        </div>

        <div class="layui-col-md1 layui-col-lg1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>

    <table class="layui-table"
           lay-data="{ url:'/interface/notify/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'notifyId', width:200}">Notify ID</th>
            <th lay-data="{field:'bizType', width:150}">Biz Type</th>
            <th lay-data="{field:'orderNo', width:200}">OrderNo</th>
            <th lay-data="{field:'status', width:100,templet:'<div>{{ formatNotifyStatus(d.status) }}</div>'}">Status
            </th>
            <th lay-data="{field:'num', width:100}">Num</th>
            <th lay-data="{field:'url', width:200}">URL</th>
            <th lay-data="{field:'param', width:200}">Data</th>
            <th lay-data="{field:'sign', width:100}">Sign</th>
            <th lay-data="{field:'result', width:200}">Result</th>
            <th lay-data="{field:'orderTime', width:200, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                CreatedTime
            </th>
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

    function formatNotifyStatus(status) {
        if (status == 0) {
            return "Created";
        }
        if (status == 1) {
            return "<Font color='##1E9FFF'>Success</Font>";
        }
        if (status == 2) {
            return "<Font color='red'>Failed</Font>";
        }
        if (status == 3) {
            return "<Font color='#ffd700'>Retry</Font>";
        }
    }

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
                    status: $("#status").val(),
                    bizType: $("#bizType").val()
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