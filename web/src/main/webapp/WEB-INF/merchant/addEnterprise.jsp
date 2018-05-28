<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<div class="box-body">

    <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
        <ul class="layui-tab-title">
            <li class="layui-this">Basic</li>
            <li>Business</li>
        </ul>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">

                <form id="companyForm" class="layui-form" action="">

                    <div class="layui-form-item">
                        <label class="layui-form-label">Merchant Name</label>
                        <div class="layui-input-inline">
                            <input type="text" value="${merchantInfo.merchantName}" name="merchantName"
                                   autocomplete="off" class="layui-input">
                        </div>
                        <div class="layui-input-inline">
                            <input type="checkbox" name="vip" title="VIP" value="1"
                                   lay-skin="primary" >
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">Country</label>
                        <div class="layui-input-inline">
                            <lp:enumTag selectId="country" selectName="country" className="CountryEnum"
                                        code="" disabled="false" />
                        </div>

                        <label class="layui-form-label">Level</label>
                        <div class="layui-input-inline">
                            <lp:enumTag selectId="level" selectName="levelCode" className="LevelEnum"
                                        code="" disabled="false" />
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">Enterprise Type</label>
                        <div class="layui-input-inline">
                            <lp:enumTag selectId="type" selectName="typeCode" className="CustomerTypeEnum"
                                        code="" disabled="false" exclude="person"/>
                        </div>
                        <label class="layui-form-label">Nature Type</label>
                        <div class="layui-input-inline">
                            <lp:enumTag selectId="nature" selectName="natureCode" className="NatureTypeEnum"
                                        code="" disabled="false"/>
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label class="layui-form-label">Licence</label>
                        <div class="layui-input-block">
                            <input type="text" name="licence" value="" autocomplete="off"
                                   class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">ID Card</label>
                        <div class="layui-input-block">
                            <input type="text" name="idCard" value="" autocomplete="off"
                                   class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="merchant-submit">Submit</button>
                            <button type="reset" class="layui-btn layui-btn-primary reset">Reset</button>
                        </div>
                    </div>
                </form>

            </div>
            <div class="layui-tab-item">
                <form id="businessForm" class="layui-form" action="">
                    <input type="hidden" id="id" name="id" value="">
                    <div class="layui-form-item">
                        <label class="layui-form-label">Business Scope</label>
                        <div class="layui-input-block">
                            <lp:enumTag selectId="businessScope" selectName="businessScope" className="BusinessScopeEnum"
                                        code="" disabled="false" multi="true"/>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">Contact Name</label>
                        <div class="layui-input-block">
                            <input type="text" value="" name="contactName" autocomplete="off"
                                   class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">Contact Phone</label>
                        <div class="layui-input-block">
                            <input type="text" value="" name="contactPhone" autocomplete="off"
                                   class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">Contact Email</label>
                        <div class="layui-input-block">
                            <input type="text" value="" name="contactEmail" autocomplete="off"
                                   class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">Address</label>
                        <div class="layui-input-block">
                            <input type="text" value="" name="address" autocomplete="off"
                                   class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="business-submit">Submit</button>
                            <button type="reset" class="layui-btn layui-btn-primary reset">Reset</button>
                        </div>
                    </div>
                </form>
            </div>

        </div>
    </div>


</div>

<script type="text/javascript">
    $(function () {

        layui.use('element', function () {
            var element = layui.element;
        });

        layui.use('form', function () {
            var form = layui.form;
            form.render();
            //监听提交
            form.on('submit(merchant-submit)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/admin/merchant/addMerchant.html",
                    data: $("#companyForm").serialize(),
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            $("#id").val(data.data);
                            layer.msg('SUCCESS!', {icon: 1, time: 1000}, function () {
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
                return false;
            });

            form.on('submit(business-submit)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/admin/merchant/updateMerchant.html",
                    data: $("#businessForm").serialize(),
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
                return false;
            });
        });
    });

</script>
