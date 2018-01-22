<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
<%@ include file="../../../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<style>
    .fly-msg, .fly-error {
        padding: 10px 15px;
        line-height: 24px;
    }

    .fly-msg {
        background-color: #F8F8F8;
        color: #666;
    }

    .fly-msg .show-blue {
        color: #4F99CF;
        padding: 10px 10px
    }

    .app-bind li {
        margin-bottom: 10px;
        line-height: 30px;
        color: #999;
    }

    .app-bind li .iconfont {
        position: relative;
        top: 3px;
        margin-right: 5px;
        font-size: 28px;
    }

    .app-bind .app-havebind {
        color: #333;
    }

    .app-bind .app-havebind .icon-qq {
        color: #7CA9C9
    }

    .app-bind .app-havebind .icon-weibo {
        color: #E6162D
    }

</style>
<body>
<div>
    <div class="layui-form layui-row" style="padding-top: 10px;padding-left: 10px">
        <div class="layui-col-md3 left-align">
            <div class="layui-inline">
                <input class="layui-input" name="orderNo" autocomplete="off">
            </div>
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    <hr>
    <div style="padding: 5px">
        <ul class="flow-default app-bind" id="orderListFlow"></ul>
    </div>
</div>

<%@ include file="../../../common/footer.jsp" %>
<script type="text/javascript">


    function formatDispatchTaskStatus(status) {
        if (status == 10) {
            return "Created";
        }
        if (status == 20) {
            return "Process";
        }
        if (status == 30) {
            return "<Font color='1E9FFF'>Complete</Font>";
        }
    }

    $(function () {

        var flow;
        layui.use('flow', function () {
            flow = layui.flow;
            flow.load({
                elem: '#orderListFlow' //指定列表容器
                , done: function (page, next) { //到达临界点（默认滚动触发），触发下一页

                    var lis = [];
                    //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
                    $.get('/task/dispatch/list.html?page=' + page, '', function (res) {
                        //假设你的列表返回在data集合中
                        layui.each(res.data, function (index, item) {
                            lis.push('<li class="fly-msg">' +
                                    '' + item.deliveryOrder.orderType + '<i class="show-blue">' + item.deliveryOrder.statusDesc + '</i>' + item.orderNo  +
                                    '<div style="float:right;display: inline"><button class="layui-btn layui-btn-normal layui-btn-mini" onclick="receive()">Receive</button></div></br>' +
                                    'Name:' + '<i class="show-blue">' + item.deliveryOrder.receiverInfo.name + '</i> Phone:<i class="show-blue">' + item.deliveryOrder.receiverInfo.contactNumber + '</i>' +
                                    '<div style="float:right;display: inline"><button class="layui-btn layui-btn-danger layui-btn-mini" style="align:right" onclick="abnormal()">Abnormal</button></div></li>');
                        });
                        //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
                        next(lis.join(''), page < res.pages);
                    }, 'json');
                }
            });
        });


        function toTransferPage(taskId) {
            $.ajax({
                url: "/task/dispatch/transferPage.html",
                type: 'POST',
                data: {"taskId": taskId},
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                        type: 1,
                        content: data,
                        area: ['700px', '500px'],
                        maxmin: true
                    });
                    layer.full(index);
                }
            });
        }

        function toDetails(taskId, orderNo) {
            $.ajax({
                url: "/task/dispatch/detailsPage.html",
                type: 'GET',
                data: {"taskId": taskId, orderNo: orderNo},
                dataType: 'text',
                success: function (data) {
                    //弹出即全屏
                    var index = layer.open({
                        type: 1,
                        content: data,
                        area: ['800px', '600px'],
                        maxmin: true
                    });
                    layer.full(index);

                }
            });
        }

    });


    function receive(taskId, orderNo, referenceNo) {
        $.ajax({
            url: "/task/dispatch/receivePage.html",
            type: 'GET',
            data: {"taskId": taskId, orderNo: orderNo, referenceNo: referenceNo},
            dataType: 'text',
            success: function (data) {
                var index = layer.open({
                    type: 1,
                    content: data,
                    area: ['700px', '500px'],
                    maxmin: true
                });
                layer.full(index);
            }
        });
    }

    function abnormal(taskId, orderNo, referenceNo) {
        $.ajax({
            url: "/task/dispatch/abnormalPage.html",
            type: 'GET',
            data: {"taskId": taskId, orderNo: orderNo, referenceNo: referenceNo},
            dataType: 'text',
            success: function (data) {
                //弹出即全屏
                var index = layer.open({
                    type: 1,
                    content: data,
                    area: ['700px', '500px'],
                    maxmin: true
                });
                layer.full(index);
            }
        });
    }
</script>
</body>
</html>