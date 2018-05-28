/*
 * Kilimall Inc.
 * Copyright (c) 2016. All Rights Reserved.
 */

package com.nilo.dms.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 身份信息
 *
 * @version 1.0
 */
public class Principal implements Serializable {

    private static final long serialVersionUID = 5798882004228239559L;

    private Date loginTime;

    private String userName;

    private String merchantId;

    private String userId;

    private String job;

    private String companyId;

    private String departmentId;

    private String companyName;

    private String departmentName;

    private boolean isRider;

    private List<Integer> networks;
    private List<String> authorities;
    private List<String> roles;
    private List<String> urlAuthorities;

    public boolean isRider() {
        return isRider;
    }

    public void setRider(boolean rider) {
        isRider = rider;
    }

    public List<String> getUrlAuthorities() {
        return urlAuthorities;
    }

    public void setUrlAuthorities(List<String> urlAuthorities) {
        this.urlAuthorities = urlAuthorities;
    }

    public List<Integer> getNetworks() {
        return networks;
    }

    public String getFirstNetwork() {
        return "" + networks.get(0);
    }

    public void setNetworks(List<Integer> networks) {
        this.networks = networks;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }


    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public String toString() {
        return String.format("User=%s loginAt=%s", userName, loginTime);
    }

}