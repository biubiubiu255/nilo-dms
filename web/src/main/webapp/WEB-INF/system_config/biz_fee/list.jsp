<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("abnormalTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "abnormal_order_type"));

%>
<body>
<div class="box-body">

    <div class="layui-form layui-row">
        <div class="layui-col-md3">
            Biz Type：
            <div class="layui-inline">
                <select name="bizType" lay-filter="bizType" lay-verify="required" lay-search="" style="display: none">
                    <option value="">Select type....</option>
                    <option value="receive" >Normal</option>
                    <c:forEach items="${abnormalTypeList}" var="r">
                        <option value=${r.code}>${r.value}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="800065">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>
    <div class="layui-row">
        <div class="layui-col-md1">
            <shiro:hasPermission name="800061">
                <button class="layui-btn layui-btn-normal add">Add</button>
            </shiro:hasPermission>
        </div>
    </div>
    <table class="layui-table"
           lay-data="{ url:'/systemConfig/bizFee/getList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'optType', width:150}">Biz Type</th>
            <th lay-data="{field:'fee', width:150}">Times</th>
            <th lay-data="{field:'status', width:150}">Status</th>
            <th lay-data="{field:'remark', width:300}">Remark</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:300, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>


    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="800062">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="800064">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="active">Active</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="800063">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
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
                if (obj.event === 'active') {
                    activeConfig(data.optType);
                }
                if (obj.event === 'delete') {
                    deleteConfig(data.optType);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            function reloadTable(item) {
                table.reload("${id0}", {
                    where: {
                        bizType: $("input[name='bizType']").val()
                    }
                });
            };


        });

        $(".add").on("click", function () {
            addConfig();
        })

        function addConfig() {

            var title = "Add";
            var url = "/systemConfig/bizFee/addPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['600px', '400px'],
                        content: data,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }

        function editConfig(data) {

            var title = "Edit";
            var url = "/systemConfig/bizFee/editPage.html";
            $.ajax({
                url: url,
                type: 'GET',
                data: {optType: data.optType},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['600px', '400px'],
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
                    url: "/systemConfig/bizFee/delete.html",
                    dataType: "json",
                    data: {
                        bizType: data,
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
                    url: "/systemConfig/bizFee/active.html",
                    dataType: "json",
                    data: {
                        bizType: data,
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