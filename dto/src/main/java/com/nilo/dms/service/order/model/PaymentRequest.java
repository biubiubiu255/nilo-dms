package com.nilo.dms.service.order.model;

public class PaymentRequest {

	private String version;// string Y 1.4 Interface version,Not in the signature field
	private String merchantId;// string Y The unique id of merchant in Lipapay.
	private String signType;// string Y Sign type. (MD5 is only supported)
	private String sign;// string Y See "Signature Rule"
	private String notifyUrl;// string Y A URL of merchant system that can accepts notifications from LipaPay
								// system.
	private String returnUrl;// string Y Jump address of payment result,But paymentMetnod='OL' this field is
								// required
	private String merchantOrderNo;// string Y The merchant platform order Id,It must be unique. And the length
									// must less than 50.
	private float amount;// number Y The amount of transaction (unit cent).
	private String sellerId;// string N Seller User ID
	private String sellerAccount;// string N Seller User Name
	private String buyerId;// string N Buyer User ID
	private String buyerAccount;// string N Buyer User Name

	private String customerIP;// string N customer ip
	private String expirationTime;// string Y Expiration Time
	private String sourceType;// string Y B Terminal source,APP:A ,Browser:B
	private String countryCode;// string Y country code, E.g. KE.Just version='1.2' need this field
	private String channels;// string N Reserved field
	private String paymentMethod;// string N OL OL: online payment, OF: offline payment
	private String email;// string N buyer's email
	private String mobile;// string N buyer's mobile phone number
	private String currency;// string Y This field is currencyCode when version>='1.3' is required

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerAccount() {
		return sellerAccount;
	}

	public void setSellerAccount(String sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerAccount() {
		return buyerAccount;
	}

	public void setBuyerAccount(String buyerAccount) {
		this.buyerAccount = buyerAccount;
	}

	public String getCustomerIP() {
		return customerIP;
	}

	public void setCustomerIP(String customerIP) {
		this.customerIP = customerIP;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
