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
            <label class="layui-form-label">Method:</label>
            <div class="layui-inline">
                <input class="layui-input" id="method" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>

    <table class="layui-table"
           lay-data="{ url:'/systemConfig/interface/getList.html',method:'post', page:true,limit:20, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'method', width:150}">Method</th>
            <th lay-data="{field:'op', width:200,edit:true}">Op</th>
            <th lay-data="{field:'url', width:300,edit:true}">URL</th>
            <th lay-data="{field:'requestMethod', width:200,edit:true}">Request Method</th>
            <th lay-data="{field:'statusCode', width:150,templet:'<div>{{ formatInterfaceConfigStatus(d.statusCode) }}</div>'}">Status</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:200, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="300012">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
    </script>

</div>
<%@ include file="../../common/footer.jsp" %>

<script type="text/javascript">

    function formatInterfaceConfigStatus(status){
        if(status ==1){
            return "<Font color='##1E9FFF'>Normal</Font>";
        }
        if(status ==2){
            return "<Font color='red'>Disabled</Font>";
        }
    }

    $(function () {

        var table;
        layui.use('table', function () {
            table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'edit') {
                    edit(data);
                }
            });

        });
        $(".search").on("click", function () {
            reloadTable();
        })

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    method: $("#method").val()
                }
            });
        };
        function edit(data) {
            //询问框
            layer.confirm('Confirm to edit?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/systemConfig/interface/edit.html",
                    dataType: "json",
                    data: {
                        method: data.method,
                        op: data.op,
                        url: data.url,
                        requestMethod: data.requestMethod,
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 1000});
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