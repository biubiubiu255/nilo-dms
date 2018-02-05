<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.service.system.SystemCodeUtil" %>

<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">
<!-- /.box-header -->
<div class="layui-row">

    <div class="layui-col-md5">
        <shiro:hasPermission name="400071">
            <button class="layui-btn layui-btn-normal loading-scan">Package Scan</button>
        </shiro:hasPermission>
        <button class="layui-btn btn-search">Search</button>
    </div>
    
 </div>
    <div class="layui-collapse" >
	<div class="layui-colla-item">
    <div class="layui-colla-content ">
    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            PackageNo:
            <div class="layui-inline">
                <input class="layui-input" name="packageNo" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md1">
            <button class="layui-btn layui-btn-normal search">Search</button>
        </div>
    </div>
    </div>
    </div>
    </div>
    

    <table class="layui-table" lay-data="{ url:'/order/package/list.html', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'orderNo', width:200}">PackageNo</th>
            <th lay-data="{field:'statusDesc', width:150}">Status</th>
            <th lay-data="{field:'nextNetworkId', width:150}">NextStation</th>
            <th lay-data="{field:'weight', width:120}">Weight</th>
            <th lay-data="{field:'length', width:120}">Length</th>
            <th lay-data="{field:'width', width:120}">Width</th>
            <th lay-data="{field:'high', width:120}">High</th>
            <th lay-data="{field:'remark', width:120}">Remark</th>
        </tr>
        </thead>
    </table>


</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use(['form', 'layer'], function () {
            var form = layui.form;
            form.render();
        })
        layui.use(['element'], function () {
        });
        layui.use('laydate', function () {
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
                    packageNo: $("input[name='packageNo']").val(),
                }
            });
        };

        $(".loading-scan").on("click", function () {
            toLoadingScanPage("");
        })

        function toLoadingScanPage() {

            var url = "/order/package/packagePage.html";
            parent.addTabs({
                id: '40008001',
                title: 'Package',
                close: true,
                url: url
            });
        }

    });

</script>
</body>
</html>