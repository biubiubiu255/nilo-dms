<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id3", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("id4", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("goodsTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "goods_type"));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
%>

<div class="box-body">

    <div class="layui-collapse">

        <div class="layui-colla-item">
            <h2 class="layui-colla-title">Delivery Order Info</h2>
            <div class="layui-colla-content layui-show">
                <form id="editOrderHeader" class="layui-form" action="">
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">OrderNo:</label>
                            <div class="layui-input-inline">
                                <input type="text" id="orderNo" name="orderNo" value="${delivery.orderNo}" autocomplete="off" class="layui-input" readonly>
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">OrderType:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="orderType" lay-verify="required" name="orderType">
                                    <option value="">Pls select order type...</option>
                                    <c:forEach items="${orderTypeList}" var="r">
                                        <option value=${r.code} <c:if test="${r.code == delivery.orderType}">selected</c:if> >${r.code}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">ReferenceNo:</label>
                            <div class="layui-input-inline">
                                <input type="text" name="referenceNo" value="${delivery.referenceNo}" lay-verify="required" autocomplete="off"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">OrderTime:</label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" lay-verify="datetime" name="orderDateTime"
                                       id="orderDateTime"
                                       placeholder="Order Time">
                            </div>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">TotalPrice:</label>
                            <div class="layui-input-inline">
                                <input type="text" name="totalPrice" autocomplete="off" value="${delivery.totalPrice}"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">Weight:</label>
                            <div class="layui-input-inline">
                                <input type="text" name="weight" lay-verify="required|number" class="layui-input" value="${delivery.weight}" >
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">Volume:</label>
                            <div class="layui-input-inline">
                                <input type="text" name="volume"  autocomplete="off" class="layui-input" value="" >
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">PayType:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="payType" lay-verify="required" name="payType">
                                    <option value="">Pls select...</option>
                                    <option>Sender To Pay</option>
                                    <option>Receiver To Pay</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">GoodsType:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="goodsType" lay-verify="required" name="goodsType">
                                    <option value="">Pls select goods type...</option>
                                    <c:forEach items="${goodsTypeList}" var="r">
                                        <option value=${r.code} <c:if test="${delivery.goodsType == r.code}">selected</c:if> >${r.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">Notes:</label>
                        <div class="layui-input-block" style="width:500px">
                            <textarea placeholder="notes" class="layui-textarea" name="notes"></textarea>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="edit-header">Submit
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="layui-colla-item">
            <h2 class="layui-colla-title">Receiver Info</h2>
            <div class="layui-colla-content">
                <form id="editReceiverInfo" class="layui-form" action="">
                    <input type="hidden" name="orderNo" value="${delivery.orderNo}" autocomplete="off" class="layui-input" >
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">Name:</label>
                            <div class="layui-input-inline">
                                <input type="text" name="receiverName" value="${delivery.receiverInfo.receiverName}" lay-verify="required" autocomplete="off"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">Country:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="country1" id="country1" lay-verify="required" name="receiverCountry">
                                    <option value="">Pls select...</option>
                                    <option value="CN" <c:if test="${delivery.receiverInfo.receiverCountry == 'CN'}">selected</c:if> >China</option>
                                    <option value="KE" <c:if test="${delivery.receiverInfo.receiverCountry == 'KE'}">selected</c:if> >Kenya</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">Phone:</label>
                            <div class="layui-input-inline">
                                <input type="text" lay-verify="required" name="receiverPhone" autocomplete="off" value="${delivery.receiverInfo.receiverPhone}"
                                       class="layui-input">
                            </div>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">Province:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="province1" id="province1" lay-verify="required" name="receiverProvince">
                                    <option selected>${delivery.receiverInfo.receiverProvince}</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">City:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="city1" id="city1" lay-verify="required" name="receiverCity">
                                    <option selected>${delivery.receiverInfo.receiverCity}</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">Area:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="area1" id="area1" name="receiverArea">
                                    <option selected>${delivery.receiverInfo.receiverArea}</option>
                                </select>
                            </div>
                        </div>

                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">Address:</label>
                        <div class="layui-input-block" style="width:500px">
                            <textarea placeholder="receiverAddress" lay-verify="required" class="layui-textarea"
                                      name="receiverAddress">${delivery.receiverInfo.receiverAddress}</textarea>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="edit-receiver-info" type="button">Submit
                            </button>
                        </div>
                    </div>
                </form>
            </div>

        </div>
        <div class="layui-colla-item">
            <h2 class="layui-colla-title">Sender Info</h2>
            <div class="layui-colla-content">
                <form id="editSenderInfo" class="layui-form" action="">
                    <input type="hidden"  name="orderNo" value="${delivery.orderNo}" autocomplete="off" class="layui-input" >
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">Name:</label>
                            <div class="layui-input-inline">
                                <input type="text" name="senderName" autocomplete="off" class="layui-input" value="${delivery.senderInfo.senderName}">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">Country:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="country2" id="country2" lay-verify="required" name="senderCountry">
                                    <option value="">Pls select...</option>
                                    <option value="CN" <c:if test="${delivery.senderInfo.senderCountry == 'CN'}">selected</c:if> >China</option>
                                    <option value="KE" <c:if test="${delivery.senderInfo.senderCountry == 'KE'}">selected</c:if> >Kenya</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">Phone:</label>
                            <div class="layui-input-inline">
                                <input type="text" name="senderPhone" autocomplete="off" value="${delivery.senderInfo.senderPhone}"
                                       class="layui-input">
                            </div>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">Province:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="province2" id="province2" name="senderProvince">
                                    <option selected>${delivery.senderInfo.senderProvince}</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">City:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="city2" id="city2" name="senderCity">
                                    <option selected>${delivery.senderInfo.senderCity}</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">Area:</label>
                            <div class="layui-input-inline">
                                <select lay-filter="area2" id="area2" name="senderArea">
                                    <option selected>${delivery.senderInfo.senderArea}</option>
                                </select>
                            </div>
                        </div>

                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">Address:</label>
                        <div class="layui-input-block" style="width:500px">
                            <textarea placeholder="senderAddress" class="layui-textarea" name="senderAddress" >${delivery.senderInfo.senderAddress}</textarea>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="edit-sender-info" type="button">Submit
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="layui-colla-item">
            <h2 class="layui-colla-title">Goods Info</h2>
            <div class="layui-colla-content">
                <button class="layui-btn add-goods-page" type="button">ADD</button>
                <hr>
                <table class="layui-hide" id="${id3}"></table>
            </div>
        </div>
    </div>

</div>

<script type="text/html" id="${id4}">
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="edit-goods">Edit</a>
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="del-goods">Delete</a>
</script>
<%@ include file="../common/footer.jsp" %>
<script>
    $(function () {
        layui.use(["address", "address2"], function () {
            var address = layui.address();
            var address2 = layui.address2();

        });
        layui.use('laydate', function () {
            var layDate = layui.laydate;
            layDate.render({
                elem: '#orderDateTime',
                type: 'datetime',
                lang: 'en'
            });
        });

        var  form,table;
        layui.use(['element', 'form'], function () {
            var element = layui.element;
            form = layui.form;
            form.on('submit(edit-header)', function (data) {

                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/deliveryOrder/editHeader.html",
                    dataType: "json",
                    data: $("#editOrderHeader").serialize(),
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                                if (data.data) {
                                    $("input[name='orderNo']").val(data.data);
                                }
                            });
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

            form.on('submit(edit-sender-info)', function (data) {


                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/deliveryOrder/editSender.html",
                    dataType: "json",
                    data: $("#editSenderInfo").serialize(),
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                                layer.closeAll();
                            });
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

            form.on('submit(edit-receiver-info)', function (data) {

                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/order/deliveryOrder/editReceiver.html",
                    dataType: "json",
                    data: $("#editReceiverInfo").serialize(),
                    success: function (data) {
                        if (data.result) {
                            layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                                layer.closeAll();
                            });
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

        layui.use('table', function () {
            table = layui.table;
            //方法级渲染
            table.render({
                elem: '#${id3}'
                , url: '/order/deliveryOrder/getGoodsList.html'
                , where: {orderNo: $("#orderNo").val()}
                , cols: [[
                    {field: 'id', title: 'ID', width: 80,}
                    , {field: 'goodsId', title: 'Goods ID', width: 200, edit: 'text'}
                    , {field: 'goodsDesc', title: 'Goods Desc', width: 200, edit: 'text'}
                    , {field: 'qty', title: 'Qty', width: 200, edit: 'text'}
                    , {field: 'quality', title: 'Quality', width: 150, edit: 'text'}
                    , {title: "Operation", width: 200, align: 'center', toolbar: '#${id4}'}
                ]]
                , id: '${id3}'
            });
            //监听工具条
            table.on('tool', function (obj) {
                var data = obj.data;
                if (obj.event === 'del-goods') {
                    layer.confirm('Confirm Delete?', function (index) {
                        delGoods(data);
                        obj.del();
                    });
                }
                if (obj.event === 'edit-goods') {
                    editGoods(data);
                }
            });
        });

        $(".add-goods-page").on('click', function () {

            $.ajax({
                url: "/order/deliveryOrder/addGoodsPage.html",
                type: 'GET',
                data: {orderNo: $("#orderNo").val()},
                dataType: 'text',
                success: function (data) {
                    var index = parent.layer.open({
                        type: 1,
                        content: data,
                        area: ['800px', '600px'],
                        offset: ['200px', '200px'],
                        end: function () {

                            table1.reload("${id0}", {
                                where: {
                                    orderNo: $("#orderNo").val(),
                                }
                            });
                        }
                    });
                }
            });
        });

        function editGoods(goods){
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/order/deliveryOrder/editGoods.html",
                dataType: "json",
                data: {orderNo:$("#orderNo").val(),goodsId:goods.goodsId,goodsDesc:goods.goodsDesc,qty:goods.qty},
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                        });
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    }
                },
                complete: function () {
                    layer.close(load);
                }
            });
        }

        function delGoods(goods){
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/order/deliveryOrder/delGoods.html",
                dataType: "json",
                data: {orderNo:$("#orderNo").val(),goodsId:goods.goodsId},
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                        });
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    }
                },
                complete: function () {
                    layer.close(load);
                }
            });
        }

    });
</script>
</body>
</html>