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
                <button class="layui-btn layui-btn-normal btn-pdf">View</button>
        </div>
    </div>

    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content">
                <div class="layui-form layui-row" useform="basic">
                    <div class="layui-col-md5 layui-col-lg3">
                        <label class="layui-form-label">CustomerType</label>
                        <div class="layui-input-inline">
                            <input type="text" name="orderOriginal" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">Country:</label>
                        <div class="layui-form-item layui-inline" style="margin: 0px; width: 41.9%;">
                            <select name="countryLay" lay-filter="countryLay">
                                <option value="Kenya">Kenya</option>
                                <option value="Uganda">Uganda</option>
                                <option value="Nigeria">Nigeria</option>
                                <%--
                                    <c:forEach items="${refuse_reason}" var="r">
                                    <option value=${r.code}>${r.value}</option>
                                </c:forEach>--%>
                            </select>
                            <input type="hidden" name="country" value="">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">Waybill No:</label>
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
                        <label class="layui-form-label">CustomerOrder:</label>
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
                        <label class="layui-form-label">Rider:</label>
                        <div class="layui-input-inline">
                            <input type="text"  autocomplete="off" class="layui-input" name="rider">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OrderStatus:</label>
                        <div class="layui-form-item layui-inline" style="margin: 0px; width: 41.9%;">
                            <select name="order_statusLay" lay-filter="order_statusLay">
                                <option value="20">Arrive</option>
                                <option value="30">Send</option>
                                <option value="30">Dispatcher</option>
                                <option value="50">sign</option>
                                <option value="60">resend</option>
                            </select>
                            <input type="hidden" name="order_status" value="">
                        </div>
                    </div>

                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">PayStatus:</label>
                        <div class="layui-form-item layui-inline" style="margin: 0px; width: 41.9%;">
                            <select  class="form-control input-sm" name="payStatusLay" lay-filter="payStatusLay" aria-invalid="false">
                                <option value="1">OK</option>
                                <option value="0">No</option>
                            </select>
                            <input type="hidden" name="payStatus" value="">
                        </div>
                    </div>

                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label">OutWarmDay:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="out_warm" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form" useform="date">
                        <div class="layui-col-md4 layui-col-lg4">
                            <label class="layui-form-label">PayBack Date:</label>
                            <div class="layui-inline">
                                <input type="text" class="layui-input" name="payBack_time_start" id="payBack_time_start"
                                       placeholder="From" use="date">
                            </div>
                            -
                            <div class="layui-inline">
                                <input type="text" class="layui-input" name="payBack_time_end" id="payBack_time_end"
                                       placeholder="To" use="date">
                            </div>
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
    <div id="me_tab">
    <table class="layui-table"
           lay-data="{ url:'/report/cod/list.html?exportType=2',method:'get', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:160}"><O></O>Waybill No</th>
            <th lay-data="{field: 'reference_no', width:130}">CustomerOrder</th>
            <th lay-data="{field: 'country', width:90}">Country</th>
            <th lay-data="{field: 'order_type', width:100}">OrderType</th>
            <th lay-data="{field: 'orderOriginal', width:130}">CustomerType</th>
            <th lay-data="{field: 'pay_type', width:90}">PayType</th>
            <th lay-data="{field: 'order_status', width:110 }">OrderStatus</th>
            <th lay-data="{field: 'send_company', width:125}">SendCompany</th>
            <th lay-data="{field: 'rider', width:90}">Rider</th>
            <th lay-data="{field: 'cycle', width:90}">PayBack Periods</th>
            <th lay-data="{field:'payStatus' , width:100, templet:'<div>{{  formatPayStatus(payStatus) }}</div>'}">PayStatus</th>
            <th lay-data="{field:'pay_orderNo', width:200}">PayOrderNo</th>
            <th lay-data="{field:'pay_price' , width:100}">Price</th>
            <th lay-data="{field:'weight', width:100}">Weight</th>
            <th lay-data="{field:'create_time', width:200, templet:'<div>{{ formatDate(create_time) }}</div>'}">CreateTime</th>
            <th lay-data="{field:'arrive_time', width:200, templet:'<div>{{ formatDate(arrive_time) }}</div>'}">ArriveTime</th>
            <th lay-data="{field:'send_time', width:200, templet:'<div>{{ formatDate(send_time) }}</div>'}">SendTime</th>
            <th lay-data="{field:'dispatcher_time', width:200, templet:'<div>{{ formatDate(dispatcher_time) }}</div>'}">DispatcherTime</th>
            <th lay-data="{field:'sign_time', width:200, templet:'<div>{{ formatDate(sign_time) }}</div>'}">SignTime</th>
            <th lay-data="{field:'payBackDate', width:200, templet:'<div>{{ formatDate(payBackDate) }}</div>'}">payBackDate</th>
            <th lay-data="{field:'delay' , width:100, templet:'<div>{{  formatPayStatus(delay) }}</div>'}">Delay</th>
        </tr>
        </thead>
    </table>
    </div>

    <iframe scrolling="no" frameborder="0" src="/report/cod/list.html?exportType=0" id="ifm" width="100%" height="100%" style="padding: 0px; display: none;"></iframe>

    <%@ include file="../../common/footer.jsp" %>
    <script src="${ctx}/dist/js/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {

            showPattern = 0;

            layui.use(['element', 'form', 'laydate'], function () {

                var form = layui.form;

                form.on('select(payStatusLay)', function (data) {
                    category = data.value;
                    //categoryName = data.elem[data.elem.selectedIndex].text;
                    $("input[name='payStatus']").val(category);
                });
                form.on('select(countryLay)', function (data) { $("input[name='country']").val(data.value); });
                form.on('select(order_statusLay)', function (data) { $("input[name='order_status']").val(data.value); });

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
                var param = getParam(1);
                var url = "/report/cod/list.html?" + param;
                window.location.href = url;
            });

            var table = layui.table;
            layui.use('table', function () {
                table = layui.table;
                //table.setPrototypeOf(where, {"exportType": 2});
                table.on('tool(demo)');
            });


            $(".btn-pdf").on("click", function () {
                showPattern==1 ? showPattern=0 : showPattern=1;
                reloadTable();
            })


            function reloadTable() {
                if (showPattern==0){
                    $("#me_tab").show();
                    $("#ifm").hide();
                    table.reload("${id0}", {
                        where: getParam(2, true)
                    });
                }else if(showPattern==1){
                    $("#ifm").show();
                    $("#me_tab").hide();
                    var url = "/report/cod/list.html";
                    document.getElementById("ifm").src = url + "?" + getParam(0);
                }


            };


            function getParam(dateType, isPojo){
                if (dateType=="" || dateType=='undefind') dateType=0;

                var paramOb = {};
                var param = '';

                var inputAll = $(".layui-form[useForm='basic']").find("input");
                //console.log("found number:" + inputAll.length);
                inputAll.each(function(index, e){
                    //if (this.name!='') param += this.name + '='+this.value + '&';
                    if (this.name!='' && this.use!='date') paramOb[this.name] = this.value;
                });
                if (inputAll.length>0) param = param.substring(0, param.length-1);

                var inputAll = $(".layui-form[useForm='date']").find("input");
                var point = '';
                inputAll.each(function(index, e){
                    this.value=='' ? point='' : point=Date.parse(new Date(this.value)) / 1000;
                    //if (this.name!='') param +=  '&' + this.name + '='+point;
                    if (this.name!='') paramOb[this.name] = point;
                });
                paramOb['exportType'] = dateType;
                if (isPojo===true) return paramOb;
                else return jQuery.param( paramOb );

            }


        });

    function formatPayStatus(status){
        console.log("ssss");
        if(status=="1"){
            return "OK";
        }else {
            return "No";
        }
    }

    </script>
</body>
</html>
