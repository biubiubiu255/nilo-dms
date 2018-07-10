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
    <div class="layui-row layui-form">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Waybill No:</label>
            <div class="layui-input-inline">
                <input type="text" name="orderNo" autocomplete="off" class="layui-input">
            </div>
        </div>


        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">ScanTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From" name="createdTime_s">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toCreatedTime" placeholder="To" name="createdTime_e">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label" style="width:110px">Rider:</label>
            <div class="layui-form-item layui-inline" style="margin: 0px">
                <select lay-filter="select-rider-fy" name="rider">
                    <option value="">choose or search....</option>
                    <c:forEach items="${riderList}" var="rider">
                        <option value="${rider.userId}">${rider.staffId}-${rider.realName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>


    </div>

    <!-- 搜索栏的第二行 -->

    <div class="layui-form layui-row">


        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Network:</label>
            <div class="layui-inline">
                <select lay-filter="nextStationCodeLay" name="nextStationCode">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStations}" var="r">
                        <option value=${r.code}>${r.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>


        <div class="layui-col-md4 layui-col-lg3">
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

    <iframe scrolling="no" frameborder="0" src="/report/sign/list.html?exportType=0" id="ifm" width="100%" height="100%" style="padding: 0px; display: none;"></iframe>

    <%@ include file="../../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {

            showPattern = 0;
            var tableMe = null;

            layui.use(['laydate','table'], function () {
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
                    ,url: '/report/sign/list.html?exportType=2' //数据接口
                    ,page: true //开启分页
                    ,limit:10
                    ,method: 'post'
                    ,cols: [[ //表头
                        {field: 'orderNo', title: 'Waybill No', width:200, fixed: 'left'}
                        ,{field: 'referenceNo', title: 'ReferenceNo', width:170}
                        ,{field: 'networkCodeDesc', title: 'Network', width:150}
                        ,{field: 'weight', title: 'Weight', width:100}
                        ,{field: 'statusDesc', title: 'Status', width:150}
                        ,{field: '', title: 'HandleTime', width:200, templet:'<div>{{ formatDate(d.handleTime) }}</div>'}
                        ,{field: 'rName', title: 'Signer', width:150}
                        ,{field: 'address', title: 'Address', width:300}
                        ,{field: 'handleBy', title: 'HandleName', width:150}
                        ,{field: 'rider', title: 'Rider', width:100}
                        ,{field: 'outsource', title: 'Outsource', width:130}
                        ,{field: 'sName', title: 'Sender', width:150}
                        ,{field: 'remark', title: 'Remark', width:170}
                    ]]
                    ,where: getParam(2, true)

                });


                //reloadTable();
            });


            $(".btn-export").on("click", function () {
                if(showPattern==1){
                    window.location.href = "/report/sign/list.html" + "?limit=1000&" + getParam(1);
                }else {
                    window.location.href = "/report/sign/list.html" + "?" + getParam(1);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            });

            $(".btn-pdf").on("click", function () {
                showPattern==1 ? showPattern=0 : showPattern=1;
                reloadTable();
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
                    var url = "/report/sign/list.html";
                    document.getElementById("ifm").src = url + "?limit=1000&" + getParam(0);
                }


            };

            function getParam(dateType, isPojo){

                if (dateType=="" || dateType=='undefind') dateType=0;
                var sTime_creat = $("#fromCreatedTime").val()=="" ? "" : new Date($("#fromCreatedTime").val()+' 00:00:00').getTime()/1000;
                var eTime_creat = $("#toCreatedTime").val()==""   ? "" : new Date($("#toCreatedTime").val()+' 00:00:00').getTime()/1000;
                if(sTime_creat==eTime_creat && eTime_creat!=0) eTime_creat += 86400;
                var outsource  = $("select[name='outsource']").val();
                var param = {
                    orderNo: $("input[name='orderNo']").val(),
                    fromHandledTime: sTime_creat,
                    toHandledTime: eTime_creat,
                    exportType: dateType,
                    riderId: $("select[name='rider']").val(),
                    networkCode: $("select[name='nextStationCode']").val(),
                    outsource: outsource
                };
                if (isPojo===true) return param;
                else return jQuery.param( param );

            }
        });

    </script>
</body>
</html>
