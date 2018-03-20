package com.nilo.dms.service.model.pda;

import java.io.Serializable;

public class PdaRider  implements Serializable {

    private static final long serialVersionUID = 4186833653531777192L;
    private String staffId;
    private Long userId;
    private String realName;
    private String idandName;
    private Long merchantId;
    private String departmentId;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getIdandName() {
        return idandName;
    }

    public void setIdandName(String idandName) {
        this.idandName = idandName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "PdaRider{" +
                "staffId='" + staffId + '\'' +
                ", userId=" + userId +
                ", realName='" + realName + '\'' +
                ", merchantId=" + merchantId +
                ", departmentId='" + departmentId + '\'' +
                '}';
    }
}
