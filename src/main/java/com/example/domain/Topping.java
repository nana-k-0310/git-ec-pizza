package com.example.domain;

/**
 * トッピングの商品情報に関するドメイン.
 * 
 * @author nanakono
 *
 */
public class Topping {
	/** ID */
	private Integer id;
	/** トッピング名 */
	private String name;
	/** サイズMの値段 */
	private Integer priceM;
	/** サイズLの値段 */
	private Integer priceL;

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

	@Override
	public String toString() {
		return "Topping [id=" + id + ", name=" + name + ", priceM=" + priceM + ", priceL=" + priceL + "]";
	}

}
