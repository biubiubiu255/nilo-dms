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



    <div class="layui-form layui-row">

        <div class="layui-col-md4 layui-col-lg5">
            <label class="layui-form-label">ScanTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From" name="createdTime_s">
            </div>
        </div>

        <div class="layui-col-md4 layui-col-lg4">
            <label class="layui-form-label">ExpressName:</label>
            <div class="layui-inline">
                <select name="expressCode" lay-verify="required" lay-filter="expressCodeLay" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${expressList}" var="r">
                        <option value=${r.expressCode}>${r.expressName}</option>
                    </c:forEach>
                </select></div>
        </div>

        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">City:</label>
            <div class="layui-input-inline">
                <input type="text" name="city" autocomplete="off" class="layui-input">
            </div>
        </div>

        <!-- 搜索按钮 -->
        <div class="layui-col-md4 layui-col-lg4" style="margin-left: 2.4rem;">
            <button class="layui-btn layui-btn-normal search">Search</button>
            <%--<button class="layui-btn layui-btn-normal btn-pdf">View</button>--%>
            <button class="layui-btn layui-btn-normal btn-export">Export</button>
        </div>

    </div>
    </div>
    <hr>

    <div id="me_tab" style="margin-top: -20px;">
        <table class="layui-table" lay-filter="demo" id="${id0}"></table>
    </div>

    <script type="text/html" id="barDemo">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="tool-tar">target</a>
    </script>

    <%--<iframe scrolling="no" frameborder="0" src="/report/sendExpress/summarizeList.html" id="ifm" width="100%" height="100%" style="padding: 0px; display: none;"></iframe>--%>

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
                    , type: 'month'
                });

                var tab = layui.table;
                tableMe = tab.render({
                    elem: '#${id0}'
                    ,url: '/report/sendExpress/summarizeDetailList.html?exportType=2' //数据接口
                    ,page: true //开启分页
                    ,limit:10
                    ,method: 'post'
                    ,cols: [[ //表头
                         {field: 'monthDate', title: 'Sign Month', width:200, fixed: 'left', align: 'center'}
                        ,{field: 'city', title: 'City', width:170, align: 'center'}
                        ,{field: 'express', title: 'Express', width:150, align: 'center'}
                        ,{field: 'signedNum', title: 'SignedNum', width:150, align: 'center'}
                    ]]
                    ,where: getParam(2, true)

                });

                tab.on('tool(demo)', function (obj) {
                    var data = obj.data;
                    if (obj.event === 'tool-tar') {
                        var choseTime  = new Date(data.monthDate+'-01 00:00:00');
                        var start_time = choseTime.getTime()/1000;
                        choseTime.setMonth(choseTime.getMonth()+1);
                        var end_time   = choseTime.getTime()/1000;

                        window.top.document.custServlet = {};
                        window.top.document.custServlet['data'] = data;
                        window.top.document.custServlet.data['fromCreatedTime'] = start_time;
                        window.top.document.custServlet.data['toCreatedTime'] = end_time;
                        //console.log(window.top.document.custServlet['data']);
                        var tag = $("span:contains(Send Express Report)", window.parent.document).parent();
                        var expressTags = $("span:contains(Send Express Report)", window.parent.document).parent().get(1);
                        console.log($(expressTags).find("i"));
                        $(expressTags).find("i").first().click();
                        tag.first().click();

                    }
                });


                //reloadTable();
            });


            $(".btn-export").on("click", function () {
                if(showPattern==1){
                    window.location.href = "/report/sendExpress/summarizeDetailList.html" + "?limit=1000&" + getParam(1);
                }else {
                    window.location.href = "/report/sendExpress/summarizeDetailList.html" + "?" + getParam(1);
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
                    var url = "/report/sendExpress/summarizeDetailList.html";
                    document.getElementById("ifm").src = url + "?limit=1000&" + getParam(0);
                }


            };

            function getParam(dateType, isPojo){

                if (dateType=="" || dateType=='undefind') dateType=0;
                var choseTime  = new Date($("#fromCreatedTime").val()+'-01 00:00:00');
                var start_time = choseTime.getTime()/1000;
                choseTime.setMonth(choseTime.getMonth()+1);
                var end_time   = choseTime.getTime()/1000;

                var param = {
                    fromCreatedTime: start_time,
                    toCreatedTime: end_time,
                    exportType: dateType,
                    express: $("select[name='expressCode']").val(),
                    city: $("input[name='city']").val()
                };
                if (isPojo===true) return param;
                else return jQuery.param( param );
            }
        });

    </script>
</body>
</html>
