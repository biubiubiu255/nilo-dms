package com.nilo.dms.service.model.pda;

import com.nilo.dms.common.enums.UserStatusEnum;
import com.nilo.dms.common.enums.UserTypeEnum;

import java.io.Serializable;

/**
 * 用户
 */
public class PdaWaybill implements Serializable {

	private static final long serialVersionUID = 4186833653531777192L;
	private String waybillNo; // 运单号

	private String receiverCountry;
	private String receiverProvince;
	private String networkName;
	private String weight;

	private String width;
	private String length;

	private String isCod;
	private String statusDes;

	private String goodsTypeDes;

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getReceiverCountry() {
		return receiverCountry;
	}

	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}

	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getIsCod() {
		return isCod;
	}

	public void setIsCod(String isCod) {
		this.isCod = isCod;
	}

	public String getStatusDes() {
		return statusDes;
	}

	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
	}

	public String getGoodsTypeDes() {
		return goodsTypeDes;
	}

	public void setGoodsTypeDes(String goodsTypeDes) {
		this.goodsTypeDes = goodsTypeDes;
	}

	
}
