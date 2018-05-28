package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * 用户信息
 */
public class UserInfoDO extends BaseDo<Long> {

    private Long merchantId;
    private String name;
    private String email;
    private String phone;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
