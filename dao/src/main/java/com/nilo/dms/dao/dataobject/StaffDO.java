package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

import java.util.List;

/**
 * Created by admin on 2017/9/29.
 */
public class StaffDO extends BaseDo<Long> {

    private String staffId;

    private Long userId;

    private Long merchantId;

    private Long companyId;

    private String departmentId;

    private String realName;

    private String nickName;

    private String job;

    private String title;

    private Long titleTime;

    private String titleLevel;

    private String birthday;

    private String idCard;

    private Integer status;

    private Integer sex;

    private String phone;

    private String email;

    private String address;

    private Integer isRider;

    private Long employTime;

    private Long regularTime;

    private Long resignedTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsRider() {
        return isRider;
    }

    public void setIsRider(Integer isRider) {
        this.isRider = isRider;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
}
