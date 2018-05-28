<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
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
            <label class="layui-form-label">OrderNo:</label>
            <div class="layui-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">BizType:</label>
            <div class="layui-inline">
                <select name="bizType" lay-filter="bizType" lay-verify="required" lay-search="">
                    <option value="">Select type....</option>
                    <option value="receive">Normal</option>
                    <c:forEach items="${abnormalTypeList}" var="r">
                        <option value=${r.code}>${r.value}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="600041">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <div class="layui-form layui-row">
        <div class="layui-col-md7">
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toTime" placeholder="To">
            </div>
        </div>

    </div>
    <hr>

    <table class="layui-table" lay-data="{ url:'/report/deliveryFeeList.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed:'left',field:'merchantId', width:150}">MerchantId</th>
            <th lay-data="{fixed:'left',field:'orderNo', width:250}">OrderNo</th>
            <th lay-data="{field:'bizType', width:150}">BizType</th>
            <th lay-data="{field:'fee', width:100}">Fee</th>
            <th lay-data="{field:'status', width:100}">Status</th>
            <th lay-data="{field:'createdTime', width:170,templet: '<div>{{formatDate(d.createdTime)}}</div>'}">Created
                Time
            </th>
            <th lay-data="{field:'updatedTime', width:170,templet: '<div>{{formatDate(d.createdTime)}}</div>'}">Updated
                Time
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
                        orderNo: $("input[name='orderNo']").val(),
                        bizType: $("select[name='bizType']").val(),
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