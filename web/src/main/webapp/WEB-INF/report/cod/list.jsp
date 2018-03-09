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
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From" name="createdTime_s">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To" name="createdTime_e">
            </div>
        </div>


    </div>

    <!-- 第二行搜索栏 -->

    <div class="layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Rider:</label>
            <div class="layui-input-inline">
                <input type="text" name="rider" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">PayMethod:</label>
            <div class="layui-input-inline">
                <input type="text" name="payMethod" autocomplete="off" class="layui-input">
            </div>
        </div>

    </div>



    <!-- 搜索栏的第三行 -->

    <div class="layui-form layui-row">

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">SignTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromReceiveTime" placeholder="From" name="signTime_s">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toReceiveTime" placeholder="To" name="signTime_e">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg3" style="width: 20.5%">
            <label class="layui-form-label">OrderPlatform:</label>
            <div class="layui-form-item layui-inline">
                <input type="text" name="orderPlatform" autocomplete="off" class="layui-input">
            </div>

        </div>

        <!-- 搜索按钮 -->

        <div class="layui-col-md">
            <button class="layui-btn layui-btn-normal search">Search</button>
            <shiro:hasPermission name="400017">
                <button class="layui-btn layui-btn-normal btn-export">Export</button>
            </shiro:hasPermission>
        </div>






    </div>
    <hr>

    <table class="layui-table"
           lay-data="{ url:'/report/cod/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}">OrderNo</th>
            <th lay-data="{field:'rider', width:100}">Rider</th>
            <th lay-data="{field:'money', width:100}">Amount Price</th>
            <th lay-data="{field:'weight', width:100}">Weight</th>
            <th lay-data="{field:'payType', width:100}">PayMethod</th>
            <th lay-data="{field:'signName', width:200}">SignTime</th>
            <th lay-data="{field:'phone', width:140}">Phone</th>
            <th lay-data="{field:'orderPlatform', width:140}">Order Platform</th>
            <th lay-data="{field:'createTime', width:200, templet:'<div>{{ formatDate(d.createTime) }}</div>'}">CreatedTime</th>
            <th lay-data="{field:'signTime', width:200, templet:'<div>{{ formatDate(d.signTime) }}</div>'}">SignTime</th>

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
               var sTime_sign = $("input[name='signTime_s']").val()=="" ? "" : Date.parse(new Date($("input[name='signTime_s']").val()))/1000;
               var eTime_sign = $("input[name='signTime_e']").val()=="" ? "" : Date.parse(new Date($("input[name='signTime_e']").val()))/1000+86400;
               if ((sTime_creat!="" && eTime_creat=="") || (eTime_creat!="" && sTime_creat=="") || (sTime_sign!="" && eTime_sign=="") || (eTime_sign!="" && sTime_sign=="")){
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
                        sTime_sign: sTime_sign,
                        eTime_sign: eTime_sign,
                        rider: $("input[name='rider']").val(),
                        payMethod: $("input[name='payMethod']").val(),
                        orderPlatform: $("input[name='orderPlatform']").val()
                    }
                });
            };

            //导出按钮

            $(".btn-export").on("click", function () {

                var orderNo = $("input[name='orderNo']").val(),
                    referenceNo = $("input[name='referenceNo']").val(),
                    orderTypes = $("select[name='orderType']").val(),
                    orderStatus = $("select[name='orderStatus']").val(),
                    fromCreatedTime = $("#fromCreatedTime").val(),
                    toCreatedTime = $("#toCreatedTime").val(),
                    platform = $("input[name='platform']").val();

                var url = "/order/deliveryOrder/export.html?orderNo="+orderNo+"&referenceNo="+referenceNo+"&orderTypes="+orderTypes+"&orderStatus="+orderStatus+"&fromCreatedTime="+fromCreatedTime+"&toCreatedTime="+toCreatedTime+"&platform="+platform;
                window.location.href = url;
            });

        });

    </script>
</body>
</html>
