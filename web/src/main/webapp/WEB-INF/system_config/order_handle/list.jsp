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
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Opt Type:</label>
            <div class="layui-inline">
                <lp:enumTag selectId="optType" selectName="optType" className="OptTypeEnum"
                            code="" disabled="false" />
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="800013">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>
    <div class="layui-row">
        <div class="layui-col-md1">
            <shiro:hasPermission name="800011">
                <button class="layui-btn layui-btn-normal add">Add</button>
            </shiro:hasPermission>
        </div>
    </div>
    <table class="layui-table"
           lay-data="{ url:'/systemConfig/orderHandle/getList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'optType', width:150}">Opt Type</th>
            <th lay-data="{field:'updateStatusDesc', width:150}">Change to Status</th>
            <th lay-data="{field:'allowStatusDesc', width:400,templet:'<div>{{ formatStatusBadge(d.allowStatusDesc) }}</div>'}">
                AllowStatus
            </th>
            <th lay-data="{field:'notAllowStatusDesc', width:300,templet:'<div>{{ formatStatusBadge(d.notAllowStatusDesc) }}</div>'}">
                NotAllowStatus
            </th>
            <th lay-data="{field:'className', width:200}">ClassName</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>


    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="800012">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
    </script>
</div>

<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">

    function formatStatusBadge(data) {
        var format = "<div class='layui-btn-group'>";
        for (var i = 0; i < data.length; i++) {
            format = format + "<button class='layui-btn layui-btn-small'>"+data[i]+"</button>";
        }
        return format+"</div>";
    }

    $(function () {

        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'edit') {
                    editOrderHandleConfig(data);
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

        $(".add").on("click", function () {
            addHandleOptConfig();
        })

        function addHandleOptConfig() {

            var title = "Add";
            var url = "/systemConfig/orderHandle/addPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {},
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

        function editOrderHandleConfig(data) {

            var title = "Edit";
            var url = "/systemConfig/orderHandle/editPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {optType: data.optType},
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


</script>
</body>
</html>