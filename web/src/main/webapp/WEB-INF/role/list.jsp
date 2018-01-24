<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">

    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            RoleName：
            <div class="layui-inline">
                <input class="layui-input" id="roleName" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>
    <div class="layui-row">
        <div class="layui-col-md3">
            <shiro:hasPermission name="100021">
                <button class="layui-btn layui-btn-normal btn-edit" id="10001">Add</button>
            </shiro:hasPermission>
        </div>
    </div>

    <table class="layui-table"
           lay-data="{ url:'/admin/role/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'roleId', width:60}">ID</th>
            <th lay-data="{field:'roleName', width:200}">RoleName</th>
            <th lay-data="{field:'description', width:200}">Description</th>
            <th lay-data="{field:'statusCode', width:100,templet:'<div>{{ formatRoleStatus(d.statusCode) }}</div>'}">
                Status
            </th>
            <th lay-data="{field:'orderTime', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{title:'Opt',fixed: 'right', width:300, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="100022">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="100023">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="100023">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="active">Active</a>
        </shiro:hasPermission>
    </script>

</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">

    function formatRoleStatus(status) {
        if (status == 1) {
            return "<Font color='##1E9FFF'>Normal</Font>";
        }
        if (status == 2) {
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
                var roleId = data.roleId;
                if (obj.event === 'edit') {
                    editRole(roleId);
                }
                if (obj.event === 'delete') {
                    delRole(roleId);
                }
                if (obj.event === 'active') {
                    activeRole(roleId);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

        });

        var reloadTable = function (item) {
            table.reload("${id0}", {
                where: {
                    roleName: $("#roleName").val()
                }
            });
        };

        $("button.btn-edit").on("click", function () {
            editRole('');
        });


        function delRole(id) {
            //询问框
            layer.confirm('Confirm to delete?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/admin/role/delRole.html",
                    dataType: "json",
                    data: {
                        roleId: id
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                                reloadTable();
                            });
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
            });
        }

        function activeRole(id) {
            //询问框
            layer.confirm('Confirm to active?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/admin/role/activeRole.html",
                    dataType: "json",
                    data: {
                        roleId: id
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                                reloadTable();
                            });
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
            });
        }

        function editRole(id) {
            var url;
            if (id) {
                url = "/admin/role/editRolePage.html?id=" + id;
            } else {
                url = "/admin/role/editRolePage.html";
            }
            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                        type: 1,
                        content: data,
                        area: ['800px', '600px'],
                        maxmin: true,
                        end: function () {
                            reloadTable();
                        }
                    });
                    layer.full(index);
                }
            });
        }

    });

</script>
</body>
</html>
