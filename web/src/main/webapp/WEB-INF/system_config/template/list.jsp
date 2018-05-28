<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
    request.setAttribute("goodsTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "goods_type"));
    request.setAttribute("orderPlatformList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "order_platform"));
%>
<body>
<div class="box-body">

    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Name:</label>
            <div class="layui-input-inline">
                <input type="text" name="name" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label" >Country:</label>
            <div class="layui-input-inline">
                <select name="country" lay-filter="country" lay-verify="required" lay-search="" style="display: none">
                    <option value="">Select type....</option>
                    <option value="KE">KE</option>
                    <option value="NG">NG</option>
                </select>
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="800036">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>

    <hr>
    <div class="layui-row">
        <div class="layui-col-md1">
            <shiro:hasPermission name="800031">
            <button class="layui-btn layui-btn-normal add"><spring:message code="add_delivery_fee_template"/></button>
            </shiro:hasPermission>
        </div>
    </div>
    <table class="layui-table"
           lay-data="{ url:'/systemConfig/deliveryFeeTemplate/getList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'merchantId', width:150}">MerchantId</th>
            <th lay-data="{field:'name', width:150}">Name</th>
            <th lay-data="{field:'country', width:100}">Country</th>
            <th lay-data="{field:'customerTypeDesc', width:150}">Customer Type</th>
            <th lay-data="{field:'customerLevelDesc', width:150}">Customer Level</th>
            <th lay-data="{field:'settleTypeDesc', width:150}">Settle Type</th>
            <th lay-data="{field:'serviceProductDesc', width:150}">Service Product</th>
            <th lay-data="{field:'transportTypeDesc', width:150}">Transport Type</th>
            <th lay-data="{field:'origin', width:100}">Origin</th>
            <th lay-data="{field:'destination', width:100}">Destination</th>

            <th lay-data="{field:'status', width:150,templet:'<div>{{ formatTemplateStatus(d.status) }}</div>'}">
                Status
            </th>
            <th lay-data="{title:'Opt',fixed: 'right', width:300, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>


    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="800032">
        <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="details">Details</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="800033">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="800034">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="active">Active</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="800035">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
        </shiro:hasPermission>
    </script>
</div>

<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">

    function formatTemplateStatus(status) {

        if (status == 1) {
            return "Normal";
        }
        if (status == 2) {
            return "Delete";
        }
    }
    $(function () {

        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'details') {
                    details(data.id);
                }
                if (obj.event === 'edit') {
                    editConfig(data.id);
                }
                if (obj.event === 'active') {
                    activeConfig(data.id);
                }
                if (obj.event === 'delete') {
                    deleteConfig(data.id);
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
            var url = "/systemConfig/deliveryFeeTemplate/addPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px', '600px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }

        function editConfig(id) {

            var title = "Edit";
            var url = "/systemConfig/deliveryFeeTemplate/editPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {id: id},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px', '600px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }


        function deleteConfig(id) {
            //询问框
            layer.confirm('Confirm to Delete?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/systemConfig/deliveryFeeTemplate/delete.html",
                    dataType: "json",
                    data: {
                        id: id,
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

        function activeConfig(id) {
            //询问框
            layer.confirm('Confirm to Active?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/systemConfig/deliveryFeeTemplate/active.html",
                    dataType: "json",
                    data: {
                        id: id,
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

        function details(id) {
            $.ajax({
                url: "/systemConfig/deliveryFeeTemplate/details.html",
                type: 'GET',
                data: {"id": id},
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                                type: 1,
                                content: data,
                                area: ['700px', '500px'],
                                maxmin: true,
                                end: function () {
                                    location.reload();
                                }
                            })
                            ;
                    layer.full(index);
                }
            });
        }
    });


</script>
</body>
</html>