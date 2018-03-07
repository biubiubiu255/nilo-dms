<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <!-- <input type="hidden" name="merchantId" value="0"> -->
        <div class="layui-form-item">
            <label class="layui-form-label">International order</label>
            <div class="layui-input-block layui-disabled">
                <input type="text" name="OrderNo" autocomplete="off" class="layui-input" lay-verify="required" disabled="" value="${resultData.orderNo}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">International channels</label>
            <div class="layui-input-block layui-disabled">
                <input type="text" name="channels" autocomplete="off" class="layui-input" lay-verify="required" readonly="readonly" value="${resultData.channels}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Weight(KG)</label>
            <div class="layui-input-block">
                <input type="text" name="weight" autocomplete="off" class="layui-input" lay-verify="required" value="${resultData.weight}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">ClientName</label>
            <div class="layui-input-block layui-disabled">
                <input type="text" name="clientName" autocomplete="off" class="layui-input" value="cuckoo" readonly="readonly" value="${resultData.clientName}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Delivery time</label>
            <div class="layui-inline layui-disabled">
                <input type="text" class="layui-input" readonly="readonly" name="receive_time" value="${resultData.receiveTime}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Remark</label>
            <div class="layui-input-block">
                <textarea name="notes" class="layui-textarea" value="${resultData.notes}"></textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="edit-external">Submit</button>
            </div>
        </div>



    </form>
</div>

<script type="text/javascript">
    $(function () {

        layui.use('jquery', function () {
           var J_$ = layui.jquery;
           J_$("input[name='receive_time']").first().val(formatDate(J_$("input[name='receive_time']").first().val()));
        });

        layui.use(['form', 'jquery'], function () {
            var form = layui.form;
            var J_$  = layui.jquery;
            form.render();
            //监听提交
            form.on('submit(edit-external)', function (data) {
                var load = layer.load(2);
                if ($("input[name='weight']").first().val()<=0) {
                    layer.msg('Incorrect information input', {icon: 0, time: 2000}, function () {
                    });
                    return false;
                }

                var param = {
                    id : ${resultData.id} ,
                    notes : $("input[name='notes']").first().val() ,
                    weight: $("input[name='weight']").first().val()
                };

                $.ajax({
                    url: "/waybill/external/editInfo.html",
                    data: param,
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS!', {icon: 1, time: 1000}, function () {
                                layer.closeAll();
                            });
                        } else {
                            //失败，提交表单成功后，释放hold，如果不释放hold，就变成了只能提交一次的表单
                            layer.closeAll();

                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                        //reloadCurrentPage();
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
