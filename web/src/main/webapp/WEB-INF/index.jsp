<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<head>
    <meta charset="utf-8">
    <title>下拉多选</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="${ctx}/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${ctx}/layui/css/multselect.css" media="all">
</head>
<body class="childrenBody">
<form class="layui-form layui-row">
    <div class="layui-form-item">
        <label class="layui-form-label">动态赋值</label>
        <div class="layui-input-block">
            <select lay-filter="aaa" multiple name="aaa" lay-verify="required">
                <option value="">请选择您的兴趣爱好</option>
                <option value="1" disabled>旅游</option>
                <option value="2">唱歌</option>
                <option value="3" selected>爬山</option>
                <option value="4" selected>游戏</option>
                <option value="5">台球</option>
                <option value="6">阅读</option>
                <option value="7">看电影</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">选择提交</label>
        <div class="layui-input-block">
            <select lay-filter="bbb" multiple name="bbb" lay-verify="required">
                <option value="">请选择您的兴趣爱好</option>
                <option value="1" disabled>旅游</option>
                <option value="2">唱歌</option>
                <option value="3">爬山</option>
                <option value="4">游戏</option>
                <option value="5">台球</option>
                <option value="6">阅读</option>
                <option value="7">看电影</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="changeUser">立即提交</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<!-- layUI -->
<script src="${ctx}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="${ctx}/layui/layui.js"></script>
<script>
    $(function () {

        var form;
        layui.use(['form', 'layer'], function () {
            form = layui.form;
            var layer = parent.layer === undefined ? layui.layer : parent.layer;
            form.on("submit(changeUser)", function (data) {
                var index = layer.msg('提交中，请稍候', {icon: 16, time: false, shade: 0.8});
                setTimeout(function () {
                    layer.close(index);
                    layer.msg("提交成功！");
                }, 2000);
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            })

/*
            $("select[name=aaa]").val(["4", "5"]);
*/
            form.render();
        })
    });
</script>
</body>
</html>