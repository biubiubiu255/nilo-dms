<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<div class="layui-row">
    <div class="layui-col-md12 content detail">
        <div class="fly-panel detail-box">
            <form id="myForm" class="layui-form" action="">
                <div class="layui-form-item" >
                    <textarea class="layui-textarea" id="edit_company" name="info" lay-verify="info" ></textarea>
                </div>
                <div class="layui-form-item">
                    <button class="layui-btn edit-company">Edit</button>
                </div>
            </form>


        </div>
    </div>
</div>
<script type="text/javascript">

    $(function () {
        layui.use('form', function () {
            var form = layui.form;
        });
        layui.use('layedit', function(){
            var layedit = layui.layedit;
            layedit.set({
                uploadImage: {
                    url: '' //接口url
                    ,type: '' //默认post
                }
            });
            layedit.build('edit_company'); //建立编辑器
        });
        $(".edit-company").on('click', editCompany);
        function editCompany() {
            $.ajax({
                url: "/company/editPage.html",
                type: 'GET',
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
</script>
</body>
</html>