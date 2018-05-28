package com.nilo.dms.dto.order;

import com.nilo.dms.common.utils.StringUtil;

/**
 * Created by admin on 2017/11/6.
 */
public class NotifyResponse {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return StringUtil.equals(this.status, "succ") ? true : false;
    }

}
