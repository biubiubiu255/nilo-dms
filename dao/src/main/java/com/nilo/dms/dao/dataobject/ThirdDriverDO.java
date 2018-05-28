package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by ronny on 2017/8/30.
 */
public class ThirdDriverDO extends BaseDo<Long> {

    private String thirdExpressCode;
    
    private String expressName;

    private String driverId;

    private String driverName;
    
    private String phone;
    
    //private String driverName;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getThirdExpressCode() {
        return thirdExpressCode;
    }

    public void setThirdExpressCode(String thirdExpressCode) {
        this.thirdExpressCode = thirdExpressCode;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
}
