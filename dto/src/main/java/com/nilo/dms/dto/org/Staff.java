package com.nilo.dms.dto.org;

import com.nilo.dms.common.enums.StaffStatusEnum;
import com.nilo.dms.common.utils.AgeUtil;

/**
 * Created by admin on 2017/9/29.
 */
public class Staff {

    private String merchantId;

    private String userId;

    private String staffId;

    private String companyId;

    private String departmentId;

    private String departmentName;

    private String realName;

    private String nickName;

    private String job;

    private String jobDesc;

    private String title;

    private Long titleTime;

    private String titleLevel;

    private String birthday;

    private String idCard;

    private StaffStatusEnum status;

    private Integer sex;

    private String phone;

    private String email;

    private Long employTime;

    private Long regularTime;

    private Long resignedTime;

    private String address;

    private Boolean isRider;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getIsRiderCode() {
        return isRider == true ? 1 : 0;
    }
    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public void setRider(Boolean rider) {
        isRider = rider;
    }

    public Boolean isRider() {
        return isRider;
    }

    public void setIsRider(Boolean rider) {
        isRider = rider;
    }

    public Long getRegularTime() {
        return regularTime;
    }

    public void setRegularTime(Long regularTime) {
        this.regularTime = regularTime;
    }

    public Long getResignedTime() {
        return resignedTime;
    }

    public void setResignedTime(Long resignedTime) {
        this.resignedTime = resignedTime;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTitleTime() {
        return titleTime;
    }

    public void setTitleTime(Long titleTime) {
        this.titleTime = titleTime;
    }

    public String getTitleLevel() {
        return titleLevel;
    }

    public void setTitleLevel(String titleLevel) {
        this.titleLevel = titleLevel;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public StaffStatusEnum getStatus() {
        return status;
    }

    public void setStatus(StaffStatusEnum status) {
        this.status = status;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getEmployTime() {
        return employTime;
    }

    public void setEmployTime(Long employTime) {
        this.employTime = employTime;
    }

    public int getAge() {
        return AgeUtil.getAge(this.birthday);
    }

    public String getStatusDesc() {
        return this.status.getDesc();
    }

    public Integer getStatusCode() {
        return this.status.getCode();
    }
}
