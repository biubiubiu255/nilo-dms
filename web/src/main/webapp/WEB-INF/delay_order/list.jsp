<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">

    <div class="layui-row">
        <div class="layui-col-md1">
            <button class="layui-btn btn-search">Search
            </button>
        </div>
    </div>

    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content ">
                <div class="layui-form layui-row">
                    <div class="layui-row">
                        <div class="layui-col-md4 layui-col-lg3">
                            <label class="layui-form-label" style="width:110px">OrderNo:</label>
                            <div class="layui-input-inline">
                                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-col-md8 layui-col-lg5">
                            <label class="layui-form-label" style="width:110px">CreateTime:</label>
                            <div class="layui-inline">
                                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From">
                            </div>
                            -
                            <div class="layui-inline">
                                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To">
                            </div>
                        </div>
                        <div class="layui-col-md1 layui-col-lg1">
                            <shiro:hasPermission name="400071">
                                <button class="layui-btn layui-btn-normal search">Search</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <hr>

    <table class="layui-table"
           lay-data="{ url:'/order/delay/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:250}">OrderNo</th>
            <th lay-data="{field:'delayReason', width:300}">Type</th>
            <th lay-data="{field:'statusDesc', width:150}">Status</th>
            <th lay-data="{field:'allowTimes', width:150}">AllowTimes</th>
            <th lay-data="{field:'delayTimes', width:150}">DelayTimes</th>
            <th lay-data="{field:'createdTime', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{field:'remark', width:200}">Remark</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="400072">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="problem">Problem</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="400073">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="resend">Resend</a>
        </shiro:hasPermission>
    </script>
</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        layui.use(['form','element','laydate'], function () {
            var layDate = layui.laydate;
            layDate.render({
                elem: '#fromCreatedTime'
                , lang: 'en'
            });
            layDate.render({
                elem: '#toCreatedTime'
                , lang: 'en'
            });
        });

        var table;
        layui.use('table', function () {
            table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    var orderNo = data.orderNo;
                    layer.msg(orderNo);
                }
                if (obj.event === 'problem') {
                    problem(data.orderNo);
                }
                if (obj.event === 'resend') {
                    resend(data.orderNo);
                }
            });


        });

        $(".search").on("click", function () {
            reloadTable();
        })
        $(".btn-search").on("click", function () {
            $(".layui-colla-content").toggleClass("layui-show");
            $(".btn-search").toggleClass("layui-btn-warm");
        })
        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    orderNo: $("input[name='orderNo']").val(),
                    fromCreatedTime: $("#fromCreatedTime").val(),
                    toCreatedTime: $("#toCreatedTime").val(),
                }
            });
        };

        function problem(orderNo) {
            $.ajax({
                url: "/order/delay/problemPage.html",
                type: 'GET',
                data: {"orderNo": orderNo},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        content: data,
                        area: ['800px', '600px'],
                        offset: ['100px', '250px'],
                        end: function () {
                            reloadCurrentPage();
                        }
                    })
                }
            });
        }

        function resend(orderNo) {
            var load = layer.load(2);
            $.ajax({
                url: "/order/delay/resend.html",
                type: 'POST',
                data: {"orderNo": orderNo},
                dataType: 'json',
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            layer.closeAll();
                            reloadCurrentPage();
                        });
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    }
                },
                complete: function () {
                    layer.close(load);
                }
            });
        }
    });
</script>
</body>
</html>