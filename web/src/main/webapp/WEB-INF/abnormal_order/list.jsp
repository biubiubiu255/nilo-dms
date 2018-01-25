<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("abnormalType", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "abnormal_order_type"));
    request.setAttribute("imageType", "abnormal");
%>
<body>
<div class="box-body">
        <div class="layui-row">
            <div class="layui-col-md4 layui-col-lg3">
                <label class="layui-form-label" >OrderNo:</label>
                <div class="layui-input-inline">
                    <input type="text" name="orderNo" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-col-md4 layui-col-lg3">
                <label class="layui-form-label" >ReferenceNo:</label>
                <div class="layui-input-inline">
                    <input type="text" name="referenceNo" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-col-md8 layui-col-lg5">
                <label class="layui-form-label" >CreateTime:</label>
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From">
                </div>
                -
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="toCreatedTime" placeholder="To">
                </div>
            </div>

        </div>
        <div class="layui-form layui-row">
            <div class="layui-col-md4 layui-col-lg3">
                <label class="layui-form-label" >Type:</label>
                <div class="layui-form-item layui-inline" style="margin: 0px">
                    <select name="type">
                        <option value="">Pls select type...</option>
                        <c:forEach items="${abnormalType}" var="r">
                            <option value=${r.code}>${r.code}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="layui-col-md4 layui-col-lg3">
                <label class="layui-form-label" >Select All:</label>
                <div class="layui-form-item layui-inline" style="margin: 0px">
                    <input type="checkbox" name="all" title="" value="1"
                           lay-skin="primary" >
                </div>
            </div>

            <div class="layui-col-md1">
                <shiro:hasPermission name="400021">
                    <button class="layui-btn layui-btn-normal search">Search</button>
                </shiro:hasPermission>
            </div>
        </div>
        <hr>

        <table class="layui-table"
               lay-data="{ url:'/order/abnormalOrder/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
               lay-filter="demo">
            <thead>
            <tr>
                <th lay-data="{fixed: 'left',field:'abnormalNo', width:200}">AbnormalNo</th>
                <th lay-data="{field:'orderNo', width:250}">OrderNo</th>
                <th lay-data="{field:'referenceNo', width:200}">ReferenceNo</th>
                <th lay-data="{field:'abnormalType', width:150}">Abnormal Type</th>
                <th lay-data="{field:'statusDesc', width:100}">Status</th>
                <th lay-data="{field:'handleTypeDesc', width:150}">Handle Type</th>
                <th lay-data="{field:'handleTime', width:170, templet:'<div>{{ formatDate(d.handleTime) }}</div>'}">
                    Handled Time
                </th>
                <th lay-data="{field:'createdTime', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                    CreatedTime
                </th>
                <th lay-data="{field:'handleRemark', width:200}">Handle Remark</th>
                <th lay-data="{field:'remark', width:200}">Remark</th>
                <th lay-data="{title:'Image', width:100,templet:'<div>{{ showImageView(d.orderNo) }}</div>'}">Image</th>
                <th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
            </tr>
            </thead>
        </table>

        <script type="text/html" id="barDemo">
            <shiro:hasPermission name="400023">
                <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="handle">Handle</a>
            </shiro:hasPermission>
        </script>
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
                if (obj.event === 'handle') {
                    handlerAbnormal(data.abnormalNo, data.orderNo, data.referenceNo);
                }
            });


        });

        $(".search").on("click", function () {
            reloadTable();
        })

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    orderNo: $("input[name='orderNo']").val(),
                    referenceNo: $("input[name='referenceNo']").val(),
                    type: $("select[name='type']").val(),
                    all: $("input[name='all']:checked").val(),
                    fromCreatedTime: $("#fromCreatedTime").val(),
                    toCreatedTime: $("#toCreatedTime").val(),
                }
            });
        };

        function handlerAbnormal(abnormalNo, orderNo, referenceNo) {
            $.ajax({
                url: "/order/abnormalOrder/handlePage.html",
                type: 'GET',
                data: {"abnormalNo": abnormalNo, "orderNo": orderNo, "referenceNo": referenceNo},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        content: data,
                        area: ['800px','500px'],
                        offset: ['100px', '250px'],
                        end: function () {
                            reloadCurrentPage();
                        }
                    })
                }
            });
        }
    });
</script>
</body>
</html>