package com.example.form;

import java.util.List;

/**
 * ショッピングカート関連フォーム.
 * 
 * @author nanakono
 *
 */
public class ShoppingCartForm {

	/** 商品ID */
	private Integer itemId;
	/** 数量 */
	private Integer quantity;
	/** サイズ */
	private String size;
	/** トッピングリスト */
	private List<Integer> toppingIdList;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public List<Integer> getToppingIdList() {
		return toppingIdList;
	}

	public void setToppingIdList(List<Integer> toppingIdList) {
		this.toppingIdList = toppingIdList;
	}

	@Override
	public String toString() {
		return "ShoppingCartForm [itemId=" + itemId + ", quantity=" + quantity + ", size=" + size + ", toppingIdList="
				+ toppingIdList + "]";
	}
	
	
}
