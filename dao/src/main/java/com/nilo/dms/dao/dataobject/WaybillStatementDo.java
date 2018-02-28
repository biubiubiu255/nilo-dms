package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

public class WaybillStatementDo extends BaseDo<Long> {

    private String orderNo;
    private String order_type;
    private Double money;
    private Double statement_time;
    private String sign_time;
    private int status;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getStatement_time() {
        return statement_time;
    }

    public void setStatement_time(Double statement_time) {
        this.statement_time = statement_time;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "WaybillStatementDo{" +
                "orderNo='" + orderNo + '\'' +
                ", order_type='" + order_type + '\'' +
                ", money=" + money +
                ", statement_time=" + statement_time +
                ", sign_time='" + sign_time + '\'' +
                ", status=" + status +
                '}';
    }
}
