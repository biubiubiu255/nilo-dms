package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/12/13.
 */
public class WaybillScanDO extends BaseDo<Long> {

    private Long merchantId;

    private String scanNo;

    private String scanBy;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getScanNo() {
        return scanNo;
    }

    public void setScanNo(String scanNo) {
        this.scanNo = scanNo;
    }

    public String getScanBy() {
        return scanBy;
    }

    public void setScanBy(String scanBy) {
        this.scanBy = scanBy;
    }
}
