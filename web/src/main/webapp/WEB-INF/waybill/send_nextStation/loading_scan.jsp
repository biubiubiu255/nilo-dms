<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>
<%@ page import="com.nilo.dms.dto.common.User" %>
<html>
<%@ include file="../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>


<body>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">

        <div class="layui-form-item">
            <div class="deliveryDiv layui-col-md5 layui-col-lg3">
                <label class="layui-form-label">Express:</label>
                <div class="layui-form-item layui-inline" style="width: 190px">
                    <select lay-filter="fil-express" name="expressLay">
                        <option>Please select content</option>
                        <c:forEach items="${expressList}" var="express">
                            <option value="${express.expressCode}">${express.expressName}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name="express" value="">
                </div>
            </div>
            <div class="layui-col-md5 layui-col-lg3">
                <label class="layui-form-label">Driver:</label>
                <div class="layui-form-item layui-inline" style="width: 190px">
                    <select lay-filter="fil-driver" name="expressLay" id="sendDriver">
                    </select>
                    <input type="hidden" name="driver" value="">
                </div>
            </div>
        </div>

        <div class="layui-form-item">

            <label class="layui-form-label" style="width:110px">Next Station</label>
            <div class="layui-input-inline">
                <select name="nextNetworkId" lay-search="" lay-filter="nextNetworkId" lay-verify="required">
                    <option value="">choose or search....</option>
                    <c:forEach items="${nextStations}" var="station">
                        <option value="${station.code}" optionSeq="${station.code}" type="${station.type}">${station.name}</option>
                    </c:forEach>
                </select>
                <input type="hidden" name="network_id" value="">
                <input type="hidden" name="nextStation" value="">
            </div>


            <label class="layui-form-label" style="width:160px">OrderNo:</label>
            <div class="layui-input-inline">
                <input type="text" id="orderNo" autocomplete="off" placeholder="Scan" class="layui-input">
            </div>
        </div>

    </form>
    <hr>



    <div style="margin-left:120px;">
        <table id='${id0}' lay-filter="filterLab"></table>
    </div>



    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
    </script>


    <div class="layui-form-item">
        <div class="layui-input-block shipDiv" style="margin-left:120px;">
            <button class="layui-btn commit" value="0">Save</button>
            <button class="layui-btn commit" value="1">Ship</button>
        </div>
    </div>

