<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>

<div class="box-body">
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label"> Type:</label>
            <div class="layui-form-item layui-inline" style="margin: 0px">
                <select lay-filter="type" name="type" lay-verify="required">
                    <option value="">Pls select ...</option>
                    <option value='delivery_order_type'>delivery_order_type</option>
                    <option value='refuse_reason'>refuse_reason</option>
                    <option value='next_station'>next_station</option>
                </select>
            </div>
        </div>
        <div class="layui-col-md4 layui-col-lg3">
            <label class="layui-form-label"> Desc:</label>
            <div class="layui-inline">
                <input class="layui-input" name="desc" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>

    <table class="layui-table"
           lay-data="{ url:'/admin/code/getList.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{field:'id', width:150}">ID</th>
            <th lay-data="{field:'type', width:200}">Type</th>
            <th lay-data="{field:'descE', width:200}">Desc E</th>
            <th lay-data="{field:'descC', width:200}">Desc C</th>
            <th lay-data="{field:'remark', width:200}">Remark</th>
            <th lay-data="{title:'Opt', width:160, align:'center', toolbar: '#barDemo'}"></th>
        </tr>
        </thead>
    </table>


    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="100051">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
    </script>
</div>

<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        layui.use('form', function () {
            var form = layui.form;
            form.render();
        })
        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'edit') {
                    edit_dic_type(data.type);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            function reloadTable(item) {
                table.reload("${id0}", {
                    where: {
                        type: $("select[name='type']").val(),
                        desc: $("input[name='desc']").val(),
                    }
                });
            };
            function edit_dic_type(type) {
                var url = "/admin/code/editCodePage.html?type=" + type;
                var title = "Add";
                $.ajax({
                    url: url,
                    type: 'GET',
                    dataType: 'text',
                    success: function (data) {
                        var index = layer.open({
                            type: 1,
                            content: data,
                            area: ['800px', '600px'],
                            maxmin: true
                        });
                        layer.full(index);
                    }
                });
            }
        });


    });


</script>
</body>
</html>