<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<%
    request.setAttribute("goodsTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "goods_type"));
%>
<div class="box-body">


    <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
        <ul class="layui-tab-title">
            <li class="layui-this">Basic</li>
        </ul>
        <div class="layui-tab-content">
            <form id="basicForm" class="layui-form" action="">
                <input type="hidden" name="id" value="${template.id}">

                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">Template Name</label>
                    <div class="layui-input-inline">
                        <input type="text" name="name" value="${template.name}" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">Customer Type</label>
                    <div class="layui-input-inline">
                        <lp:enumTag selectId="customerType" selectName="customerType" className="CustomerTypeEnum"
                                    code="${template.customerType}" disabled="false"/>

                    </div>
                    <label class="layui-form-label" style="width: 130px">Country</label>
                    <div class="layui-input-inline">
                        <select lay-filter="country1" id="country1" name="country">
                            <option value="CN" selected>China</option>
                        </select>
                        <div style="display: none">
                            <select lay-filter="country2" id="country2" name="country2">
                                <option value="CN" selected>China</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">Customer Level</label>
                    <div class="layui-input-inline">
                        <lp:enumTag selectId="level" selectName="customerLevel" className="LevelEnum"
                                    code="${template.customerLevel}" disabled="false"/>
                    </div>
                    <label class="layui-form-label" style="width: 130px">Settlement</label>
                    <div class="layui-input-inline">
                        <lp:enumTag selectId="settleType" selectName="settleType" className="SettleTypeEnum"
                                    code="${template.settleType}" disabled="false"/>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">Origin</label>
                    <div class="layui-input-inline">
                        <select lay-filter="province1" id="province1" lay-verify="required">
                            <option selected>${template.origin}</option>
                        </select>
                    </div>
                    <div class="layui-input-inline">
                        <select lay-filter="city1" id="city1" name="origin" lay-verify="required">
                            <option selected>${template.origin}</option>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">Destination</label>
                    <div class="layui-input-inline">
                        <select lay-filter="province2" id="province2" lay-verify="required">
                            <option selected>${template.destination}</option>
                        </select>
                    </div>
                    <div class="layui-input-inline">
                        <select lay-filter="city2" id="city2" name="destination" lay-verify="required">
                            <option selected>${template.destination}</option>

                        </select>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">Product Type</label>
                    <div class="layui-input-inline">
                        <lp:enumTag selectId="serviceProduct" selectName="serviceProduct" className="ProductTypeEnum"
                                    code="${template.serviceProduct}" disabled="false"/>
                    </div>

                    <label class="layui-form-label" style="width: 130px">Transport</label>
                    <div class="layui-input-inline">
                        <lp:enumTag selectId="transportType" selectName="transportType" className="TransportTypeEnum"
                                    code="${template.transportType}" disabled="false"/>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">Expire Date</label>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" name="fromTime" id="fromTime" placeholder="From">

                    </div>

                    <label class="layui-form-label" style="width: 130px">To</label>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" name="toTime" id="toTime" placeholder="To">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">First Region</label>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" value="${template.firstRegion}" name="firstRegion">
                    </div>
                    <label class="layui-form-label" style="width: 130px">First Fee</label>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" value="${template.firstFee}" name="firstFee">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">Second Region</label>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" value="${template.secondRegion}" name="secondRegion">
                    </div>
                    <label class="layui-form-label" style="width: 130px">Second Fee</label>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" value="${template.secondFee}" name="secondFee">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: 130px">Basic Fee</label>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" value="${template.basicFee}"  name="basicFee">
                    </div>
                    <div class="layui-input-inline">
                        <button class="layui-btn" lay-submit lay-filter="add-basic-fee">Submit</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
        <ul class="layui-tab-title">
            <li class="layui-this">Fee Factor</li>
        </ul>
        <div class="layui-tab-content">
            <form id="factorForm" class="layui-form" action="">
                <input type="hidden" name="templateId" value="${template.id}" id="templateId">
                <div class="layui-form-item">
                    <input type="hidden" name="factorDOs[0].factorType" value="category_type">
                    <label class="layui-form-label" style="width: 130px">Category Type</label>
                    <div class="layui-input-inline">
                        <lp:enumTag selectId="categoryType" selectName="factorDOs[0].factorCode" className="DeliveryCategoryTypeEnum"
                                    code="${map.category_type.factorCode}" disabled="false"/>
                    </div>
                    <div class="layui-input-inline" style="width: 80px">
                        <select name="factorDOs[0].operator" lay-filter="factorDOs[0].operator"  lay-verify="required" >
                            <option value="+" <c:if test="${map.category_type.operator=='+'}">selected</c:if> >+</option>
                            <option value="*" <c:if test="${map.category_type.operator=='*'}">selected</c:if> >*</option>
                        </select>
                    </div>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" value="${map.category_type.fee}" name="factorDOs[0].fee">
                    </div>

                </div>



                <div class="layui-form-item">

                    <input type="hidden" name="factorDOs[1].factorType" value="goods_type">
                    <label class="layui-form-label" style="width: 130px">Goods Type</label>
                    <div class="layui-input-inline">
                        <select name="factorDOs[1].factorCode" lay-filter="factorDOs[1].factorCode" lay-verify="required" lay-search="">
                            <option value="">Select type....</option>
                            <c:forEach items="${goodsTypeList}" var="r">
                                <option value=${r.code} <c:if test="${map.goods_type.factorCode==r.code}">selected</c:if> >${r.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="layui-input-inline" style="width: 80px">
                        <select name="factorDOs[1].operator" lay-filter="factorDOs[1].operator" lay-verify="required">
                            <option value="+" <c:if test="${map.goods_type.operator=='+'}">selected</c:if> >+</option>
                            <option value="*" <c:if test="${map.goods_type.operator=='*'}">selected</c:if> >*</option>
                        </select>
                    </div>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" value="${map.goods_type.fee}" name="factorDOs[1].fee">
                    </div>
                    <div class="layui-input-inline">
                        <button class="layui-btn" lay-submit lay-filter="add-factor">Submit</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>

<script type="text/javascript">
    $(function () {

        var address, address2;
        layui.use(["address", "address2"], function () {
            var address = layui.address();
            var address2 = layui.address2();
        });

        layui.use('element', function () {
            var element = layui.element;
        });
        layui.use('laydate', function () {
            var layDate = layui.laydate;
            var d = new Date();
            var now = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
            layDate.render({
                elem: '#fromTime',
                lang: 'en',
            });
            layDate.render({
                elem: '#toTime',
                lang: 'en',
            });
        });

        layui.use('form', function () {
            var form = layui.form;
            //监听提交
            form.on('submit(add-basic-fee)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/systemConfig/deliveryFeeTemplate/editBasic.html",
                    data: $("#basicForm").serialize(),
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.result) {
                            $("#templateId").val(data.data);
                            layer.msg('SUCCESS', {icon: 1, time: 1000});
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

            form.on('submit(add-factor)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/systemConfig/deliveryFeeTemplate/editFactor.html",
                    data: $("#factorForm").serialize(),
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

            form.render();
        });

    });
</script>