</div>
<%@ include file="../../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        //var tableData = '{"msg":0,"code":0,"pages":0,"data":[{"deliveryOrder":{"alreadyPaid":0.0,"createdBy":"168081000340000768","createdTime":1518010827,"goodsInfoList":[],"height":258.0,"isCod":"1","length":456.0,"merchantId":"1","needPayAmount":10.0,"networkDesc":"Mombasa Office","networkId":4,"nextNetworkDesc":"Main Office","nextNetworkId":1,"orderNo":"Kili201802000003","orderType":"PG","orderTypeDesc":"Package","package":true,"printTimes":0,"printed":true,"receiverInfo":{"receiverAddress":"2222222222222222222222","receiverArea":"110106","receiverCity":"1101","receiverCountry":"CN","receiverName":"ronny","receiverPhone":"ronny","receiverProvince":"11"},"senderInfo":{"senderAddress":"aldkjf;","senderArea":"110101","senderCity":"1101","senderCountry":"CN","senderName":"ronny","senderPhone":"ronny","senderProvince":"11"},"serviceTypeDesc":"","status":"ARRIVED","statusDesc":"Arrived","updatedTime":1518010827,"weight":123.0,"width":789.0},"loadingBy":168081000340000768,"loadingNo":"L0020","num":1,"orderNo":"Kili201802000003","status":1}],"count":1}';
        //var tableData = '[{"loadingNo":"aa"},{"loadingNo":"bb"},{"loadingNo":"cc"}]';
        //tableData = JSON.parse(tableData);
        //tableData = tableData.data;
        var tableData = new Array();
        view();

        layui.use('form', function () {
            var form = layui.form;
            form.on('select(nextNetworkId)', function (data) {
                var seq = data.value.toString();
                $("input[name='network_id']").val(data.value);

                $("input[name='nextStation']").val($("option[optionSeq='"+seq+"']").html());
            });
            form.on('select(fil-driver)', function (data) {
                $("input[name='driver']").val(data.value);
            });
            form.on('select(fil-express)', function (data) {
                //alert(data.value);
                $("input[name='express']").val(data.value);
                showDriverSelect(data.value);
            });
        });

        var packList = new Array();
        var form, table;

        //表单监控
        layui.use('table', function () {
            table = layui.table;
            table.on('tool(filterLab)', function (obj) {
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                var orderNo = data.orderNo;

                if (obj.event === 'delete') {
                    //deleteLoadingDetails(orderNo);
                    //alert(data.id);
                    tableData.splice(data.index-1, 1);
                    for (var i=0;i<tableData.length;i++){
                        tableData[i].index = i+1;
                    }
                    view();
                }
            });
        });

        //小包扫描
        $("#orderNo").keydown(function (event) {
            event = document.all ? window.event : event;
            if ((event.keyCode || event.which) == 13) {
                var loadingNo = $("#loadingNo").val();
                if (loadingNo == '') {
                    return false;
                }
                var orderNo = $("#orderNo").val();
                if (loadingNo == '') {
                    return false;
                }
                var load = layer.load(2);
                $.ajax({
                    type: "POST",
                    url: "/waybill/send_nextStation/scanSmallPack.html",
                    dataType: "json",
                    data: {
                        orderNo: orderNo,
                    },
                    success: function (data) {
                        if (data.result) {
                            $("#orderNo").focus();
                            $("#orderNo").val('');
                            var res = JSON.parse(data.data).data
                            for(var i=0;i<tableData.length;i++){
                                if(res.orderNo==tableData[i].orderNo){
                                    layer.msg("Order already exists", {icon: 2, time: 2000});
                                    return ;
                                }
                            }
                            res.index = tableData.length+1;
                            foramOb(res);
                            tableData.push(res);
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                        view();
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
            }
        });

        //表单提交
        $('.commit').on('click', function (e) {
            //获取点击btn的value值
            var type = e.currentTarget.value;
            commit(type);
        });

        //提交数据
        function commit(type) {
            var printed = false;  //是否需要打印
            var saveStutus = 0;   //传输到后台的状态，0仅保存、1发运
            type = parseInt(type);
            switch (type){
                case 0:
                    saveStutus=0;
                    printed=false;
                    break;
                case 1:
                    saveStutus=1;
                    printed=true;
                    break;
                default:
                    return;
            }

            //集成提交参数
            var smallPack = "";
            for(var i=0;i<tableData.length;i++){
                smallPack += tableData[i].orderNo+",";
            }
            smallPack = smallPack.substring(0, smallPack.length-1);

            var driver = $("input[name='driver']").first().val();
            var networkCode = $("input[name='network_id']").first().val();
            var nextStation = $("input[name='nextStation']").first().val();
            var thirdExpressCode = $("input[name='express']").first().val();

            if (rider=="" || networkCode=="" || nextStation=="" || thirdExpressCode=="" || smallPack=="") {
                layer.msg("Please select the option", {icon: 2, time: 2000});
                return ;
            }

            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/waybill/send_nextStation/addLoading.html",
                dataType: "json",
                data: {
                    smallPack: smallPack,
                    driver: driver,
                    thirdExpressCode: thirdExpressCode,
                    networkCode: networkCode,
                    nextStation: nextStation,
                    saveStutus: saveStutus
                },
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            if(printed==true){
                                //这里需要返回一个大包号，才能打印，因为是在后台创建时生成订单号
                                //parent.window.open("/waybill/rider_delivery/print.html?loadingNo=" + data.data.handleNo);
                                //location.reload();
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
        }

        //视图渲染
        function view(){
            //console.log(tableData);
            layui.use('table', function(){
                var table = layui.table;
                console.log("条目：" + tableData.length);
                //第一个实例
                table.render({
                    page: false //开启分页
                    ,elem : '#${id0}'
                    ,height: 315
                    //,even: true
                    //,url: '/demo/table/user/' //数据接口
                    ,data : tableData
                    ,width: 868
                    ,even: true
                    ,text: {
                        none: 'Please start scanning the order'
                    }
                    ,page : {
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
                    }
                    ,limit : 3000
                    //,limits : [5, 10, 15, 20, 25]
                    ,cellMinWidth: 80

                    ,cols: [[ //表头 //layui-btn layui-btn-primary layui-btn-mini
                        //{ title: 'id', align:'center', width:80, sort: true, fixed: 'left', templet: '<div>{{d.LAY_INDEX }}</div>'}
                        { title: 'id', align:'center', width:80, sort: true, fixed: 'left', templet: '<div>{{d.index }}</div>'}
                        ,{field: 'orderNo', title: 'OrderNo', width:160, align:'center'}
                        ,{field: 'orderType', title: 'OrderType', width:150, align:'center'}
                        ,{field: 'referenceNo', title: 'ReferenceNo', width:130, align:'center'}
                        ,{field: 'weight', title: 'Weight(KG)', width:130, align:'center'}
                        ,{field: '', title: 'opt', width:80, toolbar: '#barDemo', fixed: 'right', align:'center'}
                    ]]

                    ,done: function (res, curr, count) {
                        //如果是异步请求数据方式，res即为你接口返回的信息。
                        //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                        console.log(res);

                        //得到当前页码
                        //console.log(curr);

                        //得到数据总量
                        //console.log(count);
                        console.log("当前表格最新数量为：" + count);
                    }
                });

            });
        }

        function showDriverSelect(expressCode){
            $.post('/waybill/send_nextStation/getDriver.html', {expressCode: expressCode}, function(data){

                if (data.result) {
                    $("#sendDriver").empty();
                    $("#sendDriver").prepend("<option value='0'>choose or search....</option>");
                    var driver = data.data;
                    console.log(data.data.length);
                    for (var i = 0; i < driver.length; i++) {
                        //$("#sendDriver").append("<option value='" + driver[i].code + "'>" + driver[i].name + "</option>");
                        $("#sendDriver").append("<option value='" + driver[i].name + "'>" + driver[i].name + "</option>");
                    }
                    layui.use('form', function () {
                        form = layui.form;
                        form.render();
                    });
                }
            }, 'json');
        }

        /*
        //扫描包返回内容过滤
        function tableDateTrim(data){
            if(typeof(data.height)=='undefined') data.height='';
            if(typeof(data.height)=='undefined') data.height='';
            if(typeof(data.height)=='undefined') data.height='';
            if(typeof(data.height)=='undefined') data.height='';
        }
        */
    });



</script>
</body>
</html>