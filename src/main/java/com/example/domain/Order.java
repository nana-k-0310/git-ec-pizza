package com.example.domain;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * 注文情報に関するドメイン.
 * 
 * @author nanakono
 *
 */

public class Order {
	/** ID */
	private Integer id;
	/** ユーザーID */
	private Integer userId;
	/** 注文状態 */
	private Integer status;
	/** 合計金額 */
	private Integer totalPrice;
	/** 注文日 */
	private Date orderDate;
	/** 宛先氏名 */
	private String destinationName;
	/** 宛先Eメール　*/
	private String destinationEmail;
	/** 宛先郵便番号 */
	private String destinationZipcode;
	/** 宛先住所 */
	private String destinationAddress;
	/** 宛先電話番号 */
	private String destinationTel;
	/** 配達時間 */
	private Timestamp deliveryTime;
	/** 支払方法 */
	private Integer paymentMethod;
	/** ユーザー情報 */
	private UserInfo user;
	/** 注文商品リスト */
	private List<OrderItem> orderItemList;
	
	/**
	 * カート内商品の合計金額を取得する.
	 * 
	 * @return　カート内商品の合計金額
	 */
	public int getCalcTotalPrice() {
		int totalPrice = 0;
		for(OrderItem orderItem : getOrderItemList()) {
			totalPrice += orderItem.getSubTotalPrice() * 1.1;
		}
		return totalPrice;
	}
	
	/**
	 * 商品合計の消費税を取得する.
	 * 
	 * @return　商品合計の消費税
	 */
	public int getTax() {
		int taxPrice = 0;
		for(OrderItem orderItem : getOrderItemList()) {
			taxPrice += orderItem.getSubTotalPrice() * 0.1;
		}
		return taxPrice;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
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
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
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
	public Timestamp getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Timestamp deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", status=" + status + ", totalPrice=" + totalPrice
				+ ", orderDate=" + orderDate + ", destinationName=" + destinationName + ", destinationEmail="
				+ destinationEmail + ", destinationZipcode=" + destinationZipcode + ", destinationAddress="
				+ destinationAddress + ", destinationTel=" + destinationTel + ", paymentMethod=" + paymentMethod
				+ ", user=" + user + ", orderItemList=" + orderItemList + "]";
	}
	
	
	
	
}
