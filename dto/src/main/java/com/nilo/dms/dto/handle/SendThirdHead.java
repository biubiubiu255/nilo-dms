package com.nilo.dms.dto.handle;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/9/19.
 */
public class SendThirdHead extends BaseDo<Long> {

    private Long merchantId;
    private String handleNo;
    private String third_express_code;
    private String third_waybill_no;
    private String driver;
    private Integer network_id;
    private String  nextStation;
    private Integer handle_time;
    private Long handleBy;
    private String handleByName;
    private Integer status;
    private String remark;
    private Integer created_time;
    private Integer updated_time;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getHandleNo() {
        return handleNo;
    }

    public void setHandleNo(String handleNo) {
        this.handleNo = handleNo;
    }

    public String getThird_express_code() {
        return third_express_code;
    }

    public void setThird_express_code(String third_express_code) {
        this.third_express_code = third_express_code;
    }

    public String getThird_waybill_no() {
        return third_waybill_no;
    }

    public void setThird_waybill_no(String third_waybill_no) {
        this.third_waybill_no = third_waybill_no;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Integer getNetwork_id() {
        return network_id;
    }

    public void setNetwork_id(Integer network_id) {
        this.network_id = network_id;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public Integer getHandle_time() {
        return handle_time;
    }

    public void setHandle_time(Integer handle_time) {
        this.handle_time = handle_time;
    }

    public Long getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(Long handleBy) {
        this.handleBy = handleBy;
    }

    public String getHandleByName() {
        return handleByName;
    }

    public void setHandleByName(String handleByName) {
        this.handleByName = handleByName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Integer created_time) {
        this.created_time = created_time;
    }

    public Integer getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Integer updated_time) {
        this.updated_time = updated_time;
    }

    @Override
    public String toString() {
        return "SendThirdHead{" +
                "merchantId=" + merchantId +
                ", handleNo='" + handleNo + '\'' +
                ", third_express_code='" + third_express_code + '\'' +
                ", third_waybill_no='" + third_waybill_no + '\'' +
                ", driver='" + driver + '\'' +
                ", network_id=" + network_id +
                ", nextStation='" + nextStation + '\'' +
                ", handle_time=" + handle_time +
                ", handleBy=" + handleBy +
                ", handleByName='" + handleByName + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", created_time=" + created_time +
                ", updated_time=" + updated_time +
                '}';
    }
}
