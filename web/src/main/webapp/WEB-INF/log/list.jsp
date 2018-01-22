<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("nowDate", DateUtil.formatCurrent("yyyy-MM-dd"));
%>
<body>

<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label"> Parameter:</label>
            <div class="layui-inline">
                <input class="layui-input" name="parameter" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label"> Operator:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" name="operator">
            </div>
        </div>
        <div class="layui-col-md7 layui-col-lg4">
            <label class="layui-form-label"> CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toTime" placeholder="To">
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>


    <table class="layui-table"
           lay-data="{ url:'/admin/log/getLogList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'id', width:150}">ID</th>
            <th lay-data="{field:'operation', width:200}">Operation</th>
            <th lay-data="{field:'operator', width:200}">Operator</th>
            <th lay-data="{field:'parameter', width:500}">Parameter</th>
            <th lay-data="{field:'ip', width:200}">IP</th>
            <th lay-data="{field:'createdTime', width:200, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="detail">Detail</a>
    </script>
</div>

<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use('form',function(){
            var form = layui.form;
            form.render();
        })

        layui.use('laydate', function () {
            var layDate = layui.laydate;
            var d = new Date();
            var now = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
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
                        parameter: $("input[name='parameter']").val(),
                        operator :$("select[name='operator']").val(),
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