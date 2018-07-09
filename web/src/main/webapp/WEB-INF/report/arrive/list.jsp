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
        <div class="layui-col-xs12 layui-col-sm12 layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Waybil No:</label>
            <div class="layui-input-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>


        <div class="layui-col-xs12 layui-col-sm12 layui-col-md6 layui-col-lg6">
            <label class="layui-form-label">ScanTime:</label>
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

        <div class="layui-col-xs12 layui-col-sm10 layui-col-md5 layui-col-lg4">
            <label class="layui-form-label">ScanNetwork:</label>
            <div class="layui-input-inline">
                <select name="scanNetwork" lay-verify="required" lay-search="" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${list}" var="rider">
                        <option value="${rider.name}">${rider.name}</option>
                    </c:forEach>
                </select>
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

        <!-- 搜索按钮 -->
        <div class="layui-col-sm10 layui-col-md4 layui-col-lg4" style="margin-left: 2.4rem;">
            <button class="layui-btn layui-btn-normal btn-export">Export</button>
            <button class="layui-btn layui-btn-normal search">Search</button>
            <button class="layui-btn layui-btn-normal btn-pdf">View</button>
        </div>



    </div>
    <hr>

    <div id="me_tab" style="margin-top: -20px;">
        <table class="layui-table" lay-filter="demo" id="${id0}"></table>
    </div>


    <iframe scrolling="no" frameborder="0" src="/report/arrive/list.html?exportType=0" id="ifm" width="100%" height="100%" style="padding: 0px; display: none;"></iframe>

    <%@ include file="../../common/footer.jsp" %>
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
                    , value: new Date()
                });


                var tab = layui.table;
                tableMe = tab.render({
                    elem: '#${id0}'
                    ,url: '/report/arrive/list.html?exportType=2' //数据接口
                    ,page: true //开启分页
                    ,limit:10
                    ,method: 'post'
                    ,cols: [[ //表头
                        {field: 'orderNo', title: 'Waybill No', width:200, fixed: 'left'}
                        ,{field: 'lastNetwork', title: 'LastNetwork', width:200}
                        ,{field: 'scanNetwork', title: 'ScanNetwork', width:200}
                        ,{field: '', title: 'scanTime', width:200, templet:'<div>{{ formatDate(d.scanTime) }}</div>'}
                        ,{field: 'statusDesc', title: 'Status', width:200}
                        ,{field: 'weight', title: 'Weight', width:100}
                        ,{field: 'phone', title: 'Phone', width:200}
                        ,{field: 'recipients', title: 'ReceiveName', width:130}
                        ,{field: 'address', title: 'Address', width:300}

                    ]]
                    ,where: getParam(2, true)
                });

            });



            $(".search").on("click", function () {
                reloadTable();
            })

            $(".btn-pdf").on("click", function () {
                showPattern==1 ? showPattern=0 : showPattern=1;
                reloadTable();
            })

            $(".btn-export").on("click", function () {
                var param = getParam(1);
                if(showPattern==1){
                    url = "/report/arrive/list.html?limit=1000&" + param;
                }else {
                    url = "/report/arrive/list.html?" + param;
                }
                //console.log(url);
                window.location.href = url;
            });

            function reloadTable() {

                if (showPattern==0){
                    $("#me_tab").show();
                    $("#ifm").hide();
                    tableMe.reload({
                        where: getParam(2, true)
                    });
                }else if(showPattern==1){
                    $("#ifm").show();
                    $("#me_tab").hide();
                    var url = "/report/arrive/list.html";
                    document.getElementById("ifm").src = url + "?limit=1000&" + getParam(0);
                }


            };

            function getParam(dateType, isPojo){
                if (dateType=="" || dateType=='undefind') dateType=0;
                var sTime_creat = $("#fromCreatedTime").val()=="" ? "" : new Date($("#fromCreatedTime").val()+' 00:00:00').getTime()/1000;
                var eTime_creat = $("#toCreatedTime").val()==""   ? "" : new Date($("#toCreatedTime").val()+' 00:00:00').getTime()/1000;
                if(sTime_creat==eTime_creat) eTime_creat += 86400;
                var param = {
                    orderNo: $("input[name='orderNo']").val(),
                    fromCreatedTime: sTime_creat,
                    toCreatedTime: eTime_creat,
                    scanNetwork: $("select[name='scanNetwork']").val(),
                    exportType: dateType,
                    status: $("select[name='status']").val()
                };
                if (isPojo===true) return param;
                    else return jQuery.param( param );

            }
        });

    </script>
</body>
</html>
