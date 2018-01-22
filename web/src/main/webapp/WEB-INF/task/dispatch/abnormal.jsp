<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%
    request.setAttribute("abnormalTypeList", SystemCodeUtil.getSystemCodeList((String)session.getAttribute("merchantId"),"abnormal_order_type"));
%>
<style>
    .layui-upload-img{width: 92px; height: 92px; margin: 0 10px 10px 0;}
</style>
<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <input type="hidden" name="orderNo" value="${orderNo}">
        <input type="hidden" name="taskId" value="${taskId}">
        <input type="hidden" name="referenceNo" value="${referenceNo}">
        <div class="layui-form-item">
            <label class="layui-form-label">OrderNo:</label>
            <div class="layui-input-block">
                <label class="layui-form-label">${orderNo}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">ReferenceNo:</label>
            <div class="layui-input-block">
                <label class="layui-form-label">${referenceNo}</label>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label layui-form-text">Type:</label>
            <div class="layui-input-block">
                <select name="abnormalType" lay-verify="required" lay-search="" style="display: none">
                    <option value="">choose or search....</option>
                    <c:forEach items="${abnormalTypeList}" var="r">
                        <option value="${r.code}">${r.value}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">Remark</label>
            <div class="layui-input-block">
                <textarea placeholder="remark" class="layui-textarea" name="remark"></textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <input type="hidden" name="image">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="abnormal">Submit</button>
                <button type="button" class="layui-btn" id="uploadAbnormalImage">Upload</button>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-upload-list" id="imageList">
            </div>
        </div>

    </form>
</div>
<script type="text/javascript">
    $(function () {

        layui.use('form', function () {
            var form = layui.form;
            form.render();
            form.on('submit(abnormal)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/abnormalOrder/abnormal.html",
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


        layui.use('upload', function () {
            var upload = layui.upload;
            var orderNo = "${orderNo}";
            //执行实例
            var uploadInst = upload.render({
                elem: '#uploadAbnormalImage' //绑定元素
                , url: '/image/upload/abnormal/'+orderNo+'.html' //上传接口
                , before: function (obj) {
                    //预读本地文件示例，不支持ie8
                    obj.preview(function (index, file, result) {
                        $('#imageList').append('<img src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img">')
                    });
                }
                , done: function (res) {
                    //上传完毕回调
                    layer.msg("SUCCESS", {icon: 1, time: 1000},function () {
                        $("input[name='image']").val(res.data);
                    });
                }
            });
        });
    });
</script>