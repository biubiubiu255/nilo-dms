<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<div class="box-body">
    <form id="myForm" class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">Opt Type:</label>
            <div class="layui-input-block">
                <lp:enumTag selectId="optTypeEdit" selectName="optTypeEdit" className="OptTypeEnum"
                            code="${routeConfig.optType}" disabled="true" />
            </div>
        </div>
        <hr>

        <div class="layui-form-item">
            <label class="layui-form-label">Route Desc EN:</label>
            <div class="layui-input-block">
                <textarea name="routeDescE" placeholder="Route Desc EN" class="layui-textarea">${routeConfig.routeDescE}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Route Desc CN:</label>
            <div class="layui-input-block">
                <textarea name="routeDescC" placeholder="Route Desc CN" class="layui-textarea">${routeConfig.routeDescC}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Remark:</label>
            <div class="layui-input-block">
                <textarea name="remark" placeholder="Remark" class="layui-textarea">${routeConfig.remark}</textarea>

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
                    url: "/systemConfig/route/edit.html",
                    data: {
                        optTypeEdit: $("select[name='optTypeEdit']").val(),
                        routeDescE: $("textarea[name='routeDescE']").val(),
                        routeDescC: $("textarea[name='routeDescC']").val(),
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
