<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            Name：
            <div class="layui-inline">
                <input type="text" name="name" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="200022">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>

    </div>
    <hr>
    <div class="layui-row">
        <shiro:hasPermission name="200021">
            <div class="layui-col-md1">
                <button class="layui-btn layui-btn-normal add">Add</button>
            </div>
        </shiro:hasPermission>
    </div>
    <table class="layui-table"
           lay-data="{ url:'/admin/distribution/getList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'name', width:150}">Name</th>
            <th lay-data="{field:'merchantName', width:150}">Contract Area</th>
            <th lay-data="{field:'provinceDesc', width:200}">Province</th>
            <th lay-data="{field:'cityDesc', width:200}">City</th>
            <th lay-data="{field:'areaDesc', width:200}">Area</th>
            <th lay-data="{field:'address', width:200}">Address</th>
            <th lay-data="{field:'statusDesc', width:200}">Status</th>
            <th lay-data="{field:'remark', width:200}">Remark</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>


    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="200023">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="200024">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="200025">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="active">Active</a>
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
                    editConfig(data);
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
                        name: $("input[name='name']").val()
                    }
                });
            };


        });

        $(".add").on("click", function () {
            addConfig();
        })

        function addConfig() {

            var title = "Add";
            var url = "/admin/distribution/addPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px'],
                        content: data,
                        end: function () {
                        }
                    });
                }
            });
        }

        function editConfig(data) {

            var title = "Edit";
            var url = "/admin/distribution/editPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {optType: data.optType},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px'],
                        content: data,
                        end: function () {
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
                    url: "/admin/distribution/delete.html",
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
                    url: "/admin/distribution/active.html",
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