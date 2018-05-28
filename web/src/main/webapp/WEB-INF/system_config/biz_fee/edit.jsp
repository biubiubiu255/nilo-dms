<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<%
    request.setAttribute("abnormalTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "abnormal_order_type"));
%>
<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">Biz Type:</label>
            <div class="layui-input-block">
                <select name="bizTypeEdit" lay-filter="bizTypeEdit" lay-verify="required" lay-search="" style="display: none">
                    <option value="">Select type....</option>
                    <option value="receive" <c:if test="${bizFeeConfig.optType =='receive'}">selected</c:if>>Normal</option>
                    <c:forEach items="${abnormalTypeList}" var="r">
                        <option value=${r.code} <c:if test="${bizFeeConfig.optType ==r.code}">selected</c:if> >${r.value}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <hr>

        <div class="layui-form-item">
            <label class="layui-form-label">Times:</label>
            <div class="layui-input-block">
                <input class="layui-input" name="fee" autocomplete="off" value="${bizFeeConfig.fee}">

            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Remark:</label>
            <div class="layui-input-block">
                <textarea name="remark" placeholder="Remark" class="layui-textarea">${bizFeeConfig.remark}</textarea>

            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-route-config">Submit</button>
                <button type="reset" class="layui-btn layui-btn-primary reset">Reset</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            form.render();
            //监听提交
            form.on('submit(add-route-config)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/systemConfig/bizFee/edit.html",
                    data: {
                        bizTypeEdit: $("select[name='bizTypeEdit']").val(),
                        fee: $("input[name='fee']").val(),
                        remark: $("textarea[name='remark']").val(),

                    },
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 1000});
                            layer.closeAll();
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
