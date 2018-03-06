<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <!-- <input type="hidden" name="merchantId" value="0"> -->



        <div class="layui-form-item">
            <label class="layui-form-label">International order：</label>
            <div class="layui-input-block">
                <input type="text" name="OrderNo" autocomplete="off" class="layui-input" lay-verify="number" placeholder="Please enter the international tracking number">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">International channels：</label>
            <div class="layui-input-block">
                <input type="text" name="channels" autocomplete="off" class="layui-input" lay-verify="number" placeholder="Please enter the International channels">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Weight</label>
            <div class="layui-input-block">
                <input type="text" name="weight" autocomplete="off" class="layui-input" lay-verify="required" placeholder="Please input package weight">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">ClientName</label>
            <div class="layui-input-block">
                <input type="text" name="clientName" autocomplete="off" class="layui-input" value="cuckoo" lay-verify="required" placeholder="Please enter the merchant name">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Remark</label>
            <div class="layui-input-block">
                <input type="text" name="notes" autocomplete="off" class="layui-input" placeholder="Please enter notes, maybe not">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Delivery time</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromTime" placeholder="Please enter delivery time" lay-verify="required" name="receive_time">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-user">Submit</button>
                <button type="reset" class="layui-btn layui-btn-primary reset">Reset</button>
            </div>
        </div>
        
    </form>
</div>

<script type="text/javascript">
    $(function () {
        layui.use('laydate', function () {
            var layDate = layui.laydate;
            var d = new Date();
            var now = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
            layDate.render({
                elem: '#fromTime',
                lang: 'en',
                type: 'datetime'
            });
        });

        layui.use(['form', 'jquery', 'layer'], function () {
            var form = layui.form;
            form.render();
            //监听提交
            form.on('submit(add-user)', function (data) {
                var load = layer.load(2);
                if ($("input[name='weight']").first().val()<=0) {
                    layer.msg('Incorrect information input', {icon: 0, time: 1000}, function () {
                        layer.closeAll();
                    });
                    return ;
                }else {
                    $.ajax({
                        url: "/waybill/external/addInfo.html",
                        data: $("#myForm").serialize(),
                        type: "POST",
                        dataType: "json",
                        success: function (data) {
                            if (data.result) {
                                layer.msg('SUCCESS!', {icon: 1, time: 1000}, function () {
                                    layer.closeAll();
                                });
                            } else {
                                //失败，提交表单成功后，释放hold，如果不释放hold，就变成了只能提交一次的表单
                                layer.msg(data.msg, {icon: 2, time: 2000});
                            }
                        },
                        complete: function () {
                            layer.close(load);
                        }
                    });
                }
                return false;
            });
        });
    });

</script>
