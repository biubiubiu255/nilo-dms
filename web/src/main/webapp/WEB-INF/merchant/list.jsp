<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="shiro" uri="/shiro.tag" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<html>
<%@ include file="../common/header.jsp" %>
<div class="box-body">

    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            MerchantName:
            <div class="layui-inline">
                <input class="layui-input" id="merchantName" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>
    <div class="layui-row">
        <div class="layui-col-md4">
            <shiro:hasPermission name="200011">
                <button class="layui-btn layui-btn-normal add-merchant-enterprise" id="10001">Add Enterprise</button>
                <button class="layui-btn layui-btn-normal add-merchant-person" id="10001">Add Person</button>
            </shiro:hasPermission>
        </div>
    </div>
    <table class="layui-table"
           lay-data="{ url:'/admin/merchant/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'id', width:80}">ID</th>
            <th lay-data="{field:'merchantName', width:200}">MerchantName</th>
            <th lay-data="{field:'country', width:100}">Country</th>
            <th lay-data="{field:'typeDesc', width:200}">Type</th>
            <th lay-data="{field:'natureDesc', width:200}">Nature</th>
            <th lay-data="{field:'vip', width:100}">VIP</th>
            <th lay-data="{field:'levelDesc', width:100}">Level</th>
            <th lay-data="{field:'licence', width:200}">Licence</th>
            <th lay-data="{field:'idCard', width:200}">ID Card</th>
            <th lay-data="{field:'contactName', width:200}">Contact Name</th>
            <th lay-data="{field:'contactEmail', width:200}">Email</th>
            <th lay-data="{field:'contactPhone', width:200}">Phone</th>
            <th lay-data="{field:'statusDesc', width:100}">Status</th>
            <th lay-data="{field:'createdTime', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{title:'Opt',fixed: 'right', width:300, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="200012">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="200014">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="delete">Active</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="200013">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
        </shiro:hasPermission>

    </script>

</div>
<%@ include file="../common/footer.jsp" %>

<script type="text/javascript">
    $(function () {

        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                var id = data.id;
                if (obj.event === 'edit') {
                    editMerchant(id);
                }
                if (obj.event === 'delete') {
                    delMerchant(id);
                }
                if (obj.event === 'active') {
                    activeMerchant(id);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            var reloadTable = function (item) {
                table.reload("${id0}", {
                    where: {
                        merchantName: $("#merchantName").val()
                    }
                });
            };

        });

        function delMerchant(id) {
            //询问框
            layer.confirm('Confirm to delete?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/admin/merchant/delMerchant.html",
                    dataType: "json",
                    data: {
                        id: id
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

        function activeMerchant(id) {
            //询问框
            layer.confirm('Confirm to active?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/admin/merchant/activeMerchant.html",
                    dataType: "json",
                    data: {
                        id: id
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

        $("button.add-merchant-enterprise").on("click", function () {
            addMerchant("enterprise");
        });

        $("button.add-merchant-person").on("click", function () {
            addMerchant("person");
        });

        function addMerchant(type) {
            var title = "Add";
            var url = "/admin/merchant/addPage.html?type=" + type;
            $.ajax({
                url: url,
                type: 'POST',
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px'],
                        offset: ['100px', '200px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }

        function editMerchant(id) {
            var title = "Edit";
            var url = "/admin/merchant/editPage.html";
            $.ajax({
                url: url,
                type: 'POST',
                data: {id: id},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px'],
                        offset: ['100px', '200px'],
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