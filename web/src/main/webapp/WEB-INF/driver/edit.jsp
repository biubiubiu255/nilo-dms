<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<div class="box-body">
    <form id="myForm" class="layui-form" action="">
    
        <input type="hidden" name="id" value="${resultDate.id}">

     
        
        <div class="layui-form-item">
            <label class="layui-form-label">driverName</label>
            <div class="layui-input-block">
                <input type="text" name="driverName" value="${resultDate.driverName}" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">driverId</label>
            <div class="layui-input-block">
                <input type="text" name="driverId" value="${resultDate.driverId}" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">phone</label>
            <div class="layui-input-block">
                <input type="text" name="phone" value="${resultDate.phone}" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">expressName:</label>
            <div class="layui-input-block">
                <select name="thirdExpressCode" lay-search="">
                   <c:forEach var="values" items="${expressList}" varStatus="status">
	                   <c:choose>
						   <c:when test="${values.expressName == resultDate.expressName}">
						       <option value="${values.expressCode}" selected="selected">${values.expressName }</option>
						   </c:when>
						   <c:otherwise>
						       <option value="${values.expressCode}">${values.expressName }</option>
						   </c:otherwise>
					   </c:choose>
					   ${values.expressName}  -----  ${resultDate.expressName}
                   </c:forEach>

                </select></div>
        </div>
        
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="add-driver">Submit</button>
                <button type="reset" class="layui-btn layui-btn-primary reset">Reset</button>
            </div>
        </div>
        
    </form>
</div>

<script type="text/javascript">
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            form.render();
            //监听提交
            form.on('submit(add-driver)', function (data) {
                var load = layer.load(2);
                $.ajax({
                    url: "/admin/driver/edit.html",
                    data: $("#myForm").serialize(),
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS!', {icon: 1, time: 1000}, function () {
                                layer.closeAll();
                                //reloadCurrentPage();
                                //$(".layui-table").first().t.pt.render({});
                                //$(".layui-laypage-btn")[0].click();
/*                                 $(".search").on("click", function () {
					                reloadTable();
					            }) */
					            
                                table.layui-table.render();
                            });
                        } else {
                            //失败，提交表单成功后，释放hold，如果不释放hold，就变成了只能提交一次的表单
                            layer.msg(data.msg, {icon: 2, time: 2000});
                        }
                        //reloadCurrentPage();
                    },
                    complete: function () {
                        layer.close(load);
                    }
                });
                return false;
            });
        });
    });

</script>
