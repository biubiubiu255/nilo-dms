<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box">
    <div class="row">
        <div class="col-md-2">
            <div class="form-group">
                <label>OrderNo:</label>
                <input name="orderNo" class="form-control" type="text">
            </div>
        </div>
        <div class="col-md-2">
            <div class="form-group">
                <label>OrderType:</label>
                <input name="orderType" class="form-control" type="text">
            </div>
        </div>
        <div class="col-md-2">
            <div class="form-group">
                <label>From:</label>
                <div class="input-group date from_date" data-date="" data-date-format="dd MM yyyy"
                     data-link-field="fromTime" data-link-format="yyyy-mm-dd">
                    <input class="form-control" size="20" type="text" value="" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
                <input type="hidden" id="fromTime" value=""/>
            </div>

        </div>
        <div class="col-md-2">
            <div class="form-group">
                <label>To:</label>
                <div class="input-group date to_date" data-date="" data-date-format="dd MM yyyy"
                     data-link-field="toTime" data-link-format="yyyy-mm-dd">
                    <input class="form-control" size="20" type="text" value="" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
                <input type="hidden" id="toTime" value=""/>
            </div>
        </div>
        <div class="col-md-1">
            <div class="form-group">
                <label>&nbsp;</label>
                <button type="button" class="form-control btn btn-info" name="search">Search</button>
            </div>
        </div>
    </div>
</div>
<!-- /.box-header -->
<div class="layui-btn-group demoTable">
    <button class="layui-btn" data-type="batchDispatchToDMS">Batch Dispatch</button>
</div>

<table class="layui-table" lay-data="{ url:'/order/dispatchToDMS/list.html', page:true,limit:10, id:'dispatchToDMS'}"
       lay-filter="demo">
    <thead>
    <tr>
        <th lay-data="{checkbox:true, fixed: true}"></th>
        <th lay-data="{fixed: 'left',field:'orderNo', width:200}">OrderNo</th>
        <th lay-data="{field:'orderType', width:100}">OrderType</th>
        <th lay-data="{field:'referenceNo', width:200}">ReferenceNo</th>
        <th lay-data="{field:'orderTime', width:170, templet:'<div>{{ formatDate(d.orderTime) }}</div>'}">OrderTime</th>
        <th lay-data="{field:'country', width:100}">Country</th>
        <th lay-data="{field:'weight', width:100}">Weight</th>
        <th lay-data="{field:'goodsType', width:120}">GoodsType</th>
        <th lay-data="{field:'status', width:100}">Status</th>
        <th lay-data="{field:'receiverInfo', width:150,templet: '<div>{{d.receiverInfo.name}}</div>' }">Receiver Name
        </th>
        <th lay-data="{field:'receiverInfo', width:150,templet: '<div>{{d.receiverInfo.contactNumber}}</div>' }">
            Receiver Phone
        </th>
        <th lay-data="{field:'receiverInfo', width:150,templet: '<div>{{d.receiverInfo.address}}</div>' }">Receiver
            Address
        </th>

        <th lay-data="{field:'userdefine01', width:150}">UserDefine01</th>
        <th lay-data="{field:'userdefine02', width:150}">UserDefine02</th>
        <th lay-data="{field:'userdefine03', width:150}">UserDefine03</th>
        <th lay-data="{field:'userdefine04', width:150}">UserDefine04</th>
        <th lay-data="{field:'userdefine05', width:150}">UserDefine05</th>

        <th lay-data="{title:'Opt',fixed: 'right', width:160, align:'center', toolbar: '#barDemo'}"></th>
    </tr>
    </thead>
</table>

<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-primary layui-btn-mini" lay-event="detail">Detail</a>
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="arrive">Dispatch</a>
</script>

<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        var startDate = new Date();
        startDate.setMonth(startDate.getMonth() - 1);
        $('.from_date').datetimepicker({
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: 0,
            startDate: startDate,
            setDate: startDate,
        });

        $('.to_date').datetimepicker({
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: 0,
            startDate: startDate,
            setDate: new Date(),
        });

        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    var orderNo = data.orderNo;
                    layer.msg(orderNo);
                } else if (obj.event === 'arrive') {
                    var orderNo = data.orderNo;
                    $.ajax({
                        type: "POST",
                        url: "/order/dispatchToDMS/dispatchToDMS.html",
                        dataType: "json",
                        data: {orderNo:orderNo},
                        success: function (data) {
                            if (data.result) {
                                layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                                    reloadTable();
                                });
                            } else {
                                layer.msg(data.msg, {icon: 2, time: 2000});
                            }
                        },
                    });
                }
            });

            var $ = layui.$, active = {
                batchDispatchToDMS: function () { //获取选中数据
                    var checkStatus = table.checkStatus('dispatchToDMS')
                            , data = checkStatus.data;
                    layer.alert(JSON.stringify(data));
                }
            };

            $('.demoTable .layui-btn').on('click', function () {
                var type = $(this).data('type');
                active[type] ? active[type].call(this) : '';
            });

            var reloadTable = function (item) {
                table.reload("dispatchToDMS", {
                    where: {
                        //key: { //该写法上文已经提到
//                    type: item.type, id: item.id
                        //}
                    }
                });
            };

        });
    });

</script>
</body>
</html>