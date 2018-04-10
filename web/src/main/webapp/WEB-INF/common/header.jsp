<%@ page import="com.nilo.dms.common.utils.WebUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="shiro" uri="/shiro.tag" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<%
    request.setAttribute("ctx", WebUtil.getContextPath());
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>
        KiliExpress-DMS
    </title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link href="${ctx}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="${ctx}/plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <!-- layUI -->
    <link rel="stylesheet" href="${ctx}/layui/css/layui.css" media="all">

    <link rel="stylesheet" href="${ctx}/layui/css/multselect.css" media="all">
    <!-- Theme style -->
    <link rel="stylesheet" href="${ctx}/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${ctx}/dist/css/skins/all-skins.min.css">
    <script src="${ctx}/public/js/jquery-2.2.3.min.js"></script>
    <script src="${ctx}/public/js/iwens.js"></script>


</head>
