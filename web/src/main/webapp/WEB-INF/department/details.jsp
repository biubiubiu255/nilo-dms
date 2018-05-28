<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ page import="org.apache.commons.lang.RandomStringUtils" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<link rel="stylesheet" href="${ctx}/dist/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="${ctx}/plugins/select2/select2.min.css">

<body>
<div class="box-body">

    <hr>
    <div class="layui-row layui-col-space10">
        <div class="layui-col-md5">
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Department</h3>
                    <shiro:hasPermission name="300011">
                        <button class="layui-btn layui-btn-normal  btn-add">Add</button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="300012">
                        <button class="layui-btn layui-btn-normal  btn-edit">Edit</button>
                    </shiro:hasPermission>
                </div>
                <!-- form start -->
                <ul id="treeDepartment" class="ztree"></ul>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
<script src="${ctx}/dist/js/jquery.ztree.core-3.5.min.js"></script>
<script src="${ctx}/dist/js/jquery.ztree.excheck-3.5.min.js"></script>
<script src="${ctx}/dist/js/jquery.ztree.exedit.min.js"></script>
<script src="../../plugins/select2/select2.full.min.js"></script>

<script type="text/javascript">
    $(function () {

        $(".select2").select2();
        var setting = {
            view: {
                selectedMulti: false
            },
            edit: {
                enable: true,
                showRemoveBtn: false,
                showRenameBtn: false
            },
            data: {
                keep: {
                    parent: true,
                    leaf: true
                },
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: zTreeOnClick
            }
        };

        //加载权限项
        $.ajax({
            type: "POST",
            url: "/department/departmentTree.html",
            dataType: "json",
            data: {},
            success: function (data) {
                if (data.result) {
                    $.fn.zTree.init($("#treeDepartment"), setting, data.data);
                } else {
                    layer.msg(data.msg);
                }
            }
        });

        $(".btn-add").on("click", addDepartment);
        $(".btn-edit").on("click", editDepartment);
        $(".btn-remove").on("click", delDepartment);

        var newCount = 1;

        function zTreeOnClick(event, treeId, treeNode) {
            var departmentId = treeNode.id;
        };

        function addDepartment() {
            var zTree = $.fn.zTree.getZTreeObj("treeDepartment"),
                    nodes = zTree.getSelectedNodes(),
                    treeNode = nodes[0];

            if (nodes.length == 0) {
                layer.msg("Please select one node at first...");
                return;
            }

            var departmentId = treeNode.id;
            if (!departmentId) {
                departmentId = "";
            }

            var url = "/department/addDepartmentPage.html";
            var title = "Add Department";
            $.ajax({
                url: url,
                type: 'GET',
                data: {departmentId: departmentId},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px','600px'],
                        offset: ['100px', '250px'],
                        content: data,
                        end: function () {
                            location.reload();
                        }
                    });
                }
            });
        }

        function editDepartment() {
            var zTree = $.fn.zTree.getZTreeObj("treeDepartment"),
                    nodes = zTree.getSelectedNodes(),
                    treeNode = nodes[0];

            if (nodes.length == 0) {
                alert("Please select one node at first...");
                return;
            }

            var departmentId = treeNode.id;

            if (!departmentId) {
                departmentId = "";
            }

            var url = "/department/editDepartmentPage.html";
            var title = "Edit Department";
            $.ajax({
                url: url,
                type: 'GET',
                data: {departmentId: departmentId},
                dataType: 'text',
                success: function (data) {
                    parent.layer.open({
                        type: 1,
                        title: title,
                        area: ['800px','600px'],
                        offset: ['100px', '250px'],
                        content: data,
                        end: function () {
                            location.reload();
                        }
                    });
                }
            });
        }

        function delDepartment() {
            var zTree = $.fn.zTree.getZTreeObj("treeDepartment"),
                    nodes = zTree.getSelectedNodes(),
                    treeNode = nodes[0];

            if (nodes.length == 0) {
                alert("Please select one node at first...");
                return;
            }

            var departmentId = treeNode.id;

            if (!departmentId) {
                departmentId = "";
            }

            var url = "/department/delDepartment.html";
            var title = "Add Department";
            $.ajax({
                url: url,
                type: 'GET',
                data: {departmentId: departmentId},
                dataType: 'json',
                success: function (data) {
                    if (data.result) {
                        layer.msg('SUCCESS', {icon: 1, time: 2000}, function () {
                            var callbackFlag = $("#callbackTrigger").attr("checked");
                            zTree.removeNode(treeNode, callbackFlag);
                        });
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    }
                }
            });


        }
    });
</script>
</body>
</html>