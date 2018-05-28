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
    <%--第一排--%>
    <div class="layui-form layui-form-item">
        <div style="float: left; width: 39%; margin-left: 10px;">
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



        <label class="layui-form-label" style="width:150px">Quantity</label>
        <div class="layui-input-inline">
            <input type="text" name="quantity" value="" autocomplete="off"
                   class="layui-input layui-disabled"
                   disabled>
        </div>
    </div>

    <%--第二排--%>
    <div class="layui-form">
        <div class="layui-form-item">
            <div style="float: left; width: 39%; margin-left: 10px;">
                <label class="layui-form-label">Driver:</label>
                <div class="layui-form-item layui-inline" style="width: 190px">
                    <select lay-filter="fil-driver" name="expressLay" id="sendDriver">
                    </select>
                    <input type="hidden" name="driver" value="">
                </div>
            </div>

            <div style="float: left; width: 50%; margin-left: -3%;">
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width:150px; margin-left: 25px">Scan OrderNo:</label>
                    <div class="layui-input-inline">
                        <input type="text" id="orderNo" autocomplete="off" placeholder="Scan" class="layui-input">
                    </div>
                </div>
            </div>

        </div>
    </div>

    <hr>



    <div style="margin-left:120px;">
        <table id='${id0}' lay-filter="filterLab"></table>
    </div>



    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-danger layui-btn-mini btn-delete" lay-event="delete">Delete</a>
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

        var tableData = JSON.parse('${packList}');
        tableData = tableData.data;
        for(var i=0;i<tableData.length;i++){
            tableData[i].index = i+1;
        }
        view();

        layui.use('form', function () {
            form = layui.form;
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
                    for(var i=0;i<tableData.length;i++){
                        if (tableData.splice(i, 1));
                        break;
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
        function commit(isPrint) {
            var printed = false;  //是否需要打印
            var saveStatus = 0;   //传输到后台的状态，0仅保存、1发运
            isPrint = parseInt(isPrint);

            var express = $("input[name='express']").first().val();
            var driver  = $("input[name='driver']").first().val();
            if (express=="" || driver=="") {
                layer.msg("Please select option", {icon: 2, time: 2000});
                return ;
            }


            var smallPack = "";
            for(var i=0;i<tableData.length;i++){
                smallPack += tableData[i].orderNo+",";
            }
            smallPack = smallPack.substring(0, smallPack.length-1);
            var load = layer.load(2);
            $.ajax({
                type: "POST",
                url: "/waybill/send_nextStation/addLoading.html",
                dataType: "json",
                data: {
                    smallPack: smallPack,
                    saveStatus: isPrint,
                    //express : $("input[name='third_express_code']").val() ,
                    thirdExpressCode: $("input[name='express']").val() ,
                    driver: $("input[name='driver']").val()
                },
                success: function (data) {
                    if (data.result) {
                        layer.msg("SUCCESS", {icon: 1, time: 2000}, function () {
                            parent.layer.closeAll();
                            if(isPrint==1){
                                //这里需要返回一个大包号，才能打印，因为是在后台创建时生成订单号
                                parent.window.open("/waybill/rider_delivery/print.html?loadingNo=" + data.data.handleNo);
                                location.reload();
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

            $("input[name='quantity']").val(tableData.length);

            //console.log(tableData);
            layui.use('table', function(){
                var table = layui.table;
                console.log("条目：" + tableData.length);
                //第一个实例
                table.render({
                    elem : '#${id0}' ,
                    height: 400
                    //,even: true
                    //,url: '/demo/table/user/' //数据接口
                    ,data : tableData
                    ,page: false //开启分页
                    ,width: 550
                    ,even: true
                    ,text: {
                        none: 'Please start scanning the order'
                    }
                    /*,page : {
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
                    }*/
                    ,limit : 3000
                    //,limits : [5, 10, 15, 20, 25]
                    ,cellMinWidth: 80

                    ,cols: [[ //表头 //layui-btn layui-btn-primary layui-btn-mini
                        //{ title: 'id', align:'center', width:80, sort: true, fixed: 'left', templet: '<div>{{d.LAY_INDEX }}</div>'}
                        { title: 'id', align:'center', width:105, sort: true, fixed: 'left', templet: '<div>{{d.index }}</div>'}
                        ,{field: 'orderNo', title: 'OrderNo', width:275, align:'center'}
                        //,{field: 'weight', title: 'Weight(KG)', width:130, align:'center'}
                        ,{field: '', title: 'opt', width:177, toolbar: '#barDemo', fixed: 'right', align:'center'}
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

    });



</script>
</body>
</html>
