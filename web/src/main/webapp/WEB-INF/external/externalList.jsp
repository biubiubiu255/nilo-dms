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
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">International Order:</label>
            <div class="layui-input-inline">
                <input type="text" name="internationalNo" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">ReceiveTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreateTime" placeholder="From" name="createTime_s">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreateTime" placeholder="To" name="createTime_e">
            </div>
        </div>


    </div>
    <!-- 搜索栏的第二行 -->
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">ClientName:</label>
            <div class="layui-form-item layui-inline">
                <input type="text" name="clientName" autocomplete="off" class="layui-input">
            </div>
        </div>
        <!-- 搜索按钮 -->
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
           lay-data="{ url:'/waybill/external/externalList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'id', width:100, align:'center', templet: '<div>{{d.id}}</div>'}">No</th>
            <th lay-data="{field:'orderNo', width:150, align:'center', templet: '<div>{{d.orderNo}}</div>'}">International order</th>
            <th lay-data="{field:'clientName', width:150, align:'center',templet: '<div>{{d.clientName}}</div>'}">ClientName</th>
            <th lay-data="{field:'channels', width:150, align:'center',templet: '<div>{{d.channels}}</div>'}">Channels</th>
            <th lay-data="{field:'creator', width:150, align:'center',templet: '<div>{{d.creator}}</div>'}">Creator</th>
            <th lay-data="{field:'weight', width:150, align:'center',templet: '<div>{{d.weight}}</div>'}">Weight(KG)</th>
            <th lay-data="{field:'receiveTime', width:170, templet:'<div>{{ formatDate(d.receiveTime) }}</div>'}">receiveTime</th>
            <th lay-data="{field:'CreateTime', width:170, templet:'<div>{{ formatDate(d.createTime) }}</div>'}">CreateTime</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:172, align:'center', toolbar: '#barDemo'}"></th>
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

        layui.use(['element', 'form', 'laydate'], function () {
            var layDate = layui.laydate;
            layDate.render({
                elem: '#fromCreateTime'
                , lang: 'en'
            });
            layDate.render({
                elem: '#toCreateTime'
                , lang: 'en'
            });
        });

        var table;
        layui.use('table', function () {
            table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;

                if (obj.event === 'edit') {
                    editUser(data);
                }
                if (obj.event === 'delete') {
                    delUser(data);
                }

            });

            $(".search").on("click", function () {
                reloadTable();
            })


        });

        var reloadTable = function (item) {
            var sTime_create = $("input[name='createTime_s']").val() == "" ? "" : Date.parse(new Date($("input[name='createTime_s']").val())) / 1000;
            var eTime_create = $("input[name='createTime_e']").val() == "" ? "" : Date.parse(new Date($("input[name='createTime_e']").val())) / 1000 + 86400;
            table.reload("${id0}", {
                where: {
                    orderNo: $("input[name='internationalNo']").val() ,
                    clientName: $("input[name='clientName']").val() ,
                    sTime_create: sTime_create ,
                    eTime_create: eTime_create
                }
            });
        };

        $("button.add-user").on("click", function () {
        	addExpress();
        });

        function delUser(d) {
            //询问框
            layer.confirm('Confirm to delete?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/waybill/external/deleExternalInfo.html",
                    dataType: "json",
                    data: {
                    	id: d.id
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

        function editUser(d) {
        	
            var url = "/waybill/external/editInfoPage.html?id=" + d.id
            var title = "Edit External";
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

        function addExpress() {
            var url = "/waybill/external/addPage.html";
            var title = "Add External";
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