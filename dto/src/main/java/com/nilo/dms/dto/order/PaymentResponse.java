package com.nilo.dms.dto.order;

public class PaymentResponse {

	
	private String errorCode;// string Y 100=SUCCESS others=FAILURE
	private String merchantId;// string Y Merchant id in LipaPay
	private String merchantOrderNo;// string Y Merchant order No.
	private String orderId;// string Y Lipapay sn
	private String signType;// string Y Signature algorithm, e.g. MD5
	private String status;// string Y enum {SUCCESS, FAILURE}
	
	private String sign;// string Y See "Signature Rule"

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "errorCode=" + errorCode + "&merchantId=" + merchantId + "&merchantOrderNo="
				+ merchantOrderNo + "&orderId=" + orderId + "&signType=" + signType + "&status=" + status ;
	}

}
