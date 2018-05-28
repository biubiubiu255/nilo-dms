package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by ronny on 2017/8/30.
 */
public class LoadingDetailsDO extends BaseDo<Long> {

    private String loadingNo;

    private String orderNo;

    private Long loadingBy;

    private Integer status;

    public String getLoadingNo() {
        return loadingNo;
    }

    public void setLoadingNo(String loadingNo) {
        this.loadingNo = loadingNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getLoadingBy() {
        return loadingBy;
    }

    public void setLoadingBy(Long loadingBy) {
        this.loadingBy = loadingBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
