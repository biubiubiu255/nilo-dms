<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <!-- <input type="hidden" name="merchantId" value="0"> -->

        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>International order</label>
            <div class="layui-input-block">
                <input type="text" name="OrderNo" autocomplete="off" class="layui-input" lay-verify="required" placeholder="Please enter the international tracking number">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>International channels</label>
            <div class="layui-input-block">
                <input type="text" name="channels" autocomplete="off" class="layui-input" lay-verify="required" placeholder="Please enter the International channels">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>Weight(KG)</label>
            <div class="layui-input-block">
                <input type="text" name="weight" autocomplete="off" class="layui-input" lay-verify="required" placeholder="Please enter package weight">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>ClientName</label>
            <div class="layui-input-block">
                <input type="text" name="clientName" autocomplete="off" class="layui-input" value="cuckoo" lay-verify="required" placeholder="Please enter the merchant name">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>Delivery time</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromTime" placeholder="Please select date" lay-verify="required" name="receive_time">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Remark</label>
            <div class="layui-input-block">
                <textarea name="notes" placeholder="Please enter notes, maybe not" class="layui-textarea"></textarea>
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
        layui.use(['laydate', 'jquery'], function () {
            var J_$ = layui.jquery;
            var layDate = layui.laydate;
            var d = new Date();
            //var now = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
            var now = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
            //J_$("#fromTime").val(now);
            layDate.render({
                elem: '#fromTime',
                lang: 'en',
                value: formatDate(Date.parse(new Date())/1000),
                theme: '#393D49',
                showBottom: true,
                btns: ['clear', 'confirm'],
                ready: function(value, date, endDate){
                },
                change: function(value, date, endDate){
/*
                    console.log(value); //得到日期生成的值，如：2017-08-18
                    J_$(".laydate-btns-time")[0].click();
                    $(".laydate-time-list").unbind("click").bind("click", "" ,function(){
                        //J_$(".laydate-btns-confirm")[0].click();
                    });
                    */


                }

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
