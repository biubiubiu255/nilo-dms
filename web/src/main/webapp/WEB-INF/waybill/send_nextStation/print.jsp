<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="lp" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    request.setAttribute("nowDate", DateUtil.formatCurrent("yyyy-MM-dd HH:mm"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge;chrome=1">
    <title>print</title>
    <script src="${ctx}/plugins/jQuery/jquery-2.2.3.min.js"></script>
</head>

<body>
<script type="text/javascript">window.print();</script>
<style>

    .printer {
        padding: 0;
    }

    .printer li {
        border-bottom: 1px dashed #ddd;
        padding-bottom: 1em;
    }

    .printer li.r3 {
        border-bottom: none;
    }

    .lister tr td, .lister tr th {
        border: 1px solid #333;
        padding: 0 2px;
        color: #000;
    }

    .lister tr.title td {
        font-size: 18px;
        border: none;
    }

    .lister tr.total th {
        font-size: 14px;
    }

    .lister tr.total td {
        padding-right: 12px;
    }

    .lister tr th span.grey {
        font-weight: lighter;
    }

    .intro td {
        font-size: 15px;
        font-weight: bold;
        text-align: center;
        color: #000;
    }

    .intro2 td {
        font-size: 13px;
        text-align: left;
        color: #000;
    }

    .intro2 {
        margin: 5px 0;
    }
</style>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
<div class="container">
    <div class="row" style="text-align: center;">
        <div class="center-block" style="width:600px;">

            <h4><b><span style="font-weight: bolder; font-size: larger;">Shipment List</span></b></h4>

            <h5 style="position:absolute;right: 10px;top: 20px;"> Date ${nowDate}</h5>
        </div>
    </div>
</br>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="intro2">
                <tbody>
                <tr style="background: rgb(255, 255, 255);">
                    <td style="font-size:14px">Loading NO: ${pack.handleNo}
                    </td>
                    <td style="font-size:14px">Name: ${pack.thirdExpressCode}
                    </td>
                    <td style="font-size:14px">Rider: ${pack.driver}
                    </td>
                    <td style="font-size:14px">Operator: ${sessionScope.userName}
                    </td>
                </tr>
                
                </tbody>
                </table>
 
      </br>  
            <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="lister">
                 <tbody>
                <tr>
                    <th>No</th>
                    <th>Order No</th>
                    <th>Weight</th>
                    <th>Customer Name</th>
                    <th>Contact No</th>
                    <th>Address</th>
                </tr>
                
               
                	<c:forEach items="${smalls}" var="item" varStatus="status">
	                   <tr  style="background: rgb(255, 255, 255);">
	                   <th><strong>${status.index + 1}</strong></th>
                            
	                        <td>${item.orderNo}</td>
                        <th><strong>${item.weight}</strong></th>
	                        <td>${item.receiverInfo.receiverName}</td>
	                        <td>${item.receiverInfo.receiverPhone}</td>
	                        <td>${item.receiverInfo.receiverAddress}</td>
                       </tr>
                   </c:forEach> 
                </tbody>
            </table>
            
           
        </div>

    <div class="row">
        <div class="col-xs-6">
            Total Order :${fn:length(smalls)}
        </div>

        <br><br>
    </div>
    
<br><br>
        <div class="col-xs-12" style="float: right" >
            Rider's Name:____________________________
        </div>
        <br><br>
    </div>
    </body>
</html>