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
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            SerialType：
            <div class="layui-form-item layui-inline" style="margin: 0px">
                <select lay-filter="serialType" multiple name="serialType" lay-verify="required">
                    <option value="">Pls select ...</option>
                    <option value='delivery_order_no'>Delivery Order No.</option>
                    <option value='abnormal_order_no'>Abnormal Order No.</option>
                    <option value='loading_no'>Loading No.</option>
                    <option value='staff_id'>Staff ID</option>
                </select>
            </div>
        </div>

        <div class="layui-col-md1">
            <shiro:hasPermission name="800072">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>

    <table class="layui-table"
           lay-data="{ url:'/admin/serialNumRule/getList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'id', width:150}">ID</th>
            <th lay-data="{field:'name', width:200}">Name</th>
            <th lay-data="{field:'serialType', width:200}">SerialType</th>
            <th lay-data="{field:'prefix', width:200}">Prefix</th>
            <th lay-data="{field:'format', width:200}">Format</th>
            <th lay-data="{field:'serialLength', width:200}">Length</th>
            <th lay-data="{title:'Opt', width:160, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>


    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="800071">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
    </script>
</div>

<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        layui.use('form', function () {
            var form = layui.form;
            form.render();
        })
        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'edit') {
                    editSerialType(data);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            function reloadTable(item) {
                table.reload("${id0}", {
                    where: {
                        serialTypes: $("select[name='serialType']").val()
                    }
                });
            };

            function editSerialType(data) {

                var title = "Edit";
                var url = "/admin/serialNumRule/editPage.html";
                $.ajax({
                    url: url,
                    type: 'GET',
                    data: {serialType: data.serialType},
                    dataType: 'text',
                    success: function (data) {
                        parent.layer.open({
                            type: 1,
                            title: title,
                            area: ['600px', '500px'],
                            content: data,
                            end: function () {
                                reloadCurrentPage();
                            }
                        });
                    }
                });
            }

        });
    });


</script>
</body>
</html>