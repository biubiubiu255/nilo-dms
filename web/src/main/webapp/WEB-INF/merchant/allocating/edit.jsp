<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">Contract Area:</label>
            <div class="layui-input-block" style="width:500px">
                <select lay-filter="merchantId" lay-verify="required" name="merchantId">
                    <c:forEach items="${merchantList}" var="m">
                        <option value="${m.id}">${m.merchantName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <hr>
        <div class="layui-form-item">
            <label class="layui-form-label">Name:</label>
            <div class="layui-input-inline">
                <input type="text" value="" name="name" autocomplete="off"
                       class="layui-input">
            </div>
            <div class="layui-input-inline">
                <input type="checkbox" name="isSelfCollect" title="Self Collect" value="1"
                       lay-skin="primary">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Country:</label>
            <div class="layui-input-inline">
                <select lay-filter="country1" lay-verify="required" name="receiverCountry">
                    <option value="">Pls select...</option>
                    <option value="CN">China</option>
                    <option value="KE">Kenya</option>
                </select>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">Province:</label>
                <div class="layui-input-inline">
                    <select lay-filter="province1" id="province1" lay-verify="required" name="receiverProvince">
                    </select>
                </div>
            </div>
        </div>

        <div class="layui-form-item">

            <label class="layui-form-label">City:</label>
            <div class="layui-input-inline">
                <select lay-filter="city1" id="city1" lay-verify="required" name="receiverCity">
                </select>
            </div>
            <label class="layui-form-label">Area:</label>
            <div class="layui-input-inline">
                <select lay-filter="area1" id="area1" name="area">
                </select>
            </div>

        </div>


        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-order-handle-config">Submit</button>
                <button type="reset" class="layui-btn layui-btn-primary reset">Reset</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {
        layui.use(["address","address2"],function() {
            var address = layui.address();
            var address2 = layui.address2();

        });
        layui.use('form', function () {
            var form = layui.form;
            form.render();
            //监听提交
            form.on('submit(add-order-handle-config)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/systemConfig/orderHandle/addBasic.html",
                    data: {
                        optTypeAdd: $("select[name='optTypeAdd']").val(),
                        updateStatus: $("select[name='updateStatus']").val(),
                        allowStatus: $("select[name='allowStatus']").val(),
                        notAllowStatus: $("select[name='notAllowStatus']").val(),
                        className: $("input[name='className']").val(),

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
