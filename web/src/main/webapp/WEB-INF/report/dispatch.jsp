<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("merchantId", (String) session.getAttribute("merchantId"));
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
%>
<body>
<div class="box-body">
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Waybill No:</label>
            <div class="layui-input-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Status:</label>
            <div class="layui-input-inline">
                <select name="status" lay-filter="status" lay-search="">
                    <option value="">Select Status....</option>
                    <option value="20">Arrived</option>
                    <option value="30">Delivery</option>
                    <option value="40">Problem</option>
                    <option value="60">Refuse</option>
                    <option value="50">Sign</option>

                </select>
            </div>
        </div>



    </div>

    <div class="layui-form layui-row">
        <div class="layui-row">

            <div class="layui-col-md4 layui-col-lg4">
                <label class="layui-form-label">Rider:</label>
                <div class="layui-inline">
                    <select name="riderId" lay-verify="required" lay-filter="rider">
                        <option value="">choose or search....</option>
                        <c:forEach items="${list}" var="rider">
                            <option value=${rider.userId}>${rider.staffId}-${rider.realName}</option>
                        </c:forEach>
                    </select>
                    <%--<input type="text" name="orderNo" autocomplete="off" class="layui-input">--%>
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
    </div>

    <!-- 搜索栏的第二行 -->


    <div class="layui-form layui-row">

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label" style="width:120px">Outsource：</label>
            <div class="layui-input-inline">
                <select name="outsource" lay-filter="outsource" lay-search=""
                        <c:if test="${ not empty loading.rider}">disabled</c:if> style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${outsourceList}" var="outsource">
                        <option value="${outsource.outsource}"> ${outsource.outsourceName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <!-- 搜索按钮 -->
        <div class="layui-col-md4 layui-col-lg4" style="margin-left: 2.4rem;">
            <button class="layui-btn layui-btn-normal search">Search</button>
            <button class="layui-btn layui-btn-normal btn-pdf">View</button>
            <button class="layui-btn layui-btn-normal btn-export">Export</button>
        </div>

    </div>
    <hr>

    <div id="me_tab" style="margin-top: -20px;">
        <table class="layui-table" lay-filter="demo" id="${id0}"></table>
    </div>

    <iframe scrolling="no" frameborder="0" src="/report/dispatch/list.html?exportType=0" id="ifm" width="100%"
            height="100%" style="padding: 0px; display: none;"></iframe>

    <%@ include file="../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">


        $(function () {

            var showPattern = 0;
            var tableMe = null;

            layui.use(['laydate', 'table'], function () {
                var layDate = layui.laydate;
                layDate.render({
                    elem: '#fromCreatedTime'
                    , lang: 'en'
                    , isInitValue: true
                    , value: new Date()
                });
                layDate.render({
                    elem: '#toCreatedTime'
                    , lang: 'en'
                    , isInitValue: true
                    , value: new Date(new Date().getTime()+24*60*60*1000)
                });


                var tab = layui.table;
                tableMe = tab.render({
                    elem: '#${id0}'
                    ,url: '/report/dispatch/list.html?exportType=2' //数据接口
                    ,page: true //开启分页
                    ,limit:10
                    ,method: 'post'
                    ,cols: [[ //表头
                        {field: 'orderNo', title: 'Waybill No', width:150, fixed: 'left'}
                        ,{field: 'handleNo', title: 'HandleNo', width:100}
                        ,{field: 'referenceNo', title: 'ReferenceNo', width:150}
                        ,{field: 'orderType', title: 'OrderType', width:100}
                        ,{field: 'weight', title: 'Weight', width:100}
                        ,{field: 'rider', title: 'Rider', width:130}
                        //,{field: 'outsource', title: 'Outsource', width:130}
                        ,{field: 'statusDesc', title: 'Status', width:130}
                        ,{field: 'handleName', title: 'HandleName', width:130}
                        ,{field: '', title: 'CreatedTime', width:200, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}
                        ,{field: 'phone', title: 'Phone', width:200}
                        ,{field: 'address', title: 'Address', width:300}
                    ]]
                    ,where: getParam(2, true)
                });

            });


            $(".btn-export").on("click", function () {
                if (showPattern == 1) {
                    window.location.href = "/report/dispatch/list.html" + "?limit=1000&" + getParam(1);
                } else {
                    window.location.href = "/report/dispatch/list.html" + "?" + getParam(1);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            });

            $(".btn-pdf").on("click", function () {
                showPattern == 1 ? showPattern = 0 : showPattern = 1;
                reloadTable();
            });

            function reloadTable() {

                if (showPattern == 0) {

                    $("#me_tab").show();
                    $("#ifm").hide();
                    tableMe.reload({
                        where: getParam(2, true)
                    });
                } else if (showPattern == 1) {
                    $("#ifm").show();
                    $("#me_tab").hide();
                    var url = "/report/dispatch/list.html";
                    document.getElementById("ifm").src = url + "?limit=1000&" + getParam(0);
                }


            };

            //reloadTable();

            function getParam(dateType, isPojo) {
                if (dateType=="" || dateType=='undefind') dateType=0;
                var sTime_creat = $("#fromCreatedTime").val()=="" ? "" : new Date($("#fromCreatedTime").val()+' 00:00:00').getTime()/1000;
                var eTime_creat = $("#toCreatedTime").val()==""   ? "" : new Date($("#toCreatedTime").val()+' 00:00:00').getTime()/1000;
                if(sTime_creat==eTime_creat && eTime_creat!=0) eTime_creat += 86400;
                var status = $("select[name='status']").val();
                var riderId= $("select[name='riderId']").val();
                var outsource  = $("select[name='outsource']").val();


                var param = {
                    orderNo: $("input[name='orderNo']").val(),
                    fromCreatedTime: sTime_creat,
                    toCreatedTime: eTime_creat,
                    exportType: dateType,
                    status:status,
                    outsource: outsource,
                    riderId: riderId
                };
                if (isPojo === true) return param;
                else return jQuery.param(param);

            }
        });

    </script>
</body>
</html>
