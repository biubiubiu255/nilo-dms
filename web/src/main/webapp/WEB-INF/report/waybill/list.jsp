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
    <button class="layui-btn btn-search">Search</button>
    <div class="layui-colla-content ">
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">Waybill No:</label>
            <div class="layui-input-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">ReferenceNo:</label>
            <div class="layui-form-item layui-inline">
                <input type="text" name="referenceNo" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">OrderType:</label>
            <div class="layui-inline">
                <select name="orderType" lay-verify="required" lay-filter="orderTypeLay" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${orderTypeList}" var="r">
                        <option value=${r.code}>${r.code}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

    </div>
    <div class="layui-form layui-row">


        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">ArrivedNetWork:</label>
            <div class="layui-inline">
                <select lay-filter="firstArriveNetworkId" name="firstArriveNetworkId">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStations}" var="r">
                        <option value=${r.code}>${r.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
<%--
    </div>
    <div class="layui-form layui-row">--%>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">DeliverNetWork:</label>
            <div class="layui-inline">
                <select lay-filter="lastDeliverNetworkId" name="lastDeliverNetworkId">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStations}" var="r">
                        <option value=${r.code}>${r.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">ExpressName:</label>
            <div class="layui-inline">
                <select name="lastDeliverExpressCode" lay-verify="required" lay-filter="lastDeliverExpressCode">
                    <option value="">choose or search....</option>
                    <c:forEach items="${expressList}" var="r">
                        <option value=${r.expressCode}>${r.expressName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

    <div class="layui-form layui-row">

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">SignNetWork:</label>
            <div class="layui-inline">
                <select lay-filter="signNetworkId" name="signNetworkId">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStations}" var="r">
                        <option value=${r.code}>${r.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">SignName:</label>
            <div class="layui-input-inline">
                <input type="text" name="signHandleByName" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label" style="width:120px">Outsource：</label>
            <div class="layui-input-inline">
                <select name="signOutsourceCode" lay-filter="signOutsourceCode" lay-search=""
                        <c:if test="${ not empty loading.rider}">disabled</c:if> style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${outsourceList}" var="outsource">
                        <option value="${outsource.outsource}"> ${outsource.outsourceName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

    </div>

    <div class="layui-form layui-row">


            <div class="layui-col-md4 layui-col-lg4">
                <label class="layui-form-label">Rider:</label>
                <div class="layui-inline">
                    <select name="lastDeliverRiderId" lay-verify="required" lay-filter="lastDeliverRiderId">
                        <option value="">choose or search....</option>
                        <c:forEach items="${riderList}" var="rider">
                            <option value=${rider.userId}>${rider.staffId}-${rider.realName}</option>
                        </c:forEach>
                    </select>
                    <%--<input type="text" name="orderNo" autocomplete="off" class="layui-input">--%>
                </div>
            </div>

            <div class="layui-col-md6 layui-col-lg5">
                <label class="layui-form-label">OrderTime:</label>
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="fromOrderTime" placeholder="From" lay-type="date_a" lay-key="1">
                </div>
                -
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="toOrderTime" placeholder="To" lay-type="date_b" lay-key="2">
                </div>
            </div>


        </div>


        <div class="layui-form layui-row">
            <div class="layui-col-md6 layui-col-lg6">
                <label class="layui-form-label">OrderCreateTime:</label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" id="fromOrderCreatedTime" placeholder="From" lay-type="date_a" lay-key="3" lay-layd-init="1">
                </div>
                -
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="toOrderCreatedTime" placeholder="To" lay-type="date_b" lay-key="4" lay-layd-init="1">
                </div>
            </div>

            <div class="layui-col-md6 layui-col-lg6">
                <label class="layui-form-label">ArriveTime:</label>
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="fromFirstArriveTime" placeholder="From" lay-type="date_a" lay-key="5">
                </div>
                -
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="toFirstArriveTime" placeholder="To" lay-type="date_b" lay-key="6">
                </div>
            </div>


        </div>

        <div class="layui-form layui-row">


            <div class="layui-col-md6 layui-col-lg6">
                <label class="layui-form-label">DeliverTime:</label>
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="fromLastDeliverTime" placeholder="From"  lay-type="date_a" lay-key="7">
                </div>
                -
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="toLastDeliverTime" placeholder="To" lay-type="date_b" lay-key="8">
                </div>
            </div>

            <div class="layui-col-md6 layui-col-lg6">
                <label class="layui-form-label">SignTime:</label>
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="fromSignTime" placeholder="From" lay-type="date_a" lay-key="9">
                </div>
                -
                <div class="layui-inline">
                    <input type="text" class="layui-input" id="toSignTime" placeholder="To" lay-type="date_b" lay-key="10">
                </div>
            </div>




        </div>


        <div class="layui-form layui-row">


            <div class="layui-col-md4 layui-col-lg3">
                <button class="layui-btn layui-btn-normal btn-export">Export</button>
                <button class="layui-btn layui-btn-normal search">Search</button>
                <button class="layui-btn layui-btn-normal btn-pdf">View</button>
            </div>


        </div>




        <hr>
        </div>

    </div>
</div>




    <div id="me_tab" style="margin-top: -20px;">
        <table class="layui-table" lay-filter="demo" id="${id0}"></table>
    </div>

    <iframe scrolling="no" frameborder="0" src="/report/waybill/list.html?exportType=0" id="ifm" width="100%" height="100%" style="padding: 0px; display: none;"></iframe>

<%@ include file="../../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {

            $(".btn-search").on("click", function () {
                $(".layui-colla-content").toggleClass("layui-show");
                $(".btn-search").toggleClass("layui-btn-warm");
            });

            var showPattern = 0;
            var tableMe;
            layui.use(['laydate','table'], function () {

                var laydate = layui.laydate;
                var laydateParam = {};

                lay("input[lay-type='date_a']").each(function(i, e){
                    laydateParam = {elem: this, lang: 'en'};
                    //console.log($(this).attr("lay-layd-init"));
                    if($(this).attr("lay-layd-init")=="1"){
                        laydateParam['isInitValue'] = true;
                        laydateParam['value'] = new Date();
                    };
                    //console.log(e);
                    laydate.render(laydateParam);
                });
                lay("input[lay-type='date_b']").each(function(i, e){
                    laydateParam = {elem: this, lang: 'en'};
                    if($(e).attr("lay-layd-init")=="1"){
                        laydateParam['isInitValue'] = true;
                        laydateParam['value'] = new Date();
                    };
                    laydate.render(laydateParam);
                });

                var tab = layui.table;
                tableMe = tab.render({
                    elem: '#${id0}'
                    ,url: '/report/waybill/list.html?exportType=2' //数据接口
                    ,page: true //开启分页
                    ,limit:10
                    ,method: 'post'
                    ,cols: [[ //表头
                        {field: 'orderNo', title: 'Waybill No', width:150, fixed: 'left'}
                       ,{field: 'referenceNo', title: 'ReferenceNo', width:170}
                       ,{field: 'orderType', title: 'OrderType', width:100}
                       ,{field: 'orderStatusDesc', title: 'OrderStatus', width:130}
                       ,{field: 'firstArrive30NetworkName', title: 'FirstArriveNetworkName', width:200}
                       ,{field: 'lastDeliverNetworkName', title: 'LastDeliverNetworkName', width:200}
                       ,{field: 'lastDeliverExpressCode', title: 'LastDeliverExpressCode', width:200}
                       ,{field: 'signHandleByName', title: 'SignHandleByName', width:150}
                       ,{field: 'signNetworkName', title: 'SignNetworkName', width:150}
                       ,{field: 'lastDeliverRiderName', title: 'LastDeliverRiderName', width:170}
                       ,{title: 'OrderTime', width:200, templet:'<div>{{ formatDate(d.orderTime) }}</div>'}
                       ,{title: 'OrderCreatedTime', width:200, templet:'<div>{{ formatDate(d.orderCreatedTime) }}</div>'}
                       ,{title: 'FirstArriveTime', width:200, templet:'<div>{{ formatDate(d.firstArriveTime) }}</div>'}
                       ,{title: 'LastDeliverTime', width:200, templet:'<div>{{ formatDate(d.lastDeliverTime) }}</div>'}
                       ,{title: 'SignTime', width:200, templet:'<div>{{ formatDate(d.signTime) }}</div>'}
                       ,{title: 'CreatedTime', width:200, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}
                    ]]
                    ,where: getParam(2, true)

                });

                //table.on('tool(demo)');
                //reloadTable();
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
                    url = "/report/waybill/list.html?limit=1000&" + param;
                }else {
                    url = "/report/waybill/list.html?" + param;
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
                    var url = "/report/waybill/list.html";
                    document.getElementById("ifm").src = url + "?limit=1000&" + getParam(0);
                }

            };

            function getParam(dateType, isPojo){

                if (dateType=="" || dateType=='undefind') dateType=0;

                var fromOrderTime = $("#fromOrderTime").val()=="" ? "" : new Date($("#fromOrderTime").val()+' 00:00:00').getTime()/1000;
                var toOrderTime   = $("#toOrderTime").val()==""   ? "" : new Date($("#toOrderTime").val()+' 00:00:00').getTime()/1000;

                var fromOrderCreatedTime = $("#fromOrderCreatedTime").val()=="" ? "" : new Date($("#fromOrderCreatedTime").val()+' 00:00:00').getTime()/1000;
                var toOrderCreatedTime   = $("#toOrderCreatedTime").val()==""   ? "" : new Date($("#toOrderCreatedTime").val()+' 00:00:00').getTime()/1000;

                var fromFirstArriveTime = $("#fromFirstArriveTime").val()=="" ? "" : new Date($("#fromFirstArriveTime").val()+' 00:00:00').getTime()/1000;
                var toFirstArriveTime   = $("#toFirstArriveTime").val()==""   ? "" : new Date($("#toFirstArriveTime").val()+' 00:00:00').getTime()/1000;

                var fromLastDeliverTime = $("#fromLastDeliverTime").val()=="" ? "" : new Date($("#fromLastDeliverTime").val()+' 00:00:00').getTime()/1000;
                var toLastDeliverTime   = $("#toLastDeliverTime").val()==""   ? "" : new Date($("#toLastDeliverTime").val()+' 00:00:00').getTime()/1000;

                var fromSignTime = $("#fromSignTime").val()=="" ? "" : new Date($("#fromSignTime").val()+' 00:00:00').getTime()/1000;
                var toSignTime   = $("#toSignTime").val()==""   ? "" : new Date($("#toSignTime").val()+' 00:00:00').getTime()/1000;

                //var fromCreatedTime = $("#fromCreatedTime").val()=="" ? "" : new Date($("#fromCreatedTime").val()+' 00:00:00').getTime()/1000;
                //var toCreatedTime   = $("#toCreatedTime").val()==""   ? "" : new Date($("#toCreatedTime").val()+' 00:00:00').getTime()/1000;
                if(fromOrderTime==toOrderTime && toOrderTime!=0) toOrderTime += 86400;
                if(fromOrderCreatedTime==toOrderCreatedTime && toOrderCreatedTime!=0) toOrderCreatedTime += 86400;
                if(fromFirstArriveTime==toFirstArriveTime && toFirstArriveTime!=0) toFirstArriveTime += 86400;
                if(fromLastDeliverTime==toLastDeliverTime && toLastDeliverTime!=0) toLastDeliverTime += 86400;
                if(fromSignTime==toSignTime && toSignTime!=0) toSignTime += 86400;

                var param = {
                    orderNo: $("input[name='orderNo']").val(),
                    referenceNo: $("input[name='referenceNo']").val(),

                    orderType: $("select[name='orderType']").val(),
                    //orderStatus: $("select[name='orderStatus']").val(),
                    firstArriveNetworkId: $("select[name='firstArriveNetworkId']").val(),
                    lastDeliverNetworkId: $("select[name='lastDeliverNetworkId']").val(),

                    signNetworkId: $("select[name='signNetworkId']").val(),
                    signHandleByName: $("input[name='signHandleByName']").val(),
                    signOutsourceCode: $("select[name='signOutsourceCode']").val(),
                    lastDeliverExpressCode: $("select[name='lastDeliverExpressCode']").val(),
                    lastDeliverRiderId: $("select[name='lastDeliverRiderId']").val(),



                    fromOrderTime: fromOrderTime,
                    toOrderTime  : toOrderTime,

                    fromOrderCreatedTime : fromOrderCreatedTime,
                    toOrderCreatedTime   : toOrderCreatedTime,

                    fromFirstArriveTime  : fromFirstArriveTime,
                    toFirstArriveTime    : toFirstArriveTime,

                    fromLastDeliverTime  : fromLastDeliverTime,
                    toLastDeliverTime    : toLastDeliverTime,

                    fromSignTime : fromSignTime,
                    toSignTime   : toSignTime,

                    //fromCreatedTime : fromCreatedTime,
                    //toCreatedTime   : toCreatedTime,

                    exportType: dateType

                };

                //console.log(param);
                if (isPojo===true) return param;
                else return jQuery.param( param );

            }



        });

    </script>
</body>
</html>
