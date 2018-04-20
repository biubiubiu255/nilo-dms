<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.common.Constant" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("refuse_reason", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), Constant.REFUSE_REASON));
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("imageType", "abnormal");
%>
<body>
<div class="box-body">

    <div class="layui-row">
        <div class="layui-col-md3">
            <shiro:hasPermission name="400022">
                <button class="layui-btn layui-btn-normal btn-add">Add</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="400021">
                <button class="layui-btn btn-search">Search
                </button>
            </shiro:hasPermission>
        </div>
    </div>

    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content ">
                <div class="layui-row">
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OrderNo:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="orderNo" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-col-md8 layui-col-lg5">
                        <label class="layui-form-label">CreateTime:</label>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" id="fromCreatedTime" placeholder="From">
                        </div>
                        -
                        <div class="layui-inline">
                            <input type="text" class="layui-input" id="toCreatedTime" placeholder="To">
                        </div>
                    </div>
                </div>
                <div class="layui-form layui-row">
                    <div class="layui-col-md4 layui-col-lg4">
                        <label class="layui-form-label">Reason:</label>
                        <div class="layui-form-item layui-inline" style="margin: 0px; width: 36.5%;">
                            <select name="reason">
                                <option value="">Pls select type...</option>
                                <c:forEach items="${refuse_reason}" var="r">
                                    <option value=${r.code}>${r.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>


                    <div class="layui-col-md1">
                        <shiro:hasPermission name="400021">
                            <button class="layui-btn layui-btn-normal search">Search</button>
                        </shiro:hasPermission>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <table class="layui-table"
           lay-data="{ url:'/order/refuse/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:250}">OrderNo</th>
            <th lay-data="{field:'reason', width:150}">Reason</th>
            <th lay-data="{field:'handleName', width:150}">Handle Name</th>
            <%--<th lay-data="{field:'statusDesc', width:100}">Status</th>--%>
            <th lay-data="{field:'createdTime', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">
                CreatedTime
            </th>
            <th lay-data="{field:'remark', width:200}">Remark</th>
            <%--<th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>--%>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="400023">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="delete">Delete</a>
        </shiro:hasPermission>
    </script>
</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use(['form', 'element', 'laydate'], function () {
            var form = layui.form;
            form.render();

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
        var table;
        layui.use('table', function () {
            table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    var orderNo = data.orderNo;
                    layer.msg(orderNo);
                }
                if (obj.event === 'delete') {
                    deleteAbnormal(data.abnormalNo);
                }
            });


        });

        $(".search").on("click", function () {
            reloadTable();
        })
        $(".btn-search").on("click", function () {
            $(".layui-colla-content").toggleClass("layui-show");
            $(".btn-search").toggleClass("layui-btn-warm");
        })
        function reloadTable() {

            var fromCreatedTime = $("#fromCreatedTime").val()=="" ? "" : Date.parse(new Date($("#fromCreatedTime").val()))/1000;
            var toCreatedTime = $("#toCreatedTime").val()=="" ? "" : Date.parse(new Date($("#toCreatedTime").val()))/1000+86400;

            table.reload("${id0}", {
                where: {
                    reason: $("select[name='reason']").val(),
                    orderNo: $("input[name='orderNo']").val(),
                    fromTime: fromCreatedTime,
                    toTime: toCreatedTime,
                }
            });
        };

        $(".btn-add").on("click", function () {
            $.ajax({
                url: "/order/refuse/addPage.html",
                type: 'GET',
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        content: data,
                        area: ['800px', '500px'],
                        offset: ['100px', '250px'],
                        end: function () {
                            reloadTable();
                        }
                    })
                }
            });
        })

        function deleteAbnormal(abnormalNo) {
            $.ajax({
                type: "POST",
                url: "/order/abnormalOrder/delete.html",
                dataType: "json",
                data: {abnormalNo:abnormalNo},
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            layer.closeAll();
                        });
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    }
                },
                complete: function () {
                    reloadTable();
                }
            });
        }

    });
</script>
</body>
</html>