package com.example.form;

public class OrderForm {
	/** ○（SQL文取るときに使う）ID */
	private String id;
	/** ○（orderSQLはあり）注文状態 */
	private Integer status;
	/** ○（orderSQLはあり）合計金額 */
	private Integer totalPrice;
	/** ●宛先氏名 */
	private String destinationName;
	/** ●宛先Eメール */
	private String destinationEmail;
	/** ●宛先郵便番号 */
	private String destinationZipcode;
	/**　●宛先住所 */
	private String destinationAddress;
	/** ●宛先電話番号 */
	private String destinationTel;
	
	
	/** ●○（orderSQLはなし）配達日 */
	private String deliveryDate;
	
	
	/** ●配達時間 */
	private String deliveryTime;
	/** ●支払い方法 */
	private Integer paymentMethod;
	
	
	public Integer getIntId() {
		return Integer.parseInt(id);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getDestinationEmail() {
		return destinationEmail;
	}
	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}
	public String getDestinationZipcode() {
		return destinationZipcode;
	}
	public void setDestinationZipcode(String destinationZipcode) {
		this.destinationZipcode = destinationZipcode;
	}
	public String getDestinationAddress() {
		return destinationAddress;
	}
	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	public String getDestinationTel() {
		return destinationTel;
	}
	public void setDestinationTel(String destinationTel) {
		this.destinationTel = destinationTel;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	@Override
	public String toString() {
		return "OrderForm [id=" + id + ", status=" + status + ", totalPrice=" + totalPrice + ", destinationName="
				+ destinationName + ", destinationEmail=" + destinationEmail + ", destinationZipcode="
				+ destinationZipcode + ", destinationAddress=" + destinationAddress + ", destinationTel="
				+ destinationTel + ", deliveryDate=" + deliveryDate + ", deliveryTime=" + deliveryTime
				+ ", paymentMethod=" + paymentMethod + "]";
	}
	
	
	
	
	
	
}
