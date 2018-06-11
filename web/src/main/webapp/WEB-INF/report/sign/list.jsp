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
            <label class="layui-form-label">NextStation:</label>
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

    <div id="me_tab">
        <table class="layui-table"
               lay-data="{ url:'/report/sign/list.html?exportType=2',method:'post', page:true,limit:10, id:'${id0}'}"
               lay-filter="demo">
            <thead>
            <tr>
                <th lay-data="{fixed: 'left',field:'orderNo', width:200}">Waybill No</th>
                <th lay-data="{field:'referenceNo', width:170}">ReferenceNo</th>
                <th lay-data="{field:'networkCodeDesc', width:150}">Network</th>
                <th lay-data="{field:'rider', width:100}">Rider</th>
                <th lay-data="{field:'weight', width:100}">Weight</th>
                <th lay-data="{field:'statusDesc', width:150}">Status</th>
                <th lay-data="{field:'handleBy', width:150}">HandleName</th>
                <th lay-data="{width:200, templet:'<div>{{ formatDate(d.handleTime) }}</div>'}">HandleTime</th>
                <th lay-data="{field:'remark', width:170}">Remark</th>
                <th lay-data="{field:'rName', width:150}">Signer</th>
                <th lay-data="{field:'sName', width:150}">Sender</th>
                <th lay-data="{field:'address', width:300}">Address</th>

            </tr>
            </thead>
        </table>
    </div>

    <iframe scrolling="no" frameborder="0" src="/report/sign/list.html?exportType=0" id="ifm" width="100%" height="100%" style="padding: 0px; display: none;"></iframe>

    <%@ include file="../../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {

            showPattern = 0;

            layui.use(['element', 'form', 'laydate'], function () {
                var layDate = layui.laydate;
                layDate.render({
                    elem: '#fromCreatedTime'
                    , lang: 'en'
                    , value: new Date()
                });
                layDate.render({
                    elem: '#toCreatedTime'
                    , lang: 'en'

                });

            });

            var initLoading = true;
            var table = layui.table;
            layui.use('table', function () {
                table = layui.table;
                table.on('tool(demo)');
                reloadTable();
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
                    table.reload("${id0}", {
                        where: getParam(2, true)
                    });
                }else if(showPattern==1){
                    $("#ifm").show();
                    $("#me_tab").hide();
                    var url = "/report/sign/list.html";
                    document.getElementById("ifm").src = url + "?limit=1000&" + getParam(0);
                }


            };

            reloadTable();

            function getParam(dateType, isPojo){
                if (dateType=="" || dateType=='undefind') dateType=0;
                var sTime_creat = $("input[name='createdTime_s']").val()=="" ? "" : Date.parse(new Date($("input[name='createdTime_s']").val()))/1000;
                var eTime_creat = $("input[name='createdTime_e']").val()=="" ? "" : Date.parse(new Date($("input[name='createdTime_e']").val()))/1000+86400;
                if(initLoading==true){
                    sTime_creat = Date.parse(new Date(new Date(new Date().toLocaleDateString()).getTime()))/1000;
                    eTime_creat = Date.parse(new Date(new Date(new Date().toLocaleDateString()).getTime()+24*60*60*1000-1))/1000;
                    initLoading = false;
                }

                var param = {
                    orderNo: $("input[name='orderNo']").val(),
                    fromHandledTime: sTime_creat,
                    toHandledTime: eTime_creat,
                    exportType: dateType,
                    riderId: $("select[name='rider']").val(),
                    networkCode: $("select[name='nextStationCode']").val()
                };
                if (isPojo===true) return param;
                else return jQuery.param( param );

            }
        });

    </script>
</body>
</html>
