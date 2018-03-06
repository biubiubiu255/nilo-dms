<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
    
        <input type="hidden" name="id" value="${resultDate.id}">
        <div class="layui-form-item">
            <label class="layui-form-label">PackageNo：${resultDate.orderNo}</label>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">ClientName</label>
            <div class="layui-input-block">
                <input type="text" name="client_name" autocomplete="off" value="${resultData.clientName}" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Creator</label>
            <div class="layui-input-block">
                <input type="text" name="creator" autocomplete="off" value="${resultData.creator}" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Weight</label>
            <div class="layui-input-block">
                <input type="text" name="weight" value="${resultData.weight}" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">Remark</label>
            <div class="layui-input-block">
                <input type="text" name="notes" value="${resultData.notes}" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-driver">Submit</button>
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
            form.on('submit(add-driver)', function (data) {

                var load = layer.load(2);
                if ($("input[name='weight']").first().val()<0) {
                    layer.msg('Incorrect information input', {icon: 0, time: 2000}, function () {
                        layer.closeAll();
                    });
                    return ;
                }

                $.ajax({
                    url: "/waybill/external/editInfo.html",
                    data: $("#myForm").serialize(),
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS!', {icon: 1, time: 1000}, function () {
                                layer.closeAll();
                                //reloadCurrentPage();
                                //$(".layui-table").first().t.pt.render({});
                                //$(".layui-laypage-btn")[0].click();
/*                                 $(".search").on("click", function () {
					                reloadTable();
					            }) */

                                //table.render();
                            });
                        } else {
                            //失败，提交表单成功后，释放hold，如果不释放hold，就变成了只能提交一次的表单
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
