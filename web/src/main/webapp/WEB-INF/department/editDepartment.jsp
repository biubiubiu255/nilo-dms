<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div style="margin: 15px;">
    <form class="layui-form" id="myForm" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">Superior:</label>
            <div class="layui-input-block">
                <input type="text" value="${department.parentDepartment.departmentName}" autocomplete="off" class="layui-input" disabled>
            </div>
        </div>
        <input type="hidden" name="departmentId" value="${department.departmentId}">
        <div class="layui-form-item">
            <label class="layui-form-label">Name:</label>
            <div class="layui-input-block">
                <input type="text" name="departmentName" value="${department.departmentName}" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Leader :</label>
            <div class="layui-input-block">
                <select name="leaderId" lay-search="" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${list}" var="staff">
                        <option value="${staff.staffId}" <c:if test="${staff.staffId == department.leaderId}"> selected</c:if> >${staff.staffId}-${staff.realName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Description:</label>
            <div class="layui-input-block">
                <textarea placeholder="Description" class="layui-textarea" name="desc" >${department.desc}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="edit-department">Submit</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            form.render();
            form.on('submit(edit-department)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/department/editDepartment.html",
                    dataType: "json",
                    data: $("#myForm").serialize(),
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
                return false;
            });
        });
    });

</script>
