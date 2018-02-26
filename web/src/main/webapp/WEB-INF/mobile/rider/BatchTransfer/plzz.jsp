<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta content="telephone=no" name="format-detection" />
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
    <meta name="keywords" content="#" />
    <meta name="description" content="#" />
    <!--<link rel="apple-touch-icon-precomposed" sizes="114x114" href="">
    <link rel="apple-touch-icon-precomposed" sizes="57x57" href="">-->
    <title></title>
    <link href="/mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
    <link href="/mobile/css/mps.css" type="text/css" rel="stylesheet" />
    <script src="/mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/mobile/js/functions.js"></script>

    <script>
        $(document).ready(function(){
            $("#all").click(function(){
                var o;
                if(this.checked){
                    o=document.getElementsByName("fuxuan");
                    for(var i=0;i<o.length;i++){
                        o[i].checked=event.srcElement.checked
                    };
                } else{
                    $("[name=fuxuan]").attr("checked",false);
                }
            });
            $("[name=fuxuan]").click(function(){
                var o=document.getElementsByName("fuxuan");
                for (var i = 0; i < o.length; i++) {
                    if (!o[i].checked) {
                        $("#all").prop("checked", false);
                        return;
                    }else{
                        $("#all").prop("checked", true);
                    }
                };
            });
        });

    </script>
</head>

<body>
<div class="wap_content">
   
    <div class="wap_top"><a href="javascript:history.go(-1)" title="返回" class="wap_top_back"></a>
        <h2>Batch transfer</h2>
    </div>

    <div class="formula_modify">
        <div class="banner_content">
        <ul class="ni">
            <li>运单号</li>
            <li>金额</li>
            <li>签收时间</li>
            <li>全选&nbsp;<input type="checkbox" class="fuxuank" id="all" ></li>
        </ul>
        <c:forEach items="${list}" var="m">
            <ul class="ni">
                <li>${m.orderNo}</li>
                <li>${m.needPayAmount}</li>
                <li>${m.handledTime}</li>
                <li><input type="checkbox" class="fuxuank" name="fuxuan" value="" ></li>
            </ul>
        </c:forEach>
            <div class="bottom_a_button"><a onclick="">submit</a></div>
        </div>

    </div>
    </div>
        <div class="bottom_a_button"><a href="/mobile/rider/Batch/Complete.html" class="submit" id="submit">submit</a></div>
    </div>
</body>
<html>
