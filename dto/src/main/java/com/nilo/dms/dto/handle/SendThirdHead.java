package com.nilo.dms.dto.handle;

import com.nilo.dms.common.BaseDo;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
public class SendThirdHead extends BaseDo<Long> {

    private Long merchantId;
    private String handleNo;
    private String type;                //区分是打过包的类型、还是普通包    package or waybill
    private String thirdExpressCode;
    private String thirdWaybillNo;
    private String driver;
    private String networkCode;
    private String  nextStation;
    private Integer handleTime;
    private Long handleBy;
    private String handleName;
    private Integer status;
    private String remark;

    private List<SendThirdDetail> list ;

    public List<SendThirdDetail> getList() {
        return list;
    }

    public void setList(List<SendThirdDetail> list) {
        this.list = list;
    }

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

    public String getThirdExpressCode() {
        return thirdExpressCode;
    }

    public void setThirdExpressCode(String thirdExpressCode) {
        this.thirdExpressCode = thirdExpressCode;
    }

    public String getThirdWaybillNo() {
        return thirdWaybillNo;
    }

    public void setThirdWaybillNo(String thirdWaybillNo) {
        this.thirdWaybillNo = thirdWaybillNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getNetworkCode() {
        return networkCode;
    }

    public void setNetworkCode(String networkCode) {
        this.networkCode = networkCode;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public Integer getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Integer handleTime) {
        this.handleTime = handleTime;
    }

    public Long getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(Long handleBy) {
        this.handleBy = handleBy;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SendThirdHead{" +
                "merchantId=" + merchantId +
                ", handleNo='" + handleNo + '\'' +
                ", type='" + type + '\'' +
                ", thirdExpressCode='" + thirdExpressCode + '\'' +
                ", thirdWaybillNo='" + thirdWaybillNo + '\'' +
                ", driver='" + driver + '\'' +
                ", networkCode='" + networkCode + '\'' +
                ", nextStation='" + nextStation + '\'' +
                ", handleTime=" + handleTime +
                ", handleBy=" + handleBy +
                ", handleName='" + handleName + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", list=" + list +
                '}';
    }
}
