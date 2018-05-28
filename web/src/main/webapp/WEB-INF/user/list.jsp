<%@ page import="org.apache.commons.lang.RandomStringUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">

    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            UserName：
            <div class="layui-inline">
                <input class="layui-input" id="username" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="300021">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>
    <div class="layui-row">
        <div class="layui-col-md3">
            <shiro:hasPermission name="300022">
                <button class="layui-btn layui-btn-normal add-user" id="10001">Add</button>
            </shiro:hasPermission>
        </div>
    </div>

    <table class="layui-table"
           lay-data="{ url:'/admin/user/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'userName', width:100,templet: '<div>{{d.loginInfo.userName}}</div>'}">UserName</th>
            <th lay-data="{field:'name', width:150,templet: '<div>{{d.userInfo.name}}</div>'}">Name</th>
            <th lay-data="{field:'email', width:150,templet: '<div>{{d.userInfo.email}}</div>'}">Email</th>
            <th lay-data="{field:'phone', width:150,templet: '<div>{{d.userInfo.phone}}</div>'}">phone</th>
            <th lay-data="{field:'statusCode', width:100,templet:'<div>{{ formatUserStatus(d.loginInfo.statusCode) }}</div>'}">
                Status
            </th>
            <th lay-data="{field:'orderTime', width:170, templet:'<div>{{ formatDate(d.userInfo.createdTime) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{title:'Opt',fixed: 'right', width:300, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="300023">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="300024">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="300025">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="reset">Reset</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="300026">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="active">Active</a>
        </shiro:hasPermission>
    </script>
</div>


<!--Bottom Footer-->
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">

    function formatUserStatus(status) {
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
                var userId = data.userId;
                if (obj.event === 'edit') {
                    editUser(userId);
                }
                if (obj.event === 'delete') {
                    delUser(userId);
                }
                if (obj.event === 'active') {
                    activeUser(userId);
                }
                if (obj.event === 'reset') {
                    resetPassword(userId);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })


        });

        var reloadTable = function (item) {
            table.reload("${id0}", {
                where: {
                    username: $("#username").val()
                }
            });
        };

        $("button.add-user").on("click", function () {
            addUser();
        });

        function resetPassword(userId) {
            //询问框
            layer.confirm('Confirm to Reset Password?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/admin/user/resetPassword.html",
                    dataType: "json",
                    data: {
                        userId: userId
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 2000}, function () {
                            });
                        } else {
                            layer.msg("Failed. " + data.msg, {icon: 2});
                        }
                    }
                });
            });
        }

        function delUser(userId) {
            //询问框
            layer.confirm('Confirm to delete?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/admin/user/delUser.html",
                    dataType: "json",
                    data: {
                        userId: userId
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 2000}, function () {
                                reloadCurrentPage();
                            });
                        } else {
                            layer.msg("Failed. " + data.msg, {icon: 2});
                        }
                    }
                });
            });
        }

        function activeUser(userId) {
            //询问框
            layer.confirm('Confirm to active?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/admin/user/activeUser.html",
                    dataType: "json",
                    data: {
                        userId: userId
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 2000}, function () {
                                reloadCurrentPage();
                            });
                        } else {
                            layer.msg("Failed. " + data.msg, {icon: 2});
                        }
                    }
                });
            });
        }

        function editUser(userId) {
            var url = "/admin/user/editPage.html?userId=" + userId;
            var title = "Edit User";
            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px'],
                        offset: ['100px', '250px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });

        }

        function addUser() {
            var url = "/admin/user/addPage.html";
            var title = "Add User";
            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px'],
                        offset: ['100px', '250px'],
                        content: data ,
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