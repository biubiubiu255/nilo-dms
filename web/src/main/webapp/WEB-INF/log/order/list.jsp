<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
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
        <div class="layui-col-md7 layui-col-lg6">
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toTime" placeholder="To">
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="900021">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>


    <table class="layui-table"
           lay-data="{ url:'/waybill/log/getLogList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'id', width:150}">ID</th>
            <th lay-data="{field:'orderNo', width:200}">OrderNo</th>
            <th lay-data="{field:'opt', width:200}">Opt</th>
            <th lay-data="{field:'createdTime', width:200, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                Opt Time
            </th>
            <th lay-data="{field:'optName', width:200, templet:'<div>{{d.optName }}</div>'}">
                Opt By
            </th>
            <th lay-data="{field:'remark', width:300}">Remark</th>
        </tr>
        </thead>
    </table>

</div>

<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use('form',function(){
            var form = layui.form;
            form.render();
        })

        layui.use('laydate', function () {
            var layDate = layui.laydate;

            layDate.render({
                elem: '#fromTime',
                lang: 'en',
            });
            layDate.render({
                elem: '#toTime',
                lang: 'en',
            });
        });
        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    var id = data.id;
                    layer.msg(id);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            var reloadTable = function (item) {

                table.reload("${id0}", {
                    where: {
                        orderNo :$("input[name='orderNo']").val(),
                        fromTime:$("#fromTime").val(),
                        toTime:$("#toTime").val(),
                    }
                });
            };

        });
    });

</script>
</body>
</html>