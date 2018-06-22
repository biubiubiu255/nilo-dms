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

    <table class="layui-table"
           lay-data="{ url:'/systemConfig/sms/getList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'status', width:200,templet:'#statusTpl'}">是否启用</th>
            <th lay-data="{field:'smsType', width:200}">Message Type</th>
            <th lay-data="{field:'name', width:200}">Name</th>
            <th lay-data="{field:'content', width:400}">Content</th>
            <th lay-data="{field:'param', width:200}">Param</th>

            <th lay-data="{title:'Opt',fixed: 'right', width:200, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>


    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="800052">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
    </script>
    <!-- 表格状态列 -->
    <script type="text/html" id="statusTpl">
        {{ d.status== 1?'是' : '否' }}
    </script>

</div>

<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">

    $(function () {

        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'edit') {
                    editConfig(data.smsType);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            function reloadTable(item) {
                table.reload("${id0}", {
                    where: {
                        optType: $("#optType").val()
                    }
                });
            };


        });


        function editConfig(msgType) {

            var title = "Edit";
            var url = "/systemConfig/sms/editPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {msgType: msgType},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }

    });


</script>
</body>
</html>