<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("merchantId", (String) session.getAttribute("merchantId"));
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
%>
<body>
<div class="box-body">
    <div class="layui-row">
        <div class="layui-col-md5">
            <shiro:hasPermission name="400016">
                <button class="layui-btn btn-search">Search
                </button>
            </shiro:hasPermission>
            <shiro:hasPermission name="400017">
                <button class="layui-btn layui-btn-normal btn-export">Export</button>
            </shiro:hasPermission>
        </div>
    </div>

    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content ">
                <div class="layui-form layui-row" useform="basic">
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OrderOriginal:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="orderOriginal" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">Country:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="country" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OrderNo:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="order_no" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OrderType:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="order_type" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">ReferenceNo:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="reference_no" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">SendCompany:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="send_company" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">Driver:</label>
                        <div class="layui-input-inline">
                            <input type="text"  autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OrderStatus:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="order_status" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">PayStatus:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="payStatus" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                </div>

                <div class="layui-form layui-row" useform="date">
                    <div class="layui-col-md8 layui-col-lg5">
                        <label class="layui-form-label">ArriveTime:</label>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="arrive_time_start" id="arrive_time_start"
                                   placeholder="From" use="date">
                        </div>
                        -
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="arrive_time_end" id="arrive_time_end"
                                   placeholder="To" use="date">
                        </div>
                    </div>
                    <div class="layui-col-md8 layui-col-lg5">
                        <label class="layui-form-label">SendTime:</label>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="send_time_start" id="send_time_start"
                                   placeholder="From" use="date">
                        </div>
                        -
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="send_time_end" id="send_time_end"
                                   placeholder="To" use="date">
                        </div>
                    </div>
                    <div class="layui-col-md8 layui-col-lg5">
                        <label class="layui-form-label">DeliveryTime:</label>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="delivery_time_start" id="delivery_time_start"
                                   placeholder="From" use="date">
                        </div>
                        -
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="delivery_time_end" id="delivery_time_end"
                                   placeholder="To" use="date">
                        </div>
                    </div>
                    <div class="layui-col-md8 layui-col-lg5">
                        <label class="layui-form-label">SignName:</label>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="sign_name_start" id="sign_name_start"
                                   placeholder="From" use="date">
                        </div>
                        -
                        <div class="layui-inline">
                            <input type="text" class="layui-input" name="sign_name_end" id="sign_name_end"
                                   placeholder="To" use="date">
                        </div>
                    </div>
                </div>

                <div class="layui-form layui-row">
                    <div class="layui-col-md1">
                        <button class="layui-btn layui-btn-normal search">Search</button>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <hr>
    <iframe scrolling="no" frameborder="0" src="/report/cod/list.html?exportType=0" id="ifm" width="100%" height="100%" style="padding: 0px;"></iframe>

    <%@ include file="../../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {

            layui.use(['element', 'form', 'laydate'], function () {
                var layDate = layui.laydate;

                var lab_input_dates = $("input[use='date']");
                for (i=0; i<lab_input_dates.length; i++){
                    //console.log("日期组件装载成功：" + lab_input_dates[i].id);
                    layDate.render({
                        elem: '#' + lab_input_dates[i].id
                        , lang: 'en'
                    });
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            });

            $(".btn-search").on("click", function () {
                $(".layui-colla-content").toggleClass("layui-show");
                $(".btn-search").toggleClass("layui-btn-warm");
            });

            $(".btn-export").on("click", function () {
                var param = getParam();
                var url = "/report/cod/list.html?" + param + "&exportType=1";
                window.location.href = url;
            });

            function reloadTable() {

                //console.log(param);

                var param = getParam();

                layui.use('layer', function () {
                    var layer = layui.layer;
                    //layer.msg('Please select the full date', {icon: 2, time: 2000});
                });

                var url = "/report/cod/list.html";

                document.getElementById("ifm").src = url + '?' + param + "&exportType=0";
                /*
                parent.addTabs({
                    id: '400099001',
                    title: 'COD Report',
                    close: true,
                    url: url
                });
                */

            };

            function getParam(){
                console.log("正在进行序列化");
                var param = '';

                var inputAll = $(".layui-form[useForm='basic']").find("input");
                //console.log("found number:" + inputAll.length);
                inputAll.each(function(index, e){
                    if (this.name!='') param += this.name + '='+this.value + '&';
                });
                if (inputAll.length>0) param = param.substring(0, param.length-1);

                var inputAll = $(".layui-form[useForm='date']").find("input");
                var point = '';
                inputAll.each(function(index, e){
                    this.value=='' ? point='' : point=Date.parse(new Date(this.value)) / 1000;
                    if (this.name!='') param +=  '&' + this.name + '='+point;
                });
                return param;
            }
        });

    </script>
</body>
</html>
