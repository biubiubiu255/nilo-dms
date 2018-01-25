<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>

<%
    request.setAttribute("jobList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "job"));
%>
<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <input type="text" value="${staff.staffId}" name="staffId" hidden="true">
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">Status:</label>
                <div class="layui-input-inline">
                    <lp:enumTag selectId="staffStatus" selectName="staffStatus" className="StaffStatusEnum"
                                code="" disabled="false"/>
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">Sex:</label>
                <div class="layui-input-inline">
                    <input type="radio" name="sex" value="1" title="Male" checked>
                    <input type="radio" name="sex" value="2" title="Female">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Department:</label>
            <div class="layui-input-inline">
                <select lay-filter="departmentId" name="departmentId">
                    <option value="">Pls select order type...</option>
                    <c:forEach items="${departmentList}" var="r">
                        <option value=${r.departmentId}>${r.departmentName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="layui-input-inline">
                <input type="checkbox" name="isRiderCode" title="Rider" value="1"
                       lay-skin="primary" >
            </div>
        </div>


        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">Nick Name:</label>
                <div class="layui-input-inline">
                    <input type="text" name="nickName" value="${staff.nickName}" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">Real Name:</label>
                <div class="layui-input-inline">
                    <input type="text" name="realName" value="${staff.realName}" autocomplete="off" class="layui-input">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">Employ:</label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" name="employTimeDate" id="employTime"
                           placeholder="Employ Time">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">Birthday:</label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" name="birthday" id="birthday"
                           placeholder="Birthday">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">Job:</label>
                <div class="layui-input-inline">
                    <select name="job" lay-verify="required" lay-search="" style="display: none">
                        <option value="">choose or search....</option>
                        <c:forEach items="${jobList}" var="r">
                            <option value="${r.code}">${r.value}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">Phone</label>
                <div class="layui-input-inline">
                    <input type="text" name="phone" value="${staff.phone}" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">Email:</label>
                <div class="layui-input-inline">
                    <input type="text" name="email" value="${staff.email}" autocomplete="off" class="layui-input">
                </div>
            </div>

        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">ID Card</label>
            <div class="layui-input-block">
                <input type="text" name="idCard" value="" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Address</label>
            <div class="layui-input-block">
                <input type="text" name="address" value="${staff.address}" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-staff">Submit</button>
                <button type="reset" class="layui-btn layui-btn-primary reset">Reset</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {

        layui.use('laydate', function () {
            var layDate = layui.laydate;
            layDate.render({
                elem: '#employTime'
                , lang: 'en'
            });
            layDate.render({
                elem: '#birthday'
                , lang: 'en'
            });
        });

        layui.use('form', function () {
            var form = layui.form;
            form.render();
            //监听提交
            form.on('submit(add-staff)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/staff/addStaff.html",
                    data: $("#myForm").serialize(),
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS!', {icon: 1, time: 1000}, function () {
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
