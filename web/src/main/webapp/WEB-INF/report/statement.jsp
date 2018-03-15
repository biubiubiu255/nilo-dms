<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
    request.setAttribute("nowDate", DateUtil.formatCurrent("yyyy-MM-dd"));
%>
<body>

<div class="box-body">
    <div class="layui-form layui-row">
        <div class="layui-col-md7 layui-col-lg5">
            <label class="layui-form-label">CreateTime:</label>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="fromTime" placeholder="From">
            </div>
            -
            <div class="layui-inline">
                <input type="text" class="layui-input" id="toTime" placeholder="To">
            </div>
            <shiro:hasPermission name="600031">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>

    </div>
    <hr>
</div>


    <iframe scrolling="no" frameborder="0" src="/report/statement/list.html" id="ifm" width="100%" height="100%" style="padding: 0px;"></iframe>


<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        layui.use('form',function(){
            var form = layui.form;
            form.render();
        })

        layui.use('laydate', function () {
            var layDate = layui.laydate;
            var d = new Date();
            var now = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
            layDate.render({
                elem: '#fromTime',
                lang: 'en',
            });
            layDate.render({
                elem: '#toTime',
                lang: 'en',
            });
        });
        layui.use('table', function () {
            var table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;
                if (obj.event === 'detail') {
                    var id = data.id;
                    layer.msg(id);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })

            var reloadTable = function (item) {
                var sTime = Date.parse(new Date($("#fromTime").val()))/1000;
                var eTime = Date.parse(new Date($("#toTime").val()))/1000;

                if (eTime<sTime){
                    layui.use('layer', function(){
                        var layer = layui.layer;
                        layer.alert('The start date must be less than the end date', {
                            icon: 0,
                            skin: 'layer-ext-moon'
                        })
                    });
                    return false;
                }
                //console.log(sTime.leng + "  " + eTime.len);

                if(String(sTime).length!=10 || String(eTime).length!=10){
                    layui.use('layer', function(){
                        var layer = layui.layer;
                        layer.alert('Please enter the date of the query', {
                            icon: 0,
                            skin: 'layer-ext-moon'
                        })
                    });
                    return false;
                }
                var param = {
                        rider :$("select[name='rider']").val(),
                        sTime: sTime,
                        eTime: eTime,
                    };
                var url = "/report/statement/list.html";
                param = jQuery.param( param )
                document.getElementById("ifm").src = url + "?" + param;
            };

        });
    });

</script>
</body>
</html>