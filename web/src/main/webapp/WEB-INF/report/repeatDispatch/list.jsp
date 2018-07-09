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
<div class="box-body">
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Waybill No:</label>
            <div class="layui-input-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>
<%--
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">DeliveryType:</label>
            <div class="layui-input-inline">
                <select name="dispatchType" lay-filter="status" lay-search="">
                    <option value="">Select Status....</option>
                    <option value="riderDelivery">RiderDelivery</option>
                    <option value="package">Station</option>
                    <option value="waybill">Express</option>
                </select>
            </div>
        </div>--%>


        <div class="layui-col-md4 layui-col-lg3">
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


<%--        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Rider:</label>
            <div class="layui-inline">
                <select lay-filter="riderLay" name="rider">
                    <option value="">choose or search....</option>
                    <c:forEach items="${riderList}" var="rider">
                        <option value=${rider.userId}>${rider.staffId}-${rider.realName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>--%>


    </div>
    <div class="layui-form layui-row">



        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">OrderType:</label>
            <div class="layui-inline">
                <select name="orderType" lay-verify="required" lay-filter="orderTypeLay" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${orderTypeList}" var="r">
                        <option value=${r.code}>${r.code}</option>
                    </c:forEach>
                </select></div>
        </div>


        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To">
            </div>
        </div>


        <div class="layui-col-md4 layui-col-lg3">
            <button class="layui-btn layui-btn-normal btn-export">Export</button>
            <button class="layui-btn layui-btn-normal search">Search</button>
            <button class="layui-btn layui-btn-normal btn-pdf">View</button>
        </div>

<%--        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">NextStation:</label>
            <div class="layui-inline">
                <select lay-filter="nextStationCodeLay" name="nextStationCode">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStations}" var="r">
                        &lt;%&ndash;<option value=${r.code}>${r.name}</option>&ndash;%&gt;
                        <option value=${r.code}>${r.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>--%>


    <hr>
</div>

</div>


<div id="me_tab" style="margin-top: -20px;">
    <table class="layui-table" lay-filter="demo" id="${id0}"></table>
</div>


<iframe scrolling="no" frameborder="0" src="/report/repeat_send/list.html?exportType=0" id="ifm" width="100%" height="100%" style="padding: 0px; display: none;"></iframe>

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
                    ,url: '/report/repeat_send/list.html?exportType=2' //数据接口
                    ,page: true //开启分页
                    ,limit:10
                    ,method: 'post'
                    ,cols: [[ //表头
                        {field: 'orderNo', title: 'Waybill No', width:150, fixed: 'left'}
                        ,{field: 'referenceNo', title: 'ReferenceNo', width:190}
                        ,{field: 'dispatchNum', title: 'Times', width:116}
                        ,{field: 'orderType', title: 'OrderType', width:100}
                        ,{field: 'weight', title: 'Weight', width:100}
                        ,{field: 'statusDesc', title: 'Status', width:130}
                        ,{field: '', title: 'CreatedTime', width:200, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}
                        ,{field: 'receiveName', title: 'Name', width:153}
                        ,{field: 'phone', title: 'Phone', width:170}
                        ,{field: 'address', title: 'Address', width:300}
                    ]]
                    ,where: getParam(2, true)
                });

            });

            //reloadTable();

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
                    url = "/report/repeat_send/list.html?limit=1000&" + param;
                }else {
                    url = "/report/repeat_send/list.html?" + param;
                }
                console.log(url);
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
                    var url = "/report/repeat_send/list.html";
                    document.getElementById("ifm").src = url + "?limit=1000&" + getParam(0);
                }


            };

            //reloadTable();

            function getParam(dateType, isPojo){
                if (dateType=="" || dateType=='undefind') dateType=0;
                var sTime_creat = $("#fromCreatedTime").val()=="" ? "" : new Date($("#fromCreatedTime").val()+' 00:00:00').getTime()/1000;
                var eTime_creat = $("#toCreatedTime").val()==""   ? "" : new Date($("#toCreatedTime").val()+' 00:00:00').getTime()/1000;
                if(sTime_creat==eTime_creat) eTime_creat += 86400;
                var param = {
                    orderNo: $("input[name='orderNo']").val(),
                    //driver: $("input[name='driver']").val(),
                    fromCreatedTime: sTime_creat,
                    toCreatedTime: eTime_creat,
                    //handleNo: $("input[name='handleNo']").val(),
                    orderType: $("select[name='orderType']").val(),
                    expressCode: $("select[name='expressCode']").val(),
                    //nextStationCode: $("select[name='nextStationCode']").val(),
                    exportType: dateType,
                    //dispatchType: $("select[name='dispatchType']").val(),
                    status: $("select[name='status']").val()
                };

                if (isPojo===true) return param;
                else return jQuery.param( param );

            }
        });

    </script>
</body>
</html>
