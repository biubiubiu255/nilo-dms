<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%
    request.setAttribute("id2", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("id1", RandomStringUtils.randomAlphabetic(8));

%>
<div class="box">

    <div class="row">
        <div class="layui-field-box layui-form">
            <button type="button" class="layui-btn layui-btn-small add-code" id="getAll"><i class="fa fa-plus"
                                                                                            aria-hidden="true"></i> Add
            </button>
            <table class="layui-hide" id="${id2}"></table>
        </div>
    </div>

</div>
<script type="text/html" id="${id1}">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="edit-code">Edit</a>
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="del">Delete</a>
</script>

<script>
    $(function () {
        layui.use('table', function () {
            var table = layui.table;
            //方法级渲染
            table.render({
                elem: '#${id2}'
                , url: '/admin/code/getCodeList.html'
                , where: {type: '${type}'}
                , cols: [[
                    {field: 'id', title: 'ID', width: 80,}
                    , {field: 'type', title: 'Type', width: 200}
                    , {field: 'code', title: 'Code', width: 200}
                    , {field: 'value', title: 'Value', width: 200, edit: 'text'}
                    , {field: 'showOrder', title: 'Show Order', width: 150, edit: 'text'}
                    , {field: 'remark', title: 'Remark', width: 200, edit: 'text'}
                    , {title: "Operation", width: 200, align: 'center', toolbar: '#${id1}'}
                ]]
                , id: '${id2}'
            });
            //监听工具条
            table.on('tool', function (obj) {
                var data = obj.data;
                if (obj.event === 'del') {
                    layer.confirm('Confirm Delete?', function (index) {
                        obj.del();
                        delCode(data.type, data.code);
                    });
                }
                if (obj.event === 'edit-code') {
                    editCode(data);
                }
            });

            $(".add-code").on('click', function () {
                var title = "Add";
                var url = "/admin/code/addCodePage.html";
                $.ajax({
                    url: url,
                    type: 'GET',
                    data: {type: '${type}'},
                    dataType: 'text',
                    success: function (data) {
                        layer.open({
                            type: 1,
                            title: title,
                            area: ['800px', '500px'],
                            content: data,
                            end: function () {
                                table.reload("${id2}", {
                                    where: {type: '${type}'}
                                });
                            }
                        });
                    }
                });
            });

            function editCode(data) {
                $.ajax({
                    type: "POST",
                    url: "/admin/code/editCode.html",
                    dataType: "json",
                    data: {
                        type: data.type,
                        code: data.code,
                        value: data.value,
                        showOrder: data.showOrder,
                        remark: data.remark,
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 2000}, function () {
                            });
                        } else {
                            layer.msg("Failed. " + data.msg, {icon: 2});
                        }
                    }
                });
            }

            function delCode(type, code) {
                $.ajax({
                    type: "POST",
                    url: "/admin/code/delCode.html",
                    dataType: "json",
                    data: {
                        type: type,
                        code: code
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 2000}, function () {
                            });
                        } else {
                            layer.msg("Failed. " + data.msg, {icon: 2});
                        }
                    }
                });
            }
        });
    });
</script>
