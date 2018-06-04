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
        </div>


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
            <label class="layui-form-label">HandleNo:</label>
            <div class="layui-input-inline">
                <input type="text" name="handleNo" autocomplete="off" class="layui-input">
            </div>
        </div>

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

        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">NextStation:</label>
            <div class="layui-inline">
                <select lay-filter="nextStationCodeLay" name="nextStationCode">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStations}" var="r">
                        <%--<option value=${r.code}>${r.name}</option>--%>
                        <option value=${r.code}>${r.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

    <div class="layui-row">


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
    </div>
    <hr>
</div>

</div>




    <div id="me_tab">
        <table class="layui-table"
               lay-data="{ url:'/report/repeat_send/list.html?exportType=2',method:'post', page:true,limit:10, id:'${id0}'}"
               lay-filter="demo">
            <thead>
            <tr>
                <th lay-data="{fixed: 'left',field:'orderNo', width:200}">Waybill No</th>
                <th lay-data="{field: 'referenceNo', width:190}">ReferenceNo</th>
                <th lay-data="{field:'dispatchType', width:119}">DispatchType</th>
                <th lay-data="{field:'dispatchNum', width:116}">Times</th>
                <th lay-data="{field:'handleNo', width:100}">HandleNo</th>
                <th lay-data="{field: 'handleName', width:130}">HandleName</th>
                <th lay-data="{field:'expressName', width:130}">ExpressName</th>
                <th lay-data="{field:'nextStation', width:130}">NextStation</th>
                <th lay-data="{field: 'orderType', width:100}">OrderType</th>
                <th lay-data="{field:'weight', width:100}">Weight</th>
<%--                <th lay-data="{field:'rider', width:130}">Rider</th>--%>
                <%--<th lay-data="{field:'parentNo', width:150}">parentNo</th>--%>
                <th lay-data="{field: 'statusDesc', width:130}">Status</th>
                <th lay-data="{width:200, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">CreatedTime</th>
                <th lay-data="{field:'receiveName', width:153}">Name</th>
                <th lay-data="{field:'phone', width:150}">Phone</th>
                <th lay-data="{field:'address', width:200}">Address</th>
            </tr>
            </thead>
        </table>
    </div>

    <iframe scrolling="no" frameborder="0" src="/report/repeat_send/list.html?exportType=0" id="ifm" width="100%" height="100%" style="padding: 0px; display: none;"></iframe>

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
                });
                layDate.render({
                    elem: '#toCreatedTime'
                    , lang: 'en'
                });

            });

            var table = layui.table;
            layui.use('table', function () {
                table = layui.table;
                table.on('tool(demo)');
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
                    table.reload("${id0}", {
                        where: getParam(2, true)
                    });
                }else if(showPattern==1){
                    $("#ifm").show();
                    $("#me_tab").hide();
                    var url = "/report/repeat_send/list.html";
                    document.getElementById("ifm").src = url + "?limit=1000&" + getParam(0);
                }


            };

            function getParam(dateType, isPojo){
                if (dateType=="" || dateType=='undefind') dateType=0;
                var sTime_creat = $("#fromCreatedTime").val()=="" ? "" : Date.parse(new Date($("#fromCreatedTime").val()))/1000;
                var eTime_creat = $("#toCreatedTime").val()==""   ? "" : Date.parse(new Date($("#toCreatedTime").val()))/1000+86400;

                var param = {
                    orderNo: $("input[name='orderNo']").val(),
                    driver: $("input[name='driver']").val(),
                    fromCreatedTime: sTime_creat,
                    toCreatedTime: eTime_creat,
                    handleNo: $("input[name='handleNo']").val(),
                    orderType: $("select[name='orderType']").val(),
                    expressCode: $("select[name='expressCode']").val(),
                    nextStationCode: $("select[name='nextStationCode']").val(),
                    exportType: dateType,
                    dispatchType: $("select[name='dispatchType']").val(),
                    status: $("select[name='status']").val()
                };

                if (isPojo===true) return param;
                else return jQuery.param( param );

            }
        });

    </script>
</body>
</html>
