<%@ page import="org.apache.commons.lang.RandomStringUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label" >Name:</label>
            <div class="layui-input-inline">
                <input type="text" name="name" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Department:</label>
            <div class="layui-form-item layui-inline" style="margin: 0px">
                <select lay-filter="departmentId" name="departmentId">
                    <option value="">Pls select...</option>
                    <c:forEach items="${departmentList}" var="r">
                        <option value=${r.departmentId}>${r.departmentName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>
    <div class="layui-row">
        <div class="layui-col-md3">
            <shiro:hasPermission name="300031">
                <button class="layui-btn layui-btn-normal add-staff">Add Staff</button>
            </shiro:hasPermission>
        </div>
    </div>

    <table class="layui-table"
           lay-data="{ url:'/staff/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'staffId', width:100}">Staff ID</th>
            <th lay-data="{field:'realName', width:150}">Real Name</th>
            <th lay-data="{field:'rider', width:100}">Rider</th>
            <th lay-data="{field:'departmentName', width:200}">
                Department
            </th>
            <th lay-data="{field:'statusDesc', width:100}">
                Status
            </th>
            <th lay-data="{field:'jobDesc', width:200}">Job</th>
            <th lay-data="{field:'phone', width:200}">Phone</th>
            <th lay-data="{field:'sex', width:120, templet:'<div>{{ formatSex(d.sex) }}</div>'}">Sex</th>
            <th lay-data="{field:'employTime', width:170, templet:'<div>{{ formatDate(d.employTime) }}</div>'}">
                Employ Time
            </th>
            <th lay-data="{field:'age', width:100}">Age</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:280, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="300033">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="300034">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="regular">Regular</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="300035">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="resigned">Resigned</a>
        </shiro:hasPermission>
    </script>
</div>

<!--Bottom Footer-->
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        var table;
        layui.use('table', function () {
            table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                var staffId = data.staffId;
                if (obj.event === 'detail') {
                    layer.msg(staffId);
                }
                if (obj.event === 'edit') {
                    editStaffPage(staffId);
                }
                if (obj.event === 'regular') {
                    regular(staffId);
                }
                if (obj.event === 'resigned') {
                    resigned(staffId);
                }

            });
        });

        $(".search").on("click", function () {
            reloadTable();
        })

        $(".add-staff").on("click", function () {
            editStaffPage("");
        });

        function reloadTable() {
            table.reload("${id0}", {
                where: {
                    name: $("input[name='name']").val(),
                    departmentId: $("select[name='departmentId']").val(),
                }
            });
        };

        function editStaffPage(staffId) {
            var url;
            if (staffId) {
                url = "/staff/editStaffPage.html";
            } else {
                url = "/staff/addStaffPage.html";
            }
            $.ajax({
                url: url,
                type: 'GET',
                data: {staffId: staffId},
                dataType: 'text',
                success: function (data) {
                    var index = parent.layer.open({
                        type: 1,
                        content: data,
                        area: ['800px'],
                        offset: ['100px', '250px'],
                        maxmin: true,
                        end: function () {
                            reloadCurrentPage();
                        }
                    });
                }
            });
        }

        function regular(staffId) {
            //询问框
            layer.confirm('Confirm to Regular?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/staff/regular.html",
                    dataType: "json",
                    data: {
                        staffId: staffId,
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

        function resigned(staffId) {
            //询问框
            layer.confirm('Confirm to Resigned?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/staff/resigned.html",
                    dataType: "json",
                    data: {
                        staffId: staffId,
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