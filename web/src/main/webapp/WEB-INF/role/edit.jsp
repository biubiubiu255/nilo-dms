<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>

<link rel="stylesheet" href="${ctx}/dist/css/zTreeStyle/zTreeStyle.css">
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("id1", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("id2", RandomStringUtils.randomAlphabetic(8));
%>
<div class="ibox-content">
    <form id="myForm" class="form-horizontal" autocomplete="off" data-validator-option="{theme:'default'}"
          style="margin: 1.5em">
        <input type="hidden" id="${id0}" value="${id}">
        <div class="form-group">
            <label class="col-sm-2 control-label">Role Name</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" value="${roleName}" id="${id1}"
                       data-rule="Role Name:required;roleName">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">Description</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="${id2}" value="${description}"
                       placeholder="Please enter description" data-rule="Description:required;description">
            </div>
        </div>

        <div class="form-group">
            <div class="zTreeDemoBackground col-sm-4">
                <ul id="treePermission" class="ztree"></ul>
            </div>
        </div>
        <div class="form-group">
            <div class="text-center">
                <button class="btn btn-primary" type="button" onclick="updateRolePermission()">Submit</button>
            </div>
        </div>
    </form>
</div>
<script src="${ctx}/dist/js/jquery.ztree.core-3.5.min.js"></script>
<script src="${ctx}/dist/js/jquery.ztree.excheck-3.5.min.js"></script>

<script type="text/javascript">

    $(function () {
        var setting = {
            check: {
                enable: true,
                chkboxType: {"Y": "ps", "N": "ps"}
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };
        //加载权限项
        $.ajax({
            type: "POST",
            url: "/admin/role/permissionTree.html",
            dataType: "json",
            data: {
                id: $("#${id0}").val()
            },
            success: function (data) {
                if (data.result) {
                    $.fn.zTree.init($("#treePermission"), setting, data.data);
                } else {
                    layer.msg(data.msg);
                }
            }
        });


    });

    function updateRolePermission() {
        var treeObj = $.fn.zTree.getZTreeObj("treePermission");
        var checkedNodes = treeObj.getCheckedNodes(true);
        var data = new Array();
        for (i in checkedNodes) {
            data.push(checkedNodes[i].value);
        }
        var load = layer.load(2);
        $.ajax({
            type: "POST",
            url: "/admin/role/editRole.html",
            dataType: 'json',
            data: {
                "permissions": data,
                "id": $("#${id0}").val(),
                "roleName": $("#${id1}").val(),
                "description": $("#${id2}").val()
            },
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
                layer.close(load);
            }
        });
    }
</script>
