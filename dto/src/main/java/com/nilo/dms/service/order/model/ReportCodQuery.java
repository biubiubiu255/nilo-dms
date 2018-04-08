package com.nilo.dms.service.order.model;



/**
 * Created by wenzhuo-company on 2018/3/21.
 */
public class ReportCodQuery {

    private Long merchantId;
    @ReportColumnName(alias = "CustomerType")
    private String orderOriginal;
    private String country;
    @ReportColumnName(alias = "OrderNo")
    private String order_no;
    @ReportColumnName(alias = "OrderType")
    private String order_type;
    @ReportColumnName(alias = "CustomerOrder")
    private String reference_no;
    @ReportColumnName(alias = "ServiceType")
    private String serice_type;
    @ReportColumnName(alias = "SendCompany")
    private String  send_company;
    @ReportColumnName(alias = "Rider")
    private String  rider;
    @ReportColumnName(alias = "OrderStatus")
    private Integer order_status;
    private Integer payStatus;
    @ReportColumnName(alias = "ArrayTime")
    private Integer arrive_time_start;
    @ReportColumnName(alias = "ArrayTime")
    private Integer arrive_time_end;
    @ReportColumnName(alias = "SendTime")
    private Integer send_time_start;
    @ReportColumnName(alias = "SendTime")
    private Integer send_time_end;
    @ReportColumnName(alias = "DeliveryTime")
    private Integer delivery_time_start;
    @ReportColumnName(alias = "DeliveryTime")
    private Integer delivery_time_end;
    @ReportColumnName(alias = "SignTime")
    private Integer sign_name_start;
    @ReportColumnName(alias = "SignTime")
    private Integer sign_name_end;
    @ReportColumnName(alias = "OutWarmDay")
    private Integer out_warm;
    @ReportColumnName(alias = "PayBack Date")
    private Integer payBack_time_start;
    @ReportColumnName(alias = "PayBack Date")
    private Integer payBack_time_end;
    private String  columnStr;

    protected Integer offset;
    protected Integer limit;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
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

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getSerice_type() {
        return serice_type;
    }

    public void setSerice_type(String serice_type) {
        this.serice_type = serice_type;
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

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getArrive_time_start() {
        return arrive_time_start;
    }

    public void setArrive_time_start(Integer arrive_time_start) {
        this.arrive_time_start = arrive_time_start;
    }

    public Integer getArrive_time_end() {
        return arrive_time_end;
    }

    public void setArrive_time_end(Integer arrive_time_end) {
        this.arrive_time_end = arrive_time_end;
    }

    public Integer getSend_time_start() {
        return send_time_start;
    }

    public void setSend_time_start(Integer send_time_start) {
        this.send_time_start = send_time_start;
    }

    public Integer getSend_time_end() {
        return send_time_end;
    }

    public void setSend_time_end(Integer send_time_end) {
        this.send_time_end = send_time_end;
    }

    public Integer getDelivery_time_start() {
        return delivery_time_start;
    }

    public void setDelivery_time_start(Integer delivery_time_start) {
        this.delivery_time_start = delivery_time_start;
    }

    public Integer getDelivery_time_end() {
        return delivery_time_end;
    }

    public void setDelivery_time_end(Integer delivery_time_end) {
        this.delivery_time_end = delivery_time_end;
    }

    public Integer getSign_name_start() {
        return sign_name_start;
    }

    public void setSign_name_start(Integer sign_name_start) {
        this.sign_name_start = sign_name_start;
    }

    public Integer getSign_name_end() {
        return sign_name_end;
    }

    public void setSign_name_end(Integer sign_name_end) {
        this.sign_name_end = sign_name_end;
    }

    public Integer getOut_warm() {
        return out_warm;
    }

    public void setOut_warm(Integer out_warm) {
        this.out_warm = out_warm;
    }

    public Integer getPayBack_time_start() {
        return payBack_time_start;
    }

    public void setPayBack_time_start(Integer payBack_time_start) {
        this.payBack_time_start = payBack_time_start;
    }

    public Integer getPayBack_time_end() {
        return payBack_time_end;
    }

    public void setPayBack_time_end(Integer payBack_time_end) {
        this.payBack_time_end = payBack_time_end;
    }

    public String getColumnStr() {
        return columnStr;
    }

    public void setColumnStr(String columnStr) {
        this.columnStr = columnStr;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "ReportCodQuery{" +
                "merchantId=" + merchantId +
                ", orderOriginal='" + orderOriginal + '\'' +
                ", country='" + country + '\'' +
                ", order_no='" + order_no + '\'' +
                ", order_type='" + order_type + '\'' +
                ", reference_no='" + reference_no + '\'' +
                ", serice_type='" + serice_type + '\'' +
                ", send_company='" + send_company + '\'' +
                ", rider='" + rider + '\'' +
                ", order_status=" + order_status +
                ", payStatus=" + payStatus +
                ", arrive_time_start=" + arrive_time_start +
                ", arrive_time_end=" + arrive_time_end +
                ", send_time_start=" + send_time_start +
                ", send_time_end=" + send_time_end +
                ", delivery_time_start=" + delivery_time_start +
                ", delivery_time_end=" + delivery_time_end +
                ", sign_name_start=" + sign_name_start +
                ", sign_name_end=" + sign_name_end +
                ", out_warm=" + out_warm +
                ", payBack_time_start=" + payBack_time_start +
                ", payBack_time_end=" + payBack_time_end +
                ", columnStr='" + columnStr + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
