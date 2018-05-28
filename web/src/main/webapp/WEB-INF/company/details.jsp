<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>
<html>
<%@ include file="../common/header.jsp" %>
<style>
    .fly-panel {
        margin-bottom: 15px;
        border-radius: 2px;
        background-color: #fff;
        box-shadow: 0 1px 2px 0 rgba(0, 0, 0, .05);
    }

    .fly-panel[pad20] {
        padding: 20px;
    }

    .fly-panel-title {
        position: relative;
        height: 50px;
        line-height: 50px;
        padding: 0 15px;
        border-bottom: 1px dotted #E9E9E9;
        color: #333;
        border-radius: 2px 2px 0 0;
        font-size: 14px;
    }

    .fly-panel-main {
        padding: 10px 15px;
    }

    .detail-hits span {
        height: 20px;
        line-height: 20px;
    }

    .detail-hits .layui-btn {
        border-radius: 0;
    }

    .detail-hits .layui-btn + .layui-btn {
        margin-left: 5px;
    }

    .detail-hits .jie-admin {
        margin-right: 1px;
    }

    .detail-body {
        margin: 20px 0 0;
        min-height: 306px;
        line-height: 26px;
        font-size: 16px;
        color: #333;
        word-wrap: break-word;
    }

    .detail-body p {
        margin-bottom: 15px;
    }

    .detail-body a {
        color: #4f99cf;
    }

    .detail-body img {
        max-width: 100%;
        cursor: crosshair;
    }

    .detail-body table {
        margin: 10px 0 15px;
    }

    .detail-body table thead {
        background-color: #f2f2f2;
    }

    .detail-body table th,
    .detail-body table td {
        padding: 10px 20px;
        line-height: 22px;
        border: 1px solid #DFDFDF;
        font-size: 14px;
        font-weight: 400;
    }

    .detail .page-title {
        border: none;
        background-color: #f2f2f2;
    }

    pre {
        padding: 10px 15px;
        margin: 10px 0;
        font-size: 12px;
        border-left: 6px solid #009688;
        background-color: #f8f8f8;
        font-family: Courier New;
        overflow: auto;
    }

    body {
        background-color: #F2F2F2;
    }
    .detail-box{padding: 20px;}
</style>
<body>
    <div class="layui-row">
        <div class="layui-col-md12 content detail">
            <div class="fly-panel detail-box">

                <div class="detail-body photos">

                    <p>Company Type:${company.companyName}</p>
                    <p>Company Name:${company.companyName}</p>
                    <hr>
                    <p>Description:</p>
                    <pre>${company.description}</pre>
                </div>

                <div class="layui-form-item">
                    <button class="layui-btn edit-company">Edit</button>
                </div>
            </div>
        </div>
</div>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">

    $(function () {
        layui.use('form', function () {
            var form = layui.form;
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