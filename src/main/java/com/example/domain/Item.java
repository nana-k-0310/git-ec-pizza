package com.example.domain;

import java.util.List;

/**
 * 商品を表すドメインクラス.
 * 
 * @author nanakono
 *
 */
public class Item {
	/** 顧客ID */
	private Integer id;
	/** 商品名 */
	private String name;
	/** 商品説明 */
	private String description;
	/** Mサイズの値段 */
	private Integer priceM;
	/** Lサイズの値段 */
	private Integer priceL;
	/** 画像 */
	private String imagePath;
	/** 削除フラグ */
	private boolean deleted;
	/** トッピングリスト */
	private List<Topping> toppingList;

	
	/** トッピングIDリスト */
	private List<Integer> toppingIdList;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getPriceM() {
		return priceM;
	}


	public void setPriceM(Integer priceM) {
		this.priceM = priceM;
	}


	public Integer getPriceL() {
		return priceL;
	}


	public void setPriceL(Integer priceL) {
		this.priceL = priceL;
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public boolean getDeleted() {
		return deleted;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	public List<Topping> getToppingList() {
		return toppingList;
	}


	public void setToppingList(List<Topping> toppingList) {
		this.toppingList = toppingList;
	}


	public List<Integer> getToppingIdList() {
		return toppingIdList;
	}


	public void setToppingIdList(List<Integer> toppingIdList) {
		this.toppingIdList = toppingIdList;
	}


	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", description=" + description + ", priceM=" + priceM + ", priceL="
				+ priceL + ", imagePath=" + imagePath + ", deleted=" + deleted + ", toppingList=" + toppingList
				+ ", toppingIdList=" + toppingIdList + "]";
	}
	
	
}
