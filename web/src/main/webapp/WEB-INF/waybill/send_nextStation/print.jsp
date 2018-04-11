<%@ page import="com.nilo.dms.common.utils.DateUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<%
    request.setAttribute("nowDate", DateUtil.formatCurrent("yyyy-MM-dd HH:mm:ss"));
%>
<%@ include file="../../common/header.jsp" %>
<script type="text/javascript">window.print();</script>
<style>
    *{
        font-size: large;
    }
    html {
        -webkit-text-size-adjust: none;
    }

    td {
        padding: 5px;
    }
    tr {
        height: 200%;
    }
</style>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
<div class="container">
    <div class="row">
        <div class="center-block" style="width:400px;">

            <h4>Shipment List</h4>

            <h5 style="position:absolute;right: 10px;top: 20px;"> Date ${nowDate}</h5>
        </div>

        <br><br>
    </div>


    <div class="row">
        <div class="col-xs-3">
            HandleNo NO: ${pack.handleNo}
        </div>
        <div class="col-xs-3">
            handleByName: ${pack.handleBy}
        </div>
        <div class="col-xs-3">
            Rider :${pack.rider}
        </div>
        <br><br>
    </div>
    <br><br>
    <div class="row">
        <div class="col-xs-12">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Order No</th>
                    <th>Weight</th>
                    <th>Length</th>
                    <th>Hight</th>
                    <th>Width</th>
                    <th>CreateTime</th>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${smalls}" var="item">
	                   <tr>
	                        <td>${item.orderNo}</td>
	                        <td>${item.weight}</td>
	                        <td>${item.length}</td>
	                        <td>${item.height}</td>
	                        <td>${item.width}</td>
                            <td><date:date value="${item.created_time}" parttern="yyyy-MM-dd HH:mm:ss"></date:date></td>
                       </tr>
                   </c:forEach> 
                </tbody>
            </table>
        </div>
        <br><br>

    </div>
    <div class="row">
        <div class="col-xs-6">
            Total Order :${smalls.size()}
        </div>
        <br><br>
    </div>
    
<br><br>
        <div class="col-xs-12" style="float: right" >
            Rider's Name:____________________________
        </div>
        <br><br>
    </div>
    
</html>