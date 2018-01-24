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
            <shiro:hasPermission name="800045">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>
    <div class="layui-row">
        <div class="layui-col-md1">
            <shiro:hasPermission name="800044">
                <button class="layui-btn layui-btn-normal add">Add</button>
            </shiro:hasPermission>
        </div>
    </div>
    <table class="layui-table"
           lay-data="{ url:'/systemConfig/route/getList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'optType', width:150}">Opt Type</th>
            <th lay-data="{field:'routeDescC', width:300}">RouteDescC</th>
            <th lay-data="{field:'routeDescC', width:300}">RouteDescE</th>
            <th lay-data="{field:'remark', width:200}">Remark</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:200, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>


    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="800041">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="800042">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="active">Active</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="800043">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="delete">Delete</a>
        </shiro:hasPermission>
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
                    editConfig(data.optType);
                }
                if (obj.event === 'delete') {
                    activeConfig(data.optType);
                }
                if (obj.event === 'active') {
                    editConfig(data.optType);
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
            addConfig();
        })

        function addConfig() {

            var title = "Add";
            var url = "/systemConfig/route/addPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['600px', '600px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }

        function editConfig(optType) {

            var title = "Edit";
            var url = "/systemConfig/route/editPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {optType: optType},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['600px', '600px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }


        function deleteConfig(data) {
            //询问框
            layer.confirm('Confirm to Delete?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/systemConfig/route/delete.html",
                    dataType: "json",
                    data: {
                        optType: data,
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 1000}, function () {
                                reloadCurrentPage();
                            });
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                    }
                });
            });
        }

        function activeConfig(data) {
            //询问框
            layer.confirm('Confirm to Active?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/systemConfig/route/active.html",
                    dataType: "json",
                    data: {
                        optType: data,
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 1000}, function () {
                                reloadCurrentPage();
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