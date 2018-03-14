<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("nowDate", DateUtil.formatCurrent("yyyy-MM-dd"));
%>
<body>

<div class="box-body">
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label">Rider:</label>
            <div class="layui-inline">
                <select name="rider" lay-verify="required" lay-search="" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${list}" var="rider">
                        <option value="${rider.userId}">${rider.staffId}</option>
                    </c:forEach>
                </select></div>
        </div>
        <div class="layui-col-md7 layui-col-lg5">
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toTime" placeholder="To">
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="600021">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>
</div>

    <iframe scrolling="no" frameborder="0" src="/report/dispatch/list.html" id="ifm" width="100%" height="100%" style="padding: 0px;"></iframe>


<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            form.render();
        })

        layui.use('laydate', function () {
            var layDate = layui.laydate;
            var d = new Date();
            var now = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
            layDate.render({
                elem: '#fromTime',
                lang: 'en',
            });
            layDate.render({
                elem: '#toTime',
                lang: 'en',
            });
        });
        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    var id = data.id;
                    layer.msg(id);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            var reloadTable = function (item) {
                var param = {
                    rider: $("select[name='rider']").val(),
                    fromTime: $("#fromTime").val(),
                    toTime: $("#toTime").val(),
                };
                var url = "/report/dispatch/list.html";
                param = jQuery.param( param )
                document.getElementById("ifm").src = url + "?" + param;
            };

        });
    });

</script>
</body>
</html>