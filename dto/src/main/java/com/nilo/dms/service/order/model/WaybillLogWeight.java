package com.nilo.dms.service.order.model;

import java.io.Serializable;

import com.nilo.dms.common.BaseDo;

public class WaybillLogWeight extends BaseDo implements Serializable {

	private String orderNo;

	private Long merchantId;

	private String optBy;

	private Long optTime;

	private Integer networkId;

	private Double oldWeight;
	private Double oldHigh;
	private Double oldWidth;
	private Double oldLength;
	private Double weight;
	private Double high;
	private Double width;
	private Double length;

	public Double getOldWeight() {
		return oldWeight;
	}

	public void setOldWeight(Double oldWeight) {
		this.oldWeight = oldWeight;
	}

	public Double getOldHigh() {
		return oldHigh;
	}

	public void setOldHigh(Double oldHigh) {
		this.oldHigh = oldHigh;
	}

	public Double getOldWidth() {
		return oldWidth;
	}

	public void setOldWidth(Double oldWidth) {
		this.oldWidth = oldWidth;
	}

	public Double getOldLength() {
		return oldLength;
	}

	public void setOldLength(Double oldLength) {
		this.oldLength = oldLength;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getOptBy() {
		return optBy;
	}

	public void setOptBy(String optBy) {
		this.optBy = optBy;
	}

	public Long getOptTime() {
		return optTime;
	}

	public void setOptTime(Long optTime) {
		this.optTime = optTime;
	}

	public Integer getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

}
