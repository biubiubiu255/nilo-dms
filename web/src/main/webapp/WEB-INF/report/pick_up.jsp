<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("nowDate", DateUtil.formatCurrent("yyyy-MM-dd"));
%>
<body>

<div class="box-body">
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Rider:</label>
            <div class="layui-inline">
                <select name="rider" lay-verify="required" lay-search="" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${list}" var="rider">
                        <option value="${rider.userId}">${rider.name}</option>
                    </c:forEach>
                </select></div>
        </div>
        <div class="layui-col-md7 layui-col-lg5">
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toTime" placeholder="To">
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>

    <table class="layui-table" lay-data="{ url:'/report/pickUpList.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed:'left',field:'orderNo', width:200}">OrderNo</th>
            <th lay-data="{fixed:'left',field:'referenceNo', width:200,templet: '<div>{{d.deliveryOrder.referenceNo}}</div>'}">
                ReferenceNo
            </th>
            <th lay-data="{field:'statusDesc', width:150}">Task Status</th>
            <th lay-data="{field:'statusDesc1', width:150,templet: '<div>{{d.deliveryOrder.statusDesc}}</div>'}">Order
                Status
            </th>
            <th lay-data="{field:'handledName', width:100}">Rider</th>
            <th lay-data="{field:'country', width:100,templet: '<div>{{d.deliveryOrder.country}}</div>'}">Country</th>
            <th lay-data="{field:'weight', width:100,templet: '<div>{{d.deliveryOrder.weight}}</div>'}">Weight</th>
            <th lay-data="{field:'goodsType', width:120,templet: '<div>{{d.deliveryOrder.goodsType}}</div>'}">
                GoodsType
            </th>
            <th lay-data="{field:'address', width:300,templet: '<div>{{d.deliveryOrder.senderInfo.address}}</div>'}">
                Pick Up Address
            </th>
            <th lay-data="{field:'orderTime', width:170, templet:'<div>{{ formatDate(d.handledTime) }}</div>'}">
                OptTime
            </th>
        </tr>
        </thead>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            form.render();
        })

        layui.use('laydate', function () {
            var layDate = layui.laydate;
            var d = new Date();
            var now = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
            layDate.render({
                elem: '#fromTime',
                lang: 'en',
            });
            layDate.render({
                elem: '#toTime',
                lang: 'en',
            });
        });
        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    var id = data.id;
                    layer.msg(id);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            var reloadTable = function (item) {

                table.reload("${id0}", {
                    where: {
                        rider: $("select[name='rider']").val(),
                        fromTime: $("#fromTime").val(),
                        toTime: $("#toTime").val(),
                    }
                });
            };

        });
    });

</script>
</body>
</html>