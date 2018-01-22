<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="false" %>

<body class="white-bg">
<div class="ibox-content">
    <form id="myForm" class="form-horizontal" autocomplete="off" data-validator-option="{theme:'default'}"
          style="margin: 1.5em">
        <input type="hidden" name="id" value="${log.id}">
        <div class="form-group">
            <label class="col-sm-2">
                <spring:message code="ui.operator"/>
            </label>
            <div class="col-sm-10">
                ${log.operator}
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2">
                <spring:message code="ui.operation"/>
            </label>
            <div class="col-sm-10">
                ${log.operation}
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2">
                <spring:message code="ui.parameters"/>
            </label>
            <div class="col-sm-10">
                ${log.parameter}
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2">
                <spring:message code="ui.ip_addr"/>
            </label>
            <div class="col-sm-10">
                ${log.ip}
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <div class="text-center">
                <button class="btn btn-primary" type="submit">
                    <spring:message code="ui.close"/>
                </button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $("#myForm").validator({
        valid: function (form) {
            window.parent.layer.close(window.parent.layer.index);
        }
    });
</script>