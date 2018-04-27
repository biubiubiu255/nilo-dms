package com.nilo.dms.dao.dataobject.QO;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.dao.dataobject.ReportDispatchDO;

public class ReportDispatchQO extends ReportDispatchDO {

    private Integer offset;
    private Integer limit;
    private Integer total;
    private Integer fromCreatedTime;
    private Integer toCreatedTime;
    private Integer exportType;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFromCreatedTime() {
        return fromCreatedTime;
    }

    public void setFromCreatedTime(Integer fromCreatedTime) {
        this.fromCreatedTime = fromCreatedTime;
    }

    public Integer getToCreatedTime() {
        return toCreatedTime;
    }

    public void setToCreatedTime(Integer toCreatedTime) {
        this.toCreatedTime = toCreatedTime;
    }

    public Integer getExportType() {
        return exportType;
    }

    public void setExportType(Integer exportType) {
        this.exportType = exportType;
    }

    @Override
    public String toString() {
        return "ReportDispatchQO{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", total=" + total +
                ", fromCreatedTime=" + fromCreatedTime +
                ", toCreatedTime=" + toCreatedTime +
                ", exportType=" + exportType +
                '}';
    }
}
