<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <style>
            html,body{
                height:100%;
                width:100%;
                padding:0;
                margin:0;
            }
            #preview{
                width:100%;
                height:100%;
                padding:0;
                margin:0;
            }
            #preview *{font-family:sans-serif;font-size:16px;}
        </style>
        <script type="text/javascript" src="${ctx}/ueditor/dialogs/internal.js"></script>
        <script src="${ctx}/ueditor/ueditor.parse.js"></script>
        <title></title>
    </head>
    <body class="view">
        <div id="preview" style="margin:8px">
        </div>
    </body>
    <script>
        document.getElementById('preview').innerHTML = editor.getContent();
        uParse('#preview',{
            rootPath : '../../',
            chartContainerHeight:500
        })
        dialog.oncancel = function(){
            document.getElementById('preview').innerHTML = '';
        }
    </script>
</html>