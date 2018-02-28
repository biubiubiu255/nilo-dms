<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("nowDate", DateUtil.formatCurrent("yyyy-MM-dd"));
%>
<body>

<div class="box-body">


    <div class="layui-row">

        <div class="layui-col-md5">
            <shiro:hasPermission name="400041">
                <button class="layui-btn layui-btn-normal batch">Cancellation</button>
            </shiro:hasPermission>
            <button class="layui-btn btn-search">Search</button>
        </div>

    </div>
    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content ">
                <div class="layui-form layui-row">
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">Rider:</label>
                        <div class="layui-inline">
                            <select name="userId" lay-verify="required" lay-search="" style="display: none">
                                <option value="">choose or search....</option>
                                <c:forEach items="${riderList}" var="rider">
                                    <option value="${rider.userId}">${rider.realName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="layui-col-md1">
                        <button class="layui-btn layui-btn-normal search">Search</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <table class="layui-table" lay-data="{ url:'/order/cancellation/list.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{checkbox:true, fixed: true}"></th>
            <th lay-data="{fixed:'left',field:'orderNo', width:200}">OrderNo</th>
            <th lay-data="{field:'referenceNo', width:200,}">
                ReferenceNo
            </th>
            <th lay-data="{field:'needPayAmount', width:200}">Amount</th>
            <th lay-data="{field:'riderName', width:200}">Rider Name</th>

        </tr>
        </thead>
    </table>
</div>

<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use(['form', 'element'], function () {
            var form = layui.form;
            form.render();
        })
        $(".btn-search").on("click", function () {
            $(".layui-colla-content").toggleClass("layui-show");
            $(".btn-search").toggleClass("layui-btn-warm");
        })
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

            $('.batch').on('click', function () {
                var checkStatus = table.checkStatus('${id0}')
                        , data = checkStatus.data;
                if (data.length == 0) {
                    layer.msg("Pls select...");
                    return;
                }
                var arr = new Array();
                var totalAmount = 0;
                for (var i = 0; i < data.length; i++) {
                    arr.push(data[i].orderNo);
                    totalAmount = totalAmount + data[i].needPayAmount;
                }
                layer.prompt({title: 'Selected Order Total Amount:'+totalAmount, formType: 3,}, function(text, index){
                    layer.close(index);
                    cancellation(arr,text);
                });


            });

            $(".search").on("click", function () {
                reloadTable();
            })
            var reloadTable = function (item) {
                table.reload("${id0}", {
                    where: {
                        userId: $("select[name='userId']").val(),
                    }
                });
            };

            function cancellation(arr,transNo) {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/cancellation/handle.html",
                    dataType: "json",
                    data: {orderNos: arr,transNo:transNo},
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                                reloadCurrentPage();
                            });
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
            }
        });
    });

</script>
</body>
</html>