package com.nilo.dms.service.order.model;

public class PaymentResult {

	private String merchantId;// string Y The unique id of merchant in Lipapay.
	private String merchantOrderNo;// string Y The merchant platform order number
	private String orderId;// string Y The lipaPay order id
	private String status;// enum Y The status enum={SUCCESS,UNKNOWN}
	private String signType;// enum Y The signature algorithm enum={MD5}
	private String sign;// string Y See "Signature Rule"

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
