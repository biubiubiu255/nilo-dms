<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>

<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("orderTypeList", SystemCodeUtil.getSystemCodeList((String) session.getAttribute("merchantId"), "delivery_order_type"));
%>
<body>
<div class="box-body">


    <!-- /.box-header -->
    <div class="layui-row">

        <div class="layui-col-md5">
<%--            <shiro:hasPermission name="400061">
                <button class="layui-btn layui-btn-normal loading-scan">Delivery Scan</button>
            </shiro:hasPermission>--%>
            <button class="layui-btn layui-btn-normal loading-scan">Delivery Scan</button>
            <button class="layui-btn btn-search">Search</button>
        </div>

    </div>
    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content ">
                <div class="layui-form layui-row">
                    <div class="layui-col-md4 layui-col-lg3">
                        LoadingNo：
                        <div class="layui-inline">
                            <input class="layui-input" name="loadingNo" autocomplete="off">
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg5">
                        <label class="layui-form-label" style="width:110px">Status:</label>
                        <div class="layui-form-item layui-inline" style="margin: 0px">
                            <select lay-filter="status" name="status">
                                <option value="">Pls select Status...</option>
                                <option value=0>Loading</option>
                                <option value=1>Ship</option>
                            </select>
                        </div>
                    </div>

                    <div class="layui-col-md4 layui-col-lg3">
                        <label class="layui-form-label" style="width:110px">Rider:</label>
                        <div class="layui-form-item layui-inline" style="margin: 0px">
                            <select lay-filter="select-rider-fy" name="rider">
                                <option value="">choose or search....</option>
                                <c:forEach items="${riderList}" var="rider">
                                    <option value="${rider.userId}">${rider.staffId}-${rider.nickName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="layui-col-md1">
                        <button class="layui-btn layui-btn-normal search">Search</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <table class="layui-table" lay-data="{ url:'/waybill/rider_delivery/list.html' ,method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'handleNo', width:140}">LoadingNo</th>
            <th lay-data="{field:'riderName', width:150}">Rider</th>
            <th lay-data="{field:'handle_time', width:170, templet:'<div>{{ formatDate(d.handle_time) }}</div>'}">
                HandleTime
            </th>
            <th lay-data="{field:'', width:170, templet:'<div>{{ formatDate(d.created_time) }}</div>'}">
                CreatedTime
            </th>
            <%--<th lay-data="{field:'remark', width:120}">Remark</th>--%>
            <th lay-data="{field:'', width:120, templet:'<div>{{ parseStatus(d.status) }}</div>'}">Status</th>
            <th lay-data="{title:'Opt', width:230, align:'center', toolbar: '#barDemo', fixed: 'right'}"></th>
        </tr>
        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="tool-print">Print</a>
        <a class="layui-btn layui-btn-primary  layui-btn-mini" lay-event="tool-edit">Detail</a>
        <a class="layui-btn layui-btn-danger  layui-btn-mini" lay-event="tool-ship">Ship</a>
    </script>
</div>
<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use(['form', 'layer', 'element', 'laydate'], function () {
            var layDate = layui.laydate;
            layDate.render({
                elem: '#fromCreatedTime'
                , lang: 'en'
            });
            layDate.render({
                elem: '#toCreatedTime'
                , lang: 'en'
            });
        });
        var table;
        layui.use('table', function () {
            table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                var handleNo = data.handleNo;
                if (obj.event === 'tool-detail') {
                    toDetails(handleNo);
                }else if (obj.event === 'tool-print') {
                    if(data.status!=1){
                        layer.msg("Please first shipment", {icon: 2, time: 2000});
                        return;
                    }
                    toPrint(handleNo);
                } else if (obj.event === 'tool-ship') {
                    if(data.status==1){
                        layer.msg("Has been shipped", {icon: 2, time: 2000});
                        return;
                    }
                    ship(handleNo);
                } else if (obj.event === 'tool-edit') {
                    edit(handleNo);
                }

            });

            $(".search").on("click", function () {
                reloadTable();
            })
            $(".btn-search").on("click", function () {
                $(".layui-colla-content").toggleClass("layui-show");
                $(".btn-search").toggleClass("layui-btn-warm");
            })


        });

        function reloadTable(item) {
            table.reload("${id0}", {
                where: {
                    handleNo: $("input[name='loadingNo']").val(),
                    status: $("select[name='status']").val(),
                    rider:  $("select[name='rider']").val()
                }
            });
        };

        $(".loading-scan").on("click", function () {
            toLoadingScanPage("");
        })

        function toDetails(handleNo) {


            parent.layer.open({
                type: 2,
                title: 'Detail',
                shadeClose: true,
                shade: false,
                maxmin: true, //开启最大化最小化按钮
                area: ['900px', '600px'],
                offset: ['100px', '250px'],
                content: "/waybill/rider_delivery/detail.html?loadingNo=" + handleNo
            });

        }
        function toPrint(handleNo) {
            parent.window.open("/waybill/rider_delivery/print.html?loadingNo=" + handleNo);
            location.reload();
        }


        function toLoadingScanPage() {
            var url = "/waybill/rider_delivery/addLoadingPage.html";
            parent.addTabs({
                id: '40007002',
                title: 'Rider Delivery',
                close: true,
                url: url
            });
        }

        //暂时停用
        function ship(handleNo) {
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/waybill/rider_delivery/updateStatus.html",
                dataType: "json",
                data: {
                    handleNo: handleNo,
                    status: 1
                },
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            reloadTable();
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

    function edit(handleNo) {
        parent.layer.open({
            type: 2,
            title: 'Edit',
            shadeClose: true,
            shade: false,
            maxmin: true, //开启最大化最小化按钮
            area: ['900px', '600px'],
            offset: ['100px', '250px'],
            content: '/waybill/rider_delivery/editPage.html?handleNo='+handleNo,
        });

    }

    function strainerValue(str){
        if(typeof(str)=='undefined') str='';
        return str;
    }

    function parseStatus(str){
        if(str=="0"){
            str = "Loading";
        }else if(str=="1"){
            str = "Ship";
        }else {
            str = "";
        }
        return str;
    }

</script>
</body>
</html>