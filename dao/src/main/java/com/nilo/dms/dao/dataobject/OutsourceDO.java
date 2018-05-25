package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by ronny on 2017/8/30.
 */
public class OutsourceDO extends BaseDo<Long> {

    private String outsource;

    private String outsourceName;

    private Long merchantId;

    public String getOutsource() {
        return outsource;
    }

    public void setOutsource(String outsource) {
        this.outsource = outsource;
    }

    public String getOutsourceName() {
        return outsourceName;
    }

    public void setOutsourceName(String outsourceName) {
        this.outsourceName = outsourceName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
}
