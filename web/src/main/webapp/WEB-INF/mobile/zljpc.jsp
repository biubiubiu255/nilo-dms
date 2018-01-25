<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
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
    <title></title>
    <link href="../mobile/css/ionic.css" rel="stylesheet" type="text/css"/>
    <link href="../mobile/css/mp.css" type="text/css" rel="stylesheet" />
    <link href="../mobile/css/mps.css" type="text/css" rel="stylesheet" />
    <script src="../mobile/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="../mobile/js/functions.js"></script>
</head>
<body>
<div class="wap_content">
    <div class="wap_top"><a href="/DemoController/toIndexPage.html" title="返回" class="wap_top_back"></a>
        <h2>Detained piece inventory</h2>
        <a href="index.php" title="审核全部" class="top_a_list_check"></a></div>
     <div class="search_banner">
        <div class="search_content" id="customers-search">
            <div class="search_input">
                <i></i>
                <input type="text" placeholder="Please enter the name key of the detained piece" searchParam="customers_name like '%{keywords}%'" 
					class="search_input_field keywords"/>
            </div>
            <div class="search_button"><input type="button" value="search" class="search_input_button submit"/></div>
        </div>
   </div>
    <div class="wap_tab_banner">
        <span class="trun_left"><i></i></span>
        <nav>
            <div class="wap_tab_content" data-role="scroller">
                <a title="6月19日" href="#">6月19日</a>
                <a title="6月19日" href="#">6月19日</a>
                <a title="6月19日" href="#">6月19日</a>
                <a title="6月19日" href="#">6月19日</a>
                <a title="6月19日" href="#">6月19日</a>
                <a title="6月19日" href="#">6月19日</a>
            </div>
        </nav>
        <span class="trun_right"><i></i></span>
    </div>

</div>
</body>
</html>
