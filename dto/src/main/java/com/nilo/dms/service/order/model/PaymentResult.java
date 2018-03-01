package com.nilo.dms.service.order.model;

public class PaymentResult {

	private String amount;// string Y order amount
	private String merchantId;// string Y Merchant id in LipaPay
	private String merchantOrderNo;// string Y Merchant order No.
	private String orderId;// string Y Lipapay sn
	private String orgTransId;// string Y Transcation id from bank
	private String paymentChannel;// string Y Payment channel
	private String paymentMethod;// string Y Payment method
	private String sign;// string Y See "Signature Rule"
	private String signType;// string Y Signature algorithm
	private String status;// string Y The transaction status

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

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

	public String getOrgTransId() {
		return orgTransId;
	}

	public void setOrgTransId(String orgTransId) {
		this.orgTransId = orgTransId;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "amount=" + amount + "& merchantId=" + merchantId + "& merchantOrderNo=" + merchantOrderNo
				+ "& orderId=" + orderId + "& orgTransId=" + orgTransId + "& paymentChannel=" + paymentChannel
				+ "& paymentMethod=" + paymentMethod + "& signType=" + signType + "& status=" + status ;
	}
	

}
