<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("merchantId", (String) session.getAttribute("merchantId"));
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
%>
<body>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">OrderNo:</label>
            <div class="layui-input-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>


        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">CreatedTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From" name="createdTime_s">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To" name="createdTime_e">
            </div>
        </div>


    </div>

    <!-- 搜索栏的第二行 -->

    <div class="layui-form layui-row">

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">ReceiveTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromReceiveTime" placeholder="From" name="receiveTime_s">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toReceiveTime" placeholder="To" name="receiveTime_e">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Client Name:</label>
            <div class="layui-form-item layui-inline">
                <input type="text" name="clientName" autocomplete="off" class="layui-input">
            </div>
        </div>


        <!-- 搜索按钮 -->
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>

    <table class="layui-table"
           lay-data="{ url:'/report/receive/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}"><O></O>rderNo</th>
            <th lay-data="{field:'order_type', width:200}">OrderType</th>
            <th lay-data="{field:'order_platform', width:200}">OrderPlatform</th>
            <th lay-data="{field:'weight', width:100}">Weight</th>
            <th lay-data="{field:'name', width:100}">DriverName</th>
            <th lay-data="{field:'receive_time', width:200, templet:'<div>{{ formatDate(d.receive_time) }}</div>'}">ReceiveTime</th>
            <th lay-data="{field:'created_time', width:200, templet:'<div>{{ formatDate(d.created_time) }}</div>'}">CreatedTime</th>
            <th lay-data="{field:'created_time', width:200, templet:'<div>{{ showImageView(d.orderNo) }}</div>'}">View</th>
        </tr>
        </thead>
    </table>
    <%@ include file="../../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {

            layui.use(['element', 'form', 'laydate'], function () {
                var layDate = layui.laydate;
                layDate.render({
                    elem: '#fromCreatedTime'
                    , lang: 'en'
                });
                layDate.render({
                    elem: '#toCreatedTime'
                    , lang: 'en'
                });

                layDate.render({
                    elem: '#fromReceiveTime'
                    , lang: 'en'
                });
                layDate.render({
                    elem: '#toReceiveTime'
                    , lang: 'en'
                });
                /*
                layDate.render({
                    elem: '#toMonth'
                    , lang: 'en'
                    , type: 'month'
                    , format: 'yyyyMM'
                });

                */
            });
            var table;
            layui.use('table', function () {
                table = layui.table;
                table.on('tool(demo)');
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            function reloadTable() {
               var sTime_creat = $("input[name='createdTime_s']").val()=="" ? "" : Date.parse(new Date($("input[name='createdTime_s']").val()))/1000;
               var eTime_creat = $("input[name='createdTime_e']").val()=="" ? "" : Date.parse(new Date($("input[name='createdTime_e']").val()))/1000+86400;
               var sTime_receive = $("input[name='receiveTime_s']").val()=="" ? "" : Date.parse(new Date($("input[name='receiveTime_s']").val()))/1000;
               var eTime_receive = $("input[name='receiveTime_e']").val()=="" ? "" : Date.parse(new Date($("input[name='receiveTime_e']").val()))/1000+86400;
               if ((sTime_creat!="" && eTime_creat=="") || (eTime_creat!="" && sTime_creat=="") || (sTime_receive!="" && eTime_receive=="") || (eTime_receive!="" && sTime_receive=="")){
                   layui.use('layer', function () {
                       var layer = layui.layer;
                       layer.msg('Please select the full date', {icon: 0, time: 2000});
                   });
                   return ;
               }
                table.reload("${id0}", {
                    where: {
                        orderNo: $("input[name='orderNo']").val(),
                        sTime_creat: sTime_creat,
                        eTime_creat: eTime_creat,
                        sTime_receive: sTime_receive,
                        eTime_receive: eTime_receive,
                        mother: $("input[name='month']").val(),
                        clientName: $("input[name='clientName']").val()
                    }
                });
            };
        });

    </script>
</body>
</html>
