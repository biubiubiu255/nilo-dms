<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<div class="box-body">
    <form id="myForm" class="layui-form" action="">

        <div class="layui-form-item">
            <label class="layui-form-label">TemplateName</label>
            <div class="layui-input-block">
                <label class="layui-form-label">${template.name}</label>
            </div>
        </div>

        <hr>
        <div class="layui-form-item">
            <label class="layui-form-label">Country:</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${template.country}</label>
            </div>

            <label class="layui-form-label">OrderType:</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${template.orderType}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Platform:</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${template.orderPlatform}</label>
            </div>
            <label class="layui-form-label">GoodsType:</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${template.goodsType}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Area:</label>
            <div class="layui-input-block">
                <label class="layui-form-label">${template.area}</label>
            </div>
        </div>
        <hr>
        <div class="layui-form-item">
            <label class="layui-form-label">FirstWeight</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.first}</label>
            </div>
            <label class="layui-form-label">FirstRegion</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.firstRegion}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">SecondWeight</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.second}</label>
            </div>
            <label class="layui-form-label">SecondRegion</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.secondRegion}</label>
            </div>
        </div>
        <hr>
        <div class="layui-form-item">
            <label class="layui-form-label">当日达</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.serviceType1}</label>
            </div>
            <label class="layui-form-label">次日达</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.serviceType2}</label>
            </div>
            <label class="layui-form-label">普件</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.serviceType3}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Level1</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.level1}</label>
            </div>
            <label class="layui-form-label">Level2</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.level2}</label>
            </div>
            <label class="layui-form-label">Level3</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.level3}</label>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Discount</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.discount}</label>
            </div>
            <label class="layui-form-label">Tax</label>
            <div class="layui-input-inline">
                <label class="layui-form-label">${factor.tax}</label>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
        });

    });
</script>
