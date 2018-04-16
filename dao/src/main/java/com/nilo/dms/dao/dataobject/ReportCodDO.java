package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import org.apache.commons.lang3.math.NumberUtils;

public class ReportCodDO {
    private Long merchantId;
    private String orderNo;
    private String orderOriginal;  //订单来源
    private String country;        //来源国家
    private String reference_no;   //客户订单号
    private String order_type;     //订单类型
    private String orderPlatform;  //客户类型
    private String serice_type;    //killmall服务类型
    private String pay_type;       //支付类型
    private String pay_method;     //支付方法
    private Double pay_price;      //COD价格
    private int create_time;       //创建时间
    private int arrive_time;       //收件时间
    private int sign_time;         //签收时间
    private int send_time;         //发送时间
    private int dispatcher_time;   //派送时间
    private int payBackDate;       //回款日期
    private String order_status;   //订单状态
    private String order_problem_status;   //订单状态
    private String send_company;   //派件公司
    private String rider;          //快递员
    private String cycle;          //COD汇款周期
    private String payStatus;      //支付状态
    private String pay_orderNo;    //支付订单号
    private String out_warm;       //超时预警
    private Double weight;         //货物重量
    private String  columnStr;     //返回当前查询的所有列
    private Integer delay;         //是否是滞留件

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderOriginal() {
        return orderOriginal;
    }

    public void setOrderOriginal(String orderOriginal) {
        this.orderOriginal = orderOriginal;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrderPlatform() {
        return orderPlatform;
    }

    public void setOrderPlatform(String orderPlatform) {
        this.orderPlatform = orderPlatform;
    }

    public String getSerice_type() {
        return serice_type;
    }

    public void setSerice_type(String serice_type) {
        this.serice_type = serice_type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public Double getPay_price() {
        return pay_price;
    }

    public void setPay_price(Double pay_price) {
        this.pay_price = pay_price;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(int arrive_time) {
        this.arrive_time = arrive_time;
    }

    public int getSign_time() {
        return sign_time;
    }

    public void setSign_time(int sign_time) {
        this.sign_time = sign_time;
    }

    public int getSend_time() {
        return send_time;
    }

    public void setSend_time(int send_time) {
        this.send_time = send_time;
    }

    public int getDispatcher_time() {
        return dispatcher_time;
    }

    public void setDispatcher_time(int dispatcher_time) {
        this.dispatcher_time = dispatcher_time;
    }

    public int getPayBackDate() {
        return payBackDate;
    }

    public void setPayBackDate(int payBackDate) {
        this.payBackDate = payBackDate;
    }

    public String getOrder_status() {
        return order_status;
    }
    public String getOrder_status_desc() {

        if (order_status!=null && !order_status.equals("") && NumberUtils.isNumber(order_status)){
            if (DeliveryOrderStatusEnum.getEnum(Integer.valueOf(order_status))!=null)
            {
                order_status = DeliveryOrderStatusEnum.getEnum(Integer.valueOf(order_status)).getDesc();
            }
        }
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_problem_status() {
        return order_problem_status;
    }

    public void setOrder_problem_status(String order_problem_status) {

        this.order_problem_status = order_problem_status;
    }

    public String getSend_company() {
        return send_company;
    }

    public void setSend_company(String send_company) {
        this.send_company = send_company;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPay_orderNo() {
        return pay_orderNo;
    }

    public void setPay_orderNo(String pay_orderNo) {
        this.pay_orderNo = pay_orderNo;
    }

    public String getOut_warm() {
        return out_warm;
    }

    public void setOut_warm(String out_warm) {
        this.out_warm = out_warm;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getColumnStr() {
        return columnStr;
    }

    public void setColumnStr(String columnStr) {
        this.columnStr = columnStr;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "ReportCodDO{" +
                "merchantId=" + merchantId +
                ", orderNo='" + orderNo + '\'' +
                ", orderOriginal='" + orderOriginal + '\'' +
                ", country='" + country + '\'' +
                ", reference_no='" + reference_no + '\'' +
                ", order_type='" + order_type + '\'' +
                ", orderPlatform='" + orderPlatform + '\'' +
                ", serice_type='" + serice_type + '\'' +
                ", pay_type='" + pay_type + '\'' +
                ", pay_method='" + pay_method + '\'' +
                ", pay_price=" + pay_price +
                ", create_time=" + create_time +
                ", arrive_time=" + arrive_time +
                ", sign_time=" + sign_time +
                ", send_time=" + send_time +
                ", dispatcher_time=" + dispatcher_time +
                ", payBackDate=" + payBackDate +
                ", order_status='" + order_status + '\'' +
                ", order_problem_status='" + order_problem_status + '\'' +
                ", send_company='" + send_company + '\'' +
                ", rider='" + rider + '\'' +
                ", cycle='" + cycle + '\'' +
                ", payStatus='" + payStatus + '\'' +
                ", pay_orderNo='" + pay_orderNo + '\'' +
                ", out_warm='" + out_warm + '\'' +
                ", weight=" + weight +
                ", columnStr='" + columnStr + '\'' +
                ", delay=" + delay +
                '}';
    }
}
