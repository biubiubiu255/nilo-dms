package com.nilo.dms.dto.org;

import com.nilo.dms.common.enums.DepartmentStatusEnum;

/**
 * Created by admin on 2017/9/29.
 */
public class Department {

    private String companyId;

    private String departmentId;

    private String departmentName;

    private String desc;

    private String type;

    private Department parentDepartment;

    private String leaderId;

    private DepartmentStatusEnum status;

    public DepartmentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DepartmentStatusEnum status) {
        this.status = status;
    }

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }
}
